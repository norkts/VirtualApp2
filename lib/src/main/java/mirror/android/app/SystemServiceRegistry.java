package mirror.android.app;

import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class SystemServiceRegistry {
   public static Class<?> TYPE = RefClass.load(SystemServiceRegistry.class, "android.app.SystemServiceRegistry");
   @MethodParams({ContextImpl.class, String.class})
   public static RefStaticMethod<Object> getSystemService;
}
