package com.android.dx.command.dexer;

import com.android.dex.Dex;
import com.android.dex.DexException;
import com.android.dex.util.FileUtils;
import com.android.dx.cf.code.SimException;
import com.android.dx.cf.direct.ClassPathOpener;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.command.UsageException;
import com.android.dx.dex.DexOptions;
import com.android.dx.dex.cf.CfOptions;
import com.android.dx.dex.cf.CfTranslator;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.EncodedMethod;
import com.android.dx.merge.CollisionPolicy;
import com.android.dx.merge.DexMerger;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.rop.annotation.Annotations;
import com.android.dx.rop.annotation.AnnotationsList;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.cst.CstNat;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.Type;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

public class Main {
   private static final String DEX_EXTENSION = ".dex";
   private static final String DEX_PREFIX = "classes";
   private static final String IN_RE_CORE_CLASSES = "Ill-advised or mistaken usage of a core class (java.* or javax.*)\nwhen not building a core library.\n\nThis is often due to inadvertently including a core library file\nin your application's project, when using an IDE (such as\nEclipse). If you are sure you're not intentionally defining a\ncore class, then this is the most likely explanation of what's\ngoing on.\n\nHowever, you might actually be trying to define a class in a core\nnamespace, the source of which you may have taken, for example,\nfrom a non-Android virtual machine project. This will most\nassuredly not work. At a minimum, it jeopardizes the\ncompatibility of your app with future versions of the platform.\nIt is also often of questionable legality.\n\nIf you really intend to build a core library -- which is only\nappropriate as part of creating a full virtual machine\ndistribution, as opposed to compiling an application -- then use\nthe \"--core-library\" option to suppress this error message.\n\nIf you go ahead and use \"--core-library\" but are in fact\nbuilding an application, then be forewarned that your application\nwill still fail to build or run, at some point. Please be\nprepared for angry customers who find, for example, that your\napplication ceases to function once they upgrade their operating\nsystem. You will be to blame for this problem.\n\nIf you are legitimately using some code that happens to be in a\ncore package, then the easiest safe alternative you have is to\nrepackage that code. That is, move the classes in question into\nyour own package namespace. This means that they will never be in\nconflict with core system classes. JarJar is a tool that may help\nyou in this endeavor. If you find that you cannot do this, then\nthat is an indication that the path you are on will ultimately\nlead to pain, suffering, grief, and lamentation.\n";
   private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
   private static final Attributes.Name CREATED_BY = new Attributes.Name("Created-By");
   private static final String[] JAVAX_CORE = new String[]{"accessibility", "crypto", "imageio", "management", "naming", "net", "print", "rmi", "security", "sip", "sound", "sql", "swing", "transaction", "xml"};
   private static final int MAX_METHOD_ADDED_DURING_DEX_CREATION = 2;
   private static final int MAX_FIELD_ADDED_DURING_DEX_CREATION = 9;
   private AtomicInteger errors = new AtomicInteger(0);
   private Arguments args;
   private DexFile outputDex;
   private TreeMap<String, byte[]> outputResources;
   private final List<byte[]> libraryDexBuffers = new ArrayList();
   private ExecutorService classTranslatorPool;
   private ExecutorService classDefItemConsumer;
   private List<Future<Boolean>> addToDexFutures = new ArrayList();
   private ExecutorService dexOutPool;
   private List<Future<byte[]>> dexOutputFutures = new ArrayList();
   private Object dexRotationLock = new Object();
   private int maxMethodIdsInProcess = 0;
   private int maxFieldIdsInProcess = 0;
   private volatile boolean anyFilesProcessed;
   private long minimumFileAge = 0L;
   private Set<String> classesInMainDex = null;
   private List<byte[]> dexOutputArrays = new ArrayList();
   private OutputStreamWriter humanOutWriter = null;
   private final DxContext context;

   public Main(DxContext context) {
      this.context = context;
   }

   public static void main(String[] argArray) throws IOException {
      DxContext context = new DxContext();
      Arguments arguments = new Arguments(context);
      arguments.parse(argArray);
      int result = (new Main(context)).runDx(arguments);
      if (result != 0) {
         System.exit(result);
      }

   }

   public static void clearInternTables() {
      Prototype.clearInternTable();
      RegisterSpec.clearInternTable();
      CstType.clearInternTable();
      Type.clearInternTable();
   }

   public static int run(Arguments arguments) throws IOException {
      return (new Main(new DxContext())).runDx(arguments);
   }

   public int runDx(Arguments arguments) throws IOException {
      this.errors.set(0);
      this.libraryDexBuffers.clear();
      this.args = arguments;
      this.args.makeOptionsObjects();
      OutputStream humanOutRaw = null;
      if (this.args.humanOutName != null) {
         humanOutRaw = this.openOutput(this.args.humanOutName);
         this.humanOutWriter = new OutputStreamWriter(humanOutRaw);
      }

      int var3;
      try {
         if (!this.args.multiDex) {
            var3 = this.runMonoDex();
            return var3;
         }

         var3 = this.runMultiDex();
      } finally {
         this.closeOutput(humanOutRaw);
      }

      return var3;
   }

   private int runMonoDex() throws IOException {
      File incrementalOutFile = null;
      if (this.args.incremental) {
         if (this.args.outName == null) {
            this.context.err.println("error: no incremental output name specified");
            return -1;
         }

         incrementalOutFile = new File(this.args.outName);
         if (incrementalOutFile.exists()) {
            this.minimumFileAge = incrementalOutFile.lastModified();
         }
      }

      if (!this.processAllFiles()) {
         return 1;
      } else if (this.args.incremental && !this.anyFilesProcessed) {
         return 0;
      } else {
         byte[] outArray = null;
         if (!this.outputDex.isEmpty() || this.args.humanOutName != null) {
            outArray = this.writeDex(this.outputDex);
            if (outArray == null) {
               return 2;
            }
         }

         if (this.args.incremental) {
            outArray = this.mergeIncremental(outArray, incrementalOutFile);
         }

         outArray = this.mergeLibraryDexBuffers(outArray);
         if (this.args.jarOutput) {
            this.outputDex = null;
            if (outArray != null) {
               this.outputResources.put("classes.dex", outArray);
            }

            if (!this.createJar(this.args.outName)) {
               return 3;
            }
         } else if (outArray != null && this.args.outName != null) {
            OutputStream out = this.openOutput(this.args.outName);
            out.write(outArray);
            this.closeOutput(out);
         }

         return 0;
      }
   }

