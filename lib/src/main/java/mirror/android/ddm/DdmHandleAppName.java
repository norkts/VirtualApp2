package mirror.android.ddm;

import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class DdmHandleAppName {
   public static Class Class = RefClass.load(DdmHandleAppName.class, StringFog.decrypt("EgsWBAoHO10HCx9eLQsDOwQcEgkLHgMTIRMdDA=="));
   @MethodParams({String.class})
   public static RefStaticMethod<Void> setAppName;
}
