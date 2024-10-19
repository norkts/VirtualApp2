package mirror.android.content;

import android.util.ArraySet;
import java.util.List;
import mirror.RefClass;
import mirror.RefObject;

public class IntentFilterU {
   public static Class TYPE = RefClass.load(IntentFilterU.class, IntentFilter.class);
   public static RefObject<ArraySet<String>> mActions;
   public static RefObject<List<String>> mCategories;
}
