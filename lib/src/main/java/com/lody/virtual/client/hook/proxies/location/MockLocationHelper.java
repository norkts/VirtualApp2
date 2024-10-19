package com.lody.virtual.client.hook.proxies.location;

import android.location.GpsStatus;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualGPSSatalines;
import com.lody.virtual.client.ipc.VLocationManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.remote.vloc.VLocation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import mirror.android.location.GpsStatusL;
import mirror.android.location.LocationManager;

public class MockLocationHelper {
   public static void invokeNmeaReceived(Object listener) {
      if (listener != null) {
         VirtualGPSSatalines satalines = VirtualGPSSatalines.get();

         try {
            VLocation location = VLocationManager.get().getCurAppLocation();
            if (location != null) {
               String date = (new SimpleDateFormat(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBZfDWoaLAN3NSgP")), Locale.US)).format(new Date());
               String lat = getGPSLat(location.getLatitude());
               String lon = getGPSLat(location.getLongitude());
               String latNW = getNorthWest(location);
               String lonSE = getSouthEast(location);
               String $GPGGA = checksum(String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PRYmAmAxPBFOVzApOl4ML383GgN1Vig6PAQuD05SPzd+AS8bM141CHVSASR/DQISCF5eP3VTQV5/Myc7")), date, lat, latNW, lon, lonSE, satalines.getSvCount()));
               String $GPRMC = checksum(String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PRYmAmchEhNOVzApOhUhCHkgDSR7DjMdPhc1KEgFNzd1V1w5OTleMnxTPyN8MwU7CBUlOw==")), date, lat, latNW, lon, lonSE));
               if (LocationManager.GnssStatusListenerTransport.onNmeaReceived != null) {
                  LocationManager.GnssStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PRYmAmA2LFNOViMoMwNaKnU3OwF8MwU7Ol8DDUwJBTd1IzsbPCkfKH9TJwJ/ClgiCF8lPHxSOy1/MBEvM14LLn8nGQNPV1ApMwNaLXVSOwF/VycdOV4XKEk3JzY=")));
                  LocationManager.GnssStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), $GPGGA);
                  LocationManager.GnssStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PRYmAmQmMBdOVicoJF5aKn8xPyR8VgJAPAM5KGskTQ58VwE7My5SVg==")));
                  LocationManager.GnssStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), $GPRMC);
                  LocationManager.GnssStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PRYmAmA2LBFOHCMoMzlaKXpSOwF/DQU8P18DD08OTDd8V1wbOTohKHVTOyR8DQUiCQQpO3sJBSB/Mz8bOS5SVg==")));
               } else if (LocationManager.GpsStatusListenerTransport.onNmeaReceived != null) {
                  LocationManager.GpsStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PRYmAmA2LFNOViMoMwNaKnU3OwF8MwU7Ol8DDUwJBTd1IzsbPCkfKH9TJwJ/ClgiCF8lPHxSOy1/MBEvM14LLn8nGQNPV1ApMwNaLXVSOwF/VycdOV4XKEk3JzY=")));
                  LocationManager.GpsStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), $GPGGA);
                  LocationManager.GpsStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PRYmAmQmMBdOVicoJF5aKn8xPyR8VgJAPAM5KGskTQ58VwE7My5SVg==")));
                  LocationManager.GpsStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), $GPRMC);
                  LocationManager.GpsStatusListenerTransport.onNmeaReceived.call(listener, System.currentTimeMillis(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PRYmAmA2LBFOHCMoMzlaKXpSOwF/DQU8P18DD08OTDd8V1wbOTohKHVTOyR8DQUiCQQpO3sJBSB/Mz8bOS5SVg==")));
               }
            }
         } catch (Throwable var10) {
            Throwable e = var10;
            e.printStackTrace();
         }
      }

   }

   public static void fakeGpsStatusN(android.location.LocationManager locationManager) {
      if (LocationManager.mGpsStatusListeners != null) {
         Map mGpsStatusListeners = (Map)LocationManager.mGpsStatusListeners.get(locationManager);
         Iterator var2 = mGpsStatusListeners.values().iterator();
         if (var2.hasNext()) {
            Object listenerTransport = var2.next();
            invokeSvStatusChanged(listenerTransport);
         }

      }
   }

   public static void fakeGpsStatus(android.location.LocationManager locationManager) {
      if (VERSION.SDK_INT >= 24) {
         fakeGpsStatusN(locationManager);
      } else {
         GpsStatus mGpsStatus = null;

         try {
            mGpsStatus = (GpsStatus)Reflect.on((Object)locationManager).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwYmKG82LAZ9AQovIy5SVg==")));
         } catch (Throwable var13) {
         }

         if (mGpsStatus != null) {
            VirtualGPSSatalines satalines = VirtualGPSSatalines.get();
            int svCount = satalines.getSvCount();
            float[] snrs = satalines.getSnrs();
            int[] prns = satalines.getPrns();
            float[] elevations = satalines.getElevations();
            float[] azimuths = satalines.getAzimuths();

            try {
               int length;
               if (GpsStatusL.setStatus != null) {
                  svCount = satalines.getSvCount();
                  length = satalines.getPrns().length;
                  elevations = satalines.getElevations();
                  azimuths = satalines.getAzimuths();
                  int[] ephemerisMask = new int[length];

                  for(int i = 0; i < length; ++i) {
                     ephemerisMask[i] = satalines.getEphemerisMask();
                  }

                  int[] almanacMask = new int[length];

                  for(int i = 0; i < length; ++i) {
                     almanacMask[i] = satalines.getAlmanacMask();
                  }

                  int[] usedInFixMask = new int[length];

                  for(int i = 0; i < length; ++i) {
                     usedInFixMask[i] = satalines.getUsedInFixMask();
                  }

                  GpsStatusL.setStatus.call(mGpsStatus, svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);
               } else if (mirror.android.location.GpsStatus.setStatus != null) {
                  length = satalines.getEphemerisMask();
                  int almanacMask = satalines.getAlmanacMask();
                  int i = satalines.getUsedInFixMask();
                  mirror.android.location.GpsStatus.setStatus.call(mGpsStatus, svCount, prns, snrs, elevations, azimuths, length, almanacMask, i);
               }
            } catch (Exception var14) {
            }

         }
      }
   }

   public static void invokeSvStatusChanged(Object transport) {
      if (transport != null) {
         VirtualGPSSatalines satalines = VirtualGPSSatalines.get();

         try {
            Class<?> aClass = transport.getClass();
            int svCount;
            float[] snrs;
            float[] elevations;
            float[] azimuths;
            int[] prns;
            if (aClass == LocationManager.GnssStatusListenerTransport.TYPE) {
               svCount = satalines.getSvCount();
               prns = satalines.getPrnWithFlags();
               snrs = satalines.getSnrs();
               elevations = satalines.getElevations();
               azimuths = satalines.getAzimuths();
               if (BuildCompat.isOreo()) {
                  float[] carrierFreqs = satalines.getCarrierFreqs();

                  try {
                     LocationManager.GnssStatusListenerTransportO.onSvStatusChanged.call(transport, svCount, prns, snrs, elevations, azimuths, carrierFreqs);
                  } catch (NullPointerException var16) {
                  }
               } else {
                  LocationManager.GnssStatusListenerTransport.onSvStatusChanged.call(transport, svCount, prns, snrs, elevations, azimuths);
               }
            } else if (aClass == LocationManager.GpsStatusListenerTransport.TYPE) {
               svCount = satalines.getSvCount();
               prns = satalines.getPrns();
               snrs = satalines.getSnrs();
               elevations = satalines.getElevations();
               azimuths = satalines.getAzimuths();
               int ephemerisMask = satalines.getEphemerisMask();
               int almanacMask = satalines.getAlmanacMask();
               int usedInFixMask = satalines.getUsedInFixMask();
               if (LocationManager.GpsStatusListenerTransport.onSvStatusChanged != null) {
                  LocationManager.GpsStatusListenerTransport.onSvStatusChanged.call(transport, svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);
               } else if (LocationManager.GpsStatusListenerTransportVIVO.onSvStatusChanged != null) {
                  LocationManager.GpsStatusListenerTransportVIVO.onSvStatusChanged.call(transport, svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask, new long[svCount]);
               } else if (LocationManager.GpsStatusListenerTransportSumsungS5.onSvStatusChanged != null) {
                  LocationManager.GpsStatusListenerTransportSumsungS5.onSvStatusChanged.call(transport, svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask, new int[svCount]);
               } else if (LocationManager.GpsStatusListenerTransportOPPO_R815T.onSvStatusChanged != null) {
                  int len = prns.length;
                  int[] ephemerisMasks = new int[len];

                  for(int i = 0; i < len; ++i) {
                     ephemerisMasks[i] = satalines.getEphemerisMask();
                  }

                  int[] almanacMasks = new int[len];

                  for(int i = 0; i < len; ++i) {
                     almanacMasks[i] = satalines.getAlmanacMask();
                  }

                  int[] usedInFixMasks = new int[len];

                  for(int i = 0; i < len; ++i) {
                     usedInFixMasks[i] = satalines.getUsedInFixMask();
                  }

                  LocationManager.GpsStatusListenerTransportOPPO_R815T.onSvStatusChanged.call(transport, svCount, prns, snrs, elevations, azimuths, ephemerisMasks, almanacMasks, usedInFixMasks, svCount);
               }
            }
         } catch (Throwable var17) {
            Throwable e = var17;
            e.printStackTrace();
         }
      }

   }

   private static String getSouthEast(VLocation location) {
      return location.getLongitude() > 0.0 ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQhSVg==")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IS5SVg=="));
   }

   private static String getNorthWest(VLocation location) {
      return location.getLatitude() > 0.0 ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz5SVg==")) : StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5SVg=="));
   }

   public static String getGPSLat(double v) {
      int du = (int)v;
      double fen = (v - (double)du) * 60.0;
      return du + leftZeroPad((int)fen, 2) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg==")) + String.valueOf(fen).substring(2);
   }

   private static String leftZeroPad(int num, int size) {
      return leftZeroPad(String.valueOf(num), size);
   }

   private static String leftZeroPad(String num, int size) {
      StringBuilder sb = new StringBuilder(size);
      int i;
      if (num == null) {
         for(i = 0; i < size; ++i) {
            sb.append('0');
         }
      } else {
         for(i = 0; i < size - num.length(); ++i) {
            sb.append('0');
         }

         sb.append(num);
      }

      return sb.toString();
   }

   public static String checksum(String nema) {
      String checkStr = nema;
      if (nema.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PRhSVg==")))) {
         checkStr = nema.substring(1);
      }

      int sum = 0;

      for(int i = 0; i < checkStr.length(); ++i) {
         sum ^= (byte)checkStr.charAt(i);
      }

      return nema + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PD5SVg==")) + String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQM5KmEFSFo=")), sum).toLowerCase();
   }
}
