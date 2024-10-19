package com.android.dx.cf.code;

import com.android.dx.rop.code.LocalItem;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.Type;
import java.util.ArrayList;

public interface Machine {
   Prototype getPrototype();

   void clearArgs();

   void popArgs(Frame var1, int var2);

   void popArgs(Frame var1, Prototype var2);

   void popArgs(Frame var1, Type var2);

   void popArgs(Frame var1, Type var2, Type var3);

   void popArgs(Frame var1, Type var2, Type var3, Type var4);

   void localArg(Frame var1, int var2);

   void localInfo(boolean var1);

   void auxType(Type var1);

   void auxIntArg(int var1);

   void auxCstArg(Constant var1);

   void auxTargetArg(int var1);

   void auxSwitchArg(SwitchList var1);

   void auxInitValues(ArrayList<Constant> var1);

   void localTarget(int var1, Type var2, LocalItem var3);

   void run(Frame var1, int var2, int var3);
}
