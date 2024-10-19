package com.carlos.common.utils;

import android.os.Environment;
import com.carlos.libcommon.StringFog;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class IniFile {
   static String STORAGE;
   private String line_separator = "\n";
   private String charSet = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQUqW3pTRVo="));
   private Map<String, Section> sections = new LinkedHashMap();
   private File file = null;
   private static IniFile mIniFile;

   public void setLineSeparator(String line_separator) {
      this.line_separator = line_separator;
   }

   public void setCharSet(String charSet) {
      this.charSet = charSet;
   }

   public void set(String section, String key, Object value) {
      Section sectionObject = (Section)this.sections.get(section);
      if (sectionObject == null) {
         sectionObject = new Section();
      }

      sectionObject.name = section;
      sectionObject.set(key, value);
      this.sections.put(section, sectionObject);
   }

   public Section get(String section) {
      return (Section)this.sections.get(section);
   }

   public Object get(String section, String key) {
      return this.get(section, key, (String)null);
   }

   public Object get(String section, String key, String defaultValue) {
      Section sectionObject = (Section)this.sections.get(section);
      if (sectionObject != null) {
         Object value = sectionObject.get(key);
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

   public static IniFile getInstance() {
      return mIniFile;
   }

   private IniFile(File file) {
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
         HVLog.printException(e);
      }

   }

   public void save() {
      this.save(this.file);
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
         if (!file.exists()) {
            File storage = new File(STORAGE);
            boolean fileNewFile;
            if (!storage.exists()) {
               fileNewFile = storage.mkdirs();
               HVLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcCWAjAiRiCiQpLBdfKG4jEit4V1Ar")) + fileNewFile);
            }

            fileNewFile = file.createNewFile();
            HVLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcCWAjAiRiCic8KD0cCGkhMCtvJSQaLAgtPg==")) + fileNewFile);
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
      Pattern p = Pattern.compile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JzsEG3onTR5sCgpF")));

      try {
         String strLine;
         while((strLine = bufferedReader.readLine()) != null) {
            if (p.matcher(strLine).matches()) {
               strLine = strLine.trim();
               section = new Section();
               section.name = strLine.substring(1, strLine.length() - 1);
               this.sections.put(section.name, section);
            } else {
               String[] keyValue = strLine.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwhSVg==")));
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
            bufferedWriter.write(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + section.getName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")));
            if (line_spe) {
               bufferedWriter.write(this.line_separator);
            } else {
               bufferedWriter.newLine();
            }

            Iterator var5 = section.getValues().entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry<String, Object> entry = (Map.Entry)var5.next();
               bufferedWriter.write((String)entry.getKey());
               bufferedWriter.write(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwhSVg==")));
               bufferedWriter.write(entry.getValue().toString());
               if (line_spe) {
                  bufferedWriter.write(this.line_separator);
               } else {
                  bufferedWriter.newLine();
               }
            }
         }

         bufferedWriter.close();
      } catch (Exception var7) {
         Exception e = var7;
         HVLog.printException(e);
      }

   }

   static {
      STORAGE = Environment.getExternalStorageDirectory() + File.separator + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki42D28gIC9gJFg1"));
      mIniFile = new IniFile(new File(STORAGE + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki42D28gIC9gJFg2KQcYMQ=="))));
   }

   public class Section {
      private String name;
      private Map<String, Object> values = new LinkedHashMap();

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public void set(String key, Object value) {
         this.values.put(key, value);
      }

      public Object get(String key) {
         return this.values.get(key);
      }

      public Map<String, Object> getValues() {
         return this.values;
      }
   }
}
