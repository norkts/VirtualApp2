package com.carlos.common.clouddisk;

import android.content.Context;
import android.text.TextUtils;
import com.carlos.common.clouddisk.http.FileProgressRequestBody;
import com.carlos.common.clouddisk.http.HttpWorker;
import com.carlos.common.clouddisk.http.MyCookieJar;
import com.carlos.common.clouddisk.http.OkHttpUtil;
import com.carlos.common.clouddisk.listview.FileItem;
import com.carlos.common.ui.UIConstant;
import com.carlos.common.ui.utils.LanzouHelper;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jdeferred.Promise;

public class ClouddiskLauncher {
   private static ClouddiskLauncher mClouddiskLauncher;
   private List<String> historyDir = new ArrayList();
   private List<FileItem> currentFolder = new ArrayList();
   static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

   private ClouddiskLauncher() {
   }

   public static ClouddiskLauncher getInstance() {
      if (mClouddiskLauncher == null) {
         Class var0 = ClouddiskLauncher.class;
         synchronized(ClouddiskLauncher.class) {
            if (mClouddiskLauncher == null) {
               mClouddiskLauncher = new ClouddiskLauncher();
            }
         }
      }

      return mClouddiskLauncher;
   }

   public static String getCurrentDate() {
      return mSimpleDateFormat.format(new Date());
   }

   public static String getCurrentDate(String date) {
      long time = Long.parseLong(date);
      return mSimpleDateFormat.format(new Date(time));
   }

   public Promise<Void, Throwable, Void> updaterCloud(String uploadFilePath, String folder_id, HttpWorker.UpLoadCallbackListener listener) {
      return ResponseProgram.defer().when(() -> {
         try {
            HttpWorker.getInstance().uploadFileSync(uploadFilePath, folder_id, listener);
         } catch (Exception var4) {
            Exception e = var4;
            HVLog.printException(e);
         }

      });
   }

   public Promise<Void, Throwable, Void> updaterCloud(String uploadFilePath, String uploadFileName, String folder_id, HttpWorker.UpLoadCallbackListener listener) {
      return ResponseProgram.defer().when(() -> {
         try {
            HttpWorker.getInstance().uploadFileSync(uploadFilePath, folder_id, listener);
            OkHttpClient client = OkHttpUtil.getmOkHttpClient2();
            File file = new File(uploadFilePath);
            RequestBody fileBody = new FileProgressRequestBody("application/x-www-form-urlencoded;charset=utf-8", file, new FileProgressRequestBody.ProgressListener() {
               public void transferred(double size) {
                  listener.Progress(size);
               }
            });
            MultipartBody mBody = (new MultipartBody.Builder()).setType(MultipartBody.FORM).addFormDataPart("size", String.valueOf(file.length())).addFormDataPart("task", "1").addFormDataPart("folder_id", folder_id).addFormDataPart("name", uploadFileName).addFormDataPart("upload_file", uploadFileName, fileBody).build();
            Request request = (new Request.Builder()).url("https://pc.woozooo.com/fileup.php").post(mBody).build();
            Response response = client.newCall(request).execute();
            String var11 = response.body().string();
         } catch (Exception var12) {
            Exception e = var12;
            HVLog.printException(e);
         }

      });
   }

   public Promise<LanzouHelper.Lanzou, Throwable, Void> downFileByCloud(String fileId) {
      return ResponseProgram.defer().when(() -> {
         try {
            String fileHrefSync = HttpWorker.getInstance().getFileHrefSync(fileId);
            LanzouHelper.Lanzou lanZouRealLink = LanzouHelper.getLanZouRealLink(fileHrefSync);
            return lanZouRealLink;
         } catch (Exception var3) {
            Exception e = var3;
            HVLog.printException(e);
            return null;
         }
      });
   }

   public void launcherCloud(Context context) {
      if (this.historyDir.size() == 0) {
         MyCookieJar.resetCookies();
      }

      HttpWorker.Login("18117395833", "w329716228", new HttpWorker.loginCallbackListener() {
         public void onError(Throwable e) {
         }

         public void onFinish() {
            HVLog.d("已经成功介入蓝奏云盘 " + ClouddiskLauncher.this.historyDir.size());
            ClouddiskLauncher.this.historyDir.clear();
            String[] dateDir = new String[]{UIConstant.CLOUD_DISK_BACKUP_RECOVERY_DIRECTORY, ClouddiskLauncher.getCurrentDate()};
            ClouddiskLauncher.this.openPageByDirectory(dateDir, UIConstant.CLOUD_DISK_ROOT_ID, 0).done((re) -> {
               HVLog.d("打开目录已经完成 1111 ");
            });
         }
      });
   }

   private Promise<List<FileItem>, Throwable, Void> openPageByDirectory(String[] dirTrees, String folder_id, int treeIndex) {
      return ResponseProgram.defer().when(() -> {
         this.historyDir.add(folder_id);
         return HttpWorker.getInstance().getFolderInfoSync(folder_id);
      }).done((fileItemList) -> {
         if (fileItemList != null) {
            if (treeIndex >= dirTrees.length) {
               HVLog.d("最后目录情况:" + fileItemList + "    folder_id:" + folder_id);
               Iterator var7 = this.currentFolder.iterator();

               while(var7.hasNext()) {
                  FileItem fileItem = (FileItem)var7.next();
                  HVLog.d("最后目录 fileItem:" + fileItem.toString());
               }

               return;
            }

            this.currentFolder = fileItemList;
            HVLog.d("fileItemList:" + fileItemList);
            String directoryName = dirTrees[treeIndex];
            HVLog.d("directoryName:" + directoryName + "    treeIndex:" + treeIndex + "    dirTrees:" + dirTrees.length);
            String folderId = this.getCloudDiskFolderIdByDirectoryName(fileItemList, directoryName);
            if (TextUtils.isEmpty(folderId)) {
               HVLog.d("创建目录 " + directoryName + "    是在" + folder_id + "下面创建的");
               this.createPage(folder_id, directoryName).done((create) -> {
                  HVLog.d("创建目录成功 " + treeIndex + "    " + dirTrees.length);
                  if (treeIndex < dirTrees.length) {
                     this.openPageByDirectory(dirTrees, folderId, treeIndex + 1);
                  }

               });
            } else if (treeIndex < dirTrees.length) {
               this.openPageByDirectory(dirTrees, folderId, treeIndex + 1);
            }
         }

      });
   }

   public void launcherCloud(Context context, final String currentDate, final CloudFileCallback cloudFileCallback) {
      if (this.historyDir.size() == 0) {
         MyCookieJar.resetCookies();
      }

      HttpWorker.Login("18117395833", "w329716228", new HttpWorker.loginCallbackListener() {
         public void onError(Throwable e) {
         }

         public void onFinish() {
            HVLog.d("已经成功介入蓝奏云盘 " + ClouddiskLauncher.this.historyDir.size());
            ClouddiskLauncher.this.historyDir.clear();
            String[] dateDir = new String[]{UIConstant.CLOUD_DISK_BACKUP_RECOVERY_DIRECTORY, currentDate};
            ClouddiskLauncher.this.openPageByDirectoryFile(dateDir, UIConstant.CLOUD_DISK_ROOT_ID, 0, cloudFileCallback);
         }
      });
   }

   public void launcherCloudByAppcation(Context context, final CloudFileCallback cloudFileCallback) {
      if (this.historyDir.size() == 0) {
         MyCookieJar.resetCookies();
      }

      HttpWorker.Login("18117395833", "w329716228", new HttpWorker.loginCallbackListener() {
         public void onError(Throwable e) {
         }

         public void onFinish() {
            HVLog.d("已经成功介入蓝奏云盘 " + ClouddiskLauncher.this.historyDir.size());
            ClouddiskLauncher.this.historyDir.clear();
            String[] dateDir = new String[]{UIConstant.CLOUD_DISK_BACKUP_APPLICATION_DIRECTORY};
            ClouddiskLauncher.this.openPageByDirectoryFile(dateDir, UIConstant.CLOUD_DISK_ROOT_ID, 0, cloudFileCallback);
         }
      });
   }

   public Promise<List<FileItem>, Throwable, Void> openPageByDirectoryFile(String[] dirTrees, String folder_id, int treeIndex, CloudFileCallback cloudFileCallback) {
      return ResponseProgram.defer().when(() -> {
         this.historyDir.add(folder_id);
         return HttpWorker.getInstance().getFolderInfoSync(folder_id);
      }).done((fileItemList) -> {
         if (fileItemList != null) {
            if (treeIndex >= dirTrees.length) {
               HVLog.d("最后目录情况:" + fileItemList + "    folder_id:" + folder_id);
               ResponseProgram.defer().when(() -> {
                  try {
                     List<FileItem> fileInfoSync = HttpWorker.getInstance().getFileInfoSync(folder_id);
                     HVLog.d("文件数量： " + fileInfoSync.size());
                     cloudFileCallback.callback(fileInfoSync);
                  } catch (Exception var3) {
                     Exception e = var3;
                     HVLog.printException(e);
                  }

               });
               return;
            }

            this.currentFolder = fileItemList;
            HVLog.d("fileItemList:" + fileItemList);
            String directoryName = dirTrees[treeIndex];
            HVLog.d("directoryName:" + directoryName + "    treeIndex:" + treeIndex + "    dirTrees:" + dirTrees.length);
            String folderId = this.getCloudDiskFolderIdByDirectoryName(fileItemList, directoryName);
            if (treeIndex < dirTrees.length) {
               this.openPageByDirectoryFile(dirTrees, folderId, treeIndex + 1, cloudFileCallback);
            }
         }

      });
   }

   public List<FileItem> getCurrentFolder() {
      return this.currentFolder;
   }

   public String getCurrentDateFolderId() {
      String currentDate = getCurrentDate();
      String folderId = this.getCloudDiskFolderIdByDirectoryName(this.currentFolder, currentDate);
      return folderId;
   }

   protected Promise<List<FileItem>, Throwable, Void> openPage(String folder_id) {
      HVLog.i("打开目录的 folder_id:" + folder_id);
      this.historyDir.add(folder_id);
      return ResponseProgram.defer().when(() -> {
         try {
            return HttpWorker.getInstance().getFolderInfoSync(folder_id);
         } catch (Exception var2) {
            Exception e = var2;
            HVLog.printException(e);
            return null;
         }
      });
   }

   public Promise<Boolean, Throwable, Void> createPage(String uri, String folder_name) {
      this.historyDir.add(uri);
      return ResponseProgram.defer().when(() -> {
         try {
            return HttpWorker.getInstance().setNewFolderSync(uri, folder_name);
         } catch (Exception var3) {
            Exception e = var3;
            HVLog.printException(e);
            return false;
         }
      });
   }

   public String getCloudDiskFolderIdByDirectoryName(List<FileItem> fileItemList, String directoryName) {
      Iterator var3 = this.currentFolder.iterator();

      FileItem fileItem;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         fileItem = (FileItem)var3.next();
         HVLog.i("遍历当前目录:" + fileItem.toString());
      } while(!directoryName.equals(fileItem.getFilename()));

      return fileItem.getId();
   }

   public interface CloudFileCallback {
      void callback(List<FileItem> var1);
   }
}
