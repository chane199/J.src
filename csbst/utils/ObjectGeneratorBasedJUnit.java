/*    */ package csbst.utils;
/*    */ 
/*    */ import csbst.generators.AbsractGenerator;
/*    */ import junit.framework.TestCase;
/*    */ import org.junit.Rule;
/*    */ import org.junit.Test;
/*    */ import org.junit.rules.Timeout;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObjectGeneratorBasedJUnit
/*    */   extends TestCase
/*    */ {
/*    */   public AbsractGenerator gene;
/*    */   @Rule
/* 17 */   public Timeout to = new Timeout(5);
/*    */   
/*    */   public ObjectGeneratorBasedJUnit(AbsractGenerator ge) {
/* 20 */     this.gene = ge;
/*    */   }
/*    */   
/*    */   @Test(timeout=5L)
/*    */   public void testObject() {
/* 25 */     this.gene.generateRandom();
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\ObjectGeneratorBasedJUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */