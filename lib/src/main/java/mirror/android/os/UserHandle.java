package mirror.android.os;

import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class UserHandle {
   public static Class<?> TYPE = RefClass.load(UserHandle.class, StringFog.decrypt("EgsWBAoHO10MHFwlGgocOwQcEgkL"));
   @MethodParams({int.class})
   public static RefStaticMethod<Integer> getUserId;
}
