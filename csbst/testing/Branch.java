/*    */ package csbst.testing;
/*    */ 
/*    */ public class Branch {
/*    */   private int branchID;
/*    */   private double difficultyCoefficient;
/*    */   private int level;
/*    */   
/*    */   public Branch(int branch, double dc, int l) {
/*  9 */     this.branchID = branch;
/* 10 */     this.difficultyCoefficient = dc;
/* 11 */     this.level = l;
/*    */   }
/*    */   
/*    */   public Branch(int branch) {
/* 15 */     this.branchID = branch;
/*    */   }
/*    */   
/*    */   public double getDC() {
/* 19 */     return this.difficultyCoefficient;
/*    */   }
/*    */   
/*    */   public int getID()
/*    */   {
/* 24 */     return this.branchID;
/*    */   }
/*    */   
/*    */   public double getLevel() {
/* 28 */     return this.level;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\Branch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */