package mirror.android.os;

import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefStaticInt;
import mirror.RefStaticMethod;

public class StrictMode {
   public static Class<?> TYPE = RefClass.load(StrictMode.class, StringFog.decrypt("EgsWBAoHO10MHFwjHR0HEBE/GQEL"));
   public static RefStaticInt sVmPolicyMask;
   public static RefStaticInt DETECT_VM_FILE_URI_EXPOSURE;
   public static RefStaticInt PENALTY_DEATH_ON_FILE_URI_EXPOSURE;
   public static RefStaticMethod<Void> disableDeathOnFileUriExposure;
}
