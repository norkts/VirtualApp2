package com.lody.virtual.client.hook.proxies.isms;

import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSpecPkgMethodProxy;
import mirror.com.android.internal.telephony.ISms;

public class ISmsStub extends BinderInvocationProxy {
   public ISmsStub() {
      super(ISms.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2DW8zSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      if (VERSION.SDK_INT >= 23) {
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVHiRoDjApIy0iM2kgAghsNwYeOxg2JWoKIAtsJygSIy5fD2ggFgVrJygb")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PGsaMCtoDjApIy0iM2khNCZiATAqJhgiBGAjMBBqDig8KAdfI2gzNCY=")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AKGkbEithJyg7KC0MAG8LLCloJSgtJi4ACGkjLCVlJCw7Ki5fJ2wzSFo=")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLMDdmHiAUKi4uAWUjRQNoJAoaLS4uCA==")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLMDdmHiAUKi4uAWUjRQNoJAoaLS4uCG4gGj9vHywuKT4EQGsKFgFlHjAcJQcYJWUjSFo=")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgIMCtnEQoUKi4uAWUjRQNoJAoaLS4uCA==")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgIMCtnEQoUKi4uAWUjRQNoJAoaLS4uCG4gGj9vHywuKT4EQGsKFgFlHjAcJQcYJWUjSFo=")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLEgVgEQozIxciKGU2BituHixILD0MUmYKMDZpJygiKBgACA==")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgILAZgJyw/KBY2PWgwBlo=")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgILAZgJyw/KBVXLW8wBi9sETg5KgUqJ2cVFlo=")), 1));
      } else if (VERSION.SDK_INT >= 21) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVHiRoDjApIy0iM2kgAghsNwYeOxg2JWoKIFo="))));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVHiRoDjApIy0iM2kgAghsNwYeOxg2JWoKIAtsJygSIy5fD2ggFgVrJygb")), 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PGsaMCtoDjApIy0iM2khNCZiATAqJhgiVg=="))));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PGsaMCtoDjApIy0iM2khNCZiATAqJhgiBGAjMBBqDig8KAdfI2gzNCY=")), 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AKGkbEithJyg7KC0MAG8LLCloJSgt"))));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AKGkbEithJyg7KC0MAG8LLCloJSgtJi4ACGkjLCVlJCw7Ki5fJ2wzSFo=")), 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLMDdmHiBF"))));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLMDdmHiAUKi4uAWUjRQNoJAoaLS4uCA==")), 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgIMCtnEQpF"))));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgIMCtnEQoUKi4uAWUjRQNoJAoaLS4uCA==")), 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLEgVgEQozIxciKGU2BituHixF"))));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLEgVgEQozIxciKGU2BituHixILD0MUmYKMDZpJygiKBgACA==")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgILAZgJyw/KBY2PWgwBlo=")), 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgILAZgJyw/KBVXLW8wBi9sETg5KgUqJ2cVFlo=")), 1));
      } else if (VERSION.SDK_INT >= 18) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVHiRoDjApIy0iM2kgAghsNwYeOxg2JWoKIFo="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PGsaMCtoDjApIy0iM2khNCZiATAqJhgiVg=="))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AKGkbEithJyg7KC0MAG8LLCloJSgt"))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLMDdmHiBF"))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgIMCtnEQpF"))));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLEgVgEQozIxciKGU2BituHixF"))));
      }

   }
}
