/*    */ package csbst.utils;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class IOUtil
/*    */ {
/*    */   public static byte[] readFile(String file) throws java.io.IOException
/*    */   {
/* 11 */     String url = "file:" + file;
/* 12 */     URL myUrl = new URL(url);
/* 13 */     java.net.URLConnection connection = myUrl.openConnection();
/* 14 */     InputStream input = connection.getInputStream();
/* 15 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 16 */     int data = input.read();
/*    */     
/* 18 */     while (data != -1) {
/* 19 */       buffer.write(data);
/* 20 */       data = input.read();
/*    */     }
/*    */     
/* 23 */     input.close();
/*    */     
/* 25 */     byte[] classData = buffer.toByteArray();
/* 26 */     return classData;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\IOUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */