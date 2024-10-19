package com.lody.virtual.client.hook.secondary;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.ReflectException;

public class HackAppUtils {
   public static void enableQQLogOutput(String packageName, ClassLoader classLoader) {
      if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jNCpqAQIgKRc+Vg==")).equals(packageName)) {
         try {
            Reflect.on(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41DmogTSBlJxogPC4MO2EgLylqATAiKToqRH0VGjE=")), classLoader).set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQYYU2Y2FhVpHB4AJBVbRGEINA5jDCRLJAhSVg==")), 100);
         } catch (ReflectException var3) {
         }
      }

   }
}
