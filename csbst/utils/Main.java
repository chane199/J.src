/*    */ package csbst.utils;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.logging.FileHandler;
/*    */ import java.util.logging.Handler;
/*    */ import java.util.logging.LogManager;
/*    */ import java.util.logging.Logger;
/*    */ import java.util.logging.SimpleFormatter;
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] args) throws Exception
/*    */   {
/* 14 */     LogManager logManager = LogManager.getLogManager();
/* 15 */     logManager.reset();
/*    */     
/*    */ 
/* 18 */     Handler fileHandler = new FileHandler("log", 10000, 3, true);
/* 19 */     fileHandler.setFormatter(new SimpleFormatter());
/* 20 */     Logger.getLogger("").addHandler(fileHandler);
/*    */     
/*    */ 
/* 23 */     PrintStream stdout = System.out;
/* 24 */     PrintStream stderr = System.err;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 30 */     Logger logger = Logger.getLogger("stdout");
/* 31 */     LoggingOutputStream los = new LoggingOutputStream(logger, StdOutErrLevel.STDOUT);
/* 32 */     System.setOut(new PrintStream(los, true));
/*    */     
/* 34 */     logger = Logger.getLogger("stderr");
/* 35 */     los = new LoggingOutputStream(logger, StdOutErrLevel.STDERR);
/* 36 */     System.setErr(new PrintStream(los, true));
/*    */     
/*    */ 
/* 39 */     System.out.println("Hello world!");
/*    */     
/*    */ 
/* 42 */     logger = Logger.getLogger("test");
/* 43 */     logger.info("This is a test log message");
/*    */     
/*    */     try
/*    */     {
/* 47 */       throw new RuntimeException("Test");
/*    */     } catch (Exception e) {
/* 49 */       e.printStackTrace();
/*    */       
/*    */ 
/*    */ 
/* 53 */       stdout.println("Hello on old stdout");
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */