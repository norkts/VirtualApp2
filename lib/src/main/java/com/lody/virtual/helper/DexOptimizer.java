package com.lody.virtual.helper;

import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.utils.FileUtils;
import dalvik.system.DexFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DexOptimizer {
   public static void dex2oat(String dexFilePath, String oatFilePath) throws IOException {
      if (VERSION.SDK_INT < 30) {
         File oatFile = new File(oatFilePath);
         FileUtils.ensureDirCreate(oatFile.getParentFile());
         List<String> commandAndParams = new ArrayList();
         commandAndParams.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguIH8jGjdmEVRF")));
         commandAndParams.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwQIPGgaRCNiNAYoKARXVg==")) + dexFilePath);
         commandAndParams.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwQID2saMyNiNAYoKARXVg==")) + oatFilePath);
         commandAndParams.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwQICWogLAZhNzA5LBccDW9SPANrDi8o")) + VirtualRuntime.getCurrentInstructionSet());
         if (VERSION.SDK_INT > 25) {
            commandAndParams.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwQIOWozEgJjDlE/IzlXPGwjOAZrDg0oKRcuI30gEiBsN1RF")));
         } else {
            commandAndParams.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwQIOWozEgJjDlE/IzlXPGwjOAZrDg0oIxgcCmIFMDNlNAo9OS4uKmUaAlo=")));
         }

         ProcessBuilder pb = new ProcessBuilder(commandAndParams);
         pb.redirectErrorStream(true);
         Process dex2oatProcess = pb.start();
         DexOptimizer.StreamConsumer.consumeInputStream(dex2oatProcess.getInputStream());
         DexOptimizer.StreamConsumer.consumeInputStream(dex2oatProcess.getErrorStream());

         try {
            int ret = dex2oatProcess.waitFor();
            if (ret != 0) {
               throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguIH8jGjdmVyQtKi4uCWoJTQVlNDAwLT42J2EjNCFqDl0bLiohJGsKRQVsDTw2Ji0MLHs0PFo=")) + ret);
            }
         } catch (InterruptedException var7) {
            InterruptedException e = var7;
            throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguIH8jGjdmVyQzIykmMW8aBitsNAowKQcqJ2JTTCNsASwgPl9XVg==")) + e.getMessage(), e);
         }
      } else {
         DexFile.loadDex(dexFilePath, oatFilePath, 0).close();
      }

   }

   private static class StreamConsumer {
      static final Executor STREAM_CONSUMER = Executors.newSingleThreadExecutor();

      static void consumeInputStream(final InputStream is) {
         STREAM_CONSUMER.execute(new Runnable() {
            public void run() {
               if (is != null) {
                  byte[] buffer = new byte[256];

                  try {
                     while(true) {
                        if (is.read(buffer) > 0) {
                           continue;
                        }
                     }
                  } catch (IOException var11) {
                  } finally {
                     try {
                        is.close();
                     } catch (Exception var10) {
                     }

                  }

               }
            }
         });
      }
   }
}
