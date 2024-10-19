package com.android.dx.stock;

import com.android.dx.Code;
import com.android.dx.Comparison;
import com.android.dx.DexMaker;
import com.android.dx.FieldId;
import com.android.dx.Label;
import com.android.dx.Local;
import com.android.dx.MethodId;
import com.android.dx.TypeId;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ProxyBuilder<T> {
   public static final int VERSION = 1;
   private static final String FIELD_NAME_HANDLER = "$__handler";
   private static final String FIELD_NAME_METHODS = "$__methodArray";
   private static final Map<ProxiedClass<?>, Class<?>> generatedProxyClasses = Collections.synchronizedMap(new HashMap());
   private final Class<T> baseClass;
   private ClassLoader parentClassLoader = ProxyBuilder.class.getClassLoader();
   private InvocationHandler handler;
   private File dexCache;
   private Class<?>[] constructorArgTypes = new Class[0];
   private Object[] constructorArgValues = new Object[0];
   private List<Class<?>> interfaces = new ArrayList();
   private Method[] methods;
   private boolean sharedClassLoader;
   private boolean markTrusted;
   private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_BOXED = new HashMap();
   private static final Map<TypeId<?>, MethodId<?, ?>> PRIMITIVE_TYPE_TO_UNBOX_METHOD;
   private static final Map<Class<?>, MethodId<?, ?>> PRIMITIVE_TO_UNBOX_METHOD;

   private ProxyBuilder(Class<T> clazz) {
      this.baseClass = clazz;
   }

   public static <T> ProxyBuilder<T> forClass(Class<T> clazz) {
      return new ProxyBuilder(clazz);
   }

   public ProxyBuilder<T> parentClassLoader(ClassLoader parent) {
      this.parentClassLoader = parent;
      return this;
   }

   public ProxyBuilder<T> handler(InvocationHandler handler) {
      this.handler = handler;
      return this;
   }

   public ProxyBuilder<T> dexCache(File dexCacheParent) {
      this.dexCache = new File(dexCacheParent, "v" + Integer.toString(1));
      this.dexCache.mkdir();
      return this;
   }

   public ProxyBuilder<T> implementing(Class<?>... interfaces) {
      List<Class<?>> list = this.interfaces;
      Class[] var3 = interfaces;
      int var4 = interfaces.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class<?> i = var3[var5];
         if (!i.isInterface()) {
            throw new IllegalArgumentException("Not an interface: " + i.getName());
         }

         if (!list.contains(i)) {
            list.add(i);
         }
      }

      return this;
   }

   public ProxyBuilder<T> constructorArgValues(Object... constructorArgValues) {
      this.constructorArgValues = constructorArgValues;
      return this;
   }

   public ProxyBuilder<T> constructorArgTypes(Class<?>... constructorArgTypes) {
      this.constructorArgTypes = constructorArgTypes;
      return this;
   }

   public ProxyBuilder<T> onlyMethods(Method[] methods) {
      this.methods = methods;
      return this;
   }

   public ProxyBuilder<T> withSharedClassLoader() {
      this.sharedClassLoader = true;
      return this;
   }

   public ProxyBuilder<T> markTrusted() {
      this.markTrusted = true;
      return this;
   }

   public T build() throws IOException {
      check(this.handler != null, "handler == null");
      check(this.constructorArgTypes.length == this.constructorArgValues.length, "constructorArgValues.length != constructorArgTypes.length");
      Class<? extends T> proxyClass = this.buildProxyClass();

      Constructor constructor;
      try {
         constructor = proxyClass.getConstructor(this.constructorArgTypes);
      } catch (NoSuchMethodException var8) {
         throw new IllegalArgumentException("No constructor for " + this.baseClass.getName() + " with parameter types " + Arrays.toString(this.constructorArgTypes));
      }

      T result;
      try {
         result = (T)constructor.newInstance(this.constructorArgValues);
      } catch (InstantiationException var5) {
         InstantiationException e = var5;
         throw new AssertionError(e);
      } catch (IllegalAccessException var6) {
         IllegalAccessException e = var6;
         throw new AssertionError(e);
      } catch (InvocationTargetException var7) {
         InvocationTargetException e = var7;
         throw launderCause(e);
      }

      setInvocationHandler(result, this.handler);
      return (T)result;
   }

   public Class<? extends T> buildProxyClass() throws IOException {
      ClassLoader requestedClassloader;
      if (this.sharedClassLoader) {
         requestedClassloader = this.baseClass.getClassLoader();
      } else {
         requestedClassloader = this.parentClassLoader;
      }

      ProxiedClass<T> cacheKey = new ProxiedClass(this.baseClass, this.interfaces, requestedClassloader, this.sharedClassLoader);
      Class<? extends T> proxyClass = (Class)generatedProxyClasses.get(cacheKey);
      if (proxyClass != null) {
         return proxyClass;
      } else {
         DexMaker dexMaker = new DexMaker();
         String generatedName = getMethodNameForProxyOf(this.baseClass, this.interfaces);
         TypeId<? extends T> generatedType = TypeId.get("L" + generatedName + ";");
         TypeId<T> superType = TypeId.get(this.baseClass);
         generateConstructorsAndFields(dexMaker, generatedType, superType, this.baseClass);
         Method[] methodsToProxy;
         if (this.methods == null) {
            methodsToProxy = this.getMethodsToProxyRecursive();
         } else {
            methodsToProxy = this.methods;
         }

         Arrays.sort(methodsToProxy, new Comparator<Method>() {
            public int compare(Method method1, Method method2) {
               String m1Signature = method1.getDeclaringClass() + method1.getName() + Arrays.toString(method1.getParameterTypes()) + method1.getReturnType();
               String m2Signature = method2.getDeclaringClass() + method2.getName() + Arrays.toString(method2.getParameterTypes()) + method2.getReturnType();
               return m1Signature.compareTo(m2Signature);
            }
         });
         generateCodeForAllMethods(dexMaker, generatedType, methodsToProxy, superType);
         dexMaker.declare(generatedType, generatedName + ".generated", 1, superType, this.getInterfacesAsTypeIds());
         if (this.sharedClassLoader) {
            dexMaker.setSharedClassLoader(requestedClassloader);
         }

         if (this.markTrusted) {
            dexMaker.markAsTrusted();
         }

         ClassLoader classLoader;
         if (this.sharedClassLoader) {
            classLoader = dexMaker.generateAndLoad((ClassLoader)null, this.dexCache);
         } else {
            classLoader = dexMaker.generateAndLoad(this.parentClassLoader, this.dexCache);
         }

         try {
            proxyClass = this.loadClass(classLoader, generatedName);
         } catch (IllegalAccessError var11) {
            IllegalAccessError e = var11;
            throw new UnsupportedOperationException("cannot proxy inaccessible class " + this.baseClass, e);
         } catch (ClassNotFoundException var12) {
            ClassNotFoundException e = var12;
            throw new AssertionError(e);
         }

         setMethodsStaticField(proxyClass, methodsToProxy);
         generatedProxyClasses.put(cacheKey, proxyClass);
         return proxyClass;
      }
   }

   private Class<? extends T> loadClass(ClassLoader classLoader, String generatedName) throws ClassNotFoundException {
      return (Class<? extends T>)classLoader.loadClass(generatedName);
   }

   private static RuntimeException launderCause(InvocationTargetException e) {
      Throwable cause = e.getCause();
      if (cause instanceof Error) {
         throw (Error)cause;
      } else if (cause instanceof RuntimeException) {
         throw (RuntimeException)cause;
      } else {
         throw new UndeclaredThrowableException(cause);
      }
   }

   private static void setMethodsStaticField(Class<?> proxyClass, Method[] methodsToProxy) {
      try {
         Field methodArrayField = proxyClass.getDeclaredField("$__methodArray");
         methodArrayField.setAccessible(true);
         methodArrayField.set((Object)null, methodsToProxy);
      } catch (NoSuchFieldException var3) {
         NoSuchFieldException e = var3;
         throw new AssertionError(e);
      } catch (IllegalAccessException var4) {
         IllegalAccessException e = var4;
         throw new AssertionError(e);
      }
   }

   public static InvocationHandler getInvocationHandler(Object instance) {
      try {
         Field field = instance.getClass().getDeclaredField("$__handler");
         field.setAccessible(true);
         return (InvocationHandler)field.get(instance);
      } catch (NoSuchFieldException var2) {
         NoSuchFieldException e = var2;
         throw new IllegalArgumentException("Not a valid proxy instance", e);
      } catch (IllegalAccessException var3) {
         IllegalAccessException e = var3;
         throw new AssertionError(e);
      }
   }

   public static void setInvocationHandler(Object instance, InvocationHandler handler) {
      try {
         Field handlerField = instance.getClass().getDeclaredField("$__handler");
         handlerField.setAccessible(true);
         handlerField.set(instance, handler);
      } catch (NoSuchFieldException var3) {
         NoSuchFieldException e = var3;
         throw new IllegalArgumentException("Not a valid proxy instance", e);
      } catch (IllegalAccessException var4) {
         IllegalAccessException e = var4;
         throw new AssertionError(e);
      }
   }

   public static boolean isProxyClass(Class<?> c) {
      try {
         c.getDeclaredField("$__handler");
         return true;
      } catch (NoSuchFieldException var2) {
         return false;
      }
   }

   private static void throwAbstractMethodError(Code code, Method method, Local<String> abstractMethodErrorMessage, Local<AbstractMethodError> abstractMethodError) {
      TypeId<AbstractMethodError> abstractMethodErrorClass = TypeId.get(AbstractMethodError.class);
      MethodId<AbstractMethodError, Void> abstractMethodErrorConstructor = abstractMethodErrorClass.getConstructor(TypeId.STRING);
      code.loadConstant(abstractMethodErrorMessage, "'" + method + "' cannot be called");
      code.newInstance(abstractMethodError, abstractMethodErrorConstructor, abstractMethodErrorMessage);
      code.throwValue(abstractMethodError);
   }

   private static <T, G extends T> void generateCodeForAllMethods(DexMaker dexMaker, TypeId<G> generatedType, Method[] methodsToProxy, TypeId<T> superclassType) {
      TypeId<InvocationHandler> handlerType = TypeId.get(InvocationHandler.class);
      TypeId<Method[]> methodArrayType = TypeId.get(Method[].class);
      FieldId<G, InvocationHandler> handlerField = generatedType.getField(handlerType, "$__handler");
      FieldId<G, Method[]> allMethods = generatedType.getField(methodArrayType, "$__methodArray");
      TypeId<Method> methodType = TypeId.get(Method.class);
      TypeId<Object[]> objectArrayType = TypeId.get(Object[].class);
      MethodId<InvocationHandler, Object> methodInvoke = handlerType.getMethod(TypeId.OBJECT, "invoke", TypeId.OBJECT, methodType, objectArrayType);

      for(int m = 0; m < methodsToProxy.length; ++m) {
         Method method = methodsToProxy[m];
         String name = method.getName();
         Class<?>[] argClasses = method.getParameterTypes();
         TypeId<?>[] argTypes = new TypeId[argClasses.length];

         for(int i = 0; i < argTypes.length; ++i) {
            argTypes[i] = TypeId.get(argClasses[i]);
         }

         Class<?> returnType = method.getReturnType();
         TypeId<?> resultType = TypeId.get(returnType);
         MethodId<?, ?> methodId = generatedType.getMethod(resultType, name, argTypes);
         TypeId<AbstractMethodError> abstractMethodErrorClass = TypeId.get(AbstractMethodError.class);
         Code code = dexMaker.declare(methodId, 1);
         Local<G> localThis = code.getThis(generatedType);
         Local<InvocationHandler> localHandler = code.newLocal(handlerType);
         Local<Object> invokeResult = code.newLocal(TypeId.OBJECT);
         Local<Integer> intValue = code.newLocal(TypeId.INT);
         Local<Object[]> args = code.newLocal(objectArrayType);
         Local<Integer> argsLength = code.newLocal(TypeId.INT);
         Local<Object> temp = code.newLocal(TypeId.OBJECT);
         Local<?> resultHolder = code.newLocal(resultType);
         Local<Method[]> methodArray = code.newLocal(methodArrayType);
         Local<Method> thisMethod = code.newLocal(methodType);
         Local<Integer> methodIndex = code.newLocal(TypeId.INT);
         Class<?> aBoxedClass = (Class)PRIMITIVE_TO_BOXED.get(returnType);
         Local<?> aBoxedResult = null;
         if (aBoxedClass != null) {
            aBoxedResult = code.newLocal(TypeId.get(aBoxedClass));
         }

         Local<InvocationHandler> nullHandler = code.newLocal(handlerType);
         Local<?>[] superArgs2 = null;
         Local<?> superResult2 = null;
         MethodId<T, ?> superMethod = null;
         Local<String> abstractMethodErrorMessage = null;
         Local<AbstractMethodError> abstractMethodError = null;
         if ((method.getModifiers() & 1024) == 0) {
            superArgs2 = new Local[argClasses.length];
            superResult2 = code.newLocal(resultType);
            superMethod = superclassType.getMethod(resultType, name, argTypes);
         } else {
            abstractMethodErrorMessage = code.newLocal(TypeId.STRING);
            abstractMethodError = code.newLocal(abstractMethodErrorClass);
         }

         code.loadConstant(methodIndex, m);
         code.sget(allMethods, methodArray);
         code.aget(thisMethod, methodArray, methodIndex);
         code.loadConstant(argsLength, argTypes.length);
         code.newArray(args, argsLength);
         code.iget(handlerField, localHandler, localThis);
         code.loadConstant(nullHandler,null);
         Label handlerNullCase = new Label();
         code.compare(Comparison.EQ, handlerNullCase, nullHandler, localHandler);

         int i;
         Local superThis;
         for(i = 0; i < argTypes.length; ++i) {
            code.loadConstant(intValue, i);
            Local<?> parameter = code.getParameter(i, argTypes[i]);
            superThis = boxIfRequired(code, parameter, temp);
            code.aput(args, intValue, superThis);
         }

         code.invokeInterface(methodInvoke, invokeResult, localHandler, localThis, thisMethod, args);
         generateCodeForReturnStatement(code, returnType, invokeResult, resultHolder, aBoxedResult);
         code.mark(handlerNullCase);
         if ((method.getModifiers() & 1024) != 0) {
            throwAbstractMethodError(code, method, abstractMethodErrorMessage, abstractMethodError);
         } else {
            for(i = 0; i < superArgs2.length; ++i) {
               superArgs2[i] = code.getParameter(i, argTypes[i]);
            }

            if (Void.TYPE.equals(returnType)) {
               code.invokeSuper(superMethod, (Local)null, localThis, superArgs2);
               code.returnVoid();
            } else {
               invokeSuper(superMethod, code, localThis, superArgs2, superResult2);
               code.returnValue(superResult2);
            }
         }

         MethodId<G, ?> callsSuperMethod = generatedType.getMethod(resultType, superMethodName(method), argTypes);
         Code superCode = dexMaker.declare(callsSuperMethod, 1);
         if ((method.getModifiers() & 1024) != 0) {
            superThis = superCode.newLocal(TypeId.STRING);
            Local<AbstractMethodError> superAbstractMethodError = superCode.newLocal(abstractMethodErrorClass);
            throwAbstractMethodError(superCode, method, superThis, superAbstractMethodError);
         } else {
            superThis = superCode.getThis(generatedType);
            Local<?>[] superArgs = new Local[argClasses.length];

            for(i = 0; i < superArgs.length; ++i) {
               superArgs[i] = superCode.getParameter(i, argTypes[i]);
            }

            if (Void.TYPE.equals(returnType)) {
               superCode.invokeSuper(superMethod, (Local)null, superThis, superArgs);
               superCode.returnVoid();
            } else {
               Local<?> superResult = superCode.newLocal(resultType);
               invokeSuper(superMethod, superCode, superThis, superArgs, superResult);
               superCode.returnValue(superResult);
            }
         }
      }

   }

   private static void invokeSuper(MethodId superMethod, Code superCode, Local superThis, Local[] superArgs, Local superResult) {
      superCode.invokeSuper(superMethod, superResult, superThis, superArgs);
   }

   private static Local<?> boxIfRequired(Code code, Local<?> parameter, Local<Object> temp) {
      MethodId<?, ?> unboxMethod = (MethodId)PRIMITIVE_TYPE_TO_UNBOX_METHOD.get(parameter.getType());
      if (unboxMethod == null) {
         return parameter;
      } else {
         code.invokeStatic(unboxMethod, temp, parameter);
         return temp;
      }
   }

   public static Object callSuper(Object proxy, Method method, Object... args) throws Throwable {
      try {
         return proxy.getClass().getMethod(superMethodName(method), method.getParameterTypes()).invoke(proxy, args);
      } catch (InvocationTargetException var4) {
         InvocationTargetException e = var4;
         throw e.getCause();
      }
   }

   private static String superMethodName(Method method) {
      String returnType = method.getReturnType().getName();
      return "super$" + method.getName() + "$" + returnType.replace('.', '_').replace('[', '_').replace(';', '_');
   }

   private static void check(boolean condition, String message) {
      if (!condition) {
         throw new IllegalArgumentException(message);
      }
   }

   private static <T, G extends T> void generateConstructorsAndFields(DexMaker dexMaker, TypeId<G> generatedType, TypeId<T> superType, Class<T> superClass) {
      TypeId<InvocationHandler> handlerType = TypeId.get(InvocationHandler.class);
      TypeId<Method[]> methodArrayType = TypeId.get(Method[].class);
      FieldId<G, InvocationHandler> handlerField = generatedType.getField(handlerType, "$__handler");
      dexMaker.declare(handlerField, 2, (Object)null);
      FieldId<G, Method[]> allMethods = generatedType.getField(methodArrayType, "$__methodArray");
      dexMaker.declare(allMethods, 10, (Object)null);
      Constructor[] var8 = getConstructorsToOverwrite(superClass);
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         Constructor<T> constructor = var8[var10];
         if (constructor.getModifiers() != 16) {
            TypeId<?>[] types = classArrayToTypeArray(constructor.getParameterTypes());
            MethodId<?, ?> method = generatedType.getConstructor(types);
            Code constructorCode = dexMaker.declare(method, 1);
            Local<G> thisRef = constructorCode.getThis(generatedType);
            Local<?>[] params = new Local[types.length];

            for(int i = 0; i < params.length; ++i) {
               params[i] = constructorCode.getParameter(i, types[i]);
            }

            MethodId<T, ?> superConstructor = superType.getConstructor(types);
            constructorCode.invokeDirect(superConstructor, (Local)null, thisRef, params);
            constructorCode.returnVoid();
         }
      }

   }

   private static <T> Constructor<T>[] getConstructorsToOverwrite(Class<T> clazz) {
      return (Constructor<T>[])clazz.getDeclaredConstructors();
   }

   private TypeId<?>[] getInterfacesAsTypeIds() {
      TypeId<?>[] result = new TypeId[this.interfaces.size()];
      int i = 0;

      Class implemented;
      for(Iterator var3 = this.interfaces.iterator(); var3.hasNext(); result[i++] = TypeId.get(implemented)) {
         implemented = (Class)var3.next();
      }

      return result;
   }

   private Method[] getMethodsToProxyRecursive() {
      Set<MethodSetEntry> methodsToProxy = new HashSet();
      Set<MethodSetEntry> seenFinalMethods = new HashSet();

      Class c;
      for(c = this.baseClass; c != null; c = c.getSuperclass()) {
         this.getMethodsToProxy(methodsToProxy, seenFinalMethods, c);
      }

      for(c = this.baseClass; c != null; c = c.getSuperclass()) {
         Class[] var4 = c.getInterfaces();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Class<?> i = var4[var6];
            this.getMethodsToProxy(methodsToProxy, seenFinalMethods, i);
         }
      }

      Iterator var8 = this.interfaces.iterator();

      while(var8.hasNext()) {
         c = (Class)var8.next();
         this.getMethodsToProxy(methodsToProxy, seenFinalMethods, c);
      }

      Method[] results = new Method[methodsToProxy.size()];
      int i = 0;

      MethodSetEntry entry;
      for(Iterator var12 = methodsToProxy.iterator(); var12.hasNext(); results[i++] = entry.originalMethod) {
         entry = (MethodSetEntry)var12.next();
      }

      return results;
   }

   private void getMethodsToProxy(Set<MethodSetEntry> sink, Set<MethodSetEntry> seenFinalMethods, Class<?> c) {
      Method[] var4 = c.getDeclaredMethods();
      int var5 = var4.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         Method method = var4[var6];
         MethodSetEntry entry;
         if ((method.getModifiers() & 16) != 0) {
            entry = new MethodSetEntry(method);
            seenFinalMethods.add(entry);
            sink.remove(entry);
         } else if ((method.getModifiers() & 8) == 0 && (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers()) || this.sharedClassLoader && !Modifier.isPrivate(method.getModifiers())) && (!method.getName().equals("finalize") || method.getParameterTypes().length != 0)) {
            entry = new MethodSetEntry(method);
            if (!seenFinalMethods.contains(entry)) {
               sink.add(entry);
            }
         }
      }

      if (c.isInterface()) {
         Class[] var9 = c.getInterfaces();
         var5 = var9.length;

         for(var6 = 0; var6 < var5; ++var6) {
            Class<?> i = var9[var6];
            this.getMethodsToProxy(sink, seenFinalMethods, i);
         }
      }

   }

   private static <T> String getMethodNameForProxyOf(Class<T> clazz, List<Class<?>> interfaces) {
      String interfacesHash = Integer.toHexString(interfaces.hashCode());
      return clazz.getName().replace(".", "/") + "_" + interfacesHash + "_Proxy";
   }

   private static TypeId<?>[] classArrayToTypeArray(Class<?>[] input) {
      TypeId<?>[] result = new TypeId[input.length];

      for(int i = 0; i < input.length; ++i) {
         result[i] = TypeId.get(input[i]);
      }

      return result;
   }

   private static void generateCodeForReturnStatement(Code code, Class methodReturnType, Local localForResultOfInvoke, Local localOfMethodReturnType, Local aBoxedResult) {
      if (PRIMITIVE_TO_UNBOX_METHOD.containsKey(methodReturnType)) {
         code.cast(aBoxedResult, localForResultOfInvoke);
         MethodId unboxingMethodFor = getUnboxMethodForPrimitive(methodReturnType);
         code.invokeVirtual(unboxingMethodFor, localOfMethodReturnType, aBoxedResult);
         code.returnValue(localOfMethodReturnType);
      } else if (Void.TYPE.equals(methodReturnType)) {
         code.returnVoid();
      } else {
         code.cast(localOfMethodReturnType, localForResultOfInvoke);
         code.returnValue(localOfMethodReturnType);
      }

   }

   private static MethodId<?, ?> getUnboxMethodForPrimitive(Class<?> methodReturnType) {
      return (MethodId)PRIMITIVE_TO_UNBOX_METHOD.get(methodReturnType);
   }

   static {
      PRIMITIVE_TO_BOXED.put(Boolean.TYPE, Boolean.class);
      PRIMITIVE_TO_BOXED.put(Integer.TYPE, Integer.class);
      PRIMITIVE_TO_BOXED.put(Byte.TYPE, Byte.class);
      PRIMITIVE_TO_BOXED.put(Long.TYPE, Long.class);
      PRIMITIVE_TO_BOXED.put(Short.TYPE, Short.class);
      PRIMITIVE_TO_BOXED.put(Float.TYPE, Float.class);
      PRIMITIVE_TO_BOXED.put(Double.TYPE, Double.class);
      PRIMITIVE_TO_BOXED.put(Character.TYPE, Character.class);
      PRIMITIVE_TYPE_TO_UNBOX_METHOD = new HashMap();
      Iterator var0 = PRIMITIVE_TO_BOXED.entrySet().iterator();

      while(var0.hasNext()) {
         Map.Entry<Class<?>, Class<?>> entry = (Map.Entry)var0.next();
         TypeId<?> primitiveType = TypeId.get((Class)entry.getKey());
         TypeId<?> boxedType = TypeId.get((Class)entry.getValue());
         MethodId<?, ?> valueOfMethod = boxedType.getMethod(boxedType, "valueOf", primitiveType);
         PRIMITIVE_TYPE_TO_UNBOX_METHOD.put(primitiveType, valueOfMethod);
      }

      Map<Class<?>, MethodId<?, ?>> map = new HashMap();
      map.put(Boolean.TYPE, TypeId.get(Boolean.class).getMethod(TypeId.BOOLEAN, "booleanValue"));
      map.put(Integer.TYPE, TypeId.get(Integer.class).getMethod(TypeId.INT, "intValue"));
      map.put(Byte.TYPE, TypeId.get(Byte.class).getMethod(TypeId.BYTE, "byteValue"));
      map.put(Long.TYPE, TypeId.get(Long.class).getMethod(TypeId.LONG, "longValue"));
      map.put(Short.TYPE, TypeId.get(Short.class).getMethod(TypeId.SHORT, "shortValue"));
      map.put(Float.TYPE, TypeId.get(Float.class).getMethod(TypeId.FLOAT, "floatValue"));
      map.put(Double.TYPE, TypeId.get(Double.class).getMethod(TypeId.DOUBLE, "doubleValue"));
      map.put(Character.TYPE, TypeId.get(Character.class).getMethod(TypeId.CHAR, "charValue"));
      PRIMITIVE_TO_UNBOX_METHOD = map;
   }

   private static class ProxiedClass<U> {
      final Class<U> clazz;
      final List<Class<?>> interfaces;
      final ClassLoader requestedClassloader;
      final boolean sharedClassLoader;

      public boolean equals(Object other) {
         if (this == other) {
            return true;
         } else if (other != null && this.getClass() == other.getClass()) {
            ProxiedClass<?> that = (ProxiedClass)other;
            return this.clazz == that.clazz && this.interfaces.equals(that.interfaces) && this.requestedClassloader == that.requestedClassloader && this.sharedClassLoader == that.sharedClassLoader;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.clazz.hashCode() + this.interfaces.hashCode() + this.requestedClassloader.hashCode() + (this.sharedClassLoader ? 1 : 0);
      }

      private ProxiedClass(Class<U> clazz, List<Class<?>> interfaces, ClassLoader requestedClassloader, boolean sharedClassLoader) {
         this.clazz = clazz;
         this.interfaces = new ArrayList(interfaces);
         this.requestedClassloader = requestedClassloader;
         this.sharedClassLoader = sharedClassLoader;
      }

      // $FF: synthetic method
      ProxiedClass(Class x0, List x1, ClassLoader x2, boolean x3, Object x4) {
         this(x0, x1, x2, x3);
      }
   }

   public static class MethodSetEntry {
      public final String name;
      public final Class<?>[] paramTypes;
      public final Class<?> returnType;
      public final Method originalMethod;

      public MethodSetEntry(Method method) {
         this.originalMethod = method;
         this.name = method.getName();
         this.paramTypes = method.getParameterTypes();
         this.returnType = method.getReturnType();
      }

      public boolean equals(Object o) {
         if (!(o instanceof MethodSetEntry)) {
            return false;
         } else {
            MethodSetEntry other = (MethodSetEntry)o;
            return this.name.equals(other.name) && this.returnType.equals(other.returnType) && Arrays.equals(this.paramTypes, other.paramTypes);
         }
      }

      public int hashCode() {
         int result = 17;
         result += 31 * result + this.name.hashCode();
         result += 31 * result + this.returnType.hashCode();
         result += 31 * result + Arrays.hashCode(this.paramTypes);
         return result;
      }
   }
}
