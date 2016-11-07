/*    */ package csbst.rmi;
/*    */ 
/*    */ import java.rmi.Naming;
/*    */ 
/*    */ public class AddServer
/*    */ {
/*    */   public static void main(String[] args) {
/*    */     try {
/*  9 */       AddServerImpl addServerImpl = new AddServerImpl();
/* 10 */       Naming.rebind("AddServer", addServerImpl);
/*    */     }
/*    */     catch (Exception e) {
/* 13 */       System.out.println("Exception: " + e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\rmi\AddServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */