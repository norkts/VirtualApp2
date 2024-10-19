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
   private static final String LOG_TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITsuKWgaFg19Dlk7KC0MKGIFGgRvNx4qLhhSVg=="));
   private static final boolean DBG = false;
   private static final String TAG_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWgVSFo="));
   private static final String ATTR_FLAGS = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4EP2gwLFo="));
   private static final String ATTR_ICON_PATH = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAg2D2ojSFo="));
   private static final String ATTR_ID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgqVg=="));
   private static final String ATTR_CREATION_TIME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtiEVRF"));
   private static final String ATTR_LAST_LOGGED_IN_TIME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLHiViJDg/KBUcDg=="));
   private static final String ATTR_SERIAL_NO = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKmUVJCRoNzA3Lz0MKA=="));
   private static final String ATTR_NEXT_SERIAL_NO = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4uIGwILCthNAY7KhUYLW8jRStsN1RF"));
   private static final String ATTR_PARTIAL = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+KmwFAjdgEVRF"));
   private static final String ATTR_USER_VERSION = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4uKm8zAiVgN1RF"));
   private static final String TAG_USERS = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28gLFo="));
   private static final String TAG_USER = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo="));
   private static final String USER_INFO_DIR;
   private static final String USER_LIST_FILENAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jHi9hJw02LRdXCA=="));
   private static final String USER_PHOTO_FILENAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhhfD2wFBSZhHlk9"));
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
      this(context, pm, installLock, packagesLock, VEnvironment.getDataDirectory(), new File(VEnvironment.getDataDirectory(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo="))));
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
            File userZeroDir = new File(this.mUsersDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OhhSVg==")));
            userZeroDir.mkdirs();
            this.mBaseUserPath = baseUserPath;
            this.mUserListFile = new File(this.mUsersDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jHi9hJw02LRdXCA==")));
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
               VLog.w(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITsuKWgaFg19Dlk7KC0MKGIFGgRvNx4qLhhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij4uDWowOC9gNDs8IxciKGUzLDdlEQI0PQg2CGIKPD9uDjMpIy0YJ2w3IzU=")) + i + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl9fCGsVEit0AVRF")) + ui.name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAhSVg==")));
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
         VLog.w(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQaLCthMgY2KD1eIH4wGiZqJxocKj4bJGYFNCBlMCMs")) + userId);
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
            VLog.w(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQaLCthMlk7KgcLIH4wGiZqJxocKj4bJGYFNCBlMCMs")) + userId);
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
            VLog.w(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQaLCthMgY5Ki0XIH4wGiZqJxocKj4bJGYFNCBlMCMs")) + userId);
            return;
         }

         this.writeBitmapLocked(info, bitmap);
         this.writeUserLocked(info);
      }

      this.sendUserInfoChangedBroadcast(userId);
   }

   private void sendUserInfoChangedBroadcast(int userId) {
      Intent changedIntent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k7Kj02KG8FLCx1Nx4bKgguKmZTRSRpJzAiKQgpKmcILFRnIgYMLRUmH2YmFlc=")));
      changedIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmYFNCBlNVkhKC4qIGUVNFo=")), userId);
      changedIntent.addFlags(1073741824);
      VActivityManagerService.get().sendBroadcastAsUser(changedIntent, new VUserHandle(userId));
   }

   public Bitmap getUserIcon(int userId) {
      synchronized(this.mPackagesLock) {
         VUserInfo info = (VUserInfo)this.mUsers.get(userId);
         if (info != null && !info.partial) {
            return info.iconPath == null ? null : BitmapFactory.decodeFile(info.iconPath);
         } else {
            VLog.w(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQaLCthMgY5Ki0XIH4wGiZqJxocKj4bJGYFNCBlMCMs")) + userId);
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
               this.createUser(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JS0uM28wMFo=")), 4);
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
            VLog.w(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+MWgbAiZjAQozLwdbMWgVGix+MzwwLC5bKmAjJCl5EQo8Ly1eJHgjSFo=")) + userId);
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
         VLog.w(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShhJDAgLBccDmkJTQJqEQY/LDo6ImAjMyNqASwuLF9XVg==")), e);
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

            VLog.e(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcP2sjHitLEQo1PxguPW4jAShvDjAgKSo6KGMFND8=")));
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
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhcMCWoVJARnAVRF")).equals(user.name)) {
            user.name = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JggqDWUVBlo="));
            this.writeUserLocked(user);
         }

         userVersion = 1;
      }

      if (userVersion < 1) {
         VLog.w(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28nID5iASwpKQdfDn4zSFo=")) + this.mUserVersion + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgqCWgFAS1mVyQvIxc6KG4jBit4ETg6PQguPGEaLCZqHgotOD0cKXgVSFo=")) + 1);
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
      AtomicFile userFile = new AtomicFile(new File(this.mUsersDir, userInfo.id + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1fDWoFSFo="))));

      try {
         fos = userFile.startWrite();
         BufferedOutputStream bos = new BufferedOutputStream(fos);
         XmlSerializer serializer = new FastXmlSerializer();
         serializer.setOutput(bos, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcqPnpTRVo=")));
         serializer.startDocument((String)null, true);
         serializer.setFeature(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8OTCVOJxo3KhgmLW8zOyZlJAouPD0hDU4gFippIFkvLy5bCm8KFj9vMxo/IBdbO3kgBgJpNwY5KV8ID2waMAJmAQpF")), true);
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
            serializer.attribute((String)null, ATTR_PARTIAL, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcMI2gVSFo=")));
         }

         serializer.startTag((String)null, TAG_NAME);
         serializer.text(userInfo.name);
         serializer.endTag((String)null, TAG_NAME);
         serializer.endTag((String)null, TAG_USER);
         serializer.endDocument();
         userFile.finishWrite(fos);
      } catch (Exception var6) {
         Exception ioe = var6;
         VLog.e(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShmJywzLBccDmkJTQVsJyg5PQgYKmIwDSM=")) + userInfo.id + "\n" + ioe);
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
         serializer.setOutput(bos, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcqPnpTRVo=")));
         serializer.startDocument((String)null, true);
         serializer.setFeature(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8OTCVOJxo3KhgmLW8zOyZlJAouPD0hDU4gFippIFkvLy5bCm8KFj9vMxo/IBdbO3kgBgJpNwY5KV8ID2waMAJmAQpF")), true);
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
         VLog.e(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShmJywzLBccDmkJTQVsJyg5PQgEI2EjFlo=")));
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
         AtomicFile userFile = new AtomicFile(new File(this.mUsersDir, Integer.toString(id) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1fDWoFSFo="))));
         fis = userFile.openRead();
         XmlPullParser parser = Xml.newPullParser();
         parser.setInput(fis, (String)null);

         int type;
         while((type = parser.next()) != 2 && type != 1) {
         }

         VUserInfo userInfo;
         if (type != 2) {
            VLog.e(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcP2sjHitLEQo1PxguPW4jAShvDjAgKSo6Vg==")) + id);
            userInfo = null;
            return userInfo;
         } else {
            if (parser.getName().equals(TAG_USER)) {
               int storedId = this.readIntAttribute(parser, ATTR_ID, -1);
               String valueString;
               if (storedId != id) {
                  VLog.e(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28nIC9iVyQwKi0ML34zMCVvVjweLRcqJWNTOD9vHg0pLxg2KGsJIARrEQ40")));
                  valueString = null;
                  return valueString;
               }

               serialNumber = this.readIntAttribute(parser, ATTR_SERIAL_NO, id);
               flags = this.readIntAttribute(parser, ATTR_FLAGS, 0);
               iconPath = parser.getAttributeValue((String)null, ATTR_ICON_PATH);
               creationTime = this.readLongAttribute(parser, ATTR_CREATION_TIME, 0L);
               lastLoggedInTime = this.readLongAttribute(parser, ATTR_LAST_LOGGED_IN_TIME, 0L);
               valueString = parser.getAttributeValue((String)null, ATTR_PARTIAL);
               if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcMI2gVSFo=")).equals(valueString)) {
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

         Intent addedIntent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k7Kj02KG8FLCx1Nx4bKgguKmZTRSRpJzAiKQgpKmcILFRnIgYOLBUMBmYVSFo=")));
         addedIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmYFNCBlNVkhKC4qIGUVNFo=")), userInfo.id);
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
         Intent addedIntent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k7Kj02KG8FLCx1Nx4bKgguKmZTRSRpJzAiKQgpKmcILFRnIgZALAVbGGI2Flc=")));
         addedIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmYFNCBlNVkhKC4qIGUVNFo=")), userHandle);
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
      AtomicFile userFile = new AtomicFile(new File(this.mUsersDir, userHandle + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1fDWoFSFo="))));
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
            VLog.w(LOG_TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28hOCVhNDA9Iz1fLW8VATJ4HigbIz4cKWYgRCNqASwuLF9WJQ==")) + userId);
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
      USER_INFO_DIR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCM=")) + File.separator + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28gLFo="));
   }
}
