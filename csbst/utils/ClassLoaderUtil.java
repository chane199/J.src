/*     */ package csbst.utils;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class ClassLoaderUtil
/*     */ {
/*  45 */   private static Map abbreviationMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addAbbreviation(String primitive, String abbreviation)
/*     */   {
/*  54 */     abbreviationMap.put(primitive, abbreviation);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  61 */     addAbbreviation("int", "I");
/*  62 */     addAbbreviation("boolean", "Z");
/*  63 */     addAbbreviation("float", "F");
/*  64 */     addAbbreviation("long", "J");
/*  65 */     addAbbreviation("short", "S");
/*  66 */     addAbbreviation("byte", "B");
/*  67 */     addAbbreviation("double", "D");
/*  68 */     addAbbreviation("char", "C");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class getClass(ClassLoader classLoader, String className, boolean initialize)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     Class clazz;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     Class clazz;
/*     */     
/*     */ 
/*     */ 
/*  87 */     if (abbreviationMap.containsKey(className)) {
/*  88 */       String clsName = "[" + abbreviationMap.get(className);
/*  89 */       clazz = Class.forName(toCanonicalName(className), initialize, classLoader).getComponentType();
/*     */     }
/*     */     else {
/*  92 */       clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
/*     */     }
/*  94 */     return clazz;
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
/*     */   public static Class getClass(ClassLoader classLoader, String className)
/*     */     throws ClassNotFoundException
/*     */   {
/* 109 */     return getClass(classLoader, className, true);
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
/*     */   public static Class getClass(String className)
/*     */     throws ClassNotFoundException
/*     */   {
/* 123 */     if (className.equals("I"))
/* 124 */       return Integer.TYPE;
/* 125 */     if (className.equals("Z"))
/* 126 */       return Boolean.TYPE;
/* 127 */     if (className.equals("F"))
/* 128 */       return Float.TYPE;
/* 129 */     if (className.equals("J"))
/* 130 */       return Long.TYPE;
/* 131 */     if (className.equals("S"))
/* 132 */       return Short.TYPE;
/* 133 */     if (className.equals("B"))
/* 134 */       return Byte.TYPE;
/* 135 */     if (className.equals("D"))
/* 136 */       return Double.TYPE;
/* 137 */     if (className.equals("C")) {
/* 138 */       return Character.TYPE;
/*     */     }
/* 140 */     return getClass(className, true);
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
/*     */   public static Class getClass(String className, boolean initialize)
/*     */     throws ClassNotFoundException
/*     */   {
/* 155 */     ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
/* 156 */     ClassLoader currentCL = ClassLoaderUtil.class.getClassLoader();
/* 157 */     if (contextCL != null) {
/*     */       try {
/* 159 */         return getClass(contextCL, className, initialize);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     }
/*     */     
/*     */ 
/* 165 */     return getClass(currentCL, className, initialize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toCanonicalName(String className)
/*     */   {
/* 175 */     if (className == null) {
/* 176 */       throw new RuntimeException("Argument className was null.");
/*     */     }
/*     */     
/* 179 */     if (className.startsWith("L")) {
/* 180 */       className = className.substring(1, className.length());
/* 181 */       if (className.endsWith(";")) {
/* 182 */         className = className.substring(0, className.length() - 1);
/*     */       }
/*     */     }
/* 185 */     if (className.endsWith("[]")) {
/* 186 */       StringBuffer classNameBuffer = new StringBuffer();
/* 187 */       while (className.endsWith("[]")) {
/* 188 */         className = className.substring(0, className.length() - 2);
/* 189 */         classNameBuffer.append("[");
/*     */       }
/* 191 */       String abbreviation = (String)abbreviationMap.get(className);
/* 192 */       if (abbreviation != null) {
/* 193 */         classNameBuffer.append(abbreviation);
/*     */       }
/*     */       else {
/* 196 */         classNameBuffer.append("L").append(className).append(";");
/*     */       }
/* 198 */       className = classNameBuffer.toString();
/*     */     }
/*     */     
/* 201 */     return className;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\ClassLoaderUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */