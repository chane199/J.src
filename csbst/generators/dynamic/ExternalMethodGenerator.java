/*    */ package csbst.generators.dynamic;
/*    */ 
/*    */ import csbst.generators.AbsractGenerator;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.Vector;
/*    */ import org.eclipse.jdt.core.dom.AST;
/*    */ import org.eclipse.jdt.core.dom.Statement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExternalMethodGenerator
/*    */   extends MethodGenerator
/*    */ {
/*    */   private InstanceGenerator constructor;
/*    */   
/*    */   public ExternalMethodGenerator(AbsractGenerator parent, Class clazz, Class stub, Vector<Method> possibleMethodes)
/*    */   {
/* 33 */     super(parent, clazz, stub, possibleMethodes);
/*    */   }
/*    */   
/*    */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*    */   {
/* 38 */     return super.getStatements(ast, varName, pName);
/*    */   }
/*    */   
/*    */   public Object clone()
/*    */   {
/* 43 */     ExternalMethodGenerator newMeth = new ExternalMethodGenerator(this.parent, this.clazz, this.stub, this.recommandedWays);
/*    */     
/* 45 */     newMeth.clazz = this.clazz;
/*    */     
/* 47 */     newMeth.variableBinding = this.variableBinding;
/* 48 */     newMeth.fitness = this.fitness;
/* 49 */     newMeth.currentWay = this.currentWay;
/* 50 */     newMeth.object = this.object;
/* 51 */     newMeth.seed = this.seed;
/* 52 */     newMeth.random = this.random;
/*    */     
/* 54 */     newMeth.parameters = new Vector();
/* 55 */     if (this.parameters != null) {
/* 56 */       newMeth.parameters = new Vector();
/* 57 */       for (AbsractGenerator gene : this.parameters) {
/* 58 */         if (gene != null)
/* 59 */           newMeth.parameters.add((AbsractGenerator)gene.clone());
/*    */       }
/*    */     }
/* 62 */     newMeth.stub = this.stub;
/* 63 */     if (this.recommandedWays != null) {
/* 64 */       newMeth.recommandedWays = this.recommandedWays;
/*    */     }
/* 66 */     newMeth.unexpectedException = this.unexpectedException;
/* 67 */     newMeth.exceptions = new HashSet();
/* 68 */     newMeth.exceptions.addAll(this.exceptions);
/* 69 */     newMeth.generationNbr = this.generationNbr;
/* 70 */     newMeth.parent = this.parent;
/* 71 */     newMeth.possibleWays = this.possibleWays;
/*    */     
/* 73 */     if (this.externalConstructor != null) {
/* 74 */       newMeth.externalConstructor = ((InstanceGenerator)this.externalConstructor.clone());
/*    */     }
/* 76 */     return newMeth;
/*    */   }
/*    */   
/*    */ 
/*    */   public void execute(Object obj, Class clsUT)
/*    */   {
/* 82 */     this.constructor = new InstanceGenerator(null, this.currentWay.method.getDeclaringClass(), true);
/* 83 */     super.execute(this.constructor.getObject(), this.currentWay.method.getDeclaringClass());
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\dynamic\ExternalMethodGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */