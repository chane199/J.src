/*     */ package csbst.generators;
/*     */ 
/*     */ import csbst.analysis.String2Expression;
/*     */ import csbst.generators.atomic.AbstractPrimitive;
/*     */ import csbst.generators.atomic.ObjectGenerator;
/*     */ import csbst.generators.atomic.StringGenerator;
/*     */ import csbst.generators.containers.ArrayGenerator;
/*     */ import csbst.generators.containers.CollectionGenerator;
/*     */ import csbst.generators.containers.MapGenerator;
/*     */ import csbst.generators.dynamic.InstanceGenerator;
/*     */ import csbst.testing.JTE;
/*     */ import csbst.testing.fitness.TestingFitness;
/*     */ import csbst.utils.ASTEditor;
/*     */ import csbst.utils.ClassLoaderUtil;
/*     */ import ec.EvolutionState;
/*     */ import ec.vector.VectorGene;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.CatchClause;
/*     */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*     */ import org.eclipse.jdt.core.dom.Name;
/*     */ import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
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
/*     */ public abstract class AbsractGenerator
/*     */   extends VectorGene
/*     */ {
/*  47 */   protected IVariableBinding variableBinding = null;
/*     */   protected Class clazz;
/*     */   protected Class stub;
/*     */   protected TestingFitness fitness;
/*     */   protected Object object;
/*     */   protected long seed;
/*     */   protected Random random;
/*     */   protected AbsractGenerator parent;
/*  55 */   protected boolean isNullAccepted = true;
/*     */   
/*  57 */   public static final Class DefaultGenericType = Integer.class;
/*  58 */   public static final Class defaultClass = Integer.class;
/*     */   
/*     */   public static final int SEEDING_MAX_PROBABILITY = 20;
/*     */   
/*     */   public static final int SEEDING_MIN_PROBABILITY = 5;
/*     */   
/*     */   public static final int SEEDING_NULL_PROBABILITY = 5;
/*     */   public static final int SEEDING_MAX_NUMBER = 10;
/*     */   public static final int SEEDING_MIN_NUMBER = 0;
/*     */   private static Block CATCH_BLOCK;
/*     */   
/*     */   static
/*     */   {
/*  71 */     if (JTE.ExceptionsOriented) {
/*  72 */       String expression = "ExceptionsFormatter.printException(exce,\"" + JTE.projectPackagesPrefix + "\",\"" + JTE.className + "\");";
/*  73 */       CATCH_BLOCK = String2Expression.getInfixExpression(expression);
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract void generateRandom();
/*     */   
/*     */   public abstract void mutate();
/*     */   
/*     */   public abstract List<Statement> getStatements(AST paramAST, String paramString1, String paramString2);
/*     */   
/*     */   public AbsractGenerator(AbsractGenerator p, Class clazz) {
/*  84 */     this.random = new Random();
/*  85 */     this.parent = p;
/*  86 */     this.clazz = clazz;
/*  87 */     this.stub = clazz;
/*     */   }
/*     */   
/*     */   public AbsractGenerator getParent() {
/*  91 */     return this.parent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getObject()
/*     */   {
/*  98 */     return this.object;
/*     */   }
/*     */   
/*     */   public void setObject(Object obj) {
/* 102 */     this.object = obj;
/*     */   }
/*     */   
/*     */   public Class getClazz() {
/* 106 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public TestingFitness getFitness() {
/* 110 */     return this.fitness;
/*     */   }
/*     */   
/*     */   public IVariableBinding getVariableBinding()
/*     */   {
/* 115 */     return this.variableBinding;
/*     */   }
/*     */   
/*     */   public void setVariableBinding(IVariableBinding variableBinding) {
/* 119 */     this.variableBinding = variableBinding;
/*     */   }
/*     */   
/*     */   public void reset(EvolutionState state, int thread)
/*     */   {
/* 124 */     generateRandom();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 129 */     boolean areEqual = this.object.equals(((AbsractGenerator)other).getObject());
/* 130 */     areEqual = (areEqual) && (this.clazz.equals(((AbsractGenerator)other).getClazz()));
/* 131 */     areEqual = (areEqual) && (this.fitness.equals(((AbsractGenerator)other).getFitness()));
/* 132 */     return areEqual;
/*     */   }
/*     */   
/*     */   protected AbsractGenerator createAdequateGene(AbsractGenerator parent, Class cls) {
/* 136 */     return createAdequateGene(parent, cls, true);
/*     */   }
/*     */   
/*     */   protected AbsractGenerator createAdequateGene(AbsractGenerator parent, Class cls, java.lang.reflect.Type type) {
/* 140 */     return createAdequateGene(parent, cls, type, true);
/*     */   }
/*     */   
/*     */   protected AbsractGenerator createAdequateGene(AbsractGenerator parent, Class cls, java.lang.reflect.Type type, boolean withInstance) {
/* 144 */     AbsractGenerator gene = null;
/* 145 */     if ((Collection.class.isAssignableFrom(cls)) || (Map.class.isAssignableFrom(cls))) {
/* 146 */       ParameterizedType type1 = null;
/* 147 */       if ((type instanceof ParameterizedType)) {
/* 148 */         type1 = (ParameterizedType)type;
/*     */       }
/* 150 */       Class clazz = null;
/* 151 */       Class clazzValue = null;
/* 152 */       if ((type1 == null) || (type1.getActualTypeArguments().length == 0)) {
/* 153 */         clazz = Object.class;
/* 154 */         clazzValue = Object.class;
/*     */       }
/*     */       else
/*     */       {
/* 158 */         String nclass = type1.getActualTypeArguments()[0].toString();
/* 159 */         String nclassValue = type1.getActualTypeArguments()[0].toString();
/*     */         
/* 161 */         if (type1.getActualTypeArguments().length > 1) {
/* 162 */           nclassValue = type1.getActualTypeArguments()[1].toString();
/*     */         }
/*     */         
/*     */ 
/* 166 */         String[] nclassHierarchy = nclass.split(" ");
/* 167 */         if ((nclassHierarchy == null) || (nclassHierarchy.length < 1)) {
/* 168 */           clazz = Object.class;
/*     */         } else {
/* 170 */           if (nclassHierarchy.length == 1) {
/* 171 */             nclass = ClassLoaderUtil.toCanonicalName(nclass);
/*     */           } else
/* 173 */             nclass = nclassHierarchy[1];
/*     */           try {
/* 175 */             clazz = JTE.magicClassLoader.loadClass(nclass);
/*     */           } catch (ClassNotFoundException e) {
/* 177 */             clazz = Object.class;
/*     */           }
/*     */         }
/*     */         
/* 181 */         if (Map.class.isAssignableFrom(cls)) {
/* 182 */           if (nclassValue.equals(nclass)) {
/* 183 */             clazzValue = clazz;
/*     */           } else {
/* 185 */             nclassHierarchy = nclassValue.split(" ");
/* 186 */             if ((nclassHierarchy == null) || (nclassHierarchy.length < 1)) {
/* 187 */               clazzValue = Object.class;
/*     */             } else {
/* 189 */               if (nclassHierarchy.length == 1) {
/* 190 */                 nclassValue = ClassLoaderUtil.toCanonicalName(nclassValue);
/*     */               } else {
/* 192 */                 nclassValue = nclassHierarchy[1];
/*     */               }
/*     */               try {
/* 195 */                 clazzValue = JTE.magicClassLoader.loadClass(nclassValue);
/*     */               }
/*     */               catch (ClassNotFoundException e) {
/* 198 */                 clazzValue = Object.class;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 208 */       if (Collection.class.isAssignableFrom(cls)) {
/* 209 */         gene = new CollectionGenerator(parent, 0, cls, clazz);
/*     */       }
/*     */       else {
/* 212 */         gene = new MapGenerator(parent, 0, cls, clazz, clazzValue);
/*     */       }
/*     */     }
/*     */     else {
/* 216 */       gene = createAdequateGene(parent, cls, withInstance);
/*     */     }
/* 218 */     return gene;
/*     */   }
/*     */   
/*     */   protected AbsractGenerator createAdequateGene(AbsractGenerator parent, Class cls, boolean withInstance) {
/*     */     try {
/* 223 */       AbsractGenerator gene = null;
/* 224 */       Class geneCls = AbstractPrimitive.getGeneClass(cls);
/* 225 */       if (geneCls != null)
/*     */       {
/* 227 */         Constructor constructor = geneCls.getConstructor(new Class[] { AbsractGenerator.class, Class.class });
/* 228 */         gene = (AbsractGenerator)constructor.newInstance(new Object[] { parent, cls });
/*     */ 
/*     */       }
/* 231 */       else if (cls.equals(String.class)) {
/* 232 */         gene = new StringGenerator(parent, 0);
/* 233 */       } else if (cls.isArray()) {
/* 234 */         gene = new ArrayGenerator(parent, 0, cls.getComponentType());
/* 235 */       } else if (cls.equals(String.class)) {
/* 236 */         gene = new StringGenerator(parent, 0);
/*     */       }
/* 238 */       else if (cls.equals(Object.class))
/*     */       {
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
/* 253 */         return new ObjectGenerator(parent);
/*     */       }
/*     */       
/* 256 */       return new InstanceGenerator(parent, cls, withInstance);
/*     */     }
/*     */     catch (InstantiationException e)
/*     */     {
/* 260 */       e.printStackTrace();
/*     */     } catch (IllegalAccessException e) {
/* 262 */       e.printStackTrace();
/*     */     } catch (SecurityException e) {
/* 264 */       e.printStackTrace();
/*     */     } catch (NoSuchMethodException e) {
/* 266 */       e.printStackTrace();
/*     */     } catch (IllegalArgumentException e) {
/* 268 */       e.printStackTrace();
/*     */     } catch (InvocationTargetException e) {
/* 270 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/* 274 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isSameFamillyAs(AbsractGenerator paramAbsractGenerator);
/*     */   
/*     */ 
/*     */ 
/*     */   public void defaultCrossover(AbsractGenerator gene)
/*     */   {
/* 286 */     AbsractGenerator tmp = (AbsractGenerator)clone();
/* 287 */     AbsractGenerator tmp1 = (AbsractGenerator)gene.clone();
/* 288 */     this.object = tmp1.object;
/* 289 */     this.clazz = tmp1.clazz;
/* 290 */     this.fitness = tmp1.fitness;
/* 291 */     this.variableBinding = tmp1.variableBinding;
/* 292 */     gene = tmp;
/*     */   }
/*     */   
/*     */   public static org.eclipse.jdt.core.dom.Type getType2UseInJunitClass(Class cls, AST ast) {
/* 296 */     if (cls.isPrimitive()) {
/* 297 */       return ast.newPrimitiveType(ASTEditor.getPrimitiveCode(cls));
/*     */     }
/* 299 */     if (cls.isArray()) {
/* 300 */       return ast.newArrayType(getType2UseInJunitClass(cls.getComponentType(), ast));
/*     */     }
/* 302 */     if (cls.isAnonymousClass())
/*     */     {
/* 304 */       return getType2UseInJunitClass(cls.getEnclosingMethod().getReturnType(), ast);
/*     */     }
/*     */     
/* 307 */     Name qNameClass = getName2UseInJunitClass(cls, ast);
/* 308 */     return ast.newSimpleType(qNameClass);
/*     */   }
/*     */   
/*     */ 
/*     */   public static Name getName2UseInJunitClass(Class cls, AST ast)
/*     */   {
/* 314 */     Name qNameClass = null;
/*     */     
/* 316 */     if ((cls.isLocalClass()) || (cls.isMemberClass())) {
/* 317 */       if (cls.isLocalClass()) {
/* 318 */         for (Class c : JTE.requiredClasses) {
/* 319 */           if ((!c.equals(cls.getEnclosingClass())) && (c.getSimpleName().toString().equals(cls.getEnclosingClass().getSimpleName().toString()))) {
/* 320 */             qNameClass = ASTEditor.generateQualifiedName(cls.getCanonicalName().toString(), ast);
/* 321 */             return qNameClass;
/*     */           }
/*     */         }
/* 324 */         Name qNameClass1 = getName2UseInJunitClass(cls.getEnclosingClass(), ast);
/* 325 */         qNameClass = ast.newQualifiedName(qNameClass1, ast.newSimpleName(cls.getSimpleName()));
/*     */ 
/*     */       }
/* 328 */       else if (cls.isMemberClass()) {
/* 329 */         for (Class c : JTE.requiredClasses) {
/* 330 */           if ((!c.equals(cls.getDeclaringClass())) && (c.getSimpleName().toString().equals(cls.getDeclaringClass().getSimpleName().toString()))) {
/* 331 */             qNameClass = ASTEditor.generateQualifiedName(cls.getCanonicalName().toString(), ast);
/* 332 */             return qNameClass;
/*     */           }
/*     */         }
/* 335 */         Name qNameClass1 = getName2UseInJunitClass(cls.getDeclaringClass(), ast);
/* 336 */         qNameClass = ast.newQualifiedName(qNameClass1, ast.newSimpleName(cls.getSimpleName()));
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 341 */       for (Class c : JTE.requiredClasses) {
/* 342 */         if ((!c.equals(cls)) && (c.getSimpleName().toString().equals(cls.getSimpleName().toString()))) {
/* 343 */           qNameClass = ASTEditor.generateQualifiedName(cls.getName().toString(), ast);
/* 344 */           return qNameClass;
/*     */         }
/*     */       }
/*     */       
/* 348 */       qNameClass = ast.newSimpleName(cls.getSimpleName());
/* 349 */       JTE.requiredClasses.add(cls);
/*     */     }
/*     */     
/* 352 */     return qNameClass;
/*     */   }
/*     */   
/*     */   public static CatchClause getCatchClause(Class exce, AST ast, boolean simple) {
/* 356 */     Class except = exce;
/* 357 */     if (!simple) {
/* 358 */       except = Throwable.class;
/*     */     }
/* 360 */     SingleVariableDeclaration excepVar = ast.newSingleVariableDeclaration();
/* 361 */     excepVar.setName(ast.newSimpleName("exce"));
/* 362 */     org.eclipse.jdt.core.dom.Type TypeName = getType2UseInJunitClass(except, ast);
/* 363 */     excepVar.setType(TypeName);
/*     */     
/* 365 */     CatchClause catchClause = ast.newCatchClause();
/* 366 */     catchClause.setException(excepVar);
/* 367 */     Block b = null;
/* 368 */     if ((CATCH_BLOCK != null) && (!simple)) {
/* 369 */       b = (Block)ASTNode.copySubtree(ast, CATCH_BLOCK);
/* 370 */       catchClause.setBody(b);
/*     */     }
/*     */     
/* 373 */     return catchClause;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\AbsractGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */