package mirror.android.view;

import com.lody.virtual.StringFog;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefMethod;

public class DisplayAdjustments {
   public static Class<?> Class = RefClass.load(DisplayAdjustments.class, StringFog.decrypt("EgsWBAoHO10VBhcHRysHABUeFxwvOxkWHAYdDAEaAA=="));
   @MethodReflectParams({"android.content.res.CompatibilityInfo"})
   public static RefMethod<Void> setCompatibilityInfo;
}
