/*    */ package csbst.utils;
/*    */ 
/*    */ import csbst.generators.dynamic.MethodGenerator;
/*    */ import csbst.testing.JTE;
/*    */ import java.io.PrintStream;
/*    */ import junit.framework.TestCase;
/*    */ import org.junit.After;
/*    */ import org.junit.Rule;
/*    */ import org.junit.Test;
/*    */ import org.junit.rules.Timeout;
/*    */ 
/*    */ public class MethodExecutorBasedJUnit extends TestCase
/*    */ {
/*    */   public Object object;
/*    */   public MethodGenerator method;
/*    */   @Rule
/* 17 */   public Timeout to = new Timeout(5);
/*    */   
/*    */   public MethodExecutorBasedJUnit(Object obj, MethodGenerator meth) {
/* 20 */     this.object = obj;
/* 21 */     this.method = meth;
/*    */   }
/*    */   
/*    */   @Test(timeout=5L)
/*    */   public void testMethod()
/*    */   {
/* 27 */     if (this.object == null) {
/* 28 */       this.method.execute();
/*    */     } else
/* 30 */       this.method.execute(this.object, this.method.getClazz());
/*    */   }
/*    */   
/*    */   @After
/*    */   public void after() {
/* 35 */     JTE.stdout.println("@After is invoked, indeed.");
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\MethodExecutorBasedJUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */