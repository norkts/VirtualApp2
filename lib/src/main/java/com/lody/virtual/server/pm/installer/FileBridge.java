package com.lody.virtual.server.pm.installer;

import android.annotation.TargetApi;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.FileUtils;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteOrder;

@TargetApi(21)
public class FileBridge extends Thread {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4YDmgbFgRjDgo9KAhSVg=="));
   private static final int MSG_LENGTH = 8;
   private static final int CMD_WRITE = 1;
   private static final int CMD_FSYNC = 2;
   private static final int CMD_CLOSE = 3;
   private FileDescriptor mTarget;
   private final FileDescriptor mServer = new FileDescriptor();
   private final FileDescriptor mClient = new FileDescriptor();
   private volatile boolean mClosed;

   public FileBridge() {
      try {
         Os.socketpair(OsConstants.AF_UNIX, OsConstants.SOCK_STREAM, 0, this.mServer, this.mClient);
      } catch (ErrnoException var2) {
         throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4+CWoFNCxLEQo1PxcqKGkjQQZrDTwpKS4YIGIgLFo=")));
      }
   }

   public boolean isClosed() {
      return this.mClosed;
   }

   public void forceClose() {
      closeQuietly(this.mTarget);
      closeQuietly(this.mServer);
      closeQuietly(this.mClient);
      this.mClosed = true;
   }

   public void setTargetFile(FileDescriptor target) {
      this.mTarget = target;
   }

   public FileDescriptor getClientSocket() {
      return this.mClient;
   }

   public void run() {
      byte[] temp = new byte[8192];

      try {
         while(read(this.mServer, temp, 0, 8) == 8) {
            int cmd = FileUtils.peekInt(temp, 0, ByteOrder.BIG_ENDIAN);
            int n;
            if (cmd == 1) {
               for(int len = FileUtils.peekInt(temp, 4, ByteOrder.BIG_ENDIAN); len > 0; len -= n) {
                  n = read(this.mServer, temp, 0, Math.min(temp.length, len));
                  if (n == -1) {
                     throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcM2kKICt9Jwo/KF4mWmcLGTF4HjA/IxgEKEsaLAVlHgosIz4AIHgVSFo=")) + len + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgMJ2wFNAM=")));
                  }

                  write(this.mTarget, temp, 0, n);
               }
            } else if (cmd == 2) {
               Os.fsync(this.mTarget);
               write(this.mServer, temp, 0, 8);
            } else if (cmd == 3) {
               Os.fsync(this.mTarget);
               Os.close(this.mTarget);
               this.mClosed = true;
               write(this.mServer, temp, 0, 8);
               break;
            }
         }
      } catch (IOException | ErrnoException var8) {
         Exception e = var8;
         Log.wtf(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4+CWoFNCxLHgovIz0cDmkJTSpsNx4vLj4uVg==")), e);
      } finally {
         this.forceClose();
      }

   }

   public static void closeQuietly(FileDescriptor fd) {
      if (fd != null && fd.valid()) {
         try {
            Os.close(fd);
         } catch (ErrnoException var2) {
            ErrnoException e = var2;
            e.printStackTrace();
         }
      }

   }

   public static int read(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount) throws IOException {
      ArrayUtils.checkOffsetAndCount(bytes.length, byteOffset, byteCount);
      if (byteCount == 0) {
         return 0;
      } else {
         try {
            int readCount = Os.read(fd, bytes, byteOffset, byteCount);
            return readCount == 0 ? -1 : readCount;
         } catch (ErrnoException var5) {
            ErrnoException errnoException = var5;
            if (errnoException.errno == OsConstants.EAGAIN) {
               return 0;
            } else {
               throw new IOException(errnoException);
            }
         }
      }
   }

   public static void write(FileDescriptor fd, byte[] bytes, int byteOffset, int byteCount) throws IOException {
      ArrayUtils.checkOffsetAndCount(bytes.length, byteOffset, byteCount);
      if (byteCount != 0) {
         try {
            while(byteCount > 0) {
               int bytesWritten = Os.write(fd, bytes, byteOffset, byteCount);
               byteCount -= bytesWritten;
               byteOffset += bytesWritten;
            }

         } catch (ErrnoException var5) {
            ErrnoException errnoException = var5;
            throw new IOException(errnoException);
         }
      }
   }
}
