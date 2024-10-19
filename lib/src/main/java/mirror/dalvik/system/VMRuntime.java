package mirror.dalvik.system;

import com.lody.virtual.StringFog;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefStaticMethod;

public class VMRuntime {
   public static Class<?> TYPE = RefClass.load(VMRuntime.class, StringFog.decrypt("FwQeAAwFcQAaHAYVBEE4PjcHGBEHMhY="));
   public static RefStaticMethod<Object> getRuntime;
   @MethodParams({int.class})
   public static RefMethod<Void> setTargetSdkVersion;
   public static RefMethod<Boolean> is64Bit;
   @MethodParams({String.class})
   public static RefStaticMethod<Boolean> is64BitAbi;
   public static RefStaticMethod<String> getCurrentInstructionSet;
   public static RefMethod<Boolean> isJavaDebuggable;
}
