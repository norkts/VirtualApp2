package de.robv.android.xposed.callbacks;

import android.os.Bundle;
import de.robv.android.xposed.XposedBridge;
import java.io.Serializable;

public abstract class XCallback implements Comparable<XCallback> {
   public final int priority;
   public static final int PRIORITY_DEFAULT = 50;
   public static final int PRIORITY_LOWEST = -10000;
   public static final int PRIORITY_HIGHEST = 10000;

   /** @deprecated */
   @Deprecated
   public XCallback() {
      this.priority = 50;
   }

   public XCallback(int priority) {
      this.priority = priority;
   }

   public static void callAll(Param param) {
      if (param.callbacks == null) {
         throw new IllegalStateException("This object was not created for use with callAll");
      } else {
         for(int i = 0; i < param.callbacks.length; ++i) {
            try {
               ((XCallback)param.callbacks[i]).call(param);
            } catch (Throwable var3) {
               Throwable t = var3;
               XposedBridge.log(t);
            }
         }

      }
   }

   protected void call(Param param) throws Throwable {
   }

   public int compareTo(XCallback other) {
      if (this == other) {
         return 0;
      } else if (other.priority != this.priority) {
         return other.priority - this.priority;
      } else {
         return System.identityHashCode(this) < System.identityHashCode(other) ? -1 : 1;
      }
   }

   public abstract static class Param {
      public final Object[] callbacks;
      private Bundle extra;

      /** @deprecated */
      @Deprecated
      protected Param() {
         this.callbacks = null;
      }

      protected Param(XposedBridge.CopyOnWriteSortedSet<? extends XCallback> callbacks) {
         this.callbacks = callbacks.getSnapshot();
      }

      public synchronized Bundle getExtra() {
         if (this.extra == null) {
            this.extra = new Bundle();
         }

         return this.extra;
      }

      public Object getObjectExtra(String key) {
         Serializable o = this.getExtra().getSerializable(key);
         return o instanceof SerializeWrapper ? ((SerializeWrapper)o).object : null;
      }

      public void setObjectExtra(String key, Object o) {
         this.getExtra().putSerializable(key, new SerializeWrapper(o));
      }

      private static class SerializeWrapper implements Serializable {
         private static final long serialVersionUID = 1L;
         private final Object object;

         public SerializeWrapper(Object o) {
            this.object = o;
         }
      }
   }
}
