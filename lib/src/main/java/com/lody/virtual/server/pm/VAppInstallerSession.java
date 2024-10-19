package com.lody.virtual.server.pm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Handler;
import com.lody.virtual.StringFog;
import com.lody.virtual.server.app.IAppInstallerSession;
import java.util.ArrayList;
import java.util.List;

public class VAppInstallerSession extends IAppInstallerSession.Stub {
   private Context mContext;
   private VAppManagerService mApp;
   private boolean mCommited = false;
   private boolean mCacneled = false;
   private final List<Uri> mUris = new ArrayList();
   private final List<Uri> mSplitUris = new ArrayList();
   private IntentSender mStatusReceiver = null;

   public VAppInstallerSession(Context context, VAppManagerService app) {
      this.mContext = context;
      this.mApp = app;
   }

   public void addPackage(Uri uri) {
      this.mUris.add(uri);
   }

   public void addSplit(Uri uri) {
      this.mSplitUris.add(uri);
   }

   public void commit(IntentSender statusReceiver) {
      if (this.mCacneled) {
         throw new IllegalStateException("A canceled session cannot be committed.");
      } else {
         this.mCommited = true;
         this.mStatusReceiver = statusReceiver;

         try {
            this.mStatusReceiver.sendIntent(this.mContext, 0, new Intent(), (IntentSender.OnFinished)null, (Handler)null);
         } catch (IntentSender.SendIntentException var3) {
            IntentSender.SendIntentException e = var3;
            e.printStackTrace();
         }

      }
   }

   public void cancel() {
      if (this.mCommited) {
         throw new IllegalStateException("Session that have already been committed cannot be cancelled.");
      } else {
         this.mCacneled = true;
      }
   }
}
