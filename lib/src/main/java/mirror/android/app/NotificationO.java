package mirror.android.app;

import mirror.RefClass;
import mirror.RefObject;

public class NotificationO {
   public static Class<?> TYPE = RefClass.load(NotificationO.class, android.app.Notification.class);
   public static RefObject<String> mChannelId;
}
