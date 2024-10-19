package mirror.android.media.session;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ISessionManager {
   public static Class<?> TYPE = RefClass.load(ISessionManager.class, StringFog.decrypt("EgsWBAoHO10OChYZCEEdFhYBHwoAcTowCgEDAAAAPgQcFwILLQ=="));

   public static class Stub {
      public static Class<?> TYPE = RefClass.load(Stub.class, StringFog.decrypt("EgsWBAoHO10OChYZCEEdFhYBHwoAcTowCgEDAAAAPgQcFwILLVcwGwcS"));
      @MethodParams({IBinder.class})
      public static RefStaticMethod<IInterface> asInterface;
   }
}
