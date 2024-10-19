package mirror.com.android.internal.policy;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefStaticObject;

public class PhoneWindow {
   public static Class<?> TYPE = RefClass.load(PhoneWindow.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlALxwPBhEJRwYDAwlcJg0BMRY0BhwUBhhKJAwcEgoZEhINDhUVGycBHwEXBA=="));
   public static RefStaticObject<IInterface> sWindowManager;

   static {
      if (TYPE == null) {
         TYPE = RefClass.load(PhoneWindow.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlALxwPBhEJRz8GHAsXIQwAOxwUSyUZBwsBBCgTGAQJOgErAB4UDB0="));
      }

   }
}
