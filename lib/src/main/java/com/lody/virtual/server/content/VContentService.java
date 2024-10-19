package com.lody.virtual.server.content;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ISyncStatusObserver;
import android.content.PeriodicSync;
import android.content.SyncAdapterType;
import android.content.SyncInfo;
import android.content.SyncRequest;
import android.content.SyncStatusInfo;
import android.database.IContentObserver;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VBinder;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.interfaces.IContentService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class VContentService extends IContentService.Stub {
   private static final String TAG = "ContentService";
   private static final VContentService sInstance = new VContentService();
   private Context mContext = VirtualCore.get().getContext();
   private final ObserverNode mRootNode = new ObserverNode("");
   private SyncManager mSyncManager = null;
   private final Object mSyncManagerLock = new Object();

   public static VContentService get() {
      return sInstance;
   }

   private SyncManager getSyncManager() {
      synchronized(this.mSyncManagerLock) {
         try {
            if (this.mSyncManager == null) {
               this.mSyncManager = new SyncManager(this.mContext);
            }
         } catch (SQLiteException var4) {
            SQLiteException e = var4;
            Log.e(TAG, "Can\'t create SyncManager", e);
         }

         return this.mSyncManager;
      }
   }

   public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
      try {
         return super.onTransact(code, data, reply, flags);
      } catch (RuntimeException var6) {
         RuntimeException e = var6;
         if (!(e instanceof SecurityException)) {
            e.printStackTrace();
         }

         throw e;
      }
   }

   private VContentService() {
   }

   public static void systemReady() {
      get().getSyncManager();
   }

   public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int VUserHandle) {
      if (observer != null && uri != null) {
         synchronized(this.mRootNode) {
            this.mRootNode.addObserverLocked(uri, observer, notifyForDescendants, this.mRootNode, VBinder.getCallingUid(), VBinder.getCallingPid(), VUserHandle);
         }
      } else {
         throw new IllegalArgumentException("You must pass a valid uri and observer");
      }
   }

   public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer) {
      this.registerContentObserver(uri, notifyForDescendants, observer, VUserHandle.getCallingUserId());
   }

   public void unregisterContentObserver(IContentObserver observer) {
      if (observer == null) {
         throw new IllegalArgumentException("You must pass a valid observer");
      } else {
         synchronized(this.mRootNode) {
            this.mRootNode.removeObserverLocked(observer);
         }
      }
   }

   public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, boolean syncToNetwork, int VUserHandle) {
      if (Log.isLoggable(TAG, 2)) {
         Log.v(TAG, "Notifying update of " + uri + " for user " + VUserHandle + " from observer " + observer + ", syncToNetwork " + syncToNetwork);
      }

      int uid = VBinder.getCallingUid();
      long identityToken = clearCallingIdentity();

      try {
         ArrayList<ObserverCall> calls = new ArrayList();
         synchronized(this.mRootNode) {
            this.mRootNode.collectObserversLocked(uri, 0, observer, observerWantsSelfNotifications, VUserHandle, calls);
         }

         int numCalls = calls.size();

         for(int i = 0; i < numCalls; ++i) {
            ObserverCall oc = (ObserverCall)calls.get(i);

            try {
               oc.mObserver.onChange(oc.mSelfChange, uri, VUserHandle);
               if (Log.isLoggable(TAG, 2)) {
                  Log.v(TAG, "Notified " + oc.mObserver + " of update at " + uri);
               }
            } catch (RemoteException var28) {
               synchronized(this.mRootNode) {
                  Log.w(TAG, "Found dead observer, removing");
                  IBinder binder = oc.mObserver.asBinder();
                  ArrayList<ObserverNode.ObserverEntry> list = oc.mNode.mObservers;
                  int numList = list.size();

                  for(int j = 0; j < numList; ++j) {
                     ObserverNode.ObserverEntry oe = (ObserverNode.ObserverEntry)list.get(j);
                     if (oe.observer.asBinder() == binder) {
                        list.remove(j);
                        --j;
                        --numList;
                     }
                  }
               }
            }
         }

         if (syncToNetwork) {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
               syncManager.scheduleLocalSync((Account)null, VUserHandle, uid, uri.getAuthority());
            }
         }
      } finally {
         restoreCallingIdentity(identityToken);
      }

   }

   public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, boolean syncToNetwork) {
      this.notifyChange(uri, observer, observerWantsSelfNotifications, syncToNetwork, VUserHandle.getCallingUserId());
   }

   public void requestSync(Account account, String authority, Bundle extras) {
      ContentResolver.validateSyncExtrasBundle(extras);
      int userId = VUserHandle.getCallingUserId();
      int uId = VBinder.getCallingUid();
      long identityToken = clearCallingIdentity();

      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager != null) {
            syncManager.scheduleSync(account, userId, uId, authority, extras, 0L, 0L, false);
         }
      } finally {
         restoreCallingIdentity(identityToken);
      }

   }

   public void sync(SyncRequest request) {
      Bundle extras = (Bundle)mirror.android.content.SyncRequest.mExtras.get(request);
      long flextime = mirror.android.content.SyncRequest.mSyncFlexTimeSecs.get(request);
      long runAtTime = mirror.android.content.SyncRequest.mSyncRunTimeSecs.get(request);
      int userId = VUserHandle.getCallingUserId();
      int uId = VBinder.getCallingUid();
      long identityToken = clearCallingIdentity();

      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager != null) {
            Account account = (Account)mirror.android.content.SyncRequest.mAccountToSync.get(request);
            String provider = (String)mirror.android.content.SyncRequest.mAuthority.get(request);
            if (mirror.android.content.SyncRequest.mIsPeriodic.get(request)) {
               if (runAtTime < 60L) {
                  VLog.w(TAG, "Requested poll frequency of " + runAtTime + " seconds being rounded up to 60 seconds.");
                  runAtTime = 60L;
               }

               PeriodicSync syncToAdd = new PeriodicSync(account, provider, extras, runAtTime);
               mirror.android.content.PeriodicSync.flexTime.set(syncToAdd, flextime);
               this.getSyncManager().getSyncStorageEngine().addPeriodicSync(syncToAdd, userId);
            } else {
               long beforeRuntimeMillis = flextime * 1000L;
               long runtimeMillis = runAtTime * 1000L;
               syncManager.scheduleSync(account, userId, uId, provider, extras, beforeRuntimeMillis, runtimeMillis, false);
            }
         }
      } finally {
         restoreCallingIdentity(identityToken);
      }

   }

   public void cancelSync(Account account, String authority) {
      if (authority != null && authority.length() == 0) {
         throw new IllegalArgumentException("Authority must be non-empty");
      } else {
         int userId = VUserHandle.getCallingUserId();
         long identityToken = clearCallingIdentity();

         try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
               syncManager.clearScheduledSyncOperations(account, userId, authority);
               syncManager.cancelActiveSync(account, userId, authority);
            }
         } finally {
            restoreCallingIdentity(identityToken);
         }

      }
   }

   public SyncAdapterType[] getSyncAdapterTypes() {
      int userId = VUserHandle.getCallingUserId();
      long identityToken = clearCallingIdentity();

      SyncAdapterType[] var5;
      try {
         SyncManager syncManager = this.getSyncManager();
         var5 = syncManager.getSyncAdapterTypes();
      } finally {
         restoreCallingIdentity(identityToken);
      }

      return var5;
   }

   public boolean getSyncAutomatically(Account account, String providerName) {
      int userId = VUserHandle.getCallingUserId();
      long identityToken = clearCallingIdentity();

      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager != null) {
            boolean var7 = syncManager.getSyncStorageEngine().getSyncAutomatically(account, userId, providerName);
            return var7;
         }
      } finally {
         restoreCallingIdentity(identityToken);
      }

      return false;
   }

   public void setSyncAutomatically(Account account, String providerName, boolean sync) {
      if (TextUtils.isEmpty(providerName)) {
         throw new IllegalArgumentException("Authority must be non-empty");
      } else {
         int userId = VUserHandle.getCallingUserId();
         long identityToken = clearCallingIdentity();

         try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
               syncManager.getSyncStorageEngine().setSyncAutomatically(account, userId, providerName, sync);
            }
         } finally {
            restoreCallingIdentity(identityToken);
         }

      }
   }

   public void addPeriodicSync(Account account, String authority, Bundle extras, long pollFrequency) {
      if (account == null) {
         throw new IllegalArgumentException("Account must not be null");
      } else if (TextUtils.isEmpty(authority)) {
         throw new IllegalArgumentException("Authority must not be empty.");
      } else {
         int userId = VUserHandle.getCallingUserId();
         if (pollFrequency < 60L) {
            VLog.w(TAG, "Requested poll frequency of " + pollFrequency + " seconds being rounded up to 60 seconds.");
            pollFrequency = 60L;
         }

         long identityToken = clearCallingIdentity();

         try {
            PeriodicSync syncToAdd = new PeriodicSync(account, authority, extras, pollFrequency);
            mirror.android.content.PeriodicSync.flexTime.set(syncToAdd, SyncStorageEngine.calculateDefaultFlexTime(pollFrequency));
            this.getSyncManager().getSyncStorageEngine().addPeriodicSync(syncToAdd, userId);
         } finally {
            restoreCallingIdentity(identityToken);
         }

      }
   }

   public void removePeriodicSync(Account account, String authority, Bundle extras) {
      if (account == null) {
         throw new IllegalArgumentException("Account must not be null");
      } else if (TextUtils.isEmpty(authority)) {
         throw new IllegalArgumentException("Authority must not be empty");
      } else {
         int userId = VUserHandle.getCallingUserId();
         long identityToken = clearCallingIdentity();

         try {
            PeriodicSync syncToRemove = new PeriodicSync(account, authority, extras, 0L);
            this.getSyncManager().getSyncStorageEngine().removePeriodicSync(syncToRemove, userId);
         } finally {
            restoreCallingIdentity(identityToken);
         }

      }
   }

   public List<PeriodicSync> getPeriodicSyncs(Account account, String providerName) {
      if (account == null) {
         throw new IllegalArgumentException("Account must not be null");
      } else if (TextUtils.isEmpty(providerName)) {
         throw new IllegalArgumentException("Authority must not be empty");
      } else {
         int userId = VUserHandle.getCallingUserId();
         long identityToken = clearCallingIdentity();

         List var6;
         try {
            var6 = this.getSyncManager().getSyncStorageEngine().getPeriodicSyncs(account, userId, providerName);
         } finally {
            restoreCallingIdentity(identityToken);
         }

         return var6;
      }
   }

   public int getIsSyncable(Account account, String providerName) {
      int userId = VUserHandle.getCallingUserId();
      long identityToken = clearCallingIdentity();

      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager != null) {
            int var7 = syncManager.getIsSyncable(account, userId, providerName);
            return var7;
         }
      } finally {
         restoreCallingIdentity(identityToken);
      }

      return -1;
   }

   public void setIsSyncable(Account account, String providerName, int syncable) {
      if (TextUtils.isEmpty(providerName)) {
         throw new IllegalArgumentException("Authority must not be empty");
      } else {
         int userId = VUserHandle.getCallingUserId();
         long identityToken = clearCallingIdentity();

         try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
               syncManager.getSyncStorageEngine().setIsSyncable(account, userId, providerName, syncable);
            }
         } finally {
            restoreCallingIdentity(identityToken);
         }

      }
   }

   public boolean getMasterSyncAutomatically() {
      int userId = VUserHandle.getCallingUserId();
      long identityToken = clearCallingIdentity();

      boolean var5;
      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager == null) {
            return false;
         }

         var5 = syncManager.getSyncStorageEngine().getMasterSyncAutomatically(userId);
      } finally {
         restoreCallingIdentity(identityToken);
      }

      return var5;
   }

   public void setMasterSyncAutomatically(boolean flag) {
      int userId = VUserHandle.getCallingUserId();
      long identityToken = clearCallingIdentity();

      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager != null) {
            syncManager.getSyncStorageEngine().setMasterSyncAutomatically(flag, userId);
         }
      } finally {
         restoreCallingIdentity(identityToken);
      }

   }

   public boolean isSyncActive(Account account, String authority) {
      int userId = VUserHandle.getCallingUserId();
      long identityToken = clearCallingIdentity();

      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager != null) {
            boolean var7 = syncManager.getSyncStorageEngine().isSyncActive(account, userId, authority);
            return var7;
         }
      } finally {
         restoreCallingIdentity(identityToken);
      }

      return false;
   }

   public List<SyncInfo> getCurrentSyncs() {
      int userId = VUserHandle.getCallingUserId();
      long identityToken = clearCallingIdentity();

      try {
         List<VSyncInfo> vList = this.getSyncManager().getSyncStorageEngine().getCurrentSyncsCopy(userId);
         List<SyncInfo> list = new ArrayList(vList.size());
         Iterator var6 = vList.iterator();

         while(var6.hasNext()) {
            VSyncInfo info = (VSyncInfo)var6.next();
            list.add(info.toSyncInfo());
         }

         return list;
      } finally {
         restoreCallingIdentity(identityToken);
      }
   }

   public SyncStatusInfo getSyncStatus(Account account, String authority) {
      if (TextUtils.isEmpty(authority)) {
         throw new IllegalArgumentException("Authority must not be empty");
      } else {
         int userId = VUserHandle.getCallingUserId();
         long identityToken = clearCallingIdentity();

         try {
            SyncManager syncManager = this.getSyncManager();
            if (syncManager != null) {
               SyncStatusInfo var7 = syncManager.getSyncStorageEngine().getStatusByAccountAndAuthority(account, userId, authority);
               return var7;
            }
         } finally {
            restoreCallingIdentity(identityToken);
         }

         return null;
      }
   }

   public boolean isSyncPending(Account account, String authority) {
      int userId = VUserHandle.getCallingUserId();
      long identityToken = clearCallingIdentity();

      boolean var7;
      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager == null) {
            return false;
         }

         var7 = syncManager.getSyncStorageEngine().isSyncPending(account, userId, authority);
      } finally {
         restoreCallingIdentity(identityToken);
      }

      return var7;
   }

   public void addStatusChangeListener(int mask, ISyncStatusObserver callback) {
      long identityToken = clearCallingIdentity();

      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager != null && callback != null) {
            syncManager.getSyncStorageEngine().addStatusChangeListener(mask, callback);
         }
      } finally {
         restoreCallingIdentity(identityToken);
      }

   }

   public void removeStatusChangeListener(ISyncStatusObserver callback) {
      long identityToken = clearCallingIdentity();

      try {
         SyncManager syncManager = this.getSyncManager();
         if (syncManager != null && callback != null) {
            syncManager.getSyncStorageEngine().removeStatusChangeListener(callback);
         }
      } finally {
         restoreCallingIdentity(identityToken);
      }

   }

   public static final class ObserverNode {
      public static final int INSERT_TYPE = 0;
      public static final int UPDATE_TYPE = 1;
      public static final int DELETE_TYPE = 2;
      private String mName;
      private ArrayList<ObserverNode> mChildren = new ArrayList();
      private ArrayList<ObserverEntry> mObservers = new ArrayList();

      public ObserverNode(String name) {
         this.mName = name;
      }

      private String getUriSegment(Uri uri, int index) {
         if (uri != null) {
            return index == 0 ? uri.getAuthority() : (String)uri.getPathSegments().get(index - 1);
         } else {
            return null;
         }
      }

      private int countUriSegments(Uri uri) {
         return uri == null ? 0 : uri.getPathSegments().size() + 1;
      }

      public void addObserverLocked(Uri uri, IContentObserver observer, boolean notifyForDescendants, Object observersLock, int uid, int pid, int VUserHandle) {
         this.addObserverLocked(uri, 0, observer, notifyForDescendants, observersLock, uid, pid, VUserHandle);
      }

      private void addObserverLocked(Uri uri, int index, IContentObserver observer, boolean notifyForDescendants, Object observersLock, int uid, int pid, int VUserHandle) {
         if (index == this.countUriSegments(uri)) {
            this.mObservers.add(new ObserverEntry(observer, notifyForDescendants, observersLock, uid, pid, VUserHandle));
         } else {
            String segment = this.getUriSegment(uri, index);
            if (segment == null) {
               throw new IllegalArgumentException("Invalid Uri (" + uri + ") used for observer");
            } else {
               int N = this.mChildren.size();

               for(int i = 0; i < N; ++i) {
                  ObserverNode node = (ObserverNode)this.mChildren.get(i);
                  if (node.mName.equals(segment)) {
                     node.addObserverLocked(uri, index + 1, observer, notifyForDescendants, observersLock, uid, pid, VUserHandle);
                     return;
                  }
               }

               ObserverNode node = new ObserverNode(segment);
               this.mChildren.add(node);
               node.addObserverLocked(uri, index + 1, observer, notifyForDescendants, observersLock, uid, pid, VUserHandle);
            }
         }
      }

      public boolean removeObserverLocked(IContentObserver observer) {
         int size = this.mChildren.size();

         for(int i = 0; i < size; ++i) {
            boolean empty = ((ObserverNode)this.mChildren.get(i)).removeObserverLocked(observer);
            if (empty) {
               this.mChildren.remove(i);
               --i;
               --size;
            }
         }

         IBinder observerBinder = observer.asBinder();
         size = this.mObservers.size();

         for(int i = 0; i < size; ++i) {
            ObserverEntry entry = (ObserverEntry)this.mObservers.get(i);
            if (entry.observer.asBinder() == observerBinder) {
               this.mObservers.remove(i);
               observerBinder.unlinkToDeath(entry, 0);
               break;
            }
         }

         return this.mChildren.size() == 0 && this.mObservers.size() == 0;
      }

      private void collectMyObserversLocked(boolean leaf, IContentObserver observer, boolean observerWantsSelfNotifications, int targetUserHandle, ArrayList<ObserverCall> calls) {
         int N = this.mObservers.size();
         IBinder observerBinder = observer == null ? null : observer.asBinder();

         for(int i = 0; i < N; ++i) {
            ObserverEntry entry = (ObserverEntry)this.mObservers.get(i);
            boolean selfChange = entry.observer.asBinder() == observerBinder;
            if ((!selfChange || observerWantsSelfNotifications) && (targetUserHandle == -1 || entry.userHandle == -1 || targetUserHandle == entry.userHandle) && (leaf || entry.notifyForDescendants)) {
               calls.add(new ObserverCall(this, entry.observer, selfChange));
            }
         }

      }

      public void collectObserversLocked(Uri uri, int index, IContentObserver observer, boolean observerWantsSelfNotifications, int targetUserHandle, ArrayList<ObserverCall> calls) {
         String segment = null;
         int segmentCount = this.countUriSegments(uri);
         if (index >= segmentCount) {
            this.collectMyObserversLocked(true, observer, observerWantsSelfNotifications, targetUserHandle, calls);
         } else {
            segment = this.getUriSegment(uri, index);
            this.collectMyObserversLocked(false, observer, observerWantsSelfNotifications, targetUserHandle, calls);
         }

         int N = this.mChildren.size();

         for(int i = 0; i < N; ++i) {
            ObserverNode node = (ObserverNode)this.mChildren.get(i);
            if (segment == null || node.mName.equals(segment)) {
               node.collectObserversLocked(uri, index + 1, observer, observerWantsSelfNotifications, targetUserHandle, calls);
               if (segment != null) {
                  break;
               }
            }
         }

      }

      private class ObserverEntry implements IBinder.DeathRecipient {
         public final IContentObserver observer;
         public final int uid;
         public final int pid;
         public final boolean notifyForDescendants;
         private final int userHandle;
         private final Object observersLock;

         public ObserverEntry(IContentObserver o, boolean n, Object observersLock, int _uid, int _pid, int _userHandle) {
            this.observersLock = observersLock;
            this.observer = o;
            this.uid = _uid;
            this.pid = _pid;
            this.userHandle = _userHandle;
            this.notifyForDescendants = n;

            try {
               this.observer.asBinder().linkToDeath(this, 0);
            } catch (RemoteException var9) {
               this.binderDied();
            }

         }

         public void binderDied() {
            synchronized(this.observersLock) {
               ObserverNode.this.removeObserverLocked(this.observer);
            }
         }
      }
   }

   public static final class ObserverCall {
      final ObserverNode mNode;
      final IContentObserver mObserver;
      final boolean mSelfChange;

      ObserverCall(ObserverNode node, IContentObserver observer, boolean selfChange) {
         this.mNode = node;
         this.mObserver = observer;
         this.mSelfChange = selfChange;
      }
   }
}
