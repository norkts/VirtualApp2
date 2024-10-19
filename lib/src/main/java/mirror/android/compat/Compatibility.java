package mirror.android.compat;

import com.lody.virtual.StringFog;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefStaticObject;

public class Compatibility {
   public static Class<?> TYPE = RefClass.load(Compatibility.class, StringFog.decrypt("EgsWBAoHO10AAB8ACBtAMAofBgQaNhEKAxsEEA=="));
   public static RefStaticObject<Object> DEFAULT_CALLBACKS;
   public static RefStaticObject<Object> sCallbacks;
   @MethodReflectParams({"android.compat.Compatibility$BehaviorChangeDelegate"})
   public static RefMethod<Void> setBehaviorChangeDelegate;
}
