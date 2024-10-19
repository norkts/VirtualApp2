package mirror.android.app;

import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import java.util.List;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class IApplicationThread {
   public static Class<?> TYPE = RefClass.load(IApplicationThread.class, StringFog.decrypt("EgsWBAoHO10CHwJeIC4eAwkbFQQaNhwNOxoCDA4K"));
   @MethodParams({List.class, IBinder.class})
   public static RefMethod<Void> scheduleNewIntent;
   @MethodParams({IBinder.class, ServiceInfo.class})
   public static RefMethod<Void> scheduleCreateService;
   @MethodParams({IBinder.class, Intent.class, boolean.class})
   public static RefMethod<Void> scheduleBindService;
   @MethodParams({IBinder.class, Intent.class})
   public static RefMethod<Void> scheduleUnbindService;
   @MethodParams({IBinder.class, int.class, int.class, Intent.class})
   public static RefMethod<Void> scheduleServiceArgs;
   @MethodParams({IBinder.class})
   public static RefMethod<Void> scheduleStopService;
}
