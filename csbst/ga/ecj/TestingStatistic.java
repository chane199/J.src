/*     */ package csbst.ga.ecj;
/*     */ 
/*     */ import ec.EvolutionState;
/*     */ import ec.Individual;
/*     */ import ec.util.Output;
/*     */ 
/*     */ public class TestingStatistic extends ec.Statistics implements ec.steadystate.SteadyStateStatisticsForm
/*     */ {
/*     */   public static final String P_STATISTICS_FILE = "file";
/*     */   
/*     */   public Individual[] getBestSoFar()
/*     */   {
/*  13 */     return this.best_of_run;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  37 */   public int statisticslog = 0;
/*     */   public static final String P_COMPRESS = "gzip";
/*     */   
/*     */   public void setup(EvolutionState state, ec.util.Parameter base) {
/*  41 */     super.setup(state, base);
/*  42 */     java.io.File statisticsFile = state.parameters.getFile(
/*  43 */       base.push("file"), null);
/*     */     
/*  45 */     if (statisticsFile != null)
/*     */       try {
/*  47 */         this.statisticslog = state.output.addLog(statisticsFile, 
/*  48 */           !state.parameters.getBoolean(base.push("gzip"), null, false), 
/*  49 */           state.parameters.getBoolean(base.push("gzip"), null, false));
/*     */       }
/*     */       catch (java.io.IOException i)
/*     */       {
/*  53 */         state.output.fatal("An IOException occurred while trying to create the log " + statisticsFile + ":\n" + i);
/*     */       }
/*  55 */     this.doFull = state.parameters.getBoolean(base.push("gather-full"), null, false);
/*     */   }
/*     */   
/*     */ 
/*     */   public void preInitializationStatistics(EvolutionState state)
/*     */   {
/*  61 */     super.preInitializationStatistics(state);
/*     */     
/*     */ 
/*     */ 
/*  65 */     Runtime r = Runtime.getRuntime();
/*  66 */     this.lastTime = System.currentTimeMillis();
/*  67 */     this.lastUsage = (r.totalMemory() - r.freeMemory());
/*     */   }
/*     */   
/*     */ 
/*     */   public void postInitializationStatistics(EvolutionState state)
/*     */   {
/*  73 */     super.postInitializationStatistics(state);
/*     */     
/*     */ 
/*     */ 
/*  77 */     this.best_of_run = new Individual[state.population.subpops.length];
/*     */     
/*     */ 
/*  80 */     state.output.print("iteration,evaluations,runtime[s],fitness[MIN] \r", this.statisticslog);
/*     */   }
/*     */   
/*     */   public void preBreedingStatistics(EvolutionState state)
/*     */   {
/*  85 */     super.preBreedingStatistics(state);
/*     */   }
/*     */   
/*     */   public void postBreedingStatistics(EvolutionState state)
/*     */   {
/*  90 */     super.postBreedingStatistics(state);
/*  91 */     state.output.print(state.generation + 1 + ", ", this.statisticslog);
/*     */     
/*     */ 
/*  94 */     if (this.doFull)
/*     */     {
/*  96 */       Runtime r = Runtime.getRuntime();
/*  97 */       long curU = r.totalMemory() - r.freeMemory();
/*  98 */       state.output.print(System.currentTimeMillis() - this.lastTime + " ", this.statisticslog);
/*  99 */       state.output.print(curU - this.lastUsage + " ", this.statisticslog);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final String P_FULL = "gather-full";
/*     */   public boolean doFull;
/* 105 */   public void preEvaluationStatistics(EvolutionState state) { super.preEvaluationStatistics(state); }
/*     */   
/*     */   public Individual[] best_of_run;
/*     */   public long[] lengths;
/*     */   public long lastTime;
/*     */   public long lastUsage;
/*     */   protected void _postEvaluationStatistics(EvolutionState state) {
/* 112 */     state.output.print(state.generation + 1 + ",", this.statisticslog);
/* 113 */     state.output.print(300 * (state.generation + 1) + ",", this.statisticslog);
/*     */     
/*     */ 
/*     */ 
/* 117 */     Runtime r = Runtime.getRuntime();
/* 118 */     long curU = r.totalMemory() - r.freeMemory();
/* 119 */     state.output.print(System.currentTimeMillis() - this.lastTime + ",", this.statisticslog);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 124 */     long lengthPerGen = 0L;
/* 125 */     Individual[] best_i = new Individual[state.population.subpops.length];
/* 126 */     for (int x = 0; x < state.population.subpops.length; x++)
/*     */     {
/*     */ 
/* 129 */       double meanFitness = 0.0D;
/*     */       
/* 131 */       for (int y = 0; y < state.population.subpops[x].individuals.length; y++)
/*     */       {
/*     */ 
/* 134 */         if ((best_i[x] == null) || 
/* 135 */           (((TestCaseCandidate)state.population.subpops[x].individuals[y]).getFitness().betterThan(((TestCaseCandidate)best_i[x]).getFitness()))) {
/* 136 */           best_i[x] = state.population.subpops[x].individuals[y];
/*     */         }
/*     */         
/* 139 */         meanFitness += ((TestCaseCandidate)state.population.subpops[x].individuals[y]).getFitness().fitness();
/*     */       }
/*     */       
/*     */ 
/* 143 */       meanFitness /= state.population.subpops[x].individuals.length;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 148 */       if ((this.best_of_run[x] == null) || (((TestCaseCandidate)best_i[x]).getFitness().betterThan(((TestCaseCandidate)this.best_of_run[x]).getFitness()))) {
/* 149 */         this.best_of_run[x] = ((Individual)best_i[x].clone());
/*     */       }
/*     */     }
/* 152 */     Individual bestIndividual = null;
/* 153 */     for (int p = 0; p < best_i.length; p++) {
/* 154 */       if ((bestIndividual == null) || 
/* 155 */         (((TestCaseCandidate)best_i[p]).getFitness().betterThan(((TestCaseCandidate)bestIndividual).getFitness())))
/* 156 */         bestIndividual = best_i[p];
/*     */     }
/* 158 */     state.output.print(((TestCaseCandidate)bestIndividual).getFitness().fitness(), this.statisticslog);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void postEvaluationStatistics(EvolutionState state)
/*     */   {
/* 165 */     super.postEvaluationStatistics(state);
/* 166 */     _postEvaluationStatistics(state);
/* 167 */     state.output.println("", this.statisticslog);
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\ga\ecj\TestingStatistic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */