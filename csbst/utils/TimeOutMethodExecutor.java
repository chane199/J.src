/*    */ package csbst.utils;
/*    */ 
/*    */ import csbst.generators.dynamic.MethodGenerator;
/*    */ import csbst.testing.JTE;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
/*    */ 
/*    */ public class TimeOutMethodExecutor
/*    */ {
/*    */   public static class MethodExecutionJob implements java.util.concurrent.Callable<String>
/*    */   {
/*    */     Object object;
/*    */     MethodGenerator method;
/*    */     
/*    */     public MethodExecutionJob(Object obj, MethodGenerator meth)
/*    */     {
/* 19 */       this.object = obj;
/* 20 */       this.method = meth;
/*    */     }
/*    */     
/*    */     public String call() throws Exception
/*    */     {
/* 25 */       if (this.object == null) {
/* 26 */         this.method.execute();
/*    */       } else
/* 28 */         this.method.execute(this.object, this.method.getClazz());
/* 29 */       return "result";
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public static void executeMethod(Object obj, MethodGenerator meth)
/*    */   {
/* 36 */     Future<String> control = java.util.concurrent.Executors.newSingleThreadExecutor().submit(new MethodExecutionJob(obj, meth));
/*    */     
/*    */     try
/*    */     {
/* 40 */       result = (String)control.get(500L, TimeUnit.MILLISECONDS);
/*    */     }
/*    */     catch (TimeoutException ex) {
/*    */       String result;
/* 44 */       JTE.stdout.println("Method timeout");
/* 45 */       control.cancel(true);
/*    */     }
/*    */     catch (InterruptedException localInterruptedException) {}catch (ExecutionException localExecutionException) {}
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\TimeOutMethodExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */