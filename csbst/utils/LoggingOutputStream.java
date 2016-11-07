/*    */ package csbst.utils;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ public class LoggingOutputStream
/*    */   extends ByteArrayOutputStream
/*    */ {
/*    */   private String lineSeparator;
/*    */   private Logger logger;
/*    */   private Level level;
/*    */   
/*    */   public LoggingOutputStream(Logger logger, Level level)
/*    */   {
/* 18 */     this.logger = logger;
/* 19 */     this.level = level;
/* 20 */     this.lineSeparator = System.getProperty("line.separator");
/*    */   }
/*    */   
/*    */ 
/*    */   public void flush()
/*    */     throws IOException
/*    */   {
/* 27 */     synchronized (this) {
/* 28 */       super.flush();
/* 29 */       String record = toString();
/* 30 */       super.reset();
/*    */       
/* 32 */       if ((record.length() == 0) || (record.equals(this.lineSeparator)))
/*    */       {
/* 34 */         return;
/*    */       }
/*    */       
/* 37 */       this.logger.logp(this.level, "", "", record);
/*    */     }
/*    */     String record;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\LoggingOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */