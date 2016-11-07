/*     */ package csbst.ga.ecj;
/*     */ 
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.generators.dynamic.AbstractDynamicGenerator;
/*     */ import csbst.generators.dynamic.ExternalMethodGenerator;
/*     */ import csbst.generators.dynamic.InstanceGenerator;
/*     */ import csbst.generators.dynamic.MethodGenerator;
/*     */ import csbst.testing.ClassUnderTest;
/*     */ import csbst.testing.DataMember;
/*     */ import csbst.testing.JTE;
/*     */ import csbst.testing.Target;
/*     */ import csbst.testing.fitness.ApproachLevel;
/*     */ import csbst.testing.fitness.NumberCoveredBranches;
/*     */ import csbst.testing.fitness.TestingFitness;
/*     */ import csbst.utils.ASTEditor;
/*     */ import ec.EvolutionState;
/*     */ import ec.util.Parameter;
/*     */ import ec.vector.VectorIndividual;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.ASTParser;
/*     */ import org.eclipse.jdt.core.dom.Annotation;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.core.dom.ImportDeclaration;
/*     */ import org.eclipse.jdt.core.dom.Javadoc;
/*     */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*     */ import org.eclipse.jdt.core.dom.Modifier;
/*     */ import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
/*     */ import org.eclipse.jdt.core.dom.Name;
/*     */ import org.eclipse.jdt.core.dom.PackageDeclaration;
/*     */ import org.eclipse.jdt.core.dom.QualifiedName;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.TagElement;
/*     */ import org.eclipse.jdt.core.dom.TextElement;
/*     */ import org.eclipse.jdt.core.dom.TypeDeclaration;
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
/*     */ public class TestCaseCandidate
/*     */   extends VectorIndividual
/*     */ {
/*     */   protected Vector<AbsractGenerator> genes;
/*     */   protected Set<Integer> coveredBranches;
/*     */   private TestCaseCandidate parent;
/*     */   protected Random random;
/*     */   private boolean isTestData;
/*     */   ClassUnderTest classUnderTest;
/*     */   Class AnonymousType;
/*     */   private boolean isPrototype;
/*  79 */   private boolean interrupted = false;
/*  80 */   boolean isGoodTestData = true;
/*     */   
/*     */   public TestCaseCandidate() {
/*  83 */     this.genes = new Vector();
/*  84 */     this.coveredBranches = new HashSet();
/*  85 */     this.random = new Random();
/*  86 */     this.isPrototype = false;
/*     */   }
/*     */   
/*     */   public TestCaseCandidate(ClassUnderTest clazzUT) {
/*  90 */     this.fitness = new NumberCoveredBranches();
/*  91 */     this.genes = new Vector();
/*  92 */     this.coveredBranches = new HashSet();
/*  93 */     this.classUnderTest = clazzUT;
/*     */   }
/*     */   
/*     */   public TestCaseCandidate(ClassUnderTest clazzUT, Class AnonymousType) {
/*  97 */     this(clazzUT);
/*  98 */     this.AnonymousType = AnonymousType;
/*     */   }
/*     */   
/*     */   public TestCaseCandidate(ClassUnderTest clazzUT, boolean isPrototype) {
/* 102 */     this(clazzUT);
/* 103 */     this.isPrototype = isPrototype;
/*     */   }
/*     */   
/*     */   public Class getAnonymousType() {
/* 107 */     return this.AnonymousType;
/*     */   }
/*     */   
/*     */   public void generateRandom()
/*     */   {
/* 112 */     if (this.classUnderTest.getClazz().isAnonymousClass()) {
/* 113 */       ClassUnderTest parentClassUnderTest = new ClassUnderTest(this.classUnderTest.getClazz().getEnclosingMethod().getDeclaringClass());
/*     */       try {
/* 115 */         parentClassUnderTest.prepareDataMembers();
/*     */       }
/*     */       catch (IOException e) {
/* 118 */         e.printStackTrace();
/*     */       }
/*     */       
/* 121 */       this.parent = new TestCaseCandidate(parentClassUnderTest, this.classUnderTest.getClazz().getSuperclass());
/* 122 */       this.parent.generateRandom();
/*     */       
/* 124 */       Vector<Method> tergatMethod = new Vector();
/* 125 */       tergatMethod.add(this.classUnderTest.getClazz().getEnclosingMethod());
/* 126 */       MethodGenerator method = new MethodGenerator(this.parent, parentClassUnderTest.getClazz(), ((InstanceGenerator)this.parent.genes.get(0)).getStub(), tergatMethod, true);
/* 127 */       method.generateRandom();
/* 128 */       this.parent.genes.add(method);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */     int length = 0;
/*     */     InstanceGenerator con;
/*     */     InstanceGenerator con;
/* 138 */     if ((JTE.currentTarget.getconstructorsMayReach().size() > 0) && (this.classUnderTest.equals(JTE.currentClassUnderTest))) {
/* 139 */       con = new InstanceGenerator(null, this.classUnderTest.getClazz(), JTE.currentTarget.getconstructorsMayReach(), false);
/*     */     } else {
/* 141 */       con = new InstanceGenerator(null, this.classUnderTest.getClazz(), false);
/*     */     }
/* 143 */     generateObjects(con);
/* 144 */     this.genes.add(con);
/*     */     
/*     */ 
/*     */ 
/* 148 */     if (this.classUnderTest.getDataMembers().size() > 0)
/*     */     {
/* 150 */       for (DataMember dm : this.classUnderTest.getDataMembers()) {
/* 151 */         if (dm.getMethodTransformers().size() > 0)
/*     */         {
/*     */ 
/* 154 */           MethodGenerator method = new MethodGenerator(null, this.classUnderTest.getClazz(), con.getStub(), dm.getMethodTransformers());
/*     */           
/* 156 */           generateObjects(method);
/*     */           
/* 158 */           this.genes.add(method);
/*     */         }
/*     */       }
/* 161 */       length = this.classUnderTest.getDataMembers().size();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 166 */     Random rand = new Random();
/* 167 */     length += rand.nextInt(3) + 1;
/*     */     
/*     */ 
/* 170 */     for (int i = this.classUnderTest.getDataMembers().size(); i < length; i++)
/*     */     {
/* 172 */       MethodGenerator method = new MethodGenerator(null, this.classUnderTest.getClazz(), con.getStub(), null);
/* 173 */       generateObjects(method);
/* 174 */       this.genes.add(method);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 180 */     if (this.classUnderTest.equals(JTE.currentClassUnderTest)) {
/* 181 */       MethodGenerator method = new MethodGenerator(null, this.classUnderTest.getClazz(), con.getStub(), JTE.currentTarget.getMethodsMayReach());
/* 182 */       method.generateRandom();
/* 183 */       this.genes.add(method);
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
/*     */   public void generateRandomNoOrientation()
/*     */   {
/* 196 */     InstanceGenerator con = new InstanceGenerator(null, this.classUnderTest.getClazz(), false);
/* 197 */     if (!isStatic())
/* 198 */       generateObjects(con);
/* 199 */     this.genes.add(con);
/*     */     
/* 201 */     Random rand = new Random();
/* 202 */     int length = rand.nextInt(10);
/*     */     
/* 204 */     for (int i = 0; i < length; i++) {
/* 205 */       MethodGenerator method = new MethodGenerator(null, this.classUnderTest.getClazz(), con.getStub(), null);
/* 206 */       generateObjects(method);
/* 207 */       this.genes.add(method);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private AbsractGenerator generateObjects(AbsractGenerator ge)
/*     */   {
/* 218 */     ge.generateRandom();
/* 219 */     return ge;
/*     */   }
/*     */   
/*     */   private void executeMethod(Object obj, MethodGenerator meth)
/*     */   {
/* 224 */     meth.execute(obj, meth.getClazz());
/*     */   }
/*     */   
/*     */   private void executeMethod(MethodGenerator meth)
/*     */   {
/* 229 */     meth.execute();
/*     */   }
/*     */   
/*     */   public void mutate() {
/* 233 */     double mutPb = 1.0D / this.genes.size();
/* 234 */     for (int x = 0; x < this.genes.size(); x++) {
/* 235 */       if (this.random.nextDouble() <= mutPb) {
/* 236 */         ((AbsractGenerator)this.genes.get(x)).mutate();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void defaultCrossover(EvolutionState state, int thread, VectorIndividual ind)
/*     */   {
/* 245 */     Random rand = new Random();
/* 246 */     if (this.genes.size() == 0) return;
/*     */     int index;
/* 248 */     int index; if (this.genes.size() == 1) {
/* 249 */       index = 0;
/*     */     } else {
/* 251 */       index = rand.nextInt(this.genes.size());
/*     */     }
/*     */     
/*     */ 
/* 255 */     if ((!((AbsractGenerator)this.genes.get(index)).isSameFamillyAs((AbsractGenerator)((TestCaseCandidate)ind).genes.get(index))) || 
/* 256 */       (rand.nextBoolean()))
/*     */     {
/* 258 */       AbsractGenerator tmpGene = (AbsractGenerator)((AbsractGenerator)this.genes.get(index)).clone();
/* 259 */       this.genes.set(index, (AbsractGenerator)((TestCaseCandidate)ind).genes.get(index));
/* 260 */       ((TestCaseCandidate)ind).genes.set(index, tmpGene);
/*     */     }
/*     */     else {
/* 263 */       ((AbsractGenerator)this.genes.get(index)).defaultCrossover((AbsractGenerator)((TestCaseCandidate)ind).genes.get(index));
/*     */     }
/*     */   }
/*     */   
/*     */   public TestingFitness getFitness()
/*     */   {
/* 269 */     return (TestingFitness)this.fitness;
/*     */   }
/*     */   
/*     */   public Set<Integer> getCoveredBranches() {
/* 273 */     return this.coveredBranches;
/*     */   }
/*     */   
/*     */   public Vector<AbsractGenerator> getGenes() {
/* 277 */     return this.genes;
/*     */   }
/*     */   
/*     */   public void execute() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
/*     */   {
/* 282 */     writeCurrentTestCandidate();
/*     */     
/* 284 */     this.coveredBranches = new HashSet();
/* 285 */     Object objUT = null;
/*     */     
/* 287 */     if (!isStatic()) {
/* 288 */       if ((this.parent != null) && (this.classUnderTest.getClazz().isAnonymousClass())) {
/* 289 */         this.parent.execute();
/* 290 */         objUT = ((MethodGenerator)this.parent.genes.get(this.parent.genes.size() - 1)).getReturnedObject();
/*     */       }
/*     */       else {
/* 293 */         InstanceGenerator con = (InstanceGenerator)this.genes.elementAt(0);
/* 294 */         objUT = con.getInstanceOnce();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 301 */       for (int i = 1; i < this.genes.size(); i++) {
/* 302 */         if (this.interrupted)
/*     */           break;
/* 304 */         if (((MethodGenerator)this.genes.elementAt(i)).isStatic()) {
/* 305 */           if ((this.genes.elementAt(i) instanceof ExternalMethodGenerator))
/*     */           {
/* 307 */             executeMethod((ExternalMethodGenerator)this.genes.elementAt(i));
/*     */           }
/*     */           else {
/* 310 */             executeMethod((MethodGenerator)this.genes.elementAt(i));
/*     */           }
/*     */         }
/* 313 */         else if ((this.genes.elementAt(i) instanceof ExternalMethodGenerator))
/*     */         {
/* 315 */           executeMethod(null, (ExternalMethodGenerator)this.genes.elementAt(i));
/* 316 */         } else if (objUT != null)
/*     */         {
/* 318 */           executeMethod(objUT, (MethodGenerator)this.genes.elementAt(i));
/*     */         }
/*     */       }
/* 321 */       if (this.interrupted) { this.isTestData = false;return;
/*     */       }
/* 323 */       if (isTestData()) {
/* 324 */         for (AbsractGenerator g : this.genes)
/* 325 */           if (((AbstractDynamicGenerator)g).getUnexpectedException() != null) {
/* 326 */             this.isGoodTestData = false;
/* 327 */             break;
/*     */           }
/* 329 */         if (this.interrupted) { this.isTestData = false;return;
/*     */         }
/*     */         
/*     */ 
/* 333 */         if (JTE.ExceptionsOriented) {
/* 334 */           this.isGoodTestData = (!this.isGoodTestData);
/*     */         }
/* 336 */         if (this.isGoodTestData) {
/* 337 */           JTE.allCoveredBranches.addAll(this.coveredBranches);
/* 338 */           JTE.testDataSet.add(this);
/*     */         } else {
/* 340 */           boolean containsAll = true;
/* 341 */           for (Iterator localIterator2 = this.coveredBranches.iterator(); localIterator2.hasNext();) { int i = ((Integer)localIterator2.next()).intValue();
/* 342 */             if (!JTE.allCoveredBranchesWithErrors.contains(Integer.valueOf(i))) {
/* 343 */               containsAll = false;
/* 344 */               break;
/*     */             } }
/* 346 */           if (!containsAll) {
/* 347 */             JTE.allCoveredBranchesWithErrors.addAll(this.coveredBranches);
/* 348 */             JTE.testDataSetWithErrors.add(this);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 356 */       e.printStackTrace();
/*     */     }
/*     */     
/* 359 */     this.isTestData = false;
/*     */   }
/*     */   
/*     */   public MethodDeclaration generateTestCaseSourceCode(ASTNode node, String methodName) {
/* 363 */     return generateTestCaseSourceCode(node, methodName, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MethodDeclaration generateTestCaseSourceCode(ASTNode node, String methodName, boolean lastOne)
/*     */   {
/* 370 */     AST ast = node.getAST();
/* 371 */     MethodDeclaration method = ast.newMethodDeclaration();
/* 372 */     method = ast.newMethodDeclaration();
/*     */     
/* 374 */     Annotation annotation = ast.newMarkerAnnotation();
/* 375 */     annotation.setTypeName(ast.newName("Test"));
/* 376 */     method.modifiers().add(annotation);
/*     */     
/* 378 */     method.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
/* 379 */     method.setName(ast.newSimpleName(methodName));
/*     */     
/* 381 */     method.setBody(ast.newBlock());
/*     */     
/*     */ 
/* 384 */     Javadoc jd = ast.newJavadoc();
/* 385 */     TextElement txtElt = ast.newTextElement();
/*     */     try {
/* 387 */       txtElt.setText(toString());
/*     */     } catch (IllegalArgumentException exec) {
/* 389 */       txtElt.setText("Error while generating chromosome text ");
/*     */     }
/*     */     
/* 392 */     TagElement tagElt = ast.newTagElement();
/* 393 */     tagElt.fragments().add(txtElt);
/*     */     
/* 395 */     jd.tags().add(tagElt);
/* 396 */     method.setJavadoc(jd);
/* 397 */     method.thrownExceptions().add(ast.newSimpleName("Throwable"));
/*     */     
/*     */ 
/* 400 */     method.getBody().statements().addAll(getStatements(ast, lastOne));
/* 401 */     return method;
/*     */   }
/*     */   
/*     */ 
/* 405 */   public List<Statement> getStatements(AST ast) { return getStatements(ast, false); }
/*     */   
/*     */   public List<Statement> getStatements(AST ast, boolean lastOne) {
/* 408 */     String objectName = "";
/* 409 */     List<Statement> statements = new ArrayList();
/* 410 */     if (!isStatic()) {
/* 411 */       if ((this.parent != null) && (this.classUnderTest.getClazz().isAnonymousClass()))
/*     */       {
/* 413 */         statements.addAll(this.parent.getStatements(ast, lastOne));
/* 414 */         objectName = ((MethodGenerator)this.parent.genes.get(this.parent.genes.size() - 1)).getReturnedObjectName();
/*     */       }
/*     */       else {
/* 417 */         InstanceGenerator con = (InstanceGenerator)this.genes.get(0);
/* 418 */         objectName = "clsUT" + con.getClazz().getSimpleName();
/* 419 */         statements.addAll(con.getStatements(ast, objectName, "P1"));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 426 */     for (int i = 1; i < this.genes.size(); i++) {
/* 427 */       AbsractGenerator gene = (AbsractGenerator)this.genes.get(i);
/* 428 */       if (((gene instanceof AbstractDynamicGenerator)) && (lastOne)) {
/* 429 */         ((AbstractDynamicGenerator)gene).SystematicallySurroundCall = true;
/*     */       }
/* 431 */       statements.addAll(gene.getStatements(ast, objectName, "P" + (i + 1)));
/*     */       
/* 433 */       if (((gene instanceof AbstractDynamicGenerator)) && (lastOne)) {
/* 434 */         ((AbstractDynamicGenerator)gene).SystematicallySurroundCall = false;
/*     */       }
/*     */     }
/* 437 */     return statements;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isStatic()
/*     */   {
/* 443 */     boolean allMethodsStatic = true;
/* 444 */     Constructor[] allDeclaredConstructors = JTE.currentClassUnderTest.getClazz().getDeclaredConstructors();
/* 445 */     Constructor[] arrayOfConstructor1; int j = (arrayOfConstructor1 = allDeclaredConstructors).length; for (int i = 0; i < j; i++) { Constructor c = arrayOfConstructor1[i];
/* 446 */       if (!Modifier.isStatic(c.getModifiers())) {
/* 447 */         allMethodsStatic = false;
/* 448 */         break;
/*     */       }
/*     */     }
/*     */     
/* 452 */     Method[] allDeclaredMethods = JTE.currentClassUnderTest.getClazz().getDeclaredMethods();
/* 453 */     Method[] arrayOfMethod1; int k = (arrayOfMethod1 = allDeclaredMethods).length; for (j = 0; j < k; j++) { Method m = arrayOfMethod1[j];
/* 454 */       if (!Modifier.isStatic(m.getModifiers()))
/*     */       {
/* 456 */         allMethodsStatic = false;
/* 457 */         break;
/*     */       }
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
/* 470 */     for (int i = 1; i < this.genes.size(); i++) {
/* 471 */       AbsractGenerator gene = (AbsractGenerator)this.genes.get(i);
/* 472 */       if (!((MethodGenerator)gene).isStatic()) {
/* 473 */         allMethodsStatic = false;
/* 474 */         break;
/*     */       }
/*     */     }
/*     */     
/* 478 */     return allMethodsStatic;
/*     */   }
/*     */   
/* 481 */   private void setBD(int bID, double branchDistance) { ((ApproachLevel)this.fitness).setBD(bID, branchDistance); }
/*     */   
/*     */ 
/*     */   public Parameter defaultBase()
/*     */   {
/* 486 */     return null;
/*     */   }
/*     */   
/*     */   public void reset(EvolutionState state, int thread)
/*     */   {
/* 491 */     generateRandom();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 497 */     boolean areEqual = this.fitness.equals(((TestCaseCandidate)other).getFitness());
/*     */     
/* 499 */     int i = 0;
/* 500 */     while ((areEqual) && (i < this.genes.size())) {
/* 501 */       areEqual = (areEqual) && (((AbsractGenerator)this.genes.get(i)).equals(((TestCaseCandidate)other).getGenes().get(i)));
/*     */     }
/*     */     
/* 504 */     return areEqual;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 510 */     int hash = getClass().hashCode();
/*     */     
/* 512 */     hash = hash << 1 | hash >>> 31;
/* 513 */     for (int x = 0; x < this.genes.size(); x++) {
/* 514 */       if (this.genes.get(x) != null)
/* 515 */         hash = (hash << 1 | hash >>> 31) ^ ((AbsractGenerator)this.genes.get(x)).hashCode();
/*     */     }
/* 517 */     return hash;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 522 */     String str = new String();
/* 523 */     str = "Chromosome :\r";
/* 524 */     int order = 1;
/* 525 */     if (((AbsractGenerator)this.genes.get(0)).getObject() != null) {
/* 526 */       str = str + "1)----->" + ((AbsractGenerator)this.genes.get(0)).toString() + "\r";
/* 527 */       order = 2;
/*     */     }
/*     */     
/* 530 */     for (int i = 1; i < this.genes.size(); i++)
/*     */     {
/*     */ 
/*     */ 
/* 534 */       str = str + order + ")----->" + ((AbsractGenerator)this.genes.get(i)).toString();
/* 535 */       order++;
/* 536 */       if (i < this.genes.size() - 1)
/* 537 */         str = str + ", \r";
/*     */     }
/* 539 */     str = str + "\r Covered Branches:" + this.coveredBranches;
/* 540 */     return str;
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 545 */     TestCaseCandidate newChrom = new TestCaseCandidate(this.classUnderTest);
/* 546 */     newChrom.random = this.random;
/* 547 */     newChrom.coveredBranches = new HashSet();
/* 548 */     newChrom.coveredBranches.addAll(this.coveredBranches);
/* 549 */     newChrom.isTestData = this.isTestData;
/* 550 */     newChrom.AnonymousType = this.AnonymousType;
/* 551 */     for (AbsractGenerator gene : this.genes) {
/* 552 */       newChrom.getGenes().add((AbsractGenerator)gene.clone());
/*     */     }
/* 554 */     return newChrom;
/*     */   }
/*     */   
/*     */   public boolean isTestData() {
/* 558 */     return this.isTestData;
/*     */   }
/*     */   
/*     */   public void setIsTestData(boolean isTestData) {
/* 562 */     this.isTestData = isTestData;
/*     */   }
/*     */   
/*     */   public void setInterrupted()
/*     */   {
/* 567 */     this.interrupted = true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void writeCurrentTestCandidate()
/*     */   {
/* 573 */     ASTParser parser = ASTParser.newParser(3);
/* 574 */     parser.setSource("".toCharArray());
/* 575 */     CompilationUnit unit = (CompilationUnit)parser.createAST(null);
/* 576 */     unit.recordModifications();
/* 577 */     AST ast = unit.getAST();
/*     */     
/*     */ 
/* 580 */     if (!JTE.packageName.equals("")) {
/* 581 */       PackageDeclaration pkgDec = ast.newPackageDeclaration();
/* 582 */       pkgDec.setName(ASTEditor.generateQualifiedName(JTE.packageName, ast));
/* 583 */       unit.setPackage(pkgDec);
/*     */     }
/*     */     
/* 586 */     ImportDeclaration importDeclaration2 = ast.newImportDeclaration();
/* 587 */     QualifiedName name2 = ASTEditor.generateQualifiedName("org.junit.Test", ast);
/* 588 */     importDeclaration2.setName(name2);
/* 589 */     unit.imports().add(importDeclaration2);
/*     */     
/* 591 */     ImportDeclaration importDeclaration3 = ast.newImportDeclaration();
/* 592 */     QualifiedName name3 = ASTEditor.generateQualifiedName("org.junit.Rule", ast);
/* 593 */     importDeclaration3.setName(name3);
/* 594 */     unit.imports().add(importDeclaration3);
/*     */     
/* 596 */     if (JTE.ExceptionsOriented) {
/* 597 */       ImportDeclaration importDeclaration4 = ast.newImportDeclaration();
/* 598 */       QualifiedName name4 = ASTEditor.generateQualifiedName("csbst.utils.ExceptionsFormatter", ast);
/* 599 */       importDeclaration4.setName(name4);
/* 600 */       unit.imports().add(importDeclaration4);
/*     */     }
/*     */     
/*     */ 
/* 604 */     TypeDeclaration clazzNode = ast.newTypeDeclaration();
/* 605 */     clazzNode.setInterface(false);
/* 606 */     clazzNode.setName(ast.newSimpleName(JTE.srcFileName + "LastTestCandidate"));
/* 607 */     clazzNode.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
/*     */     
/* 609 */     unit.types().add(clazzNode);
/*     */     
/*     */ 
/* 612 */     clazzNode.bodyDeclarations().add(generateTestCaseSourceCode(clazzNode, "TestCase", true));
/*     */     
/*     */ 
/*     */ 
/* 616 */     Set<Class> rc = new HashSet();
/* 617 */     rc.addAll(JTE.requiredClasses);
/*     */     
/* 619 */     for (Class cls : rc)
/*     */     {
/* 621 */       if ((cls.getCanonicalName() != null) && 
/* 622 */         (!cls.isPrimitive()) && (
/* 623 */         (cls.getPackage() == null) || (!cls.getPackage().getName().toString().equals("java.lang"))))
/*     */       {
/*     */ 
/*     */ 
/* 627 */         ImportDeclaration impDec = ast.newImportDeclaration();
/* 628 */         String binaryName = cls.getName();
/* 629 */         if (cls.isMemberClass())
/* 630 */           binaryName = cls.getDeclaringClass().getName();
/* 631 */         if (cls.isLocalClass()) {
/* 632 */           binaryName = cls.getEnclosingClass().getName();
/*     */         }
/* 634 */         binaryName = binaryName.replace("$", ".");
/*     */         Name impName;
/*     */         Name impName;
/* 637 */         if (binaryName.lastIndexOf(".") < 0) {
/* 638 */           impName = ast.newSimpleName(binaryName);
/*     */         }
/*     */         else
/*     */         {
/* 642 */           impName = ASTEditor.generateQualifiedName(binaryName, ast);
/*     */         }
/*     */         
/* 645 */         impDec.setName(impName);
/*     */         
/* 647 */         boolean exist = false;
/* 648 */         for (Object imp1 : unit.imports()) {
/* 649 */           if (((ImportDeclaration)imp1).getName().toString().equals(impName)) {
/* 650 */             exist = true;
/* 651 */             break;
/*     */           }
/*     */         }
/* 654 */         if (!exist)
/*     */         {
/* 656 */           unit.imports().add(impDec);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 661 */     String fileName = JTE.testCasesPath + File.separator + JTE.subPath + File.separator + JTE.srcFileName + "LastTestCandidate" + ".java";
/* 662 */     ASTEditor.unit2File(unit, fileName);
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\ga\ecj\TestCaseCandidate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */