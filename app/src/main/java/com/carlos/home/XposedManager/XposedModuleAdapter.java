package com.carlos.home.XposedManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//import com.lody.virtual.server.bit64.V64BitHelper;

import java.util.List;


import com.carlos.R;
import com.carlos.common.AppComponentDelegate;
import com.carlos.home.models.AppData;
import com.carlos.home.models.MultiplePackageAppData;
import com.carlos.home.models.PackageAppData;
import com.carlos.home.repo.AppRepository;
import com.lody.virtual.client.core.AppCallback;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.remote.ClientConfig;

public class XposedModuleAdapter extends RecyclerView.Adapter<XposedModuleAdapter.ViewHolder> {
    public static final String PASS_PKG_NAME_ARGUMENT = "MODEL_ARGUMENT";
    public static final String PASS_KEY_INTENT = "KEY_INTENT";
    public static final String PASS_KEY_USER = "KEY_USER";

    CardView cardView;

    private Context context;
    private LayoutInflater mInflater;
    private List<AppData> modules;
    private AppRepository repository;

//    @InjectComponent
//    XposedConfig config;

    public XposedModuleAdapter(Context context, AppRepository repository, List<AppData> modules) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.modules = modules;
        this.repository = repository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_module, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(modules.get(i));
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;
        TextView desc;
        TextView version;
        CheckBox enable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            version = itemView.findViewById(R.id.version_name);
            enable = itemView.findViewById(R.id.checkbox);
        }

        public void bind(AppData data) {
            icon.setImageDrawable(data.getIcon());
            title.setText(data.getName());
            version.setText(data.getVersionName());
            enable.setChecked(true);
//            enable.setOnCheckedChangeListener((compoundButton, b) -> config.enableModule(data.getPackageName(), b));
            /*if (data.getXposedModule() != null) {
                //desc.setText(data.getXposedModule().desc);
            }*/
            if (data.canLaunch()) {
                itemView.setOnClickListener(view -> launchModule(data));
            } else {
                itemView.setOnClickListener(null);
            }
            if (data.canDelete()) {
                itemView.setOnLongClickListener(view -> deleteModule(data));
            } else {
                itemView.setOnLongClickListener(null);
            }
        }

        void launchModule(AppData data) {
            try {
                if (data instanceof PackageAppData) {
                    PackageAppData appData = (PackageAppData) data;
                    appData.isFirstOpen = false;
//                    LoadingActivity.launch(context, appData.packageName, 0);
                    launchApp(data);
                } else if (data instanceof MultiplePackageAppData) {
                    MultiplePackageAppData multipleData = (MultiplePackageAppData) data;
                    multipleData.isFirstOpen = false;
//                    LoadingActivity.launch(context, multipleData.appInfo.packageName, ((MultiplePackageAppData) data).userId);
                    launchApp(data);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        boolean deleteModule(AppData data) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Module")
                    .setMessage("Do you want to delete " + data.getName() + "?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        try {
                            if (data instanceof PackageAppData) {
                                repository.removeVirtualApp(((PackageAppData) data).packageName, 0);
                            } else {
                                MultiplePackageAppData appData = (MultiplePackageAppData) data;
                                repository.removeVirtualApp(appData.appInfo.packageName, appData.userId);
                            }
                            modules.remove(data);
                            notifyDataSetChanged();
                        } catch (Throwable throwable) {

                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return false;
        }

    }
    public void launchApp(AppData data) {
        try {
            int userId = data.getUserId();
            String packageName = data.getPackageName();
            if (userId != -1 && packageName != null) {
                boolean runAppNow = true;
                /*if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(packageName, userId);
                    ApplicationInfo applicationInfo = info.getApplicationInfo(userId);
                    boolean is64bit = VirtualCore.get().isRun64BitProcess(info.packageName);
                    if (is64bit) {
                        if (check64bitEnginePermission()) {
                            return;
                        }
                    }
                    if (PermissionCompat.isCheckPermissionRequired(applicationInfo)) {
                        String[] permissions = VPackageManager.get().getDangrousPermissions(info.packageName);
                        if (!PermissionCompat.checkPermissions(permissions, is64bit)) {
                            runAppNow = false;
                            PermissionRequestActivity.requestPermission((Activity) context, permissions, data.getName(), userId, packageName, REQUEST_PERMISSION);
                        }
                    }
                }*/
                if (runAppNow) {
                    data.isFirstOpen = false;
                    launchApp(userId, packageName);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

/*    public boolean check64bitEnginePermission() {
        if (VirtualCore.get().is64BitEngineInstalled()) {
            if (!V64BitHelper.has64BitEngineStartPermission()) {
                return true;
            }
        }
        return false;
    }*/
    private void launchApp(int userId, String packageName) {
//        if (VirtualCore.get().isRun64BitProcess(packageName)) {
//            if (!VirtualCore.get().is64BitEngineInstalled()) {
//                Toast.makeText(context, "Please install 64bit engine.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (!V64BitHelper.has64BitEngineStartPermission()) {
//                Toast.makeText(context, "No Permission to start 64bit engine.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//        LoadingActivity.launch(context,packageName,userId);
        launchApp(userId, packageName,true);

    }


    public boolean launchApp(final int userId, final String packageName, boolean preview) {
        Context context = VirtualCore.get().getContext();
        VPackageManager pm = VPackageManager.get();
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        intentToResolve.addCategory(Intent.CATEGORY_INFO);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(context), 0, userId);

        // Otherwise, try to find a main launcher activity.
        if (ris == null || ris.size() <= 0) {
            // reuse the intent instance
            intentToResolve.removeCategory(Intent.CATEGORY_INFO);
            intentToResolve.addCategory(Intent.CATEGORY_LAUNCHER);
            intentToResolve.setPackage(packageName);
            ris = pm.queryIntentActivities(intentToResolve, intentToResolve.resolveType(context), 0, userId);
        }
        if (ris == null || ris.size() <= 0) {
            return false;
        }
        final ActivityInfo info = ris.get(0).activityInfo;
        final Intent intent = new Intent(intentToResolve);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(info.packageName, info.name);
        //1.va的进程初始化500ms
        //2.app的Application初始化，这个要看app
        //3.app的4组件初始化
        if (!preview || VActivityManager.get().isAppRunning(info.packageName, userId, true)) {
            VLog.d("kk", "app's main thread was running.");
            VActivityManager.get().startActivity(intent, userId);
        } else {
            VLog.d("kk", "app's main thread not running.");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            final String processName = ComponentUtils.getProcessName(info);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //wait 500ms
                    ClientConfig clientConfig = initProcess(packageName, processName, userId);
                    if (clientConfig != null) {
                        VActivityManager.get().startActivity(intent, userId);
                        //VActivityManager#startActivity启动速度比WindowPreviewActivity快
                    }
                }
            }).start();
        }
        return true;
    }

    public ClientConfig initProcess(String packageName, String processName, int userId) {
        try {
            return VActivityManager.get().getService().initProcess(packageName, processName, userId);
        } catch (RemoteException e) {
            return VirtualRuntime.crash(e);
        }
    }
}
