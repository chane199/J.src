/*     */ package csbst.utils;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ExceptionsFormatter
/*     */ {
/*     */   public static final String EXCEPTION_PREAMBLE = "**startException**, ";
/*     */   
/*     */   public static void printException(Throwable exce, String projectPrefix, String CUT)
/*     */   {
/*  12 */     if ((exce == null) || (exce.getStackTrace() == null) || (exce.getStackTrace().length < 1) || (projectPrefix == null) || (projectPrefix.length() < 1)) {
/*  13 */       return;
/*     */     }
/*  15 */     ExceptionFormat ex = new ExceptionFormat(exce, projectPrefix);
/*  16 */     if (!ex.clazz.contains("JTETestCases")) {
/*  17 */       System.err.println("**startException**, " + ex.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ExceptionFormat
/*     */   {
/*  23 */     private String exception = "";
/*  24 */     private String clazz = "";
/*  25 */     private String method = "";
/*  26 */     private String line = "";
/*  27 */     private String fileName = "";
/*  28 */     private String level = "";
/*  29 */     private String message = "";
/*  30 */     private List<ExceptionFormat> causesList = new ArrayList();
/*     */     private ExceptionFormat tc;
/*  32 */     private boolean exist = false;
/*     */     
/*     */     public ExceptionFormat(Throwable exce, String projectPrefix) {
/*  35 */       this(exce, projectPrefix, true);
/*     */     }
/*     */     
/*     */ 
/*     */     public ExceptionFormat(Throwable exce, String projectPrefix, boolean large)
/*     */     {
/*  41 */       int e = 0;
/*  42 */       boolean exist = false;
/*  43 */       while ((e < exce.getStackTrace().length) && (!exist)) {
/*  44 */         if (exce.getStackTrace()[e].getClassName().contains(projectPrefix)) {
/*  45 */           exist = true;
/*     */         } else
/*  47 */           e++;
/*     */       }
/*  49 */       if (!exist) {
/*  50 */         return;
/*     */       }
/*  52 */       this.exist = exist;
/*     */       
/*     */ 
/*  55 */       this.exception = exce.getClass().getName();
/*  56 */       setClazz(exce.getStackTrace()[e].getClassName());
/*  57 */       this.method = exce.getStackTrace()[e].getMethodName();
/*  58 */       setLine(exce.getStackTrace()[e].getLineNumber());
/*  59 */       this.fileName = exce.getStackTrace()[e].getFileName();
/*  60 */       this.level = e;
/*  61 */       if (large) {
/*  62 */         this.tc = new ExceptionFormat(exce, "JTETestCases", false);
/*     */         
/*  64 */         if (!this.tc.exist) {
/*  65 */           this.tc.clazz = "???";
/*  66 */           this.tc.method = "???";
/*  67 */           this.tc.line = "000";
/*     */         }
/*     */         
/*  70 */         Throwable cause = exce.getCause();
/*  71 */         while (cause != null) {
/*  72 */           ExceptionFormat ef = new ExceptionFormat(cause, projectPrefix, false);
/*  73 */           if (!ef.exist) {
/*     */             break;
/*     */           }
/*  76 */           getCausesList().add(ef);
/*  77 */           cause = cause.getCause();
/*     */         }
/*     */       }
/*     */       
/*  81 */       this.message = exce.getMessage();
/*  82 */       if (this.message == null)
/*  83 */         this.message = "";
/*  84 */       this.message = this.message.replace(",", "_COMMA_");
/*  85 */       this.message = this.message.replace(System.getProperty("line.separator"), "_EOL_");
/*  86 */       this.message = this.message.replace("\032", "_EOF_");
/*  87 */       char EOF = '\032';
/*  88 */       this.message = this.message.replace('\032', '_');
/*     */     }
/*     */     
/*     */     public ExceptionFormat(String logLine) {
/*  92 */       this(logLine, true);
/*     */     }
/*     */     
/*     */     public ExceptionFormat(String logLine, boolean large) {
/*     */       String[] fields;
/*  97 */       if (large) {
/*  98 */         logLine = logLine + " END.";
/*  99 */         String[] fields = logLine.split(", ");
/* 100 */         if (fields == null) {
/* 101 */           return;
/*     */         }
/* 103 */         this.exception = fields[1];
/* 104 */         setClazz(fields[2]);
/* 105 */         this.method = fields[3];
/* 106 */         setLine(fields[4]);
/* 107 */         this.fileName = fields[5];
/* 108 */         this.level = fields[6];
/* 109 */         this.tc = new ExceptionFormat(fields[7], false);
/*     */       } else {
/* 111 */         fields = logLine.split(": ");
/* 112 */         if (fields == null) {
/* 113 */           return;
/*     */         }
/* 115 */         setClazz(fields[0]);
/* 116 */         this.method = fields[1];
/* 117 */         setLine(fields[2]);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 122 */       int i = 8;
/* 123 */       while (i < fields.length - 1) {
/* 124 */         getCausesList().add(new ExceptionFormat(fields[i], false));
/* 125 */         i++;
/*     */       }
/*     */       
/*     */ 
/* 129 */       this.message = fields[(fields.length - 1)];
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 134 */       String st = this.exception + 
/* 135 */         ", " + getClazz() + 
/* 136 */         ", " + this.method + 
/* 137 */         ", " + getLine() + 
/* 138 */         ", " + this.fileName + 
/* 139 */         ", " + this.level;
/*     */       
/* 141 */       if (this.tc != null) {
/* 142 */         st = st + ", " + this.tc.toSmallString();
/*     */       }
/* 144 */       for (ExceptionFormat cause : getCausesList()) {
/* 145 */         st = st + ", " + cause.toSmallString();
/*     */       }
/* 147 */       st = st + ", " + this.message;
/* 148 */       return st;
/*     */     }
/*     */     
/*     */     public String toSmallString() {
/* 152 */       return 
/*     */       
/* 154 */         getClazz() + ": " + this.method + ": " + getLine();
/*     */     }
/*     */     
/*     */     public String getBugString() {
/* 158 */       String st = this.exception + 
/* 159 */         ", " + getClazz() + 
/* 160 */         ", " + this.method + 
/* 161 */         ", " + getLine() + 
/* 162 */         ", " + this.tc.toSmallString() + 
/* 163 */         ", " + this.fileName + 
/* 164 */         ", " + this.level;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 169 */       return st;
/*     */     }
/*     */     
/*     */     public String getClazz() {
/* 173 */       return this.clazz;
/*     */     }
/*     */     
/*     */     public void setClazz(String clazz) {
/* 177 */       this.clazz = clazz;
/*     */     }
/*     */     
/*     */     public String getLine() {
/* 181 */       return this.line;
/*     */     }
/*     */     
/*     */     public void setLine(String line) {
/* 185 */       this.line = line;
/*     */     }
/*     */     
/*     */     public List<ExceptionFormat> getCausesList() {
/* 189 */       return this.causesList;
/*     */     }
/*     */     
/*     */     public void setCausesList(List<ExceptionFormat> causesList) {
/* 193 */       this.causesList = causesList;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\ExceptionsFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */