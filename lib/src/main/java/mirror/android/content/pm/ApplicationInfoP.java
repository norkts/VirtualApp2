package mirror.android.content.pm;

import android.content.pm.ApplicationInfo;
import java.util.List;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefObject;

public class ApplicationInfoP {
   public static Class<?> TYPE = RefClass.load(ApplicationInfoP.class, ApplicationInfo.class);
   @MethodParams({int.class})
   public static RefObject<List<Object>> sharedLibraryInfos;

   public static List sharedLibraryInfos(ApplicationInfo applicationInfo) {
      RefObject<List<Object>> field = sharedLibraryInfos;
      return field != null ? (List)field.get(applicationInfo) : null;
   }

   public static void sharedLibraryInfos(ApplicationInfo applicationInfo, List list) {
      RefObject<List<Object>> field = sharedLibraryInfos;
      if (field != null) {
         field.set(applicationInfo, list);
      }

   }
}
