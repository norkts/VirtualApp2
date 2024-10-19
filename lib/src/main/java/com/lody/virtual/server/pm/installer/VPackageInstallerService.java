package com.lody.virtual.server.pm.installer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.IPackageInstallerCallback;
import android.content.pm.IPackageInstallerSession;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.SparseArray;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.ObjectsCompat;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.os.VBinder;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.VParceledListSlice;
import com.lody.virtual.server.IPackageInstaller;
import com.lody.virtual.server.pm.VAppManagerService;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@TargetApi(21)
public class VPackageInstallerService extends IPackageInstaller.Stub {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ihg+OWUzJC1iDAY2Iy42OW8zOCtsN1RF"));
   private static final long MAX_ACTIVE_SESSIONS = 1024L;
   private static final Singleton<VPackageInstallerService> gDefault = new Singleton<VPackageInstallerService>() {
      protected VPackageInstallerService create() {
         return new VPackageInstallerService();
      }
   };
   private final Random mRandom;
   private final SparseArray<PackageInstallerSession> mSessions;
   private final Handler mInstallHandler;
   private final Callbacks mCallbacks;
   private final HandlerThread mInstallThread;
   private final InternalCallback mInternalCallback;
   private Context mContext;

   private VPackageInstallerService() {
      this.mRandom = new SecureRandom();
      this.mSessions = new SparseArray();
      this.mInternalCallback = new InternalCallback();
      this.mContext = VirtualCore.get().getContext();
      this.mInstallThread = new HandlerThread(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ihg+OWUzJC1iDAY2Iy42OW8zOCtsN1RF")));
      this.mInstallThread.start();
      this.mInstallHandler = new Handler(this.mInstallThread.getLooper());
      this.mCallbacks = new Callbacks(this.mInstallThread.getLooper());
   }

   public static VPackageInstallerService get() {
      return (VPackageInstallerService)gDefault.get();
   }

   private static int getSessionCount(SparseArray<PackageInstallerSession> sessions, int installerUid) {
      int count = 0;
      int size = sessions.size();

      for(int i = 0; i < size; ++i) {
         PackageInstallerSession session = (PackageInstallerSession)sessions.valueAt(i);
         if (session.installerUid == installerUid) {
            ++count;
         }
      }

      return count;
   }

   public int createSession(SessionParams params, String installerPackageName, int userId) {
      try {
         return this.createSessionInternal(params, installerPackageName, userId, VBinder.getCallingUid());
      } catch (IOException var5) {
         IOException e = var5;
         throw new IllegalStateException(e);
      }
   }

   private int createSessionInternal(SessionParams params, String installerPackageName, int userId, int callingUid) throws IOException {
      int sessionId;
      PackageInstallerSession session;
      synchronized(this.mSessions) {
         int activeCount = getSessionCount(this.mSessions, callingUid);
         if ((long)activeCount >= 1024L) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRgAD3sFEjdgNxk8LwcqLmwgHit4HjAgKT02I2AgRTZ5HjgeLF9XXGILMzQ=")) + callingUid);
         }

         sessionId = this.allocateSessionIdLocked();
         session = new PackageInstallerSession(this.mInternalCallback, this.mContext, this.mInstallHandler.getLooper(), installerPackageName, sessionId, userId, callingUid, params, VEnvironment.getPackageInstallerStageDir());
      }

      synchronized(this.mSessions) {
         this.mSessions.put(sessionId, session);
      }

      this.mCallbacks.notifySessionCreated(session.sessionId, session.userId);
      return sessionId;
   }

   public void updateSessionAppIcon(int sessionId, Bitmap appIcon) {
      synchronized(this.mSessions) {
         PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
         if (session != null && this.isCallingUidOwner(session)) {
            session.params.appIcon = appIcon;
            session.params.appIconLastModified = -1L;
            this.mInternalCallback.onSessionBadgingChanged(session);
         } else {
            throw new SecurityException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4+DmoFNARLHho7IykmDm8JTTdoJzAgKT01JGYaDSNlJAo8LAg2KWU3IFo=")) + sessionId);
         }
      }
   }

   public void updateSessionAppLabel(int sessionId, String appLabel) {
      synchronized(this.mSessions) {
         PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
         if (session != null && this.isCallingUidOwner(session)) {
            session.params.appLabel = appLabel;
            this.mInternalCallback.onSessionBadgingChanged(session);
         } else {
            throw new SecurityException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4+DmoFNARLHho7IykmDm8JTTdoJzAgKT01JGYaDSNlJAo8LAg2KWU3IFo=")) + sessionId);
         }
      }
   }

   public void abandonSession(int sessionId) {
      synchronized(this.mSessions) {
         PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
         if (session != null && this.isCallingUidOwner(session)) {
            try {
               session.abandon();
            } catch (RemoteException var6) {
               RemoteException e = var6;
               e.printStackTrace();
            }

         } else {
            throw new SecurityException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4+DmoFNARLHho7IykmDm8JTTdoJzAgKT01JGYaDSNlJAo8LAg2KWU3IFo=")) + sessionId);
         }
      }
   }

   public IPackageInstallerSession openSession(int sessionId) {
      try {
         return this.openSessionInternal(sessionId);
      } catch (IOException var3) {
         IOException e = var3;
         throw new IllegalStateException(e);
      }
   }

   private IPackageInstallerSession openSessionInternal(int sessionId) throws IOException {
      synchronized(this.mSessions) {
         PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
         if (session != null && this.isCallingUidOwner(session)) {
            session.open();
            return session;
         } else {
            throw new SecurityException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4+DmoFNARLHho7IykmDm8JTTdoJzAgKT01JGYaDSNlJAo8LAg2KWU3IFo=")) + sessionId);
         }
      }
   }

   public SessionInfo getSessionInfo(int sessionId) {
      synchronized(this.mSessions) {
         PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
         return session != null ? session.generateInfo() : null;
      }
   }

   public VParceledListSlice getAllSessions(int userId) {
      List<SessionInfo> result = new ArrayList();
      synchronized(this.mSessions) {
         for(int i = 0; i < this.mSessions.size(); ++i) {
            PackageInstallerSession session = (PackageInstallerSession)this.mSessions.valueAt(i);
            if (session.userId == userId) {
               result.add(session.generateInfo());
            }
         }

         return new VParceledListSlice(result);
      }
   }

   public VParceledListSlice getMySessions(String installerPackageName, int userId) {
      List<SessionInfo> result = new ArrayList();
      synchronized(this.mSessions) {
         for(int i = 0; i < this.mSessions.size(); ++i) {
            PackageInstallerSession session = (PackageInstallerSession)this.mSessions.valueAt(i);
            if (ObjectsCompat.equals(session.installerPackageName, installerPackageName) && session.userId == userId) {
               result.add(session.generateInfo());
            }
         }

         return new VParceledListSlice(result);
      }
   }

   public void registerCallback(IPackageInstallerCallback callback, int userId) {
      this.mCallbacks.register(callback, userId);
   }

   public void unregisterCallback(IPackageInstallerCallback callback) {
      this.mCallbacks.unregister(callback);
   }

   public void uninstall(String packageName, String callerPackageName, int flags, IntentSender statusReceiver, int userId) {
      boolean success = VAppManagerService.get().uninstallPackage(packageName);
      if (statusReceiver != null) {
         Intent fillIn = new Intent();
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSAAIAY+HWMhNBNiJTgSLAhSVg==")), packageName);
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSwTICscXGQjSFo=")), success ? 0 : 1);
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSwTICscXGQmGkhgHDBBIwU+Bg==")), PackageHelper.deleteStatusToString(success));
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8Ml1JJwZbH2YIGg9kDzhOKAYuVg==")), success ? 1 : -1);

         try {
            statusReceiver.sendIntent(this.mContext, 0, fillIn, (IntentSender.OnFinished)null, (Handler)null);
         } catch (IntentSender.SendIntentException var9) {
            IntentSender.SendIntentException e = var9;
            e.printStackTrace();
         }
      }

   }

   public void setPermissionsResult(int sessionId, boolean accepted) {
      synchronized(this.mSessions) {
         PackageInstallerSession session = (PackageInstallerSession)this.mSessions.get(sessionId);
         if (session != null) {
            session.setPermissionsResult(accepted);
         }

      }
   }

   private boolean isCallingUidOwner(PackageInstallerSession session) {
      return true;
   }

   private int allocateSessionIdLocked() {
      int n = 0;

      do {
         int sessionId = this.mRandom.nextInt(2147483646) + 1;
         if (this.mSessions.get(sessionId) == null) {
            return sessionId;
         }
      } while(n++ < 32);

      throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4+CWoFNCxLEQo1PxciCG8zNCloDiwgPQc2J2EjNCxsJBEpIiwcVg==")));
   }

   // $FF: synthetic method
   VPackageInstallerService(Object x0) {
      this();
   }

   class InternalCallback {
      public void onSessionBadgingChanged(PackageInstallerSession session) {
         VPackageInstallerService.this.mCallbacks.notifySessionBadgingChanged(session.sessionId, session.userId);
      }

      public void onSessionActiveChanged(PackageInstallerSession session, boolean active) {
         VPackageInstallerService.this.mCallbacks.notifySessionActiveChanged(session.sessionId, session.userId, active);
      }

      public void onSessionProgressChanged(PackageInstallerSession session, float progress) {
         VPackageInstallerService.this.mCallbacks.notifySessionProgressChanged(session.sessionId, session.userId, progress);
      }

      public void onSessionFinished(final PackageInstallerSession session, boolean success) {
         VPackageInstallerService.this.mCallbacks.notifySessionFinished(session.sessionId, session.userId, success);
         VPackageInstallerService.this.mInstallHandler.post(new Runnable() {
            public void run() {
               synchronized(VPackageInstallerService.this.mSessions) {
                  VPackageInstallerService.this.mSessions.remove(session.sessionId);
               }
            }
         });
      }

      public void onSessionPrepared(PackageInstallerSession session) {
      }

      public void onSessionSealedBlocking(PackageInstallerSession session) {
      }
   }

   static class PackageInstallObserverAdapter extends PackageInstallObserver {
      private final Context mContext;
      private final IntentSender mTarget;
      private final int mSessionId;
      private final int mUserId;

      PackageInstallObserverAdapter(Context context, IntentSender target, int sessionId, int userId) {
         this.mContext = context;
         this.mTarget = target;
         this.mSessionId = sessionId;
         this.mUserId = userId;
      }

      public void onUserActionRequired(Intent intent) {
         Intent fillIn = new Intent();
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSxJJAUYBX0hBhN9HyxF")), this.mSessionId);
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSwTICscXGQjSFo=")), -1);
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZrDlk/KS49KmsIRVRmDB4T")), intent);

         try {
            this.mTarget.sendIntent(this.mContext, 0, fillIn, (IntentSender.OnFinished)null, (Handler)null);
         } catch (IntentSender.SendIntentException var4) {
         }

      }

      public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) {
         Intent fillIn = new Intent();
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSAAIAY+HWMhNBNiJTgSLAhSVg==")), basePackageName);
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSxJJAUYBX0hBhN9HyxF")), this.mSessionId);
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSwTICscXGQjSFo=")), PackageHelper.installStatusToPublicStatus(returnCode));
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8NSwTICscXGQmGkhgHDBBIwU+Bg==")), PackageHelper.installStatusToString(returnCode, msg));
         fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8Ml1JJwZbH2YIGg9kDzhOKAYuVg==")), returnCode);
         if (extras != null) {
            String existing = extras.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8MjgAIiwiXGQxNBNgHFkWLywMGmQ2IBFiNV0MJCw+WmAVSFo=")));
            if (!TextUtils.isEmpty(existing)) {
               fillIn.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1NDwePC4uPGYVMCR8MlkTIjwAU2EmIB1jNV0OLCs2U2Q2OF5hJ1RF")), existing);
            }
         }

         try {
            this.mTarget.sendIntent(this.mContext, 0, fillIn, (IntentSender.OnFinished)null, (Handler)null);
         } catch (IntentSender.SendIntentException var7) {
         }

      }
   }

   private static class Callbacks extends Handler {
      private static final int MSG_SESSION_CREATED = 1;
      private static final int MSG_SESSION_BADGING_CHANGED = 2;
      private static final int MSG_SESSION_ACTIVE_CHANGED = 3;
      private static final int MSG_SESSION_PROGRESS_CHANGED = 4;
      private static final int MSG_SESSION_FINISHED = 5;
      private final RemoteCallbackList<IPackageInstallerCallback> mCallbacks = new RemoteCallbackList();

      public Callbacks(Looper looper) {
         super(looper);
      }

      public void register(IPackageInstallerCallback callback, int userId) {
         this.mCallbacks.register(callback, new VUserHandle(userId));
      }

      public void unregister(IPackageInstallerCallback callback) {
         this.mCallbacks.unregister(callback);
      }

      public void handleMessage(Message msg) {
         int userId = msg.arg2;
         int n = this.mCallbacks.beginBroadcast();

         for(int i = 0; i < n; ++i) {
            IPackageInstallerCallback callback = (IPackageInstallerCallback)this.mCallbacks.getBroadcastItem(i);
            VUserHandle user = (VUserHandle)this.mCallbacks.getBroadcastCookie(i);
            if (userId == user.getIdentifier()) {
               try {
                  this.invokeCallback(callback, msg);
               } catch (RemoteException var8) {
               }
            }
         }

         this.mCallbacks.finishBroadcast();
      }

      private void invokeCallback(IPackageInstallerCallback callback, Message msg) throws RemoteException {
         int sessionId = msg.arg1;
         switch (msg.what) {
            case 1:
               callback.onSessionCreated(sessionId);
               break;
            case 2:
               callback.onSessionBadgingChanged(sessionId);
               break;
            case 3:
               callback.onSessionActiveChanged(sessionId, (Boolean)msg.obj);
               break;
            case 4:
               callback.onSessionProgressChanged(sessionId, (Float)msg.obj);
               break;
            case 5:
               callback.onSessionFinished(sessionId, (Boolean)msg.obj);
         }

      }

      private void notifySessionCreated(int sessionId, int userId) {
         this.obtainMessage(1, sessionId, userId).sendToTarget();
      }

      private void notifySessionBadgingChanged(int sessionId, int userId) {
         this.obtainMessage(2, sessionId, userId).sendToTarget();
      }

      private void notifySessionActiveChanged(int sessionId, int userId, boolean active) {
         this.obtainMessage(3, sessionId, userId, active).sendToTarget();
      }

      private void notifySessionProgressChanged(int sessionId, int userId, float progress) {
         this.obtainMessage(4, sessionId, userId, progress).sendToTarget();
      }

      public void notifySessionFinished(int sessionId, int userId, boolean success) {
         this.obtainMessage(5, sessionId, userId, success).sendToTarget();
      }
   }
}
