package mirror.android.os;

import java.io.FileDescriptor;
import mirror.RefClass;
import mirror.RefMethod;

public class MemoryFile {
   public static Class<?> TYPE = RefClass.load(MemoryFile.class, android.os.MemoryFile.class);
   public static RefMethod<FileDescriptor> getFileDescriptor;
}
