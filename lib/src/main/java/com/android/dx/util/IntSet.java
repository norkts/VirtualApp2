package com.android.dx.util;

public interface IntSet {
   void add(int var1);

   void remove(int var1);

   boolean has(int var1);

   void merge(IntSet var1);

   int elements();

   IntIterator iterator();
}
