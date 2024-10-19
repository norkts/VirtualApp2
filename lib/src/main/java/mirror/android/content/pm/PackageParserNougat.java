package mirror.android.content.pm;

import com.lody.virtual.StringFog;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class PackageParserNougat {
   public static Class<?> TYPE = RefClass.load(PackageParserNougat.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhc="));
   @MethodReflectParams({"android.content.pm.PackageParser$Package", "int"})
   public static RefStaticMethod<Void> collectCertificates;
}
