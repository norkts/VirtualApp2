package mirror.android.permission;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;

public class PermissionManager {
   public static Class<?> TYPE = RefClass.load(PermissionManager.class, "android.os.PermissionManager");
   public static RefObject<IInterface> mPermissionManager;
}
