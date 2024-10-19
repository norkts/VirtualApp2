package mirror.android.media;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IAudioService {
   public static Class<?> TYPE = RefClass.load(IAudioService.class, StringFog.decrypt("EgsWBAoHO10OChYZCEEnMhAWHwo9OgEVBhEV"));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10OChYZCEEnMhAWHwo9OgEVBhEVTTwaBgc="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
