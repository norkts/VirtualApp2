package mirror.android.view;

import com.lody.virtual.StringFog;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefMethod;

public class CompatibilityInfoHolder {
   public static Class<?> Class = RefClass.load(CompatibilityInfoHolder.class, "android.view.CompatibilityInfoHolder");
   @MethodReflectParams({"android.content.res.CompatibilityInfo"})
   public static RefMethod<Void> set;
}
