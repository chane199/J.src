/*     */ package csbst.generators.dynamic;
/*     */ 
/*     */ import csbst.ga.ecj.TestCaseCandidate;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.generators.CopyGenerator;
/*     */ import csbst.generators.containers.ListGenerator;
/*     */ import csbst.utils.ASTEditor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.Assignment;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.CastExpression;
/*     */ import org.eclipse.jdt.core.dom.Expression;
/*     */ import org.eclipse.jdt.core.dom.ExpressionStatement;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.Name;
/*     */ import org.eclipse.jdt.core.dom.NullLiteral;
/*     */ import org.eclipse.jdt.core.dom.ParameterizedType;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.TryStatement;
/*     */ import org.eclipse.jdt.core.dom.Type;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodGenerator
/*     */   extends AbstractDynamicGenerator<Method>
/*     */ {
/*     */   Object classUTInstance;
/*     */   Object returnObject;
/*     */   String returnObjectName;
/*     */   boolean isAnonymousGenerator;
/*     */   private Vector<MethodGenerator> methodsOnReturnObject;
/*  60 */   protected static Map<Class, Vector<AbstractDynamicGenerator.ExecutionWay>> class2MethodsWays = new HashMap();
/*     */   
/*     */   public MethodGenerator(AbsractGenerator parent, Class clazz, Class stub, Vector<Method> recommandedMethodes) {
/*  63 */     super(parent, clazz, stub, recommandedMethodes, false);
/*     */   }
/*     */   
/*     */   public MethodGenerator(AbsractGenerator parent, Class clazz) {
/*  67 */     super(parent, clazz);
/*     */   }
/*     */   
/*     */ 
/*     */   public MethodGenerator(TestCaseCandidate testCaseCandidate, Class clazz, Class stub, Vector<Method> methodsMayReach, boolean isAnonymousGenerator)
/*     */   {
/*  73 */     this(null, clazz, stub, methodsMayReach);
/*  74 */     this.testCaseCandidate = testCaseCandidate;
/*  75 */     this.isAnonymousGenerator = isAnonymousGenerator;
/*     */   }
/*     */   
/*     */   public Object getReturnedObject()
/*     */   {
/*  80 */     return this.returnObject;
/*     */   }
/*     */   
/*     */   public String getReturnedObjectName() {
/*  84 */     return this.returnObjectName;
/*     */   }
/*     */   
/*     */   public void generateRandom()
/*     */   {
/*  89 */     super.generateRandom();
/*     */   }
/*     */   
/*     */   private void generateSubSequence()
/*     */   {
/*  94 */     Vector<Method> methods = new Vector();
/*  95 */     Class cls = this.currentWay.method.getReturnType();
/*  96 */     Method[] arrayOfMethod; int j = (arrayOfMethod = cls.getMethods()).length; for (int i = 0; i < j; i++) { Method m = arrayOfMethod[i];
/*  97 */       if ((m.getDeclaringClass().equals(cls)) && 
/*  98 */         (!m.getDeclaringClass().equals(Object.class)))
/*     */       {
/*     */ 
/* 101 */         methods.add(m);
/*     */       }
/*     */     }
/*     */     
/* 105 */     this.methodsOnReturnObject = new Vector();
/* 106 */     if (methods.size() < 1) {
/* 107 */       return;
/*     */     }
/* 109 */     int mn = new Random().nextInt(3);
/* 110 */     for (int i = 0; i < mn; i++) {
/* 111 */       this.methodsOnReturnObject.add(new MethodGenerator(this, this.currentWay.method.getReturnType(), this.currentWay.method.getReturnType(), methods));
/* 112 */       ((MethodGenerator)this.methodsOnReturnObject.get(i)).generateRandom();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void executeSubSequence()
/*     */   {
/* 119 */     for (MethodGenerator m : this.methodsOnReturnObject) {
/* 120 */       m.execute(this.returnObject, this.returnObject.getClass());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Vector<AbstractDynamicGenerator.ExecutionWay> generateInstantiationWays(Class cls)
/*     */   {
/* 128 */     if ((this.recommandedWays != null) && (this.recommandedWays.size() > 0)) {
/* 129 */       for (Method m : this.recommandedWays) {
/* 130 */         this.possibleWays.add(new AbstractDynamicGenerator.ExecutionWay(m, 0));
/*     */       }
/*     */     } else {
/* 133 */       Vector<Method> allMethods = new Vector();
/*     */       
/*     */       Method[] arrayOfMethod1;
/* 136 */       int j = (arrayOfMethod1 = cls.getDeclaredMethods()).length; for (int i = 0; i < j; i++) { Method m = arrayOfMethod1[i];
/* 137 */         if ((isAccessible(m)) && 
/* 138 */           (!allMethods.contains(m)) && (!m.isSynthetic()) && (!m.isBridge())) {
/* 139 */           allMethods.add(m);
/* 140 */           this.possibleWays.add(new AbstractDynamicGenerator.ExecutionWay(m, INITIAL_COST));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 147 */       j = (arrayOfMethod1 = this.stub.getDeclaredMethods()).length; for (i = 0; i < j; i++) { Method m = arrayOfMethod1[i];
/* 148 */         if ((isAccessible(m)) && 
/* 149 */           (!allMethods.contains(m)) && (!m.isSynthetic()) && (!m.isBridge()))
/*     */         {
/*     */ 
/* 152 */           allMethods.add(m);
/* 153 */           this.possibleWays.add(new AbstractDynamicGenerator.ExecutionWay(m, 2 * INITIAL_COST));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 158 */       Class superCls = cls.getSuperclass();
/* 159 */       int currentCost = 3 * INITIAL_COST;
/* 160 */       int level = 0;
/* 161 */       while ((superCls != null) && (!superCls.equals(Object.class)) && 
/* 162 */         (level < 3) && (!superCls.isInterface()) && (!
/* 163 */         superCls.getName().startsWith("java.lang"))) {
/* 164 */         currentCost += INITIAL_COST;
/* 165 */         level++;
/* 166 */         Method[] arrayOfMethod2; int m = (arrayOfMethod2 = superCls.getDeclaredMethods()).length; for (int k = 0; k < m; k++) { Method m = arrayOfMethod2[k];
/* 167 */           if ((isAccessible(m)) && (!m.isSynthetic()) && (!m.isBridge()) && 
/* 168 */             (!allMethods.contains(m)))
/*     */           {
/*     */ 
/* 171 */             allMethods.add(m);
/* 172 */             this.possibleWays.add(new AbstractDynamicGenerator.ExecutionWay(m, currentCost));
/*     */           }
/*     */         }
/* 175 */         superCls = superCls.getSuperclass();
/*     */       }
/*     */     }
/*     */     
/* 179 */     return this.possibleWays;
/*     */   }
/*     */   
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName) {
/* 183 */     List<Statement> returnList = new ArrayList();
/* 184 */     if (this.currentWay.method == null)
/* 185 */       return returnList;
/* 186 */     if ((this.parent instanceof AbstractDynamicGenerator)) {
/* 187 */       this.SystematicallySurroundCall = ((AbstractDynamicGenerator)this.parent).SystematicallySurroundCall;
/*     */     }
/* 189 */     MethodInvocation methodInv = ast.newMethodInvocation();
/*     */     
/* 191 */     if (!Modifier.isStatic(this.currentWay.method.getModifiers()))
/*     */     {
/* 193 */       methodInv.setExpression(ast.newSimpleName(varName));
/*     */     }
/*     */     else {
/* 196 */       Name qNamef = getName2UseInJunitClass(this.currentWay.method.getDeclaringClass(), ast);
/* 197 */       methodInv.setExpression(qNamef);
/*     */     }
/*     */     
/* 200 */     methodInv.setName(ast.newSimpleName(this.currentWay.method.getName().toString()));
/*     */     
/* 202 */     for (int i = 0; i < this.parameters.size(); i++) {
/* 203 */       if ((((AbsractGenerator)this.parameters.get(i)).getObject() == null) && (!(this.parameters.get(i) instanceof CopyGenerator))) {
/* 204 */         NullLiteral nLiteral = ast.newNullLiteral();
/* 205 */         CastExpression ce = ast.newCastExpression();
/*     */         
/*     */ 
/* 208 */         Type TypeName = getType2UseInJunitClass(this.currentWay.method.getParameterTypes()[i], ast);
/* 209 */         if ((this.parameters.get(i) instanceof ListGenerator)) {
/* 210 */           ParameterizedType pt = ast.newParameterizedType(ast.newSimpleType(ast.newSimpleName("List")));
/* 211 */           pt.typeArguments().add(TypeName);
/* 212 */           ce.setType(pt);
/*     */         }
/*     */         else {
/* 215 */           ce.setType(TypeName);
/*     */         }
/* 217 */         ce.setExpression(nLiteral);
/* 218 */         methodInv.arguments().add(ce);
/*     */       }
/*     */       else
/*     */       {
/*     */         String newVarName;
/*     */         
/*     */         String newVarName;
/* 225 */         if ((this.parameters.get(i) instanceof CopyGenerator)) {
/* 226 */           newVarName = varName;
/*     */         } else {
/* 228 */           newVarName = varName + pName + "P" + (i + 1);
/* 229 */           returnList.addAll(((AbsractGenerator)this.parameters.get(i)).getStatements(ast, newVarName, ""));
/*     */         }
/* 231 */         methodInv.arguments().add(ast.newSimpleName(newVarName));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     Block b;
/*     */     
/* 238 */     if ((!this.currentWay.method.getReturnType().equals(Void.TYPE)) && (isAccessible(this.currentWay.method.getReturnType()))) {
/* 239 */       VariableDeclarationFragment varDec = ast.newVariableDeclarationFragment();
/* 240 */       this.returnObjectName = (varName + pName + "R");
/* 241 */       varDec.setName(ast.newSimpleName(this.returnObjectName));
/* 242 */       varDec.setInitializer(methodInv);
/*     */       
/* 244 */       VariableDeclarationStatement varDecStat = ast.newVariableDeclarationStatement(varDec);
/*     */       
/* 246 */       if (this.currentWay.method.getReturnType().isPrimitive()) {
/* 247 */         varDecStat.setType(ast.newPrimitiveType(ASTEditor.getPrimitiveCode(this.currentWay.method.getReturnType())));
/*     */       } else {
/* 249 */         varDecStat.setType(getType2UseInJunitClass(this.currentWay.method.getReturnType(), ast));
/*     */       }
/* 251 */       Statement statement = varDecStat;
/*     */       
/*     */ 
/* 254 */       if ((!(varDec.getInitializer() instanceof NullLiteral)) && ((this.exceptions.size() > 0) || (getUnexpectedException() != null) || (this.SystematicallySurroundCall)))
/*     */       {
/* 256 */         Assignment Ass = ast.newAssignment();
/* 257 */         Ass.setLeftHandSide(ast.newSimpleName(this.returnObjectName));
/* 258 */         Expression leftHandExp = varDec.getInitializer();
/*     */         
/* 260 */         if (this.currentWay.method.getReturnType().isPrimitive()) {
/* 261 */           if (this.currentWay.method.getReturnType().getSimpleName().equals("boolean")) {
/* 262 */             varDec.setInitializer(ast.newBooleanLiteral(false));
/* 263 */           } else if (this.currentWay.method.getReturnType().getSimpleName().equals("char")) {
/* 264 */             varDec.setInitializer(ast.newCharacterLiteral());
/*     */           } else
/* 266 */             varDec.setInitializer(ast.newNumberLiteral(ASTEditor.getPrimitiveInitialiser(this.currentWay.method.getReturnType())));
/*     */         } else {
/* 268 */           varDec.setInitializer(ast.newNullLiteral());
/*     */         }
/* 270 */         returnList.add(varDecStat);
/*     */         
/* 272 */         Ass.setRightHandSide(leftHandExp);
/* 273 */         ExpressionStatement AssStatement = ast.newExpressionStatement(Ass);
/*     */         
/* 275 */         TryStatement tryStatement = ast.newTryStatement();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 282 */         Class except = Exception.class;
/* 283 */         tryStatement.catchClauses().add(getCatchClause(except, ast, false));
/*     */         
/* 285 */         Block b = ast.newBlock();
/* 286 */         b.statements().add(AssStatement);
/* 287 */         tryStatement.setBody(b);
/* 288 */         returnList.add(tryStatement);
/*     */       } else {
/* 290 */         returnList.add(varDecStat);
/*     */       }
/*     */     }
/*     */     else {
/* 294 */       Statement statement = ast.newExpressionStatement(methodInv);
/*     */       
/* 296 */       if ((this.exceptions.size() > 0) || (getUnexpectedException() != null) || (this.SystematicallySurroundCall)) {
/* 297 */         TryStatement tryStatement = ast.newTryStatement();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 304 */         Class except = Exception.class;
/* 305 */         tryStatement.catchClauses().add(getCatchClause(except, ast, false));
/*     */         
/* 307 */         b = ast.newBlock();
/* 308 */         b.statements().add(statement);
/* 309 */         tryStatement.setBody(b);
/* 310 */         returnList.add(tryStatement);
/*     */       }
/*     */       else {
/* 313 */         returnList.add(statement);
/*     */       }
/*     */     }
/* 316 */     if ((this.returnObject != null) && 
/* 317 */       (this.parent == null) && 
/* 318 */       (!this.currentWay.method.getReturnType().isPrimitive()) && 
/* 319 */       (!this.currentWay.method.getReturnType().equals(Void.TYPE)) && 
/* 320 */       (isAccessible(this.currentWay.method.getReturnType())) && 
/* 321 */       (this.methodsOnReturnObject != null)) {
/* 322 */       int i = 0;
/* 323 */       for (MethodGenerator m : this.methodsOnReturnObject) {
/* 324 */         returnList.addAll(m.getStatements(ast, this.returnObjectName, "P" + i++));
/*     */       }
/*     */     }
/*     */     
/* 328 */     return returnList;
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 333 */     MethodGenerator newCon = new MethodGenerator(this.parent, this.clazz);
/*     */     
/* 335 */     newCon.clazz = this.clazz;
/*     */     
/*     */ 
/* 338 */     newCon.fitness = this.fitness;
/* 339 */     newCon.currentWay = this.currentWay;
/* 340 */     newCon.object = this.object;
/* 341 */     newCon.seed = this.seed;
/* 342 */     newCon.random = this.random;
/* 343 */     if (this.parameters != null) {
/* 344 */       newCon.parameters = new Vector();
/* 345 */       for (AbsractGenerator gene : this.parameters) {
/* 346 */         if (gene != null)
/* 347 */           newCon.parameters.add((AbsractGenerator)gene.clone());
/*     */       }
/*     */     }
/* 350 */     newCon.stub = this.stub;
/*     */     
/*     */ 
/*     */ 
/* 354 */     if (this.recommandedWays != null)
/* 355 */       newCon.recommandedWays = this.recommandedWays;
/* 356 */     newCon.exceptions = new HashSet();
/* 357 */     for (Class e : this.exceptions)
/* 358 */       newCon.addExceptionClass(e);
/* 359 */     newCon.unexpectedException = this.unexpectedException;
/* 360 */     newCon.generationNbr = this.generationNbr;
/* 361 */     newCon.parent = this.parent;
/* 362 */     newCon.possibleWays = this.possibleWays;
/*     */     
/* 364 */     if (this.externalConstructor != null) {
/* 365 */       newCon.externalConstructor = ((InstanceGenerator)this.externalConstructor.clone());
/*     */     }
/* 367 */     return newCon;
/*     */   }
/*     */   
/*     */   public void execute(Object obj, Class clsUT) {
/* 371 */     this.classUTInstance = obj;
/* 372 */     this.exceptions = new HashSet();
/* 373 */     if ((this != null) && 
/* 374 */       (this.currentWay.method != null)) {
/*     */       try { Object nObj;
/*     */         final Object nObj;
/* 377 */         if ((obj.getClass().equals(clsUT)) && (clsUT.isAssignableFrom(obj.getClass()))) {
/* 378 */           nObj = clsUT.cast(obj);
/*     */         } else
/* 380 */           nObj = obj;
/* 381 */         final Method methodUT = this.currentWay.method;
/*     */         try {
/* 383 */           AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */             public Object run() throws Exception {
/* 385 */               if (!methodUT.isAccessible()) {
/* 386 */                 methodUT.setAccessible(true);
/*     */               }
/*     */               
/*     */ 
/* 390 */               MethodGenerator.this.exceptions = new HashSet();
/* 391 */               MethodGenerator.this.setUnexpectedException(null);
/* 392 */               Class[] arrayOfClass; int j = (arrayOfClass = methodUT.getExceptionTypes()).length; for (int i = 0; i < j; i++) { Class thro = arrayOfClass[i];
/* 393 */                 MethodGenerator.this.addExceptionClass(thro); }
/* 394 */               MethodGenerator.this.returnObject = null;
/*     */               try {
/* 396 */                 MethodGenerator.this.returnObject = methodUT.invoke(nObj, MethodGenerator.this.getParameters());
/*     */               }
/*     */               catch (Throwable e) {
/* 399 */                 MethodGenerator.this.setUnexpectedException(e);
/*     */               }
/* 401 */               return null;
/*     */             }
/*     */           });
/*     */         }
/*     */         catch (PrivilegedActionException localPrivilegedActionException) {}
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         return;
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {}catch (SecurityException localSecurityException) {}catch (Exception e)
/*     */       {
/* 416 */         e.printStackTrace();
/*     */       }
/*     */     }
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
/*     */   public void execute()
/*     */   {
/* 433 */     if ((this != null) && 
/* 434 */       (this.currentWay.method != null)) {
/*     */       try {
/* 436 */         final Class currentClass = this.currentWay.method.getDeclaringClass();
/* 437 */         final Method methodUT = currentClass.getDeclaredMethod(this.currentWay.method.getName(), this.currentWay.method.getParameterTypes());
/* 438 */         AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */           public Object run() throws Exception {
/* 440 */             if (!methodUT.isAccessible()) {
/* 441 */               methodUT.setAccessible(true);
/*     */             }
/*     */             
/* 444 */             MethodGenerator.this.getExceptions().clear();
/* 445 */             Class[] arrayOfClass; int j = (arrayOfClass = methodUT.getExceptionTypes()).length; for (int i = 0; i < j; i++) { Class thro = arrayOfClass[i];
/* 446 */               MethodGenerator.this.addExceptionClass(thro); }
/* 447 */             MethodGenerator.this.returnObject = null;
/*     */             try {
/* 449 */               MethodGenerator.this.returnObject = methodUT.invoke(currentClass, MethodGenerator.this.getParameters());
/*     */             } catch (Throwable e) {
/* 451 */               MethodGenerator.this.setUnexpectedException(e);
/*     */             }
/* 453 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {}catch (PrivilegedActionException localPrivilegedActionException) {}catch (IllegalArgumentException localIllegalArgumentException) {}catch (SecurityException localSecurityException) {}catch (Exception localException) {}
/*     */     }
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
/*     */ 
/*     */   public String toString()
/*     */   {
/* 477 */     String str = new String();
/* 478 */     if (this.currentWay.method == null) {
/* 479 */       str = "nothing";
/*     */     } else
/* 481 */       str = this.currentWay.method.getName();
/* 482 */     if (this.parameters != null) {
/* 483 */       str = str + this.parameters.toString();
/*     */     } else
/* 485 */       str = str + "()";
/* 486 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isStatic()
/*     */   {
/* 492 */     if (this.currentWay.method == null) return true;
/* 493 */     return Modifier.isStatic(this.currentWay.method.getModifiers());
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\dynamic\MethodGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */