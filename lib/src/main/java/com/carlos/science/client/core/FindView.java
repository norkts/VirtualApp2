package com.carlos.science.client.core;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.libcommon.StringFog;
import com.carlos.science.stebcore.StepImpl;
import com.kook.common.utils.HVLog;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.jdeferred.Promise;

public class FindView {
   public static int[] getLocationOnScreen(View view) {
      int[] location = new int[2];
      view.getLocationOnScreen(location);
      return location;
   }

   public static Promise<List<View>, Throwable, Void> responseViews(Activity activity, StepImpl stepImpl, String idResName) {
      return ResponseProgram.defer().when(() -> {
         List<View> views = null;

         try {
            View view = activity.getWindow().getDecorView();
            int idRes = getViewIdByName(activity, idResName);

            while(views == null || views.size() <= 0) {
               if (stepImpl.finishStep) {
                  HVLog.i(StringFog.decrypt("ltjhk+zju8jYivjRSRwaFhU7GxUCf5bU3ZXL5ojV4IPv6Q=="));
                  return null;
               }

               views = getAllChildViews(view, idRes);
               boolean findResult = views == null || views.size() <= 1;
               if (findResult) {
                  views = getViewsByWindow(activity, idRes);
               }

               findResult = views.size() <= 0;
               if (!findResult) {
                  break;
               }

               Thread.sleep(200L);
            }

            return views;
         } catch (Exception var7) {
            Exception e = var7;
            HVLog.printException(e);
            return null;
         }
      });
   }

   public static List<View> getViewsByWindow(Context context, int idRes) {
      Field wmGlobalField = null;
      int targetViewId = idRes;

      try {
         wmGlobalField = context.getSystemService(StringFog.decrypt("BAwcEgoZ")).getClass().getDeclaredField(StringFog.decrypt("HiIeGQcPMw=="));
      } catch (Exception var14) {
         Exception e = var14;
         wmGlobalField = null;
         HVLog.printException(e);
      }

      List<View> viewList = new ArrayList();
      Exception e;
      View viewById;
      if (wmGlobalField != null) {
         try {
            wmGlobalField.setAccessible(true);
            Object wmGlobal = wmGlobalField.get(context.getSystemService(StringFog.decrypt("BAwcEgoZ")));
            Field viewsField = wmGlobal.getClass().getDeclaredField(StringFog.decrypt("HjMbExId"));
            viewsField.setAccessible(true);
            ArrayList<View> views = (ArrayList)viewsField.get(wmGlobal);
            Iterator var20 = views.iterator();

            while(var20.hasNext()) {
               View view = (View)var20.next();
               viewById = view.findViewById(targetViewId);
               if (viewById != null) {
                  viewList.add(viewById);
               }
            }

            return viewList;
         } catch (Exception var12) {
            e = var12;
            e.printStackTrace();
            HVLog.printException(e);
            return null;
         }
      } else {
         try {
            Field wmLocalField = context.getSystemService(StringFog.decrypt("BAwcEgoZ")).getClass().getSuperclass().getDeclaredField(StringFog.decrypt("HjIbGAEBKD4CARMXDB0="));
            wmLocalField.setAccessible(true);
            Object wmLocal = wmLocalField.get(context.getSystemService(StringFog.decrypt("BAwcEgoZ")));
            Field viewsField = wmLocal.getClass().getDeclaredField(StringFog.decrypt("HjMbExId"));
            viewsField.setAccessible(true);
            List<View> views = Arrays.asList((View[])viewsField.get(wmLocal));
            Iterator var9 = views.iterator();

            while(var9.hasNext()) {
               viewById = (View)var9.next();
               View viewById = viewById.findViewById(targetViewId);
               if (viewById != null) {
                  viewList.add(viewById);
               }
            }

            return viewList;
         } catch (Exception var13) {
            e = var13;
            HVLog.printException(e);
            return null;
         }
      }
   }

   public static List<View> getAllChildViews(View view, int targetViewId) {
      List<View> allchildren = new ArrayList();
      if (view instanceof ViewGroup) {
         ViewGroup vp = (ViewGroup)view;

         for(int i = 0; i < vp.getChildCount(); ++i) {
            View viewchild = vp.getChildAt(i);
            if (viewchild.getId() == targetViewId) {
               allchildren.add(viewchild);
            }

            allchildren.addAll(getAllChildViews(viewchild, targetViewId));
         }
      }

      return allchildren;
   }

   public static List<View> getAllChildViews(View view, String viewIdName) {
      int targetViewId = getViewIdByName(view.getContext(), viewIdName);
      List<View> allChildViews = getAllChildViews(view, targetViewId);
      return allChildViews;
   }

   public static View findView(Activity activity, Object idRes) {
      View viewByIdRes = null;
      if (!(idRes instanceof Integer) && !(idRes instanceof String) && !(idRes instanceof Class)) {
         HVLog.e(StringFog.decrypt("m9rrn+Liu8vuh9TxjOXOluDXk+DYu8j1iMPLjPHllP/2kPDeuf7Nh83rj/LLnNn+kt3jt/Deifj6gdD3muL+k+T0uOnnitbajMvjlfjwkt/o"));
      }

      if (idRes instanceof Integer) {
         viewByIdRes = activity.findViewById((Integer)idRes);
      } else if (idRes instanceof String) {
         viewByIdRes = findViewByName(activity, (String)idRes);
         if (viewByIdRes == null) {
            viewByIdRes = getViewByWindow(activity, (String)idRes);
         }
      } else if (idRes instanceof Class) {
         List<View> viewListByClass = findViewListByClass(activity, (Class)idRes);
         if (viewListByClass.size() != 1) {
            throw new NullPointerException(StringFog.decrypt("lfrXkOzQuOnnGRsVHk+I5tWb8eqK5/6H1/JQSU9OAAwIE18=") + viewListByClass.size());
         }

         viewByIdRes = (View)viewListByClass.get(0);
      }

      return viewByIdRes;
   }

