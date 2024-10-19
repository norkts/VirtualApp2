package com.lody.virtual.client.hook.proxies.telephony;

import android.os.Bundle;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.NeighboringCellInfo;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.SkipInject;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.remote.VDeviceConfig;
import com.lody.virtual.remote.vloc.VCell;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class MethodProxies {
   private static Bundle getCellLocationInternal(VCell cell) {
      if (cell != null) {
         Bundle cellData = new Bundle();
         if (cell.type == 2) {
            try {
               CdmaCellLocation cellLoc = new CdmaCellLocation();
               cellLoc.setCellLocationData(cell.baseStationId, Integer.MAX_VALUE, Integer.MAX_VALUE, cell.systemId, cell.networkId);
               cellLoc.fillInNotifierBundle(cellData);
            } catch (Throwable var4) {
               cellData.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+KWgYLAZ9AQozKi0YXmkzSFo=")), cell.baseStationId);
               cellData.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+KWgYLAZ9AQozKi0YU24gBi9vHigvLhhSVg==")), Integer.MAX_VALUE);
               cellData.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4+KWgYLAZ9AQozKi0YU28FMC1qDiwwLgguVg==")), Integer.MAX_VALUE);
               cellData.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YKWwFNCNrDgpF")), cell.systemId);
               cellData.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4uLGwzGgRjIgYw")), cell.networkId);
            }
         } else {
            try {
               GsmCellLocation cellLoc = new GsmCellLocation();
               cellLoc.setLacAndCid(cell.lac, cell.cid);
               cellLoc.fillInNotifierBundle(cellData);
            } catch (Throwable var3) {
               cellData.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+OQ==")), cell.lac);
               cellData.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4YPA==")), cell.cid);
               cellData.putInt(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khc2OQ==")), cell.psc);
            }
         }

         return cellData;
      } else {
         return null;
      }
   }

   private static CellInfo createCellInfo(VCell cell) {
      if (cell.type == 2) {
         CellInfoCdma cdma = (CellInfoCdma)mirror.android.telephony.CellInfoCdma.ctor.newInstance();
         CellIdentityCdma identityCdma = (CellIdentityCdma)mirror.android.telephony.CellInfoCdma.mCellIdentityCdma.get(cdma);
         CellSignalStrengthCdma strengthCdma = (CellSignalStrengthCdma)mirror.android.telephony.CellInfoCdma.mCellSignalStrengthCdma.get(cdma);
         mirror.android.telephony.CellIdentityCdma.mNetworkId.set(identityCdma, cell.networkId);
         mirror.android.telephony.CellIdentityCdma.mSystemId.set(identityCdma, cell.systemId);
         mirror.android.telephony.CellIdentityCdma.mBasestationId.set(identityCdma, cell.baseStationId);
         mirror.android.telephony.CellSignalStrengthCdma.mCdmaDbm.set(strengthCdma, -74);
         mirror.android.telephony.CellSignalStrengthCdma.mCdmaEcio.set(strengthCdma, -91);
         mirror.android.telephony.CellSignalStrengthCdma.mEvdoDbm.set(strengthCdma, -64);
         mirror.android.telephony.CellSignalStrengthCdma.mEvdoSnr.set(strengthCdma, 7);
         return cdma;
      } else {
         CellInfoGsm gsm = (CellInfoGsm)mirror.android.telephony.CellInfoGsm.ctor.newInstance();
         CellIdentityGsm identityGsm = (CellIdentityGsm)mirror.android.telephony.CellInfoGsm.mCellIdentityGsm.get(gsm);
         CellSignalStrengthGsm strengthGsm = (CellSignalStrengthGsm)mirror.android.telephony.CellInfoGsm.mCellSignalStrengthGsm.get(gsm);
         mirror.android.telephony.CellIdentityGsm.mMcc.set(identityGsm, cell.mcc);
         mirror.android.telephony.CellIdentityGsm.mMnc.set(identityGsm, cell.mnc);
         mirror.android.telephony.CellIdentityGsm.mLac.set(identityGsm, cell.lac);
         mirror.android.telephony.CellIdentityGsm.mCid.set(identityGsm, cell.cid);
         mirror.android.telephony.CellSignalStrengthGsm.mSignalStrength.set(strengthGsm, 20);
         mirror.android.telephony.CellSignalStrengthGsm.mBitErrorRate.set(strengthGsm, 0);
         return gsm;
      }
   }

   @SkipInject
   static class GetNeighboringCellInfo extends ReplaceCallingPkgMethodProxy {
      public GetNeighboringCellInfo() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjNC9iJBo6Ki4uMW8VEhNrAQIdOxgcImAjSFo=")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (!isFakeLocationEnable()) {
            return super.call(who, method, args);
         } else {
            List<VCell> cells = VirtualLocationManager.get().getNeighboringCell(getAppUserId(), getAppPkg());
            if (cells == null) {
               return null;
            } else {
               List<NeighboringCellInfo> infos = new ArrayList();
               Iterator var6 = cells.iterator();

               while(var6.hasNext()) {
                  VCell cell = (VCell)var6.next();
                  NeighboringCellInfo info = new NeighboringCellInfo();
                  mirror.android.telephony.NeighboringCellInfo.mLac.set(info, cell.lac);
                  mirror.android.telephony.NeighboringCellInfo.mCid.set(info, cell.cid);
                  mirror.android.telephony.NeighboringCellInfo.mRssi.set(info, 6);
                  infos.add(info);
               }

               return infos;
            }
         }
      }
   }

   @SkipInject
   static class GetAllCellInfo extends ReplaceCallingPkgMethodProxy {
      public GetAllCellInfo() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVHiRlJDAoKhUcDmkVNFo=")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (isFakeLocationEnable() && !getAppPkg().equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm4jMCxsNwYaLgQcCGMKQSBqEVRF")))) {
            List<VCell> cells = VirtualLocationManager.get().getAllCell(getAppUserId(), getAppPkg());
            if (cells == null) {
               return null;
            } else {
               List<CellInfo> result = new ArrayList();
               Iterator var6 = cells.iterator();

               while(var6.hasNext()) {
                  VCell cell = (VCell)var6.next();
                  result.add(MethodProxies.createCellInfo(cell));
               }

               return result;
            }
         } else {
            return super.call(who, method, args);
         }
      }
   }

   static class GetAllCellInfoUsingSubId extends ReplaceCallingPkgMethodProxy {
      public GetAllCellInfoUsingSubId() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMVHiRlJDAoKhUcDmkVNFBsJx4bLjs2CX02Gi8=")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return isFakeLocationEnable() ? null : super.call(who, method, args);
      }
   }

   @SkipInject
   static class GetCellLocation extends ReplaceCallingPkgMethodProxy {
      public GetCellLocation() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGMzNCRgHFE1Ly0iLmwjNCY=")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (isFakeLocationEnable()) {
            VCell cell = VirtualLocationManager.get().getCell(getAppUserId(), getAppPkg());
            return cell != null ? MethodProxies.getCellLocationInternal(cell) : null;
         } else {
            return super.call(who, method, args);
         }
      }
   }

   static class GetMeidForSlot extends GetDeviceId {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIVNC9iHDw1IzwqCG8KBlo="));
      }
   }

   static class GetImeiForSlot extends GetDeviceId {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLH0VEitjDDw1IzwqCG8KBlo="));
      }
   }

   static class GetDeviceId extends ReplaceLastPkgMethodProxy {
      public GetDeviceId() {
         super(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAFND5jDig/IQc2Vg==")));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         VDeviceConfig config = getDeviceConfig();
         String imei;
         if (config.enable) {
            imei = config.deviceId;
            if (!TextUtils.isEmpty(imei)) {
               return imei;
            }
         }

         if (config != null) {
            imei = config.deviceId;
            if (!TextUtils.isEmpty(imei)) {
               return imei;
            }
         }

         return super.call(who, method, args);
      }
   }
}
