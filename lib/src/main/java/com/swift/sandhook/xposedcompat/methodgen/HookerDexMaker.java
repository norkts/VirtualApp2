package com.swift.sandhook.xposedcompat.methodgen;

import android.text.TextUtils;
import com.android.dx.BinaryOp;
import com.android.dx.Code;
import com.android.dx.Comparison;
import com.android.dx.DexMaker;
import com.android.dx.FieldId;
import com.android.dx.Label;
import com.android.dx.Local;
import com.android.dx.MethodId;
import com.android.dx.TypeId;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.SandHookConfig;
import com.swift.sandhook.SandHookMethodResolver;
import com.swift.sandhook.wrapper.HookWrapper;
import com.swift.sandhook.xposedcompat.XposedCompat;
import com.swift.sandhook.xposedcompat.utils.DexLog;
import com.swift.sandhook.xposedcompat.utils.DexMakerUtils;
import dalvik.system.InMemoryDexClassLoader;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Map;

public class HookerDexMaker implements HookMaker {
   public static final String METHOD_NAME_BACKUP = "backup";
   public static final String METHOD_NAME_HOOK = "hook";
   public static final String METHOD_NAME_CALL_BACKUP = "callBackup";
   public static final String METHOD_NAME_SETUP = "setup";
   public static final String METHOD_NAME_LOG = "printMethodHookIn";
   public static final TypeId<Object[]> objArrayTypeId = TypeId.get(Object[].class);
   private static final String CLASS_DESC_PREFIX = "L";
   private static final String CLASS_NAME_PREFIX = "SandHooker";
   private static final String FIELD_NAME_HOOK_INFO = "additionalHookInfo";
   private static final String FIELD_NAME_METHOD = "method";
   private static final String FIELD_NAME_BACKUP_METHOD = "backupMethod";
   private static final String PARAMS_FIELD_NAME_METHOD = "method";
   private static final String PARAMS_FIELD_NAME_THIS_OBJECT = "thisObject";
   private static final String PARAMS_FIELD_NAME_ARGS = "args";
   private static final String CALLBACK_METHOD_NAME_BEFORE = "callBeforeHookedMethod";
   private static final String CALLBACK_METHOD_NAME_AFTER = "callAfterHookedMethod";
   private static final TypeId<Throwable> throwableTypeId = TypeId.get(Throwable.class);
   private static final TypeId<Member> memberTypeId = TypeId.get(Member.class);
   private static final TypeId<Method> methodTypeId = TypeId.get(Method.class);
   private static final TypeId<XC_MethodHook> callbackTypeId = TypeId.get(XC_MethodHook.class);
   private static final TypeId<XposedBridge.AdditionalHookInfo> hookInfoTypeId = TypeId.get(XposedBridge.AdditionalHookInfo.class);
   private static final TypeId<XposedBridge.CopyOnWriteSortedSet> callbacksTypeId = TypeId.get(XposedBridge.CopyOnWriteSortedSet.class);
   private static final TypeId<XC_MethodHook.MethodHookParam> paramTypeId = TypeId.get(XC_MethodHook.MethodHookParam.class);
   private static final MethodId<XC_MethodHook.MethodHookParam, Void> setResultMethodId;
   private static final MethodId<XC_MethodHook.MethodHookParam, Void> setThrowableMethodId;
   private static final MethodId<XC_MethodHook.MethodHookParam, Object> getResultMethodId;
   private static final MethodId<XC_MethodHook.MethodHookParam, Throwable> getThrowableMethodId;
   private static final MethodId<XC_MethodHook.MethodHookParam, Boolean> hasThrowableMethodId;
   private static final MethodId<XC_MethodHook, Void> callAfterCallbackMethodId;
   private static final MethodId<XC_MethodHook, Void> callBeforeCallbackMethodId;
   private static final FieldId<XC_MethodHook.MethodHookParam, Boolean> returnEarlyFieldId;
   private static final TypeId<XposedBridge> xposedBridgeTypeId;
   private static final MethodId<XposedBridge, Void> logThrowableMethodId;
   private FieldId<?, XposedBridge.AdditionalHookInfo> mHookInfoFieldId;
   private FieldId<?, Member> mMethodFieldId;
   private FieldId<?, Method> mBackupMethodFieldId;
   private MethodId<?, ?> mBackupMethodId;
   private MethodId<?, ?> mCallBackupMethodId;
   private MethodId<?, ?> mHookMethodId;
   private MethodId<?, ?> mPrintLogMethodId;
   private MethodId<?, ?> mSandHookCallOriginMethodId;
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
   private Method mCallBackupMethod;
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
      this.generateSetupMethod();
      if (XposedCompat.retryWhenCallOriginError) {
         this.generateBackupAndCallOriginCheckMethod();
      } else {
         this.generateBackupMethod();
      }

