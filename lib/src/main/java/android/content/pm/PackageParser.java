package android.content.pm;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.SparseArray;
import com.lody.virtual.StringFog;
import java.io.File;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class PackageParser {
   public static final int PARSE_IS_SYSTEM = 1;
   public static final int PARSE_IS_SYSTEM_DIR = 16;
   public static final int PARSE_COLLECT_CERTIFICATES = 32;
   public static final int PARSE_ENFORCE_CODE = 64;

   public static ApkLite parseApkLite(File apkFile, int flags) throws PackageParserException {
      throw new RuntimeException("Stub!");
   }

   @TargetApi(29)
   public void setCallback(Callback cb) {
      throw new RuntimeException("Stub!");
   }

   private abstract static class SplitDependencyLoader<E extends Exception> {
      private static int[] append(int[] src, int elem) {
         if (src == null) {
            return new int[]{elem};
         } else {
            int[] dst = Arrays.copyOf(src, src.length + 1);
            dst[src.length] = elem;
            return dst;
         }
      }

      public static SparseArray<int[]> createDependenciesFromPackage(PackageLite pkg) throws IllegalDependencyException {
         SparseArray<int[]> splitDependencies = new SparseArray();
         splitDependencies.put(0, new int[]{-1});

         int i;
         for(int splitIdx = 0; splitIdx < pkg.splitNames.length; ++splitIdx) {
            if (pkg.isFeatureSplits[splitIdx]) {
               String splitDependency = pkg.usesSplitNames[splitIdx];
               if (splitDependency != null) {
                  splitIdx = Arrays.binarySearch(pkg.splitNames, splitDependency);
                  if (splitIdx < 0) {
                     throw new IllegalDependencyException("Split \'" + pkg.splitNames[splitIdx] + "\' requires split \'" + splitDependency + "\', which is missing.");
                  }

                  i = splitIdx + 1;
               } else {
                  i = 0;
               }

               splitDependencies.put(splitIdx + 1, new int[]{i});
            }
         }

         int splitIdx = 0;
         int targetSplitIdx;
         for(i = pkg.splitNames.length; splitIdx < i; ++splitIdx) {
            if (!pkg.isFeatureSplits[splitIdx]) {
               String configForSplit = pkg.configForSplit[splitIdx];
               if (configForSplit != null) {
                  int depIdx = Arrays.binarySearch(pkg.splitNames, configForSplit);
                  if (depIdx < 0) {
                     throw new IllegalDependencyException("Split \'" + pkg.splitNames[splitIdx] + "\' targets split \'" + configForSplit + "\', which is missing.");
                  }

                  if (!pkg.isFeatureSplits[depIdx]) {
                     throw new IllegalDependencyException("Split \'" + pkg.splitNames[splitIdx] + "\' declares itself as configuration split for a non-feature split \'" + pkg.splitNames[depIdx] + "\'");
                  }

                  targetSplitIdx = depIdx + 1;
               } else {
                  targetSplitIdx = 0;
               }

               splitDependencies.put(targetSplitIdx, append((int[])splitDependencies.get(targetSplitIdx), splitIdx + 1));
            }
         }

         BitSet bitset = new BitSet();
         i = 0;

         for(targetSplitIdx = splitDependencies.size(); i < targetSplitIdx; ++i) {
            splitIdx = splitDependencies.keyAt(i);
            bitset.clear();

            while(splitIdx != -1) {
               if (bitset.get(splitIdx)) {
                  throw new IllegalDependencyException("Cycle detected in split dependencies.");
               }

               bitset.set(splitIdx);
               int[] deps = (int[])splitDependencies.get(splitIdx);
               splitIdx = deps != null ? deps[0] : -1;
            }
         }

         return splitDependencies;
      }

      public static class IllegalDependencyException extends Exception {
         private IllegalDependencyException(String message) {
            super(message);
         }

         // $FF: synthetic method
         IllegalDependencyException(String x0, Object x1) {
            this(x0);
         }
      }
   }

   public static class PackageLite {
      public String packageName;
      public int versionCode;
      public int versionCodeMajor;
      public int installLocation;
      public String[] splitNames;
      public boolean[] isFeatureSplits;
      public String[] usesSplitNames;
      public String[] configForSplit;
      public String codePath;
      public String baseCodePath;
      public String[] splitCodePaths;
      public int baseRevisionCode;
      public int[] splitRevisionCodes;
      public boolean coreApp;
      public boolean debuggable;
      public boolean multiArch;
      public boolean use32bitAbi;
      public boolean extractNativeLibs;
      public boolean isolatedSplits;
      public boolean profilableByShell;
      public boolean isSplitRequired;
      public boolean useEmbeddedDex;

      public String toString() {
         return "PackageLite{packageName=\'" + this.packageName + '\'' + ", versionCode=" + this.versionCode + ", versionCodeMajor=" + this.versionCodeMajor + ", installLocation=" + this.installLocation + ", splitNames=" + Arrays.toString(this.splitNames) + ", isFeatureSplits=" + Arrays.toString(this.isFeatureSplits) + ", usesSplitNames=" + Arrays.toString(this.usesSplitNames) + ", configForSplit=" + Arrays.toString(this.configForSplit) + ", codePath=\'" + this.codePath + '\'' + ", baseCodePath=\'" + this.baseCodePath + '\'' + ", splitCodePaths=" + Arrays.toString(this.splitCodePaths) + ", baseRevisionCode=" + this.baseRevisionCode + ", splitRevisionCodes=" + Arrays.toString(this.splitRevisionCodes) + ", coreApp=" + this.coreApp + ", debuggable=" + this.debuggable + ", multiArch=" + this.multiArch + ", use32bitAbi=" + this.use32bitAbi + ", extractNativeLibs=" + this.extractNativeLibs + ", isolatedSplits=" + this.isolatedSplits + ", profilableByShell=" + this.profilableByShell + ", isSplitRequired=" + this.isSplitRequired + ", useEmbeddedDex=" + this.useEmbeddedDex + '}';
      }
   }

   public class ProviderIntentInfo extends IntentInfo {
      public Provider provider;
   }

   public class ServiceIntentInfo extends IntentInfo {
      public Service service;
   }

   public class ActivityIntentInfo extends IntentInfo {
      public Activity activity;
   }

   public final class PermissionGroup extends Component<IntentInfo> {
      public PermissionGroupInfo info;
   }

   public final class Permission extends Component<IntentInfo> {
      public PermissionInfo info;
   }

   public final class Instrumentation extends Component<IntentInfo> {
      public InstrumentationInfo info;
   }

   public final class Provider extends Component<ProviderIntentInfo> {
      public ProviderInfo info;
   }

   public final class Service extends Component<ServiceIntentInfo> {
      public ServiceInfo info;
   }

   public class Package {
      public final ArrayList<Activity> activities = new ArrayList(0);
      public final ArrayList<Activity> receivers = new ArrayList(0);
      public final ArrayList<Provider> providers = new ArrayList(0);
      public final ArrayList<Service> services = new ArrayList(0);
      public final ArrayList<Instrumentation> instrumentation = new ArrayList(0);
      public final ArrayList<Permission> permissions = new ArrayList(0);
      public final ArrayList<PermissionGroup> permissionGroups = new ArrayList(0);
      public final ArrayList<String> requestedPermissions = new ArrayList();
      public Signature[] mSignatures;
      public SigningDetails mSigningDetails;
      public Bundle mAppMetaData;
      public Object mExtras;
      public String packageName;
      public int mPreferredOrder;
      public String mSharedUserId;
      public ArrayList<String> usesLibraries;
      public ArrayList<String> usesOptionalLibraries;
      public int mVersionCode;
      public ApplicationInfo applicationInfo;
      public String mVersionName;
      public boolean use32bitAbi;
      public ArrayList<ConfigurationInfo> configPreferences = null;
      public ArrayList<FeatureInfo> reqFeatures = null;
      public int mSharedUserLabel;
   }

   public static class ApkLite {
      public String codePath;
      public String packageName;
      public String splitName;
      public int versionCode;
      public int versionCodeMajor;
      public int installLocation;
      public Signature[] signatures;
      public boolean coreApp;
      public boolean multiArch;
      public boolean use32bitAbi;
      public boolean extractNativeLibs;
   }

   public static final class Activity extends Component<ActivityIntentInfo> {
      public ActivityInfo info;
   }

   public static class Component<II extends IntentInfo> {
      public Package owner;
      public ArrayList<II> intents;
      public String className;
      public Bundle metaData;

      public ComponentName getComponentName() {
         return null;
      }
   }

   public static class IntentInfo extends IntentFilter {
      public boolean hasDefault;
      public int labelRes;
      public CharSequence nonLocalizedLabel;
      public int icon;
      public int logo;
      public int banner;
   }

   @TargetApi(28)
   public static class Builder {
      private Signature[] mSignatures;

      public Builder setSignatures(Signature[] signatures) {
         this.mSignatures = signatures;
         return this;
      }

      public SigningDetails build() throws CertificateException {
         return new SigningDetails();
      }
   }

   @TargetApi(28)
   public static final class SigningDetails {
      public Signature[] signatures;
      public Signature[] pastSigningCertificates;
      public static final SigningDetails UNKNOWN = null;
   }

   @TargetApi(29)
   public static final class CallbackImpl implements Callback {
      public CallbackImpl(PackageManager pm) {
         throw new RuntimeException("Stub!");
      }
   }

   @TargetApi(29)
   public interface Callback {
   }

   public static class PackageParserException extends Exception {
      public final int error;

      public PackageParserException(int error, String detailMessage) {
         super(detailMessage);
         this.error = error;
      }

      public PackageParserException(int error, String detailMessage, Throwable throwable) {
         super(detailMessage, throwable);
         this.error = error;
      }
   }
}