   private int runMultiDex() throws IOException {
      assert !this.args.incremental;

      if (this.args.mainDexListFile != null) {
         this.classesInMainDex = new HashSet();
         readPathsFromFile(this.args.mainDexListFile, this.classesInMainDex);
      }

      this.dexOutPool = Executors.newFixedThreadPool(this.args.numThreads);
      if (!this.processAllFiles()) {
         return 1;
      } else if (!this.libraryDexBuffers.isEmpty()) {
         throw new DexException("Library dex files are not supported in multi-dex mode");
      } else {
         if (this.outputDex != null) {
            this.dexOutputFutures.add(this.dexOutPool.submit(new DexWriter(this.outputDex)));
            this.outputDex = null;
         }

         try {
            this.dexOutPool.shutdown();
            if (!this.dexOutPool.awaitTermination(600L, TimeUnit.SECONDS)) {
               throw new RuntimeException("Timed out waiting for dex writer threads.");
            }

            Iterator var1 = this.dexOutputFutures.iterator();

            while(var1.hasNext()) {
               Future<byte[]> f = (Future)var1.next();
               this.dexOutputArrays.add((byte[])f.get());
            }
         } catch (InterruptedException var9) {
            this.dexOutPool.shutdownNow();
            throw new RuntimeException("A dex writer thread has been interrupted.");
         } catch (Exception var10) {
            this.dexOutPool.shutdownNow();
            throw new RuntimeException("Unexpected exception in dex writer thread");
         }

         if (this.args.jarOutput) {
            for(int i = 0; i < this.dexOutputArrays.size(); ++i) {
               this.outputResources.put(getDexFileName(i), (byte[])this.dexOutputArrays.get(i));
            }

            if (!this.createJar(this.args.outName)) {
               return 3;
            }
         } else if (this.args.outName != null) {
            File outDir = new File(this.args.outName);

            assert outDir.isDirectory();

            for(int i = 0; i < this.dexOutputArrays.size(); ++i) {
               OutputStream out = new FileOutputStream(new File(outDir, getDexFileName(i)));

               try {
                  ((OutputStream)out).write((byte[])this.dexOutputArrays.get(i));
               } finally {
                  this.closeOutput(out);
               }
            }
         }

         return 0;
      }
   }

   private static String getDexFileName(int i) {
      return i == 0 ? "classes.dex" : "classes" + (i + 1) + ".dex";
   }

   private static void readPathsFromFile(String fileName, Collection<String> paths) throws IOException {
      BufferedReader bfr = null;

      try {
         FileReader fr = new FileReader(fileName);
         bfr = new BufferedReader(fr);

         String line;
         while(null != (line = bfr.readLine())) {
            paths.add(fixPath(line));
         }
      } finally {
         if (bfr != null) {
            bfr.close();
         }

      }

   }

   private byte[] mergeIncremental(byte[] update, File base) throws IOException {
      Dex dexA = null;
      Dex dexB = null;
      if (update != null) {
         dexA = new Dex(update);
      }

      if (base.exists()) {
         dexB = new Dex(base);
      }

      if (dexA == null && dexB == null) {
         return null;
      } else {
         Dex result;
         if (dexA == null) {
            result = dexB;
         } else if (dexB == null) {
            result = dexA;
         } else {
            result = (new DexMerger(new Dex[]{dexA, dexB}, CollisionPolicy.KEEP_FIRST, this.context)).merge();
         }

         ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
         result.writeTo((OutputStream)bytesOut);
         return bytesOut.toByteArray();
      }
   }

   private byte[] mergeLibraryDexBuffers(byte[] outArray) throws IOException {
      ArrayList<Dex> dexes = new ArrayList();
      if (outArray != null) {
         dexes.add(new Dex(outArray));
      }

      Iterator var3 = this.libraryDexBuffers.iterator();

      while(var3.hasNext()) {
         byte[] libraryDex = (byte[])var3.next();
         dexes.add(new Dex(libraryDex));
      }

      if (dexes.isEmpty()) {
         return null;
      } else {
         Dex merged = (new DexMerger((Dex[])dexes.toArray(new Dex[dexes.size()]), CollisionPolicy.FAIL, this.context)).merge();
         return merged.getBytes();
      }
   }

