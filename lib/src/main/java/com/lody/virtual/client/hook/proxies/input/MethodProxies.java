package com.lody.virtual.client.hook.proxies.input;

import android.view.inputmethod.EditorInfo;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.lang.reflect.Method;

class MethodProxies {
   static class StartInputOrWindowGainedFocus extends MethodProxy {
      public String getMethodName() {
         return "startInputOrWindowGainedFocus";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int editorInfoIndex = ArrayUtils.indexOfFirst(args, EditorInfo.class);
         if (editorInfoIndex != -1) {
            EditorInfo attribute = (EditorInfo)args[editorInfoIndex];
            attribute.packageName = getHostPkg();
         }

         return method.invoke(who, args);
      }
   }

   static class WindowGainedFocus extends StartInputOrWindowGainedFocus {
      public String getMethodName() {
         return "windowGainedFocus";
      }
   }

   static class StartInput extends StartInputOrWindowGainedFocus {
      public String getMethodName() {
         return "startInput";
      }
   }
}
