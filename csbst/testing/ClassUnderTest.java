/*    */ package csbst.testing;
/*    */ 
/*    */ import csbst.analysis.DataMemberUseAnalyser;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import java.util.Vector;
/*    */ import org.eclipse.jdt.core.dom.ITypeBinding;
/*    */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*    */ 
/*    */ public class ClassUnderTest
/*    */ {
/*    */   private Vector<DataMember> dataMembers;
/*    */   private Class clazz;
/*    */   
/*    */   public ClassUnderTest(Class cls)
/*    */   {
/* 19 */     this.clazz = cls;
/*    */   }
/*    */   
/*    */   public void prepareDataMembers() throws java.io.IOException
/*    */   {
/* 24 */     this.dataMembers = new Vector();
/* 25 */     for (Map.Entry<IVariableBinding, Set<Integer>> dm : JTE.dataMemberUseAnalyser.getDataMemberBranchTransformersMap().entrySet()) {
/* 26 */       if (dm.getKey() != null)
/*    */       {
/*    */ 
/*    */ 
/* 30 */         if ((!Modifier.isFinal(((IVariableBinding)dm.getKey()).getModifiers())) || (((IVariableBinding)dm.getKey()).getVariableDeclaration().getConstantValue() == null))
/*    */         {
/*    */ 
/* 33 */           if (((IVariableBinding)dm.getKey()).getDeclaringClass().getQualifiedName().equals(this.clazz.getCanonicalName()))
/*    */           {
/*    */ 
/*    */ 
/*    */ 
/* 38 */             DataMember tmpDM = new DataMember((IVariableBinding)dm.getKey());
/* 39 */             this.dataMembers.add(tmpDM);
/*    */           } } }
/*    */     }
/*    */   }
/*    */   
/*    */   public Vector<DataMember> getDataMembers() {
/* 45 */     return this.dataMembers;
/*    */   }
/*    */   
/*    */   public Class getClazz() {
/* 49 */     return this.clazz;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\ClassUnderTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */