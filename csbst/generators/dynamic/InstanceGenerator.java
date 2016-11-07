/*     */ package csbst.generators.dynamic;
/*     */ 
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.generators.containers.ListGenerator;
/*     */ import csbst.testing.ClassUnderTest;
/*     */ import csbst.testing.JTE;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.Assignment;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.CastExpression;
/*     */ import org.eclipse.jdt.core.dom.ClassInstanceCreation;
/*     */ import org.eclipse.jdt.core.dom.Expression;
/*     */ import org.eclipse.jdt.core.dom.ExpressionStatement;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.Name;
/*     */ import org.eclipse.jdt.core.dom.NullLiteral;
/*     */ import org.eclipse.jdt.core.dom.ParameterizedType;
/*     */ import org.eclipse.jdt.core.dom.QualifiedName;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.TryStatement;
/*     */ import org.eclipse.jdt.core.dom.Type;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
/*     */ import org.reflections.Reflections;
/*     */ import org.reflections.ReflectionsException;
/*     */ import org.reflections.scanners.MethodParameterScanner;
/*     */ import org.reflections.scanners.Scanner;
/*     */ import org.reflections.scanners.SubTypesScanner;
/*     */ import org.reflections.util.ConfigurationBuilder;
/*     */ import org.reflections.util.FilterBuilder;
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
/*     */ public class InstanceGenerator
/*     */   extends AbstractDynamicGenerator<Constructor>
/*     */ {
/*  67 */   public static Map<Class, Vector<AbstractDynamicGenerator.ExecutionWay>> class2InstantiationWays = new HashMap();
/*  68 */   public static Map<Class, Vector<Class>> interface2Implementation = new HashMap();
/*     */   
/*     */   public static Reflections reflections;
/*  71 */   public static Map<String, Reflections> package2Reflections = new HashMap();
/*  72 */   public static Vector<Class> defaultClassesSet = new Vector();
/*  73 */   private boolean once = false;
/*     */   
/*     */   static {
/*  76 */     for (int i = 0; i < 100; i++) {
/*  77 */       defaultClassesSet.add(JTE.currentClassUnderTest.getClazz());
/*  78 */       defaultClassesSet.add(String.class);
/*  79 */       defaultClassesSet.add(Integer.class);
/*  80 */       defaultClassesSet.add(Long.class);
/*  81 */       defaultClassesSet.add(Short.class);
/*  82 */       defaultClassesSet.add(Double.class);
/*  83 */       defaultClassesSet.add(Float.class);
/*  84 */       defaultClassesSet.add(Date.class);
/*  85 */       defaultClassesSet.add(Boolean.class);
/*  86 */       defaultClassesSet.add(Character.class);
/*  87 */       defaultClassesSet.add(Byte.class);
/*     */     }
/*     */     
/*  90 */     URL[] urls = new URL[JTE.classPath.length];
/*  91 */     for (int i = 0; i < JTE.classPath.length; i++) {
/*     */       try {
/*  93 */         urls[i] = new File(JTE.classPath[i]).toURL();
/*     */       }
/*     */       catch (MalformedURLException e) {
/*  96 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 100 */     reflections = new Reflections(new ConfigurationBuilder()
/* 101 */       .setScanners(new Scanner[] {new SubTypesScanner(false), new MethodParameterScanner() })
/* 102 */       .setUrls(urls));
/*     */   }
/*     */   
/*     */   public InstanceGenerator(AbsractGenerator parent, Class clazz, Vector<Constructor> recommandedConstractors, boolean withInstance)
/*     */   {
/* 107 */     super(parent, clazz, recommandedConstractors, withInstance);
/*     */   }
/*     */   
/*     */   public InstanceGenerator(AbsractGenerator parent, Class clazz, boolean withInstance) {
/* 111 */     this(parent, clazz, new Vector(), withInstance);
/* 112 */     if (clazz.toString().equals("[Ljava.lang.String")) {
/*     */       try {
/* 114 */         throw new Exception("Erreur [Ljava.lang.String");
/*     */       }
/*     */       catch (Exception e) {
/* 117 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public InstanceGenerator(AbsractGenerator p, Class clazz) {
/* 123 */     super(p, clazz);
/*     */   }
/*     */   
/*     */   public void generateRandom()
/*     */   {
/* 128 */     super.generateRandom();
/*     */     
/* 130 */     this.object = getInstance();
/*     */     
/* 132 */     if ((this.object != null) && (!defaultClassesSet.contains(this.clazz)) && (!this.clazz.equals(Object.class)) && (isAccessible(this.clazz)))
/* 133 */       defaultClassesSet.add(this.clazz);
/*     */   }
/*     */   
/* 136 */   protected boolean canGoOn() { Long stubCost = (Long)stub2Cost.get(this.stub);
/* 137 */     return (!isParent(this.stub, this)) && (!isParent(this.clazz, this)) && (deep(this.clazz) <= 20);
/*     */   }
/*     */   
/*     */   private int deep(Class cls) {
/* 141 */     int d = 0;
/* 142 */     AbsractGenerator currentParent = this.parent;
/* 143 */     while (currentParent != null) {
/* 144 */       d++;
/* 145 */       currentParent = currentParent.getParent();
/*     */     }
/* 147 */     return d;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isInstantiable(Class cls)
/*     */   {
/* 159 */     if (cls.isAnonymousClass()) return false;
/* 160 */     if (cls.isInterface()) return false;
/* 161 */     if (generateFactoryMethods(cls).size() > 0) return true;
/* 162 */     if (getSingletons(cls).size() > 0) return true;
/* 163 */     if (Modifier.isAbstract(cls.getModifiers())) return false;
/* 164 */     if (getConstructors(cls).size() > 0) return true;
/* 165 */     if (cls.getDeclaredConstructors().length == 0) { return true;
/*     */     }
/* 167 */     return false;
/*     */   }
/*     */   
/*     */   private Set<Class<?>> getStubs4Class(Class cls) {
/* 171 */     Set<Class<?>> subTypeSet = null;
/*     */     try {
/* 173 */       subTypeSet = reflections.getSubTypesOf(cls);
/*     */     }
/*     */     catch (ReflectionsException localReflectionsException) {}
/*     */     
/*     */ 
/* 178 */     return subTypeSet;
/*     */   }
/*     */   
/*     */   private Vector<Class> generateStubs(Class clazz) {
/* 182 */     if (clazz.equals(Object.class)) {
/* 183 */       return defaultClassesSet;
/*     */     }
/* 185 */     Vector<Class> tmpPossiblesStubs = new Vector();
/* 186 */     tmpPossiblesStubs = (Vector)interface2Implementation.get(clazz);
/* 187 */     if (tmpPossiblesStubs != null) {
/* 188 */       return tmpPossiblesStubs;
/*     */     }
/*     */     
/* 191 */     tmpPossiblesStubs = new Vector();
/*     */     
/* 193 */     Set<Class<?>> subTypeSet = getStubs4Class(clazz);
/*     */     
/* 195 */     if (subTypeSet != null) {
/* 196 */       for (Class clss : subTypeSet)
/*     */       {
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/*     */ 
/* 203 */           if (((areInSameArchive(clss, clazz)) || (areInSameArchive(clss, JTE.currentClassUnderTest.getClazz()))) && 
/* 204 */             (!clss.isAnonymousClass()) && 
/* 205 */             (isAccessible(clss))) {
/* 206 */             tmpPossiblesStubs.add(clss);
/*     */           }
/*     */         }
/*     */         catch (Exception e) {
/* 210 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 214 */     interface2Implementation.put(clazz, new Vector(tmpPossiblesStubs));
/*     */     
/* 216 */     return tmpPossiblesStubs;
/*     */   }
/*     */   
/*     */   private boolean areInSameArchive(Class cls1, Class cls2) {
/* 220 */     return getResourcePath(cls1).equalsIgnoreCase(getResourcePath(cls2));
/*     */   }
/*     */   
/*     */   private int nbrOfDeclaringClasses(Class cls)
/*     */   {
/* 225 */     if (cls.getDeclaringClass() == null) {
/* 226 */       return 0;
/*     */     }
/* 228 */     return nbrOfDeclaringClasses(cls.getDeclaringClass()) + 1;
/*     */   }
/*     */   
/*     */   private Vector<Method> generateFactoryMethods(Class cls) {
/* 232 */     Vector<Method> allFactoryMethods = new Vector();
/* 233 */     if ((cls == null) || (cls.equals(Object.class)) || (cls.equals(Class.class))) {
/* 234 */       return allFactoryMethods;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 239 */     Method[] allMethods = cls.getDeclaredMethods();
/*     */     
/*     */     Method[] arrayOfMethod1;
/*     */     
/* 243 */     int j = (arrayOfMethod1 = allMethods).length; for (int i = 0; i < j; i++) { Method m = arrayOfMethod1[i];
/* 244 */       if ((cls.equals(m.getReturnType())) && 
/* 245 */         (!m.getName().startsWith("access")) && 
/* 246 */         (Modifier.isStatic(m.getModifiers())) && 
/* 247 */         (isAccessible(m))) {
/* 248 */         allFactoryMethods.add(m);
/*     */       }
/*     */     }
/* 251 */     return allFactoryMethods;
/*     */   }
/*     */   
/*     */   private boolean isAccessible(Field field) {
/* 255 */     Class declaringClass = field.getDeclaringClass();
/*     */     
/*     */ 
/*     */ 
/* 259 */     return (isAccessible(declaringClass)) && (!Modifier.isPrivate(field.getModifiers())) && (!Modifier.isProtected(field.getModifiers())) && ((Modifier.isPublic(field.getModifiers())) || (JTE.currentClassUnderTest.getClazz().getPackage().equals(declaringClass.getPackage())));
/*     */   }
/*     */   
/*     */   private Vector<Constructor> getConstructors(Class cls)
/*     */   {
/* 264 */     if ((cls == null) || (cls.equals(Object.class)) || (cls.equals(Class.class))) {
/* 265 */       return new Vector();
/*     */     }
/* 267 */     Vector<Constructor> allConstructors = new Vector();
/* 268 */     Constructor[] allDeclaredConstructors = cls.getDeclaredConstructors();
/* 269 */     Constructor[] arrayOfConstructor1; int j = (arrayOfConstructor1 = allDeclaredConstructors).length; for (int i = 0; i < j; i++) { Constructor c = arrayOfConstructor1[i];
/* 270 */       if (isAccessible(c))
/* 271 */         allConstructors.add(c);
/*     */     }
/* 273 */     return allConstructors;
/*     */   }
/*     */   
/*     */   private Vector<Field> getSingletons(Class cls) {
/* 277 */     if ((cls == null) || (cls.equals(Object.class)) || (cls.equals(Class.class))) {
/* 278 */       return new Vector();
/*     */     }
/* 280 */     Vector<Field> allSingletons = new Vector();
/* 281 */     Field[] allFields = cls.getDeclaredFields();
/* 282 */     Field[] arrayOfField1; int j = (arrayOfField1 = allFields).length; for (int i = 0; i < j; i++) { Field f = arrayOfField1[i];
/* 283 */       if (((f.getType().isAssignableFrom(cls)) || (cls.isAssignableFrom(f.getType()))) && 
/* 284 */         (Modifier.isStatic(f.getModifiers())) && 
/* 285 */         (isAccessible(f)) && 
/* 286 */         (!f.getType().equals(Object.class)))
/*     */       {
/* 288 */         allSingletons.add(f);
/*     */       }
/*     */     }
/* 291 */     return allSingletons;
/*     */   }
/*     */   
/*     */ 
/*     */   private Vector<Method> generateExternalMethods(Class cls)
/*     */   {
/* 297 */     Vector<Method> externalMethods = new Vector();
/*     */     
/*     */ 
/* 300 */     if ((cls == null) || (cls.equals(Object.class)) || (cls.equals(Class.class))) {
/* 301 */       return externalMethods;
/*     */     }
/* 303 */     Set<Method> returnTypeSet = null;
/* 304 */     Set<Class<?>> subTypeSet = null;
/*     */     try {
/* 306 */       returnTypeSet = reflections.getMethodsReturn(cls);
/*     */     }
/*     */     catch (ReflectionsException localReflectionsException) {}catch (NoClassDefFoundError localNoClassDefFoundError) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 315 */     if (returnTypeSet != null) {
/* 316 */       for (Method md : returnTypeSet)
/* 317 */         if (isAccessible(md))
/*     */         {
/* 319 */           if ((!Modifier.isAbstract(md.getModifiers())) && 
/* 320 */             (!md.getName().startsWith("access$")) && 
/* 321 */             (md.getDeclaringClass() != cls) && 
/* 322 */             (!md.getDeclaringClass().isAnonymousClass()) && (
/* 323 */             (areInSameArchive(md.getDeclaringClass(), cls)) || (areInSameArchive(md.getDeclaringClass(), JTE.currentClassUnderTest.getClazz()))))
/* 324 */             externalMethods.add(md); }
/*     */     }
/* 326 */     return externalMethods;
/*     */   }
/*     */   
/*     */   protected Vector<AbstractDynamicGenerator.ExecutionWay> generateInstantiationWays(Class cls)
/*     */   {
/* 331 */     Vector<AbstractDynamicGenerator.ExecutionWay> tmpPossibleWays = (Vector)class2InstantiationWays.get(cls);
/*     */     
/* 333 */     if ((tmpPossibleWays != null) && (!cls.equals(Object.class))) {
/* 334 */       this.possibleWays = tmpPossibleWays;
/* 335 */       return this.possibleWays;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 341 */     tmpPossibleWays = new Vector();
/*     */     
/* 343 */     if (cls.isAnonymousClass()) {
/* 344 */       if (cls.getEnclosingMethod() != null) {
/* 345 */         Method m = cls.getEnclosingMethod();
/* 346 */         if (m.getReturnType().isAssignableFrom(cls))
/* 347 */           tmpPossibleWays.add(new AbstractDynamicGenerator.ExecutionWay(m, m.getDeclaringClass()));
/*     */       }
/* 349 */       this.possibleWays = tmpPossibleWays;
/* 350 */       return this.possibleWays;
/*     */     }
/*     */     
/*     */ 
/* 354 */     for (Method em : generateExternalMethods(cls)) {
/* 355 */       tmpPossibleWays.add(new AbstractDynamicGenerator.ExecutionWay(em, null));
/*     */     }
/*     */     
/* 358 */     for (Field f : getSingletons(cls)) {
/* 359 */       tmpPossibleWays.add(new AbstractDynamicGenerator.ExecutionWay(f));
/*     */     }
/*     */     
/*     */ 
/* 363 */     for (Method fm : generateFactoryMethods(cls)) {
/* 364 */       tmpPossibleWays.add(new AbstractDynamicGenerator.ExecutionWay(fm));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 369 */     if (!Modifier.isAbstract(cls.getModifiers())) {
/* 370 */       for (Constructor c : getConstructors(cls)) {
/* 371 */         if (this.recommandedWays.contains(c)) {
/* 372 */           tmpPossibleWays.add(new AbstractDynamicGenerator.ExecutionWay(c, true));
/*     */         } else
/* 374 */           tmpPossibleWays.add(new AbstractDynamicGenerator.ExecutionWay(c));
/*     */       }
/*     */     }
/* 377 */     if ((cls.getDeclaredConstructors().length == 0) && 
/* 378 */       (!Modifier.isAbstract(cls.getModifiers())) && 
/* 379 */       (!Modifier.isInterface(cls.getModifiers()))) {
/* 380 */       tmpPossibleWays.add(new AbstractDynamicGenerator.ExecutionWay(cls));
/*     */     }
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
/* 394 */     for (Class s : generateStubs(cls))
/*     */     {
/* 396 */       if ((!s.isInterface()) && (!Modifier.isAbstract(s.getModifiers()))) {
/* 397 */         tmpPossibleWays.addAll(generateInstantiationWays(s));
/*     */       }
/*     */     }
/*     */     
/* 401 */     if (!cls.equals(Object.class))
/*     */     {
/* 403 */       if ((cls.getPackage() != null) && (cls.getPackage().getName().toString().startsWith("java.")))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 410 */         Reflections reflections1 = (Reflections)package2Reflections.get(cls.getPackage().getName().toString());
/* 411 */         URL[] urls; if (reflections1 == null) {
/* 412 */           File java_home = new File(System.getProperty("java.home"));
/* 413 */           File java_classes = new File(java_home.getParent() + "/Classes/classes.jar");
/* 414 */           if (JAVA_VERSION >= 1.7D) {
/* 415 */             java_classes = new File(java_home.getParent() + "/jre/lib/rt.jar");
/*     */           }
/* 417 */           urls = new URL[1];
/* 418 */           for (int i = 0; i < 1; i++) {
/*     */             try {
/* 420 */               urls[i] = java_classes.toURL();
/*     */             }
/*     */             catch (MalformedURLException e) {
/* 423 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */           
/* 427 */           reflections1 = new Reflections(new ConfigurationBuilder()
/* 428 */             .setScanners(new Scanner[] {new SubTypesScanner(false) })
/* 429 */             .filterInputsBy(new FilterBuilder().includePackage(cls.getPackage().getName()))
/* 430 */             .setUrls(urls));
/* 431 */           package2Reflections.put(cls.getPackage().getName().toString(), reflections1);
/*     */         }
/*     */         
/* 434 */         Object subTypes = reflections1.getSubTypesOf(cls);
/* 435 */         for (Class s : (Set)subTypes)
/*     */         {
/*     */ 
/*     */ 
/* 439 */           if ((!s.isInterface()) && (!Modifier.isAbstract(s.getModifiers())) && 
/* 440 */             (!s.isAnonymousClass()) && (isAccessible(s)))
/*     */           {
/* 442 */             tmpPossibleWays.addAll(generateInstantiationWays(s));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 451 */     this.possibleWays = tmpPossibleWays;
/* 452 */     class2InstantiationWays.put(cls, this.possibleWays);
/* 453 */     if (this.possibleWays.size() < 1) { stub2Cost.put(this.clazz, MAX_COST);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 464 */     return this.possibleWays;
/*     */   }
/*     */   
/* 467 */   public static double JAVA_VERSION = getVersion();
/*     */   
/*     */   static double getVersion() {
/* 470 */     String version = System.getProperty("java.version");
/*     */     
/* 472 */     int pos = 0;int count = 0;
/* 473 */     for (; (pos < version.length()) && (count < 2); pos++) {
/* 474 */       if (version.charAt(pos) == '.') { count++;
/*     */       }
/*     */     }
/* 477 */     return Double.parseDouble(version.substring(0, pos - 1));
/*     */   }
/*     */   
/*     */   private boolean isProtectedOrAbstract(Class cls) {
/* 481 */     if (cls.equals(Object.class))
/* 482 */       return true;
/* 483 */     if ((cls.isInterface()) || 
/* 484 */       (Modifier.isAbstract(cls.getModifiers())) || (
/* 485 */       (Modifier.isProtected(cls.getModifiers())) && (cls.equals(JTE.currentClassUnderTest.getClazz()))))
/* 486 */       return true;
/* 487 */     Object localObject; int j = (localObject = cls.getDeclaredConstructors()).length; for (int i = 0; i < j; i++) { Constructor c = localObject[i];
/* 488 */       if ((Modifier.isProtected(c.getModifiers())) && (cls.equals(JTE.currentClassUnderTest.getClazz())))
/* 489 */         return true; }
/* 490 */     j = (localObject = cls.getDeclaredMethods()).length; for (i = 0; i < j; i++) { Method m = localObject[i];
/* 491 */       if ((Modifier.isAbstract(m.getModifiers())) || (
/* 492 */         (Modifier.isProtected(cls.getModifiers())) && (cls.equals(JTE.currentClassUnderTest.getClazz()))))
/* 493 */         return true; }
/* 494 */     return false;
/*     */   }
/*     */   
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName) {
/* 498 */     if ((this.parent instanceof AbstractDynamicGenerator)) {
/* 499 */       this.SystematicallySurroundCall = ((AbstractDynamicGenerator)this.parent).SystematicallySurroundCall;
/*     */     }
/* 501 */     List<Statement> returnList = new ArrayList();
/*     */     
/* 503 */     VariableDeclarationFragment varDec = ast.newVariableDeclarationFragment();
/* 504 */     varDec.setName(ast.newSimpleName(varName));
/*     */     
/* 506 */     switch (this.currentWay.source) {
/*     */     case newInstance: 
/* 508 */       NullLiteral nullLiteral = ast.newNullLiteral();
/* 509 */       varDec.setInitializer(nullLiteral);
/*     */       
/* 511 */       break;
/*     */     
/*     */     case dataMember: 
/* 514 */       ClassInstanceCreation classInstance = null;
/* 515 */       if (this.parameters != null) {
/* 516 */         classInstance = ast.newClassInstanceCreation();
/*     */         
/* 518 */         if (this.currentWay.constructor.getDeclaringClass().isMemberClass()) {
/* 519 */           String newVarName = "";
/* 520 */           int i = 0;
/* 521 */           if (!Modifier.isStatic(this.currentWay.constructor.getDeclaringClass().getModifiers())) {
/* 522 */             classInstance.setType(ast.newSimpleType(ast.newSimpleName(this.currentWay.constructor.getDeclaringClass().getSimpleName())));
/*     */             
/* 524 */             newVarName = varName + pName + "P" + 1;
/* 525 */             returnList.addAll(((AbsractGenerator)this.parameters.get(0)).getStatements(ast, newVarName, ""));
/* 526 */             classInstance.setExpression(ast.newSimpleName(newVarName));
/* 527 */             i++;
/*     */           }
/*     */           else {
/* 530 */             Type TypeName = getType2UseInJunitClass(this.currentWay.constructor.getDeclaringClass(), ast);
/* 531 */             classInstance.setType(TypeName);
/*     */           }
/* 538 */           for (; 
/*     */               
/*     */ 
/*     */ 
/*     */ 
/* 538 */               i < this.parameters.size(); i++) {
/* 539 */             newVarName = varName + pName + "P" + (i + 1);
/* 540 */             returnList.addAll(((AbsractGenerator)this.parameters.get(i)).getStatements(ast, newVarName, ""));
/* 541 */             classInstance.arguments().add(ast.newSimpleName(newVarName));
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 557 */           Type TypeName = getType2UseInJunitClass(this.currentWay.constructor.getDeclaringClass(), ast);
/* 558 */           classInstance.setType(TypeName);
/*     */           
/* 560 */           for (int i = 0; i < this.parameters.size(); i++) {
/* 561 */             if (((AbsractGenerator)this.parameters.get(i)).getObject() == null)
/*     */             {
/* 563 */               NullLiteral nLiteral = ast.newNullLiteral();
/* 564 */               CastExpression ce = ast.newCastExpression();
/*     */               
/* 566 */               TypeName = getType2UseInJunitClass(this.currentWay.constructor.getParameterTypes()[i], ast);
/* 567 */               if ((this.parameters.get(i) instanceof ListGenerator)) {
/* 568 */                 ParameterizedType pt = ast.newParameterizedType(ast.newSimpleType(ast.newSimpleName("List")));
/* 569 */                 pt.typeArguments().add(TypeName);
/* 570 */                 ce.setType(pt);
/*     */               }
/*     */               else {
/* 573 */                 ce.setType(TypeName);
/*     */               }
/* 575 */               ce.setExpression(nLiteral);
/* 576 */               classInstance.arguments().add(ce);
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 581 */               String newVarName = varName + pName + "P" + (i + 1);
/* 582 */               returnList.addAll(((AbsractGenerator)this.parameters.get(i)).getStatements(ast, newVarName, ""));
/* 583 */               classInstance.arguments().add(ast.newSimpleName(newVarName));
/*     */             }
/*     */           }
/*     */         }
/* 587 */         varDec.setInitializer(classInstance);
/*     */       }
/*     */       else {
/* 590 */         MethodInvocation newInstanceInv = ast.newMethodInvocation();
/* 591 */         Name qNameClass = getName2UseInJunitClass(this.stub, ast);
/* 592 */         newInstanceInv.setExpression(qNameClass);
/* 593 */         newInstanceInv.setName(ast.newSimpleName("newInstance"));
/* 594 */         varDec.setInitializer(newInstanceInv);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 599 */       break;
/*     */     case externalMethod: 
/* 601 */       MethodInvocation methodInv1 = ast.newMethodInvocation();
/* 602 */       Name qNamef = getName2UseInJunitClass(this.currentWay.method.getDeclaringClass(), ast);
/* 603 */       methodInv1.setExpression(qNamef);
/* 604 */       methodInv1.setName(ast.newSimpleName(this.currentWay.method.getName()));
/* 605 */       if (this.parameters != null) {
/* 606 */         for (int i = 0; i < this.parameters.size(); i++) {
/* 607 */           String newVarName = varName + pName + "P" + (i + 1);
/* 608 */           returnList.addAll(((AbsractGenerator)this.parameters.get(i)).getStatements(ast, newVarName, ""));
/* 609 */           methodInv1.arguments().add(ast.newSimpleName(newVarName));
/*     */         }
/*     */       }
/*     */       
/* 613 */       varDec.setInitializer(methodInv1);
/*     */       
/*     */ 
/* 616 */       break;
/*     */     
/*     */     case factoryMethod: 
/* 619 */       if (Modifier.isStatic(this.currentWay.method.getModifiers())) {
/* 620 */         String objectName = this.currentWay.method.getDeclaringClass().getSimpleName().toString();
/* 621 */         MethodInvocation methodInv = ast.newMethodInvocation();
/* 622 */         Name qNameEx = getName2UseInJunitClass(this.currentWay.method.getDeclaringClass(), ast);
/* 623 */         methodInv.setExpression(qNameEx);
/* 624 */         methodInv.setName(ast.newSimpleName(this.currentWay.method.getName()));
/* 625 */         if (this.parameters != null) {
/* 626 */           for (int i = 0; i < this.parameters.size(); i++) {
/* 627 */             String newVarName = varName + pName + "P" + (i + 1);
/* 628 */             returnList.addAll(((AbsractGenerator)this.parameters.get(i)).getStatements(ast, newVarName, ""));
/* 629 */             methodInv.arguments().add(ast.newSimpleName(newVarName));
/*     */           }
/*     */         }
/* 632 */         varDec.setInitializer(methodInv);
/*     */       } else {
/* 634 */         String objectName = varName + pName + "P0";
/* 635 */         if (this.externalConstructor.currentWay.source != AbstractDynamicGenerator.ExecutionSource.isNull) {
/* 636 */           returnList.addAll(this.externalConstructor.getStatements(ast, objectName, ""));
/* 637 */           MethodInvocation methodInv = ast.newMethodInvocation();
/* 638 */           methodInv.setExpression(ast.newSimpleName(objectName));
/* 639 */           methodInv.setName(ast.newSimpleName(this.currentWay.method.getName()));
/* 640 */           if (this.parameters != null) {
/* 641 */             for (int i = 0; i < this.parameters.size(); i++) {
/* 642 */               String newVarName = varName + pName + "P" + (i + 1);
/* 643 */               returnList.addAll(((AbsractGenerator)this.parameters.get(i)).getStatements(ast, newVarName, ""));
/* 644 */               methodInv.arguments().add(ast.newSimpleName(newVarName));
/*     */             }
/*     */           }
/* 647 */           varDec.setInitializer(methodInv);
/*     */         } else {
/* 649 */           NullLiteral nullLiteral1 = ast.newNullLiteral();
/* 650 */           varDec.setInitializer(nullLiteral1);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 657 */       break;
/*     */     case isNull: 
/* 659 */       CastExpression cast = ast.newCastExpression();
/*     */       
/* 661 */       Type TypeName = getType2UseInJunitClass(this.clazz, ast);
/* 662 */       cast.setType(TypeName);
/* 663 */       Name qNameClass1 = getName2UseInJunitClass(this.currentWay.field.getDeclaringClass(), ast);
/* 664 */       QualifiedName qName = ast.newQualifiedName(qNameClass1, ast.newSimpleName(this.currentWay.field.getName()));
/* 665 */       cast.setExpression(qName);
/*     */       
/* 667 */       varDec.setInitializer(cast);
/*     */       
/*     */ 
/* 670 */       break;
/*     */     case constructor: 
/* 672 */       MethodInvocation newInstanceInv = ast.newMethodInvocation();
/* 673 */       Name qNameClass = getName2UseInJunitClass(this.stub, ast);
/* 674 */       newInstanceInv.setExpression(qNameClass);
/* 675 */       newInstanceInv.setName(ast.newSimpleName("newInstance"));
/* 676 */       varDec.setInitializer(newInstanceInv);
/*     */       
/*     */ 
/* 679 */       break;
/*     */     default: 
/* 681 */       System.out.println(this.currentWay);
/*     */     }
/*     */     
/* 684 */     VariableDeclarationStatement varDecStat = ast.newVariableDeclarationStatement(varDec);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 689 */     Type TypeName = getType2UseInJunitClass(this.clazz, ast);
/* 690 */     varDecStat.setType(TypeName);
/*     */     
/* 692 */     if ((!(varDec.getInitializer() instanceof NullLiteral)) && ((this.exceptions.size() > 0) || (getUnexpectedException() != null) || (this.SystematicallySurroundCall)))
/*     */     {
/* 694 */       Assignment Ass = ast.newAssignment();
/* 695 */       Ass.setLeftHandSide(ast.newSimpleName(varName));
/* 696 */       Expression leftHandExp = varDec.getInitializer();
/* 697 */       varDec.setInitializer(ast.newNullLiteral());
/* 698 */       returnList.add(varDecStat);
/*     */       
/* 700 */       Ass.setRightHandSide(leftHandExp);
/* 701 */       ExpressionStatement AssStatement = ast.newExpressionStatement(Ass);
/*     */       
/* 703 */       TryStatement tryStatement = ast.newTryStatement();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 709 */       Class except = Exception.class;
/* 710 */       tryStatement.catchClauses().add(getCatchClause(except, ast, false));
/*     */       
/* 712 */       Block b = ast.newBlock();
/* 713 */       b.statements().add(AssStatement);
/* 714 */       tryStatement.setBody(b);
/* 715 */       returnList.add(tryStatement);
/*     */     } else {
/* 717 */       returnList.add(varDecStat);
/*     */     }
/*     */     
/* 720 */     return returnList;
/*     */   }
/*     */   
/*     */   public Object getInstanceOnce() {
/* 724 */     this.once = true;
/* 725 */     return getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getInstance()
/*     */   {
/* 731 */     this.exceptions = new HashSet();
/* 732 */     if (this.currentWay.toString().startsWith("readFrom"))
/* 733 */       this.currentWay = this.currentWay;
/*     */     try {
/* 735 */       switch (this.currentWay.source)
/*     */       {
/*     */       case newInstance: 
/* 738 */         this.object = null;
/* 739 */         break;
/*     */       case dataMember: 
/* 741 */         final Constructor tmpCon = this.currentWay.constructor;
/* 742 */         AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */           public Object run() throws Exception {
/* 744 */             if (!tmpCon.isAccessible()) {
/* 745 */               tmpCon.setAccessible(true);
/*     */             }
/*     */             
/* 748 */             InstanceGenerator.this.exceptions = new HashSet();
/*     */             Class[] arrayOfClass;
/* 750 */             int j = (arrayOfClass = tmpCon.getExceptionTypes()).length; for (int i = 0; i < j; i++) { Class thro = arrayOfClass[i];
/* 751 */               InstanceGenerator.this.addExceptionClass(thro);
/*     */             }
/*     */             try {
/* 754 */               InstanceGenerator.this.object = tmpCon.newInstance(InstanceGenerator.this.getParameters());
/*     */             }
/*     */             catch (Throwable e) {
/* 757 */               InstanceGenerator.this.setUnexpectedException(e);
/*     */             }
/* 759 */             return null;
/*     */           }
/* 761 */         });
/* 762 */         break;
/*     */       case externalMethod: 
/*     */       case factoryMethod: 
/* 765 */         final Method tmpMethod1 = this.currentWay.method;
/*     */         Object tmpObject;
/* 767 */         final Object tmpObject; if (Modifier.isStatic(tmpMethod1.getModifiers())) {
/* 768 */           tmpObject = tmpMethod1.getDeclaringClass();
/*     */         }
/*     */         else {
/* 771 */           if (this.externalConstructor.currentWay.source == AbstractDynamicGenerator.ExecutionSource.isNull) {
/* 772 */             this.object = null;
/* 773 */             break;
/*     */           }
/* 775 */           tmpObject = this.externalConstructor.object;
/*     */         }
/* 777 */         AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */           public Object run() throws Exception {
/* 779 */             if (!tmpMethod1.isAccessible()) {
/* 780 */               tmpMethod1.setAccessible(true);
/*     */             }
/*     */             
/* 783 */             InstanceGenerator.this.exceptions = new HashSet();
/*     */             Class[] arrayOfClass;
/* 785 */             int j = (arrayOfClass = tmpMethod1.getExceptionTypes()).length; for (int i = 0; i < j; i++) { Class thro = arrayOfClass[i];
/* 786 */               InstanceGenerator.this.addExceptionClass(thro);
/*     */             }
/* 788 */             try { InstanceGenerator.this.object = tmpMethod1.invoke(tmpObject, InstanceGenerator.this.getParameters());
/*     */             } catch (Throwable e) {
/* 790 */               InstanceGenerator.this.setUnexpectedException(e);
/*     */             }
/* 792 */             return null;
/*     */           }
/* 794 */         });
/* 795 */         break;
/*     */       
/*     */ 
/*     */       case constructor: 
/* 799 */         this.exceptions = new HashSet();
/* 800 */         setUnexpectedException(null);
/*     */         try {
/* 802 */           this.object = this.stub.newInstance();
/*     */         } catch (Throwable e) {
/* 804 */           setUnexpectedException(e);
/*     */         }
/*     */       
/*     */       case isNull: 
/* 808 */         final Field tmpField = this.currentWay.field;
/* 809 */         AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */           public Object run() throws Exception {
/* 811 */             if (!tmpField.isAccessible()) {
/* 812 */               tmpField.setAccessible(true);
/*     */             }
/*     */             
/* 815 */             InstanceGenerator.this.exceptions = new HashSet();
/* 816 */             InstanceGenerator.this.setUnexpectedException(null);
/*     */             try {
/* 818 */               InstanceGenerator.this.object = tmpField.get(null);
/*     */             } catch (Throwable e) {
/* 820 */               InstanceGenerator.this.setUnexpectedException(e);
/*     */             }
/* 822 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/* 828 */       if ((exc instanceof IllegalArgumentException))
/* 829 */         exc.printStackTrace();
/* 830 */       if (((exc instanceof IllegalAccessException)) || 
/* 831 */         ((exc instanceof InstantiationException)) || 
/* 832 */         ((exc instanceof InvocationTargetException)))
/*     */       {
/*     */ 
/* 835 */         exc.printStackTrace();
/*     */       }
/* 837 */       if (!(exc instanceof InvocationTargetException))
/*     */       {
/* 839 */         exc.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 843 */     if (this.once) {
/* 844 */       return this.object;
/*     */     }
/*     */     
/* 847 */     if (this.object == null)
/*     */     {
/* 849 */       if (this.currentWay.source != AbstractDynamicGenerator.ExecutionSource.isNull) {
/* 850 */         this.generationNbr += 1;
/* 851 */         this.currentWay.cost += PENALITY_COST;
/* 852 */         this.lastGenerationCost = this.currentWay.cost;
/* 853 */         if (this.generationNbr <= MAX_GENERATION_NBR) {
/* 854 */           selectInstantiationWay();
/* 855 */           generateRandom();
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 864 */       this.currentWay.cost += AbstractDynamicGenerator.ExecutionWay.getInitialCost(this.currentWay) + PENALITY_COST / 10;
/* 865 */       this.lastGenerationCost = this.currentWay.cost;
/*     */       
/*     */ 
/* 868 */       if ((this.object != null) && (this.stub != null) && (!defaultClassesSet.contains(this.stub)) && (!this.stub.isAnonymousClass()) && (!this.stub.equals(Object.class)) && (isAccessible(this.stub))) {
/* 869 */         defaultClassesSet.add(this.stub);
/*     */       }
/*     */     }
/* 872 */     return this.object;
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 877 */     InstanceGenerator newCon = new InstanceGenerator(this.parent, this.clazz);
/*     */     
/*     */ 
/*     */ 
/* 881 */     newCon.withInstance = this.withInstance;
/*     */     
/* 883 */     newCon.fitness = this.fitness;
/* 884 */     newCon.currentWay = new AbstractDynamicGenerator.ExecutionWay(this.currentWay);
/* 885 */     newCon.object = this.object;
/* 886 */     newCon.seed = this.seed;
/* 887 */     newCon.random = this.random;
/* 888 */     if (this.parameters != null) {
/* 889 */       newCon.parameters = new Vector();
/* 890 */       for (AbsractGenerator gene : this.parameters) {
/* 891 */         newCon.parameters.add((AbsractGenerator)gene.clone());
/*     */       }
/*     */     }
/* 894 */     newCon.stub = this.stub;
/*     */     
/*     */ 
/*     */ 
/* 898 */     if (this.recommandedWays != null)
/* 899 */       newCon.recommandedWays = this.recommandedWays;
/* 900 */     newCon.exceptions = new HashSet();
/* 901 */     for (Class e : this.exceptions)
/* 902 */       newCon.addExceptionClass(e);
/* 903 */     newCon.unexpectedException = this.unexpectedException;
/* 904 */     newCon.generationNbr = this.generationNbr;
/* 905 */     newCon.parent = this.parent;
/* 906 */     newCon.possibleWays = this.possibleWays;
/*     */     
/* 908 */     if (this.externalConstructor != null) {
/* 909 */       newCon.externalConstructor = ((InstanceGenerator)this.externalConstructor.clone());
/*     */     }
/* 911 */     return newCon;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 916 */     if ((this.object != null) && (this.object.getClass().equals(Object.class))) {
/* 917 */       this.object = this.object;
/*     */     }
/* 919 */     String str = new String();
/* 920 */     switch (this.currentWay.source) {
/*     */     case dataMember: 
/* 922 */       str = this.currentWay.constructor.getName();
/* 923 */       if (this.parameters != null)
/* 924 */         str = str + this.parameters.toString();
/* 925 */       break;
/*     */     case externalMethod: 
/*     */     case factoryMethod: 
/* 928 */       str = this.currentWay.method.getName();
/* 929 */       if (this.parameters != null)
/* 930 */         str = str + this.parameters.toString();
/* 931 */       break;
/*     */     case isNull: 
/* 933 */       str = this.currentWay.field.getName();
/*     */     
/*     */ 
/*     */     case newInstance: 
/* 937 */       str = "null";
/*     */     }
/*     */     
/* 940 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isStatic()
/*     */   {
/* 946 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\dynamic\InstanceGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */