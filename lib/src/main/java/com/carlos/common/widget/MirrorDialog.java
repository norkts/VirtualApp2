package com.carlos.common.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.kook.librelease.R.style;

public class MirrorDialog {
   Dialog mDialog = null;
   AlertDialog.Builder mBuilder;
   static MirrorDialog mMirrorDialog;

   private MirrorDialog() {
   }

   public static MirrorDialog getInstance() {
      if (mMirrorDialog == null) {
         Class var0 = MirrorDialog.class;
         synchronized(MirrorDialog.class) {
            if (mMirrorDialog == null) {
               mMirrorDialog = new MirrorDialog();
            }
         }
      }

      return mMirrorDialog;
   }

   public void showBakupAndRecovery(Activity activity, String content, BakupAndRecoveryClickListener onClickListener) {
      this.mBuilder = new AlertDialog.Builder(activity, style.VACustomTheme);
      View view1 = activity.getLayoutInflater().inflate(layout.dialog_mirror_backup, (ViewGroup)null);
      this.mBuilder.setView(view1);
      if (!activity.isFinishing()) {
         this.mDialog = this.mBuilder.show();
      }

      if (this.mDialog != null) {
         this.mDialog.setCanceledOnTouchOutside(false);
         TextView textView = (TextView)view1.findViewById(id.tips_content);
         textView.setText(content);
         this.mDialog.setCancelable(false);
         view1.findViewById(id.double_btn_layout).setVisibility(0);
         TextProgressBar textProgressBar = (TextProgressBar)view1.findViewById(id.progress_bar);
         textProgressBar.setVisibility(4);
         view1.findViewById(id.btn_cancel).setOnClickListener((view) -> {
            this.mDialog.dismiss();
         });
         view1.findViewById(id.btn_ok).setOnClickListener((view) -> {
            onClickListener.onClick(view, this.mDialog, textProgressBar);
         });
      }
   }

   public void tipsSingleDialog(Activity activity, String content, SingleDialogClickListener onclick) {
      this.mBuilder = new AlertDialog.Builder(activity, style.VACustomTheme);
      View view1 = activity.getLayoutInflater().inflate(layout.dialog_tips, (ViewGroup)null);
      this.mBuilder.setView(view1);
      if (!activity.isFinishing()) {
         this.mDialog = this.mBuilder.show();
      }

      if (this.mDialog != null) {
         this.mDialog.setCanceledOnTouchOutside(false);
         TextView textView = (TextView)view1.findViewById(id.tips_content);
         textView.setText(content);
         this.mDialog.setCancelable(false);
         view1.findViewById(id.single_btn_layout).setVisibility(0);
         view1.findViewById(id.single_btn).setOnClickListener((view) -> {
            onclick.onClick(view, this.mDialog);
         });
      }
   }

   public void singleRecoveryInputDialog(Activity activity, String content, SingleInputDialogClickListener listener) {
      AlertDialog.Builder builder = new AlertDialog.Builder(activity, style.VACustomTheme);
      View view1 = activity.getLayoutInflater().inflate(layout.dialog_single_input, (ViewGroup)null);
      builder.setView(view1);
      Dialog dialog = builder.show();
      ((Dialog)dialog).setCanceledOnTouchOutside(false);
      TextView textView = (TextView)view1.findViewById(id.tips_content);
      textView.setText(content);
      AppCompatEditText editText1 = (AppCompatEditText)view1.findViewById(id.edit_code);
      TextProgressBar textProgressBar = (TextProgressBar)view1.findViewById(id.progress_bar);
      textProgressBar.setVisibility(4);
      ((Dialog)dialog).setCancelable(false);
      view1.findViewById(id.btn_cancel).setOnClickListener((v2) -> {
         dialog.dismiss();
      });
      view1.findViewById(id.btn_ok).setOnClickListener((v2) -> {
         listener.onClick(v2, editText1, dialog, textProgressBar);
      });
   }

   public interface SingleInputDialogClickListener {
      void onClick(View var1, EditText var2, Dialog var3, TextProgressBar var4);
   }

   public interface SingleDialogClickListener {
      void onClick(View var1, Dialog var2);
   }

   public interface BakupAndRecoveryClickListener {
      void onClick(View var1, Dialog var2, TextProgressBar var3);
   }
}
