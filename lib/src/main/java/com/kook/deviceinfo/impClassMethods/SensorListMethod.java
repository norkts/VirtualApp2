package com.kook.deviceinfo.impClassMethods;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.kook.common.utils.HVLog;
import com.kook.deviceinfo.impClasses.BuildInfo;
import com.kook.deviceinfo.models.SensorListModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SensorListMethod {
   private final Context context;
   List<Sensor> sensors = new ArrayList();
   ArrayList<SensorListModel> sensorList = new ArrayList();
   SensorManager sm;
   BuildInfo buildInfo;

   public SensorListMethod(Context context) {
      this.context = context;
      this.sm = (SensorManager)context.getSystemService("sensor");
      this.sensorInfo();
   }

   private void sensorInfo() {
      this.buildInfo = new BuildInfo(this.context);
      this.sensors = this.sm.getSensorList(-1);
      Iterator var1 = this.sensors.iterator();

      while(var1.hasNext()) {
         Sensor s = (Sensor)var1.next();
         int id = s.getId();
         String name = s.getName();
         String vendor = s.getVendor();
         String stringType = s.getStringType();
         float power = s.getPower();
         int version = s.getVersion();
         float resolution = s.getResolution();
         float maximumRange = s.getMaximumRange();
         int fifoMaxEventCount = s.getFifoMaxEventCount();
         int fifoReservedEventCount = s.getFifoReservedEventCount();
         int maxDelay = s.getMaxDelay();
         int minDelay = s.getMinDelay();
         int reportingMode = s.getReportingMode();
         SensorListModel sensorListModel = new SensorListModel(id, name, vendor, stringType, power, version, resolution, maximumRange, fifoMaxEventCount, fifoReservedEventCount, maxDelay, minDelay, reportingMode);
         this.sensorList.add(sensorListModel);
         HVLog.d("sensorListModel:" + sensorListModel.toString());
      }

   }

   public ArrayList<SensorListModel> getSensorList() {
      return this.sensorList;
   }
}
