package com.lody.virtual.client.hook.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LogInvocation {
   Condition value() default LogInvocation.Condition.ALWAYS;

   public static enum Condition {
      NEVER {
         public int getLogLevel(boolean isHooked, boolean isError) {
            return -1;
         }
      },
      ALWAYS {
         public int getLogLevel(boolean isHooked, boolean isError) {
            return isError ? 5 : 4;
         }
      },
      ON_ERROR {
         public int getLogLevel(boolean isHooked, boolean isError) {
            return isError ? 5 : -1;
         }
      },
      NOT_HOOKED {
         public int getLogLevel(boolean isHooked, boolean isError) {
            return isHooked ? -1 : (isError ? 5 : 4);
         }
      };

      private Condition() {
      }

      public abstract int getLogLevel(boolean var1, boolean var2);

      // $FF: synthetic method
      Condition(Object x2) {
         this();
      }
   }
}
