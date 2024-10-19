package com.lody.virtual.server.secondary;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;

public class FakeIdentityBinder extends Binder {
   protected Binder mBase;
   protected int mFakeUid;
   protected int mFakePid;

   public FakeIdentityBinder(Binder binder, int fakeUid, int fakePid) {
      this.mBase = binder;
      this.mFakeUid = fakeUid;
      this.mFakePid = fakePid;
   }

   public void attachInterface(IInterface owner, String descriptor) {
      this.mBase.attachInterface(owner, descriptor);
   }

   public String getInterfaceDescriptor() {
      return this.mBase.getInterfaceDescriptor();
   }

   public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
      long identity = Binder.clearCallingIdentity();

      boolean var7;
      try {
         Binder.restoreCallingIdentity(this.getFakeIdentity());
         var7 = this.mBase.transact(code, data, reply, flags);
      } finally {
         Binder.restoreCallingIdentity(identity);
      }

      return var7;
   }

   public static long getIdentity(int uid, int pid) {
      return (long)uid << 32 | (long)pid;
   }

   public static void setSystemIdentity() {
      long identity = getIdentity(1000, Process.myPid());
      Binder.restoreCallingIdentity(identity);
   }

   public static void setIdentity(int uid, int pid) {
      long identity = getIdentity(uid, pid);
      Binder.restoreCallingIdentity(identity);
   }

   protected long getFakeIdentity() {
      return getIdentity(this.getFakeUid(), this.getFakePid());
   }

   protected int getFakeUid() {
      return this.mFakeUid;
   }

   protected int getFakePid() {
      return this.mFakePid;
   }

   public final IInterface queryLocalInterface(String descriptor) {
      return this.mBase.queryLocalInterface(descriptor);
   }

   public boolean pingBinder() {
      return this.mBase.pingBinder();
   }

   public boolean isBinderAlive() {
      return this.mBase.isBinderAlive();
   }

   public void linkToDeath(IBinder.DeathRecipient recipient, int flags) {
      this.mBase.linkToDeath(recipient, flags);
   }

   public boolean unlinkToDeath(IBinder.DeathRecipient recipient, int flags) {
      return this.mBase.unlinkToDeath(recipient, flags);
   }
}
