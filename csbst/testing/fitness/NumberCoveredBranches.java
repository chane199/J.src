/*    */ package csbst.testing.fitness;
/*    */ 
/*    */ import csbst.ga.ecj.TestCaseCandidate;
/*    */ import csbst.testing.JTE;
/*    */ import csbst.testing.Target;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ public class NumberCoveredBranches
/*    */   extends TestingFitness
/*    */ {
/*    */   public static void maintainPathTrace(int branch, int className) {}
/*    */   
/*    */   public static void maintainPathTrace(int branch, String className)
/*    */   {
/* 16 */     if (!JTE.className.equals(className)) {
/* 17 */       return;
/*    */     }
/* 19 */     if (underEvaluationChromosome != null) {
/* 20 */       underEvaluationChromosome.getCoveredBranches().add(Integer.valueOf(branch));
/*    */     }
/*    */     
/* 23 */     if ((!JTE.allCoveredBranches.contains(Integer.valueOf(branch))) && 
/* 24 */       (underEvaluationChromosome != null))
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 30 */       underEvaluationChromosome.setIsTestData(true);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public double fitness()
/*    */   {
/* 40 */     return 1.0D / (1.0D + this.distance);
/*    */   }
/*    */   
/*    */   public boolean isIdealFitness()
/*    */   {
/* 45 */     return JTE.allCoveredBranches.contains(Integer.valueOf(JTE.currentTarget.getBranch()));
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\fitness\NumberCoveredBranches.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */