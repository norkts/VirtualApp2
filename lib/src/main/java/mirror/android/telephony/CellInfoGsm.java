package mirror.android.telephony;

import android.annotation.TargetApi;
import mirror.RefClass;
import mirror.RefConstructor;
import mirror.RefObject;

@TargetApi(17)
public class CellInfoGsm {
   public static Class<?> TYPE = RefClass.load(CellInfoGsm.class, android.telephony.CellInfoGsm.class);
   public static RefConstructor<android.telephony.CellInfoGsm> ctor;
   public static RefObject<android.telephony.CellIdentityGsm> mCellIdentityGsm;
   public static RefObject<android.telephony.CellSignalStrengthGsm> mCellSignalStrengthGsm;
}
