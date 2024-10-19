package com.carlos.common.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;

public class AgreementsDialog extends BaseDialogFragment {
   public static Builder newBuilder() {
      Builder builder = new Builder();
      return builder;
   }

   public static AgreementsDialog newInstance(Builder builder) {
      AgreementsDialog dialog = new AgreementsDialog();
      return dialog;
   }

   protected View setView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(layout.dialog_agreement, container, false);
      TextView tvContent = (TextView)view.findViewById(id.tv_content);
      TextView tvConfirm = (TextView)view.findViewById(id.tv_confirm);
      TextView tvCancle = (TextView)view.findViewById(id.tv_cancle);
      tvConfirm.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            if (AgreementsDialog.this.mDialogResultListener != null) {
               AgreementsDialog.this.mDialogResultListener.result("");
            }

         }
      });
      tvCancle.setOnClickListener((v) -> {
         if (this.mDialogDismissListener != null) {
            this.mDialogDismissListener.dismiss(this);
         }

      });
      return view;
   }

   public static class Builder extends BaseDialogFragment.Builder<Builder, AgreementsDialog> {
      public AgreementsDialog build() {
         return AgreementsDialog.newInstance(this);
      }
   }
}
