package mirror.android.view;

import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class HardwareRenderer {
   public static Class<?> TYPE = RefClass.load(HardwareRenderer.class, StringFog.decrypt("EgsWBAoHO10VBhcHRycPAQEFFxcLDRYNCxcCDB0="));
   @MethodParams({File.class})
   public static RefStaticMethod<Void> setupDiskCache;
}
