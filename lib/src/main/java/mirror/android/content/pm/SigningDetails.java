package mirror.android.content.pm;

import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefConstructor;

public class SigningDetails {
   public static Class<?> TYPE = RefClass.load(SigningDetails.class, "android.content.pm.SigningDetails");
   @MethodParams({android.content.pm.PackageParser.SigningDetails.class})
   public static RefConstructor<Object> ctor;
}
