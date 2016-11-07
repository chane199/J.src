/*    */ package csbst.utils;
/*    */ 
/*    */ import csbst.generators.dynamic.InstanceGenerator;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.reflections.Reflections;
/*    */ import org.reflections.scanners.SubTypesScanner;
/*    */ import org.reflections.util.ConfigurationBuilder;
/*    */ import org.reflections.util.FilterBuilder;
/*    */ 
/*    */ public class ClassFinder
/*    */ {
/*    */   private static Collection<String> packages;
/*    */   
/*    */   public static List<Class> getFQNs(String simpleName)
/*    */   {
/* 24 */     if (packages == null) {
/* 25 */       packages = getPackages();
/*    */     }
/*    */     
/* 28 */     Set<Class<?>> allClasses = InstanceGenerator.reflections.getSubTypesOf(Object.class);
/* 29 */     List<Class> fqns = new ArrayList();
/* 30 */     for (Class<?> c : allClasses) {
/* 31 */       System.out.println(c);
/* 32 */       System.out.println(c.getSimpleName().toString() + "=" + simpleName + "*");
/* 33 */       System.out.println(c.getSimpleName().toString().equals(simpleName));
/* 34 */       if (c.getSimpleName().toString().equals(simpleName)) {
/* 35 */         fqns.add(c);
/*    */       }
/*    */     }
/* 38 */     if (fqns.size() < 1) {
/* 39 */       Reflections reflections1 = (Reflections)InstanceGenerator.package2Reflections.get("java.lang");
/* 40 */       File java_classes; if (reflections1 == null) {
/* 41 */         File java_home = new File(System.getProperty("java.home"));
/* 42 */         java_classes = new File(java_home.getParent() + "/Classes/classes.jar");
/*    */         
/* 44 */         URL[] urls = new URL[1];
/* 45 */         for (int i = 0; i < 1; i++) {
/*    */           try {
/* 47 */             urls[i] = java_classes.toURL();
/*    */           }
/*    */           catch (MalformedURLException e) {
/* 50 */             e.printStackTrace();
/*    */           }
/*    */         }
/*    */         
/* 54 */         reflections1 = new Reflections(new ConfigurationBuilder()
/* 55 */           .setScanners(new org.reflections.scanners.Scanner[] {new SubTypesScanner(false) })
/* 56 */           .filterInputsBy(new FilterBuilder().includePackage("java.lang"))
/* 57 */           .setUrls(urls));
/* 58 */         InstanceGenerator.package2Reflections.put("java.lang", reflections1);
/*    */       }
/* 60 */       allClasses = reflections1.getSubTypesOf(Object.class);
/*    */       
/* 62 */       for (Object c : allClasses) {
/* 63 */         if (((Class)c).getSimpleName().toString().equals(simpleName))
/* 64 */           fqns.add(c);
/*    */       }
/*    */     }
/* 67 */     return fqns;
/*    */   }
/*    */   
/*    */   public static Collection<String> getPackages() {
/* 71 */     Set<String> packages = new java.util.HashSet();
/* 72 */     Package[] arrayOfPackage; int j = (arrayOfPackage = Package.getPackages()).length; for (int i = 0; i < j; i++) { Package aPackage = arrayOfPackage[i];
/* 73 */       packages.add(aPackage.getName());
/*    */     }
/* 75 */     return packages;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\ClassFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */