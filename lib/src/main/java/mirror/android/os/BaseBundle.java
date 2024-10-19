package mirror.android.os;

import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;

public class BaseBundle {
   public static Class<?> TYPE = RefClass.load(BaseBundle.class, "android.os.BaseBundle");
   public static RefObject<android.os.Parcel> mParcelledData;
}
