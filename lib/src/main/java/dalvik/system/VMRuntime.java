package dalvik.system;

import com.lody.virtual.StringFog;

public class VMRuntime {
   public static VMRuntime getRuntime() {
      throw new IllegalArgumentException(StringFog.decrypt("ABEHFA=="));
   }

   public native void setHiddenApiExemptions(String[] var1);
}
