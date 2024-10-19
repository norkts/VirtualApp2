package de.robv.android.xposed;

public abstract class XC_MethodReplacement extends XC_MethodHook {
   public static final XC_MethodReplacement DO_NOTHING = new XC_MethodReplacement(20000) {
      protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
         return null;
      }
   };

   public XC_MethodReplacement() {
   }

   public XC_MethodReplacement(int priority) {
      super(priority);
   }

   protected final void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
      try {
         Object result = this.replaceHookedMethod(param);
         param.setResult(result);
      } catch (Throwable var3) {
         Throwable t = var3;
         param.setThrowable(t);
      }

   }

   protected final void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
   }

   protected abstract Object replaceHookedMethod(XC_MethodHook.MethodHookParam var1) throws Throwable;

   public static XC_MethodReplacement returnConstant(Object result) {
      return returnConstant(50, result);
   }

   public static XC_MethodReplacement returnConstant(int priority, final Object result) {
      return new XC_MethodReplacement(priority) {
         protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            return result;
         }
      };
   }
}
