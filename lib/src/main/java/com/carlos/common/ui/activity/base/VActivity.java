package com.carlos.common.ui.activity.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import com.carlos.common.utils.ResponseProgram;
import org.jdeferred.android.AndroidDeferredManager;

public abstract class VActivity extends BaseActivity {
   Handler mHandler = new Handler();

   public Activity getActivity() {
      return this;
   }

   public Context getContext() {
      return this;
   }

   protected AndroidDeferredManager defer() {
      return ResponseProgram.defer();
   }

   public Fragment findFragmentById(@IdRes int id) {
      return this.getSupportFragmentManager().findFragmentById(id);
   }

   public void replaceFragment(@IdRes int id, Fragment fragment) {
      this.getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
   }

   protected <T extends View> T bind(int id) {
      return this.findViewById(id);
   }

   public void enableBackHome() {
      ActionBar actionBar = this.getSupportActionBar();
      if (actionBar != null) {
         actionBar.setDisplayHomeAsUpEnabled(true);
      }

   }

   protected void onStart() {
      super.onStart();
   }

   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case 16908332:
            this.finish();
         default:
            return super.onOptionsItemSelected(item);
      }
   }

   protected void onStop() {
      super.onStop();
   }

   public Handler getHandler() {
      return this.mHandler;
   }
}
