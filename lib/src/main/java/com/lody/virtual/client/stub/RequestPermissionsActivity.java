package com.lody.virtual.client.stub;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BundleCompat;
import com.lody.virtual.server.IRequestPermissionsResult;

@TargetApi(23)
public class RequestPermissionsActivity extends Activity {
   private static final int REQUEST_PERMISSION_CODE = 996;
   private IRequestPermissionsResult mCallBack;

   public static void request(Context context, boolean isExt, String[] permissions, IRequestPermissionsResult callback) {
      Intent intent = new Intent();
      if (isExt) {
         intent.setClassName(StubManifest.EXT_PACKAGE_NAME, RequestPermissionsActivity.class.getName());
      } else {
         intent.setClassName(StubManifest.PACKAGE_NAME, RequestPermissionsActivity.class.getName());
      }

      intent.setFlags(268435456);
      intent.putExtra("permissions", permissions);
      BundleCompat.putBinder(intent, "callback", callback.asBinder());
      context.startActivity(intent);
   }

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Intent intent = this.getIntent();
      if (intent == null) {
         this.finish();
      } else {
         String[] permissions = intent.getStringArrayExtra("permissions");
         IBinder binder = BundleCompat.getBinder(intent, "callback");
         if (binder != null && permissions != null) {
            this.mCallBack = IRequestPermissionsResult.Stub.asInterface(binder);
            this.requestPermissions(permissions, 996);
         } else {
            this.finish();
         }
      }
   }

   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (this.mCallBack != null) {
         try {
            boolean success = this.mCallBack.onResult(requestCode, permissions, grantResults);
            if (!success) {
               this.runOnUiThread(new Runnable() {
                  public void run() {
                     Toast.makeText(RequestPermissionsActivity.this, "Request permission failed.", 0).show();
                  }
               });
            }
         } catch (Throwable var5) {
            Throwable e = var5;
            e.printStackTrace();
         }
      }

      this.finish();
   }
}
