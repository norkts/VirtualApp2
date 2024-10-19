package mirror.android.app;

import android.annotation.TargetApi;
import android.os.IInterface;
import mirror.RefClass;
import mirror.RefObject;

@TargetApi(19)
public class AppOpsManager {
   public static Class<?> TYPE = RefClass.load(AppOpsManager.class, android.app.AppOpsManager.class);
   public static RefObject<IInterface> mService;
}
