package com.lody.virtual.server.interfaces;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.ReceiverInfo;
import com.lody.virtual.remote.VParceledListSlice;
import java.util.ArrayList;
import java.util.List;

public interface IPackageManager extends IInterface {
   int getPackageUid(String var1, int var2) throws RemoteException;

   String[] getPackagesForUid(int var1) throws RemoteException;

   List<String> getSharedLibraries(String var1) throws RemoteException;

   int checkPermission(boolean var1, String var2, String var3, int var4) throws RemoteException;

   PackageInfo getPackageInfo(String var1, int var2, int var3) throws RemoteException;

   ActivityInfo getActivityInfo(ComponentName var1, int var2, int var3) throws RemoteException;

   boolean activitySupportsIntent(ComponentName var1, Intent var2, String var3) throws RemoteException;

   ActivityInfo getReceiverInfo(ComponentName var1, int var2, int var3) throws RemoteException;

   ServiceInfo getServiceInfo(ComponentName var1, int var2, int var3) throws RemoteException;

   ProviderInfo getProviderInfo(ComponentName var1, int var2, int var3) throws RemoteException;

   ResolveInfo resolveIntent(Intent var1, String var2, int var3, int var4) throws RemoteException;

   List<ResolveInfo> queryIntentActivities(Intent var1, String var2, int var3, int var4) throws RemoteException;

   List<ResolveInfo> queryIntentReceivers(Intent var1, String var2, int var3, int var4) throws RemoteException;

   ResolveInfo resolveService(Intent var1, String var2, int var3, int var4) throws RemoteException;

   List<ResolveInfo> queryIntentServices(Intent var1, String var2, int var3, int var4) throws RemoteException;

   List<ResolveInfo> queryIntentContentProviders(Intent var1, String var2, int var3, int var4) throws RemoteException;

   VParceledListSlice getInstalledPackages(int var1, int var2) throws RemoteException;

   VParceledListSlice getInstalledApplications(int var1, int var2) throws RemoteException;

   List<ReceiverInfo> getReceiverInfos(String var1, String var2, int var3) throws RemoteException;

   PermissionInfo getPermissionInfo(String var1, int var2) throws RemoteException;

   List<PermissionInfo> queryPermissionsByGroup(String var1, int var2) throws RemoteException;

   PermissionGroupInfo getPermissionGroupInfo(String var1, int var2) throws RemoteException;

   List<PermissionGroupInfo> getAllPermissionGroups(int var1) throws RemoteException;

   ProviderInfo resolveContentProvider(String var1, int var2, int var3) throws RemoteException;

   ApplicationInfo getApplicationInfo(String var1, int var2, int var3) throws RemoteException;

   VParceledListSlice queryContentProviders(String var1, int var2, int var3) throws RemoteException;

   List<String> querySharedPackages(String var1) throws RemoteException;

   String getNameForUid(int var1) throws RemoteException;

   IBinder getPackageInstaller() throws RemoteException;

   int checkSignatures(String var1, String var2) throws RemoteException;

   String[] getDangerousPermissions(String var1) throws RemoteException;

   void setComponentEnabledSetting(ComponentName var1, int var2, int var3, int var4) throws RemoteException;

   int getComponentEnabledSetting(ComponentName var1, int var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IPackageManager {
      private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IPackageManager";
      static final int TRANSACTION_getPackageUid = 1;
      static final int TRANSACTION_getPackagesForUid = 2;
      static final int TRANSACTION_getSharedLibraries = 3;
      static final int TRANSACTION_checkPermission = 4;
      static final int TRANSACTION_getPackageInfo = 5;
      static final int TRANSACTION_getActivityInfo = 6;
      static final int TRANSACTION_activitySupportsIntent = 7;
      static final int TRANSACTION_getReceiverInfo = 8;
      static final int TRANSACTION_getServiceInfo = 9;
      static final int TRANSACTION_getProviderInfo = 10;
      static final int TRANSACTION_resolveIntent = 11;
      static final int TRANSACTION_queryIntentActivities = 12;
      static final int TRANSACTION_queryIntentReceivers = 13;
      static final int TRANSACTION_resolveService = 14;
      static final int TRANSACTION_queryIntentServices = 15;
      static final int TRANSACTION_queryIntentContentProviders = 16;
      static final int TRANSACTION_getInstalledPackages = 17;
      static final int TRANSACTION_getInstalledApplications = 18;
      static final int TRANSACTION_getReceiverInfos = 19;
      static final int TRANSACTION_getPermissionInfo = 20;
      static final int TRANSACTION_queryPermissionsByGroup = 21;
      static final int TRANSACTION_getPermissionGroupInfo = 22;
      static final int TRANSACTION_getAllPermissionGroups = 23;
      static final int TRANSACTION_resolveContentProvider = 24;
      static final int TRANSACTION_getApplicationInfo = 25;
      static final int TRANSACTION_queryContentProviders = 26;
      static final int TRANSACTION_querySharedPackages = 27;
      static final int TRANSACTION_getNameForUid = 28;
      static final int TRANSACTION_getPackageInstaller = 29;
      static final int TRANSACTION_checkSignatures = 30;
      static final int TRANSACTION_getDangerousPermissions = 31;
      static final int TRANSACTION_setComponentEnabledSetting = 32;
      static final int TRANSACTION_getComponentEnabledSetting = 33;

      public Stub() {
         this.attachInterface(this, "com.lody.virtual.server.interfaces.IPackageManager");
      }

      public static IPackageManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPackageManager)(iin != null && iin instanceof IPackageManager ? (IPackageManager)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         ComponentName cn_arg0;
         int i_arg0;
         int _result;
         int i_arg1;
         List list_result;
         String[] sarr_result;
         String s_arg0;
         String s1_arg0;
         List list_result1;
         int int_arg0;
         ProviderInfo pi_result;
         ResolveInfo ri_result;
         VParceledListSlice vls_result;
         Intent intent_arg0;
         ActivityInfo ai_result;
         String _arg2;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               s1_arg0 = data.readString();
               i_arg1 = data.readInt();
               _result = this.getPackageUid(s1_arg0, i_arg1);
               reply.writeNoException();
               reply.writeInt(_result);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               i_arg0 = data.readInt();
               sarr_result = this.getPackagesForUid(i_arg0);
               reply.writeNoException();
               reply.writeStringArray(sarr_result);
               return true;
            case 3:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               list_result = this.getSharedLibraries(s_arg0);
               reply.writeNoException();
               reply.writeStringList(list_result);
               return true;
            case 4:
               data.enforceInterface(descriptor);
               boolean _arg0 = 0 != data.readInt();
               String s_arg1 = data.readString();
               _arg2 = data.readString();
               int int_arg3 = data.readInt();
               int int_result = this.checkPermission(_arg0, s_arg1, _arg2, int_arg3);
               reply.writeNoException();
               reply.writeInt(int_result);
               return true;
            case 5:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               int _arg1 = data.readInt();
               int_arg0 = data.readInt();
               PackageInfo pki_result = this.getPackageInfo(s_arg0, _arg1, int_arg0);
               reply.writeNoException();
               if (pki_result != null) {
                  reply.writeInt(1);
                  pki_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 6:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  cn_arg0 = (ComponentName)ComponentName.CREATOR.createFromParcel(data);
               } else {
                  cn_arg0 = null;
               }

               _arg1 = data.readInt();
               _result = data.readInt();
               ai_result = this.getActivityInfo(cn_arg0, _arg1, _result);
               reply.writeNoException();
               if (ai_result != null) {
                  reply.writeInt(1);
                  ai_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 7:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  cn_arg0 = (ComponentName)ComponentName.CREATOR.createFromParcel(data);
               } else {
                  cn_arg0 = null;
               }

               Intent intent_arg1;
               if (0 != data.readInt()) {
                  intent_arg1 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  intent_arg1 = null;
               }

               _arg2 = data.readString();
               boolean b_result = this.activitySupportsIntent(cn_arg0, intent_arg1, _arg2);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 8:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  cn_arg0 = (ComponentName)ComponentName.CREATOR.createFromParcel(data);
               } else {
                  cn_arg0 = null;
               }

               _arg1 = data.readInt();
               _result = data.readInt();
               ai_result = this.getReceiverInfo(cn_arg0, _arg1, _result);
               reply.writeNoException();
               if (ai_result != null) {
                  reply.writeInt(1);
                  ai_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 9:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  cn_arg0 = (ComponentName)ComponentName.CREATOR.createFromParcel(data);
               } else {
                  cn_arg0 = null;
               }

               _arg1 = data.readInt();
               _result = data.readInt();
               ServiceInfo si_result = this.getServiceInfo(cn_arg0, _arg1, _result);
               reply.writeNoException();
               if (si_result != null) {
                  reply.writeInt(1);
                  si_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 10:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  cn_arg0 = (ComponentName)ComponentName.CREATOR.createFromParcel(data);
               } else {
                  cn_arg0 = null;
               }

               _arg1 = data.readInt();
               _result = data.readInt();
               pi_result = this.getProviderInfo(cn_arg0, _arg1, _result);
               reply.writeNoException();
               if (pi_result != null) {
                  reply.writeInt(1);
                  pi_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 11:
               data.enforceInterface(descriptor);
               Intent inent_arg0;
               if (0 != data.readInt()) {
                  inent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  inent_arg0 = null;
               }

               s_arg1 = data.readString();
               int_result = data.readInt();
               int _arg3 = data.readInt();
               ri_result = this.resolveIntent(inent_arg0, s_arg1, int_result, _arg3);
               reply.writeNoException();
               if (ri_result != null) {
                  reply.writeInt(1);
                  ri_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 12:
               data.enforceInterface(descriptor);
               ;

               if (0 != data.readInt()) {
                  inent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  inent_arg0 = null;
               }

               s_arg1 = data.readString();
               _result = data.readInt();
               _arg3 = data.readInt();
               list_result = this.queryIntentActivities(inent_arg0, s_arg1, _result, _arg3);
               reply.writeNoException();
               reply.writeTypedList(list_result);
               return true;
            case 13:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  inent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  inent_arg0 = null;
               }

               s_arg1 = data.readString();
               _result = data.readInt();
               _arg3 = data.readInt();
               list_result = this.queryIntentReceivers(inent_arg0, s_arg1, _result, _arg3);
               reply.writeNoException();
               reply.writeTypedList(list_result);
               return true;
            case 14:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  inent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  inent_arg0 = null;
               }

               s_arg1 = data.readString();
               _result = data.readInt();
               _arg3 = data.readInt();
               ri_result = this.resolveService(inent_arg0, s_arg1, _result, _arg3);
               reply.writeNoException();
               if (ri_result != null) {
                  reply.writeInt(1);
                  ri_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 15:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  inent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  inent_arg0 = null;
               }

               s_arg1 = data.readString();
               _result = data.readInt();
               _arg3 = data.readInt();
               list_result = this.queryIntentServices(inent_arg0, s_arg1, _result, _arg3);
               reply.writeNoException();
               reply.writeTypedList(list_result);
               return true;
            case 16:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  inent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  inent_arg0 = null;
               }

               s_arg1 = data.readString();
               _result = data.readInt();
               _arg3 = data.readInt();
               list_result = this.queryIntentContentProviders(inent_arg0, s_arg1, _result, _arg3);
               reply.writeNoException();
               reply.writeTypedList(list_result);
               return true;
            case 17:
               data.enforceInterface(descriptor);
               int_arg0 = data.readInt();
               _arg1 = data.readInt();
               vls_result = this.getInstalledPackages(int_arg0, _arg1);
               reply.writeNoException();
               if (vls_result != null) {
                  reply.writeInt(1);
                  vls_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 18:
               data.enforceInterface(descriptor);
               int_arg0 = data.readInt();
               _arg1 = data.readInt();
               vls_result = this.getInstalledApplications(int_arg0, _arg1);
               reply.writeNoException();
               if (vls_result != null) {
                  reply.writeInt(1);
                  vls_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 19:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               s_arg1 = data.readString();
               _result = data.readInt();
               list_result = this.getReceiverInfos(s_arg0, s_arg1, _result);
               reply.writeNoException();
               reply.writeTypedList(list_result);
               return true;
            case 20:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               i_arg1 = data.readInt();
               PermissionInfo pmi_result = this.getPermissionInfo(s_arg0, i_arg1);
               reply.writeNoException();
               if (pmi_result != null) {
                  reply.writeInt(1);
                  pmi_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 21:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               i_arg1 = data.readInt();
               List<PermissionInfo> permissionInfoList_result = this.queryPermissionsByGroup(s_arg0, i_arg1);
               reply.writeNoException();
               reply.writeTypedList(permissionInfoList_result);
               return true;
            case 22:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               i_arg1 = data.readInt();
               PermissionGroupInfo pg_result = this.getPermissionGroupInfo(s_arg0, i_arg1);
               reply.writeNoException();
               if (pg_result != null) {
                  reply.writeInt(1);
                  pg_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 23:
               data.enforceInterface(descriptor);
               i_arg0 = data.readInt();
               list_result = this.getAllPermissionGroups(i_arg0);
               reply.writeNoException();
               reply.writeTypedList(list_result);
               return true;
            case 24:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               _arg1 = data.readInt();
               _result = data.readInt();
               pi_result = this.resolveContentProvider(s_arg0, _arg1, _result);
               reply.writeNoException();
               if (pi_result != null) {
                  reply.writeInt(1);
                  pi_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 25:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               _arg1 = data.readInt();
               _result = data.readInt();
               ApplicationInfo applicationInfo_result = this.getApplicationInfo(s_arg0, _arg1, _result);
               reply.writeNoException();
               if (applicationInfo_result != null) {
                  reply.writeInt(1);
                  applicationInfo_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 26:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               _arg1 = data.readInt();
               _result = data.readInt();
               vls_result = this.queryContentProviders(s_arg0, _arg1, _result);
               reply.writeNoException();
               if (vls_result != null) {
                  reply.writeInt(1);
                  vls_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 27:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               list_result = this.querySharedPackages(s_arg0);
               reply.writeNoException();
               reply.writeStringList(list_result);
               return true;
            case 28:
               data.enforceInterface(descriptor);
               i_arg0 = data.readInt();
               s_arg1 = this.getNameForUid(i_arg0);
               reply.writeNoException();
               reply.writeString(s_arg1);
               return true;
            case 29:
               data.enforceInterface(descriptor);
               IBinder bind_result = this.getPackageInstaller();
               reply.writeNoException();
               reply.writeStrongBinder(bind_result);
               return true;
            case 30:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               s_arg1 = data.readString();
               _result = this.checkSignatures(s_arg0, s_arg1);
               reply.writeNoException();
               reply.writeInt(_result);
               return true;
            case 31:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               sarr_result = this.getDangerousPermissions(s_arg0);
               reply.writeNoException();
               reply.writeStringArray(sarr_result);
               return true;
            case 32:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  cn_arg0 = (ComponentName)ComponentName.CREATOR.createFromParcel(data);
               } else {
                  cn_arg0 = null;
               }

               _arg1 = data.readInt();
               _result = data.readInt();
               _arg3 = data.readInt();
               this.setComponentEnabledSetting(cn_arg0, _arg1, _result, _arg3);
               reply.writeNoException();
               return true;
            case 33:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  cn_arg0 = (ComponentName)ComponentName.CREATOR.createFromParcel(data);
               } else {
                  cn_arg0 = null;
               }

               _arg1 = data.readInt();
               _result = this.getComponentEnabledSetting(cn_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPackageManager impl) {
         if (IPackageManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IPackageManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPackageManager getDefaultImpl() {
         return IPackageManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPackageManager {
         private IBinder mRemote;
         public static IPackageManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.lody.virtual.server.interfaces.IPackageManager";
         }

         public int getPackageUid(String packageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(packageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  int var7 = IPackageManager.Stub.getDefaultImpl().getPackageUid(packageName, userId);
                  return var7;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public String[] getPackagesForUid(int vuid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String[] _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeInt(vuid);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  String[] var6 = IPackageManager.Stub.getDefaultImpl().getPackagesForUid(vuid);
                  return var6;
               }

               _reply.readException();
               _result = _reply.createStringArray();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public List<String> getSharedLibraries(String pkgName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(pkgName);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  List var6 = IPackageManager.Stub.getDefaultImpl().getSharedLibraries(pkgName);
                  return var6;
               }

               _reply.readException();
               _result = _reply.createStringArrayList();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public int checkPermission(boolean isExt, String permName, String pkgName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var9;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeInt(isExt ? 1 : 0);
               _data.writeString(permName);
               _data.writeString(pkgName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var9 = IPackageManager.Stub.getDefaultImpl().checkPermission(isExt, permName, pkgName, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var9;
         }

         public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            PackageInfo _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(packageName);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  PackageInfo var8 = IPackageManager.Stub.getDefaultImpl().getPackageInfo(packageName, flags, userId);
                  return var8;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (PackageInfo)PackageInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public ActivityInfo getActivityInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ActivityInfo var8;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (componentName != null) {
                  _data.writeInt(1);
                  componentName.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  ActivityInfo _result;
                  if (0 != _reply.readInt()) {
                     _result = (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var8 = IPackageManager.Stub.getDefaultImpl().getActivityInfo(componentName, flags, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var8;
         }

         public boolean activitySupportsIntent(ComponentName component, Intent intent, String resolvedType) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var8;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (component != null) {
                  _data.writeInt(1);
                  component.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(resolvedType);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var8 = IPackageManager.Stub.getDefaultImpl().activitySupportsIntent(component, intent, resolvedType);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var8;
         }

         public ActivityInfo getReceiverInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ActivityInfo _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (componentName != null) {
                  _data.writeInt(1);
                  componentName.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  ActivityInfo var8 = IPackageManager.Stub.getDefaultImpl().getReceiverInfo(componentName, flags, userId);
                  return var8;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public ServiceInfo getServiceInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ServiceInfo _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (componentName != null) {
                  _data.writeInt(1);
                  componentName.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  ServiceInfo var8 = IPackageManager.Stub.getDefaultImpl().getServiceInfo(componentName, flags, userId);
                  return var8;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (ServiceInfo)ServiceInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public ProviderInfo getProviderInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ProviderInfo var8;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (componentName != null) {
                  _data.writeInt(1);
                  componentName.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  ProviderInfo _result;
                  if (0 != _reply.readInt()) {
                     _result = (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var8 = IPackageManager.Stub.getDefaultImpl().getProviderInfo(componentName, flags, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var8;
         }

         public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ResolveInfo var9;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(resolvedType);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(11, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  ResolveInfo _result;
                  if (0 != _reply.readInt()) {
                     _result = (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var9 = IPackageManager.Stub.getDefaultImpl().resolveIntent(intent, resolvedType, flags, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var9;
         }

         public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(resolvedType);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(12, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  List var9 = IPackageManager.Stub.getDefaultImpl().queryIntentActivities(intent, resolvedType, flags, userId);
                  return var9;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(ResolveInfo.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var9;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(resolvedType);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(13, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<ResolveInfo> _result = _reply.createTypedArrayList(ResolveInfo.CREATOR);
                  return _result;
               }

               var9 = IPackageManager.Stub.getDefaultImpl().queryIntentReceivers(intent, resolvedType, flags, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var9;
         }

         public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ResolveInfo _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(resolvedType);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(14, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  ResolveInfo var9 = IPackageManager.Stub.getDefaultImpl().resolveService(intent, resolvedType, flags, userId);
                  return var9;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var9;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(resolvedType);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(15, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<ResolveInfo> _result = _reply.createTypedArrayList(ResolveInfo.CREATOR);
                  return _result;
               }

               var9 = IPackageManager.Stub.getDefaultImpl().queryIntentServices(intent, resolvedType, flags, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var9;
         }

         public List<ResolveInfo> queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var9;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(resolvedType);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(16, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<ResolveInfo> _result = _reply.createTypedArrayList(ResolveInfo.CREATOR);
                  return _result;
               }

               var9 = IPackageManager.Stub.getDefaultImpl().queryIntentContentProviders(intent, resolvedType, flags, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var9;
         }

         public VParceledListSlice getInstalledPackages(int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VParceledListSlice var7;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(17, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  VParceledListSlice _result;
                  if (0 != _reply.readInt()) {
                     _result = (VParceledListSlice)VParceledListSlice.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IPackageManager.Stub.getDefaultImpl().getInstalledPackages(flags, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public VParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VParceledListSlice var7;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(18, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  VParceledListSlice _result;
                  if (0 != _reply.readInt()) {
                     _result = (VParceledListSlice)VParceledListSlice.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IPackageManager.Stub.getDefaultImpl().getInstalledApplications(flags, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public List<ReceiverInfo> getReceiverInfos(String packageName, String processName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var8;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(packageName);
               _data.writeString(processName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(19, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<ReceiverInfo> _result = _reply.createTypedArrayList(ReceiverInfo.CREATOR);
                  return _result;
               }

               var8 = IPackageManager.Stub.getDefaultImpl().getReceiverInfos(packageName, processName, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var8;
         }

         public PermissionInfo getPermissionInfo(String name, int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            PermissionInfo var7;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(name);
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(20, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  PermissionInfo _result;
                  if (0 != _reply.readInt()) {
                     _result = (PermissionInfo)PermissionInfo.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IPackageManager.Stub.getDefaultImpl().getPermissionInfo(name, flags);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var7;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(group);
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(21, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<PermissionInfo> _result = _reply.createTypedArrayList(PermissionInfo.CREATOR);
                  return _result;
               }

               var7 = IPackageManager.Stub.getDefaultImpl().queryPermissionsByGroup(group, flags);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            PermissionGroupInfo var7;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(name);
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(22, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  PermissionGroupInfo _result;
                  if (0 != _reply.readInt()) {
                     _result = (PermissionGroupInfo)PermissionGroupInfo.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IPackageManager.Stub.getDefaultImpl().getPermissionGroupInfo(name, flags);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public List<PermissionGroupInfo> getAllPermissionGroups(int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var6;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(23, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<PermissionGroupInfo> _result = _reply.createTypedArrayList(PermissionGroupInfo.CREATOR);
                  return _result;
               }

               var6 = IPackageManager.Stub.getDefaultImpl().getAllPermissionGroups(flags);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public ProviderInfo resolveContentProvider(String name, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ProviderInfo _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(name);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(24, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  ProviderInfo var8 = IPackageManager.Stub.getDefaultImpl().resolveContentProvider(name, flags, userId);
                  return var8;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ApplicationInfo _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(packageName);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(25, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  ApplicationInfo var8 = IPackageManager.Stub.getDefaultImpl().getApplicationInfo(packageName, flags, userId);
                  return var8;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public VParceledListSlice queryContentProviders(String processName, int vuid, int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VParceledListSlice _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(processName);
               _data.writeInt(vuid);
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(26, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  VParceledListSlice var8 = IPackageManager.Stub.getDefaultImpl().queryContentProviders(processName, vuid, flags);
                  return var8;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (VParceledListSlice)VParceledListSlice.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public List<String> querySharedPackages(String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var6;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(27, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<String> _result = _reply.createStringArrayList();
                  return _result;
               }

               var6 = IPackageManager.Stub.getDefaultImpl().querySharedPackages(packageName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public String getNameForUid(int uid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var6;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeInt(uid);
               boolean _status = this.mRemote.transact(28, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var6 = IPackageManager.Stub.getDefaultImpl().getNameForUid(uid);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public IBinder getPackageInstaller() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IBinder _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               boolean _status = this.mRemote.transact(29, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  IBinder var5 = IPackageManager.Stub.getDefaultImpl().getPackageInstaller();
                  return var5;
               }

               _reply.readException();
               _result = _reply.readStrongBinder();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(pkg1);
               _data.writeString(pkg2);
               boolean _status = this.mRemote.transact(30, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  int var7 = IPackageManager.Stub.getDefaultImpl().checkSignatures(pkg1, pkg2);
                  return var7;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public String[] getDangerousPermissions(String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String[] _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(31, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  String[] var6 = IPackageManager.Stub.getDefaultImpl().getDangerousPermissions(packageName);
                  return var6;
               }

               _reply.readException();
               _result = _reply.createStringArray();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (componentName != null) {
                  _data.writeInt(1);
                  componentName.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(newState);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(32, _data, _reply, 0);
               if (!_status && IPackageManager.Stub.getDefaultImpl() != null) {
                  IPackageManager.Stub.getDefaultImpl().setComponentEnabledSetting(componentName, newState, flags, userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public int getComponentEnabledSetting(ComponentName component, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var7;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IPackageManager");
               if (component != null) {
                  _data.writeInt(1);
                  component.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(33, _data, _reply, 0);
               if (_status || IPackageManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var7 = IPackageManager.Stub.getDefaultImpl().getComponentEnabledSetting(component, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }
      }
   }

   public static class Default implements IPackageManager {
      public int getPackageUid(String packageName, int userId) throws RemoteException {
         return 0;
      }

      public String[] getPackagesForUid(int vuid) throws RemoteException {
         return null;
      }

      public List<String> getSharedLibraries(String pkgName) throws RemoteException {
         return null;
      }

      public int checkPermission(boolean isExt, String permName, String pkgName, int userId) throws RemoteException {
         return 0;
      }

      public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
         return null;
      }

      public ActivityInfo getActivityInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
         return null;
      }

      public boolean activitySupportsIntent(ComponentName component, Intent intent, String resolvedType) throws RemoteException {
         return false;
      }

      public ActivityInfo getReceiverInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
         return null;
      }

      public ServiceInfo getServiceInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
         return null;
      }

      public ProviderInfo getProviderInfo(ComponentName componentName, int flags, int userId) throws RemoteException {
         return null;
      }

      public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
         return null;
      }

      public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
         return null;
      }

      public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
         return null;
      }

      public ResolveInfo resolveService(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
         return null;
      }

      public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
         return null;
      }

      public List<ResolveInfo> queryIntentContentProviders(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
         return null;
      }

      public VParceledListSlice getInstalledPackages(int flags, int userId) throws RemoteException {
         return null;
      }

      public VParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException {
         return null;
      }

      public List<ReceiverInfo> getReceiverInfos(String packageName, String processName, int userId) throws RemoteException {
         return null;
      }

      public PermissionInfo getPermissionInfo(String name, int flags) throws RemoteException {
         return null;
      }

      public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) throws RemoteException {
         return null;
      }

      public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
         return null;
      }

      public List<PermissionGroupInfo> getAllPermissionGroups(int flags) throws RemoteException {
         return null;
      }

      public ProviderInfo resolveContentProvider(String name, int flags, int userId) throws RemoteException {
         return null;
      }

      public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
         return null;
      }

      public VParceledListSlice queryContentProviders(String processName, int vuid, int flags) throws RemoteException {
         return null;
      }

      public List<String> querySharedPackages(String packageName) throws RemoteException {
         return null;
      }

      public String getNameForUid(int uid) throws RemoteException {
         return null;
      }

      public IBinder getPackageInstaller() throws RemoteException {
         return null;
      }

      public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
         return 0;
      }

      public String[] getDangerousPermissions(String packageName) throws RemoteException {
         return null;
      }

      public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags, int userId) throws RemoteException {
      }

      public int getComponentEnabledSetting(ComponentName component, int userId) throws RemoteException {
         return 0;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
