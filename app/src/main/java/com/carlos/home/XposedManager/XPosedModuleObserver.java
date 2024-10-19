package com.carlos.home.XposedManager;

import static com.lody.virtual.os.VUserHandle.getUserId;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.os.RemoteException;

import com.lody.virtual.client.core.AppCallback;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.server.interfaces.IPackageObserver;

import me.weishu.exposed.ExposedBridge;

public class XPosedModuleObserver implements AppCallback {
    @Override
    public void beforeStartApplication(String var1, String var2, Context var3) {

    }

    @Override
    public void beforeApplicationCreate(String var1, String var2, Application var3) {

    }

    @Override
    public void afterApplicationCreate(String var1, String var2, Application var3) {

    }

    @Override
    public void beforeActivityOnCreate(Activity var1) {

    }

    @Override
    public void afterActivityOnCreate(Activity var1) {

    }

    @Override
    public void beforeActivityOnStart(Activity var1) {

    }

    @Override
    public void afterActivityOnStart(Activity var1) {

    }

    @Override
    public void beforeActivityOnResume(Activity var1) {

    }

    @Override
    public void afterActivityOnResume(Activity var1) {

    }

    @Override
    public void beforeActivityOnStop(Activity var1) {

    }

    @Override
    public void afterActivityOnStop(Activity var1) {

    }

    @Override
    public void beforeActivityOnDestroy(Activity var1) {

    }

    @Override
    public void afterActivityOnDestroy(Activity var1) {

    }
}
