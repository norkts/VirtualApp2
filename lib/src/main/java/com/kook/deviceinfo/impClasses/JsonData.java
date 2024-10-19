package com.kook.deviceinfo.impClasses;

import android.content.Context;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.constant.SystemFileConStant;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonData {
   private String cpuFamily = "";
   private String process = "";
   private String memory = "";
   private String bandwidth = "";
   private String channels = "";
   private String json = null;
   private String machine;
   private final Context context;

   public JsonData(Context context) {
      this.context = context;
   }

   private void getDataFromJson() {
      IOException e;
      try {
         InputStream inputStream = this.context.getAssets().open("socList.json");
         int size = inputStream.available();
         byte[] bytes = new byte[size];
         inputStream.read(bytes);
         inputStream.close();
         this.json = new String(bytes);
      } catch (IOException var5) {
         e = var5;
         e.printStackTrace();
      }

      String str;
      try {
         for(BufferedReader bufferedReader = new BufferedReader(new FileReader(SystemFileConStant.SOC_MACHINE)); (str = bufferedReader.readLine()) != null; this.machine = str) {
         }
      } catch (IOException var6) {
         e = var6;
         this.machine = "error";
         e.printStackTrace();
      }

      if (this.json != null) {
         try {
            JSONObject jsonObject = new JSONObject(this.json);
            HVLog.e("machine:" + this.machine + "   " + jsonObject.isNull(this.machine));
            if (!jsonObject.isNull(this.machine)) {
               JSONObject object = jsonObject.getJSONObject(this.machine);
               if (!object.getString("CPU").equals("")) {
                  this.cpuFamily = object.getString("CPU");
               }

               if (!object.getString("FAB").equals("")) {
                  this.process = object.getString("FAB");
               }

               this.memory = object.getString("MEMORY");
               this.bandwidth = object.getString("BANDWIDTH");
               this.channels = object.getString("CHANNELS");
            }
         } catch (JSONException var4) {
            var4.printStackTrace();
         }
      }

   }

   public String getCpuFamily() {
      return this.cpuFamily;
   }

   public String getProcess() {
      return this.process;
   }

   public String getMemory() {
      return this.memory;
   }

   public String getBandwidth() {
      return this.bandwidth;
   }

   public String getChannels() {
      return this.channels;
   }
}
