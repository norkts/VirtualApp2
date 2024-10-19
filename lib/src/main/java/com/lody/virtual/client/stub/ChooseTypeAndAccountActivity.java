package com.lody.virtual.client.stub;

import android.accounts.Account;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.ipc.VAccountManager;
import com.lody.virtual.helper.utils.VLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChooseTypeAndAccountActivity extends Activity implements AccountManagerCallback<Bundle> {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWowNCZmHCg0Ki1fL2kgRVo="));
   private static final boolean DEBUG = false;
   public static final String EXTRA_ALLOWABLE_ACCOUNTS_ARRAYLIST = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEDmowPDd9NFE/JwcqP28KGiZvHjBF"));
   public static final String EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEDmowPDd9NFE/JwcqP28KGiZvHCw0KQguDw=="));
   public static final String EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGMVLClgJzA2LBVfKmUzLCVlNDBF"));
   public static final String EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGMVLClgJzA2LBYuPWogGi9sNygvJi4uO2YVLDVuASxF"));
   public static final String EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUIMCVjJDA2JBgcKmkjSFo="));
   public static final String EXTRA_SELECTED_ACCOUNT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZiDgoRLy0qDWUjMAY="));
   /** @deprecated */
   @Deprecated
   public static final String EXTRA_ALWAYS_PROMPT_FOR_ACCOUNT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggELWsaAgNpESw1KggmLmEVNARgATAqLD0uKmYVSFo="));
   public static final String EXTRA_DESCRIPTION_TEXT_OVERRIDE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguKWswFi9hEQozKi0YAGkgFgZ9JCQgKS0MI2IaLFo="));
   public static final int REQUEST_NULL = 0;
   public static final int REQUEST_CHOOSE_TYPE = 1;
   public static final int REQUEST_ADD_ACCOUNT = 2;
   private static final String KEY_INSTANCE_STATE_PENDING_REQUEST = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguCGgFAiZiJSw/IwgMPWoKBlo="));
   private static final String KEY_INSTANCE_STATE_EXISTING_ACCOUNTS = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfCW8wMC9gNDgRLy0qDWUjMAZsJ1RF"));
   private static final String KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZiDgoRLy0qDWUjMAZ9NzgeLhhSVg=="));
   private static final String KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZiDgoRKBc2E24FAiVvARo/"));
   private static final String KEY_INSTANCE_STATE_ACCOUNT_LIST = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHFEzIy42Vg=="));
   public static final String KEY_USER_ID = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28hAiw="));
   private static final int SELECTED_ITEM_NONE = -1;
   private Set<Account> mSetOfAllowableAccounts;
   private Set<String> mSetOfRelevantAccountTypes;
   private String mSelectedAccountName = null;
   private boolean mSelectedAddNewAccount = false;
   private String mDescriptionOverride;
   private ArrayList<Account> mAccounts;
   private int mPendingRequest = 0;
   private Parcelable[] mExistingAccounts = null;
   private int mSelectedItemIndex;
   private Button mOkButton;
   private int mCallingUserId;
   private boolean mDontShowPicker;

   public void onCreate(Bundle savedInstanceState) {
      Intent intent = this.getIntent();
      if (savedInstanceState != null) {
         this.mPendingRequest = savedInstanceState.getInt(KEY_INSTANCE_STATE_PENDING_REQUEST);
         this.mExistingAccounts = savedInstanceState.getParcelableArray(KEY_INSTANCE_STATE_EXISTING_ACCOUNTS);
         this.mSelectedAccountName = savedInstanceState.getString(KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME);
         this.mSelectedAddNewAccount = savedInstanceState.getBoolean(KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, false);
         this.mAccounts = savedInstanceState.getParcelableArrayList(KEY_INSTANCE_STATE_ACCOUNT_LIST);
         this.mCallingUserId = savedInstanceState.getInt(KEY_USER_ID);
      } else {
         this.mPendingRequest = 0;
         this.mExistingAccounts = null;
         this.mCallingUserId = intent.getIntExtra(KEY_USER_ID, -1);
         Account selectedAccount = (Account)intent.getParcelableExtra(EXTRA_SELECTED_ACCOUNT);
         if (selectedAccount != null) {
            this.mSelectedAccountName = selectedAccount.name;
         }
      }

      VLog.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZiDg08LwcqP28KGiZvVjwbLRgIJ0saGjZ5EVRF")) + this.mSelectedAccountName);
      this.mSetOfAllowableAccounts = this.getAllowableAccountSet(intent);
      this.mSetOfRelevantAccountTypes = this.getReleventAccountTypes(intent);
      this.mDescriptionOverride = intent.getStringExtra(EXTRA_DESCRIPTION_TEXT_OVERRIDE);
      this.mAccounts = this.getAcceptableAccountChoices(VAccountManager.get());
      if (this.mDontShowPicker) {
         super.onCreate(savedInstanceState);
      } else {
         if (this.mPendingRequest == 0 && this.mAccounts.isEmpty()) {
            this.setNonLabelThemeAndCallSuperCreate(savedInstanceState);
            if (this.mSetOfRelevantAccountTypes.size() == 1) {
               this.runAddAccountForAuthenticator((String)this.mSetOfRelevantAccountTypes.iterator().next());
            } else {
               this.startChooseAccountTypeActivity();
            }
         }

         String[] listItems = this.getListOfDisplayableOptions(this.mAccounts);
         this.mSelectedItemIndex = this.getItemIndexToSelect(this.mAccounts, this.mSelectedAccountName, this.mSelectedAddNewAccount);
         super.onCreate(savedInstanceState);
         this.setContentView(layout.choose_type_and_account);
         this.overrideDescriptionIfSupplied(this.mDescriptionOverride);
         this.populateUIAccountList(listItems);
         this.mOkButton = (Button)this.findViewById(16908314);
         this.mOkButton.setEnabled(this.mSelectedItemIndex != -1);
      }
   }

   protected void onDestroy() {
      if (Log.isLoggable(TAG, 2)) {
         Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji5fD2owLCtuEQYsKAUiDmkxQSloJwYwLC0qHX0jFixqNBo9LioqKWUxMD9vNCwbJi4fJ38FSFo=")));
      }

      super.onDestroy();
   }

   protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      outState.putInt(KEY_INSTANCE_STATE_PENDING_REQUEST, this.mPendingRequest);
      if (this.mPendingRequest == 2) {
         outState.putParcelableArray(KEY_INSTANCE_STATE_EXISTING_ACCOUNTS, this.mExistingAccounts);
      }

      if (this.mSelectedItemIndex != -1) {
         if (this.mSelectedItemIndex == this.mAccounts.size()) {
            outState.putBoolean(KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, true);
         } else {
            outState.putBoolean(KEY_INSTANCE_STATE_SELECTED_ADD_ACCOUNT, false);
            outState.putString(KEY_INSTANCE_STATE_SELECTED_ACCOUNT_NAME, ((Account)this.mAccounts.get(this.mSelectedItemIndex)).name);
         }
      }

      outState.putParcelableArrayList(KEY_INSTANCE_STATE_ACCOUNT_LIST, this.mAccounts);
   }

   public void onCancelButtonClicked(View view) {
      this.onBackPressed();
   }

   public void onOkButtonClicked(View view) {
      if (this.mSelectedItemIndex == this.mAccounts.size()) {
         this.startChooseAccountTypeActivity();
      } else if (this.mSelectedItemIndex != -1) {
         this.onAccountSelected((Account)this.mAccounts.get(this.mSelectedItemIndex));
      }

   }

   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (Log.isLoggable(TAG, 2)) {
         if (data != null && data.getExtras() != null) {
            data.getExtras().keySet();
         }

         Bundle extras = data != null ? data.getExtras() : null;
         Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji5fD2owLCtuEQYsKAUiDmkxQSloJwYwLC0qHX0jFixqNBo9LioqKWUxJDVsAR4hJQgMCn0wFiNlJyQZPBcMM28bLCViHjMd")) + requestCode + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KmgaLBNgJAo/PghSVg==")) + resultCode + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186M2kKMAR9ASsd")) + extras + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAhSVg==")));
      }

      this.mPendingRequest = 0;
      if (resultCode == 0) {
         if (this.mAccounts.isEmpty()) {
            this.setResult(0);
            this.finish();
         }

      } else {
         if (resultCode == -1) {
            String accountName;
            if (requestCode == 1) {
               if (data != null) {
                  accountName = data.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcMVg==")));
                  if (accountName != null) {
                     this.runAddAccountForAuthenticator(accountName);
                     return;
                  }
               }

               Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji5fD2owLCtuEQYsKAUiDmkxQSloJwYwLC0qHX0jFixqNBo9LioqKWUxJDVsAR4hJQgMCn0wFiNlJyQZODo6I2ojJCpgHjM8LBdeOmkVLCZrVjwsLT42KWYKRT95ETAyLD4fKHgaICZoHiw0Jj0MIGwwIzZlNwo0PhcMM28aNCthJw08LC0iL34zAjdlNzAgLAguIA==")));
            } else if (requestCode == 2) {
               accountName = null;
               String accountType = null;
               if (data != null) {
                  accountName = data.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGULJCl9JB4vKj42Vg==")));
                  accountType = data.getStringExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcMVg==")));
               }

               if (accountName == null || accountType == null) {
                  Account[] currentAccounts = VAccountManager.get().getAccounts(this.mCallingUserId, (String)null);
                  Set<Account> preExistingAccounts = new HashSet();
                  Parcelable[] var8 = this.mExistingAccounts;
                  int var9 = var8.length;

                  int var10;
                  for(var10 = 0; var10 < var9; ++var10) {
                     Parcelable accountParcel = var8[var10];
                     preExistingAccounts.add((Account)accountParcel);
                  }

                  Account[] var13 = currentAccounts;
                  var9 = currentAccounts.length;

                  for(var10 = 0; var10 < var9; ++var10) {
                     Account account = var13[var10];
                     if (!preExistingAccounts.contains(account)) {
                        accountName = account.name;
                        accountType = account.type;
                        break;
                     }
                  }
               }

               if (accountName != null || accountType != null) {
                  this.setResultAndFinish(accountName, accountType);
                  return;
               }
            }

            Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji5fD2owLCtuEQYsKAUiDmkxQSloJwYwLC0qHX0jFixqNBo9LioqKWUxJDVsAR4hJQgMCn0wFiNlJyQZODo6I2ojJCpgHjM8LBdeOmkVLCZrVjwsLggqJ2JTOCRpJCweIy4qCnVSICRvJygZJAdfI28KAj9+NBo/LQQ6KmgaJAViASggPxg6OWoJTSloARoqLhgEJ2IVSFo=")));
         }

         if (Log.isLoggable(TAG, 2)) {
            Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji5fD2owLCtuEQYsKAUiDmkxQSloJwYwLC0qHX0jFixqNBo9LioqKWUxJDVsAR4hJQgMCn0wFiNlJyQZODo6OWsVBiliDlE/KBhSVg==")));
         }

         this.setResult(0);
         this.finish();
      }
   }

   protected void runAddAccountForAuthenticator(String type) {
      if (Log.isLoggable(TAG, 2)) {
         Log.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj0uCGMVMCxlDig5Ki4MDmUxHiVsNTgwKghfJ2AzFixpJCQ9KQdePngVSFo=")) + type);
      }

      Bundle options = this.getIntent().getBundleExtra(EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE);
      String[] requiredFeatures = this.getIntent().getStringArrayExtra(EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY);
      String authTokenType = this.getIntent().getStringExtra(EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING);
      VAccountManager.get().addAccount(this.mCallingUserId, type, authTokenType, requiredFeatures, options, (Activity)null, this, (Handler)null);
   }

   public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
      Bundle bundle;
      try {
         bundle = (Bundle)accountManagerFuture.getResult();
         Intent intent = (Intent)bundle.getParcelable(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgVBgY=")));
         if (intent != null) {
            this.mPendingRequest = 2;
            this.mExistingAccounts = VAccountManager.get().getAccounts(this.mCallingUserId, (String)null);
            intent.setFlags(intent.getFlags() & -268435457);
            this.startActivityForResult(intent, 2);
            return;
         }
      } catch (OperationCanceledException var4) {
         this.setResult(0);
         this.finish();
         return;
      } catch (AuthenticatorException | IOException var5) {
         Exception e = var5;
         ((Exception)e).printStackTrace();
      }

      bundle = new Bundle();
      bundle.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowFg1iASgpLwc6PQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQcMKmowESh9JB43KggMDmwjAjdvER4bLjo6MWMFFit5ESwuLBcEJ2wzSFo=")));
      this.setResult(-1, (new Intent()).putExtras(bundle));
      this.finish();
   }

   private void setNonLabelThemeAndCallSuperCreate(Bundle savedInstanceState) {
      if (VERSION.SDK_INT >= 21) {
         this.setTheme(16974396);
      } else {
         this.setTheme(16973941);
      }

      super.onCreate(savedInstanceState);
   }

   private void onAccountSelected(Account account) {
      Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uDmgVLAZiDg08LwcqP28KGiZvVjxF")) + account);
      this.setResultAndFinish(account.name, account.type);
   }

   private void setResultAndFinish(String accountName, String accountType) {
      Bundle bundle = new Bundle();
      bundle.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGULJCl9JB4vKj42Vg==")), accountName);
      bundle.putString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWowNCZmHwoZIxcMVg==")), accountType);
      this.setResult(-1, (new Intent()).putExtras(bundle));
      VLog.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji5fD2owLCtuEQYsKAUiDmkxQSloJwYwLC0qHX0jFixqNBo9LioqD2sKMABoHjAaJhgMAmwwLFVsJywwKi5eInsKLCtgHjA5LBcMPn4zQSloJwYwLC0pJA==")) + accountName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg==")) + accountType);
      this.finish();
   }

   private void startChooseAccountTypeActivity() {
      Intent intent = new Intent(this, ChooseAccountTypeActivity.class);
      intent.setFlags(524288);
      intent.putExtra(EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY, this.getIntent().getStringArrayExtra(EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY));
      intent.putExtra(EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE, this.getIntent().getBundleExtra(EXTRA_ADD_ACCOUNT_OPTIONS_BUNDLE));
      intent.putExtra(EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY, this.getIntent().getStringArrayExtra(EXTRA_ADD_ACCOUNT_REQUIRED_FEATURES_STRING_ARRAY));
      intent.putExtra(EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING, this.getIntent().getStringExtra(EXTRA_ADD_ACCOUNT_AUTH_TOKEN_TYPE_STRING));
      intent.putExtra(KEY_USER_ID, this.mCallingUserId);
      this.startActivityForResult(intent, 1);
      this.mPendingRequest = 1;
   }

   private int getItemIndexToSelect(ArrayList<Account> accounts, String selectedAccountName, boolean selectedAddNewAccount) {
      if (selectedAddNewAccount) {
         return accounts.size();
      } else {
         for(int i = 0; i < accounts.size(); ++i) {
            if (((Account)accounts.get(i)).name.equals(selectedAccountName)) {
               return i;
            }
         }

         return -1;
      }
   }

   private String[] getListOfDisplayableOptions(ArrayList<Account> accounts) {
      String[] listItems = new String[accounts.size() + 1];

      for(int i = 0; i < accounts.size(); ++i) {
         listItems[i] = ((Account)accounts.get(i)).name;
      }

      listItems[accounts.size()] = this.getResources().getString(string.add_account_button_label);
      return listItems;
   }

   private ArrayList<Account> getAcceptableAccountChoices(VAccountManager accountManager) {
      Account[] accounts = accountManager.getAccounts(this.mCallingUserId, (String)null);
      ArrayList<Account> accountsToPopulate = new ArrayList(accounts.length);
      Account[] var4 = accounts;
      int var5 = accounts.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Account account = var4[var6];
         if ((this.mSetOfAllowableAccounts == null || this.mSetOfAllowableAccounts.contains(account)) && (this.mSetOfRelevantAccountTypes == null || this.mSetOfRelevantAccountTypes.contains(account.type))) {
            accountsToPopulate.add(account);
         }
      }

      return accountsToPopulate;
   }

   private Set<String> getReleventAccountTypes(Intent intent) {
      String[] allowedAccountTypes = intent.getStringArrayExtra(EXTRA_ALLOWABLE_ACCOUNT_TYPES_STRING_ARRAY);
      AuthenticatorDescription[] descs = VAccountManager.get().getAuthenticatorTypes(this.mCallingUserId);
      Set<String> supportedAccountTypes = new HashSet(descs.length);
      AuthenticatorDescription[] var6 = descs;
      int var7 = descs.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         AuthenticatorDescription desc = var6[var8];
         supportedAccountTypes.add(desc.type);
      }

      Set<String> setOfRelevantAccountTypes;
      if (allowedAccountTypes != null) {
         setOfRelevantAccountTypes = new HashSet();
         Collections.addAll(setOfRelevantAccountTypes, allowedAccountTypes);
         setOfRelevantAccountTypes.retainAll(supportedAccountTypes);
      } else {
         setOfRelevantAccountTypes = supportedAccountTypes;
      }

      return setOfRelevantAccountTypes;
   }

   private Set<Account> getAllowableAccountSet(Intent intent) {
      Set<Account> setOfAllowableAccounts = null;
      ArrayList<Parcelable> validAccounts = intent.getParcelableArrayListExtra(EXTRA_ALLOWABLE_ACCOUNTS_ARRAYLIST);
      if (validAccounts != null) {
         setOfAllowableAccounts = new HashSet(validAccounts.size());
         Iterator var4 = validAccounts.iterator();

         while(var4.hasNext()) {
            Parcelable parcelable = (Parcelable)var4.next();
            setOfAllowableAccounts.add((Account)parcelable);
         }
      }

      return setOfAllowableAccounts;
   }

   private void overrideDescriptionIfSupplied(String descriptionOverride) {
      TextView descriptionView = (TextView)this.findViewById(id.description);
      if (!TextUtils.isEmpty(descriptionOverride)) {
         descriptionView.setText(descriptionOverride);
      } else {
         descriptionView.setVisibility(8);
      }

   }

   private void populateUIAccountList(String[] listItems) {
      ListView list = (ListView)this.findViewById(16908298);
      list.setAdapter(new ArrayAdapter(this, 17367055, listItems));
      list.setChoiceMode(1);
      list.setItemsCanFocus(false);
      list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            ChooseTypeAndAccountActivity.this.mSelectedItemIndex = position;
            ChooseTypeAndAccountActivity.this.mOkButton.setEnabled(true);
         }
      });
      if (this.mSelectedItemIndex != -1) {
         list.setItemChecked(this.mSelectedItemIndex, true);
      }

   }
}
