package mirror.android.util;

import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class Singleton {
   public static Class<?> TYPE = RefClass.load(Singleton.class, StringFog.decrypt("EgsWBAoHO10WGxscRzwHHQIeExEBMQ=="));
   public static RefMethod<Object> get;
   public static RefObject<Object> mInstance;
}
