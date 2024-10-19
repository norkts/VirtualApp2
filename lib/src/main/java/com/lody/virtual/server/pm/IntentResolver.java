package com.lody.virtual.server.pm;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.pm.parser.VPackage;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class IntentResolver<F extends VPackage.IntentInfo, R> {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLGgVBgZpNDApKi1bLGkgRVo="));
   private static final Comparator sResolvePrioritySorter = new Comparator() {
      public int compare(Object o1, Object o2) {
         int q1;
         int q2;
         if (o1 instanceof IntentFilter) {
            q1 = ((IntentFilter)o1).getPriority();
            q2 = ((IntentFilter)o2).getPriority();
         } else {
            if (!(o1 instanceof ResolveInfo)) {
               return 0;
            }

            ResolveInfo r1 = (ResolveInfo)o1;
            ResolveInfo r2 = (ResolveInfo)o2;
            q1 = r1.filter == null ? 0 : r1.filter.getPriority();
            q2 = r2.filter == null ? 0 : r2.filter.getPriority();
         }

         return q1 > q2 ? -1 : (q1 < q2 ? 1 : 0);
      }
   };
   private HashSet<F> mFilters = new HashSet();
   private HashMap<String, F[]> mTypeToFilter = new HashMap();
   private HashMap<String, F[]> mBaseTypeToFilter = new HashMap();
   private HashMap<String, F[]> mWildTypeToFilter = new HashMap();
   private HashMap<String, F[]> mSchemeToFilter = new HashMap();
   private HashMap<String, F[]> mActionToFilter = new HashMap();
   private HashMap<String, F[]> mTypedActionToFilter = new HashMap();

   private static FastImmutableArraySet<String> getFastIntentCategories(Intent intent) {
      Set<String> categories = intent.getCategories();
      return categories == null ? null : new FastImmutableArraySet((String[])categories.toArray(new String[categories.size()]));
   }

   public void addFilter(F f) {
      this.mFilters.add(f);
      int numS = this.register_intent_filter(f, f.filter.schemesIterator(), this.mSchemeToFilter, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJIyhpJCg0KAdXPXhSTVo=")));
      int numT = this.register_mime_types(f, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJIyhuEQYsKAQHOg==")));
      if (numS == 0 && numT == 0) {
         this.register_intent_filter(f, f.filter.actionsIterator(), this.mActionToFilter, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJIyhlDiggKQdfDnhSTVo=")));
      }

      if (numT != 0) {
         this.register_intent_filter(f, f.filter.actionsIterator(), this.mTypedActionToFilter, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJIyhuEQYsKAc2E24KBi9lJx0xPQhSVg==")));
      }

   }

   private boolean filterEquals(IntentFilter f1, IntentFilter f2) {
      int s1 = f1.countActions();
      int s2 = f2.countActions();
      if (s1 != s2) {
         return false;
      } else {
         int i;
         for(i = 0; i < s1; ++i) {
            if (!f2.hasAction(f1.getAction(i))) {
               return false;
            }
         }

         s1 = f1.countCategories();
         s2 = f2.countCategories();
         if (s1 != s2) {
            return false;
         } else {
            for(i = 0; i < s1; ++i) {
               if (!f2.hasCategory(f1.getCategory(i))) {
                  return false;
               }
            }

            s1 = f1.countDataTypes();
            s2 = f2.countDataTypes();
            if (s1 != s2) {
               return false;
            } else {
               s1 = f1.countDataSchemes();
               s2 = f2.countDataSchemes();
               if (s1 != s2) {
                  return false;
               } else {
                  for(i = 0; i < s1; ++i) {
                     if (!f2.hasDataScheme(f1.getDataScheme(i))) {
                        return false;
                     }
                  }

                  s1 = f1.countDataAuthorities();
                  s2 = f2.countDataAuthorities();
                  if (s1 != s2) {
                     return false;
                  } else {
                     s1 = f1.countDataPaths();
                     s2 = f2.countDataPaths();
                     if (s1 != s2) {
                        return false;
                     } else {
                        if (VERSION.SDK_INT >= 19) {
                           s1 = f1.countDataSchemeSpecificParts();
                           s2 = f2.countDataSchemeSpecificParts();
                           if (s1 != s2) {
                              return false;
                           }
                        }

                        return true;
                     }
                  }
               }
            }
         }
      }
   }

   private ArrayList<F> collectFilters(F[] array, IntentFilter matching) {
      ArrayList<F> res = null;
      if (array != null) {
         for(int i = 0; i < array.length; ++i) {
            F cur = array[i];
            if (cur == null) {
               break;
            }

            if (this.filterEquals(cur.filter, matching)) {
               if (res == null) {
                  res = new ArrayList();
               }

               res.add(cur);
            }
         }
      }

      return res;
   }

   public ArrayList<F> findFilters(IntentFilter matching) {
      if (matching.countDataSchemes() == 1) {
         return this.collectFilters(this.mSchemeToFilter.get(matching.getDataScheme(0)), matching);
      } else if (matching.countDataTypes() != 0 && matching.countActions() == 1) {
         return this.collectFilters(this.mTypedActionToFilter.get(matching.getAction(0)), matching);
      } else if (matching.countDataTypes() == 0 && matching.countDataSchemes() == 0 && matching.countActions() == 1) {
         return this.collectFilters(this.mActionToFilter.get(matching.getAction(0)), matching);
      } else {
         ArrayList<F> res = null;
         Iterator var3 = this.mFilters.iterator();

         while(var3.hasNext()) {
            F cur = (F)var3.next();
            if (this.filterEquals(cur.filter, matching)) {
               if (res == null) {
                  res = new ArrayList();
               }

               res.add(cur);
            }
         }

         return res;
      }
   }

   public void removeFilter(F f) {
      this.removeFilterInternal(f);
      this.mFilters.remove(f);
   }

   void removeFilterInternal(F f) {
      int numS = this.unregister_intent_filter(f, f.filter.schemesIterator(), this.mSchemeToFilter, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJIyhpJCg0KAdXPXhSTVo=")));
      int numT = this.unregister_mime_types(f, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJIyhuEQYsKAQHOg==")));
      if (numS == 0 && numT == 0) {
         this.unregister_intent_filter(f, f.filter.actionsIterator(), this.mActionToFilter, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJIyhlDiggKQdfDnhSTVo=")));
      }

      if (numT != 0) {
         this.unregister_intent_filter(f, f.filter.actionsIterator(), this.mTypedActionToFilter, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJIyhuEQYsKAc2E24KBi9lJx0xPQhSVg==")));
      }

   }

   public Iterator<F> filterIterator() {
      return new IteratorWrapper(this.mFilters.iterator());
   }

   public Set<F> filterSet() {
      return Collections.unmodifiableSet(this.mFilters);
   }

   public List<R> queryIntentFromList(Intent intent, String resolvedType, boolean defaultOnly, ArrayList<F[]> listCut, int userId) {
      ArrayList<R> resultList = new ArrayList();
      FastImmutableArraySet<String> categories = getFastIntentCategories(intent);
      String scheme = intent.getScheme();
      int N = listCut.size();

      for(int i = 0; i < N; ++i) {
         this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, listCut.get(i), resultList, userId);
      }

      this.sortResults(resultList);
      return resultList;
   }

   public List<R> queryIntent(Intent intent, String resolvedType, boolean defaultOnly, int userId) {
      String scheme = intent.getScheme();
      ArrayList<R> finalList = new ArrayList();
      F[] firstTypeCut = null;
      F[] secondTypeCut = null;
      F[] thirdTypeCut = null;
      F[] schemeCut = null;
      if (resolvedType != null) {
         int slashpos = resolvedType.indexOf(47);
         if (slashpos > 0) {
            String baseType = resolvedType.substring(0, slashpos);
            if (!baseType.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PD5SVg==")))) {
               if (resolvedType.length() == slashpos + 2 && resolvedType.charAt(slashpos + 1) == '*') {
                  firstTypeCut = this.mBaseTypeToFilter.get(baseType);
                  secondTypeCut = this.mWildTypeToFilter.get(baseType);
               } else {
                  firstTypeCut = this.mTypeToFilter.get(resolvedType);
                  secondTypeCut = this.mWildTypeToFilter.get(baseType);
               }

               thirdTypeCut = this.mWildTypeToFilter.get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PD5SVg==")));
            } else if (intent.getAction() != null) {
               firstTypeCut = this.mTypedActionToFilter.get(intent.getAction());
            }
         }
      }

      if (scheme != null) {
         schemeCut = this.mSchemeToFilter.get(scheme);
      }

      if (resolvedType == null && scheme == null && intent.getAction() != null) {
         firstTypeCut = this.mActionToFilter.get(intent.getAction());
      }

      FastImmutableArraySet<String> categories = getFastIntentCategories(intent);
      if (firstTypeCut != null) {
         this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, firstTypeCut, finalList, userId);
      }

      if (secondTypeCut != null) {
         this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, secondTypeCut, finalList, userId);
      }

      if (thirdTypeCut != null) {
         this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, thirdTypeCut, finalList, userId);
      }

      if (schemeCut != null) {
         this.buildResolveList(intent, categories, defaultOnly, resolvedType, scheme, schemeCut, finalList, userId);
      }

      this.sortResults(finalList);
      return finalList;
   }

   protected boolean allowFilterResult(F filter, List<R> dest) {
      return true;
   }

   protected boolean isFilterStopped(F filter) {
      return false;
   }

   protected abstract boolean isPackageForFilter(String var1, F var2);

   protected abstract F[] newArray(int var1);

   protected R newResult(F filter, int match, int userId) {
      return (R)filter;
   }

   protected void sortResults(List<R> results) {
      Collections.sort(results, sResolvePrioritySorter);
   }

   protected void dumpFilter(PrintWriter out, String prefix, F filter) {
      out.print(prefix);
      out.println(filter);
   }

   protected Object filterToLabel(F filter) {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLGgVBgZqNAYoLBcMKA=="));
   }

   protected void dumpFilterLabel(PrintWriter out, String prefix, Object label, int count) {
      out.print(prefix);
      out.print(label);
      out.print(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ODo6Vg==")));
      out.println(count);
   }

   private void addFilter(HashMap<String, F[]> map, String name, F filter) {
      F[] array = map.get(name);
      if (array == null) {
         array = this.newArray(2);
         map.put(name, array);
         array[0] = filter;
      } else {
         int N = array.length;

         int i;
         for(i = N; i > 0 && array[i - 1] == null; --i) {
         }

         if (i < N) {
            array[i] = filter;
         } else {
            F[] newa = this.newArray(N * 3 / 2);
            System.arraycopy(array, 0, newa, 0, N);
            newa[N] = filter;
            map.put(name, newa);
         }
      }

   }

   private int register_mime_types(F filter, String prefix) {
      Iterator<String> i = filter.filter.typesIterator();
      if (i == null) {
         return 0;
      } else {
         int num = 0;

         while(i.hasNext()) {
            String name = (String)i.next();
            ++num;
            String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
               baseName = name.substring(0, slashpos).intern();
            } else {
               name = name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MypXVg=="));
            }

            this.addFilter(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
               this.addFilter(this.mBaseTypeToFilter, baseName, filter);
            } else {
               this.addFilter(this.mWildTypeToFilter, baseName, filter);
            }
         }

         return num;
      }
   }

   private int unregister_mime_types(F filter, String prefix) {
      Iterator<String> i = filter.filter.typesIterator();
      if (i == null) {
         return 0;
      } else {
         int num = 0;

         while(i.hasNext()) {
            String name = (String)i.next();
            ++num;
            String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
               baseName = name.substring(0, slashpos).intern();
            } else {
               name = name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MypXVg=="));
            }

            this.remove_all_objects(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
               this.remove_all_objects(this.mBaseTypeToFilter, baseName, filter);
            } else {
               this.remove_all_objects(this.mWildTypeToFilter, baseName, filter);
            }
         }

         return num;
      }
   }

   private int register_intent_filter(F filter, Iterator<String> i, HashMap<String, F[]> dest, String prefix) {
      if (i == null) {
         return 0;
      } else {
         int num = 0;

         while(i.hasNext()) {
            String name = (String)i.next();
            ++num;
            this.addFilter(dest, name, filter);
         }

         return num;
      }
   }

   private int unregister_intent_filter(F filter, Iterator<String> i, HashMap<String, F[]> dest, String prefix) {
      if (i == null) {
         return 0;
      } else {
         int num = 0;

         while(i.hasNext()) {
            String name = (String)i.next();
            ++num;
            this.remove_all_objects(dest, name, filter);
         }

         return num;
      }
   }

   private void remove_all_objects(HashMap<String, F[]> map, String name, Object object) {
      F[] array = map.get(name);
      if (array != null) {
         int LAST;
         for(LAST = array.length - 1; LAST >= 0 && array[LAST] == null; --LAST) {
         }

         for(int idx = LAST; idx >= 0; --idx) {
            if (array[idx] == object) {
               int remain = LAST - idx;
               if (remain > 0) {
                  System.arraycopy(array, idx + 1, array, idx, remain);
               }

               array[LAST] = null;
               --LAST;
            }
         }

         if (LAST < 0) {
            map.remove(name);
         } else if (LAST < array.length / 2) {
            F[] newa = this.newArray(LAST + 2);
            System.arraycopy(array, 0, newa, 0, LAST + 1);
            map.put(name, newa);
         }
      }

   }

   private void buildResolveList(Intent intent, FastImmutableArraySet<String> categories, boolean defaultOnly, String resolvedType, String scheme, F[] src, List<R> dest, int userId) {
      String action = intent.getAction();
      Uri data = intent.getData();
      String packageName = intent.getPackage();
      int N = src != null ? src.length : 0;
      boolean hasNonDefaults = false;

      F filter;
      for(int i = 0; i < N && (filter = src[i]) != null; ++i) {
         if ((packageName == null || this.isPackageForFilter(packageName, filter)) && this.allowFilterResult(filter, dest)) {
            int match = filter.filter.match(action, resolvedType, scheme, data, categories, TAG);
            if (match >= 0) {
               if (defaultOnly && !filter.filter.hasCategory(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoJzg/LhgmKWEzBSlmHApKICsAAmcVSFo=")))) {
                  hasNonDefaults = true;
               } else {
                  R oneResult = this.newResult(filter, match, userId);
                  if (oneResult != null) {
                     dest.add(oneResult);
                  }
               }
            }
         }
      }

      if (hasNonDefaults) {
         if (dest.size() == 0) {
            VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWozHj5iDAY2LBcMDmU3TS5oAR4dLhgpPksaICpqDh4tOD4mO28VLAZ6DTw1IAgLL2wwRQJpI1EiLAcqCnsLLBFuHDATIiwuG2MLBhVjNTgWJAUqVg==")));
         } else if (dest.size() > 1) {
            VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uKWozHj5iDAY2LBcMDmU0IyhlDigdKggYDmAaLyNsDiQ9KAgMJ2wnGTRqNxo7IQMiOmwgTTF+NA4wKRheOGMxJFFqDDgLOzwcHWExGghgDChTIghSVg==")));
         }
      }

   }

   private class IteratorWrapper implements Iterator<F> {
      private Iterator<F> mI;
      private F mCur;

      IteratorWrapper(Iterator<F> it) {
         this.mI = it;
      }

      public boolean hasNext() {
         return this.mI.hasNext();
      }

      public F next() {
         return this.mCur = (F) this.mI.next();
      }

      public void remove() {
         if (this.mCur != null) {
            IntentResolver.this.removeFilterInternal(this.mCur);
         }

         this.mI.remove();
      }
   }
}
