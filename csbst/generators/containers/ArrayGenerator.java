/*     */ package csbst.generators.containers;
/*     */ 
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.utils.ASTEditor;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.ArrayCreation;
/*     */ import org.eclipse.jdt.core.dom.ArrayInitializer;
/*     */ import org.eclipse.jdt.core.dom.BooleanLiteral;
/*     */ import org.eclipse.jdt.core.dom.CharacterLiteral;
/*     */ import org.eclipse.jdt.core.dom.NullLiteral;
/*     */ import org.eclipse.jdt.core.dom.NumberLiteral;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.Type;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayGenerator
/*     */   extends AbsractGenerator
/*     */ {
/*     */   public static final int maxLength = 10;
/*  31 */   protected int length = 0;
/*     */   protected boolean isFixedSize;
/*     */   protected AbsractGenerator[] elements;
/*     */   
/*     */   public ArrayGenerator(AbsractGenerator parent, int l, Class type) {
/*  36 */     super(parent, type);
/*  37 */     this.length = l;
/*  38 */     if (l != 0) {
/*  39 */       this.isFixedSize = true;
/*     */     } else {
/*  41 */       this.isFixedSize = false;
/*     */     }
/*  43 */     if (type.equals(Object.class)) {
/*  44 */       type = DefaultGenericType;
/*     */     }
/*  46 */     this.clazz = type;
/*  47 */     generateRandom();
/*     */   }
/*     */   
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  52 */     boolean returnValue = false;
/*  53 */     returnValue = gene instanceof ArrayGenerator;
/*  54 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  55 */     return returnValue;
/*     */   }
/*     */   
/*     */   public void generateRandom()
/*     */   {
/*  60 */     if (this.random.nextInt(100) < 5) {
/*  61 */       setObject(null);
/*  62 */       this.elements = null;
/*  63 */       this.length = 0;
/*  64 */       return;
/*     */     }
/*     */     
/*  67 */     if (!this.isFixedSize) {
/*  68 */       int probability = this.random.nextInt(100);
/*  69 */       if (probability < 5) {
/*  70 */         this.length = 0;
/*     */       } else {
/*  72 */         Random rand = new Random();
/*  73 */         this.length = rand.nextInt(10);
/*     */       }
/*     */     }
/*     */     
/*  77 */     this.elements = new AbsractGenerator[this.length];
/*  78 */     if (this.length > 0) {
/*  79 */       for (int i = 0; i < this.length; i++) {
/*  80 */         this.elements[i] = createAdequateGene(this, this.clazz);
/*  81 */         this.elements[i].generateRandom();
/*     */       }
/*     */     }
/*     */     
/*  85 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public void mutate()
/*     */   {
/*  91 */     int muProp = this.random.nextInt(100);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  96 */     int muConProp = this.random.nextInt(100);
/*     */     
/*  98 */     if ((!this.isFixedSize) && (muConProp < 50)) {
/*  99 */       generateRandom();
/* 100 */       return;
/*     */     }
/*     */     
/* 103 */     if (this.length > 0) {
/* 104 */       int mutPb = 100 / this.length;
/* 105 */       for (int i = 0; i < this.length; i++) {
/* 106 */         if (this.random.nextInt(100) <= mutPb)
/* 107 */           this.elements[i].mutate();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 113 */     ArrayGenerator newArray = new ArrayGenerator(this.parent, this.length, this.clazz);
/* 114 */     newArray.clazz = this.clazz;
/* 115 */     newArray.variableBinding = this.variableBinding;
/* 116 */     newArray.fitness = this.fitness;
/* 117 */     newArray.object = this.object;
/* 118 */     newArray.seed = this.seed;
/* 119 */     newArray.random = this.random;
/* 120 */     newArray.length = this.length;
/* 121 */     newArray.isFixedSize = this.isFixedSize;
/*     */     
/* 123 */     if (this.length > 0) {
/* 124 */       newArray.elements = new AbsractGenerator[this.length];
/* 125 */       for (int i = 0; i < this.length; i++)
/* 126 */         newArray.elements[i] = ((AbsractGenerator)this.elements[i].clone());
/*     */     }
/* 128 */     return newArray;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getObject()
/*     */   {
/* 134 */     if (this.elements == null) {
/* 135 */       return null;
/*     */     }
/* 137 */     Object retVal = Array.newInstance(this.clazz, this.length);
/* 138 */     for (int i = 0; i < this.length; i++) {
/*     */       try {
/* 140 */         if (this.elements[i].getObject() != null)
/* 141 */           Array.set(retVal, i, this.elements[i].getObject());
/*     */       } catch (Exception e) {
/* 143 */         System.err.println("Array Error :" + this.elements[i].getObject() + " class :" + this.clazz);
/* 144 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 147 */     return retVal;
/*     */   }
/*     */   
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/* 152 */     List<Statement> returnList = new ArrayList();
/*     */     
/* 154 */     VariableDeclarationFragment myVar = ast.newVariableDeclarationFragment();
/* 155 */     myVar.setName(ast.newSimpleName(varName));
/*     */     
/* 157 */     VariableDeclarationStatement myVarDec = ast.newVariableDeclarationStatement(myVar);
/*     */     
/* 159 */     if (this.clazz.isPrimitive()) {
/* 160 */       myVarDec.setType(ast.newArrayType(ast.newPrimitiveType(ASTEditor.getPrimitiveCode(this.clazz))));
/* 161 */       if (getObject() == null) {
/* 162 */         NullLiteral nullLiteral = ast.newNullLiteral();
/* 163 */         myVar.setInitializer(nullLiteral);
/*     */       } else {
/* 165 */         ArrayCreation aCreation = ast.newArrayCreation();
/* 166 */         myVar.setInitializer(aCreation);
/* 167 */         ArrayInitializer aInitializer = ast.newArrayInitializer();
/* 168 */         aCreation.setType(ast.newArrayType(ast.newPrimitiveType(ASTEditor.getPrimitiveCode(this.clazz))));
/* 169 */         for (int i = 0; i < this.length; i++) {
/* 170 */           if (this.clazz.equals(Character.TYPE)) {
/* 171 */             CharacterLiteral charLiteral = ast.newCharacterLiteral();
/* 172 */             charLiteral.setCharValue(((Character)this.elements[i].getObject()).charValue());
/* 173 */             aInitializer.expressions().add(charLiteral);
/* 174 */           } else if (this.clazz.equals(Boolean.TYPE)) {
/* 175 */             BooleanLiteral boolLiteral = ast.newBooleanLiteral(((Boolean)this.elements[i].getObject()).booleanValue());
/* 176 */             aInitializer.expressions().add(boolLiteral);
/*     */           } else {
/* 178 */             NumberLiteral numberLiteral = ast.newNumberLiteral(this.elements[i].toString());
/* 179 */             aInitializer.expressions().add(numberLiteral);
/*     */           }
/*     */         }
/* 182 */         aCreation.setInitializer(aInitializer);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 187 */       Type TypeName = getType2UseInJunitClass(this.clazz, ast);
/*     */       
/*     */ 
/* 190 */       Type TypeNameCopy = (Type)ASTNode.copySubtree(ast, TypeName);
/* 191 */       myVarDec.setType(ast.newArrayType(TypeNameCopy));
/*     */       
/* 193 */       if (getObject() == null) {
/* 194 */         NullLiteral nullLiteral = ast.newNullLiteral();
/* 195 */         myVar.setInitializer(nullLiteral);
/*     */       }
/*     */       else {
/* 198 */         ArrayCreation aCreation = ast.newArrayCreation();
/* 199 */         myVar.setInitializer(aCreation);
/* 200 */         ArrayInitializer aInitializer = ast.newArrayInitializer();
/*     */         
/* 202 */         aCreation.setType(ast.newArrayType(TypeName));
/* 203 */         for (int i = 0; i < this.length; i++) {
/* 204 */           String newVarName = varName + pName + "P" + (i + 1);
/* 205 */           returnList.addAll(this.elements[i].getStatements(ast, newVarName, ""));
/* 206 */           aInitializer.expressions().add(ast.newSimpleName(newVarName));
/*     */         }
/* 208 */         aCreation.setInitializer(aInitializer);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 213 */     returnList.add(myVarDec);
/*     */     
/* 215 */     return returnList;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 221 */     int hash = getClass().hashCode();
/*     */     
/* 223 */     hash = hash << 1 | hash >>> 31;
/* 224 */     if (this.elements == null)
/* 225 */       return 0;
/* 226 */     for (int x = 0; x < this.elements.length; x++) {
/* 227 */       if (this.elements[x] != null)
/* 228 */         hash = (hash << 1 | hash >>> 31) ^ this.elements[x].hashCode();
/*     */     }
/* 230 */     return hash;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 235 */     String str = new String();
/* 236 */     str = "[";
/* 237 */     if (this.elements != null)
/* 238 */       for (int i = 0; i < this.elements.length; i++) {
/* 239 */         str = str + this.elements[i].toString();
/* 240 */         if (i < this.length - 1)
/* 241 */           str = str + ",";
/*     */       }
/* 243 */     str = str + "]";
/*     */     
/* 245 */     return str;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\containers\ArrayGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */