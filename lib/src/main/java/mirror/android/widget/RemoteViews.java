package mirror.android.widget;

import android.content.pm.ApplicationInfo;
import java.util.ArrayList;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefObject;

public class RemoteViews {
   public static Class<?> TYPE = RefClass.load(RemoteViews.class, android.widget.RemoteViews.class);
   @MethodParams({ApplicationInfo.class, int.class})
   public static RefConstructor<android.widget.RemoteViews> ctor;
   public static RefObject<ApplicationInfo> mApplication;
   public static RefObject<ArrayList<Object>> mActions;
   public static RefObject<String> mPackage;
}