   private static View findViewByName(Activity activity, String resName) {
      try {
         int targetViewId = getViewIdByName(activity, resName);
         View decorView = activity.getWindow().getDecorView();
         View view = decorView.findViewById(targetViewId);
         if (view == null) {
         }

         return view;
      } catch (Exception var5) {
         Exception e = var5;
         HVLog.printException(e);
         return null;
      }
   }

   public static int getViewIdByName(Context context, String viewIdName) {
      if (TextUtils.isEmpty(viewIdName)) {
         HVLog.e(StringFog.decrypt("AQABOAQDOlMKHFIeHAMC"));
         return -1;
      } else {
         int targetViewId = context.getResources().getIdentifier(viewIdName, StringFog.decrypt("GgE="), context.getPackageName());
         return targetViewId;
      }
   }

   private static View getViewByWindow(Context context, String resName) {
      if (TextUtils.isEmpty(resName)) {
         HVLog.e(StringFog.decrypt("FAAGIAwLKDEaOBseDQAZUxcXBSsPMhZDBgFQBxoCHw=="));
         return null;
      } else {
         Field wmGlobalField = null;
         int targetViewId = context.getResources().getIdentifier(resName, StringFog.decrypt("GgE="), context.getPackageName());

         Exception e;
         try {
            wmGlobalField = context.getSystemService(StringFog.decrypt("BAwcEgoZ")).getClass().getDeclaredField(StringFog.decrypt("HiIeGQcPMw=="));
         } catch (Exception var14) {
            e = var14;
            wmGlobalField = null;
            HVLog.printException(e);
         }

         if (wmGlobalField != null) {
            try {
               wmGlobalField.setAccessible(true);
               Object wmGlobal = wmGlobalField.get(context.getSystemService(StringFog.decrypt("BAwcEgoZ")));
               Field viewsField = wmGlobal.getClass().getDeclaredField(StringFog.decrypt("HjMbExId"));
               viewsField.setAccessible(true);
               ArrayList<View> views = (ArrayList)viewsField.get(wmGlobal);
               Iterator var19 = views.iterator();

               View viewById;
               do {
                  if (!var19.hasNext()) {
                     return null;
                  }

                  View view = (View)var19.next();
                  viewById = view.findViewById(targetViewId);
               } while(viewById == null);

               return viewById;
            } catch (Exception var12) {
               e = var12;
               e.printStackTrace();
               HVLog.printException(e);
               return null;
            }
         } else {
            try {
               Field wmLocalField = context.getSystemService(StringFog.decrypt("BAwcEgoZ")).getClass().getSuperclass().getDeclaredField(StringFog.decrypt("HjIbGAEBKD4CARMXDB0="));
               wmLocalField.setAccessible(true);
               Object wmLocal = wmLocalField.get(context.getSystemService(StringFog.decrypt("BAwcEgoZ")));
               Field viewsField = wmLocal.getClass().getDeclaredField(StringFog.decrypt("HjMbExId"));
               viewsField.setAccessible(true);
               List<View> viewList = Arrays.asList((View[])viewsField.get(wmLocal));
               ArrayList<View> views = new ArrayList();
               Iterator var9 = views.iterator();

               View viewById;
               do {
                  if (!var9.hasNext()) {
                     return null;
                  }

                  View view = (View)var9.next();
                  viewById = view.findViewById(targetViewId);
               } while(viewById == null);

               return viewById;
            } catch (Exception var13) {
               e = var13;
               HVLog.printException(e);
               return null;
            }
         }
      }
   }

   protected static List<View> findViewListByClass(Activity activity, Class classz) {
      try {
         View decorView = activity.getWindow().getDecorView();
         List<View> allChildViews = getAllChildViews(decorView, classz);
         if (allChildViews == null || allChildViews.size() == 0) {
            HVLog.e(StringFog.decrypt("FQwcEjMHOgQvBgEEKxYtHwQBBUUNMxIQHAhK") + classz + "    " + activity.getLocalClassName() + StringFog.decrypt("U0VSVkUYNhYUTxsDSQEbHwk="));
         }

         return allChildViews;
      } catch (Exception var4) {
         Exception e = var4;
         HVLog.printException(e);
         return null;
      }
   }

   protected static List<View> getAllChildViews(View view, Class classzc) {
      List<View> allchildren = new ArrayList();
      if (view instanceof ViewGroup) {
         ViewGroup vp = (ViewGroup)view;

         for(int i = 0; i < vp.getChildCount(); ++i) {
            View viewchild = vp.getChildAt(i);

            try {
               if (classzc.asSubclass(viewchild.getClass()) == null) {
                  allchildren.add(viewchild);
               }
            } catch (ClassCastException var7) {
               ClassCastException e = var7;
               HVLog.printException(e);
            }

            allchildren.addAll(getAllChildViews(viewchild, classzc));
         }
      }

      return allchildren;
   }
}
