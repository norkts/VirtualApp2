package com.lody.virtual.helper.collection;

import com.lody.virtual.StringFog;
import java.util.Map;

public class SimpleArrayMap<K, V> {
   private static final boolean DEBUG = false;
   private static final String TAG = "ArrayMap";
   private static final int BASE_SIZE = 4;
   private static final int CACHE_SIZE = 10;
   static Object[] mBaseCache;
   static int mBaseCacheSize;
   static Object[] mTwiceBaseCache;
   static int mTwiceBaseCacheSize;
   int[] mHashes;
   Object[] mArray;
   int mSize;

   public SimpleArrayMap() {
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      this.mSize = 0;
   }

   public SimpleArrayMap(int capacity) {
      if (capacity == 0) {
         this.mHashes = ContainerHelpers.EMPTY_INTS;
         this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      } else {
         this.allocArrays(capacity);
      }

      this.mSize = 0;
   }

   public SimpleArrayMap(SimpleArrayMap map) {
      this();
      if (map != null) {
         this.putAll(map);
      }

   }

   private static void freeArrays(int[] hashes, Object[] array, int size) {
      Class var3;
      int i;
      if (hashes.length == 8) {
         var3 = ArrayMap.class;
         synchronized(ArrayMap.class) {
            if (mTwiceBaseCacheSize < 10) {
               array[0] = mTwiceBaseCache;
               array[1] = hashes;

               for(i = (size << 1) - 1; i >= 2; --i) {
                  array[i] = null;
               }

               mTwiceBaseCache = array;
               ++mTwiceBaseCacheSize;
            }
         }
      } else if (hashes.length == 4) {
         var3 = ArrayMap.class;
         synchronized(ArrayMap.class) {
            if (mBaseCacheSize < 10) {
               array[0] = mBaseCache;
               array[1] = hashes;

               for(i = (size << 1) - 1; i >= 2; --i) {
                  array[i] = null;
               }

               mBaseCache = array;
               ++mBaseCacheSize;
            }
         }
      }

   }

   int indexOf(Object key, int hash) {
      int N = this.mSize;
      if (N == 0) {
         return -1;
      } else {
         int index = ContainerHelpers.binarySearch(this.mHashes, N, hash);
         if (index < 0) {
            return index;
         } else if (key.equals(this.mArray[index << 1])) {
            return index;
         } else {
            int end;
            for(end = index + 1; end < N && this.mHashes[end] == hash; ++end) {
               if (key.equals(this.mArray[end << 1])) {
                  return end;
               }
            }

            for(int i = index - 1; i >= 0 && this.mHashes[i] == hash; --i) {
               if (key.equals(this.mArray[i << 1])) {
                  return i;
               }
            }

            return ~end;
         }
      }
   }

