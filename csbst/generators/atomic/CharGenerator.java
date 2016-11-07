/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.analysis.LittralConstantAnalyser;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.testing.JTE;
/*     */ import csbst.utils.ASTEditor;
/*     */ import csbst.utils.RandomStringGenerator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.CharacterLiteral;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
/*     */ 
/*     */ public class CharGenerator extends AbstractPrimitive<Character>
/*     */ {
/*     */   public CharGenerator(AbsractGenerator parent, Class cls)
/*     */   {
/*  22 */     super(parent, cls);
/*     */     
/*  24 */     this.absolutuBound = Character.valueOf(65535);
/*  25 */     this.absolutlBound = Character.valueOf('\000');
/*  26 */     this.uBound = ((Character)this.absolutuBound);
/*  27 */     this.lBound = ((Character)this.absolutlBound);
/*  28 */     generateRandom();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  34 */     boolean returnValue = false;
/*  35 */     returnValue = gene instanceof CharGenerator;
/*  36 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  37 */     return returnValue;
/*     */   }
/*     */   
/*     */   public void setObject(Object obj)
/*     */   {
/*  42 */     Character v = (Character)obj;
/*  43 */     super.setObject(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setlBound(Character l)
/*     */   {
/*  50 */     this.lBound = l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setuBound(Character u)
/*     */   {
/*  57 */     this.uBound = u;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void generateRandom()
/*     */   {
/*  64 */     Random random = new Random();
/*  65 */     generateRandom(random);
/*     */   }
/*     */   
/*     */   public void generateRandom(Random random) {
/*  69 */     if ((random.nextInt(100) < 5) && (!this.clazz.equals(Character.TYPE))) {
/*  70 */       setObject(null);
/*  71 */       return;
/*     */     }
/*     */     
/*  74 */     if (random.nextInt(100) < 90) {
/*  75 */       int probability = random.nextInt(100);
/*  76 */       if (((probability < 5) && (JTE.litteralConstantAnalyser.getCharacterConstants().size() > 0)) || (
/*  77 */         (probability < 20) && (JTE.litteralConstantAnalyser.getCharacterConstants().size() > 10))) {
/*  78 */         int index = random.nextInt(JTE.litteralConstantAnalyser.getCharacterConstants().size());
/*     */         
/*     */ 
/*  81 */         setObject((Character)JTE.litteralConstantAnalyser.getCharacterConstants().get(index));
/*  82 */         return;
/*     */       }
/*  84 */       String str = new String();
/*  85 */       while ((str == null) || (str.length() < 1)) {
/*  86 */         RandomStringGenerator randStr = new RandomStringGenerator(1, random.nextBoolean(), random.nextBoolean(), 
/*  87 */           random.nextBoolean(), random.nextBoolean(), random.nextBoolean(), random.nextBoolean());
/*     */         
/*  89 */         str = randStr.getRandomString();
/*     */       }
/*     */       
/*  92 */       setObject(Character.valueOf(str.charAt(0)));
/*     */     }
/*     */     else {
/*  95 */       char val = (char)(((Character)this.lBound).charValue() + random.nextInt(((Character)this.uBound).charValue() - ((Character)this.lBound).charValue() + 1));
/*  96 */       Character valChar = Character.valueOf(val);
/*  97 */       setObject(valChar);
/*     */     }
/*  99 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 105 */     int hash = getClass().hashCode();
/* 106 */     if (this.object == null) {
/* 107 */       return 1;
/*     */     }
/* 109 */     hash = (hash << 1 | hash >>> 31) ^ ((Character)getObject()).charValue();
/* 110 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 115 */     return super.equals(other);
/*     */   }
/*     */   
/*     */   public void mutate()
/*     */   {
/* 120 */     generateRandom();
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 125 */     CharGenerator newGene = new CharGenerator(this.parent, this.clazz);
/* 126 */     if (this.object != null)
/* 127 */       newGene.object = Character.valueOf(((Character)getObject()).charValue());
/* 128 */     newGene.fitness = this.fitness;
/* 129 */     newGene.clazz = this.clazz;
/* 130 */     newGene.variableBinding = this.variableBinding;
/* 131 */     newGene.random = this.random;
/* 132 */     newGene.seed = this.seed;
/*     */     
/* 134 */     return newGene;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/* 141 */     List<Statement> returnList = new ArrayList();
/* 142 */     VariableDeclarationFragment varDec = ast.newVariableDeclarationFragment();
/* 143 */     varDec.setName(ast.newSimpleName(varName + pName));
/*     */     
/*     */ 
/* 146 */     if (this.object != null) {
/* 147 */       CharacterLiteral charLiteral = ast.newCharacterLiteral();
/* 148 */       charLiteral.setCharValue(((Character)getObject()).charValue());
/* 149 */       varDec.setInitializer(charLiteral);
/*     */     } else {
/* 151 */       org.eclipse.jdt.core.dom.NullLiteral charNull = ast.newNullLiteral();
/* 152 */       varDec.setInitializer(charNull);
/*     */     }
/*     */     
/* 155 */     VariableDeclarationStatement varDecStat = ast.newVariableDeclarationStatement(varDec);
/*     */     
/* 157 */     if (this.clazz.equals(Character.TYPE)) {
/* 158 */       varDecStat.setType(ast.newPrimitiveType(ASTEditor.getPrimitiveCode(this.clazz)));
/*     */     } else {
/* 160 */       varDecStat.setType(ast.newSimpleType(ast.newSimpleName(this.clazz.getSimpleName())));
/*     */     }
/* 162 */     returnList.add(varDecStat);
/* 163 */     return returnList;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 168 */     if (this.object == null) {
/* 169 */       return "null";
/*     */     }
/*     */     
/* 172 */     return toUnicode(((Character)getObject()).charValue());
/*     */   }
/*     */   
/*     */   private static String toUnicode(char ch) {
/* 176 */     String s = String.format("\\u%04x", new Object[] { Integer.valueOf(ch) });
/* 177 */     return s;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\CharGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */