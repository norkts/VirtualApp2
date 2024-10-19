package com.lody.virtual.helper.collection;

import com.lody.virtual.StringFog;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class MapCollections<K, V> {
   MapCollections<K, V>.EntrySet mEntrySet;
   MapCollections<K, V>.KeySet mKeySet;
   MapCollections<K, V>.ValuesCollection mValues;

   public static <K, V> boolean containsAllHelper(Map<K, V> map, Collection<?> collection) {
      Iterator<?> it = collection.iterator();

      do {
         if (!it.hasNext()) {
            return true;
         }
      } while(map.containsKey(it.next()));

      return false;
   }

   public static <K, V> boolean removeAllHelper(Map<K, V> map, Collection<?> collection) {
      int oldSize = map.size();
      Iterator<?> it = collection.iterator();

      while(it.hasNext()) {
         map.remove(it.next());
      }

      return oldSize != map.size();
   }

   public static <K, V> boolean retainAllHelper(Map<K, V> map, Collection<?> collection) {
      int oldSize = map.size();
      Iterator<K> it = map.keySet().iterator();

      while(it.hasNext()) {
         if (!collection.contains(it.next())) {
            it.remove();
         }
      }

      return oldSize != map.size();
   }

   public static <T> boolean equalsSetHelper(Set<T> set, Object object) {
      if (set == object) {
         return true;
      } else if (object instanceof Set) {
         Set<?> s = (Set)object;

         try {
            return set.size() == s.size() && set.containsAll(s);
         } catch (NullPointerException var4) {
            return false;
         } catch (ClassCastException var5) {
            return false;
         }
      } else {
         return false;
      }
   }

   public Object[] toArrayHelper(int offset) {
      int N = this.colGetSize();
      Object[] result = new Object[N];

      for(int i = 0; i < N; ++i) {
         result[i] = this.colGetEntry(i, offset);
      }

      return result;
   }

   public <T> T[] toArrayHelper(T[] array, int offset) {
      int N = this.colGetSize();
      if (array.length < N) {
         T[] newArray = (T[])Array.newInstance(array.getClass().getComponentType(), N);
         array = newArray;
      }

      for(int i = 0; i < N; ++i) {
         array[i] = (T)this.colGetEntry(i, offset);
      }

      if (array.length > N) {
         array[N] = null;
      }

      return array;
   }

   public Set<Map.Entry<K, V>> getEntrySet() {
      if (this.mEntrySet == null) {
         this.mEntrySet = new EntrySet();
      }

      return this.mEntrySet;
   }

   public Set<K> getKeySet() {
      if (this.mKeySet == null) {
         this.mKeySet = new KeySet();
      }

      return this.mKeySet;
   }

   public Collection<V> getValues() {
      if (this.mValues == null) {
         this.mValues = new ValuesCollection();
      }

      return this.mValues;
   }

   protected abstract int colGetSize();

   protected abstract Object colGetEntry(int var1, int var2);

   protected abstract int colIndexOfKey(Object var1);

   protected abstract int colIndexOfValue(Object var1);

   protected abstract Map<K, V> colGetMap();

   protected abstract void colPut(K var1, V var2);

   protected abstract V colSetValue(int var1, V var2);

   protected abstract void colRemoveAt(int var1);

   protected abstract void colClear();

   final class ValuesCollection implements Collection<V> {
      public boolean add(V object) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection<? extends V> collection) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         MapCollections.this.colClear();
      }

      public boolean contains(Object object) {
         return MapCollections.this.colIndexOfValue(object) >= 0;
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

      public boolean isEmpty() {
         return MapCollections.this.colGetSize() == 0;
      }

      public Iterator<V> iterator() {
         return new ArrayIterator(1);
      }

      public boolean remove(Object object) {
         int index = MapCollections.this.colIndexOfValue(object);
         if (index >= 0) {
            MapCollections.this.colRemoveAt(index);
            return true;
         } else {
            return false;
         }
      }

      public boolean removeAll(Collection<?> collection) {
         int N = MapCollections.this.colGetSize();
         boolean changed = false;

         for(int i = 0; i < N; ++i) {
            Object cur = MapCollections.this.colGetEntry(i, 1);
            if (collection.contains(cur)) {
               MapCollections.this.colRemoveAt(i);
               --i;
               --N;
               changed = true;
            }
         }

         return changed;
      }

      public boolean retainAll(Collection<?> collection) {
         int N = MapCollections.this.colGetSize();
         boolean changed = false;

         for(int i = 0; i < N; ++i) {
            Object cur = MapCollections.this.colGetEntry(i, 1);
            if (!collection.contains(cur)) {
               MapCollections.this.colRemoveAt(i);
               --i;
               --N;
               changed = true;
            }
         }

         return changed;
      }

      public int size() {
         return MapCollections.this.colGetSize();
      }

      public Object[] toArray() {
         return MapCollections.this.toArrayHelper(1);
      }

      public <T> T[] toArray(T[] array) {
         return MapCollections.this.toArrayHelper(array, 1);
      }
   }

   final class KeySet implements Set<K> {
      public boolean add(K object) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection<? extends K> collection) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         MapCollections.this.colClear();
      }

      public boolean contains(Object object) {
         return MapCollections.this.colIndexOfKey(object) >= 0;
      }

      public boolean containsAll(Collection<?> collection) {
         return MapCollections.containsAllHelper(MapCollections.this.colGetMap(), collection);
      }

      public boolean isEmpty() {
         return MapCollections.this.colGetSize() == 0;
      }

      public Iterator<K> iterator() {
         return new ArrayIterator(0);
      }

      public boolean remove(Object object) {
         int index = MapCollections.this.colIndexOfKey(object);
         if (index >= 0) {
            MapCollections.this.colRemoveAt(index);
            return true;
         } else {
            return false;
         }
      }

      public boolean removeAll(Collection<?> collection) {
         return MapCollections.removeAllHelper(MapCollections.this.colGetMap(), collection);
      }

      public boolean retainAll(Collection<?> collection) {
         return MapCollections.retainAllHelper(MapCollections.this.colGetMap(), collection);
      }

      public int size() {
         return MapCollections.this.colGetSize();
      }

      public Object[] toArray() {
         return MapCollections.this.toArrayHelper(0);
      }

      public <T> T[] toArray(T[] array) {
         return MapCollections.this.toArrayHelper(array, 0);
      }

      public boolean equals(Object object) {
         return MapCollections.equalsSetHelper(this, object);
      }

      public int hashCode() {
         int result = 0;

         for(int i = MapCollections.this.colGetSize() - 1; i >= 0; --i) {
            Object obj = MapCollections.this.colGetEntry(i, 0);
            result += obj == null ? 0 : obj.hashCode();
         }

         return result;
      }
   }

   final class EntrySet implements Set<Map.Entry<K, V>> {
      public boolean add(Map.Entry<K, V> object) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
         int oldSize = MapCollections.this.colGetSize();
         Iterator var3 = collection.iterator();

         while(var3.hasNext()) {
            Map.Entry<K, V> entry = (Map.Entry)var3.next();
            MapCollections.this.colPut(entry.getKey(), entry.getValue());
         }

         return oldSize != MapCollections.this.colGetSize();
      }

      public void clear() {
         MapCollections.this.colClear();
      }

      public boolean contains(Object o) {
         if (!(o instanceof Map.Entry)) {
            return false;
         } else {
            Map.Entry<?, ?> e = (Map.Entry)o;
            int index = MapCollections.this.colIndexOfKey(e.getKey());
            if (index < 0) {
               return false;
            } else {
               Object foundVal = MapCollections.this.colGetEntry(index, 1);
               return ContainerHelpers.equal(foundVal, e.getValue());
            }
         }
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

      public boolean isEmpty() {
         return MapCollections.this.colGetSize() == 0;
      }

      public Iterator<Map.Entry<K, V>> iterator() {
         return MapCollections.this.new MapIterator();
      }

      public boolean remove(Object object) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection<?> collection) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection<?> collection) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return MapCollections.this.colGetSize();
      }

      public Object[] toArray() {
         throw new UnsupportedOperationException();
      }

      public <T> T[] toArray(T[] array) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object object) {
         return MapCollections.equalsSetHelper(this, object);
      }

      public int hashCode() {
         int result = 0;

         for(int i = MapCollections.this.colGetSize() - 1; i >= 0; --i) {
            Object key = MapCollections.this.colGetEntry(i, 0);
            Object value = MapCollections.this.colGetEntry(i, 1);
            result += (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
         }

         return result;
      }
   }

   final class MapIterator implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V> {
      int mEnd = MapCollections.this.colGetSize() - 1;
      int mIndex = -1;
      boolean mEntryValid = false;

      public boolean hasNext() {
         return this.mIndex < this.mEnd;
      }

      public Map.Entry<K, V> next() {
         ++this.mIndex;
         this.mEntryValid = true;
         return this;
      }

      public void remove() {
         if (!this.mEntryValid) {
            throw new IllegalStateException();
         } else {
            MapCollections.this.colRemoveAt(this.mIndex);
            --this.mIndex;
            --this.mEnd;
            this.mEntryValid = false;
         }
      }

      public K getKey() {
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else {
            return (K)MapCollections.this.colGetEntry(this.mIndex, 0);
         }
      }

      public V getValue() {
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else {
            return (V)MapCollections.this.colGetEntry(this.mIndex, 1);
         }
      }

      public V setValue(V object) {
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else {
            return MapCollections.this.colSetValue(this.mIndex, object);
         }
      }

      public final boolean equals(Object o) {
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else if (!(o instanceof Map.Entry)) {
            return false;
         } else {
            Map.Entry<?, ?> e = (Map.Entry)o;
            return ContainerHelpers.equal(e.getKey(), MapCollections.this.colGetEntry(this.mIndex, 0)) && ContainerHelpers.equal(e.getValue(), MapCollections.this.colGetEntry(this.mIndex, 1));
         }
      }

      public final int hashCode() {
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else {
            Object key = MapCollections.this.colGetEntry(this.mIndex, 0);
            Object value = MapCollections.this.colGetEntry(this.mIndex, 1);
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
         }
      }

      public final String toString() {
         return this.getKey() + "=" + this.getValue();
      }
   }

   final class ArrayIterator<T> implements Iterator<T> {
      final int mOffset;
      int mSize;
      int mIndex;
      boolean mCanRemove = false;

      ArrayIterator(int offset) {
         this.mOffset = offset;
         this.mSize = MapCollections.this.colGetSize();
      }

      public boolean hasNext() {
         return this.mIndex < this.mSize;
      }

      public T next() {
         T res = (T)MapCollections.this.colGetEntry(this.mIndex, this.mOffset);
         ++this.mIndex;
         this.mCanRemove = true;
         return res;
      }

      public void remove() {
         if (!this.mCanRemove) {
            throw new IllegalStateException();
         } else {
            --this.mIndex;
            --this.mSize;
            this.mCanRemove = false;
            MapCollections.this.colRemoveAt(this.mIndex);
         }
      }
   }
}
