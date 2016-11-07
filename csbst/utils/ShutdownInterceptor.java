/*    */ package csbst.utils;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class ShutdownInterceptor extends Thread {
/*    */   private IApp app;
/*    */   
/*  8 */   public ShutdownInterceptor(IApp app) { this.app = app; }
/*    */   
/*    */   public void run()
/*    */   {
/* 12 */     System.out.println("Call the shutdown routine");
/* 13 */     this.app.shutDown();
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\ShutdownInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */