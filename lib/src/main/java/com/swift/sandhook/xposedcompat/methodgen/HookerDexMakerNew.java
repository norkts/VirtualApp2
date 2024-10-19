package com.swift.sandhook.xposedcompat.methodgen;

import android.text.TextUtils;
import com.android.dx.Code;
import com.android.dx.DexMaker;
import com.android.dx.FieldId;
import com.android.dx.Local;
import com.android.dx.MethodId;
import com.android.dx.TypeId;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.wrapper.HookWrapper;
import com.swift.sandhook.xposedcompat.hookstub.HookStubManager;
import com.swift.sandhook.xposedcompat.utils.DexMakerUtils;
import dalvik.system.InMemoryDexClassLoader;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Map;

public class HookerDexMakerNew implements HookMaker {
   public static final String METHOD_NAME_BACKUP = "backup";
   public static final String METHOD_NAME_HOOK = "hook";
   public static final TypeId<Object[]> objArrayTypeId = TypeId.get(Object[].class);
   private static final String CLASS_DESC_PREFIX = "L";
   private static final String CLASS_NAME_PREFIX = "SandHookerNew";
   private static final String FIELD_NAME_HOOK_INFO = "additionalHookInfo";
   private static final String FIELD_NAME_METHOD = "method";
   private static final String FIELD_NAME_BACKUP_METHOD = "backupMethod";
   private static final TypeId<Member> memberTypeId = TypeId.get(Member.class);
   private static final TypeId<Method> methodTypeId = TypeId.get(Method.class);
   private static final TypeId<XposedBridge.AdditionalHookInfo> hookInfoTypeId = TypeId.get(XposedBridge.AdditionalHookInfo.class);
   private FieldId<?, XposedBridge.AdditionalHookInfo> mHookInfoFieldId;
   private FieldId<?, Member> mMethodFieldId;
   private FieldId<?, Method> mBackupMethodFieldId;
   private MethodId<?, ?> mHookMethodId;
   private MethodId<?, ?> mBackupMethodId;
   private MethodId<?, ?> mSandHookBridgeMethodId;
   private TypeId<?> mHookerTypeId;
   private TypeId<?>[] mParameterTypeIds;
   private Class<?>[] mActualParameterTypes;
   private Class<?> mReturnType;
   private TypeId<?> mReturnTypeId;
   private boolean mIsStatic;
   private boolean mHasThrowable;
   private DexMaker mDexMaker;
   private Member mMember;
   private XposedBridge.AdditionalHookInfo mHookInfo;
   private ClassLoader mAppClassLoader;
   private Class<?> mHookClass;
   private Method mHookMethod;
   private Method mBackupMethod;
   private String mDexDirPath;

   private static TypeId<?>[] getParameterTypeIds(Class<?>[] parameterTypes, boolean isStatic) {
      int parameterSize = parameterTypes.length;
      int targetParameterSize = isStatic ? parameterSize : parameterSize + 1;
      TypeId<?>[] parameterTypeIds = new TypeId[targetParameterSize];
      int offset = 0;
      if (!isStatic) {
         parameterTypeIds[0] = TypeId.OBJECT;
         offset = 1;
      }

      for(int i = 0; i < parameterTypes.length; ++i) {
         parameterTypeIds[i + offset] = TypeId.get(parameterTypes[i]);
      }

      return parameterTypeIds;
   }

   private static Class<?>[] getParameterTypes(Class<?>[] parameterTypes, boolean isStatic) {
      if (isStatic) {
         return parameterTypes;
      } else {
         int parameterSize = parameterTypes.length;
         int targetParameterSize = parameterSize + 1;
         Class<?>[] newParameterTypes = new Class[targetParameterSize];
         int offset = 1;
         newParameterTypes[0] = Object.class;
         System.arraycopy(parameterTypes, 0, newParameterTypes, offset, parameterTypes.length);
         return newParameterTypes;
      }
   }

   public void start(Member member, XposedBridge.AdditionalHookInfo hookInfo, ClassLoader appClassLoader, String dexDirPath) throws Exception {
      if (member instanceof Method) {
         Method method = (Method)member;
         this.mIsStatic = Modifier.isStatic(method.getModifiers());
         this.mReturnType = method.getReturnType();
         if (!this.mReturnType.equals(Void.class) && !this.mReturnType.equals(Void.TYPE) && !this.mReturnType.isPrimitive()) {
            this.mReturnType = Object.class;
            this.mReturnTypeId = TypeId.OBJECT;
         } else {
            this.mReturnTypeId = TypeId.get(this.mReturnType);
         }

         this.mParameterTypeIds = getParameterTypeIds(method.getParameterTypes(), this.mIsStatic);
         this.mActualParameterTypes = getParameterTypes(method.getParameterTypes(), this.mIsStatic);
         this.mHasThrowable = method.getExceptionTypes().length > 0;
      } else {
         if (!(member instanceof Constructor)) {
            if (member.getDeclaringClass().isInterface()) {
               throw new IllegalArgumentException("Cannot hook interfaces: " + member.toString());
            }

            if (Modifier.isAbstract(member.getModifiers())) {
               throw new IllegalArgumentException("Cannot hook abstract methods: " + member.toString());
            }

            throw new IllegalArgumentException("Only methods and constructors can be hooked: " + member.toString());
         }

         Constructor constructor = (Constructor)member;
         this.mIsStatic = false;
         this.mReturnType = Void.TYPE;
         this.mReturnTypeId = TypeId.VOID;
         this.mParameterTypeIds = getParameterTypeIds(constructor.getParameterTypes(), this.mIsStatic);
         this.mActualParameterTypes = getParameterTypes(constructor.getParameterTypes(), this.mIsStatic);
         this.mHasThrowable = constructor.getExceptionTypes().length > 0;
      }

      this.mMember = member;
      this.mHookInfo = hookInfo;
      this.mDexDirPath = dexDirPath;
      if (appClassLoader != null && !appClassLoader.getClass().getName().equals("java.lang.BootClassLoader")) {
         this.mAppClassLoader = appClassLoader;
      } else {
         this.mAppClassLoader = this.getClass().getClassLoader();
      }

      this.mDexMaker = new DexMaker();
      String className = this.getClassName(this.mMember);
      String dexName = className + ".jar";
      HookWrapper.HookEntity hookEntity = null;

      try {
         ClassLoader loader = this.mDexMaker.loadClassDirect(this.mAppClassLoader, new File(this.mDexDirPath), dexName);
         if (loader != null) {
            hookEntity = this.loadHookerClass(loader, className);
         }
      } catch (Throwable var9) {
      }

      if (hookEntity == null) {
         hookEntity = this.doMake(className, dexName);
      }

      SandHook.hook(hookEntity);
   }

   private HookWrapper.HookEntity doMake(String className, String dexName) throws Exception {
      this.mHookerTypeId = TypeId.get("L" + className + ";");
      this.mDexMaker.declare(this.mHookerTypeId, className + ".generated", 1, TypeId.OBJECT);
      this.generateFields();
      this.generateHookMethod();
      this.generateBackupMethod();
      ClassLoader loader = null;
      if (TextUtils.isEmpty(this.mDexDirPath)) {
         if (SandHookConfig.SDK_INT < 26) {
            throw new IllegalArgumentException("dexDirPath should not be empty!!!");
         }

         byte[] dexBytes = this.mDexMaker.generate();
         loader = new InMemoryDexClassLoader(ByteBuffer.wrap(dexBytes), this.mAppClassLoader);
      } else {
         try {
            loader = this.mDexMaker.generateAndLoad(this.mAppClassLoader, new File(this.mDexDirPath), dexName);
         } catch (IOException var6) {
            if (SandHookConfig.SDK_INT >= 26) {
               byte[] dexBytes = this.mDexMaker.generate();
               loader = new InMemoryDexClassLoader(ByteBuffer.wrap(dexBytes), this.mAppClassLoader);
            }
         }
      }

      return loader == null ? null : this.loadHookerClass((ClassLoader)loader, className);
   }

   private HookWrapper.HookEntity loadHookerClass(ClassLoader loader, String className) throws Exception {
      this.mHookClass = loader.loadClass(className);
      this.mHookMethod = this.mHookClass.getMethod("hook", this.mActualParameterTypes);
      this.mBackupMethod = this.mHookClass.getMethod("backup");
      this.setup(this.mHookClass);
      return new HookWrapper.HookEntity(this.mMember, this.mHookMethod, this.mBackupMethod, false);
   }

   private void setup(Class mHookClass) {
      XposedHelpers.setStaticObjectField(mHookClass, "method", this.mMember);
      XposedHelpers.setStaticObjectField(mHookClass, "backupMethod", this.mBackupMethod);
      XposedHelpers.setStaticObjectField(mHookClass, "additionalHookInfo", this.mHookInfo);
   }

   private String getClassName(Member originMethod) {
      return "SandHookerNew_" + DexMakerUtils.MD5(originMethod.toString());
   }

   public Method getHookMethod() {
      return this.mHookMethod;
   }

