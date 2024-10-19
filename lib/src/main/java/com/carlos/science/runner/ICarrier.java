package com.carlos.science.runner;

import android.content.Context;

public interface ICarrier {
   Context getContext();

   void onMove(int var1, int var2, int var3, int var4);

   void onDone();

   boolean post(Runnable var1);

   boolean removeCallbacks(Runnable var1);
}
