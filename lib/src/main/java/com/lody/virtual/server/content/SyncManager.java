package com.lody.virtual.server.content;

import android.accounts.Account;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ISyncAdapter;
import android.content.ISyncContext;
import android.content.ISyncStatusObserver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.PeriodicSync;
import android.content.ServiceConnection;
import android.content.SyncAdapterType;
import android.content.SyncResult;
import android.content.SyncStatusInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.Pair;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.compat.ContentResolverCompat;
import com.lody.virtual.os.BackgroundThread;
import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.lody.virtual.server.accounts.AccountAndUser;
import com.lody.virtual.server.accounts.VAccountManagerService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SyncManager {
   private static final String TAG = "SyncManager";
   private static final long LOCAL_SYNC_DELAY;
   private static final long MAX_TIME_PER_SYNC;
   private static final long SYNC_NOTIFICATION_DELAY;
   private static final long INITIAL_SYNC_RETRY_TIME_IN_MS = 30000L;
   private static final long DEFAULT_MAX_SYNC_RETRY_TIME_IN_SECONDS = 3600L;
   private static final int DELAY_RETRY_SYNC_IN_PROGRESS_IN_SECONDS = 10;
   private static final int INITIALIZATION_UNBIND_DELAY_MS = 5000;
   private static final String SYNC_WAKE_LOCK_PREFIX = "*sync*";
   private static final String HANDLE_SYNC_ALARM_WAKE_LOCK = "SyncManagerHandleSyncAlarm";
   private static final String SYNC_LOOP_WAKE_LOCK = "SyncLoopWakeLock";
   private static final int MAX_SIMULTANEOUS_REGULAR_SYNCS;
   private static final int MAX_SIMULTANEOUS_INITIALIZATION_SYNCS;
   private Context mContext;
   private static final AccountAndUser[] INITIAL_ACCOUNTS_ARRAY;
   private volatile AccountAndUser[] mRunningAccounts;
   private volatile boolean mDataConnectionIsConnected;
   private volatile boolean mStorageIsLow;
   private AlarmManager mAlarmService;
   private SyncStorageEngine mSyncStorageEngine;
   private final SyncQueue mSyncQueue;
   protected final ArrayList<ActiveSyncContext> mActiveSyncContexts;
   private final PendingIntent mSyncAlarmIntent;
   private ConnectivityManager mConnManagerDoNotUseDirectly;
   protected SyncAdaptersCache mSyncAdapters;
   private BroadcastReceiver mStorageIntentReceiver;
   private BroadcastReceiver mBootCompletedReceiver;
   private BroadcastReceiver mBackgroundDataSettingChanged;
   private BroadcastReceiver mAccountsUpdatedReceiver;
   private final PowerManager mPowerManager;
   private int mSyncRandomOffsetMillis;
   private final VUserManager mUserManager;
   private static final long SYNC_ALARM_TIMEOUT_MIN = 30000L;
   private static final long SYNC_ALARM_TIMEOUT_MAX = 7200000L;
   private BroadcastReceiver mConnectivityIntentReceiver;
   private BroadcastReceiver mShutdownIntentReceiver;
   private BroadcastReceiver mUserIntentReceiver;
   private static final String ACTION_SYNC_ALARM = "android.content.syncmanager.SYNC_ALARM";
   private final SyncHandler mSyncHandler;
   private volatile boolean mBootCompleted;

   private List<VUserInfo> getAllUsers() {
      return this.mUserManager.getUsers();
   }

   private boolean containsAccountAndUser(AccountAndUser[] accounts, Account account, int userId) {
      boolean found = false;

      for(int i = 0; i < accounts.length; ++i) {
         if (accounts[i].userId == userId && accounts[i].account.equals(account)) {
            found = true;
            break;
         }
      }

      return found;
   }

   public void updateRunningAccounts() {
      this.mRunningAccounts = VAccountManagerService.get().getAllAccounts();
      if (this.mBootCompleted) {
         this.doDatabaseCleanup();
      }

      Iterator var1 = this.mActiveSyncContexts.iterator();

      while(var1.hasNext()) {
         ActiveSyncContext currentSyncContext = (ActiveSyncContext)var1.next();
         if (!this.containsAccountAndUser(this.mRunningAccounts, currentSyncContext.mSyncOperation.account, currentSyncContext.mSyncOperation.userId)) {
            Log.d(TAG, "canceling sync since the account is no longer running");
            this.sendSyncFinishedOrCanceledMessage(currentSyncContext, (SyncResult)null);
         }
      }

      this.sendCheckAlarmsMessage();
   }

   private void doDatabaseCleanup() {
      Iterator var1 = this.mUserManager.getUsers(true).iterator();

      while(var1.hasNext()) {
         VUserInfo user = (VUserInfo)var1.next();
         if (!user.partial) {
            Account[] accountsForUser = VAccountManagerService.get().getAccounts(user.id, (String)null);
            this.mSyncStorageEngine.doDatabaseCleanup(accountsForUser, user.id);
         }
      }

   }

   private boolean readDataConnectionState() {
      NetworkInfo networkInfo = this.getConnectivityManager().getActiveNetworkInfo();
      return networkInfo != null && networkInfo.isConnected();
   }

   private ConnectivityManager getConnectivityManager() {
      synchronized(this) {
         if (this.mConnManagerDoNotUseDirectly == null) {
            this.mConnManagerDoNotUseDirectly = (ConnectivityManager)this.mContext.getSystemService("connectivity");
         }

         return this.mConnManagerDoNotUseDirectly;
      }
   }

   public SyncManager(Context context) {
      this.mRunningAccounts = INITIAL_ACCOUNTS_ARRAY;
      this.mDataConnectionIsConnected = false;
      this.mStorageIsLow = false;
      this.mAlarmService = null;
      this.mActiveSyncContexts = new ArrayList();
      this.mStorageIntentReceiver = new BroadcastReceiver() {
         public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.DEVICE_STORAGE_LOW".equals(action)) {
               Log.v("SyncManager", "Internal storage is low.");
               SyncManager.this.mStorageIsLow = true;
               SyncManager.this.cancelActiveSync((Account)null, -1, (String)null);
            } else if ("android.intent.action.DEVICE_STORAGE_OK".equals(action)) {
               Log.v("SyncManager", "Internal storage is ok.");
               SyncManager.this.mStorageIsLow = false;
               SyncManager.this.sendCheckAlarmsMessage();
            }

         }
      };
      this.mBootCompletedReceiver = new BroadcastReceiver() {
         public void onReceive(Context context, Intent intent) {
            SyncManager.this.mSyncHandler.onBootCompleted();
         }
      };
      this.mBackgroundDataSettingChanged = new BroadcastReceiver() {
         public void onReceive(Context context, Intent intent) {
            if (SyncManager.this.getConnectivityManager().getBackgroundDataSetting()) {
               SyncManager.this.scheduleSync((Account)null, -1, -1, (String)null, new Bundle(), 0L, 0L, false);
            }

         }
      };
      this.mAccountsUpdatedReceiver = new BroadcastReceiver() {
         public void onReceive(Context context, Intent intent) {
            SyncManager.this.updateRunningAccounts();
            SyncManager.this.scheduleSync((Account)null, -1, -2, (String)null, (Bundle)null, 0L, 0L, false);
         }
      };
      this.mConnectivityIntentReceiver = new BroadcastReceiver() {
         public void onReceive(Context context, Intent intent) {
            boolean wasConnected = SyncManager.this.mDataConnectionIsConnected;
            SyncManager.this.mDataConnectionIsConnected = SyncManager.this.readDataConnectionState();
            if (SyncManager.this.mDataConnectionIsConnected) {
               if (!wasConnected) {
                  Log.v("SyncManager", "Reconnection detected: clearing all backoffs");
                  synchronized(SyncManager.this.mSyncQueue) {
                     SyncManager.this.mSyncStorageEngine.clearAllBackoffsLocked(SyncManager.this.mSyncQueue);
                  }
               }

               SyncManager.this.sendCheckAlarmsMessage();
            }

         }
      };
      this.mShutdownIntentReceiver = new BroadcastReceiver() {
         public void onReceive(Context context, Intent intent) {
            Log.w("SyncManager", "Writing sync state before shutdown...");
            SyncManager.this.getSyncStorageEngine().writeAllState();
         }
      };
      this.mUserIntentReceiver = new BroadcastReceiver() {
         public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int userId = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            if (userId != -10000) {
               if ("virtual.android.intent.action.USER_REMOVED".equals(action)) {
                  SyncManager.this.onUserRemoved(userId);
               } else if ("virtual.android.intent.action.USER_ADDED".equals(action)) {
                  SyncManager.this.onUserStarting(userId);
               } else if ("virtual.android.intent.action.USER_REMOVED".equals(action)) {
                  SyncManager.this.onUserStopping(userId);
               }

            }
         }
      };
      this.mBootCompleted = false;
      this.mContext = context;
      SyncStorageEngine.init(context);
      this.mSyncStorageEngine = SyncStorageEngine.getSingleton();
      this.mSyncStorageEngine.setOnSyncRequestListener(new SyncStorageEngine.OnSyncRequestListener() {
         public void onSyncRequest(Account account, int userId, int reason, String authority, Bundle extras) {
            SyncManager.this.scheduleSync(account, userId, reason, authority, extras, 0L, 0L, false);
         }
      });
      this.mSyncAdapters = new SyncAdaptersCache(this.mContext);
      this.mSyncAdapters.refreshServiceCache((String)null);
      this.mSyncQueue = new SyncQueue(this.mSyncStorageEngine, this.mSyncAdapters);
      this.mSyncHandler = new SyncHandler(BackgroundThread.get().getLooper());
      if (VERSION.SDK_INT >= 31) {
         this.mSyncAlarmIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("android.content.syncmanager.SYNC_ALARM"), 67108864);
      } else {
         this.mSyncAlarmIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("android.content.syncmanager.SYNC_ALARM"), 0);
      }

      IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
      context.registerReceiver(this.mConnectivityIntentReceiver, intentFilter);
      intentFilter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
      context.registerReceiver(this.mBootCompletedReceiver, intentFilter);
      intentFilter = new IntentFilter("android.net.conn.BACKGROUND_DATA_SETTING_CHANGED");
      context.registerReceiver(this.mBackgroundDataSettingChanged, intentFilter);
      intentFilter = new IntentFilter("android.intent.action.DEVICE_STORAGE_LOW");
      intentFilter.addAction("android.intent.action.DEVICE_STORAGE_OK");
      context.registerReceiver(this.mStorageIntentReceiver, intentFilter);
      intentFilter = new IntentFilter("android.intent.action.ACTION_SHUTDOWN");
      intentFilter.setPriority(100);
      context.registerReceiver(this.mShutdownIntentReceiver, intentFilter);
      intentFilter = new IntentFilter();
      intentFilter.addAction("virtual.android.intent.action.USER_REMOVED");
      intentFilter.addAction("virtual.android.intent.action.USER_ADDED");
      intentFilter.addAction("virtual.android.intent.action.USER_REMOVED");
      this.mContext.registerReceiver(this.mUserIntentReceiver, intentFilter);
      context.registerReceiver(new SyncAlarmIntentReceiver(), new IntentFilter("android.content.syncmanager.SYNC_ALARM"));
      this.mPowerManager = (PowerManager)context.getSystemService("power");
      this.mUserManager = VUserManager.get();
      this.mSyncStorageEngine.addStatusChangeListener(1, new ISyncStatusObserver.Stub() {
         public void onStatusChanged(int which) {
            SyncManager.this.sendCheckAlarmsMessage();
         }
      });
      this.mSyncRandomOffsetMillis = this.mSyncStorageEngine.getSyncRandomOffset() * 1000;
   }

   private long jitterize(long minValue, long maxValue) {
      Random random = new Random(SystemClock.elapsedRealtime());
      long spread = maxValue - minValue;
      if (spread > 2147483647L) {
         throw new IllegalArgumentException("the difference between the maxValue and the minValue must be less than 2147483647");
      } else {
         return minValue + (long)random.nextInt((int)spread);
      }
   }

   public SyncStorageEngine getSyncStorageEngine() {
      return this.mSyncStorageEngine;
   }

   public int getIsSyncable(Account account, int userId, String providerName) {
      int isSyncable = this.mSyncStorageEngine.getIsSyncable(account, userId, providerName);
      VUserInfo userInfo = VUserManager.get().getUserInfo(userId);
      if (userInfo != null && userInfo.isRestricted()) {
         SyncAdaptersCache.SyncAdapterInfo syncAdapterInfo = this.mSyncAdapters.getServiceInfo(account, providerName);
         return syncAdapterInfo == null ? isSyncable : 0;
      } else {
         return isSyncable;
      }
   }

   private void ensureAlarmService() {
      if (this.mAlarmService == null) {
         this.mAlarmService = (AlarmManager)this.mContext.getSystemService("alarm");
      }

   }

   public void scheduleSync(Account requestedAccount, int userId, int reason, String requestedAuthority, Bundle extras, long beforeRuntimeMillis, long runtimeMillis, boolean onlyThoseWithUnkownSyncableState) {
      boolean backgroundDataUsageAllowed = !this.mBootCompleted || this.getConnectivityManager().getBackgroundDataSetting();
      if (extras == null) {
         extras = new Bundle();
      }

      Log.d(TAG, "one-time sync for: " + requestedAccount + " " + extras.toString() + " " + requestedAuthority);
      Boolean expedited = extras.getBoolean("expedited", false);
      if (expedited) {
         runtimeMillis = -1L;
      }

      AccountAndUser[] accounts;
      if (requestedAccount != null && userId != -1) {
         accounts = new AccountAndUser[]{new AccountAndUser(requestedAccount, userId)};
      } else {
         accounts = this.mRunningAccounts;
         if (accounts.length == 0) {
            Log.v(TAG, "scheduleSync: no accounts configured, dropping");
            return;
         }
      }

      boolean uploadOnly = extras.getBoolean("upload", false);
      boolean manualSync = extras.getBoolean("force", false);
      if (manualSync) {
         extras.putBoolean("ignore_backoff", true);
         extras.putBoolean("ignore_settings", true);
      }

      boolean ignoreSettings = extras.getBoolean("ignore_settings", false);
      byte source;
      if (uploadOnly) {
         source = 1;
      } else if (manualSync) {
         source = 3;
      } else if (requestedAuthority == null) {
         source = 2;
      } else {
         source = 0;
      }

      AccountAndUser[] var18 = accounts;
      int var19 = accounts.length;

      label135:
      for(int var20 = 0; var20 < var19; ++var20) {
         AccountAndUser account = var18[var20];
         HashSet<String> syncableAuthorities = new HashSet();
         Iterator var23 = this.mSyncAdapters.getAllServices().iterator();

         while(var23.hasNext()) {
            SyncAdaptersCache.SyncAdapterInfo syncAdapter = (SyncAdaptersCache.SyncAdapterInfo)var23.next();
            syncableAuthorities.add(syncAdapter.type.authority);
         }

         if (requestedAuthority != null) {
            boolean hasSyncAdapter = syncableAuthorities.contains(requestedAuthority);
            syncableAuthorities.clear();
            if (hasSyncAdapter) {
               syncableAuthorities.add(requestedAuthority);
            }
         }

         var23 = syncableAuthorities.iterator();

         while(true) {
            int isSyncable;
            SyncAdaptersCache.SyncAdapterInfo syncAdapterInfo;
            boolean allowParallelSyncs;
            String authority;
            do {
               do {
                  do {
                     do {
                        if (!var23.hasNext()) {
                           continue label135;
                        }

                        authority = (String)var23.next();
                        isSyncable = this.getIsSyncable(account.account, account.userId, authority);
                     } while(isSyncable == 0);

                     syncAdapterInfo = this.mSyncAdapters.getServiceInfo(account.account, authority);
                  } while(syncAdapterInfo == null);

                  allowParallelSyncs = syncAdapterInfo.type.allowParallelSyncs();
                  boolean isAlwaysSyncable = syncAdapterInfo.type.isAlwaysSyncable();
                  if (isSyncable < 0 && isAlwaysSyncable) {
                     this.mSyncStorageEngine.setIsSyncable(account.account, account.userId, authority, 1);
                     isSyncable = 1;
                  }
               } while(onlyThoseWithUnkownSyncableState && isSyncable >= 0);
            } while(!syncAdapterInfo.type.supportsUploading() && uploadOnly);

            boolean syncAllowed = isSyncable < 0 || ignoreSettings || backgroundDataUsageAllowed && this.mSyncStorageEngine.getMasterSyncAutomatically(account.userId) && this.mSyncStorageEngine.getSyncAutomatically(account.account, account.userId, authority);
            if (!syncAllowed) {
               Log.d(TAG, "scheduleSync: sync of " + account + ", " + authority + " is not allowed, dropping request");
            } else {
               Pair<Long, Long> backoff = this.mSyncStorageEngine.getBackoff(account.account, account.userId, authority);
               long delayUntil = this.mSyncStorageEngine.getDelayUntilTime(account.account, account.userId, authority);
               long backoffTime = backoff != null ? (Long)backoff.first : 0L;
               if (isSyncable < 0) {
                  Bundle newExtras = new Bundle();
                  newExtras.putBoolean("initialize", true);
                  Log.v(TAG, "schedule initialisation Sync:, delay until " + delayUntil + ", run by " + 0 + ", source " + source + ", account " + account + ", authority " + authority + ", extras " + newExtras);
                  this.scheduleSyncOperation(new SyncOperation(account.account, account.userId, reason, source, authority, newExtras, 0L, 0L, backoffTime, delayUntil, allowParallelSyncs));
               }

               if (!onlyThoseWithUnkownSyncableState) {
                  Log.v(TAG, "scheduleSync: delay until " + delayUntil + " run by " + runtimeMillis + " flex " + beforeRuntimeMillis + ", source " + source + ", account " + account + ", authority " + authority + ", extras " + extras);
                  this.scheduleSyncOperation(new SyncOperation(account.account, account.userId, reason, source, authority, extras, runtimeMillis, beforeRuntimeMillis, backoffTime, delayUntil, allowParallelSyncs));
               }
            }
         }
      }

   }

   public void scheduleLocalSync(Account account, int userId, int reason, String authority) {
      Bundle extras = new Bundle();
      extras.putBoolean("upload", true);
      this.scheduleSync(account, userId, reason, authority, extras, LOCAL_SYNC_DELAY, 2L * LOCAL_SYNC_DELAY, false);
   }

   public SyncAdapterType[] getSyncAdapterTypes() {
      Collection<SyncAdaptersCache.SyncAdapterInfo> serviceInfos = this.mSyncAdapters.getAllServices();
      SyncAdapterType[] types = new SyncAdapterType[serviceInfos.size()];
      int i = 0;

      for(Iterator var4 = serviceInfos.iterator(); var4.hasNext(); ++i) {
         SyncAdaptersCache.SyncAdapterInfo serviceInfo = (SyncAdaptersCache.SyncAdapterInfo)var4.next();
         types[i] = serviceInfo.type;
      }

      return types;
   }

   private void sendSyncAlarmMessage() {
      Log.v(TAG, "sending MESSAGE_SYNC_ALARM");
      this.mSyncHandler.sendEmptyMessage(2);
   }

   private void sendCheckAlarmsMessage() {
      Log.v(TAG, "sending MESSAGE_CHECK_ALARMS");
      this.mSyncHandler.removeMessages(3);
      this.mSyncHandler.sendEmptyMessage(3);
   }

   private void sendSyncFinishedOrCanceledMessage(ActiveSyncContext syncContext, SyncResult syncResult) {
      Log.v(TAG, "sending MESSAGE_SYNC_FINISHED");
      Message msg = this.mSyncHandler.obtainMessage();
      msg.what = 1;
      msg.obj = new SyncHandlerMessagePayload(syncContext, syncResult);
      this.mSyncHandler.sendMessage(msg);
   }

   private void sendCancelSyncsMessage(Account account, int userId, String authority) {
      Log.v(TAG, "sending MESSAGE_CANCEL");
      Message msg = this.mSyncHandler.obtainMessage();
      msg.what = 6;
      msg.obj = Pair.create(account, authority);
      msg.arg1 = userId;
      this.mSyncHandler.sendMessage(msg);
   }

   private void clearBackoffSetting(SyncOperation op) {
      this.mSyncStorageEngine.setBackoff(op.account, op.userId, op.authority, -1L, -1L);
      synchronized(this.mSyncQueue) {
         this.mSyncQueue.onBackoffChanged(op.account, op.userId, op.authority, 0L);
      }
   }

   private void increaseBackoffSetting(SyncOperation op) {
      long now = SystemClock.elapsedRealtime();
      Pair<Long, Long> previousSettings = this.mSyncStorageEngine.getBackoff(op.account, op.userId, op.authority);
      long newDelayInMs = -1L;
      if (previousSettings != null) {
         if (now < (Long)previousSettings.first) {
            Log.v(TAG, "Still in backoff, do not increase it. Remaining: " + ((Long)previousSettings.first - now) / 1000L + " seconds.");
            return;
         }

         newDelayInMs = (Long)previousSettings.second * 2L;
      }

      if (newDelayInMs <= 0L) {
         newDelayInMs = this.jitterize(30000L, 33000L);
      }

      long maxSyncRetryTimeInSeconds = 3600L;
      if (newDelayInMs > maxSyncRetryTimeInSeconds * 1000L) {
         newDelayInMs = maxSyncRetryTimeInSeconds * 1000L;
      }

      long backoff = now + newDelayInMs;
      this.mSyncStorageEngine.setBackoff(op.account, op.userId, op.authority, backoff, newDelayInMs);
      op.backoff = backoff;
      op.updateEffectiveRunTime();
      synchronized(this.mSyncQueue) {
         this.mSyncQueue.onBackoffChanged(op.account, op.userId, op.authority, backoff);
      }
   }

   private void setDelayUntilTime(SyncOperation op, long delayUntilSeconds) {
      long delayUntil = delayUntilSeconds * 1000L;
      long absoluteNow = System.currentTimeMillis();
      long newDelayUntilTime;
      if (delayUntil > absoluteNow) {
         newDelayUntilTime = SystemClock.elapsedRealtime() + (delayUntil - absoluteNow);
      } else {
         newDelayUntilTime = 0L;
      }

      this.mSyncStorageEngine.setDelayUntilTime(op.account, op.userId, op.authority, newDelayUntilTime);
      synchronized(this.mSyncQueue) {
         this.mSyncQueue.onDelayUntilTimeChanged(op.account, op.authority, newDelayUntilTime);
      }
   }

   public void cancelActiveSync(Account account, int userId, String authority) {
      this.sendCancelSyncsMessage(account, userId, authority);
   }

   public void scheduleSyncOperation(SyncOperation syncOperation) {
      boolean queueChanged;
      synchronized(this.mSyncQueue) {
         queueChanged = this.mSyncQueue.add(syncOperation);
      }

      if (queueChanged) {
         Log.v(TAG, "scheduleSyncOperation: enqueued " + syncOperation);
         this.sendCheckAlarmsMessage();
      } else {
         Log.v(TAG, "scheduleSyncOperation: dropping duplicate sync operation " + syncOperation);
      }

   }

   public void clearScheduledSyncOperations(Account account, int userId, String authority) {
      synchronized(this.mSyncQueue) {
         this.mSyncQueue.remove(account, userId, authority);
      }

      this.mSyncStorageEngine.setBackoff(account, userId, authority, -1L, -1L);
   }

   void maybeRescheduleSync(SyncResult syncResult, SyncOperation operation) {
      Log.d(TAG, "encountered error(s) during the sync: " + syncResult + ", " + operation);
      operation = new SyncOperation(operation);
      if (operation.extras.getBoolean("ignore_backoff", false)) {
         operation.extras.remove("ignore_backoff");
      }

      if (operation.extras.getBoolean("do_not_retry", false)) {
         Log.d(TAG, "not retrying sync operation because SYNC_EXTRAS_DO_NOT_RETRY was specified " + operation);
      } else if (operation.extras.getBoolean("upload", false) && !syncResult.syncAlreadyInProgress) {
         operation.extras.remove("upload");
         Log.d(TAG, "retrying sync operation as a two-way sync because an upload-only sync encountered an error: " + operation);
         this.scheduleSyncOperation(operation);
      } else if (syncResult.tooManyRetries) {
         Log.d(TAG, "not retrying sync operation because it retried too many times: " + operation);
      } else if (syncResult.madeSomeProgress()) {
         Log.d(TAG, "retrying sync operation because even though it had an error it achieved some success");
         this.scheduleSyncOperation(operation);
      } else if (syncResult.syncAlreadyInProgress) {
         Log.d(TAG, "retrying sync operation that failed because there was already a sync in progress: " + operation);
         this.scheduleSyncOperation(new SyncOperation(operation.account, operation.userId, operation.reason, operation.syncSource, operation.authority, operation.extras, 10000L, operation.flexTime, operation.backoff, operation.delayUntil, operation.allowParallelSyncs));
      } else if (syncResult.hasSoftError()) {
         Log.d(TAG, "retrying sync operation because it encountered a soft error: " + operation);
         this.scheduleSyncOperation(operation);
      } else {
         Log.d(TAG, "not retrying sync operation because the error is a hard error: " + operation);
      }

   }

   private void onUserStarting(int userId) {
      this.mSyncAdapters.refreshServiceCache((String)null);
      this.updateRunningAccounts();
      synchronized(this.mSyncQueue) {
         this.mSyncQueue.addPendingOperations(userId);
      }

      Account[] accounts = VAccountManagerService.get().getAccounts(userId, (String)null);
      Account[] var3 = accounts;
      int var4 = accounts.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Account account = var3[var5];
         this.scheduleSync(account, userId, -8, (String)null, (Bundle)null, 0L, 0L, true);
      }

      this.sendCheckAlarmsMessage();
   }

   private void onUserStopping(int userId) {
      this.updateRunningAccounts();
      this.cancelActiveSync((Account)null, userId, (String)null);
   }

   private void onUserRemoved(int userId) {
      this.updateRunningAccounts();
      this.mSyncStorageEngine.doDatabaseCleanup(new Account[0], userId);
      synchronized(this.mSyncQueue) {
         this.mSyncQueue.removeUser(userId);
      }
   }

   static String formatTime(long time) {
      Time tobj = new Time();
      tobj.set(time);
      return tobj.format("%Y-%m-%d %H:%M:%S");
   }

   private String getLastFailureMessage(int code) {
      switch (code) {
         case 1:
            return "sync already in progress";
         case 2:
            return "authentication error";
         case 3:
            return "I/O error";
         case 4:
            return "parse error";
         case 5:
            return "conflict error";
         case 6:
            return "too many deletions error";
         case 7:
            return "too many retries error";
         case 8:
            return "internal error";
         default:
            return "unknown";
      }
   }

   private boolean isSyncStillActive(ActiveSyncContext activeSyncContext) {
      Iterator var2 = this.mActiveSyncContexts.iterator();

      ActiveSyncContext sync;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         sync = (ActiveSyncContext)var2.next();
      } while(sync != activeSyncContext);

      return true;
   }

   static {
      boolean isLargeRAM = true;
      int defaultMaxInitSyncs = 5;
      int defaultMaxRegularSyncs = 2;
      MAX_SIMULTANEOUS_INITIALIZATION_SYNCS = defaultMaxInitSyncs;
      MAX_SIMULTANEOUS_REGULAR_SYNCS = defaultMaxRegularSyncs;
      LOCAL_SYNC_DELAY = 30000L;
      MAX_TIME_PER_SYNC = 300000L;
      SYNC_NOTIFICATION_DELAY = 30000L;
      INITIAL_ACCOUNTS_ARRAY = new AccountAndUser[0];
   }

   class SyncHandler extends Handler {
      private static final int MESSAGE_SYNC_FINISHED = 1;
      private static final int MESSAGE_SYNC_ALARM = 2;
      private static final int MESSAGE_CHECK_ALARMS = 3;
      private static final int MESSAGE_SERVICE_CONNECTED = 4;
      private static final int MESSAGE_SERVICE_DISCONNECTED = 5;
      private static final int MESSAGE_CANCEL = 6;
      public final SyncNotificationInfo mSyncNotificationInfo = new SyncNotificationInfo();
      private Long mAlarmScheduleTime = null;
      public final SyncTimeTracker mSyncTimeTracker = SyncManager.this.new SyncTimeTracker();
      private List<Message> mBootQueue = new ArrayList();

      public void onBootCompleted() {
         Log.v("SyncManager", "Boot completed, clearing boot queue.");
         SyncManager.this.doDatabaseCleanup();
         synchronized(this) {
            Iterator var2 = this.mBootQueue.iterator();

            while(var2.hasNext()) {
               Message message = (Message)var2.next();
               this.sendMessage(message);
            }

            this.mBootQueue = null;
            SyncManager.this.mBootCompleted = true;
         }
      }

      private boolean tryEnqueueMessageUntilReadyToRun(Message msg) {
         synchronized(this) {
            if (!SyncManager.this.mBootCompleted) {
               this.mBootQueue.add(Message.obtain(msg));
               return true;
            } else {
               return false;
            }
         }
      }

      public SyncHandler(Looper looper) {
         super(looper);
      }

      public void handleMessage(Message msg) {
         if (!this.tryEnqueueMessageUntilReadyToRun(msg)) {
            long earliestFuturePollTime = Long.MAX_VALUE;
            long nextPendingSyncTime = Long.MAX_VALUE;

            try {
               SyncManager.this.mDataConnectionIsConnected = SyncManager.this.readDataConnectionState();
               earliestFuturePollTime = this.scheduleReadyPeriodicSyncs();
               switch (msg.what) {
                  case 1:
                     Log.v("SyncManager", "handleSyncHandlerMessage: MESSAGE_SYNC_FINISHED");
                     SyncHandlerMessagePayload payloadx = (SyncHandlerMessagePayload)msg.obj;
                     if (!SyncManager.this.isSyncStillActive(payloadx.activeSyncContext)) {
                        Log.d("SyncManager", "handleSyncHandlerMessage: dropping since the sync is no longer active: " + payloadx.activeSyncContext);
                     } else {
                        this.runSyncFinishedOrCanceledLocked(payloadx.syncResult, payloadx.activeSyncContext);
                        nextPendingSyncTime = this.maybeStartNextSyncLocked();
                     }
                     break;
                  case 2:
                     boolean isLoggable = true;
                     Log.v("SyncManager", "handleSyncHandlerMessage: MESSAGE_SYNC_ALARM");
                     this.mAlarmScheduleTime = null;
                     nextPendingSyncTime = this.maybeStartNextSyncLocked();
                     break;
                  case 3:
                     Log.v("SyncManager", "handleSyncHandlerMessage: MESSAGE_CHECK_ALARMS");
                     nextPendingSyncTime = this.maybeStartNextSyncLocked();
                     break;
                  case 4:
                     ServiceConnectionData msgData = (ServiceConnectionData)msg.obj;
                     Log.d("SyncManager", "handleSyncHandlerMessage: MESSAGE_SERVICE_CONNECTED: " + msgData.activeSyncContext);
                     if (SyncManager.this.isSyncStillActive(msgData.activeSyncContext)) {
                        this.runBoundToSyncAdapter(msgData.activeSyncContext, msgData.syncAdapter);
                     }
                     break;
                  case 5:
                     ActiveSyncContext currentSyncContext = ((ServiceConnectionData)msg.obj).activeSyncContext;
                     Log.d("SyncManager", "handleSyncHandlerMessage: MESSAGE_SERVICE_DISCONNECTED: " + currentSyncContext);
                     if (SyncManager.this.isSyncStillActive(currentSyncContext)) {
                        if (currentSyncContext.mSyncAdapter != null) {
                           try {
                              currentSyncContext.mSyncAdapter.cancelSync(currentSyncContext);
                           } catch (RemoteException var12) {
                           }
                        }

                        SyncResult syncResult = new SyncResult();
                        ++syncResult.stats.numIoExceptions;
                        this.runSyncFinishedOrCanceledLocked(syncResult, currentSyncContext);
                        nextPendingSyncTime = this.maybeStartNextSyncLocked();
                     }
                     break;
                  case 6:
                     Pair<Account, String> payload = (Pair)msg.obj;
                     Log.d("SyncManager", "handleSyncHandlerMessage: MESSAGE_SERVICE_CANCEL: " + payload.first + ", " + (String)payload.second);
                     this.cancelActiveSyncLocked((Account)payload.first, msg.arg1, (String)payload.second);
                     nextPendingSyncTime = this.maybeStartNextSyncLocked();
               }
            } finally {
               this.manageSyncNotificationLocked();
               this.manageSyncAlarmLocked(earliestFuturePollTime, nextPendingSyncTime);
               this.mSyncTimeTracker.update();
            }

         }
      }

      private long scheduleReadyPeriodicSyncs() {
         Log.v("SyncManager", "scheduleReadyPeriodicSyncs");
         boolean backgroundDataUsageAllowed = SyncManager.this.getConnectivityManager().getBackgroundDataSetting();
         long earliestFuturePollTime = Long.MAX_VALUE;
         if (!backgroundDataUsageAllowed) {
            return earliestFuturePollTime;
         } else {
            AccountAndUser[] accounts = SyncManager.this.mRunningAccounts;
            long nowAbsolute = System.currentTimeMillis();
            long shiftedNowAbsolute = 0L < nowAbsolute - (long)SyncManager.this.mSyncRandomOffsetMillis ? nowAbsolute - (long)SyncManager.this.mSyncRandomOffsetMillis : 0L;
            ArrayList<Pair<SyncStorageEngine.AuthorityInfo, SyncStatusInfo>> infos = SyncManager.this.mSyncStorageEngine.getCopyOfAllAuthoritiesWithSyncStatus();
            Iterator var10 = infos.iterator();

            while(true) {
               while(var10.hasNext()) {
                  Pair<SyncStorageEngine.AuthorityInfo, SyncStatusInfo> info = (Pair)var10.next();
                  SyncStorageEngine.AuthorityInfo authorityInfo = (SyncStorageEngine.AuthorityInfo)info.first;
                  SyncStatusInfo status = (SyncStatusInfo)info.second;
                  if (TextUtils.isEmpty(authorityInfo.authority)) {
                     Log.e("SyncManager", "Got an empty provider string. Skipping: " + authorityInfo);
                  } else if (SyncManager.this.containsAccountAndUser(accounts, authorityInfo.account, authorityInfo.userId) && SyncManager.this.mSyncStorageEngine.getMasterSyncAutomatically(authorityInfo.userId) && SyncManager.this.mSyncStorageEngine.getSyncAutomatically(authorityInfo.account, authorityInfo.userId, authorityInfo.authority) && SyncManager.this.getIsSyncable(authorityInfo.account, authorityInfo.userId, authorityInfo.authority) != 0) {
                     int i = 0;

                     for(int N = authorityInfo.periodicSyncs.size(); i < N; ++i) {
                        PeriodicSync sync = (PeriodicSync)authorityInfo.periodicSyncs.get(i);
                        Bundle extras = sync.extras;
                        long periodInMillis = sync.period * 1000L;
                        long flexInMillis = mirror.android.content.PeriodicSync.flexTime.get(sync) * 1000L;
                        if (periodInMillis > 0L) {
                           long lastPollTimeAbsolute = status.getPeriodicSyncTime(i);
                           long remainingMillis = periodInMillis - shiftedNowAbsolute % periodInMillis;
                           long timeSinceLastRunMillis = nowAbsolute - lastPollTimeAbsolute;
                           boolean runEarly = remainingMillis <= flexInMillis && timeSinceLastRunMillis > periodInMillis - flexInMillis;
                           Log.v("SyncManager", "sync: " + i + " for " + authorityInfo.authority + ". period: " + periodInMillis + " flex: " + flexInMillis + " remaining: " + remainingMillis + " time_since_last: " + timeSinceLastRunMillis + " last poll absol: " + lastPollTimeAbsolute + " shifted now: " + shiftedNowAbsolute + " run_early: " + runEarly);
                           if (runEarly || remainingMillis == periodInMillis || lastPollTimeAbsolute > nowAbsolute || timeSinceLastRunMillis >= periodInMillis) {
                              Pair<Long, Long> backoff = SyncManager.this.mSyncStorageEngine.getBackoff(authorityInfo.account, authorityInfo.userId, authorityInfo.authority);
                              SyncAdaptersCache.SyncAdapterInfo syncAdapterInfo = SyncManager.this.mSyncAdapters.getServiceInfo(authorityInfo.account, authorityInfo.authority);
                              if (syncAdapterInfo == null) {
                                 continue;
                              }

                              SyncManager.this.mSyncStorageEngine.setPeriodicSyncTime(authorityInfo.ident, (PeriodicSync)authorityInfo.periodicSyncs.get(i), nowAbsolute);
                              SyncManager.this.scheduleSyncOperation(new SyncOperation(authorityInfo.account, authorityInfo.userId, -4, 4, authorityInfo.authority, extras, 0L, 0L, backoff != null ? (Long)backoff.first : 0L, SyncManager.this.mSyncStorageEngine.getDelayUntilTime(authorityInfo.account, authorityInfo.userId, authorityInfo.authority), syncAdapterInfo.type.allowParallelSyncs()));
                           }

                           long nextPollTimeAbsolute;
                           if (runEarly) {
                              nextPollTimeAbsolute = nowAbsolute + periodInMillis + remainingMillis;
                           } else {
                              nextPollTimeAbsolute = nowAbsolute + remainingMillis;
                           }

                           if (nextPollTimeAbsolute < earliestFuturePollTime) {
                              earliestFuturePollTime = nextPollTimeAbsolute;
                           }
                        }
                     }
                  }
               }

               if (earliestFuturePollTime == Long.MAX_VALUE) {
                  return Long.MAX_VALUE;
               }

               return SystemClock.elapsedRealtime() + (earliestFuturePollTime < nowAbsolute ? 0L : earliestFuturePollTime - nowAbsolute);
            }
         }
      }

      private long maybeStartNextSyncLocked() {
         boolean isLoggable = true;
         if (isLoggable) {
            Log.v("SyncManager", "maybeStartNextSync");
         }

         if (!SyncManager.this.mDataConnectionIsConnected) {
            if (isLoggable) {
               Log.v("SyncManager", "maybeStartNextSync: no data connection, skipping");
            }

            return Long.MAX_VALUE;
         } else if (SyncManager.this.mStorageIsLow) {
            if (isLoggable) {
               Log.v("SyncManager", "maybeStartNextSync: memory low, skipping");
            }

            return Long.MAX_VALUE;
         } else {
            AccountAndUser[] accounts = SyncManager.this.mRunningAccounts;
            if (accounts == SyncManager.INITIAL_ACCOUNTS_ARRAY) {
               if (isLoggable) {
                  Log.v("SyncManager", "maybeStartNextSync: accounts not known, skipping");
               }

               return Long.MAX_VALUE;
            } else {
               long now = SystemClock.elapsedRealtime();
               long nextReadyToRunTime = Long.MAX_VALUE;
               ArrayList<SyncOperation> operations = new ArrayList();
               int numInit;
               synchronized(SyncManager.this.mSyncQueue) {
                  if (isLoggable) {
                     Log.v("SyncManager", "build the operation array, syncQueue size is " + SyncManager.this.mSyncQueue.getOperations().size());
                  }

                  Iterator<SyncOperation> operationIterator = SyncManager.this.mSyncQueue.getOperations().iterator();
                  Set<Integer> removedUsers = new HashSet();

                  while(true) {
                     if (!operationIterator.hasNext()) {
                        Iterator var26 = removedUsers.iterator();

                        while(var26.hasNext()) {
                           Integer user = (Integer)var26.next();
                           if (SyncManager.this.mUserManager.getUserInfo(user) == null) {
                              SyncManager.this.onUserRemoved(user);
                           }
                        }
                        break;
                     }

                     SyncOperation op = (SyncOperation)operationIterator.next();
                     if (!SyncManager.this.containsAccountAndUser(accounts, op.account, op.userId)) {
                        operationIterator.remove();
                        SyncManager.this.mSyncStorageEngine.deleteFromPending(op.pendingOperation);
                        if (isLoggable) {
                           Log.v("SyncManager", "    Dropping sync operation: account doesn\'t exist.");
                        }
                     } else {
                        numInit = SyncManager.this.getIsSyncable(op.account, op.userId, op.authority);
                        if (numInit == 0) {
                           operationIterator.remove();
                           SyncManager.this.mSyncStorageEngine.deleteFromPending(op.pendingOperation);
                           if (isLoggable) {
                              Log.v("SyncManager", "    Dropping sync operation: isSyncable == 0.");
                           }
                        } else {
                           VUserInfo userInfo = SyncManager.this.mUserManager.getUserInfo(op.userId);
                           if (userInfo == null) {
                              removedUsers.add(op.userId);
                           }

                           if (isLoggable) {
                              Log.v("SyncManager", "    Dropping sync operation: user not running.");
                           }
                        }
                     }
                  }
               }

               if (isLoggable) {
                  Log.v("SyncManager", "sort the candidate operations, size " + operations.size());
               }

               Collections.sort(operations);
               if (isLoggable) {
                  Log.v("SyncManager", "dispatch all ready sync operations");
               }

               int i = 0;

               for(int N = operations.size(); i < N; ++i) {
                  SyncOperation candidate = (SyncOperation)operations.get(i);
                  boolean candidateIsInitialization = candidate.isInitialization();
                  numInit = 0;
                  int numRegular = 0;
                  ActiveSyncContext conflict = null;
                  ActiveSyncContext longRunning = null;
                  ActiveSyncContext toReschedule = null;
                  ActiveSyncContext oldestNonExpeditedRegular = null;
                  Iterator var18 = SyncManager.this.mActiveSyncContexts.iterator();

                  while(true) {
                     while(var18.hasNext()) {
                        ActiveSyncContext activeSyncContext = (ActiveSyncContext)var18.next();
                        SyncOperation activeOp = activeSyncContext.mSyncOperation;
                        if (activeOp.isInitialization()) {
                           ++numInit;
                        } else {
                           ++numRegular;
                           if (!activeOp.isExpedited() && (oldestNonExpeditedRegular == null || oldestNonExpeditedRegular.mStartTime > activeSyncContext.mStartTime)) {
                              oldestNonExpeditedRegular = activeSyncContext;
                           }
                        }

                        if (activeOp.account.type.equals(candidate.account.type) && activeOp.authority.equals(candidate.authority) && activeOp.userId == candidate.userId && (!activeOp.allowParallelSyncs || activeOp.account.name.equals(candidate.account.name))) {
                           conflict = activeSyncContext;
                        } else if (candidateIsInitialization == activeOp.isInitialization() && activeSyncContext.mStartTime + SyncManager.MAX_TIME_PER_SYNC < now) {
                           longRunning = activeSyncContext;
                        }
                     }

                     if (isLoggable) {
                        Log.v("SyncManager", "candidate " + (i + 1) + " of " + N + ": " + candidate);
                        Log.v("SyncManager", "  numActiveInit=" + numInit + ", numActiveRegular=" + numRegular);
                        Log.v("SyncManager", "  longRunning: " + longRunning);
                        Log.v("SyncManager", "  conflict: " + conflict);
                        Log.v("SyncManager", "  oldestNonExpeditedRegular: " + oldestNonExpeditedRegular);
                     }

                     boolean roomAvailable = candidateIsInitialization ? numInit < SyncManager.MAX_SIMULTANEOUS_INITIALIZATION_SYNCS : numRegular < SyncManager.MAX_SIMULTANEOUS_REGULAR_SYNCS;
                     if (conflict != null) {
                        if (candidateIsInitialization && !conflict.mSyncOperation.isInitialization() && numInit < SyncManager.MAX_SIMULTANEOUS_INITIALIZATION_SYNCS) {
                           toReschedule = conflict;
                           Log.v("SyncManager", "canceling and rescheduling sync since an initialization takes higher priority, " + conflict);
                        } else {
                           if (!candidate.expedited || conflict.mSyncOperation.expedited || candidateIsInitialization != conflict.mSyncOperation.isInitialization()) {
                              break;
                           }

                           toReschedule = conflict;
                           Log.v("SyncManager", "canceling and rescheduling sync since an expedited takes higher priority, " + conflict);
                        }
                     } else if (!roomAvailable) {
                        if (candidate.isExpedited() && oldestNonExpeditedRegular != null && !candidateIsInitialization) {
                           toReschedule = oldestNonExpeditedRegular;
                           Log.v("SyncManager", "canceling and rescheduling sync since an expedited is ready to run, " + oldestNonExpeditedRegular);
                        } else {
                           if (longRunning == null || candidateIsInitialization != longRunning.mSyncOperation.isInitialization()) {
                              break;
                           }

                           toReschedule = longRunning;
                           Log.v("SyncManager", "canceling and rescheduling sync since it ran roo long, " + longRunning);
                        }
                     }

                     if (toReschedule != null) {
                        this.runSyncFinishedOrCanceledLocked((SyncResult)null, toReschedule);
                        SyncManager.this.scheduleSyncOperation(toReschedule.mSyncOperation);
                     }

                     synchronized(SyncManager.this.mSyncQueue) {
                        SyncManager.this.mSyncQueue.remove(candidate);
                     }

                     this.dispatchSyncOperation(candidate);
                     break;
                  }
               }

               return nextReadyToRunTime;
            }
         }
      }

      private boolean dispatchSyncOperation(SyncOperation op) {
         Log.v("SyncManager", "dispatchSyncOperation: we are going to sync " + op);
         Log.v("SyncManager", "num active syncs: " + SyncManager.this.mActiveSyncContexts.size());
         Iterator var2 = SyncManager.this.mActiveSyncContexts.iterator();

         ActiveSyncContext activeSyncContext;
         while(var2.hasNext()) {
            activeSyncContext = (ActiveSyncContext)var2.next();
            Log.v("SyncManager", activeSyncContext.toString());
         }

         SyncAdaptersCache.SyncAdapterInfo syncAdapterInfo = SyncManager.this.mSyncAdapters.getServiceInfo(op.account, op.authority);
         if (syncAdapterInfo == null) {
            Log.d("SyncManager", "can\'t find a sync adapter for " + op.authority + ", removing settings for it");
            SyncManager.this.mSyncStorageEngine.removeAuthority(op.account, op.userId, op.authority);
            return false;
         } else {
            activeSyncContext = SyncManager.this.new ActiveSyncContext(op, this.insertStartSyncEvent(op));
            activeSyncContext.mSyncInfo = SyncManager.this.mSyncStorageEngine.addActiveSync(activeSyncContext);
            SyncManager.this.mActiveSyncContexts.add(activeSyncContext);
            Log.v("SyncManager", "dispatchSyncOperation: starting " + activeSyncContext);
            if (!activeSyncContext.bindToSyncAdapter(syncAdapterInfo, op.userId)) {
               Log.e("SyncManager", "Bind attempt failed to " + syncAdapterInfo);
               this.closeActiveSyncContext(activeSyncContext);
               return false;
            } else {
               return true;
            }
         }
      }

      private void runBoundToSyncAdapter(ActiveSyncContext activeSyncContext, ISyncAdapter syncAdapter) {
         activeSyncContext.mSyncAdapter = syncAdapter;
         SyncOperation syncOperation = activeSyncContext.mSyncOperation;

         try {
            activeSyncContext.mIsLinkedToDeath = true;
            syncAdapter.asBinder().linkToDeath(activeSyncContext, 0);
            syncAdapter.startSync(activeSyncContext, syncOperation.authority, syncOperation.account, syncOperation.extras);
         } catch (RemoteException var5) {
            RemoteException remoteExc = var5;
            Log.d("SyncManager", "maybeStartNextSync: caught a RemoteException, rescheduling", remoteExc);
            this.closeActiveSyncContext(activeSyncContext);
            SyncManager.this.increaseBackoffSetting(syncOperation);
            SyncManager.this.scheduleSyncOperation(new SyncOperation(syncOperation));
         } catch (RuntimeException var6) {
            RuntimeException exc = var6;
            this.closeActiveSyncContext(activeSyncContext);
            Log.e("SyncManager", "Caught RuntimeException while starting the sync " + syncOperation, exc);
         }

      }

      private void cancelActiveSyncLocked(Account account, int userId, String authority) {
         ArrayList<ActiveSyncContext> activeSyncs = new ArrayList(SyncManager.this.mActiveSyncContexts);
         Iterator var5 = activeSyncs.iterator();

         while(true) {
            ActiveSyncContext activeSyncContext;
            do {
               do {
                  do {
                     do {
                        if (!var5.hasNext()) {
                           return;
                        }

                        activeSyncContext = (ActiveSyncContext)var5.next();
                     } while(activeSyncContext == null);
                  } while(account != null && !account.equals(activeSyncContext.mSyncOperation.account));
               } while(authority != null && !authority.equals(activeSyncContext.mSyncOperation.authority));
            } while(userId != -1 && userId != activeSyncContext.mSyncOperation.userId);

            this.runSyncFinishedOrCanceledLocked((SyncResult)null, activeSyncContext);
         }
      }

      private void runSyncFinishedOrCanceledLocked(SyncResult syncResult, ActiveSyncContext activeSyncContext) {
         if (activeSyncContext.mIsLinkedToDeath) {
            activeSyncContext.mSyncAdapter.asBinder().unlinkToDeath(activeSyncContext, 0);
            activeSyncContext.mIsLinkedToDeath = false;
         }

         this.closeActiveSyncContext(activeSyncContext);
         SyncOperation syncOperation = activeSyncContext.mSyncOperation;
         long elapsedTime = SystemClock.elapsedRealtime() - activeSyncContext.mStartTime;
         String historyMessage;
         byte downstreamActivity;
         byte upstreamActivity;
         if (syncResult != null) {
            Log.v("SyncManager", "runSyncFinishedOrCanceled [finished]: " + syncOperation + ", result " + syncResult);
            if (!syncResult.hasError()) {
               historyMessage = "success";
               downstreamActivity = 0;
               upstreamActivity = 0;
               SyncManager.this.clearBackoffSetting(syncOperation);
            } else {
               Log.d("SyncManager", "failed sync operation " + syncOperation + ", " + syncResult);
               if (!syncResult.syncAlreadyInProgress) {
                  SyncManager.this.increaseBackoffSetting(syncOperation);
               }

               SyncManager.this.maybeRescheduleSync(syncResult, syncOperation);
               historyMessage = ContentResolverCompat.syncErrorToString(this.syncResultToErrorNumber(syncResult));
               downstreamActivity = 0;
               upstreamActivity = 0;
            }

            SyncManager.this.setDelayUntilTime(syncOperation, syncResult.delayUntil);
         } else {
            Log.v("SyncManager", "runSyncFinishedOrCanceled [canceled]: " + syncOperation);
            if (activeSyncContext.mSyncAdapter != null) {
               try {
                  activeSyncContext.mSyncAdapter.cancelSync(activeSyncContext);
               } catch (RemoteException var10) {
               }
            }

            historyMessage = "canceled";
            downstreamActivity = 0;
            upstreamActivity = 0;
         }

         this.stopSyncEvent(activeSyncContext.mHistoryRowId, syncOperation, historyMessage, upstreamActivity, downstreamActivity, elapsedTime);
         if (syncResult != null && syncResult.fullSyncRequested) {
            SyncManager.this.scheduleSyncOperation(new SyncOperation(syncOperation.account, syncOperation.userId, syncOperation.reason, syncOperation.syncSource, syncOperation.authority, new Bundle(), 0L, 0L, syncOperation.backoff, syncOperation.delayUntil, syncOperation.allowParallelSyncs));
         }

      }

      private void closeActiveSyncContext(ActiveSyncContext activeSyncContext) {
         activeSyncContext.close();
         SyncManager.this.mActiveSyncContexts.remove(activeSyncContext);
         SyncManager.this.mSyncStorageEngine.removeActiveSync(activeSyncContext.mSyncInfo, activeSyncContext.mSyncOperation.userId);
      }

      private int syncResultToErrorNumber(SyncResult syncResult) {
         if (syncResult.syncAlreadyInProgress) {
            return 1;
         } else if (syncResult.stats.numAuthExceptions > 0L) {
            return 2;
         } else if (syncResult.stats.numIoExceptions > 0L) {
            return 3;
         } else if (syncResult.stats.numParseExceptions > 0L) {
            return 4;
         } else if (syncResult.stats.numConflictDetectedExceptions > 0L) {
            return 5;
         } else if (syncResult.tooManyDeletions) {
            return 6;
         } else if (syncResult.tooManyRetries) {
            return 7;
         } else if (syncResult.databaseError) {
            return 8;
         } else {
            throw new IllegalStateException("we are not in an error state, " + syncResult);
         }
      }

      private void manageSyncNotificationLocked() {
         boolean shouldCancel;
         boolean shouldInstall;
         if (SyncManager.this.mActiveSyncContexts.isEmpty()) {
            this.mSyncNotificationInfo.startTime = null;
            shouldCancel = this.mSyncNotificationInfo.isActive;
            shouldInstall = false;
         } else {
            long now = SystemClock.elapsedRealtime();
            if (this.mSyncNotificationInfo.startTime == null) {
               this.mSyncNotificationInfo.startTime = now;
            }

            if (this.mSyncNotificationInfo.isActive) {
               shouldCancel = false;
               shouldInstall = false;
            } else {
               shouldCancel = false;
               boolean timeToShowNotification = now > this.mSyncNotificationInfo.startTime + SyncManager.SYNC_NOTIFICATION_DELAY;
               if (timeToShowNotification) {
                  shouldInstall = true;
               } else {
                  shouldInstall = false;
                  Iterator var6 = SyncManager.this.mActiveSyncContexts.iterator();

                  while(var6.hasNext()) {
                     ActiveSyncContext activeSyncContext = (ActiveSyncContext)var6.next();
                     boolean manualSync = activeSyncContext.mSyncOperation.extras.getBoolean("force", false);
                     if (manualSync) {
                        shouldInstall = true;
                        break;
                     }
                  }
               }
            }
         }

         if (shouldCancel && !shouldInstall) {
            this.sendSyncStateIntent();
            this.mSyncNotificationInfo.isActive = false;
         }

         if (shouldInstall) {
            this.sendSyncStateIntent();
            this.mSyncNotificationInfo.isActive = true;
         }

      }

      private void manageSyncAlarmLocked(long nextPeriodicEventElapsedTime, long nextPendingEventElapsedTime) {
         if (SyncManager.this.mDataConnectionIsConnected) {
            if (!SyncManager.this.mStorageIsLow) {
               long notificationTime = !SyncManager.this.mSyncHandler.mSyncNotificationInfo.isActive && SyncManager.this.mSyncHandler.mSyncNotificationInfo.startTime != null ? SyncManager.this.mSyncHandler.mSyncNotificationInfo.startTime + SyncManager.SYNC_NOTIFICATION_DELAY : Long.MAX_VALUE;
               long earliestTimeoutTime = Long.MAX_VALUE;
               Iterator var9 = SyncManager.this.mActiveSyncContexts.iterator();

               long now;
               while(var9.hasNext()) {
                  ActiveSyncContext currentSyncContext = (ActiveSyncContext)var9.next();
                  now = currentSyncContext.mTimeoutStartTime + SyncManager.MAX_TIME_PER_SYNC;
                  Log.v("SyncManager", "manageSyncAlarm: active sync, mTimeoutStartTime + MAX is " + now);
                  if (earliestTimeoutTime > now) {
                     earliestTimeoutTime = now;
                  }
               }

               Log.v("SyncManager", "manageSyncAlarm: notificationTime is " + notificationTime);
               Log.v("SyncManager", "manageSyncAlarm: earliestTimeoutTime is " + earliestTimeoutTime);
               Log.v("SyncManager", "manageSyncAlarm: nextPeriodicEventElapsedTime is " + nextPeriodicEventElapsedTime);
               Log.v("SyncManager", "manageSyncAlarm: nextPendingEventElapsedTime is " + nextPendingEventElapsedTime);
               long alarmTime = Math.min(notificationTime, earliestTimeoutTime);
               alarmTime = Math.min(alarmTime, nextPeriodicEventElapsedTime);
               alarmTime = Math.min(alarmTime, nextPendingEventElapsedTime);
               now = SystemClock.elapsedRealtime();
               if (alarmTime < now + 30000L) {
                  Log.v("SyncManager", "manageSyncAlarm: the alarmTime is too small, " + alarmTime + ", setting to " + (now + 30000L));
                  alarmTime = now + 30000L;
               } else if (alarmTime > now + 7200000L) {
                  Log.v("SyncManager", "manageSyncAlarm: the alarmTime is too large, " + alarmTime + ", setting to " + (now + 30000L));
                  alarmTime = now + 7200000L;
               }

               boolean shouldSet = false;
               boolean shouldCancel = false;
               boolean alarmIsActive = this.mAlarmScheduleTime != null && now < this.mAlarmScheduleTime;
               boolean needAlarm = alarmTime != Long.MAX_VALUE;
               if (needAlarm) {
                  if (!alarmIsActive || alarmTime < this.mAlarmScheduleTime) {
                     shouldSet = true;
                  }
               } else {
                  shouldCancel = alarmIsActive;
               }

               SyncManager.this.ensureAlarmService();
               if (shouldSet) {
                  Log.v("SyncManager", "requesting that the alarm manager wake us up at elapsed time " + alarmTime + ", now is " + now + ", " + (alarmTime - now) / 1000L + " secs from now");
                  this.mAlarmScheduleTime = alarmTime;
                  SyncManager.this.mAlarmService.setExact(2, alarmTime, SyncManager.this.mSyncAlarmIntent);
               } else if (shouldCancel) {
                  this.mAlarmScheduleTime = null;
                  SyncManager.this.mAlarmService.cancel(SyncManager.this.mSyncAlarmIntent);
               }

            }
         }
      }

      private void sendSyncStateIntent() {
      }

      public long insertStartSyncEvent(SyncOperation syncOperation) {
         int source = syncOperation.syncSource;
         long now = System.currentTimeMillis();
         return SyncManager.this.mSyncStorageEngine.insertStartSyncEvent(syncOperation.account, syncOperation.userId, syncOperation.reason, syncOperation.authority, now, source, syncOperation.isInitialization(), syncOperation.extras);
      }

      public void stopSyncEvent(long rowId, SyncOperation syncOperation, String resultMessage, int upstreamActivity, int downstreamActivity, long elapsedTime) {
         SyncManager.this.mSyncStorageEngine.stopSyncEvent(rowId, elapsedTime, resultMessage, (long)downstreamActivity, (long)upstreamActivity);
      }

      class SyncNotificationInfo {
         public boolean isActive = false;
         public Long startTime = null;

         public void toString(StringBuilder sb) {
            sb.append("isActive ").append(this.isActive).append(", startTime ").append(this.startTime);
         }

         public String toString() {
            StringBuilder sb = new StringBuilder();
            this.toString(sb);
            return sb.toString();
         }
      }
   }

   class ServiceConnectionData {
      public final ActiveSyncContext activeSyncContext;
      public final ISyncAdapter syncAdapter;

      ServiceConnectionData(ActiveSyncContext activeSyncContext, ISyncAdapter syncAdapter) {
         this.activeSyncContext = activeSyncContext;
         this.syncAdapter = syncAdapter;
      }
   }

   private class SyncTimeTracker {
      boolean mLastWasSyncing;
      long mWhenSyncStarted;
      private long mTimeSpentSyncing;

      private SyncTimeTracker() {
         this.mLastWasSyncing = false;
         this.mWhenSyncStarted = 0L;
      }

      public synchronized void update() {
         boolean isSyncInProgress = !SyncManager.this.mActiveSyncContexts.isEmpty();
         if (isSyncInProgress != this.mLastWasSyncing) {
            long now = SystemClock.elapsedRealtime();
            if (isSyncInProgress) {
               this.mWhenSyncStarted = now;
            } else {
               this.mTimeSpentSyncing += now - this.mWhenSyncStarted;
            }

            this.mLastWasSyncing = isSyncInProgress;
         }
      }

      public synchronized long timeSpentSyncing() {
         if (!this.mLastWasSyncing) {
            return this.mTimeSpentSyncing;
         } else {
            long now = SystemClock.elapsedRealtime();
            return this.mTimeSpentSyncing + (now - this.mWhenSyncStarted);
         }
      }

      // $FF: synthetic method
      SyncTimeTracker(Object x1) {
         this();
      }
   }

   class ActiveSyncContext extends ISyncContext.Stub implements ServiceConnection, IBinder.DeathRecipient {
      final SyncOperation mSyncOperation;
      final long mHistoryRowId;
      ISyncAdapter mSyncAdapter;
      final long mStartTime;
      long mTimeoutStartTime;
      boolean mBound;
      VSyncInfo mSyncInfo;
      boolean mIsLinkedToDeath = false;

      public ActiveSyncContext(SyncOperation syncOperation, long historyRowId) {
         this.mSyncOperation = syncOperation;
         this.mHistoryRowId = historyRowId;
         this.mSyncAdapter = null;
         this.mStartTime = SystemClock.elapsedRealtime();
         this.mTimeoutStartTime = this.mStartTime;
      }

      public void sendHeartbeat() {
      }

      public void onFinished(SyncResult result) {
         Log.v("SyncManager", "onFinished: " + this);
         SyncManager.this.sendSyncFinishedOrCanceledMessage(this, result);
      }

      public void toString(StringBuilder sb) {
         sb.append("startTime ").append(this.mStartTime).append(", mTimeoutStartTime ").append(this.mTimeoutStartTime).append(", mHistoryRowId ").append(this.mHistoryRowId).append(", syncOperation ").append(this.mSyncOperation);
      }

      public void onServiceConnected(ComponentName name, IBinder service) {
         Message msg = SyncManager.this.mSyncHandler.obtainMessage();
         msg.what = 4;
         msg.obj = SyncManager.this.new ServiceConnectionData(this, ISyncAdapter.Stub.asInterface(service));
         SyncManager.this.mSyncHandler.sendMessage(msg);
      }

      public void onServiceDisconnected(ComponentName name) {
         Message msg = SyncManager.this.mSyncHandler.obtainMessage();
         msg.what = 5;
         msg.obj = SyncManager.this.new ServiceConnectionData(this, (ISyncAdapter)null);
         SyncManager.this.mSyncHandler.sendMessage(msg);
      }

      boolean bindToSyncAdapter(SyncAdaptersCache.SyncAdapterInfo info, int userId) {
         Log.d("SyncManager", "bindToSyncAdapter: " + info.serviceInfo + ", connection " + this);
         Intent intent = new Intent();
         intent.setAction("android.content.SyncAdapter");
         intent.setComponent(info.componentName);
         this.mBound = true;
         boolean bindResult = VActivityManager.get().bindService(SyncManager.this.mContext, intent, this, 21, this.mSyncOperation.userId);
         if (!bindResult) {
            this.mBound = false;
         }

         return bindResult;
      }

      protected void close() {
         Log.d("SyncManager", "unBindFromSyncAdapter: connection " + this);
         if (this.mBound) {
            this.mBound = false;
            VActivityManager.get().unbindService(SyncManager.this.mContext, this);
         }

      }

      public String toString() {
         StringBuilder sb = new StringBuilder();
         this.toString(sb);
         return sb.toString();
      }

      public void binderDied() {
         SyncManager.this.sendSyncFinishedOrCanceledMessage(this, (SyncResult)null);
      }
   }

   class SyncAlarmIntentReceiver extends BroadcastReceiver {
      public void onReceive(Context context, Intent intent) {
         SyncManager.this.sendSyncAlarmMessage();
      }
   }

   class SyncHandlerMessagePayload {
      public final ActiveSyncContext activeSyncContext;
      public final SyncResult syncResult;

      SyncHandlerMessagePayload(ActiveSyncContext syncContext, SyncResult syncResult) {
         this.activeSyncContext = syncContext;
         this.syncResult = syncResult;
      }
   }
}
