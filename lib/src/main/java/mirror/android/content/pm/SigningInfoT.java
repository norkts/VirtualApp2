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
   public static Class<?> TYPE = RefClass.load(SigningInfo.class, "android.content.pm.SigningInfo");
   @MethodReflectParams({"android.content.pm.SigningDetails"})
   public static RefConstructor<Object> ctor;

   public static Object createSigningInfo(android.content.pm.PackageParser.SigningDetails signingDetails) {
      try {
         Object detail = Class.forName("android.content.pm.SigningDetails").getDeclaredConstructor((new Signature[0]).getClass(), Integer.TYPE, (new ArraySet()).getClass(), (new Signature[0]).getClass()).newInstance(Reflect.on((Object)signingDetails).field("signatures").get(), Reflect.on((Object)signingDetails).field("signatureSchemeVersion").get(), Reflect.on((Object)signingDetails).field("publicKeys").get(), Reflect.on((Object)signingDetails).field("pastSigningCertificates").get());
         Constructor[] var2 = Class.forName("android.content.pm.SigningInfo").getDeclaredConstructors();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Constructor constructorSigInfo = var2[var4];
            if (constructorSigInfo.toString().contains("SigningDetails")) {
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
