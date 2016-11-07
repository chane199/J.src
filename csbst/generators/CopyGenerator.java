/*    */ package csbst.generators;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.eclipse.jdt.core.dom.AST;
/*    */ import org.eclipse.jdt.core.dom.Statement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CopyGenerator
/*    */   extends AbsractGenerator
/*    */ {
/*    */   public CopyGenerator()
/*    */   {
/* 15 */     super(null, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void generateRandom() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void mutate() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*    */   {
/* 34 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*    */   {
/* 40 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 46 */     return 0;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\CopyGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */