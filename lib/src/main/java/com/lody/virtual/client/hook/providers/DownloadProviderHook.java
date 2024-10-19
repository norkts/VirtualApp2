package com.lody.virtual.client.hook.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.MethodBox;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class DownloadProviderHook extends ExternalProviderHook {
   private static final String TAG = DownloadProviderHook.class.getSimpleName();
   private static final String COLUMN_NOTIFICATION_PACKAGE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDmozQSlqJzguLhhSVg=="));
   private static final String COLUMN_NOTIFICATION_CLASS = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDm4FODdsJDBF"));
   public static final String COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2H2wjAgNjDiwoKAZfMW8YNCxlJCAbLAgAO2IVNF9qDhpF"));
   public static final String COLUMN_VISIBILITY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKWUVFi9gHgYgLQhSVg=="));
   public static final String COLUMN_DESCRIPTION = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguKWswFi9hEQozKi0YVg=="));
   public static final String COLUMN_FILE_NAME_HINT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBgYCGwFSFo="));

   DownloadProviderHook(IInterface base) {
      super(base);
   }

   public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
      VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRgALWojHiV9DgoNLwcYOWkFGgQ=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+DmoJIFo=")) + method.getName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl8HJnsFSFo=")) + Arrays.toString(args));
      return super.invoke(proxy, method, args);
   }

   public Uri insert(MethodBox methodBox, Uri url, ContentValues initialValues) throws InvocationTargetException {
      VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRgALWojHiV9DgoNLwcYOWkFGgQ=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcKWgaFgZ3MCRF")) + initialValues);
      String notificationPkg = initialValues.getAsString(COLUMN_NOTIFICATION_PACKAGE);
      VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRgALWojHiV9DgoNLwcYOWkFGgQ=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDmIzJC1+MzxF")) + notificationPkg);
      if (notificationPkg == null) {
         return (Uri)methodBox.call();
      } else {
         initialValues.put(COLUMN_NOTIFICATION_PACKAGE, VirtualCore.get().getHostPkg());
         initialValues.put(COLUMN_VISIBILITY, 1);
         String hint = initialValues.getAsString(COLUMN_FILE_NAME_HINT);
         String replaceHint = hint.replace(notificationPkg, VirtualCore.get().getHostPkg());
         initialValues.put(COLUMN_FILE_NAME_HINT, replaceHint);
         return super.insert(methodBox, url, initialValues);
      }
   }

   public Cursor query(MethodBox methodBox, Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder, Bundle originQueryArgs) throws InvocationTargetException {
      VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KgcuM28gDSh3MCQpKAdbPW4KBi9lJx0xPQhSVg==")) + selection + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186P28jPAN3MCRF")) + Arrays.toString(selectionArgs));
      return super.query(methodBox, url, projection, selection, selectionArgs, sortOrder, originQueryArgs);
   }
}
