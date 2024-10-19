package com.lody.virtual.oem;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BuildCompat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class OemPermissionHelper {
   private static List<ComponentName> EMUI_AUTO_START_COMPONENTS = Arrays.asList(new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYL2ggAgZrAQ4eLRgcO2IgLDU=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYL2ggAgZrAQ4eLRgcO2IgLDV8Nyw9KC1fCm8KIAFoNA05IAcfJX0jLDVqHhoaKhYcD28jEjdgHCAsIxVbMWoKBhFoJCwaKi4YCmcFSFo="))), new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYL2ggAgZrAQ4eLRgcO2IgLDU=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYL2ggAgZrAQ4eLRgcO2IgLDV8NFk5Iz42L2oKTT96Jwo6Ji4MOmoaOCRlMywLIy4ALGcwMDdhNwoRLy42MWUVLAZuAVRF"))), new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYL2ggAgZrAQ4eLRgcO2IgLDU=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYL2ggAgZrAQ4eLRgcO2IgLDV8NCQ5LD4YKWUwMCZqNwU5OwcuM28FJD1lNDM5Ii0qP28gMAVhHCAsIxUqDW8aBgRlJwICLT0qI2YwGj9rAVRF"))), new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYL2ggAgZrAQ4eLRgcO2IgLDU=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYL2ggAgZrAQ4eLRgcO2IgLDV8Nyw9KC1fCm8KIAFoNA05IAcfJX0jLDVqHhoaKhY+LWsVQStiHCAsIxVbMWoKBhFoJCwaKi4YCmcFSFo="))));
   private static List<ComponentName> FLYME_AUTO_START_COMPONENTS = Arrays.asList(new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojEitjAQIvOj4qOWkVGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojEitjAQIvOj4qOWkVBSZsESg5LBgYD2EgGipsMB4SKS5bCG8bFlZjETAZJQg6IGoVBlo="))), new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojEitjAQIvOj4qOWkVGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojEitjAQIvOj4qOWkVBSZsJygqKhcMI2YVBSlnHlkcLyxbJW8VAiJlHiwg"))));
   private static List<ComponentName> VIVO_AUTO_START_COMPONENTS = Arrays.asList(new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojAgFgJBE2Iy0MP2UgRSs=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojAgFgJBE2Iy0MP2UgRSt1NCgaPC06LGAgRSBsJyA9Ki4mI24zNwRjJyBBIBcmOWobFiZnJ105LggmM28jSFo="))), new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogOC9mNBE2IxcMKG8jLANsJx4cLC4IO2AwPCJuAShF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogOC9mNBE2IxcMKG8jLANsJx4cLC4IO2AwPCJuASsdKC4YCmoKOAVsDhE5Lxg2OWowBjFlDBooLjw+OWwFAj5jAQoZ"))), new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogOC9mNBE2IxcMKG8jLANsJx4cLC4IO2AwPCJuAShF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogOC9mNBE2IxcMKG8jLANsJx4cLC4IO2AwPCJuASsdKC4YCmoKOAVsDhE5Ly0YLWobPDFqESAwKi02CWozBhZiAQo7KQdbE24KBi9vNx4/LxhSVg=="))), new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogOC9mNBE2IxcMKG8jLANsJx4cLC4IO2AwPCJuAShF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogOC9mNBE2IxcMKG8jLANsJx4cLC4IO2AwPCJuASsdKC4YCmoKOAVsDhE5Lxg2OWowBjFlD102KRgYLmUaMD8="))));

   public static Intent getPermissionActivityIntent(Context context) {
      BuildCompat.ROMType romType = BuildCompat.getROMType();
      Intent intent;
      ComponentName component;
      Intent intent;
      Iterator var5;
      switch (romType) {
         case EMUI:
            var5 = EMUI_AUTO_START_COMPONENTS.iterator();

            do {
               if (!var5.hasNext()) {
                  return null;
               }

               component = (ComponentName)var5.next();
               intent = new Intent();
               intent.addFlags(268435456);
               intent.setComponent(component);
            } while(!verifyIntent(context, intent));

            return intent;
         case MIUI:
            intent = new Intent();
            intent.addFlags(268435456);
            intent.setClassName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojEi9mDhk2Iy0MP2UgRS9vHh4qLhgcCmIFMFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojEi9mDhk2IxcMKG8jAitlNCwgKSocO2YFFiplJzAqLBcbKmAKNCBqMjAZOwgqM2QKOAJuJw40IwguCGwLJClmHgYuKQg2IQ==")));
            if (verifyIntent(context, intent)) {
               return intent;
            }
            break;
         case FLYME:
            var5 = FLYME_AUTO_START_COMPONENTS.iterator();

            do {
               if (!var5.hasNext()) {
                  return null;
               }

               component = (ComponentName)var5.next();
               intent = new Intent();
               intent.addFlags(268435456);
               intent.setComponent(component);
            } while(!verifyIntent(context, intent));

            return intent;
         case COLOR_OS:
            intent = new Intent();
            intent.addFlags(268435456);
            intent.setClassName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLCVgHh4qKi4pDmoFQS5rATAgLC0qJ2EzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLCVgHh4qKi4pDmoFQS5rATAgLC0qJ2E0RTZqHiQ7Iz0ADmgKICR6IjAZOwgqM2oFPB9qNFERLAc2LGMVLAZjATwzLBgcVg==")));
            if (verifyIntent(context, intent)) {
               return intent;
            }
            break;
         case LETV:
            intent = new Intent();
            intent.addFlags(268435456);
            intent.setClassName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHitmET82LwcYPmoVNC9rVhodLhcqMmEgPCFuAVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHitmET82LwcYPmoVNC9rVhodLhcqMmEgPCFuCh4AIy0cKWgzGgNsDw4oJj0mLm4IODNlNzAhLAcqJw==")));
            if (verifyIntent(context, intent)) {
               return intent;
            }
            break;
         case VIVO:
            var5 = VIVO_AUTO_START_COMPONENTS.iterator();

            do {
               if (!var5.hasNext()) {
                  return null;
               }

               component = (ComponentName)var5.next();
               intent = new Intent();
               intent.addFlags(268435456);
               intent.setComponent(component);
            } while(!verifyIntent(context, intent));

            return intent;
         case _360:
            intent = new Intent();
            intent.addFlags(268435456);
            intent.setClassName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogJC9jHh41Myo9Kn8VPCVoNx4dLhc2O2IwLFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogJC9jHh41Myo9Kn8VPCVoNx4dLhc2O2IwLylqDh0dKi4qIGsKRARjHjwdLAdfM24FNB9uDhowKT4YLGkVSFo=")));
            if (verifyIntent(context, intent)) {
               return intent;
            }
      }

      return null;
   }

   private static boolean verifyIntent(Context context, Intent intent) {
      ResolveInfo info = context.getPackageManager().resolveActivity(intent, 0);
      return info != null && info.activityInfo != null && info.activityInfo.exported;
   }
}
