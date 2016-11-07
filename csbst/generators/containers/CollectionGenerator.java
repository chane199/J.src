/*     */ package csbst.generators.containers;
/*     */ 
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.generators.dynamic.InstanceGenerator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.BooleanLiteral;
/*     */ import org.eclipse.jdt.core.dom.CharacterLiteral;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.NumberLiteral;
/*     */ import org.eclipse.jdt.core.dom.Statement;
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
/*     */ public class CollectionGenerator
/*     */   extends AbsractGenerator
/*     */ {
/*     */   public static final int maxLength = 10;
/*  36 */   protected int length = 0;
/*     */   protected boolean isFixedSize;
/*     */   protected AbsractGenerator container;
/*     */   protected AbsractGenerator[] elements;
/*     */   protected Class containerType;
/*     */   protected Class elementType;
/*     */   
/*     */   public CollectionGenerator(AbsractGenerator parent, int l, Class containerType, Class elementType) {
/*  44 */     super(parent, elementType);
/*  45 */     this.length = l;
/*  46 */     if (l != 0) {
/*  47 */       this.isFixedSize = true;
/*     */     } else {
/*  49 */       this.isFixedSize = false;
/*     */     }
/*  51 */     this.elementType = elementType;
/*  52 */     this.containerType = containerType;
/*     */     
/*     */ 
/*     */ 
/*  56 */     generateRandom();
/*     */   }
/*     */   
/*     */   public void generateRandom()
/*     */   {
/*  61 */     this.container = createAdequateGene(this, this.containerType);
/*  62 */     this.container.generateRandom();
/*  63 */     if (this.container.getObject() == null) {
/*  64 */       setObject(null);
/*  65 */       this.elements = null;
/*  66 */       this.length = 0;
/*  67 */       return;
/*     */     }
/*     */     
/*  70 */     if (this.elementType.equals(Object.class)) {
/*  71 */       Random rand = new Random();
/*  72 */       int muProp = rand.nextInt(100);
/*  73 */       if (muProp < 90) {
/*  74 */         rand = new Random();
/*  75 */         int index = rand.nextInt(InstanceGenerator.defaultClassesSet.size());
/*  76 */         this.clazz = ((Class)InstanceGenerator.defaultClassesSet.get(index));
/*     */       }
/*     */       else {
/*  79 */         this.clazz = this.elementType;
/*     */       }
/*     */     }
/*  82 */     if (!this.isFixedSize) {
/*  83 */       int probability = this.random.nextInt(100);
/*  84 */       if (probability < 5) {
/*  85 */         this.length = 0;
/*     */       } else {
/*  87 */         Random rand = new Random();
/*  88 */         this.length = rand.nextInt(10);
/*     */       }
/*     */     }
/*     */     
/*  92 */     this.elements = new AbsractGenerator[this.length];
/*     */     
/*  94 */     for (int i = 0; i < this.length; i++) {
/*  95 */       this.elements[i] = createAdequateGene(this, this.clazz);
/*     */       
/*  97 */       this.elements[i].generateRandom();
/*     */     }
/*     */     
/*     */ 
/* 101 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/* 110 */     List<Statement> returnList = new ArrayList();
/* 111 */     String newVarNameContainer = varName + pName;
/* 112 */     returnList.addAll(this.container.getStatements(ast, newVarNameContainer, ""));
/*     */     
/*     */ 
/*     */ 
/* 116 */     if (this.clazz.isPrimitive()) {
/* 117 */       for (int i = 0; i < this.length; i++) {
/* 118 */         if (this.clazz.equals(Character.TYPE)) {
/* 119 */           CharacterLiteral charLiteral = ast.newCharacterLiteral();
/* 120 */           charLiteral.setCharValue(((Character)this.elements[i].getObject()).charValue());
/*     */           
/* 122 */           MethodInvocation invokeAsList = ast.newMethodInvocation();
/* 123 */           invokeAsList.setExpression(ast.newSimpleName(newVarNameContainer));
/* 124 */           invokeAsList.setName(ast.newSimpleName("add"));
/*     */           
/* 126 */           invokeAsList.arguments().add(charLiteral);
/*     */           
/* 128 */           Statement statement = ast.newExpressionStatement(invokeAsList);
/* 129 */           returnList.add(statement);
/*     */         }
/* 131 */         else if (this.clazz.equals(Boolean.TYPE)) {
/* 132 */           BooleanLiteral boolLiteral = ast.newBooleanLiteral(((Boolean)this.elements[i].getObject()).booleanValue());
/*     */           
/* 134 */           MethodInvocation invokeAsList = ast.newMethodInvocation();
/* 135 */           invokeAsList.setExpression(ast.newSimpleName(newVarNameContainer));
/* 136 */           invokeAsList.setName(ast.newSimpleName("add"));
/* 137 */           invokeAsList.arguments().add(boolLiteral);
/* 138 */           Statement statement = ast.newExpressionStatement(invokeAsList);
/* 139 */           returnList.add(statement);
/*     */         } else {
/* 141 */           NumberLiteral numberLiteral = ast.newNumberLiteral(this.elements[i].toString());
/* 142 */           MethodInvocation invokeAsList = ast.newMethodInvocation();
/* 143 */           invokeAsList.setExpression(ast.newSimpleName(newVarNameContainer));
/* 144 */           invokeAsList.setName(ast.newSimpleName("add"));
/* 145 */           invokeAsList.arguments().add(numberLiteral);
/* 146 */           Statement statement = ast.newExpressionStatement(invokeAsList);
/* 147 */           returnList.add(statement);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 154 */       for (int i = 0; i < this.length; i++) {
/* 155 */         String newVarName = newVarNameContainer + "C" + (i + 1);
/* 156 */         returnList.addAll(this.elements[i].getStatements(ast, newVarName, ""));
/* 157 */         MethodInvocation invokeAsList = ast.newMethodInvocation();
/* 158 */         invokeAsList.setExpression(ast.newSimpleName(newVarNameContainer));
/* 159 */         invokeAsList.setName(ast.newSimpleName("add"));
/* 160 */         invokeAsList.arguments().add(ast.newSimpleName(newVarName));
/* 161 */         Statement statement = ast.newExpressionStatement(invokeAsList);
/* 162 */         returnList.add(statement);
/*     */       }
/*     */     }
/*     */     
/* 166 */     return returnList;
/*     */   }
/*     */   
/*     */   public Object getObject()
/*     */   {
/* 171 */     Collection c = (Collection)this.container.getObject();
/* 172 */     if (c == null) {
/* 173 */       return c;
/*     */     }
/* 175 */     c.clear();
/* 176 */     for (int i = 0; i < this.length; i++) {
/* 177 */       if (this.elements[i].getObject() != null) {
/*     */         try {
/* 179 */           c.add(this.elements[i].getObject());
/*     */         }
/*     */         catch (Exception localException) {}
/*     */       }
/*     */     }
/* 184 */     this.object = c;
/* 185 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 191 */     int hash = getClass().hashCode();
/*     */     
/* 193 */     hash = hash << 1 | hash >>> 31;
/* 194 */     if (this.elements == null)
/* 195 */       return 0;
/* 196 */     for (int x = 0; x < this.elements.length; x++) {
/* 197 */       if (this.elements[x] != null)
/* 198 */         hash = (hash << 1 | hash >>> 31) ^ this.elements[x].hashCode();
/*     */     }
/* 200 */     return hash;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 205 */     String str = new String();
/* 206 */     str = "[";
/* 207 */     if (this.elements != null)
/* 208 */       for (int i = 0; i < this.elements.length; i++) {
/* 209 */         str = str + this.elements[i].toString();
/* 210 */         if (i < this.length - 1)
/* 211 */           str = str + ",";
/*     */       }
/* 213 */     str = str + "]";
/*     */     
/* 215 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */   public void mutate()
/*     */   {
/* 221 */     int muProp = this.random.nextInt(100);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 226 */     int muConProp = this.random.nextInt(100);
/*     */     
/* 228 */     if ((!this.isFixedSize) && (muConProp < 50)) {
/* 229 */       generateRandom();
/* 230 */       return;
/*     */     }
/*     */     
/* 233 */     if (this.length > 0) {
/* 234 */       int mutPb = 100 / this.length;
/* 235 */       for (int i = 0; i < this.length; i++) {
/* 236 */         if (this.random.nextInt(100) <= mutPb) {
/* 237 */           this.elements[i].mutate();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene) {
/* 244 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\containers\CollectionGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */