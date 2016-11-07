/*    */ package csbst.analysis;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*    */ import org.eclipse.jdt.core.dom.InfixExpression;
/*    */ import org.eclipse.jdt.core.dom.InfixExpression.Operator;
/*    */ 
/*    */ 
/*    */ public class InfixExpressionDecomposer
/*    */   extends ASTVisitor
/*    */ {
/*    */   private InfixExpression.Operator operator;
/* 13 */   private ArrayList<InfixExpression> atomicExpressions = new ArrayList();
/*    */   
/*    */   public InfixExpressionDecomposer(InfixExpression.Operator operator)
/*    */   {
/* 17 */     this.operator = operator;
/*    */   }
/*    */   
/*    */   public boolean visit(InfixExpression node)
/*    */   {
/* 22 */     if (node.getOperator() != this.operator) {
/* 23 */       this.atomicExpressions.add(node);
/* 24 */       return false;
/*    */     }
/* 26 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ArrayList<InfixExpression> getExpressionsList()
/*    */   {
/* 38 */     return this.atomicExpressions;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\InfixExpressionDecomposer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */