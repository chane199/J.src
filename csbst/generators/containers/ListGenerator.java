/*     */ package csbst.generators.containers;
/*     */ 
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.testing.JTE;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ClassInstanceCreation;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
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
/*     */ public class ListGenerator
/*     */   extends ArrayGenerator
/*     */ {
/*     */   public ListGenerator(AbsractGenerator parent, Class type)
/*     */   {
/*  34 */     super(parent, 0, type);
/*     */   }
/*     */   
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/*  39 */     List<Statement> returnList = new ArrayList();
/*  40 */     String newVarName = varName + pName + "P1";
/*  41 */     returnList.addAll(super.getStatements(ast, newVarName, ""));
/*     */     
/*     */ 
/*  44 */     VariableDeclarationFragment myVar = ast.newVariableDeclarationFragment();
/*  45 */     myVar.setName(ast.newSimpleName(varName));
/*     */     
/*     */ 
/*  48 */     ClassInstanceCreation classInstanceArrayList = ast.newClassInstanceCreation();
/*  49 */     classInstanceArrayList.setType(ast.newSimpleType(ast.newSimpleName("ArrayList")));
/*     */     
/*     */ 
/*  52 */     MethodInvocation invokeAsList = ast.newMethodInvocation();
/*  53 */     invokeAsList.setExpression(ast.newSimpleName("Arrays"));
/*  54 */     invokeAsList.setName(ast.newSimpleName("asList"));
/*     */     
/*  56 */     classInstanceArrayList.arguments().add(invokeAsList);
/*     */     
/*  58 */     VariableDeclarationStatement myVarDec = ast.newVariableDeclarationStatement(myVar);
/*  59 */     myVarDec.setType(ast.newSimpleType(ast.newSimpleName("List")));
/*  60 */     myVar.setInitializer(classInstanceArrayList);
/*     */     
/*  62 */     invokeAsList.arguments().add(ast.newSimpleName(newVarName));
/*     */     
/*  64 */     returnList.add(myVarDec);
/*     */     
/*  66 */     JTE.requiredClasses.add(List.class);
/*  67 */     JTE.requiredClasses.add(Arrays.class);
/*  68 */     JTE.requiredClasses.add(ArrayList.class);
/*  69 */     return returnList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getObject()
/*     */   {
/*  76 */     Object retVal = new ArrayList();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  81 */       Method add = List.class.getDeclaredMethod("add", new Class[] { Object.class });
/*     */       
/*  83 */       for (int i = 0; i < this.length; i++) {
/*     */         try {
/*  85 */           add.invoke(retVal, new Object[] { this.elements[i].getObject() });
/*     */         }
/*     */         catch (IllegalArgumentException e) {
/*  88 */           e.printStackTrace();
/*     */         }
/*     */         catch (IllegalAccessException e) {
/*  91 */           e.printStackTrace();
/*     */         }
/*     */         catch (InvocationTargetException e) {
/*  94 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SecurityException e) {
/*  99 */       e.printStackTrace();
/*     */     }
/*     */     catch (NoSuchMethodException e) {
/* 102 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/* 106 */     return retVal;
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 111 */     ListGenerator newList = new ListGenerator(this.parent, this.clazz);
/* 112 */     newList.clazz = this.clazz;
/* 113 */     newList.variableBinding = this.variableBinding;
/* 114 */     newList.fitness = this.fitness;
/* 115 */     newList.object = this.object;
/* 116 */     newList.seed = this.seed;
/* 117 */     newList.random = this.random;
/* 118 */     newList.length = this.length;
/* 119 */     newList.isFixedSize = this.isFixedSize;
/*     */     
/* 121 */     if (this.length > 0) {
/* 122 */       newList.elements = new AbsractGenerator[this.length];
/* 123 */       for (int i = 0; i < this.length; i++)
/* 124 */         newList.elements[i] = ((AbsractGenerator)this.elements[i].clone());
/*     */     }
/* 126 */     return newList;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\containers\ListGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */