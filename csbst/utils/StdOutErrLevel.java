/*    */ package csbst.utils;
/*    */ 
/*    */ import java.io.InvalidObjectException;
/*    */ import java.io.ObjectStreamException;
/*    */ import java.util.logging.Level;
/*    */ 
/*    */ public class StdOutErrLevel
/*    */   extends Level
/*    */ {
/*    */   private StdOutErrLevel(String name, int value)
/*    */   {
/* 12 */     super(name, value);
/*    */   }
/*    */   
/*    */ 
/* 16 */   public static Level STDOUT = new StdOutErrLevel("STDOUT", Level.INFO.intValue() + 53);
/*    */   
/*    */ 
/* 19 */   public static Level STDERR = new StdOutErrLevel("STDERR", Level.INFO.intValue() + 54);
/*    */   
/*    */   protected Object readResolve() throws ObjectStreamException
/*    */   {
/* 23 */     if (intValue() == STDOUT.intValue())
/* 24 */       return STDOUT;
/* 25 */     if (intValue() == STDERR.intValue())
/* 26 */       return STDERR;
/* 27 */     throw new InvalidObjectException("Unknown instance :" + this);
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\StdOutErrLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */