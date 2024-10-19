package mirror.android.app;

import android.graphics.drawable.Icon;
import mirror.RefClass;
import mirror.RefObject;

public class NotificationM {
   public static Class<?> TYPE = RefClass.load(NotificationM.class, android.app.Notification.class);
   public static RefObject<Icon> mLargeIcon;
   public static RefObject<Icon> mSmallIcon;
}
