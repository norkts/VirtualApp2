package mirror.android.content.pm;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import com.lody.virtual.StringFog;
import java.io.File;
import java.util.List;
import mirror.MethodParams;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefMethod;
import mirror.RefObject;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;

public class PackageParser {
   public static Class<?> TYPE = RefClass.load(PackageParser.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhc="));
   @MethodReflectParams({"android.content.pm.PackageParser$Package", "int"})
   public static RefMethod<Void> collectCertificates;
   @MethodParams({String.class})
   public static RefConstructor<android.content.pm.PackageParser> ctor;
   @MethodReflectParams({"android.content.pm.PackageParser$Activity", "int"})
   public static RefStaticMethod<ActivityInfo> generateActivityInfo;
   @MethodReflectParams({"android.content.pm.PackageParser$Package", "int"})
   public static RefStaticMethod<ApplicationInfo> generateApplicationInfo;
   @MethodReflectParams({"android.content.pm.PackageParser$Package", "[I", "int", "long", "long"})
   public static RefStaticMethod<PackageInfo> generatePackageInfo;
   @MethodReflectParams({"android.content.pm.PackageParser$Provider", "int"})
   public static RefStaticMethod<ProviderInfo> generateProviderInfo;
   @MethodReflectParams({"android.content.pm.PackageParser$Service", "int"})
   public static RefStaticMethod<ServiceInfo> generateServiceInfo;
   @MethodParams({File.class, String.class, DisplayMetrics.class, int.class})
   public static RefMethod<android.content.pm.PackageParser.Package> parsePackage;

   public static class SigningDetails {
      public static Class<?> TYPE = RefClass.load(SigningDetails.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhdWJQwJMRoNCDYVHQ4HHxY="));
      public static RefObject<Signature[]> signatures;
      public static RefObject<Signature[]> pastSigningCertificates;
      public static RefMethod<Boolean> hasPastSigningCertificates;
      public static RefMethod<Boolean> hasSignatures;
      public static RefMethod<Void> writeToParcel;
      public static RefStaticObject<Parcelable.Creator<Object>> CREATOR;
   }

   public static class Component {
      public static Class<?> TYPE = RefClass.load(Component.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhdWNQoDLxwNChwE"));
      public static RefObject<String> className;
      public static RefObject<ComponentName> componentName;
      public static RefObject<List<IntentFilter>> intents;
   }

   public static class PermissionGroup {
      public static Class<?> TYPE = RefClass.load(PermissionGroup.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhdWJgAcMhoQHBsfBygcHBAC"));
      public static RefObject<PermissionGroupInfo> info;
   }

   public static class Permission {
      public static Class<?> TYPE = RefClass.load(Permission.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhdWJgAcMhoQHBsfBw=="));
      public static RefObject<PermissionInfo> info;
   }

   public static class Service {
      public static Class<?> TYPE = RefClass.load(Provider.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhdWJQAcKRoACg=="));
      public static RefObject<ServiceInfo> info;
   }

   public static class Provider {
      public static Class<?> TYPE = RefClass.load(Provider.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhdWJhcBKRoHCgA="));
      public static RefObject<ProviderInfo> info;
   }

   public static class Activity {
      public static Class<?> TYPE = RefClass.load(Activity.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhdWNwYaNgUKGws="));
      public static RefObject<ActivityInfo> info;
   }

   public static class Package {
      public static Class<?> TYPE = RefClass.load(Package.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWDUPPBgCCBcgCB0dFhdWJgQNNBIECg=="));
      public static RefObject<List> activities;
      public static RefObject<Bundle> mAppMetaData;
      public static RefObject<String> mSharedUserId;
      public static RefObject<Signature[]> mSignatures;
      public static RefObject<Integer> mVersionCode;
      public static RefObject<String> packageName;
      public static RefObject<List> permissionGroups;
      public static RefObject<List> permissions;
      public static RefObject<List<String>> protectedBroadcasts;
      public static RefObject<List> providers;
      public static RefObject<List> receivers;
      public static RefObject<List<String>> requestedPermissions;
      public static RefObject<List> services;
      public static RefObject<Object> mSigningDetails;
   }
}
