package com.carlos.common.imagepicker.utils;

import androidx.annotation.RequiresApi;
import com.carlos.libcommon.StringFog;

public class PermissionsConstant {
   public static final int REQUEST_CAMERA = 1;
   public static final int REQUEST_EXTERNAL_READ = 2;
   public static final int REQUEST_EXTERNAL_WRITE = 3;
   public static final String[] PERMISSIONS_CAMERA = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCw2HWgILB9hAVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsmU2sLFgpgIgoXOzwAU30xJExmMjBOLiwqAmYmFlo="))};
   public static final String[] PERMISSIONS_EXTERNAL_WRITE = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsmU2sLFgpgIgoXOzwAU30xJExmMjBOLiwqAmYmFlo="))};
   @RequiresApi(
      api = 16
   )
   public static final String[] PERMISSIONS_EXTERNAL_READ = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl9mDwYTJytfDGALHhNnMiwQLzsmAGYFSFo="))};
}
