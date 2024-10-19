package com.bytedance.shadowhook;

public final class ShadowHook {
   private static final int ERRNO_OK = 0;
   private static final int ERRNO_PENDING = 1;
   private static final int ERRNO_UNINIT = 2;
   private static final int ERRNO_LOAD_LIBRARY_EXCEPTION = 100;
   private static final int ERRNO_INIT_EXCEPTION = 101;
   private static boolean inited = false;
   private static int initErrno = 2;
   private static long initCostMs = -1L;
   private static final String libName = "shadowhook";
   private static final ILibLoader defaultLibLoader = null;
   private static final int defaultMode;
   private static final boolean defaultDebuggable = false;
   private static final boolean defaultRecordable = false;
   private static final int recordItemAll = 1023;
   private static final int recordItemTimestamp = 1;
   private static final int recordItemCallerLibName = 2;
   private static final int recordItemOp = 4;
   private static final int recordItemLibName = 8;
   private static final int recordItemSymName = 16;
   private static final int recordItemSymAddr = 32;
   private static final int recordItemNewAddr = 64;
   private static final int recordItemBackupLen = 128;
   private static final int recordItemErrno = 256;
   private static final int recordItemStub = 512;

   public static String getVersion() {
      return nativeGetVersion();
   }

   public static int init() {
      return init((Config)null);
   }

   public static synchronized int init(Config config) {
      if (inited) {
         return initErrno;
      } else {
         inited = true;
         long start = System.currentTimeMillis();
         if (config == null) {
            config = (new ConfigBuilder()).build();
         }

         if (!loadLibrary(config)) {
            initErrno = 100;
            initCostMs = System.currentTimeMillis() - start;
            return initErrno;
         } else {
            try {
               initErrno = nativeInit(config.getMode(), config.getDebuggable());
            } catch (Throwable var5) {
               initErrno = 101;
            }

            if (config.getRecordable()) {
               try {
                  nativeSetRecordable(config.getRecordable());
               } catch (Throwable var4) {
                  initErrno = 101;
               }
            }

            initCostMs = System.currentTimeMillis() - start;
            return initErrno;
         }
      }
   }

   public static int getInitErrno() {
      return initErrno;
   }

   public static long getInitCostMs() {
      return initCostMs;
   }

   public static Mode getMode() {
      if (isInitedOk()) {
         return ShadowHook.Mode.SHARED.getValue() == nativeGetMode() ? ShadowHook.Mode.SHARED : ShadowHook.Mode.UNIQUE;
      } else {
         return ShadowHook.Mode.SHARED;
      }
   }

   public static boolean getDebuggable() {
      return isInitedOk() ? nativeGetDebuggable() : false;
   }

   public static void setDebuggable(boolean debuggable) {
      if (isInitedOk()) {
         nativeSetDebuggable(debuggable);
      }

   }

   public static boolean getRecordable() {
      return isInitedOk() ? nativeGetRecordable() : false;
   }

   public static void setRecordable(boolean recordable) {
      if (isInitedOk()) {
         nativeSetRecordable(recordable);
      }

   }

   public static String toErrmsg(int errno) {
      if (errno == 0) {
         return "OK";
      } else if (errno == 1) {
         return "Pending task";
      } else if (errno == 2) {
         return "Not initialized";
      } else if (errno == 100) {
         return "Load libshadowhook.so failed";
      } else if (errno == 101) {
         return "Init exception";
      } else {
         return isInitedOk() ? nativeToErrmsg(errno) : "Unknown";
      }
   }

