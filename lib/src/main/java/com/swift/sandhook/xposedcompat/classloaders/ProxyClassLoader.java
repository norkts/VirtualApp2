package com.swift.sandhook.xposedcompat.classloaders;

public class ProxyClassLoader extends ClassLoader {
   private final ClassLoader mClassLoader;

   public ProxyClassLoader(ClassLoader parentCL, ClassLoader appCL) {
      super(parentCL);
      this.mClassLoader = appCL;
   }

   protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      Class clazz = null;

      try {
         clazz = this.mClassLoader.loadClass(name);
      } catch (ClassNotFoundException var5) {
      }

      if (clazz == null) {
         clazz = super.loadClass(name, resolve);
         if (clazz == null) {
            throw new ClassNotFoundException();
         }
      }

      return clazz;
   }
}
