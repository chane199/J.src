/*     */ package csbst.ga.ecj;
/*     */ 
/*     */ import csbst.testing.fitness.TestingFitness;
/*     */ import ec.EvolutionState;
/*     */ import ec.Fitness;
/*     */ import ec.Individual;
/*     */ import ec.Population;
/*     */ import ec.Subpopulation;
/*     */ import ec.util.MersenneTwisterFast;
/*     */ import ec.util.RandomChoice;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TestingProblem extends ec.Problem
/*     */ {
/*     */   public int[][] paramsBranchesUseMatrix;
/*     */   
/*     */   public void preprocessPopulation(EvolutionState state, Population pop, boolean countVictoriesOnly)
/*     */   {
/*  24 */     if ((state.generation % 20 == 0) && (state.generation != 0)) {
/*  25 */       for (int i = 0; i < pop.subpops.length / 2; i++) {
/*  26 */         int x = state.random[0].nextInt(pop.subpops.length);
/*  27 */         int y = state.random[0].nextInt(pop.subpops.length);
/*  28 */         int minLen = pop.subpops[x].individuals.length > pop.subpops[y].individuals.length ? pop.subpops[y].individuals.length : pop.subpops[x].individuals.length;
/*  29 */         int p = minLen / 10;
/*  30 */         if (x != y) {
/*  31 */           for (int k = 0; k < p; k++) {
/*  32 */             int z = state.random[0].nextInt(minLen);
/*  33 */             Individual tmp = (Individual)pop.subpops[x].individuals[z].clone();
/*  34 */             pop.subpops[x].individuals[z] = ((Individual)pop.subpops[y].individuals[z].clone());
/*  35 */             pop.subpops[y].individuals[z] = tmp;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  43 */     if ((state.generation % 4 == 0) && (state.generation != 0)) {
/*  44 */       double[] lR = new double[pop.subpops.length];
/*  45 */       double[] meanfitnesses = new double[pop.subpops.length];
/*     */       
/*  47 */       for (int p = 0; p < pop.subpops.length; p++) {
/*  48 */         meanfitnesses[p] = pop.subpops[p].getMeanFitness();
/*     */       }
/*  50 */       int[] indices = new int[pop.subpops.length];
/*     */       
/*  52 */       RandomChoice.linearRanking(1.7D, meanfitnesses, lR, indices);
/*  53 */       meanfitnesses = lR;
/*     */       
/*  55 */       RandomChoice.organizeDistribution(meanfitnesses, true);
/*     */       
/*  57 */       int x = indices[RandomChoice.pickFromDistribution(meanfitnesses, state.random[0].nextDouble())];
/*  58 */       int y = indices[RandomChoice.pickFromDistribution(meanfitnesses, state.random[0].nextDouble())];
/*  59 */       if (pop.subpops[x].getProg() < pop.subpops[y].getProg()) {
/*  60 */         int tmp = x;
/*  61 */         x = y;
/*  62 */         y = x;
/*     */       }
/*  64 */       int minLen = pop.subpops[x].individuals.length > pop.subpops[y].individuals.length ? pop.subpops[y].individuals.length : pop.subpops[x].individuals.length;
/*  65 */       int p = minLen / 10;
/*  66 */       if (p < 1) p = 1;
/*  67 */       p = state.random[0].nextInt(p) + 1;
/*  68 */       if (x != y) {
/*  69 */         for (int k = 0; k < p; k++) {
/*  70 */           lR = new double[pop.subpops[y].individuals.length];
/*  71 */           meanfitnesses = new double[pop.subpops[y].individuals.length];
/*  72 */           for (int i = 0; i < pop.subpops[y].individuals.length; i++)
/*  73 */             meanfitnesses[i] = pop.subpops[y].individuals[i].fitness.fitness();
/*  74 */           indices = new int[pop.subpops[y].individuals.length];
/*  75 */           RandomChoice.linearRanking(1.7D, meanfitnesses, lR, indices);
/*  76 */           meanfitnesses = lR;
/*  77 */           RandomChoice.organizeDistribution(meanfitnesses, true);
/*  78 */           int z = indices[(indices.length - RandomChoice.pickFromDistribution(meanfitnesses, state.random[0].nextDouble()) - 1)];
/*     */           
/*  80 */           if (pop.subpops[y].individuals.length > 5) {
/*  81 */             Individual[] indsX = new Individual[pop.subpops[x].individuals.length + 1];
/*  82 */             Individual[] indsY = new Individual[pop.subpops[y].individuals.length - 1];
/*     */             
/*  84 */             for (int j = 0; j < pop.subpops[x].individuals.length; j++) {
/*  85 */               indsX[j] = pop.subpops[x].individuals[j];
/*     */             }
/*  87 */             indsX[pop.subpops[x].individuals.length] = pop.subpops[y].individuals[z];
/*     */             
/*     */ 
/*  90 */             pop.subpops[x].individuals = indsX;
/*     */             
/*  92 */             for (int j = 0; j < z; j++) {
/*  93 */               indsY[j] = pop.subpops[y].individuals[j];
/*     */             }
/*  95 */             for (int j = z + 1; j < pop.subpops[y].individuals.length; j++) {
/*  96 */               indsY[(j - 1)] = pop.subpops[y].individuals[j];
/*     */             }
/*     */             
/*  99 */             pop.subpops[y].individuals = indsY;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void postprocessPopulation(EvolutionState state, Population pop, boolean countVictoriesOnly)
/*     */   {
/* 109 */     Map<Double, Integer> avgFitness = new HashMap();
/* 110 */     for (int i = 0; i < pop.subpops.length; i++) {
/* 111 */       double meanFitness = 0.0D;
/* 112 */       for (int j = 0; j < pop.subpops[i].individuals.length; j++)
/*     */       {
/* 114 */         pop.subpops[i].individuals[j].evaluated = true;
/*     */         
/* 116 */         meanFitness += ((TestCaseCandidate)state.population.subpops[i].individuals[j]).getFitness().fitness();
/*     */       }
/*     */       
/* 119 */       meanFitness /= state.population.subpops[i].individuals.length;
/* 120 */       avgFitness.put(new Double(meanFitness), new Integer(i));
/* 121 */       state.population.subpops[i].setMeanFitness(meanFitness);
/*     */     }
/*     */     
/* 124 */     Iterator it = avgFitness.entrySet().iterator();
/* 125 */     int i = 0;
/* 126 */     while (it.hasNext()) {
/* 127 */       Map.Entry pairs = (Map.Entry)it.next();
/* 128 */       pop.subpops[((Integer)pairs.getValue()).intValue()].setProg(i + 1);
/* 129 */       i++;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void evaluate(EvolutionState state, Individual ind, int subpopulation, int threadnum)
/*     */     throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
/*     */   {
/* 137 */     if (!(ind instanceof TestCaseCandidate)) {
/* 138 */       state.output.fatal("The individuals in the testing problem should be Chromosome.");
/*     */     }
/* 140 */     ((TestCaseCandidate)ind).getFitness().evaluate((TestCaseCandidate)ind);
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\ga\ecj\TestingProblem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */