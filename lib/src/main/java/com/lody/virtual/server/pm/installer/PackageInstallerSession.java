package com.lody.virtual.server.pm.installer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.Checksum;
import android.content.pm.DataLoaderParamsParcel;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.IPackageInstallerSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import com.lody.virtual.server.pm.VAppManagerService;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@TargetApi(21)
public class PackageInstallerSession extends IPackageInstallerSession.Stub {
   public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;
   public static final int INSTALL_FAILED_ABORTED = -115;
   public static final int INSTALL_SUCCEEDED = 1;
   public static final int INSTALL_FAILED_INVALID_APK = -2;
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ihg+OWUzJC1iDAY2Iy42OW8zOCtsN1RF"));
   private static final String REMOVE_SPLIT_MARKER_EXTENSION = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz0MM2oVGj5iDgpF"));
   private static final int MSG_COMMIT = 0;
   private final VPackageInstallerService.InternalCallback mCallback;
   private final Context mContext;
   private final Handler mHandler;
   final int sessionId;
   final int userId;
   final int installerUid;
   final SessionParams params;
   final String installerPackageName;
   private boolean mPermissionsAccepted;
   final File stageDir;
   private final AtomicInteger mActiveCount = new AtomicInteger();
   private final Object mLock = new Object();
   private float mClientProgress = 0.0F;
   private float mInternalProgress = 0.0F;
   private float mProgress = 0.0F;
   private float mReportedProgress = -1.0F;
   private boolean mPrepared = false;
   private boolean mSealed = false;
   private boolean mDestroyed = false;
   private int mFinalStatus;
   private String mFinalMessage;
   private IPackageInstallObserver2 mRemoteObserver;
   private ArrayList<FileBridge> mBridges = new ArrayList();
   private File mResolvedStageDir;
   private String mPackageName;
   private File mResolvedBaseFile;
   private final List<File> mResolvedStagedFiles = new ArrayList();
   private final Handler.Callback mHandlerCallback = new Handler.Callback() {
      public boolean handleMessage(Message msg) {
         synchronized(PackageInstallerSession.this.mLock) {
            if (msg.obj != null) {
               PackageInstallerSession.this.mRemoteObserver = (IPackageInstallObserver2)msg.obj;
            }

            try {
               PackageInstallerSession.this.commitLocked();
            } catch (PackageManagerException var6) {
               PackageManagerException e = var6;
               String completeMsg = PackageInstallerSession.getCompleteMessage(e);
               VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ihg+OWUzJC1iDAY2Iy42OW8zOCtsN1RF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ADWoVAgZLHh4+PxgqPWoKAi9lJx0r")) + PackageInstallerSession.this.sessionId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgiP2UVHitiVgU8")) + completeMsg);
               PackageInstallerSession.this.destroyInternal();
               PackageInstallerSession.this.dispatchSessionFinished(e.error, completeMsg, (Bundle)null);
            }

            return true;
         }
      }
   };

   public PackageInstallerSession(VPackageInstallerService.InternalCallback callback, Context context, Looper looper, String installerPackageName, int sessionId, int userId, int installerUid, SessionParams params, File stageDir) {
      this.mCallback = callback;
      this.mContext = context;
      this.mHandler = new Handler(looper, this.mHandlerCallback);
      this.installerPackageName = installerPackageName;
      this.sessionId = sessionId;
      this.userId = userId;
      this.installerUid = installerUid;
      this.mPackageName = params.appPackageName;
      this.params = params;
      this.stageDir = stageDir;
   }

   public SessionInfo generateInfo() {
      SessionInfo info = new SessionInfo();
      synchronized(this.mLock) {
         info.sessionId = this.sessionId;
         info.installerPackageName = this.installerPackageName;
         info.resolvedBaseCodePath = this.mResolvedBaseFile != null ? this.mResolvedBaseFile.getAbsolutePath() : null;
         info.progress = this.mProgress;
         info.sealed = this.mSealed;
         info.active = this.mActiveCount.get() > 0;
         info.mode = this.params.mode;
         info.sizeBytes = this.params.sizeBytes;
         info.appPackageName = this.params.appPackageName;
         info.appIcon = this.params.appIcon;
         info.appLabel = this.params.appLabel;
         return info;
      }
   }

   private void commitLocked() throws PackageManagerException {
      if (this.mDestroyed) {
         throw new PackageManagerException(-110, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKW8zAiVgMCQwKAgqLmoVND9rASxF")));
      } else if (!this.mSealed) {
         throw new PackageManagerException(-110, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKW8zAiVgMCQ2Ki41OmoFGjdlESgv")));
      } else {
         try {
            this.resolveStageDir();
         } catch (IOException var7) {
            IOException e = var7;
            e.printStackTrace();
         }

         this.validateInstallLocked();
         this.mInternalProgress = 0.5F;
         this.computeProgressLocked(true);
         boolean success = false;
         File[] var2 = this.stageDir.listFiles();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            File file = var2[var4];
            VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4AI2ojMyh9ASQxPxccDn4wAgZoASAgPQgqI2E3GSM=")) + file.getPath());
            VAppInstallerResult res = VAppManagerService.get().installPackage(Uri.fromFile(file), new VAppInstallerParams());
            if (res.status == 0) {
               success = true;
            }
         }

         this.destroyInternal();
         int returnCode = success ? 1 : -115;
         this.dispatchSessionFinished(returnCode, (String)null, (Bundle)null);
      }
   }

   private void validateInstallLocked() throws PackageManagerException {
      this.mResolvedBaseFile = null;
      this.mResolvedStagedFiles.clear();
      File[] addedFiles = this.mResolvedStageDir.listFiles();
      if (addedFiles != null && addedFiles.length != 0) {
         int i = 0;
         File[] var3 = addedFiles;
         int var4 = addedFiles.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File addedFile = var3[var5];
            if (!addedFile.isDirectory()) {
               String targetName = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+KWgYGlo=")) + i + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4+KGUzSFo="));
               File targetFile = new File(this.mResolvedStageDir, targetName);
               if (!addedFile.equals(targetFile)) {
                  addedFile.renameTo(targetFile);
               }

               this.mResolvedBaseFile = targetFile;
               this.mResolvedStagedFiles.add(targetFile);
               ++i;
            }
         }

         if (this.mResolvedBaseFile == null) {
            throw new PackageManagerException(-2, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT0uDmoJIC9gNyggLwdbCH4zPAVsJC8rIxgcJWAVLC9uCiAqOD5fO2wjNzRvATg2JS0mLm4FSFo=")));
         }
      } else {
         throw new PackageManagerException(-2, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOG8FJCljJCA9KAgpOmoKBjdrJygv")));
      }
   }

   public void setClientProgress(float progress) throws RemoteException {
      synchronized(this.mLock) {
         boolean forcePublish = this.mClientProgress == 0.0F;
         this.mClientProgress = progress;
         this.computeProgressLocked(forcePublish);
      }
   }

   private static float constrain(float amount, float low, float high) {
      return amount < low ? low : (amount > high ? high : amount);
   }

   private void computeProgressLocked(boolean forcePublish) {
      this.mProgress = constrain(this.mClientProgress * 0.8F, 0.0F, 0.8F) + constrain(this.mInternalProgress * 0.2F, 0.0F, 0.2F);
      if (forcePublish || (double)Math.abs(this.mProgress - this.mReportedProgress) >= 0.01) {
         this.mReportedProgress = this.mProgress;
         this.mCallback.onSessionProgressChanged(this, this.mProgress);
      }

   }

   public void addClientProgress(float progress) throws RemoteException {
      synchronized(this.mLock) {
         this.setClientProgress(this.mClientProgress + progress);
      }
   }

   public String[] getNames() throws RemoteException {
      this.assertPreparedAndNotSealed(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjJCNiAShF")));

      try {
         return this.resolveStageDir().list();
      } catch (IOException var2) {
         IOException e = var2;
         throw new IllegalStateException(e);
      }
   }

   private File resolveStageDir() throws IOException {
      synchronized(this.mLock) {
         if (this.mResolvedStageDir == null && this.stageDir != null) {
            this.mResolvedStageDir = this.stageDir;
            if (!this.stageDir.exists()) {
               this.stageDir.mkdirs();
            }
         }

         return this.mResolvedStageDir;
      }
   }

   public ParcelFileDescriptor openWrite(String name, long offsetBytes, long lengthBytes) throws RemoteException {
      try {
         return this.openWriteInternal(name, offsetBytes, lengthBytes);
      } catch (IOException var7) {
         IOException e = var7;
         throw new IllegalStateException(e);
      }
   }

   private void assertPreparedAndNotSealed(String cookie) {
      synchronized(this.mLock) {
         if (!this.mPrepared) {
            throw new IllegalStateException(cookie + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgMM2gjGgRiCiQsIz0MKm4gRStrEVRF")));
         } else if (this.mSealed) {
            throw new SecurityException(cookie + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgcD2wJIDdgHlE1LC0MPn4zQS5vESg5PQg2KWAKQSxqEVRF")));
         }
      }
   }

   private ParcelFileDescriptor openWriteInternal(String name, long offsetBytes, long lengthBytes) throws IOException {
      FileBridge bridge;
      synchronized(this.mLock) {
         this.assertPreparedAndNotSealed(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy06M2omPARjAQo/")));
         bridge = new FileBridge();
         this.mBridges.add(bridge);
      }

      try {
         File target = new File(this.resolveStageDir(), name);
         FileDescriptor targetFd = Os.open(target.getAbsolutePath(), OsConstants.O_CREAT | OsConstants.O_WRONLY, 420);
         if (lengthBytes > 0L) {
            Os.posix_fallocate(targetFd, 0L, lengthBytes);
         }

         if (offsetBytes > 0L) {
            Os.lseek(targetFd, offsetBytes, OsConstants.SEEK_SET);
         }

         bridge.setTargetFile(targetFd);
         bridge.start();
         return ParcelFileDescriptor.dup(bridge.getClientSocket());
      } catch (ErrnoException var9) {
         ErrnoException e = var9;
         throw new IOException(e);
      }
   }

   public ParcelFileDescriptor openRead(String name) throws RemoteException {
      try {
         return this.openReadInternal(name);
      } catch (IOException var3) {
         IOException e = var3;
         throw new IllegalStateException(e);
      }
   }

   private ParcelFileDescriptor openReadInternal(String name) throws IOException {
      this.assertPreparedAndNotSealed(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy06M2omFit9DgpF")));

      try {
         if (!FileUtils.isValidExtFilename(name)) {
            throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLmsVHi9iVyQ2LwdXPXhSTVo=")) + name);
         } else {
            File target = new File(this.resolveStageDir(), name);
            FileDescriptor targetFd = Os.open(target.getAbsolutePath(), OsConstants.O_RDONLY, 0);
            return ParcelFileDescriptor.dup(targetFd);
         }
      } catch (ErrnoException var4) {
         ErrnoException e = var4;
         throw new IOException(e);
      }
   }

   public void removeSplit(String splitName) throws RemoteException {
      if (TextUtils.isEmpty(this.params.appPackageName)) {
         throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwcuKWwJIANhHjA5KQc+IX4wTTdoJ10sLj4tJGAwPChuCiA9KQRXCGsFEgNsJys3OwMiOmUaQT1lN1RF")));
      } else {
         try {
            this.createRemoveSplitMarker(splitName);
         } catch (IOException var3) {
            IOException e = var3;
            throw new IllegalStateException(e);
         }
      }
   }

   private void createRemoveSplitMarker(String splitName) throws IOException {
      try {
         String markerName = splitName + REMOVE_SPLIT_MARKER_EXTENSION;
         if (!FileUtils.isValidExtFilename(markerName)) {
            throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLmsVHi9iVyQ3LwguCWkgRDJ4EVRF")) + markerName);
         } else {
            File target = new File(this.resolveStageDir(), markerName);
            target.createNewFile();
            Os.chmod(target.getAbsolutePath(), 0);
         }
      } catch (ErrnoException var4) {
         ErrnoException e = var4;
         throw new IOException(e);
      }
   }

   public void close() throws RemoteException {
      if (this.mActiveCount.decrementAndGet() == 0) {
         this.mCallback.onSessionActiveChanged(this, false);
      }

   }

   @TargetApi(26)
   public void commit(IntentSender statusReceiver, boolean forTransfer) throws RemoteException {
      this.commit(statusReceiver);
   }

   public void commit(IntentSender statusReceiver) throws RemoteException {
      boolean wasSealed;
      synchronized(this.mLock) {
         wasSealed = this.mSealed;
         if (!this.mSealed) {
            Iterator var4 = this.mBridges.iterator();

            while(var4.hasNext()) {
               FileBridge bridge = (FileBridge)var4.next();
               if (!bridge.isClosed()) {
                  throw new SecurityException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4YDmgaLyhhJwozKhdaOm8KTStlN1RF")));
               }
            }

            this.mSealed = true;
         }

         this.mClientProgress = 1.0F;
         this.computeProgressLocked(true);
      }

      if (!wasSealed) {
         this.mCallback.onSessionSealedBlocking(this);
      }

      this.mActiveCount.incrementAndGet();
      VPackageInstallerService.PackageInstallObserverAdapter adapter = new VPackageInstallerService.PackageInstallObserverAdapter(this.mContext, statusReceiver, this.sessionId, this.userId);
      this.mHandler.obtainMessage(0, adapter.getBinder()).sendToTarget();
   }

   public void abandon() throws RemoteException {
      this.destroyInternal();
      this.dispatchSessionFinished(-115, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKW8zAiVgMCQtLwgpOm4jRTdlNywcLC4uIA==")), (Bundle)null);
   }

   public boolean isMultiPackage() throws RemoteException {
      return false;
   }

   public DataLoaderParamsParcel getDataLoaderParams() throws RemoteException {
      return null;
   }

   public void setChecksums(String name, Checksum[] checksums, byte[] signature) {
   }

   private void destroyInternal() {
      synchronized(this.mLock) {
         this.mSealed = true;
         this.mDestroyed = true;
         Iterator var2 = this.mBridges.iterator();

         while(true) {
            if (!var2.hasNext()) {
               break;
            }

            FileBridge bridge = (FileBridge)var2.next();
            bridge.forceClose();
         }
      }

      if (this.stageDir != null) {
         FileUtils.deleteDir(this.stageDir.getAbsolutePath());
      }

   }

   private void dispatchSessionFinished(int returnCode, String msg, Bundle extras) {
      this.mFinalStatus = returnCode;
      this.mFinalMessage = msg;
      if (this.mRemoteObserver != null) {
         try {
            this.mRemoteObserver.onPackageInstalled(this.mPackageName, returnCode, msg, extras);
         } catch (RemoteException var5) {
         }
      }

      boolean success = returnCode == 1;
      this.mCallback.onSessionFinished(this, success);
   }

   void setPermissionsResult(boolean accepted) {
      if (!this.mSealed) {
         throw new SecurityException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwcuKWwJICpiCiQpKAciCGkjAShvERkrLRg2JWIFOD95ESAuLBgmI2wgLAVqNxoc")));
      } else {
         if (accepted) {
            synchronized(this.mLock) {
               this.mPermissionsAccepted = true;
            }

            this.mHandler.obtainMessage(0).sendToTarget();
         } else {
            this.destroyInternal();
            this.dispatchSessionFinished(-115, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28nIARiDgI/Ly42PWk3TQJrDgoeIxc2D2MKAillJ1RF")), (Bundle)null);
         }

      }
   }

   public void open() throws IOException {
      if (this.mActiveCount.getAndIncrement() == 0) {
         this.mCallback.onSessionActiveChanged(this, true);
      }

      synchronized(this.mLock) {
         if (!this.mPrepared) {
            if (this.stageDir == null) {
               throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQdfP2swMCRnCiQ1Kj0LOm8FGShsJCwsLj4uGmMFMyNsJyspLAccO2sjNB9lES83Jy4MKG4gETZvJAYcKV86OmhSIANiAQpF")));
            }

            this.mPrepared = true;
            this.mCallback.onSessionPrepared(this);
         }

      }
   }

   public static String getCompleteMessage(Throwable t) {
      StringBuilder builder = new StringBuilder();
      builder.append(t.getMessage());

      while((t = t.getCause()) != null) {
         builder.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ODo6Vg=="))).append(t.getMessage());
      }

      return builder.toString();
   }

   private class PackageManagerException extends Exception {
      public final int error;

      PackageManagerException(int error, String detailMessage) {
         super(detailMessage);
         this.error = error;
      }
   }
}
