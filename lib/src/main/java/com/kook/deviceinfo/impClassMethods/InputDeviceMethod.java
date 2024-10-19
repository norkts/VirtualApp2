package com.kook.deviceinfo.impClassMethods;

import android.content.Context;
import android.view.InputDevice;
import com.kook.deviceinfo.impClasses.BuildInfo;
import com.kook.deviceinfo.models.InputModel;
import java.util.ArrayList;
import java.util.List;

public class InputDeviceMethod {
   private final Context context;
   ArrayList<InputModel> inputList = new ArrayList();
   List<InputDevice.MotionRange> motionRangeList;
   int[] id;
   BuildInfo buildInfo;
   int i = 0;

   public InputDeviceMethod(Context context) {
      this.context = context;
      this.id = InputDevice.getDeviceIds();
      this.inputDevices();
   }

   private void inputDevices() {
      this.buildInfo = new BuildInfo(this.context);
      int[] var1 = this.id;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int facing = var1[var3];
         String keyboardType = null;
         String axis = null;
         String range = null;
         String flat = null;
         String fuzz = null;
         String resol = null;
         String source = null;
         String s = null;
         boolean hasMotionRange = false;
         StringBuilder stringBuilder = new StringBuilder();
         this.motionRangeList = null;
         InputDevice inputDevice = InputDevice.getDevice(facing);
         String name = inputDevice.getName();
         String vendorId = String.valueOf(inputDevice.getVendorId());
         String proId = String.valueOf(inputDevice.getProductId());
         String hasVibrator = String.valueOf(inputDevice.getVibrator().hasVibrator());
         switch (inputDevice.getKeyboardType()) {
            case 0:
               keyboardType = "None";
               break;
            case 1:
               keyboardType = "Non-Alphabetic";
               break;
            case 2:
               keyboardType = "Alphabetic";
         }

         String deviceId = String.valueOf(inputDevice.getId());
         String desc = inputDevice.getDescriptor();
         String sources = String.valueOf(inputDevice.getSources());
         if ((inputDevice.getSources() & 257) == 257) {
            stringBuilder.append("Keyboard, ");
         }

         if ((inputDevice.getSources() & 16777232) == 16777232) {
            stringBuilder.append("JoyStick, ");
         }

         if ((inputDevice.getSources() & 513) == 513) {
            stringBuilder.append("Dpad, ");
         }

         if ((inputDevice.getSources() & 8194) == 8194) {
            stringBuilder.append("Mouse, ");
         }

         if ((inputDevice.getSources() & 1025) == 1025) {
            stringBuilder.append("GamePad, ");
         }

         if ((inputDevice.getSources() & 1048584) == 1048584) {
            stringBuilder.append("TouchPad, ");
         }

         if ((inputDevice.getSources() & 65540) == 65540) {
            stringBuilder.append("TrackBall, ");
         }

         if ((inputDevice.getSources() & 16386) == 16386) {
            stringBuilder.append("Stylus, ");
         }

         if ((inputDevice.getSources() & 4098) == 4098) {
            stringBuilder.append("TouchScreen, ");
         }

         if (stringBuilder.toString().length() > 0) {
            s = sources + " (" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 2) + ")";
         }

         this.motionRangeList = inputDevice.getMotionRanges();
         if (this.motionRangeList.size() != 0) {
            hasMotionRange = true;

            for(int i = 0; i < this.motionRangeList.size(); ++i) {
               axis = this.buildInfo.getAxis(((InputDevice.MotionRange)this.motionRangeList.get(i)).getAxis());
               range = String.valueOf(((InputDevice.MotionRange)this.motionRangeList.get(i)).getRange());
               resol = String.valueOf(((InputDevice.MotionRange)this.motionRangeList.get(i)).getResolution());
               flat = String.valueOf(((InputDevice.MotionRange)this.motionRangeList.get(i)).getFlat());
               fuzz = String.valueOf(((InputDevice.MotionRange)this.motionRangeList.get(i)).getFuzz());
               source = "0x" + Integer.toHexString(((InputDevice.MotionRange)this.motionRangeList.get(i)).getSource());
            }
         }

         if (Integer.parseInt(deviceId) >= 0) {
            if (this.i == 1) {
               this.inputList.add(new InputModel(name, desc, vendorId, proId, hasVibrator, keyboardType, deviceId, s, axis, range, flat, fuzz, resol, source, hasMotionRange));
            }

            this.inputList.add(new InputModel(name, desc, vendorId, proId, hasVibrator, keyboardType, deviceId, s, axis, range, flat, fuzz, resol, source, hasMotionRange));
            ++this.i;
         }
      }

   }

   public ArrayList<InputModel> getInputList() {
      return this.inputList;
   }
}
