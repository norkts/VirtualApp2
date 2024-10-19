package de.robv.android.xposed.callbacks;

import android.view.View;
import de.robv.android.xposed.XposedBridge;

public abstract class XC_LayoutInflated extends XCallback {
   public XC_LayoutInflated() {
   }

   public XC_LayoutInflated(int priority) {
      super(priority);
   }

   protected void call(XCallback.Param param) throws Throwable {
      if (param instanceof LayoutInflatedParam) {
         this.handleLayoutInflated((LayoutInflatedParam)param);
      }

   }

   public abstract void handleLayoutInflated(LayoutInflatedParam var1) throws Throwable;

   public class Unhook implements IXUnhook<XC_LayoutInflated> {
      private final String resDir;
      private final int id;

      public Unhook(String resDir, int id) {
         this.resDir = resDir;
         this.id = id;
      }

      public int getId() {
         return this.id;
      }

      public XC_LayoutInflated getCallback() {
         return XC_LayoutInflated.this;
      }

      public void unhook() {
      }
   }

   public static final class LayoutInflatedParam extends XCallback.Param {
      public View view;

      public LayoutInflatedParam(XposedBridge.CopyOnWriteSortedSet<XC_LayoutInflated> callbacks) {
         super(callbacks);
      }
   }
}
