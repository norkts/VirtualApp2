package mirror.com.android.internal.textservice;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ITextServicesManager {
   public static Class<?> TYPE = RefClass.load(ITextServicesManager.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYbGwEVGxkHEABcPzELJwcwCgAGAAwLACgTGAQJOgE="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAKxYbGwEVGxkHEABcPzELJwcwCgAGAAwLACgTGAQJOgFHPAYFCw=="));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