   private boolean processAllFiles() {
      this.createDexFile();
      if (this.args.jarOutput) {
         this.outputResources = new TreeMap();
      }

      this.anyFilesProcessed = false;
      String[] fileNames = this.args.fileNames;
      Arrays.sort(fileNames);
      this.classTranslatorPool = new ThreadPoolExecutor(this.args.numThreads, this.args.numThreads, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue(2 * this.args.numThreads, true), new ThreadPoolExecutor.CallerRunsPolicy());
      this.classDefItemConsumer = Executors.newSingleThreadExecutor();

      try {
         int i;
         if (this.args.mainDexListFile != null) {
            ClassPathOpener.FileNameFilter mainPassFilter = this.args.strictNameCheck ? new MainDexListFilter() : new BestEffortMainDexListFilter();

            for(i = 0; i < fileNames.length; ++i) {
               this.processOne(fileNames[i], (ClassPathOpener.FileNameFilter)mainPassFilter);
            }

            if (this.dexOutputFutures.size() > 0) {
               throw new DexException("Too many classes in --main-dex-list, main dex capacity exceeded");
            }

            if (this.args.minimalMainDex) {
               synchronized(this.dexRotationLock) {
                  while(this.maxMethodIdsInProcess > 0 || this.maxFieldIdsInProcess > 0) {
                     try {
                        this.dexRotationLock.wait();
                     } catch (InterruptedException var6) {
                     }
                  }
               }

               this.rotateDexFile();
            }

            ClassPathOpener.FileNameFilter filter = new RemoveModuleInfoFilter(new NotFilter((ClassPathOpener.FileNameFilter)mainPassFilter));

            for(int i = 0; i < fileNames.length; ++i) {
               this.processOne(fileNames[i], filter);
            }
         } else {
            ClassPathOpener.FileNameFilter filter = new RemoveModuleInfoFilter(ClassPathOpener.acceptAll);

            for(i = 0; i < fileNames.length; ++i) {
               this.processOne(fileNames[i], filter);
            }
         }
      } catch (StopProcessing var11) {
      }

      try {
         this.classTranslatorPool.shutdown();
         this.classTranslatorPool.awaitTermination(600L, TimeUnit.SECONDS);
         this.classDefItemConsumer.shutdown();
         this.classDefItemConsumer.awaitTermination(600L, TimeUnit.SECONDS);
         Iterator var15 = this.addToDexFutures.iterator();

         while(var15.hasNext()) {
            Future<Boolean> f = (Future)var15.next();

            try {
               f.get();
            } catch (ExecutionException var7) {
               int count = this.errors.incrementAndGet();
               if (count >= 10) {
                  throw new InterruptedException("Too many errors");
               }

               if (this.args.debug) {
                  this.context.err.println("Uncaught translation error:");
                  var7.getCause().printStackTrace(this.context.err);
               } else {
                  this.context.err.println("Uncaught translation error: " + var7.getCause());
               }
            }
         }
      } catch (InterruptedException var8) {
         this.classTranslatorPool.shutdownNow();
         this.classDefItemConsumer.shutdownNow();
         throw new RuntimeException("Translation has been interrupted", var8);
      } catch (Exception var9) {
         this.classTranslatorPool.shutdownNow();
         this.classDefItemConsumer.shutdownNow();
         var9.printStackTrace(this.context.out);
         throw new RuntimeException("Unexpected exception in translator thread.", var9);
      }

      int errorNum = this.errors.get();
      if (errorNum != 0) {
         this.context.err.println(errorNum + " error" + (errorNum == 1 ? "" : "s") + "; aborting");
         return false;
      } else if (this.args.incremental && !this.anyFilesProcessed) {
         return true;
      } else if (!this.anyFilesProcessed && !this.args.emptyOk) {
         this.context.err.println("no classfiles specified");
         return false;
      } else {
         if (this.args.optimize && this.args.statistics) {
            this.context.codeStatistics.dumpStatistics(this.context.out);
         }

         return true;
      }
   }

   private void createDexFile() {
      this.outputDex = new DexFile(this.args.dexOptions);
      if (this.args.dumpWidth != 0) {
         this.outputDex.setDumpWidth(this.args.dumpWidth);
      }

   }

   private void rotateDexFile() {
      if (this.outputDex != null) {
         if (this.dexOutPool != null) {
            this.dexOutputFutures.add(this.dexOutPool.submit(new DexWriter(this.outputDex)));
         } else {
            this.dexOutputArrays.add(this.writeDex(this.outputDex));
         }
      }

      this.createDexFile();
   }

   private void processOne(String pathname, ClassPathOpener.FileNameFilter filter) {
      ClassPathOpener opener = new ClassPathOpener(pathname, true, filter, new FileBytesConsumer());
      if (opener.process()) {
         this.updateStatus(true);
      }

   }

   private void updateStatus(boolean res) {
      this.anyFilesProcessed |= res;
   }

   private boolean processFileBytes(String name, long lastModified, byte[] bytes) {
      boolean isClass = name.endsWith(".class");
      boolean isClassesDex = name.equals("classes.dex");
      boolean keepResources = this.outputResources != null;
      if (!isClass && !isClassesDex && !keepResources) {
         if (this.args.verbose) {
            this.context.out.println("ignored resource " + name);
         }

         return false;
      } else {
         if (this.args.verbose) {
            this.context.out.println("processing " + name + "...");
         }

         String fixedName = fixPath(name);
         if (isClass) {
            if (keepResources && this.args.keepClassesInJar) {
               synchronized(this.outputResources) {
                  this.outputResources.put(fixedName, bytes);
               }
            }

            if (lastModified < this.minimumFileAge) {
               return true;
            } else {
               this.processClass(fixedName, bytes);
               return false;
            }
         } else if (isClassesDex) {
            synchronized(this.libraryDexBuffers) {
               this.libraryDexBuffers.add(bytes);
               return true;
            }
         } else {
            synchronized(this.outputResources) {
               this.outputResources.put(fixedName, bytes);
               return true;
            }
         }
      }
   }

   private boolean processClass(String name, byte[] bytes) {
      if (!this.args.coreLibrary) {
         this.checkClassName(name);
      }

      try {
         (new DirectClassFileConsumer(name, bytes, (Future)null)).call((new ClassParserTask(name, bytes)).call());
         return true;
      } catch (ParseException var4) {
         ParseException ex = var4;
         throw ex;
      } catch (Exception var5) {
         Exception ex = var5;
         throw new RuntimeException("Exception parsing classes", ex);
      }
   }

   private DirectClassFile parseClass(String name, byte[] bytes) {
      DirectClassFile cf = new DirectClassFile(bytes, name, this.args.cfOptions.strictNameCheck);
      cf.setAttributeFactory(StdAttributeFactory.THE_ONE);
      cf.getMagic();
      return cf;
   }

   private ClassDefItem translateClass(byte[] bytes, DirectClassFile cf) {
      try {
         return CfTranslator.translate(this.context, cf, bytes, this.args.cfOptions, this.args.dexOptions, this.outputDex);
      } catch (ParseException var4) {
         ParseException ex = var4;
         this.context.err.println("\ntrouble processing:");
         if (this.args.debug) {
            ex.printStackTrace(this.context.err);
         } else {
            ex.printContext(this.context.err);
         }

         this.errors.incrementAndGet();
         return null;
      }
   }

   private boolean addClassToDex(ClassDefItem clazz) {
      synchronized(this.outputDex) {
         this.outputDex.add(clazz);
         return true;
      }
   }

