/*     */ package csbst.generators.dynamic;
/*     */ 
/*     */ import csbst.ga.ecj.TestCaseCandidate;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.generators.CopyGenerator;
/*     */ import csbst.testing.ClassUnderTest;
/*     */ import csbst.testing.JTE;
/*     */ import ec.util.RandomChoice;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDynamicGenerator<T>
/*     */   extends AbsractGenerator
/*     */ {
/*  33 */   public static Map<Class, Long> stub2Cost = new HashMap();
/*  34 */   protected static Long MAX_COST = Long.valueOf(1000000L);
/*  35 */   protected static int INITIAL_COST = 20;
/*  36 */   protected static int PENALITY_COST = 200;
/*  37 */   protected static int MAX_GENERATION_NBR = 2;
/*     */   
/*  39 */   protected static boolean selectRandom = false;
/*     */   
/*     */   protected int lastGenerationCost;
/*     */   protected Vector<AbsractGenerator> parameters;
/*     */   protected Vector<T> recommandedWays;
/*  44 */   protected Vector<ExecutionWay> possibleWays = new Vector();
/*     */   protected ExecutionWay currentWay;
/*  46 */   protected Set<Class> exceptions = new HashSet();
/*     */   
/*     */   protected Throwable unexpectedException;
/*     */   
/*     */   protected InstanceGenerator externalConstructor;
/*     */   protected Class stub;
/*  52 */   protected int generationNbr = 0;
/*     */   protected boolean withInstance;
/*     */   protected ParameterizedType parameterizedType;
/*     */   protected TestCaseCandidate testCaseCandidate;
/*  56 */   public boolean SystematicallySurroundCall = false;
/*  57 */   protected boolean isParameter = true;
/*     */   
/*     */ 
/*     */ 
/*     */   public AbstractDynamicGenerator(AbsractGenerator parent, Class clazz, Class stub, ParameterizedType parameterizedType, Vector<T> recommandedMethodes, boolean wi)
/*     */   {
/*  63 */     super(parent, clazz);
/*  64 */     this.recommandedWays = recommandedMethodes;
/*  65 */     this.parameterizedType = parameterizedType;
/*  66 */     this.currentWay = new ExecutionWay(ExecutionSource.isNull);
/*  67 */     this.withInstance = wi;
/*  68 */     if (this.stub == null) {
/*  69 */       this.stub = clazz;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */     this.lastGenerationCost = 0;
/*  77 */     generateInstantiationWays(clazz);
/*  78 */     selectInstantiationWay();
/*     */     
/*     */ 
/*     */ 
/*  82 */     if (!canGoOn()) {
/*  83 */       this.currentWay = new ExecutionWay(ExecutionSource.isNull);
/*  84 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   public AbstractDynamicGenerator(AbsractGenerator parent, Class clazz, Class stub, Vector<T> recommandedMethodes, boolean wi)
/*     */   {
/*  90 */     this(parent, clazz, stub, null, recommandedMethodes, wi);
/*     */   }
/*     */   
/*     */   public AbstractDynamicGenerator(AbsractGenerator parent, Class clazz, Vector<T> recommandedMethodes, boolean wi) {
/*  94 */     this(parent, clazz, clazz, recommandedMethodes, wi);
/*     */   }
/*     */   
/*     */   public AbstractDynamicGenerator(AbsractGenerator parent, Class clazz, Class stub, Vector<T> recommandedMethodes) {
/*  98 */     this(parent, clazz, stub, recommandedMethodes, true);
/*     */   }
/*     */   
/*     */   public AbstractDynamicGenerator(AbsractGenerator parent, Class clazz) {
/* 102 */     this(parent, clazz, clazz, new Vector(), true);
/*     */   }
/*     */   
/*     */   public Class getStub() {
/* 106 */     return this.stub;
/*     */   }
/*     */   
/*     */   protected boolean canGoOn() {
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public Vector<AbsractGenerator> getChromosome() {
/* 114 */     return this.parameters;
/*     */   }
/*     */   
/*     */   protected abstract Vector<ExecutionWay> generateInstantiationWays(Class paramClass);
/*     */   
/*     */   public Object[] getParameters() {
/* 120 */     Object[] params = new Object[this.parameters.size()];
/* 121 */     for (int i = 0; i < this.parameters.size(); i++) {
/* 122 */       if ((this.parameters.get(i) instanceof CopyGenerator))
/*     */       {
/* 124 */         params[i] = ((MethodGenerator)this).classUTInstance;
/*     */       }
/*     */       else
/* 127 */         params[i] = ((AbsractGenerator)this.parameters.get(i)).getObject();
/*     */     }
/* 129 */     return params;
/*     */   }
/*     */   
/*     */   protected void selectInstantiationWay()
/*     */   {
/* 134 */     if (this.possibleWays.size() < 1) {
/* 135 */       this.currentWay = new ExecutionWay(ExecutionSource.isNull);
/* 136 */       return;
/*     */     }
/*     */     
/* 139 */     this.currentWay = ExecutionWay.selectWay(this.possibleWays, this);
/* 140 */     if (this.currentWay.source == ExecutionSource.constructor) {
/* 141 */       this.stub = this.currentWay.cls;
/*     */     } else {
/* 143 */       this.stub = this.clazz;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void generateParameters() {
/* 148 */     boolean anyGeneNotInstiantiable = false;
/* 149 */     this.parameters = new Vector();
/*     */     Class[] methodParameters;
/*     */     Class[] methodParameters;
/* 152 */     if (this.currentWay.source == ExecutionSource.constructor) {
/* 153 */       methodParameters = this.currentWay.constructor.getParameterTypes();
/*     */     }
/*     */     else
/*     */     {
/* 157 */       methodParameters = this.currentWay.method.getParameterTypes();
/*     */     }
/*     */     
/* 160 */     for (int i = 0; i < methodParameters.length; i++)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 167 */       Class cls = methodParameters[i];
/*     */       AbsractGenerator gene;
/* 169 */       AbsractGenerator gene; if (((this instanceof MethodGenerator)) && (cls.isAssignableFrom(this.clazz)) && 
/* 170 */         (!((MethodGenerator)this).isStatic()) && (this.random.nextInt(100) < 5)) {
/* 171 */         gene = new CopyGenerator();
/*     */       } else { AbsractGenerator gene;
/* 173 */         if ((Collection.class.isAssignableFrom(cls)) || (Map.class.isAssignableFrom(cls))) { AbsractGenerator gene;
/* 174 */           if (this.currentWay.source == ExecutionSource.constructor) {
/* 175 */             gene = createAdequateGene(this, cls, this.currentWay.constructor.getGenericParameterTypes()[i]);
/*     */           } else {
/* 177 */             gene = createAdequateGene(this, cls, this.currentWay.method.getGenericParameterTypes()[i]);
/*     */           }
/*     */         } else {
/* 180 */           gene = createAdequateGene(this, cls);
/*     */         }
/*     */         
/* 183 */         gene.generateRandom();
/* 184 */         if ((gene.getObject() == null) && ((gene instanceof InstanceGenerator)) && (((InstanceGenerator)gene).currentWay.source != ExecutionSource.isNull))
/* 185 */           this.currentWay.cost += PENALITY_COST;
/* 186 */         if ((gene instanceof InstanceGenerator))
/* 187 */           this.currentWay.cost += ((InstanceGenerator)gene).lastGenerationCost;
/*     */       }
/* 189 */       this.parameters.add(gene);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isAccessible(Class cls1)
/*     */   {
/* 196 */     Class cls = cls1;
/*     */     
/* 198 */     if (cls.equals(JTE.currentClassUnderTest.getClazz())) {
/* 199 */       return true;
/*     */     }
/* 201 */     while (cls.isArray()) {
/* 202 */       cls = cls.getComponentType();
/*     */     }
/* 204 */     if (cls.isPrimitive()) {
/* 205 */       return true;
/*     */     }
/* 207 */     boolean accessibility = 
/*     */     
/* 209 */       (Modifier.isPublic(cls.getModifiers())) || (
/* 210 */       (JTE.currentClassUnderTest.getClazz().getPackage().equals(cls.getPackage())) && (!Modifier.isPrivate(cls.getModifiers())) && 
/* 211 */       (!Modifier.isProtected(cls.getModifiers())));
/*     */     
/* 213 */     if (!accessibility) {
/* 214 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 220 */     if ((accessibility) && (cls.getDeclaringClass() != null)) {
/* 221 */       return isAccessible(cls.getDeclaringClass());
/*     */     }
/* 223 */     return accessibility;
/*     */   }
/*     */   
/*     */   private boolean isInClassPath(Class cls) {
/* 227 */     String path = getResourcePath(cls);
/*     */     
/* 229 */     if ((path.endsWith("/rt.jar")) || (path.endsWith("/classes.jar"))) {
/* 230 */       return true;
/*     */     }
/* 232 */     for (int i = 0; i < JTE.classPath.length; i++) {
/* 233 */       if (path.equalsIgnoreCase(JTE.classPath[i]))
/* 234 */         return true;
/*     */     }
/* 236 */     return false;
/*     */   }
/*     */   
/*     */   protected String getResourcePath(Class clss) {
/* 240 */     String resource = '/' + clss.getName().replace('.', '/') + ".class";
/* 241 */     URL location = clss.getResource(resource);
/*     */     
/* 243 */     String locstr = location.toString();
/* 244 */     int first = locstr.lastIndexOf(":") + 1;
/* 245 */     int last = locstr.indexOf("!");
/* 246 */     if (last <= 0)
/* 247 */       last = locstr.indexOf(resource);
/* 248 */     last = last > 0 ? last : locstr.length();
/*     */     
/* 250 */     String path = locstr.substring(first, last);
/*     */     
/*     */ 
/* 253 */     return path;
/*     */   }
/*     */   
/*     */   protected boolean isAccessible(Method method) {
/* 257 */     Class declaringClass = method.getDeclaringClass();
/*     */     Class[] arrayOfClass;
/* 259 */     int j = (arrayOfClass = method.getParameterTypes()).length; for (int i = 0; i < j; i++) { Class p = arrayOfClass[i];
/* 260 */       if ((p.isAnonymousClass()) || (!isAccessible(p)))
/*     */       {
/* 262 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 271 */     return (isAccessible(declaringClass)) && (!method.getName().toString().startsWith("access$")) && (!Modifier.isAbstract(method.getModifiers())) && (!Modifier.isPrivate(method.getModifiers())) && (!Modifier.isProtected(method.getModifiers())) && ((Modifier.isPublic(method.getModifiers())) || (JTE.packageName.equals(declaringClass.getPackage().getName())));
/*     */   }
/*     */   
/*     */   protected boolean isAccessible(Constructor con)
/*     */   {
/* 276 */     Class declaringClass = con.getDeclaringClass();
/*     */     
/*     */     Class[] arrayOfClass;
/* 279 */     int j = (arrayOfClass = con.getParameterTypes()).length; for (int i = 0; i < j; i++) { Class p = arrayOfClass[i];
/* 280 */       if ((p.isAnonymousClass()) || (!isAccessible(p)))
/*     */       {
/* 282 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 286 */     String pkg = "";
/* 287 */     if (declaringClass.getPackage() != null) {
/* 288 */       pkg = declaringClass.getPackage().getName();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 294 */     return ((!Modifier.isProtected(con.getModifiers())) && (!Modifier.isPrivate(con.getModifiers())) && (JTE.packageName.equals(pkg))) || ((Modifier.isPublic(con.getModifiers())) && (isAccessible(declaringClass)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 301 */     int hash = getClass().hashCode();
/*     */     
/* 303 */     hash = hash << 1 | hash >>> 31;
/* 304 */     if (this.parameters != null) {
/* 305 */       for (int x = 0; x < this.parameters.size(); x++)
/* 306 */         if (this.parameters.get(x) != null)
/* 307 */           hash = (hash << 1 | hash >>> 31) ^ ((AbsractGenerator)this.parameters.get(x)).hashCode();
/*     */     }
/* 309 */     return hash;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void generateRandom()
/*     */   {
/* 316 */     if (this.currentWay.source == ExecutionSource.isNull) {
/* 317 */       return;
/*     */     }
/* 319 */     Random rand = new Random();
/* 320 */     if ((rand.nextInt(100) < 5) && (this.withInstance) && (this.isParameter)) {
/* 321 */       this.currentWay = new ExecutionWay(ExecutionSource.isNull);
/* 322 */       return;
/*     */     }
/*     */     
/* 325 */     if ((this.currentWay.source == ExecutionSource.externalMethod) && 
/* 326 */       (!Modifier.isStatic(this.currentWay.method.getModifiers()))) {
/*     */       try
/*     */       {
/* 329 */         this.externalConstructor = new InstanceGenerator(this, this.currentWay.method.getDeclaringClass(), true);
/* 330 */         this.externalConstructor.isParameter = false;
/*     */         
/* 332 */         this.externalConstructor.generateRandom();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 336 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 341 */     if ((this.currentWay.source == ExecutionSource.constructor) || 
/* 342 */       (this.currentWay.source == ExecutionSource.factoryMethod) || 
/* 343 */       (this.currentWay.source == ExecutionSource.externalMethod)) {
/* 344 */       generateParameters();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void mutate()
/*     */   {
/* 352 */     int parNbr = 0;
/* 353 */     if (this.parameters != null)
/* 354 */       parNbr = this.parameters.size();
/* 355 */     double mutPb = 1.0D / (1.0D + parNbr);
/*     */     
/* 357 */     if (this.random.nextDouble() <= mutPb) {
/* 358 */       this.lastGenerationCost = 0;
/* 359 */       generateRandom();
/* 360 */       return;
/*     */     }
/* 362 */     if (this.parameters != null) {
/* 363 */       for (AbsractGenerator gene : this.parameters) {
/* 364 */         if (this.random.nextDouble() <= mutPb)
/* 365 */           gene.mutate();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene) {
/* 371 */     boolean returnValue = false;
/* 372 */     if ((this instanceof MethodGenerator)) {
/* 373 */       returnValue = gene instanceof MethodGenerator;
/*     */     }
/* 375 */     if ((this instanceof InstanceGenerator)) {
/* 376 */       returnValue = gene instanceof InstanceGenerator;
/*     */     }
/* 378 */     returnValue = (returnValue) && (this.currentWay.source == ((AbstractDynamicGenerator)gene).currentWay.source);
/* 379 */     returnValue = (returnValue) && (this.currentWay.constructor == ((AbstractDynamicGenerator)gene).currentWay.constructor);
/* 380 */     returnValue = (returnValue) && (this.currentWay.field == ((AbstractDynamicGenerator)gene).currentWay.field);
/* 381 */     returnValue = (returnValue) && (this.currentWay.method == ((AbstractDynamicGenerator)gene).currentWay.method);
/* 382 */     return returnValue;
/*     */   }
/*     */   
/*     */ 
/*     */   public void defaultCrossover(AbsractGenerator gene)
/*     */   {
/* 388 */     Random rand = new Random();
/* 389 */     if ((this.parameters == null) || (this.parameters.size() == 0) || 
/* 390 */       (((AbstractDynamicGenerator)gene).parameters == null) || (((AbstractDynamicGenerator)gene).parameters.size() == 0)) {
/* 391 */       return;
/*     */     }
/* 393 */     int len = this.parameters.size() <= ((AbstractDynamicGenerator)gene).parameters.size() ? this.parameters.size() : ((AbstractDynamicGenerator)gene).parameters.size();
/*     */     int index;
/* 395 */     int index; if (len == 1) {
/* 396 */       index = 0;
/*     */     } else {
/* 398 */       index = rand.nextInt(len);
/*     */     }
/* 400 */     if ((!((AbsractGenerator)this.parameters.get(index)).isSameFamillyAs((AbsractGenerator)((AbstractDynamicGenerator)gene).parameters.get(index))) || 
/* 401 */       (rand.nextBoolean())) {
/* 402 */       AbsractGenerator tmpGene = (AbsractGenerator)((AbsractGenerator)this.parameters.get(index)).clone();
/* 403 */       this.parameters.set(index, (AbsractGenerator)((AbstractDynamicGenerator)gene).parameters.get(index));
/* 404 */       ((AbstractDynamicGenerator)gene).parameters.set(index, tmpGene);
/*     */     } else {
/* 406 */       ((AbsractGenerator)this.parameters.get(index)).defaultCrossover((AbsractGenerator)((AbstractDynamicGenerator)gene).parameters.get(index));
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract boolean isStatic();
/*     */   
/*     */   public Set<Class> getExceptions()
/*     */   {
/* 414 */     return this.exceptions;
/*     */   }
/*     */   
/*     */   public void addExceptionClass(Class exception) {
/* 418 */     if (!isAccessible(exception))
/* 419 */       return;
/* 420 */     for (Class e : this.exceptions) {
/* 421 */       if (exception.isAssignableFrom(e)) {
/* 422 */         e = exception;
/* 423 */         return;
/*     */       }
/* 425 */       if (e.isAssignableFrom(exception))
/* 426 */         return;
/*     */     }
/* 428 */     this.exceptions.add(exception);
/*     */   }
/*     */   
/*     */   public Throwable getUnexpectedException() {
/* 432 */     return this.unexpectedException;
/*     */   }
/*     */   
/*     */   public void setUnexpectedException(Throwable ue) {
/* 436 */     this.unexpectedException = ue;
/* 437 */     if (ue == null) {
/* 438 */       return;
/*     */     }
/* 440 */     this.unexpectedException = ue.getCause();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static boolean isParent(Class cls, InstanceGenerator gc)
/*     */   {
/* 447 */     AbsractGenerator currentParent = gc.parent;
/* 448 */     while ((currentParent != null) && (!(currentParent instanceof MethodGenerator))) {
/* 449 */       if ((currentParent instanceof InstanceGenerator)) {
/* 450 */         if (((InstanceGenerator)currentParent).clazz.equals(cls))
/* 451 */           return true;
/* 452 */         if ((((InstanceGenerator)currentParent).getStub() != null) && (((InstanceGenerator)currentParent).getStub().equals(cls)))
/* 453 */           return true;
/*     */       }
/* 455 */       currentParent = currentParent.getParent();
/*     */     }
/* 457 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isParent(Method md, InstanceGenerator gc) {
/* 461 */     Class dc = md.getDeclaringClass();
/* 462 */     if ((isParent(dc, gc)) || (gc.clazz.equals(dc)) || (gc.getStub().equals(dc)))
/* 463 */       return true;
/* 464 */     Class[] methodParameters = md.getParameterTypes();
/* 465 */     Class[] arrayOfClass1; int j = (arrayOfClass1 = methodParameters).length; for (int i = 0; i < j; i++) { Class p = arrayOfClass1[i];
/* 466 */       if ((isParent(p, gc)) || (gc.clazz.equals(p)) || (gc.getStub().equals(p)))
/* 467 */         return true;
/*     */     }
/* 469 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isParent(Constructor con, InstanceGenerator gc) {
/* 473 */     Class[] methodParameters = con.getParameterTypes();
/* 474 */     Class[] arrayOfClass1; int j = (arrayOfClass1 = methodParameters).length; for (int i = 0; i < j; i++) { Class p = arrayOfClass1[i];
/* 475 */       if ((isParent(p, gc)) || (gc.clazz.equals(p)) || (gc.getStub().equals(p)))
/* 476 */         return true;
/*     */     }
/* 478 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isParent(ExecutionWay iw, InstanceGenerator gc) {
/* 482 */     switch (iw.source) {
/*     */     case dataMember: 
/* 484 */       return isParent(iw.constructor, gc);
/*     */     case factoryMethod: 
/* 486 */       return isParent(iw.method, gc);
/*     */     }
/* 488 */     return false;
/*     */   }
/*     */   
/*     */   protected static enum ExecutionSource {
/* 492 */     newInstance, 
/* 493 */     constructor, 
/* 494 */     factoryMethod, 
/* 495 */     externalMethod, 
/* 496 */     dataMember, 
/* 497 */     isNull;
/*     */   }
/*     */   
/*     */   public static class ExecutionWay
/*     */   {
/* 502 */     int level = 0;
/*     */     int cost;
/*     */     int initialCost;
/*     */     AbstractDynamicGenerator.ExecutionSource source;
/*     */     Constructor constructor;
/*     */     Method method;
/*     */     Field field;
/*     */     Class cls;
/*     */     
/*     */     ExecutionWay(Class cls) {
/* 512 */       this.source = AbstractDynamicGenerator.ExecutionSource.newInstance;
/* 513 */       this.cost = 0;
/* 514 */       this.cost += AbstractDynamicGenerator.INITIAL_COST;
/* 515 */       this.cls = cls;
/*     */     }
/*     */     
/*     */     ExecutionWay(Field field) {
/* 519 */       this.field = field;
/* 520 */       this.source = AbstractDynamicGenerator.ExecutionSource.dataMember;
/* 521 */       this.cost = 0;
/* 522 */       this.cost += AbstractDynamicGenerator.INITIAL_COST;
/* 523 */       this.cls = field.getDeclaringClass();
/*     */     }
/*     */     
/*     */     ExecutionWay(Constructor constructor) {
/* 527 */       this(constructor, false);
/*     */     }
/*     */     
/*     */     ExecutionWay(Constructor constructor, boolean withoutInitialCost)
/*     */     {
/* 532 */       this.constructor = constructor;
/* 533 */       this.source = AbstractDynamicGenerator.ExecutionSource.constructor;
/* 534 */       this.cost = (constructor.getParameterTypes().length * AbstractDynamicGenerator.INITIAL_COST);
/* 535 */       if (!withoutInitialCost) {
/* 536 */         this.cost += AbstractDynamicGenerator.INITIAL_COST;
/* 537 */         this.initialCost = AbstractDynamicGenerator.INITIAL_COST;
/*     */       }
/* 539 */       this.cls = constructor.getDeclaringClass();
/*     */     }
/*     */     
/*     */     ExecutionWay(Method method) {
/* 543 */       this(method, AbstractDynamicGenerator.INITIAL_COST);
/*     */     }
/*     */     
/*     */     ExecutionWay(Method method, int InitialCost)
/*     */     {
/* 548 */       this.method = method;
/* 549 */       this.source = AbstractDynamicGenerator.ExecutionSource.factoryMethod;
/* 550 */       this.cost = (method.getParameterTypes().length * AbstractDynamicGenerator.INITIAL_COST);
/*     */       
/* 552 */       this.cost += InitialCost;
/* 553 */       this.initialCost = InitialCost;
/* 554 */       this.cls = method.getDeclaringClass();
/*     */     }
/*     */     
/*     */     ExecutionWay(Method method, Class clss)
/*     */     {
/* 559 */       this.method = method;
/* 560 */       this.source = AbstractDynamicGenerator.ExecutionSource.externalMethod;
/* 561 */       this.cost = ((method.getParameterTypes().length + 1) * AbstractDynamicGenerator.INITIAL_COST);
/* 562 */       this.cost += AbstractDynamicGenerator.INITIAL_COST;
/* 563 */       this.cls = method.getDeclaringClass();
/*     */     }
/*     */     
/*     */     public static int getInitialCost(ExecutionWay iw) {
/* 567 */       int theCost = iw.initialCost;
/* 568 */       switch (iw.source) {
/*     */       case dataMember: 
/* 570 */         theCost += iw.constructor.getParameterTypes().length * AbstractDynamicGenerator.INITIAL_COST;
/* 571 */         break;
/*     */       case externalMethod: 
/*     */       case factoryMethod: 
/* 574 */         theCost += (iw.method.getParameterTypes().length + 1) * AbstractDynamicGenerator.INITIAL_COST;
/* 575 */         break;
/*     */       case isNull: 
/*     */       case newInstance: 
/* 578 */         theCost = 0;
/*     */       }
/* 580 */       return theCost;
/*     */     }
/*     */     
/*     */     public ExecutionWay(ExecutionWay iw) {
/* 584 */       this.constructor = iw.constructor;
/* 585 */       this.cost = iw.cost;
/* 586 */       this.field = iw.field;
/* 587 */       this.method = iw.method;
/* 588 */       this.source = iw.source;
/* 589 */       this.cls = iw.cls;
/*     */     }
/*     */     
/*     */     public ExecutionWay(AbstractDynamicGenerator.ExecutionSource isstatic) {
/* 593 */       this.source = isstatic;
/* 594 */       this.cls = null;
/*     */     }
/*     */     
/*     */     static ExecutionWay selectWay(Vector<ExecutionWay> allWays, AbstractDynamicGenerator gc)
/*     */     {
/* 599 */       if (AbstractDynamicGenerator.selectRandom) {
/* 600 */         Random rand = new Random();
/* 601 */         int index = rand.nextInt(allWays.size());
/* 602 */         return (ExecutionWay)allWays.get(index);
/*     */       }
/*     */       
/*     */ 
/* 606 */       double[] probabilities = new double[allWays.size()];
/* 607 */       long minCost = 0L;
/* 608 */       for (int i = 0; i < allWays.size(); i++) {
/* 609 */         Long stubCost = null;
/* 610 */         if ((gc instanceof InstanceGenerator)) {
/* 611 */           ((InstanceGenerator)gc);stubCost = (Long)InstanceGenerator.stub2Cost.get(allWays.get(i)); }
/* 612 */         long wayCost = 0L;
/* 613 */         if (stubCost != null)
/* 614 */           wayCost = stubCost.longValue();
/* 615 */         wayCost += ((ExecutionWay)allWays.get(i)).cost;
/* 616 */         if (minCost > wayCost)
/* 617 */           minCost = wayCost;
/* 618 */         if (((gc instanceof InstanceGenerator)) && 
/* 619 */           (AbstractDynamicGenerator.isParent((ExecutionWay)allWays.get(i), (InstanceGenerator)gc))) wayCost += AbstractDynamicGenerator.MAX_COST.longValue();
/* 620 */         if (wayCost < 0L)
/* 621 */           wayCost = AbstractDynamicGenerator.MAX_COST.longValue();
/* 622 */         probabilities[i] = (1.0D / (AbstractDynamicGenerator.INITIAL_COST + wayCost));
/* 623 */         if (probabilities[i] < 0.0D) {
/* 624 */           probabilities[i] = (1.0D / AbstractDynamicGenerator.MAX_COST.longValue());
/*     */         }
/*     */       }
/*     */       
/* 628 */       if ((gc instanceof InstanceGenerator)) {
/* 629 */         ((InstanceGenerator)gc);InstanceGenerator.stub2Cost.put(((InstanceGenerator)gc).getStub(), Long.valueOf(minCost));
/*     */       }
/*     */       
/* 632 */       RandomChoice.organizeDistribution(probabilities, true);
/* 633 */       Random rand = new Random();
/* 634 */       int index = RandomChoice.pickFromDistribution(probabilities, rand.nextDouble());
/*     */       
/* 636 */       ((ExecutionWay)allWays.get(index)).toString();
/* 637 */       return (ExecutionWay)allWays.get(index);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 641 */       String str = new String();
/* 642 */       switch (this.source) {
/*     */       case dataMember: 
/* 644 */         str = this.constructor.getName();
/* 645 */         break;
/*     */       case externalMethod: 
/*     */       case factoryMethod: 
/* 648 */         str = this.method.getName();
/* 649 */         break;
/*     */       case isNull: 
/* 651 */         str = this.field.getName();
/*     */       
/*     */ 
/*     */       case newInstance: 
/* 655 */         str = "null";
/*     */       }
/* 657 */       str = str + "[cost=" + this.cost + "]";
/* 658 */       return str;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\dynamic\AbstractDynamicGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */