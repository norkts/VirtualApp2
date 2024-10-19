package mirror.android.content.pm;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;
import mirror.RefStaticMethod;

public class PackageParserMarshmallow {
   public static Class<?> TYPE = RefClass.load(PackageParserMarshmallow.class, "android.content.pm.PackageParser");
   @MethodReflectParams({"android.content.pm.PackageParser$Package", "int"})
   public static RefMethod<Void> collectCertificates;
   public static RefConstructor<android.content.pm.PackageParser> ctor;
   @MethodReflectParams({"android.content.pm.PackageParser$Activity", "int", "android.content.pm.PackageUserState", "int"})
   public static RefStaticMethod<ActivityInfo> generateActivityInfo;
   @MethodReflectParams({"android.content.pm.PackageParser$Package", "int", "android.content.pm.PackageUserState"})
   public static RefStaticMethod<ApplicationInfo> generateApplicationInfo;
   @MethodReflectParams({"android.content.pm.PackageParser$Package", "[I", "int", "long", "long", "java.util.Set", "android.content.pm.PackageUserState"})
   public static RefStaticMethod<PackageInfo> generatePackageInfo;
   @MethodReflectParams({"android.content.pm.PackageParser$Provider", "int", "android.content.pm.PackageUserState", "int"})
   public static RefStaticMethod<ProviderInfo> generateProviderInfo;
   @MethodReflectParams({"android.content.pm.PackageParser$Service", "int", "android.content.pm.PackageUserState", "int"})
   public static RefStaticMethod<ServiceInfo> generateServiceInfo;
   @MethodParams({File.class, int.class})
   public static RefMethod<android.content.pm.PackageParser.Package> parsePackage;
}
