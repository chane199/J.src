/*    */ package csbst.testing;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.Set;
/*    */ import java.util.Vector;
/*    */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*    */ 
/*    */ public class DataMember
/*    */ {
/*    */   private IVariableBinding key;
/*    */   private boolean isAccessible;
/*    */   private Vector<Method> methodsMayTransforme;
/*    */   private Vector<Constructor> constructorsMayTransforme;
/*    */   
/*    */   public DataMember(IVariableBinding key) throws IOException
/*    */   {
/* 20 */     this.key = key;
/* 21 */     Set<Integer> branchTransformers = (Set)JTE.dataMemberUseAnalyser.getDataMemberBranchTransformersMap().get(key);
/* 22 */     Vector<Path> dmPaths = new Vector();
/* 23 */     for (Integer b : branchTransformers) {
/* 24 */       dmPaths.addAll(JTE.getAccessiblePaths(b.intValue()));
/*    */     }
/*    */     
/* 27 */     this.methodsMayTransforme = new Vector();
/* 28 */     this.constructorsMayTransforme = new Vector();
/* 29 */     for (Path p : dmPaths)
/*    */     {
/* 31 */       if ((p.getEntryPoint() instanceof Method)) {
/* 32 */         if (((Method)p.getEntryPoint()).getDeclaringClass().equals(key.getDeclaringClass())) {
/* 33 */           this.methodsMayTransforme.add((Method)p.getEntryPoint());
/*    */         }
/*    */       }
/* 36 */       else if ((p.getEntryPoint() != null) && ((p.getEntryPoint() instanceof Constructor)) && 
/* 37 */         (((Constructor)p.getEntryPoint()).getDeclaringClass().equals(key.getDeclaringClass()))) {
/* 38 */         this.constructorsMayTransforme.add((Constructor)p.getEntryPoint());
/*    */       }
/*    */     }
/* 41 */     this.isAccessible = ((!Modifier.isPrivate(key.getModifiers())) && (!Modifier.isProtected(key.getModifiers())));
/*    */   }
/*    */   
/*    */   public IVariableBinding getKey() {
/* 45 */     return this.key;
/*    */   }
/*    */   
/* 48 */   public Vector<Method> getMethodTransformers() { return this.methodsMayTransforme; }
/*    */   
/*    */   public Vector<Constructor> getConstructorTransformers()
/*    */   {
/* 52 */     return this.constructorsMayTransforme;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\DataMember.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */