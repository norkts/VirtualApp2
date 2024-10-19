package mirror.android.telephony;

import android.annotation.TargetApi;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefObject;

@TargetApi(17)
public class CellInfoCdma {
   public static Class<?> TYPE = RefClass.load(CellInfoCdma.class, android.telephony.CellInfoCdma.class);
   public static RefConstructor<android.telephony.CellInfoCdma> ctor;
   public static RefObject<android.telephony.CellIdentityCdma> mCellIdentityCdma;
   public static RefObject<android.telephony.CellSignalStrengthCdma> mCellSignalStrengthCdma;
}
