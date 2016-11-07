/*    */ package csbst.ga.ecj;
/*    */ 
/*    */ import csbst.generators.AbsractGenerator;
/*    */ import csbst.testing.JTE;
/*    */ import ec.EvolutionState;
/*    */ import ec.Individual;
/*    */ import ec.Species;
/*    */ import ec.util.Parameter;
/*    */ import ec.vector.VectorDefaults;
/*    */ 
/*    */ public class ChromosomeSpecies extends Species
/*    */ {
/*    */   public static final String P_VECTORSPECIES = "species";
/*    */   public static final String P_GENE = "gene";
/*    */   public AbsractGenerator genePrototype;
/*    */   
/*    */   public void setup(EvolutionState state, Parameter base)
/*    */   {
/* 19 */     Parameter def = defaultBase();
/*    */     
/* 21 */     super.setup(state, base);
/*    */   }
/*    */   
/*    */   public Individual newIndividual(EvolutionState state, int thread)
/*    */   {
/* 26 */     TestCaseCandidate newind = new TestCaseCandidate(JTE.currentClassUnderTest);
/* 27 */     newind.generateRandom();
/* 28 */     newind.evaluated = false;
/*    */     
/*    */ 
/* 31 */     newind.species = this;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */     return newind;
/*    */   }
/*    */   
/*    */   public Parameter defaultBase()
/*    */   {
/* 42 */     return VectorDefaults.base().push("species");
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\ga\ecj\ChromosomeSpecies.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */