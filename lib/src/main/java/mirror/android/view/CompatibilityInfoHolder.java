package mirror.android.view;

import com.lody.virtual.StringFog;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefMethod;

public class CompatibilityInfoHolder {
   public static Class<?> Class = RefClass.load(CompatibilityInfoHolder.class, StringFog.decrypt("EgsWBAoHO10VBhcHRywBHhUTAgwMNh8KGws5BwkBOwoeEgAc"));
   @MethodReflectParams({"android.content.res.CompatibilityInfo"})
   public static RefMethod<Void> set;
}
