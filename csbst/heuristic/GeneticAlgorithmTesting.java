/*    */ package csbst.heuristic;
/*    */ 
/*    */ import csbst.ga.ecj.TestingState;
/*    */ import csbst.testing.JTE;
/*    */ import csbst.testing.fitness.TestingFitness;
/*    */ import ec.Evolve;
/*    */ import ec.util.Parameter;
/*    */ import ec.util.ParameterDatabase;
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GeneticAlgorithmTesting
/*    */ {
/*    */   private int generations;
/*    */   
/*    */   public GeneticAlgorithmTesting(int generations)
/*    */   {
/* 21 */     this.generations = generations;
/*    */   }
/*    */   
/*    */   public int run(String gaParametersFile)
/*    */   {
/* 26 */     if (this.generations <= 0)
/* 27 */       return 0;
/* 28 */     int currentGenNbr = 0;
/*    */     try
/*    */     {
/* 31 */       ParameterDatabase parameters = new ParameterDatabase(new File(gaParametersFile));
/* 32 */       int nbrgeneration = this.generations / 150;
/* 33 */       if (nbrgeneration <= 0) {
/* 34 */         nbrgeneration = 1;
/*    */       }
/* 36 */       parameters.set(new Parameter("generations"), Integer.toString(nbrgeneration));
/* 37 */       if (JTE.seed != 0)
/* 38 */         parameters.set(new Parameter("seed.0"), Long.toString(JTE.seed));
/* 39 */       TestingState state = (TestingState)Evolve.initialize(parameters, 0);
/* 40 */       TestingFitness.evaluations = 0;
/* 41 */       state.run(0);
/* 42 */       Evolve.cleanup(state);
/*    */     } catch (FileNotFoundException e) {
/* 44 */       e.printStackTrace();
/*    */     } catch (IOException e) {
/* 46 */       e.printStackTrace();
/*    */     }
/* 48 */     return TestingFitness.evaluations;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\heuristic\GeneticAlgorithmTesting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */