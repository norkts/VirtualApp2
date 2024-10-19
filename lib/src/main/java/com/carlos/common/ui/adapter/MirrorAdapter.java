package com.carlos.common.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.carlos.common.App;
import com.carlos.common.ui.activity.base.BaseActivity;
import com.carlos.common.ui.adapter.bean.MirrorData;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.ipc.VDeviceManager;
import com.lody.virtual.client.ipc.VLocationManager;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.remote.vloc.VLocation;
import java.util.List;

public class MirrorAdapter extends RecyclerView.Adapter<ViewHolder> {
   private int VIEW_TYPE_NORMAL = 0;
   private int VIEW_TYPE_TV = 1;
   private int VIEW_TYPE_BTN = 2;
   private LayoutInflater mInflater;
   private List<MirrorData> mList;
   private BaseActivity mBaseActivity;
   private OnAppClickListener mAppClickListener;
   String packageName;
   int userId;

   public MirrorAdapter(BaseActivity context, String packageName, int userId) {
      this.mBaseActivity = context;
      this.mInflater = LayoutInflater.from(context);
      this.packageName = packageName;
      this.userId = userId;
   }

   String getString(int resId) {
      return this.mBaseActivity.getString(resId);
   }

   public void add(MirrorData data) {
      int insertPos = this.getItemCount();
      this.mList.add(insertPos, data);
      this.notifyItemInserted(insertPos);
   }

   public void replace(int index, MirrorData data) {
      this.mList.set(index, data);
      this.notifyItemChanged(index);
   }

   public void remove(MirrorData data) {
      if (this.mList.remove(data)) {
         this.notifyDataSetChanged();
      }

   }

   public int getItemViewType(int position) {
      MirrorData mirrorData = (MirrorData)this.mList.get(position);
      if (mirrorData.getItemLayoutRes() == layout.activity_mirror_item_menu) {
         return this.VIEW_TYPE_TV;
      } else {
         return mirrorData.getItemLayoutRes() == layout.activity_mirror_item_menu_btn2 ? this.VIEW_TYPE_BTN : this.VIEW_TYPE_NORMAL;
      }
   }

   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if (viewType == this.VIEW_TYPE_BTN) {
         return new ViewHolder(this.mInflater.inflate(layout.activity_mirror_item_menu_btn2, (ViewGroup)null));
      } else {
         return viewType == this.VIEW_TYPE_TV ? new ViewHolder(this.mInflater.inflate(layout.activity_mirror_item_menu, (ViewGroup)null)) : new ViewHolder(this.mInflater.inflate(layout.activity_mirror_item_menu, (ViewGroup)null));
      }
   }

   public void onBindViewHolder(ViewHolder holder, int position) {
      int itemViewType = this.getItemViewType(position);
      MirrorData data = (MirrorData)this.mList.get(position);
      holder.menu_icon.setImageResource(data.getIcon());
      holder.menu_title.setText(data.getTitle());
      if (itemViewType == this.VIEW_TYPE_TV) {
         holder.menu_sub_title.setText(data.getSubTitle());
      }

      if (itemViewType == this.VIEW_TYPE_BTN) {
         holder.menu_action_btn2.setText(data.getSubTitle());
         holder.menu_action_btn2.setOnClickListener((v) -> {
            if (this.mAppClickListener != null) {
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cOWoFAiljICRF")) + position);
               this.mAppClickListener.onAppClick(position, data, (String)holder.menu_action_btn2.getTag());
            }

         });
      }

      holder.menu_action_btn.setText(data.getActionBtn());
      holder.menu_action_btn.setOnClickListener((v) -> {
         if (this.mAppClickListener != null) {
            HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cOWoFAiljICRF")) + position);
            this.mAppClickListener.onAppClick(position, data, (String)holder.menu_action_btn.getTag());
         }

      });
      switch (data.getMenuType()) {
         case 0:
            this.setWifiaddr(data, holder);
            break;
         case 1:
            this.setMockLocation(holder);
            break;
         case 2:
            BaseActivity var10000 = this.mBaseActivity;
            int deviceId = BaseActivity.getDeviceId(this.packageName, this.userId);
            VDeviceConfig deviceConfig = VDeviceManager.get().getDeviceConfig(deviceId);
            boolean enable = VDeviceManager.get().isEnable(deviceId);
            if (enable) {
               holder.menu_sub_title.setText(string.is_mock);
            } else {
               holder.menu_sub_title.setText(string.no_mock);
            }
         case 3:
      }

   }

   public void setWifiaddr(MirrorData mirrorData, ViewHolder holder) {
      String wifi_addr = this.getString(string.wifi_addr);
      SettingConfig.FakeWifiStatus fakeWifiStatus = App.getApp().mConfig.getFakeWifiStatus(this.packageName, this.userId);
      if (fakeWifiStatus == null) {
         holder.menu_sub_title.setText(string.no_mock);
      } else {
         holder.menu_sub_title.setText(String.format(wifi_addr, fakeWifiStatus.getSSID(), fakeWifiStatus.getMAC()));
      }

   }

   public void setMockLocation(ViewHolder holder) {
      VLocation location = VLocationManager.get().getLocation(this.packageName, this.userId);
      if (location == null) {
         holder.menu_sub_title.setText(string.no_mock);
      } else {
         double latitude = location.getLatitude();
         double longitude = location.getLongitude();
         String mock_location = this.getString(string.mock_location);
         holder.menu_sub_title.setText(String.format(mock_location, longitude, latitude));
      }

   }

   public int getItemCount() {
      return this.mList == null ? 0 : this.mList.size();
   }

   public List<MirrorData> getList() {
      return this.mList;
   }

   public void setList(List<MirrorData> list) {
      this.mList = list;
      this.notifyDataSetChanged();
   }

   public void setAppClickListener(OnAppClickListener mAppClickListener) {
      this.mAppClickListener = mAppClickListener;
   }

   public void moveItem(int pos, int targetPos) {
      MirrorData model = (MirrorData)this.mList.remove(pos);
      this.mList.add(targetPos, model);
      this.notifyItemMoved(pos, targetPos);
   }

   public void refresh(MirrorData model) {
      int index = this.mList.indexOf(model);
      if (index >= 0) {
         this.notifyItemChanged(index);
      }

   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      AppCompatImageView menu_icon;
      AppCompatTextView menu_title;
      AppCompatTextView menu_sub_title;
      AppCompatTextView menu_action_btn;
      AppCompatTextView menu_action_btn2;

      ViewHolder(View itemView) {
         super(itemView);
         this.menu_icon = (AppCompatImageView)itemView.findViewById(id.menu_icon);
         this.menu_title = (AppCompatTextView)itemView.findViewById(id.menu_title);
         this.menu_sub_title = (AppCompatTextView)itemView.findViewById(id.menu_sub_title);
         this.menu_action_btn = (AppCompatTextView)itemView.findViewById(id.menu_action_btn);
         this.menu_action_btn2 = (AppCompatTextView)itemView.findViewById(id.menu_action_btn2);
      }
   }

   public interface OnAppClickListener {
      void onAppClick(int var1, MirrorData var2, String var3);
   }
}
