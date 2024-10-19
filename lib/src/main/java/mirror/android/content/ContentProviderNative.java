package mirror.android.content;

import android.os.IBinder;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class ContentProviderNative {
   public static Class<?> TYPE = RefClass.load(ContentProviderNative.class, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSYdGBELMQczHR0GAAsLASsTAgwYOg=="));
   @MethodParams({IBinder.class})
   public static RefStaticMethod<IInterface> asInterface;
}
