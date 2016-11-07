/*    */ package csbst.testing;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*    */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Path<T>
/*    */   implements Comparable<Path>
/*    */ {
/*    */   private double difficultyCoefficient;
/* 14 */   private Vector<Branch> branches = new Vector();
/*    */   
/*    */   private T entryPoint;
/*    */   private MethodDeclaration topPoint;
/*    */   private TypeDeclaration clazz;
/*    */   
/*    */   public Path()
/*    */   {
/* 22 */     this.difficultyCoefficient = 1.0D;
/*    */   }
/*    */   
/*    */   public void add(Branch branch) {
/* 26 */     this.branches.add(0, branch);
/* 27 */     this.difficultyCoefficient += branch.getDC();
/*    */   }
/*    */   
/*    */   public boolean contains(int branch)
/*    */   {
/* 32 */     for (Branch b : this.branches)
/* 33 */       if (b.getID() == branch)
/* 34 */         return true;
/* 35 */     return false;
/*    */   }
/*    */   
/*    */   public void addAll(Path pathCaller) {
/* 39 */     this.branches.addAll(pathCaller.getBranches());
/* 40 */     this.difficultyCoefficient += pathCaller.getDC();
/*    */   }
/*    */   
/*    */   public Vector<Branch> getBranches() {
/* 44 */     return this.branches;
/*    */   }
/*    */   
/*    */   public T getEntryPoint() {
/* 48 */     return (T)this.entryPoint;
/*    */   }
/*    */   
/*    */   public void setEntryPoint(T entry) {
/* 52 */     this.entryPoint = entry;
/*    */   }
/*    */   
/*    */   public double getDC() {
/* 56 */     return this.difficultyCoefficient;
/*    */   }
/*    */   
/*    */   public int compareTo(Path p) {
/* 60 */     double diff = this.difficultyCoefficient - p.getDC();
/* 61 */     if (diff < 0.0D)
/* 62 */       return -1;
/* 63 */     if (diff > 0.0D) {
/* 64 */       return 1;
/*    */     }
/* 66 */     return 0;
/*    */   }
/*    */   
/*    */   public int size() {
/* 70 */     return this.branches.size();
/*    */   }
/*    */   
/*    */   public MethodDeclaration getTopPoint() {
/* 74 */     return this.topPoint;
/*    */   }
/*    */   
/*    */   public void setTopPoint(MethodDeclaration topPoint) {
/* 78 */     this.topPoint = topPoint;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\Path.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */