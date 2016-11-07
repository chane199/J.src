/*     */ package csbst.testing.fitness;
/*     */ 
/*     */ import csbst.ga.ecj.TestCaseCandidate;
/*     */ import ec.Fitness;
/*     */ import ec.util.Parameter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TestingFitness
/*     */   extends Fitness
/*     */ {
/*  13 */   public static TestingFitness underEvaluationFitness = null;
/*     */   public static TestCaseCandidate underEvaluationChromosome;
/*  15 */   public static int evaluations = 0;
/*     */   
/*     */   protected double distance;
/*     */   
/*     */   public void initialize()
/*     */   {
/*  21 */     underEvaluationFitness = this;
/*  22 */     this.distance = 0.0D;
/*     */   }
/*     */   
/*     */   public void update() {
/*  26 */     underEvaluationFitness = null;
/*     */   }
/*     */   
/*     */   public void evaluate(TestCaseCandidate ch) {
/*  30 */     evaluations += 1;
/*     */     try
/*     */     {
/*  33 */       underEvaluationChromosome = ch;
/*  34 */       initialize();
/*  35 */       ch.execute();
/*  36 */       update();
/*  37 */       underEvaluationChromosome = null;
/*     */     } catch (IllegalArgumentException e) {
/*  39 */       e.printStackTrace();
/*     */     } catch (IllegalAccessException e) {
/*  41 */       e.printStackTrace();
/*     */     } catch (InvocationTargetException e) {
/*  43 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TestingFitness()
/*     */   {
/*  51 */     this.distance = 0.0D;
/*     */   }
/*     */   
/*     */   public double getDistance() {
/*  55 */     return this.distance;
/*     */   }
/*     */   
/*     */   public void setDistance(double dis) {
/*  59 */     this.distance = dis;
/*     */   }
/*     */   
/*     */   public boolean equals(TestingFitness other) {
/*  63 */     return this.distance == other.getDistance();
/*     */   }
/*     */   
/*     */   public double getNormalizedBD() {
/*  67 */     return this.distance / (this.distance + 1.0D);
/*     */   }
/*     */   
/*     */   public double getNormalizedBDLog() {
/*  71 */     return 1.0D - 1.0D / (1.0D + Math.log(1.0D + this.distance));
/*     */   }
/*     */   
/*     */   public double getNormalizedBDPow()
/*     */   {
/*  76 */     return 1.0D - Math.pow(1.001D, -1.0D * this.distance);
/*     */   }
/*     */   
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/*  82 */     TestingFitness fit = (TestingFitness)obj;
/*  83 */     if (fitness() > fit.fitness())
/*  84 */       return -1;
/*  85 */     if (fitness() < fit.fitness()) {
/*  86 */       return 1;
/*     */     }
/*  88 */     return 0;
/*     */   }
/*     */   
/*     */   public double fitness()
/*     */   {
/*  93 */     return getNormalizedBDLog();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean betterThan(Fitness _fitness)
/*     */   {
/* 103 */     return compareTo(_fitness) == 1;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 108 */     String str = new String();
/* 109 */     str = fitness();
/* 110 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equivalentTo(Fitness _fitness)
/*     */   {
/* 116 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public Parameter defaultBase()
/*     */   {
/* 122 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\fitness\TestingFitness.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */