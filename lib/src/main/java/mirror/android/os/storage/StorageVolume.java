package mirror.android.os.storage;

import com.lody.virtual.StringFog;
import java.io.File;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class StorageVolume {
   public static Class<?> TYPE = RefClass.load(StorageVolume.class, StringFog.decrypt("EgsWBAoHO10MHFwDHQAcEgIXWDYaMAECCBcmBgMbHgA="));
   public static RefObject<File> mPath;
   public static RefObject<File> mInternalPath;
   public static RefObject<String> mDescription;
   public static RefMethod<String> getPath;
   public static RefMethod<File> getPathFile;
}