      this.generateCallBackupMethod();
      this.generateHookMethod();
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
      this.mBackupMethod = this.mHookClass.getMethod("backup", this.mActualParameterTypes);
      this.mCallBackupMethod = this.mHookClass.getMethod("callBackup", this.mActualParameterTypes);
      SandHook.resolveStaticMethod(this.mCallBackupMethod);
      SandHookMethodResolver.resolveMethod(this.mCallBackupMethod, this.mBackupMethod);
      SandHook.compileMethod(this.mCallBackupMethod);
      this.mHookClass.getMethod("setup", Member.class, Method.class, XposedBridge.AdditionalHookInfo.class).invoke((Object)null, this.mMember, this.mBackupMethod, this.mHookInfo);
      return new HookWrapper.HookEntity(this.mMember, this.mHookMethod, this.mBackupMethod);
   }

   private String getClassName(Member originMethod) {
      return "SandHooker_" + DexMakerUtils.MD5(originMethod.toString());
   }

   public Method getHookMethod() {
      return this.mHookMethod;
   }

   public Method getBackupMethod() {
      return this.mBackupMethod;
   }

   public Method getCallBackupMethod() {
      return this.mCallBackupMethod;
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

   private void generateSetupMethod() {
      MethodId<?, Void> setupMethodId = this.mHookerTypeId.getMethod(TypeId.VOID, "setup", memberTypeId, methodTypeId, hookInfoTypeId);
      Code code = this.mDexMaker.declare(setupMethodId, 9);
      Local<Member> method = code.getParameter(0, memberTypeId);
      Local<Method> backupMethod = code.getParameter(1, methodTypeId);
      Local<XposedBridge.AdditionalHookInfo> hookInfo = code.getParameter(2, hookInfoTypeId);
      code.sput(this.mMethodFieldId, method);
      code.sput(this.mBackupMethodFieldId, backupMethod);
      code.sput(this.mHookInfoFieldId, hookInfo);
      code.returnVoid();
   }

   private void generateBackupMethod() {
      this.mBackupMethodId = this.mHookerTypeId.getMethod(this.mReturnTypeId, "backup", this.mParameterTypeIds);
      Code code = this.mDexMaker.declare(this.mBackupMethodId, 9);
      Local<Member> method = code.newLocal(memberTypeId);
      Map<TypeId, Local> resultLocals = DexMakerUtils.createResultLocals(code);
      MethodId<?, ?> errLogMethod = TypeId.get(DexLog.class).getMethod(TypeId.get(Void.TYPE), "printCallOriginError", memberTypeId);
      Label tryCatchBlock = new Label();
      code.addCatchClause(throwableTypeId, tryCatchBlock);
      code.sget(this.mMethodFieldId, method);
      code.invokeStatic(errLogMethod, (Local)null, method);
      code.mark(tryCatchBlock);
      if (this.mReturnTypeId.equals(TypeId.VOID)) {
         code.returnVoid();
      } else {
         code.returnValue((Local)resultLocals.get(this.mReturnTypeId));
      }

   }

   private void generateBackupAndCallOriginCheckMethod() {
      this.mBackupMethodId = this.mHookerTypeId.getMethod(this.mReturnTypeId, "backup", this.mParameterTypeIds);
      this.mSandHookCallOriginMethodId = TypeId.get(ErrorCatch.class).getMethod(TypeId.get(Object.class), "callOriginError", memberTypeId, methodTypeId, TypeId.get(Object.class), TypeId.get(Object[].class));
      MethodId<?, ?> errLogMethod = TypeId.get(DexLog.class).getMethod(TypeId.get(Void.TYPE), "printCallOriginError", methodTypeId);
      Code code = this.mDexMaker.declare(this.mBackupMethodId, 9);
      Local<Member> method = code.newLocal(memberTypeId);
      Local<Method> backupMethod = code.newLocal(methodTypeId);
      Local<Object> thisObject = code.newLocal(TypeId.OBJECT);
      Local<Object[]> args = code.newLocal(objArrayTypeId);
      Local<Integer> actualParamSize = code.newLocal(TypeId.INT);
      Local<Integer> argIndex = code.newLocal(TypeId.INT);
      Local<Object> resultObj = code.newLocal(TypeId.OBJECT);
      Label tryCatchBlock = new Label();
      Local[] allArgsLocals = this.createParameterLocals(code);
      Map<TypeId, Local> resultLocals = DexMakerUtils.createResultLocals(code);
      code.addCatchClause(throwableTypeId, tryCatchBlock);
      code.sget(this.mMethodFieldId, method);
      code.invokeStatic(errLogMethod, (Local)null, method);
      code.loadConstant(args, (Object)null);
      code.loadConstant(argIndex, 0);
      code.sget(this.mBackupMethodFieldId, backupMethod);
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
         code.invokeStatic(this.mSandHookCallOriginMethodId, (Local)null, method, backupMethod, thisObject, args);
         code.returnVoid();
      } else {
         code.invokeStatic(this.mSandHookCallOriginMethodId, resultObj, method, backupMethod, thisObject, args);
         TypeId objTypeId = DexMakerUtils.getObjTypeIdIfPrimitive(this.mReturnTypeId);
         matchObjLocal = (Local)resultLocals.get(objTypeId);
         code.cast(matchObjLocal, resultObj);
         Local toReturn = (Local)resultLocals.get(this.mReturnTypeId);
         DexMakerUtils.autoUnboxIfNecessary(code, toReturn, matchObjLocal, resultLocals, true);
         code.returnValue(toReturn);
      }

      code.mark(tryCatchBlock);
      if (this.mReturnTypeId.equals(TypeId.VOID)) {
         code.returnVoid();
      } else {
         code.returnValue((Local)resultLocals.get(this.mReturnTypeId));
      }

   }

   private void generateCallBackupMethod() {
      this.mCallBackupMethodId = this.mHookerTypeId.getMethod(this.mReturnTypeId, "callBackup", this.mParameterTypeIds);
      Code code = this.mDexMaker.declare(this.mCallBackupMethodId, 9);
      Local localOrigin = code.newLocal(memberTypeId);
      Local localBackup = code.newLocal(methodTypeId);
      Local[] allArgsLocals = this.createParameterLocals(code);
      Map<TypeId, Local> resultLocals = DexMakerUtils.createResultLocals(code);
      code.sget(this.mMethodFieldId, localOrigin);
      code.sget(this.mBackupMethodFieldId, localBackup);
      MethodId methodId = TypeId.get(SandHook.class).getMethod(TypeId.get(Void.TYPE), "ensureBackupMethod", memberTypeId, methodTypeId);
      code.invokeStatic(methodId, (Local)null, localOrigin, localBackup);
      if (this.mReturnTypeId.equals(TypeId.VOID)) {
         code.invokeStatic(this.mBackupMethodId, (Local)null, allArgsLocals);
         code.returnVoid();
      } else {
         Local result = (Local)resultLocals.get(this.mReturnTypeId);
         code.invokeStatic(this.mBackupMethodId, result, allArgsLocals);
         code.returnValue(result);
      }

   }

   private void generateHookMethod() {
      this.mHookMethodId = this.mHookerTypeId.getMethod(this.mReturnTypeId, "hook", this.mParameterTypeIds);
      this.mPrintLogMethodId = TypeId.get(DexLog.class).getMethod(TypeId.get(Void.TYPE), "printMethodHookIn", TypeId.get(Member.class));
      Code code = this.mDexMaker.declare(this.mHookMethodId, 9);
      Label noHookReturn = new Label();
      Label incrementAndCheckBefore = new Label();
      Label tryBeforeCatch = new Label();
      Label noExceptionBefore = new Label();
      Label checkAndCallBackup = new Label();
      Label beginCallBefore = new Label();
      Label beginCallAfter = new Label();
      Label tryOrigCatch = new Label();
      Label noExceptionOrig = new Label();
      Label tryAfterCatch = new Label();
      Label decrementAndCheckAfter = new Label();
      Label noBackupThrowable = new Label();
      Label throwThrowable = new Label();
      Local<Boolean> disableHooks = code.newLocal(TypeId.BOOLEAN);
      Local<XposedBridge.AdditionalHookInfo> hookInfo = code.newLocal(hookInfoTypeId);
      Local<XposedBridge.CopyOnWriteSortedSet> callbacks = code.newLocal(callbacksTypeId);
      Local<Object[]> snapshot = code.newLocal(objArrayTypeId);
      Local<Integer> snapshotLen = code.newLocal(TypeId.INT);
      Local<Object> callbackObj = code.newLocal(TypeId.OBJECT);
      Local<XC_MethodHook> callback = code.newLocal(callbackTypeId);
      Local<Object> resultObj = code.newLocal(TypeId.OBJECT);
      Local<Integer> one = code.newLocal(TypeId.INT);
      Local<Object> nullObj = code.newLocal(TypeId.OBJECT);
      Local<Throwable> throwable = code.newLocal(throwableTypeId);
      Local<XC_MethodHook.MethodHookParam> param = code.newLocal(paramTypeId);
      Local<Member> method = code.newLocal(memberTypeId);
      Local<Object> thisObject = code.newLocal(TypeId.OBJECT);
      Local<Object[]> args = code.newLocal(objArrayTypeId);
      Local<Boolean> returnEarly = code.newLocal(TypeId.BOOLEAN);
      Local<Integer> actualParamSize = code.newLocal(TypeId.INT);
      Local<Integer> argIndex = code.newLocal(TypeId.INT);
      Local<Integer> beforeIdx = code.newLocal(TypeId.INT);
      Local<Object> lastResult = code.newLocal(TypeId.OBJECT);
      Local<Throwable> lastThrowable = code.newLocal(throwableTypeId);
      Local<Boolean> hasThrowable = code.newLocal(TypeId.BOOLEAN);
      Local[] allArgsLocals = this.createParameterLocals(code);
      Map<TypeId, Local> resultLocals = DexMakerUtils.createResultLocals(code);
      code.loadConstant(args, (Object)null);
      code.loadConstant(argIndex, 0);
      code.loadConstant(one, 1);
      code.loadConstant(snapshotLen, 0);
      code.loadConstant(nullObj, (Object)null);
      code.sget(this.mMethodFieldId, method);
      code.invokeStatic(this.mPrintLogMethodId, (Local)null, method);
      FieldId<XposedBridge, Boolean> disableHooksField = xposedBridgeTypeId.getField(TypeId.BOOLEAN, "disableHooks");
      code.sget(disableHooksField, disableHooks);
      code.compareZ(Comparison.NE, noHookReturn, disableHooks);
      code.sget(this.mHookInfoFieldId, hookInfo);
      code.iget(hookInfoTypeId.getField(callbacksTypeId, "callbacks"), callbacks, hookInfo);
      code.invokeVirtual(callbacksTypeId.getMethod(objArrayTypeId, "getSnapshot"), snapshot, callbacks);
      code.arrayLength(snapshotLen, snapshot);
      code.compareZ(Comparison.EQ, noHookReturn, snapshotLen);
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

      int i;
      Local matchObjLocal;
      for(i = offset; i < paramsSize; ++i) {
         matchObjLocal = allArgsLocals[i];
         DexMakerUtils.autoBoxIfNecessary(code, resultObj, matchObjLocal);
         code.loadConstant(argIndex, i - offset);
         code.aput(args, argIndex, resultObj);
      }

      code.newInstance(param, paramTypeId.getConstructor());
      code.iput(paramTypeId.getField(memberTypeId, "method"), param, method);
      code.iput(paramTypeId.getField(TypeId.OBJECT, "thisObject"), param, thisObject);
      code.iput(paramTypeId.getField(objArrayTypeId, "args"), param, args);
      code.loadConstant(beforeIdx, 0);
      code.mark(beginCallBefore);
      code.addCatchClause(throwableTypeId, tryBeforeCatch);
      code.aget(callbackObj, snapshot, beforeIdx);
      code.cast(callback, callbackObj);
      code.invokeVirtual(callBeforeCallbackMethodId, (Local)null, callback, param);
      code.jump(noExceptionBefore);
      code.removeCatchClause(throwableTypeId);
      code.mark(tryBeforeCatch);
      code.moveException(throwable);
      code.invokeStatic(logThrowableMethodId, (Local)null, throwable);
      code.invokeVirtual(setResultMethodId, (Local)null, param, nullObj);
      code.loadConstant(returnEarly, false);
      code.iput(returnEarlyFieldId, param, returnEarly);
      code.jump(incrementAndCheckBefore);
      code.mark(noExceptionBefore);
      code.iget(returnEarlyFieldId, returnEarly, param);
      code.compareZ(Comparison.EQ, incrementAndCheckBefore, returnEarly);
      code.op(BinaryOp.ADD, beforeIdx, beforeIdx, one);
      code.jump(checkAndCallBackup);
      code.mark(incrementAndCheckBefore);
      code.op(BinaryOp.ADD, beforeIdx, beforeIdx, one);
      code.compare(Comparison.LT, beginCallBefore, beforeIdx, snapshotLen);
      code.mark(checkAndCallBackup);
      code.iget(returnEarlyFieldId, returnEarly, param);
      code.compareZ(Comparison.NE, noExceptionOrig, returnEarly);
      code.addCatchClause(throwableTypeId, tryOrigCatch);
      offset = this.mIsStatic ? 0 : 1;

      for(i = offset; i < allArgsLocals.length; ++i) {
         code.loadConstant(argIndex, i - offset);
         code.aget(resultObj, args, argIndex);
         DexMakerUtils.autoUnboxIfNecessary(code, allArgsLocals[i], resultObj, resultLocals, true);
      }

      Local result;
      if (this.mReturnTypeId.equals(TypeId.VOID)) {
         code.invokeStatic(this.mBackupMethodId, (Local)null, allArgsLocals);
         code.invokeVirtual(setResultMethodId, (Local)null, param, nullObj);
      } else {
         result = (Local)resultLocals.get(this.mReturnTypeId);
         code.invokeStatic(this.mBackupMethodId, result, allArgsLocals);
         DexMakerUtils.autoBoxIfNecessary(code, resultObj, result);
         code.invokeVirtual(setResultMethodId, (Local)null, param, resultObj);
      }

      code.jump(noExceptionOrig);
      code.removeCatchClause(throwableTypeId);
      code.mark(tryOrigCatch);
      code.moveException(throwable);
      code.invokeVirtual(setThrowableMethodId, (Local)null, param, throwable);
      code.mark(noExceptionOrig);
      code.op(BinaryOp.SUBTRACT, beforeIdx, beforeIdx, one);
      code.mark(beginCallAfter);
      code.invokeVirtual(getResultMethodId, lastResult, param);
      code.invokeVirtual(getThrowableMethodId, lastThrowable, param);
      code.addCatchClause(throwableTypeId, tryAfterCatch);
      code.aget(callbackObj, snapshot, beforeIdx);
      code.cast(callback, callbackObj);
      code.invokeVirtual(callAfterCallbackMethodId, (Local)null, callback, param);
      code.jump(decrementAndCheckAfter);
      code.removeCatchClause(throwableTypeId);
      code.mark(tryAfterCatch);
      code.moveException(throwable);
      code.invokeStatic(logThrowableMethodId, (Local)null, throwable);
      code.compareZ(Comparison.EQ, noBackupThrowable, lastThrowable);
      code.invokeVirtual(setThrowableMethodId, (Local)null, param, lastThrowable);
      code.jump(decrementAndCheckAfter);
      code.mark(noBackupThrowable);
      code.invokeVirtual(setResultMethodId, (Local)null, param, lastResult);
      code.mark(decrementAndCheckAfter);
      code.op(BinaryOp.SUBTRACT, beforeIdx, beforeIdx, one);
      code.compareZ(Comparison.GE, beginCallAfter, beforeIdx);
      code.invokeVirtual(hasThrowableMethodId, hasThrowable, param);
      code.compareZ(Comparison.NE, throwThrowable, hasThrowable);
      if (this.mReturnTypeId.equals(TypeId.VOID)) {
         code.returnVoid();
      } else {
         code.invokeVirtual(getResultMethodId, resultObj, param);
         TypeId objTypeId = DexMakerUtils.getObjTypeIdIfPrimitive(this.mReturnTypeId);
         matchObjLocal = (Local)resultLocals.get(objTypeId);
         code.cast(matchObjLocal, resultObj);
         Local toReturn = (Local)resultLocals.get(this.mReturnTypeId);
         DexMakerUtils.autoUnboxIfNecessary(code, toReturn, matchObjLocal, resultLocals, true);
         code.returnValue(toReturn);
      }

      code.mark(throwThrowable);
      code.invokeVirtual(getThrowableMethodId, throwable, param);
      code.throwValue(throwable);
      code.mark(noHookReturn);
      if (this.mReturnTypeId.equals(TypeId.VOID)) {
         code.invokeStatic(this.mBackupMethodId, (Local)null, allArgsLocals);
         code.returnVoid();
      } else {
         result = (Local)resultLocals.get(this.mReturnTypeId);
         code.invokeStatic(this.mBackupMethodId, result, allArgsLocals);
         code.returnValue(result);
      }

   }

   private Local[] createParameterLocals(Code code) {
      Local[] paramLocals = new Local[this.mParameterTypeIds.length];

      for(int i = 0; i < this.mParameterTypeIds.length; ++i) {
         paramLocals[i] = code.getParameter(i, this.mParameterTypeIds[i]);
      }

      return paramLocals;
   }

   static {
      setResultMethodId = paramTypeId.getMethod(TypeId.VOID, "setResult", TypeId.OBJECT);
      setThrowableMethodId = paramTypeId.getMethod(TypeId.VOID, "setThrowable", throwableTypeId);
      getResultMethodId = paramTypeId.getMethod(TypeId.OBJECT, "getResult");
      getThrowableMethodId = paramTypeId.getMethod(throwableTypeId, "getThrowable");
      hasThrowableMethodId = paramTypeId.getMethod(TypeId.BOOLEAN, "hasThrowable");
      callAfterCallbackMethodId = callbackTypeId.getMethod(TypeId.VOID, "callAfterHookedMethod", paramTypeId);
      callBeforeCallbackMethodId = callbackTypeId.getMethod(TypeId.VOID, "callBeforeHookedMethod", paramTypeId);
      returnEarlyFieldId = paramTypeId.getField(TypeId.BOOLEAN, "returnEarly");
      xposedBridgeTypeId = TypeId.get(XposedBridge.class);
      logThrowableMethodId = xposedBridgeTypeId.getMethod(TypeId.VOID, "log", throwableTypeId);
   }
}
