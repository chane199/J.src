/*     */ package csbst.ga.ecj;
/*     */ 
/*     */ import ec.Evaluator;
/*     */ import ec.Exchanger;
/*     */ import ec.Statistics;
/*     */ 
/*     */ public class TestingState extends ec.EvolutionState
/*     */ {
/*     */   public void run(int condition)
/*     */   {
/*  11 */     if (condition == 0)
/*     */     {
/*  13 */       startFresh();
/*     */     }
/*     */     else
/*     */     {
/*  17 */       startFromCheckpoint();
/*     */     }
/*     */     
/*     */ 
/*  21 */     int result = 2;
/*  22 */     while (result == 2)
/*     */     {
/*  24 */       result = evolve();
/*     */     }
/*     */     
/*  27 */     finish(result);
/*     */   }
/*     */   
/*     */ 
/*     */   public void startFresh()
/*     */   {
/*  33 */     setup(this, null);
/*     */     
/*     */ 
/*     */ 
/*  37 */     this.statistics.preInitializationStatistics(this);
/*  38 */     this.population = this.initializer.initialPopulation(this, 0);
/*  39 */     this.statistics.postInitializationStatistics(this);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  44 */     this.exchanger.initializeContacts(this);
/*  45 */     this.evaluator.initializeContacts(this);
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
/*     */   public int evolve()
/*     */   {
/*  58 */     this.statistics.preEvaluationStatistics(this);
/*  59 */     this.evaluator.evaluatePopulation(this);
/*  60 */     this.statistics.postEvaluationStatistics(this);
/*     */     
/*     */ 
/*  63 */     if ((this.evaluator.runComplete(this)) && (this.quitOnRunComplete))
/*     */     {
/*     */ 
/*  66 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*  70 */     if (this.generation == this.numGenerations - 1)
/*     */     {
/*  72 */       return 1;
/*     */     }
/*     */     
/*     */ 
/*  76 */     this.statistics.prePreBreedingExchangeStatistics(this);
/*  77 */     this.population = this.exchanger.preBreedingExchangePopulation(this);
/*  78 */     this.statistics.postPreBreedingExchangeStatistics(this);
/*     */     
/*  80 */     String exchangerWantsToShutdown = this.exchanger.runComplete(this);
/*  81 */     if (exchangerWantsToShutdown != null)
/*     */     {
/*  83 */       this.output.message(exchangerWantsToShutdown);
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
/*  96 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 100 */     this.statistics.preBreedingStatistics(this);
/*     */     
/* 102 */     this.population = this.breeder.breedPopulation(this);
/*     */     
/*     */ 
/* 105 */     this.statistics.postBreedingStatistics(this);
/*     */     
/*     */ 
/* 108 */     this.statistics.prePostBreedingExchangeStatistics(this);
/* 109 */     this.population = this.exchanger.postBreedingExchangePopulation(this);
/* 110 */     this.statistics.postPostBreedingExchangeStatistics(this);
/*     */     
/*     */ 
/* 113 */     this.generation += 1;
/* 114 */     if ((this.checkpoint) && (this.generation % this.checkpointModulo == 0))
/*     */     {
/* 116 */       this.output.message("Checkpointing");
/* 117 */       this.statistics.preCheckpointStatistics(this);
/* 118 */       ec.util.Checkpoint.setCheckpoint(this);
/* 119 */       this.statistics.postCheckpointStatistics(this);
/*     */     }
/*     */     
/* 122 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finish(int result)
/*     */   {
/* 132 */     this.statistics.finalStatistics(this, result);
/* 133 */     this.finisher.finishPopulation(this, result);
/* 134 */     this.exchanger.closeContacts(this, result);
/* 135 */     this.evaluator.closeContacts(this, result);
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\ga\ecj\TestingState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */