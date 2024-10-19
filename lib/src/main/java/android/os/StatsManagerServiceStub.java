package android.os;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import mirror.android.os.IStatsManagerService;

public class StatsManagerServiceStub extends BinderInvocationProxy {
   private static final String SERVER_NAME = "statsmanager";

   public StatsManagerServiceStub() {
      super(IStatsManagerService.Stub.asInterface, "statsmanager");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy("setDataFetchOperation", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("removeDataFetchOperation", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("setActiveConfigsChangedOperation", new long[0]));
      this.addMethodProxy(new ResultStaticMethodProxy("removeActiveConfigsChangedOperation", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("setBroadcastSubscriber", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("unsetBroadcastSubscriber", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getRegisteredExperimentIds", new long[0]));
      this.addMethodProxy(new ResultStaticMethodProxy("getMetadata", new byte[0]));
      this.addMethodProxy(new ResultStaticMethodProxy("getData", new byte[0]));
      this.addMethodProxy(new ResultStaticMethodProxy("addConfiguration", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("registerPullAtomCallback", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("unregisterPullAtomCallback", (Object)null));
   }
}
