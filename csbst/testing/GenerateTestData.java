package csbst.testing;
/*    */ /*    */ import csbst.utils.ClassPathHacker;
/*    */ import java.io.File;
/*    */ import java.net.URL;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GenerateTestData
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 16 */     loadClassPath(args);
/*    */   }
/*    */   
/*    */   private static void loadClassPath(String[] args) throws Exception {
/* 20 */     List<URL> pathList = new ArrayList();
/*    */     
/* 22 */     String prgPath = GenerateTestData.class.getResource("GenerateTestData.class").getPath();
/* 23 */     int index = prgPath.indexOf(':');
/* 24 */     if (index > 0)
/* 25 */       prgPath = prgPath.substring(prgPath.indexOf(':') + 1);
/* 26 */     index = prgPath.lastIndexOf('/');
/* 27 */     if (index > 0) {
/* 28 */       prgPath = prgPath.substring(0, index);
/*    */     }
/* 30 */     if (prgPath.endsWith("!")) {
/* 31 */       prgPath = prgPath.substring(0, prgPath.length() - 1);
/*    */     }
/* 33 */     if (prgPath.endsWith(File.separator + "bin")) {
/* 34 */       prgPath = prgPath.substring(0, prgPath.length() - 4);
/*    */     }
/* 36 */     File prj = new File(prgPath);
/* 37 */     pathList.add(prj.toURL());
/*    */     
/*    */ 
/* 40 */     if (prgPath.endsWith(".jar")) {
/* 41 */       index = prgPath.lastIndexOf('/');
/* 42 */       if (index > 0) {
/* 43 */         prgPath = prgPath.substring(0, index);
/* 44 */         prj = new File(prgPath);
/*    */       }
/*    */     }
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
/* 57 */     File bin = new File(prj.getAbsolutePath() + File.separator + "bin");
/* 58 */     pathList.add(bin.toURL());
/*    */     
/* 60 */     File lib = new File(prj.getAbsolutePath() + File.separator + "lib");
/* 61 */     if (lib.exists()) {
/* 62 */       pathList.addAll(getJarListInLibDir(lib));
/*    */     }
/*    */     
/* 65 */     URL[] classPath = new URL[pathList.size()];
/* 66 */     classPath = (URL[])pathList.toArray(classPath);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 71 */     for (int i1 = 0; i1 < pathList.size(); i1++) {
/* 72 */       ClassPathHacker.addFile(((URL)pathList.get(i1)).getPath());
/*    */     }
/* 74 */     JTE.classPathList = pathList;
/* 75 */     JTE.main(args);
/*    */   }
/*    */   
/*    */ 
/*    */   public static List<URL> getJarListInLibDir(File lib)
/*    */     throws Exception
/*    */   {
/* 82 */     List<URL> pathList = new ArrayList();
/*    */     
/* 84 */     File[] files = lib.listFiles();
/* 85 */     for (int i = 0; i < files.length; i++) {
/* 86 */       if (files[i].getName().endsWith(".jar")) {
/* 87 */         pathList.add(files[i].toURL());
/* 88 */       } else if (files[i].isDirectory()) {
/* 89 */         pathList.addAll(getJarListInLibDir(files[i]));
/*    */       }
/*    */     }
/* 92 */     return pathList;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\GenerateTestData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */