package mirror.android.content;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IContentService {
   public static Class<?> TYPE = RefClass.load(IContentService.class, "android.content.IContentService");

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, "android.content.IContentService$Stub");
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
