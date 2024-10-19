package de.robv.android.xposed;

public interface IXposedHookZygoteInit extends IXposedMod {
   void initZygote(StartupParam var1) throws Throwable;

   public static final class StartupParam {
      public String modulePath;
      public boolean startsSystemServer;

      StartupParam() {
      }
   }
}
