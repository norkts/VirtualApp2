package com.lody.virtual.client.hook.proxies.isms;

import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceSpecPkgMethodProxy;
import mirror.com.android.internal.telephony.ISms;

public class ISmsStub extends BinderInvocationProxy {
   public ISmsStub() {
      super(ISms.Stub.asInterface, "isms");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      if (VERSION.SDK_INT >= 23) {
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("getAllMessagesFromIccEfForSubscriber", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("updateMessageOnIccEfForSubscriber", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("copyMessageToIccEfForSubscriber", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendDataForSubscriber", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendDataForSubscriberWithSelfPermissions", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendTextForSubscriber", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendTextForSubscriberWithSelfPermissions", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendMultipartTextForSubscriber", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendStoredText", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendStoredMultipartText", 1));
      } else if (VERSION.SDK_INT >= 21) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getAllMessagesFromIccEf"));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("getAllMessagesFromIccEfForSubscriber", 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("updateMessageOnIccEf"));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("updateMessageOnIccEfForSubscriber", 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("copyMessageToIccEf"));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("copyMessageToIccEfForSubscriber", 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("sendData"));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendDataForSubscriber", 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("sendText"));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendTextForSubscriber", 1));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("sendMultipartText"));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendMultipartTextForSubscriber", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendStoredText", 1));
         this.addMethodProxy(new ReplaceSpecPkgMethodProxy("sendStoredMultipartText", 1));
      } else if (VERSION.SDK_INT >= 18) {
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getAllMessagesFromIccEf"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("updateMessageOnIccEf"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("copyMessageToIccEf"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("sendData"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("sendText"));
         this.addMethodProxy(new ReplaceCallingPkgMethodProxy("sendMultipartText"));
      }

   }
}
