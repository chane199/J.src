/*    */ package csbst.utils;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ 
/*    */ public class ReadSpecificLine
/*    */ {
/*    */   public static String getLine(String filname, int lineNumber) throws java.io.IOException
/*    */   {
/*  9 */     String line = null;
/*    */     
/* 11 */     java.io.FileReader fr = null;
/* 12 */     BufferedReader br = null;
/*    */     try {
/* 14 */       fr = new java.io.FileReader(filname);
/* 15 */       br = new BufferedReader(fr);
/* 16 */       int lineNo = 1;
/* 17 */       while (lineNo <= lineNumber) {
/* 18 */         line = br.readLine();
/* 19 */         if (lineNo == lineNumber)
/* 20 */           return line;
/* 21 */         lineNo++;
/*    */       }
/*    */     } finally {
/* 24 */       if (fr != null)
/* 25 */         fr.close();
/* 26 */       if (br != null) {
/* 27 */         br.close();
/*    */       }
/*    */     }
/*    */     int lineNo;
/* 24 */     if (fr != null)
/* 25 */       fr.close();
/* 26 */     if (br != null) {
/* 27 */       br.close();
/*    */     }
/* 29 */     return line;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\ReadSpecificLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */