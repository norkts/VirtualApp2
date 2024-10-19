package mirror.android.view;

import android.graphics.Bitmap;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class SurfaceControl {
   public static Class<?> TYPE = RefClass.load(SurfaceControl.class, "android.view.SurfaceControl");
   @MethodParams({int.class, int.class})
   public static RefStaticMethod<Bitmap> screnshot;
}
