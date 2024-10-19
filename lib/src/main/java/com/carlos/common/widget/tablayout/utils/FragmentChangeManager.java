package com.carlos.common.widget.tablayout.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.Iterator;

public class FragmentChangeManager {
   private FragmentManager mFragmentManager;
   private int mContainerViewId;
   private ArrayList<Fragment> mFragments;
   private int mCurrentTab;

   public FragmentChangeManager(FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments) {
      this.mFragmentManager = fm;
      this.mContainerViewId = containerViewId;
      this.mFragments = fragments;
      this.initFragments();
   }

   private void initFragments() {
      Iterator var1 = this.mFragments.iterator();

      while(var1.hasNext()) {
         Fragment fragment = (Fragment)var1.next();
         this.mFragmentManager.beginTransaction().add(this.mContainerViewId, fragment).hide(fragment).commit();
      }

      this.setFragments(0);
   }

   public void setFragments(int index) {
      for(int i = 0; i < this.mFragments.size(); ++i) {
         FragmentTransaction ft = this.mFragmentManager.beginTransaction();
         Fragment fragment = (Fragment)this.mFragments.get(i);
         if (i == index) {
            ft.show(fragment);
         } else {
            ft.hide(fragment);
         }

         ft.commit();
      }

      this.mCurrentTab = index;
   }

   public int getCurrentTab() {
      return this.mCurrentTab;
   }

   public Fragment getCurrentFragment() {
      return (Fragment)this.mFragments.get(this.mCurrentTab);
   }
}
