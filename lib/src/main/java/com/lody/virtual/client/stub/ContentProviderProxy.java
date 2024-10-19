package com.lody.virtual.client.stub;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VPackageManager;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import mirror.android.content.ContentProviderClientICS;
import mirror.android.content.ContentProviderClientJB;

public class ContentProviderProxy extends ContentProvider {
   public static Uri buildProxyUri(int userId, boolean isExt, String authority, Uri uri) {
      String proxyAuthority = StubManifest.getProxyAuthority(isExt);
      Uri proxyUriPrefix = Uri.parse(String.format(Locale.ENGLISH, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1OikLKXkwDSV7Cg0vLgQfJ08kFjY=")), proxyAuthority, userId, authority));
      return Uri.withAppendedPath(proxyUriPrefix, uri.toString());
   }

   private TargetProviderInfo getProviderProviderInfo(Uri uri) {
      if (!VirtualCore.get().isEngineLaunched()) {
         return null;
      } else {
         List<String> segments = uri.getPathSegments();
         if (segments != null && segments.size() >= 3) {
            int userId = -1;

            try {
               userId = Integer.parseInt((String)segments.get(0));
            } catch (NumberFormatException var8) {
               NumberFormatException e = var8;
               e.printStackTrace();
            }

            if (userId == -1) {
               return null;
            } else {
               String authority = (String)segments.get(1);
               ProviderInfo providerInfo = VPackageManager.get().resolveContentProvider(authority, 0, userId);
               if (providerInfo != null && providerInfo.enabled) {
                  String uriContent = uri.toString();
                  String targetUriStr = uriContent.substring(authority.length() + uriContent.indexOf(authority, 1) + 1);
                  if (targetUriStr.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1"))) && !targetUriStr.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1Oi5SVg==")))) {
                     targetUriStr = targetUriStr.replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1Oi5SVg==")));
                  }

                  return new TargetProviderInfo(userId, providerInfo, Uri.parse(targetUriStr));
               } else {
                  return null;
               }
            }
         } else {
            return null;
         }
      }
   }

   private ContentProviderClient acquireProviderClient(TargetProviderInfo info) {
      try {
         IInterface provider = VActivityManager.get().acquireProviderClient(info.userId, info.info);
         if (provider != null) {
            if (VERSION.SDK_INT > 15) {
               return (ContentProviderClient)ContentProviderClientJB.ctor.newInstance(this.getContext().getContentResolver(), provider, true);
            }

            return (ContentProviderClient)ContentProviderClientICS.ctor.newInstance(this.getContext().getContentResolver(), provider);
         }
      } catch (RemoteException var3) {
         RemoteException e = var3;
         e.printStackTrace();
      }

      return null;
   }

   public ContentProviderClient acquireTargetProviderClient(Uri uri) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      return info != null ? this.acquireProviderClient(info) : null;
   }

   public boolean onCreate() {
      return true;
   }

   public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.query(info.uri, projection, selection, selectionArgs, sortOrder);
            } catch (RemoteException var9) {
               RemoteException e = var9;
               e.printStackTrace();
            }
         }
      }

      return null;
   }

   public String getType(Uri uri) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.getType(info.uri);
            } catch (RemoteException var5) {
               RemoteException e = var5;
               e.printStackTrace();
            }
         }
      }

      return null;
   }

   public Uri insert(Uri uri, ContentValues values) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.insert(info.uri, values);
            } catch (RemoteException var6) {
               RemoteException e = var6;
               e.printStackTrace();
            }
         }
      }

      return null;
   }

   public int delete(Uri uri, String selection, String[] selectionArgs) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.delete(info.uri, selection, selectionArgs);
            } catch (RemoteException var7) {
               RemoteException e = var7;
               e.printStackTrace();
            }
         }
      }

      return 0;
   }

   public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.update(info.uri, values, selection, selectionArgs);
            } catch (RemoteException var8) {
               RemoteException e = var8;
               e.printStackTrace();
            }
         }
      }

      return 0;
   }

   @TargetApi(19)
   public Uri canonicalize(Uri uri) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.canonicalize(info.uri);
            } catch (RemoteException var5) {
               RemoteException e = var5;
               e.printStackTrace();
            }
         }
      }

      return null;
   }

   @TargetApi(19)
   public Uri uncanonicalize(Uri uri) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.uncanonicalize(info.uri);
            } catch (RemoteException var5) {
               RemoteException e = var5;
               e.printStackTrace();
            }
         }
      }

      return uri;
   }

   @TargetApi(26)
   public boolean refresh(Uri uri, Bundle args, CancellationSignal cancellationSignal) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.refresh(info.uri, args, cancellationSignal);
            } catch (RemoteException var7) {
               RemoteException e = var7;
               e.printStackTrace();
            }
         }
      }

      return false;
   }

   public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.openFile(info.uri, mode);
            } catch (RemoteException var6) {
               RemoteException e = var6;
               e.printStackTrace();
            }
         }
      }

      return null;
   }

   public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
      TargetProviderInfo info = this.getProviderProviderInfo(uri);
      if (info != null) {
         ContentProviderClient client = this.acquireProviderClient(info);
         if (client != null) {
            try {
               return client.getStreamTypes(info.uri, mimeTypeFilter);
            } catch (RemoteException var6) {
               RemoteException e = var6;
               e.printStackTrace();
            }
         }
      }

      return null;
   }

   private class TargetProviderInfo {
      int userId;
      ProviderInfo info;
      Uri uri;

      TargetProviderInfo(int userId, ProviderInfo info, Uri uri) {
         this.userId = userId;
         this.info = info;
         this.uri = uri;
      }

      public String toString() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRg+KmgzNAZpESw1LD0cPmkgRQllNyQcLz0uD2IFMBZuVlFF")) + this.userId + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186CWojOCV0AVRF")) + this.info + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186I28jDTM=")) + this.uri + '}';
      }
   }
}
