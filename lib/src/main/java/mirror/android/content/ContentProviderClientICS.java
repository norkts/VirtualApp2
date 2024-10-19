package mirror.android.content;

import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;

public class ContentProviderClientICS {
   public static Class TYPE = RefClass.load(ContentProviderClientICS.class, android.content.ContentProviderClient.class);
   @MethodReflectParams({"android.content.ContentResolver", "android.content.IContentProvider"})
   public static RefConstructor<android.content.ContentProviderClient> ctor;
}
