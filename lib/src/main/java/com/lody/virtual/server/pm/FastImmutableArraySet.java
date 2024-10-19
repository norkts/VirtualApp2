package com.lody.virtual.server.pm;

import java.util.AbstractSet;
import java.util.Iterator;

public final class FastImmutableArraySet<T> extends AbstractSet<T> {
   FastIterator<T> mIterator;
   T[] mContents;

   public FastImmutableArraySet(T[] contents) {
      this.mContents = contents;
   }

   public Iterator<T> iterator() {
      FastIterator<T> it = this.mIterator;
      if (it == null) {
         it = new FastIterator(this.mContents);
         this.mIterator = it;
      } else {
         it.mIndex = 0;
      }

      return it;
   }

   public int size() {
      return this.mContents.length;
   }

   private static final class FastIterator<T> implements Iterator<T> {
      private final T[] mContents;
      int mIndex;

      public FastIterator(T[] contents) {
         this.mContents = contents;
      }

      public boolean hasNext() {
         return this.mIndex != this.mContents.length;
      }

      public T next() {
         return this.mContents[this.mIndex++];
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
