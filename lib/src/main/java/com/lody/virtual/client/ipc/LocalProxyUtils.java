package com.lody.virtual.client.ipc;

public class LocalProxyUtils {
   public static <T> T genProxy(Class<T> interfaceClass, Object base) {
      return (T)base;
   }
}
