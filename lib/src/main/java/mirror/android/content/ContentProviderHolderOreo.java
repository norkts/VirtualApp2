package mirror.android.content;

import android.content.pm.ProviderInfo;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefBoolean;
import mirror.RefClass;
import mirror.RefObject;

public class ContentProviderHolderOreo {
   public static Class<?> TYPE = RefClass.load(ContentProviderHolderOreo.class, "android.app.ContentProviderHolder");
   public static RefObject<ProviderInfo> info;
   public static RefObject<IInterface> provider;
   public static RefBoolean noReleaseNeeded;
}
