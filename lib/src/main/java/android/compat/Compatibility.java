package android.compat;

import android.annotation.SystemApi;
import android.util.Log;
import com.lody.virtual.StringFog;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@SystemApi(
   client = SystemApi.Client.MODULE_LIBRARIES
)
public final class Compatibility {
   public static final BehaviorChangeDelegate DEFAULT_CALLBACKS = new BehaviorChangeDelegate() {
   };
   public static volatile BehaviorChangeDelegate sCallbacks;

   private Compatibility() {
   }

   @SystemApi(
      client = SystemApi.Client.MODULE_LIBRARIES
   )
   public static void reportUnconditionalChange(long changeId) {
      sCallbacks.onChangeReported(changeId);
   }

   @SystemApi(
      client = SystemApi.Client.MODULE_LIBRARIES
   )
   public static boolean isChangeEnabled(long changeId) {
      Log.d("kook", "  Compatibility  isChangeEnabled ");
      return sCallbacks.isChangeEnabled(changeId);
   }

   @SystemApi(
      client = SystemApi.Client.MODULE_LIBRARIES
   )
   public static void setBehaviorChangeDelegate(BehaviorChangeDelegate callbacks) {
      Log.d("kook", "  Compatibility  setBehaviorChangeDelegate ");
      sCallbacks = (BehaviorChangeDelegate)Objects.requireNonNull(callbacks);
   }

   @SystemApi(
      client = SystemApi.Client.MODULE_LIBRARIES
   )
   public static void clearBehaviorChangeDelegate() {
      sCallbacks = DEFAULT_CALLBACKS;
   }

   @SystemApi(
      client = SystemApi.Client.MODULE_LIBRARIES
   )
   public static void setOverrides(ChangeConfig overrides) {
      if (sCallbacks instanceof OverrideCallbacks) {
         throw new IllegalStateException("setOverrides has already been called!");
      } else {
         sCallbacks = new OverrideCallbacks(sCallbacks, overrides);
      }
   }

   @SystemApi(
      client = SystemApi.Client.MODULE_LIBRARIES
   )
   public static void clearOverrides() {
      if (!(sCallbacks instanceof OverrideCallbacks)) {
         throw new IllegalStateException("No overrides set");
      } else {
         sCallbacks = ((OverrideCallbacks)sCallbacks).delegate;
      }
   }

   static {
      sCallbacks = DEFAULT_CALLBACKS;
   }

   private static class OverrideCallbacks implements BehaviorChangeDelegate {
      private final BehaviorChangeDelegate delegate;
      private final ChangeConfig changeConfig;

      private OverrideCallbacks(BehaviorChangeDelegate delegate, ChangeConfig changeConfig) {
         this.delegate = (BehaviorChangeDelegate)Objects.requireNonNull(delegate);
         this.changeConfig = (ChangeConfig)Objects.requireNonNull(changeConfig);
      }

      public boolean isChangeEnabled(long changeId) {
         if (this.changeConfig.isForceEnabled(changeId)) {
            return true;
         } else {
            return this.changeConfig.isForceDisabled(changeId) ? false : this.delegate.isChangeEnabled(changeId);
         }
      }

      // $FF: synthetic method
      OverrideCallbacks(BehaviorChangeDelegate x0, ChangeConfig x1, Object x2) {
         this(x0, x1);
      }
   }

   @SystemApi(
      client = SystemApi.Client.MODULE_LIBRARIES
   )
   public static final class ChangeConfig {
      private final Set<Long> enabled;
      private final Set<Long> disabled;

      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      public ChangeConfig(Set<Long> enabled, Set<Long> disabled) {
         this.enabled = (Set)Objects.requireNonNull(enabled);
         this.disabled = (Set)Objects.requireNonNull(disabled);
         if (enabled.contains((Object)null)) {
            throw new NullPointerException();
         } else if (disabled.contains((Object)null)) {
            throw new NullPointerException();
         } else {
            Set<Long> intersection = new HashSet(enabled);
            intersection.retainAll(disabled);
            if (!intersection.isEmpty()) {
               throw new IllegalArgumentException("Cannot have changes " + intersection + " enabled and disabled!");
            }
         }
      }

      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      public boolean isEmpty() {
         return this.enabled.isEmpty() && this.disabled.isEmpty();
      }

      private static long[] toLongArray(Set<Long> values) {
         long[] result = new long[values.size()];
         int idx = 0;

         Long value;
         for(Iterator var3 = values.iterator(); var3.hasNext(); result[idx++] = value) {
            value = (Long)var3.next();
         }

         return result;
      }

      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      public long[] getEnabledChangesArray() {
         return toLongArray(this.enabled);
      }

      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      public long[] getDisabledChangesArray() {
         return toLongArray(this.disabled);
      }

      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      public Set<Long> getEnabledSet() {
         return Collections.unmodifiableSet(this.enabled);
      }

      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      public Set<Long> getDisabledSet() {
         return Collections.unmodifiableSet(this.disabled);
      }

      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      public boolean isForceEnabled(long changeId) {
         return this.enabled.contains(changeId);
      }

      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      public boolean isForceDisabled(long changeId) {
         return this.disabled.contains(changeId);
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (!(o instanceof ChangeConfig)) {
            return false;
         } else {
            ChangeConfig that = (ChangeConfig)o;
            return this.enabled.equals(that.enabled) && this.disabled.equals(that.disabled);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.enabled, this.disabled});
      }

      public String toString() {
         return "ChangeConfig{enabled=" + this.enabled + ", disabled=" + this.disabled + '}';
      }
   }

   @SystemApi(
      client = SystemApi.Client.MODULE_LIBRARIES
   )
   public interface BehaviorChangeDelegate {
      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      default void onChangeReported(long changeId) {
         Log.d("kook", " onChangeReported ");
      }

      @SystemApi(
         client = SystemApi.Client.MODULE_LIBRARIES
      )
      default boolean isChangeEnabled(long changeId) {
         Log.d("kook", " kook 移植系统java");
         return true;
      }
   }
}