   public static String getRecords(RecordItem... recordItems) {
      if (isInitedOk()) {
         int itemFlags = 0;
         RecordItem[] var2 = recordItems;
         int var3 = recordItems.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            RecordItem recordItem = var2[var4];
            switch (recordItem) {
               case TIMESTAMP:
                  itemFlags |= 1;
                  break;
               case CALLER_LIB_NAME:
                  itemFlags |= 2;
                  break;
               case OP:
                  itemFlags |= 4;
                  break;
               case LIB_NAME:
                  itemFlags |= 8;
                  break;
               case SYM_NAME:
                  itemFlags |= 16;
                  break;
               case SYM_ADDR:
                  itemFlags |= 32;
                  break;
               case NEW_ADDR:
                  itemFlags |= 64;
                  break;
               case BACKUP_LEN:
                  itemFlags |= 128;
                  break;
               case ERRNO:
                  itemFlags |= 256;
                  break;
               case STUB:
                  itemFlags |= 512;
            }
         }

         if (itemFlags == 0) {
            itemFlags = 1023;
         }

         return nativeGetRecords(itemFlags);
      } else {
         return null;
      }
   }

   public static String getArch() {
      return isInitedOk() ? nativeGetArch() : "unknown";
   }

   private static boolean loadLibrary(Config config) {
      try {
         if (config != null && config.getLibLoader() != null) {
            config.getLibLoader().loadLibrary("shadowhook");
         } else {
            System.loadLibrary("shadowhook");
         }

         return true;
      } catch (Throwable var2) {
         return false;
      }
   }

   private static boolean loadLibrary() {
      return loadLibrary((Config)null);
   }

   private static boolean isInitedOk() {
      if (inited) {
         return initErrno == 0;
      } else if (!loadLibrary()) {
         return false;
      } else {
         try {
            int errno = nativeGetInitErrno();
            if (errno != 2) {
               initErrno = errno;
               inited = true;
            }

            return errno == 0;
         } catch (Throwable var1) {
            return false;
         }
      }
   }

   private static native String nativeGetVersion();

   private static native int nativeInit(int var0, boolean var1);

   private static native int nativeGetInitErrno();

   private static native int nativeGetMode();

   private static native boolean nativeGetDebuggable();

   private static native void nativeSetDebuggable(boolean var0);

   private static native boolean nativeGetRecordable();

   private static native void nativeSetRecordable(boolean var0);

   private static native String nativeToErrmsg(int var0);

   private static native String nativeGetRecords(int var0);

   private static native String nativeGetArch();

   static {
      defaultMode = ShadowHook.Mode.SHARED.getValue();
   }

   public interface ILibLoader {
      void loadLibrary(String var1);
   }

   public static class Config {
      private ILibLoader libLoader;
      private int mode;
      private boolean debuggable;
      private boolean recordable;

      public void setLibLoader(ILibLoader libLoader) {
         this.libLoader = libLoader;
      }

      public ILibLoader getLibLoader() {
         return this.libLoader;
      }

      public void setMode(int mode) {
         this.mode = mode;
      }

      public int getMode() {
         return this.mode;
      }

      public void setDebuggable(boolean debuggable) {
         this.debuggable = debuggable;
      }

      public boolean getDebuggable() {
         return this.debuggable;
      }

      public void setRecordable(boolean recordable) {
         this.recordable = recordable;
      }

      public boolean getRecordable() {
         return this.recordable;
      }
   }

   public static class ConfigBuilder {
      private ILibLoader libLoader;
      private int mode;
      private boolean debuggable;
      private boolean recordable;

      public ConfigBuilder() {
         this.libLoader = ShadowHook.defaultLibLoader;
         this.mode = ShadowHook.defaultMode;
         this.debuggable = false;
         this.recordable = false;
      }

      public ConfigBuilder setLibLoader(ILibLoader libLoader) {
         this.libLoader = libLoader;
         return this;
      }

      public ConfigBuilder setMode(Mode mode) {
         this.mode = mode.getValue();
         return this;
      }

      public ConfigBuilder setDebuggable(boolean debuggable) {
         this.debuggable = debuggable;
         return this;
      }

      public ConfigBuilder setRecordable(boolean recordable) {
         this.recordable = recordable;
         return this;
      }

      public Config build() {
         Config config = new Config();
         config.setLibLoader(this.libLoader);
         config.setMode(this.mode);
         config.setDebuggable(this.debuggable);
         config.setRecordable(this.recordable);
         return config;
      }
   }

   public static enum Mode {
      SHARED(0),
      UNIQUE(1);

      private final int value;

      private Mode(int value) {
         this.value = value;
      }

      int getValue() {
         return this.value;
      }

      // $FF: synthetic method
      private static Mode[] $values() {
         return new Mode[]{SHARED, UNIQUE};
      }
   }

   public static enum RecordItem {
      TIMESTAMP,
      CALLER_LIB_NAME,
      OP,
      LIB_NAME,
      SYM_NAME,
      SYM_ADDR,
      NEW_ADDR,
      BACKUP_LEN,
      ERRNO,
      STUB;

      // $FF: synthetic method
      private static RecordItem[] $values() {
         return new RecordItem[]{TIMESTAMP, CALLER_LIB_NAME, OP, LIB_NAME, SYM_NAME, SYM_ADDR, NEW_ADDR, BACKUP_LEN, ERRNO, STUB};
      }
   }
}
