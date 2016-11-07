/*     */ package csbst.analysis;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import org.eclipse.jdt.core.JavaCore;
/*     */ import org.eclipse.jdt.core.dom.ASTParser;
/*     */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.core.dom.Expression;
/*     */ import org.eclipse.jdt.core.dom.IfStatement;
/*     */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class String2Expression
/*     */ {
/*     */   private static Expression expression;
/*     */   private static Block block;
/*     */   private static MethodDeclaration method;
/*     */   
/*     */   private static class expressionVisitor
/*     */     extends ASTVisitor
/*     */   {
/*     */     public boolean visit(IfStatement node)
/*     */     {
/*  27 */       String2Expression.expression = node.getExpression();
/*  28 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InfixExpressionVisitor extends ASTVisitor
/*     */   {
/*     */     public boolean visit(Block node) {
/*  35 */       String2Expression.block = node;
/*  36 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static Expression getExpression(String txtExpression) {
/*  41 */     Expression newExpression = null;
/*  42 */     String classX = "class X{ X(){if(" + txtExpression + ");}}";
/*     */     
/*  44 */     ASTParser parser = ASTParser.newParser(3);
/*  45 */     parser.setSource(classX.toCharArray());
/*  46 */     parser.setEnvironment(null, null, null, true);
/*  47 */     parser.setResolveBindings(true);
/*  48 */     parser.setStatementsRecovery(true);
/*  49 */     parser.setBindingsRecovery(true);
/*  50 */     Hashtable<String, String> options = JavaCore.getDefaultOptions();
/*  51 */     options.put("org.eclipse.jdt.core.compiler.source", "1.6");
/*  52 */     parser.setCompilerOptions(options);
/*     */     
/*     */ 
/*     */ 
/*  56 */     CompilationUnit astRoot = (CompilationUnit)parser.createAST(null);
/*  57 */     expressionVisitor ev = new expressionVisitor(null);
/*  58 */     astRoot.accept(ev);
/*  59 */     return expression;
/*     */   }
/*     */   
/*     */   public static Block getInfixExpression(String txtExpression) {
/*  63 */     Expression newExpression = null;
/*  64 */     String classX = "class X{ X(){ " + txtExpression + "}}";
/*     */     
/*     */ 
/*  67 */     ASTParser parser = ASTParser.newParser(3);
/*  68 */     parser.setSource(classX.toCharArray());
/*  69 */     parser.setEnvironment(null, null, null, true);
/*  70 */     parser.setResolveBindings(true);
/*  71 */     parser.setStatementsRecovery(true);
/*  72 */     parser.setBindingsRecovery(true);
/*  73 */     Hashtable<String, String> options = JavaCore.getDefaultOptions();
/*  74 */     options.put("org.eclipse.jdt.core.compiler.source", "1.7");
/*  75 */     parser.setCompilerOptions(options);
/*     */     
/*     */ 
/*     */ 
/*  79 */     CompilationUnit astRoot = (CompilationUnit)parser.createAST(null);
/*  80 */     InfixExpressionVisitor ev = new InfixExpressionVisitor(null);
/*  81 */     astRoot.accept(ev);
/*     */     
/*  83 */     return block;
/*     */   }
/*     */   
/*     */   public static MethodDeclaration getMethodDeclaration(String txtExpression) {
/*  87 */     Expression newExpression = null;
/*  88 */     String classX = "class X{ " + txtExpression + "}";
/*     */     
/*     */ 
/*  91 */     ASTParser parser = ASTParser.newParser(3);
/*  92 */     parser.setSource(classX.toCharArray());
/*  93 */     parser.setEnvironment(null, null, null, true);
/*  94 */     parser.setResolveBindings(true);
/*  95 */     parser.setStatementsRecovery(true);
/*  96 */     parser.setBindingsRecovery(true);
/*  97 */     Hashtable<String, String> options = JavaCore.getDefaultOptions();
/*  98 */     options.put("org.eclipse.jdt.core.compiler.source", "1.7");
/*  99 */     parser.setCompilerOptions(options);
/*     */     
/*     */ 
/*     */ 
/* 103 */     CompilationUnit astRoot = (CompilationUnit)parser.createAST(null);
/* 104 */     MethodDeclarationVisitor ev = new MethodDeclarationVisitor(null);
/* 105 */     astRoot.accept(ev);
/*     */     
/* 107 */     return method;
/*     */   }
/*     */   
/*     */   private static class MethodDeclarationVisitor extends ASTVisitor
/*     */   {
/*     */     public boolean visit(MethodDeclaration node) {
/* 113 */       String2Expression.method = node;
/* 114 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\String2Expression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */