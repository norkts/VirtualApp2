package mirror.android.content.pm;

import android.content.pm.Signature;
import android.util.ArraySet;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Constructor;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;

public class SigningInfoT {
   public static Class<?> TYPE = RefClass.load(SigningInfo.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDYHOB0KARU5BwkB"));
   @MethodReflectParams({"android.content.pm.SigningDetails"})
   public static RefConstructor<Object> ctor;

   public static Object createSigningInfo(android.content.pm.PackageParser.SigningDetails signingDetails) {
      try {
         Object detail = Class.forName(StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDYHOB0KARU0DBsPGgkB")).getDeclaredConstructor((new Signature[0]).getClass(), Integer.TYPE, (new ArraySet()).getClass(), (new Signature[0]).getClass()).newInstance(Reflect.on((Object)signingDetails).field(StringFog.decrypt("AAwVGAQaKgEGHA==")).get(), Reflect.on((Object)signingDetails).field(StringFog.decrypt("AAwVGAQaKgEGPBEYDAILJQAABQwBMQ==")).get(), Reflect.on((Object)signingDetails).field(StringFog.decrypt("AxAQGgwNFBYaHA==")).get(), Reflect.on((Object)signingDetails).field(StringFog.decrypt("AwQBAjYHOB0KARUzDB0aGgMbFQQaOgA=")).get());
         Constructor[] var2 = Class.forName(StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDYHOB0KARU5BwkB")).getDeclaredConstructors();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Constructor constructorSigInfo = var2[var4];
            if (constructorSigInfo.toString().contains(StringFog.decrypt("IAwVGAwAODcGGxMZBRw="))) {
               return constructorSigInfo.newInstance(detail);
            }
         }

         return null;
      } catch (Throwable var6) {
         Throwable e = var6;
         e.printStackTrace();
         return null;
      }
   }
}
