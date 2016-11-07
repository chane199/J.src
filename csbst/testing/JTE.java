/*      */ package csbst.testing;
/*      */ 
/*      */ import csbst.analysis.ASTBuilder;
/*      */ import csbst.analysis.BranchesCoder;
/*      */ import csbst.analysis.DataMemberUseAnalyser;
/*      */ import csbst.analysis.InfluenceAnalyser;
/*      */ import csbst.analysis.Instrumentor;
/*      */ import csbst.analysis.LittralConstantAnalyser;
/*      */ import csbst.analysis.MethodCallsAnalyser;
/*      */ import csbst.ga.ecj.TestCaseCandidate;
/*      */ import csbst.generators.dynamic.InstanceGenerator;
/*      */ import csbst.utils.ASTEditor;
/*      */ import csbst.utils.ClassLoaderUtil;
/*      */ import csbst.utils.LoggingOutputStream;
/*      */ import csbst.utils.StdOutErrLevel;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.security.Permission;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.FileHandler;
/*      */ import java.util.logging.Handler;
/*      */ import java.util.logging.LogManager;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.logging.SimpleFormatter;
/*      */ import javax.tools.Diagnostic;
/*      */ import javax.tools.DiagnosticCollector;
/*      */ import javax.tools.DiagnosticListener;
/*      */ import javax.tools.JavaCompiler;
/*      */ import javax.tools.JavaCompiler.CompilationTask;
/*      */ import javax.tools.StandardJavaFileManager;
/*      */ import javax.tools.StandardLocation;
/*      */ import javax.tools.ToolProvider;
/*      */ import org.eclipse.jdt.core.dom.AST;
/*      */ import org.eclipse.jdt.core.dom.ASTParser;
/*      */ import org.eclipse.jdt.core.dom.Annotation;
/*      */ import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
/*      */ import org.eclipse.jdt.core.dom.Block;
/*      */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*      */ import org.eclipse.jdt.core.dom.EnumDeclaration;
/*      */ import org.eclipse.jdt.core.dom.FieldDeclaration;
/*      */ import org.eclipse.jdt.core.dom.IMethodBinding;
/*      */ import org.eclipse.jdt.core.dom.ITypeBinding;
/*      */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*      */ import org.eclipse.jdt.core.dom.ImportDeclaration;
/*      */ import org.eclipse.jdt.core.dom.Javadoc;
/*      */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*      */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*      */ import org.eclipse.jdt.core.dom.Modifier;
/*      */ import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
/*      */ import org.eclipse.jdt.core.dom.Name;
/*      */ import org.eclipse.jdt.core.dom.PackageDeclaration;
/*      */ import org.eclipse.jdt.core.dom.QualifiedName;
/*      */ import org.eclipse.jdt.core.dom.SimpleName;
/*      */ import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
/*      */ import org.eclipse.jdt.core.dom.TagElement;
/*      */ import org.eclipse.jdt.core.dom.TextElement;
/*      */ import org.eclipse.jdt.core.dom.Type;
/*      */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*      */ import org.eclipse.jdt.core.dom.VariableDeclaration;
/*      */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*      */ import org.reflections.Reflections;
/*      */ import org.reflections.scanners.Scanner;
/*      */ import org.reflections.scanners.SubTypesScanner;
/*      */ import org.reflections.util.ConfigurationBuilder;
/*      */ import org.reflections.util.FilterBuilder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 

/*      */ 
/*      */ public class JTE
/*      */ {
/*      */   private static CompilationUnit compilationUnit;
/*      */   public static String className;
/*  105 */   public static String packageName = "";
/*      */   public static String projectPackagesPrefix;
/*      */   public static String subPath;
/*      */   public static String testCasesPath;
/*      */   public static String srcPath;
/*      */   public static String srcPath0;
/*      */   public static String binPath;
/*      */   public static String[] classPath;
/*  113 */   private static String classPath0 = "";
/*      */   
/*      */   public static String srcFileName;
/*      */   private static String gaParametersFile;
/*      */   private static String interfacesFile;
/*      */   private static String jteOutputPath;
/*      */   private static String javaFileName;
/*      */   private static String javaDirectoryName;
/*  121 */   public static int seed = 0;
/*  122 */   private static boolean printProgress = false;
/*  123 */   private static boolean showErrors = false;
/*  124 */   public static boolean ExceptionsOriented = false;
/*  125 */   private static boolean overrideExistTestCase = false;
/*  126 */   private static boolean instrument = true;
/*      */   
/*  128 */   public static boolean writeTestCasesIsDone = false;
/*  129 */   private static boolean cutCallsSystemExist = false;
/*      */   
/*      */   private static BranchesCoder branchCoderAnalyser;
/*      */   
/*      */   public static DataMemberUseAnalyser dataMemberUseAnalyser;
/*      */   private static MethodCallsAnalyser methodCallsAnalyser;
/*      */   private static InfluenceAnalyser influenceAnalyser;
/*      */   public static LittralConstantAnalyser litteralConstantAnalyser;
/*  137 */   public static Set<Integer> branchesTarget = new TreeSet();
/*  138 */   public static Set<Integer> allCoveredBranches = new HashSet();
/*  139 */   public static Set<Integer> allCoveredBranchesWithErrors = new HashSet();
/*  140 */   public static Set<Integer> allNotCoveredBranches = new HashSet();
/*  141 */   private static int lastSaveNumberOfNCB = 0;
/*  142 */   private static int maxEvaluations = 0;
/*  143 */   private static int maxTime = 0;
/*      */   private static int totalEvaluations;
/*  145 */   private static int geneartionNumber = 0;
/*  146 */   private static int nmberOfStuckThreads = 0;
/*  147 */   private static int remaindEvaluations = 0;
/*      */   
/*      */   private static Set<Integer> tmpUncoveredTarget;
/*      */   
/*      */   private static long totalTime;
/*      */   
/*      */   private static long totalSearchTime;
/*      */   
/*      */   private static long ActualstartTime;
/*      */   
/*      */   public static ClassUnderTest currentClassUnderTest;
/*      */   public static Class mainClassUnderTest;
/*      */   public static Target currentTarget;
/*      */   private static Path currentPathTarget;
/*      */   public static ClassLoader magicClassLoader;
/*  162 */   private static Map<String, ClassUnderTest> classesUnderTest = new HashMap();
/*  163 */   public static Set<TestCaseCandidate> testDataSet = new HashSet();
/*  164 */   public static Set<TestCaseCandidate> testDataSetWithErrors = new HashSet();
/*  165 */   private static Map<MethodDeclaration, Method> astMethod2ReflexionMethod = new HashMap();
/*  166 */   private static Map<MethodDeclaration, Constructor> astMethod2ReflexionConstructor = new HashMap();
/*  167 */   public static Set<Class> requiredClasses = new HashSet();
/*      */   
/*      */   public static final String TEST_CASES_SURFIX = "JTETestCases";
/*      */   
/*      */   public static PrintStream stdout;
/*      */   
/*      */   public static PrintStream stderr;
/*      */   
/*  175 */   public static List<URL> classPathList = new ArrayList();
/*      */   
/*  177 */   public static String heuristicName = "RA";
/*  178 */   public static int minEvaluationPerBranch = 500;
/*  179 */   public static int maxRandomEvaluationsFitness = 5;
/*  180 */   public static int maxThreadNumber = 49;
/*      */   public static long startTimeNano;
/*      */   public static long startTime;
/*      */   
/*      */   private static boolean Initialize() throws Exception
/*      */   {
/*  186 */     System.out.println();
/*  187 */     System.out.println("Unit Under Test: " + className);
/*      */     
/*      */ 
/*  190 */     loadClassPath();
/*      */     
/*  192 */     createInstrumentedCUD();
/*      */     
/*      */ 
/*      */ 
/*  196 */     if (!compileICUD()) {
/*  197 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  212 */     AnalyseCU();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  217 */     InstantiateICUD(className);
/*      */     
/*      */ 
/*  220 */     if ((interfacesFile != null) && (!interfacesFile.equals(""))) {
/*  221 */       BufferedReader buff = new BufferedReader(new FileReader(interfacesFile));
/*  222 */       String tmp_line = buff.readLine();
/*  223 */       while (tmp_line != null) {
/*  224 */         String[] inter2class = tmp_line.split(";");
/*  225 */         Class clss1 = magicClassLoader.loadClass(inter2class[0]);
/*  226 */         Vector<Class> lst1 = (Vector)InstanceGenerator.interface2Implementation.get(clss1);
/*  227 */         if (lst1 != null) {
/*  228 */           lst1.add(magicClassLoader.loadClass(inter2class[1]));
/*      */         } else {
/*  230 */           lst1 = new Vector();
/*  231 */           lst1.add(magicClassLoader.loadClass(inter2class[1]));
/*  232 */           InstanceGenerator.interface2Implementation.put(clss1, lst1);
/*      */         }
/*  234 */         tmp_line = buff.readLine();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  241 */     astMethod2ReflexionMethod = new HashMap();
/*  242 */     astMethod2ReflexionConstructor = new HashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  249 */     return true;
/*      */   }
/*      */   
/*      */   private static void setAllBranchesTarget()
/*      */   {
/*  254 */     branchesTarget.clear();
/*  255 */     for (int i = 1; i <= branchCoderAnalyser.getLastBranch(); i++) {
/*  256 */       branchesTarget.add(Integer.valueOf(i));
/*      */     }
/*      */   }
/*      */   
/*      */   private static void createInstrumentedCUD() throws Exception {
/*  261 */     litteralConstantAnalyser = new LittralConstantAnalyser();
/*  262 */     compilationUnit.accept(litteralConstantAnalyser);
/*      */     
/*      */ 
/*  265 */     branchCoderAnalyser = new BranchesCoder();
/*  266 */     compilationUnit.accept(branchCoderAnalyser);
/*      */     
/*  268 */     Instrumentor instrumentor = new Instrumentor();
/*  269 */     compilationUnit.accept(instrumentor);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  275 */     ASTEditor.unit2File(compilationUnit, srcPath + File.separator + subPath + File.separator + srcFileName + ".java");
/*      */   }
/*      */   
/*      */   private static boolean compileICUD() throws IOException {
/*  279 */     File dotclass = new File(binPath + File.separator + subPath + File.separator + srcFileName + ".class");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  284 */     File instrumentedClass = new File(binPath + File.separator + subPath);
/*  285 */     instrumentedClass.getParentFile().mkdirs();
/*      */     
/*      */ 
/*  288 */     JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
/*  289 */     StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, 
/*  290 */       Locale.ENGLISH, 
/*  291 */       null);
/*      */     
/*  293 */     List<File> pathList = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  299 */     URLClassLoader urlClassLoader = (URLClassLoader)Thread.currentThread().getContextClassLoader();
/*  300 */     URL[] arrayOfURL; int j = (arrayOfURL = urlClassLoader.getURLs()).length; for (int i = 0; i < j; i++) { URL url = arrayOfURL[i];
/*  301 */       pathList.add(new File(url.getFile()));
/*      */     }
/*      */     try
/*      */     {
/*  305 */       fileManager.setLocation(StandardLocation.CLASS_PATH, pathList);
/*  306 */       fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(new File(binPath)));
/*      */     } catch (IOException e) {
/*  308 */       e.printStackTrace();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  315 */     File[] files = { new File(srcPath + File.separator + subPath + File.separator + srcFileName + ".java") };
/*      */     
/*  317 */     Iterable fileObjects = fileManager.getJavaFileObjects(files);
/*  318 */     Object diagnostics = new DiagnosticCollector();
/*  319 */     JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, (DiagnosticListener)diagnostics, null, null, fileObjects);
/*      */     
/*      */ 
/*  322 */     if (!task.call().booleanValue()) {
/*  323 */       System.err.println("Compilation' errors in the instrumented file: ");
/*      */       
/*  325 */       for (Diagnostic diagnostic : ((DiagnosticCollector)diagnostics).getDiagnostics()) {
/*  326 */         System.err.println(diagnostic.getMessage(null));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  331 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  341 */     return true;
/*      */   }
/*      */   
/*      */   private static Vector<File> getJavaFiles(String path)
/*      */   {
/*  346 */     Vector<File> allFiles = new Vector();
/*  347 */     File directory = new File(path);
/*  348 */     File[] files = directory.listFiles();
/*  349 */     File[] arrayOfFile1; int j = (arrayOfFile1 = files).length; for (int i = 0; i < j; i++) { File f = arrayOfFile1[i];
/*  350 */       if ((f.isFile()) && (f.getName().toString().endsWith(".java")))
/*  351 */         allFiles.add(f);
/*  352 */       if (f.isDirectory())
/*  353 */         allFiles.addAll(getJavaFiles(f.toString()));
/*      */     }
/*  355 */     return allFiles;
/*      */   }
/*      */   
/*      */   private static void InstantiateICUD(String className) throws ClassNotFoundException, IOException {
/*  359 */     Class cls = magicClassLoader.loadClass(className);
/*  360 */     mainClassUnderTest = cls;
/*  361 */     setClassUnderTest(cls);
/*      */   }
/*      */   
/*      */   private static Class findMemberClass(String canonicalClassName, Class cls) {
/*  365 */     if ((canonicalClassName == null) || (cls == null))
/*  366 */       return null;
/*  367 */     if (cls.getCanonicalName() == null) {
/*  368 */       return null;
/*      */     }
/*  370 */     if (canonicalClassName.equals(cls.getCanonicalName()))
/*  371 */       return cls;
/*  372 */     Class[] arrayOfClass; int j = (arrayOfClass = cls.getDeclaredClasses()).length; for (int i = 0; i < j; i++) { Class c = arrayOfClass[i];
/*  373 */       if ((c.getCanonicalName() != null) && (canonicalClassName.startsWith(c.getCanonicalName())) && (
/*  374 */         (canonicalClassName.length() == c.getCanonicalName().length()) || ((canonicalClassName.length() > c.getCanonicalName().length()) && (canonicalClassName.charAt(c.getCanonicalName().length() + 1) == '.'))))
/*  375 */         return findMemberClass(canonicalClassName, c);
/*      */     }
/*  377 */     return null;
/*      */   }
/*      */   
/*      */   private static Class findDeclaringClass(String canonicalClassName, Class cls)
/*      */   {
/*  382 */     Class c = cls;
/*  383 */     while ((c != null) && (!canonicalClassName.equals(c.getCanonicalName()))) {
/*  384 */       c = c.getDeclaringClass();
/*      */     }
/*  386 */     return c;
/*      */   }
/*      */   
/*      */   private static Class findFriendClass(String declaringClassName, String canonicalClassName, Class cls)
/*      */   {
/*  391 */     Class c = cls;
/*  392 */     if (c == null) {
/*  393 */       return null;
/*      */     }
/*  395 */     while ((c != null) && (!declaringClassName.equals(c.getName()))) {
/*  396 */       c = c.getDeclaringClass();
/*      */     }
/*      */     
/*  399 */     return findMemberClass(canonicalClassName, c);
/*      */   }
/*      */   
/*      */   private static void AnalyseCU() throws ClassNotFoundException
/*      */   {
/*  404 */     methodCallsAnalyser = new MethodCallsAnalyser();
/*  405 */     compilationUnit.accept(methodCallsAnalyser);
/*      */     
/*      */ 
/*  408 */     dataMemberUseAnalyser = new DataMemberUseAnalyser();
/*  409 */     compilationUnit.accept(dataMemberUseAnalyser);
/*      */     
/*  411 */     influenceAnalyser = new InfluenceAnalyser();
/*  412 */     compilationUnit.accept(influenceAnalyser);
/*      */   }
/*      */   
/*      */   public static Vector<Path> getAccessiblePaths(int branch) throws IOException {
/*  416 */     return getAccessiblePaths(branch, null);
/*      */   }
/*      */   
/*      */   public static Vector<Path> getAccessiblePaths(int initBranch, Path oldPath) throws IOException {
/*  420 */     int cBranch = initBranch;
/*  421 */     Vector<Path> allPaths = new Vector();
/*  422 */     Path cPath = new Path();
/*  423 */     if (oldPath != null)
/*  424 */       cPath.addAll(oldPath);
/*  425 */     double DC = 1.0D;
/*  426 */     if ((Double)((Block)branchCoderAnalyser.getBranch2BlockMap().get(Integer.valueOf(cBranch))).getProperty("difficultyCoefficient") != null)
/*  427 */       DC = ((Double)((Block)branchCoderAnalyser.getBranch2BlockMap().get(Integer.valueOf(cBranch))).getProperty("difficultyCoefficient")).doubleValue();
/*  428 */     cPath.add(new Branch(cBranch, DC, cPath.size() + 1));
/*      */     
/*      */ 
/*  431 */     while (!(((Block)branchCoderAnalyser.getBranch2BlockMap().get(Integer.valueOf(cBranch))).getParent() instanceof MethodDeclaration))
/*      */     {
/*  433 */       cBranch = ((Integer)((Block)branchCoderAnalyser.getBranch2BlockMap().get(Integer.valueOf(cBranch))).getProperty("numberParentBranch")).intValue();
/*  434 */       DC = 1.0D;
/*  435 */       if ((Double)((Block)branchCoderAnalyser.getBranch2BlockMap().get(Integer.valueOf(cBranch))).getProperty("difficultyCoefficient") != null)
/*  436 */         DC = ((Double)((Block)branchCoderAnalyser.getBranch2BlockMap().get(Integer.valueOf(cBranch))).getProperty("difficultyCoefficient")).doubleValue();
/*  437 */       cPath.add(new Branch(cBranch, DC, cPath.size() + 1));
/*      */     }
/*      */     
/*  440 */     MethodDeclaration currentMethod = (MethodDeclaration)((Block)branchCoderAnalyser.getBranch2BlockMap().get(Integer.valueOf(cBranch))).getParent();
/*  441 */     if (currentMethod.getName().toString().equals("withOffsetParsed")) {
/*  442 */       methodCallsAnalyser = methodCallsAnalyser;
/*      */     }
/*      */     
/*  445 */     boolean isAcssiblePath = true;
/*      */     
/*  447 */     IMethodBinding imb = currentMethod.resolveBinding();
/*  448 */     int i; if (imb != null) {
/*  449 */       isAcssiblePath = isAccessible(imb);
/*      */     }
/*      */     else {
/*  452 */       for (int i = 0; i < currentMethod.modifiers().size(); i++) {
/*  453 */         if (currentMethod.modifiers().get(i).toString().equalsIgnoreCase("public")) {
/*      */           break;
/*      */         }
/*  456 */         if ((currentMethod.modifiers().get(i).toString().equalsIgnoreCase("private")) || 
/*  457 */           (currentMethod.modifiers().get(i).toString().equalsIgnoreCase("protected")))
/*      */         {
/*  459 */           isAcssiblePath = false;
/*  460 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  469 */       TypeDeclaration declaringClazz = (TypeDeclaration)currentMethod.getParent();
/*  470 */       for (i = 0; i < declaringClazz.modifiers().size(); i++) {
/*  471 */         if (declaringClazz.modifiers().get(i).toString().equalsIgnoreCase("public")) {
/*      */           break;
/*      */         }
/*  474 */         if ((declaringClazz.modifiers().get(i).toString().equalsIgnoreCase("private")) || 
/*  475 */           (declaringClazz.modifiers().get(i).toString().equalsIgnoreCase("protected")))
/*      */         {
/*  477 */           isAcssiblePath = false;
/*  478 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  484 */     if (isAcssiblePath) {
/*      */       try {
/*  486 */         if (!currentMethod.isConstructor()) {
/*  487 */           cPath.setEntryPoint(methodDec2MethodRef(currentMethod));
/*      */         }
/*      */         else {
/*  490 */           cPath.setEntryPoint(methodDec2ConstructorRef(currentMethod));
/*      */         }
/*      */       } catch (ClassNotFoundException e) {
/*  493 */         e.printStackTrace();
/*      */       }
/*  495 */       allPaths.add(cPath);
/*      */ 
/*      */ 
/*      */     }
/*  499 */     else if (methodCallsAnalyser.getMethodBranchCallersMap().get(currentMethod.resolveBinding()) != null) {
/*  500 */       for (i = ((Set)methodCallsAnalyser.getMethodBranchCallersMap().get(currentMethod.resolveBinding())).iterator(); i.hasNext();) { int br = ((Integer)i.next()).intValue();
/*  501 */         if (!cPath.contains(br)) {
/*  502 */           allPaths.addAll(getAccessiblePaths(br, cPath));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  511 */     return allPaths;
/*      */   }
/*      */   
/*      */   private static boolean isAccessible(IMethodBinding method) {
/*  515 */     if (method == null) return false;
/*  516 */     boolean accessibility = true;
/*  517 */     if ((Modifier.isPrivate(method.getModifiers())) || 
/*  518 */       (Modifier.isProtected(method.getModifiers())))
/*  519 */       accessibility = false;
/*      */     ITypeBinding[] arrayOfITypeBinding;
/*  521 */     int j = (arrayOfITypeBinding = method.getParameterTypes()).length; for (int i = 0; i < j; i++) { ITypeBinding p = arrayOfITypeBinding[i];
/*      */       
/*  523 */       if ((p.isAnonymous()) || (!isAccessible(p)))
/*      */       {
/*  525 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  529 */     if (accessibility) {
/*  530 */       accessibility = isAccessible(method.getDeclaringClass());
/*      */     }
/*      */     
/*  533 */     return accessibility;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static boolean isAccessible(ITypeBinding clazz)
/*      */   {
/*  540 */     if ((clazz == null) || (clazz.isAnonymous())) {
/*  541 */       return false;
/*      */     }
/*  543 */     boolean accessibility = true;
/*  544 */     if ((Modifier.isPrivate(clazz.getModifiers())) || 
/*  545 */       (Modifier.isProtected(clazz.getModifiers())))
/*  546 */       accessibility = false;
/*  547 */     if ((accessibility) && (clazz.isAnonymous())) {
/*  548 */       accessibility = isAccessible(clazz.getDeclaringMethod());
/*      */     }
/*      */     
/*  551 */     if ((accessibility) && (!clazz.isAnonymous()) && (clazz.getDeclaringClass() != null)) {
/*  552 */       accessibility = isAccessible(clazz.getDeclaringClass());
/*      */     }
/*      */     
/*  555 */     return accessibility;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class NoExitSecurityManager
/*      */     extends SecurityManager
/*      */   {
/*      */     public void checkExec(String cmd) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void checkPermission(Permission perm) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void checkPermission(Permission perm, Object context) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void checkWrite(String file)
/*      */     {
/*  584 */       if ((!file.startsWith(JTE.jteOutputPath)) && (!file.contains(File.separator + "tmp" + File.separator))) {
/*  585 */         JTE.cutCallsSystemExist = true;
/*  586 */         throw new SecurityException();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void checkDelete(String file)
/*      */     {
/*  594 */       if (!file.startsWith(JTE.jteOutputPath)) {
/*  595 */         throw new SecurityException("Delete a File is Not allowed." + file);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void checkExit(int status)
/*      */     {
/*  603 */       if (status != 5555) {
/*  604 */         throw new SecurityException("Exit is Not allowed.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void forbidSystemExitCall()
/*      */   {
/*  617 */     SecurityManager securityManager = new NoExitSecurityManager(null);
/*  618 */     System.setSecurityManager(securityManager);
/*      */   }
/*      */   
/*      */   private static void enableSystemExitCall() {
/*  622 */     System.setSecurityManager(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getMaxEvaluations()
/*      */   {
/*  705 */     int EstimatedTotalEvaluations = 0;
/*  706 */     if (maxEvaluations == 0) {
/*  707 */       if ((maxTime == 0) || (totalEvaluations == 0)) {
/*  708 */         EstimatedTotalEvaluations = branchCoderAnalyser.getLastBranch() * minEvaluationPerBranch;
/*      */       } else {
/*  710 */         long currentTime = System.nanoTime();
/*  711 */         long allowedTime = maxTime;
/*  712 */         long avgTimePerEvaluation = (currentTime - startTimeNano) / totalEvaluations;
/*      */         long evalTime;
/*  714 */         long evalTime; if (avgTimePerEvaluation > 0L) {
/*  715 */           evalTime = allowedTime * 1000000000L / avgTimePerEvaluation;
/*      */         } else
/*  717 */           evalTime = branchCoderAnalyser.getLastBranch() * minEvaluationPerBranch;
/*  718 */         EstimatedTotalEvaluations = (int)evalTime;
/*      */       }
/*      */     }
/*  721 */     else if ((maxTime == 0) || (totalEvaluations == 0)) {
/*  722 */       EstimatedTotalEvaluations = maxEvaluations;
/*      */     } else {
/*  724 */       long currentTime = System.nanoTime();
/*  725 */       long allowedTime = maxTime;
/*  726 */       long avgTimePerEvaluation = (currentTime - startTimeNano) / totalEvaluations;
/*      */       long evalTime;
/*      */       long evalTime;
/*  729 */       if (avgTimePerEvaluation > 0L) {
/*  730 */         evalTime = allowedTime * 1000000000L / avgTimePerEvaluation;
/*      */       } else {
/*  732 */         evalTime = branchCoderAnalyser.getLastBranch() * minEvaluationPerBranch;
/*      */       }
/*  734 */       if (evalTime < maxEvaluations) {
/*  735 */         EstimatedTotalEvaluations = (int)evalTime;
/*      */       } else {
/*  737 */         EstimatedTotalEvaluations = maxEvaluations;
/*      */       }
/*      */     }
/*      */     
/*  741 */     return EstimatedTotalEvaluations;
/*      */   }
/*      */   
/*  744 */   private static int getRemaindEvaluations() { return getMaxEvaluations() - totalEvaluations; }
/*      */   
/*      */   /* Error */
/*      */   private static void generateTestData()
/*      */     throws Exception
/*      */   {
/*      */     // Byte code:
/*      */     //   0: getstatic 826	csbst/testing/JTE:stdout	Ljava/io/PrintStream;
/*      */     //   3: ldc_w 828
/*      */     //   6: invokevirtual 222	java/io/PrintStream:println	(Ljava/lang/String;)V
/*      */     //   9: invokestatic 830	csbst/testing/JTE:setAllBranchesTarget	()V
/*      */     //   12: getstatic 137	csbst/testing/JTE:allCoveredBranches	Ljava/util/Set;
/*      */     //   15: invokeinterface 307 1 0
/*      */     //   20: iconst_0
/*      */     //   21: putstatic 807	csbst/testing/JTE:totalEvaluations	I
/*      */     //   24: iconst_0
/*      */     //   25: putstatic 149	csbst/testing/JTE:geneartionNumber	I
/*      */     //   28: iconst_0
/*      */     //   29: putstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   32: iconst_0
/*      */     //   33: putstatic 153	csbst/testing/JTE:remaindEvaluations	I
/*      */     //   36: iconst_0
/*      */     //   37: putstatic 123	csbst/testing/JTE:writeTestCasesIsDone	Z
/*      */     //   40: iconst_0
/*      */     //   41: putstatic 143	csbst/testing/JTE:lastSaveNumberOfNCB	I
/*      */     //   44: invokestatic 832	java/lang/System:currentTimeMillis	()J
/*      */     //   47: putstatic 835	csbst/testing/JTE:startTime	J
/*      */     //   50: invokestatic 809	java/lang/System:nanoTime	()J
/*      */     //   53: putstatic 813	csbst/testing/JTE:startTimeNano	J
/*      */     //   56: new 837	csbst/testing/KillMeAfterTimeOut
/*      */     //   59: dup
/*      */     //   60: getstatic 147	csbst/testing/JTE:maxTime	I
/*      */     //   63: i2l
/*      */     //   64: invokespecial 839	csbst/testing/KillMeAfterTimeOut:<init>	(J)V
/*      */     //   67: astore_0
/*      */     //   68: new 842	csbst/testing/TestDataSaver
/*      */     //   71: dup
/*      */     //   72: getstatic 147	csbst/testing/JTE:maxTime	I
/*      */     //   75: i2l
/*      */     //   76: invokespecial 844	csbst/testing/TestDataSaver:<init>	(J)V
/*      */     //   79: astore_1
/*      */     //   80: getstatic 149	csbst/testing/JTE:geneartionNumber	I
/*      */     //   83: iconst_1
/*      */     //   84: iadd
/*      */     //   85: putstatic 149	csbst/testing/JTE:geneartionNumber	I
/*      */     //   88: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   91: getstatic 137	csbst/testing/JTE:allCoveredBranches	Ljava/util/Set;
/*      */     //   94: invokeinterface 845 2 0
/*      */     //   99: pop
/*      */     //   100: getstatic 807	csbst/testing/JTE:totalEvaluations	I
/*      */     //   103: invokestatic 848	csbst/testing/JTE:printProgress	(I)V
/*      */     //   106: invokestatic 851	csbst/testing/JTE:getRemaindEvaluations	()I
/*      */     //   109: putstatic 153	csbst/testing/JTE:remaindEvaluations	I
/*      */     //   112: new 134	java/util/HashSet
/*      */     //   115: dup
/*      */     //   116: invokespecial 136	java/util/HashSet:<init>	()V
/*      */     //   119: putstatic 853	csbst/testing/JTE:tmpUncoveredTarget	Ljava/util/Set;
/*      */     //   122: getstatic 853	csbst/testing/JTE:tmpUncoveredTarget	Ljava/util/Set;
/*      */     //   125: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   128: invokeinterface 855 2 0
/*      */     //   133: pop
/*      */     //   134: invokestatic 856	csbst/testing/JTE:iteration	()V
/*      */     //   137: goto +116 -> 253
/*      */     //   140: astore_2
/*      */     //   141: aload_2
/*      */     //   142: invokevirtual 859	java/lang/Exception:printStackTrace	()V
/*      */     //   145: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   148: ifle +45 -> 193
/*      */     //   151: getstatic 471	java/lang/System:err	Ljava/io/PrintStream;
/*      */     //   154: new 205	java/lang/StringBuilder
/*      */     //   157: dup
/*      */     //   158: ldc_w 860
/*      */     //   161: invokespecial 209	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   164: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   167: iconst_1
/*      */     //   168: iadd
/*      */     //   169: invokevirtual 862	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   172: ldc_w 865
/*      */     //   175: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   178: getstatic 867	csbst/testing/JTE:currentClassUnderTest	Lcsbst/testing/ClassUnderTest;
/*      */     //   181: invokevirtual 869	csbst/testing/ClassUnderTest:getClazz	()Ljava/lang/Class;
/*      */     //   184: invokevirtual 874	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   187: invokevirtual 218	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   190: invokevirtual 222	java/io/PrintStream:println	(Ljava/lang/String;)V
/*      */     //   193: invokestatic 877	csbst/testing/JTE:writeTestCases	()V
/*      */     //   196: goto +108 -> 304
/*      */     //   199: astore_3
/*      */     //   200: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   203: ifle +45 -> 248
/*      */     //   206: getstatic 471	java/lang/System:err	Ljava/io/PrintStream;
/*      */     //   209: new 205	java/lang/StringBuilder
/*      */     //   212: dup
/*      */     //   213: ldc_w 860
/*      */     //   216: invokespecial 209	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   219: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   222: iconst_1
/*      */     //   223: iadd
/*      */     //   224: invokevirtual 862	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   227: ldc_w 865
/*      */     //   230: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   233: getstatic 867	csbst/testing/JTE:currentClassUnderTest	Lcsbst/testing/ClassUnderTest;
/*      */     //   236: invokevirtual 869	csbst/testing/ClassUnderTest:getClazz	()Ljava/lang/Class;
/*      */     //   239: invokevirtual 874	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   242: invokevirtual 218	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   245: invokevirtual 222	java/io/PrintStream:println	(Ljava/lang/String;)V
/*      */     //   248: invokestatic 877	csbst/testing/JTE:writeTestCases	()V
/*      */     //   251: aload_3
/*      */     //   252: athrow
/*      */     //   253: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   256: ifle +45 -> 301
/*      */     //   259: getstatic 471	java/lang/System:err	Ljava/io/PrintStream;
/*      */     //   262: new 205	java/lang/StringBuilder
/*      */     //   265: dup
/*      */     //   266: ldc_w 860
/*      */     //   269: invokespecial 209	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   272: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   275: iconst_1
/*      */     //   276: iadd
/*      */     //   277: invokevirtual 862	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   280: ldc_w 865
/*      */     //   283: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   286: getstatic 867	csbst/testing/JTE:currentClassUnderTest	Lcsbst/testing/ClassUnderTest;
/*      */     //   289: invokevirtual 869	csbst/testing/ClassUnderTest:getClazz	()Ljava/lang/Class;
/*      */     //   292: invokevirtual 874	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   295: invokevirtual 218	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   298: invokevirtual 222	java/io/PrintStream:println	(Ljava/lang/String;)V
/*      */     //   301: invokestatic 877	csbst/testing/JTE:writeTestCases	()V
/*      */     //   304: return
/*      */     // Line number table:
/*      */     //   Java source line #749	-> byte code offset #0
/*      */     //   Java source line #751	-> byte code offset #9
/*      */     //   Java source line #752	-> byte code offset #12
/*      */     //   Java source line #753	-> byte code offset #20
/*      */     //   Java source line #754	-> byte code offset #24
/*      */     //   Java source line #755	-> byte code offset #28
/*      */     //   Java source line #756	-> byte code offset #32
/*      */     //   Java source line #757	-> byte code offset #36
/*      */     //   Java source line #758	-> byte code offset #40
/*      */     //   Java source line #760	-> byte code offset #44
/*      */     //   Java source line #761	-> byte code offset #50
/*      */     //   Java source line #762	-> byte code offset #56
/*      */     //   Java source line #763	-> byte code offset #68
/*      */     //   Java source line #769	-> byte code offset #80
/*      */     //   Java source line #770	-> byte code offset #88
/*      */     //   Java source line #771	-> byte code offset #100
/*      */     //   Java source line #774	-> byte code offset #106
/*      */     //   Java source line #779	-> byte code offset #112
/*      */     //   Java source line #780	-> byte code offset #122
/*      */     //   Java source line #783	-> byte code offset #134
/*      */     //   Java source line #786	-> byte code offset #140
/*      */     //   Java source line #787	-> byte code offset #141
/*      */     //   Java source line #793	-> byte code offset #145
/*      */     //   Java source line #794	-> byte code offset #151
/*      */     //   Java source line #795	-> byte code offset #193
/*      */     //   Java source line #789	-> byte code offset #199
/*      */     //   Java source line #793	-> byte code offset #200
/*      */     //   Java source line #794	-> byte code offset #206
/*      */     //   Java source line #795	-> byte code offset #248
/*      */     //   Java source line #796	-> byte code offset #251
/*      */     //   Java source line #793	-> byte code offset #253
/*      */     //   Java source line #794	-> byte code offset #259
/*      */     //   Java source line #795	-> byte code offset #301
/*      */     //   Java source line #798	-> byte code offset #304
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   67	2	0	killMeAfterTimeOut	KillMeAfterTimeOut
/*      */     //   79	2	1	testDataSaver	TestDataSaver
/*      */     //   140	2	2	e	Exception
/*      */     //   199	53	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   80	137	140	java/lang/Exception
/*      */     //   80	145	199	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   static void iteration()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: goto +294 -> 294
/*      */     //   3: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   6: invokeinterface 731 1 0
/*      */     //   11: invokeinterface 484 1 0
/*      */     //   16: checkcast 313	java/lang/Integer
/*      */     //   19: invokevirtual 666	java/lang/Integer:intValue	()I
/*      */     //   22: istore_0
/*      */     //   23: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   26: invokeinterface 887 1 0
/*      */     //   31: istore_1
/*      */     //   32: new 888	java/util/Random
/*      */     //   35: dup
/*      */     //   36: invokespecial 890	java/util/Random:<init>	()V
/*      */     //   39: iload_1
/*      */     //   40: invokevirtual 891	java/util/Random:nextInt	(I)I
/*      */     //   43: istore_2
/*      */     //   44: iconst_0
/*      */     //   45: istore_3
/*      */     //   46: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   49: invokeinterface 731 1 0
/*      */     //   54: astore 5
/*      */     //   56: goto +29 -> 85
/*      */     //   59: aload 5
/*      */     //   61: invokeinterface 484 1 0
/*      */     //   66: checkcast 313	java/lang/Integer
/*      */     //   69: invokevirtual 666	java/lang/Integer:intValue	()I
/*      */     //   72: istore 4
/*      */     //   74: iload_3
/*      */     //   75: iload_2
/*      */     //   76: if_icmpne +6 -> 82
/*      */     //   79: iload 4
/*      */     //   81: istore_0
/*      */     //   82: iinc 3 1
/*      */     //   85: aload 5
/*      */     //   87: invokeinterface 496 1 0
/*      */     //   92: ifne -33 -> 59
/*      */     //   95: new 895	csbst/testing/Target
/*      */     //   98: dup
/*      */     //   99: iload_0
/*      */     //   100: invokespecial 897	csbst/testing/Target:<init>	(I)V
/*      */     //   103: putstatic 899	csbst/testing/JTE:currentTarget	Lcsbst/testing/Target;
/*      */     //   106: iconst_0
/*      */     //   107: istore 4
/*      */     //   109: getstatic 153	csbst/testing/JTE:remaindEvaluations	I
/*      */     //   112: getstatic 853	csbst/testing/JTE:tmpUncoveredTarget	Ljava/util/Set;
/*      */     //   115: invokeinterface 887 1 0
/*      */     //   120: idiv
/*      */     //   121: istore 5
/*      */     //   123: iload 5
/*      */     //   125: getstatic 179	csbst/testing/JTE:minEvaluationPerBranch	I
/*      */     //   128: if_icmpge +8 -> 136
/*      */     //   131: getstatic 153	csbst/testing/JTE:remaindEvaluations	I
/*      */     //   134: istore 5
/*      */     //   136: getstatic 149	csbst/testing/JTE:geneartionNumber	I
/*      */     //   139: getstatic 179	csbst/testing/JTE:minEvaluationPerBranch	I
/*      */     //   142: imul
/*      */     //   143: iload 5
/*      */     //   145: if_icmpge +19 -> 164
/*      */     //   148: getstatic 149	csbst/testing/JTE:geneartionNumber	I
/*      */     //   151: iconst_5
/*      */     //   152: if_icmpge +12 -> 164
/*      */     //   155: getstatic 149	csbst/testing/JTE:geneartionNumber	I
/*      */     //   158: getstatic 179	csbst/testing/JTE:minEvaluationPerBranch	I
/*      */     //   161: imul
/*      */     //   162: istore 5
/*      */     //   164: new 901	csbst/heuristic/RandomTesting
/*      */     //   167: dup
/*      */     //   168: iconst_1
/*      */     //   169: invokespecial 903	csbst/heuristic/RandomTesting:<init>	(I)V
/*      */     //   172: astore 6
/*      */     //   174: aload 6
/*      */     //   176: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   179: invokevirtual 904	csbst/heuristic/RandomTesting:run	(Ljava/util/Set;)I
/*      */     //   182: istore 4
/*      */     //   184: iload 4
/*      */     //   186: iconst_m1
/*      */     //   187: if_icmpne +11 -> 198
/*      */     //   190: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   193: iconst_1
/*      */     //   194: iadd
/*      */     //   195: putstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   198: iconst_1
/*      */     //   199: istore 4
/*      */     //   201: getstatic 807	csbst/testing/JTE:totalEvaluations	I
/*      */     //   204: iload 4
/*      */     //   206: iadd
/*      */     //   207: putstatic 807	csbst/testing/JTE:totalEvaluations	I
/*      */     //   210: invokestatic 851	csbst/testing/JTE:getRemaindEvaluations	()I
/*      */     //   213: putstatic 153	csbst/testing/JTE:remaindEvaluations	I
/*      */     //   216: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   219: getstatic 899	csbst/testing/JTE:currentTarget	Lcsbst/testing/Target;
/*      */     //   222: invokevirtual 908	csbst/testing/Target:getBranch	()I
/*      */     //   225: invokestatic 312	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*      */     //   228: invokeinterface 911 2 0
/*      */     //   233: pop
/*      */     //   234: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   237: getstatic 137	csbst/testing/JTE:allCoveredBranches	Ljava/util/Set;
/*      */     //   240: invokeinterface 845 2 0
/*      */     //   245: pop
/*      */     //   246: getstatic 853	csbst/testing/JTE:tmpUncoveredTarget	Ljava/util/Set;
/*      */     //   249: getstatic 137	csbst/testing/JTE:allCoveredBranches	Ljava/util/Set;
/*      */     //   252: invokeinterface 845 2 0
/*      */     //   257: pop
/*      */     //   258: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   261: invokeinterface 914 1 0
/*      */     //   266: ifeq +23 -> 289
/*      */     //   269: getstatic 149	csbst/testing/JTE:geneartionNumber	I
/*      */     //   272: iconst_1
/*      */     //   273: iadd
/*      */     //   274: putstatic 149	csbst/testing/JTE:geneartionNumber	I
/*      */     //   277: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   280: getstatic 853	csbst/testing/JTE:tmpUncoveredTarget	Ljava/util/Set;
/*      */     //   283: invokeinterface 855 2 0
/*      */     //   288: pop
/*      */     //   289: iload 4
/*      */     //   291: invokestatic 848	csbst/testing/JTE:printProgress	(I)V
/*      */     //   294: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   297: invokeinterface 914 1 0
/*      */     //   302: ifne +92 -> 394
/*      */     //   305: getstatic 153	csbst/testing/JTE:remaindEvaluations	I
/*      */     //   308: ifle +86 -> 394
/*      */     //   311: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   314: getstatic 183	csbst/testing/JTE:maxThreadNumber	I
/*      */     //   317: if_icmplt -314 -> 3
/*      */     //   320: goto +74 -> 394
/*      */     //   323: astore_0
/*      */     //   324: aload_0
/*      */     //   325: invokevirtual 859	java/lang/Exception:printStackTrace	()V
/*      */     //   328: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   331: invokeinterface 914 1 0
/*      */     //   336: ifne +87 -> 423
/*      */     //   339: getstatic 153	csbst/testing/JTE:remaindEvaluations	I
/*      */     //   342: ifle +81 -> 423
/*      */     //   345: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   348: getstatic 183	csbst/testing/JTE:maxThreadNumber	I
/*      */     //   351: if_icmpge +72 -> 423
/*      */     //   354: invokestatic 856	csbst/testing/JTE:iteration	()V
/*      */     //   357: goto +66 -> 423
/*      */     //   360: astore 7
/*      */     //   362: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   365: invokeinterface 914 1 0
/*      */     //   370: ifne +21 -> 391
/*      */     //   373: getstatic 153	csbst/testing/JTE:remaindEvaluations	I
/*      */     //   376: ifle +15 -> 391
/*      */     //   379: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   382: getstatic 183	csbst/testing/JTE:maxThreadNumber	I
/*      */     //   385: if_icmpge +6 -> 391
/*      */     //   388: invokestatic 856	csbst/testing/JTE:iteration	()V
/*      */     //   391: aload 7
/*      */     //   393: athrow
/*      */     //   394: getstatic 132	csbst/testing/JTE:branchesTarget	Ljava/util/Set;
/*      */     //   397: invokeinterface 914 1 0
/*      */     //   402: ifne +21 -> 423
/*      */     //   405: getstatic 153	csbst/testing/JTE:remaindEvaluations	I
/*      */     //   408: ifle +15 -> 423
/*      */     //   411: getstatic 151	csbst/testing/JTE:nmberOfStuckThreads	I
/*      */     //   414: getstatic 183	csbst/testing/JTE:maxThreadNumber	I
/*      */     //   417: if_icmpge +6 -> 423
/*      */     //   420: invokestatic 856	csbst/testing/JTE:iteration	()V
/*      */     //   423: return
/*      */     // Line number table:
/*      */     //   Java source line #802	-> byte code offset #0
/*      */     //   Java source line #804	-> byte code offset #3
/*      */     //   Java source line #805	-> byte code offset #23
/*      */     //   Java source line #806	-> byte code offset #32
/*      */     //   Java source line #807	-> byte code offset #44
/*      */     //   Java source line #808	-> byte code offset #46
/*      */     //   Java source line #810	-> byte code offset #74
/*      */     //   Java source line #811	-> byte code offset #79
/*      */     //   Java source line #812	-> byte code offset #82
/*      */     //   Java source line #808	-> byte code offset #85
/*      */     //   Java source line #817	-> byte code offset #95
/*      */     //   Java source line #818	-> byte code offset #106
/*      */     //   Java source line #820	-> byte code offset #109
/*      */     //   Java source line #822	-> byte code offset #123
/*      */     //   Java source line #823	-> byte code offset #131
/*      */     //   Java source line #825	-> byte code offset #136
/*      */     //   Java source line #826	-> byte code offset #155
/*      */     //   Java source line #828	-> byte code offset #164
/*      */     //   Java source line #829	-> byte code offset #174
/*      */     //   Java source line #832	-> byte code offset #184
/*      */     //   Java source line #833	-> byte code offset #190
/*      */     //   Java source line #836	-> byte code offset #198
/*      */     //   Java source line #837	-> byte code offset #201
/*      */     //   Java source line #838	-> byte code offset #210
/*      */     //   Java source line #842	-> byte code offset #216
/*      */     //   Java source line #843	-> byte code offset #234
/*      */     //   Java source line #844	-> byte code offset #246
/*      */     //   Java source line #845	-> byte code offset #258
/*      */     //   Java source line #846	-> byte code offset #269
/*      */     //   Java source line #847	-> byte code offset #277
/*      */     //   Java source line #850	-> byte code offset #289
/*      */     //   Java source line #802	-> byte code offset #294
/*      */     //   Java source line #852	-> byte code offset #323
/*      */     //   Java source line #853	-> byte code offset #324
/*      */     //   Java source line #856	-> byte code offset #328
/*      */     //   Java source line #857	-> byte code offset #354
/*      */     //   Java source line #855	-> byte code offset #360
/*      */     //   Java source line #856	-> byte code offset #362
/*      */     //   Java source line #857	-> byte code offset #388
/*      */     //   Java source line #858	-> byte code offset #391
/*      */     //   Java source line #856	-> byte code offset #394
/*      */     //   Java source line #857	-> byte code offset #420
/*      */     //   Java source line #859	-> byte code offset #423
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   22	78	0	branch	int
/*      */     //   323	2	0	e	Exception
/*      */     //   31	9	1	size	int
/*      */     //   43	33	2	item	int
/*      */     //   45	38	3	i	int
/*      */     //   72	8	4	obj	int
/*      */     //   107	183	4	consumedEF	int
/*      */     //   54	32	5	localIterator	Iterator
/*      */     //   121	42	5	avgEvalPerBranch	int
/*      */     //   172	3	6	RA	csbst.heuristic.RandomTesting
/*      */     //   360	32	7	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	320	323	java/lang/Exception
/*      */     //   0	328	360	finally
/*      */   }
/*      */   
/*      */   static void writeTestCases()
/*      */   {
/*  863 */     writeTestCases(true);
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   static void writeTestCases(boolean close)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: invokestatic 832	java/lang/System:currentTimeMillis	()J
/*      */     //   3: lstore_1
/*      */     //   4: getstatic 123	csbst/testing/JTE:writeTestCasesIsDone	Z
/*      */     //   7: ifeq +17 -> 24
/*      */     //   10: iload_0
/*      */     //   11: ifeq +12 -> 23
/*      */     //   14: invokestatic 925	csbst/testing/JTE:enableSystemExitCall	()V
/*      */     //   17: sipush 5555
/*      */     //   20: invokestatic 927	java/lang/System:exit	(I)V
/*      */     //   23: return
/*      */     //   24: iload_0
/*      */     //   25: ifeq +7 -> 32
/*      */     //   28: iconst_1
/*      */     //   29: putstatic 123	csbst/testing/JTE:writeTestCasesIsDone	Z
/*      */     //   32: iload_0
/*      */     //   33: ifeq +75 -> 108
/*      */     //   36: getstatic 162	csbst/testing/JTE:testDataSetWithErrors	Ljava/util/Set;
/*      */     //   39: invokeinterface 731 1 0
/*      */     //   44: astore 4
/*      */     //   46: goto +52 -> 98
/*      */     //   49: aload 4
/*      */     //   51: invokeinterface 484 1 0
/*      */     //   56: checkcast 930	csbst/ga/ecj/TestCaseCandidate
/*      */     //   59: astore_3
/*      */     //   60: getstatic 137	csbst/testing/JTE:allCoveredBranches	Ljava/util/Set;
/*      */     //   63: aload_3
/*      */     //   64: invokevirtual 932	csbst/ga/ecj/TestCaseCandidate:getCoveredBranches	()Ljava/util/Set;
/*      */     //   67: invokeinterface 936 2 0
/*      */     //   72: ifne +26 -> 98
/*      */     //   75: getstatic 137	csbst/testing/JTE:allCoveredBranches	Ljava/util/Set;
/*      */     //   78: aload_3
/*      */     //   79: invokevirtual 932	csbst/ga/ecj/TestCaseCandidate:getCoveredBranches	()Ljava/util/Set;
/*      */     //   82: invokeinterface 855 2 0
/*      */     //   87: pop
/*      */     //   88: getstatic 160	csbst/testing/JTE:testDataSet	Ljava/util/Set;
/*      */     //   91: aload_3
/*      */     //   92: invokeinterface 318 2 0
/*      */     //   97: pop
/*      */     //   98: aload 4
/*      */     //   100: invokeinterface 496 1 0
/*      */     //   105: ifne -56 -> 49
/*      */     //   108: lload_1
/*      */     //   109: getstatic 939	csbst/testing/JTE:ActualstartTime	J
/*      */     //   112: lsub
/*      */     //   113: putstatic 941	csbst/testing/JTE:totalTime	J
/*      */     //   116: lload_1
/*      */     //   117: getstatic 835	csbst/testing/JTE:startTime	J
/*      */     //   120: lsub
/*      */     //   121: putstatic 943	csbst/testing/JTE:totalSearchTime	J
/*      */     //   124: getstatic 141	csbst/testing/JTE:allNotCoveredBranches	Ljava/util/Set;
/*      */     //   127: invokeinterface 307 1 0
/*      */     //   132: iconst_1
/*      */     //   133: istore_3
/*      */     //   134: goto +34 -> 168
/*      */     //   137: getstatic 137	csbst/testing/JTE:allCoveredBranches	Ljava/util/Set;
/*      */     //   140: iload_3
/*      */     //   141: invokestatic 312	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*      */     //   144: invokeinterface 945 2 0
/*      */     //   149: ifne +16 -> 165
/*      */     //   152: getstatic 141	csbst/testing/JTE:allNotCoveredBranches	Ljava/util/Set;
/*      */     //   155: iload_3
/*      */     //   156: invokestatic 312	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*      */     //   159: invokeinterface 318 2 0
/*      */     //   164: pop
/*      */     //   165: iinc 3 1
/*      */     //   168: iload_3
/*      */     //   169: getstatic 319	csbst/testing/JTE:branchCoderAnalyser	Lcsbst/analysis/BranchesCoder;
/*      */     //   172: invokevirtual 321	csbst/analysis/BranchesCoder:getLastBranch	()I
/*      */     //   175: if_icmplt -38 -> 137
/*      */     //   178: iload_0
/*      */     //   179: ifne +20 -> 199
/*      */     //   182: getstatic 160	csbst/testing/JTE:testDataSet	Ljava/util/Set;
/*      */     //   185: invokeinterface 887 1 0
/*      */     //   190: getstatic 143	csbst/testing/JTE:lastSaveNumberOfNCB	I
/*      */     //   193: if_icmpne +6 -> 199
/*      */     //   196: goto -186 -> 10
/*      */     //   199: getstatic 160	csbst/testing/JTE:testDataSet	Ljava/util/Set;
/*      */     //   202: invokeinterface 887 1 0
/*      */     //   207: putstatic 143	csbst/testing/JTE:lastSaveNumberOfNCB	I
/*      */     //   210: invokestatic 947	csbst/testing/JTE:getTestCasesSourceCode	()Lorg/eclipse/jdt/core/dom/CompilationUnit;
/*      */     //   213: astore_3
/*      */     //   214: new 205	java/lang/StringBuilder
/*      */     //   217: dup
/*      */     //   218: getstatic 951	csbst/testing/JTE:testCasesPath	Ljava/lang/String;
/*      */     //   221: invokestatic 347	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   224: invokespecial 209	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   227: getstatic 350	java/io/File:separator	Ljava/lang/String;
/*      */     //   230: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   233: getstatic 355	csbst/testing/JTE:subPath	Ljava/lang/String;
/*      */     //   236: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   239: getstatic 350	java/io/File:separator	Ljava/lang/String;
/*      */     //   242: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   245: getstatic 357	csbst/testing/JTE:srcFileName	Ljava/lang/String;
/*      */     //   248: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   251: ldc 88
/*      */     //   253: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   256: ldc_w 359
/*      */     //   259: invokevirtual 214	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   262: invokevirtual 218	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   265: astore 4
/*      */     //   267: aload_3
/*      */     //   268: aload 4
/*      */     //   270: invokestatic 361	csbst/utils/ASTEditor:unit2File	(Lorg/eclipse/jdt/core/dom/CompilationUnit;Ljava/lang/String;)V
/*      */     //   273: invokestatic 953	csbst/testing/JTE:writeSummaryFile	()V
/*      */     //   276: iload_0
/*      */     //   277: ifeq +61 -> 338
/*      */     //   280: getstatic 160	csbst/testing/JTE:testDataSet	Ljava/util/Set;
/*      */     //   283: invokeinterface 307 1 0
/*      */     //   288: getstatic 162	csbst/testing/JTE:testDataSetWithErrors	Ljava/util/Set;
/*      */     //   291: invokeinterface 307 1 0
/*      */     //   296: goto +42 -> 338
/*      */     //   299: astore_1
/*      */     //   300: aload_1
/*      */     //   301: invokevirtual 859	java/lang/Exception:printStackTrace	()V
/*      */     //   304: iload_0
/*      */     //   305: ifeq +46 -> 351
/*      */     //   308: invokestatic 925	csbst/testing/JTE:enableSystemExitCall	()V
/*      */     //   311: sipush 5555
/*      */     //   314: invokestatic 927	java/lang/System:exit	(I)V
/*      */     //   317: goto +34 -> 351
/*      */     //   320: astore 5
/*      */     //   322: iload_0
/*      */     //   323: ifeq +12 -> 335
/*      */     //   326: invokestatic 925	csbst/testing/JTE:enableSystemExitCall	()V
/*      */     //   329: sipush 5555
/*      */     //   332: invokestatic 927	java/lang/System:exit	(I)V
/*      */     //   335: aload 5
/*      */     //   337: athrow
/*      */     //   338: iload_0
/*      */     //   339: ifeq +12 -> 351
/*      */     //   342: invokestatic 925	csbst/testing/JTE:enableSystemExitCall	()V
/*      */     //   345: sipush 5555
/*      */     //   348: invokestatic 927	java/lang/System:exit	(I)V
/*      */     //   351: return
/*      */     // Line number table:
/*      */     //   Java source line #868	-> byte code offset #0
/*      */     //   Java source line #869	-> byte code offset #4
/*      */     //   Java source line #924	-> byte code offset #10
/*      */     //   Java source line #925	-> byte code offset #14
/*      */     //   Java source line #926	-> byte code offset #17
/*      */     //   Java source line #870	-> byte code offset #23
/*      */     //   Java source line #871	-> byte code offset #24
/*      */     //   Java source line #872	-> byte code offset #28
/*      */     //   Java source line #874	-> byte code offset #32
/*      */     //   Java source line #875	-> byte code offset #36
/*      */     //   Java source line #876	-> byte code offset #60
/*      */     //   Java source line #877	-> byte code offset #75
/*      */     //   Java source line #878	-> byte code offset #88
/*      */     //   Java source line #875	-> byte code offset #98
/*      */     //   Java source line #895	-> byte code offset #108
/*      */     //   Java source line #896	-> byte code offset #116
/*      */     //   Java source line #898	-> byte code offset #124
/*      */     //   Java source line #900	-> byte code offset #132
/*      */     //   Java source line #901	-> byte code offset #137
/*      */     //   Java source line #902	-> byte code offset #152
/*      */     //   Java source line #900	-> byte code offset #165
/*      */     //   Java source line #905	-> byte code offset #178
/*      */     //   Java source line #906	-> byte code offset #196
/*      */     //   Java source line #908	-> byte code offset #199
/*      */     //   Java source line #910	-> byte code offset #210
/*      */     //   Java source line #911	-> byte code offset #214
/*      */     //   Java source line #912	-> byte code offset #267
/*      */     //   Java source line #913	-> byte code offset #273
/*      */     //   Java source line #914	-> byte code offset #276
/*      */     //   Java source line #915	-> byte code offset #280
/*      */     //   Java source line #916	-> byte code offset #288
/*      */     //   Java source line #919	-> byte code offset #299
/*      */     //   Java source line #921	-> byte code offset #300
/*      */     //   Java source line #924	-> byte code offset #304
/*      */     //   Java source line #925	-> byte code offset #308
/*      */     //   Java source line #926	-> byte code offset #311
/*      */     //   Java source line #923	-> byte code offset #320
/*      */     //   Java source line #924	-> byte code offset #322
/*      */     //   Java source line #925	-> byte code offset #326
/*      */     //   Java source line #926	-> byte code offset #329
/*      */     //   Java source line #928	-> byte code offset #335
/*      */     //   Java source line #924	-> byte code offset #338
/*      */     //   Java source line #925	-> byte code offset #342
/*      */     //   Java source line #926	-> byte code offset #345
/*      */     //   Java source line #930	-> byte code offset #351
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	352	0	close	boolean
/*      */     //   3	114	1	endTime	long
/*      */     //   299	2	1	e	Exception
/*      */     //   59	33	3	ch	TestCaseCandidate
/*      */     //   133	36	3	i	int
/*      */     //   213	55	3	unit	CompilationUnit
/*      */     //   44	55	4	localIterator	Iterator
/*      */     //   265	4	4	fileName	String
/*      */     //   320	16	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	10	299	java/lang/Exception
/*      */     //   24	296	299	java/lang/Exception
/*      */     //   0	10	320	finally
/*      */     //   24	304	320	finally
/*      */   }
/*      */   
/*      */   private static void writeSummaryFile()
/*      */   {
/*      */     try
/*      */     {
/*  935 */       FileWriter jteSummary = new FileWriter(new File(testCasesPath + File.separator + subPath + File.separator + srcFileName + "jteSummary.csv"));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  946 */       jteSummary.write(className);jteSummary.write(";");
/*  947 */       jteSummary.write(branchCoderAnalyser.getLastBranch());jteSummary.write(";");
/*  948 */       jteSummary.write(allCoveredBranches.toArray().length);jteSummary.write(";");
/*  949 */       DecimalFormat df = new DecimalFormat("#.##");
/*  950 */       jteSummary.write(df.format(100.0D * allCoveredBranches.size() / branchCoderAnalyser.getLastBranch()));jteSummary.write(";");
/*  951 */       jteSummary.write(totalEvaluations);jteSummary.write(";");
/*  952 */       jteSummary.write((int)totalSearchTime);jteSummary.write(";");
/*  953 */       jteSummary.write((int)totalTime);
/*  954 */       jteSummary.write("\r");
/*      */       
/*  956 */       jteSummary.close();
/*      */     }
/*      */     catch (IOException e) {
/*  959 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   private static void printProgress(int cons) {
/*  964 */     if (!printProgress)
/*  965 */       return;
/*  966 */     String bar1 = "[..................................................]";
/*  967 */     String bar2 = "[                                                  ]";
/*  968 */     DecimalFormat df = new DecimalFormat("##.00");
/*  969 */     double sprc = 1.0D * totalEvaluations / getMaxEvaluations();
/*  970 */     int sprg = (int)(sprc * bar1.length());
/*  971 */     String search = "search: ";
/*  972 */     if (bar1.length() < sprg)
/*  973 */       sprg = bar1.length();
/*  974 */     search = search + bar1.substring(0, sprg);
/*  975 */     search = search + bar2.substring(sprg);
/*  976 */     search = search + "[" + df.format(100.0D * sprc) + "%]";
/*      */     
/*  978 */     sprc = 1.0D * allCoveredBranches.size() / branchCoderAnalyser.getLastBranch();
/*  979 */     sprg = (int)(sprc * bar1.length());
/*  980 */     String coverage = "coverage: ";
/*  981 */     coverage = coverage + bar1.substring(0, sprg);
/*  982 */     coverage = coverage + bar2.substring(sprg);
/*  983 */     coverage = coverage + "[" + df.format(100.0D * sprc) + "%]";
/*  984 */     stdout.print("\r" + search + "     " + coverage);
/*      */   }
/*      */   
/*      */   private static CompilationUnit getTestCasesSourceCode()
/*      */   {
/*  989 */     ASTParser parser = ASTParser.newParser(3);
/*  990 */     parser.setSource("".toCharArray());
/*  991 */     CompilationUnit unit = (CompilationUnit)parser.createAST(null);
/*  992 */     unit.recordModifications();
/*  993 */     AST ast = unit.getAST();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  999 */     Javadoc jd = ast.newJavadoc();
/* 1000 */     StringBuffer txt = new StringBuffer("");
/*      */     
/*      */ 
/* 1003 */     txt.append("This class was automatically generated to test the " + className + " class according to all branches coverage criterion" + System.getProperty("line.separator"));
/* 1004 */     txt.append("ExceptionsOriented: " + ExceptionsOriented + " " + System.getProperty("line.separator"));
/* 1005 */     txt.append("projectPackagesPrefix:" + projectPackagesPrefix + " " + System.getProperty("line.separator"));
/* 1006 */     txt.append("Covered branches: " + allCoveredBranches.toString() + System.getProperty("line.separator"));
/* 1007 */     txt.append("Uncovered branches: " + allNotCoveredBranches.toString() + System.getProperty("line.separator"));
/* 1008 */     txt.append("Total number of branches: " + branchCoderAnalyser.getLastBranch() + System.getProperty("line.separator"));
/* 1009 */     txt.append("Total number of covered branches: " + allCoveredBranches.toArray().length + System.getProperty("line.separator"));
/* 1010 */     DecimalFormat df = new DecimalFormat("#.##");
/* 1011 */     txt.append("Coverage : " + df.format(100.0D * allCoveredBranches.size() / branchCoderAnalyser.getLastBranch()) + "%" + System.getProperty("line.separator"));
/* 1012 */     txt.append("Evaluations : " + totalEvaluations + System.getProperty("line.separator"));
/* 1013 */     txt.append("search time (ms): " + totalSearchTime + System.getProperty("line.separator"));
/* 1014 */     txt.append("total runtime (ms): " + totalTime);
/*      */     
/* 1016 */     TextElement txtElt = ast.newTextElement();
/* 1017 */     txtElt.setText(txt.toString());
/*      */     
/* 1019 */     TagElement tagElt = ast.newTagElement();
/* 1020 */     tagElt.fragments().add(txtElt);
/*      */     
/*      */ 
/* 1023 */     stdout.println(txt);
/* 1024 */     String fileName = testCasesPath + File.separator + subPath + File.separator + srcFileName + "JTETestCases" + ".java";
/* 1025 */     stdout.println("A JUnit test suite was created at: " + fileName);
/*      */     
/*      */ 
/* 1028 */     if (subPath.indexOf(File.separator) > 0) {
/* 1029 */       PackageDeclaration pkgDec = ast.newPackageDeclaration();
/* 1030 */       pkgDec.setName(ASTEditor.generateQualifiedName(subPath.replace(File.separator, "."), ast));
/* 1031 */       unit.setPackage(pkgDec);
/* 1032 */     } else if (!subPath.equals("")) {
/* 1033 */       PackageDeclaration pkgDec = ast.newPackageDeclaration();
/* 1034 */       pkgDec.setName(ast.newSimpleName(subPath));
/* 1035 */       unit.setPackage(pkgDec);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1046 */     ImportDeclaration importDeclaration2 = ast.newImportDeclaration();
/* 1047 */     QualifiedName name2 = ASTEditor.generateQualifiedName("org.junit.Test", ast);
/* 1048 */     importDeclaration2.setName(name2);
/* 1049 */     unit.imports().add(importDeclaration2);
/*      */     
/* 1051 */     ImportDeclaration importDeclaration3 = ast.newImportDeclaration();
/* 1052 */     QualifiedName name3 = ASTEditor.generateQualifiedName("org.junit.Rule", ast);
/* 1053 */     importDeclaration3.setName(name3);
/* 1054 */     unit.imports().add(importDeclaration3);
/*      */     
/*      */ 
/* 1057 */     if (ExceptionsOriented) {
/* 1058 */       ImportDeclaration importDeclaration4 = ast.newImportDeclaration();
/* 1059 */       QualifiedName name4 = ASTEditor.generateQualifiedName("csbst.utils.ExceptionsFormatter", ast);
/* 1060 */       importDeclaration4.setName(name4);
/* 1061 */       unit.imports().add(importDeclaration4);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1095 */     TypeDeclaration clazzNode = ast.newTypeDeclaration();
/* 1096 */     clazzNode.setInterface(false);
/* 1097 */     clazzNode.setName(ast.newSimpleName(srcFileName + "JTETestCases"));
/* 1098 */     clazzNode.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
/*      */     
/* 1100 */     unit.types().add(clazzNode);
/*      */     
/*      */     ImportDeclaration importDeclaration4;
/*      */     
/*      */     Annotation annotation;
/* 1105 */     if (cutCallsSystemExist) {
/* 1106 */       ImportDeclaration importDeclaration1 = ast.newImportDeclaration();
/*      */       
/* 1108 */       QualifiedName name1 = ASTEditor.generateQualifiedName("org.junit.contrib.java.lang.system.ExpectedSystemExit", ast);
/* 1109 */       importDeclaration1.setName(name1);
/* 1110 */       importDeclaration1.setStatic(true);
/* 1111 */       importDeclaration1.setOnDemand(true);
/* 1112 */       unit.imports().add(importDeclaration1);
/*      */       
/* 1114 */       importDeclaration4 = ast.newImportDeclaration();
/* 1115 */       QualifiedName name4 = ASTEditor.generateQualifiedName("org.junit.contrib.java.lang.system.ExpectedSystemExit", ast);
/* 1116 */       importDeclaration4.setName(name4);
/* 1117 */       unit.imports().add(importDeclaration4);
/*      */       
/* 1119 */       VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();
/* 1120 */       MethodInvocation methodInvocation = ast.newMethodInvocation();
/* 1121 */       methodInvocation.setName(ast.newSimpleName("none"));
/* 1122 */       methodInvocation.setExpression(ast.newSimpleName("ExpectedSystemExit"));
/* 1123 */       vdf.setInitializer(methodInvocation);
/* 1124 */       vdf.setName(ast.newSimpleName("exit"));
/*      */       
/* 1126 */       FieldDeclaration vds = ast.newFieldDeclaration(vdf);
/* 1127 */       List modifiers = vds.modifiers();
/* 1128 */       annotation = ast.newMarkerAnnotation();
/* 1129 */       annotation.setTypeName(ast.newName("Rule"));
/* 1130 */       modifiers.add(annotation);
/* 1131 */       vds.setType(ast.newSimpleType(ast.newSimpleName("ExpectedSystemExit")));
/* 1132 */       modifiers.add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
/* 1133 */       modifiers.add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
/*      */       
/* 1135 */       clazzNode.bodyDeclarations().add(0, vds);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1154 */     int i = 0;
/* 1155 */     for (TestCaseCandidate ch : testDataSet)
/*      */     {
/* 1157 */       clazzNode.bodyDeclarations().add(ch.generateTestCaseSourceCode(clazzNode, "TestCase" + i));
/* 1158 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1163 */     for (Class cls : requiredClasses)
/*      */     {
/* 1165 */       if ((cls.getCanonicalName() != null) && 
/* 1166 */         (!cls.isPrimitive()) && (
/* 1167 */         (cls.getPackage() == null) || (!cls.getPackage().getName().toString().equals("java.lang"))))
/*      */       {
/*      */ 
/*      */ 
/* 1171 */         ImportDeclaration impDec = ast.newImportDeclaration();
/* 1172 */         String binaryName = cls.getName();
/* 1173 */         if (cls.isMemberClass())
/* 1174 */           binaryName = cls.getDeclaringClass().getName();
/* 1175 */         if (cls.isLocalClass()) {
/* 1176 */           binaryName = cls.getEnclosingClass().getName();
/*      */         }
/* 1178 */         binaryName = binaryName.replace("$", ".");
/*      */         Name impName;
/* 1180 */         Name impName; if (binaryName.lastIndexOf(".") < 0) {
/* 1181 */           impName = ast.newSimpleName(binaryName);
/*      */         }
/*      */         else
/*      */         {
/* 1185 */           impName = ASTEditor.generateQualifiedName(binaryName, ast);
/*      */         }
/*      */         
/*      */ 
/* 1189 */         impDec.setName(impName);
/*      */         
/* 1191 */         boolean exist = false;
/* 1192 */         for (Object imp1 : unit.imports()) {
/* 1193 */           if (((ImportDeclaration)imp1).getName().toString().equals(impName)) {
/* 1194 */             exist = true;
/* 1195 */             break;
/*      */           }
/*      */         }
/* 1198 */         if (!exist)
/*      */         {
/* 1200 */           unit.imports().add(impDec); }
/*      */       }
/*      */     }
/* 1203 */     jd.tags().add(tagElt);
/* 1204 */     clazzNode.setJavadoc(jd);
/* 1205 */     return unit;
/*      */   }
/*      */   
/*      */   private static void checkCUTCompatibility(MethodDeclaration declaredMethod) throws IOException {
/* 1209 */     boolean isMemberClass = false;
/* 1210 */     boolean isLocalClass = false;
/* 1211 */     String declaringClassName = className;
/* 1212 */     String canonicalClassName = "";
/*      */     
/* 1214 */     if ((declaredMethod.getParent() instanceof TypeDeclaration)) {
/* 1215 */       TypeDeclaration declaringClazz = (TypeDeclaration)declaredMethod.getParent();
/* 1216 */       isMemberClass = declaringClazz.isMemberTypeDeclaration();
/* 1217 */       isLocalClass = declaringClazz.isLocalTypeDeclaration();
/*      */       
/* 1219 */       canonicalClassName = declaringClassName;
/*      */       
/* 1221 */       if ((declaringClazz != null) && ((declaringClazz.isMemberTypeDeclaration()) || (declaringClazz.isLocalTypeDeclaration()))) {
/* 1222 */         canonicalClassName = canonicalClassName + "." + declaringClazz.getName();
/*      */       }
/*      */       
/*      */ 
/* 1226 */       if ((declaringClazz != null) && (declaringClazz.isLocalTypeDeclaration())) {
/* 1227 */         declaringClassName = declaringClassName + "." + declaringClazz.getName();
/*      */       }
/* 1229 */     } else if ((declaredMethod.getParent() instanceof EnumDeclaration)) {
/* 1230 */       EnumDeclaration declaringClazz = (EnumDeclaration)declaredMethod.getParent();
/* 1231 */       isMemberClass = declaringClazz.isMemberTypeDeclaration();
/* 1232 */       isLocalClass = declaringClazz.isLocalTypeDeclaration();
/*      */       
/*      */ 
/* 1235 */       canonicalClassName = declaringClassName;
/* 1236 */       if ((declaringClazz != null) && (declaringClazz.isMemberTypeDeclaration())) {
/* 1237 */         canonicalClassName = canonicalClassName + "." + declaringClazz.getName();
/*      */       }
/*      */       
/*      */ 
/* 1241 */       if ((declaringClazz != null) && (declaringClazz.isLocalTypeDeclaration())) {
/* 1242 */         declaringClassName = declaringClassName + "." + declaringClazz.getName();
/*      */       }
/* 1244 */     } else if ((declaredMethod.getParent() instanceof AnonymousClassDeclaration)) {
/* 1245 */       ITypeBinding type = ((AnonymousClassDeclaration)declaredMethod.getParent()).resolveBinding();
/* 1246 */       if (type != null) {
/* 1247 */         canonicalClassName = type.getBinaryName();
/* 1248 */         declaringClassName = canonicalClassName;
/*      */       }
/* 1250 */       if (canonicalClassName != null) {}
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1255 */       System.err.println("Mismatch error between the source code and the compiled class at the function: checkCUTCompatibility " + declaredMethod.getName());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1261 */     if ((currentClassUnderTest.getClazz().isAnonymousClass()) || 
/* 1262 */       (!canonicalClassName.equals(currentClassUnderTest.getClazz().getCanonicalName().toString()))) {
/* 1263 */       Class candidatCUT = null;
/* 1264 */       if (((currentClassUnderTest.getClazz().isMemberClass()) || (isMemberClass)) && (!currentClassUnderTest.getClazz().isAnonymousClass())) {
/* 1265 */         if (canonicalClassName.startsWith(currentClassUnderTest.getClazz().getCanonicalName().toString())) {
/* 1266 */           candidatCUT = findMemberClass(canonicalClassName, currentClassUnderTest.getClazz());
/*      */         }
/* 1268 */         else if (currentClassUnderTest.getClazz().getCanonicalName().toString().startsWith(canonicalClassName)) {
/* 1269 */           candidatCUT = findDeclaringClass(canonicalClassName, currentClassUnderTest.getClazz());
/*      */         } else {
/* 1271 */           candidatCUT = findFriendClass(declaringClassName, canonicalClassName, currentClassUnderTest.getClazz());
/*      */         }
/*      */       }
/* 1274 */       if ((currentClassUnderTest.getClazz().isAnonymousClass()) || ((!currentClassUnderTest.getClazz().isMemberClass()) && (!isMemberClass)) || (candidatCUT == null)) {
/*      */         try {
/* 1276 */           Class cls = magicClassLoader.loadClass(declaringClassName);
/* 1277 */           setClassUnderTest(cls);
/*      */         }
/*      */         catch (MalformedURLException e)
/*      */         {
/* 1281 */           e.printStackTrace();
/*      */         }
/*      */         catch (ClassNotFoundException e) {
/* 1284 */           e.printStackTrace();
/*      */         }
/*      */         
/* 1287 */         candidatCUT = findMemberClass(canonicalClassName, currentClassUnderTest.getClazz());
/*      */       }
/* 1289 */       if (candidatCUT != null)
/* 1290 */         setClassUnderTest(candidatCUT);
/*      */     }
/*      */   }
/*      */   
/*      */   private static Method methodDec2MethodRefSimple(MethodDeclaration declaredMethod) throws IOException {
/* 1295 */     checkCUTCompatibility(declaredMethod);
/* 1296 */     Method reflexionMethod = null;
/* 1297 */     Method[] refelexionMethods = currentClassUnderTest.getClazz().getDeclaredMethods();
/* 1298 */     List declaredParemeter = declaredMethod.parameters();
/*      */     
/* 1300 */     for (int m = 0; m < refelexionMethods.length; m++) {
/* 1301 */       reflexionMethod = refelexionMethods[m];
/* 1302 */       Class[] cParameterTypes = reflexionMethod.getParameterTypes();
/*      */       
/* 1304 */       String reflexionSimpleName = reflexionMethod.getName().toString();
/* 1305 */       reflexionSimpleName = reflexionSimpleName.replace("$", ".");
/* 1306 */       String[] HirarchicalName = reflexionSimpleName.split("\\.");
/* 1307 */       reflexionSimpleName = HirarchicalName[(HirarchicalName.length - 1)];
/*      */       
/* 1309 */       if ((reflexionSimpleName.equals(declaredMethod.getName().toString())) && 
/* 1310 */         (cParameterTypes.length == declaredParemeter.size()))
/*      */       {
/* 1312 */         boolean matched = true;
/* 1313 */         for (int p = 0; p < cParameterTypes.length; p++) {
/* 1314 */           Type currentType = ((SingleVariableDeclaration)declaredMethod.parameters().get(p)).getType();
/*      */           
/* 1316 */           while ((currentType.isArrayType()) || (cParameterTypes[p].isArray())) {
/* 1317 */             if ((!currentType.isArrayType()) || (!cParameterTypes[p].isArray())) {
/* 1318 */               matched = false;
/* 1319 */               break;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1324 */           if (currentType.isPrimitiveType()) {
/* 1325 */             if (!cParameterTypes[p].isAssignableFrom(getPrimitiveClass(currentType.toString()))) {
/* 1326 */               matched = false;
/* 1327 */               break;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 1332 */             Class clstmp = null;
/* 1333 */             String clsCompleteName = getQualifiedName(currentType.toString());
/* 1334 */             if (!clsCompleteName.equals("")) {
/*      */               try {
/* 1336 */                 clstmp = magicClassLoader.loadClass(clsCompleteName);
/*      */               } catch (ClassNotFoundException e) {
/* 1338 */                 e.printStackTrace();
/*      */               }
/*      */             } else {
/* 1341 */               clstmp = getClassFromSimpleName(currentType.toString());
/*      */             }
/*      */             
/* 1344 */             if ((clstmp == null) || (cParameterTypes == null) || (cParameterTypes[p] == null) || (!cParameterTypes[p].isAssignableFrom(clstmp))) {
/* 1345 */               matched = false;
/* 1346 */               break;
/*      */             }
/*      */           }
/*      */         }
/* 1350 */         if (matched) {
/* 1351 */           astMethod2ReflexionMethod.put(declaredMethod, reflexionMethod);
/* 1352 */           return reflexionMethod;
/*      */         }
/*      */       }
/*      */     }
/* 1356 */     System.err.println("Mismatch error between the source code and the compiled class at the function: methodDec2MethodRefSimple " + declaredMethod.getName());
/*      */     
/*      */ 
/*      */ 
/* 1360 */     return null;
/*      */   }
/*      */   
/*      */   private static String getQualifiedName(String simpleName) {
/* 1364 */     String qName = "";
/* 1365 */     if (simpleName.equals("Object"))
/* 1366 */       return "java.lang.Object";
/* 1367 */     if (currentClassUnderTest.getClazz().getSimpleName().toString().equals(simpleName)) {
/* 1368 */       return currentClassUnderTest.getClazz().getCanonicalName();
/*      */     }
/* 1370 */     for (Object i : compilationUnit.imports()) {
/* 1371 */       if (((ImportDeclaration)i).getName().toString().endsWith("." + simpleName))
/* 1372 */         qName = ((ImportDeclaration)i).getName().toString();
/*      */     }
/* 1374 */     return qName;
/*      */   }
/*      */   
/*      */   private static Class getClassFromSimpleName(String simpleName)
/*      */   {
/* 1379 */     Set<Class<?>> allClasses = InstanceGenerator.reflections.getSubTypesOf(Object.class);
/* 1380 */     for (Class<?> c : allClasses) {
/* 1381 */       if (c.getSimpleName().toString().equals(simpleName)) {
/* 1382 */         return c;
/*      */       }
/*      */     }
/*      */     
/* 1386 */     Reflections reflections1 = (Reflections)InstanceGenerator.package2Reflections.get("java.lang");
/* 1387 */     File java_classes; if (reflections1 == null) {
/* 1388 */       File java_home = new File(System.getProperty("java.home"));
/* 1389 */       java_classes = new File(java_home.getParent() + "/Classes/classes.jar");
/*      */       
/* 1391 */       URL[] urls = new URL[1];
/* 1392 */       for (int i = 0; i < 1; i++) {
/*      */         try {
/* 1394 */           urls[i] = java_classes.toURL();
/*      */         } catch (MalformedURLException e) {
/* 1396 */           e.printStackTrace();
/*      */         }
/*      */       }
/*      */       
/* 1400 */       reflections1 = new Reflections(new ConfigurationBuilder()
/* 1401 */         .setScanners(new Scanner[] {new SubTypesScanner(false) })
/* 1402 */         .filterInputsBy(new FilterBuilder().includePackage("java.lang"))
/* 1403 */         .setUrls(urls));
/* 1404 */       InstanceGenerator.package2Reflections.put("java.lang", reflections1);
/*      */     }
/* 1406 */     allClasses = reflections1.getSubTypesOf(Object.class);
/*      */     
/* 1408 */     for (Object c : allClasses) {
/* 1409 */       if (((Class)c).getSimpleName().toString().equals(simpleName)) {
/* 1410 */         return (Class)c;
/*      */       }
/*      */     }
/* 1413 */     return null;
/*      */   }
/*      */   
/*      */   private static Method methodDec2MethodRef(MethodDeclaration declaredMethod) throws ClassNotFoundException, IOException {
/* 1417 */     Method reflexionMethod = null;
/* 1418 */     reflexionMethod = (Method)astMethod2ReflexionMethod.get(declaredMethod);
/* 1419 */     if (reflexionMethod != null) {
/* 1420 */       return reflexionMethod;
/*      */     }
/* 1422 */     IMethodBinding imb = declaredMethod.resolveBinding();
/* 1423 */     ITypeBinding[] declaredParemeterType = (ITypeBinding[])null;
/*      */     
/* 1425 */     if (imb != null) {
/* 1426 */       checkCUTCompatibility(declaredMethod);
/* 1427 */       declaredParemeterType = imb.getParameterTypes();
/*      */     }
/*      */     else {
/* 1430 */       return methodDec2MethodRefSimple(declaredMethod);
/*      */     }
/* 1432 */     Method[] refelexionMethods = (Method[])null;
/*      */     try {
/* 1434 */       refelexionMethods = currentClassUnderTest.getClazz().getDeclaredMethods();
/*      */     }
/*      */     catch (ClassFormatError localClassFormatError) {}catch (Exception localException) {}
/*      */     
/* 1438 */     if (refelexionMethods == null) {
/* 1439 */       return null;
/*      */     }
/*      */     
/* 1442 */     List declaredParemeter = declaredMethod.parameters();
/* 1443 */     for (int m = 0; m < refelexionMethods.length; m++)
/*      */     {
/* 1445 */       reflexionMethod = refelexionMethods[m];
/* 1446 */       Class[] cParameterTypes = reflexionMethod.getParameterTypes();
/* 1447 */       String reflexionSimpleName = reflexionMethod.getName().toString();
/* 1448 */       reflexionSimpleName = reflexionSimpleName.replace("$", ".");
/* 1449 */       String[] HirarchicalName = reflexionSimpleName.split("\\.");
/* 1450 */       if (HirarchicalName.length > 0) {
/* 1451 */         reflexionSimpleName = HirarchicalName[(HirarchicalName.length - 1)];
/*      */       }
/* 1453 */       if ((reflexionSimpleName.equals(declaredMethod.getName().toString())) && 
/* 1454 */         (cParameterTypes.length == declaredParemeter.size()))
/*      */       {
/* 1456 */         if ((imb == null) && (cParameterTypes.length > 0)) {
/* 1457 */           return null;
/*      */         }
/* 1459 */         boolean matched = true;
/* 1460 */         for (int p = 0; p < cParameterTypes.length; p++) {
/* 1461 */           ITypeBinding currentType = declaredParemeterType[p];
/* 1462 */           while ((currentType.isArray()) || (cParameterTypes[p].isArray())) {
/* 1463 */             if ((!currentType.isArray()) || (!cParameterTypes[p].isArray())) {
/* 1464 */               matched = false;
/* 1465 */               break;
/*      */             }
/* 1467 */             cParameterTypes[p] = cParameterTypes[p].getComponentType();
/* 1468 */             currentType = currentType.getComponentType();
/*      */           }
/*      */           
/* 1471 */           if (currentType.isPrimitive()) {
/* 1472 */             if (!getPrimitiveClass(currentType.getName()).isAssignableFrom(cParameterTypes[p])) {
/* 1473 */               matched = false;
/* 1474 */               break;
/*      */             }
/*      */           }
/*      */           else {
/* 1478 */             boolean generic = false;
/*      */             
/* 1480 */             String clsCompleteName = currentType.getBinaryName();
/* 1481 */             if ((!currentType.isMember()) && (!currentType.isLocal())) {
/* 1482 */               clsCompleteName = currentType.getQualifiedName();
/*      */             }
/*      */             
/* 1485 */             if (imb.isGenericMethod()) {
/* 1486 */               for (int n = 0; n < imb.getTypeParameters().length; n++)
/* 1487 */                 if (imb.getTypeParameters()[n].getName().equals(currentType.getName())) {
/* 1488 */                   generic = true;
/* 1489 */                   break;
/*      */                 }
/* 1491 */               if (generic) {}
/*      */ 
/*      */ 
/*      */             }
/* 1495 */             else if (imb.getDeclaringClass().isGenericType()) {
/* 1496 */               for (int n = 0; n < imb.getDeclaringClass().getTypeParameters().length; n++)
/* 1497 */                 if (imb.getDeclaringClass().getTypeParameters()[n].getName().equals(currentType.getName())) {
/* 1498 */                   generic = true;
/* 1499 */                   break;
/*      */                 }
/* 1501 */               if (generic) {}
/*      */ 
/*      */             }
/* 1504 */             else if ((currentType.isGenericType()) || (currentType.isParameterizedType())) {
/* 1505 */               clsCompleteName = currentType.getErasure().getBinaryName();
/* 1506 */               Class clstmp = magicClassLoader.loadClass(ClassLoaderUtil.toCanonicalName(clsCompleteName));
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1511 */               if (!cParameterTypes[p].isAssignableFrom(clstmp))
/*      */               {
/*      */ 
/* 1514 */                 matched = false;
/* 1515 */                 break;
/*      */               }
/*      */             }
/*      */             else {
/* 1519 */               if (clsCompleteName.startsWith("[")) {
/* 1520 */                 System.out.println("@@@@@@@@@@@" + currentType.isArray() + "@@@@@@@");
/* 1521 */                 clsCompleteName = clsCompleteName;
/*      */               }
/*      */               
/* 1524 */               Class clstmp = ClassLoaderUtil.getClass(ClassLoaderUtil.toCanonicalName(clsCompleteName));
/*      */               
/* 1526 */               if ((clstmp == null) || (cParameterTypes == null) || (cParameterTypes[p] == null) || (!clstmp.isAssignableFrom(cParameterTypes[p]))) {
/* 1527 */                 matched = false;
/* 1528 */                 break;
/*      */               }
/*      */             }
/*      */           } }
/* 1532 */         if (matched) {
/* 1533 */           astMethod2ReflexionMethod.put(declaredMethod, reflexionMethod);
/* 1534 */           return reflexionMethod;
/*      */         }
/*      */       }
/*      */     }
/* 1538 */     System.err.println("Mismatch error between the source code and the compiled class at the function: methodDec2MethodRef " + declaredMethod.getName());
/*      */     
/*      */ 
/*      */ 
/* 1542 */     return null;
/*      */   }
/*      */   
/*      */   private static Constructor methodDec2ConstructorRefSimple(MethodDeclaration declaredMethod) throws IOException {
/* 1546 */     checkCUTCompatibility(declaredMethod);
/* 1547 */     Constructor reflexionMethod = null;
/* 1548 */     Constructor[] refelexionMethods = currentClassUnderTest.getClazz().getDeclaredConstructors();
/* 1549 */     List declaredParemeter = declaredMethod.parameters();
/*      */     
/* 1551 */     for (int m = 0; m < refelexionMethods.length; m++) {
/* 1552 */       reflexionMethod = refelexionMethods[m];
/* 1553 */       Class[] cParameterTypes = reflexionMethod.getParameterTypes();
/* 1554 */       String reflexionSimpleName = reflexionMethod.getName().toString();
/* 1555 */       reflexionSimpleName = reflexionSimpleName.replace("$", ".");
/* 1556 */       String[] HirarchicalName = reflexionSimpleName.split("\\.");
/* 1557 */       reflexionSimpleName = HirarchicalName[(HirarchicalName.length - 1)];
/* 1558 */       if ((reflexionSimpleName.equals(declaredMethod.getName().toString())) && 
/* 1559 */         (cParameterTypes.length == declaredParemeter.size()))
/*      */       {
/* 1561 */         boolean matched = true;
/* 1562 */         for (int p = 0; p < cParameterTypes.length; p++) {
/* 1563 */           Type currentType = ((SingleVariableDeclaration)declaredMethod.parameters().get(p)).getType();
/* 1564 */           String currentTypeString = currentType.toString();
/* 1565 */           while ((currentType.isArrayType()) || (cParameterTypes[p].isArray())) {
/* 1566 */             if ((!currentType.isArrayType()) || (!cParameterTypes[p].isArray())) {
/* 1567 */               matched = false;
/* 1568 */               break;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1580 */           if (currentType.isPrimitiveType()) {
/* 1581 */             if (!cParameterTypes[p].isAssignableFrom(getPrimitiveClass(currentTypeString))) {
/* 1582 */               matched = false;
/* 1583 */               break;
/*      */             }
/*      */           }
/*      */           else {
/* 1587 */             Class clstmp = null;
/* 1588 */             String clsCompleteName = getQualifiedName(currentType.toString());
/* 1589 */             if (!clsCompleteName.equals("")) {
/*      */               try {
/* 1591 */                 clstmp = magicClassLoader.loadClass(clsCompleteName);
/*      */               } catch (ClassNotFoundException e) {
/* 1593 */                 e.printStackTrace();
/*      */               }
/*      */             } else {
/* 1596 */               clstmp = getClassFromSimpleName(currentType.toString());
/*      */             }
/*      */             
/* 1599 */             if ((clstmp == null) || (cParameterTypes == null) || (cParameterTypes[p] == null) || (!cParameterTypes[p].isAssignableFrom(clstmp))) {
/* 1600 */               matched = false;
/* 1601 */               break;
/*      */             }
/*      */           }
/*      */         }
/* 1605 */         if (matched) {
/* 1606 */           astMethod2ReflexionConstructor.put(declaredMethod, reflexionMethod);
/* 1607 */           return reflexionMethod;
/*      */         }
/*      */       }
/*      */     }
/* 1611 */     System.err.println("Mismatch error between the source code and the compiled class at the function: methodDec2ConstructorRefSimple " + declaredMethod.getName());
/*      */     
/*      */ 
/*      */ 
/* 1615 */     return null;
/*      */   }
/*      */   
/*      */   private static Constructor methodDec2ConstructorRef(MethodDeclaration declaredMethod) throws ClassNotFoundException, IOException
/*      */   {
/* 1620 */     Constructor reflexionConstructor = null;
/* 1621 */     reflexionConstructor = (Constructor)astMethod2ReflexionConstructor.get(declaredMethod);
/* 1622 */     if (reflexionConstructor != null) {
/* 1623 */       return reflexionConstructor;
/*      */     }
/*      */     
/*      */ 
/* 1627 */     List declaredParemeter = declaredMethod.parameters();
/* 1628 */     IMethodBinding imb = declaredMethod.resolveBinding();
/*      */     
/* 1630 */     if (imb == null) {
/* 1631 */       return methodDec2ConstructorRefSimple(declaredMethod);
/*      */     }
/* 1633 */     checkCUTCompatibility(declaredMethod);
/*      */     
/*      */ 
/* 1636 */     Constructor[] refelexionConstructors = currentClassUnderTest.getClazz().getDeclaredConstructors();
/* 1637 */     ITypeBinding[] declaredParemeterType = imb.getParameterTypes();
/*      */     
/*      */ 
/* 1640 */     for (int c = 0; c < refelexionConstructors.length; c++)
/*      */     {
/* 1642 */       reflexionConstructor = refelexionConstructors[c];
/* 1643 */       Class[] cParameterTypes = reflexionConstructor.getParameterTypes();
/*      */       
/* 1645 */       String reflexionSimpleName = reflexionConstructor.getName().toString();
/* 1646 */       reflexionSimpleName = reflexionSimpleName.replace("$", ".");
/* 1647 */       String[] HirarchicalName = reflexionSimpleName.split("\\.");
/* 1648 */       reflexionSimpleName = HirarchicalName[(HirarchicalName.length - 1)];
/*      */       
/* 1650 */       if ((reflexionSimpleName.equals(declaredMethod.getName().toString())) && 
/* 1651 */         (cParameterTypes.length == declaredParemeter.size()))
/*      */       {
/* 1653 */         boolean matched = true;
/* 1654 */         for (int p = 0; p < cParameterTypes.length; p++) {
/* 1655 */           ITypeBinding currentType = declaredParemeterType[p];
/* 1656 */           while ((currentType.isArray()) || (cParameterTypes[p].isArray())) {
/* 1657 */             if ((!currentType.isArray()) || (!cParameterTypes[p].isArray())) {
/* 1658 */               matched = false;
/* 1659 */               break;
/*      */             }
/*      */             
/* 1662 */             cParameterTypes[p] = cParameterTypes[p].getComponentType();
/* 1663 */             currentType = currentType.getComponentType();
/*      */           }
/*      */           
/*      */ 
/* 1667 */           if (currentType.isPrimitive()) {
/* 1668 */             if (!cParameterTypes[p].isAssignableFrom(getPrimitiveClass(currentType.getName()))) {
/* 1669 */               matched = false;
/* 1670 */               break;
/*      */             }
/*      */           }
/*      */           else {
/* 1674 */             boolean generic = false;
/*      */             
/* 1676 */             String clsCompleteName = currentType.getBinaryName();
/*      */             
/*      */ 
/* 1679 */             if (imb.isGenericMethod()) {
/* 1680 */               for (int n = 0; n < imb.getTypeParameters().length; n++)
/* 1681 */                 if (imb.getTypeParameters()[n].getName().equals(currentType.getName())) {
/* 1682 */                   generic = true;
/* 1683 */                   break;
/*      */                 }
/* 1685 */               if (generic) {}
/*      */ 
/*      */ 
/*      */             }
/* 1689 */             else if (imb.getDeclaringClass().isGenericType()) {
/* 1690 */               for (int n = 0; n < imb.getDeclaringClass().getTypeParameters().length; n++)
/* 1691 */                 if (imb.getDeclaringClass().getTypeParameters()[n].getName().equals(currentType.getName())) {
/* 1692 */                   generic = true;
/* 1693 */                   break;
/*      */                 }
/* 1695 */               if (generic) {}
/*      */ 
/*      */             }
/* 1698 */             else if ((currentType.isGenericType()) || (currentType.isParameterizedType())) {
/* 1699 */               clsCompleteName = currentType.getBinaryName();
/* 1700 */               Class clstmp = magicClassLoader.loadClass(ClassLoaderUtil.toCanonicalName(clsCompleteName));
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1705 */               if (cParameterTypes[p].isAssignableFrom(clstmp)) {}
/*      */             }
/*      */             else
/*      */             {
/* 1709 */               Class clstmp = magicClassLoader.loadClass(ClassLoaderUtil.toCanonicalName(clsCompleteName));
/* 1710 */               if ((clstmp == null) || (cParameterTypes == null) || (cParameterTypes[p] == null) || (!cParameterTypes[p].isAssignableFrom(clstmp))) {
/* 1711 */                 matched = false;
/* 1712 */                 break;
/*      */               }
/*      */             }
/*      */           } }
/* 1716 */         if (matched) {
/* 1717 */           astMethod2ReflexionConstructor.put(declaredMethod, reflexionConstructor);
/* 1718 */           return reflexionConstructor;
/*      */         }
/*      */       }
/*      */     }
/* 1722 */     try { System.err.println("Mismatch error between the source code and the compiled class at the function: methodDec2ConstructorRef " + declaredMethod.getName());
/*      */     }
/*      */     catch (Exception localException) {}
/*      */     
/* 1726 */     return null;
/*      */   }
/*      */   
/*      */   public static Path getCurrentPathTarget()
/*      */   {
/* 1731 */     return currentPathTarget;
/*      */   }
/*      */   
/*      */   public static void setCurrentPathTarget(Path path) {
/* 1735 */     currentPathTarget = path;
/*      */   }
/*      */   
/*      */   public static IVariableBinding getVariableBinding(Method method, int paramIndex) {
/* 1739 */     MethodDeclaration cMethod = null;
/* 1740 */     MethodDeclaration[] classMethods = branchCoderAnalyser.getClassNode().getMethods();
/* 1741 */     Class[] declaredParemeterTypes = method.getParameterTypes();
/*      */     
/* 1743 */     for (int i = 0; i < classMethods.length; i++) {
/* 1744 */       cMethod = classMethods[i];
/* 1745 */       List cParametersType = cMethod.parameters();
/*      */       
/* 1747 */       if ((cMethod.getName().toString().equals(method.getName().toString())) && 
/* 1748 */         (cParametersType.size() == declaredParemeterTypes.length))
/*      */       {
/* 1750 */         boolean matched = true;
/* 1751 */         for (int j = 0; j < cParametersType.size(); j++)
/* 1752 */           if (!cParametersType.get(j).getClass().isAssignableFrom(declaredParemeterTypes[j])) {
/* 1753 */             matched = false;
/* 1754 */             break;
/*      */           }
/* 1756 */         if (matched) {
/* 1757 */           return ((VariableDeclaration)cMethod.parameters().get(paramIndex)).resolveBinding();
/*      */         }
/*      */       }
/*      */     }
/* 1761 */     return null;
/*      */   }
/*      */   
/*      */   public static int getInfluence(IVariableBinding iVariableBinding, int branchID)
/*      */   {
/* 1766 */     if ((((Block)branchCoderAnalyser.getBranch2BlockMap().get(Integer.valueOf(branchID))).getProperty("influencers") != null) && 
/* 1767 */       (((Vector)((Block)branchCoderAnalyser.getBranch2BlockMap().get(Integer.valueOf(branchID))).getProperty("influencers")).contains(iVariableBinding))) {
/* 1768 */       return 1;
/*      */     }
/* 1770 */     return 0;
/*      */   }
/*      */   
/*      */   public static final Class<?> getPrimitiveClass(String typeName) {
/* 1774 */     if (typeName.equals("byte"))
/* 1775 */       return Byte.TYPE;
/* 1776 */     if (typeName.equals("short"))
/* 1777 */       return Short.TYPE;
/* 1778 */     if (typeName.equals("int"))
/* 1779 */       return Integer.TYPE;
/* 1780 */     if (typeName.equals("long"))
/* 1781 */       return Long.TYPE;
/* 1782 */     if (typeName.equals("char"))
/* 1783 */       return Character.TYPE;
/* 1784 */     if (typeName.equals("float"))
/* 1785 */       return Float.TYPE;
/* 1786 */     if (typeName.equals("double"))
/* 1787 */       return Double.TYPE;
/* 1788 */     if (typeName.equals("boolean"))
/* 1789 */       return Boolean.TYPE;
/* 1790 */     if (typeName.equals("void"))
/* 1791 */       return Void.TYPE;
/* 1792 */     throw new IllegalArgumentException("Not primitive type : " + typeName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void copyFileUsingStream(String source, String dest)
/*      */     throws IOException
/*      */   {
/* 1806 */     if (source.equals(dest))
/* 1807 */       return;
/* 1808 */     File originalFile = new File(source);
/* 1809 */     if (!originalFile.exists())
/* 1810 */       return;
/* 1811 */     File backupFile = new File(dest);
/* 1812 */     backupFile.getParentFile().mkdirs();
/* 1813 */     ASTEditor.copyFileUsingStream(originalFile, backupFile);
/*      */   }
/*      */   
/*      */   public static void main(String[] args) throws Exception {
/* 1817 */     String usageTxt = "JTExpert -jf Java_unit_under_test  [-cp classpath] [-maxTime search_time_in_seconds] " + System.getProperty("line.separator");
/*      */     
/*      */ 
/*      */ 
/* 1821 */     usageTxt = usageTxt + "-jf \t: use it to set the Java file under test. JTExpert Generates test data suite for this file;" + System.getProperty("line.separator");
/*      */     
/* 1823 */     usageTxt = usageTxt + "-cp \t: use it to set the class path. Paths have to be separated by the system pathseparator (e.g., for linux is :);  " + System.getProperty("line.separator");
/*      */     
/* 1825 */     usageTxt = usageTxt + "-tp \t: use it to set the work directory, wherein the test suite will be saved;  " + System.getProperty("line.separator");
/* 1826 */     usageTxt = usageTxt + "-maxTime : sets the max search time. " + System.getProperty("line.separator");
/* 1827 */     usageTxt = usageTxt + "-o\t: use it to override an existing test data file ; " + System.getProperty("line.separator");
/* 1828 */     usageTxt = usageTxt + "-p\t: use it to disply a progress bars; " + System.getProperty("line.separator");
/* 1829 */     usageTxt = usageTxt + "-s \t: use to show messages and errors thrown by the class under test; " + System.getProperty("line.separator");
/* 1830 */     usageTxt = usageTxt + "-seed: seed to use for the random number generator. " + System.getProperty("line.separator");
/* 1831 */     usageTxt = usageTxt + "-E\t: use it to activate the Exception-Otriented Test-Data Generation search. " + System.getProperty("line.separator");

/* 1904 */     int i = 0;
/*      */     
/* 1906 */     while ((i < args.length) && (args[i].startsWith("-"))) {
/* 1907 */       String arg = args[(i++)];
/* 1908 */       if (arg.equals("-sp"))
/* 1909 */         if (i < args.length) { srcPath0 = args[(i++)];
/*      */         } else {
/* 1911 */           System.err.println(arg + " requires a value");
/* 1912 */           System.exit(-1);
/*      */         }
/* 1914 */       if (arg.equals("-jc"))
/* 1915 */         if (i < args.length) { className = args[(i++)];
/*      */         } else {
/* 1917 */           System.err.println(arg + " requires a value");
/* 1918 */           System.exit(-1);
/*      */         }
/* 1920 */       if (arg.equals("-jf"))
/* 1921 */         if (i < args.length) { javaFileName = args[(i++)];
/*      */         } else {
/* 1923 */           System.err.println(arg + " requires a value");
/* 1924 */           System.exit(-1);
/*      */         }
/* 1926 */       if (arg.equals("-jd"))
/* 1927 */         if (i < args.length) { javaDirectoryName = args[(i++)];
/*      */         } else {
/* 1929 */           System.err.println(arg + " requires a value");
/* 1930 */           System.exit(-1);
/*      */         }
/* 1932 */       if (arg.equals("-cp"))
/* 1933 */         if (i < args.length) { classPath0 = args[(i++)];
/*      */         } else {
/* 1935 */           System.err.println(arg + " requires a value");
/* 1936 */           System.exit(-1);
/*      */         }
/* 1938 */       if (arg.equals("-tp"))
/* 1939 */         if (i < args.length) { jteOutputPath = args[(i++)];
/*      */         } else {
/* 1941 */           System.err.println(arg + " requires a value");
/* 1942 */           System.exit(-1);
/*      */         }
/* 1944 */       if (arg.equals("-gp"))
/* 1945 */         if (i < args.length) { gaParametersFile = args[(i++)];
/*      */         } else {
/* 1947 */           System.err.println(arg + " requires a value");
/* 1948 */           System.exit(-1);
/*      */         }
/* 1950 */       if (arg.equals("-im"))
/* 1951 */         if (i < args.length) { interfacesFile = args[(i++)];
/*      */         } else {
/* 1953 */           System.err.println(arg + " requires a value");
/* 1954 */           System.exit(-1);
/*      */         }
/* 1956 */       if (arg.equals("-ppp"))
/* 1957 */         if (i < args.length) { projectPackagesPrefix = args[(i++)];
/*      */         } else {
/* 1959 */           System.err.println(arg + " requires a value");
/* 1960 */           System.exit(-1);
/*      */         }
/* 1962 */       if (arg.equals("-hn"))
/* 1963 */         if (i < args.length) { heuristicName = args[(i++)];
/*      */         } else {
/* 1965 */           System.err.println(arg + " requires a value");
/* 1966 */           System.exit(-1);
/*      */         }
/* 1968 */       if (arg.equals("-seed")) {
/* 1969 */         String seedstr = "";
/* 1970 */         if (i < args.length) {
/* 1971 */           seedstr = args[(i++)];
/*      */         } else {
/* 1973 */           System.err.println(arg + " requires a value " + seedstr);
/* 1974 */           System.exit(-1);
/*      */         }
/*      */         
/* 1977 */         if (seedstr.length() > 9) seedstr = seedstr.substring(0, 9);
/* 1978 */         seed = Integer.parseInt(seedstr);
/*      */       }
/*      */       
/* 1981 */       if (arg.equals("-maxEval"))
/* 1982 */         if (i < args.length) { maxEvaluations = Integer.parseInt(args[(i++)]);
/*      */         } else {
/* 1984 */           System.err.println(arg + " requires a value ");
/* 1985 */           System.exit(-1);
/*      */         }
/* 1987 */       if (arg.equals("-maxTime")) {
/* 1988 */         if (i < args.length) { maxTime = Integer.parseInt(args[(i++)]);
/*      */         } else {
/* 1990 */           System.err.println(arg + " requires a value ");
/* 1991 */           System.exit(-1);
/*      */         }
/*      */       }
/* 1994 */       if (arg.equals("-o")) overrideExistTestCase = true;
/* 1995 */       if (arg.equals("-i")) instrument = true;
/* 1996 */       if (arg.equals("-p")) printProgress = true;
/* 1997 */       if (arg.equals("-s")) showErrors = true;
/* 1998 */       if (arg.equals("-E")) { ExceptionsOriented = true;
/*      */       }
/*      */     }
/*      */     

/*      */ 
/* 2016 */     boolean parssingErreur = false;
/* 2017 */     if (((srcPath0 == null) || (srcPath0.equals(""))) && 
/* 2018 */       ((javaFileName == null) || (javaFileName.equals(""))) && (
/* 2019 */       (javaDirectoryName == null) || (javaDirectoryName.equals("")))) {
/* 2020 */       parssingErreur = true;
/* 2021 */       System.err.println("A source path directory is required");
/*      */     }
/* 2023 */     if (((className == null) || (className.equals(""))) && 
/* 2024 */       ((javaFileName == null) || (javaFileName.equals(""))) && (
/* 2025 */       (javaDirectoryName == null) || (javaDirectoryName.equals("")))) {
/* 2026 */       parssingErreur = true;
/* 2027 */       System.err.println("A class name is required");
/*      */     }
/* 2029 */     if (parssingErreur) {
/* 2030 */       System.err.println("Usage: " + System.getProperty("line.separator") + usageTxt);
/* 2031 */       System.exit(-1);
/*      */     }
/*      */     
/* 2034 */     if ((gaParametersFile == null) || (gaParametersFile.equals(""))) {
/* 2035 */       File relativeJTEPath = new File("");
/* 2036 */       String absolutJTEPath = relativeJTEPath.getAbsolutePath();
/* 2037 */       gaParametersFile = absolutJTEPath + File.separator + "testing.params";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2047 */     if ((jteOutputPath == null) || (jteOutputPath.equals(""))) {
/* 2048 */       File relativeJTEPath = new File("");
/* 2049 */       String absolutJTEPath = relativeJTEPath.getAbsolutePath();
/* 2050 */       jteOutputPath = absolutJTEPath + File.separator + "jteOutput";
/*      */     }
/*      */     
/* 2053 */     testCasesPath = jteOutputPath + File.separator + "testcases";
/*      */     
/*      */ 
/*      */ 
/* 2057 */     binPath = jteOutputPath + File.separator + "bin";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2063 */     srcPath = jteOutputPath + File.separator + "src";
/*      */     
/*      */ 
/*      */ 
/* 2067 */     classPath = classPath0.split(File.pathSeparator);
/*      */     
/*      */ 
/*      */ 

/*      */ 
/* 2081 */     if ((javaFileName != null) && (!javaFileName.equals(""))) {
/* 2082 */       generateTestData4JavaSrcFile();
/* 2083 */     } else if ((className != null) && (!className.equals("")))
/*      */     {
/* 2085 */       String[] clsNameHierarchy = className.split("\\.");
/* 2086 */       srcFileName = clsNameHierarchy[(clsNameHierarchy.length - 1)];
/*      */       
/* 2088 */       packageName = className.substring(0, className.lastIndexOf("."));
/* 2089 */       subPath = packageName.replace(".", File.separator);
/*      */       
/* 2091 */       javaFileName = srcPath0 + File.separator + subPath + File.separator + srcFileName + ".java";
/* 2092 */       copyFileUsingStream(javaFileName, srcPath + File.separator + subPath + File.separator + srcFileName + ".java");
/* 2093 */       javaFileName = srcPath + File.separator + subPath + File.separator + srcFileName + ".java";
/*      */       
/* 2095 */       ASTBuilder ASTRoot = new ASTBuilder(classPath0, srcPath, javaFileName);
/* 2096 */       compilationUnit = ASTRoot.getASTRoot();
/*      */       
/* 2098 */       if (Initialize())
/* 2099 */         generateTestData();
/*      */     } else {
/* 2101 */       generateTestData4JavaSrcPath(javaDirectoryName);
/*      */     }
/* 2103 */     enableSystemExitCall();
/*      */   }
/*      */   
/*      */   private static void generateTestData4JavaSrcPath(String javaSrcPath) throws Exception {
/* 2107 */     File srcDirectory = new File(javaSrcPath);
/* 2108 */     File[] files = srcDirectory.listFiles();
/* 2109 */     for (int f = 0; f < files.length; f++) {
/* 2110 */       if ((files[f].isFile()) && (files[f].getName().endsWith(".java"))) {
/* 2111 */         javaFileName = files[f].toString();
/* 2112 */         generateTestData4JavaSrcFile();
/*      */       }
/* 2114 */       if (files[f].isDirectory()) {
/* 2115 */         generateTestData4JavaSrcPath(files[f].toString());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void generateTestData4JavaSrcFile()
/*      */     throws Exception
/*      */   {
/* 2124 */     ActualstartTime = System.currentTimeMillis();
/* 2125 */     srcFileName = javaFileName.substring(javaFileName.lastIndexOf(File.separator) + 1, javaFileName.length());
/* 2126 */     srcFileName = srcFileName.substring(0, srcFileName.length() - 5);
/*      */     
/*      */ 
/* 2129 */     ASTBuilder ASTRoot = new ASTBuilder(null, null, javaFileName);
/* 2130 */     compilationUnit = ASTRoot.getASTRoot();
/*      */     
/* 2132 */     if (compilationUnit.getPackage() != null) {
/* 2133 */       packageName = compilationUnit.getPackage().getName().toString();
/* 2134 */       if ((projectPackagesPrefix == null) || (projectPackagesPrefix == ""))
/* 2135 */         projectPackagesPrefix = packageName;
/* 2136 */       if (!packageName.contains(projectPackagesPrefix))
/* 2137 */         projectPackagesPrefix = packageName;
/*      */     } else {
/* 2139 */       packageName = "";
/*      */     }
/* 2141 */     subPath = packageName.replace(".", File.separator);
/*      */     
/* 2143 */     className = srcFileName;
/* 2144 */     if (!packageName.equals("")) {
/* 2145 */       className = packageName + "." + srcFileName;
/*      */     }
/*      */     
/* 2148 */     binPath = binPath + File.separator + className;
/* 2149 */     File fbin = new File(binPath);
/* 2150 */     fbin.mkdirs();
/* 2151 */     classPath0 = binPath + File.separator + File.pathSeparator + classPath0;
/* 2152 */     classPath = classPath0.split(File.pathSeparator);
/* 2153 */     loadClassPath();
/*      */     
/* 2155 */     String fileTC = testCasesPath + File.separator + subPath + File.separator + srcFileName + "JTETestCases" + ".java";
/* 2156 */     File f = new File(fileTC);
/* 2157 */     if ((!overrideExistTestCase) && (f.exists())) {
/* 2158 */       System.out.println("A test case file already exists in the distination diectory " + System.getProperty("line.separator") + "(" + testCasesPath + File.separator + subPath + File.separator + srcFileName + "JTETestCases" + ".java" + "). " + System.getProperty("line.separator") + "To override it change the option overrideExistTestCase into true");
/* 2159 */       return;
/*      */     }
/*      */     
/* 2162 */     copyFileUsingStream(javaFileName, srcPath + File.separator + subPath + File.separator + srcFileName + ".java");
/* 2163 */     javaFileName = srcPath + File.separator + subPath + File.separator + srcFileName + ".java";
/*      */     
/*      */ 
/*      */ 
/* 2167 */     ASTRoot = new ASTBuilder(classPath0, srcPath, javaFileName);
/* 2168 */     compilationUnit = ASTRoot.getASTRoot();
/*      */     
/* 2170 */     if (Initialize()) {
/* 2171 */       forbidSystemExitCall();
/* 2172 */       redirectStdOutput();
/* 2173 */       generateTestData();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void loadClassPath()
/*      */     throws Exception
/*      */   {
/* 2181 */     List<URL> pathList = new ArrayList();
/*      */     String[] arrayOfString;
/* 2183 */     int j = (arrayOfString = classPath).length; for (int i = 0; i < j; i++) { String p = arrayOfString[i];
/* 2184 */       pathList.add(new File(p).toURL());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2192 */     pathList.addAll(classPathList);
/*      */     
/* 2194 */     URL[] urls = new URL[pathList.size()];
/* 2195 */     urls = (URL[])pathList.toArray(urls);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2201 */     magicClassLoader = new URLClassLoader(urls);
/* 2202 */     Thread.currentThread().setContextClassLoader(magicClassLoader);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static List<URL> getJarListInLibDir(File lib)
/*      */     throws Exception
/*      */   {
/* 2212 */     List<URL> pathList = new ArrayList();
/*      */     
/* 2214 */     File[] files = lib.listFiles();
/* 2215 */     for (int i = 0; i < files.length; i++) {
/* 2216 */       if (files[i].getName().endsWith(".jar")) {
/* 2217 */         pathList.add(files[i].toURL());
/* 2218 */       } else if (files[i].isDirectory()) {
/* 2219 */         pathList.addAll(getJarListInLibDir(files[i]));
/*      */       }
/*      */     }
/* 2222 */     return pathList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setClassUnderTest(Class cls)
/*      */     throws IOException
/*      */   {
/* 2242 */     while ((cls.getDeclaringClass() != null) && (
/* 2243 */       (Modifier.isPrivate(cls.getModifiers())) || (Modifier.isProtected(cls.getModifiers())))) {
/* 2244 */       cls = cls.getDeclaringClass();
/*      */     }
/*      */     
/* 2247 */     currentClassUnderTest = (ClassUnderTest)classesUnderTest.get(cls.getCanonicalName());
/* 2248 */     if (currentClassUnderTest == null) {
/* 2249 */       currentClassUnderTest = new ClassUnderTest(cls);
/*      */       
/* 2251 */       classesUnderTest.put(cls.getCanonicalName(), currentClassUnderTest);
/* 2252 */       ClassUnderTest savecut = currentClassUnderTest;
/* 2253 */       currentClassUnderTest.prepareDataMembers();
/* 2254 */       currentClassUnderTest = savecut;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void redirectStdOutput()
/*      */   {
/* 2261 */     LogManager logManager = LogManager.getLogManager();
/* 2262 */     logManager.reset();
/*      */     
/*      */ 
/*      */ 
/* 2266 */     Handler fileHandler = null;
/*      */     try
/*      */     {
/* 2269 */       File logsDir = new File(jteOutputPath);
/* 2270 */       String dir = logsDir.getAbsolutePath() + File.separator + "logs" + File.separator + className;
/* 2271 */       logsDir = new File(dir);
/* 2272 */       logsDir.mkdirs();
/* 2273 */       fileHandler = new FileHandler(logsDir.getAbsolutePath() + File.separator + "jte%u.log", 10000, 3, true);
/*      */     }
/*      */     catch (SecurityException e) {
/* 2276 */       e.printStackTrace();
/*      */     }
/*      */     catch (IOException e) {
/* 2279 */       e.printStackTrace();
/*      */     }
/*      */     
/* 2282 */     fileHandler.setFormatter(new SimpleFormatter());
/* 2283 */     Logger.getLogger("").addHandler(fileHandler);
/*      */     
/*      */ 
/* 2286 */     stdout = System.out;
/* 2287 */     stderr = System.err;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2293 */     Logger logger = Logger.getLogger("stdout");
/* 2294 */     LoggingOutputStream los = new LoggingOutputStream(logger, StdOutErrLevel.STDOUT);
/* 2295 */     if (!showErrors) {
/* 2296 */       System.setOut(new PrintStream(los, true));
/*      */     }
/* 2298 */     logger = Logger.getLogger("stderr");
/* 2299 */     los = new LoggingOutputStream(logger, StdOutErrLevel.STDERR);
/* 2300 */     if (!showErrors) {
/* 2301 */       System.setErr(new PrintStream(los, true));
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\JTE.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */