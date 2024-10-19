package external.org.apache.commons.lang3;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {
   public static final String EMPTY = "";
   public static final int INDEX_NOT_FOUND = -1;
   private static final int PAD_LIMIT = 8192;
   private static final Pattern WHITESPACE_BLOCK = Pattern.compile("\\s+");

   public static boolean isEmpty(CharSequence cs) {
      return cs == null || cs.length() == 0;
   }

   public static boolean isNotEmpty(CharSequence cs) {
      return !isEmpty(cs);
   }

   public static boolean isBlank(CharSequence cs) {
      int strLen;
      if (cs != null && (strLen = cs.length()) != 0) {
         for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(cs.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean isNotBlank(CharSequence cs) {
      return !isBlank(cs);
   }

   public static String trim(String str) {
      return str == null ? null : str.trim();
   }

   public static String trimToNull(String str) {
      String ts = trim(str);
      return isEmpty(ts) ? null : ts;
   }

   public static String trimToEmpty(String str) {
      return str == null ? "" : str.trim();
   }

   public static String strip(String str) {
      return strip(str, (String)null);
   }

   public static String stripToNull(String str) {
      if (str == null) {
         return null;
      } else {
         str = strip(str, (String)null);
         return str.length() == 0 ? null : str;
      }
   }

   public static String stripToEmpty(String str) {
      return str == null ? "" : strip(str, (String)null);
   }

   public static String strip(String str, String stripChars) {
      if (isEmpty(str)) {
         return str;
      } else {
         str = stripStart(str, stripChars);
         return stripEnd(str, stripChars);
      }
   }

   public static String stripStart(String str, String stripChars) {
      int strLen;
      if (str != null && (strLen = str.length()) != 0) {
         int start = 0;
         if (stripChars == null) {
            while(start != strLen && Character.isWhitespace(str.charAt(start))) {
               ++start;
            }
         } else {
            if (stripChars.length() == 0) {
               return str;
            }

            while(start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
               ++start;
            }
         }

         return str.substring(start);
      } else {
         return str;
      }
   }

   public static String stripEnd(String str, String stripChars) {
      int end;
      if (str != null && (end = str.length()) != 0) {
         if (stripChars == null) {
            while(end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
               --end;
            }
         } else {
            if (stripChars.length() == 0) {
               return str;
            }

            while(end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
               --end;
            }
         }

         return str.substring(0, end);
      } else {
         return str;
      }
   }

   public static String[] stripAll(String... strs) {
      return stripAll(strs, (String)null);
   }

   public static String[] stripAll(String[] strs, String stripChars) {
      int strsLen;
      if (strs != null && (strsLen = strs.length) != 0) {
         String[] newArr = new String[strsLen];

         for(int i = 0; i < strsLen; ++i) {
            newArr[i] = strip(strs[i], stripChars);
         }

         return newArr;
      } else {
         return strs;
      }
   }

   public static String stripAccents(String input) {
      if (input == null) {
         return null;
      } else {
         SecurityException se;
         try {
            se = null;
            String result;
            if (StringUtils.InitStripAccents.java6NormalizeMethod != null) {
               result = removeAccentsJava6(input);
            } else {
               if (StringUtils.InitStripAccents.sunDecomposeMethod == null) {
                  throw new UnsupportedOperationException("The stripAccents(CharSequence) method requires at least Java6, but got: " + StringUtils.InitStripAccents.java6Exception + "; or a Sun JVM: " + StringUtils.InitStripAccents.sunException);
               }

               result = removeAccentsSUN(input);
            }

            return result;
         } catch (IllegalArgumentException var2) {
            IllegalArgumentException iae = var2;
            throw new RuntimeException("IllegalArgumentException occurred", iae);
         } catch (IllegalAccessException var3) {
            IllegalAccessException iae = var3;
            throw new RuntimeException("IllegalAccessException occurred", iae);
         } catch (InvocationTargetException var4) {
            InvocationTargetException ite = var4;
            throw new RuntimeException("InvocationTargetException occurred", ite);
         } catch (SecurityException var5) {
            se = var5;
            throw new RuntimeException("SecurityException occurred", se);
         }
      }
   }

   private static String removeAccentsJava6(CharSequence text) throws IllegalAccessException, InvocationTargetException {
      if (StringUtils.InitStripAccents.java6NormalizeMethod != null && StringUtils.InitStripAccents.java6NormalizerFormNFD != null) {
         String result = (String)StringUtils.InitStripAccents.java6NormalizeMethod.invoke((Object)null, text, StringUtils.InitStripAccents.java6NormalizerFormNFD);
         result = StringUtils.InitStripAccents.java6Pattern.matcher(result).replaceAll("");
         return result;
      } else {
         throw new IllegalStateException("java.text.Normalizer is not available", StringUtils.InitStripAccents.java6Exception);
      }
   }

   private static String removeAccentsSUN(CharSequence text) throws IllegalAccessException, InvocationTargetException {
      if (StringUtils.InitStripAccents.sunDecomposeMethod == null) {
         throw new IllegalStateException("sun.text.Normalizer is not available", StringUtils.InitStripAccents.sunException);
      } else {
         String result = (String)StringUtils.InitStripAccents.sunDecomposeMethod.invoke((Object)null, text, Boolean.FALSE, 0);
         result = StringUtils.InitStripAccents.sunPattern.matcher(result).replaceAll("");
         return result;
      }
   }

   public static boolean equals(CharSequence cs1, CharSequence cs2) {
      return cs1 == null ? cs2 == null : cs1.equals(cs2);
   }

   public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
      if (str1 != null && str2 != null) {
         return CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, Math.max(str1.length(), str2.length()));
      } else {
         return str1 == str2;
      }
   }

   public static int indexOf(CharSequence seq, int searchChar) {
      return isEmpty(seq) ? -1 : CharSequenceUtils.indexOf(seq, searchChar, 0);
   }

   public static int indexOf(CharSequence seq, int searchChar, int startPos) {
      return isEmpty(seq) ? -1 : CharSequenceUtils.indexOf(seq, searchChar, startPos);
   }

   public static int indexOf(CharSequence seq, CharSequence searchSeq) {
      return seq != null && searchSeq != null ? CharSequenceUtils.indexOf(seq, searchSeq, 0) : -1;
   }

   public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
      return seq != null && searchSeq != null ? CharSequenceUtils.indexOf(seq, searchSeq, startPos) : -1;
   }

   public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
      return ordinalIndexOf(str, searchStr, ordinal, false);
   }

   private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
      if (str != null && searchStr != null && ordinal > 0) {
         if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
         } else {
            int found = 0;
            int index = lastIndex ? str.length() : -1;

            do {
               if (lastIndex) {
                  index = CharSequenceUtils.lastIndexOf(str, searchStr, index - 1);
               } else {
                  index = CharSequenceUtils.indexOf(str, searchStr, index + 1);
               }

               if (index < 0) {
                  return index;
               }

               ++found;
            } while(found < ordinal);

            return index;
         }
      } else {
         return -1;
      }
   }

   public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
      return indexOfIgnoreCase(str, searchStr, 0);
   }

   public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
      if (str != null && searchStr != null) {
         if (startPos < 0) {
            startPos = 0;
         }

         int endLimit = str.length() - searchStr.length() + 1;
         if (startPos > endLimit) {
            return -1;
         } else if (searchStr.length() == 0) {
            return startPos;
         } else {
            for(int i = startPos; i < endLimit; ++i) {
               if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                  return i;
               }
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public static int lastIndexOf(CharSequence seq, int searchChar) {
      return isEmpty(seq) ? -1 : CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
   }

   public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
      return isEmpty(seq) ? -1 : CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
   }

   public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
      return seq != null && searchSeq != null ? CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length()) : -1;
   }

   public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
      return ordinalIndexOf(str, searchStr, ordinal, true);
   }

   public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
      return seq != null && searchSeq != null ? CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos) : -1;
   }

   public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
      return str != null && searchStr != null ? lastIndexOfIgnoreCase(str, searchStr, str.length()) : -1;
   }

   public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
      if (str != null && searchStr != null) {
         if (startPos > str.length() - searchStr.length()) {
            startPos = str.length() - searchStr.length();
         }

         if (startPos < 0) {
            return -1;
         } else if (searchStr.length() == 0) {
            return startPos;
         } else {
            for(int i = startPos; i >= 0; --i) {
               if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                  return i;
               }
            }

            return -1;
         }
      } else {
         return -1;
      }
   }

   public static boolean contains(CharSequence seq, int searchChar) {
      if (isEmpty(seq)) {
         return false;
      } else {
         return CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0;
      }
   }

   public static boolean contains(CharSequence seq, CharSequence searchSeq) {
      if (seq != null && searchSeq != null) {
         return CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0;
      } else {
         return false;
      }
   }

   public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
      if (str != null && searchStr != null) {
         int len = searchStr.length();
         int max = str.length() - len;

         for(int i = 0; i <= max; ++i) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, len)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean containsWhitespace(CharSequence seq) {
      if (isEmpty(seq)) {
         return false;
      } else {
         int strLen = seq.length();

         for(int i = 0; i < strLen; ++i) {
            if (Character.isWhitespace(seq.charAt(i))) {
               return true;
            }
         }

         return false;
      }
   }

   public static int indexOfAny(CharSequence cs, char... searchChars) {
      if (!isEmpty(cs) && !ArrayUtils.isEmpty(searchChars)) {
         int csLen = cs.length();
         int csLast = csLen - 1;
         int searchLen = searchChars.length;
         int searchLast = searchLen - 1;

         for(int i = 0; i < csLen; ++i) {
            char ch = cs.charAt(i);

            for(int j = 0; j < searchLen; ++j) {
               if (searchChars[j] == ch) {
                  if (i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch)) {
                     return i;
                  }

                  if (searchChars[j + 1] == cs.charAt(i + 1)) {
                     return i;
                  }
               }
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public static int indexOfAny(CharSequence cs, String searchChars) {
      return !isEmpty(cs) && !isEmpty(searchChars) ? indexOfAny(cs, searchChars.toCharArray()) : -1;
   }

   public static boolean containsAny(CharSequence cs, char... searchChars) {
      if (!isEmpty(cs) && !ArrayUtils.isEmpty(searchChars)) {
         int csLength = cs.length();
         int searchLength = searchChars.length;
         int csLast = csLength - 1;
         int searchLast = searchLength - 1;

         for(int i = 0; i < csLength; ++i) {
            char ch = cs.charAt(i);

            for(int j = 0; j < searchLength; ++j) {
               if (searchChars[j] == ch) {
                  if (!Character.isHighSurrogate(ch)) {
                     return true;
                  }

                  if (j == searchLast) {
                     return true;
                  }

                  if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                     return true;
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
      return searchChars == null ? false : containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
   }

   public static int indexOfAnyBut(CharSequence cs, char... searchChars) {
      if (!isEmpty(cs) && !ArrayUtils.isEmpty(searchChars)) {
         int csLen = cs.length();
         int csLast = csLen - 1;
         int searchLen = searchChars.length;
         int searchLast = searchLen - 1;

         label38:
         for(int i = 0; i < csLen; ++i) {
            char ch = cs.charAt(i);

            for(int j = 0; j < searchLen; ++j) {
               if (searchChars[j] == ch && (i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch) || searchChars[j + 1] == cs.charAt(i + 1))) {
                  continue label38;
               }
            }

            return i;
         }

         return -1;
      } else {
         return -1;
      }
   }

   public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
      if (!isEmpty(seq) && !isEmpty(searchChars)) {
         int strLen = seq.length();

         for(int i = 0; i < strLen; ++i) {
            char ch = seq.charAt(i);
            boolean chFound = CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0;
            if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
               char ch2 = seq.charAt(i + 1);
               if (chFound && CharSequenceUtils.indexOf(searchChars, ch2, 0) < 0) {
                  return i;
               }
            } else if (!chFound) {
               return i;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public static boolean containsOnly(CharSequence cs, char... valid) {
      if (valid != null && cs != null) {
         if (cs.length() == 0) {
            return true;
         } else if (valid.length == 0) {
            return false;
         } else {
            return indexOfAnyBut(cs, valid) == -1;
         }
      } else {
         return false;
      }
   }

   public static boolean containsOnly(CharSequence cs, String validChars) {
      return cs != null && validChars != null ? containsOnly(cs, validChars.toCharArray()) : false;
   }

   public static boolean containsNone(CharSequence cs, char... searchChars) {
      if (cs != null && searchChars != null) {
         int csLen = cs.length();
         int csLast = csLen - 1;
         int searchLen = searchChars.length;
         int searchLast = searchLen - 1;

         for(int i = 0; i < csLen; ++i) {
            char ch = cs.charAt(i);

            for(int j = 0; j < searchLen; ++j) {
               if (searchChars[j] == ch) {
                  if (!Character.isHighSurrogate(ch)) {
                     return false;
                  }

                  if (j == searchLast) {
                     return false;
                  }

                  if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                     return false;
                  }
               }
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean containsNone(CharSequence cs, String invalidChars) {
      return cs != null && invalidChars != null ? containsNone(cs, invalidChars.toCharArray()) : true;
   }

   public static int indexOfAny(CharSequence str, CharSequence... searchStrs) {
      if (str != null && searchStrs != null) {
         int sz = searchStrs.length;
         int ret = Integer.MAX_VALUE;
         int tmp = false;

         for(int i = 0; i < sz; ++i) {
            CharSequence search = searchStrs[i];
            if (search != null) {
               int tmp = CharSequenceUtils.indexOf(str, search, 0);
               if (tmp != -1 && tmp < ret) {
                  ret = tmp;
               }
            }
         }

         return ret == Integer.MAX_VALUE ? -1 : ret;
      } else {
         return -1;
      }
   }

   public static int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
      if (str != null && searchStrs != null) {
         int sz = searchStrs.length;
         int ret = -1;
         int tmp = false;

         for(int i = 0; i < sz; ++i) {
            CharSequence search = searchStrs[i];
            if (search != null) {
               int tmp = CharSequenceUtils.lastIndexOf(str, search, str.length());
               if (tmp > ret) {
                  ret = tmp;
               }
            }
         }

         return ret;
      } else {
         return -1;
      }
   }

   public static String substring(String str, int start) {
      if (str == null) {
         return null;
      } else {
         if (start < 0) {
            start += str.length();
         }

         if (start < 0) {
            start = 0;
         }

         return start > str.length() ? "" : str.substring(start);
      }
   }

   public static String substring(String str, int start, int end) {
      if (str == null) {
         return null;
      } else {
         if (end < 0) {
            end += str.length();
         }

         if (start < 0) {
            start += str.length();
         }

         if (end > str.length()) {
            end = str.length();
         }

         if (start > end) {
            return "";
         } else {
            if (start < 0) {
               start = 0;
            }

            if (end < 0) {
               end = 0;
            }

            return str.substring(start, end);
         }
      }
   }

   public static String left(String str, int len) {
      if (str == null) {
         return null;
      } else if (len < 0) {
         return "";
      } else {
         return str.length() <= len ? str : str.substring(0, len);
      }
   }

   public static String right(String str, int len) {
      if (str == null) {
         return null;
      } else if (len < 0) {
         return "";
      } else {
         return str.length() <= len ? str : str.substring(str.length() - len);
      }
   }

   public static String mid(String str, int pos, int len) {
      if (str == null) {
         return null;
      } else if (len >= 0 && pos <= str.length()) {
         if (pos < 0) {
            pos = 0;
         }

         return str.length() <= pos + len ? str.substring(pos) : str.substring(pos, pos + len);
      } else {
         return "";
      }
   }

   public static String substringBefore(String str, String separator) {
      if (!isEmpty(str) && separator != null) {
         if (separator.length() == 0) {
            return "";
         } else {
            int pos = str.indexOf(separator);
            return pos == -1 ? str : str.substring(0, pos);
         }
      } else {
         return str;
      }
   }

   public static String substringAfter(String str, String separator) {
      if (isEmpty(str)) {
         return str;
      } else if (separator == null) {
         return "";
      } else {
         int pos = str.indexOf(separator);
         return pos == -1 ? "" : str.substring(pos + separator.length());
      }
   }

   public static String substringBeforeLast(String str, String separator) {
      if (!isEmpty(str) && !isEmpty(separator)) {
         int pos = str.lastIndexOf(separator);
         return pos == -1 ? str : str.substring(0, pos);
      } else {
         return str;
      }
   }

   public static String substringAfterLast(String str, String separator) {
      if (isEmpty(str)) {
         return str;
      } else if (isEmpty(separator)) {
         return "";
      } else {
         int pos = str.lastIndexOf(separator);
         return pos != -1 && pos != str.length() - separator.length() ? str.substring(pos + separator.length()) : "";
      }
   }

   public static String substringBetween(String str, String tag) {
      return substringBetween(str, tag, tag);
   }

   public static String substringBetween(String str, String open, String close) {
      if (str != null && open != null && close != null) {
         int start = str.indexOf(open);
         if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
               return str.substring(start + open.length(), end);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static String[] substringsBetween(String str, String open, String close) {
      if (str != null && !isEmpty(open) && !isEmpty(close)) {
         int strLen = str.length();
         if (strLen == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
         } else {
            int closeLen = close.length();
            int openLen = open.length();
            List<String> list = new ArrayList();

            int end;
            for(int pos = 0; pos < strLen - closeLen; pos = end + closeLen) {
               int start = str.indexOf(open, pos);
               if (start < 0) {
                  break;
               }

               start += openLen;
               end = str.indexOf(close, start);
               if (end < 0) {
                  break;
               }

               list.add(str.substring(start, end));
            }

            return list.isEmpty() ? null : (String[])list.toArray(new String[list.size()]);
         }
      } else {
         return null;
      }
   }

   public static String[] split(String str) {
      return split(str, (String)null, -1);
   }

   public static String[] split(String str, char separatorChar) {
      return splitWorker(str, separatorChar, false);
   }

   public static String[] split(String str, String separatorChars) {
      return splitWorker(str, separatorChars, -1, false);
   }

   public static String[] split(String str, String separatorChars, int max) {
      return splitWorker(str, separatorChars, max, false);
   }

   public static String[] splitByWholeSeparator(String str, String separator) {
      return splitByWholeSeparatorWorker(str, separator, -1, false);
   }

   public static String[] splitByWholeSeparator(String str, String separator, int max) {
      return splitByWholeSeparatorWorker(str, separator, max, false);
   }

   public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
      return splitByWholeSeparatorWorker(str, separator, -1, true);
   }

   public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
      return splitByWholeSeparatorWorker(str, separator, max, true);
   }

   private static String[] splitByWholeSeparatorWorker(String str, String separator, int max, boolean preserveAllTokens) {
      if (str == null) {
         return null;
      } else {
         int len = str.length();
         if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
         } else if (separator != null && !"".equals(separator)) {
            int separatorLength = separator.length();
            ArrayList<String> substrings = new ArrayList();
            int numberOfSubstrings = 0;
            int beg = 0;
            int end = 0;

            while(end < len) {
               end = str.indexOf(separator, beg);
               if (end > -1) {
                  if (end > beg) {
                     ++numberOfSubstrings;
                     if (numberOfSubstrings == max) {
                        end = len;
                        substrings.add(str.substring(beg));
                     } else {
                        substrings.add(str.substring(beg, end));
                        beg = end + separatorLength;
                     }
                  } else {
                     if (preserveAllTokens) {
                        ++numberOfSubstrings;
                        if (numberOfSubstrings == max) {
                           end = len;
                           substrings.add(str.substring(beg));
                        } else {
                           substrings.add("");
                        }
                     }

                     beg = end + separatorLength;
                  }
               } else {
                  substrings.add(str.substring(beg));
                  end = len;
               }
            }

            return (String[])substrings.toArray(new String[substrings.size()]);
         } else {
            return splitWorker(str, (String)null, max, preserveAllTokens);
         }
      }
   }

   public static String[] splitPreserveAllTokens(String str) {
      return splitWorker(str, (String)null, -1, true);
   }

   public static String[] splitPreserveAllTokens(String str, char separatorChar) {
      return splitWorker(str, separatorChar, true);
   }

   private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
      if (str == null) {
         return null;
      } else {
         int len = str.length();
         if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
         } else {
            List<String> list = new ArrayList();
            int i = 0;
            int start = 0;
            boolean match = false;
            boolean lastMatch = false;

            while(true) {
               while(i < len) {
                  if (str.charAt(i) == separatorChar) {
                     if (match || preserveAllTokens) {
                        list.add(str.substring(start, i));
                        match = false;
                        lastMatch = true;
                     }

                     ++i;
                     start = i;
                  } else {
                     lastMatch = false;
                     match = true;
                     ++i;
                  }
               }

               if (match || preserveAllTokens && lastMatch) {
                  list.add(str.substring(start, i));
               }

               return (String[])list.toArray(new String[list.size()]);
            }
         }
      }
   }

   public static String[] splitPreserveAllTokens(String str, String separatorChars) {
      return splitWorker(str, separatorChars, -1, true);
   }

   public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
      return splitWorker(str, separatorChars, max, true);
   }

   private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
      if (str == null) {
         return null;
      } else {
         int len = str.length();
         if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
         } else {
            List<String> list = new ArrayList();
            int sizePlus1 = 1;
            int i = 0;
            int start = 0;
            boolean match = false;
            boolean lastMatch = false;
            if (separatorChars != null) {
               if (separatorChars.length() != 1) {
                  label87:
                  while(true) {
                     while(true) {
                        if (i >= len) {
                           break label87;
                        }

                        if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                           if (match || preserveAllTokens) {
                              lastMatch = true;
                              if (sizePlus1++ == max) {
                                 i = len;
                                 lastMatch = false;
                              }

                              list.add(str.substring(start, i));
                              match = false;
                           }

                           ++i;
                           start = i;
                        } else {
                           lastMatch = false;
                           match = true;
                           ++i;
                        }
                     }
                  }
               } else {
                  char sep = separatorChars.charAt(0);

                  label71:
                  while(true) {
                     while(true) {
                        if (i >= len) {
                           break label71;
                        }

                        if (str.charAt(i) == sep) {
                           if (match || preserveAllTokens) {
                              lastMatch = true;
                              if (sizePlus1++ == max) {
                                 i = len;
                                 lastMatch = false;
                              }

                              list.add(str.substring(start, i));
                              match = false;
                           }

                           ++i;
                           start = i;
                        } else {
                           lastMatch = false;
                           match = true;
                           ++i;
                        }
                     }
                  }
               }
            } else {
               label103:
               while(true) {
                  while(true) {
                     if (i >= len) {
                        break label103;
                     }

                     if (Character.isWhitespace(str.charAt(i))) {
                        if (match || preserveAllTokens) {
                           lastMatch = true;
                           if (sizePlus1++ == max) {
                              i = len;
                              lastMatch = false;
                           }

                           list.add(str.substring(start, i));
                           match = false;
                        }

                        ++i;
                        start = i;
                     } else {
                        lastMatch = false;
                        match = true;
                        ++i;
                     }
                  }
               }
            }

            if (match || preserveAllTokens && lastMatch) {
               list.add(str.substring(start, i));
            }

            return (String[])list.toArray(new String[list.size()]);
         }
      }
   }

   public static String[] splitByCharacterType(String str) {
      return splitByCharacterType(str, false);
   }

   public static String[] splitByCharacterTypeCamelCase(String str) {
      return splitByCharacterType(str, true);
   }

   private static String[] splitByCharacterType(String str, boolean camelCase) {
      if (str == null) {
         return null;
      } else if (str.length() == 0) {
         return ArrayUtils.EMPTY_STRING_ARRAY;
      } else {
         char[] c = str.toCharArray();
         List<String> list = new ArrayList();
         int tokenStart = 0;
         int currentType = Character.getType(c[tokenStart]);

         for(int pos = tokenStart + 1; pos < c.length; ++pos) {
            int type = Character.getType(c[pos]);
            if (type != currentType) {
               if (camelCase && type == 2 && currentType == 1) {
                  int newTokenStart = pos - 1;
                  if (newTokenStart != tokenStart) {
                     list.add(new String(c, tokenStart, newTokenStart - tokenStart));
                     tokenStart = newTokenStart;
                  }
               } else {
                  list.add(new String(c, tokenStart, pos - tokenStart));
                  tokenStart = pos;
               }

               currentType = type;
            }
         }

         list.add(new String(c, tokenStart, c.length - tokenStart));
         return (String[])list.toArray(new String[list.size()]);
      }
   }

   public static <T> String join(T... elements) {
      return join((Object[])elements, (String)null);
   }

   public static String join(Object[] array, char separator) {
      return array == null ? null : join(array, separator, 0, array.length);
   }

   public static String join(Object[] array, char separator, int startIndex, int endIndex) {
      if (array == null) {
         return null;
      } else {
         int noOfItems = endIndex - startIndex;
         if (noOfItems <= 0) {
            return "";
         } else {
            StringBuilder buf = new StringBuilder(noOfItems * 16);

            for(int i = startIndex; i < endIndex; ++i) {
               if (i > startIndex) {
                  buf.append(separator);
               }

               if (array[i] != null) {
                  buf.append(array[i]);
               }
            }

            return buf.toString();
         }
      }
   }

   public static String join(Object[] array, String separator) {
      return array == null ? null : join(array, separator, 0, array.length);
   }

   public static String join(Object[] array, String separator, int startIndex, int endIndex) {
      if (array == null) {
         return null;
      } else {
         if (separator == null) {
            separator = "";
         }

         int noOfItems = endIndex - startIndex;
         if (noOfItems <= 0) {
            return "";
         } else {
            StringBuilder buf = new StringBuilder(noOfItems * 16);

            for(int i = startIndex; i < endIndex; ++i) {
               if (i > startIndex) {
                  buf.append(separator);
               }

               if (array[i] != null) {
                  buf.append(array[i]);
               }
            }

            return buf.toString();
         }
      }
   }

   public static String join(Iterator<?> iterator, char separator) {
      if (iterator == null) {
         return null;
      } else if (!iterator.hasNext()) {
         return "";
      } else {
         Object first = iterator.next();
         if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
         } else {
            StringBuilder buf = new StringBuilder(256);
            if (first != null) {
               buf.append(first);
            }

            while(iterator.hasNext()) {
               buf.append(separator);
               Object obj = iterator.next();
               if (obj != null) {
                  buf.append(obj);
               }
            }

            return buf.toString();
         }
      }
   }

   public static String join(Iterator<?> iterator, String separator) {
      if (iterator == null) {
         return null;
      } else if (!iterator.hasNext()) {
         return "";
      } else {
         Object first = iterator.next();
         if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
         } else {
            StringBuilder buf = new StringBuilder(256);
            if (first != null) {
               buf.append(first);
            }

            while(iterator.hasNext()) {
               if (separator != null) {
                  buf.append(separator);
               }

               Object obj = iterator.next();
               if (obj != null) {
                  buf.append(obj);
               }
            }

            return buf.toString();
         }
      }
   }

   public static String join(Iterable<?> iterable, char separator) {
      return iterable == null ? null : join(iterable.iterator(), separator);
   }

   public static String join(Iterable<?> iterable, String separator) {
      return iterable == null ? null : join(iterable.iterator(), separator);
   }

   public static String deleteWhitespace(String str) {
      if (isEmpty(str)) {
         return str;
      } else {
         int sz = str.length();
         char[] chs = new char[sz];
         int count = 0;

         for(int i = 0; i < sz; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
               chs[count++] = str.charAt(i);
            }
         }

         if (count == sz) {
            return str;
         } else {
            return new String(chs, 0, count);
         }
      }
   }

   public static String removeStart(String str, String remove) {
      if (!isEmpty(str) && !isEmpty(remove)) {
         return str.startsWith(remove) ? str.substring(remove.length()) : str;
      } else {
         return str;
      }
   }

   public static String removeStartIgnoreCase(String str, String remove) {
      if (!isEmpty(str) && !isEmpty(remove)) {
         return startsWithIgnoreCase(str, remove) ? str.substring(remove.length()) : str;
      } else {
         return str;
      }
   }

   public static String removeEnd(String str, String remove) {
      if (!isEmpty(str) && !isEmpty(remove)) {
         return str.endsWith(remove) ? str.substring(0, str.length() - remove.length()) : str;
      } else {
         return str;
      }
   }

   public static String removeEndIgnoreCase(String str, String remove) {
      if (!isEmpty(str) && !isEmpty(remove)) {
         return endsWithIgnoreCase(str, remove) ? str.substring(0, str.length() - remove.length()) : str;
      } else {
         return str;
      }
   }

   public static String remove(String str, String remove) {
      return !isEmpty(str) && !isEmpty(remove) ? replace(str, remove, "", -1) : str;
   }

   public static String remove(String str, char remove) {
      if (!isEmpty(str) && str.indexOf(remove) != -1) {
         char[] chars = str.toCharArray();
         int pos = 0;

         for(int i = 0; i < chars.length; ++i) {
            if (chars[i] != remove) {
               chars[pos++] = chars[i];
            }
         }

         return new String(chars, 0, pos);
      } else {
         return str;
      }
   }

   public static String replaceOnce(String text, String searchString, String replacement) {
      return replace(text, searchString, replacement, 1);
   }

   public static String replace(String text, String searchString, String replacement) {
      return replace(text, searchString, replacement, -1);
   }

   public static String replace(String text, String searchString, String replacement, int max) {
      if (!isEmpty(text) && !isEmpty(searchString) && replacement != null && max != 0) {
         int start = 0;
         int end = text.indexOf(searchString, start);
         if (end == -1) {
            return text;
         } else {
            int replLength = searchString.length();
            int increase = replacement.length() - replLength;
            increase = increase < 0 ? 0 : increase;
            increase *= max < 0 ? 16 : (max > 64 ? 64 : max);

            StringBuilder buf;
            for(buf = new StringBuilder(text.length() + increase); end != -1; end = text.indexOf(searchString, start)) {
               buf.append(text.substring(start, end)).append(replacement);
               start = end + replLength;
               --max;
               if (max == 0) {
                  break;
               }
            }

            buf.append(text.substring(start));
            return buf.toString();
         }
      } else {
         return text;
      }
   }

   public static String replaceEach(String text, String[] searchList, String[] replacementList) {
      return replaceEach(text, searchList, replacementList, false, 0);
   }

   public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
      int timeToLive = searchList == null ? 0 : searchList.length;
      return replaceEach(text, searchList, replacementList, true, timeToLive);
   }

   private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat, int timeToLive) {
      if (text != null && text.length() != 0 && searchList != null && searchList.length != 0 && replacementList != null && replacementList.length != 0) {
         if (timeToLive < 0) {
            throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
         } else {
            int searchLength = searchList.length;
            int replacementLength = replacementList.length;
            if (searchLength != replacementLength) {
               throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
            } else {
               boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
               int textIndex = -1;
               int replaceIndex = -1;
               int tempIndex = true;

               int start;
               int tempIndex;
               for(start = 0; start < searchLength; ++start) {
                  if (!noMoreMatchesForReplIndex[start] && searchList[start] != null && searchList[start].length() != 0 && replacementList[start] != null) {
                     tempIndex = text.indexOf(searchList[start]);
                     if (tempIndex == -1) {
                        noMoreMatchesForReplIndex[start] = true;
                     } else if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = start;
                     }
                  }
               }

               if (textIndex == -1) {
                  return text;
               } else {
                  start = 0;
                  int increase = 0;

                  int i;
                  for(int i = 0; i < searchList.length; ++i) {
                     if (searchList[i] != null && replacementList[i] != null) {
                        i = replacementList[i].length() - searchList[i].length();
                        if (i > 0) {
                           increase += 3 * i;
                        }
                     }
                  }

                  increase = Math.min(increase, text.length() / 5);
                  StringBuilder buf = new StringBuilder(text.length() + increase);

                  while(textIndex != -1) {
                     for(i = start; i < textIndex; ++i) {
                        buf.append(text.charAt(i));
                     }

                     buf.append(replacementList[replaceIndex]);
                     start = textIndex + searchList[replaceIndex].length();
                     textIndex = -1;
                     replaceIndex = -1;
                     tempIndex = true;

                     for(i = 0; i < searchLength; ++i) {
                        if (!noMoreMatchesForReplIndex[i] && searchList[i] != null && searchList[i].length() != 0 && replacementList[i] != null) {
                           tempIndex = text.indexOf(searchList[i], start);
                           if (tempIndex == -1) {
                              noMoreMatchesForReplIndex[i] = true;
                           } else if (textIndex == -1 || tempIndex < textIndex) {
                              textIndex = tempIndex;
                              replaceIndex = i;
                           }
                        }
                     }
                  }

                  i = text.length();

                  for(int i = start; i < i; ++i) {
                     buf.append(text.charAt(i));
                  }

                  String result = buf.toString();
                  if (!repeat) {
                     return result;
                  } else {
                     return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
                  }
               }
            }
         }
      } else {
         return text;
      }
   }

   public static String replaceChars(String str, char searchChar, char replaceChar) {
      return str == null ? null : str.replace(searchChar, replaceChar);
   }

   public static String replaceChars(String str, String searchChars, String replaceChars) {
      if (!isEmpty(str) && !isEmpty(searchChars)) {
         if (replaceChars == null) {
            replaceChars = "";
         }

         boolean modified = false;
         int replaceCharsLength = replaceChars.length();
         int strLength = str.length();
         StringBuilder buf = new StringBuilder(strLength);

         for(int i = 0; i < strLength; ++i) {
            char ch = str.charAt(i);
            int index = searchChars.indexOf(ch);
            if (index >= 0) {
               modified = true;
               if (index < replaceCharsLength) {
                  buf.append(replaceChars.charAt(index));
               }
            } else {
               buf.append(ch);
            }
         }

         if (modified) {
            return buf.toString();
         } else {
            return str;
         }
      } else {
         return str;
      }
   }

   public static String overlay(String str, String overlay, int start, int end) {
      if (str == null) {
         return null;
      } else {
         if (overlay == null) {
            overlay = "";
         }

         int len = str.length();
         if (start < 0) {
            start = 0;
         }

         if (start > len) {
            start = len;
         }

         if (end < 0) {
            end = 0;
         }

         if (end > len) {
            end = len;
         }

         if (start > end) {
            int temp = start;
            start = end;
            end = temp;
         }

         return (new StringBuilder(len + start - end + overlay.length() + 1)).append(str.substring(0, start)).append(overlay).append(str.substring(end)).toString();
      }
   }

   public static String chomp(String str) {
      if (isEmpty(str)) {
         return str;
      } else if (str.length() == 1) {
         char ch = str.charAt(0);
         return ch != '\r' && ch != '\n' ? str : "";
      } else {
         int lastIdx = str.length() - 1;
         char last = str.charAt(lastIdx);
         if (last == '\n') {
            if (str.charAt(lastIdx - 1) == '\r') {
               --lastIdx;
            }
         } else if (last != '\r') {
            ++lastIdx;
         }

         return str.substring(0, lastIdx);
      }
   }

   /** @deprecated */
   @Deprecated
   public static String chomp(String str, String separator) {
      return removeEnd(str, separator);
   }

   public static String chop(String str) {
      if (str == null) {
         return null;
      } else {
         int strLen = str.length();
         if (strLen < 2) {
            return "";
         } else {
            int lastIdx = strLen - 1;
            String ret = str.substring(0, lastIdx);
            char last = str.charAt(lastIdx);
            return last == '\n' && ret.charAt(lastIdx - 1) == '\r' ? ret.substring(0, lastIdx - 1) : ret;
         }
      }
   }

   public static String repeat(String str, int repeat) {
      if (str == null) {
         return null;
      } else if (repeat <= 0) {
         return "";
      } else {
         int inputLength = str.length();
         if (repeat != 1 && inputLength != 0) {
            if (inputLength == 1 && repeat <= 8192) {
               return repeat(str.charAt(0), repeat);
            } else {
               int outputLength = inputLength * repeat;
               switch (inputLength) {
                  case 1:
                     return repeat(str.charAt(0), repeat);
                  case 2:
                     char ch0 = str.charAt(0);
                     char ch1 = str.charAt(1);
                     char[] output2 = new char[outputLength];

                     for(int i = repeat * 2 - 2; i >= 0; --i) {
                        output2[i] = ch0;
                        output2[i + 1] = ch1;
                        --i;
                     }

                     return new String(output2);
                  default:
                     StringBuilder buf = new StringBuilder(outputLength);

                     for(int i = 0; i < repeat; ++i) {
                        buf.append(str);
                     }

                     return buf.toString();
               }
            }
         } else {
            return str;
         }
      }
   }

   public static String repeat(String str, String separator, int repeat) {
      if (str != null && separator != null) {
         String result = repeat(str + separator, repeat);
         return removeEnd(result, separator);
      } else {
         return repeat(str, repeat);
      }
   }

   public static String repeat(char ch, int repeat) {
      char[] buf = new char[repeat];

      for(int i = repeat - 1; i >= 0; --i) {
         buf[i] = ch;
      }

      return new String(buf);
   }

   public static String rightPad(String str, int size) {
      return rightPad(str, size, ' ');
   }

   public static String rightPad(String str, int size, char padChar) {
      if (str == null) {
         return null;
      } else {
         int pads = size - str.length();
         if (pads <= 0) {
            return str;
         } else {
            return pads > 8192 ? rightPad(str, size, String.valueOf(padChar)) : str.concat(repeat(padChar, pads));
         }
      }
   }

   public static String rightPad(String str, int size, String padStr) {
      if (str == null) {
         return null;
      } else {
         if (isEmpty(padStr)) {
            padStr = " ";
         }

         int padLen = padStr.length();
         int strLen = str.length();
         int pads = size - strLen;
         if (pads <= 0) {
            return str;
         } else if (padLen == 1 && pads <= 8192) {
            return rightPad(str, size, padStr.charAt(0));
         } else if (pads == padLen) {
            return str.concat(padStr);
         } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
         } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();

            for(int i = 0; i < pads; ++i) {
               padding[i] = padChars[i % padLen];
            }

            return str.concat(new String(padding));
         }
      }
   }

   public static String leftPad(String str, int size) {
      return leftPad(str, size, ' ');
   }

   public static String leftPad(String str, int size, char padChar) {
      if (str == null) {
         return null;
      } else {
         int pads = size - str.length();
         if (pads <= 0) {
            return str;
         } else {
            return pads > 8192 ? leftPad(str, size, String.valueOf(padChar)) : repeat(padChar, pads).concat(str);
         }
      }
   }

   public static String leftPad(String str, int size, String padStr) {
      if (str == null) {
         return null;
      } else {
         if (isEmpty(padStr)) {
            padStr = " ";
         }

         int padLen = padStr.length();
         int strLen = str.length();
         int pads = size - strLen;
         if (pads <= 0) {
            return str;
         } else if (padLen == 1 && pads <= 8192) {
            return leftPad(str, size, padStr.charAt(0));
         } else if (pads == padLen) {
            return padStr.concat(str);
         } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
         } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();

            for(int i = 0; i < pads; ++i) {
               padding[i] = padChars[i % padLen];
            }

            return (new String(padding)).concat(str);
         }
      }
   }

   public static int length(CharSequence cs) {
      return cs == null ? 0 : cs.length();
   }

   public static String center(String str, int size) {
      return center(str, size, ' ');
   }

   public static String center(String str, int size, char padChar) {
      if (str != null && size > 0) {
         int strLen = str.length();
         int pads = size - strLen;
         if (pads <= 0) {
            return str;
         } else {
            str = leftPad(str, strLen + pads / 2, padChar);
            str = rightPad(str, size, padChar);
            return str;
         }
      } else {
         return str;
      }
   }

   public static String center(String str, int size, String padStr) {
      if (str != null && size > 0) {
         if (isEmpty(padStr)) {
            padStr = " ";
         }

         int strLen = str.length();
         int pads = size - strLen;
         if (pads <= 0) {
            return str;
         } else {
            str = leftPad(str, strLen + pads / 2, padStr);
            str = rightPad(str, size, padStr);
            return str;
         }
      } else {
         return str;
      }
   }

   public static String upperCase(String str) {
      return str == null ? null : str.toUpperCase();
   }

   public static String upperCase(String str, Locale locale) {
      return str == null ? null : str.toUpperCase(locale);
   }

   public static String lowerCase(String str) {
      return str == null ? null : str.toLowerCase();
   }

   public static String lowerCase(String str, Locale locale) {
      return str == null ? null : str.toLowerCase(locale);
   }

   public static String capitalize(String str) {
      int strLen;
      return str != null && (strLen = str.length()) != 0 ? (new StringBuilder(strLen)).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString() : str;
   }

   public static String uncapitalize(String str) {
      int strLen;
      return str != null && (strLen = str.length()) != 0 ? (new StringBuilder(strLen)).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString() : str;
   }

   public static String swapCase(String str) {
      if (isEmpty(str)) {
         return str;
      } else {
         char[] buffer = str.toCharArray();

         for(int i = 0; i < buffer.length; ++i) {
            char ch = buffer[i];
            if (Character.isUpperCase(ch)) {
               buffer[i] = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
               buffer[i] = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
               buffer[i] = Character.toUpperCase(ch);
            }
         }

         return new String(buffer);
      }
   }

   public static int countMatches(CharSequence str, CharSequence sub) {
      if (!isEmpty(str) && !isEmpty(sub)) {
         int count = 0;

         for(int idx = 0; (idx = CharSequenceUtils.indexOf(str, sub, idx)) != -1; idx += sub.length()) {
            ++count;
         }

         return count;
      } else {
         return 0;
      }
   }

   public static boolean isAlpha(CharSequence cs) {
      if (cs != null && cs.length() != 0) {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isLetter(cs.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isAlphaSpace(CharSequence cs) {
      if (cs == null) {
         return false;
      } else {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isLetter(cs.charAt(i)) && cs.charAt(i) != ' ') {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAlphanumeric(CharSequence cs) {
      if (cs != null && cs.length() != 0) {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isLetterOrDigit(cs.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isAlphanumericSpace(CharSequence cs) {
      if (cs == null) {
         return false;
      } else {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAsciiPrintable(CharSequence cs) {
      if (cs == null) {
         return false;
      } else {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!CharUtils.isAsciiPrintable(cs.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isNumeric(CharSequence cs) {
      if (cs != null && cs.length() != 0) {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isDigit(cs.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isNumericSpace(CharSequence cs) {
      if (cs == null) {
         return false;
      } else {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isWhitespace(CharSequence cs) {
      if (cs == null) {
         return false;
      } else {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isWhitespace(cs.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAllLowerCase(CharSequence cs) {
      if (cs != null && !isEmpty(cs)) {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isLowerCase(cs.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isAllUpperCase(CharSequence cs) {
      if (cs != null && !isEmpty(cs)) {
         int sz = cs.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isUpperCase(cs.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static String defaultString(String str) {
      return str == null ? "" : str;
   }

   public static String defaultString(String str, String defaultStr) {
      return str == null ? defaultStr : str;
   }

   public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
      return isBlank(str) ? defaultStr : str;
   }

   public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
      return isEmpty(str) ? defaultStr : str;
   }

   public static String reverse(String str) {
      return str == null ? null : (new StringBuilder(str)).reverse().toString();
   }

   public static String reverseDelimited(String str, char separatorChar) {
      if (str == null) {
         return null;
      } else {
         String[] strs = split(str, separatorChar);
         ArrayUtils.reverse((Object[])strs);
         return join((Object[])strs, separatorChar);
      }
   }

   public static String abbreviate(String str, int maxWidth) {
      return abbreviate(str, 0, maxWidth);
   }

   public static String abbreviate(String str, int offset, int maxWidth) {
      if (str == null) {
         return null;
      } else if (maxWidth < 4) {
         throw new IllegalArgumentException("Minimum abbreviation width is 4");
      } else if (str.length() <= maxWidth) {
         return str;
      } else {
         if (offset > str.length()) {
            offset = str.length();
         }

         if (str.length() - offset < maxWidth - 3) {
            offset = str.length() - (maxWidth - 3);
         }

         String abrevMarker = "...";
         if (offset <= 4) {
            return str.substring(0, maxWidth - 3) + "...";
         } else if (maxWidth < 7) {
            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
         } else {
            return offset + maxWidth - 3 < str.length() ? "..." + abbreviate(str.substring(offset), maxWidth - 3) : "..." + str.substring(str.length() - (maxWidth - 3));
         }
      }
   }

   public static String abbreviateMiddle(String str, String middle, int length) {
      if (!isEmpty(str) && !isEmpty(middle)) {
         if (length < str.length() && length >= middle.length() + 2) {
            int targetSting = length - middle.length();
            int startOffset = targetSting / 2 + targetSting % 2;
            int endOffset = str.length() - targetSting / 2;
            StringBuilder builder = new StringBuilder(length);
            builder.append(str.substring(0, startOffset));
            builder.append(middle);
            builder.append(str.substring(endOffset));
            return builder.toString();
         } else {
            return str;
         }
      } else {
         return str;
      }
   }

   public static String difference(String str1, String str2) {
      if (str1 == null) {
         return str2;
      } else if (str2 == null) {
         return str1;
      } else {
         int at = indexOfDifference(str1, str2);
         return at == -1 ? "" : str2.substring(at);
      }
   }

   public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
      if (cs1 == cs2) {
         return -1;
      } else if (cs1 != null && cs2 != null) {
         int i;
         for(i = 0; i < cs1.length() && i < cs2.length() && cs1.charAt(i) == cs2.charAt(i); ++i) {
         }

         return i >= cs2.length() && i >= cs1.length() ? -1 : i;
      } else {
         return 0;
      }
   }

   public static int indexOfDifference(CharSequence... css) {
      if (css != null && css.length > 1) {
         boolean anyStringNull = false;
         boolean allStringsNull = true;
         int arrayLen = css.length;
         int shortestStrLen = Integer.MAX_VALUE;
         int longestStrLen = 0;

         int firstDiff;
         for(firstDiff = 0; firstDiff < arrayLen; ++firstDiff) {
            if (css[firstDiff] == null) {
               anyStringNull = true;
               shortestStrLen = 0;
            } else {
               allStringsNull = false;
               shortestStrLen = Math.min(css[firstDiff].length(), shortestStrLen);
               longestStrLen = Math.max(css[firstDiff].length(), longestStrLen);
            }
         }

         if (allStringsNull || longestStrLen == 0 && !anyStringNull) {
            return -1;
         } else if (shortestStrLen == 0) {
            return 0;
         } else {
            firstDiff = -1;

            for(int stringPos = 0; stringPos < shortestStrLen; ++stringPos) {
               char comparisonChar = css[0].charAt(stringPos);

               for(int arrayPos = 1; arrayPos < arrayLen; ++arrayPos) {
                  if (css[arrayPos].charAt(stringPos) != comparisonChar) {
                     firstDiff = stringPos;
                     break;
                  }
               }

               if (firstDiff != -1) {
                  break;
               }
            }

            return firstDiff == -1 && shortestStrLen != longestStrLen ? shortestStrLen : firstDiff;
         }
      } else {
         return -1;
      }
   }

   public static String getCommonPrefix(String... strs) {
      if (strs != null && strs.length != 0) {
         int smallestIndexOfDiff = indexOfDifference(strs);
         if (smallestIndexOfDiff == -1) {
            return strs[0] == null ? "" : strs[0];
         } else {
            return smallestIndexOfDiff == 0 ? "" : strs[0].substring(0, smallestIndexOfDiff);
         }
      } else {
         return "";
      }
   }

   public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
      if (s != null && t != null) {
         int n = s.length();
         int m = t.length();
         if (n == 0) {
            return m;
         } else if (m == 0) {
            return n;
         } else {
            if (n > m) {
               CharSequence tmp = s;
               s = t;
               t = tmp;
               n = m;
               m = t.length();
            }

            int[] p = new int[n + 1];
            int[] d = new int[n + 1];

            int i;
            for(i = 0; i <= n; p[i] = i++) {
            }

            for(int j = 1; j <= m; ++j) {
               char t_j = t.charAt(j - 1);
               d[0] = j;

               for(i = 1; i <= n; ++i) {
                  int cost = s.charAt(i - 1) == t_j ? 0 : 1;
                  d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
               }

               int[] _d = p;
               p = d;
               d = _d;
            }

            return p[n];
         }
      } else {
         throw new IllegalArgumentException("Strings must not be null");
      }
   }

   public static int getLevenshteinDistance(CharSequence s, CharSequence t, int threshold) {
      if (s != null && t != null) {
         if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
         } else {
            int n = s.length();
            int m = t.length();
            if (n == 0) {
               return m <= threshold ? m : -1;
            } else if (m == 0) {
               return n <= threshold ? n : -1;
            } else {
               if (n > m) {
                  CharSequence tmp = s;
                  s = t;
                  t = tmp;
                  n = m;
                  m = t.length();
               }

               int[] p = new int[n + 1];
               int[] d = new int[n + 1];
               int boundary = Math.min(n, threshold) + 1;

               int j;
               for(j = 0; j < boundary; p[j] = j++) {
               }

               Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
               Arrays.fill(d, Integer.MAX_VALUE);

               for(j = 1; j <= m; ++j) {
                  char t_j = t.charAt(j - 1);
                  d[0] = j;
                  int min = Math.max(1, j - threshold);
                  int max = Math.min(n, j + threshold);
                  if (min > max) {
                     return -1;
                  }

                  if (min > 1) {
                     d[min - 1] = Integer.MAX_VALUE;
                  }

                  for(int i = min; i <= max; ++i) {
                     if (s.charAt(i - 1) == t_j) {
                        d[i] = p[i - 1];
                     } else {
                        d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
                     }
                  }

                  int[] _d = p;
                  p = d;
                  d = _d;
               }

               if (p[n] <= threshold) {
                  return p[n];
               } else {
                  return -1;
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Strings must not be null");
      }
   }

   public static boolean startsWith(CharSequence str, CharSequence prefix) {
      return startsWith(str, prefix, false);
   }

   public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
      return startsWith(str, prefix, true);
   }

   private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
      if (str != null && prefix != null) {
         return prefix.length() > str.length() ? false : CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
      } else {
         return str == null && prefix == null;
      }
   }

   public static boolean startsWithAny(CharSequence string, CharSequence... searchStrings) {
      if (!isEmpty(string) && !ArrayUtils.isEmpty((Object[])searchStrings)) {
         CharSequence[] var2 = searchStrings;
         int var3 = searchStrings.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            CharSequence searchString = var2[var4];
            if (startsWith(string, searchString)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean endsWith(CharSequence str, CharSequence suffix) {
      return endsWith(str, suffix, false);
   }

   public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
      return endsWith(str, suffix, true);
   }

   private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
      if (str != null && suffix != null) {
         if (suffix.length() > str.length()) {
            return false;
         } else {
            int strOffset = str.length() - suffix.length();
            return CharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
         }
      } else {
         return str == null && suffix == null;
      }
   }

   public static String normalizeSpace(String str) {
      return str == null ? null : WHITESPACE_BLOCK.matcher(trim(str)).replaceAll(" ");
   }

   public static boolean endsWithAny(CharSequence string, CharSequence... searchStrings) {
      if (!isEmpty(string) && !ArrayUtils.isEmpty((Object[])searchStrings)) {
         CharSequence[] var2 = searchStrings;
         int var3 = searchStrings.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            CharSequence searchString = var2[var4];
            if (endsWith(string, searchString)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
      return charsetName == null ? new String(bytes) : new String(bytes, charsetName);
   }

   private static class InitStripAccents {
      private static final Throwable sunException;
      private static final Method sunDecomposeMethod;
      private static final Pattern sunPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
      private static final Throwable java6Exception;
      private static final Method java6NormalizeMethod;
      private static final Object java6NormalizerFormNFD;
      private static final Pattern java6Pattern;

      static {
         java6Pattern = sunPattern;
         Object _java6NormalizerFormNFD = null;
         Method _java6NormalizeMethod = null;
         Method _sunDecomposeMethod = null;
         Throwable _java6Exception = null;
         Throwable _sunException = null;

         Class normalizerClass;
         try {
            Class<?> normalizerFormClass = Thread.currentThread().getContextClassLoader().loadClass("java.text.Normalizer$Form");
            _java6NormalizerFormNFD = normalizerFormClass.getField("NFD").get((Object)null);
            normalizerClass = Thread.currentThread().getContextClassLoader().loadClass("java.text.Normalizer");
            _java6NormalizeMethod = normalizerClass.getMethod("normalize", CharSequence.class, normalizerFormClass);
         } catch (Exception var8) {
            Exception e1 = var8;
            _java6Exception = e1;

            try {
               normalizerClass = Thread.currentThread().getContextClassLoader().loadClass("sun.text.Normalizer");
               _sunDecomposeMethod = normalizerClass.getMethod("decompose", String.class, Boolean.TYPE, Integer.TYPE);
            } catch (Exception var7) {
               Exception e2 = var7;
               _sunException = e2;
            }
         }

         java6Exception = _java6Exception;
         java6NormalizerFormNFD = _java6NormalizerFormNFD;
         java6NormalizeMethod = _java6NormalizeMethod;
         sunException = _sunException;
         sunDecomposeMethod = _sunDecomposeMethod;
      }
   }
}
