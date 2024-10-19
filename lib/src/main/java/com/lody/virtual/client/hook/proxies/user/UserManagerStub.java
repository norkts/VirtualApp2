package com.lody.virtual.client.hook.proxies.user;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import java.util.Collections;
import mirror.android.content.pm.UserInfo;
import mirror.android.os.IUserManager;

@TargetApi(17)
public class UserManagerStub extends BinderInvocationProxy {
   public UserManagerStub() {
      super(IUserManager.Stub.asInterface, "user");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("setApplicationRestrictions"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getApplicationRestrictions"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getApplicationRestrictionsForUser"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isUserUnlocked"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isProfile"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isUserUnlockingOrUnlocked"));
      this.addMethodProxy(new ReplaceLastUserIdMethodProxy("isManagedProfile"));
      this.addMethodProxy(new ResultStaticMethodProxy("getProfileParent", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getUserIcon", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getUserInfo", UserInfo.ctor.newInstance(0, "Admin", UserInfo.FLAG_PRIMARY.get())));
      this.addMethodProxy(new ResultStaticMethodProxy("getDefaultGuestRestrictions", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("setDefaultGuestRestrictions", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("removeRestrictions", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getUsers", Collections.singletonList(UserInfo.ctor.newInstance(0, "Admin", UserInfo.FLAG_PRIMARY.get()))));
      this.addMethodProxy(new ResultStaticMethodProxy("createUser", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("createProfileForUser", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getProfiles", Collections.EMPTY_LIST));
      this.addMethodProxy(new ResultStaticMethodProxy("setUserEnabled", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("removeUser", false));
      this.addMethodProxy(new ResultStaticMethodProxy("setUserName", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("setUserIcon", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("canAddMoreManagedProfiles", false));
      this.addMethodProxy(new ResultStaticMethodProxy("setUserRestrictions", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("setUserRestriction", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("markGuestForDeletion", true));
      this.addMethodProxy(new ResultStaticMethodProxy("createRestrictedProfile", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("getPrimaryUser", (Object)null));
      this.addMethodProxy(new ResultStaticMethodProxy("hasBaseUserRestriction", false));
      this.addMethodProxy(new ResultStaticMethodProxy("getUserName", ""));
      this.addMethodProxy(new ResultStaticMethodProxy("getSeedAccountOptions", (Object)null));
   }
}
