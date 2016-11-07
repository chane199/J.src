/*    */ package csbst.rmi;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class AddClient {
/*    */   public static void main(String[] args) {
/*  7 */     try { String addServerURL = "rmi://" + args[0] + "/AddServer";
/*  8 */       AddServerIntf addServerIntf = 
/*  9 */         (AddServerIntf)java.rmi.Naming.lookup(addServerURL);
/* 10 */       System.out.println("The first number is: " + args[1]);
/* 11 */       double d1 = Double.valueOf(args[1]).doubleValue();
/* 12 */       System.out.println("The second number is: " + args[2]);
/*    */       
/* 14 */       d2 = Double.valueOf(args[2]).doubleValue();
/*    */     }
/*    */     catch (Exception e) {
/*    */       double d2;
/* 18 */       System.out.println("Exception: " + e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\rmi\AddClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */