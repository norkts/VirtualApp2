package com.lody.virtual.server.content;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ISyncStatusObserver;
import android.content.PeriodicSync;
import android.content.SyncStatusInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.util.Xml;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.AtomicFile;
import com.lody.virtual.helper.utils.FastXmlSerializer;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.server.accounts.AccountAndUser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class SyncStorageEngine extends Handler {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0YCGsxEjdgNCA9KAguVg=="));
   private static final boolean DEBUG = false;
   private static final String TAG_FILE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0YCGsxEjdgNCA9KAguWWwjOCs="));
   private static final String XML_ATTR_NEXT_AUTHORITY_ID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4uIGwLJAVmHho1Iz0cLmghLCw="));
   private static final String XML_ATTR_LISTEN_FOR_TICKLES = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYKWwFNCZODjw1IzlXLmwjAiFlESg6"));
   private static final String XML_ATTR_SYNC_RANDOM_OFFSET = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4iPm8zNAZrDlkPKAcqDW8VBgM="));
   private static final String XML_ATTR_ENABLED = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQgcP2sjHitiEVRF"));
   private static final String XML_ATTR_USER = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28jSFo="));
   private static final String XML_TAG_LISTEN_FOR_TICKLES = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYKWwFNCZqNB4qJBccP2wFOCtsJ1RF"));
   private static final long DEFAULT_POLL_FREQUENCY_SECONDS = 86400L;
   private static final double DEFAULT_FLEX_PERCENT_SYNC = 0.04;
   private static final long DEFAULT_MIN_FLEX_ALLOWED_SECS = 5L;
   public static final int EVENT_START = 0;
   public static final int EVENT_STOP = 1;
   public static final String[] EVENTS = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IisqEWcmMFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IisqUmcFSFo="))};
   public static final int SOURCE_SERVER = 0;
   public static final int SOURCE_LOCAL = 1;
   public static final int SOURCE_POLL = 2;
   public static final int SOURCE_USER = 3;
   public static final int SOURCE_PERIODIC = 4;
   public static final long NOT_IN_BACKOFF_MODE = -1L;
   public static final String[] SOURCES = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IiwuDGQhNF8=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OxYAE2MbHlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhYAQGIFSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQU2WGcjSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhYuDH0bGhZrDChF"))};
   public static final String MESG_SUCCESS = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0uOWszNANhJ1RF"));
   public static final String MESG_CANCELED = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGszNCRiDgpF"));
   public static final int MAX_HISTORY = 100;
   private static final int MSG_WRITE_STATUS = 1;
   private static final long WRITE_STATUS_DELAY = 600000L;
   private static final int MSG_WRITE_STATISTICS = 2;
   private static final long WRITE_STATISTICS_DELAY = 1800000L;
   private static final boolean SYNC_ENABLED_DEFAULT = false;
   private static final int ACCOUNTS_VERSION = 2;
   private static HashMap<String, String> sAuthorityRenames = new HashMap();
   private final SparseArray<AuthorityInfo> mAuthorities = new SparseArray();
   private final HashMap<AccountAndUser, AccountInfo> mAccounts = new HashMap();
   private final ArrayList<PendingOperation> mPendingOperations = new ArrayList();
   private final SparseArray<ArrayList<VSyncInfo>> mCurrentSyncs = new SparseArray();
   private final SparseArray<SyncStatusInfo> mSyncStatus = new SparseArray();
   private final ArrayList<SyncHistoryItem> mSyncHistory = new ArrayList();
   private final RemoteCallbackList<ISyncStatusObserver> mChangeListeners = new RemoteCallbackList();
   private final HashMap<ComponentName, SparseArray<AuthorityInfo>> mServices = new HashMap();
   private int mNextAuthorityId = 0;
   private final DayStats[] mDayStats = new DayStats[28];
   private final Calendar mCal;
   private int mYear;
   private int mYearInDays;
   private final Context mContext;
   private static volatile SyncStorageEngine sSyncStorageEngine;
   private int mSyncRandomOffset;
   private final AtomicFile mAccountInfoFile;
   private final AtomicFile mStatusFile;
   private final AtomicFile mStatisticsFile;
   private final AtomicFile mPendingFile;
   private static final int PENDING_FINISH_TO_WRITE = 4;
   private int mNumPendingFinished = 0;
   private int mNextHistoryId = 0;
   private SparseArray<Boolean> mMasterSyncAutomatically = new SparseArray();
   private boolean mDefaultMasterSyncAutomatically;
   private OnSyncRequestListener mSyncRequestListener;
   public static final int STATUS_FILE_END = 0;
   public static final int STATUS_FILE_ITEM = 100;
   public static final int PENDING_OPERATION_VERSION = 3;
   private static final String XML_ATTR_AUTHORITYID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZJi0cPg=="));
   private static final String XML_ATTR_SOURCE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4AI28jLCs="));
   private static final String XML_ATTR_EXPEDITED = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVMC9mHjAw"));
   private static final String XML_ATTR_REASON = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uP28zGiY="));
   private static final String XML_ATTR_VERSION = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4uKm8zAiVgN1RF"));
   public static final int STATISTICS_FILE_END = 0;
   public static final int STATISTICS_FILE_ITEM_OLD = 100;
   public static final int STATISTICS_FILE_ITEM = 101;

   private SyncStorageEngine(Context context, File syncDir) {
      this.mContext = context;
      sSyncStorageEngine = this;
      this.mCal = Calendar.getInstance(TimeZone.getTimeZone(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JSwIBnU0IFo="))));
      this.mDefaultMasterSyncAutomatically = false;
      this.maybeDeleteLegacyPendingInfoLocked(syncDir);
      this.mAccountInfoFile = new AtomicFile(new File(syncDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmESs2LRdXCA=="))));
      this.mStatusFile = new AtomicFile(new File(syncDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKNANONCwzKj5SVg=="))));
      this.mPendingFile = new AtomicFile(new File(syncDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguCGgFAiZiIFkaKgdbVg=="))));
      this.mStatisticsFile = new AtomicFile(new File(syncDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKLyZ9NAY2"))));
      this.readAccountInfoLocked();
      this.readStatusLocked();
      this.readPendingOperationsLocked();
      this.readStatisticsLocked();
      this.readAndDeleteLegacyAccountInfoLocked();
      this.writeAccountInfoLocked();
      this.writeStatusLocked();
      this.writePendingOperationsLocked();
      this.writeStatisticsLocked();
   }

   public static void init(Context context) {
      if (sSyncStorageEngine == null) {
         File dataDir = VEnvironment.getSyncDirectory();
         FileUtils.ensureDirCreate(dataDir);
         sSyncStorageEngine = new SyncStorageEngine(context, dataDir);
      }
   }

   public static SyncStorageEngine getSingleton() {
      if (sSyncStorageEngine == null) {
         throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALHsFAiZjAQozLwdbMWgVGiw=")));
      } else {
         return sSyncStorageEngine;
      }
   }

   protected void setOnSyncRequestListener(OnSyncRequestListener listener) {
      if (this.mSyncRequestListener == null) {
         this.mSyncRequestListener = listener;
      }

   }

   public void handleMessage(Message msg) {
      if (msg.what == 1) {
         synchronized(this.mAuthorities) {
            this.writeStatusLocked();
         }
      } else if (msg.what == 2) {
         synchronized(this.mAuthorities) {
            this.writeStatisticsLocked();
         }
      }

   }

   public int getSyncRandomOffset() {
      return this.mSyncRandomOffset;
   }

   public void addStatusChangeListener(int mask, ISyncStatusObserver callback) {
      synchronized(this.mAuthorities) {
         this.mChangeListeners.register(callback, mask);
      }
   }

   public void removeStatusChangeListener(ISyncStatusObserver callback) {
      synchronized(this.mAuthorities) {
         this.mChangeListeners.unregister(callback);
      }
   }

   public static long calculateDefaultFlexTime(long syncTimeSeconds) {
      if (syncTimeSeconds < 5L) {
         return 0L;
      } else {
         return syncTimeSeconds < 86400L ? (long)((double)syncTimeSeconds * 0.04) : 3456L;
      }
   }

   private void reportChange(int which) {
      ArrayList<ISyncStatusObserver> reports = null;
      synchronized(this.mAuthorities) {
         int i = this.mChangeListeners.beginBroadcast();

         while(i > 0) {
            --i;
            Integer mask = (Integer)this.mChangeListeners.getBroadcastCookie(i);
            if ((which & mask) != 0) {
               if (reports == null) {
                  reports = new ArrayList(i);
               }

               reports.add((ISyncStatusObserver)this.mChangeListeners.getBroadcastItem(i));
            }
         }

         this.mChangeListeners.finishBroadcast();
      }

      if (reports != null) {
         int i = reports.size();

         while(i > 0) {
            --i;

            try {
               ((ISyncStatusObserver)reports.get(i)).onStatusChanged(which);
            } catch (RemoteException var7) {
            }
         }
      }

   }

   public boolean getSyncAutomatically(Account account, int userId, String providerName) {
      synchronized(this.mAuthorities) {
         if (account == null) {
            int i = this.mAuthorities.size();

            AuthorityInfo authority;
            do {
               if (i <= 0) {
                  return false;
               }

               --i;
               authority = (AuthorityInfo)this.mAuthorities.valueAt(i);
            } while(!authority.authority.equals(providerName) || authority.userId != userId || !authority.enabled);

            return true;
         } else {
            AuthorityInfo authority = this.getAuthorityLocked(account, userId, providerName, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcwAiZ9IiAvLBdfD24gBi9oJzgdLAcYVg==")));
            return authority != null && authority.enabled;
         }
      }
   }

   public void setSyncAutomatically(Account account, int userId, String providerName, boolean sync) {
      synchronized(this.mAuthorities) {
         AuthorityInfo authority = this.getOrCreateAuthorityLocked(account, userId, providerName, -1, false);
         if (authority.enabled == sync) {
            return;
         }

         authority.enabled = sync;
         this.writeAccountInfoLocked();
      }

      if (sync) {
         this.requestSync(account, userId, -6, providerName, new Bundle());
      }

      this.reportChange(1);
   }

   public int getIsSyncable(Account account, int userId, String providerName) {
      synchronized(this.mAuthorities) {
         if (account != null) {
            AuthorityInfo authority = this.getAuthorityLocked(account, userId, providerName, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLH0aLF5nDlk5LwcuCGkjSFo=")));
            return authority == null ? -1 : authority.syncable;
         } else {
            int i = this.mAuthorities.size();

            AuthorityInfo authority;
            do {
               if (i <= 0) {
                  return -1;
               }

               --i;
               authority = (AuthorityInfo)this.mAuthorities.valueAt(i);
            } while(!authority.authority.equals(providerName));

            return authority.syncable;
         }
      }
   }

   public void setIsSyncable(Account account, int userId, String providerName, int syncable) {
      if (syncable > 1) {
         syncable = 1;
      } else if (syncable < -1) {
         syncable = -1;
      }

      synchronized(this.mAuthorities) {
         AuthorityInfo authority = this.getOrCreateAuthorityLocked(account, userId, providerName, -1, false);
         if (authority.syncable == syncable) {
            return;
         }

         authority.syncable = syncable;
         this.writeAccountInfoLocked();
      }

      if (syncable > 0) {
         this.requestSync(account, userId, -5, providerName, new Bundle());
      }

      this.reportChange(1);
   }

   public Pair<Long, Long> getBackoff(Account account, int userId, String providerName) {
      synchronized(this.mAuthorities) {
         AuthorityInfo authority = this.getAuthorityLocked(account, userId, providerName, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMjJCljJB4+KD5SVg==")));
         return authority != null && authority.backoffTime >= 0L ? Pair.create(authority.backoffTime, authority.backoffDelay) : null;
      }
   }

   public void setBackoff(Account account, int userId, String providerName, long nextSyncTime, long nextDelay) {
      boolean changed = false;
      synchronized(this.mAuthorities) {
         if (account != null && providerName != null) {
            AuthorityInfo authority = this.getOrCreateAuthorityLocked(account, userId, providerName, -1, true);
            if (authority.backoffTime == nextSyncTime && authority.backoffDelay == nextDelay) {
               return;
            }

            authority.backoffTime = nextSyncTime;
            authority.backoffDelay = nextDelay;
            changed = true;
         } else {
            Iterator var10 = this.mAccounts.values().iterator();

            label75:
            while(true) {
               AccountInfo accountInfo;
               do {
                  if (!var10.hasNext()) {
                     break label75;
                  }

                  accountInfo = (AccountInfo)var10.next();
               } while(account != null && !account.equals(accountInfo.accountAndUser.account) && userId != accountInfo.accountAndUser.userId);

               Iterator var12 = accountInfo.authorities.values().iterator();

               while(true) {
                  AuthorityInfo authorityInfo;
                  do {
                     do {
                        if (!var12.hasNext()) {
                           continue label75;
                        }

                        authorityInfo = (AuthorityInfo)var12.next();
                     } while(providerName != null && !providerName.equals(authorityInfo.authority));
                  } while(authorityInfo.backoffTime == nextSyncTime && authorityInfo.backoffDelay == nextDelay);

                  authorityInfo.backoffTime = nextSyncTime;
                  authorityInfo.backoffDelay = nextDelay;
                  changed = true;
               }
            }
         }
      }

      if (changed) {
         this.reportChange(1);
      }

   }

   public void clearAllBackoffsLocked(SyncQueue syncQueue) {
      boolean changed = false;
      synchronized(this.mAuthorities) {
         Iterator var4 = this.mAccounts.values().iterator();

         label38:
         while(true) {
            if (!var4.hasNext()) {
               break;
            }

            AccountInfo accountInfo = (AccountInfo)var4.next();
            Iterator var6 = accountInfo.authorities.values().iterator();

            while(true) {
               AuthorityInfo authorityInfo;
               do {
                  if (!var6.hasNext()) {
                     continue label38;
                  }

                  authorityInfo = (AuthorityInfo)var6.next();
               } while(authorityInfo.backoffTime == -1L && authorityInfo.backoffDelay == -1L);

               authorityInfo.backoffTime = -1L;
               authorityInfo.backoffDelay = -1L;
               syncQueue.onBackoffChanged(accountInfo.accountAndUser.account, accountInfo.accountAndUser.userId, authorityInfo.authority, 0L);
               changed = true;
            }
         }
      }

      if (changed) {
         this.reportChange(1);
      }

   }

   public void setDelayUntilTime(Account account, int userId, String providerName, long delayUntil) {
      synchronized(this.mAuthorities) {
         AuthorityInfo authority = this.getOrCreateAuthorityLocked(account, userId, providerName, -1, true);
         if (authority.delayUntil == delayUntil) {
            return;
         }

         authority.delayUntil = delayUntil;
      }

      this.reportChange(1);
   }

   public long getDelayUntilTime(Account account, int userId, String providerName) {
      synchronized(this.mAuthorities) {
         AuthorityInfo authority = this.getAuthorityLocked(account, userId, providerName, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFNCR9AQZKKj42MW8zSFo=")));
         return authority == null ? 0L : authority.delayUntil;
      }
   }

   private void updateOrRemovePeriodicSync(PeriodicSync toUpdate, int userId, boolean add) {
      synchronized(this.mAuthorities) {
         if (toUpdate.period <= 0L && add) {
            Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmUVGixLVlA8M15aOmoFFiVvAQIvPQgcJ2YwLDV5HgYqLD1XJ2U3IAVqIzwaJxcMKGoaFkhqHFk0IwgALmgYICthNAY1KBccP2IKLCZoIFArLRgqIE4FSFo=")) + add);
         }

         if (toUpdate.extras == null) {
            Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoJICtnEQoqLwgpCH4wAiBlJCgdLgQ6KmIFICBlMCAhKC1XDmsFATRlER03IAgiI2kFLDFnDllALQgID2wjNExiASwzKi02MW4IAj9lNzMxPQg+IGJTQVo=")) + add);
         }

         try {
            AuthorityInfo authority = this.getOrCreateAuthorityLocked(toUpdate.account, userId, toUpdate.authority, -1, false);
            if (!add) {
               SyncStatusInfo status = (SyncStatusInfo)this.mSyncStatus.get(authority.ident);
               boolean changed = false;
               Iterator<PeriodicSync> iterator = authority.periodicSyncs.iterator();
               int i = 0;

               while(iterator.hasNext()) {
                  PeriodicSync syncInfo = (PeriodicSync)iterator.next();
                  if (mirror.android.content.PeriodicSync.syncExtrasEquals(syncInfo.extras, toUpdate.extras)) {
                     iterator.remove();
                     changed = true;
                     if (status != null) {
                        status.removePeriodicSyncTime(i);
                     } else {
                        Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRcMCWgVMyhhNDA3Ki4+MW8VHShsJB4bLTo6D2YaPD9qAS8pKQgpJGwzNAFqNCQ0DRgiLGUwBgFpNzA2Phc2J2ojLyh9NzAgKBccPn4zMCVvVjwtIxgcIEsaGj98N1RF")));
                     }
                  } else {
                     ++i;
                  }
               }

               if (!changed) {
                  return;
               }
            } else {
               boolean alreadyPresent = false;
               int i = 0;

               for(int N = authority.periodicSyncs.size(); i < N; ++i) {
                  PeriodicSync syncInfo = (PeriodicSync)authority.periodicSyncs.get(i);
                  if (mirror.android.content.PeriodicSync.syncExtrasEquals(toUpdate.extras, syncInfo.extras)) {
                     if (toUpdate.period == syncInfo.period && mirror.android.content.PeriodicSync.flexTime.get(toUpdate) == mirror.android.content.PeriodicSync.flexTime.get(syncInfo)) {
                        return;
                     }

                     authority.periodicSyncs.set(i, mirror.android.content.PeriodicSync.clone(toUpdate));
                     alreadyPresent = true;
                     break;
                  }
               }

               if (!alreadyPresent) {
                  authority.periodicSyncs.add(mirror.android.content.PeriodicSync.clone(toUpdate));
                  SyncStatusInfo status = this.getOrCreateSyncStatusLocked(authority.ident);
                  status.setPeriodicSyncTime(authority.periodicSyncs.size() - 1, 0L);
               }
            }
         } finally {
            this.writeAccountInfoLocked();
            this.writeStatusLocked();
         }
      }

      this.reportChange(1);
   }

   public void addPeriodicSync(PeriodicSync toAdd, int userId) {
      this.updateOrRemovePeriodicSync(toAdd, userId, true);
   }

   public void removePeriodicSync(PeriodicSync toRemove, int userId) {
      this.updateOrRemovePeriodicSync(toRemove, userId, false);
   }

   public List<PeriodicSync> getPeriodicSyncs(Account account, int userId, String providerName) {
      ArrayList<PeriodicSync> syncs = new ArrayList();
      synchronized(this.mAuthorities) {
         AuthorityInfo authority = this.getAuthorityLocked(account, userId, providerName, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGcFNARjDh4wKQcqAWgjMClsJ1RF")));
         if (authority != null) {
            Iterator var7 = authority.periodicSyncs.iterator();

            while(var7.hasNext()) {
               PeriodicSync item = (PeriodicSync)var7.next();
               syncs.add(mirror.android.content.PeriodicSync.clone(item));
            }
         }

         return syncs;
      }
   }

   public void setMasterSyncAutomatically(boolean flag, int userId) {
      synchronized(this.mAuthorities) {
         Boolean auto = (Boolean)this.mMasterSyncAutomatically.get(userId);
         if (auto != null && auto == flag) {
            return;
         }

         this.mMasterSyncAutomatically.put(userId, flag);
         this.writeAccountInfoLocked();
      }

      if (flag) {
         this.requestSync((Account)null, userId, -7, (String)null, new Bundle());
      }

      this.reportChange(1);
   }

   public boolean getMasterSyncAutomatically(int userId) {
      synchronized(this.mAuthorities) {
         Boolean auto = (Boolean)this.mMasterSyncAutomatically.get(userId);
         return auto == null ? this.mDefaultMasterSyncAutomatically : auto;
      }
   }

   public void removeAuthority(Account account, int userId, String authority) {
      synchronized(this.mAuthorities) {
         this.removeAuthorityLocked(account, userId, authority, true);
      }
   }

   public AuthorityInfo getAuthority(int authorityId) {
      synchronized(this.mAuthorities) {
         return (AuthorityInfo)this.mAuthorities.get(authorityId);
      }
   }

   public boolean isSyncActive(Account account, int userId, String authority) {
      synchronized(this.mAuthorities) {
         Iterator var5 = this.getCurrentSyncs(userId).iterator();

         AuthorityInfo ainfo;
         do {
            if (!var5.hasNext()) {
               return false;
            }

            VSyncInfo syncInfo = (VSyncInfo)var5.next();
            ainfo = this.getAuthority(syncInfo.authorityId);
         } while(ainfo == null || !ainfo.account.equals(account) || !ainfo.authority.equals(authority) || ainfo.userId != userId);

         return true;
      }
   }

   public PendingOperation insertIntoPending(PendingOperation op) {
      synchronized(this.mAuthorities) {
         AuthorityInfo authority = this.getOrCreateAuthorityLocked(op.account, op.userId, op.authority, -1, true);
         if (authority == null) {
            return null;
         }

         op = new PendingOperation(op);
         op.authorityId = authority.ident;
         this.mPendingOperations.add(op);
         this.appendPendingOperationLocked(op);
         SyncStatusInfo status = this.getOrCreateSyncStatusLocked(authority.ident);
         status.pending = true;
      }

      this.reportChange(2);
      return op;
   }

   public boolean deleteFromPending(PendingOperation op) {
      boolean res = false;
      synchronized(this.mAuthorities) {
         if (this.mPendingOperations.remove(op)) {
            if (this.mPendingOperations.size() != 0 && this.mNumPendingFinished < 4) {
               ++this.mNumPendingFinished;
            } else {
               this.writePendingOperationsLocked();
               this.mNumPendingFinished = 0;
            }

            AuthorityInfo authority = this.getAuthorityLocked(op.account, op.userId, op.authority, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguDmgaMCtqNyw1KgYmPW8VBi9lNyBF")));
            if (authority != null) {
               int N = this.mPendingOperations.size();
               boolean morePending = false;

               for(int i = 0; i < N; ++i) {
                  PendingOperation cur = (PendingOperation)this.mPendingOperations.get(i);
                  if (cur.account.equals(op.account) && cur.authority.equals(op.authority) && cur.userId == op.userId) {
                     morePending = true;
                     break;
                  }
               }

               if (!morePending) {
                  SyncStatusInfo status = this.getOrCreateSyncStatusLocked(authority.ident);
                  status.pending = false;
               }
            }

            res = true;
         }
      }

      this.reportChange(2);
      return res;
   }

   public ArrayList<PendingOperation> getPendingOperations() {
      synchronized(this.mAuthorities) {
         return new ArrayList(this.mPendingOperations);
      }
   }

   public int getPendingOperationCount() {
      synchronized(this.mAuthorities) {
         return this.mPendingOperations.size();
      }
   }

   public void doDatabaseCleanup(Account[] accounts, int userId) {
      synchronized(this.mAuthorities) {
         SparseArray<AuthorityInfo> removing = new SparseArray();
         Iterator<AccountInfo> accIt = this.mAccounts.values().iterator();

         while(true) {
            AccountInfo acc;
            do {
               do {
                  if (!accIt.hasNext()) {
                     int i = removing.size();
                     if (i > 0) {
                        while(true) {
                           if (i <= 0) {
                              this.writeAccountInfoLocked();
                              this.writeStatusLocked();
                              this.writePendingOperationsLocked();
                              this.writeStatisticsLocked();
                              break;
                           }

                           --i;
                           int ident = removing.keyAt(i);
                           this.mAuthorities.remove(ident);
                           int j = this.mSyncStatus.size();

                           while(j > 0) {
                              --j;
                              if (this.mSyncStatus.keyAt(j) == ident) {
                                 this.mSyncStatus.remove(this.mSyncStatus.keyAt(j));
                              }
                           }

                           j = this.mSyncHistory.size();

                           while(j > 0) {
                              --j;
                              if (((SyncHistoryItem)this.mSyncHistory.get(j)).authorityId == ident) {
                                 this.mSyncHistory.remove(j);
                              }
                           }
                        }
                     }

                     return;
                  }

                  acc = (AccountInfo)accIt.next();
               } while(ArrayUtils.contains(accounts, acc.accountAndUser.account));
            } while(acc.accountAndUser.userId != userId);

            Iterator var7 = acc.authorities.values().iterator();

            while(var7.hasNext()) {
               AuthorityInfo auth = (AuthorityInfo)var7.next();
               removing.put(auth.ident, auth);
            }

            accIt.remove();
         }
      }
   }

   public VSyncInfo addActiveSync(SyncManager.ActiveSyncContext activeSyncContext) {
      VSyncInfo syncInfo;
      synchronized(this.mAuthorities) {
         AuthorityInfo authority = this.getOrCreateAuthorityLocked(activeSyncContext.mSyncOperation.account, activeSyncContext.mSyncOperation.userId, activeSyncContext.mSyncOperation.authority, -1, true);
         syncInfo = new VSyncInfo(authority.ident, authority.account, authority.authority, activeSyncContext.mStartTime);
         this.getCurrentSyncs(authority.userId).add(syncInfo);
      }

      this.reportActiveChange();
      return syncInfo;
   }

   public void removeActiveSync(VSyncInfo syncInfo, int userId) {
      synchronized(this.mAuthorities) {
         this.getCurrentSyncs(userId).remove(syncInfo);
      }

      this.reportActiveChange();
   }

   public void reportActiveChange() {
      this.reportChange(4);
   }

   public long insertStartSyncEvent(Account accountName, int userId, int reason, String authorityName, long now, int source, boolean initialization, Bundle extras) {
      long id;
      synchronized(this.mAuthorities) {
         AuthorityInfo authority = this.getAuthorityLocked(accountName, userId, authorityName, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcKWgaFgZpJwo7Iz42AWgjMCljDiQgLC0qVg==")));
         if (authority == null) {
            return -1L;
         }

         SyncHistoryItem item = new SyncHistoryItem();
         item.initialization = initialization;
         item.authorityId = authority.ident;
         item.historyId = this.mNextHistoryId++;
         if (this.mNextHistoryId < 0) {
            this.mNextHistoryId = 0;
         }

         item.eventTime = now;
         item.source = source;
         item.reason = reason;
         item.extras = extras;
         item.event = 0;
         this.mSyncHistory.add(0, item);

         while(true) {
            if (this.mSyncHistory.size() <= 100) {
               id = (long)item.historyId;
               break;
            }

            this.mSyncHistory.remove(this.mSyncHistory.size() - 1);
         }
      }

      this.reportChange(8);
      return id;
   }

   public void stopSyncEvent(long historyId, long elapsedTime, String resultMessage, long downstreamActivity, long upstreamActivity) {
      synchronized(this.mAuthorities) {
         SyncHistoryItem item = null;

         for(int i = this.mSyncHistory.size(); i > 0; item = null) {
            --i;
            item = (SyncHistoryItem)this.mSyncHistory.get(i);
            if ((long)item.historyId == historyId) {
               break;
            }
         }

         if (item == null) {
            Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qD28ILD9gNCgVLD0MDmU0IyhlNxkrIwgYD2YaAjVrCiAvKQdeJGoFMzQ=")) + historyId);
            return;
         }

         item.elapsedTime = elapsedTime;
         item.event = 1;
         item.mesg = resultMessage;
         item.downstreamActivity = downstreamActivity;
         item.upstreamActivity = upstreamActivity;
         SyncStatusInfo status = this.getOrCreateSyncStatusLocked(item.authorityId);
         ++status.numSyncs;
         status.totalElapsedTime += elapsedTime;
         switch (item.source) {
            case 0:
               ++status.numSourceServer;
               break;
            case 1:
               ++status.numSourceLocal;
               break;
            case 2:
               ++status.numSourcePoll;
               break;
            case 3:
               ++status.numSourceUser;
               break;
            case 4:
               ++status.numSourcePeriodic;
         }

         boolean writeStatisticsNow = false;
         int day = this.getCurrentDayLocked();
         if (this.mDayStats[0] == null) {
            this.mDayStats[0] = new DayStats(day);
         } else if (day != this.mDayStats[0].day) {
            System.arraycopy(this.mDayStats, 0, this.mDayStats, 1, this.mDayStats.length - 1);
            this.mDayStats[0] = new DayStats(day);
            writeStatisticsNow = true;
         } else if (this.mDayStats[0] == null) {
         }

         DayStats ds = this.mDayStats[0];
         long lastSyncTime = item.eventTime + elapsedTime;
         boolean writeStatusNow = false;
         if (!MESG_SUCCESS.equals(resultMessage)) {
            if (!MESG_CANCELED.equals(resultMessage)) {
               if (status.lastFailureTime == 0L) {
                  writeStatusNow = true;
               }

               status.lastFailureTime = lastSyncTime;
               status.lastFailureSource = item.source;
               status.lastFailureMesg = resultMessage;
               if (status.initialFailureTime == 0L) {
                  status.initialFailureTime = lastSyncTime;
               }

               ++ds.failureCount;
               ds.failureTime += elapsedTime;
            }
         } else {
            if (status.lastSuccessTime == 0L || status.lastFailureTime != 0L) {
               writeStatusNow = true;
            }

            status.lastSuccessTime = lastSyncTime;
            status.lastSuccessSource = item.source;
            status.lastFailureTime = 0L;
            status.lastFailureSource = -1;
            status.lastFailureMesg = null;
            status.initialFailureTime = 0L;
            ++ds.successCount;
            ds.successTime += elapsedTime;
         }

         if (writeStatusNow) {
            this.writeStatusLocked();
         } else if (!this.hasMessages(1)) {
            this.sendMessageDelayed(this.obtainMessage(1), 600000L);
         }

         if (writeStatisticsNow) {
            this.writeStatisticsLocked();
         } else if (!this.hasMessages(2)) {
            this.sendMessageDelayed(this.obtainMessage(2), 1800000L);
         }
      }

      this.reportChange(8);
   }

   private List<VSyncInfo> getCurrentSyncs(int userId) {
      synchronized(this.mAuthorities) {
         return this.getCurrentSyncsLocked(userId);
      }
   }

   public List<VSyncInfo> getCurrentSyncsCopy(int userId) {
      synchronized(this.mAuthorities) {
         List<VSyncInfo> syncs = this.getCurrentSyncsLocked(userId);
         List<VSyncInfo> syncsCopy = new ArrayList();
         Iterator var5 = syncs.iterator();

         while(var5.hasNext()) {
            VSyncInfo sync = (VSyncInfo)var5.next();
            syncsCopy.add(new VSyncInfo(sync));
         }

         return syncsCopy;
      }
   }

   private List<VSyncInfo> getCurrentSyncsLocked(int userId) {
      ArrayList<VSyncInfo> syncs = (ArrayList)this.mCurrentSyncs.get(userId);
      if (syncs == null) {
         syncs = new ArrayList();
         this.mCurrentSyncs.put(userId, syncs);
      }

      return syncs;
   }

   public ArrayList<SyncStatusInfo> getSyncStatus() {
      synchronized(this.mAuthorities) {
         int N = this.mSyncStatus.size();
         ArrayList<SyncStatusInfo> ops = new ArrayList(N);

         for(int i = 0; i < N; ++i) {
            ops.add((SyncStatusInfo)this.mSyncStatus.valueAt(i));
         }

         return ops;
      }
   }

   public Pair<AuthorityInfo, SyncStatusInfo> getCopyOfAuthorityWithSyncStatus(Account account, int userId, String authority) {
      synchronized(this.mAuthorities) {
         AuthorityInfo authorityInfo = this.getOrCreateAuthorityLocked(account, userId, authority, -1, true);
         return this.createCopyPairOfAuthorityWithSyncStatusLocked(authorityInfo);
      }
   }

   public ArrayList<Pair<AuthorityInfo, SyncStatusInfo>> getCopyOfAllAuthoritiesWithSyncStatus() {
      synchronized(this.mAuthorities) {
         ArrayList<Pair<AuthorityInfo, SyncStatusInfo>> infos = new ArrayList(this.mAuthorities.size());

         for(int i = 0; i < this.mAuthorities.size(); ++i) {
            infos.add(this.createCopyPairOfAuthorityWithSyncStatusLocked((AuthorityInfo)this.mAuthorities.valueAt(i)));
         }

         return infos;
      }
   }

   public SyncStatusInfo getStatusByAccountAndAuthority(Account account, int userId, String authority) {
      if (account != null && authority != null) {
         synchronized(this.mAuthorities) {
            int N = this.mSyncStatus.size();

            for(int i = 0; i < N; ++i) {
               SyncStatusInfo cur = (SyncStatusInfo)this.mSyncStatus.valueAt(i);
               AuthorityInfo ainfo = (AuthorityInfo)this.mAuthorities.get(cur.authorityId);
               if (ainfo != null && ainfo.authority.equals(authority) && ainfo.userId == userId && account.equals(ainfo.account)) {
                  return cur;
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public boolean isSyncPending(Account account, int userId, String authority) {
      synchronized(this.mAuthorities) {
         int N = this.mSyncStatus.size();

         for(int i = 0; i < N; ++i) {
            SyncStatusInfo cur = (SyncStatusInfo)this.mSyncStatus.valueAt(i);
            AuthorityInfo ainfo = (AuthorityInfo)this.mAuthorities.get(cur.authorityId);
            if (ainfo != null && userId == ainfo.userId && (account == null || ainfo.account.equals(account)) && ainfo.authority.equals(authority) && cur.pending) {
               return true;
            }
         }

         return false;
      }
   }

   public ArrayList<SyncHistoryItem> getSyncHistory() {
      synchronized(this.mAuthorities) {
         int N = this.mSyncHistory.size();
         ArrayList<SyncHistoryItem> items = new ArrayList(N);

         for(int i = 0; i < N; ++i) {
            items.add((SyncHistoryItem)this.mSyncHistory.get(i));
         }

         return items;
      }
   }

   public DayStats[] getDayStatistics() {
      synchronized(this.mAuthorities) {
         DayStats[] ds = new DayStats[this.mDayStats.length];
         System.arraycopy(this.mDayStats, 0, ds, 0, ds.length);
         return ds;
      }
   }

   private Pair<AuthorityInfo, SyncStatusInfo> createCopyPairOfAuthorityWithSyncStatusLocked(AuthorityInfo authorityInfo) {
      SyncStatusInfo syncStatusInfo = this.getOrCreateSyncStatusLocked(authorityInfo.ident);
      return Pair.create(new AuthorityInfo(authorityInfo), new SyncStatusInfo(syncStatusInfo));
   }

   private int getCurrentDayLocked() {
      this.mCal.setTimeInMillis(System.currentTimeMillis());
      int dayOfYear = this.mCal.get(6);
      if (this.mYear != this.mCal.get(1)) {
         this.mYear = this.mCal.get(1);
         this.mCal.clear();
         this.mCal.set(1, this.mYear);
         this.mYearInDays = (int)(this.mCal.getTimeInMillis() / 86400000L);
      }

      return dayOfYear + this.mYearInDays;
   }

   private AuthorityInfo getAuthorityLocked(Account accountName, int userId, String authorityName, String tag) {
      AccountAndUser au = new AccountAndUser(accountName, userId);
      AccountInfo accountInfo = (AccountInfo)this.mAccounts.get(au);
      if (accountInfo == null) {
         if (tag != null) {
         }

         return null;
      } else {
         AuthorityInfo authority = (AuthorityInfo)accountInfo.authorities.get(authorityName);
         if (authority == null) {
            if (tag != null) {
            }

            return null;
         } else {
            return authority;
         }
      }
   }

   private AuthorityInfo getAuthorityLocked(ComponentName service, int userId, String tag) {
      AuthorityInfo authority = (AuthorityInfo)((SparseArray)this.mServices.get(service)).get(userId);
      if (authority == null) {
         if (tag != null) {
         }

         return null;
      } else {
         return authority;
      }
   }

   private AuthorityInfo getOrCreateAuthorityLocked(ComponentName cname, int userId, int ident, boolean doWrite) {
      SparseArray<AuthorityInfo> aInfo = (SparseArray)this.mServices.get(cname);
      if (aInfo == null) {
         aInfo = new SparseArray();
         this.mServices.put(cname, aInfo);
      }

      AuthorityInfo authority = (AuthorityInfo)aInfo.get(userId);
      if (authority == null) {
         if (ident < 0) {
            ident = this.mNextAuthorityId++;
            doWrite = true;
         }

         authority = new AuthorityInfo(cname, userId, ident);
         aInfo.put(userId, authority);
         this.mAuthorities.put(ident, authority);
         if (doWrite) {
            this.writeAccountInfoLocked();
         }
      }

      return authority;
   }

   private AuthorityInfo getOrCreateAuthorityLocked(Account accountName, int userId, String authorityName, int ident, boolean doWrite) {
      AccountAndUser au = new AccountAndUser(accountName, userId);
      AccountInfo account = (AccountInfo)this.mAccounts.get(au);
      if (account == null) {
         account = new AccountInfo(au);
         this.mAccounts.put(au, account);
      }

      AuthorityInfo authority = (AuthorityInfo)account.authorities.get(authorityName);
      if (authority == null) {
         if (ident < 0) {
            ident = this.mNextAuthorityId++;
            doWrite = true;
         }

         authority = new AuthorityInfo(accountName, userId, authorityName, ident);
         account.authorities.put(authorityName, authority);
         this.mAuthorities.put(ident, authority);
         if (doWrite) {
            this.writeAccountInfoLocked();
         }
      }

      return authority;
   }

   private void removeAuthorityLocked(Account account, int userId, String authorityName, boolean doWrite) {
      AccountInfo accountInfo = (AccountInfo)this.mAccounts.get(new AccountAndUser(account, userId));
      if (accountInfo != null) {
         AuthorityInfo authorityInfo = (AuthorityInfo)accountInfo.authorities.remove(authorityName);
         if (authorityInfo != null) {
            this.mAuthorities.remove(authorityInfo.ident);
            if (doWrite) {
               this.writeAccountInfoLocked();
            }
         }
      }

   }

   public void setPeriodicSyncTime(int authorityId, PeriodicSync targetPeriodicSync, long when) {
      boolean found = false;
      AuthorityInfo authorityInfo;
      synchronized(this.mAuthorities) {
         authorityInfo = (AuthorityInfo)this.mAuthorities.get(authorityId);
         int i = 0;

         while(i < authorityInfo.periodicSyncs.size()) {
            PeriodicSync periodicSync = (PeriodicSync)authorityInfo.periodicSyncs.get(i);
            if (!targetPeriodicSync.equals(periodicSync)) {
               ++i;
            } else {
               ((SyncStatusInfo)this.mSyncStatus.get(authorityId)).setPeriodicSyncTime(i, when);
               found = true;
               break;
            }
         }
      }

      if (!found) {
         Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgmCGowFi9gNDs8Iy0MLmIzGgRqAQYvIxg2UmcKRSZiHhocLypXCGsKJC9oHjAZDRc6JmU0PDV+NB4gIz41OGwFRTdmVyQwKi0ML34zMCVvVjwgLwgYD2ZTRCNhAQo9Kj4uCGoKMCt5IzxF")) + authorityInfo.authority);
      }

   }

   private SyncStatusInfo getOrCreateSyncStatusLocked(int authorityId) {
      SyncStatusInfo status = (SyncStatusInfo)this.mSyncStatus.get(authorityId);
      if (status == null) {
         status = new SyncStatusInfo(authorityId);
         this.mSyncStatus.put(authorityId, status);
      }

      return status;
   }

   public void writeAllState() {
      synchronized(this.mAuthorities) {
         if (this.mNumPendingFinished > 0) {
            this.writePendingOperationsLocked();
         }

         this.writeStatusLocked();
         this.writeStatisticsLocked();
      }
   }

   public void clearAndReadState() {
      synchronized(this.mAuthorities) {
         this.mAuthorities.clear();
         this.mAccounts.clear();
         this.mServices.clear();
         this.mPendingOperations.clear();
         this.mSyncStatus.clear();
         this.mSyncHistory.clear();
         this.readAccountInfoLocked();
         this.readStatusLocked();
         this.readPendingOperationsLocked();
         this.readStatisticsLocked();
         this.readAndDeleteLegacyAccountInfoLocked();
         this.writeAccountInfoLocked();
         this.writeStatusLocked();
         this.writePendingOperationsLocked();
         this.writeStatisticsLocked();
      }
   }

   private void readAccountInfoLocked() {
      int highestAuthorityId = -1;
      FileInputStream fis = null;

      label313: {
         try {
            fis = this.mAccountInfoFile.openRead();
            Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij4uP2gFAiZiICRF")) + this.mAccountInfoFile.getBaseFile());
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, (String)null);

            int eventType;
            for(eventType = parser.getEventType(); eventType != 2; eventType = parser.next()) {
            }

            String tagName = parser.getName();
            if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEShF")).equals(tagName)) {
               String listen = parser.getAttributeValue((String)null, XML_ATTR_LISTEN_FOR_TICKLES);
               String versionString = parser.getAttributeValue((String)null, XML_ATTR_VERSION);

               int version;
               try {
                  version = versionString == null ? 0 : Integer.parseInt(versionString);
               } catch (NumberFormatException var28) {
                  version = 0;
               }

               String nextIdString = parser.getAttributeValue((String)null, XML_ATTR_NEXT_AUTHORITY_ID);

               try {
                  int id = nextIdString == null ? 0 : Integer.parseInt(nextIdString);
                  this.mNextAuthorityId = Math.max(this.mNextAuthorityId, id);
               } catch (NumberFormatException var27) {
               }

               String offsetString = parser.getAttributeValue((String)null, XML_ATTR_SYNC_RANDOM_OFFSET);

               try {
                  this.mSyncRandomOffset = offsetString == null ? 0 : Integer.parseInt(offsetString);
               } catch (NumberFormatException var26) {
                  this.mSyncRandomOffset = 0;
               }

               if (this.mSyncRandomOffset == 0) {
                  Random random = new Random(System.currentTimeMillis());
                  this.mSyncRandomOffset = random.nextInt(86400);
               }

               this.mMasterSyncAutomatically.put(0, listen == null || Boolean.parseBoolean(listen));
               eventType = parser.next();
               AuthorityInfo authority = null;
               PeriodicSync periodicSync = null;

               do {
                  if (eventType == 2) {
                     tagName = parser.getName();
                     if (parser.getDepth() == 2) {
                        if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZ")).equals(tagName)) {
                           authority = this.parseAuthority(parser, version);
                           periodicSync = null;
                           if (authority.ident > highestAuthorityId) {
                              highestAuthorityId = authority.ident;
                           }
                        } else if (XML_TAG_LISTEN_FOR_TICKLES.equals(tagName)) {
                           this.parseListenForTickles(parser);
                        }
                     } else if (parser.getDepth() == 3) {
                        if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmUVGixjDigPLQcYPw==")).equals(tagName) && authority != null) {
                           periodicSync = this.parsePeriodicSync(parser, authority);
                        }
                     } else if (parser.getDepth() == 4 && periodicSync != null && StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfLG8jJFo=")).equals(tagName)) {
                        this.parseExtra(parser, periodicSync.extras);
                     }
                  }

                  eventType = parser.next();
               } while(eventType != 1);
            }
            break label313;
         } catch (XmlPullParserException var29) {
            XmlPullParserException e = var29;
            Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShhNDA7KBccDmkJTTdoJzAcKhgcCmEjSFo=")), e);
         } catch (IOException var30) {
            IOException e = var30;
            if (fis == null) {
               Log.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOGUVBi9mHgY7Kl4mOW4FAiVvARo/KT5SVg==")));
               return;
            }

            Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShhNDA7KBccDmkJTTdoJzAcKhgcCmEjSFo=")), e);
            return;
         } finally {
            this.mNextAuthorityId = Math.max(highestAuthorityId + 1, this.mNextAuthorityId);
            if (fis != null) {
               try {
                  fis.close();
               } catch (IOException var25) {
               }
            }

         }

         return;
      }

      this.maybeMigrateSettingsForRenamedAuthorities();
   }

   private void maybeDeleteLegacyPendingInfoLocked(File syncDir) {
      File file = new File(syncDir, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguCGgFAiZiIFk6KQcYVg==")));
      if (file.exists()) {
         file.delete();
      }
   }

   private boolean maybeMigrateSettingsForRenamedAuthorities() {
      boolean writeNeeded = false;
      ArrayList<AuthorityInfo> authoritiesToRemove = new ArrayList();
      int N = this.mAuthorities.size();

      AuthorityInfo authority;
      for(int i = 0; i < N; ++i) {
         authority = (AuthorityInfo)this.mAuthorities.valueAt(i);
         String newAuthorityName = (String)sAuthorityRenames.get(authority.authority);
         if (newAuthorityName != null) {
            authoritiesToRemove.add(authority);
            if (authority.enabled && this.getAuthorityLocked(authority.account, authority.userId, newAuthorityName, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EM2sVBgVhEVRF"))) == null) {
               AuthorityInfo newAuthority = this.getOrCreateAuthorityLocked(authority.account, authority.userId, newAuthorityName, -1, false);
               newAuthority.enabled = true;
               writeNeeded = true;
            }
         }
      }

      for(Iterator var8 = authoritiesToRemove.iterator(); var8.hasNext(); writeNeeded = true) {
         authority = (AuthorityInfo)var8.next();
         this.removeAuthorityLocked(authority.account, authority.userId, authority.authority, false);
      }

      return writeNeeded;
   }

   private void parseListenForTickles(XmlPullParser parser) {
      String user = parser.getAttributeValue((String)null, XML_ATTR_USER);
      int userId = 0;

      try {
         userId = Integer.parseInt(user);
      } catch (NumberFormatException var6) {
         NumberFormatException e = var6;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowEShhHiAqIy0cDmkJTQZqESsrKhc2J2E0OCFsJyspKT42D28VNAR6ESQ6JzlbM28KMAVvNwYc")), e);
      } catch (NullPointerException var7) {
         NullPointerException e = var7;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRhfM3sKNANiAS88KQcXOm8zLANvESgbPBgiKWE0QT9vDiwaKT4AD3gVAiV7ARoaJhdXVg==")), e);
      }

      String enabled = parser.getAttributeValue((String)null, XML_ATTR_ENABLED);
      boolean listen = enabled == null || Boolean.parseBoolean(enabled);
      this.mMasterSyncAutomatically.put(userId, listen);
   }

   private AuthorityInfo parseAuthority(XmlPullParser parser, int version) {
      AuthorityInfo authority = null;
      int id = -1;

      try {
         id = Integer.parseInt(parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgqVg=="))));
      } catch (NumberFormatException var14) {
         NumberFormatException e = var14;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowEShhHiAqIy0cDmkJTQZqESsrIxgpJGAgIyNqHgYuOD5bCW8VRQNvJx4ZIQhSVg==")), e);
      } catch (NullPointerException var15) {
         NullPointerException e = var15;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRhfM3sFAixLHh4+Pxg2MmknTTdvDiwZLD0MI2YVBSNvAS8pKRcAKGUVSFo=")), e);
      }

      if (id >= 0) {
         String authorityName = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZ")));
         String enabled = parser.getAttributeValue((String)null, XML_ATTR_ENABLED);
         String syncable = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGszJCpgHjBF")));
         String accountName = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEVRF")));
         String accountType = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")));
         String user = parser.getAttributeValue((String)null, XML_ATTR_USER);
         String packageName = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iAVRF")));
         String className = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EP28wLFo=")));
         int userId = user == null ? 0 : Integer.parseInt(user);
         if (accountType == null) {
            accountType = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAhSVg=="));
            syncable = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gN1RF"));
         }

         authority = (AuthorityInfo)this.mAuthorities.get(id);
         Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JggqPGUVBi1LHiAvLBcADWoVLAZuClArLRg2JWAjLClqVlFF")) + accountName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg+I2wFRDM=")) + authorityName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcuKWgaETM=")) + userId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhguCGsVFiRiDg0d")) + enabled + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phc2J2ojLDd9NFE/PghSVg==")) + syncable);
         if (authority == null) {
            Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji0MM2saMC9gNDs8KAcYLmoaLFo=")));
            if (accountName != null && accountType != null) {
               authority = this.getOrCreateAuthorityLocked(new Account(accountName, accountType), userId, authorityName, id, false);
            } else {
               authority = this.getOrCreateAuthorityLocked(new ComponentName(packageName, className), userId, id, false);
            }

            if (version > 0) {
               authority.periodicSyncs.clear();
            }
         }

         if (authority != null) {
            authority.enabled = enabled == null || Boolean.parseBoolean(enabled);
            if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gN1RF")).equals(syncable)) {
               authority.syncable = -1;
            } else {
               authority.syncable = syncable != null && !Boolean.parseBoolean(syncable) ? 0 : 1;
            }
         } else {
            Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4+CWoKNARiCiQ7KBc2MW8VHShoDig/IwgACGMFFgZ7MCAqKAgYKW8FBiB+EVRF")) + accountName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg+I2wFRDM=")) + authorityName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhguCGsVFiRiDg0d")) + enabled + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phc2J2ojLDd9NFE/PghSVg==")) + syncable);
         }
      }

      return authority;
   }

   private PeriodicSync parsePeriodicSync(XmlPullParser parser, AuthorityInfo authority) {
      Bundle extras = new Bundle();
      String periodValue = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmUVGiw=")));
      String flexValue = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4EM2kFSFo=")));

      long period;
      try {
         period = Long.parseLong(periodValue);
      } catch (NumberFormatException var13) {
         NumberFormatException e = var13;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowEShhHiAqIy0cDmkJTQZqESsrKQguCGMKAi95HlkvOD5aJGwVNCZlEQYzJQctL2UjBgJuAVRF")), e);
         return null;
      } catch (NullPointerException var14) {
         NullPointerException e = var14;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRhfM3sKICthNAY1KF4mDWlSTTd4HjwgKS4YKWIaGiZ5ESwyKRgXJGoKLzRqJCg7JhhSVg==")), e);
         return null;
      }

      long flextime;
      try {
         flextime = Long.parseLong(flexValue);
      } catch (NumberFormatException var11) {
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShiNB4qKgciLmUzLCZrIzw9LRgECWIOODNpASg8Ly4bJGszGiZ7Djw0Jz0AJm4aBjN+NB4gIz41OGgjHitnVgU8")) + flexValue);
         flextime = calculateDefaultFlexTime(period);
      } catch (NullPointerException var12) {
         flextime = calculateDefaultFlexTime(period);
         Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOGgjHitnVyQgKQdXPX4wAgJrATAaLi4YJ2JTOCFsJyspIz4MI2wnICVpERo2CF4iPGUgBgJpDVEoPhgqM2gjJAVgEQ02PxgmPWoVLCVrV1Ar")) + period + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgiDmgaRDJLEVRF")) + flextime);
      }

      PeriodicSync periodicSync = new PeriodicSync(authority.account, authority.authority, extras, period);
      mirror.android.content.PeriodicSync.flexTime.set(periodicSync, flextime);
      authority.periodicSyncs.add(periodicSync);
      return periodicSync;
   }

   private void parseExtra(XmlPullParser parser, Bundle extras) {
      String name = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWgVSFo=")));
      String type = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")));
      String value1 = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwE=")));
      String value2 = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwQ=")));

      try {
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgACGgzSFo=")).equals(type)) {
            extras.putLong(name, Long.parseLong(value1));
         } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVPCthN1RF")).equals(type)) {
            extras.putInt(name, Integer.parseInt(value1));
         } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgAI2sjHis=")).equals(type)) {
            extras.putDouble(name, Double.parseDouble(value1));
         } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4ED2saMFo=")).equals(type)) {
            extras.putFloat(name, Float.parseFloat(value1));
         } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgN1RF")).equals(type)) {
            extras.putBoolean(name, Boolean.parseBoolean(value1));
         } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qKmUVBi0=")).equals(type)) {
            extras.putString(name, value1);
         } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEVRF")).equals(type)) {
            extras.putParcelable(name, new Account(value1, value2));
         }
      } catch (NumberFormatException var8) {
         NumberFormatException e = var8;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowEShhHiAqIy0cDmkJTSpvARovLAgtJGYwPDdqDgpF")), e);
      } catch (NullPointerException var9) {
         NullPointerException e = var9;
         Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowEShhHiAqIy0cDmkJTSpvARovLAgtJGYwPDdqDgpF")), e);
      }

   }

   private void writeAccountInfoLocked() {
      Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IS0MCWwFAiZiICQ2KAg5Og==")) + this.mAccountInfoFile.getBaseFile());
      FileOutputStream fos = null;

      try {
         fos = this.mAccountInfoFile.startWrite();
         XmlSerializer out = new FastXmlSerializer();
         out.setOutput(fos, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcqPnpTRVo=")));
         out.startDocument((String)null, true);
         out.setFeature(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8OTCVOJxo3KhgmLW8zOyZlJAouPD0hDU4gFippIFkvLy5bCm8KFj9vMxo/IBdbO3kgBgJpNwY5KV8ID2waMAJmAQpF")), true);
         out.startTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEShF")));
         out.attribute((String)null, XML_ATTR_VERSION, Integer.toString(2));
         out.attribute((String)null, XML_ATTR_NEXT_AUTHORITY_ID, Integer.toString(this.mNextAuthorityId));
         out.attribute((String)null, XML_ATTR_SYNC_RANDOM_OFFSET, Integer.toString(this.mSyncRandomOffset));
         int M = this.mMasterSyncAutomatically.size();

         int N;
         int i;
         for(N = 0; N < M; ++N) {
            i = this.mMasterSyncAutomatically.keyAt(N);
            Boolean listen = (Boolean)this.mMasterSyncAutomatically.valueAt(N);
            out.startTag((String)null, XML_TAG_LISTEN_FOR_TICKLES);
            out.attribute((String)null, XML_ATTR_USER, Integer.toString(i));
            out.attribute((String)null, XML_ATTR_ENABLED, Boolean.toString(listen));
            out.endTag((String)null, XML_TAG_LISTEN_FOR_TICKLES);
         }

         N = this.mAuthorities.size();

         for(i = 0; i < N; ++i) {
            AuthorityInfo authority = (AuthorityInfo)this.mAuthorities.valueAt(i);
            out.startTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZ")));
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgqVg==")), Integer.toString(authority.ident));
            out.attribute((String)null, XML_ATTR_USER, Integer.toString(authority.userId));
            out.attribute((String)null, XML_ATTR_ENABLED, Boolean.toString(authority.enabled));
            if (authority.service == null) {
               out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEVRF")), authority.account.name);
               out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")), authority.account.type);
               out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZ")), authority.authority);
            } else {
               out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+OWUzJC1iAVRF")), authority.service.getPackageName());
               out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EP28wLFo=")), authority.service.getClassName());
            }

            if (authority.syncable < 0) {
               out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGszJCpgHjBF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gN1RF")));
            } else {
               out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGszJCpgHjBF")), Boolean.toString(authority.syncable != 0));
            }

            Iterator var7 = authority.periodicSyncs.iterator();

            while(var7.hasNext()) {
               PeriodicSync periodicSync = (PeriodicSync)var7.next();
               out.startTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmUVGixjDigPLQcYPw==")));
               out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmUVGiw=")), Long.toString(periodicSync.period));
               long flexTime = mirror.android.content.PeriodicSync.flexTime.get(periodicSync);
               out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4EM2kFSFo=")), Long.toString(flexTime));
               Bundle extras = periodicSync.extras;
               this.extrasToXml(out, extras);
               out.endTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmUVGixjDigPLQcYPw==")));
            }

            out.endTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZ")));
         }

         out.endTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEShF")));
         out.endDocument();
         this.mAccountInfoFile.finishWrite(fos);
      } catch (IOException var12) {
         IOException e1 = var12;
         Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShmJywzLBccDmkJTTdoJzAcKhgcCmEjSFo=")), e1);
         if (fos != null) {
            this.mAccountInfoFile.failWrite(fos);
         }
      }

   }

   static int getIntColumn(Cursor c, String name) {
      return c.getInt(c.getColumnIndex(name));
   }

   static long getLongColumn(Cursor c, String name) {
      return c.getLong(c.getColumnIndex(name));
   }

   private void readAndDeleteLegacyAccountInfoLocked() {
      File file = this.mContext.getDatabasePath(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGszEjdgNCA9KAgtDmkzRVo=")));
      if (file.exists()) {
         String path = file.getPath();
         SQLiteDatabase db = null;

         try {
            db = SQLiteDatabase.openDatabase(path, (SQLiteDatabase.CursorFactory)null, 1);
         } catch (SQLiteException var15) {
         }

         if (db != null) {
            boolean hasType = db.getVersion() >= 11;
            Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij4uP2gFAiZiICQoKAc6OW4KLyhsJB4bLTo6O30gNCpqDh49LARXIGgzSFo=")));
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKLyRLESggLwg2LWoFSFo=")));
            HashMap<String, String> map = new HashMap();
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4YPA==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKNANONR4zKF4mOWoJTR9qASxF")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKLyZ9Dig5Ki4MDmU3TTdsIzwsLT42KWYKRT8=")));
            if (hasType) {
               map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHx4gLQgmPQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKLyZ9Dig5Ki4MDmU2NAZuDjwgPQg+D0saPCZpJFk+KRccE28aAiRoEVRF")));
            }

            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZ")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKLyZ9ATAgKRdfKGwgBj94ETg6PQg+CWYaBiplNBo9Li5SVg==")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgALGsVHhVgHiAsIy0MPn0zLCNrAVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgALGsVHhVgHiAsIy0MPn0zLCNrAVRF")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWcwAiZ9JyhF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWcwAiZ9JyhF")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/IhdfP24jOFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/IhdfP24jOFo=")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/OxdfCG8zSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/OxdfCG8zSFo=")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/Oy0MKGUVGgQ=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/Oy0MKGUVGgQ=")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/JAgqPWoVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/JAgqPWoVSFo=")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwILAV9JCg/Iy4qAW8KGgRoJyhF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwILAV9JCg/Iy4qAW8KGgRoJyhF")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwILAV9JCg/Iy4qAGwjPCs=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwILAV9JCg/Iy4qAGwjPCs=")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLODdjDlEvIz0MAW8KGgRoJyhF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLODdjDlEvIz0MAW8KGgRoJyhF")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLODdjDlEvIz0MAGwjPCs=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLODdjDlEvIz0MAGwjPCs=")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLODdjDlEvIz0MUmkgAi0=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLODdjDlEvIz0MUmkgAi0=")));
            map.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguCGgFAiZiJ1RF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguCGgFAiZiJ1RF")));
            qb.setProjectionMap(map);
            qb.appendWhere(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKLyZsJAYwP19WOmoKBjdvHig6PC02Cn0FFjZgJBot")));
            Cursor c = qb.query(db, (String[])null, (String)null, (String[])null, (String)null, (String)null, (String)null);

            while(true) {
               AuthorityInfo authority;
               do {
                  String name;
                  String value;
                  String provider;
                  if (!c.moveToNext()) {
                     c.close();
                     qb = new SQLiteQueryBuilder();
                     qb.setTables(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGwFAiZiJyhF")));
                     c = qb.query(db, (String[])null, (String)null, (String[])null, (String)null, (String)null, (String)null);

                     while(true) {
                        label98:
                        while(true) {
                           do {
                              if (!c.moveToNext()) {
                                 c.close();
                                 db.close();
                                 (new File(path)).delete();
                                 return;
                              }

                              name = c.getString(c.getColumnIndex(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWgVSFo="))));
                              value = c.getString(c.getColumnIndex(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNFo="))));
                           } while(name == null);

                           if (name.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYKWwFNCZsJDw1IzxfLmwjAiFlESg6")))) {
                              this.setMasterSyncAutomatically(value == null || Boolean.parseBoolean(value), 0);
                           } else if (name.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGs2GgJhNB4uKQc2PWoYNFo=")))) {
                              provider = name.substring(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGs2GgJhNB4uKQc2PWoYNFo=")).length(), name.length());
                              int i = this.mAuthorities.size();

                              while(true) {
                                 AuthorityInfo authority;
                                 do {
                                    if (i <= 0) {
                                       continue label98;
                                    }

                                    --i;
                                    authority = (AuthorityInfo)this.mAuthorities.valueAt(i);
                                 } while(!authority.authority.equals(provider));

                                 authority.enabled = value == null || Boolean.parseBoolean(value);
                                 authority.syncable = 1;
                              }
                           }
                        }
                     }
                  }

                  name = c.getString(c.getColumnIndex(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEVRF"))));
                  value = hasType ? c.getString(c.getColumnIndex(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHx4gLQgmPQ==")))) : null;
                  if (value == null) {
                     value = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAhSVg=="));
                  }

                  provider = c.getString(c.getColumnIndex(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZ"))));
                  authority = this.getOrCreateAuthorityLocked(new Account(name, value), 0, provider, -1, false);
               } while(authority == null);

               int i = this.mSyncStatus.size();
               boolean found = false;
               SyncStatusInfo st = null;

               while(i > 0) {
                  --i;
                  st = (SyncStatusInfo)this.mSyncStatus.valueAt(i);
                  if (st.authorityId == authority.ident) {
                     found = true;
                     break;
                  }
               }

               if (!found) {
                  st = new SyncStatusInfo(authority.ident);
                  this.mSyncStatus.put(authority.ident, st);
               }

               st.totalElapsedTime = getLongColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgALGsVHhVgHiAsIy0MPn0zLCNrAVRF")));
               st.numSyncs = getIntColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWcwAiZ9JyhF")));
               st.numSourceLocal = getIntColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/IhdfP24jOFo=")));
               st.numSourcePoll = getIntColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/OxdfCG8zSFo=")));
               st.numSourceServer = getIntColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/Oy0MKGUVGgQ=")));
               st.numSourceUser = getIntColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDWczGgVhNCg/JAgqPWoVSFo=")));
               st.numSourcePeriodic = 0;
               st.lastSuccessSource = getIntColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwILAV9JCg/Iy4qAW8KGgRoJyhF")));
               st.lastSuccessTime = getLongColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwILAV9JCg/Iy4qAGwjPCs=")));
               st.lastFailureSource = getIntColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLODdjDlEvIz0MAW8KGgRoJyhF")));
               st.lastFailureTime = getLongColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLODdjDlEvIz0MAGwjPCs=")));
               st.lastFailureMesg = c.getString(c.getColumnIndex(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLODdjDlEvIz0MUmkgAi0="))));
               st.pending = getIntColumn(c, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguCGgFAiZiJ1RF"))) != 0;
            }
         }
      }
   }

   private void readStatusLocked() {
      Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij4uP2gFAiZiICRF")) + this.mStatusFile.getBaseFile());

      try {
         byte[] data = this.mStatusFile.readFully();
         Parcel in = Parcel.obtain();
         in.unmarshall(data, 0, data.length);
         in.setDataPosition(0);

         int token;
         while((token = in.readInt()) != 0) {
            if (token != 100) {
               Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcMWojGj1gMCQpLBciLmUgDShvEQYiLhgbPksVSFo=")) + token);
               break;
            }

            SyncStatusInfo status = new SyncStatusInfo(in);
            if (this.mAuthorities.indexOfKey(status.authorityId) >= 0) {
               status.pending = false;
               Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JggqPGUVBi1LESggLwg2LWoJTS5lJA0rIxgpJA==")) + status.authorityId);
               this.mSyncStatus.put(status.authorityId, status);
            }
         }
      } catch (IOException var5) {
         Log.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOGUVBi9mHgY7Kl4mL2UzQQZvDjBF")));
      }

   }

   private void writeStatusLocked() {
      Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IS0MCWwFAiZiICQ2KAg5Og==")) + this.mStatusFile.getBaseFile());
      this.removeMessages(1);
      FileOutputStream fos = null;

      try {
         fos = this.mStatusFile.startWrite();
         Parcel out = Parcel.obtain();
         int N = this.mSyncStatus.size();

         for(int i = 0; i < N; ++i) {
            SyncStatusInfo status = (SyncStatusInfo)this.mSyncStatus.valueAt(i);
            out.writeInt(100);
            status.writeToParcel(out, 0);
         }

         out.writeInt(0);
         fos.write(out.marshall());
         out.recycle();
         this.mStatusFile.finishWrite(fos);
      } catch (IOException var6) {
         IOException e1 = var6;
         Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShmJywzLBccDmkJTQNvETg/Khc2Vg==")), e1);
         if (fos != null) {
            this.mStatusFile.failWrite(fos);
         }
      }

   }

   private void readPendingOperationsLocked() {
      FileInputStream fis = null;
      if (!this.mPendingFile.getBaseFile().exists()) {
         Log.v(TAG_FILE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOG8FNCZiHgY2KCkmDWozGgRoDiwaLD4bJGIwGjduCh5F")));
      } else {
         try {
            fis = this.mPendingFile.openRead();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, (String)null);

            int eventType;
            for(eventType = parser.getEventType(); eventType != 2 && eventType != 1; eventType = parser.next()) {
            }

            if (eventType == 1) {
               return;
            }

            String tagName = parser.getName();

            do {
               PendingOperation pop = null;
               if (eventType == 2) {
                  try {
                     tagName = parser.getName();
                     if (parser.getDepth() == 1 && StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy06Vg==")).equals(tagName)) {
                        String versionString = parser.getAttributeValue((String)null, XML_ATTR_VERSION);
                        if (versionString == null || Integer.parseInt(versionString) != 3) {
                           Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcMWojGj1gMCQsKAcYPmwjMC14EQY7LhcMO2YaGipsMCA/Ly1fD2oFGgR7AVRF")) + versionString);
                           throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcMWojGj1gMCQuKAguL2wjNCZ1N1RF")));
                        }

                        int authorityId = Integer.valueOf(parser.getAttributeValue((String)null, XML_ATTR_AUTHORITYID));
                        boolean expedited = Boolean.valueOf(parser.getAttributeValue((String)null, XML_ATTR_EXPEDITED));
                        int syncSource = Integer.valueOf(parser.getAttributeValue((String)null, XML_ATTR_SOURCE));
                        int reason = Integer.valueOf(parser.getAttributeValue((String)null, XML_ATTR_REASON));
                        AuthorityInfo authority = (AuthorityInfo)this.mAuthorities.get(authorityId);
                        Log.v(TAG_FILE, authorityId + " " + expedited + " " + syncSource + " " + reason);
                        if (authority != null) {
                           pop = new PendingOperation(authority.account, authority.userId, reason, syncSource, authority.authority, new Bundle(), expedited);
                           pop.flatExtras = null;
                           this.mPendingOperations.add(pop);
                           Log.v(TAG_FILE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JggqPGUVBi1LESQ/Kj02MW8VHShlJD8xPQhSVg==")) + pop.authority + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phc2Kms0Elo=")) + pop.syncSource + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcMM2saLCVgM11F")) + pop.reason + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhguIG8FNCxjAQo/KF9XVg==")) + pop.expedited);
                        } else {
                           pop = null;
                           Log.v(TAG_FILE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOGsaNAZjHh4qKQg2IX4zHiVvARovPQgiKWE0OFo=")) + authorityId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KWUzAgJhHgY2KC5SVg==")));
                        }
                     } else if (parser.getDepth() == 2 && pop != null && StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfLG8jJFo=")).equals(tagName)) {
                        this.parseExtra(parser, pop.extras);
                     }
                  } catch (NumberFormatException var24) {
                     NumberFormatException e = var24;
                     Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLmsVHi9iVyQwLwg2OX4zLCZ4HlkeLAQ6ImMKTSB8N1RF")), e);
                  }
               }

               eventType = parser.next();
            } while(eventType != 1);
         } catch (IOException var25) {
            IOException e = var25;
            Log.w(TAG_FILE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShhNDA7KBccDmkJTQJrARovIxgcIUsaFiRqHicd")), e);
         } catch (XmlPullParserException var26) {
            XmlPullParserException e = var26;
            Log.w(TAG_FILE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShhHiAqIy0cDmkJTQJrARovIxgcIUsaAjNlICAxKS4hKg==")), e);
         } finally {
            if (fis != null) {
               try {
                  fis.close();
               } catch (IOException var23) {
               }
            }

         }

      }
   }

   private void writePendingOperationsLocked() {
      int N = this.mPendingOperations.size();
      FileOutputStream fos = null;

      try {
         if (N == 0) {
            Log.v(TAG_FILE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRcMI2ojLDdmHgY2KCkmVg==")) + this.mPendingFile.getBaseFile());
            this.mPendingFile.truncate();
            return;
         }

         Log.v(TAG_FILE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IS0MCWwFAiZiICQ2KAg5Og==")) + this.mPendingFile.getBaseFile());
         fos = this.mPendingFile.startWrite();
         XmlSerializer out = new FastXmlSerializer();
         out.setOutput(fos, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcqPnpTRVo=")));

         for(int i = 0; i < N; ++i) {
            PendingOperation pop = (PendingOperation)this.mPendingOperations.get(i);
            this.writePendingOperationLocked(pop, out);
         }

         out.endDocument();
         this.mPendingFile.finishWrite(fos);
      } catch (IOException var6) {
         IOException e1 = var6;
         Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShmJywzLBccDmkJTQJrARovIxgcIUsaAjNuASgqIz42KWUwLFo=")), e1);
         if (fos != null) {
            this.mPendingFile.failWrite(fos);
         }
      }

   }

   private void writePendingOperationLocked(PendingOperation pop, XmlSerializer out) throws IOException {
      out.startTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy06Vg==")));
      out.attribute((String)null, XML_ATTR_VERSION, Integer.toString(3));
      out.attribute((String)null, XML_ATTR_AUTHORITYID, Integer.toString(pop.authorityId));
      out.attribute((String)null, XML_ATTR_SOURCE, Integer.toString(pop.syncSource));
      out.attribute((String)null, XML_ATTR_EXPEDITED, Boolean.toString(pop.expedited));
      out.attribute((String)null, XML_ATTR_REASON, Integer.toString(pop.reason));
      this.extrasToXml(out, pop.extras);
      out.endTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy06Vg==")));
   }

   private void appendPendingOperationLocked(PendingOperation op) {
      Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgc6KGgVBixjDlk9Pxg2DX4zSFo=")) + this.mPendingFile.getBaseFile());
      FileOutputStream fos = null;

      try {
         fos = this.mPendingFile.openAppend();
      } catch (IOException var15) {
         Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4+CWoFNCxLHiAsIxcMDmk0JyhvJAoaKggYKmIkOCFqDl0bOD4EI2UVNFo=")));
         this.writePendingOperationsLocked();
         return;
      }

      try {
         XmlSerializer out = new FastXmlSerializer();
         out.setOutput(fos, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcqPnpTRVo=")));
         this.writePendingOperationLocked(op, out);
         out.endDocument();
         this.mPendingFile.finishWrite(fos);
      } catch (IOException var13) {
         IOException e1 = var13;
         Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShmJywzLBccDmkJTTdsHjwgLC4qI2AwJyNsJyAuLBhbCmoFGgQ=")), e1);
         this.mPendingFile.failWrite(fos);
      } finally {
         try {
            fos.close();
         } catch (IOException var12) {
         }

      }

   }

   private static byte[] flattenBundle(Bundle bundle) {
      byte[] flatData = null;
      Parcel parcel = Parcel.obtain();

      byte[] flatData;
      try {
         bundle.writeToParcel(parcel, 0);
         flatData = parcel.marshall();
      } finally {
         parcel.recycle();
      }

      return flatData;
   }

   private static Bundle unflattenBundle(byte[] flatData) {
      Parcel parcel = Parcel.obtain();

      Bundle bundle;
      try {
         parcel.unmarshall(flatData, 0, flatData.length);
         parcel.setDataPosition(0);
         bundle = parcel.readBundle();
      } catch (RuntimeException var7) {
         bundle = new Bundle();
      } finally {
         parcel.recycle();
      }

      return bundle;
   }

   private void extrasToXml(XmlSerializer out, Bundle extras) throws IOException {
      for(Iterator var3 = extras.keySet().iterator(); var3.hasNext(); out.endTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfLG8jJFo=")))) {
         String key = (String)var3.next();
         out.startTag((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfLG8jJFo=")));
         out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWgVSFo=")), key);
         Object value = extras.get(key);
         if (value instanceof Long) {
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgACGgzSFo=")));
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwE=")), value.toString());
         } else if (value instanceof Integer) {
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVPCthN1RF")));
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwE=")), value.toString());
         } else if (value instanceof Boolean) {
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgN1RF")));
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwE=")), value.toString());
         } else if (value instanceof Float) {
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4ED2saMFo=")));
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwE=")), value.toString());
         } else if (value instanceof Double) {
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgAI2sjHis=")));
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwE=")), value.toString());
         } else if (value instanceof String) {
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qKmUVBi0=")));
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwE=")), value.toString());
         } else if (value instanceof Account) {
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEVRF")));
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwE=")), ((Account)value).name);
            out.attribute((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNwQ=")), ((Account)value).type);
         }
      }

   }

   private void requestSync(Account account, int userId, int reason, String authority, Bundle extras) {
      if (Process.myUid() == 1000 && this.mSyncRequestListener != null) {
         this.mSyncRequestListener.onSyncRequest(account, userId, reason, authority, extras);
      } else {
         ContentResolver.requestSync(account, authority, extras);
      }

   }

   private void readStatisticsLocked() {
      try {
         byte[] data = this.mStatisticsFile.readFully();
         Parcel in = Parcel.obtain();
         in.unmarshall(data, 0, data.length);
         in.setDataPosition(0);
         int index = 0;

         int token;
         while((token = in.readInt()) != 0) {
            if (token != 101 && token != 100) {
               Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcMWojGj1gMCQpLBciLmoJTQZlJ10gLClWJA==")) + token);
               break;
            }

            int day = in.readInt();
            if (token == 100) {
               day = day - 2009 + 14245;
            }

            DayStats ds = new DayStats(day);
            ds.successCount = in.readInt();
            ds.successTime = in.readLong();
            ds.failureCount = in.readInt();
            ds.failureTime = in.readLong();
            if (index < this.mDayStats.length) {
               this.mDayStats[index] = ds;
               ++index;
            }
         }
      } catch (IOException var7) {
         Log.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOGUVBi9mHgY7Kl4mL2UzQQZqDjA/Ixg2Dw==")));
      }

   }

   private void writeStatisticsLocked() {
      Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IS0MCWwFAiZiICQ2KAg5Og==")) + this.mStatisticsFile.getBaseFile());
      this.removeMessages(2);
      FileOutputStream fos = null;

      try {
         fos = this.mStatisticsFile.startWrite();
         Parcel out = Parcel.obtain();
         int N = this.mDayStats.length;

         for(int i = 0; i < N; ++i) {
            DayStats ds = this.mDayStats[i];
            if (ds == null) {
               break;
            }

            out.writeInt(101);
            out.writeInt(ds.day);
            out.writeInt(ds.successCount);
            out.writeLong(ds.successTime);
            out.writeInt(ds.failureCount);
            out.writeLong(ds.failureTime);
         }

         out.writeInt(0);
         fos.write(out.marshall());
         out.recycle();
         this.mStatisticsFile.finishWrite(fos);
      } catch (IOException var6) {
         IOException e1 = var6;
         Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQcMKmowEShmJywzLBccDmkJTQNvETg/KT5SVg==")), e1);
         if (fos != null) {
            this.mStatisticsFile.failWrite(fos);
         }
      }

   }

   public void dumpPendingOperations(StringBuilder sb) {
      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IhguCGgFAiZiICQLIxgpIH4zSFo="))).append(this.mPendingOperations.size()).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgAKGgaFjdmHgY1KjkAL3wlIFo=")));
      Iterator var2 = this.mPendingOperations.iterator();

      while(var2.hasNext()) {
         PendingOperation pop = (PendingOperation)var2.next();
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBhSVg==")) + pop.account).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Iw==")) + pop.userId).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg==")) + pop.authority).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg==")) + pop.extras).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAJXVg==")));
      }

   }

   static {
      sAuthorityRenames.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFJClmEShF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1Dm4FNCZvETgqKgc2Vg==")));
      sAuthorityRenames.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+DmgVBix9ASxF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1Dm4FQSRrARovLRcMVg==")));
      sSyncStorageEngine = null;
   }

   public interface OnSyncRequestListener {
      void onSyncRequest(Account var1, int var2, int var3, String var4, Bundle var5);
   }

   public static class DayStats {
      public final int day;
      public int successCount;
      public long successTime;
      public int failureCount;
      public long failureTime;

      public DayStats(int day) {
         this.day = day;
      }
   }

   public static class SyncHistoryItem {
      int authorityId;
      int historyId;
      long eventTime;
      long elapsedTime;
      int source;
      int event;
      long upstreamActivity;
      long downstreamActivity;
      String mesg;
      boolean initialization;
      Bundle extras;
      int reason;
   }

   public static class AuthorityInfo {
      final ComponentName service;
      final Account account;
      final int userId;
      final String authority;
      final int ident;
      boolean enabled;
      int syncable;
      long backoffTime;
      long backoffDelay;
      long delayUntil;
      final ArrayList<PeriodicSync> periodicSyncs;

      AuthorityInfo(AuthorityInfo toCopy) {
         this.account = toCopy.account;
         this.userId = toCopy.userId;
         this.authority = toCopy.authority;
         this.service = toCopy.service;
         this.ident = toCopy.ident;
         this.enabled = toCopy.enabled;
         this.syncable = toCopy.syncable;
         this.backoffTime = toCopy.backoffTime;
         this.backoffDelay = toCopy.backoffDelay;
         this.delayUntil = toCopy.delayUntil;
         this.periodicSyncs = new ArrayList();
         Iterator var2 = toCopy.periodicSyncs.iterator();

         while(var2.hasNext()) {
            PeriodicSync sync = (PeriodicSync)var2.next();
            this.periodicSyncs.add(mirror.android.content.PeriodicSync.clone(sync));
         }

      }

      AuthorityInfo(Account account, int userId, String authority, int ident) {
         this.account = account;
         this.userId = userId;
         this.authority = authority;
         this.service = null;
         this.ident = ident;
         this.enabled = false;
         this.syncable = -1;
         this.backoffTime = -1L;
         this.backoffDelay = -1L;
         this.periodicSyncs = new ArrayList();
         PeriodicSync sync = new PeriodicSync(account, authority, new Bundle(), 86400L);
         long flexTime = SyncStorageEngine.calculateDefaultFlexTime(86400L);
         mirror.android.content.PeriodicSync.flexTime.set(sync, flexTime);
         this.periodicSyncs.add(sync);
      }

      AuthorityInfo(ComponentName cname, int userId, int ident) {
         this.account = null;
         this.userId = userId;
         this.authority = null;
         this.service = cname;
         this.ident = ident;
         this.enabled = true;
         this.syncable = -1;
         this.backoffTime = -1L;
         this.backoffDelay = -1L;
         this.periodicSyncs = new ArrayList();
         PeriodicSync periodicSync = new PeriodicSync(this.account, this.authority, new Bundle(), 86400L);
         mirror.android.content.PeriodicSync.flexTime.set(periodicSync, SyncStorageEngine.calculateDefaultFlexTime(86400L));
         this.periodicSyncs.add(periodicSync);
      }
   }

   static class AccountInfo {
      final AccountAndUser accountAndUser;
      final HashMap<String, AuthorityInfo> authorities = new HashMap();

      AccountInfo(AccountAndUser accountAndUser) {
         this.accountAndUser = accountAndUser;
      }
   }

   public static class PendingOperation {
      final Account account;
      final int userId;
      final int reason;
      final int syncSource;
      final String authority;
      final Bundle extras;
      final ComponentName serviceName;
      final boolean expedited;
      int authorityId;
      byte[] flatExtras;

      PendingOperation(Account account, int userId, int reason, int source, String authority, Bundle extras, boolean expedited) {
         this.account = account;
         this.userId = userId;
         this.syncSource = source;
         this.reason = reason;
         this.authority = authority;
         this.extras = extras != null ? new Bundle(extras) : extras;
         this.expedited = expedited;
         this.authorityId = -1;
         this.serviceName = null;
      }

      PendingOperation(PendingOperation other) {
         this.account = other.account;
         this.userId = other.userId;
         this.reason = other.reason;
         this.syncSource = other.syncSource;
         this.authority = other.authority;
         this.extras = other.extras;
         this.authorityId = other.authorityId;
         this.expedited = other.expedited;
         this.serviceName = other.serviceName;
      }
   }
}
