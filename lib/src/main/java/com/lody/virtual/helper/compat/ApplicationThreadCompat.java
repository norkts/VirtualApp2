package com.lody.virtual.helper.compat;

import android.os.IBinder;
import android.os.IInterface;
import mirror.android.app.ApplicationThreadNative;
import mirror.android.app.IApplicationThreadOreo;

public class ApplicationThreadCompat {
   public static IInterface asInterface(IBinder binder) {
      return BuildCompat.isOreo() ? (IInterface)IApplicationThreadOreo.Stub.asInterface.call(binder) : (IInterface)ApplicationThreadNative.asInterface.call(binder);
   }
}
