package com.lody.virtual.os;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.LocalProxyUtils;
import com.lody.virtual.client.ipc.ServiceManagerNative;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.server.interfaces.IUserManager;
import java.util.List;

public class VUserManager {
   private static String TAG = "VUserManager";
   private IUserManager mService;
   private static final VUserManager sInstance = new VUserManager();

   private Object getRemoteInterface() {
      return IUserManager.Stub.asInterface(ServiceManagerNative.getService("user"));
   }

   public IUserManager getService() {
      if (!IInterfaceUtils.isAlive(this.mService) || VirtualCore.get().isExtHelperProcess()) {
         Class var1 = VUserManager.class;
         synchronized(VUserManager.class) {
            Object remote = this.getRemoteInterface();
            this.mService = (IUserManager)LocalProxyUtils.genProxy(IUserManager.class, remote);
         }
      }

      return this.mService;
   }

   public static synchronized VUserManager get() {
      return sInstance;
   }

   public static boolean supportsMultipleUsers() {
      return getMaxSupportedUsers() > 1;
   }

   public int getUserHandle() {
      return VUserHandle.myUserId();
   }

   public String getUserName() {
      try {
         return this.getService().getUserInfo(this.getUserHandle()).name;
      } catch (RemoteException var2) {
         RemoteException re = var2;
         Log.w(TAG, "Could not get user name", re);
         return "";
      }
   }

   public boolean isUserAGoat() {
      return false;
   }

   public VUserInfo getUserInfo(int handle) {
      try {
         return this.getService().getUserInfo(handle);
      } catch (RemoteException var3) {
         RemoteException re = var3;
         Log.w(TAG, "Could not get user info", re);
         return null;
      }
   }

   public long getSerialNumberForUser(VUserHandle user) {
      return (long)this.getUserSerialNumber(user.getIdentifier());
   }

   public VUserHandle getUserForSerialNumber(long serialNumber) {
      int ident = this.getUserHandle((int)serialNumber);
      return ident >= 0 ? new VUserHandle(ident) : null;
   }

   public VUserInfo createUser(String name, int flags) {
      try {
         return this.getService().createUser(name, flags);
      } catch (RemoteException var4) {
         RemoteException re = var4;
         Log.w(TAG, "Could not create a user", re);
         return null;
      }
   }

   public int getUserCount() {
      List<VUserInfo> users = this.getUsers();
      return users != null ? users.size() : 1;
   }

   public List<VUserInfo> getUsers() {
      try {
         return this.getService().getUsers(false);
      } catch (RemoteException var2) {
         RemoteException re = var2;
         re.printStackTrace();
         Log.w(TAG, "Could not get user list", re);
         return null;
      }
   }

   public List<VUserInfo> getUsers(boolean excludeDying) {
      try {
         return this.getService().getUsers(excludeDying);
      } catch (RemoteException var3) {
         RemoteException re = var3;
         Log.w(TAG, "Could not get user list", re);
         return null;
      }
   }

   public boolean removeUser(int handle) {
      try {
         return this.getService().removeUser(handle);
      } catch (RemoteException var3) {
         RemoteException re = var3;
         Log.w(TAG, "Could not remove user ", re);
         return false;
      }
   }

   public void setUserName(int handle, String name) {
      try {
         this.getService().setUserName(handle, name);
      } catch (RemoteException var4) {
         RemoteException re = var4;
         Log.w(TAG, "Could not set the user name ", re);
      }

   }

   public void setUserIcon(int handle, Bitmap icon) {
      try {
         this.getService().setUserIcon(handle, icon);
      } catch (RemoteException var4) {
         RemoteException re = var4;
         Log.w(TAG, "Could not set the user icon ", re);
      }

   }

   public Bitmap getUserIcon(int handle) {
      try {
         return this.getService().getUserIcon(handle);
      } catch (RemoteException var3) {
         RemoteException re = var3;
         Log.w(TAG, "Could not get the user icon ", re);
         return null;
      }
   }

   public void setGuestEnabled(boolean enable) {
      try {
         this.getService().setGuestEnabled(enable);
      } catch (RemoteException var3) {
         Log.w(TAG, "Could not change guest account availability to " + enable);
      }

   }

   public boolean isGuestEnabled() {
      try {
         return this.getService().isGuestEnabled();
      } catch (RemoteException var2) {
         Log.w(TAG, "Could not retrieve guest enabled state");
         return false;
      }
   }

   public void wipeUser(int handle) {
      try {
         this.getService().wipeUser(handle);
      } catch (RemoteException var3) {
         Log.w(TAG, "Could not wipe user " + handle);
      }

   }

   public static int getMaxSupportedUsers() {
      return Integer.MAX_VALUE;
   }

   public int getUserSerialNumber(int handle) {
      try {
         return this.getService().getUserSerialNumber(handle);
      } catch (RemoteException var3) {
         Log.w(TAG, "Could not get serial number for user " + handle);
         return -1;
      }
   }

   public int getUserHandle(int userSerialNumber) {
      try {
         return this.getService().getUserHandle(userSerialNumber);
      } catch (RemoteException var3) {
         Log.w(TAG, "Could not get VUserHandle for user " + userSerialNumber);
         return -1;
      }
   }
}
