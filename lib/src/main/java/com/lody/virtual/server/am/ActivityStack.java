package com.lody.virtual.server.am;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.BridgeActivity;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.helper.compat.ObjectsCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.ClassUtils;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.AppTaskInfo;
import com.lody.virtual.remote.ShadowActivityInfo;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import mirror.android.app.ActivityManagerNative;
import mirror.android.app.IActivityManager;
import mirror.com.android.internal.R_Hide;

class ActivityStack {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2LGUaOC9mEQYPLBciP2wFSFo="));
   private static final boolean sTrace = true;
   private final VActivityManagerService mService;
   private final Set<ActivityRecord> mPendingLaunchActivities = Collections.synchronizedSet(new HashSet());
   private final ActivityManager mAM;
   private final SparseArray<TaskRecord> mHistory = new SparseArray();

   ActivityStack(VActivityManagerService mService) {
      this.mService = mService;
      this.mAM = (ActivityManager)VirtualCore.get().getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUaOC9mEQZF")));
   }

   private static void removeFlags(Intent intent, int flags) {
      intent.setFlags(intent.getFlags() & ~flags);
   }

   private static boolean containFlags(Intent intent, int flags) {
      return (intent.getFlags() & flags) != 0;
   }

   private static int removeFlags(int flags, int mask) {
      return flags & ~mask;
   }

   private static boolean containFlags(int flags, int mask) {
      return (flags & mask) != 0;
   }

   private void deliverNewIntentLocked(ActivityRecord sourceRecord, ActivityRecord launchRecord, Intent intent) {
      if (launchRecord != null) {
         String creator = sourceRecord != null ? sourceRecord.component.getPackageName() : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iEVRF"));
         if (launchRecord.started && launchRecord.process != null && launchRecord.process.client != null) {
            try {
               launchRecord.process.client.scheduleNewIntent(creator, launchRecord.token, intent);
            } catch (RemoteException var6) {
               RemoteException e = var6;
               e.printStackTrace();
            }
         } else {
            launchRecord.pendingNewIntent = new PendingNewIntent(creator, intent);
         }

      }
   }

   private TaskRecord findTaskByComponentLocked(int userId, ComponentName comp) {
      for(int i = 0; i < this.mHistory.size(); ++i) {
         TaskRecord r = (TaskRecord)this.mHistory.valueAt(i);
         if (userId == r.userId) {
            synchronized(r.activities) {
               Iterator var6 = r.activities.iterator();

               while(var6.hasNext()) {
                  ActivityRecord a = (ActivityRecord)var6.next();
                  if (!a.marked && a.component.equals(comp)) {
                     return r;
                  }
               }
            }
         }
      }

      return null;
   }

   private TaskRecord findTaskByAffinityLocked(int userId, String affinity) {
      for(int i = 0; i < this.mHistory.size(); ++i) {
         TaskRecord r = (TaskRecord)this.mHistory.valueAt(i);
         if (userId == r.userId && affinity.equals(r.affinity) && !r.isFinishing()) {
            return r;
         }
      }

      return null;
   }

   private TaskRecord findTaskByIntentLocked(int userId, Intent intent) {
      for(int i = 0; i < this.mHistory.size(); ++i) {
         TaskRecord r = (TaskRecord)this.mHistory.valueAt(i);
         if (userId == r.userId && r.taskRoot != null && ObjectsCompat.equals(intent.getComponent(), r.taskRoot.getComponent())) {
            return r;
         }
      }

      return null;
   }

   private ActivityRecord findActivityByToken(int userId, IBinder token) {
      ActivityRecord target = null;
      if (token != null) {
         for(int i = 0; i < this.mHistory.size(); ++i) {
            TaskRecord task = (TaskRecord)this.mHistory.valueAt(i);
            if (task.userId == userId) {
               synchronized(task.activities) {
                  Iterator var7 = task.activities.iterator();

                  while(var7.hasNext()) {
                     ActivityRecord r = (ActivityRecord)var7.next();
                     if (r.token == token) {
                        target = r;
                     }
                  }
               }
            }
         }
      }

      return target;
   }

   private void optimizeTasksLocked() {
      List<ActivityManager.RecentTaskInfo> recentTask = VirtualCore.get().getRecentTasksEx(Integer.MAX_VALUE, 3);
      int N = this.mHistory.size();

      while(N-- > 0) {
         TaskRecord task = (TaskRecord)this.mHistory.valueAt(N);
         ListIterator<ActivityManager.RecentTaskInfo> iterator = recentTask.listIterator();
         boolean taskAlive = false;

         while(iterator.hasNext()) {
            ActivityManager.RecentTaskInfo info = (ActivityManager.RecentTaskInfo)iterator.next();
            if (info.id == task.taskId) {
               taskAlive = true;
               iterator.remove();
               break;
            }
         }

         if (!taskAlive) {
            this.mHistory.removeAt(N);
         }
      }

   }

   int startActivitiesLocked(int userId, Intent[] intents, ActivityInfo[] infos, IBinder resultTo, Bundle options) {
      for(int i = 0; i < intents.length; ++i) {
         this.startActivityLocked(userId, intents[i], infos[i], resultTo, options, (String)null, 0);
      }

      return 0;
   }

   private static String launchModeToString(int launchMode) {
      switch (launchMode) {
         case 0:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2ojMDdhNApF"));
         case 1:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YCGgzHituHh4s"));
         case 2:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YCGgzHituHiApKS5SVg=="));
         case 3:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YCGgzHitrDlkpLBciDm4FGlo="));
         default:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gN1RF"));
      }
   }

   private static String documentLaunchModeToString(int launchMode) {
      switch (launchMode) {
         case 0:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ACGgVSFo="));
         case 1:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGoxNDBjASggKQcYMw=="));
         case 2:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggELWsaAgM="));
         case 3:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4uLmgaFlo="));
         default:
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgcMWojGj1gN1RF"));
      }
   }

   private static String componentInfoToString(ComponentInfo info) {
      return info.packageName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + info.name;
   }

   private static String activityInfoFlagsToString(int flags) {
      StringBuilder sb = new StringBuilder();
      if (containFlags(flags, 1)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2Gg1uDFFLIQYmAmcLAhVkIjMrKAQ6Vg==")));
         flags = removeFlags(flags, 1);
      }

      if (containFlags(flags, 1048576)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GlNrDygJJztbWmMIBg9hJR5AITsqHWgxFl9hDyAfOD0hJA==")));
         flags = removeFlags(flags, 1048576);
      }

      if (containFlags(flags, 2)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GghrDFkJOysAHWcLMB9nHzgQOzsAAmULLBNhIhkpLTpXVg==")));
         flags = removeFlags(flags, 2);
      }

      if (containFlags(flags, 4)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhNoHDAROzxfAGYmAgthJQZAIDwEHW4IRQBnVyA1OD5SVg==")));
         flags = removeFlags(flags, 4);
      }

      if (containFlags(flags, 8)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFoHzgRJQYqHWIbGlFgDx5AIDsqHWkmEl99JTAAOzwfJGlSIFo=")));
         flags = removeFlags(flags, 8);
      }

      if (containFlags(flags, 16)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2Gl5uHCBLIAZfQGcIBh99NShLJgYuGksVTCM=")));
         flags = removeFlags(flags, 16);
      }

      if (containFlags(flags, 64)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFoHFELJCxfAGYmAgthIgpLIQY+U2oIRVRnDB5LOD0hJA==")));
         flags = removeFlags(flags, 64);
      }

      if (containFlags(flags, 128)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GgBoJR4KIQYqAGcIRVR4HgUr")));
         flags = removeFlags(flags, 128);
      }

      if (containFlags(flags, 256)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GghrDFkJOysAHWcLMB9gJQJBITwuE2khGhBiHApBJQYcBWALHl5gMjM3Il4iVg==")));
         flags = removeFlags(flags, 256);
      }

      if (containFlags(flags, 512)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GgplDywWJCsiAmEmNBFgJTBLJAYuU2ULFgpmVyA1OD5SVg==")));
         flags = removeFlags(flags, 512);
      }

      if (containFlags(flags, 1073741824)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2Gl5rDFkTIhUMHX0mAhVkMzw3PQhSVg==")));
         flags = removeFlags(flags, 1073741824);
      }

      if (containFlags(flags, 32)) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhVvHCgOJAU2WmMLHl99JQ4MISwuH2oIRVR9ICA1OD5SVg==")));
         flags = removeFlags(flags, 32);
      }

      if (flags != 0) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OhdfVg=="))).append(Integer.toHexString(flags));
      } else if (sb.length() > 2) {
         sb.delete(sb.length() - 2, sb.length());
      }

      return sb.toString();
   }

   private static String activityInfoToString(ActivityInfo info) {
      StringBuilder sb = new StringBuilder();
      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+I2ojLCBoDh4wKAQHOg==")));
      sb.append(launchModeToString(info.launchMode));
      if (VERSION.SDK_INT >= 21) {
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Dj4qD2swNCNiDlkgIhciLW8VAiB9AQYvLl5WJA==")));
         sb.append(documentLaunchModeToString(info.documentLaunchMode));
      }

      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Dj4+PmgjAiZjAQoZPTkmVg=="))).append(info.taskAffinity);
      sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Dj4iDmsVPAN3MCRF"))).append(activityInfoFlagsToString(info.flags));
      return sb.toString();
   }

   private static String parseIntentFlagsToString(Intent intent) {
      int flags = intent.getFlags();
      if (flags == 0) {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OhdeKA=="));
      } else {
         StringBuilder sb = new StringBuilder();
         if (containFlags(flags, 268435456)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNABjDCAMIgY+UmskOAF5EVRF")));
            flags = removeFlags(flags, 268435456);
         }

         if (containFlags(flags, 32768)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNBN9HygCISsAXWULNAh5EVwp")));
            flags = removeFlags(flags, 32768);
         }

         if (containFlags(flags, 134217728)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNA1nDwIVOxU6AmoLAlRhDyxPOD0hJA==")));
            flags = removeFlags(flags, 134217728);
         }

         if (containFlags(flags, 131072)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNF9jDwYfJgYuU2whFhRgIjgRIQYqXXgaGTQ=")));
            flags = removeFlags(flags, 131072);
         }

         if (containFlags(flags, 131072)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNF9jDwYfJgYuU2whFhRgIjgRIQYqXXgaGTQ=")));
            flags = removeFlags(flags, 131072);
         }

         if (containFlags(flags, 536870912)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNF5iDxpJJAYuE24YAh15EVwp")));
            flags = removeFlags(flags, 536870912);
         }

         if (containFlags(flags, 134217728)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNA1nDwIVOxU6AmoLAlRhDyxPOD0hJA==")));
            flags = removeFlags(flags, 134217728);
         }

         if (containFlags(flags, 33554432)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNAh9IgoUJRUMGmwhMAp9JQpAOzpXOHgVSFo=")));
            flags = removeFlags(flags, 33554432);
         }

         if (containFlags(flags, 16384)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNFFgDDBNIDwADGwmBhRkDA0pLTpXVg==")));
            flags = removeFlags(flags, 16384);
         }

         if (containFlags(flags, 67108864)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNBN9HygCISsAXWghOyNoVyBF")));
            flags = removeFlags(flags, 67108864);
         }

         if (containFlags(flags, 262144)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNAB9IgYWITwuU2wmPABiHBpXIV9XOHgVSFo=")));
            flags = removeFlags(flags, 262144);
         }

         if (containFlags(flags, 8192)) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JTwEEWA2GhFlJQoJJDscAGAmNF9jDCwCOxYcE2sIRV99MgoCJywqXWQnICh7AVRF")));
            flags = removeFlags(flags, 8192);
         }

         if (flags != 0) {
            sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OhdfVg=="))).append(Integer.toHexString(flags));
         } else if (sb.length() >= 2) {
            sb.delete(sb.length() - 2, sb.length());
         }

         return sb.toString();
      }
   }

   private ActivityRecord findActivityByComponentName(TaskRecord task, ComponentName comp) {
      synchronized(task.activities) {
         for(int i = task.activities.size() - 1; i >= 0; --i) {
            ActivityRecord r = (ActivityRecord)task.activities.get(i);
            if (!r.marked && r.component.equals(comp)) {
               return r;
            }
         }

         return null;
      }
   }

   boolean performClearTaskLocked(TaskRecord task, ComponentName comp, ClearTaskAction action, boolean singleTop) {
      boolean marked = false;
      synchronized(task.activities) {
         switch (action) {
            case TASK:
               for(Iterator var14 = task.activities.iterator(); var14.hasNext(); marked = true) {
                  ActivityRecord r = (ActivityRecord)var14.next();
                  r.marked = true;
               }

               return marked;
            case ACTIVITY:
               ActivityRecord r = this.findActivityByComponentName(task, comp);
               if (r != null) {
                  r.marked = true;
                  marked = true;
               }
               break;
            case TOP:
               int foundIndex = -1;

               for(int i = task.activities.size() - 1; i >= 0; --i) {
                  r = (ActivityRecord)task.activities.get(i);
                  if (r.component.equals(comp)) {
                     foundIndex = i;
                     break;
                  }
               }

               if (foundIndex >= 0) {
                  marked = true;
                  if (singleTop) {
                     ++foundIndex;
                  }

                  while(foundIndex < task.activities.size()) {
                     r = (ActivityRecord)task.activities.get(foundIndex);
                     r.marked = true;
                     ++foundIndex;
                  }
               }
         }

         return marked;
      }
   }

   int startActivityLocked(int userId, Intent intent, ActivityInfo info, IBinder resultTo, Bundle options, String resultWho, int requestCode) {
      synchronized(this.mHistory) {
         this.optimizeTasksLocked();
      }

      ActivityRecord sourceRecord = this.findActivityByToken(userId, resultTo);
      if (resultTo != null && sourceRecord == null) {
         VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4ALHsFOCVmDlkwPxgqDWUgRSlrDTw5Lhg2KWEwET15EVRF")) + resultTo);
      }

      VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMBF9JwozLD0cLmgkIww=")) + (sourceRecord == null ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoFSFo=")) : componentInfoToString(sourceRecord.info)) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85DXpTAShLEVRF")) + componentInfoToString(info) + "\n" + activityInfoToString(info) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Dj4YCGwFNCZmVyQ+KhciM2oOIyg=")) + parseIntentFlagsToString(intent) + "\n" + intent + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Dj0MM28aNCthJwofKi02PXhSTVo=")) + requestCode);
      TaskRecord sourceTask = null;
      if (sourceRecord != null) {
         sourceTask = sourceRecord.task;
      } else {
         resultTo = null;
      }

      String affinity = ComponentUtils.getTaskAffinity(info);
      int mLauncherFlags = 0;
      boolean newTask = containFlags(intent, 268435456);
      boolean clearTop = containFlags(intent, 67108864);
      boolean clearTask = newTask && containFlags(intent, 32768);
      boolean multipleTask = newTask && containFlags(intent, 134217728);
      boolean singleTop = containFlags(intent, 536870912);
      boolean reorderToFront = containFlags(intent, 131072) && !clearTop;
      boolean forwardResult = containFlags(intent, 33554432);
      boolean newDocument = false;
      int launchMode = info.launchMode;
      int documentLaunchMode = info.documentLaunchMode;
      if ((info.flags & 32) != 0 || containFlags(intent, 8388608)) {
         mLauncherFlags |= 8388608;
      }

      if (containFlags(intent, 65536)) {
         mLauncherFlags |= 65536;
      }

      if (containFlags(intent, 8192)) {
         mLauncherFlags |= 8192;
      }

      if (launchMode == 1 || launchMode == 2 || launchMode == 3) {
         singleTop = true;
      }

      if (forwardResult) {
         if (sourceRecord != null && sourceRecord.resultTo != null) {
            ActivityRecord forwardTo = this.findActivityByToken(userId, sourceRecord.resultTo);
            if (forwardTo != null) {
               mLauncherFlags |= 33554432;
               resultTo = forwardTo.token;
            } else {
               VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4AKmwzJARiHyw/Iy4MCGU3TS5oAR4dLhgpPksVSFo=")) + intent);
            }
         } else {
            VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4AKmwzJARiHyw/Iy4MCGU3TS5oAR4dLhgpPksVSFo=")) + intent);
         }
      }

      ComponentName launchComponent;
      if (info.targetActivity != null) {
         launchComponent = new ComponentName(info.packageName, info.targetActivity);
      } else {
         launchComponent = new ComponentName(info.packageName, info.name);
      }

      TaskRecord reuseTask = null;
      if (documentLaunchMode == 2) {
         multipleTask = true;
      } else if ((documentLaunchMode == 1 || containFlags(intent.getFlags(), 524288)) && newTask) {
         newDocument = true;
      }

      if (!multipleTask) {
         if (newDocument) {
            reuseTask = this.findTaskByIntentLocked(userId, intent);
         } else {
            if (!newTask && sourceRecord != null && (requestCode >= 0 || sourceRecord.info.launchMode != 3 && (launchMode == 0 || launchMode == 1))) {
               reuseTask = sourceTask;
            }

            if (reuseTask == null) {
               if (launchMode == 3) {
                  reuseTask = this.findTaskByComponentLocked(userId, launchComponent);
               } else {
                  reuseTask = this.findTaskByAffinityLocked(userId, affinity);
               }
            }
         }
      }

      if (reuseTask != null && !reuseTask.isFinishing()) {
         try {
            this.mAM.moveTaskToFront(reuseTask.taskId, 0);
         } catch (Exception var30) {
            Exception e = var30;
            e.printStackTrace();
         }

         boolean startTaskToFront = false;
         if (launchMode == 0 && !singleTop && !clearTask && !clearTop && !reorderToFront && requestCode <= 0 && resultTo == null) {
            startTaskToFront = ComponentUtils.intentFilterEquals(reuseTask.taskRoot, intent);
         }

         if (startTaskToFront) {
            return 0;
         } else {
            ClearTaskAction clearTaskAction = ClearTaskAction.NONE;
            if (launchMode == 2 || launchMode == 3 || clearTop) {
               clearTaskAction = ClearTaskAction.TOP;
            }

            if (reorderToFront) {
               clearTaskAction = ClearTaskAction.ACTIVITY;
            }

            if (clearTask) {
               clearTaskAction = ClearTaskAction.TASK;
            }

            boolean cleared = false;
            ActivityRecord launchRecord;
            if (singleTop) {
               if (clearTaskAction == ClearTaskAction.TOP) {
                  cleared = this.performClearTaskLocked(reuseTask, launchComponent, ClearTaskAction.TOP, true);
               } else if (clearTaskAction == ClearTaskAction.NONE) {
                  cleared = true;
               }

               if (cleared) {
                  launchRecord = reuseTask.getTopActivityRecord();
                  if (launchRecord != null && launchRecord.component.equals(launchComponent)) {
                     this.deliverNewIntentLocked(sourceRecord, launchRecord, intent);
                     this.finishMarkedActivity();
                     return 0;
                  }
               }

               clearTaskAction = ClearTaskAction.NONE;
            }

            if (cleared) {
               this.finishMarkedActivity();
            }

            launchRecord = this.newActivityRecord(userId, intent, info, resultTo);
            launchRecord.requestCode = requestCode;
            launchRecord.resultWho = resultWho;
            launchRecord.options = options;
            launchRecord.pendingClearAction = clearTaskAction;
            launchRecord.task = reuseTask;
            Intent destIntent = this.startActivityProcess(userId, launchRecord, intent, info);
            if (destIntent == null) {
               return -1;
            } else {
               reuseTask.activities.add(launchRecord);
               this.mPendingLaunchActivities.add(launchRecord);
               destIntent.addFlags(mLauncherFlags);
               ActivityRecord callerRecord;
               if (sourceTask == reuseTask) {
                  callerRecord = sourceRecord;
               } else {
                  callerRecord = reuseTask.getTopActivityRecord(false);
               }

               if (callerRecord != null && callerRecord.process != null) {
                  launchRecord.started = true;
                  return this.startActivityFromSourceTask(callerRecord.process, callerRecord.token, destIntent, resultWho, requestCode, options);
               } else {
                  VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzJCRgHjAqOz0MP28KRSx4ESQsIxgEJ2JSGSM=")) + intent);
                  return -1;
               }
            }
         }
      } else {
         VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMBF9JwozLD0cLmghLCZ9Nyg+Igg+D2MnGSM=")) + intent);
         return this.startActivityInNewTaskLocked(mLauncherFlags, userId, intent, info, options);
      }
   }

   private ActivityRecord newActivityRecord(int userId, Intent intent, ActivityInfo info, IBinder resultTo) {
      return new ActivityRecord(userId, intent, info, resultTo);
   }

   private void startActivity(Intent intent, Bundle options, boolean addon) {
      ComponentName component = intent.getComponent();
      boolean useBridgeActivity = false;
      if (component != null && !VirtualCore.get().isAppInstalled(component.getPackageName())) {
         useBridgeActivity = true;
      }

      if (addon && !VirtualCore.get().isSharedUserId()) {
         useBridgeActivity = true;
      }

      if (useBridgeActivity) {
         Intent gotoExtIntent = new Intent(VirtualCore.get().getContext(), BridgeActivity.class);
         gotoExtIntent.setFlags(268435456);
         gotoExtIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9jDlkgKAcYLmMFSFo=")), intent);
         gotoExtIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh99NzA2KBdbPWMFSFo=")), options);
         VirtualCore.get().getContext().startActivity(gotoExtIntent);
      } else {
         VirtualCore.get().getContext().startActivity(intent, options);
      }
   }

   private int startActivityInNewTaskLocked(int launcherFlags, int userId, Intent intent, ActivityInfo info, Bundle options) {
      ActivityRecord launchRecord = this.newActivityRecord(userId, intent, info, (IBinder)null);
      launchRecord.options = options;
      Intent destIntent = this.startActivityProcess(userId, launchRecord, intent, info);
      if (destIntent == null) {
         return -1;
      } else {
         destIntent.addFlags(launcherFlags);
         destIntent.addFlags(268435456);
         destIntent.addFlags(134217728);
         destIntent.addFlags(2097152);
         destIntent.addFlags(524288);
         if (options != null) {
            VirtualCore.get().getContext().startActivity(destIntent, options);
         } else {
            VirtualCore.get().getContext().startActivity(destIntent);
         }

         return 0;
      }
   }

   private void finishMarkedActivity() {
      synchronized(this.mHistory) {
         int N = this.mHistory.size();

         while(N-- > 0) {
            TaskRecord task = (TaskRecord)this.mHistory.valueAt(N);
            synchronized(task.activities) {
               Iterator<ActivityRecord> it = task.activities.iterator();

               while(it.hasNext()) {
                  ActivityRecord r = (ActivityRecord)it.next();
                  if (r.marked && r.started) {
                     try {
                        if (r.process != null && r.process.client != null) {
                           r.process.client.finishActivity(r.token);
                        }

                        it.remove();
                     } catch (RemoteException var10) {
                        RemoteException e = var10;
                        e.printStackTrace();
                     }
                  }
               }
            }
         }

      }
   }

   public boolean finishActivityAffinity(int userId, IBinder token) {
      synchronized(this.mHistory) {
         ActivityRecord r = this.findActivityByToken(userId, token);
         if (r == null) {
            return false;
         }

         String taskAffinity = ComponentUtils.getTaskAffinity(r.info);
         synchronized(r.task.activities) {
            int index = r.task.activities.indexOf(r);

            while(index >= 0) {
               ActivityRecord cur = (ActivityRecord)r.task.activities.get(index);
               if (ComponentUtils.getTaskAffinity(cur.info).equals(taskAffinity)) {
                  cur.marked = true;
                  --index;
                  continue;
               }
            }
         }
      }

      this.finishMarkedActivity();
      return false;
   }

   private int startActivityFromSourceTask(ProcessRecord r, IBinder resultTo, Intent intent, String resultWho, int requestCode, Bundle options) {
      return this.realStartActivityLocked(r.appThread, resultTo, intent, resultWho, requestCode, options);
   }

   private int realStartActivityLocked(IInterface appThread, IBinder resultTo, Intent intent, String resultWho, int requestCode, Bundle options) {
      Class<?>[] types = IActivityManager.startActivity.paramList();
      Object[] args = new Object[types.length];
      args[0] = appThread;
      int intentIndex = ArrayUtils.protoIndexOf(types, Intent.class);
      int resultToIndex = ArrayUtils.protoIndexOf(types, IBinder.class, 2);
      int optionsIndex = ArrayUtils.protoIndexOf(types, Bundle.class);
      int resolvedTypeIndex = intentIndex + 1;
      int resultWhoIndex = resultToIndex + 1;
      int requestCodeIndex = resultToIndex + 2;
      args[intentIndex] = intent;
      args[resultToIndex] = resultTo;
      args[resultWhoIndex] = resultWho;
      args[requestCodeIndex] = requestCode;
      if (optionsIndex != -1) {
         args[optionsIndex] = options;
      }

      args[resolvedTypeIndex] = intent.getType();
      args[intentIndex - 1] = VirtualCore.get().getHostPkg();
      ClassUtils.fixArgs(types, args);

      try {
         return (Integer)IActivityManager.startActivity.call(ActivityManagerNative.getDefault.call(), args);
      } catch (Throwable var16) {
         Throwable e = var16;
         e.printStackTrace();
         return 0;
      }
   }

   private String selectShadowActivity(int vpid, ActivityInfo targetInfo) {
      boolean isFloating = false;
      boolean isTranslucent = false;
      boolean showWallpaper = false;

      try {
         int[] R_Styleable_Window = (int[])R_Hide.styleable.Window.get();
         int R_Styleable_Window_windowIsTranslucent = R_Hide.styleable.Window_windowIsTranslucent.get();
         int R_Styleable_Window_windowIsFloating = R_Hide.styleable.Window_windowIsFloating.get();
         int R_Styleable_Window_windowShowWallpaper = R_Hide.styleable.Window_windowShowWallpaper.get();
         AttributeCache.Entry ent = AttributeCache.instance().get(targetInfo.packageName, targetInfo.theme, R_Styleable_Window);
         if (ent != null && ent.array != null) {
            showWallpaper = ent.array.getBoolean(R_Styleable_Window_windowShowWallpaper, false);
            isTranslucent = ent.array.getBoolean(R_Styleable_Window_windowIsTranslucent, false);
            isFloating = ent.array.getBoolean(R_Styleable_Window_windowIsFloating, false);
         }
      } catch (Throwable var11) {
         Throwable e = var11;
         e.printStackTrace();
      }

      boolean isDialogStyle = isFloating || isTranslucent || showWallpaper;
      return isDialogStyle ? StubManifest.getStubDialogName(vpid, targetInfo) : StubManifest.getStubActivityName(vpid, targetInfo);
   }

   private Intent startActivityProcess(int userId, ActivityRecord targetRecord, Intent intent, ActivityInfo info) {
      ProcessRecord targetApp = this.mService.startProcessIfNeeded(info.processName, userId, info.packageName, -1);
      return targetApp == null ? null : this.getStartShadowActivityIntentInner(intent, targetApp.isExt, targetApp.vpid, userId, targetRecord, info);
   }

   private Intent getStartShadowActivityIntentInner(Intent intent, boolean isStub, int vpid, int userId, ActivityRecord targetRecord, ActivityInfo info) {
      intent = new Intent(intent);
      Intent targetIntent = new Intent();
      if (info.screenOrientation == 3 && targetRecord.task != null && targetRecord.task.getTopActivityRecord() != null) {
         info.screenOrientation = targetRecord.task.getTopActivityRecord().info.screenOrientation;
      }

      targetIntent.setClassName(StubManifest.getStubPackageName(isStub), this.selectShadowActivity(vpid, info));
      ComponentName component = intent.getComponent();
      if (component == null) {
         component = ComponentUtils.toComponentName(info);
      }

      targetIntent.setType(component.flattenToString());
      ShadowActivityInfo saveInstance = new ShadowActivityInfo(intent, info, userId, targetRecord);
      saveInstance.saveToIntent(targetIntent);
      return targetIntent;
   }

   void onActivityCreated(ProcessRecord targetApp, IBinder token, int taskId, ActivityRecord record) {
      VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cEWswMC9mNAYgLQUqKGkjQQZrAS8r")) + record.info + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcqP28zQQliVgU8")) + taskId);
      synchronized(this.mHistory) {
         this.mPendingLaunchActivities.remove(record);
         this.optimizeTasksLocked();
         TaskRecord task = (TaskRecord)this.mHistory.get(taskId);
         if (task == null) {
            if (record.task != null) {
               task = record.task;
            } else {
               task = new TaskRecord(taskId, targetApp.userId, ComponentUtils.getTaskAffinity(record.info), record.intent);
               this.mHistory.put(taskId, task);
            }
         }

         if (record.task != null && record.task != task) {
            synchronized(record.task.activities) {
               record.task.activities.remove(record);
            }
         }

         record.task = task;
         synchronized(record.task.activities) {
            task.activities.remove(record);
         }

         if (record.pendingClearAction != ClearTaskAction.NONE) {
            this.performClearTaskLocked(task, record.component, record.pendingClearAction, false);
            record.pendingClearAction = ClearTaskAction.NONE;
         }

         record.init(task, targetApp, token);
         task.activities.add(record);
         if (record.pendingNewIntent != null) {
            PendingNewIntent newIntent = record.pendingNewIntent;

            try {
               record.process.client.scheduleNewIntent(newIntent.creator, record.token, newIntent.intent);
            } catch (RemoteException var11) {
               RemoteException e = var11;
               e.printStackTrace();
            }

            record.pendingNewIntent = null;
         }

         this.finishMarkedActivity();
      }
   }

   void onActivityResumed(int userId, IBinder token) {
      synchronized(this.mHistory) {
         this.optimizeTasksLocked();
         ActivityRecord r = this.findActivityByToken(userId, token);
         if (r != null) {
            synchronized(r.task.activities) {
               r.task.activities.remove(r);
               r.task.activities.add(r);
            }
         }

      }
   }

   void onActivityFinish(int userId, IBinder token) {
      synchronized(this.mHistory) {
         ActivityRecord r = this.findActivityByToken(userId, token);
         if (r != null) {
            r.marked = true;
         }

      }
   }

   ActivityRecord onActivityDestroyed(int userId, IBinder token) {
      synchronized(this.mHistory) {
         this.optimizeTasksLocked();
         ActivityRecord r = this.findActivityByToken(userId, token);
         if (r != null) {
            VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cEWswMC9mNAYgLQU2PWoKBgRlJB4gLgQ6Vg==")) + r.info + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcqP28zQQliVgU8")) + r.task.taskId);
            r.marked = true;
            synchronized(r.task.activities) {
               r.task.activities.remove(r);
            }
         }

         return r;
      }
   }

   void processDied(ProcessRecord record) {
      synchronized(this.mHistory) {
         this.optimizeTasksLocked();
         int N = this.mHistory.size();

         label50:
         while(N-- > 0) {
            TaskRecord task = (TaskRecord)this.mHistory.valueAt(N);
            synchronized(task.activities) {
               Iterator<ActivityRecord> iterator = task.activities.iterator();

               while(true) {
                  ActivityRecord r;
                  do {
                     do {
                        if (!iterator.hasNext()) {
                           continue label50;
                        }

                        r = (ActivityRecord)iterator.next();
                     } while(!r.started);
                  } while(r.process != null && r.process.pid != record.pid);

                  iterator.remove();
                  if (task.activities.isEmpty()) {
                     this.mHistory.remove(task.taskId);
                  }
               }
            }
         }

      }
   }

   public void finishAllActivities(ProcessRecord record) {
      synchronized(this.mHistory) {
         int N = this.mHistory.size();

         while(true) {
            if (N-- <= 0) {
               break;
            }

            TaskRecord task = (TaskRecord)this.mHistory.valueAt(N);
            synchronized(task.activities) {
               Iterator<ActivityRecord> iterator = task.activities.iterator();

               while(iterator.hasNext()) {
                  ActivityRecord r = (ActivityRecord)iterator.next();
                  if (r.process.pid == record.pid) {
                     r.marked = true;
                  }
               }
            }
         }
      }

      this.finishMarkedActivity();
   }

   String getPackageForToken(int userId, IBinder token) {
      synchronized(this.mHistory) {
         ActivityRecord r = this.findActivityByToken(userId, token);
         return r != null ? r.info.packageName : null;
      }
   }

   private ActivityRecord getCallingRecordLocked(int userId, IBinder token) {
      ActivityRecord r = this.findActivityByToken(userId, token);
      return r == null ? null : this.findActivityByToken(userId, r.resultTo);
   }

   ComponentName getCallingActivity(int userId, IBinder token) {
      ActivityRecord r = this.getCallingRecordLocked(userId, token);
      return r != null ? r.component : null;
   }

   String getCallingPackage(int userId, IBinder token) {
      ActivityRecord r = this.getCallingRecordLocked(userId, token);
      return r != null ? r.info.packageName : null;
   }

   AppTaskInfo getTaskInfo(int taskId) {
      synchronized(this.mHistory) {
         TaskRecord task = (TaskRecord)this.mHistory.get(taskId);
         return task != null ? task.getAppTaskInfo() : null;
      }
   }

   ComponentName getActivityClassForToken(int userId, IBinder token) {
      synchronized(this.mHistory) {
         ActivityRecord r = this.findActivityByToken(userId, token);
         return r != null ? r.component : null;
      }
   }

   public int startActivityFromHistoryLocked(Intent intent) {
      VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP28gMBF9JwozLD0cLmghHgRlJw5OIxc2CmAjMAZ7MCBF")) + intent);
      synchronized(this.mHistory) {
         ShadowActivityInfo info = new ShadowActivityInfo(intent);
         ActivityRecord record = (ActivityRecord)info.virtualToken;
         if (record != null && this.mPendingLaunchActivities.contains(record)) {
            if (record.task == null) {
               VirtualCore.get().getContext().startActivity(intent);
               return 0;
            } else {
               ActivityRecord callerRecord = this.findActivityByToken(record.userId, record.resultTo);
               if (callerRecord == null || callerRecord.task != record.task) {
                  callerRecord = record.task.getTopActivityRecord();
               }

               return this.startActivityFromSourceTask(callerRecord.process, callerRecord.token, intent, record.resultWho, record.requestCode, record.options);
            }
         } else {
            VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uOWowFixLHlk1LF4mMW9STQJrARovIxgcIUsaTSxlJzMd")));
            return -1;
         }
      }
   }
}
