/*    */ package csbst.ga.ecj;
/*    */ 
/*    */ import ec.Individual;
/*    */ 
/*    */ public class TestingMutator extends ec.BreedingPipeline
/*    */ {
/*    */   public static final String P_OURMUTATION = "testing-mutation";
/*    */   public static final int NUM_SOURCES = 1;
/*    */   
/*    */   public ec.util.Parameter defaultBase() {
/* 11 */     return ec.vector.VectorDefaults.base().push("testing-mutation");
/*    */   }
/*    */   
/*    */   public int numSources() {
/* 15 */     return 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int produce(int min, int max, int start, int subpopulation, Individual[] inds, ec.EvolutionState state, int thread)
/*    */   {
/* 27 */     int n = this.sources[0].produce(min, max, start, subpopulation, inds, state, thread);
/*    */     
/*    */ 
/*    */ 
/* 31 */     if (!state.random[thread].nextBoolean(this.likelihood)) {
/* 32 */       return reproduce(n, start, subpopulation, inds, state, thread, false);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 38 */     if (!(this.sources[0] instanceof ec.BreedingPipeline)) {
/* 39 */       for (int q = start; q < n + start; q++) {
/* 40 */         inds[q] = ((Individual)inds[q].clone());
/*    */       }
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 48 */     if (!(inds[start] instanceof TestCaseCandidate))
/* 49 */       state.output.fatal("OurMutatorPipeline didn't get an Chromosome.The offending individual is in subpopulation " + 
/* 50 */         subpopulation + " and it's:" + inds[start]);
/* 51 */     ChromosomeSpecies species = (ChromosomeSpecies)inds[start].species;
/*    */     
/*    */ 
/*    */ 
/* 55 */     for (int q = start; q < n + start; q++)
/*    */     {
/* 57 */       TestCaseCandidate i = (TestCaseCandidate)inds[q];
/* 58 */       double mutp = 1.0D / i.getGenes().size();
/*    */       
/* 60 */       double[] propa = new double[i.getGenes().size()];
/* 61 */       for (int x = 0; x < i.getGenes().size(); x++)
/* 62 */         propa[x] = mutp;
/* 63 */       for (int x = 0; x < i.getGenes().size(); x++) {
/* 64 */         if (state.random[thread].nextBoolean(propa[x]))
/*    */         {
/*    */ 
/* 67 */           ((csbst.generators.AbsractGenerator)i.getGenes().get(x)).mutate();
/*    */         }
/*    */       }
/*    */       
/* 71 */       i.evaluated = false;
/*    */     }
/*    */     
/* 74 */     return n;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\ga\ecj\TestingMutator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */