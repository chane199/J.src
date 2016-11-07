/*    */ package csbst.utils;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import sun.misc.Unsafe;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AJavaFailure
/*    */ {
/*    */   private static void TestCase1()
/*    */   {
/* 14 */     Unsafe theUnsafe = Unsafe.getUnsafe();
/* 15 */     long rw = theUnsafe.getLong(null, 566L);
/*    */   }
/*    */   
/*    */ 
/*    */   public static void main(String[] args) {}
/*    */   
/*    */   private static void TestCase2()
/*    */   {
/* 23 */     Unsafe theUnsafe = (Unsafe)AccessController.doPrivileged(
/* 24 */       new PrivilegedAction()
/*    */       {
/*    */         public Object run() {
/*    */           try {
/* 28 */             Field f = Unsafe.class.getDeclaredField("theUnsafe");
/* 29 */             f.setAccessible(true);
/* 30 */             return f.get(null);
/*    */           }
/*    */           catch (NoSuchFieldException e)
/*    */           {
/* 34 */             throw new Error();
/*    */           } catch (IllegalAccessException e) {
/* 36 */             throw new Error();
/*    */           }
/*    */           
/*    */         }
/* 40 */       });
/* 41 */     theUnsafe.getLongVolatile(null, 5555L);
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\AJavaFailure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */