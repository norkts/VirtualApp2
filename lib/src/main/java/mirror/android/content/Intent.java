package mirror.android.content;

import android.os.Bundle;
import android.os.IBinder;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class Intent {
   public static Class<?> TYPE = RefClass.load(Intent.class, android.content.Intent.class);
   @MethodParams({String.class})
   public static RefMethod<IBinder> getIBinderExtra;
   public static RefObject<Bundle> mExtras;
   @MethodParams({String.class, IBinder.class})
   public static RefMethod<Void> putExtra;
}
