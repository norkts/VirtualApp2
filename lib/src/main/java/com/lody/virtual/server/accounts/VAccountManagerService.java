package com.lody.virtual.server.accounts;

import android.accounts.Account;
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountAuthenticator;
import android.accounts.IAccountAuthenticatorResponse;
import android.accounts.IAccountManagerResponse;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VBinder;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.am.VActivityManagerService;
import com.lody.virtual.server.interfaces.IAccountManager;
import com.lody.virtual.server.pm.VPackageManagerService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import mirror.com.android.internal.R_Hide;

public class VAccountManagerService extends IAccountManager.Stub {
   private static final Singleton<VAccountManagerService> sInstance = new Singleton<VAccountManagerService>() {
      protected VAccountManagerService create() {
         return new VAccountManagerService();
      }
   };
   private static final long CHECK_IN_TIME = 43200000L;
   private static final String TAG = VAccountManagerService.class.getSimpleName();
   private final SparseArray<List<VAccount>> accountsByUserId = new SparseArray();
   private final SparseArray<List<VAccountVisibility>> accountsVisibilitiesByUserId = new SparseArray();
   private final LinkedList<AuthTokenRecord> authTokenRecords = new LinkedList();
   private final LinkedHashMap<String, Session> mSessions = new LinkedHashMap();
   private final AuthenticatorCache cache = new AuthenticatorCache();
   private Context mContext = VirtualCore.get().getContext();
   private long lastAccountChangeTime = 0L;

   public static VAccountManagerService get() {
      return (VAccountManagerService)sInstance.get();
   }

   public static void systemReady() {
      get().readAllAccounts();
      get().readAllAccountVisibilities();
   }

   private static AuthenticatorDescription parseAuthenticatorDescription(Resources resources, String packageName, AttributeSet attributeSet) {
      TypedArray array = resources.obtainAttributes(attributeSet, (int[])R_Hide.styleable.AccountAuthenticator.get());

      AuthenticatorDescription var10;
      try {
         String accountType = array.getString(R_Hide.styleable.AccountAuthenticator_accountType.get());
         int label = array.getResourceId(R_Hide.styleable.AccountAuthenticator_label.get(), 0);
         int icon = array.getResourceId(R_Hide.styleable.AccountAuthenticator_icon.get(), 0);
         int smallIcon = array.getResourceId(R_Hide.styleable.AccountAuthenticator_smallIcon.get(), 0);
         int accountPreferences = array.getResourceId(R_Hide.styleable.AccountAuthenticator_accountPreferences.get(), 0);
         boolean customTokens = array.getBoolean(R_Hide.styleable.AccountAuthenticator_customTokens.get(), false);
         if (!TextUtils.isEmpty(accountType)) {
            var10 = new AuthenticatorDescription(accountType, packageName, label, icon, smallIcon, accountPreferences, customTokens);
            return var10;
         }

         var10 = null;
      } finally {
         array.recycle();
      }

      return var10;
   }

   public AuthenticatorDescription[] getAuthenticatorTypes(int userId) {
      synchronized(this.cache) {
         AuthenticatorDescription[] descArray = new AuthenticatorDescription[this.cache.authenticators.size()];
         int i = 0;

         for(Iterator var5 = this.cache.authenticators.values().iterator(); var5.hasNext(); ++i) {
            AuthenticatorInfo info = (AuthenticatorInfo)var5.next();
            descArray[i] = info.desc;
         }

         return descArray;
      }
   }

   public void getAccountsByFeatures(int userId, IAccountManagerResponse response, String type, String[] features) {
      if (response == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKW8FGiZhJDM8KQgpOm8aGiRlEVRF")));
      } else if (type == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcLOmwgDShlNCgdLAhSVg==")));
      } else {
         AuthenticatorInfo info = this.getAuthenticatorInfo(type);
         Bundle bundle;
         RemoteException e;
         if (info == null) {
            bundle = new Bundle();
            bundle.putParcelableArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEShF")), new Account[0]);

            try {
               response.onResult(bundle);
            } catch (RemoteException var8) {
               e = var8;
               e.printStackTrace();
            }

         } else {
            if (features != null && features.length != 0) {
               (new GetAccountsByTypeAndFeatureSession(response, userId, info, features)).bind();
            } else {
               bundle = new Bundle();
               bundle.putParcelableArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEShF")), this.getAccounts(userId, type));

               try {
                  response.onResult(bundle);
               } catch (RemoteException var9) {
                  e = var9;
                  e.printStackTrace();
               }
            }

         }
      }
   }

   public final String getPreviousName(int userId, Account account) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         synchronized(this.accountsByUserId) {
            String previousName = null;
            VAccount vAccount = this.getAccount(userId, account);
            if (vAccount != null) {
               previousName = vAccount.previousName;
            }

            return previousName;
         }
      }
   }

   public Account[] getAccounts(int userId, String type) {
      List<Account> accountList = this.getAccountList(userId, type);
      return (Account[])accountList.toArray(new Account[accountList.size()]);
   }

   private List<Account> getAccountList(int userId, String type) {
      synchronized(this.accountsByUserId) {
         List<Account> accounts = new ArrayList();
         List<VAccount> vAccounts = (List)this.accountsByUserId.get(userId);
         if (vAccounts != null) {
            Iterator var6 = vAccounts.iterator();

            while(true) {
               VAccount vAccount;
               do {
                  if (!var6.hasNext()) {
                     return accounts;
                  }

                  vAccount = (VAccount)var6.next();
               } while(type != null && !vAccount.type.equals(type));

               accounts.add(new Account(vAccount.name, vAccount.type));
            }
         } else {
            return accounts;
         }
      }
   }

   public final void getAuthToken(final int userId, IAccountManagerResponse response, final Account account, final String authTokenType, final boolean notifyOnAuthFailure, boolean expectActivityLaunch, final Bundle loginOptions) {
      if (response == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKW8FGiZhJDM8KQgpOm8aGiRlEVRF")));
      } else {
         try {
            if (account == null) {
               VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaNAZjHwo1KS0MDn4zAjdlEQIgLgQ6MWMFFit5Hh4+KT4hJGgFLDVqNCg5IBhSVg==")));
               response.onError(7, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
               return;
            }

            if (authTokenType == null) {
               VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMaNAZjHwo1KS0MDn4zAjdlEQIgLgQ6MWMFFit5Hh4+KT4hJGgKNCBlDCw6JS02JWIVBiZpJ1RF")));
               response.onError(7, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUIMCVjJDA2JBgcKmknTS9sIzwbKhgEKA==")));
               return;
            }
         } catch (RemoteException var16) {
            RemoteException e = var16;
            e.printStackTrace();
            return;
         }

         AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
         if (info == null) {
            try {
               response.onError(7, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV1kgLQgmPX4zBiVrDjMrLC4ACksaLAVvASw9")));
            } catch (RemoteException var14) {
               RemoteException e = var14;
               e.printStackTrace();
            }

         } else {
            final String callerPkg = loginOptions.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iHyQ7Ly0EOWkFGgBoAQ4g")));
            final boolean customTokens = info.desc.customTokens;
            loginOptions.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+DmoFNARuDgYw")), VBinder.getCallingUid());
            loginOptions.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+DmoFNARpHgYw")), VBinder.getCallingPid());
            if (notifyOnAuthFailure) {
               loginOptions.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD9oJFkRLAg2MmEVQS9lHig5LhhSVg==")), true);
            }

            if (!customTokens) {
               VAccount vAccount;
               synchronized(this.accountsByUserId) {
                  vAccount = this.getAccount(userId, account);
               }

               String authToken = vAccount != null ? (String)vAccount.authTokens.get(authTokenType) : null;
               if (authToken != null) {
                  Bundle result = new Bundle();
                  result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUKMCVjJDA2")), authToken);
                  result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGULJCl9JB4vKj42Vg==")), account.name);
                  result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcMVg==")), account.type);
                  this.onResult(response, result);
                  return;
               }
            }

            if (customTokens) {
               String authToken = this.getCustomAuthToken(userId, account, authTokenType, callerPkg);
               if (authToken != null) {
                  Bundle result = new Bundle();
                  result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUKMCVjJDA2")), authToken);
                  result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGULJCl9JB4vKj42Vg==")), account.name);
                  result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcMVg==")), account.type);
                  this.onResult(response, result);
                  return;
               }
            }

            (new Session(response, userId, info, expectActivityLaunch, false, account.name) {
               protected String toDebugString(long now) {
                  return super.toDebugString(now) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186PWgaMBFmAQo0JBdfCWkjMyR4EVRF")) + account + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186P2waMCBuHh4xKAcYAGggTSt4EVRF")) + authTokenType + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186DmozPC9gMh4sLBccDW8aDSg=")) + loginOptions + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186CGowMC9iNwYLKjsiLWUzFghoAR4dKhcMJ0sVSFo=")) + notifyOnAuthFailure;
               }

               public void run() throws RemoteException {
                  this.mAuthenticator.getAuthToken(this, account, authTokenType, loginOptions);
               }

               public void onResult(Bundle result) throws RemoteException {
                  if (result != null) {
                     String authToken = result.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUKMCVjJDA2")));
                     if (authToken != null) {
                        String name = result.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGULJCl9JB4vKj42Vg==")));
                        String type = result.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcMVg==")));
                        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(name)) {
                           this.onError(5, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRhfM3sKMD9hHjM8LwcYPn4zMDdlASsrKT5fKWYKTS95Hh4eIzpXJmsJID9qHjwZIQhSVg==")));
                           return;
                        }

                        if (!customTokens) {
                           synchronized(VAccountManagerService.this.accountsByUserId) {
                              VAccount accountx = VAccountManagerService.this.getAccount(userId, name, type);
                              if (accountx == null) {
                                 List<VAccount> accounts = (List)VAccountManagerService.this.accountsByUserId.get(userId);
                                 if (accounts == null) {
                                    accounts = new ArrayList();
                                    VAccountManagerService.this.accountsByUserId.put(userId, accounts);
                                 }

                                 accountx = new VAccount(userId, new Account(name, type));
                                 ((List)accounts).add(accountx);
                                 VAccountManagerService.this.saveAllAccounts();
                              }
                           }
                        }

                        long expiryMillis = result.getLong(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7Ly0qDWUjMAZsIxogLwc6I2EzGlo=")), 0L);
                        if (customTokens && expiryMillis > System.currentTimeMillis()) {
                           AuthTokenRecord record = new AuthTokenRecord(userId, account, authTokenType, callerPkg, authToken, expiryMillis);
                           synchronized(VAccountManagerService.this.authTokenRecords) {
                              VAccountManagerService.this.authTokenRecords.remove(record);
                              VAccountManagerService.this.authTokenRecords.add(record);
                           }
                        }
                     }

                     Intent intent = (Intent)result.getParcelable(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVBgY=")));
                     if (intent != null && notifyOnAuthFailure && !customTokens) {
                     }
                  }

                  super.onResult(result);
               }
            }).bind();
         }
      }
   }

   public void setPassword(int userId, Account account, String password) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         this.setPasswordInternal(userId, account, password);
      }
   }

   private void setPasswordInternal(int userId, Account account, String password) {
      synchronized(this.accountsByUserId) {
         VAccount vAccount = this.getAccount(userId, account);
         if (vAccount != null) {
            vAccount.password = password;
            vAccount.authTokens.clear();
            this.saveAllAccounts();
            synchronized(this.authTokenRecords) {
               Iterator<AuthTokenRecord> iterator = this.authTokenRecords.iterator();

               while(iterator.hasNext()) {
                  AuthTokenRecord record = (AuthTokenRecord)iterator.next();
                  if (record.userId == userId && record.account.equals(account)) {
                     iterator.remove();
                  }
               }
            }

            this.sendAccountsChangedBroadcast(userId);
         }

      }
   }

   public void setAuthToken(int userId, Account account, String authTokenType, String authToken) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else if (authTokenType == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUIMCVjJDA2JBgcKmknTS9sIzwbKhgEKA==")));
      } else {
         synchronized(this.accountsByUserId) {
            VAccount vAccount = this.getAccount(userId, account);
            if (vAccount != null) {
               vAccount.authTokens.put(authTokenType, authToken);
               this.saveAllAccounts();
            }

         }
      }
   }

   public void setUserData(int userId, Account account, String key, String value) {
      if (key == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4uJ3sFAgNLHlkvKhdbVg==")));
      } else if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         VAccount vAccount = this.getAccount(userId, account);
         if (vAccount != null) {
            synchronized(this.accountsByUserId) {
               vAccount.userDatas.put(key, value);
               this.saveAllAccounts();
            }
         }

      }
   }

   public void hasFeatures(int userId, IAccountManagerResponse response, final Account account, final String[] features) {
      if (response == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKW8FGiZhJDM8KQgpOm8aGiRlEVRF")));
      } else if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else if (features == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4uP2wKNARiASs8KQgpOm8aGiRlEVRF")));
      } else {
         AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
         if (info == null) {
            try {
               response.onError(7, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV1kgLQgmPX4zBiVrDjMrLC4ACksaLAVvASw9")));
            } catch (RemoteException var7) {
               RemoteException e = var7;
               e.printStackTrace();
            }

         } else {
            (new Session(response, userId, info, false, true, account.name) {
               public void run() throws RemoteException {
                  try {
                     this.mAuthenticator.hasFeatures(this, account, features);
                  } catch (RemoteException var2) {
                     this.onError(1, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowMCtLHjAaLy0MKmUzLCVlN1RF")));
                  }

               }

               public void onResult(Bundle result) throws RemoteException {
                  IAccountManagerResponse response = this.getResponseAndClose();
                  if (response != null) {
                     try {
                        if (result == null) {
                           response.onError(5, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoJICpmDlkwKhcMVg==")));
                           return;
                        }

                        Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg2P2oFHi9gNDs8Ki0YAmkgAgVlHi8ZM186KWA0ODVuASw5KQgqD2sJIFo=")) + response);
                        Bundle newResult = new Bundle();
                        newResult.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgNSw/Iy4MCGUzSFo=")), result.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgNSw/Iy4MCGUzSFo=")), false));
                        response.onResult(newResult);
                     } catch (RemoteException var4) {
                        RemoteException e = var4;
                        Log.v(VAccountManagerService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoKNARiCiQtKRccCGknTSZlJCwaLi0YI2AwJyNlNAo8LD4uKmwjNFo=")), e);
                     }
                  }

               }
            }).bind();
         }
      }
   }

   public void updateCredentials(int userId, IAccountManagerResponse response, final Account account, final String authTokenType, boolean expectActivityLaunch, final Bundle loginOptions) {
      if (response == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKW8FGiZhJDM8KQgpOm8aGiRlEVRF")));
      } else if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else if (authTokenType == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUIMCVjJDA2JBgcKmknTS9sIzwbKhgEKA==")));
      } else {
         AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
         if (info == null) {
            try {
               response.onError(7, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV1kgLQgmPX4zBiVrDjMrLC4ACksaLAVvASw9")));
            } catch (RemoteException var9) {
               RemoteException e = var9;
               e.printStackTrace();
            }

         } else {
            (new Session(response, userId, info, expectActivityLaunch, false, account.name) {
               public void run() throws RemoteException {
                  this.mAuthenticator.updateCredentials(this, account, authTokenType, loginOptions);
               }

               protected String toDebugString(long now) {
                  if (loginOptions != null) {
                     loginOptions.keySet();
                  }

                  return super.toDebugString(now) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186I28FMDdmHjAfIz0MPmkjMAZqATgdKToDJA==")) + account + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186P2waMCBuHh4xKAcYAGggTSt4EVRF")) + authTokenType + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186DmozPC9gMh4sLBccDW8aDSg=")) + loginOptions;
               }
            }).bind();
         }
      }
   }

   public String getPassword(int userId, Account account) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         synchronized(this.accountsByUserId) {
            VAccount vAccount = this.getAccount(userId, account);
            return vAccount != null ? vAccount.password : null;
         }
      }
   }

   public String getUserData(int userId, Account account, String key) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else if (key == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4uJ3sFAgNLHlkvKhdbVg==")));
      } else {
         synchronized(this.accountsByUserId) {
            VAccount vAccount = this.getAccount(userId, account);
            return vAccount != null ? (String)vAccount.userDatas.get(key) : null;
         }
      }
   }

   public void editProperties(int userId, IAccountManagerResponse response, final String accountType, boolean expectActivityLaunch) {
      if (response == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKW8FGiZhJDM8KQgpOm8aGiRlEVRF")));
      } else if (accountType == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcLOmwgDShlNCgdLAhSVg==")));
      } else {
         AuthenticatorInfo info = this.getAuthenticatorInfo(accountType);
         if (info == null) {
            try {
               response.onError(7, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV1kgLQgmPX4zBiVrDjMrLC4ACksaLAVvASw9")));
            } catch (RemoteException var7) {
               RemoteException e = var7;
               e.printStackTrace();
            }

         } else {
            (new Session(response, userId, info, expectActivityLaunch, true, (String)null) {
               public void run() throws RemoteException {
                  this.mAuthenticator.editProperties(this, this.mAuthenticatorInfo.desc.type);
               }

               protected String toDebugString(long now) {
                  return super.toDebugString(now) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186M2gFAgZpESw1IxcMKGUzLCtsIwUrLRg2JWAjLClqHzAyLD4fJA==")) + accountType;
               }
            }).bind();
         }
      }
   }

   public void getAuthTokenLabel(int userId, IAccountManagerResponse response, String accountType, final String authTokenType) {
      if (accountType == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcLOmwgDShlNCgdLAhSVg==")));
      } else if (authTokenType == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUIMCVjJDA2JBgcKmknTS9sIzwbKhgEKA==")));
      } else {
         AuthenticatorInfo info = this.getAuthenticatorInfo(accountType);
         if (info == null) {
            try {
               response.onError(7, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV1kgLQgmPX4zBiVrDjMrLC4ACksaLAVvASw9")));
            } catch (RemoteException var7) {
               RemoteException e = var7;
               e.printStackTrace();
            }

         } else {
            (new Session(response, userId, info, false, false, (String)null) {
               public void run() throws RemoteException {
                  this.mAuthenticator.getAuthTokenLabel(this, authTokenType);
               }

               public void onResult(Bundle result) throws RemoteException {
                  if (result != null) {
                     String label = result.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUIMCVjJDA2IhciOGkjOAtrDh5F")));
                     Bundle bundle = new Bundle();
                     bundle.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUIMCVjJDA2IhciOGkjOAtrDh5F")), label);
                     super.onResult(bundle);
                  } else {
                     super.onResult((Bundle)null);
                  }

               }
            }).bind();
         }
      }
   }

   public void confirmCredentials(int userId, IAccountManagerResponse response, final Account account, final Bundle options, boolean expectActivityLaunch) {
      if (response == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKW8FGiZhJDM8KQgpOm8aGiRlEVRF")));
      } else if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
         if (info == null) {
            try {
               response.onError(7, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV1kgLQgmPX4zBiVrDjMrLC4ACksaLAVvASw9")));
            } catch (RemoteException var8) {
               RemoteException e = var8;
               e.printStackTrace();
            }

         } else {
            (new Session(response, userId, info, expectActivityLaunch, true, account.name, true, true) {
               public void run() throws RemoteException {
                  this.mAuthenticator.confirmCredentials(this, account, options);
               }
            }).bind();
         }
      }
   }

   public void addAccount(int userId, IAccountManagerResponse response, final String accountType, final String authTokenType, final String[] requiredFeatures, boolean expectActivityLaunch, final Bundle optionsIn) {
      if (response == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKW8FGiZhJDM8KQgpOm8aGiRlEVRF")));
      } else if (accountType == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcLOmwgDShlNCgdLAhSVg==")));
      } else {
         AuthenticatorInfo info = this.getAuthenticatorInfo(accountType);
         if (info == null) {
            try {
               Bundle result = new Bundle();
               result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUKMCVjJDA2")), authTokenType);
               result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcMVg==")), accountType);
               result.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgNSw/Iy4MCGUzSFo=")), false);
               response.onResult(result);
            } catch (RemoteException var10) {
               RemoteException e = var10;
               e.printStackTrace();
            }

         } else {
            (new Session(response, userId, info, expectActivityLaunch, true, (String)null, false, true) {
               public void run() throws RemoteException {
                  this.mAuthenticator.addAccount(this, this.mAuthenticatorInfo.desc.type, authTokenType, requiredFeatures, optionsIn);
               }

               protected String toDebugString(long now) {
                  return super.toDebugString(now) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186P2gFMBF9JCg1LAcYLn83TTdoJzAcKhgcCm4VGjNuCiBF")) + accountType + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KmgaJAVjASw/KBU+PW4gBgVsNyg6PQhSVg==")) + (requiredFeatures != null ? TextUtils.join(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MxhSVg==")), requiredFeatures) : null);
               }
            }).bind();
         }
      }
   }

   public boolean addAccountExplicitly(int userId, Account account, String password, Bundle extras) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         return this.insertAccountIntoDatabase(userId, account, password, extras);
      }
   }

   public boolean removeAccountExplicitly(int userId, Account account) {
      return account != null && this.removeAccountInternal(userId, account);
   }

   public void renameAccount(int userId, IAccountManagerResponse response, Account accountToRename, String newName) {
      if (accountToRename == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         Account resultingAccount = this.renameAccountInternal(userId, accountToRename, newName);
         Bundle result = new Bundle();
         result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGULJCl9JB4vKj42Vg==")), resultingAccount.name);
         result.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcMVg==")), resultingAccount.type);

         try {
            response.onResult(result);
         } catch (RemoteException var8) {
            RemoteException e = var8;
            Log.w(TAG, e.getMessage());
         }

      }
   }

   public void removeAccount(final int userId, IAccountManagerResponse response, final Account account, boolean expectActivityLaunch) {
      if (response == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKW8FGiZhJDM8KQgpOm8aGiRlEVRF")));
      } else if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         AuthenticatorInfo info = this.getAuthenticatorInfo(account.type);
         if (info == null) {
            try {
               response.onError(7, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV1kgLQgmPX4zBiVrDjMrLC4ACksaLAVvASw9")));
            } catch (RemoteException var7) {
               RemoteException e = var7;
               e.printStackTrace();
            }

         } else {
            (new Session(response, userId, info, expectActivityLaunch, true, account.name) {
               protected String toDebugString(long now) {
                  return super.toDebugString(now) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KmgVEiVmNDARLy0qDWUjMAZ1VjwsLT42KWYKRT95EVRF")) + account;
               }

               public void run() throws RemoteException {
                  this.mAuthenticator.getAccountRemovalAllowed(this, account);
               }

               public void onResult(Bundle result) throws RemoteException {
                  if (result != null && result.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgNSw/Iy4MCGUzSFo="))) && !result.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVBgY=")))) {
                     boolean removalAllowed = result.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgNSw/Iy4MCGUzSFo=")));
                     if (removalAllowed) {
                        VAccountManagerService.this.removeAccountInternal(userId, account);
                     }

                     IAccountManagerResponse response = this.getResponseAndClose();
                     if (response != null) {
                        Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg2P2oFHi9gNDs8Ki0YAmkgAgVlHi8ZM186KWA0ODVuASw5KQgqD2sJIFo=")) + response);
                        Bundle result2 = new Bundle();
                        result2.putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgNSw/Iy4MCGUzSFo=")), removalAllowed);

                        try {
                           response.onResult(result2);
                        } catch (RemoteException var6) {
                           RemoteException e = var6;
                           e.printStackTrace();
                        }
                     }
                  }

                  super.onResult(result);
               }
            }).bind();
         }
      }
   }

   public void clearPassword(int userId, Account account) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         this.setPasswordInternal(userId, account, (String)null);
      }
   }

   private boolean removeAccountInternal(int userId, Account account) {
      List<VAccount> accounts = (List)this.accountsByUserId.get(userId);
      if (accounts != null) {
         Iterator<VAccount> iterator = accounts.iterator();

         while(iterator.hasNext()) {
            VAccount vAccount = (VAccount)iterator.next();
            if (userId == vAccount.userId && TextUtils.equals(vAccount.name, account.name) && TextUtils.equals(account.type, vAccount.type)) {
               iterator.remove();
               this.saveAllAccounts();
               this.sendAccountsChangedBroadcast(userId);
               return true;
            }
         }
      }

      return false;
   }

   public boolean accountAuthenticated(int userId, Account account) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         synchronized(this.accountsByUserId) {
            VAccount vAccount = this.getAccount(userId, account);
            if (vAccount != null) {
               vAccount.lastAuthenticatedTime = System.currentTimeMillis();
               this.saveAllAccounts();
               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void invalidateAuthToken(int userId, String accountType, String authToken) {
      if (accountType == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcLOmwgDShlNCgdLAhSVg==")));
      } else if (authToken == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUIMCVjJDA2PxccL34zMAVlEQJF")));
      } else {
         synchronized(this.accountsByUserId) {
            List<VAccount> accounts = (List)this.accountsByUserId.get(userId);
            Iterator iterator;
            if (accounts != null) {
               boolean changed = false;
               iterator = accounts.iterator();

               while(iterator.hasNext()) {
                  VAccount account = (VAccount)iterator.next();
                  if (account.type.equals(accountType)) {
                     account.authTokens.values().remove(authToken);
                     changed = true;
                  }
               }

               if (changed) {
                  this.saveAllAccounts();
               }
            }

            synchronized(this.authTokenRecords) {
               iterator = this.authTokenRecords.iterator();

               while(iterator.hasNext()) {
                  AuthTokenRecord record = (AuthTokenRecord)iterator.next();
                  if (record.userId == userId && record.authTokenType.equals(accountType) && record.authToken.equals(authToken)) {
                     iterator.remove();
                  }
               }
            }

         }
      }
   }

   private Account renameAccountInternal(int userId, Account accountToRename, String newName) {
      synchronized(this.accountsByUserId) {
         VAccount vAccount = this.getAccount(userId, accountToRename);
         if (vAccount != null) {
            vAccount.previousName = vAccount.name;
            vAccount.name = newName;
            this.saveAllAccounts();
            Account newAccount = new Account(vAccount.name, vAccount.type);
            synchronized(this.authTokenRecords) {
               Iterator var8 = this.authTokenRecords.iterator();

               while(var8.hasNext()) {
                  AuthTokenRecord record = (AuthTokenRecord)var8.next();
                  if (record.userId == userId && record.account.equals(accountToRename)) {
                     record.account = newAccount;
                  }
               }
            }

            this.sendAccountsChangedBroadcast(userId);
            return newAccount;
         } else {
            return accountToRename;
         }
      }
   }

   public String peekAuthToken(int userId, Account account, String authTokenType) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else if (authTokenType == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUIMCVjJDA2JBgcKmknTS9sIzwbKhgEKA==")));
      } else {
         synchronized(this.accountsByUserId) {
            VAccount vAccount = this.getAccount(userId, account);
            return vAccount != null ? (String)vAccount.authTokens.get(authTokenType) : null;
         }
      }
   }

   private String getCustomAuthToken(int userId, Account account, String authTokenType, String packageName) {
      AuthTokenRecord record = new AuthTokenRecord(userId, account, authTokenType, packageName);
      String authToken = null;
      long now = System.currentTimeMillis();
      synchronized(this.authTokenRecords) {
         Iterator<AuthTokenRecord> iterator = this.authTokenRecords.iterator();

         while(true) {
            while(iterator.hasNext()) {
               AuthTokenRecord one = (AuthTokenRecord)iterator.next();
               if (one.expiryEpochMillis > 0L && one.expiryEpochMillis < now) {
                  iterator.remove();
               } else if (record.equals(one)) {
                  authToken = record.authToken;
               }
            }

            return authToken;
         }
      }
   }

   private void onResult(IAccountManagerResponse response, Bundle result) {
      try {
         response.onResult(result);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
      }

   }

   private AuthenticatorInfo getAuthenticatorInfo(String type) {
      synchronized(this.cache) {
         return type == null ? null : (AuthenticatorInfo)this.cache.authenticators.get(type);
      }
   }

   private VAccount getAccount(int userId, Account account) {
      return this.getAccount(userId, account.name, account.type);
   }

   private boolean insertAccountIntoDatabase(int userId, Account account, String password, Bundle extras) {
      if (account == null) {
         return false;
      } else {
         synchronized(this.accountsByUserId) {
            if (this.getAccount(userId, account.name, account.type) != null) {
               return false;
            } else {
               VAccount vAccount = new VAccount(userId, account);
               vAccount.password = password;
               if (extras != null) {
                  Iterator var7 = extras.keySet().iterator();

                  while(var7.hasNext()) {
                     String key = (String)var7.next();
                     Object value = extras.get(key);
                     if (value instanceof String) {
                        vAccount.userDatas.put(key, (String)value);
                     }
                  }
               }

               List<VAccount> accounts = (List)this.accountsByUserId.get(userId);
               if (accounts == null) {
                  accounts = new ArrayList();
                  this.accountsByUserId.put(userId, accounts);
               }

               ((List)accounts).add(vAccount);
               this.saveAllAccounts();
               this.sendAccountsChangedBroadcast(vAccount.userId);
               return true;
            }
         }
      }
   }

   private void sendAccountsChangedBroadcast(int userId) {
      Intent loginChangeIntent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7Ly0qDWUjMAZsIxpTJDwmBWgxAg5hIixXOywqXWQmGh99DzgfLCs2BQ==")));
      VActivityManagerService.get().sendBroadcastAsUser(loginChangeIntent, new VUserHandle(userId));
      Intent accountsChangeIntent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7Ly0qDWUjMAZsIxosLT0qI2AgRCliMhoSIixfAmMIGh1jNTAQKAVfWH0hRR1kNV0fJSwuWQ==")));
      VActivityManagerService.get().sendBroadcastAsUser(accountsChangeIntent, new VUserHandle(userId));
      this.broadcastCheckInNowIfNeed(userId);
   }

   private void broadcastCheckInNowIfNeed(int userId) {
      long time = System.currentTimeMillis();
      if (Math.abs(time - this.lastAccountChangeTime) > 43200000L) {
         this.lastAccountChangeTime = time;
         this.saveAllAccounts();
         Intent intent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kpKAguLGkgRCZoJ1kgLT5bI2A0RQBnHAoCIgY2DGEhBl5kN1RF")));
         VActivityManagerService.get().sendBroadcastAsUser(intent, new VUserHandle(userId));
      }

   }

   private void saveAllAccounts() {
      File accountFile = VEnvironment.getAccountConfigFile();
      Parcel dest = Parcel.obtain();

      try {
         dest.writeInt(1);
         List<VAccount> accounts = new ArrayList();

         for(int i = 0; i < this.accountsByUserId.size(); ++i) {
            List<VAccount> list = (List)this.accountsByUserId.valueAt(i);
            if (list != null) {
               accounts.addAll(list);
            }
         }

         dest.writeInt(accounts.size());
         Iterator var8 = accounts.iterator();

         while(var8.hasNext()) {
            VAccount account = (VAccount)var8.next();
            account.writeToParcel(dest, 0);
         }

         dest.writeLong(this.lastAccountChangeTime);
         FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
         fileOutputStream.write(dest.marshall());
         fileOutputStream.close();
      } catch (Exception var6) {
         Exception e = var6;
         e.printStackTrace();
      }

      dest.recycle();
   }

   private void readAllAccounts() {
      File accountFile = VEnvironment.getAccountConfigFile();
      this.refreshAuthenticatorCache((String)null);
      if (accountFile.exists()) {
         this.accountsByUserId.clear();
         Parcel dest = Parcel.obtain();

         try {
            FileInputStream is = new FileInputStream(accountFile);
            byte[] bytes = new byte[(int)accountFile.length()];
            int readLength = is.read(bytes);
            is.close();
            if (readLength != bytes.length) {
               throw new IOException(String.format(Locale.ENGLISH, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQdfKGgVLAZLHlE/Kj06Lmw3TCtrVgUrLS0uCksaJCpqVyMuLzoqVg==")), bytes.length, readLength));
            }

            dest.unmarshall(bytes, 0, bytes.length);
            dest.setDataPosition(0);
            dest.readInt();

            VAccount account;
            List<VAccount> accounts;
            for(int size = dest.readInt(); size-- > 0; ((List)accounts).add(account)) {
               account = new VAccount(dest);
               VLog.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij4uP2gFAiZiICQ7Ly0qDWUjMAZ4V1Ar")) + account.type);
               accounts = (List)this.accountsByUserId.get(account.userId);
               if (accounts == null) {
                  accounts = new ArrayList();
                  this.accountsByUserId.put(account.userId, accounts);
               }
            }

            this.lastAccountChangeTime = dest.readLong();
         } catch (Exception var12) {
            Exception e = var12;
            e.printStackTrace();
         } finally {
            dest.recycle();
         }
      }

   }

   private VAccount getAccount(int userId, String accountName, String accountType) {
      List<VAccount> accounts = (List)this.accountsByUserId.get(userId);
      if (accounts != null) {
         Iterator var5 = accounts.iterator();

         while(var5.hasNext()) {
            VAccount account = (VAccount)var5.next();
            if (TextUtils.equals(account.name, accountName) && TextUtils.equals(account.type, accountType)) {
               return account;
            }
         }
      }

      return null;
   }

   public void refreshAuthenticatorCache(String packageName) {
      this.cache.authenticators.clear();
      Intent intent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7Ly0qDWUjMAZsIxoCLT42KWYKRT9hAQo9Kj4AKm8VAjVrHiw6Jz5SVg==")));
      if (packageName != null) {
         intent.setPackage(packageName);
      }

      this.generateServicesMap(VPackageManagerService.get().queryIntentServices(intent, (String)null, 128, 0), this.cache.authenticators, new RegisteredServicesParser());
   }

   private void generateServicesMap(List<ResolveInfo> services, Map<String, AuthenticatorInfo> map, RegisteredServicesParser accountParser) {
      Iterator var4 = services.iterator();

      while(true) {
         ResolveInfo info;
         XmlResourceParser parser;
         do {
            if (!var4.hasNext()) {
               return;
            }

            info = (ResolveInfo)var4.next();
            parser = accountParser.getParser(this.mContext, info.serviceInfo, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7Ly0qDWUjMAZsIxoCLT42KWYKRT9hAQo9Kj4AKm8VAjVrHiw6Jz5SVg==")));
         } while(parser == null);

         try {
            AttributeSet attributeSet = Xml.asAttributeSet(parser);

            int type;
            while((type = parser.next()) != 1 && type != 2) {
            }

            if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmV107LAg2MmkjMAZqATAsKggACA==")).equals(parser.getName())) {
               AuthenticatorDescription desc = parseAuthenticatorDescription(accountParser.getResources(this.mContext, info.serviceInfo.applicationInfo), info.serviceInfo.packageName, attributeSet);
               if (desc != null) {
                  map.put(desc.type, new AuthenticatorInfo(desc, info.serviceInfo));
               }
            }
         } catch (Exception var10) {
            Exception e = var10;
            e.printStackTrace();
         }
      }
   }

   @TargetApi(26)
   private boolean removeAccountVisibility(int userId, Account account) {
      List<VAccountVisibility> list = (List)this.accountsVisibilitiesByUserId.get(userId);
      if (list != null) {
         Iterator<VAccountVisibility> it = list.iterator();

         while(it.hasNext()) {
            VAccountVisibility vAccountVisibility = (VAccountVisibility)it.next();
            if (userId == vAccountVisibility.userId && TextUtils.equals(vAccountVisibility.name, account.name) && TextUtils.equals(account.type, vAccountVisibility.type)) {
               it.remove();
               this.saveAllAccountVisibilities();
               this.sendAccountsChangedBroadcast(userId);
               return true;
            }
         }
      }

      return false;
   }

   @TargetApi(26)
   private boolean renameAccountVisibility(int userId, Account account, String name) {
      synchronized(this.accountsVisibilitiesByUserId) {
         VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
         if (accountVisibility != null) {
            accountVisibility.name = name;
            this.saveAllAccountVisibilities();
            this.sendAccountsChangedBroadcast(userId);
            return true;
         } else {
            return false;
         }
      }
   }

   @TargetApi(26)
   public Map<String, Integer> getPackagesAndVisibilityForAccount(int userId, Account account) {
      VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
      return accountVisibility != null ? accountVisibility.visibility : null;
   }

   @TargetApi(26)
   public boolean addAccountExplicitlyWithVisibility(int userId, Account account, String password, Bundle extras, Map visibility) {
      if (account == null) {
         throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmVyQzIykmDmUjOCQ=")));
      } else {
         boolean insertAccountIntoDatabase = this.insertAccountIntoDatabase(userId, account, password, extras);
         this.insertAccountVisibilityIntoDatabase(userId, account, visibility);
         return insertAccountIntoDatabase;
      }
   }

   @TargetApi(26)
   public boolean setAccountVisibility(int userId, Account account, String packageName, int newVisibility) {
      VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
      if (accountVisibility == null) {
         return false;
      } else {
         accountVisibility.visibility.put(packageName, newVisibility);
         this.saveAllAccountVisibilities();
         this.sendAccountsChangedBroadcast(userId);
         return true;
      }
   }

   @TargetApi(26)
   public int getAccountVisibility(int userId, Account account, String packageName) {
      VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
      return accountVisibility != null && accountVisibility.visibility.containsKey(packageName) ? (Integer)accountVisibility.visibility.get(packageName) : 0;
   }

   @TargetApi(26)
   public Map<Account, Integer> getAccountsAndVisibilityForPackage(int userId, String packageName, String accountType) {
      Map<Account, Integer> hashMap = new HashMap();
      Iterator var5 = this.getAccountList(userId, accountType).iterator();

      while(var5.hasNext()) {
         Account account = (Account)var5.next();
         VAccountVisibility accountVisibility = this.getAccountVisibility(userId, account);
         if (accountVisibility != null && accountVisibility.visibility.containsKey(packageName)) {
            hashMap.put(account, (Integer)accountVisibility.visibility.get(packageName));
         }
      }

      return hashMap;
   }

   @TargetApi(26)
   private boolean insertAccountVisibilityIntoDatabase(int userId, Account account, Map<String, Integer> map) {
      if (account == null) {
         return false;
      } else {
         synchronized(this.accountsVisibilitiesByUserId) {
            VAccountVisibility vAccountVisibility = new VAccountVisibility(userId, account, map);
            List<VAccountVisibility> list = (List)this.accountsVisibilitiesByUserId.get(userId);
            if (list == null) {
               list = new ArrayList();
               this.accountsVisibilitiesByUserId.put(userId, list);
            }

            ((List)list).add(vAccountVisibility);
            this.saveAllAccountVisibilities();
            this.sendAccountsChangedBroadcast(vAccountVisibility.userId);
            return true;
         }
      }
   }

   @TargetApi(26)
   private void saveAllAccountVisibilities() {
      File accountVisibilityConfigFile = VEnvironment.getAccountVisibilityConfigFile();
      Parcel obtain = Parcel.obtain();

      try {
         obtain.writeInt(1);
         obtain.writeInt(this.accountsVisibilitiesByUserId.size());

         for(int i = 0; i < this.accountsVisibilitiesByUserId.size(); ++i) {
            obtain.writeInt(i);
            List<VAccountVisibility> list = (List)this.accountsVisibilitiesByUserId.valueAt(i);
            if (list == null) {
               obtain.writeInt(0);
            } else {
               obtain.writeInt(list.size());
               Iterator var5 = list.iterator();

               while(var5.hasNext()) {
                  VAccountVisibility writeToParcel = (VAccountVisibility)var5.next();
                  writeToParcel.writeToParcel(obtain, 0);
               }
            }
         }

         obtain.writeLong(this.lastAccountChangeTime);
         FileOutputStream fileOutputStream = new FileOutputStream(accountVisibilityConfigFile);
         fileOutputStream.write(obtain.marshall());
         fileOutputStream.close();
      } catch (Exception var7) {
         Exception e = var7;
         e.printStackTrace();
      }

      obtain.recycle();
   }

   @TargetApi(26)
   private void readAllAccountVisibilities() {
      File accountVisibilityConfigFile = VEnvironment.getAccountVisibilityConfigFile();
      Parcel dest = Parcel.obtain();
      if (accountVisibilityConfigFile.exists()) {
         try {
            FileInputStream is = new FileInputStream(accountVisibilityConfigFile);
            byte[] bytes = new byte[(int)accountVisibilityConfigFile.length()];
            int readLength = is.read(bytes);
            is.close();
            if (readLength != bytes.length) {
               throw new IOException(String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQdfKGgVLAZLHlE/Kj06Lmw3TCtrVgUrLS0uCksaJCpqVyMuLzoqVg==")), bytes.length, readLength));
            }

            dest.unmarshall(bytes, 0, bytes.length);
            dest.setDataPosition(0);
            int version = dest.readInt();
            int userCount = dest.readInt();

            for(int i = 0; i < userCount; ++i) {
               int userId = dest.readInt();
               int count = dest.readInt();
               List<VAccountVisibility> list = new ArrayList();
               this.accountsVisibilitiesByUserId.put(userId, list);

               for(int j = 0; j < count; ++j) {
                  list.add(new VAccountVisibility(dest));
               }
            }

            this.lastAccountChangeTime = dest.readLong();
         } catch (Throwable var13) {
            Throwable e = var13;
            e.printStackTrace();
         }
      }

      dest.recycle();
   }

   @TargetApi(26)
   private VAccountVisibility getAccountVisibility(int i, String name, String type) {
      List<VAccountVisibility> list = (List)this.accountsVisibilitiesByUserId.get(i);
      if (list != null) {
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            VAccountVisibility vAccountVisibility = (VAccountVisibility)var5.next();
            if (TextUtils.equals(vAccountVisibility.name, name) && TextUtils.equals(vAccountVisibility.type, type)) {
               return vAccountVisibility;
            }
         }
      }

      return null;
   }

   @TargetApi(26)
   private VAccountVisibility getAccountVisibility(int userId, Account account) {
      return this.getAccountVisibility(userId, account.name, account.type);
   }

   public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
      throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0qI2snJFo=")));
   }

   public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
      throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0qI2snJFo=")));
   }

   public void registerAccountListener(String[] accountTypes) throws RemoteException {
   }

   public void unregisterAccountListener(String[] accountTypes) throws RemoteException {
   }

   public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) throws RemoteException {
      throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0qI2snJFo=")));
   }

   public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) throws RemoteException {
      throw new RuntimeException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0qI2snJFo=")));
   }

   public AccountAndUser[] getAllAccounts() {
      List<AccountAndUser> list = new ArrayList();

      for(int i = 0; i < this.accountsByUserId.size(); ++i) {
         List<VAccount> accounts = (List)this.accountsByUserId.valueAt(i);
         Iterator var4 = accounts.iterator();

         while(var4.hasNext()) {
            VAccount account = (VAccount)var4.next();
            list.add(new AccountAndUser(new Account(account.name, account.type), account.userId));
         }
      }

      return (AccountAndUser[])list.toArray(new AccountAndUser[0]);
   }

   private class GetAccountsByTypeAndFeatureSession extends Session {
      private final String[] mFeatures;
      private volatile Account[] mAccountsOfType = null;
      private volatile ArrayList<Account> mAccountsWithFeatures = null;
      private volatile int mCurrentAccount = 0;

      public GetAccountsByTypeAndFeatureSession(IAccountManagerResponse response, int userId, AuthenticatorInfo info, String[] features) {
         super(response, userId, info, false, true, (String)null);
         this.mFeatures = features;
      }

      public void run() throws RemoteException {
         this.mAccountsOfType = VAccountManagerService.this.getAccounts(this.mUserId, this.mAuthenticatorInfo.desc.type);
         this.mAccountsWithFeatures = new ArrayList(this.mAccountsOfType.length);
         this.mCurrentAccount = 0;
         this.checkAccount();
      }

      public void checkAccount() {
         if (this.mCurrentAccount >= this.mAccountsOfType.length) {
            this.sendResult();
         } else {
            IAccountAuthenticator accountAuthenticator = this.mAuthenticator;
            if (accountAuthenticator == null) {
               Log.v(VAccountManagerService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fM2szQRF9JCg1LAcYLnhSTTdoNwY5KggYKmIkODZuASw8Ki4uKngaLAVqJzA0DRg+LHkaOCRpI1E5Iyo6DmozBi1iAS88Ly1fDm8VGilvESgvPQcqKUsVFituCiAqIy0cLGsFBiBlETAoIBcYOXxTPFo=")) + this.toDebugString());
            } else {
               try {
                  accountAuthenticator.hasFeatures(this, this.mAccountsOfType[this.mCurrentAccount], this.mFeatures);
               } catch (RemoteException var3) {
                  this.onError(1, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowMCtLHjAaLy0MKmUzLCVlN1RF")));
               }

            }
         }
      }

      public void onResult(Bundle result) {
         ++this.mNumResults;
         if (result == null) {
            this.onError(5, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoJICpmDlkwKhcMVg==")));
         } else {
            if (result.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgNSw/Iy4MCGUzSFo=")), false)) {
               this.mAccountsWithFeatures.add(this.mAccountsOfType[this.mCurrentAccount]);
            }

            ++this.mCurrentAccount;
            this.checkAccount();
         }
      }

      public void sendResult() {
         IAccountManagerResponse response = this.getResponseAndClose();
         if (response != null) {
            try {
               Account[] accounts = new Account[this.mAccountsWithFeatures.size()];

               for(int i = 0; i < accounts.length; ++i) {
                  accounts[i] = (Account)this.mAccountsWithFeatures.get(i);
               }

               if (Log.isLoggable(VAccountManagerService.TAG, 2)) {
                  Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg2P2oFHi9gNDs8Ki0YAmkgAgVlHi8ZM186KWA0ODVuASw5KQgqD2sJIFo=")) + response);
               }

               Bundle result = new Bundle();
               result.putParcelableArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmEShF")), accounts);
               response.onResult(result);
            } catch (RemoteException var4) {
               RemoteException e = var4;
               Log.v(VAccountManagerService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoKNARiCiQtKRccCGknTSZlJCwaLi0YI2AwJyNlNAo8LD4uKmwjNFo=")), e);
            }
         }

      }

      protected String toDebugString(long now) {
         return super.toDebugString(now) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186PWgaMBF9JCg1LAcYLmoLRT9nHh47LhY+KmIYICBpATA+LBgAD3VSIFo=")) + (this.mFeatures != null ? TextUtils.join(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MxhSVg==")), this.mFeatures) : null);
      }
   }

   private abstract class Session extends IAccountAuthenticatorResponse.Stub implements IBinder.DeathRecipient, ServiceConnection {
      final int mUserId;
      final AuthenticatorInfo mAuthenticatorInfo;
      private final boolean mStripAuthTokenFromResult;
      public int mNumResults;
      IAccountAuthenticator mAuthenticator;
      private IAccountManagerResponse mResponse;
      private boolean mExpectActivityLaunch;
      private long mCreationTime;
      private String mAccountName;
      private boolean mAuthDetailsRequired;
      private boolean mUpdateLastAuthenticatedTime;
      private int mNumRequestContinued;
      private int mNumErrors;

      Session(IAccountManagerResponse response, int userId, AuthenticatorInfo info, boolean expectActivityLaunch, boolean stripAuthTokenFromResult, String accountName, boolean authDetailsRequired, boolean updateLastAuthenticatedTime) {
         if (info == null) {
            throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcLOmwgDShlNCgdLAhSVg==")));
         } else {
            this.mStripAuthTokenFromResult = stripAuthTokenFromResult;
            this.mResponse = response;
            this.mUserId = userId;
            this.mAuthenticatorInfo = info;
            this.mExpectActivityLaunch = expectActivityLaunch;
            this.mCreationTime = SystemClock.elapsedRealtime();
            this.mAccountName = accountName;
            this.mAuthDetailsRequired = authDetailsRequired;
            this.mUpdateLastAuthenticatedTime = updateLastAuthenticatedTime;
            synchronized(VAccountManagerService.this.mSessions) {
               VAccountManagerService.this.mSessions.put(this.toString(), this);
            }

            if (response != null) {
               try {
                  response.asBinder().linkToDeath(this, 0);
               } catch (RemoteException var12) {
                  this.mResponse = null;
                  this.binderDied();
               }
            }

         }
      }

      Session(IAccountManagerResponse response, int userId, AuthenticatorInfo info, boolean expectActivityLaunch, boolean stripAuthTokenFromResult, String accountName) {
         this(response, userId, info, expectActivityLaunch, stripAuthTokenFromResult, accountName, false, false);
      }

      IAccountManagerResponse getResponseAndClose() {
         if (this.mResponse == null) {
            return null;
         } else {
            IAccountManagerResponse response = this.mResponse;
            this.close();
            return response;
         }
      }

      private void close() {
         synchronized(VAccountManagerService.this.mSessions) {
            if (VAccountManagerService.this.mSessions.remove(this.toString()) == null) {
               return;
            }
         }

         if (this.mResponse != null) {
            this.mResponse.asBinder().unlinkToDeath(this, 0);
            this.mResponse = null;
         }

         this.unbind();
      }

      public void onServiceConnected(ComponentName name, IBinder service) {
         this.mAuthenticator = IAccountAuthenticator.Stub.asInterface(service);

         try {
            this.run();
         } catch (RemoteException var4) {
            this.onError(1, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uDWowMCtLHjAaLy0MKmUzLCVlN1RF")));
         }

      }

      public void onRequestContinued() {
         ++this.mNumRequestContinued;
      }

      public void onError(int errorCode, String errorMessage) {
         ++this.mNumErrors;
         IAccountManagerResponse response = this.getResponseAndClose();
         if (response != null) {
            Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg2P2oFHi9gNDs8Ki0YWmoaRSVsM1gaPQgAKksVMCBlJyAeKRcYJ3gVSFo=")) + response);

            try {
               response.onError(errorCode, errorMessage);
            } catch (RemoteException var5) {
               RemoteException e = var5;
               Log.v(VAccountManagerService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKW8zAiVgMFk1KjsMKGoVNAR+MzwqLRcuIWMVESN9NAocKQccJ2MKRTVoHjwZJQcYJXkVID5sJyQ0PhcMM28wICVgNAozKj06Vg==")), e);
            }
         } else {
            Log.v(VAccountManagerService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKW8zAiVgMFk1KjsMKGoVNAR+MzwsLAcMJ30KFgZ5HiwbKQcYJ2sVSFo=")));
         }

      }

      public void onServiceDisconnected(ComponentName name) {
         this.mAuthenticator = null;
         IAccountManagerResponse response = this.getResponseAndClose();
         if (response != null) {
            try {
               response.onError(1, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYKWszGiZgNDA5LBcMPg==")));
            } catch (RemoteException var4) {
               RemoteException e = var4;
               Log.v(VAccountManagerService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKW8zAiVgMFk1KjwqPWoaHi9oJyhKIxc2JWAgRSluDiw9Ly4bPngVLDNsESA/IF4iE24KTQFlNwYKKBg2M28KMC9gJFg8LC0AMW8zBShsNyg6KQgAKmIaGiluJ1RF")), e);
            }
         }

      }

      public void onResult(Bundle result) throws RemoteException {
         ++this.mNumResults;
         if (result != null) {
            boolean isSuccessfulConfirmCreds = result.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4AD2oFNDdgNSw/Iy4MCGUzSFo=")), false);
            boolean isSuccessfulUpdateCredsOrAddAccount = result.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGULJCl9JB4vKj42Vg=="))) && result.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcMVg==")));
            boolean needUpdate = this.mUpdateLastAuthenticatedTime && (isSuccessfulConfirmCreds || isSuccessfulUpdateCredsOrAddAccount);
            if (needUpdate || this.mAuthDetailsRequired) {
               synchronized(VAccountManagerService.this.accountsByUserId) {
                  VAccount account = VAccountManagerService.this.getAccount(this.mUserId, this.mAccountName, this.mAuthenticatorInfo.desc.type);
                  if (needUpdate && account != null) {
                     account.lastAuthenticatedTime = System.currentTimeMillis();
                     VAccountManagerService.this.saveAllAccounts();
                  }

                  if (this.mAuthDetailsRequired) {
                     long lastAuthenticatedTime = -1L;
                     if (account != null) {
                        lastAuthenticatedTime = account.lastAuthenticatedTime;
                     }

                     result.putLong(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+KWwLJAVmHho/Kj42MW4FQQZrASwVIxgIJw==")), lastAuthenticatedTime);
                  }
               }
            }
         }

         if (result != null && !TextUtils.isEmpty(result.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUKMCVjJDA2"))))) {
         }

         Intent intent = null;
         if (result != null) {
            intent = (Intent)result.getParcelable(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVBgY=")));
         }

         IAccountManagerResponse response;
         if (this.mExpectActivityLaunch && result != null && result.containsKey(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVBgY=")))) {
            response = this.mResponse;
         } else {
            response = this.getResponseAndClose();
         }

         if (response != null) {
            try {
               if (result == null) {
                  Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg2P2oFHi9gNDs8Ki0YWmoaRSVsM1gaPQgAKksVMCBlJyAeKRcYJ3gVSFo=")) + response);
                  response.onError(5, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoJICpmDlkwKhcLOmoVGgZvDgobLhgqVg==")));
               } else {
                  if (this.mStripAuthTokenFromResult) {
                     result.remove(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUKMCVjJDA2")));
                  }

                  Log.v(VAccountManagerService.TAG, this.getClass().getSimpleName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg2P2oFHi9gNDs8Ki0YAmkgAgVlHi8ZM186KWA0ODVuASw5KQgqD2sJIFo=")) + response);
                  if (result.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowFhNgJAo/")), -1) > 0 && intent == null) {
                     response.onError(result.getInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowFhNgJAo/"))), result.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowFg1iASgpLwc6PQ=="))));
                  } else {
                     response.onResult(result);
                  }
               }
            } catch (RemoteException var11) {
               Log.v(VAccountManagerService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoKNARiCiQtKRccCGknTSZlJCwaLi0YI2AwJyNlNAo8LD4uKmwjNFo=")), var11);
            }
         }

      }

      public abstract void run() throws RemoteException;

      void bind() {
         Log.v(VAccountManagerService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcCWwFAjdmHgY2KCkmOGwjMCx4HiwcPQg+CWYaBiBsNzAiKAhbCmUgETRsDh4dJAMiVg==")) + this.mAuthenticatorInfo.desc.type);
         Intent intent = new Intent();
         intent.setAction(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7Ly0qDWUjMAZsIxoCLT42KWYKRT9hAQo9Kj4AKm8VAjVrHiw6Jz5SVg==")));
         intent.setClassName(this.mAuthenticatorInfo.serviceInfo.packageName, this.mAuthenticatorInfo.serviceInfo.name);
         if (!VActivityManager.get().bindService(VAccountManagerService.this.mContext, intent, this, 1, this.mUserId)) {
            Log.d(VAccountManagerService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4YCGgJIDdmEQo/KggmLn4zHjdqAQIgLgQ6ImAjMyM=")) + this.toDebugString());
            this.onError(1, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4YCGgJIC59DgYoLAguPQ==")));
         }

      }

      protected String toDebugString() {
         return this.toDebugString(SystemClock.elapsedRealtime());
      }

      protected String toDebugString(long now) {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uKW8zAiVgMwU8KAgAKmkjAgZ9ETgwLC42LEsVSFo=")) + this.mExpectActivityLaunch + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186OWozBiZiDiggKAc1Og==")) + (this.mAuthenticator != null) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KWwFJAZhICc0")) + this.mNumResults + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + this.mNumRequestContinued + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + this.mNumErrors + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAQDOGoFAi5iAQozKgcLOg==")) + (double)(now - this.mCreationTime) / 1000.0;
      }

      private void unbind() {
         if (this.mAuthenticator != null) {
            this.mAuthenticator = null;
            VActivityManager.get().unbindService(VAccountManagerService.this.mContext, this);
         }

      }

      public void binderDied() {
         this.mResponse = null;
         this.close();
      }
   }

   private final class AuthenticatorCache {
      final Map<String, AuthenticatorInfo> authenticators;

      private AuthenticatorCache() {
         this.authenticators = new HashMap();
      }

      // $FF: synthetic method
      AuthenticatorCache(Object x1) {
         this();
      }
   }

   private final class AuthenticatorInfo {
      final AuthenticatorDescription desc;
      final ServiceInfo serviceInfo;

      AuthenticatorInfo(AuthenticatorDescription desc, ServiceInfo info) {
         this.desc = desc;
         this.serviceInfo = info;
      }
   }

   static final class AuthTokenRecord {
      public int userId;
      public Account account;
      public long expiryEpochMillis;
      public String authToken;
      private String authTokenType;
      private String packageName;

      AuthTokenRecord(int userId, Account account, String authTokenType, String packageName, String authToken, long expiryEpochMillis) {
         this.userId = userId;
         this.account = account;
         this.authTokenType = authTokenType;
         this.packageName = packageName;
         this.authToken = authToken;
         this.expiryEpochMillis = expiryEpochMillis;
      }

      AuthTokenRecord(int userId, Account account, String authTokenType, String packageName) {
         this.userId = userId;
         this.account = account;
         this.authTokenType = authTokenType;
         this.packageName = packageName;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            AuthTokenRecord that = (AuthTokenRecord)o;
            return this.userId == that.userId && this.account.equals(that.account) && this.authTokenType.equals(that.authTokenType) && this.packageName.equals(that.packageName);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return ((this.userId * 31 + this.account.hashCode()) * 31 + this.authTokenType.hashCode()) * 31 + this.packageName.hashCode();
      }
   }
}
