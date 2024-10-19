package com.lody.virtual.client.stub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import com.kook.librelease.R.string;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import java.util.List;

public class ChooserActivity extends ResolverActivity {
   public static final String EXTRA_DATA = "android.intent.extra.virtual.data";
   public static final String EXTRA_WHO = "android.intent.extra.virtual.who";
   public static final String EXTRA_INTENT = "android.intent.extra.virtual.intent";
   public static final String EXTRA_REQUEST_CODE = "android.intent.extra.virtual.request_code";
   public static final String ACTION;
   public static final String EXTRA_RESULTTO = "_va|ibinder|resultTo";

   public static boolean check(Intent intent) {
      try {
         return TextUtils.equals(ACTION, intent.getAction()) || TextUtils.equals("android.intent.action.CHOOSER", intent.getAction());
      } catch (Exception var2) {
         Exception e = var2;
         e.printStackTrace();
         return false;
      }
   }

   @SuppressLint({"MissingSuperCall"})
   protected void onCreate(Bundle savedInstanceState) {
      Bundle extras = this.getIntent().getExtras();
      Intent intent = this.getIntent();
      int userId = extras.getInt("android.intent.extra.user_handle", VUserHandle.getCallingUserId());
      this.mOptions = (Bundle)extras.getParcelable(EXTRA_DATA);
      this.mResultWho = extras.getString(EXTRA_WHO);
      this.mRequestCode = extras.getInt(EXTRA_REQUEST_CODE, 0);
      this.mResultTo = BundleCompat.getBinder(extras, EXTRA_RESULTTO);
      Parcelable targetParcelable = intent.getParcelableExtra("android.intent.extra.INTENT");
      if (!(targetParcelable instanceof Intent)) {
         VLog.w("ChooseActivity", "Target is not an intent: %s", targetParcelable);
         this.finish();
      } else {
         Intent target = (Intent)targetParcelable;
         CharSequence title = intent.getCharSequenceExtra("android.intent.extra.TITLE");
         if (title == null) {
            title = this.getString(string.choose);
         }

         Parcelable[] pa = intent.getParcelableArrayExtra("android.intent.extra.INITIAL_INTENTS");
         Intent[] initialIntents = null;
         if (pa != null) {
            initialIntents = new Intent[pa.length];

            for(int i = 0; i < pa.length; ++i) {
               if (!(pa[i] instanceof Intent)) {
                  VLog.w("ChooseActivity", "Initial intent #" + i + " not an Intent: %s", pa[i]);
                  this.finish();
                  return;
               }

               initialIntents[i] = (Intent)pa[i];
            }
         }

         super.onCreate(savedInstanceState, target, (CharSequence)title, initialIntents, (List)null, false, userId);
      }
   }

   static {
      Intent target = new Intent();
      Intent intent = Intent.createChooser(target, "");
      ACTION = intent.getAction();
   }
}
