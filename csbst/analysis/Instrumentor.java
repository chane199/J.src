/*     */ package csbst.analysis;
/*     */ 
/*     */ import csbst.testing.BranchDistance;
/*     */ import csbst.testing.fitness.ApproachLevel;
/*     */ import csbst.testing.fitness.NumberCoveredBranches;
/*     */ import csbst.utils.ASTEditor;
/*     */ import java.util.List;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.core.dom.ConstructorInvocation;
/*     */ import org.eclipse.jdt.core.dom.Expression;
/*     */ import org.eclipse.jdt.core.dom.ExpressionStatement;
/*     */ import org.eclipse.jdt.core.dom.ImportDeclaration;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.Name;
/*     */ import org.eclipse.jdt.core.dom.NumberLiteral;
/*     */ import org.eclipse.jdt.core.dom.PackageDeclaration;
/*     */ import org.eclipse.jdt.core.dom.SimpleName;
/*     */ import org.eclipse.jdt.core.dom.StringLiteral;
/*     */ import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
/*     */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Instrumentor
/*     */   extends ASTVisitor
/*     */ {
/*     */   String className;
/*  37 */   String packageName = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addExecutionPathTracer(Block b, int index, String iteration, int oldBranch)
/*     */   {
/*  46 */     if (b.getProperty("numberBranch") == null)
/*  47 */       return;
/*  48 */     if ((b.statements().size() > 0) && (index <= b.statements().size()) && (
/*  49 */       ((b.statements().get(index) instanceof SuperConstructorInvocation)) || ((b.statements().get(index) instanceof ConstructorInvocation)))) {
/*  50 */       index++;
/*     */     }
/*     */     
/*  53 */     int branch = ((Integer)b.getProperty("numberBranch")).intValue();
/*     */     
/*     */ 
/*  56 */     MethodInvocation trace = b.getAST().newMethodInvocation();
/*  57 */     trace.setExpression(b.getAST().newSimpleName(NumberCoveredBranches.class.getSimpleName()));
/*  58 */     trace.setName(b.getAST().newSimpleName("maintainPathTrace"));
/*     */     
/*  60 */     NumberLiteral p1 = b.getAST().newNumberLiteral(branch);
/*  61 */     trace.arguments().add(p1);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */     StringLiteral p2 = b.getAST().newStringLiteral();
/*  72 */     if ((this.packageName != null) && (!this.packageName.equals(""))) {
/*  73 */       p2.setLiteralValue(this.packageName + "." + this.className);
/*     */     } else
/*  75 */       p2.setLiteralValue(this.className);
/*  76 */     trace.arguments().add(p2);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */     ExpressionStatement eStatement2 = b.getAST().newExpressionStatement(trace);
/*  83 */     b.statements().add(index, eStatement2);
/*     */   }
/*     */   
/*     */   private void branchInstrumentation(Block block) throws Exception {
/*  87 */     Expression expression = (Expression)block.getProperty("expression");
/*  88 */     if (expression.toString().equals("true")) {
/*  89 */       return;
/*     */     }
/*     */     
/*  92 */     MethodInvocation gBD = block.getAST().newMethodInvocation();
/*  93 */     gBD.setExpression(block.getAST().newSimpleName(ApproachLevel.class.getSimpleName()));
/*  94 */     gBD.setName(block.getAST().newSimpleName("maintainBranchDistance"));
/*     */     
/*     */ 
/*  97 */     gBD.arguments().add(block.getAST().newNumberLiteral(block.getProperty("numberBranch")));
/*  98 */     gBD.arguments().add((Expression)ASTNode.copySubtree(block.getAST(), String2Expression.getExpression("1")));
/*     */     
/*     */ 
/*     */ 
/* 102 */     Expression nodeExp = (Expression)ASTNode.copySubtree(block.getAST(), expression);
/* 103 */     Expression exp = (Expression)ASTNode.copySubtree(block.getAST(), String2Expression.getExpression(BranchDistance.expressionFormater(nodeExp)));
/*     */     
/* 105 */     gBD.arguments().add(exp);
/* 106 */     ExpressionStatement gBDStatement = block.getAST().newExpressionStatement(gBD);
/*     */     
/*     */ 
/*     */ 
/* 110 */     if ((block.getParent().getParent() instanceof Block)) {
/* 111 */       ((Block)block.getParent().getParent()).statements().add(((Block)block.getParent().getParent()).statements().indexOf(block.getParent()), gBDStatement);
/*     */     } else {
/* 113 */       block.statements().add(0, gBDStatement);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean visit(Block node)
/*     */   {
/* 119 */     if (node.getProperty("numberBranch") != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */       addExecutionPathTracer(node, 0, "1", -1);
/*     */     }
/* 128 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(PackageDeclaration node)
/*     */   {
/* 134 */     this.packageName = node.getName().toString();
/*     */     
/* 136 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(TypeDeclaration node)
/*     */   {
/* 142 */     if ((node.isLocalTypeDeclaration()) || (node.isMemberTypeDeclaration())) {
/* 143 */       return true;
/*     */     }
/* 145 */     this.className = node.getName().toString();
/*     */     
/* 147 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean visit(CompilationUnit node)
/*     */   {
/* 168 */     String pckgName = "";
/* 169 */     if (node.getPackage() != null) {
/* 170 */       pckgName = node.getPackage().getName().getFullyQualifiedName();
/*     */     }
/* 172 */     ImportDeclaration importDeclaration = node.getAST().newImportDeclaration();
/* 173 */     importDeclaration.setName(ASTEditor.generateQualifiedName("csbst.testing.fitness", node.getAST()));
/* 174 */     importDeclaration.setOnDemand(true);
/* 175 */     node.imports().add(importDeclaration);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 189 */     return true;
/*     */   }
/*     */   
/*     */   private Name ASTEditor(String pckgName, AST ast)
/*     */   {
/* 194 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\Instrumentor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */