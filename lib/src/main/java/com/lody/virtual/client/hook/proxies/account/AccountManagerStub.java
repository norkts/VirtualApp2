package com.lody.virtual.client.hook.proxies.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.IAccountManagerResponse;
import android.annotation.TargetApi;
import android.os.Bundle;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.ipc.VAccountManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.os.VUserHandle;
import java.lang.reflect.Method;
import java.util.Map;
import mirror.android.accounts.IAccountManager;

public class AccountManagerStub extends BinderInvocationProxy {
   private static VAccountManager Mgr = VAccountManager.get();

   public AccountManagerStub() {
      super(IAccountManager.Stub.asInterface, "account");
   }

   public void inject() throws Throwable {
      super.inject();

      try {
         AccountManager accountManager = (AccountManager)this.getContext().getSystemService("account");
         Reflect.on((Object)accountManager).set("mService", ((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
      } catch (Throwable var2) {
         Throwable e = var2;
         e.printStackTrace();
      }

   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new getPassword());
      this.addMethodProxy(new getUserData());
      this.addMethodProxy(new getAuthenticatorTypes());
      this.addMethodProxy(new getAccounts());
      this.addMethodProxy(new getAccountsForPackage());
      this.addMethodProxy(new getAccountsByTypeForPackage());
      this.addMethodProxy(new getAccountsAsUser());
      this.addMethodProxy(new hasFeatures());
      this.addMethodProxy(new getAccountsByFeatures());
      this.addMethodProxy(new addAccountExplicitly());
      this.addMethodProxy(new removeAccount());
      this.addMethodProxy(new removeAccountAsUser());
      this.addMethodProxy(new removeAccountExplicitly());
      this.addMethodProxy(new copyAccountToUser());
      this.addMethodProxy(new invalidateAuthToken());
      this.addMethodProxy(new peekAuthToken());
      this.addMethodProxy(new setAuthToken());
      this.addMethodProxy(new setPassword());
      this.addMethodProxy(new clearPassword());
      this.addMethodProxy(new setUserData());
      this.addMethodProxy(new updateAppPermission());
      this.addMethodProxy(new getAuthToken());
      this.addMethodProxy(new addAccount());
      this.addMethodProxy(new addAccountAsUser());
      this.addMethodProxy(new updateCredentials());
      this.addMethodProxy(new editProperties());
      this.addMethodProxy(new confirmCredentialsAsUser());
      this.addMethodProxy(new accountAuthenticated());
      this.addMethodProxy(new getAuthTokenLabel());
      this.addMethodProxy(new addSharedAccountAsUser());
      this.addMethodProxy(new getSharedAccountsAsUser());
      this.addMethodProxy(new removeSharedAccountAsUser());
      this.addMethodProxy(new renameAccount());
      this.addMethodProxy(new getPreviousName());
      this.addMethodProxy(new renameSharedAccountAsUser());
      if (BuildCompat.isOreo()) {
         this.addMethodProxy(new finishSessionAsUser());
         this.addMethodProxy(new getAccountVisibility());
         this.addMethodProxy(new addAccountExplicitlyWithVisibility());
         this.addMethodProxy(new getAccountsAndVisibilityForPackage());
         this.addMethodProxy(new getPackagesAndVisibilityForAccount());
         this.addMethodProxy(new setAccountVisibility());
         this.addMethodProxy(new startAddAccountSession());
         this.addMethodProxy(new startUpdateCredentialsSession());
         this.addMethodProxy(new registerAccountListener());
         this.addMethodProxy(new unregisterAccountListener());
      }

   }

   @TargetApi(26)
   private static class unregisterAccountListener extends MethodProxy {
      private unregisterAccountListener() {
      }

      public String getMethodName() {
         return "unregisterAccountListener";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         AccountManagerStub.Mgr.unregisterAccountListener((String[])objArr[0]);
         return 0;
      }

      // $FF: synthetic method
      unregisterAccountListener(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class registerAccountListener extends MethodProxy {
      private registerAccountListener() {
      }

      public String getMethodName() {
         return "registerAccountListener";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         AccountManagerStub.Mgr.registerAccountListener((String[])objArr[0]);
         return 0;
      }

      // $FF: synthetic method
      registerAccountListener(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class startUpdateCredentialsSession extends MethodProxy {
      private startUpdateCredentialsSession() {
      }

      public String getMethodName() {
         return "startUpdateCredentialsSession";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         AccountManagerStub.Mgr.startUpdateCredentialsSession((IAccountManagerResponse)objArr[0], (Account)objArr[1], (String)objArr[2], (Boolean)objArr[3], (Bundle)objArr[4]);
         return 0;
      }

      // $FF: synthetic method
      startUpdateCredentialsSession(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class startAddAccountSession extends MethodProxy {
      private startAddAccountSession() {
      }

      public String getMethodName() {
         return "startAddAccountSession";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         AccountManagerStub.Mgr.startAddAccountSession((IAccountManagerResponse)objArr[0], (String)objArr[1], (String)objArr[2], (String[])objArr[3], (Boolean)objArr[4], (Bundle)objArr[5]);
         return 0;
      }

      // $FF: synthetic method
      startAddAccountSession(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class setAccountVisibility extends MethodProxy {
      private setAccountVisibility() {
      }

      public String getMethodName() {
         return "setAccountVisibility";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         return AccountManagerStub.Mgr.setAccountVisibility((Account)objArr[0], (String)objArr[1], (Integer)objArr[2]);
      }

      // $FF: synthetic method
      setAccountVisibility(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class getPackagesAndVisibilityForAccount extends MethodProxy {
      private getPackagesAndVisibilityForAccount() {
      }

      public String getMethodName() {
         return "getPackagesAndVisibilityForAccount";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         return AccountManagerStub.Mgr.getPackagesAndVisibilityForAccount((Account)objArr[0]);
      }

      // $FF: synthetic method
      getPackagesAndVisibilityForAccount(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class getAccountsAndVisibilityForPackage extends MethodProxy {
      private getAccountsAndVisibilityForPackage() {
      }

      public String getMethodName() {
         return "getAccountsAndVisibilityForPackage";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         return AccountManagerStub.Mgr.getAccountsAndVisibilityForPackage((String)objArr[0], (String)objArr[1]);
      }

      // $FF: synthetic method
      getAccountsAndVisibilityForPackage(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class addAccountExplicitlyWithVisibility extends MethodProxy {
      private addAccountExplicitlyWithVisibility() {
      }

      public String getMethodName() {
         return "addAccountExplicitlyWithVisibility";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         return AccountManagerStub.Mgr.addAccountExplicitlyWithVisibility((Account)objArr[0], (String)objArr[1], (Bundle)objArr[2], (Map)objArr[3]);
      }

      // $FF: synthetic method
      addAccountExplicitlyWithVisibility(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class getAccountVisibility extends MethodProxy {
      private getAccountVisibility() {
      }

      public String getMethodName() {
         return "getAccountVisibility";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         return AccountManagerStub.Mgr.getAccountVisibility((Account)objArr[0], (String)objArr[1]);
      }

      // $FF: synthetic method
      getAccountVisibility(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class finishSessionAsUser extends MethodProxy {
      private finishSessionAsUser() {
      }

      public String getMethodName() {
         return "finishSessionAsUser";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         AccountManagerStub.Mgr.finishSessionAsUser((IAccountManagerResponse)objArr[0], (Bundle)objArr[1], (Boolean)objArr[2], (Bundle)objArr[3], (Integer)objArr[4]);
         return 0;
      }

      // $FF: synthetic method
      finishSessionAsUser(Object x0) {
         this();
      }
   }

   @TargetApi(26)
   private static class isCredentialsUpdateSuggested extends MethodProxy {
      public String getMethodName() {
         return "isCredentialsUpdateSuggested";
      }

      public Object call(Object obj, Method method, Object... objArr) throws Throwable {
         AccountManagerStub.Mgr.isCredentialsUpdateSuggested((IAccountManagerResponse)objArr[0], (Account)objArr[1], (String)objArr[2]);
         return 0;
      }
   }

   private static class renameSharedAccountAsUser extends MethodProxy {
      private renameSharedAccountAsUser() {
      }

      public String getMethodName() {
         return "renameSharedAccountAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account accountToRename = (Account)args[0];
         String newName = (String)args[1];
         int userId = (Integer)args[2];
         return method.invoke(who, args);
      }

      // $FF: synthetic method
      renameSharedAccountAsUser(Object x0) {
         this();
      }
   }

   private static class getPreviousName extends MethodProxy {
      private getPreviousName() {
      }

      public String getMethodName() {
         return "getPreviousName";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         return AccountManagerStub.Mgr.getPreviousName(account);
      }

      // $FF: synthetic method
      getPreviousName(Object x0) {
         this();
      }
   }

   private static class renameAccount extends MethodProxy {
      private renameAccount() {
      }

      public String getMethodName() {
         return "renameAccount";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         Account accountToRename = (Account)args[1];
         String newName = (String)args[2];
         AccountManagerStub.Mgr.renameAccount(response, accountToRename, newName);
         return 0;
      }

      // $FF: synthetic method
      renameAccount(Object x0) {
         this();
      }
   }

   private static class removeSharedAccountAsUser extends MethodProxy {
      private removeSharedAccountAsUser() {
      }

      public String getMethodName() {
         return "removeSharedAccountAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         int userId = (Integer)args[1];
         return method.invoke(who, args);
      }

      // $FF: synthetic method
      removeSharedAccountAsUser(Object x0) {
         this();
      }
   }

   private static class getSharedAccountsAsUser extends MethodProxy {
      private getSharedAccountsAsUser() {
      }

      public String getMethodName() {
         return "getSharedAccountsAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int userId = (Integer)args[0];
         return method.invoke(who, args);
      }

      // $FF: synthetic method
      getSharedAccountsAsUser(Object x0) {
         this();
      }
   }

   private static class addSharedAccountAsUser extends MethodProxy {
      private addSharedAccountAsUser() {
      }

      public String getMethodName() {
         return "addSharedAccountAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         int userId = (Integer)args[1];
         return method.invoke(who, args);
      }

      // $FF: synthetic method
      addSharedAccountAsUser(Object x0) {
         this();
      }
   }

   private static class getAuthTokenLabel extends MethodProxy {
      private getAuthTokenLabel() {
      }

      public String getMethodName() {
         return "getAuthTokenLabel";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         String accountType = (String)args[1];
         String authTokenType = (String)args[2];
         AccountManagerStub.Mgr.getAuthTokenLabel(response, accountType, authTokenType);
         return 0;
      }

      // $FF: synthetic method
      getAuthTokenLabel(Object x0) {
         this();
      }
   }

   private static class accountAuthenticated extends MethodProxy {
      private accountAuthenticated() {
      }

      public String getMethodName() {
         return "accountAuthenticated";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         return AccountManagerStub.Mgr.accountAuthenticated(account);
      }

      // $FF: synthetic method
      accountAuthenticated(Object x0) {
         this();
      }
   }

   private static class confirmCredentialsAsUser extends MethodProxy {
      private confirmCredentialsAsUser() {
      }

      public String getMethodName() {
         return "confirmCredentialsAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         Account account = (Account)args[1];
         Bundle options = (Bundle)args[2];
         boolean expectActivityLaunch = (Boolean)args[3];
         AccountManagerStub.Mgr.confirmCredentials(response, account, options, expectActivityLaunch);
         return 0;
      }

      // $FF: synthetic method
      confirmCredentialsAsUser(Object x0) {
         this();
      }
   }

   private static class editProperties extends MethodProxy {
      private editProperties() {
      }

      public String getMethodName() {
         return "editProperties";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         String authTokenType = (String)args[1];
         boolean expectActivityLaunch = (Boolean)args[2];
         AccountManagerStub.Mgr.editProperties(response, authTokenType, expectActivityLaunch);
         return 0;
      }

      // $FF: synthetic method
      editProperties(Object x0) {
         this();
      }
   }

   private static class updateCredentials extends MethodProxy {
      private updateCredentials() {
      }

      public String getMethodName() {
         return "updateCredentials";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         Account account = (Account)args[1];
         String authTokenType = (String)args[2];
         boolean expectActivityLaunch = (Boolean)args[3];
         Bundle options = (Bundle)args[4];
         AccountManagerStub.Mgr.updateCredentials(response, account, authTokenType, expectActivityLaunch, options);
         return 0;
      }

      // $FF: synthetic method
      updateCredentials(Object x0) {
         this();
      }
   }

   private static class addAccountAsUser extends MethodProxy {
      private addAccountAsUser() {
      }

      public String getMethodName() {
         return "addAccountAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         String accountType = (String)args[1];
         String authTokenType = (String)args[2];
         String[] requiredFeatures = (String[])args[3];
         boolean expectActivityLaunch = (Boolean)args[4];
         Bundle options = (Bundle)args[5];
         AccountManagerStub.Mgr.addAccount(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
         return 0;
      }

      // $FF: synthetic method
      addAccountAsUser(Object x0) {
         this();
      }
   }

   private static class addAccount extends MethodProxy {
      private addAccount() {
      }

      public String getMethodName() {
         return "addAccount";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         String accountType = (String)args[1];
         String authTokenType = (String)args[2];
         String[] requiredFeatures = (String[])args[3];
         boolean expectActivityLaunch = (Boolean)args[4];
         Bundle options = (Bundle)args[5];
         AccountManagerStub.Mgr.addAccount(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
         return 0;
      }

      // $FF: synthetic method
      addAccount(Object x0) {
         this();
      }
   }

   private static class getAuthToken extends MethodProxy {
      private getAuthToken() {
      }

      public String getMethodName() {
         return "getAuthToken";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         Account account = (Account)args[1];
         String authTokenType = (String)args[2];
         boolean notifyOnAuthFailure = (Boolean)args[3];
         boolean expectActivityLaunch = (Boolean)args[4];
         Bundle options = (Bundle)args[5];
         AccountManagerStub.Mgr.getAuthToken(response, account, authTokenType, notifyOnAuthFailure, expectActivityLaunch, options);
         return 0;
      }

      // $FF: synthetic method
      getAuthToken(Object x0) {
         this();
      }
   }

   private static class updateAppPermission extends MethodProxy {
      private updateAppPermission() {
      }

      public String getMethodName() {
         return "updateAppPermission";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         String authTokenType = (String)args[1];
         int uid = (Integer)args[2];
         boolean val = (Boolean)args[3];
         method.invoke(who, args);
         return 0;
      }

      // $FF: synthetic method
      updateAppPermission(Object x0) {
         this();
      }
   }

   private static class setUserData extends MethodProxy {
      private setUserData() {
      }

      public String getMethodName() {
         return "setUserData";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         String key = (String)args[1];
         String value = (String)args[2];
         AccountManagerStub.Mgr.setUserData(account, key, value);
         return 0;
      }

      // $FF: synthetic method
      setUserData(Object x0) {
         this();
      }
   }

   private static class clearPassword extends MethodProxy {
      private clearPassword() {
      }

      public String getMethodName() {
         return "clearPassword";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         AccountManagerStub.Mgr.clearPassword(account);
         return 0;
      }

      // $FF: synthetic method
      clearPassword(Object x0) {
         this();
      }
   }

   private static class setPassword extends MethodProxy {
      private setPassword() {
      }

      public String getMethodName() {
         return "setPassword";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         String password = (String)args[1];
         AccountManagerStub.Mgr.setPassword(account, password);
         return 0;
      }

      // $FF: synthetic method
      setPassword(Object x0) {
         this();
      }
   }

   private static class setAuthToken extends MethodProxy {
      private setAuthToken() {
      }

      public String getMethodName() {
         return "setAuthToken";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         String authTokenType = (String)args[1];
         String authToken = (String)args[2];
         AccountManagerStub.Mgr.setAuthToken(account, authTokenType, authToken);
         return 0;
      }

      // $FF: synthetic method
      setAuthToken(Object x0) {
         this();
      }
   }

   private static class peekAuthToken extends MethodProxy {
      private peekAuthToken() {
      }

      public String getMethodName() {
         return "peekAuthToken";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         String authTokenType = (String)args[1];
         return AccountManagerStub.Mgr.peekAuthToken(account, authTokenType);
      }

      // $FF: synthetic method
      peekAuthToken(Object x0) {
         this();
      }
   }

   private static class invalidateAuthToken extends MethodProxy {
      private invalidateAuthToken() {
      }

      public String getMethodName() {
         return "invalidateAuthToken";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String accountType = (String)args[0];
         String authToken = (String)args[1];
         AccountManagerStub.Mgr.invalidateAuthToken(accountType, authToken);
         return 0;
      }

      // $FF: synthetic method
      invalidateAuthToken(Object x0) {
         this();
      }
   }

   private static class copyAccountToUser extends MethodProxy {
      private copyAccountToUser() {
      }

      public String getMethodName() {
         return "copyAccountToUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         Account account = (Account)args[1];
         int userFrom = (Integer)args[2];
         int userTo = (Integer)args[3];
         method.invoke(who, args);
         return 0;
      }

      // $FF: synthetic method
      copyAccountToUser(Object x0) {
         this();
      }
   }

   private static class removeAccountExplicitly extends MethodProxy {
      private removeAccountExplicitly() {
      }

      public String getMethodName() {
         return "removeAccountExplicitly";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         return AccountManagerStub.Mgr.removeAccountExplicitly(account);
      }

      // $FF: synthetic method
      removeAccountExplicitly(Object x0) {
         this();
      }
   }

   private static class removeAccountAsUser extends MethodProxy {
      private removeAccountAsUser() {
      }

      public String getMethodName() {
         return "removeAccountAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         Account account = (Account)args[1];
         boolean expectActivityLaunch = (Boolean)args[2];
         AccountManagerStub.Mgr.removeAccount(response, account, expectActivityLaunch);
         return 0;
      }

      // $FF: synthetic method
      removeAccountAsUser(Object x0) {
         this();
      }
   }

   private static class removeAccount extends MethodProxy {
      private removeAccount() {
      }

      public String getMethodName() {
         return "removeAccount";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         Account account = (Account)args[1];
         boolean expectActivityLaunch = (Boolean)args[2];
         AccountManagerStub.Mgr.removeAccount(response, account, expectActivityLaunch);
         return 0;
      }

      // $FF: synthetic method
      removeAccount(Object x0) {
         this();
      }
   }

   private static class addAccountExplicitly extends MethodProxy {
      private addAccountExplicitly() {
      }

      public String getMethodName() {
         return "addAccountExplicitly";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         String password = (String)args[1];
         Bundle extras = (Bundle)args[2];
         return AccountManagerStub.Mgr.addAccountExplicitly(account, password, extras);
      }

      // $FF: synthetic method
      addAccountExplicitly(Object x0) {
         this();
      }
   }

   private static class getAccountsByFeatures extends MethodProxy {
      private getAccountsByFeatures() {
      }

      public String getMethodName() {
         return "getAccountsByFeatures";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         String accountType = (String)args[1];
         String[] features = (String[])args[2];
         AccountManagerStub.Mgr.getAccountsByFeatures(response, accountType, features);
         return 0;
      }

      // $FF: synthetic method
      getAccountsByFeatures(Object x0) {
         this();
      }
   }

   private static class hasFeatures extends MethodProxy {
      private hasFeatures() {
      }

      public String getMethodName() {
         return "hasFeatures";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         IAccountManagerResponse response = (IAccountManagerResponse)args[0];
         Account account = (Account)args[1];
         String[] features = (String[])args[2];
         AccountManagerStub.Mgr.hasFeatures(response, account, features);
         return 0;
      }

      // $FF: synthetic method
      hasFeatures(Object x0) {
         this();
      }
   }

   private static class getAccountsAsUser extends MethodProxy {
      private getAccountsAsUser() {
      }

      public String getMethodName() {
         return "getAccountsAsUser";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String accountType = (String)args[0];
         return AccountManagerStub.Mgr.getAccounts(accountType);
      }

      // $FF: synthetic method
      getAccountsAsUser(Object x0) {
         this();
      }
   }

   private static class getAccountByTypeAndFeatures extends MethodProxy {
      public String getMethodName() {
         return "getAccountByTypeAndFeatures";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String type = (String)args[0];
         String packageName = (String)args[1];
         return AccountManagerStub.Mgr.getAccounts(type);
      }
   }

   private static class getAccountsByTypeForPackage extends MethodProxy {
      private getAccountsByTypeForPackage() {
      }

      public String getMethodName() {
         return "getAccountsByTypeForPackage";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String type = (String)args[0];
         String packageName = (String)args[1];
         return AccountManagerStub.Mgr.getAccounts(type);
      }

      // $FF: synthetic method
      getAccountsByTypeForPackage(Object x0) {
         this();
      }
   }

   private static class getAccountsForPackage extends MethodProxy {
      private getAccountsForPackage() {
      }

      public String getMethodName() {
         return "getAccountsForPackage";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String packageName = (String)args[0];
         return AccountManagerStub.Mgr.getAccounts((String)null);
      }

      // $FF: synthetic method
      getAccountsForPackage(Object x0) {
         this();
      }
   }

   private static class getAccounts extends MethodProxy {
      private getAccounts() {
      }

      public String getMethodName() {
         return "getAccounts";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         String accountType = (String)args[0];
         return AccountManagerStub.Mgr.getAccounts(accountType);
      }

      // $FF: synthetic method
      getAccounts(Object x0) {
         this();
      }
   }

   private static class getAuthenticatorTypes extends MethodProxy {
      private getAuthenticatorTypes() {
      }

      public String getMethodName() {
         return "getAuthenticatorTypes";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return AccountManagerStub.Mgr.getAuthenticatorTypes(VUserHandle.myUserId());
      }

      // $FF: synthetic method
      getAuthenticatorTypes(Object x0) {
         this();
      }
   }

   private static class getUserData extends MethodProxy {
      private getUserData() {
      }

      public String getMethodName() {
         return "getUserData";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         String key = (String)args[1];
         return AccountManagerStub.Mgr.getUserData(account, key);
      }

      // $FF: synthetic method
      getUserData(Object x0) {
         this();
      }
   }

   private static class getPassword extends MethodProxy {
      private getPassword() {
      }

      public String getMethodName() {
         return "getPassword";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Account account = (Account)args[0];
         return AccountManagerStub.Mgr.getPassword(account);
      }

      // $FF: synthetic method
      getPassword(Object x0) {
         this();
      }
   }
}
