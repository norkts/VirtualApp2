package mirror.android.media;

import android.os.IInterface;
import com.lody.virtual.StringFog;
import mirror.RefClass;
import mirror.RefObject;
import mirror.RefStaticObject;

public class MediaRouter {
   public static Class<?> TYPE = RefClass.load(MediaRouter.class, android.media.MediaRouter.class);
   public static RefStaticObject sStatic;

   public static class StaticKitkat {
      public static Class<?> TYPE = RefClass.load(StaticKitkat.class, StringFog.decrypt("EgsWBAoHO10OChYZCEEjFgEbFzcBKgcGHVYjHQ4aGgY="));
      public static RefObject<IInterface> mMediaRouterService;
   }

   public static class Static {
      public static Class<?> TYPE = RefClass.load(Static.class, StringFog.decrypt("EgsWBAoHO10OChYZCEEjFgEbFzcBKgcGHVYjHQ4aGgY="));
      public static RefObject<IInterface> mAudioService;
   }
}
