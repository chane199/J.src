/*     */ package csbst.testing;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Target
/*     */ {
/*     */   private int branch;
/*     */   private Vector<Path> paths;
/*     */   private Vector<Method> methodsMayReach;
/*     */   private Vector<Constructor> constructorsMayReach;
/*     */   private Vector<Method> externalMethodsMayReach;
/*     */   
/*     */   public Target(int b) throws IOException
/*     */   {
/*  19 */     this.branch = b;
/*  20 */     this.methodsMayReach = new Vector();
/*  21 */     this.constructorsMayReach = new Vector();
/*  22 */     this.externalMethodsMayReach = new Vector();
/*     */     
/*     */ 
/*  25 */     this.paths = JTE.getAccessiblePaths(b);
/*     */     
/*  27 */     generateReachability();
/*     */   }
/*     */   
/*     */   public int getBranch() {
/*  31 */     return this.branch;
/*     */   }
/*     */   
/*     */   public Vector<Method> getMethodsMayReach() {
/*  35 */     return this.methodsMayReach;
/*     */   }
/*     */   
/*     */   public Vector<Method> getExternalMethodsMayReach() {
/*  39 */     return this.externalMethodsMayReach;
/*     */   }
/*     */   
/*     */   public Vector<Constructor> getconstructorsMayReach() {
/*  43 */     return this.constructorsMayReach;
/*     */   }
/*     */   
/*     */   private void generateReachability() throws IOException
/*     */   {
/*  48 */     if ((this.paths == null) || (this.paths.size() == 0)) {
/*  49 */       return;
/*     */     }
/*     */     
/*  52 */     if (((((Path)this.paths.get(0)).getEntryPoint() instanceof Constructor)) && 
/*  53 */       (!((Constructor)((Path)this.paths.get(0)).getEntryPoint()).getDeclaringClass().equals(JTE.currentClassUnderTest.getClazz()))) {
/*  54 */       JTE.setClassUnderTest(((Constructor)((Path)this.paths.get(0)).getEntryPoint()).getDeclaringClass());
/*     */     }
/*     */     
/*     */ 
/*  58 */     if (((((Path)this.paths.get(0)).getEntryPoint() instanceof Method)) && 
/*  59 */       (!((Method)((Path)this.paths.get(0)).getEntryPoint()).getDeclaringClass().equals(JTE.currentClassUnderTest.getClazz()))) {
/*  60 */       JTE.setClassUnderTest(((Method)((Path)this.paths.get(0)).getEntryPoint()).getDeclaringClass());
/*     */     }
/*     */     
/*  63 */     for (Path p : this.paths) {
/*  64 */       if ((p.getEntryPoint() instanceof Constructor)) {
/*  65 */         if (!java.lang.reflect.Modifier.isProtected(((Constructor)p.getEntryPoint()).getModifiers())) {
/*  66 */           this.constructorsMayReach.add((Constructor)p.getEntryPoint());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
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
/*     */       }
/*  88 */       else if (p.getEntryPoint() != null) {
/*  89 */         this.methodsMayReach.add((Method)p.getEntryPoint());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getSugnature(Constructor md)
/*     */   {
/* 102 */     String nm = md.getName().replace('$', '.');
/* 103 */     String[] name = nm.split("\\.");
/*     */     
/* 105 */     return signature(name[(name.length - 1)], "", md.getParameterTypes());
/*     */   }
/*     */   
/* 108 */   private String getSugnature(Method md) { return signature(md.getName(), md.getReturnType().getName(), md.getParameterTypes()); }
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
/*     */   private String signature(String name, String returnType, Class[] parametersType)
/*     */   {
/* 126 */     String signature = "";
/* 127 */     signature = signature + returnType + " ";
/* 128 */     signature = signature + name + " (";
/* 129 */     for (int i = 0; i < parametersType.length; i++) {
/* 130 */       Class p = parametersType[i];
/* 131 */       signature = signature + p.getName();
/* 132 */       if (i < parametersType.length - 1) {
/* 133 */         signature = signature + ", ";
/*     */       }
/*     */     }
/*     */     
/* 137 */     signature = signature + ")";
/*     */     
/* 139 */     return signature;
/*     */   }
/*     */   
/*     */   public Iterator<Path> iterator() {
/* 143 */     return this.paths.iterator();
/*     */   }
/*     */   
/*     */   public int size() {
/* 147 */     return this.paths.size();
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\Target.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */