package com.lody.virtual.server.content;

import android.accounts.Account;
import android.util.Log;
import android.util.Pair;
import com.lody.virtual.StringFog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SyncQueue {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0YCGsxEjdgNCA9KAguVg=="));
   private final SyncStorageEngine mSyncStorageEngine;
   private final SyncAdaptersCache mSyncAdapters;
   private final HashMap<String, SyncOperation> mOperationsMap = new HashMap();

   public SyncQueue(SyncStorageEngine syncStorageEngine, SyncAdaptersCache syncAdapters) {
      this.mSyncStorageEngine = syncStorageEngine;
      this.mSyncAdapters = syncAdapters;
   }

   public void addPendingOperations(int userId) {
      Iterator var2 = this.mSyncStorageEngine.getPendingOperations().iterator();

      while(var2.hasNext()) {
         SyncStorageEngine.PendingOperation op = (SyncStorageEngine.PendingOperation)var2.next();
         if (op.userId == userId) {
            Pair<Long, Long> backoff = this.mSyncStorageEngine.getBackoff(op.account, op.userId, op.authority);
            SyncAdaptersCache.SyncAdapterInfo syncAdapterInfo = this.mSyncAdapters.getServiceInfo(op.account, op.authority);
            if (syncAdapterInfo == null) {
               Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwgYKW8zAiZiICQpLQcYP34zQSxoDjw/LhcLJGMKRSFsICAvKQdeJGgKNCBlAQYbJQgMCnkVSFo=")) + op.authority + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186I28zNARrDg08")) + op.userId);
            } else {
               SyncOperation syncOperation = new SyncOperation(op.account, op.userId, op.reason, op.syncSource, op.authority, op.extras, 0L, 0L, backoff != null ? (Long)backoff.first : 0L, this.mSyncStorageEngine.getDelayUntilTime(op.account, op.userId, op.authority), syncAdapterInfo.type.allowParallelSyncs());
               syncOperation.expedited = op.expedited;
               syncOperation.pendingOperation = op;
               this.add(syncOperation, op);
            }
         }
      }

   }

   public boolean add(SyncOperation operation) {
      return this.add(operation, (SyncStorageEngine.PendingOperation)null);
   }

   private boolean add(SyncOperation operation, SyncStorageEngine.PendingOperation pop) {
      String operationKey = operation.key;
      SyncOperation existingOperation = (SyncOperation)this.mOperationsMap.get(operationKey);
      if (existingOperation != null) {
         boolean changed = false;
         if (operation.compareTo(existingOperation) <= 0) {
            existingOperation.expedited = operation.expedited;
            long newRunTime = Math.min(existingOperation.latestRunTime, operation.latestRunTime);
            existingOperation.latestRunTime = newRunTime;
            existingOperation.flexTime = operation.flexTime;
            changed = true;
         }

         return changed;
      } else {
         operation.pendingOperation = pop;
         if (operation.pendingOperation == null) {
            pop = new SyncStorageEngine.PendingOperation(operation.account, operation.userId, operation.reason, operation.syncSource, operation.authority, operation.extras, operation.expedited);
            pop = this.mSyncStorageEngine.insertIntoPending(pop);
            if (pop == null) {
               throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowESh9DgowKQcYM34wTStlNywaLC4lJGEjGilpICAeLD4ACGgKMAVqNx03")) + operation);
            }

            operation.pendingOperation = pop;
         }

         this.mOperationsMap.put(operationKey, operation);
         return true;
      }
   }

   public void removeUser(int userId) {
      ArrayList<SyncOperation> opsToRemove = new ArrayList();
      Iterator var3 = this.mOperationsMap.values().iterator();

      SyncOperation op;
      while(var3.hasNext()) {
         op = (SyncOperation)var3.next();
         if (op.userId == userId) {
            opsToRemove.add(op);
         }
      }

      var3 = opsToRemove.iterator();

      while(var3.hasNext()) {
         op = (SyncOperation)var3.next();
         this.remove(op);
      }

   }

   public void remove(SyncOperation operation) {
      SyncOperation operationToRemove = (SyncOperation)this.mOperationsMap.remove(operation.key);
      if (operationToRemove != null) {
         if (!this.mSyncStorageEngine.deleteFromPending(operationToRemove.pendingOperation)) {
            String errorMessage = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcP2sjHitLEQo1Pxc+MW8VAShsESgbLggYKmIkODVsJz8pLxguCHgVSFo=")) + operationToRemove;
            Log.e(TAG, errorMessage, new IllegalStateException(errorMessage));
         }

      }
   }

   public void onBackoffChanged(Account account, int userId, String providerName, long backoff) {
      Iterator var6 = this.mOperationsMap.values().iterator();

      while(var6.hasNext()) {
         SyncOperation op = (SyncOperation)var6.next();
         if (op.account.equals(account) && op.authority.equals(providerName) && op.userId == userId) {
            op.backoff = backoff;
            op.updateEffectiveRunTime();
         }
      }

   }

   public void onDelayUntilTimeChanged(Account account, String providerName, long delayUntil) {
      Iterator var5 = this.mOperationsMap.values().iterator();

      while(var5.hasNext()) {
         SyncOperation op = (SyncOperation)var5.next();
         if (op.account.equals(account) && op.authority.equals(providerName)) {
            op.delayUntil = delayUntil;
            op.updateEffectiveRunTime();
         }
      }

   }

   public void remove(Account account, int userId, String authority) {
      Iterator<Map.Entry<String, SyncOperation>> entries = this.mOperationsMap.entrySet().iterator();

      while(true) {
         SyncOperation syncOperation;
         do {
            do {
               if (!entries.hasNext()) {
                  return;
               }

               Map.Entry<String, SyncOperation> entry = (Map.Entry)entries.next();
               syncOperation = (SyncOperation)entry.getValue();
            } while(account != null && !syncOperation.account.equals(account));
         } while(authority != null && !syncOperation.authority.equals(authority));

         if (userId == syncOperation.userId) {
            entries.remove();
            if (!this.mSyncStorageEngine.deleteFromPending(syncOperation.pendingOperation)) {
               String errorMessage = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcP2sjHitLEQo1Pxc+MW8VAShsESgbLggYKmIkODVsJz8pLxguCHgVSFo=")) + syncOperation;
               Log.e(TAG, errorMessage, new IllegalStateException(errorMessage));
            }
         }
      }
   }

   public Collection<SyncOperation> getOperations() {
      return this.mOperationsMap.values();
   }
}