   private void checkClassName(String name) {
      boolean bogus = false;
      if (name.startsWith("java/")) {
         bogus = true;
      } else if (name.startsWith("javax/")) {
         int slashAt = name.indexOf(47, 6);
         if (slashAt == -1) {
            bogus = true;
         } else {
            String pkg = name.substring(6, slashAt);
            bogus = Arrays.binarySearch(JAVAX_CORE, pkg) >= 0;
         }
      }

      if (bogus) {
         this.context.err.println("\ntrouble processing \"" + name + "\":\n\n" + "Ill-advised or mistaken usage of a core class (java.* or javax.*)\nwhen not building a core library.\n\nThis is often due to inadvertently including a core library file\nin your application's project, when using an IDE (such as\nEclipse). If you are sure you're not intentionally defining a\ncore class, then this is the most likely explanation of what's\ngoing on.\n\nHowever, you might actually be trying to define a class in a core\nnamespace, the source of which you may have taken, for example,\nfrom a non-Android virtual machine project. This will most\nassuredly not work. At a minimum, it jeopardizes the\ncompatibility of your app with future versions of the platform.\nIt is also often of questionable legality.\n\nIf you really intend to build a core library -- which is only\nappropriate as part of creating a full virtual machine\ndistribution, as opposed to compiling an application -- then use\nthe \"--core-library\" option to suppress this error message.\n\nIf you go ahead and use \"--core-library\" but are in fact\nbuilding an application, then be forewarned that your application\nwill still fail to build or run, at some point. Please be\nprepared for angry customers who find, for example, that your\napplication ceases to function once they upgrade their operating\nsystem. You will be to blame for this problem.\n\nIf you are legitimately using some code that happens to be in a\ncore package, then the easiest safe alternative you have is to\nrepackage that code. That is, move the classes in question into\nyour own package namespace. This means that they will never be in\nconflict with core system classes. JarJar is a tool that may help\nyou in this endeavor. If you find that you cannot do this, then\nthat is an indication that the path you are on will ultimately\nlead to pain, suffering, grief, and lamentation.\n");
         this.errors.incrementAndGet();
         throw new StopProcessing();
      }
   }

   private byte[] writeDex(DexFile outputDex) {
      byte[] outArray = null;

      try {
         try {
            if (this.args.methodToDump != null) {
               outputDex.toDex((Writer)null, false);
               this.dumpMethod(outputDex, this.args.methodToDump, this.humanOutWriter);
            } else {
               outArray = outputDex.toDex(this.humanOutWriter, this.args.verboseDump);
            }

            if (this.args.statistics) {
               this.context.out.println(outputDex.getStatistics().toHuman());
            }
         } finally {
            if (this.humanOutWriter != null) {
               this.humanOutWriter.flush();
            }

         }

         return outArray;
      } catch (Exception var7) {
         Exception ex = var7;
         if (this.args.debug) {
            this.context.err.println("\ntrouble writing output:");
            ex.printStackTrace(this.context.err);
         } else {
            this.context.err.println("\ntrouble writing output: " + ex.getMessage());
         }

         return null;
      }
   }

   private boolean createJar(String fileName) {
      try {
         Manifest manifest = this.makeManifest();
         OutputStream out = this.openOutput(fileName);
         JarOutputStream jarOut = new JarOutputStream(out, manifest);

         try {
            Iterator var5 = this.outputResources.entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry<String, byte[]> e = (Map.Entry)var5.next();
               String name = (String)e.getKey();
               byte[] contents = (byte[])e.getValue();
               JarEntry entry = new JarEntry(name);
               int length = contents.length;
               if (this.args.verbose) {
                  this.context.out.println("writing " + name + "; size " + length + "...");
               }

               entry.setSize((long)length);
               jarOut.putNextEntry(entry);
               jarOut.write(contents);
               jarOut.closeEntry();
            }
         } finally {
            jarOut.finish();
            jarOut.flush();
            this.closeOutput(out);
         }

         return true;
      } catch (Exception var15) {
         Exception ex = var15;
         if (this.args.debug) {
            this.context.err.println("\ntrouble writing output:");
            ex.printStackTrace(this.context.err);
         } else {
            this.context.err.println("\ntrouble writing output: " + ex.getMessage());
         }

         return false;
      }
   }

   private Manifest makeManifest() throws IOException {
      byte[] manifestBytes = (byte[])this.outputResources.get("META-INF/MANIFEST.MF");
      Manifest manifest;
      Attributes attribs;
      if (manifestBytes == null) {
         manifest = new Manifest();
         attribs = manifest.getMainAttributes();
         attribs.put(Name.MANIFEST_VERSION, "1.0");
      } else {
         manifest = new Manifest(new ByteArrayInputStream(manifestBytes));
         attribs = manifest.getMainAttributes();
         this.outputResources.remove("META-INF/MANIFEST.MF");
      }

      String createdBy = attribs.getValue(CREATED_BY);
      if (createdBy == null) {
         createdBy = "";
      } else {
         createdBy = createdBy + " + ";
      }

      createdBy = createdBy + "dx 1.16";
      attribs.put(CREATED_BY, createdBy);
      attribs.putValue("Dex-Location", "classes.dex");
      return manifest;
   }

   private OutputStream openOutput(String name) throws IOException {
      return (OutputStream)(!name.equals("-") && !name.startsWith("-.") ? new FileOutputStream(name) : this.context.out);
   }

   private void closeOutput(OutputStream stream) throws IOException {
      if (stream != null) {
         stream.flush();
         if (stream != this.context.out) {
            stream.close();
         }

      }
   }

   private static String fixPath(String path) {
      if (File.separatorChar == '\\') {
         path = path.replace('\\', '/');
      }

      int index = path.lastIndexOf("/./");
      if (index != -1) {
         return path.substring(index + 3);
      } else {
         return path.startsWith("./") ? path.substring(2) : path;
      }
   }

   private void dumpMethod(DexFile dex, String fqName, OutputStreamWriter out) {
      boolean wildcard = fqName.endsWith("*");
      int lastDot = fqName.lastIndexOf(46);
      if (lastDot > 0 && lastDot != fqName.length() - 1) {
         String className = fqName.substring(0, lastDot).replace('.', '/');
         String methodName = fqName.substring(lastDot + 1);
         ClassDefItem clazz = dex.getClassOrNull(className);
         if (clazz == null) {
            this.context.err.println("no such class: " + className);
         } else {
            if (wildcard) {
               methodName = methodName.substring(0, methodName.length() - 1);
            }

            ArrayList<EncodedMethod> allMeths = clazz.getMethods();
            TreeMap<CstNat, EncodedMethod> meths = new TreeMap();
            Iterator var11 = allMeths.iterator();

            while(true) {
               EncodedMethod meth;
               String methName;
               do {
                  if (!var11.hasNext()) {
                     if (meths.size() == 0) {
                        this.context.err.println("no such method: " + fqName);
                        return;
                     }

                     PrintWriter pw = new PrintWriter(out);
                     Iterator var23 = meths.values().iterator();

                     while(true) {
                        AnnotationsList parameterAnnotations;
                        do {
                           if (!var23.hasNext()) {
                              pw.flush();
                              return;
                           }

                           EncodedMethod meth = (EncodedMethod)var23.next();
                           meth.debugPrint(pw, this.args.verboseDump);
                           CstString sourceFile = clazz.getSourceFile();
                           if (sourceFile != null) {
                              pw.println("  source file: " + sourceFile.toQuoted());
                           }

                           Annotations methodAnnotations = clazz.getMethodAnnotations(meth.getRef());
                           parameterAnnotations = clazz.getParameterAnnotations(meth.getRef());
                           if (methodAnnotations != null) {
                              pw.println("  method annotations:");
                              Iterator var17 = methodAnnotations.getAnnotations().iterator();

                              while(var17.hasNext()) {
                                 Annotation a = (Annotation)var17.next();
                                 pw.println("    " + a);
                              }
                           }
                        } while(parameterAnnotations == null);

                        pw.println("  parameter annotations:");
                        int sz = parameterAnnotations.size();

                        for(int i = 0; i < sz; ++i) {
                           pw.println("    parameter " + i);
                           Annotations annotations = parameterAnnotations.get(i);
                           Iterator var20 = annotations.getAnnotations().iterator();

                           while(var20.hasNext()) {
                              Annotation a = (Annotation)var20.next();
                              pw.println("      " + a);
                           }
                        }
                     }
                  }

                  meth = (EncodedMethod)var11.next();
                  methName = meth.getName().getString();
               } while((!wildcard || !methName.startsWith(methodName)) && (wildcard || !methName.equals(methodName)));

               meths.put(meth.getRef().getNat(), meth);
            }
         }
      } else {
         this.context.err.println("bogus fully-qualified method name: " + fqName);
      }
   }

   private class DexWriter implements Callable<byte[]> {
      private final DexFile dexFile;

      private DexWriter(DexFile dexFile) {
         this.dexFile = dexFile;
      }

      public byte[] call() throws IOException {
         return Main.this.writeDex(this.dexFile);
      }

      // $FF: synthetic method
      DexWriter(DexFile x1, Object x2) {
         this(x1);
      }
   }

   private class ClassDefItemConsumer implements Callable<Boolean> {
      String name;
      Future<ClassDefItem> futureClazz;
      int maxMethodIdsInClass;
      int maxFieldIdsInClass;

      private ClassDefItemConsumer(String name, Future<ClassDefItem> futureClazz, int maxMethodIdsInClass, int maxFieldIdsInClass) {
         this.name = name;
         this.futureClazz = futureClazz;
         this.maxMethodIdsInClass = maxMethodIdsInClass;
         this.maxFieldIdsInClass = maxFieldIdsInClass;
      }

      public Boolean call() throws Exception {
         boolean var12 = false;

         Boolean var18;
         try {
            var12 = true;
            ClassDefItem clazz = (ClassDefItem)this.futureClazz.get();
            if (clazz != null) {
               Main.this.addClassToDex(clazz);
               Main.this.updateStatus(true);
            }

            var18 = true;
            var12 = false;
         } catch (ExecutionException var15) {
            ExecutionException ex = var15;
            Throwable t = ex.getCause();
            throw (Exception)(t instanceof Exception ? (Exception)t : ex);
         } finally {
            if (var12) {
               if (Main.this.args.multiDex) {
                  synchronized(Main.this.dexRotationLock) {
                     Main.this.maxMethodIdsInProcess = this.maxMethodIdsInClass;
                     Main.this.maxFieldIdsInProcess = this.maxFieldIdsInClass;
                     Main.this.dexRotationLock.notifyAll();
                  }
               }

            }
         }

         if (Main.this.args.multiDex) {
            synchronized(Main.this.dexRotationLock) {
               Main.this.maxMethodIdsInProcess = this.maxMethodIdsInClass;
               Main.this.maxFieldIdsInProcess = this.maxFieldIdsInClass;
               Main.this.dexRotationLock.notifyAll();
            }
         }

         return var18;
      }

      // $FF: synthetic method
      ClassDefItemConsumer(String x1, Future x2, int x3, int x4, Object x5) {
         this(x1, x2, x3, x4);
      }
   }

   private class ClassTranslatorTask implements Callable<ClassDefItem> {
      String name;
      byte[] bytes;
      DirectClassFile classFile;

      private ClassTranslatorTask(String name, byte[] bytes, DirectClassFile classFile) {
         this.name = name;
         this.bytes = bytes;
         this.classFile = classFile;
      }

      public ClassDefItem call() {
         ClassDefItem clazz = Main.this.translateClass(this.bytes, this.classFile);
         return clazz;
      }

      // $FF: synthetic method
      ClassTranslatorTask(String x1, byte[] x2, DirectClassFile x3, Object x4) {
         this(x1, x2, x3);
      }
   }

   private class DirectClassFileConsumer implements Callable<Boolean> {
      String name;
      byte[] bytes;
      Future<DirectClassFile> dcff;

      private DirectClassFileConsumer(String name, byte[] bytes, Future<DirectClassFile> dcff) {
         this.name = name;
         this.bytes = bytes;
         this.dcff = dcff;
      }

      public Boolean call() throws Exception {
         DirectClassFile cf = (DirectClassFile)this.dcff.get();
         return this.call(cf);
      }

      private Boolean call(DirectClassFile cf) {
         int maxMethodIdsInClass = 0;
         int maxFieldIdsInClass = 0;
         if (Main.this.args.multiDex) {
            int constantPoolSize = cf.getConstantPool().size();
            maxMethodIdsInClass = constantPoolSize + cf.getMethods().size() + 2;
            maxFieldIdsInClass = constantPoolSize + cf.getFields().size() + 9;
            synchronized(Main.this.dexRotationLock) {
               int numMethodIds;
               int numFieldIds;
               synchronized(Main.this.outputDex) {
                  numMethodIds = Main.this.outputDex.getMethodIds().items().size();
                  numFieldIds = Main.this.outputDex.getFieldIds().items().size();
               }

               while(numMethodIds + maxMethodIdsInClass + Main.this.maxMethodIdsInProcess > Main.this.args.maxNumberOfIdxPerDex || numFieldIds + maxFieldIdsInClass + Main.this.maxFieldIdsInProcess > Main.this.args.maxNumberOfIdxPerDex) {
                  if (Main.this.maxMethodIdsInProcess <= 0 && Main.this.maxFieldIdsInProcess <= 0) {
                     if (Main.this.outputDex.getClassDefs().items().size() <= 0) {
                        break;
                     }

                     Main.this.rotateDexFile();
                  } else {
                     try {
                        Main.this.dexRotationLock.wait();
                     } catch (InterruptedException var13) {
                     }
                  }

                  synchronized(Main.this.outputDex) {
                     numMethodIds = Main.this.outputDex.getMethodIds().items().size();
                     numFieldIds = Main.this.outputDex.getFieldIds().items().size();
                  }
               }

               Main.this.maxMethodIdsInProcess = maxMethodIdsInClass;
               Main.this.maxFieldIdsInProcess = maxFieldIdsInClass;
            }
         }

         Future<ClassDefItem> cdif = Main.this.classTranslatorPool.submit(Main.this.new ClassTranslatorTask(this.name, this.bytes, cf));
         Future<Boolean> res = Main.this.classDefItemConsumer.submit(Main.this.new ClassDefItemConsumer(this.name, cdif, maxMethodIdsInClass, maxFieldIdsInClass));
         Main.this.addToDexFutures.add(res);
         return true;
      }

      // $FF: synthetic method
      DirectClassFileConsumer(String x1, byte[] x2, Future x3, Object x4) {
         this(x1, x2, x3);
      }
   }

   private class ClassParserTask implements Callable<DirectClassFile> {
      String name;
      byte[] bytes;

      private ClassParserTask(String name, byte[] bytes) {
         this.name = name;
         this.bytes = bytes;
      }

      public DirectClassFile call() throws Exception {
         DirectClassFile cf = Main.this.parseClass(this.name, this.bytes);
         return cf;
      }

      // $FF: synthetic method
      ClassParserTask(String x1, byte[] x2, Object x3) {
         this(x1, x2);
      }
   }

   private class FileBytesConsumer implements ClassPathOpener.Consumer {
      private FileBytesConsumer() {
      }

      public boolean processFileBytes(String name, long lastModified, byte[] bytes) {
         return Main.this.processFileBytes(name, lastModified, bytes);
      }

      public void onException(Exception ex) {
         if (ex instanceof StopProcessing) {
            throw (StopProcessing)ex;
         } else {
            if (ex instanceof SimException) {
               Main.this.context.err.println("\nEXCEPTION FROM SIMULATION:");
               Main.this.context.err.println(ex.getMessage() + "\n");
               Main.this.context.err.println(((SimException)ex).getContext());
            } else if (ex instanceof ParseException) {
               Main.this.context.err.println("\nPARSE ERROR:");
               ParseException parseException = (ParseException)ex;
               if (Main.this.args.debug) {
                  parseException.printStackTrace(Main.this.context.err);
               } else {
                  parseException.printContext(Main.this.context.err);
               }
            } else {
               Main.this.context.err.println("\nUNEXPECTED TOP-LEVEL EXCEPTION:");
               ex.printStackTrace(Main.this.context.err);
            }

            Main.this.errors.incrementAndGet();
         }
      }

      public void onProcessArchiveStart(File file) {
         if (Main.this.args.verbose) {
            Main.this.context.out.println("processing archive " + file + "...");
         }

      }

      // $FF: synthetic method
      FileBytesConsumer(Object x1) {
         this();
      }
   }

   public static class Arguments {
      private static final String MINIMAL_MAIN_DEX_OPTION = "--minimal-main-dex";
      private static final String MAIN_DEX_LIST_OPTION = "--main-dex-list";
      private static final String MULTI_DEX_OPTION = "--multi-dex";
      private static final String NUM_THREADS_OPTION = "--num-threads";
      private static final String INCREMENTAL_OPTION = "--incremental";
      private static final String INPUT_LIST_OPTION = "--input-list";
      public final DxContext context;
      public boolean debug;
      public boolean warnings;
      public boolean verbose;
      public boolean verboseDump;
      public boolean coreLibrary;
      public String methodToDump;
      public int dumpWidth;
      public String outName;
      public String humanOutName;
      public boolean strictNameCheck;
      public boolean emptyOk;
      public boolean jarOutput;
      public boolean keepClassesInJar;
      public int minSdkVersion;
      public int positionInfo;
      public boolean localInfo;
      public boolean incremental;
      public boolean forceJumbo;
      public boolean allowAllInterfaceMethodInvokes;
      public String[] fileNames;
      public boolean optimize;
      public String optimizeListFile;
      public String dontOptimizeListFile;
      public boolean statistics;
      public CfOptions cfOptions;
      public DexOptions dexOptions;
      public int numThreads;
      public boolean multiDex;
      public String mainDexListFile;
      public boolean minimalMainDex;
      public int maxNumberOfIdxPerDex;
      private List<String> inputList;
      private boolean outputIsDirectory;
      private boolean outputIsDirectDex;

      public Arguments(DxContext context) {
         this.debug = false;
         this.warnings = true;
         this.verbose = false;
         this.verboseDump = false;
         this.coreLibrary = false;
         this.methodToDump = null;
         this.dumpWidth = 0;
         this.outName = null;
         this.humanOutName = null;
         this.strictNameCheck = true;
         this.emptyOk = false;
         this.jarOutput = false;
         this.keepClassesInJar = false;
         this.minSdkVersion = 13;
         this.positionInfo = 2;
         this.localInfo = true;
         this.incremental = false;
         this.forceJumbo = false;
         this.allowAllInterfaceMethodInvokes = false;
         this.optimize = true;
         this.optimizeListFile = null;
         this.dontOptimizeListFile = null;
         this.numThreads = 1;
         this.multiDex = false;
         this.mainDexListFile = null;
         this.minimalMainDex = false;
         this.maxNumberOfIdxPerDex = 65536;
         this.inputList = null;
         this.outputIsDirectory = false;
         this.outputIsDirectDex = false;
         this.context = context;
      }

      public Arguments() {
         this(new DxContext());
      }

      private void parseFlags(ArgumentsParser parser) {
         while(true) {
            if (parser.getNext()) {
               if (parser.isArg("--debug")) {
                  this.debug = true;
                  continue;
               }

               if (parser.isArg("--no-warning")) {
                  this.warnings = false;
                  continue;
               }

               if (parser.isArg("--verbose")) {
                  this.verbose = true;
                  continue;
               }

               if (parser.isArg("--verbose-dump")) {
                  this.verboseDump = true;
                  continue;
               }

               if (parser.isArg("--no-files")) {
                  this.emptyOk = true;
                  continue;
               }

               if (parser.isArg("--no-optimize")) {
                  this.optimize = false;
                  continue;
               }

               if (parser.isArg("--no-strict")) {
                  this.strictNameCheck = false;
                  continue;
               }

               if (parser.isArg("--core-library")) {
                  this.coreLibrary = true;
                  continue;
               }

               if (parser.isArg("--statistics")) {
                  this.statistics = true;
                  continue;
               }

               if (parser.isArg("--optimize-list=")) {
                  if (this.dontOptimizeListFile != null) {
                     this.context.err.println("--optimize-list and --no-optimize-list are incompatible.");
                     throw new UsageException();
                  }

                  this.optimize = true;
                  this.optimizeListFile = parser.getLastValue();
                  continue;
               }

               if (parser.isArg("--no-optimize-list=")) {
                  if (this.dontOptimizeListFile != null) {
                     this.context.err.println("--optimize-list and --no-optimize-list are incompatible.");
                     throw new UsageException();
                  }

                  this.optimize = true;
                  this.dontOptimizeListFile = parser.getLastValue();
                  continue;
               }

               if (parser.isArg("--keep-classes")) {
                  this.keepClassesInJar = true;
                  continue;
               }

               if (parser.isArg("--output=")) {
                  this.outName = parser.getLastValue();
                  if ((new File(this.outName)).isDirectory()) {
                     this.jarOutput = false;
                     this.outputIsDirectory = true;
                     continue;
                  }

                  if (FileUtils.hasArchiveSuffix(this.outName)) {
                     this.jarOutput = true;
                     continue;
                  }

                  if (!this.outName.endsWith(".dex") && !this.outName.equals("-")) {
                     this.context.err.println("unknown output extension: " + this.outName);
                     throw new UsageException();
                  }

                  this.jarOutput = false;
                  this.outputIsDirectDex = true;
                  continue;
               }

               if (parser.isArg("--dump-to=")) {
                  this.humanOutName = parser.getLastValue();
                  continue;
               }

               if (parser.isArg("--dump-width=")) {
                  this.dumpWidth = Integer.parseInt(parser.getLastValue());
                  continue;
               }

               if (parser.isArg("--dump-method=")) {
                  this.methodToDump = parser.getLastValue();
                  this.jarOutput = false;
                  continue;
               }

               String arg;
               if (parser.isArg("--positions=")) {
                  arg = parser.getLastValue().intern();
                  if (arg == "none") {
                     this.positionInfo = 1;
                     continue;
                  }

                  if (arg == "important") {
                     this.positionInfo = 3;
                     continue;
                  }

                  if (arg == "lines") {
                     this.positionInfo = 2;
                     continue;
                  }

                  this.context.err.println("unknown positions option: " + arg);
                  throw new UsageException();
               }

               if (parser.isArg("--no-locals")) {
                  this.localInfo = false;
                  continue;
               }

               if (parser.isArg("--num-threads=")) {
                  this.numThreads = Integer.parseInt(parser.getLastValue());
                  continue;
               }

               if (parser.isArg("--incremental")) {
                  this.incremental = true;
                  continue;
               }

               if (parser.isArg("--force-jumbo")) {
                  this.forceJumbo = true;
                  continue;
               }

               if (parser.isArg("--multi-dex")) {
                  this.multiDex = true;
                  continue;
               }

               if (parser.isArg("--main-dex-list=")) {
                  this.mainDexListFile = parser.getLastValue();
                  continue;
               }

               if (parser.isArg("--minimal-main-dex")) {
                  this.minimalMainDex = true;
                  continue;
               }

               if (parser.isArg("--set-max-idx-number=")) {
                  this.maxNumberOfIdxPerDex = Integer.parseInt(parser.getLastValue());
                  continue;
               }

               if (parser.isArg("--input-list=")) {
                  File inputListFile = new File(parser.getLastValue());

                  try {
                     this.inputList = new ArrayList();
                     Main.readPathsFromFile(inputListFile.getAbsolutePath(), this.inputList);
                     continue;
                  } catch (IOException var5) {
                     this.context.err.println("Unable to read input list file: " + inputListFile.getName());
                     throw new UsageException();
                  }
               }

               if (parser.isArg("--min-sdk-version=")) {
                  arg = parser.getLastValue();

                  int value;
                  try {
                     value = Integer.parseInt(arg);
                  } catch (NumberFormatException var6) {
                     value = -1;
                  }

                  if (value < 1) {
                     System.err.println("improper min-sdk-version option: " + arg);
                     throw new UsageException();
                  }

                  this.minSdkVersion = value;
                  continue;
               }

               if (parser.isArg("--allow-all-interface-method-invokes")) {
                  this.allowAllInterfaceMethodInvokes = true;
                  continue;
               }

               this.context.err.println("unknown option: " + parser.getCurrent());
               throw new UsageException();
            }

            return;
         }
      }

      private void parse(String[] args) {
         ArgumentsParser parser = new ArgumentsParser(args);
         this.parseFlags(parser);
         this.fileNames = parser.getRemaining();
         if (this.inputList != null && !this.inputList.isEmpty()) {
            this.inputList.addAll(Arrays.asList(this.fileNames));
            this.fileNames = (String[])this.inputList.toArray(new String[this.inputList.size()]);
         }

         if (this.fileNames.length == 0) {
            if (!this.emptyOk) {
               this.context.err.println("no input files specified");
               throw new UsageException();
            }
         } else if (this.emptyOk) {
            this.context.out.println("ignoring input files");
         }

         if (this.humanOutName == null && this.methodToDump != null) {
            this.humanOutName = "-";
         }

         if (this.mainDexListFile != null && !this.multiDex) {
            this.context.err.println("--main-dex-list is only supported in combination with --multi-dex");
            throw new UsageException();
         } else if (!this.minimalMainDex || this.mainDexListFile != null && this.multiDex) {
            if (this.multiDex && this.incremental) {
               this.context.err.println("--incremental is not supported with --multi-dex");
               throw new UsageException();
            } else if (this.multiDex && this.outputIsDirectDex) {
               this.context.err.println("Unsupported output \"" + this.outName + "\". " + "--multi-dex" + " supports only archive or directory output");
               throw new UsageException();
            } else {
               if (this.outputIsDirectory && !this.multiDex) {
                  this.outName = (new File(this.outName, "classes.dex")).getPath();
               }

               this.makeOptionsObjects();
            }
         } else {
            this.context.err.println("--minimal-main-dex is only supported in combination with --multi-dex and --main-dex-list");
            throw new UsageException();
         }
      }

      public void parseFlags(String[] flags) {
         this.parseFlags(new ArgumentsParser(flags));
      }

      public void makeOptionsObjects() {
         this.cfOptions = new CfOptions();
         this.cfOptions.positionInfo = this.positionInfo;
         this.cfOptions.localInfo = this.localInfo;
         this.cfOptions.strictNameCheck = this.strictNameCheck;
         this.cfOptions.optimize = this.optimize;
         this.cfOptions.optimizeListFile = this.optimizeListFile;
         this.cfOptions.dontOptimizeListFile = this.dontOptimizeListFile;
         this.cfOptions.statistics = this.statistics;
         if (this.warnings) {
            this.cfOptions.warn = this.context.err;
         } else {
            this.cfOptions.warn = this.context.noop;
         }

         this.dexOptions = new DexOptions(this.context.err);
         this.dexOptions.minSdkVersion = this.minSdkVersion;
         this.dexOptions.forceJumbo = this.forceJumbo;
         this.dexOptions.allowAllInterfaceMethodInvokes = this.allowAllInterfaceMethodInvokes;
      }

      private static class ArgumentsParser {
         private final String[] arguments;
         private int index;
         private String current;
         private String lastValue;

         public ArgumentsParser(String[] arguments) {
            this.arguments = arguments;
            this.index = 0;
         }

         public String getCurrent() {
            return this.current;
         }

         public String getLastValue() {
            return this.lastValue;
         }

         public boolean getNext() {
            if (this.index >= this.arguments.length) {
               return false;
            } else {
               this.current = this.arguments[this.index];
               if (!this.current.equals("--") && this.current.startsWith("--")) {
                  ++this.index;
                  return true;
               } else {
                  return false;
               }
            }
         }

         private boolean getNextValue() {
            if (this.index >= this.arguments.length) {
               return false;
            } else {
               this.current = this.arguments[this.index];
               ++this.index;
               return true;
            }
         }

         public String[] getRemaining() {
            int n = this.arguments.length - this.index;
            String[] remaining = new String[n];
            if (n > 0) {
               System.arraycopy(this.arguments, this.index, remaining, 0, n);
            }

            return remaining;
         }

         public boolean isArg(String prefix) {
            int n = prefix.length();
            if (n > 0 && prefix.charAt(n - 1) == '=') {
               if (this.current.startsWith(prefix)) {
                  this.lastValue = this.current.substring(n);
                  return true;
               } else {
                  prefix = prefix.substring(0, n - 1);
                  if (this.current.equals(prefix)) {
                     if (this.getNextValue()) {
                        this.lastValue = this.current;
                        return true;
                     } else {
                        System.err.println("Missing value after parameter " + prefix);
                        throw new UsageException();
                     }
                  } else {
                     return false;
                  }
               }
            } else {
               return this.current.equals(prefix);
            }
         }
      }
   }

   private static class StopProcessing extends RuntimeException {
      private StopProcessing() {
      }

      // $FF: synthetic method
      StopProcessing(Object x0) {
         this();
      }
   }

   private class BestEffortMainDexListFilter implements ClassPathOpener.FileNameFilter {
      Map<String, List<String>> map = new HashMap();

      public BestEffortMainDexListFilter() {
         String normalized;
         Object fullPath;
         for(Iterator var2 = Main.this.classesInMainDex.iterator(); var2.hasNext(); ((List)fullPath).add(normalized)) {
            String pathOfClass = (String)var2.next();
            normalized = Main.fixPath(pathOfClass);
            String simple = this.getSimpleName(normalized);
            fullPath = (List)this.map.get(simple);
            if (fullPath == null) {
               fullPath = new ArrayList(1);
               this.map.put(simple, fullPath);
            }
         }

      }

      public boolean accept(String path) {
         if (!path.endsWith(".class")) {
            return true;
         } else {
            String normalized = Main.fixPath(path);
            String simple = this.getSimpleName(normalized);
            List<String> fullPaths = (List)this.map.get(simple);
            if (fullPaths != null) {
               Iterator var5 = fullPaths.iterator();

               while(var5.hasNext()) {
                  String fullPath = (String)var5.next();
                  if (normalized.endsWith(fullPath)) {
                     return true;
                  }
               }
            }

            return false;
         }
      }

      private String getSimpleName(String path) {
         int index = path.lastIndexOf(47);
         return index >= 0 ? path.substring(index + 1) : path;
      }
   }

   private class MainDexListFilter implements ClassPathOpener.FileNameFilter {
      private MainDexListFilter() {
      }

      public boolean accept(String fullPath) {
         if (fullPath.endsWith(".class")) {
            String path = Main.fixPath(fullPath);
            return Main.this.classesInMainDex.contains(path);
         } else {
            return true;
         }
      }

      // $FF: synthetic method
      MainDexListFilter(Object x1) {
         this();
      }
   }

   private static class RemoveModuleInfoFilter implements ClassPathOpener.FileNameFilter {
      protected final ClassPathOpener.FileNameFilter delegate;

      public RemoveModuleInfoFilter(ClassPathOpener.FileNameFilter delegate) {
         this.delegate = delegate;
      }

      public boolean accept(String path) {
         return this.delegate.accept(path) && !"module-info.class".equals(path);
      }
   }

   private static class NotFilter implements ClassPathOpener.FileNameFilter {
      private final ClassPathOpener.FileNameFilter filter;

      private NotFilter(ClassPathOpener.FileNameFilter filter) {
         this.filter = filter;
      }

      public boolean accept(String path) {
         return !this.filter.accept(path);
      }

      // $FF: synthetic method
      NotFilter(ClassPathOpener.FileNameFilter x0, Object x1) {
         this(x0);
      }
   }
}
