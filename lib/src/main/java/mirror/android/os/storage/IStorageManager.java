package mirror.android.os.storage;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class IStorageManager {
   public static Class<?> Class = RefClass.load(IStorageManager.class, StringFog.decrypt("EgsWBAoHO10MHFwDHQAcEgIXWCw9KxwRDhUVJA4AEgIXBA=="));

   public static class Stub {
      public static Class<?> Class = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10MHFwDHQAcEgIXWCw9KxwRDhUVJA4AEgIXBEE9KwYB"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
