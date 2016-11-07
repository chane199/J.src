/*     */ package csbst.utils;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ASTParser;
/*     */ import org.eclipse.jdt.core.dom.Assignment;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.core.dom.ExpressionStatement;
/*     */ import org.eclipse.jdt.core.dom.FieldDeclaration;
/*     */ import org.eclipse.jdt.core.dom.ImportDeclaration;
/*     */ import org.eclipse.jdt.core.dom.InfixExpression;
/*     */ import org.eclipse.jdt.core.dom.InfixExpression.Operator;
/*     */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*     */ import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
/*     */ import org.eclipse.jdt.core.dom.PostfixExpression;
/*     */ import org.eclipse.jdt.core.dom.PostfixExpression.Operator;
/*     */ import org.eclipse.jdt.core.dom.QualifiedName;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ 
/*     */ public class CreateJavaClass
/*     */ {
/*     */   private static CompilationUnit getTestCasesSourceCode()
/*     */   {
/*  33 */     ASTParser parser = ASTParser.newParser(3);
/*  34 */     parser.setSource("".toCharArray());
/*  35 */     CompilationUnit unit = (CompilationUnit)parser.createAST(null);
/*  36 */     unit.recordModifications();
/*  37 */     AST ast = unit.getAST();
/*     */     
/*     */ 
/*     */ 
/*  41 */     ImportDeclaration importDeclaration = ast.newImportDeclaration();
/*     */     
/*     */ 
/*  44 */     QualifiedName nQN = ast.newQualifiedName(ast.newQualifiedName(ast.newSimpleName("org"), ast.newSimpleName("junit")), ast.newSimpleName("Test"));
/*  45 */     importDeclaration.setName(nQN);
/*  46 */     unit.imports().add(importDeclaration);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  51 */     TypeDeclaration clazzNode = ast.newTypeDeclaration();
/*  52 */     clazzNode.setInterface(false);
/*  53 */     clazzNode.setName(ast.newSimpleName("ClassName"));
/*  54 */     clazzNode.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
/*  55 */     clazzNode.setSuperclassType(ast.newSimpleType(ast.newName("SuperClassName")));
/*  56 */     unit.types().add(clazzNode);
/*     */     
/*     */ 
/*     */ 
/*  60 */     VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
/*  61 */     vdf.setInitializer(ast.newNumberLiteral("0"));
/*  62 */     vdf.setName(ast.newSimpleName("FieldName"));
/*     */     
/*  64 */     FieldDeclaration vds = ast.newFieldDeclaration(vdf);
/*  65 */     List modifiers = vds.modifiers();
/*  66 */     vds.setType(ast.newSimpleType(ast.newSimpleName("Integer")));
/*  67 */     modifiers.add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
/*  68 */     modifiers.add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
/*     */     
/*  70 */     clazzNode.bodyDeclarations().add(0, vds);
/*     */     
/*     */ 
/*     */ 
/*  74 */     MethodDeclaration method = ast.newMethodDeclaration();
/*  75 */     method = ast.newMethodDeclaration();
/*     */     
/*  77 */     method.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
/*  78 */     method.setName(ast.newSimpleName("MethodName"));
/*  79 */     method.setBody(ast.newBlock());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  84 */     List<Statement> returnList = new ArrayList();
/*     */     
/*     */ 
/*  87 */     VariableDeclarationFragment varDec = ast.newVariableDeclarationFragment();
/*  88 */     varDec.setName(ast.newSimpleName("i"));
/*  89 */     varDec.setInitializer(ast.newSimpleName("FieldName"));
/*     */     
/*  91 */     org.eclipse.jdt.core.dom.VariableDeclarationStatement varDecStat = ast.newVariableDeclarationStatement(varDec);
/*     */     
/*     */ 
/*  94 */     returnList.add(varDecStat);
/*     */     
/*     */ 
/*  97 */     PostfixExpression inc = ast.newPostfixExpression();
/*  98 */     inc.setOperator(PostfixExpression.Operator.INCREMENT);
/*  99 */     inc.setOperand(ast.newSimpleName("i"));
/* 100 */     ExpressionStatement incStatement = ast.newExpressionStatement(inc);
/*     */     
/*     */ 
/* 103 */     returnList.add(incStatement);
/*     */     
/*     */ 
/* 106 */     Assignment Ass = ast.newAssignment();
/* 107 */     Ass.setLeftHandSide(ast.newSimpleName("i"));
/* 108 */     InfixExpression rightHandSideExpression = ast.newInfixExpression();
/* 109 */     rightHandSideExpression.setOperator(InfixExpression.Operator.PLUS);
/* 110 */     rightHandSideExpression.setLeftOperand(ast.newSimpleName("i"));
/* 111 */     rightHandSideExpression.setRightOperand(ast.newNumberLiteral("2"));
/* 112 */     Ass.setRightHandSide(rightHandSideExpression);
/* 113 */     ExpressionStatement AssStatement = ast.newExpressionStatement(Ass);
/*     */     
/*     */ 
/* 116 */     returnList.add(AssStatement);
/*     */     
/* 118 */     method.getBody().statements().addAll(returnList);
/*     */     
/* 120 */     clazzNode.bodyDeclarations().add(method);
/*     */     
/*     */ 
/* 123 */     return unit;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 127 */     Collection c = null;
/*     */     
/* 129 */     Map m = new java.util.HashMap();m.put(null, null);m.put(null, null);
/*     */     
/* 131 */     List a = new ArrayList();a.add(null);
/* 132 */     Set s = new HashSet();s.addAll(a);s.add(null);
/*     */     
/* 134 */     int[] b = new int[0];
/* 135 */     Vector v = new Vector();v.add(null);
/*     */     
/*     */ 
/* 138 */     System.out.println("a " + a.getClass().isArray() + " " + a.getClass().isAssignableFrom(Collection.class) + " " + Collection.class.isAssignableFrom(a.getClass()));
/* 139 */     System.out.println("b " + b.getClass().isArray() + " " + b.getClass().isAssignableFrom(Collection.class) + " " + Collection.class.isAssignableFrom(b.getClass()));
/* 140 */     System.out.println("v " + v.getClass().isArray() + " " + v.getClass().isAssignableFrom(Collection.class) + " " + Collection.class.isAssignableFrom(v.getClass()));
/* 141 */     System.out.println("m " + m.getClass().isArray() + " " + m.getClass().isAssignableFrom(Collection.class) + " " + Collection.class.isAssignableFrom(m.getClass()));
/* 142 */     System.out.println("s " + s.getClass().isArray() + " " + s.getClass().isAssignableFrom(Collection.class) + " " + Collection.class.isAssignableFrom(s.getClass()));
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\CreateJavaClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */