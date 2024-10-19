package com.lody.virtual.helper.collection;

import com.lody.virtual.StringFog;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet<E> implements Collection<E>, Set<E> {
   private static final boolean DEBUG = false;
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JgcMKmsaAl5iAQpF"));
   private static final int BASE_SIZE = 4;
   private static final int CACHE_SIZE = 10;
   static Object[] mBaseCache;
   static int mBaseCacheSize;
   static Object[] mTwiceBaseCache;
   static int mTwiceBaseCacheSize;
   int[] mHashes;
   Object[] mArray;
   int mSize;
   MapCollections<E, E> mCollections;

   public ArraySet() {
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      this.mSize = 0;
   }

   public ArraySet(int capacity) {
      if (capacity == 0) {
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         this.allocArrays(capacity);
      }

      this.mSize = 0;
   }

   public ArraySet(ArraySet<E> set) {
      this();
      if (set != null) {
         this.addAll(set);
      }

   }

   public ArraySet(Collection<E> set) {
      this();
      if (set != null) {
         this.addAll(set);
      }

   }

   private static void freeArrays(int[] hashes, Object[] array, int size) {
      Class var3;
      int i;
      if (hashes.length == 8) {
         var3 = ArraySet.class;
         synchronized(ArraySet.class) {
            if (mTwiceBaseCacheSize < 10) {
               array[0] = mTwiceBaseCache;
               array[1] = hashes;

               for(i = size - 1; i >= 2; --i) {
                  array[i] = null;
               }

               mTwiceBaseCache = array;
               ++mTwiceBaseCacheSize;
            }
         }
      } else if (hashes.length == 4) {
         var3 = ArraySet.class;
         synchronized(ArraySet.class) {
            if (mBaseCacheSize < 10) {
               array[0] = mBaseCache;
               array[1] = hashes;

               for(i = size - 1; i >= 2; --i) {
                  array[i] = null;
               }

               mBaseCache = array;
               ++mBaseCacheSize;
            }
         }
      }

   }

   private int indexOf(Object key, int hash) {
      int N = this.mSize;
      if (N == 0) {
         return -1;
      } else {
         int index = ContainerHelpers.binarySearch(this.mHashes, N, hash);
         if (index < 0) {
            return index;
         } else if (key.equals(this.mArray[index])) {
            return index;
         } else {
            int end;
            for(end = index + 1; end < N && this.mHashes[end] == hash; ++end) {
               if (key.equals(this.mArray[end])) {
                  return end;
               }
            }

            for(int i = index - 1; i >= 0 && this.mHashes[i] == hash; --i) {
               if (key.equals(this.mArray[i])) {
                  return i;
               }
            }

            return ~end;
         }
      }
   }

   private int indexOfNull() {
      int N = this.mSize;
      if (N == 0) {
         return -1;
      } else {
         int index = ContainerHelpers.binarySearch(this.mHashes, N, 0);
         if (index < 0) {
            return index;
         } else if (null == this.mArray[index]) {
            return index;
         } else {
            int end;
            for(end = index + 1; end < N && this.mHashes[end] == 0; ++end) {
               if (null == this.mArray[end]) {
                  return end;
               }
            }

            for(int i = index - 1; i >= 0 && this.mHashes[i] == 0; --i) {
               if (null == this.mArray[i]) {
                  return i;
               }
            }

            return ~end;
         }
      }
   }

   private void allocArrays(int size) {
      Class var2;
      Object[] array;
      if (size == 8) {
         var2 = ArraySet.class;
         synchronized(ArraySet.class) {
            if (mTwiceBaseCache != null) {
               array = mTwiceBaseCache;
               this.mArray = array;
               mTwiceBaseCache = (Object[])array[0];
               this.mHashes = (int[])array[1];
               array[0] = array[1] = null;
               --mTwiceBaseCacheSize;
               return;
            }
         }
      } else if (size == 4) {
         var2 = ArraySet.class;
         synchronized(ArraySet.class) {
            if (mBaseCache != null) {
               array = mBaseCache;
               this.mArray = array;
               mBaseCache = (Object[])array[0];
               this.mHashes = (int[])array[1];
               array[0] = array[1] = null;
               --mBaseCacheSize;
               return;
            }
         }
      }

      this.mHashes = new int[size];
      this.mArray = new Object[size];
   }

   public void clear() {
      if (this.mSize != 0) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
      }

   }

   public void ensureCapacity(int minimumCapacity) {
      if (this.mHashes.length < minimumCapacity) {
         int[] ohashes = this.mHashes;
         Object[] oarray = this.mArray;
         this.allocArrays(minimumCapacity);
         if (this.mSize > 0) {
            System.arraycopy(ohashes, 0, this.mHashes, 0, this.mSize);
            System.arraycopy(oarray, 0, this.mArray, 0, this.mSize);
         }

         freeArrays(ohashes, oarray, this.mSize);
      }

   }

   public boolean contains(Object key) {
      return this.indexOf(key) >= 0;
   }

   public int indexOf(Object key) {
      return key == null ? this.indexOfNull() : this.indexOf(key, key.hashCode());
   }

   public E valueAt(int index) {
      return (E)this.mArray[index];
   }

   public boolean isEmpty() {
      return this.mSize <= 0;
   }

   public boolean add(E value) {
      int hash;
      int index;
      if (value == null) {
         hash = 0;
         index = this.indexOfNull();
      } else {
         hash = value.hashCode();
         index = this.indexOf(value, hash);
      }

      if (index >= 0) {
         return false;
      } else {
         index = ~index;
         if (this.mSize >= this.mHashes.length) {
            int n = this.mSize >= 8 ? this.mSize + (this.mSize >> 1) : (this.mSize >= 4 ? 8 : 4);
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            this.allocArrays(n);
            if (this.mHashes.length > 0) {
               System.arraycopy(ohashes, 0, this.mHashes, 0, ohashes.length);
               System.arraycopy(oarray, 0, this.mArray, 0, oarray.length);
            }

            freeArrays(ohashes, oarray, this.mSize);
         }

         if (index < this.mSize) {
            System.arraycopy(this.mHashes, index, this.mHashes, index + 1, this.mSize - index);
            System.arraycopy(this.mArray, index, this.mArray, index + 1, this.mSize - index);
         }

         this.mHashes[index] = hash;
         this.mArray[index] = value;
         ++this.mSize;
         return true;
      }
   }

   public void addAll(ArraySet<? extends E> array) {
      int N = array.mSize;
      this.ensureCapacity(this.mSize + N);
      if (this.mSize == 0) {
         if (N > 0) {
            System.arraycopy(array.mHashes, 0, this.mHashes, 0, N);
            System.arraycopy(array.mArray, 0, this.mArray, 0, N);
            this.mSize = N;
         }
      } else {
         for(int i = 0; i < N; ++i) {
            this.add(array.valueAt(i));
         }
      }

   }

   public boolean remove(Object object) {
      int index = this.indexOf(object);
      if (index >= 0) {
         this.removeAt(index);
         return true;
      } else {
         return false;
      }
   }

   public E removeAt(int index) {
      Object old = this.mArray[index];
      if (this.mSize <= 1) {
         freeArrays(this.mHashes, this.mArray, this.mSize);
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
         this.mSize = 0;
      } else if (this.mHashes.length > 8 && this.mSize < this.mHashes.length / 3) {
         int n = this.mSize > 8 ? this.mSize + (this.mSize >> 1) : 8;
         int[] ohashes = this.mHashes;
         Object[] oarray = this.mArray;
         this.allocArrays(n);
         --this.mSize;
         if (index > 0) {
            System.arraycopy(ohashes, 0, this.mHashes, 0, index);
            System.arraycopy(oarray, 0, this.mArray, 0, index);
         }

         if (index < this.mSize) {
            System.arraycopy(ohashes, index + 1, this.mHashes, index, this.mSize - index);
            System.arraycopy(oarray, index + 1, this.mArray, index, this.mSize - index);
         }
      } else {
         --this.mSize;
         if (index < this.mSize) {
            System.arraycopy(this.mHashes, index + 1, this.mHashes, index, this.mSize - index);
            System.arraycopy(this.mArray, index + 1, this.mArray, index, this.mSize - index);
         }

         this.mArray[this.mSize] = null;
      }

      return (E)old;
   }

   public boolean removeAll(ArraySet<? extends E> array) {
      int N = array.mSize;
      int originalSize = this.mSize;

      for(int i = 0; i < N; ++i) {
         this.remove(array.valueAt(i));
      }

      return originalSize != this.mSize;
   }

   public int size() {
      return this.mSize;
   }

   public Object[] toArray() {
      Object[] result = new Object[this.mSize];
      System.arraycopy(this.mArray, 0, result, 0, this.mSize);
      return result;
   }

   public <T> T[] toArray(T[] array) {
      if (array.length < this.mSize) {
         T[] newArray = (T[])Array.newInstance(array.getClass().getComponentType(), this.mSize);
         array = newArray;
      }

      System.arraycopy(this.mArray, 0, array, 0, this.mSize);
      if (array.length > this.mSize) {
         array[this.mSize] = null;
      }

      return array;
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (object instanceof Set) {
         Set<?> set = (Set)object;
         if (this.size() != set.size()) {
            return false;
         } else {
            try {
               for(int i = 0; i < this.mSize; ++i) {
                  E mine = this.valueAt(i);
                  if (!set.contains(mine)) {
                     return false;
                  }
               }

               return true;
            } catch (NullPointerException var5) {
               return false;
            } catch (ClassCastException var6) {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int[] hashes = this.mHashes;
      int result = 0;
      int i = 0;

      for(int s = this.mSize; i < s; ++i) {
         result += hashes[i];
      }

      return result;
   }

   public String toString() {
      if (this.isEmpty()) {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC0IVg=="));
      } else {
         StringBuilder buffer = new StringBuilder(this.mSize * 14);
         buffer.append('{');

         for(int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
               buffer.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186Vg==")));
            }

            Object value = this.valueAt(i);
            if (value != this) {
               buffer.append(value);
            } else {
               buffer.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBcqCmUaLyhpJDAgOQhSVg==")));
            }
         }

         buffer.append('}');
         return buffer.toString();
      }
   }

   private MapCollections<E, E> getCollection() {
      if (this.mCollections == null) {
         this.mCollections = new MapCollections<E, E>() {
            protected int colGetSize() {
               return ArraySet.this.mSize;
            }

            protected Object colGetEntry(int index, int offset) {
               return ArraySet.this.mArray[index];
            }

            protected int colIndexOfKey(Object key) {
               return ArraySet.this.indexOf(key);
            }

            protected int colIndexOfValue(Object value) {
               return ArraySet.this.indexOf(value);
            }

            protected Map<E, E> colGetMap() {
               throw new UnsupportedOperationException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALHsFJyhgDiAs")));
            }

            protected void colPut(E key, E value) {
               ArraySet.this.add(key);
            }

            protected E colSetValue(int index, E value) {
               throw new UnsupportedOperationException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALHsFJyhgDiAs")));
            }

            protected void colRemoveAt(int index) {
               ArraySet.this.removeAt(index);
            }

            protected void colClear() {
               ArraySet.this.clear();
            }
         };
      }

      return this.mCollections;
   }

   public Iterator<E> iterator() {
      return this.getCollection().getKeySet().iterator();
   }

   public boolean containsAll(Collection<?> collection) {
      Iterator<?> it = collection.iterator();

      do {
         if (!it.hasNext()) {
            return true;
         }
      } while(this.contains(it.next()));

      return false;
   }

   public boolean addAll(Collection<? extends E> collection) {
      this.ensureCapacity(this.mSize + collection.size());
      boolean added = false;

      E value;
      for(Iterator<? extends E> var3 = collection.iterator(); var3.hasNext(); added |= this.add(value)) {
         value = var3.next();
      }

      return added;
   }

   public boolean removeAll(Collection<?> collection) {
      boolean removed = false;

      Object value;
      for(Iterator var3 = collection.iterator(); var3.hasNext(); removed |= this.remove(value)) {
         value = var3.next();
      }

      return removed;
   }

   public boolean retainAll(Collection<?> collection) {
      boolean removed = false;

      for(int i = this.mSize - 1; i >= 0; --i) {
         if (!collection.contains(this.mArray[i])) {
            this.removeAt(i);
            removed = true;
         }
      }

      return removed;
   }
}
