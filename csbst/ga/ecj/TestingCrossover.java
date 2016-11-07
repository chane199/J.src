/*     */ package csbst.ga.ecj;
/*     */ 
/*     */ import ec.BreedingPipeline;
/*     */ import ec.BreedingSource;
/*     */ import ec.EvolutionState;
/*     */ import ec.Individual;
/*     */ import ec.util.MersenneTwisterFast;
/*     */ import ec.util.Parameter;
/*     */ import ec.util.ParameterDatabase;
/*     */ import ec.vector.VectorDefaults;
/*     */ import ec.vector.VectorIndividual;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TestingCrossover
/*     */   extends BreedingPipeline
/*     */ {
/*     */   public static final String P_TOSS = "toss";
/*     */   public static final String P_CROSSOVER = "xover";
/*     */   public static final int NUM_SOURCES = 2;
/*     */   public boolean tossSecondParent;
/*  26 */   VectorIndividual[] parents = new TestCaseCandidate[2];
/*  27 */   public Parameter defaultBase() { return VectorDefaults.base().push("xover"); }
/*     */   
/*     */   public int numSources() {
/*  30 */     return 2;
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  34 */     TestingCrossover c = (TestingCrossover)super.clone();
/*     */     
/*     */ 
/*  37 */     c.parents = ((TestCaseCandidate[])this.parents.clone());
/*     */     
/*  39 */     return c;
/*     */   }
/*     */   
/*     */   public void setup(EvolutionState state, Parameter base)
/*     */   {
/*  44 */     super.setup(state, base);
/*  45 */     Parameter def = defaultBase();
/*  46 */     this.tossSecondParent = state.parameters.getBoolean(base.push("toss"), 
/*  47 */       def.push("toss"), false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int typicalIndsProduced()
/*     */   {
/*  54 */     return this.tossSecondParent ? minChildProduction() : minChildProduction() * 2;
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
/*     */   public int produce(int min, int max, int start, int subpopulation, Individual[] inds, EvolutionState state, int thread)
/*     */   {
/*  67 */     int n = typicalIndsProduced();
/*  68 */     if (n < min) n = min;
/*  69 */     if (n > max) { n = max;
/*     */     }
/*     */     
/*  72 */     if (!state.random[thread].nextBoolean(this.likelihood)) {
/*  73 */       return reproduce(n, start, subpopulation, inds, state, thread, true);
/*     */     }
/*  75 */     for (int q = start; q < n + start;)
/*     */     {
/*     */ 
/*  78 */       if (this.sources[0] == this.sources[1])
/*     */       {
/*  80 */         this.sources[0].produce(2, 2, 0, subpopulation, this.parents, state, thread);
/*  81 */         if (!(this.sources[0] instanceof BreedingPipeline))
/*     */         {
/*  83 */           this.parents[0] = ((TestCaseCandidate)this.parents[0].clone());
/*  84 */           this.parents[1] = ((TestCaseCandidate)this.parents[1].clone());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  89 */         this.sources[0].produce(1, 1, 0, subpopulation, this.parents, state, thread);
/*  90 */         this.sources[1].produce(1, 1, 1, subpopulation, this.parents, state, thread);
/*  91 */         if (!(this.sources[0] instanceof BreedingPipeline))
/*  92 */           this.parents[0] = ((VectorIndividual)this.parents[0].clone());
/*  93 */         if (!(this.sources[1] instanceof BreedingPipeline)) {
/*  94 */           this.parents[1] = ((VectorIndividual)this.parents[1].clone());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 104 */       this.parents[0].defaultCrossover(state, thread, this.parents[1]);
/* 105 */       this.parents[0].evaluated = false;
/* 106 */       this.parents[1].evaluated = false;
/*     */       
/*     */ 
/* 109 */       inds[q] = this.parents[0];
/* 110 */       q++;
/* 111 */       if ((q < n + start) && (!this.tossSecondParent))
/*     */       {
/* 113 */         inds[q] = this.parents[1];
/* 114 */         q++;
/*     */       }
/*     */     }
/* 117 */     return n;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\ga\ecj\TestingCrossover.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */