package mirror.android.net.wifi;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefStaticMethod;

@TargetApi(19)
public class WifiSsid {
   public static final Class<?> TYPE = RefClass.load(WifiSsid.class, StringFog.decrypt("EgsWBAoHO10NCgZeHgYIGkslHwMHDAAKCw=="));
   public static RefStaticMethod<Object> createFromAsciiEncoded;
   public static RefStaticMethod<Object> createFromByteArray;
   public static RefStaticMethod<Object> createFromHex;
}
