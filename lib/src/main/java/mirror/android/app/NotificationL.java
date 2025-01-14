package mirror.android.app;

import android.content.Context;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class NotificationL {
   public static Class<?> TYPE = RefClass.load(NotificationL.class, android.app.Notification.class);

   public static class Builder {
      public static Class<?> TYPE = RefClass.load(Builder.class, android.app.Notification.Builder.class);
      @MethodParams({Context.class, android.app.Notification.class})
      public static RefStaticMethod<android.app.Notification> rebuild;
   }
}
