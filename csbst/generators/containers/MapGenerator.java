/*     */ package csbst.generators.containers;
/*     */ 
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.generators.dynamic.InstanceGenerator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapGenerator
/*     */   extends AbsractGenerator
/*     */ {
/*     */   public static final int maxLength = 10;
/*  37 */   protected int length = 0;
/*     */   protected boolean isFixedSize;
/*     */   protected AbsractGenerator container;
/*     */   protected Class containerType;
/*     */   protected AbsractGenerator[] keys;
/*     */   protected Class keyType;
/*     */   protected Class cKeyType;
/*     */   protected AbsractGenerator[] values;
/*     */   protected Class valueType;
/*     */   protected Class cValueType;
/*     */   
/*     */   public MapGenerator(AbsractGenerator parent, int l, Class containerType, Class keyType, Class valueType) {
/*  49 */     super(parent, containerType);
/*  50 */     this.length = l;
/*  51 */     if (l != 0) {
/*  52 */       this.isFixedSize = true;
/*     */     } else {
/*  54 */       this.isFixedSize = false;
/*     */     }
/*  56 */     this.keyType = keyType;
/*  57 */     this.valueType = keyType;
/*  58 */     this.containerType = containerType;
/*     */     
/*  60 */     generateRandom();
/*     */   }
/*     */   
/*     */   public void generateRandom()
/*     */   {
/*  65 */     this.container = createAdequateGene(this, this.containerType);
/*  66 */     this.container.generateRandom();
/*     */     
/*  68 */     if (this.container.getObject() == null) {
/*  69 */       setObject(null);
/*  70 */       this.keys = null;
/*  71 */       this.length = 0;
/*  72 */       return;
/*     */     }
/*     */     
/*  75 */     this.cKeyType = this.keyType;
/*  76 */     if (this.keyType.equals(Object.class)) {
/*  77 */       Random rand = new Random();
/*  78 */       int muProp = rand.nextInt(100);
/*  79 */       if (muProp < 90) {
/*  80 */         rand = new Random();
/*  81 */         int index = rand.nextInt(InstanceGenerator.defaultClassesSet.size());
/*  82 */         this.cKeyType = ((Class)InstanceGenerator.defaultClassesSet.get(index));
/*     */       }
/*     */     }
/*     */     
/*  86 */     this.cValueType = this.valueType;
/*  87 */     if (this.valueType.equals(Object.class)) {
/*  88 */       Random rand = new Random();
/*  89 */       int muProp = rand.nextInt(100);
/*  90 */       if (muProp < 90) {
/*  91 */         rand = new Random();
/*  92 */         int index = rand.nextInt(InstanceGenerator.defaultClassesSet.size());
/*  93 */         this.cValueType = ((Class)InstanceGenerator.defaultClassesSet.get(index));
/*     */       }
/*     */     }
/*     */     
/*  97 */     if (!this.isFixedSize) {
/*  98 */       int probability = this.random.nextInt(100);
/*  99 */       if (probability < 5) {
/* 100 */         this.length = 0;
/*     */       } else {
/* 102 */         Random rand = new Random();
/* 103 */         this.length = rand.nextInt(10);
/*     */       }
/*     */     }
/*     */     
/* 107 */     this.keys = new AbsractGenerator[this.length];
/* 108 */     this.values = new AbsractGenerator[this.length];
/*     */     
/*     */ 
/* 111 */     for (int i = 0; i < this.length; i++) {
/* 112 */       this.keys[i] = createAdequateGene(this, this.cKeyType);
/* 113 */       this.keys[i].generateRandom();
/*     */       
/* 115 */       this.values[i] = createAdequateGene(this, this.cValueType);
/* 116 */       this.values[i].generateRandom();
/*     */     }
/*     */     
/*     */ 
/* 120 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/* 129 */     List<Statement> returnList = new ArrayList();
/* 130 */     String newVarNameContainer = varName + pName;
/* 131 */     returnList.addAll(this.container.getStatements(ast, newVarNameContainer, ""));
/*     */     
/*     */ 
/*     */ 
/* 135 */     for (int i = 0; i < this.length; i++) {
/* 136 */       String keyVarName = newVarNameContainer + "K" + (i + 1);
/* 137 */       returnList.addAll(this.keys[i].getStatements(ast, keyVarName, ""));
/* 138 */       String valueVarName = newVarNameContainer + "V" + (i + 1);
/* 139 */       returnList.addAll(this.values[i].getStatements(ast, valueVarName, ""));
/*     */       
/* 141 */       MethodInvocation invokeAsList = ast.newMethodInvocation();
/* 142 */       invokeAsList.setExpression(ast.newSimpleName(newVarNameContainer));
/* 143 */       invokeAsList.setName(ast.newSimpleName("put"));
/* 144 */       invokeAsList.arguments().add(ast.newSimpleName(keyVarName));
/* 145 */       invokeAsList.arguments().add(ast.newSimpleName(valueVarName));
/* 146 */       Statement statement = ast.newExpressionStatement(invokeAsList);
/* 147 */       returnList.add(statement);
/*     */     }
/*     */     
/* 150 */     return returnList;
/*     */   }
/*     */   
/*     */   public Object getObject()
/*     */   {
/* 155 */     Map c = (Map)this.container.getObject();
/* 156 */     if (c == null) {
/* 157 */       return c;
/*     */     }
/* 159 */     c.clear();
/* 160 */     for (int i = 0; i < this.length; i++) {
/* 161 */       if (this.keys[i].getObject() != null) {
/*     */         try {
/* 163 */           c.put(this.keys[i].getObject(), this.values[i].getObject());
/*     */         }
/*     */         catch (Exception localException) {}
/*     */       }
/*     */     }
/* 168 */     this.object = c;
/* 169 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 175 */     int hash = getClass().hashCode();
/*     */     
/* 177 */     hash = hash << 1 | hash >>> 31;
/* 178 */     if (this.keys == null)
/* 179 */       return 0;
/* 180 */     for (int x = 0; x < this.keys.length; x++) {
/* 181 */       if (this.keys[x] != null)
/* 182 */         hash = (hash << 1 | hash >>> 31) ^ this.keys[x].hashCode();
/*     */     }
/* 184 */     return hash;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 189 */     String str = new String();
/* 190 */     str = "[";
/* 191 */     if (this.keys != null)
/* 192 */       for (int i = 0; i < this.keys.length; i++) {
/* 193 */         str = str + this.keys[i].toString();
/* 194 */         if (i < this.length - 1)
/* 195 */           str = str + ",";
/*     */       }
/* 197 */     str = str + "]";
/*     */     
/* 199 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */   public void mutate()
/*     */   {
/* 205 */     int muProp = this.random.nextInt(100);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 210 */     int muConProp = this.random.nextInt(100);
/*     */     
/* 212 */     if ((!this.isFixedSize) && (muConProp < 50)) {
/* 213 */       generateRandom();
/* 214 */       return;
/*     */     }
/*     */     
/* 217 */     if (this.length > 0) {
/* 218 */       int mutPb = 100 / this.length;
/* 219 */       for (int i = 0; i < this.length; i++) {
/* 220 */         if (this.random.nextInt(100) <= mutPb) {
/* 221 */           this.keys[i].mutate();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene) {
/* 228 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\containers\MapGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */