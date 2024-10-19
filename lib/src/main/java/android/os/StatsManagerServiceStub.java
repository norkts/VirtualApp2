package android.os;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.android.os.IStatsManagerService;

public class StatsManagerServiceStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = StringFog.decrypt("ABETAhYDPh0CCBcC");

   public StatsManagerServiceStub() {
      super(IStatsManagerService.Stub.asInterface, StringFog.decrypt("ABETAhYDPh0CCBcC"));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("AAAGMgQaPjUGGxEYJh8LAQQGHwoA"), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("AQAfGRMLGxIXDjQVHQwGPBUXBAQaNhwN"), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("AAAGNwYaNgUGLB0eDwYJACYaFwsJOhcsHxcCCBsHHAs="), new long[0]));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("AQAfGRMLHhAXBgQVKgAAFQwVBSYGPh0EChY/GQocEhEbGQs="), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("AAAGNBcBPhcADgEEOhoMAAYAHwcLLQ=="), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("BgsBExEsLRwCCxERGhs9BgcBFRcHPRYR"), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("FAAGJAAJNgAXCgAVDSoWAwAAHwgLMQcqCwE="), new long[0]));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("FAAGOwAaPhcCGxM="), new byte[0]));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("FAAGMgQaPg=="), new byte[0]));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("EgEWNQoAORoEGgARHQYBHQ=="), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("AQAVHxYaOgEzGh4cKBsBHiYTGgkMPhAI"), (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy(StringFog.decrypt("BgsAEwIHLAcGHSIFBQMvBwofNQQCMxECDBk="), (Object)null));
   }
}
