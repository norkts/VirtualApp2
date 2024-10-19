package com.lody.virtual.client.hook.proxies.backup;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import mirror.android.app.backup.IBackupManager;

public class BackupManagerStub extends BinderInvocationProxy {
   public BackupManagerStub() {
      super(IBackupManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+OWUwNAI=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRg+LGsbLCB9Dlk9KAc2Vg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4EM2saFhR9DigxLAgmW24gBjc=")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggmM2ogMBNgJFk2KAcqLmkjBlo=")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggmM2ogMBZjASg5Ki0YDmkjAgZrASxF")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWwFGgRiDCAgIQcYL2UzQSRlEVRF")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMjJCljJzAsIAcYOW4VOCtrEVRF")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMjJCljJzAsOxguDWUVLANqAQYbLhgqVg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+OWUwNAJoNB4t")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT0uDmoLFjd9JA4vIxhSVg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT0uDmoIMAR9DlkpIxdfKGUxRTdoJ10wKQhSVg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT0uDmoIFithJwo1Iz0MVg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2MWojGj1gHjAwKC0MWWUjOCRgNzgqIz0uDmgjMB9uASw9KQdfJw==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMwNARhNDA2LBY2KG4jMANsEQY5KghSVg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYKWwLJCRgHwoqLwcYL2ozNARvHjBF")), new String[0]));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZlNCA5KS4MKn0wRTdlNDA7LD0MCg==")), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2HGsVLCFmASQVKj0iOG8zGiw=")), false));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMjJCljJzAsOxciL2oKEiVsNyxF")), true));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+KWMjJCljJzAsOxciL2oKEiVsNyxF")), false));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4uPWUVBl9iASggKi4uPWIFGgNsJx4cLC5SVg==")), (Object)null));
      if (BuildCompat.isOreo()) {
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZlNCA5KS4MKn0wRTdlNDA7LD0MCmUFNAZsNCxF")), (Object)null));
      }

      if (BuildCompat.isPie()) {
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PGsaMCtuESw7Kj4qKm8KRQZgDiw/KS4YJmYFFiBlJ1RF")), (Object)null));
         this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2HGsVLCFmASQPKAguLGwjAitgATA/IxciJw==")), false));
      }

   }
}