   int indexOfNull() {
      int N = this.mSize;
      if (N == 0) {
         return -1;
      } else {
         int index = ContainerHelpers.binarySearch(this.mHashes, N, 0);
         if (index < 0) {
            return index;
         } else if (null == this.mArray[index << 1]) {
            return index;
         } else {
            int end;
            for(end = index + 1; end < N && this.mHashes[end] == 0; ++end) {
               if (null == this.mArray[end << 1]) {
                  return end;
               }
            }

            for(int i = index - 1; i >= 0 && this.mHashes[i] == 0; --i) {
               if (null == this.mArray[i << 1]) {
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
         var2 = ArrayMap.class;
         synchronized(ArrayMap.class) {
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
         var2 = ArrayMap.class;
         synchronized(ArrayMap.class) {
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
      this.mArray = new Object[size << 1];
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
            System.arraycopy(oarray, 0, this.mArray, 0, this.mSize << 1);
         }

         freeArrays(ohashes, oarray, this.mSize);
      }

   }

   public boolean containsKey(Object key) {
      return this.indexOfKey(key) >= 0;
   }

   public int indexOfKey(Object key) {
      return key == null ? this.indexOfNull() : this.indexOf(key, key.hashCode());
   }

   int indexOfValue(Object value) {
      int N = this.mSize * 2;
      Object[] array = this.mArray;
      int i;
      if (value == null) {
         for(i = 1; i < N; i += 2) {
            if (array[i] == null) {
               return i >> 1;
            }
         }
      } else {
         for(i = 1; i < N; i += 2) {
            if (value.equals(array[i])) {
               return i >> 1;
            }
         }
      }

      return -1;
   }

   public boolean containsValue(Object value) {
      return this.indexOfValue(value) >= 0;
   }

   public V get(Object key) {
      int index = this.indexOfKey(key);
      return index >= 0 ? (V)this.mArray[(index << 1) + 1] : null;
   }

   public K keyAt(int index) {
      return (K)this.mArray[index << 1];
   }

   public V valueAt(int index) {
      return (V)this.mArray[(index << 1) + 1];
   }

   public V setValueAt(int index, V value) {
      index = (index << 1) + 1;
      V old = (V)this.mArray[index];
      this.mArray[index] = value;
      return old;
   }

   public boolean isEmpty() {
      return this.mSize <= 0;
   }

   public V put(K key, V value) {
      int hash;
      int index;
      if (key == null) {
         hash = 0;
         index = this.indexOfNull();
      } else {
         hash = key.hashCode();
         index = this.indexOf(key, hash);
      }

      if (index >= 0) {
         index = (index << 1) + 1;
         V old = (V)this.mArray[index];
         this.mArray[index] = value;
         return old;
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
            System.arraycopy(this.mArray, index << 1, this.mArray, index + 1 << 1, this.mSize - index << 1);
         }

         this.mHashes[index] = hash;
         this.mArray[index << 1] = key;
         this.mArray[(index << 1) + 1] = value;
         ++this.mSize;
         return null;
      }
   }

   public void putAll(SimpleArrayMap<? extends K, ? extends V> array) {
      int N = array.mSize;
      this.ensureCapacity(this.mSize + N);
      if (this.mSize == 0) {
         if (N > 0) {
            System.arraycopy(array.mHashes, 0, this.mHashes, 0, N);
            System.arraycopy(array.mArray, 0, this.mArray, 0, N << 1);
            this.mSize = N;
         }
      } else {
         for(int i = 0; i < N; ++i) {
            this.put(array.keyAt(i), array.valueAt(i));
         }
      }

   }

   public V remove(Object key) {
      int index = this.indexOfKey(key);
      return index >= 0 ? this.removeAt(index) : null;
   }

   public V removeAt(int index) {
      Object old = this.mArray[(index << 1) + 1];
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
            System.arraycopy(oarray, 0, this.mArray, 0, index << 1);
         }

         if (index < this.mSize) {
            System.arraycopy(ohashes, index + 1, this.mHashes, index, this.mSize - index);
            System.arraycopy(oarray, index + 1 << 1, this.mArray, index << 1, this.mSize - index << 1);
         }
      } else {
         --this.mSize;
         if (index < this.mSize) {
            System.arraycopy(this.mHashes, index + 1, this.mHashes, index, this.mSize - index);
            System.arraycopy(this.mArray, index + 1 << 1, this.mArray, index << 1, this.mSize - index << 1);
         }

         this.mArray[this.mSize << 1] = null;
         this.mArray[(this.mSize << 1) + 1] = null;
      }

      return (V) old;
   }

   public int size() {
      return this.mSize;
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (!(object instanceof Map)) {
         return false;
      } else {
         Map<?, ?> map = (Map)object;
         if (this.size() != map.size()) {
            return false;
         } else {
            try {
               for(int i = 0; i < this.mSize; ++i) {
                  K key = this.keyAt(i);
                  V mine = this.valueAt(i);
                  Object theirs = map.get(key);
                  if (mine == null) {
                     if (theirs != null || !map.containsKey(key)) {
                        return false;
                     }
                  } else if (!mine.equals(theirs)) {
                     return false;
                  }
               }

               return true;
            } catch (NullPointerException var7) {
               return false;
            } catch (ClassCastException var8) {
               return false;
            }
         }
      }
   }

   public int hashCode() {
      int[] hashes = this.mHashes;
      Object[] array = this.mArray;
      int result = 0;
      int i = 0;
      int v = 1;

      for(int s = this.mSize; i < s; v += 2) {
         Object value = array[v];
         result += hashes[i] ^ (value == null ? 0 : value.hashCode());
         ++i;
      }

      return result;
   }

   public String toString() {
      if (this.isEmpty()) {
         return "{}";
      } else {
         StringBuilder buffer = new StringBuilder(this.mSize * 28);
         buffer.append('{');

         for(int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
               buffer.append(", ");
            }

            Object key = this.keyAt(i);
            if (key != this) {
               buffer.append(key);
            } else {
               buffer.append("(this Map)");
            }

            buffer.append('=');
            Object value = this.valueAt(i);
            if (value != this) {
               buffer.append(value);
            } else {
               buffer.append("(this Map)");
            }
         }

         buffer.append('}');
         return buffer.toString();
      }
   }
}
