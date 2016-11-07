/*    */ package csbst.utils;
/*    */ import java.io.PrintStream;
/*    */ 
/*  4 */ public class TimeOut extends Thread { Thread threadToInterrupt = null;
/*    */   
/*    */   public TimeOut() {
/*  7 */     this.threadToInterrupt = Thread.currentThread();
/*  8 */     setDaemon(true);
/*    */   }
/*    */   
/*    */   public void run() {
/* 12 */     System.out.println("TimeOut has terminated normaly");
/*    */   }
/*    */   
/*    */   public void stopThread()
/*    */   {
/* 17 */     System.out.println("TimeOut has been interrupted");
/* 18 */     this.threadToInterrupt.interrupt();
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\TimeOut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */