package com.swift.sandhook.wrapper;

import android.text.TextUtils;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;
import com.swift.sandhook.annotation.MethodReflectParams;
import com.swift.sandhook.annotation.Param;
import com.swift.sandhook.annotation.SkipParamCheck;
import com.swift.sandhook.annotation.ThisObject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HookWrapper {
   public static void addHookClass(Class<?>... classes) throws HookErrorException {
      addHookClass((ClassLoader)null, (Class[])classes);
   }

   public static void addHookClass(ClassLoader classLoader, Class<?>... classes) throws HookErrorException {
      Class[] var2 = classes;
      int var3 = classes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Class clazz = var2[var4];
         addHookClass(classLoader, clazz);
      }

   }

   public static void addHookClass(ClassLoader classLoader, Class<?> clazz) throws HookErrorException {
      Class targetHookClass = getTargetHookClass(classLoader, clazz);
      if (targetHookClass == null) {
         throw new HookErrorException("error hook wrapper class :" + clazz.getName());
      } else {
         Map<Member, HookEntity> hookEntityMap = getHookMethods(classLoader, targetHookClass, clazz);

         try {
            fillBackupMethod(classLoader, clazz, hookEntityMap);
         } catch (Throwable var6) {
            Throwable throwable = var6;
            throw new HookErrorException("fillBackupMethod error!", throwable);
         }

         Iterator var7 = hookEntityMap.values().iterator();

         while(var7.hasNext()) {
            HookEntity entity = (HookEntity)var7.next();
            SandHook.hook(entity);
         }

      }
   }

   private static void fillBackupMethod(ClassLoader classLoader, Class<?> clazz, Map<Member, HookEntity> hookEntityMap) {
      Field[] fields = null;

      try {
         fields = clazz.getDeclaredFields();
      } catch (Throwable var13) {
      }

      if (fields != null && fields.length != 0) {
         if (!hookEntityMap.isEmpty()) {
            Field[] var4 = fields;
            int var5 = fields.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Field field = var4[var6];
               if (Modifier.isStatic(field.getModifiers())) {
                  HookMethodBackup hookMethodBackup = (HookMethodBackup)field.getAnnotation(HookMethodBackup.class);
                  if (hookMethodBackup != null) {
                     Iterator var9 = hookEntityMap.values().iterator();

                     while(var9.hasNext()) {
                        HookEntity hookEntity = (HookEntity)var9.next();
                        if (TextUtils.equals(hookEntity.isCtor() ? "<init>" : hookEntity.target.getName(), hookMethodBackup.value()) && samePars(classLoader, field, hookEntity.pars)) {
                           field.setAccessible(true);
                           if (hookEntity.backup == null) {
                              hookEntity.backup = StubMethodsFactory.getStubMethod();
                              hookEntity.hookIsStub = true;
                              hookEntity.resolveDexCache = false;
                           }

                           if (hookEntity.backup != null) {
                              try {
                                 if (field.getType() == Method.class) {
                                    field.set((Object)null, hookEntity.backup);
                                 } else if (field.getType() == HookEntity.class) {
                                    field.set((Object)null, hookEntity);
                                 }
                              } catch (IllegalAccessException var12) {
                                 IllegalAccessException e = var12;
                                 e.printStackTrace();
                              }
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   private static Map<Member, HookEntity> getHookMethods(ClassLoader classLoader, Class targetHookClass, Class<?> hookWrapperClass) throws HookErrorException {
      Map<Member, HookEntity> hookEntityMap = new HashMap();
      Method[] methods = null;

      try {
         methods = hookWrapperClass.getDeclaredMethods();
      } catch (Throwable var17) {
      }

      if (methods != null && methods.length != 0) {
         Method[] var5 = methods;
         int var6 = methods.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Method method = var5[var7];
            HookMethod hookMethodAnno = (HookMethod)method.getAnnotation(HookMethod.class);
            HookMethodBackup hookMethodBackupAnno = (HookMethodBackup)method.getAnnotation(HookMethodBackup.class);
            String methodName;
            Object foundMethod;
            Class[] pars;
            NoSuchMethodException e;
            HookEntity entity;
            if (hookMethodAnno != null) {
               methodName = hookMethodAnno.value();
               pars = parseMethodPars(classLoader, method);

               try {
                  if (methodName.equals("<init>")) {
                     foundMethod = targetHookClass.getConstructor(pars);
                  } else {
                     foundMethod = targetHookClass.getDeclaredMethod(methodName, pars);
                  }
               } catch (NoSuchMethodException var15) {
                  e = var15;
                  throw new HookErrorException("can not find target method: " + methodName, e);
               }

               if (!method.isAnnotationPresent(SkipParamCheck.class)) {
                  checkSignature((Member)foundMethod, method, pars);
               }

               entity = (HookEntity)hookEntityMap.get(foundMethod);
               if (entity == null) {
                  entity = new HookEntity((Member)foundMethod);
                  hookEntityMap.put(foundMethod, entity);
               }

               entity.pars = pars;
               entity.hook = method;
            } else if (hookMethodBackupAnno != null) {
               methodName = hookMethodBackupAnno.value();
               pars = parseMethodPars(classLoader, method);

               try {
                  if (methodName.equals("<init>")) {
                     foundMethod = targetHookClass.getConstructor(pars);
                  } else {
                     foundMethod = targetHookClass.getDeclaredMethod(methodName, pars);
                  }
               } catch (NoSuchMethodException var16) {
                  e = var16;
                  throw new HookErrorException("can not find target method: " + methodName, e);
               }

               if (!method.isAnnotationPresent(SkipParamCheck.class)) {
                  checkSignature((Member)foundMethod, method, pars);
               }

               entity = (HookEntity)hookEntityMap.get(foundMethod);
               if (entity == null) {
                  entity = new HookEntity((Member)foundMethod);
                  hookEntityMap.put(foundMethod, entity);
               }

               entity.pars = pars;
               entity.backup = method;
            }
         }

         return hookEntityMap;
      } else {
         throw new HookErrorException("error hook wrapper class :" + targetHookClass.getName());
      }
   }

   private static Class[] parseMethodPars(ClassLoader classLoader, Method method) throws HookErrorException {
      MethodParams methodParams = (MethodParams)method.getAnnotation(MethodParams.class);
      MethodReflectParams methodReflectParams = (MethodReflectParams)method.getAnnotation(MethodReflectParams.class);
      if (methodParams != null) {
         return methodParams.value();
      } else if (methodReflectParams == null) {
         if (getParsCount(method) > 0) {
            if (getParsCount(method) == 1) {
               return hasThisObject(method) ? parseMethodParsNew(classLoader, method) : null;
            } else {
               return parseMethodParsNew(classLoader, method);
            }
         } else {
            return null;
         }
      } else if (methodReflectParams.value().length == 0) {
         return null;
      } else {
         Class[] pars = new Class[methodReflectParams.value().length];

         for(int i = 0; i < methodReflectParams.value().length; ++i) {
            try {
               pars[i] = classNameToClass(methodReflectParams.value()[i], classLoader);
            } catch (ClassNotFoundException var7) {
               ClassNotFoundException e = var7;
               throw new HookErrorException("hook method pars error: " + method.getName(), e);
            }
         }

         return pars;
      }
   }

   private static Class[] parseMethodPars(ClassLoader classLoader, Field field) throws HookErrorException {
      MethodParams methodParams = (MethodParams)field.getAnnotation(MethodParams.class);
      MethodReflectParams methodReflectParams = (MethodReflectParams)field.getAnnotation(MethodReflectParams.class);
      if (methodParams != null) {
         return methodParams.value();
      } else if (methodReflectParams == null) {
         return null;
      } else if (methodReflectParams.value().length == 0) {
         return null;
      } else {
         Class[] pars = new Class[methodReflectParams.value().length];

         for(int i = 0; i < methodReflectParams.value().length; ++i) {
            try {
               pars[i] = classNameToClass(methodReflectParams.value()[i], classLoader);
            } catch (ClassNotFoundException var7) {
               ClassNotFoundException e = var7;
               throw new HookErrorException("hook method pars error: " + field.getName(), e);
            }
         }

         return pars;
      }
   }

   private static Class[] parseMethodParsNew(ClassLoader classLoader, Method method) throws HookErrorException {
      Class[] hookMethodPars = method.getParameterTypes();
      if (hookMethodPars != null && hookMethodPars.length != 0) {
         Annotation[][] annotations = method.getParameterAnnotations();
         Class[] realPars = null;
         int parIndex = 0;

         for(int i = 0; i < annotations.length; ++i) {
            Class hookPar = hookMethodPars[i];
            Annotation[] methodAnnos = annotations[i];
            if (i == 0) {
               if (isThisObject(methodAnnos)) {
                  realPars = new Class[annotations.length - 1];
                  continue;
               }

               realPars = new Class[annotations.length];
            }

            try {
               realPars[parIndex] = getRealParType(classLoader, hookPar, methodAnnos, method.isAnnotationPresent(SkipParamCheck.class));
            } catch (Exception var10) {
               Exception e = var10;
               throw new HookErrorException("hook method <" + method.getName() + "> parser pars error", e);
            }

            ++parIndex;
         }

         return realPars;
      } else {
         return null;
      }
   }

   private static Class getRealParType(ClassLoader classLoader, Class hookPar, Annotation[] annotations, boolean skipCheck) throws Exception {
      if (annotations != null && annotations.length != 0) {
         Annotation[] var4 = annotations;
         int var5 = annotations.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Annotation annotation = var4[var6];
            if (annotation instanceof Param) {
               Param param = (Param)annotation;
               if (TextUtils.isEmpty(param.value())) {
                  return hookPar;
               }

               Class realPar = classNameToClass(param.value(), classLoader);
               if (!skipCheck && !realPar.equals(hookPar) && !hookPar.isAssignableFrom(realPar)) {
                  throw new ClassCastException("hook method par cast error!");
               }

               return realPar;
            }
         }

         return hookPar;
      } else {
         return hookPar;
      }
   }

   private static boolean hasThisObject(Method method) {
      Annotation[][] annotations = method.getParameterAnnotations();
      if (annotations != null && annotations.length != 0) {
         Annotation[] thisParAnno = annotations[0];
         return isThisObject(thisParAnno);
      } else {
         return false;
      }
   }

   private static boolean isThisObject(Annotation[] annotations) {
      if (annotations != null && annotations.length != 0) {
         Annotation[] var1 = annotations;
         int var2 = annotations.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Annotation annotation = var1[var3];
            if (annotation instanceof ThisObject) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static int getParsCount(Method method) {
      Class[] hookMethodPars = method.getParameterTypes();
      return hookMethodPars == null ? 0 : hookMethodPars.length;
   }

   private static Class classNameToClass(String name, ClassLoader classLoader) throws ClassNotFoundException {
      Class clazz;
      switch (name) {
         case "boolean":
            clazz = Boolean.TYPE;
            break;
         case "byte":
            clazz = Byte.TYPE;
            break;
         case "char":
            clazz = Character.TYPE;
            break;
         case "double":
            clazz = Double.TYPE;
            break;
         case "float":
            clazz = Float.TYPE;
            break;
         case "int":
            clazz = Integer.TYPE;
            break;
         case "long":
            clazz = Long.TYPE;
            break;
         case "short":
            clazz = Short.TYPE;
            break;
         default:
            if (classLoader == null) {
               clazz = Class.forName(name);
            } else {
               clazz = Class.forName(name, true, classLoader);
            }
      }

      return clazz;
   }

   private static boolean samePars(ClassLoader classLoader, Field field, Class[] par) {
      try {
         Class[] parsOnField = parseMethodPars(classLoader, field);
         if (parsOnField == null && field.isAnnotationPresent(SkipParamCheck.class)) {
            return true;
         } else {
            if (par == null) {
               par = new Class[0];
            }

            if (parsOnField == null) {
               parsOnField = new Class[0];
            }

            if (par.length != parsOnField.length) {
               return false;
            } else {
               for(int i = 0; i < par.length; ++i) {
                  if (par[i] != parsOnField[i]) {
                     return false;
                  }
               }

               return true;
            }
         }
      } catch (HookErrorException var5) {
         return false;
      }
   }

   private static Class getTargetHookClass(ClassLoader classLoader, Class<?> hookWrapperClass) {
      HookClass hookClass = (HookClass)hookWrapperClass.getAnnotation(HookClass.class);
      HookReflectClass hookReflectClass = (HookReflectClass)hookWrapperClass.getAnnotation(HookReflectClass.class);
      if (hookClass != null) {
         return hookClass.value();
      } else if (hookReflectClass != null) {
         try {
            return classLoader == null ? Class.forName(hookReflectClass.value()) : Class.forName(hookReflectClass.value(), true, classLoader);
         } catch (ClassNotFoundException var5) {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void checkSignature(Member origin, Method fake, Class[] originPars) throws HookErrorException {
      if (!Modifier.isStatic(fake.getModifiers())) {
         throw new HookErrorException("hook method must static! - " + fake.getName());
      } else {
         if (origin instanceof Constructor) {
            if (!fake.getReturnType().equals(Void.TYPE)) {
               throw new HookErrorException("error return type! - " + fake.getName());
            }
         } else if (origin instanceof Method) {
            Class originRet = ((Method)origin).getReturnType();
            if (originRet != fake.getReturnType() && !originRet.isAssignableFrom(originRet)) {
               throw new HookErrorException("error return type! - " + fake.getName());
            }
         }

         Class[] fakePars = fake.getParameterTypes();
         if (fakePars == null) {
            fakePars = new Class[0];
         }

         if (originPars == null) {
            originPars = new Class[0];
         }

         if (originPars.length != 0 || fakePars.length != 0) {
            int parOffset = 0;
            if (!Modifier.isStatic(origin.getModifiers())) {
               parOffset = 1;
               if (fakePars.length == 0) {
                  throw new HookErrorException("first par must be this! " + fake.getName());
               }

               if (fakePars[0] != origin.getDeclaringClass() && !fakePars[0].isAssignableFrom(origin.getDeclaringClass())) {
                  throw new HookErrorException("first par must be this! " + fake.getName());
               }

               if (fakePars.length != originPars.length + 1) {
                  throw new HookErrorException("hook method pars must match the origin method! " + fake.getName());
               }
            } else if (fakePars.length != originPars.length) {
               throw new HookErrorException("hook method pars must match the origin method! " + fake.getName());
            }

            for(int i = 0; i < originPars.length; ++i) {
               if (fakePars[i + parOffset] != originPars[i] && !fakePars[i + parOffset].isAssignableFrom(originPars[i])) {
                  throw new HookErrorException("hook method pars must match the origin method! " + fake.getName());
               }
            }

         }
      }
   }

   public static class HookEntity {
      public Member target;
      public Method hook;
      public Method backup;
      public boolean hookIsStub = false;
      public boolean resolveDexCache = true;
      public boolean backupIsStub = true;
      public boolean initClass = true;
      public Class[] pars;
      public int hookMode;

      public HookEntity(Member target) {
         this.target = target;
      }

      public HookEntity(Member target, Method hook, Method backup) {
         this.target = target;
         this.hook = hook;
         this.backup = backup;
      }

      public HookEntity(Member target, Method hook, Method backup, boolean resolveDexCache) {
         this.target = target;
         this.hook = hook;
         this.backup = backup;
         this.resolveDexCache = resolveDexCache;
      }

      public boolean isCtor() {
         return this.target instanceof Constructor;
      }

      public Object callOrigin(Object thiz, Object... args) throws Throwable {
         return SandHook.callOriginMethod(this.backupIsStub, this.target, this.backup, thiz, args);
      }
   }
}
