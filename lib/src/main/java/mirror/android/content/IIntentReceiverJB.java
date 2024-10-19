package mirror.android.content;

import android.os.Bundle;
import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;

public class IIntentReceiverJB {
   public static Class<?> TYPE = RefClass.load(IIntentReceiverJB.class, "android.content.IIntentReceiver");
   @MethodParams({android.content.Intent.class, int.class, String.class, Bundle.class, boolean.class, boolean.class, int.class})
   public static RefMethod<Void> performReceive;
}
