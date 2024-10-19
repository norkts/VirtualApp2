package com.carlos.home.XposedManager;

import static com.lody.virtual.os.VUserHandle.getUserId;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.os.RemoteException;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.server.interfaces.IPackageObserver;

import me.weishu.exposed.ExposedBridge;

public class XPosedModuleObserver implements IPackageObserver {
    @Override
    public void onPackageInstalled(String packageName) throws RemoteException {

        Context context = VirtualCore.get().getContext();
        ApplicationInfo applicationInfo = VPackageManager.get().getApplicationInfo(packageName, 0, getUserId(0));
        ExposedBridge.initOnce(context, applicationInfo, context.getClassLoader());
    }

    @Override
    public void onPackageUninstalled(String s) throws RemoteException {

    }

    @Override
    public void onPackageInstalledAsUser(int userId, String packageName) throws RemoteException {
        Context context = VirtualCore.get().getContext();
        ApplicationInfo applicationInfo = VPackageManager.get().getApplicationInfo(packageName, 0, getUserId(userId));
        ExposedBridge.initOnce(context, applicationInfo, context.getClassLoader());
    }

    @Override
    public void onPackageUninstalledAsUser(int i, String s) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
