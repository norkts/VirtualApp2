package mirror.com.android.internal.content;

import com.lody.virtual.StringFog;
import java.io.File;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefStaticMethod;

public class NativeLibraryHelper {
   public static Class<?> TYPE = RefClass.load(NativeLibraryHelper.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAPBwNGxceHUEgEhEbAAAiNhERDgAJIQoCAwAA"));
   @MethodParams({Handle.class, File.class, String.class})
   public static RefStaticMethod<Integer> copyNativeBinaries;
   @MethodParams({Handle.class, String.class})
   public static RefStaticMethod<Integer> findSupportedAbi;

   public static class Handle {
      public static Class<?> TYPE = RefClass.load(Handle.class, StringFog.decrypt("EAofWAQAOwEMBhZeAAEaFhccFwlAPBwNGxceHUEgEhEbAAAiNhERDgAJIQoCAwAAUi0PMRcPCg=="));
      @MethodParams({File.class})
      public static RefStaticMethod<Object> create;
   }
}
