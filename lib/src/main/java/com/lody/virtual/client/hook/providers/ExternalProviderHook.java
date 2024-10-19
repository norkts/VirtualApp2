package com.lody.virtual.client.hook.providers;

import android.content.AttributionSource;
import android.os.IInterface;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.fixer.ContextFixer;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;

public class ExternalProviderHook extends ProviderHook {
   public ExternalProviderHook(IInterface base) {
      super(base);
   }

   protected void processArgs(Method method, Object... args) {
      if (args != null && args.length > 0) {
         if (args[0] instanceof String) {
            String pkg = (String)args[0];
            if (VirtualCore.get().isAppInstalled(pkg)) {
               args[0] = VirtualCore.get().getHostPkg();
            }
         } else {
            try {
               if (BuildCompat.isS()) {
                  int index = MethodParameterUtils.getIndex(args, AttributionSource.class);
                  if (index < 0) {
                     return;
                  }

                  AttributionSource source = (AttributionSource)args[index];
                  ContextFixer.fixAttributionSource(source);
               }
            } catch (Exception var5) {
               Exception e = var5;
               e.printStackTrace();
            }
         }

      }
   }
}
