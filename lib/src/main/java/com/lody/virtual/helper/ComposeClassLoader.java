package com.lody.virtual.helper;

public class ComposeClassLoader extends ClassLoader {
   private final ClassLoader mAppClassLoader;

   public ComposeClassLoader(ClassLoader parent, ClassLoader appClassLoader) {
      super(parent);
      this.mAppClassLoader = appClassLoader;
   }

   protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      Class clazz = null;

      try {
         clazz = this.mAppClassLoader.loadClass(name);
      } catch (ClassNotFoundException var5) {
      }

      if (clazz == null) {
         clazz = super.loadClass(name, resolve);
      }

      if (clazz == null) {
         throw new ClassNotFoundException();
      } else {
         return clazz;
      }
   }
}