   public Method getBackupMethod() {
      return this.mBackupMethod;
   }

   public Method getCallBackupMethod() {
      return this.mBackupMethod;
   }

   public Class getHookClass() {
      return this.mHookClass;
   }

   private void generateFields() {
      this.mHookInfoFieldId = this.mHookerTypeId.getField(hookInfoTypeId, "additionalHookInfo");
      this.mMethodFieldId = this.mHookerTypeId.getField(memberTypeId, "method");
      this.mBackupMethodFieldId = this.mHookerTypeId.getField(methodTypeId, "backupMethod");
      this.mDexMaker.declare(this.mHookInfoFieldId, 8, (Object)null);
      this.mDexMaker.declare(this.mMethodFieldId, 8, (Object)null);
      this.mDexMaker.declare(this.mBackupMethodFieldId, 8, (Object)null);
   }

   private void generateBackupMethod() {
      this.mBackupMethodId = this.mHookerTypeId.getMethod(TypeId.VOID, "backup");
      Code code = this.mDexMaker.declare(this.mBackupMethodId, 9);
      code.returnVoid();
   }

   private void generateHookMethod() {
      this.mHookMethodId = this.mHookerTypeId.getMethod(this.mReturnTypeId, "hook", this.mParameterTypeIds);
      this.mSandHookBridgeMethodId = TypeId.get(HookStubManager.class).getMethod(TypeId.get(Object.class), "hookBridge", memberTypeId, methodTypeId, hookInfoTypeId, TypeId.get(Object.class), TypeId.get(Object[].class));
      Code code = this.mDexMaker.declare(this.mHookMethodId, 9);
      Local<Member> originMethod = code.newLocal(memberTypeId);
      Local<Method> backupMethod = code.newLocal(methodTypeId);
      Local<XposedBridge.AdditionalHookInfo> hookInfo = code.newLocal(hookInfoTypeId);
      Local<Object> thisObject = code.newLocal(TypeId.OBJECT);
      Local<Object[]> args = code.newLocal(objArrayTypeId);
      Local<Integer> actualParamSize = code.newLocal(TypeId.INT);
      Local<Integer> argIndex = code.newLocal(TypeId.INT);
      Local<Object> resultObj = code.newLocal(TypeId.OBJECT);
      Local[] allArgsLocals = this.createParameterLocals(code);
      Map<TypeId, Local> resultLocals = DexMakerUtils.createResultLocals(code);
      code.loadConstant(args, null);
      code.loadConstant(argIndex, 0);
      code.sget(this.mMethodFieldId, originMethod);
      code.sget(this.mBackupMethodFieldId, backupMethod);
      code.sget(this.mHookInfoFieldId, hookInfo);
      int paramsSize = this.mParameterTypeIds.length;
      int offset = 0;
      if (this.mIsStatic) {
         code.loadConstant(thisObject, (Object)null);
      } else {
         offset = 1;
         code.move(thisObject, allArgsLocals[0]);
      }

      code.loadConstant(actualParamSize, paramsSize - offset);
      code.newArray(args, actualParamSize);

      Local matchObjLocal;
      for(int i = offset; i < paramsSize; ++i) {
         matchObjLocal = allArgsLocals[i];
         DexMakerUtils.autoBoxIfNecessary(code, resultObj, matchObjLocal);
         code.loadConstant(argIndex, i - offset);
         code.aput(args, argIndex, resultObj);
      }

      if (this.mReturnTypeId.equals(TypeId.VOID)) {
         code.invokeStatic(this.mSandHookBridgeMethodId, (Local)null, originMethod, backupMethod, hookInfo, thisObject, args);
         code.returnVoid();
      } else {
         code.invokeStatic(this.mSandHookBridgeMethodId, resultObj, originMethod, backupMethod, hookInfo, thisObject, args);
         TypeId objTypeId = DexMakerUtils.getObjTypeIdIfPrimitive(this.mReturnTypeId);
         matchObjLocal = (Local)resultLocals.get(objTypeId);
         code.cast(matchObjLocal, resultObj);
         Local toReturn = (Local)resultLocals.get(this.mReturnTypeId);
         DexMakerUtils.autoUnboxIfNecessary(code, toReturn, matchObjLocal, resultLocals, true);
         code.returnValue(toReturn);
      }

   }

   private Local[] createParameterLocals(Code code) {
      Local[] paramLocals = new Local[this.mParameterTypeIds.length];

      for(int i = 0; i < this.mParameterTypeIds.length; ++i) {
         paramLocals[i] = code.getParameter(i, this.mParameterTypeIds[i]);
      }

      return paramLocals;
   }
}
