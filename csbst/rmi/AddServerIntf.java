package csbst.rmi;

import csbst.generators.AbsractGenerator;
import csbst.generators.dynamic.MethodGenerator;
import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface AddServerIntf
  extends Remote
{
  public abstract void MethodExecutorBasedRMI(Object paramObject, MethodGenerator paramMethodGenerator)
    throws RemoteException;
  
  public abstract void ObjectGeneratorBasedRMI(AbsractGenerator paramAbsractGenerator)
    throws RemoteException;
}


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\rmi\AddServerIntf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */