/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.utils.ASTEditor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.BooleanLiteral;
/*     */ import org.eclipse.jdt.core.dom.NullLiteral;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
/*     */ 
/*     */ 
/*     */ public class BooleanGenerator
/*     */   extends AbstractPrimitive<Boolean>
/*     */ {
/*     */   public BooleanGenerator(AbsractGenerator parent, Class cls)
/*     */   {
/*  21 */     super(parent, cls);
/*  22 */     this.absolutuBound = Boolean.TRUE;
/*  23 */     this.absolutlBound = Boolean.FALSE;
/*  24 */     this.uBound = ((Boolean)this.absolutuBound);
/*  25 */     this.lBound = ((Boolean)this.absolutlBound);
/*  26 */     generateRandom();
/*     */   }
/*     */   
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  31 */     boolean returnValue = false;
/*  32 */     returnValue = gene instanceof BooleanGenerator;
/*  33 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  34 */     return returnValue;
/*     */   }
/*     */   
/*     */   public void setObject(Object obj)
/*     */   {
/*  39 */     Boolean v = (Boolean)obj;
/*  40 */     super.setObject(v);
/*     */   }
/*     */   
/*     */   public Object getObject()
/*     */   {
/*  45 */     return (Boolean)this.object;
/*     */   }
/*     */   
/*     */   public void generateRandom()
/*     */   {
/*  50 */     Random random = new Random();
/*  51 */     generateRandom(random);
/*     */   }
/*     */   
/*     */   public void generateRandom(Random random) {
/*  55 */     if ((random.nextInt(100) < 5) && (!this.clazz.equals(Boolean.TYPE))) {
/*  56 */       setObject(null);
/*  57 */       return;
/*     */     }
/*  59 */     setObject(Boolean.valueOf(random.nextBoolean()));
/*  60 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  66 */     int hash = getClass().hashCode();
/*  67 */     hash = 0;
/*  68 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/*  73 */     return super.equals(other);
/*     */   }
/*     */   
/*     */ 
/*     */   public void mutate()
/*     */   {
/*  79 */     generateRandom();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  84 */     if (this.object == null)
/*  85 */       return "null";
/*  86 */     return ((Boolean)getObject()).toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/*  92 */     List<Statement> returnList = new ArrayList();
/*  93 */     VariableDeclarationFragment varDec = ast.newVariableDeclarationFragment();
/*  94 */     varDec.setName(ast.newSimpleName(varName + pName));
/*     */     
/*  96 */     if (this.object != null) {
/*  97 */       BooleanLiteral boolLiteral = ast.newBooleanLiteral(((Boolean)this.object).booleanValue());
/*  98 */       varDec.setInitializer(boolLiteral);
/*     */     } else {
/* 100 */       NullLiteral boolLiteral = ast.newNullLiteral();
/* 101 */       varDec.setInitializer(boolLiteral);
/*     */     }
/*     */     
/*     */ 
/* 105 */     VariableDeclarationStatement varDecStat = ast.newVariableDeclarationStatement(varDec);
/* 106 */     if (this.clazz.equals(Boolean.TYPE)) {
/* 107 */       varDecStat.setType(ast.newPrimitiveType(ASTEditor.getPrimitiveCode(this.clazz)));
/*     */     } else {
/* 109 */       varDecStat.setType(ast.newSimpleType(ast.newSimpleName(this.clazz.getSimpleName())));
/*     */     }
/* 111 */     returnList.add(varDecStat);
/* 112 */     return returnList;
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 117 */     BooleanGenerator newGene = new BooleanGenerator(this.parent, this.clazz);
/* 118 */     if (this.object != null)
/* 119 */       newGene.object = Boolean.valueOf(((Boolean)this.object).booleanValue());
/* 120 */     newGene.fitness = this.fitness;
/* 121 */     newGene.clazz = this.clazz;
/* 122 */     newGene.variableBinding = this.variableBinding;
/* 123 */     newGene.random = this.random;
/* 124 */     newGene.seed = this.seed;
/*     */     
/* 126 */     return newGene;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\BooleanGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */