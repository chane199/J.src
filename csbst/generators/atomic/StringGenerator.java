/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.analysis.LittralConstantAnalyser;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.testing.JTE;
/*     */ import csbst.utils.RandomStringGenerator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ClassInstanceCreation;
/*     */ import org.eclipse.jdt.core.dom.NullLiteral;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.StringLiteral;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
/*     */ 
/*     */ public class StringGenerator
/*     */   extends AbsractGenerator
/*     */ {
/*  22 */   public final int MAX_LENGTH = 255;
/*  23 */   public final int AVG_LENGTH = 255;
/*  24 */   private int length = 0;
/*     */   
/*     */   private final boolean isFixedSize;
/*     */   
/*     */   public StringGenerator(AbsractGenerator parent, int l)
/*     */   {
/*  30 */     super(parent, String.class);
/*  31 */     this.length = l;
/*  32 */     if (l != 0) {
/*  33 */       this.isFixedSize = true;
/*     */     } else
/*  35 */       this.isFixedSize = false;
/*  36 */     this.clazz = String.class;
/*  37 */     generateRandom();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  43 */     boolean returnValue = false;
/*  44 */     returnValue = gene instanceof StringGenerator;
/*  45 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  46 */     return returnValue;
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
/*     */   public void generateRandom()
/*     */   {
/*  59 */     if (this.random.nextInt(100) < 5) {
/*  60 */       setObject(null);
/*  61 */       this.length = 0;
/*  62 */       this.object = null;
/*  63 */       return;
/*     */     }
/*     */     
/*     */ 
/*  67 */     Random rand = new Random();
/*  68 */     if (!this.isFixedSize) {
/*  69 */       int probability = rand.nextInt(100);
/*  70 */       if (((probability < 5) && (JTE.litteralConstantAnalyser.getStringConstants().size() > 0)) || (
/*  71 */         (probability < 20) && (JTE.litteralConstantAnalyser.getStringConstants().size() > 10)))
/*     */       {
/*  73 */         int index = rand.nextInt(JTE.litteralConstantAnalyser.getStringConstants().size());
/*     */         
/*  75 */         this.object = JTE.litteralConstantAnalyser.getStringConstants().get(index);
/*  76 */         this.length = ((String)JTE.litteralConstantAnalyser.getStringConstants().get(index)).length();
/*  77 */         return;
/*     */       }
/*     */       
/*  80 */       probability = rand.nextInt(100);
/*  81 */       if (((probability < 105) && (JTE.litteralConstantAnalyser.getStringConstants().size() > 0)) || (
/*  82 */         (probability < 20) && (JTE.litteralConstantAnalyser.getStringConstants().size() > 10)))
/*     */       {
/*  84 */         ObjectGenerator newObj = new ObjectGenerator(this);
/*     */         
/*  86 */         if (newObj.getObject() != null) {
/*     */           try {
/*  88 */             String str = newObj.getObject().toString();
/*  89 */             this.object = str;
/*  90 */             this.length = newObj.toString().length();
/*  91 */             return;
/*     */           }
/*     */           catch (Exception localException) {}
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  98 */       if (probability < 5) {
/*  99 */         this.length = 0;
/*     */       }
/* 101 */       else if (this.random.nextInt(100) < 50) {
/* 102 */         this.length = rand.nextInt(255);
/*     */       } else {
/* 104 */         this.length = rand.nextInt(255);
/*     */       }
/*     */     }
/* 107 */     String str = new String();
/*     */     
/* 109 */     int probability = this.random.nextInt(100);
/* 110 */     RandomStringGenerator randStr; RandomStringGenerator randStr; if (((probability < 5) && (JTE.litteralConstantAnalyser.getCharacterConstants().size() > 0)) || (
/* 111 */       (probability < 20) && (JTE.litteralConstantAnalyser.getCharacterConstants().size() > 10)))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 116 */       String vectorAsConcatenatedString = "";
/* 117 */       for (Character c : JTE.litteralConstantAnalyser.getCharacterConstants()) {
/* 118 */         vectorAsConcatenatedString = vectorAsConcatenatedString + c;
/*     */       }
/* 120 */       randStr = new RandomStringGenerator(this.length, rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(), 
/* 121 */         rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(), vectorAsConcatenatedString);
/*     */     } else {
/* 123 */       randStr = new RandomStringGenerator(this.length, rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(), 
/* 124 */         rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean());
/*     */     }
/* 126 */     this.object = randStr.getRandomString();
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
/*     */   public void mutate()
/*     */   {
/* 139 */     generateRandom();
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
/*     */   public Object clone()
/*     */   {
/* 157 */     StringGenerator newStr = new StringGenerator(this.parent, this.length);
/* 158 */     newStr.clazz = this.clazz;
/* 159 */     newStr.variableBinding = this.variableBinding;
/* 160 */     newStr.fitness = this.fitness;
/* 161 */     newStr.object = this.object;
/* 162 */     newStr.seed = this.seed;
/* 163 */     newStr.random = this.random;
/*     */     
/* 165 */     return newStr;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getObject()
/*     */   {
/* 171 */     return this.object;
/*     */   }
/*     */   
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/* 176 */     List<Statement> returnList = new ArrayList();
/*     */     
/* 178 */     VariableDeclarationFragment string = ast.newVariableDeclarationFragment();
/* 179 */     string.setName(ast.newSimpleName(varName));
/* 180 */     if (this.object != null) {
/* 181 */       ClassInstanceCreation classInstance = ast.newClassInstanceCreation();
/* 182 */       classInstance.setType(ast.newSimpleType(ast.newSimpleName(this.clazz.getSimpleName())));
/* 183 */       StringLiteral sl = ast.newStringLiteral();
/* 184 */       sl.setLiteralValue(((String)this.object).replace("\\", "\\\\"));
/* 185 */       classInstance.arguments().add(sl);
/* 186 */       string.setInitializer(classInstance);
/*     */     } else {
/* 188 */       NullLiteral nl = ast.newNullLiteral();
/* 189 */       string.setInitializer(nl);
/*     */     }
/*     */     
/* 192 */     VariableDeclarationStatement stringStmt = ast.newVariableDeclarationStatement(string);
/* 193 */     stringStmt.setType(ast.newSimpleType(ast.newSimpleName(this.clazz.getSimpleName())));
/*     */     
/* 195 */     returnList.add(stringStmt);
/* 196 */     return returnList;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 202 */     int hash = getClass().hashCode();
/*     */     
/* 204 */     hash = hash << 1 | hash >>> 31;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 210 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 216 */     return "String";
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\StringGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */