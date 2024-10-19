package com.carlos.science;

import android.content.Context;

public interface LocationService {
   void onLocationChanged(int var1, int var2);

   int[] onRestoreLocation();

   void start(Context var1);
}
