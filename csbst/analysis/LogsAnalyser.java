/*     */ package csbst.analysis;
/*     */ 
/*     */ import csbst.utils.ExceptionsFormatter.ExceptionFormat;
/*     */ import csbst.utils.ReadSpecificLine;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogsAnalyser
/*     */ {
/*     */   private String logFile;
/*     */   private String[] sourceDirectories;
/*     */   
/*     */   public LogsAnalyser(String logFile, String[] sourceDirectories)
/*     */   {
/*  27 */     this.logFile = logFile;
/*  28 */     this.sourceDirectories = sourceDirectories;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private void log2Bugs()
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 15	csbst/analysis/LogsAnalyser:logFile	Ljava/lang/String;
/*     */     //   4: ifnonnull +4 -> 8
/*     */     //   7: return
/*     */     //   8: aconst_null
/*     */     //   9: astore_1
/*     */     //   10: aconst_null
/*     */     //   11: astore_2
/*     */     //   12: aconst_null
/*     */     //   13: astore_3
/*     */     //   14: ldc 27
/*     */     //   16: astore 4
/*     */     //   18: new 29	java/io/FileReader
/*     */     //   21: dup
/*     */     //   22: aload_0
/*     */     //   23: getfield 15	csbst/analysis/LogsAnalyser:logFile	Ljava/lang/String;
/*     */     //   26: invokespecial 31	java/io/FileReader:<init>	(Ljava/lang/String;)V
/*     */     //   29: astore_2
/*     */     //   30: new 34	java/io/BufferedReader
/*     */     //   33: dup
/*     */     //   34: aload_2
/*     */     //   35: invokespecial 36	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
/*     */     //   38: astore_3
/*     */     //   39: aload_3
/*     */     //   40: invokevirtual 39	java/io/BufferedReader:readLine	()Ljava/lang/String;
/*     */     //   43: astore_1
/*     */     //   44: goto +80 -> 124
/*     */     //   47: aload_1
/*     */     //   48: ldc 43
/*     */     //   50: invokevirtual 45	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*     */     //   53: ifeq +66 -> 119
/*     */     //   56: aload_0
/*     */     //   57: aload_1
/*     */     //   58: invokespecial 51	csbst/analysis/LogsAnalyser:checkException	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   61: astore 5
/*     */     //   63: aload 5
/*     */     //   65: ifnull +54 -> 119
/*     */     //   68: aload 5
/*     */     //   70: ldc 55
/*     */     //   72: invokevirtual 45	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*     */     //   75: ifeq +44 -> 119
/*     */     //   78: aload 5
/*     */     //   80: ldc 57
/*     */     //   82: ldc 27
/*     */     //   84: invokevirtual 59	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
/*     */     //   87: astore 5
/*     */     //   89: new 63	java/lang/StringBuilder
/*     */     //   92: dup
/*     */     //   93: aload 4
/*     */     //   95: invokestatic 65	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   98: invokespecial 69	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   101: aload 5
/*     */     //   103: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   106: ldc 74
/*     */     //   108: invokestatic 76	java/lang/System:getProperty	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   111: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   114: invokevirtual 81	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   117: astore 4
/*     */     //   119: aload_3
/*     */     //   120: invokevirtual 39	java/io/BufferedReader:readLine	()Ljava/lang/String;
/*     */     //   123: astore_1
/*     */     //   124: aload_1
/*     */     //   125: ifnonnull -78 -> 47
/*     */     //   128: goto +24 -> 152
/*     */     //   131: astore 6
/*     */     //   133: aload_2
/*     */     //   134: ifnull +7 -> 141
/*     */     //   137: aload_2
/*     */     //   138: invokevirtual 84	java/io/FileReader:close	()V
/*     */     //   141: aload_3
/*     */     //   142: ifnull +7 -> 149
/*     */     //   145: aload_3
/*     */     //   146: invokevirtual 87	java/io/BufferedReader:close	()V
/*     */     //   149: aload 6
/*     */     //   151: athrow
/*     */     //   152: aload_2
/*     */     //   153: ifnull +7 -> 160
/*     */     //   156: aload_2
/*     */     //   157: invokevirtual 84	java/io/FileReader:close	()V
/*     */     //   160: aload_3
/*     */     //   161: ifnull +7 -> 168
/*     */     //   164: aload_3
/*     */     //   165: invokevirtual 87	java/io/BufferedReader:close	()V
/*     */     //   168: aload 4
/*     */     //   170: new 63	java/lang/StringBuilder
/*     */     //   173: dup
/*     */     //   174: aload_0
/*     */     //   175: getfield 15	csbst/analysis/LogsAnalyser:logFile	Ljava/lang/String;
/*     */     //   178: invokestatic 65	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   181: invokespecial 69	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   184: ldc 88
/*     */     //   186: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   189: invokevirtual 81	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   192: invokestatic 90	csbst/analysis/LogsAnalyser:unit2File	(Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   195: getstatic 94	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   198: new 63	java/lang/StringBuilder
/*     */     //   201: dup
/*     */     //   202: ldc 98
/*     */     //   204: invokespecial 69	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   207: aload_0
/*     */     //   208: getfield 15	csbst/analysis/LogsAnalyser:logFile	Ljava/lang/String;
/*     */     //   211: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   214: ldc 88
/*     */     //   216: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   219: invokevirtual 81	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   222: invokevirtual 100	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   225: return
/*     */     // Line number table:
/*     */     //   Java source line #32	-> byte code offset #0
/*     */     //   Java source line #33	-> byte code offset #7
/*     */     //   Java source line #35	-> byte code offset #8
/*     */     //   Java source line #37	-> byte code offset #10
/*     */     //   Java source line #38	-> byte code offset #12
/*     */     //   Java source line #39	-> byte code offset #14
/*     */     //   Java source line #41	-> byte code offset #18
/*     */     //   Java source line #42	-> byte code offset #30
/*     */     //   Java source line #44	-> byte code offset #39
/*     */     //   Java source line #45	-> byte code offset #44
/*     */     //   Java source line #46	-> byte code offset #47
/*     */     //   Java source line #47	-> byte code offset #56
/*     */     //   Java source line #48	-> byte code offset #63
/*     */     //   Java source line #49	-> byte code offset #78
/*     */     //   Java source line #50	-> byte code offset #89
/*     */     //   Java source line #53	-> byte code offset #119
/*     */     //   Java source line #45	-> byte code offset #124
/*     */     //   Java source line #55	-> byte code offset #131
/*     */     //   Java source line #56	-> byte code offset #133
/*     */     //   Java source line #57	-> byte code offset #137
/*     */     //   Java source line #58	-> byte code offset #141
/*     */     //   Java source line #59	-> byte code offset #145
/*     */     //   Java source line #60	-> byte code offset #149
/*     */     //   Java source line #56	-> byte code offset #152
/*     */     //   Java source line #57	-> byte code offset #156
/*     */     //   Java source line #58	-> byte code offset #160
/*     */     //   Java source line #59	-> byte code offset #164
/*     */     //   Java source line #62	-> byte code offset #168
/*     */     //   Java source line #63	-> byte code offset #195
/*     */     //   Java source line #64	-> byte code offset #225
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	226	0	this	LogsAnalyser
/*     */     //   9	116	1	line	String
/*     */     //   11	146	2	fr	java.io.FileReader
/*     */     //   13	152	3	br	java.io.BufferedReader
/*     */     //   16	153	4	BugsFile	String
/*     */     //   61	41	5	newLine	String
/*     */     //   131	19	6	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   18	131	131	finally
/*     */   }
/*     */   
/*     */   public static void unit2File(String unit, String fileName)
/*     */   {
/*     */     try
/*     */     {
/*  69 */       File file = new File(fileName);
/*  70 */       file.getParentFile().mkdirs();
/*     */       
/*  72 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
/*  73 */       out.write(unit);
/*  74 */       out.close();
/*     */     } catch (IOException e) {
/*  76 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private String checkException(String logLine) {
/*  81 */     String newLine = null;
/*  82 */     if (logLine == null) {
/*  83 */       return null;
/*     */     }
/*  85 */     ExceptionsFormatter.ExceptionFormat exception = new ExceptionsFormatter.ExceptionFormat(logLine);
/*     */     
/*  87 */     if (exception == null) {
/*  88 */       return null;
/*     */     }
/*  90 */     boolean checkThrow = checkException(exception.getClazz(), exception.getLine());
/*  91 */     if (!checkThrow) {
/*  92 */       for (ExceptionsFormatter.ExceptionFormat cause : exception.getCausesList()) {
/*  93 */         checkThrow = checkException(cause.getClazz(), cause.getLine());
/*  94 */         if (checkThrow) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*  99 */     if (checkThrow) {
/* 100 */       newLine = "true";
/*     */     } else {
/* 102 */       newLine = "false";
/*     */     }
/* 104 */     newLine = newLine + ", " + exception.getBugString();
/*     */     
/* 106 */     return newLine;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean checkException(String clazz, String line)
/*     */   {
/* 112 */     if (clazz.contains("JTETestCases")) {
/* 113 */       return true;
/*     */     }
/* 115 */     int dollarPosition = clazz.indexOf('$');
/* 116 */     if (dollarPosition > 0) {
/* 117 */       clazz = clazz.substring(0, dollarPosition);
/*     */     }
/*     */     
/* 120 */     String javaclazz = clazz.replace(".", File.separator) + ".java";
/*     */     
/* 122 */     String javaFile = "";
/* 123 */     boolean exist = false;
/* 124 */     for (int i = 0; i < this.sourceDirectories.length; i++) {
/* 125 */       javaFile = this.sourceDirectories[i] + File.separator + javaclazz;
/* 126 */       File checkExist = new File(javaFile);
/* 127 */       if (checkExist.exists()) {
/* 128 */         exist = true;
/* 129 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 135 */     boolean error = true;
/*     */     
/* 137 */     if (!exist) {
/* 138 */       return error;
/*     */     }
/* 140 */     int nLine = -1;
/*     */     try {
/* 142 */       nLine = Integer.parseInt(line);
/*     */     } catch (NumberFormatException e) {
/* 144 */       return error;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 149 */       throwLine = ReadSpecificLine.getLine(javaFile, nLine);
/*     */     } catch (IOException e) {
/*     */       String throwLine;
/* 152 */       return error;
/*     */     }
/*     */     String throwLine;
/* 155 */     if (throwLine == null) {
/* 156 */       throwLine = "";
/*     */     }
/* 158 */     if ((throwLine.contains("throw")) || 
/* 159 */       (throwLine.contains("Exception")) || 
/* 160 */       (throwLine.contains("check")) || 
/* 161 */       (throwLine.contains("Assert")) || 
/* 162 */       (throwLine.contains("Invariant")) || 
/* 163 */       (throwLine.contains("Requires")) || 
/* 164 */       ((throwLine.contains("System")) && (throwLine.contains("exit"))) || 
/* 165 */       (throwLine.contains("Throwable"))) {
/* 166 */       return true;
/*     */     }
/* 168 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 224 */     String src = "";
/* 225 */     String log = "";
/* 226 */     int i = 0;
/*     */     
/* 228 */     while ((i < args.length) && (args[i].startsWith("-"))) {
/* 229 */       String arg = args[(i++)];
/* 230 */       if (arg.equals("-src"))
/* 231 */         if (i < args.length) { src = args[(i++)];
/*     */         } else {
/* 233 */           System.err.println(arg + " requires a value");
/* 234 */           System.exit(-1);
/*     */         }
/* 236 */       if (arg.equals("-log")) {
/* 237 */         if (i < args.length) { log = args[(i++)];
/*     */         } else {
/* 239 */           System.err.println(arg + " requires a value");
/* 240 */           System.exit(-1);
/*     */         }
/*     */       }
/*     */     }
/* 244 */     String[] srcDirectories = src.split(File.pathSeparator);
/* 245 */     LogsAnalyser bf = new LogsAnalyser(log, srcDirectories);
/*     */     try
/*     */     {
/* 248 */       bf.log2Bugs();
/*     */     }
/*     */     catch (IOException e) {
/* 251 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\LogsAnalyser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */