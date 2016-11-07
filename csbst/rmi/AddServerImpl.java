/*    */ package csbst.rmi;
/*    */ 
/*    */ import csbst.generators.AbsractGenerator;
/*    */ import csbst.generators.dynamic.MethodGenerator;
/*    */ import java.rmi.RemoteException;
/*    */ import java.rmi.server.UnicastRemoteObject;
/*    */ 
/*    */ public class AddServerImpl
/*    */   extends UnicastRemoteObject
/*    */   implements AddServerIntf
/*    */ {
/*    */   protected AddServerImpl()
/*    */     throws RemoteException
/*    */   {}
/*    */   
/*    */   public void MethodExecutorBasedRMI(Object obj, MethodGenerator meth) throws RemoteException
/*    */   {
/* 18 */     if (obj == null) {
/* 19 */       meth.execute();
/*    */     } else {
/* 21 */       meth.execute(obj, meth.getClazz());
/*    */     }
/*    */   }
/*    */   
/*    */   public void ObjectGeneratorBasedRMI(AbsractGenerator ge) throws RemoteException {
/* 26 */     ge.generateRandom();
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\rmi\AddServerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */