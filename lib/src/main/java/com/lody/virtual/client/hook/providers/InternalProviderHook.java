package com.lody.virtual.client.hook.providers;

import android.content.AttributionSource;
import android.os.IInterface;
import com.lody.virtual.client.fixer.ContextFixer;
import java.lang.reflect.Method;

public class InternalProviderHook extends ProviderHook {
   public InternalProviderHook(IInterface base) {
      super(base);
   }

   public void processArgs(Method method, Object... args) {
      if (args != null && args.length > 0) {
         if (args[0] instanceof AttributionSource) {
            ContextFixer.fixAttributionSource(args[0]);
         }

      }
   }
}
