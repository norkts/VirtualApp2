package com.kook.deviceinfo.persistence;

import android.text.TextUtils;
import com.kook.common.utils.HVLog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class IniFile {
   public static String SYSTEM_EXPORT_CONFIG = VEnvironment.get().getPersisteceDataPath() + "export_config.ini";
   private File file = null;
   private String line_separator = "\n";
   private String charSet = "UTF-8";
   private Map<String, Section> sections = new LinkedHashMap();

   public static IniFile getInstance(String file) {
      File storage = new File(file);

      try {
         storage.createNewFile();
      } catch (IOException var3) {
      }

      IniFile mIniFile = new IniFile(storage);
      return mIniFile;
   }

   public File getFile() {
      return this.file;
   }

   public void setLineSeparator(String line_separator) {
      this.line_separator = line_separator;
   }

   public void setCharSet(String charSet) {
      this.charSet = charSet;
   }

   public void set(String section, String key, String value) {
      Section sectionObject = (Section)this.sections.get(section);
      if (sectionObject == null) {
         sectionObject = new Section();
      }

      sectionObject.name = section;
      sectionObject.set(key, value);
      this.sections.put(section, sectionObject);
   }

   public void set(String section, String key, Object objectValue) {
      if (!(objectValue instanceof Integer) && !(objectValue instanceof Long) && !(objectValue instanceof Float)) {
         throw new ClassCastException(objectValue + "不能转砖成String");
      } else {
         String value = String.valueOf(objectValue);
         Section sectionObject = (Section)this.sections.get(section);
         if (sectionObject == null) {
            sectionObject = new Section();
         }

         sectionObject.name = section;
         sectionObject.set(key, value);
         this.sections.put(section, sectionObject);
      }
   }

   public void setList(String section, String key, List<String> listValue) {
      Section sectionObject = (Section)this.sections.get(section);
      if (sectionObject == null) {
         sectionObject = new Section();
      }

      sectionObject.name = section;
      sectionObject.set(key, String.valueOf(listValue.size()));
      int index = 0;

      for(Iterator var6 = listValue.iterator(); var6.hasNext(); ++index) {
         String value = (String)var6.next();
         sectionObject.set(key + index, value);
      }

      this.sections.put(section, sectionObject);
   }

   public Section get(String section) {
      return (Section)this.sections.get(section);
   }

   public String get(String section, String key) {
      return this.get(section, key, (String)null);
   }

   public List<String> getList(String section, String key) {
      Section sectionObject = (Section)this.sections.get(section);
      List<String> listData = null;
      if (sectionObject != null) {
         listData = new ArrayList();
         int listcount = (Integer)sectionObject.get(key);

         for(int i = 0; i < listcount; ++i) {
            String valueItem = (String)sectionObject.get(key + i);
            listData.add(valueItem);
         }
      }

      return listData;
   }

   public String get(String section, String key, String defaultValue) {
      Section sectionObject = (Section)this.sections.get(section);
      if (sectionObject != null) {
         String value = (String)sectionObject.get(key);
         return value != null && !value.toString().trim().equals("") ? value : defaultValue;
      } else {
         return null;
      }
   }

   public void remove(String section) {
      this.sections.remove(section);
   }

   public void remove(String section, String key) {
      Section sectionObject = (Section)this.sections.get(section);
      if (sectionObject != null) {
         sectionObject.getValues().remove(key);
      }

   }

   public IniFile(File file) {
      this.file = file;
      this.initFromFile(file);
   }

   public IniFile(InputStream inputStream) {
      this.initFromInputStream(inputStream);
   }

   public void load(File file) {
      this.file = file;
      this.initFromFile(file);
   }

   public void load(InputStream inputStream) {
      this.initFromInputStream(inputStream);
   }

   public void save(OutputStream outputStream) {
      try {
         BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, this.charSet));
         this.saveConfig(bufferedWriter);
      } catch (UnsupportedEncodingException var4) {
         UnsupportedEncodingException e = var4;
         HVLog.printException(e);
      }

   }

   private void save(File file) {
      try {
         BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
         this.saveConfig(bufferedWriter);
      } catch (IOException var3) {
         IOException e = var3;
         HVLog.e("文件" + file.getAbsolutePath() + "读写异常");
         HVLog.printException(e);
      }

   }

   public File save() {
      this.save(this.file);
      return this.file;
   }

   private void initFromInputStream(InputStream inputStream) {
      try {
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, this.charSet));
         this.toIniFile(bufferedReader);
      } catch (UnsupportedEncodingException var4) {
         UnsupportedEncodingException e = var4;
         HVLog.printException(e);
      }

   }

   private void initFromFile(File file) {
      try {
         HVLog.e("initFromFile file:" + file.getAbsolutePath() + "    file:" + file.exists());
         if (!file.exists()) {
            File storage = new File(file.getAbsolutePath());
            if (!storage.exists()) {
               boolean fileNewFile = file.createNewFile();
               HVLog.e("IniConfig storage : " + fileNewFile);
            }
         }

         BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
         this.toIniFile(bufferedReader);
      } catch (FileNotFoundException var5) {
         FileNotFoundException e = var5;
         HVLog.printException(e);
      } catch (IOException var6) {
         IOException e = var6;
         HVLog.printException(e);
      }

   }

   private void toIniFile(BufferedReader bufferedReader) {
      Section section = null;
      Pattern p = Pattern.compile("^\\[.*\\]$");

      try {
         String strLine;
         while((strLine = bufferedReader.readLine()) != null) {
            if (p.matcher(strLine).matches()) {
               strLine = strLine.trim();
               section = new Section();
               section.name = strLine.substring(1, strLine.length() - 1);
               this.sections.put(section.name, section);
            } else {
               String[] keyValue = strLine.split("=");
               if (keyValue.length == 2) {
                  section.set(keyValue[0], keyValue[1]);
               }
            }
         }

         bufferedReader.close();
      } catch (IOException var6) {
         IOException e = var6;
         HVLog.printException(e);
      }

   }

   private void saveConfig(BufferedWriter bufferedWriter) {
      try {
         boolean line_spe = false;
         if (this.line_separator == null || this.line_separator.trim().equals("")) {
            line_spe = true;
         }

         Iterator var3 = this.sections.values().iterator();

         while(var3.hasNext()) {
            Section section = (Section)var3.next();
            bufferedWriter.write("[" + section.getName() + "]");
            if (line_spe) {
               bufferedWriter.write(this.line_separator);
            } else {
               bufferedWriter.newLine();
            }

            Iterator var5 = section.getValues().entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry<String, String> entry = (Map.Entry)var5.next();
               String key = (String)entry.getKey();
               if (!TextUtils.isEmpty(key)) {
                  bufferedWriter.write((String)entry.getKey());
                  bufferedWriter.write("=");
                  Object value = entry.getValue();
                  if (value != null) {
                     bufferedWriter.write(value.toString());
                  }

                  if (line_spe) {
                     bufferedWriter.write(this.line_separator);
                  } else {
                     bufferedWriter.newLine();
                  }
               }
            }
         }

         bufferedWriter.flush();
         bufferedWriter.close();
      } catch (Exception var9) {
         Exception e = var9;
         HVLog.printException(e);
      }

   }

   public class Section {
      private String name;
      private Map<String, String> values = new LinkedHashMap();

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public void set(String key, String value) {
         this.values.put(key, value);
      }

      public Object get(String key) {
         return this.values.get(key);
      }

      public Map<String, String> getValues() {
         return this.values;
      }
   }
}
