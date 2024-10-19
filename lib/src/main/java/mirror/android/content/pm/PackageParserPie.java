package mirror.android.content.pm;

import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class PackageParserPie {
   public static Class<?> TYPE = RefClass.load(PackageParserPie.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhc="));
   @MethodReflectParams({"android.content.pm.PackageParser$Package", "boolean"})
   public static RefStaticMethod<Void> collectCertificates;
   @MethodParams({File.class, int.class})
   public static RefStaticMethod<Object> parseClusterPackageLite;
   @MethodParams({File.class, int.class})
   public static RefStaticMethod<Object> parseMonolithicPackageLite;
   @MethodParams({File.class, int.class})
   public static RefStaticMethod<Object> parsePackageLite;
}
