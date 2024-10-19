package com.lody.virtual.client.stub;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.IAccountManagerResponse;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.utils.VLog;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AmsTask extends FutureTask<Bundle> implements AccountManagerFuture<Bundle> {
   protected final IAccountManagerResponse mResponse;
   final Handler mHandler;
   final AccountManagerCallback<Bundle> mCallback;
   final Activity mActivity;

   public AmsTask(Activity activity, Handler handler, AccountManagerCallback<Bundle> callback) {
      super(new Callable<Bundle>() {
         public Bundle call() throws Exception {
            throw new IllegalStateException("this should never be called");
         }
      });
      this.mHandler = handler;
      this.mCallback = callback;
      this.mActivity = activity;
      this.mResponse = new Response();
   }

   public final AccountManagerFuture<Bundle> start() {
      try {
         this.doWork();
      } catch (RemoteException var2) {
         RemoteException e = var2;
         this.setException(e);
      }

      return this;
   }

   protected void set(Bundle bundle) {
      if (bundle == null) {
         VLog.e("AccountManager", "the bundle must not be null\n%s", new Exception());
      }

      super.set(bundle);
   }

   public abstract void doWork() throws RemoteException;

   private Bundle internalGetResult(Long timeout, TimeUnit unit) throws OperationCanceledException, IOException, AuthenticatorException {
      Bundle var16;
      try {
         if (timeout == null) {
            var16 = (Bundle)this.get();
            return var16;
         }

         var16 = (Bundle)this.get(timeout, unit);
      } catch (CancellationException var11) {
         throw new OperationCanceledException();
      } catch (TimeoutException var12) {
         throw new OperationCanceledException();
      } catch (InterruptedException var13) {
         throw new OperationCanceledException();
      } catch (ExecutionException var14) {
         ExecutionException e = var14;
         Throwable cause = e.getCause();
         if (cause instanceof IOException) {
            throw (IOException)cause;
         }

         if (cause instanceof UnsupportedOperationException) {
            throw new AuthenticatorException(cause);
         }

         if (cause instanceof AuthenticatorException) {
            throw (AuthenticatorException)cause;
         }

         if (cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
         }

         if (cause instanceof Error) {
            throw (Error)cause;
         }

         throw new IllegalStateException(cause);
      } finally {
         this.cancel(true);
      }

      return var16;
   }

   public Bundle getResult() throws OperationCanceledException, IOException, AuthenticatorException {
      return this.internalGetResult((Long)null, (TimeUnit)null);
   }

   public Bundle getResult(long timeout, TimeUnit unit) throws OperationCanceledException, IOException, AuthenticatorException {
      return this.internalGetResult(timeout, unit);
   }

   protected void done() {
      if (this.mCallback != null) {
         this.postToHandler(this.mHandler, this.mCallback, this);
      }

   }

   private Exception convertErrorToException(int code, String message) {
      if (code == 3) {
         return new IOException(message);
      } else if (code == 6) {
         return new UnsupportedOperationException(message);
      } else if (code == 5) {
         return new AuthenticatorException(message);
      } else {
         return (Exception)(code == 7 ? new IllegalArgumentException(message) : new AuthenticatorException(message));
      }
   }

   private void postToHandler(Handler handler, final AccountManagerCallback<Bundle> callback, final AccountManagerFuture<Bundle> future) {
      handler = handler == null ? VirtualRuntime.getUIHandler() : handler;
      handler.post(new Runnable() {
         public void run() {
            callback.run(future);
         }
      });
   }

   private class Response extends IAccountManagerResponse.Stub {
      private Response() {
      }

      public void onResult(Bundle bundle) {
         Intent intent = (Intent)bundle.getParcelable("intent");
         if (intent != null && AmsTask.this.mActivity != null) {
            AmsTask.this.mActivity.startActivity(intent);
         } else if (bundle.getBoolean("retry")) {
            try {
               AmsTask.this.doWork();
            } catch (RemoteException var4) {
               RemoteException e = var4;
               throw new RuntimeException(e);
            }
         } else {
            AmsTask.this.set(bundle);
         }

      }

      public void onError(int code, String message) {
         if (code != 4 && code != 100 && code != 101) {
            AmsTask.this.setException(AmsTask.this.convertErrorToException(code, message));
         } else {
            AmsTask.this.cancel(true);
         }
      }

      // $FF: synthetic method
      Response(Object x1) {
         this();
      }
   }
}
