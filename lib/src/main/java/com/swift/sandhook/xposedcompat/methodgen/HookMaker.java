package com.swift.sandhook.xposedcompat.methodgen;

import de.robv.android.xposed.XposedBridge;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public interface HookMaker {
   void start(Member var1, XposedBridge.AdditionalHookInfo var2, ClassLoader var3, String var4) throws Exception;

   Method getHookMethod();

   Method getBackupMethod();

   Method getCallBackupMethod();
}
