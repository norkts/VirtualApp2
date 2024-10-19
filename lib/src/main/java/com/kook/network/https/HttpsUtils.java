package com.kook.network.https;

import com.kook.network.StringFog;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpsUtils {
   public static SSLSocketFactory getSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
      try {
         TrustManager[] trustManagers = prepareTrustManager(certificates);
         KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
         SSLContext sslContext = SSLContext.getInstance(StringFog.decrypt("ODwj"));
         sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))}, new SecureRandom());
         return sslContext.getSocketFactory();
      } catch (NoSuchAlgorithmException var6) {
         NoSuchAlgorithmException e = var6;
         throw new AssertionError(e);
      } catch (KeyManagementException var7) {
         KeyManagementException e = var7;
         throw new AssertionError(e);
      } catch (KeyStoreException var8) {
         KeyStoreException e = var8;
         throw new AssertionError(e);
      }
   }

   private static TrustManager[] prepareTrustManager(InputStream... certificates) {
      if (certificates != null && certificates.length > 0) {
         try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(StringFog.decrypt("M0FaWxQ="));
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load((KeyStore.LoadStoreParameter)null);
            int index = 0;
            InputStream[] trustManagerFactory = certificates;
            int var5 = certificates.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               InputStream certificate = trustManagerFactory[var6];
               String certificateAlias = Integer.toString(index++);
               keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

               try {
                  if (certificate != null) {
                     certificate.close();
                  }
               } catch (IOException var10) {
               }
            }

            trustManagerFactory = null;
            TrustManagerFactory trustManagerFactory2 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory2.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory2.getTrustManagers();
            return trustManagers;
         } catch (NoSuchAlgorithmException var11) {
            var11.printStackTrace();
         } catch (CertificateException var12) {
            var12.printStackTrace();
         } catch (KeyStoreException var13) {
            var13.printStackTrace();
         } catch (Exception var14) {
            Exception e = var14;
            e.printStackTrace();
         }

         return null;
      } else {
         return null;
      }
   }

   private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
      try {
         if (bksFile != null && password != null) {
            KeyStore clientKeyStore = KeyStore.getInstance(StringFog.decrypt("KSQ8"));
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
         }

         return null;
      } catch (KeyStoreException var4) {
         KeyStoreException e = var4;
         e.printStackTrace();
      } catch (NoSuchAlgorithmException var5) {
         NoSuchAlgorithmException e = var5;
         e.printStackTrace();
      } catch (UnrecoverableKeyException var6) {
         UnrecoverableKeyException e = var6;
         e.printStackTrace();
      } catch (CertificateException var7) {
         CertificateException e = var7;
         e.printStackTrace();
      } catch (IOException var8) {
         IOException e = var8;
         e.printStackTrace();
      } catch (Exception var9) {
         Exception e = var9;
         e.printStackTrace();
      }

      return null;
   }

   private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
      TrustManager[] var1 = trustManagers;
      int var2 = trustManagers.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         TrustManager trustManager = var1[var3];
         if (trustManager instanceof X509TrustManager) {
            return (X509TrustManager)trustManager;
         }
      }

      return null;
   }

   public static SSLSocketFactory initSSLSocketFactory() {
      SSLContext sslContext = null;

      try {
         sslContext = SSLContext.getInstance(StringFog.decrypt("ODwj"));
         X509TrustManager[] xTrustArray = new X509TrustManager[]{initTrustManager()};
         sslContext.init((KeyManager[])null, xTrustArray, new SecureRandom());
      } catch (Exception var2) {
         Exception e = var2;
         e.printStackTrace();
      }

      return sslContext.getSocketFactory();
   }

   public static X509TrustManager initTrustManager() {
      X509TrustManager mTrustManager = new X509TrustManager() {
         public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
         }

         public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
         }

         public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
         }
      };
      return mTrustManager;
   }

   private static class MyTrustManager implements X509TrustManager {
      private X509TrustManager defaultTrustManager;
      private X509TrustManager localTrustManager;

      public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
         TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
         var4.init((KeyStore)null);
         this.defaultTrustManager = HttpsUtils.chooseTrustManager(var4.getTrustManagers());
         this.localTrustManager = localTrustManager;
      }

      public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      }

      public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
         try {
            this.defaultTrustManager.checkServerTrusted(chain, authType);
         } catch (CertificateException var4) {
            this.localTrustManager.checkServerTrusted(chain, authType);
         }

      }

      public X509Certificate[] getAcceptedIssuers() {
         return new X509Certificate[0];
      }
   }
}
