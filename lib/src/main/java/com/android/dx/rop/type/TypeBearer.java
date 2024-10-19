package com.android.dx.rop.type;

import com.android.dx.util.ToHuman;

public interface TypeBearer extends ToHuman {
   Type getType();

   TypeBearer getFrameType();

   int getBasicType();

   int getBasicFrameType();

   boolean isConstant();
}
