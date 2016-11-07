/*     */ package csbst.ga.ecj;
/*     */ 
/*     */ import ec.Evaluator;
/*     */ import ec.EvolutionState;
/*     */ import ec.Individual;
/*     */ import ec.Population;
/*     */ import ec.Problem;
/*     */ import ec.Subpopulation;
/*     */ import ec.util.Output;
/*     */ import ec.util.Parameter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ 
/*     */ public class TestingEvaluator extends Evaluator
/*     */ {
/*     */   public void setup(EvolutionState state, Parameter base)
/*     */   {
/*  17 */     super.setup(state, base);
/*  18 */     if (!(this.p_problem instanceof TestingProblem)) {
/*  19 */       state.output.fatal(getClass() + " used, but the Problem is not of SimpleProblemForm", 
/*  20 */         base.push("problem"));
/*     */     }
/*     */   }
/*     */   
/*     */   public void evaluatePopulation(EvolutionState state) {
/*  25 */     ((TestingProblem)this.p_problem).preprocessPopulation(state, state.population, false);
/*  26 */     int[][] numinds = 
/*  27 */       new int[state.evalthreads][state.population.subpops.length];
/*  28 */     int[][] from = 
/*  29 */       new int[state.evalthreads][state.population.subpops.length];
/*     */     
/*  31 */     for (int y = 0; y < state.evalthreads; y++) {
/*  32 */       for (int x = 0; x < state.population.subpops.length; x++)
/*     */       {
/*     */ 
/*  35 */         if (y < state.evalthreads - 1) {
/*  36 */           numinds[y][x] = 
/*  37 */             (state.population.subpops[x].individuals.length / 
/*  38 */             state.evalthreads);
/*     */         } else {
/*  40 */           numinds[y][x] = 
/*  41 */             (state.population.subpops[x].individuals.length / 
/*  42 */             state.evalthreads + (
/*     */             
/*  44 */             state.population.subpops[x].individuals.length - 
/*  45 */             state.population.subpops[x].individuals.length / 
/*  46 */             state.evalthreads * 
/*  47 */             state.evalthreads));
/*     */         }
/*     */         
/*  50 */         from[y][x] = 
/*  51 */           (state.population.subpops[x].individuals.length / 
/*  52 */           state.evalthreads * y);
/*     */       }
/*     */     }
/*  55 */     if (state.evalthreads == 1) {
/*  56 */       evalPopChunk(state, numinds[0], from[0], 0, (TestingProblem)this.p_problem.clone());
/*     */     }
/*     */     else
/*     */     {
/*  60 */       Thread[] t = new Thread[state.evalthreads];
/*     */       
/*     */ 
/*  63 */       for (int y = 0; y < state.evalthreads; y++)
/*     */       {
/*  65 */         SimpleEvaluatorThread r = new SimpleEvaluatorThread();
/*  66 */         r.threadnum = y;
/*  67 */         r.numinds = numinds[y];
/*  68 */         r.from = from[y];
/*  69 */         r.me = this;
/*  70 */         r.state = state;
/*  71 */         r.p = ((TestingProblem)this.p_problem.clone());
/*  72 */         t[y] = new Thread(r);
/*  73 */         t[y].start();
/*     */       }
/*     */       
/*     */ 
/*  77 */       for (int y = 0; y < state.evalthreads; y++) {
/*     */         try {
/*  79 */           t[y].join();
/*     */         }
/*     */         catch (InterruptedException e)
/*     */         {
/*  83 */           state.output.fatal("Whoa! The main evaluation thread got interrupted!  Dying...");
/*     */         }
/*     */       }
/*     */     }
/*  87 */     ((TestingProblem)this.p_problem).postprocessPopulation(state, state.population, false);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void evalPopChunk(EvolutionState state, int[] numinds, int[] from, int threadnum, TestingProblem p)
/*     */   {
/*  93 */     p.prepareToEvaluate(state, threadnum);
/*     */     
/*  95 */     for (int pop = 0; pop < state.population.subpops.length; pop++)
/*     */     {
/*     */ 
/*  98 */       int upperbound = from[pop] + numinds[pop];
/*  99 */       for (int x = from[pop]; x < upperbound; x++) {
/*     */         try
/*     */         {
/* 102 */           p.evaluate(state, state.population.subpops[pop].individuals[x], pop, threadnum);
/* 103 */           if (state.population.subpops[pop].individuals[x].fitness.isIdealFitness()) {
/* 104 */             state.finish(1);
/* 105 */             return;
/*     */           }
/*     */         } catch (IllegalArgumentException e) {
/* 108 */           e.printStackTrace();
/*     */         } catch (IllegalAccessException e) {
/* 110 */           e.printStackTrace();
/*     */         } catch (InvocationTargetException e) {
/* 112 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 116 */     p.finishEvaluating(state, threadnum);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean runComplete(EvolutionState state)
/*     */   {
/* 125 */     for (int x = 0; x < state.population.subpops.length; x++)
/* 126 */       for (int y = 0; y < state.population.subpops[x].individuals.length; y++)
/*     */       {
/* 128 */         if (((TestCaseCandidate)state.population.subpops[x].individuals[y]).getFitness().isIdealFitness())
/* 129 */           return true; }
/* 130 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\ga\ecj\TestingEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */