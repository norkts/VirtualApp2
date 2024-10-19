package com.lody.virtual.server.interfaces;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.BadgerInfo;
import com.lody.virtual.remote.ClientConfig;
import com.lody.virtual.remote.IntentSenderData;
import com.lody.virtual.remote.VParceledListSlice;
import java.util.List;

public interface IActivityManager extends IInterface {
   ClientConfig initProcess(String var1, String var2, int var3) throws RemoteException;

   void appDoneExecuting(String var1, int var2) throws RemoteException;

   int getFreeStubCount() throws RemoteException;

   int checkPermission(boolean var1, String var2, int var3, int var4, String var5) throws RemoteException;

   int getSystemPid() throws RemoteException;

   int getSystemUid() throws RemoteException;

   int getUidByPid(int var1) throws RemoteException;

   int getCurrentUserId() throws RemoteException;

   boolean isAppProcess(String var1) throws RemoteException;

   boolean isAppRunning(String var1, int var2, boolean var3) throws RemoteException;

   boolean isAppPid(int var1) throws RemoteException;

   String getAppProcessName(int var1) throws RemoteException;

   List<String> getProcessPkgList(int var1) throws RemoteException;

   void killAllApps() throws RemoteException;

   void killAppByPkg(String var1, int var2) throws RemoteException;

   void killApplicationProcess(String var1, int var2) throws RemoteException;

   void dump() throws RemoteException;

   String getInitialPackage(int var1) throws RemoteException;

   int startActivities(Intent[] var1, String[] var2, IBinder var3, Bundle var4, String var5, int var6) throws RemoteException;

   int startActivity(Intent var1, ActivityInfo var2, IBinder var3, Bundle var4, String var5, int var6, String var7, int var8) throws RemoteException;

   int startActivityFromHistory(Intent var1) throws RemoteException;

   boolean finishActivityAffinity(int var1, IBinder var2) throws RemoteException;

   void onActivityCreated(IBinder var1, IBinder var2, int var3) throws RemoteException;

   void onActivityResumed(int var1, IBinder var2) throws RemoteException;

   boolean onActivityDestroyed(int var1, IBinder var2) throws RemoteException;

   void onActivityFinish(int var1, IBinder var2) throws RemoteException;

   ComponentName getActivityClassForToken(int var1, IBinder var2) throws RemoteException;

   String getCallingPackage(int var1, IBinder var2) throws RemoteException;

   ComponentName getCallingActivity(int var1, IBinder var2) throws RemoteException;

   AppTaskInfo getTaskInfo(int var1) throws RemoteException;

   String getPackageForToken(int var1, IBinder var2) throws RemoteException;

   IBinder acquireProviderClient(int var1, ProviderInfo var2) throws RemoteException;

   boolean broadcastFinish(IBinder var1) throws RemoteException;

   void addOrUpdateIntentSender(IntentSenderData var1, int var2) throws RemoteException;

   void removeIntentSender(IBinder var1) throws RemoteException;

   IntentSenderData getIntentSender(IBinder var1) throws RemoteException;

   void processRestarted(String var1, String var2, int var3) throws RemoteException;

   void notifyBadgerChange(BadgerInfo var1) throws RemoteException;

   void setAppInactive(String var1, boolean var2, int var3) throws RemoteException;

   boolean isAppInactive(String var1, int var2) throws RemoteException;

   VParceledListSlice getServices(String var1, int var2, int var3, int var4) throws RemoteException;

   void handleDownloadCompleteIntent(Intent var1) throws RemoteException;

   int getAppPid(String var1, int var2, String var3) throws RemoteException;

   void setSettingsProvider(int var1, int var2, String var3, String var4) throws RemoteException;

   String getSettingsProvider(int var1, int var2, String var3) throws RemoteException;

   public abstract static class Stub extends Binder implements IActivityManager {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ="));
      static final int TRANSACTION_initProcess = 1;
      static final int TRANSACTION_appDoneExecuting = 2;
      static final int TRANSACTION_getFreeStubCount = 3;
      static final int TRANSACTION_checkPermission = 4;
      static final int TRANSACTION_getSystemPid = 5;
      static final int TRANSACTION_getSystemUid = 6;
      static final int TRANSACTION_getUidByPid = 7;
      static final int TRANSACTION_getCurrentUserId = 8;
      static final int TRANSACTION_isAppProcess = 9;
      static final int TRANSACTION_isAppRunning = 10;
      static final int TRANSACTION_isAppPid = 11;
      static final int TRANSACTION_getAppProcessName = 12;
      static final int TRANSACTION_getProcessPkgList = 13;
      static final int TRANSACTION_killAllApps = 14;
      static final int TRANSACTION_killAppByPkg = 15;
      static final int TRANSACTION_killApplicationProcess = 16;
      static final int TRANSACTION_dump = 17;
      static final int TRANSACTION_getInitialPackage = 18;
      static final int TRANSACTION_startActivities = 19;
      static final int TRANSACTION_startActivity = 20;
      static final int TRANSACTION_startActivityFromHistory = 21;
      static final int TRANSACTION_finishActivityAffinity = 22;
      static final int TRANSACTION_onActivityCreated = 23;
      static final int TRANSACTION_onActivityResumed = 24;
      static final int TRANSACTION_onActivityDestroyed = 25;
      static final int TRANSACTION_onActivityFinish = 26;
      static final int TRANSACTION_getActivityClassForToken = 27;
      static final int TRANSACTION_getCallingPackage = 28;
      static final int TRANSACTION_getCallingActivity = 29;
      static final int TRANSACTION_getTaskInfo = 30;
      static final int TRANSACTION_getPackageForToken = 31;
      static final int TRANSACTION_acquireProviderClient = 32;
      static final int TRANSACTION_broadcastFinish = 33;
      static final int TRANSACTION_addOrUpdateIntentSender = 34;
      static final int TRANSACTION_removeIntentSender = 35;
      static final int TRANSACTION_getIntentSender = 36;
      static final int TRANSACTION_processRestarted = 37;
      static final int TRANSACTION_notifyBadgerChange = 38;
      static final int TRANSACTION_setAppInactive = 39;
      static final int TRANSACTION_isAppInactive = 40;
      static final int TRANSACTION_getServices = 41;
      static final int TRANSACTION_handleDownloadCompleteIntent = 42;
      static final int TRANSACTION_getAppPid = 43;
      static final int TRANSACTION_setSettingsProvider = 44;
      static final int TRANSACTION_getSettingsProvider = 45;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
      }

      public static IActivityManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IActivityManager)(iin != null && iin instanceof IActivityManager ? (IActivityManager)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         int _arg1;
         String _arg2;
         String _result;
         int _arg5;
         String s_arg0;
         Intent intent_arg0;
         boolean b_arg1;
         String s_arg1;
         int i_arg2;
         boolean b_result;
         int u_result;
         IBinder binder_arg0;
         IBinder binder_arg2;
         Bundle bundle_arg3;
         String s_arg4;
         ComponentName cn_result;
         IBinder bind_arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               s_arg1 = data.readString();
               i_arg2 = data.readInt();
               ClientConfig cc_result = this.initProcess(s_arg0, s_arg1, i_arg2);
               reply.writeNoException();
               if (cc_result != null) {
                  reply.writeInt(1);
                  cc_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 2:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               int i_arg1 = data.readInt();
               this.appDoneExecuting(s_arg0, i_arg1);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = this.getFreeStubCount();
               reply.writeNoException();
               reply.writeInt(_arg0);
               return true;
            case 4:
               data.enforceInterface(descriptor);
               boolean b_arg0 = 0 != data.readInt();
               s_arg1 = data.readString();
               i_arg2 = data.readInt();
               int i_result = data.readInt();
               s_arg4 = data.readString();
               _arg5 = this.checkPermission(b_arg0, s_arg1, i_arg2, i_result, s_arg4);
               reply.writeNoException();
               reply.writeInt(_arg5);
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = this.getSystemPid();
               reply.writeNoException();
               reply.writeInt(_arg0);
               return true;
            case 6:
               data.enforceInterface(descriptor);
               _arg0 = this.getSystemUid();
               reply.writeNoException();
               reply.writeInt(_arg0);
               return true;
            case 7:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = this.getUidByPid(_arg0);
               reply.writeNoException();
               reply.writeInt(_arg1);
               return true;
            case 8:
               data.enforceInterface(descriptor);
               _arg0 = this.getCurrentUserId();
               reply.writeNoException();
               reply.writeInt(_arg0);
               return true;
            case 9:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               b_arg1 = this.isAppProcess(s_arg0);
               reply.writeNoException();
               reply.writeInt(b_arg1 ? 1 : 0);
               return true;
            case 10:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               i_arg1 = data.readInt();
               b_result = 0 != data.readInt();
               boolean b1_result = this.isAppRunning(s_arg0, i_arg1, b_result);
               reply.writeNoException();
               reply.writeInt(b1_result ? 1 : 0);
               return true;
            case 11:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               b_arg1 = this.isAppPid(_arg0);
               reply.writeNoException();
               reply.writeInt(b_arg1 ? 1 : 0);
               return true;
            case 12:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               s_arg1 = this.getAppProcessName(_arg0);
               reply.writeNoException();
               reply.writeString(s_arg1);
               return true;
            case 13:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               List<String> list_result = this.getProcessPkgList(_arg0);
               reply.writeNoException();
               reply.writeStringList(list_result);
               return true;
            case 14:
               data.enforceInterface(descriptor);
               this.killAllApps();
               reply.writeNoException();
               return true;
            case 15:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               i_arg1 = data.readInt();
               this.killAppByPkg(s_arg0, i_arg1);
               reply.writeNoException();
               return true;
            case 16:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               _arg1 = data.readInt();
               this.killApplicationProcess(s_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 17:
               data.enforceInterface(descriptor);
               this.dump();
               reply.writeNoException();
               return true;
            case 18:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               s_arg1 = this.getInitialPackage(_arg0);
               reply.writeNoException();
               reply.writeString(s_arg1);
               return true;
            case 19:
               data.enforceInterface(descriptor);
               Intent[] intents_arg0 = (Intent[])data.createTypedArray(Intent.CREATOR);
               String[] strs_arg1 = data.createStringArray();
               binder_arg2 = data.readStrongBinder();
               if (0 != data.readInt()) {
                  bundle_arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  bundle_arg3 = null;
               }

               s_arg4 = data.readString();
               _arg5 = data.readInt();
               i_result = this.startActivities(intents_arg0, strs_arg1, binder_arg2, bundle_arg3, s_arg4, _arg5);
               reply.writeNoException();
               reply.writeInt(i_result);
               return true;
            case 20:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  intent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  intent_arg0 = null;
               }

               ActivityInfo activityInfo_arg1;
               if (0 != data.readInt()) {
                  activityInfo_arg1 = (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(data);
               } else {
                  activityInfo_arg1 = null;
               }

               binder_arg2 = data.readStrongBinder();
               if (0 != data.readInt()) {
                  bundle_arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  bundle_arg3 = null;
               }

               s_arg4 = data.readString();
               _arg5 = data.readInt();
               String _arg6 = data.readString();
               int _arg7 = data.readInt();
               i_result = this.startActivity(intent_arg0, activityInfo_arg1, binder_arg2, bundle_arg3, s_arg4, _arg5, _arg6, _arg7);
               reply.writeNoException();
               reply.writeInt(i_result);
               return true;
            case 21:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  intent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  intent_arg0 = null;
               }

               _arg1 = this.startActivityFromHistory(intent_arg0);
               reply.writeNoException();
               reply.writeInt(_arg1);
               return true;
            case 22:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               IBinder binder_arg1 = data.readStrongBinder();
               b_result = this.finishActivityAffinity(_arg0, binder_arg1);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 23:
               data.enforceInterface(descriptor);
               binder_arg0 = data.readStrongBinder();
               binder_arg1 = data.readStrongBinder();
               i_arg2 = data.readInt();
               this.onActivityCreated(binder_arg0, binder_arg1, i_arg2);
               reply.writeNoException();
               return true;
            case 24:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               binder_arg1 = data.readStrongBinder();
               this.onActivityResumed(_arg0, binder_arg1);
               reply.writeNoException();
               return true;
            case 25:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               binder_arg1 = data.readStrongBinder();
               b_result = this.onActivityDestroyed(_arg0, binder_arg1);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 26:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               binder_arg1 = data.readStrongBinder();
               this.onActivityFinish(_arg0, binder_arg1);
               reply.writeNoException();
               return true;
            case 27:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               binder_arg1 = data.readStrongBinder();
               cn_result = this.getActivityClassForToken(_arg0, binder_arg1);
               reply.writeNoException();
               if (cn_result != null) {
                  reply.writeInt(1);
                  cn_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 28:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               binder_arg1 = data.readStrongBinder();
               _arg2 = this.getCallingPackage(_arg0, binder_arg1);
               reply.writeNoException();
               reply.writeString(_arg2);
               return true;
            case 29:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               binder_arg1 = data.readStrongBinder();
               cn_result = this.getCallingActivity(_arg0, binder_arg1);
               reply.writeNoException();
               if (cn_result != null) {
                  reply.writeInt(1);
                  cn_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 30:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               AppTaskInfo appTaskInfo_result = this.getTaskInfo(_arg0);
               reply.writeNoException();
               if (appTaskInfo_result != null) {
                  reply.writeInt(1);
                  appTaskInfo_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 31:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               binder_arg1 = data.readStrongBinder();
               _arg2 = this.getPackageForToken(_arg0, binder_arg1);
               reply.writeNoException();
               reply.writeString(_arg2);
               return true;
            case 32:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ProviderInfo providerInfo_arg1;
               if (0 != data.readInt()) {
                  providerInfo_arg1 = (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(data);
               } else {
                  providerInfo_arg1 = null;
               }

               binder_arg2 = this.acquireProviderClient(_arg0, providerInfo_arg1);
               reply.writeNoException();
               reply.writeStrongBinder(binder_arg2);
               return true;
            case 33:
               data.enforceInterface(descriptor);
               binder_arg0 = data.readStrongBinder();
               b_arg1 = this.broadcastFinish(binder_arg0);
               reply.writeNoException();
               reply.writeInt(b_arg1 ? 1 : 0);
               return true;
            case 34:
               data.enforceInterface(descriptor);
               IntentSenderData intentSenderData_arg0;
               if (0 != data.readInt()) {
                  intentSenderData_arg0 = (IntentSenderData)IntentSenderData.CREATOR.createFromParcel(data);
               } else {
                  intentSenderData_arg0 = null;
               }

               _arg1 = data.readInt();
               this.addOrUpdateIntentSender(intentSenderData_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 35:
               data.enforceInterface(descriptor);
               binder_arg0 = data.readStrongBinder();
               this.removeIntentSender(binder_arg0);
               reply.writeNoException();
               return true;
            case 36:
               data.enforceInterface(descriptor);
               binder_arg0 = data.readStrongBinder();
               IntentSenderData intentSender_result = this.getIntentSender(binder_arg0);
               reply.writeNoException();
               if (intentSender_result != null) {
                  reply.writeInt(1);
                  intentSender_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 37:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               s_arg1 = data.readString();
               i_arg2 = data.readInt();
               this.processRestarted(s_arg0, s_arg1, i_arg2);
               reply.writeNoException();
               return true;
            case 38:
               data.enforceInterface(descriptor);
               BadgerInfo badgerInfo_arg0;
               if (0 != data.readInt()) {
                  badgerInfo_arg0 = (BadgerInfo)BadgerInfo.CREATOR.createFromParcel(data);
               } else {
                  badgerInfo_arg0 = null;
               }

               this.notifyBadgerChange(badgerInfo_arg0);
               reply.writeNoException();
               return true;
            case 39:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               b_arg1 = 0 != data.readInt();
               i_arg2 = data.readInt();
               this.setAppInactive(s_arg0, b_arg1, i_arg2);
               reply.writeNoException();
               return true;
            case 40:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               _arg1 = data.readInt();
               b_result = this.isAppInactive(s_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 41:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               i_arg1 = data.readInt();
               i_arg2 = data.readInt();
               i_result = data.readInt();
               VParceledListSlice vls_result = this.getServices(s_arg0, i_arg1, i_arg2, i_result);
               reply.writeNoException();
               if (vls_result != null) {
                  reply.writeInt(1);
                  vls_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 42:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  intent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  intent_arg0 = null;
               }

               this.handleDownloadCompleteIntent(intent_arg0);
               reply.writeNoException();
               return true;
            case 43:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               _arg1 = data.readInt();
               _arg2 = data.readString();
               i_result = this.getAppPid(s_arg0, _arg1, _arg2);
               reply.writeNoException();
               reply.writeInt(i_result);
               return true;
            case 44:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readInt();
               _arg2 = data.readString();
               _result = data.readString();
               this.setSettingsProvider(_arg0, _arg1, _arg2, _result);
               reply.writeNoException();
               return true;
            case 45:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readInt();
               _arg2 = data.readString();
               _result = this.getSettingsProvider(_arg0, _arg1, _arg2);
               reply.writeNoException();
               reply.writeString(_result);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IActivityManager impl) {
         if (IActivityManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IActivityManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IActivityManager getDefaultImpl() {
         return IActivityManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IActivityManager {
         private IBinder mRemote;
         public static IActivityManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ="));
         }

         public ClientConfig initProcess(String packageName, String processName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ClientConfig var8;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(packageName);
               _data.writeString(processName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  ClientConfig _result;
                  if (0 != _reply.readInt()) {
                     _result = (ClientConfig)ClientConfig.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var8 = IActivityManager.Stub.getDefaultImpl().initProcess(packageName, processName, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var8;
         }

         public void appDoneExecuting(String packageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(packageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  IActivityManager.Stub.getDefaultImpl().appDoneExecuting(packageName, userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public int getFreeStubCount() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  int var5 = IActivityManager.Stub.getDefaultImpl().getFreeStubCount();
                  return var5;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public int checkPermission(boolean isExt, String permission, int pid, int uid, String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(isExt ? 1 : 0);
               _data.writeString(permission);
               _data.writeInt(pid);
               _data.writeInt(uid);
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  int var10 = IActivityManager.Stub.getDefaultImpl().checkPermission(isExt, permission, pid, uid, packageName);
                  return var10;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public int getSystemPid() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  int var5 = IActivityManager.Stub.getDefaultImpl().getSystemPid();
                  return var5;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public int getSystemUid() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var5 = IActivityManager.Stub.getDefaultImpl().getSystemUid();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public int getUidByPid(int pid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(pid);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var6 = IActivityManager.Stub.getDefaultImpl().getUidByPid(pid);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public int getCurrentUserId() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  int var5 = IActivityManager.Stub.getDefaultImpl().getCurrentUserId();
                  return var5;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public boolean isAppProcess(String processName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(processName);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var6 = IActivityManager.Stub.getDefaultImpl().isAppProcess(processName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public boolean isAppRunning(String packageName, int userId, boolean foreground) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var8;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(packageName);
               _data.writeInt(userId);
               _data.writeInt(foreground ? 1 : 0);
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var8 = IActivityManager.Stub.getDefaultImpl().isAppRunning(packageName, userId, foreground);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var8;
         }

         public boolean isAppPid(int pid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(pid);
               boolean _status = this.mRemote.transact(11, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  boolean var6 = IActivityManager.Stub.getDefaultImpl().isAppPid(pid);
                  return var6;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public String getAppProcessName(int pid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(pid);
               boolean _status = this.mRemote.transact(12, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var6 = IActivityManager.Stub.getDefaultImpl().getAppProcessName(pid);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public List<String> getProcessPkgList(int pid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(pid);
               boolean _status = this.mRemote.transact(13, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<String> _result = _reply.createStringArrayList();
                  return _result;
               }

               var6 = IActivityManager.Stub.getDefaultImpl().getProcessPkgList(pid);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public void killAllApps() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               boolean _status = this.mRemote.transact(14, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IActivityManager.Stub.getDefaultImpl().killAllApps();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void killAppByPkg(String pkg, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(pkg);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(15, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  IActivityManager.Stub.getDefaultImpl().killAppByPkg(pkg, userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void killApplicationProcess(String processName, int vuid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(processName);
               _data.writeInt(vuid);
               boolean _status = this.mRemote.transact(16, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  IActivityManager.Stub.getDefaultImpl().killApplicationProcess(processName, vuid);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void dump() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               boolean _status = this.mRemote.transact(17, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IActivityManager.Stub.getDefaultImpl().dump();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public String getInitialPackage(int pid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(pid);
               boolean _status = this.mRemote.transact(18, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var6 = IActivityManager.Stub.getDefaultImpl().getInitialPackage(pid);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public int startActivities(Intent[] intents, String[] resolvedTypes, IBinder token, Bundle options, String callingPkg, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var11;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeTypedArray(intents, 0);
               _data.writeStringArray(resolvedTypes);
               _data.writeStrongBinder(token);
               if (options != null) {
                  _data.writeInt(1);
                  options.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(callingPkg);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(19, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var11 = IActivityManager.Stub.getDefaultImpl().startActivities(intents, resolvedTypes, token, options, callingPkg, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var11;
         }

         public int startActivity(Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode, String callingPkg, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               if (info != null) {
                  _data.writeInt(1);
                  info.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeStrongBinder(resultTo);
               if (options != null) {
                  _data.writeInt(1);
                  options.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(resultWho);
               _data.writeInt(requestCode);
               _data.writeString(callingPkg);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(20, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  int var13 = IActivityManager.Stub.getDefaultImpl().startActivity(intent, info, resultTo, options, resultWho, requestCode, callingPkg, userId);
                  return var13;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public int startActivityFromHistory(Intent intent) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(21, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var6 = IActivityManager.Stub.getDefaultImpl().startActivityFromHistory(intent);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public boolean finishActivityAffinity(int userId, IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(22, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IActivityManager.Stub.getDefaultImpl().finishActivityAffinity(userId, token);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void onActivityCreated(IBinder record, IBinder token, int taskId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeStrongBinder(record);
               _data.writeStrongBinder(token);
               _data.writeInt(taskId);
               boolean _status = this.mRemote.transact(23, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IActivityManager.Stub.getDefaultImpl().onActivityCreated(record, token, taskId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onActivityResumed(int userId, IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(24, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  IActivityManager.Stub.getDefaultImpl().onActivityResumed(userId, token);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean onActivityDestroyed(int userId, IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(25, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  boolean var7 = IActivityManager.Stub.getDefaultImpl().onActivityDestroyed(userId, token);
                  return var7;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void onActivityFinish(int userId, IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(26, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IActivityManager.Stub.getDefaultImpl().onActivityFinish(userId, token);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public ComponentName getActivityClassForToken(int userId, IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ComponentName var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(27, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  ComponentName _result;
                  if (0 != _reply.readInt()) {
                     _result = (ComponentName)ComponentName.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IActivityManager.Stub.getDefaultImpl().getActivityClassForToken(userId, token);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public String getCallingPackage(int userId, IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(28, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var7 = IActivityManager.Stub.getDefaultImpl().getCallingPackage(userId, token);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public ComponentName getCallingActivity(int userId, IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ComponentName var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(29, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  ComponentName _result;
                  if (0 != _reply.readInt()) {
                     _result = (ComponentName)ComponentName.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IActivityManager.Stub.getDefaultImpl().getCallingActivity(userId, token);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public AppTaskInfo getTaskInfo(int taskId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            AppTaskInfo var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(taskId);
               boolean _status = this.mRemote.transact(30, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  AppTaskInfo _result;
                  if (0 != _reply.readInt()) {
                     _result = (AppTaskInfo)AppTaskInfo.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IActivityManager.Stub.getDefaultImpl().getTaskInfo(taskId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public String getPackageForToken(int userId, IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(31, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  String var7 = IActivityManager.Stub.getDefaultImpl().getPackageForToken(userId, token);
                  return var7;
               }

               _reply.readException();
               _result = _reply.readString();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public IBinder acquireProviderClient(int userId, ProviderInfo info) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IBinder var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               if (info != null) {
                  _data.writeInt(1);
                  info.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(32, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  IBinder _result = _reply.readStrongBinder();
                  return _result;
               }

               var7 = IActivityManager.Stub.getDefaultImpl().acquireProviderClient(userId, info);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public boolean broadcastFinish(IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(33, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var6 = IActivityManager.Stub.getDefaultImpl().broadcastFinish(token);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public void addOrUpdateIntentSender(IntentSenderData sender, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               if (sender != null) {
                  _data.writeInt(1);
                  sender.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(34, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  IActivityManager.Stub.getDefaultImpl().addOrUpdateIntentSender(sender, userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void removeIntentSender(IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(35, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  IActivityManager.Stub.getDefaultImpl().removeIntentSender(token);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public IntentSenderData getIntentSender(IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IntentSenderData var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(36, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  IntentSenderData _result;
                  if (0 != _reply.readInt()) {
                     _result = (IntentSenderData)IntentSenderData.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IActivityManager.Stub.getDefaultImpl().getIntentSender(token);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public void processRestarted(String packageName, String processName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(packageName);
               _data.writeString(processName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(37, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  IActivityManager.Stub.getDefaultImpl().processRestarted(packageName, processName, userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void notifyBadgerChange(BadgerInfo info) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               if (info != null) {
                  _data.writeInt(1);
                  info.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(38, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  IActivityManager.Stub.getDefaultImpl().notifyBadgerChange(info);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setAppInactive(String packageName, boolean idle, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(packageName);
               _data.writeInt(idle ? 1 : 0);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(39, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IActivityManager.Stub.getDefaultImpl().setAppInactive(packageName, idle, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean isAppInactive(String packageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(packageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(40, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  boolean var7 = IActivityManager.Stub.getDefaultImpl().isAppInactive(packageName, userId);
                  return var7;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public VParceledListSlice getServices(String pkg, int maxNum, int flags, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VParceledListSlice _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(pkg);
               _data.writeInt(maxNum);
               _data.writeInt(flags);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(41, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  VParceledListSlice var9 = IActivityManager.Stub.getDefaultImpl().getServices(pkg, maxNum, flags, userId);
                  return var9;
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

         public void handleDownloadCompleteIntent(Intent intent) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(42, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IActivityManager.Stub.getDefaultImpl().handleDownloadCompleteIntent(intent);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public int getAppPid(String packageName, int userId, String proccessName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeString(packageName);
               _data.writeInt(userId);
               _data.writeString(proccessName);
               boolean _status = this.mRemote.transact(43, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  int var8 = IActivityManager.Stub.getDefaultImpl().getAppPid(packageName, userId, proccessName);
                  return var8;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void setSettingsProvider(int userId, int tableIndex, String arg, String value) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeInt(tableIndex);
               _data.writeString(arg);
               _data.writeString(value);
               boolean _status = this.mRemote.transact(44, _data, _reply, 0);
               if (_status || IActivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IActivityManager.Stub.getDefaultImpl().setSettingsProvider(userId, tableIndex, arg, value);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public String getSettingsProvider(int userId, int tableIndex, String arg) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuM28FJD1lNDASLggcP2gzNAQ=")));
               _data.writeInt(userId);
               _data.writeInt(tableIndex);
               _data.writeString(arg);
               boolean _status = this.mRemote.transact(45, _data, _reply, 0);
               if (!_status && IActivityManager.Stub.getDefaultImpl() != null) {
                  String var8 = IActivityManager.Stub.getDefaultImpl().getSettingsProvider(userId, tableIndex, arg);
                  return var8;
               }

               _reply.readException();
               _result = _reply.readString();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }
      }
   }

   public static class Default implements IActivityManager {
      public ClientConfig initProcess(String packageName, String processName, int userId) throws RemoteException {
         return null;
      }

      public void appDoneExecuting(String packageName, int userId) throws RemoteException {
      }

      public int getFreeStubCount() throws RemoteException {
         return 0;
      }

      public int checkPermission(boolean isExt, String permission, int pid, int uid, String packageName) throws RemoteException {
         return 0;
      }

      public int getSystemPid() throws RemoteException {
         return 0;
      }

      public int getSystemUid() throws RemoteException {
         return 0;
      }

      public int getUidByPid(int pid) throws RemoteException {
         return 0;
      }

      public int getCurrentUserId() throws RemoteException {
         return 0;
      }

      public boolean isAppProcess(String processName) throws RemoteException {
         return false;
      }

      public boolean isAppRunning(String packageName, int userId, boolean foreground) throws RemoteException {
         return false;
      }

      public boolean isAppPid(int pid) throws RemoteException {
         return false;
      }

      public String getAppProcessName(int pid) throws RemoteException {
         return null;
      }

      public List<String> getProcessPkgList(int pid) throws RemoteException {
         return null;
      }

      public void killAllApps() throws RemoteException {
      }

      public void killAppByPkg(String pkg, int userId) throws RemoteException {
      }

      public void killApplicationProcess(String processName, int vuid) throws RemoteException {
      }

      public void dump() throws RemoteException {
      }

      public String getInitialPackage(int pid) throws RemoteException {
         return null;
      }

      public int startActivities(Intent[] intents, String[] resolvedTypes, IBinder token, Bundle options, String callingPkg, int userId) throws RemoteException {
         return 0;
      }

      public int startActivity(Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode, String callingPkg, int userId) throws RemoteException {
         return 0;
      }

      public int startActivityFromHistory(Intent intent) throws RemoteException {
         return 0;
      }

      public boolean finishActivityAffinity(int userId, IBinder token) throws RemoteException {
         return false;
      }

      public void onActivityCreated(IBinder record, IBinder token, int taskId) throws RemoteException {
      }

      public void onActivityResumed(int userId, IBinder token) throws RemoteException {
      }

      public boolean onActivityDestroyed(int userId, IBinder token) throws RemoteException {
         return false;
      }

      public void onActivityFinish(int userId, IBinder token) throws RemoteException {
      }

      public ComponentName getActivityClassForToken(int userId, IBinder token) throws RemoteException {
         return null;
      }

      public String getCallingPackage(int userId, IBinder token) throws RemoteException {
         return null;
      }

      public ComponentName getCallingActivity(int userId, IBinder token) throws RemoteException {
         return null;
      }

      public AppTaskInfo getTaskInfo(int taskId) throws RemoteException {
         return null;
      }

      public String getPackageForToken(int userId, IBinder token) throws RemoteException {
         return null;
      }

      public IBinder acquireProviderClient(int userId, ProviderInfo info) throws RemoteException {
         return null;
      }

      public boolean broadcastFinish(IBinder token) throws RemoteException {
         return false;
      }

      public void addOrUpdateIntentSender(IntentSenderData sender, int userId) throws RemoteException {
      }

      public void removeIntentSender(IBinder token) throws RemoteException {
      }

      public IntentSenderData getIntentSender(IBinder token) throws RemoteException {
         return null;
      }

      public void processRestarted(String packageName, String processName, int userId) throws RemoteException {
      }

      public void notifyBadgerChange(BadgerInfo info) throws RemoteException {
      }

      public void setAppInactive(String packageName, boolean idle, int userId) throws RemoteException {
      }

      public boolean isAppInactive(String packageName, int userId) throws RemoteException {
         return false;
      }

      public VParceledListSlice getServices(String pkg, int maxNum, int flags, int userId) throws RemoteException {
         return null;
      }

      public void handleDownloadCompleteIntent(Intent intent) throws RemoteException {
      }

      public int getAppPid(String packageName, int userId, String proccessName) throws RemoteException {
         return 0;
      }

      public void setSettingsProvider(int userId, int tableIndex, String arg, String value) throws RemoteException {
      }

      public String getSettingsProvider(int userId, int tableIndex, String arg) throws RemoteException {
         return null;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
