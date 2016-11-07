/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.utils.ASTEditor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.NullLiteral;
/*     */ import org.eclipse.jdt.core.dom.NumberLiteral;
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
/*     */ public abstract class AbstractPrimitive<T>
/*     */   extends AbsractGenerator
/*     */ {
/*     */   protected T uBound;
/*     */   protected T lBound;
/*     */   protected T absolutuBound;
/*     */   protected T absolutlBound;
/*     */   
/*     */   public AbstractPrimitive(AbsractGenerator parent, Class type)
/*     */   {
/*  30 */     super(parent, type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/*  37 */     List<Statement> returnList = new ArrayList();
/*  38 */     VariableDeclarationFragment varDec = ast.newVariableDeclarationFragment();
/*  39 */     varDec.setName(ast.newSimpleName(varName + pName));
/*     */     
/*     */ 
/*  42 */     if (this.object != null) {
/*  43 */       NumberLiteral numberLiteral = ast.newNumberLiteral(toString());
/*  44 */       varDec.setInitializer(numberLiteral);
/*     */     } else {
/*  46 */       NullLiteral numberLiteral = ast.newNullLiteral();
/*  47 */       varDec.setInitializer(numberLiteral);
/*     */     }
/*  49 */     VariableDeclarationStatement varDecStat = ast.newVariableDeclarationStatement(varDec);
/*     */     
/*  51 */     if (ASTEditor.getPrimitiveCode(this.clazz) != null) {
/*  52 */       varDecStat.setType(ast.newPrimitiveType(ASTEditor.getPrimitiveCode(this.clazz)));
/*     */     } else {
/*  54 */       varDecStat.setType(ast.newSimpleType(ast.newSimpleName(this.clazz.getSimpleName())));
/*     */     }
/*     */     
/*  57 */     returnList.add(varDecStat);
/*  58 */     return returnList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setuBound(T u)
/*     */   {
/*  67 */     this.uBound = u;
/*     */   }
/*     */   
/*     */   public void setlBound(T l) {
/*  71 */     this.lBound = l;
/*     */   }
/*     */   
/*     */   public T getuBound() {
/*  75 */     return (T)this.uBound;
/*     */   }
/*     */   
/*     */   public T getlBound() {
/*  79 */     return (T)this.lBound;
/*     */   }
/*     */   
/*     */   public T getAbsolutuBound() {
/*  83 */     return (T)this.absolutuBound;
/*     */   }
/*     */   
/*     */   public T getAbsolutlBound() {
/*  87 */     return (T)this.absolutlBound;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/*  91 */     boolean areEqual = super.equals(other);
/*  92 */     areEqual = (areEqual) && (this.uBound.equals(((AbstractPrimitive)other).getuBound()));
/*  93 */     areEqual = (areEqual) && (this.lBound.equals(((AbstractPrimitive)other).getlBound()));
/*  94 */     areEqual = (areEqual) && (this.absolutuBound.equals(((AbstractPrimitive)other).getuBound()));
/*  95 */     areEqual = (areEqual) && (this.absolutlBound.equals(((AbstractPrimitive)other).getAbsolutlBound()));
/*  96 */     return areEqual;
/*     */   }
/*     */   
/*     */   public static final Class<?> getGeneClass(Class cls) {
/* 100 */     if ((cls.equals(Byte.class)) || (cls.equals(Byte.TYPE)))
/* 101 */       return ByteGenerator.class;
/* 102 */     if ((cls.equals(Short.class)) || (cls.equals(Short.TYPE)))
/* 103 */       return ShortGenerator.class;
/* 104 */     if ((cls.equals(Integer.class)) || (cls.equals(Integer.TYPE)))
/* 105 */       return IntegerGenerator.class;
/* 106 */     if ((cls.equals(Long.class)) || (cls.equals(Long.TYPE)))
/* 107 */       return LongGenerator.class;
/* 108 */     if ((cls.equals(Boolean.class)) || (cls.equals(Boolean.TYPE)))
/* 109 */       return BooleanGenerator.class;
/* 110 */     if ((cls.equals(Character.TYPE)) || (cls.equals(Character.class)))
/* 111 */       return CharGenerator.class;
/* 112 */     if ((cls.equals(Float.class)) || (cls.equals(Float.TYPE)))
/* 113 */       return FloatGenerator.class;
/* 114 */     if ((cls.equals(Double.class)) || (cls.equals(Double.TYPE))) {
/* 115 */       return DoubleGenerator.class;
/*     */     }
/*     */     
/*     */ 
/* 119 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\AbstractPrimitive.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */