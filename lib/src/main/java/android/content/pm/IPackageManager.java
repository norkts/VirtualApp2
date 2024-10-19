package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.os.RemoteException;
import androidx.annotation.RequiresApi;

public interface IPackageManager {
   String[] getPackagesForUid(int var1) throws RemoteException;

   int getPackageUid(String var1, int var2) throws RemoteException;

   int[] getPackageGids(String var1) throws RemoteException;

   PackageInfo getPackageInfo(String var1, int var2, int var3) throws RemoteException;

   @RequiresApi(33)
   PackageInfo getPackageInfo(String var1, long var2, int var4) throws RemoteException;

   ApplicationInfo getApplicationInfo(String var1, int var2, int var3) throws RemoteException;

   @RequiresApi(33)
   ApplicationInfo getApplicationInfo(String var1, long var2, int var4) throws RemoteException;

   ActivityInfo getActivityInfo(ComponentName var1, int var2, int var3) throws RemoteException;

   ActivityInfo getReceiverInfo(ComponentName var1, int var2, int var3) throws RemoteException;

   ServiceInfo getServiceInfo(ComponentName var1, int var2, int var3) throws RemoteException;

   ServiceInfo getServiceInfo(ComponentName var1, long var2, int var4) throws RemoteException;

   ProviderInfo getProviderInfo(ComponentName var1, int var2, int var3) throws RemoteException;

   ResolveInfo resolveIntent(Intent var1, String var2, int var3, int var4) throws RemoteException;

   ResolveInfo resolveIntent(Intent var1, String var2, long var3, int var5) throws RemoteException;

   ProviderInfo resolveContentProvider(String var1, int var2, int var3) throws RemoteException;

   ProviderInfo resolveContentProvider(String var1, long var2, int var4) throws RemoteException;

   int checkPermission(String var1, String var2, int var3) throws RemoteException;
}
