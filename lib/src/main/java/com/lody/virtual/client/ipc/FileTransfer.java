package com.lody.virtual.client.ipc;

import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.remote.FileInfo;
import com.lody.virtual.server.fs.IFileTransfer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileTransfer {
   private static final FileTransfer sInstance = new FileTransfer();
   private IFileTransfer mTransfer;

   public IFileTransfer getService() {
      if (!IInterfaceUtils.isAlive(this.mTransfer)) {
         Class var1 = FileTransfer.class;
         synchronized(FileTransfer.class) {
            Object remote = this.getStubInterface();
            this.mTransfer = (IFileTransfer)LocalProxyUtils.genProxy(IFileTransfer.class, remote);
         }
      }

      return this.mTransfer;
   }

   private Object getStubInterface() {
      return IFileTransfer.Stub.asInterface(ServiceManagerNative.getService("file-transfer"));
   }

   public static FileTransfer get() {
      return sInstance;
   }

   public ParcelFileDescriptor openFile(File file) {
      return this.openFile(file.getAbsolutePath());
   }

   public ParcelFileDescriptor openFile(String path) {
      try {
         return this.getService().openFile(path);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (ParcelFileDescriptor)VirtualRuntime.crash(e);
      }
   }

   public void copyFile(File remote, File local) {
      FileUtils.ensureDirCreate(local.getParentFile());
      ParcelFileDescriptor fd = this.openFile(remote);
      if (fd != null) {
         FileInputStream is = new ParcelFileDescriptor.AutoCloseInputStream(fd);

         try {
            FileUtils.writeToFile((InputStream)is, local);
         } catch (IOException var6) {
            IOException e = var6;
            e.printStackTrace();
         }

         FileUtils.closeQuietly(is);
      }
   }

   public void copyDirectory(File remoteDir, File localDir) {
      FileInfo[] remoteInfos = this.listFiles(remoteDir);
      if (remoteInfos != null) {
         FileUtils.ensureDirCreate(localDir);
         FileInfo[] var4 = remoteInfos;
         int var5 = remoteInfos.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            FileInfo remoteInfo = var4[var6];
            File remote = new File(remoteInfo.path);
            File local = new File(localDir, remote.getName());
            if (remoteInfo.isDirectory) {
               this.copyDirectory(remote, local);
            } else {
               this.copyFile(remote, local);
            }
         }

      }
   }

   public FileInfo[] listFiles(File dir) {
      return this.listFiles(dir.getPath());
   }

   public FileInfo[] listFiles(String path) {
      try {
         return this.getService().listFiles(path);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (FileInfo[])VirtualRuntime.crash(e);
      }
   }
}
