package mirror.android.app;

import android.content.Intent;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class IActivityManagerN {
   public static Class<?> TYPE = RefClass.load(IActivityManagerN.class, StringFog.decrypt("EgsWBAoHO10CHwJeIC4NBwwEHxEXEhINDhUVGw=="));
   @MethodParams({IBinder.class, int.class, Intent.class, int.class})
   public static RefMethod<Boolean> finishActivity;
}
