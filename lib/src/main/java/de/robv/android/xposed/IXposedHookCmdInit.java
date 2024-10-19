package de.robv.android.xposed;

public interface IXposedHookCmdInit extends IXposedMod {
   void initCmdApp(StartupParam var1) throws Throwable;

   public static final class StartupParam {
      public String modulePath;
      public String startClassName;

      StartupParam() {
      }
   }
}
