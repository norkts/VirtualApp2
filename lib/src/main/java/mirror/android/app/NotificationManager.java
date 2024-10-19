package mirror.android.app;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefStaticMethod;
import mirror.RefStaticObject;

public class NotificationManager {
   public static Class<?> TYPE = RefClass.load(NotificationManager.class, "android.app.NotificationManager");
   public static RefStaticMethod<IInterface> getService;
   public static RefStaticObject<IInterface> sService;
}
