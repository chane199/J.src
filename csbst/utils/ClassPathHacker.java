/*    */ package csbst.utils;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.net.URLClassLoader;
/*    */ 
/*    */ public class ClassPathHacker
/*    */ {
/* 10 */   private static final Class[] parameters = { java.net.URL.class };
/*    */   
/*    */   public static void addFile(String s) throws IOException {
/* 13 */     File f = new File(s);
/* 14 */     addFile(f);
/*    */   }
/*    */   
/*    */   public static void addFile(File f) throws IOException {
/* 18 */     addURL(f.toURL());
/*    */   }
/*    */   
/*    */   public static void addURL(java.net.URL u)
/*    */     throws IOException
/*    */   {
/* 24 */     URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
/* 25 */     Class sysclass = URLClassLoader.class;
/*    */     try
/*    */     {
/* 28 */       Method method = sysclass.getDeclaredMethod("addURL", parameters);
/* 29 */       method.setAccessible(true);
/* 30 */       method.invoke(sysloader, new Object[] { u });
/*    */     } catch (Throwable t) {
/* 32 */       t.printStackTrace();
/* 33 */       throw new IOException("Error, could not add URL to system classloader");
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\ClassPathHacker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */