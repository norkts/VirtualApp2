package com.carlos.common.ui.adapter.bean;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.string;
import java.util.ArrayList;
import java.util.List;

public class MirrorData {
   public static List<MirrorData> mirrorDataList = new ArrayList();
   int MENU_TYPE;
   @DrawableRes
   int icon;
   @StringRes
   int title;
   @StringRes
   int subTitle;
   @StringRes
   int actionBtn;
   @LayoutRes
   int itemLayoutRes;

   public MirrorData(@DrawableRes int icon, @StringRes int title, @StringRes int subTitle, @StringRes int actionBtn, @LayoutRes int layout, int menuType) {
      this.icon = icon;
      this.title = title;
      this.subTitle = subTitle;
      this.actionBtn = actionBtn;
      this.MENU_TYPE = menuType;
      this.itemLayoutRes = layout;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public int getTitle() {
      return this.title;
   }

   public void setTitle(int title) {
      this.title = title;
   }

   public int getSubTitle() {
      return this.subTitle;
   }

   public void setSubTitle(int subTitle) {
      this.subTitle = subTitle;
   }

   public int getActionBtn() {
      return this.actionBtn;
   }

   public void setActionBtn(int actionBtn) {
      this.actionBtn = actionBtn;
   }

   public int getMenuType() {
      return this.MENU_TYPE;
   }

   public void setMenuType(int menuType) {
      this.MENU_TYPE = menuType;
   }

   public int getItemLayoutRes() {
      return this.itemLayoutRes;
   }

   public void setItemLayoutRes(int itemLayoutRes) {
      this.itemLayoutRes = itemLayoutRes;
   }

   static {
      mirrorDataList.add(new MirrorData(drawable.icon_loc, string.virtual_location, string.no_mock, string.activity_choose_location, layout.activity_mirror_item_menu, 1));
      mirrorDataList.add(new MirrorData(drawable.icon_wifi, string.menu_mock_wifi, string.no_mock, string.settings, layout.activity_mirror_item_menu, 0));
      mirrorDataList.add(new MirrorData(drawable.icon_phone, string.menu_mock_phone, string.menu_mock_wifi, string.fake_device_info, layout.activity_mirror_item_menu, 2));
      mirrorDataList.add(new MirrorData(drawable.icon_delete, string.application_manager, string.delete, string.clear, layout.activity_mirror_item_menu_btn2, 4));
      mirrorDataList.add(new MirrorData(drawable.icon_shortcut, string.create_shortcut, string.create_desktop_icon, string.create, layout.activity_mirror_item_menu, 3));
      mirrorDataList.add(new MirrorData(drawable.icon_bure, string.backup_recovery_title, string.recovery, string.backup, layout.activity_mirror_item_menu_btn2, 5));
   }
}
