package mirror.android.content.pm.split;

import android.util.SparseArray;
import com.lody.virtual.StringFog;
import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class SplitDependencyLoader {
   public static Class<?> TYPE = RefClass.load(SplitDependencyLoader.class, "android.content.pm.split.SplitDependencyLoader");
   @MethodReflectParams({"android.content.pm.PackageParser$PackageLite"})
   public static RefStaticMethod<SparseArray<int[]>> createDependenciesFromPackage;
}
