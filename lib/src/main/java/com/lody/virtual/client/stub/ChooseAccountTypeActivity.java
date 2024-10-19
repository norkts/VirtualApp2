package com.lody.virtual.client.stub;

import android.accounts.AuthenticatorDescription;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VAccountManager;
import com.lody.virtual.helper.utils.VLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChooseAccountTypeActivity extends Activity {
   public static final String KEY_USER_ID = "userId";
   private static final String TAG = "AccountChooser";
   private static final boolean DEBUG = false;
   private HashMap<String, AuthInfo> mTypeToAuthenticatorInfo = new HashMap();
   private ArrayList<AuthInfo> mAuthenticatorInfosToDisplay;
   private int mCallingUid;

   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.mCallingUid = this.getIntent().getIntExtra(KEY_USER_ID, -1);
      if (this.mCallingUid == -1) {
         this.finish();
      } else {
         Set<String> setOfAllowableAccountTypes = null;
         String[] validAccountTypes = this.getIntent().getStringArrayExtra("allowableAccountTypes");
         if (validAccountTypes != null) {
            setOfAllowableAccountTypes = new HashSet(validAccountTypes.length);
            Collections.addAll(setOfAllowableAccountTypes, validAccountTypes);
         }

         this.buildTypeToAuthDescriptionMap();
         this.mAuthenticatorInfosToDisplay = new ArrayList(this.mTypeToAuthenticatorInfo.size());
         Iterator var4 = this.mTypeToAuthenticatorInfo.entrySet().iterator();

         while(true) {
            String type;
            AuthInfo info;
            do {
               if (!var4.hasNext()) {
                  if (this.mAuthenticatorInfosToDisplay.isEmpty()) {
                     Bundle bundle = new Bundle();
                     bundle.putString("errorMessage", "no allowable account types");
                     this.setResult(-1, (new Intent()).putExtras(bundle));
                     this.finish();
                     return;
                  }

                  if (this.mAuthenticatorInfosToDisplay.size() == 1) {
                     this.setResultAndFinish(((AuthInfo)this.mAuthenticatorInfosToDisplay.get(0)).desc.type);
                     return;
                  }

                  this.setContentView(layout.choose_account_type);
                  ListView list = (ListView)this.findViewById(16908298);
                  list.setAdapter(new AccountArrayAdapter(this, 17367043, this.mAuthenticatorInfosToDisplay));
                  list.setChoiceMode(0);
                  list.setTextFilterEnabled(false);
                  list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        ChooseAccountTypeActivity.this.setResultAndFinish(((AuthInfo)ChooseAccountTypeActivity.this.mAuthenticatorInfosToDisplay.get(position)).desc.type);
                     }
                  });
                  return;
               }

               Map.Entry<String, AuthInfo> entry = (Map.Entry)var4.next();
               type = (String)entry.getKey();
               info = (AuthInfo)entry.getValue();
            } while(setOfAllowableAccountTypes != null && !setOfAllowableAccountTypes.contains(type));

            this.mAuthenticatorInfosToDisplay.add(info);
         }
      }
   }

   private void setResultAndFinish(String type) {
      Bundle bundle = new Bundle();
      bundle.putString("accountType", type);
      this.setResult(-1, (new Intent()).putExtras(bundle));
      this.finish();
   }

   private void buildTypeToAuthDescriptionMap() {
      AuthenticatorDescription[] var1 = VAccountManager.get().getAuthenticatorTypes(this.mCallingUid);
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         AuthenticatorDescription desc = var1[var3];
         String name = null;
         Drawable icon = null;

         try {
            Resources res = VirtualCore.get().getResources(desc.packageName);
            icon = res.getDrawable(desc.iconId);
            CharSequence sequence = res.getText(desc.labelId);
            name = sequence.toString();
            name = sequence.toString();
         } catch (Resources.NotFoundException var9) {
            VLog.w(TAG, "No icon resource for account type " + desc.type);
         }

         AuthInfo authInfo = new AuthInfo(desc, name, icon);
         this.mTypeToAuthenticatorInfo.put(desc.type, authInfo);
      }

   }

   private static class AccountArrayAdapter extends ArrayAdapter<AuthInfo> {
      private LayoutInflater mLayoutInflater;
      private ArrayList<AuthInfo> mInfos;

      AccountArrayAdapter(Context context, int textViewResourceId, ArrayList<AuthInfo> infos) {
         super(context, textViewResourceId, infos);
         this.mInfos = infos;
         this.mLayoutInflater = (LayoutInflater)context.getSystemService("layout_inflater");
      }

      public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder;
         if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(layout.choose_account_row, (ViewGroup)null);
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(id.account_row_text);
            holder.icon = (ImageView)convertView.findViewById(id.account_row_icon);
            convertView.setTag(holder);
         } else {
            holder = (ViewHolder)convertView.getTag();
         }

         holder.text.setText(((AuthInfo)this.mInfos.get(position)).name);
         holder.icon.setImageDrawable(((AuthInfo)this.mInfos.get(position)).drawable);
         return convertView;
      }
   }

   private static class ViewHolder {
      ImageView icon;
      TextView text;

      private ViewHolder() {
      }

      // $FF: synthetic method
      ViewHolder(Object x0) {
         this();
      }
   }

   private static class AuthInfo {
      final AuthenticatorDescription desc;
      final String name;
      final Drawable drawable;

      AuthInfo(AuthenticatorDescription desc, String name, Drawable drawable) {
         this.desc = desc;
         this.name = name;
         this.drawable = drawable;
      }
   }
}
