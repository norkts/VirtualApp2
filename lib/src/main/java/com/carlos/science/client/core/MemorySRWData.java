package com.carlos.science.client.core;

import android.text.TextUtils;
import com.carlos.libcommon.StringFog;

public final class MemorySRWData {
   private String searchValue;
   private SearchValueType defaultType;
   private String writeValue;
   private boolean addressPermission;
   private static final String ARRY_SPLIT = ";";
   private static final String ARRY_DATA_SPLIT = "_";

   private MemorySRWData() {
      this.defaultType = MemorySRWData.SearchValueType.i32;
      this.addressPermission = false;
   }

   public static MemorySRWData AddMemorySearch(String value, SearchValueType type) {
      if (!TextUtils.isEmpty(value) && type != null) {
         MemorySRWData memorySearch = new MemorySRWData();
         String searchValueType = MemorySRWData.SearchValueType.toString(type);
         memorySearch.searchValue = value + ARRY_DATA_SPLIT + 0 + ARRY_DATA_SPLIT + searchValueType + ARRY_SPLIT;
         memorySearch.defaultType = type;
         memorySearch.writeValue = "";
         return memorySearch;
      } else {
         throw new NullPointerException("value = " + value + "  type = " + type + " 、is null");
      }
   }

   public MemorySRWData append(String value, int offset) {
      this.searchValue = this.searchValue + value + ARRY_DATA_SPLIT + offset + ARRY_DATA_SPLIT + this.defaultType + ARRY_SPLIT;
      return this;
   }

   public MemorySRWData append(String value, SearchValueType type, int offset) {
      this.searchValue = this.searchValue + value + ARRY_DATA_SPLIT + offset + ARRY_DATA_SPLIT + MemorySRWData.SearchValueType.toString(type) + ARRY_SPLIT;
      return this;
   }

   public MemorySRWData writeValue(String addOffset, SearchValueType type, String value) {
      String searchValueType = MemorySRWData.SearchValueType.toString(type);
      this.writeValue = this.writeValue + addOffset + ARRY_DATA_SPLIT + searchValueType + ARRY_DATA_SPLIT + value + ARRY_SPLIT;
      return this;
   }

   public MemorySRWData writeValue(String addOffset, String value) {
      return this.writeValue(addOffset, this.defaultType, value);
   }

   public MemorySRWData setAddressPermission(boolean permission) {
      this.addressPermission = permission;
      return this;
   }

   public boolean getAddressPermission() {
      return this.addressPermission;
   }

   public void clear() {
      this.searchValue = "";
   }

   public String getSearchValue() {
      return this.searchValue;
   }

   public String getWriteValue() {
      return this.writeValue;
   }

   public static enum SearchValueType {
      i8,
      i16,
      i32,
      i64,
      f32,
      f64,
      I8,
      I16,
      I32,
      I64,
      F32,
      F64;

      public static String toString(SearchValueType type) {
         switch (type) {
            case i8:
            case I8:
               return "i8";
            case i16:
            case I16:
               return "i16";
            case i32:
            case I32:
               return "i32";
            case i64:
            case I64:
               return "i64";
            case f32:
            case F32:
               return "f32";
            case f64:
            case F64:
               return "f64";
            default:
               throw new RuntimeException("error type!");
         }
      }
   }
}
