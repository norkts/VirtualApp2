package com.carlos.home.XposedManager;

import android.os.IBinder;
import android.os.RemoteException;

import com.lody.virtual.server.interfaces.IPackageObserver;

public class XPosedModuleObserver implements IPackageObserver {
    @Override
    public void onPackageInstalled(String s) throws RemoteException {

    }

    @Override
    public void onPackageUninstalled(String s) throws RemoteException {

    }

    @Override
    public void onPackageInstalledAsUser(int i, String s) throws RemoteException {

    }

    @Override
    public void onPackageUninstalledAsUser(int i, String s) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
