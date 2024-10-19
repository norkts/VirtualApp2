package mirror.android.content;

import mirror.MethodReflectParams;
import mirror.RefClass;
import mirror.RefConstructor;

public class ContentProviderClientJB {
   public static Class TYPE = RefClass.load(ContentProviderClientJB.class, android.content.ContentProviderClient.class);
   @MethodReflectParams({"android.content.ContentResolver", "android.content.IContentProvider", "boolean"})
   public static RefConstructor<android.content.ContentProviderClient> ctor;
}
