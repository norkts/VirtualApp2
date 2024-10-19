package com.lody.virtual.client.hook.proxies.slice;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import java.util.Collections;
import mirror.com.android.internal.app.ISliceManager;

public class SliceManagerStub extends BinderInvocationProxy {
   public SliceManagerStub() {
      super(ISliceManager.Stub.TYPE, "slice");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ResultStaticMethodProxy("pinSlice", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("unpinSlice", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("hasSliceAccess", false));
      this.addMethodProxy(new ResultStaticMethodProxy("grantSlicePermission", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("revokeSlicePermission", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("checkSlicePermission", 0));
      this.addMethodProxy(new ResultStaticMethodProxy("grantPermissionFromUser", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getPinnedSpecs", Collections.EMPTY_LIST.toArray()));
      this.addMethodProxy(new ResultStaticMethodProxy("getPinnedSlices", Collections.EMPTY_LIST.toArray()));
   }
}
