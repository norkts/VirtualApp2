package com.lody.virtual.client.ipc;

import android.accounts.Account;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountManagerResponse;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.stub.AmsTask;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.interfaces.IAccountManager;
import java.util.Map;

public class VAccountManager {
   private static VAccountManager sMgr = new VAccountManager();
   private IAccountManager mService;

   public static VAccountManager get() {
      return sMgr;
   }

   public IAccountManager getService() {
      if (!IInterfaceUtils.isAlive(this.mService)) {
         Class var1 = VAccountManager.class;
         synchronized(VAccountManager.class) {
            Object remote = this.getStubInterface();
            this.mService = (IAccountManager)LocalProxyUtils.genProxy(IAccountManager.class, remote);
         }
      }

      return this.mService;
   }

   private Object getStubInterface() {
      return IAccountManager.Stub.asInterface(ServiceManagerNative.getService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEVRF"))));
   }

   public AuthenticatorDescription[] getAuthenticatorTypes(int userId) {
      try {
         return this.getService().getAuthenticatorTypes(userId);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (AuthenticatorDescription[])VirtualRuntime.crash(e);
      }
   }

   public void removeAccount(IAccountManagerResponse response, Account account, boolean expectActivityLaunch) {
      try {
         this.getService().removeAccount(VUserHandle.myUserId(), response, account, expectActivityLaunch);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public void getAuthToken(IAccountManagerResponse response, Account account, String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, Bundle loginOptions) {
      try {
         this.getService().getAuthToken(VUserHandle.myUserId(), response, account, authTokenType, notifyOnAuthFailure, expectActivityLaunch, loginOptions);
      } catch (RemoteException var8) {
         RemoteException e = var8;
         e.printStackTrace();
      }

   }

   public boolean addAccountExplicitly(Account account, String password, Bundle extras) {
      try {
         return this.getService().addAccountExplicitly(VUserHandle.myUserId(), account, password, extras);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public Account[] getAccounts(int userId, String type) {
      try {
         return this.getService().getAccounts(userId, type);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Account[])VirtualRuntime.crash(e);
      }
   }

   public Account[] getAccounts(String type) {
      try {
         return this.getService().getAccounts(VUserHandle.myUserId(), type);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Account[])VirtualRuntime.crash(e);
      }
   }

   public String peekAuthToken(Account account, String authTokenType) {
      try {
         return this.getService().peekAuthToken(VUserHandle.myUserId(), account, authTokenType);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (String)VirtualRuntime.crash(e);
      }
   }

   public String getPreviousName(Account account) {
      try {
         return this.getService().getPreviousName(VUserHandle.myUserId(), account);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (String)VirtualRuntime.crash(e);
      }
   }

   public void hasFeatures(IAccountManagerResponse response, Account account, String[] features) {
      try {
         this.getService().hasFeatures(VUserHandle.myUserId(), response, account, features);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public boolean accountAuthenticated(Account account) {
      try {
         return this.getService().accountAuthenticated(VUserHandle.myUserId(), account);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public void clearPassword(Account account) {
      try {
         this.getService().clearPassword(VUserHandle.myUserId(), account);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         e.printStackTrace();
      }

   }

   public void renameAccount(IAccountManagerResponse response, Account accountToRename, String newName) {
      try {
         this.getService().renameAccount(VUserHandle.myUserId(), response, accountToRename, newName);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public void setPassword(Account account, String password) {
      try {
         this.getService().setPassword(VUserHandle.myUserId(), account, password);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
      }

   }

   public void addAccount(int userId, IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle optionsIn) {
      try {
         this.getService().addAccount(userId, response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, optionsIn);
      } catch (RemoteException var9) {
         RemoteException e = var9;
         e.printStackTrace();
      }

   }

   public void addAccount(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle optionsIn) {
      try {
         this.getService().addAccount(VUserHandle.myUserId(), response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, optionsIn);
      } catch (RemoteException var8) {
         RemoteException e = var8;
         e.printStackTrace();
      }

   }

   public void updateCredentials(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle loginOptions) {
      try {
         this.getService().updateCredentials(VUserHandle.myUserId(), response, account, authTokenType, expectActivityLaunch, loginOptions);
      } catch (RemoteException var7) {
         RemoteException e = var7;
         e.printStackTrace();
      }

   }

   public boolean removeAccountExplicitly(Account account) {
      try {
         return this.getService().removeAccountExplicitly(VUserHandle.myUserId(), account);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public void setUserData(Account account, String key, String value) {
      try {
         this.getService().setUserData(VUserHandle.myUserId(), account, key, value);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public void editProperties(IAccountManagerResponse response, String accountType, boolean expectActivityLaunch) {
      try {
         this.getService().editProperties(VUserHandle.myUserId(), response, accountType, expectActivityLaunch);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public void getAuthTokenLabel(IAccountManagerResponse response, String accountType, String authTokenType) {
      try {
         this.getService().getAuthTokenLabel(VUserHandle.myUserId(), response, accountType, authTokenType);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public void confirmCredentials(IAccountManagerResponse response, Account account, Bundle options, boolean expectActivityLaunch) {
      try {
         this.getService().confirmCredentials(VUserHandle.myUserId(), response, account, options, expectActivityLaunch);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         e.printStackTrace();
      }

   }

   public void invalidateAuthToken(String accountType, String authToken) {
      try {
         this.getService().invalidateAuthToken(VUserHandle.myUserId(), accountType, authToken);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
      }

   }

   public void getAccountsByFeatures(IAccountManagerResponse response, String type, String[] features) {
      try {
         this.getService().getAccountsByFeatures(VUserHandle.myUserId(), response, type, features);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public void setAuthToken(Account account, String authTokenType, String authToken) {
      try {
         this.getService().setAuthToken(VUserHandle.myUserId(), account, authTokenType, authToken);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         e.printStackTrace();
      }

   }

   public Object getPassword(Account account) {
      try {
         return this.getService().getPassword(VUserHandle.myUserId(), account);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return VirtualRuntime.crash(e);
      }
   }

   public String getUserData(Account account, String key) {
      try {
         return this.getService().getUserData(VUserHandle.myUserId(), account, key);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (String)VirtualRuntime.crash(e);
      }
   }

   public AccountManagerFuture<Bundle> addAccount(final int userId, final String accountType, final String authTokenType, final String[] requiredFeatures, Bundle addAccountOptions, final Activity activity, AccountManagerCallback<Bundle> callback, Handler handler) {
      if (accountType == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcLOmwgDShlNCgdLAhSVg==")));
      } else {
         final Bundle optionsIn = new Bundle();
         if (addAccountOptions != null) {
            optionsIn.putAll(addAccountOptions);
         }

         optionsIn.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iHyQ7Ly0EOWkFGgBoAQ4g")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iEVRF")));
         return (new AmsTask(activity, handler, callback) {
            public void doWork() throws RemoteException {
               VAccountManager.this.addAccount(userId, this.mResponse, accountType, authTokenType, requiredFeatures, activity != null, optionsIn);
            }
         }).start();
      }
   }

   public boolean setAccountVisibility(Account a, String packageName, int newVisibility) {
      try {
         return this.getService().setAccountVisibility(VUserHandle.myUserId(), a, packageName, newVisibility);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }

   public int getAccountVisibility(Account a, String packageName) {
      try {
         return this.getService().getAccountVisibility(VUserHandle.myUserId(), a, packageName);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) {
      try {
         this.getService().startAddAccountSession(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
      } catch (RemoteException var8) {
         RemoteException e = var8;
         VirtualRuntime.crash(e);
      }

   }

   public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) {
      try {
         this.getService().startUpdateCredentialsSession(response, account, authTokenType, expectActivityLaunch, options);
      } catch (RemoteException var7) {
         RemoteException e = var7;
         VirtualRuntime.crash(e);
      }

   }

   public void registerAccountListener(String[] accountTypes) {
      try {
         this.getService().registerAccountListener(accountTypes);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         VirtualRuntime.crash(e);
      }

   }

   public void unregisterAccountListener(String[] accountTypes) {
      try {
         this.getService().unregisterAccountListener(accountTypes);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         VirtualRuntime.crash(e);
      }

   }

   public Map getPackagesAndVisibilityForAccount(Account account) {
      try {
         return this.getService().getPackagesAndVisibilityForAccount(VUserHandle.myUserId(), account);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Map)VirtualRuntime.crash(e);
      }
   }

   public Map getAccountsAndVisibilityForPackage(String packageName, String accountType) {
      try {
         return this.getService().getAccountsAndVisibilityForPackage(VUserHandle.myUserId(), packageName, accountType);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         return (Map)VirtualRuntime.crash(e);
      }
   }

   public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) {
      try {
         this.getService().finishSessionAsUser(response, sessionBundle, expectActivityLaunch, appInfo, userId);
      } catch (RemoteException var7) {
         RemoteException e = var7;
         VirtualRuntime.crash(e);
      }

   }

   public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) {
      try {
         this.getService().isCredentialsUpdateSuggested(response, account, statusToken);
      } catch (RemoteException var5) {
         RemoteException e = var5;
         VirtualRuntime.crash(e);
      }

   }

   public boolean addAccountExplicitlyWithVisibility(Account account, String password, Bundle extras, Map visibility) {
      try {
         return this.getService().addAccountExplicitlyWithVisibility(VUserHandle.myUserId(), account, password, extras, visibility);
      } catch (RemoteException var6) {
         RemoteException e = var6;
         return (Boolean)VirtualRuntime.crash(e);
      }
   }
}
