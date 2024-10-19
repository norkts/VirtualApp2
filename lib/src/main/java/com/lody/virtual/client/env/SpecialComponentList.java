package com.lody.virtual.client.env;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.ArraySet;
import androidx.annotation.RequiresApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.BroadcastIntentData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mirror.android.content.IntentFilterU;

public final class SpecialComponentList {
   private static final List<ComponentName> GMS_BLOCK_COMPONENT = Arrays.asList(new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EjSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EkRTBlHjAqIz4fKmQgAiVsASg8KAgiI2kFLDFiAQYbKT4YOWgVSFo="))), new ComponentName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mD2IzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mD2I0RTBlHjAqIz4fKmQgAiVsASg8KAgiI2kFLDFiAQYbKT4YOWgVSFo="))));
   private static final List<String> GMS_BLOCK_ACTION_LIST = Arrays.asList(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EkRTBlHjAqIz4fKmQmMB1nIiwALys2E2I2Bh1hJ1RF")));
   private static final List<String> ACTION_BLACK_LIST = new ArrayList(2);
   private static final Map<String, String> PROTECTED_ACTION_MAP = new HashMap(5);
   private static final HashSet<String> WHITE_PERMISSION = new HashSet(3);
   private static final HashSet<String> BROADCAST_START_WHITE_LIST = new HashSet();
   private static final HashSet<String> INSTRUMENTATION_CONFLICTING = new HashSet(2);
   private static final HashSet<String> SPEC_SYSTEM_APP_LIST = new HashSet(3);
   public static final Set<String> SYSTEM_BROADCAST_ACTION = new HashSet(7);
   private static final Set<String> PRE_INSTALL_PACKAGES = new HashSet(7);
   private static final String PROTECT_ACTION_PREFIX = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwIARgJwo/Ly42PWk2NFo="));

   public static Set<String> getPreInstallPackages() {
      return PRE_INSTALL_PACKAGES;
   }

   public static void addStaticBroadCastWhiteList(String pkg) {
      BROADCAST_START_WHITE_LIST.add(pkg);
   }

   public static boolean isSpecSystemPackage(String pkg) {
      return SPEC_SYSTEM_APP_LIST.contains(pkg);
   }

   public static boolean isConflictingInstrumentation(String packageName) {
      return INSTRUMENTATION_CONFLICTING.contains(packageName);
   }

   public static boolean shouldBlockIntent(Intent intent) {
      ComponentName component = intent.getComponent();
      if (component != null && GMS_BLOCK_COMPONENT.contains(component)) {
         return true;
      } else {
         String action = intent.getAction();
         return action != null && GMS_BLOCK_ACTION_LIST.contains(action);
      }
   }

   public static boolean isActionInBlackList(String action) {
      return ACTION_BLACK_LIST.contains(action);
   }

   public static void addBlackAction(String action) {
      ACTION_BLACK_LIST.add(action);
   }

   @RequiresApi(
      api = 23
   )
   public static void protectIntentFilter(IntentFilter filter) {
      Collection<String> collection = new ArraySet();
      if (filter != null) {
         if (BuildCompat.isUpsideDownCake()) {
            try {
               collection = (Collection)IntentFilterU.mActions.get(filter);
            } catch (Exception var6) {
            }
         } else {
            collection = (Collection)mirror.android.content.IntentFilter.mActions.get(filter);
         }

         Iterator<String> iterator = ((Collection)collection).iterator();
         ArrayList<String> list = new ArrayList();

         while(iterator.hasNext()) {
            String action = (String)iterator.next();
            if (isActionInBlackList(action)) {
               iterator.remove();
            } else {
               String newAction = protectAction(action);
               if (newAction != null) {
                  iterator.remove();
                  list.add(newAction);
               }
            }
         }

         ((Collection)collection).addAll(list);
      }

   }

   public static void protectIntent(Intent intent) {
      String protectAction = protectAction(intent.getAction());
      if (protectAction != null) {
         intent.setAction(protectAction);
      }

   }

   public static Intent unprotectIntent(int userId, Intent intent) {
      BroadcastIntentData intentData = new BroadcastIntentData(intent);
      if (intentData.intent != null && (intentData.userId == -1 || intentData.userId == userId)) {
         intent = intentData.intent;
         String unprotectAction = unprotectAction(intent.getAction());
         if (unprotectAction != null) {
            intent.setAction(unprotectAction);
         }
      }

      return intent;
   }

   public static String getProtectActionPrefix() {
      return PROTECT_ACTION_PREFIX;
   }

   public static String protectAction(String originAction) {
      if (originAction == null) {
         return null;
      } else if (VirtualCore.getConfig().isUnProtectAction(originAction)) {
         return originAction;
      } else if (originAction.startsWith(getProtectActionPrefix())) {
         return originAction;
      } else {
         String newAction = (String)PROTECTED_ACTION_MAP.get(originAction);
         if (newAction == null) {
            newAction = getProtectActionPrefix() + originAction;
         }

         return newAction;
      }
   }

   public static String unprotectAction(String action) {
      if (action == null) {
         return null;
      } else if (action.startsWith(getProtectActionPrefix())) {
         return action.substring(getProtectActionPrefix().length());
      } else {
         Iterator var1 = PROTECTED_ACTION_MAP.entrySet().iterator();

         Map.Entry next;
         String modifiedAction;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            next = (Map.Entry)var1.next();
            modifiedAction = (String)next.getValue();
         } while(!modifiedAction.equals(action));

         return (String)next.getKey();
      }
   }

   public static boolean isWhitePermission(String permission) {
      return WHITE_PERMISSION.contains(permission);
   }

   public static boolean allowedStartFromBroadcast(String str) {
      return BROADCAST_START_WHITE_LIST.contains(str);
   }

   static {
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xNAB9MgpJIRUuA30zSFo=")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xNAB9MgpJIRUuA2MxOFo=")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42RQpiJVlXOyscG30hAl9gMgYMIwVXHQ==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xFhZkDAoOOzw2H2IjSFo=")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xFhZkDAoOJAYAXQ==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xFhZkDAoJIQYqGWEhLFFjHxoILAUMVg==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42MA5iHzBJJBU2E2AhRR1iJSAKLBhSVg==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42MA5iHzBJJBU2E30bGgs=")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42MA5iHzBJJBU2E30hQR1hEVRF")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42PABiHBpXIRUuQH0mPFRnIgYMLitfH2YIMAxhJRpF")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42PABiHBpXIRUuQH0mPFRnIgYJLQYuDGQmAkxhJR5OJQYqVg==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xLBBmDygOJDtfGWQhNF9kAVRF")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksIz1fLGwjBitsMxoVLhgEJ2EaBipsNx0dJAYmUmEmFlRjNSgWKDs2BQ==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksIz1fLGwjBitsMxoVLhgEJ2EaBipsNx0dJAYmUmEhMFRiDx5MLAYqVg==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k2KAg1DmUFLC5qDRoQIgY+XWoLAgBnHCRTJwYAVg==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k2KAg1DmUFLC5qDRoQJTw+DGwhMAp9JQpAOzsYVg==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k2KAg1DmUFLC5qDRoUOxYiBWwhNFRhDzBJJQYYBmALBlZgHyxF")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k2KAg1Dm4FNCZlMxoAJDwcDGoINFRnDzhNOzs2E2AhRR1iJSAK")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42PBNjD1lIICscHWEmLApjHCwK")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xNBZkD1kSOzxbXWMIGh99DzgfLCs2BQ==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1koKi0qOWUzLCVlMxoRISwAX2sIFgp9NSwOIAYMHX0xPFRgAVRF")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1koKi0qOWUzLCVlMxpXJDwqGWwmNBVhDB5LJywcVg==")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42BgphDDASJyscE2QbHglgN1RF")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k3KAc2MW4nMFN9JQIWJBYuE2UmBg5kMjxJJzsuHWAmMFBiNRpF")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42NBRkMjhNJwUAU2AIMFBiNRoAIyscAmQ2IFZhN1RF")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42FlFkMiRBIiwYE2QhNF9nNQZAKisuGWEIAlRhJRpF")));
      SYSTEM_BROADCAST_ACTION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRcYCGsVEi99JR4pKAcYL28KRR9oJ1ksLC4mJw==")));
      ACTION_BLACK_LIST.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxgmI2wjBi1rDi8bLRg2CmMKAil8MiQfJDsIBWMbPFRkDAZPLxUMAmIYFlo=")));
      ACTION_BLACK_LIST.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxgmI2wjBi1rDi8bLRg2CmMKAil8MiQfJDsIBWMbPFRkDAYMLitfB2cIIAtiHwZF")));
      WHITE_PERMISSION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EkRTZuATA9Ki4qIWwnBg9gHzBPLzsAWGMLRQ1hIhpOJAYcWmczSFo=")));
      WHITE_PERMISSION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4+DmEVNyllHl0+LAQqQGQxAgxjHzBLKiwuBmIbLFJnHw5B")));
      WHITE_PERMISSION.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCw+H2UmAlVkNTAOISxbDGALPFRnJ1RF")));
      PROTECTED_ACTION_MAP.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xOA5hIgIAJwYAE2ALMFVgHyxF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k7Kj02KG8FLCx1Nx4bKgguKmZTRSRpJzAiKQgpKmQbJB99NTgILAYYAmYYLFZhN1RF")));
      PROTECTED_ACTION_MAP.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xOA5hIgIAJwYAE2QxNEhiMiQKLBhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k7Kj02KG8FLCx1Nx4bKgguKmZTRSRpJzAiKQgpKmQbJB99NTgILAYYE2YITUh9HwYJ")));
      PROTECTED_ACTION_MAP.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xOA5hIgIAJwYAE2AhRR1iJSAKLBhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k7Kj02KG8FLCx1Nx4bKgguKmZTRSRpJzAiKQgpKmQbJB99NTgILAYYDGcYOExhDwYJ")));
      PROTECTED_ACTION_MAP.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xLBBmDygOICwcGmMLMFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k7Kj02KG8FLCx1Nx4bKgguKmZTRSRpJzAiKQgpKmcILFRnIgYOLBUMBmYVSFo=")));
      PROTECTED_ACTION_MAP.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk4xLBBmDygOJBYAAX0mOFRgAVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKmwKNDdgV1k7Kj02KG8FLCx1Nx4bKgguKmZTRSRpJzAiKQgpKmcILFRnIgZALAVbGGI2Flc=")));
      PROTECTED_ACTION_MAP.put(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42QQpmHBoAJQUYH2ALBl9gHAoALysuAmQxRVVkJSQK")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42QQpmHBoAJQUYH2ALBl9gHAoALysuAmQxRVVkJSQK")));
      INSTRUMENTATION_CONFLICTING.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogJC9jHh41Oj1XOWkFLCk=")));
      INSTRUMENTATION_CONFLICTING.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogJC9jHh41Oj1XOWkFLClhJw4wKggYDmAaLFo=")));
      INSTRUMENTATION_CONFLICTING.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojODd9JDA6Ki1fCX8VJDdvETgbLRhSVg==")));
      SPEC_SYSTEM_APP_LIST.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iEVRF")));
      SPEC_SYSTEM_APP_LIST.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC0mJ30zICxuATxF")));
      SPEC_SYSTEM_APP_LIST.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmowRSVvNx4vLhcMD04wFipqJB4bKQhbIGwjSFo=")));
      SPEC_SYSTEM_APP_LIST.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4uDmUaICtoHjAzLBcLDmIFNCprDg0bLRc6DmMKNCpsN1RF")));
      PRE_INSTALL_PACKAGES.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojRQV9ATg/KQMYMmUFLCw=")));
      PRE_INSTALL_PACKAGES.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogOC9mNBE2Iy02CWozOAVrJx4b")));
      PRE_INSTALL_PACKAGES.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogRS99Dh43KQMYM24jPCtoJygbKgguCE4zNC9vIB48Ly1fMmoFLD8=")));
      if (VEnvironment.enableMediaRedirect()) {
      }

   }
}
