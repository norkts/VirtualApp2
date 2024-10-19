package com.lody.virtual.server.pm;

import android.app.IStopUserCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.util.Xml;
import com.kook.librelease.R.string;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.SpecialComponentList;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.AtomicFile;
import com.lody.virtual.helper.utils.FastXmlSerializer;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.server.am.VActivityManagerService;
import com.lody.virtual.server.interfaces.IUserManager;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class VUserManagerService extends IUserManager.Stub {
   private static final String LOG_TAG = "VUserManagerService";
   private static final boolean DBG = false;
   private static final String TAG_NAME = "name";
   private static final String ATTR_FLAGS = "flags";
   private static final String ATTR_ICON_PATH = "icon";
   private static final String ATTR_ID = "id";
   private static final String ATTR_CREATION_TIME = "created";
   private static final String ATTR_LAST_LOGGED_IN_TIME = "lastLoggedIn";
   private static final String ATTR_SERIAL_NO = "serialNumber";
   private static final String ATTR_NEXT_SERIAL_NO = "nextSerialNumber";
   private static final String ATTR_PARTIAL = "partial";
   private static final String ATTR_USER_VERSION = "version";
   private static final String TAG_USERS = "users";
   private static final String TAG_USER = "user";
   private static final String USER_INFO_DIR;
   private static final String USER_LIST_FILENAME = "userlist.xml";
   private static final String USER_PHOTO_FILENAME = "photo.png";
   private static final int MIN_USER_ID = 1;
   private static final int USER_VERSION = 1;
   private static final long EPOCH_PLUS_30_YEARS = 946080000000L;
   private static VUserManagerService sInstance;
   private final Context mContext;
   private final VPackageManagerService mPm;
   private final Object mInstallLock;
   private final Object mPackagesLock;
   private final File mUsersDir;
   private final File mUserListFile;
   private final File mBaseUserPath;
   private SparseArray<VUserInfo> mUsers;
   private HashSet<Integer> mRemovingUserIds;
   private int[] mUserIds;
   private boolean mGuestEnabled;
   private int mNextSerialNumber;
   private int mNextUserId;
   private int mUserVersion;

   VUserManagerService(Context context, VPackageManagerService pm, Object installLock, Object packagesLock) {
      this(context, pm, installLock, packagesLock, VEnvironment.getDataDirectory(), new File(VEnvironment.getDataDirectory(), "user"));
   }

   private VUserManagerService(Context context, VPackageManagerService pm, Object installLock, Object packagesLock, File dataDir, File baseUserPath) {
      this.mUsers = new SparseArray();
      this.mRemovingUserIds = new HashSet();
      this.mNextUserId = 1;
      this.mUserVersion = 0;
      this.mContext = context;
      this.mPm = pm;
      this.mInstallLock = installLock;
      this.mPackagesLock = packagesLock;
      synchronized(this.mInstallLock) {
         synchronized(this.mPackagesLock) {
            this.mUsersDir = new File(dataDir, USER_INFO_DIR);
            this.mUsersDir.mkdirs();
            File userZeroDir = new File(this.mUsersDir, "0");
            userZeroDir.mkdirs();
            this.mBaseUserPath = baseUserPath;
            this.mUserListFile = new File(this.mUsersDir, "userlist.xml");
            this.readUserListLocked();
            ArrayList<VUserInfo> partials = new ArrayList();

            int i;
            VUserInfo ui;
            for(i = 0; i < this.mUsers.size(); ++i) {
               ui = (VUserInfo)this.mUsers.valueAt(i);
               if (ui.partial && i != 0) {
                  partials.add(ui);
               }
            }

            for(i = 0; i < partials.size(); ++i) {
               ui = (VUserInfo)partials.get(i);
               VLog.w("VUserManagerService", "Removing partially created user #" + i + " (name=" + ui.name + ")");
               this.removeUserStateLocked(ui.id);
            }

            sInstance = this;
         }
      }
   }

   public static VUserManagerService get() {
      Class var0 = VUserManagerService.class;
      synchronized(VUserManagerService.class) {
         return sInstance;
      }
   }

   public List<VUserInfo> getUsers(boolean excludeDying) {
      synchronized(this.mPackagesLock) {
         ArrayList<VUserInfo> users = new ArrayList(this.mUsers.size());

         for(int i = 0; i < this.mUsers.size(); ++i) {
            VUserInfo ui = (VUserInfo)this.mUsers.valueAt(i);
            if (!ui.partial && (!excludeDying || !this.mRemovingUserIds.contains(ui.id))) {
               users.add(ui);
            }
         }

         return users;
      }
   }

   public VUserInfo getUserInfo(int userId) {
      synchronized(this.mPackagesLock) {
         return this.getUserInfoLocked(userId);
      }
   }

   private VUserInfo getUserInfoLocked(int userId) {
      VUserInfo ui = (VUserInfo)this.mUsers.get(userId);
      if (ui != null && ui.partial && !this.mRemovingUserIds.contains(userId)) {
         VLog.w(LOG_TAG, "getUserInfo: unknown user #" + userId);
         return null;
      } else {
         return ui;
      }
   }

   public boolean exists(int userId) {
      synchronized(this.mPackagesLock) {
         return ArrayUtils.contains(this.mUserIds, userId);
      }
   }

   public void setUserName(int userId, String name) {
      boolean changed = false;
      synchronized(this.mPackagesLock) {
         VUserInfo info = (VUserInfo)this.mUsers.get(userId);
         if (info == null || info.partial) {
            VLog.w(LOG_TAG, "setUserName: unknown user #" + userId);
            return;
         }

         if (name != null && !name.equals(info.name)) {
            info.name = name;
            this.writeUserLocked(info);
            changed = true;
         }
      }

      if (changed) {
         this.sendUserInfoChangedBroadcast(userId);
      }

   }

   public void setUserIcon(int userId, Bitmap bitmap) {
      synchronized(this.mPackagesLock) {
         VUserInfo info = (VUserInfo)this.mUsers.get(userId);
         if (info == null || info.partial) {
            VLog.w(LOG_TAG, "setUserIcon: unknown user #" + userId);
            return;
         }

         this.writeBitmapLocked(info, bitmap);
         this.writeUserLocked(info);
      }

      this.sendUserInfoChangedBroadcast(userId);
   }

   private void sendUserInfoChangedBroadcast(int userId) {
      Intent changedIntent = new Intent("virtual.android.intent.action.USER_CHANGED");
      changedIntent.putExtra("android.intent.extra.user_handle", userId);
      changedIntent.addFlags(1073741824);
      VActivityManagerService.get().sendBroadcastAsUser(changedIntent, new VUserHandle(userId));
   }

   public Bitmap getUserIcon(int userId) {
      synchronized(this.mPackagesLock) {
         VUserInfo info = (VUserInfo)this.mUsers.get(userId);
         if (info != null && !info.partial) {
            return info.iconPath == null ? null : BitmapFactory.decodeFile(info.iconPath);
         } else {
            VLog.w(LOG_TAG, "getUserIcon: unknown user #" + userId);
            return null;
         }
      }
   }

   public boolean isGuestEnabled() {
      synchronized(this.mPackagesLock) {
         return this.mGuestEnabled;
      }
   }

   public void setGuestEnabled(boolean enable) {
      synchronized(this.mPackagesLock) {
         if (this.mGuestEnabled != enable) {
            this.mGuestEnabled = enable;

            for(int i = 0; i < this.mUsers.size(); ++i) {
               VUserInfo user = (VUserInfo)this.mUsers.valueAt(i);
               if (!user.partial && user.isGuest()) {
                  if (!enable) {
                     this.removeUser(user.id);
                  }

                  return;
               }
            }

            if (enable) {
               this.createUser("Guest", 4);
            }
         }

      }
   }

   public void wipeUser(int userHandle) {
   }

   public void makeInitialized(int userId) {
      synchronized(this.mPackagesLock) {
         VUserInfo info = (VUserInfo)this.mUsers.get(userId);
         if (info == null || info.partial) {
            VLog.w(LOG_TAG, "makeInitialized: unknown user #" + userId);
         }

         if ((info.flags & 16) == 0) {
            info.flags |= 16;
            this.writeUserLocked(info);
         }

      }
   }

   private boolean isUserLimitReachedLocked() {
      int nUsers = this.mUsers.size();
      return nUsers >= VUserManager.getMaxSupportedUsers();
   }

   private void writeBitmapLocked(VUserInfo info, Bitmap bitmap) {
      try {
         File dir = new File(this.mUsersDir, Integer.toString(info.id));
         File file = new File(dir, USER_PHOTO_FILENAME);
         if (!dir.exists()) {
            dir.mkdir();
         }

         FileOutputStream os;
         if (bitmap.compress(CompressFormat.PNG, 100, os = new FileOutputStream(file))) {
            info.iconPath = file.getAbsolutePath();
         }

         try {
            os.close();
         } catch (IOException var7) {
         }
      } catch (FileNotFoundException var8) {
         FileNotFoundException e = var8;
         VLog.w(LOG_TAG, "Error setting photo for user ", e);
      }

   }

   public int[] getUserIds() {
      synchronized(this.mPackagesLock) {
         return this.mUserIds;
      }
   }

   int[] getUserIdsLPr() {
      return this.mUserIds;
   }

   private void readUserList() {
      synchronized(this.mPackagesLock) {
         this.readUserListLocked();
      }
   }

   private void readUserListLocked() {
      this.mGuestEnabled = false;
      if (!this.mUserListFile.exists()) {
         this.fallbackToSingleUserLocked();
      } else {
         FileInputStream fis = null;
         AtomicFile userListFile = new AtomicFile(this.mUserListFile);

         try {
            fis = userListFile.openRead();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, (String)null);

            int type;
            while((type = parser.next()) != 2 && type != 1) {
            }

            if (type == 2) {
               this.mNextSerialNumber = -1;
               String id;
               if (parser.getName().equals(TAG_USERS)) {
                  id = parser.getAttributeValue((String)null, ATTR_NEXT_SERIAL_NO);
                  if (id != null) {
                     this.mNextSerialNumber = Integer.parseInt(id);
                  }

                  String versionNumber = parser.getAttributeValue((String)null, ATTR_USER_VERSION);
                  if (versionNumber != null) {
                     this.mUserVersion = Integer.parseInt(versionNumber);
                  }
               }

               while(true) {
                  VUserInfo user;
                  do {
                     do {
                        do {
                           do {
                              if ((type = parser.next()) == 1) {
                                 this.updateUserIdsLocked();
                                 this.upgradeIfNecessary();
                                 return;
                              }
                           } while(type != 2);
                        } while(!parser.getName().equals(TAG_USER));

                        id = parser.getAttributeValue((String)null, ATTR_ID);
                        user = this.readUser(Integer.parseInt(id));
                     } while(user == null);

                     this.mUsers.put(user.id, user);
                     if (user.isGuest()) {
                        this.mGuestEnabled = true;
                     }
                  } while(this.mNextSerialNumber >= 0 && this.mNextSerialNumber > user.id);

                  this.mNextSerialNumber = user.id + 1;
               }
            }

            VLog.e(LOG_TAG, "Unable to read user list");
            this.fallbackToSingleUserLocked();
         } catch (IOException var18) {
            this.fallbackToSingleUserLocked();
            return;
         } catch (XmlPullParserException var19) {
            this.fallbackToSingleUserLocked();
            return;
         } finally {
            if (fis != null) {
               try {
                  fis.close();
               } catch (IOException var17) {
                  IOException e = var17;
                  e.printStackTrace();
               }
            }

         }

      }
   }

   private void upgradeIfNecessary() {
      int userVersion = this.mUserVersion;
      if (userVersion < 1) {
         VUserInfo user = (VUserInfo)this.mUsers.get(0);
         if ("Primary".equals(user.name)) {
            user.name = "Admin";
            this.writeUserLocked(user);
         }

         userVersion = 1;
      }

      if (userVersion < 1) {
         VLog.w(LOG_TAG, "User version " + this.mUserVersion + " didn\'t upgrade as expected to " + 1);
      } else {
         this.mUserVersion = userVersion;
         this.writeUserListLocked();
      }

   }

   private void fallbackToSingleUserLocked() {
      VUserInfo primary = new VUserInfo(0, this.mContext.getResources().getString(string.owner_name), (String)null, 19);
      this.mUsers.put(0, primary);
      this.mNextSerialNumber = 1;
      this.updateUserIdsLocked();
      this.writeUserListLocked();
      this.writeUserLocked(primary);
   }

   private void writeUserLocked(VUserInfo userInfo) {
      FileOutputStream fos = null;
      AtomicFile userFile = new AtomicFile(new File(this.mUsersDir, userInfo.id + ".xml"));

      try {
         fos = userFile.startWrite();
         BufferedOutputStream bos = new BufferedOutputStream(fos);
         XmlSerializer serializer = new FastXmlSerializer();
         serializer.setOutput(bos, "utf-8");
         serializer.startDocument((String)null, true);
         serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
         serializer.startTag((String)null, TAG_USER);
         serializer.attribute((String)null, ATTR_ID, Integer.toString(userInfo.id));
         serializer.attribute((String)null, ATTR_SERIAL_NO, Integer.toString(userInfo.serialNumber));
         serializer.attribute((String)null, ATTR_FLAGS, Integer.toString(userInfo.flags));
         serializer.attribute((String)null, ATTR_CREATION_TIME, Long.toString(userInfo.creationTime));
         serializer.attribute((String)null, ATTR_LAST_LOGGED_IN_TIME, Long.toString(userInfo.lastLoggedInTime));
         if (userInfo.iconPath != null) {
            serializer.attribute((String)null, ATTR_ICON_PATH, userInfo.iconPath);
         }

         if (userInfo.partial) {
            serializer.attribute((String)null, ATTR_PARTIAL, "true");
         }

         serializer.startTag((String)null, TAG_NAME);
         serializer.text(userInfo.name);
         serializer.endTag((String)null, TAG_NAME);
         serializer.endTag((String)null, TAG_USER);
         serializer.endDocument();
         userFile.finishWrite(fos);
      } catch (Exception var6) {
         Exception ioe = var6;
         VLog.e(LOG_TAG, "Error writing user info " + userInfo.id + "\n" + ioe);
         userFile.failWrite(fos);
      }

   }

   private void writeUserListLocked() {
      FileOutputStream fos = null;
      AtomicFile userListFile = new AtomicFile(this.mUserListFile);

      try {
         fos = userListFile.startWrite();
         BufferedOutputStream bos = new BufferedOutputStream(fos);
         XmlSerializer serializer = new FastXmlSerializer();
         serializer.setOutput(bos, "utf-8");
         serializer.startDocument((String)null, true);
         serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
         serializer.startTag((String)null, TAG_USERS);
         serializer.attribute((String)null, ATTR_NEXT_SERIAL_NO, Integer.toString(this.mNextSerialNumber));
         serializer.attribute((String)null, ATTR_USER_VERSION, Integer.toString(this.mUserVersion));

         for(int i = 0; i < this.mUsers.size(); ++i) {
            VUserInfo user = (VUserInfo)this.mUsers.valueAt(i);
            serializer.startTag((String)null, TAG_USER);
            serializer.attribute((String)null, ATTR_ID, Integer.toString(user.id));
            serializer.endTag((String)null, TAG_USER);
         }

         serializer.endTag((String)null, TAG_USERS);
         serializer.endDocument();
         userListFile.finishWrite(fos);
      } catch (Exception var7) {
         userListFile.failWrite(fos);
         VLog.e(LOG_TAG, "Error writing user list");
      }

   }

   private VUserInfo readUser(int id) {
      int flags = 0;
      int serialNumber = id;
      String name = null;
      String iconPath = null;
      long creationTime = 0L;
      long lastLoggedInTime = 0L;
      boolean partial = false;
      FileInputStream fis = null;

      try {
         AtomicFile userFile = new AtomicFile(new File(this.mUsersDir, Integer.toString(id) + ".xml"));
         fis = userFile.openRead();
         XmlPullParser parser = Xml.newPullParser();
         parser.setInput(fis, (String)null);

         int type;
         while((type = parser.next()) != 2 && type != 1) {
         }

         VUserInfo userInfo;
         if (type != 2) {
            VLog.e(LOG_TAG, "Unable to read user " + id);
            userInfo = null;
            return userInfo;
         } else {
            if (parser.getName().equals(TAG_USER)) {
               int storedId = this.readIntAttribute(parser, ATTR_ID, -1);
               String valueString;
               if (storedId != id) {
                  VLog.e(LOG_TAG, "User id does not match the file name");
                  valueString = null;
                  return null;
               }

               serialNumber = this.readIntAttribute(parser, ATTR_SERIAL_NO, id);
               flags = this.readIntAttribute(parser, ATTR_FLAGS, 0);
               iconPath = parser.getAttributeValue((String)null, ATTR_ICON_PATH);
               creationTime = this.readLongAttribute(parser, ATTR_CREATION_TIME, 0L);
               lastLoggedInTime = this.readLongAttribute(parser, ATTR_LAST_LOGGED_IN_TIME, 0L);
               valueString = parser.getAttributeValue((String)null, ATTR_PARTIAL);
               if ("true".equals(valueString)) {
                  partial = true;
               }

               while(true) {
                  if ((type = parser.next()) == 2 || type == 1) {
                     if (type == 2 && parser.getName().equals(TAG_NAME)) {
                        type = parser.next();
                        if (type == 4) {
                           name = parser.getText();
                        }
                     }
                     break;
                  }
               }
            }

            userInfo = new VUserInfo(id, name, iconPath, flags);
            userInfo.serialNumber = serialNumber;
            userInfo.creationTime = creationTime;
            userInfo.lastLoggedInTime = lastLoggedInTime;
            userInfo.partial = partial;
            VUserInfo var34 = userInfo;
            return var34;
         }
      } catch (IOException var30) {
         return null;
      } catch (XmlPullParserException var31) {
         return null;
      } finally {
         if (fis != null) {
            try {
               fis.close();
            } catch (IOException var29) {
            }
         }

      }
   }

   private int readIntAttribute(XmlPullParser parser, String attr, int defaultValue) {
      String valueString = parser.getAttributeValue((String)null, attr);
      if (valueString == null) {
         return defaultValue;
      } else {
         try {
            return Integer.parseInt(valueString);
         } catch (NumberFormatException var6) {
            return defaultValue;
         }
      }
   }

   private long readLongAttribute(XmlPullParser parser, String attr, long defaultValue) {
      String valueString = parser.getAttributeValue((String)null, attr);
      if (valueString == null) {
         return defaultValue;
      } else {
         try {
            return Long.parseLong(valueString);
         } catch (NumberFormatException var7) {
            return defaultValue;
         }
      }
   }

   public VUserInfo createUser(String name, int flags) {
      long ident = Binder.clearCallingIdentity();

      final VUserInfo userInfo;
      try {
         synchronized(this.mInstallLock) {
            synchronized(this.mPackagesLock) {
               if (this.isUserLimitReachedLocked()) {
                  Object var21 = null;
                  return (VUserInfo)var21;
               }

               int userId = this.getNextAvailableIdLocked();
               userInfo = new VUserInfo(userId, name, (String)null, flags);
               File userPath = new File(this.mBaseUserPath, Integer.toString(userId));
               userInfo.serialNumber = this.mNextSerialNumber++;
               long now = System.currentTimeMillis();
               userInfo.creationTime = now > 946080000000L ? now : 0L;
               userInfo.partial = true;
               VAppManagerService.get().onUserCreated(userInfo);
               this.mUsers.put(userId, userInfo);
               this.writeUserListLocked();
               this.writeUserLocked(userInfo);
               this.mPm.createNewUser(userId, userPath);
               userInfo.partial = false;
               this.writeUserLocked(userInfo);
               this.updateUserIdsLocked();
            }
         }

         Intent addedIntent = new Intent("virtual.android.intent.action.USER_ADDED");
         addedIntent.putExtra("android.intent.extra.user_handle", userInfo.id);
         VActivityManagerService.get().sendBroadcastAsUser(addedIntent, VUserHandle.ALL, (String)null);
      } finally {
         Binder.restoreCallingIdentity(ident);
      }

      (new Thread(new Runnable() {
         public void run() {
            Iterator var1 = SpecialComponentList.getPreInstallPackages().iterator();

            while(var1.hasNext()) {
               String preInstallPkg = (String)var1.next();
               if (userInfo.id != 0 && !VAppManagerService.get().isAppInstalledAsUser(userInfo.id, preInstallPkg)) {
                  VAppManagerService.get().installPackageAsUser(userInfo.id, preInstallPkg);
               }
            }

         }
      })).start();
      return userInfo;
   }

   public boolean removeUser(int userHandle) {
      synchronized(this.mPackagesLock) {
         VUserInfo user = (VUserInfo)this.mUsers.get(userHandle);
         if (userHandle == 0 || user == null) {
            return false;
         }

         this.mRemovingUserIds.add(userHandle);
         user.partial = true;
         this.writeUserLocked(user);
      }

      int res = VActivityManagerService.get().stopUser(userHandle, new IStopUserCallback.Stub() {
         public void userStopped(int userId) {
            VUserManagerService.this.finishRemoveUser(userId);
         }

         public void userStopAborted(int userId) {
         }
      });
      return res == 0;
   }

   void finishRemoveUser(final int userHandle) {
      long identity = Binder.clearCallingIdentity();

      try {
         Intent addedIntent = new Intent("virtual.android.intent.action.USER_REMOVED");
         addedIntent.putExtra("android.intent.extra.user_handle", userHandle);
         VActivityManagerService.get().sendOrderedBroadcastAsUser(addedIntent, VUserHandle.ALL, (String)null, new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
               (new Thread() {
                  public void run() {
                     synchronized(VUserManagerService.this.mInstallLock) {
                        synchronized(VUserManagerService.this.mPackagesLock) {
                           VUserManagerService.this.removeUserStateLocked(userHandle);
                        }

                     }
                  }
               }).start();
            }
         }, (Handler)null, -1, (String)null, (Bundle)null);
      } finally {
         Binder.restoreCallingIdentity(identity);
      }

   }

   private void removeUserStateLocked(int userHandle) {
      this.mPm.cleanUpUser(userHandle);
      this.mUsers.remove(userHandle);
      this.mRemovingUserIds.remove(userHandle);
      AtomicFile userFile = new AtomicFile(new File(this.mUsersDir, userHandle + ".xml"));
      userFile.delete();
      this.writeUserListLocked();
      this.updateUserIdsLocked();
      this.removeDirectoryRecursive(VEnvironment.getDataUserDirectory(userHandle));
   }

   private void removeDirectoryRecursive(File parent) {
      if (parent.isDirectory()) {
         String[] files = parent.list();
         String[] var3 = files;
         int var4 = files.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String filename = var3[var5];
            File child = new File(parent, filename);
            this.removeDirectoryRecursive(child);
         }
      }

      parent.delete();
   }

   public int getUserSerialNumber(int userHandle) {
      synchronized(this.mPackagesLock) {
         return !this.exists(userHandle) ? -1 : this.getUserInfoLocked(userHandle).serialNumber;
      }
   }

   public int getUserHandle(int userSerialNumber) {
      synchronized(this.mPackagesLock) {
         int[] var3 = this.mUserIds;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int userId = var3[var5];
            if (this.getUserInfoLocked(userId).serialNumber == userSerialNumber) {
               return userId;
            }
         }

         return -1;
      }
   }

   private void updateUserIdsLocked() {
      int num = 0;

      for(int i = 0; i < this.mUsers.size(); ++i) {
         if (!((VUserInfo)this.mUsers.valueAt(i)).partial) {
            ++num;
         }
      }

      int[] newUsers = new int[num];
      int n = 0;

      for(int i = 0; i < this.mUsers.size(); ++i) {
         if (!((VUserInfo)this.mUsers.valueAt(i)).partial) {
            newUsers[n++] = this.mUsers.keyAt(i);
         }
      }

      this.mUserIds = newUsers;
   }

   public void userForeground(int userId) {
      synchronized(this.mPackagesLock) {
         VUserInfo user = (VUserInfo)this.mUsers.get(userId);
         long now = System.currentTimeMillis();
         if (user != null && !user.partial) {
            if (now > 946080000000L) {
               user.lastLoggedInTime = now;
               this.writeUserLocked(user);
            }

         } else {
            VLog.w(LOG_TAG, "userForeground: unknown user #" + userId);
         }
      }
   }

   private int getNextAvailableIdLocked() {
      synchronized(this.mPackagesLock) {
         int i;
         for(i = this.mNextUserId; i < Integer.MAX_VALUE && (this.mUsers.indexOfKey(i) >= 0 || this.mRemovingUserIds.contains(i)); ++i) {
         }

         this.mNextUserId = i + 1;
         return i;
      }
   }

   static {
      USER_INFO_DIR = "system" + File.separator + "users";
   }
}
