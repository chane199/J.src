/*    */ package csbst.utils;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.net.URLClassLoader;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JTEClassLoader
/*    */   extends URLClassLoader
/*    */ {
/* 21 */   public static Map<String, String> class2Binary = new HashMap();
/*    */   
/*    */   public JTEClassLoader(Map<String, String> c2b, URL[] urls) {
/* 24 */     super(urls);
/* 25 */     if (class2Binary == null)
/* 26 */       class2Binary = c2b;
/* 27 */     if (class2Binary == null) {
/* 28 */       class2Binary = new HashMap();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Class<?> loadClass(String name)
/*    */     throws ClassNotFoundException
/*    */   {
/* 64 */     if (name.equals("I"))
/* 65 */       return Integer.TYPE;
/* 66 */     if (name.equals("Z"))
/* 67 */       return Boolean.TYPE;
/* 68 */     if (name.equals("F"))
/* 69 */       return Float.TYPE;
/* 70 */     if (name.equals("J"))
/* 71 */       return Long.TYPE;
/* 72 */     if (name.equals("S"))
/* 73 */       return Short.TYPE;
/* 74 */     if (name.equals("B"))
/* 75 */       return Byte.TYPE;
/* 76 */     if (name.equals("D"))
/* 77 */       return Double.TYPE;
/* 78 */     if (name.equals("C")) {
/* 79 */       return Character.TYPE;
/*    */     }
/*    */     
/* 82 */     return super.loadClass(name);
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\JTEClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */