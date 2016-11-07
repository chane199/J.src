/*     */ package csbst.analysis;
/*     */ 
/*     */ import csbst.testing.BranchDistance;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.core.dom.DoStatement;
/*     */ import org.eclipse.jdt.core.dom.Expression;
/*     */ import org.eclipse.jdt.core.dom.ExpressionStatement;
/*     */ import org.eclipse.jdt.core.dom.FieldDeclaration;
/*     */ import org.eclipse.jdt.core.dom.ForStatement;
/*     */ import org.eclipse.jdt.core.dom.IMethodBinding;
/*     */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*     */ import org.eclipse.jdt.core.dom.IfStatement;
/*     */ import org.eclipse.jdt.core.dom.InfixExpression;
/*     */ import org.eclipse.jdt.core.dom.InfixExpression.Operator;
/*     */ import org.eclipse.jdt.core.dom.Initializer;
/*     */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.NumberLiteral;
/*     */ import org.eclipse.jdt.core.dom.ParenthesizedExpression;
/*     */ import org.eclipse.jdt.core.dom.PostfixExpression;
/*     */ import org.eclipse.jdt.core.dom.PostfixExpression.Operator;
/*     */ import org.eclipse.jdt.core.dom.PrefixExpression;
/*     */ import org.eclipse.jdt.core.dom.PrefixExpression.Operator;
/*     */ import org.eclipse.jdt.core.dom.PrimitiveType;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.SwitchCase;
/*     */ import org.eclipse.jdt.core.dom.SwitchStatement;
/*     */ import org.eclipse.jdt.core.dom.Type;
/*     */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
/*     */ import org.eclipse.jdt.core.dom.WhileStatement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstrumentorOld
/*     */   extends ASTVisitor
/*     */ {
/*     */   private AST ast;
/*     */   private CompilationUnit compilationUnit;
/*     */   private Integer cBranch;
/*     */   private int currentBranch;
/*     */   private int branchesCounter;
/*     */   private TypeDeclaration classNode;
/*     */   private int methodNodeIndex;
/*  63 */   private Map<Integer, Integer> branchDominator = new HashMap();
/*  64 */   private Stack<Integer> cDominator = new Stack();
/*  65 */   private Map<Integer, ArrayList<Double>> branchDC = new HashMap();
/*  66 */   private Map<Integer, ASTNode> branch2ASTNode = new HashMap();
/*  67 */   private Map<Integer, Integer> branch2Method = new HashMap();
/*  68 */   private List<Set<IVariableBinding>> dataMembersIfluencePropagators = new ArrayList();
/*  69 */   private List<Set<IVariableBinding>> parametersIfluencePropagators = new ArrayList();
/*     */   
/*     */   public InstrumentorOld(CompilationUnit cU)
/*     */   {
/*  73 */     this.compilationUnit = cU;
/*  74 */     this.ast = cU.getAST();
/*  75 */     this.cBranch = new Integer(-1);
/*  76 */     this.currentBranch = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addExecutionPathTracer(Block b, int index, String iteration, int oldBranch)
/*     */   {
/*  88 */     if ((b.getParent() instanceof Initializer))
/*     */       return;
/*     */     int branch;
/*     */     int branch;
/*  92 */     if (((b.getParent() instanceof IfStatement)) && 
/*  93 */       (((IfStatement)b.getParent()).getElseStatement().equals(b))) {
/*  94 */       branch = ((Integer)b.getParent().getProperty("BranchNumber")).intValue(); } else { int branch;
/*  95 */       if (oldBranch > -1) {
/*  96 */         branch = oldBranch;
/*     */       } else {
/*  98 */         this.cBranch = Integer.valueOf(this.cBranch.intValue() + 1);
/*  99 */         branch = this.cBranch.intValue();
/*     */       }
/*     */     }
/* 102 */     MethodInvocation trace = this.ast.newMethodInvocation();
/* 103 */     trace.setName(this.ast.newSimpleName("maintainPathTrace"));
/*     */     
/* 105 */     NumberLiteral p1 = this.ast.newNumberLiteral(branch);
/* 106 */     trace.arguments().add(p1);
/* 107 */     if (((b.getParent() instanceof ForStatement)) || 
/* 108 */       ((b.getParent() instanceof DoStatement)) || 
/* 109 */       ((b.getParent() instanceof WhileStatement))) {
/* 110 */       trace.arguments().add(this.ast.newSimpleName("i" + branch));
/*     */ 
/*     */ 
/*     */     }
/* 114 */     else if (((b.getParent() instanceof IfStatement)) && 
/* 115 */       (((IfStatement)b.getParent()).getElseStatement().equals(b))) {
/* 116 */       NumberLiteral p2 = this.ast.newNumberLiteral("-1");
/* 117 */       trace.arguments().add(p2);
/*     */     }
/*     */     else {
/* 120 */       trace.arguments().add((Expression)ASTNode.copySubtree(this.ast, String2Expression.getExpression(iteration)));
/*     */     }
/*     */     
/* 123 */     ExpressionStatement eStatement2 = this.ast.newExpressionStatement(trace);
/* 124 */     b.statements().add(index, eStatement2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int getMethodIndexFromSignature(String methodSignature)
/*     */   {
/* 132 */     for (int i = 0; i < this.classNode.getMethods().length; i++)
/* 133 */       if (this.classNode.getMethods()[i].resolveBinding().getMethodDeclaration().toString().equals(methodSignature))
/* 134 */         return i;
/* 135 */     return -1;
/*     */   }
/*     */   
/*     */   private int getDataMemberIndexFromSignature(String DataMemberSignature) {
/* 139 */     int index = 0;
/* 140 */     for (int i = 0; i < this.classNode.getFields().length; i++) {
/* 141 */       String modifiers = this.classNode.getFields()[i].modifiers().toString();
/* 142 */       modifiers = modifiers.replace(",", "");
/* 143 */       modifiers = modifiers.replace("[", "");
/* 144 */       modifiers = modifiers.replace("]", "");
/* 145 */       String type = this.classNode.getFields()[i].getType().toString();
/* 146 */       for (int j = 0; j < this.classNode.getFields()[i].fragments().size(); index++) {
/* 147 */         int equalIndex = this.classNode.getFields()[i].fragments().get(j).toString().indexOf("=");
/* 148 */         if (equalIndex < 0) equalIndex = this.classNode.getFields()[i].fragments().get(j).toString().length();
/* 149 */         String name = this.classNode.getFields()[i].fragments().get(j).toString().substring(0, equalIndex);
/*     */         
/* 151 */         String cSignature = modifiers + " " + type + " " + name;
/* 152 */         if (cSignature.equals(DataMemberSignature)) {
/* 153 */           return index;
/*     */         }
/* 146 */         j++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */     return -1;
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
/*     */   public boolean visit(TypeDeclaration node)
/*     */   {
/* 209 */     this.classNode = node;
/* 210 */     this.branchesCounter = 0;
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
/* 222 */     return true;
/*     */   }
/*     */   
/*     */   private void defineNewBranch(ASTNode node) {
/* 226 */     this.branchesCounter += 1;
/* 227 */     node.setProperty("numberParentBranch", Integer.valueOf(this.currentBranch));
/* 228 */     this.currentBranch = this.branchesCounter;
/*     */   }
/*     */   
/*     */   public boolean visit(MethodDeclaration node)
/*     */   {
/* 233 */     node.resolveBinding().getMethodDeclaration().toString();
/* 234 */     this.methodNodeIndex = getMethodIndexFromSignature(node.resolveBinding().getMethodDeclaration().toString());
/* 235 */     this.cDominator.clear();
/* 236 */     defineNewBranch(node);
/* 237 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(MethodDeclaration node)
/*     */   {
/* 242 */     this.currentBranch = ((Integer)node.getProperty("numberParentBranch")).intValue();
/*     */   }
/*     */   
/*     */   private void computeDifficultyCoefficient(Integer branch, Expression exp, List decomposedExpression)
/*     */   {
/*     */     InfixExpressionDecomposer iExpDecomposer;
/*     */     InfixExpressionDecomposer iExpDecomposer;
/* 249 */     if ((decomposedExpression.get(0) != null) && (((Boolean)decomposedExpression.get(0)).booleanValue())) {
/* 250 */       iExpDecomposer = new InfixExpressionDecomposer(InfixExpression.Operator.CONDITIONAL_AND);
/*     */     } else {
/* 252 */       iExpDecomposer = new InfixExpressionDecomposer(InfixExpression.Operator.CONDITIONAL_OR);
/*     */     }
/* 254 */     exp.accept(iExpDecomposer);
/*     */     
/* 256 */     decomposedExpression.addAll(iExpDecomposer.getExpressionsList());
/*     */     
/*     */ 
/* 259 */     ArrayList dCs = new ArrayList();
/* 260 */     for (InfixExpression e : iExpDecomposer.getExpressionsList())
/*     */       try {
/* 262 */         double DC = DifficultyCoefficient.getDifficultyCoefficient(e);
/* 263 */         dCs.add(new Double(DC));
/*     */       } catch (Exception e1) {
/* 265 */         e1.printStackTrace();
/*     */       }
/* 267 */     this.branchDC.put(this.cBranch, dCs);
/*     */   }
/*     */   
/*     */   private void branchDistanceSimpleInstrumentation(Block bInstrumentation, int index, Integer branch, String iteration, boolean forNegation) throws Exception {
/* 271 */     Expression expression = (Expression)((ASTNode)this.branch2ASTNode.get(branch)).getProperty("Expression");
/*     */     
/* 273 */     MethodInvocation gBD = this.ast.newMethodInvocation();
/* 274 */     gBD.setName(this.ast.newSimpleName("maintainBranchDistance"));
/*     */     Expression exp;
/*     */     Expression exp;
/* 277 */     if (!forNegation) {
/* 278 */       exp = (Expression)ASTNode.copySubtree(this.ast, String2Expression.getExpression(BranchDistance.expressionFormater(expression)));
/*     */     }
/*     */     else {
/* 281 */       PrefixExpression notExpression = this.ast.newPrefixExpression();
/* 282 */       notExpression.setOperator(PrefixExpression.Operator.NOT);
/* 283 */       Expression exp2 = (Expression)ASTNode.copySubtree(this.ast, expression);
/* 284 */       ParenthesizedExpression pExp = this.ast.newParenthesizedExpression();
/* 285 */       pExp.setExpression(exp2);
/* 286 */       notExpression.setOperand(pExp);
/* 287 */       exp = (Expression)ASTNode.copySubtree(this.ast, String2Expression.getExpression(BranchDistance.expressionFormater(notExpression)));
/*     */     }
/* 289 */     gBD.arguments().add(exp);
/* 290 */     ExpressionStatement gBDStatement = this.ast.newExpressionStatement(gBD);
/*     */     
/*     */ 
/* 293 */     MethodInvocation isPartOfTestTarget = this.ast.newMethodInvocation();
/* 294 */     isPartOfTestTarget.setName(this.ast.newSimpleName("isPartOfTestTarget"));
/* 295 */     isPartOfTestTarget.arguments().add(this.ast.newNumberLiteral(branch));
/* 296 */     if ((iteration == "0") || (iteration == "1")) {
/* 297 */       isPartOfTestTarget.arguments().add(this.ast.newNumberLiteral(iteration));
/*     */     } else {
/* 299 */       isPartOfTestTarget.arguments().add((Expression)ASTNode.copySubtree(this.ast, String2Expression.getExpression(iteration)));
/*     */     }
/* 301 */     IfStatement ifStatement = this.ast.newIfStatement();
/* 302 */     ifStatement.setExpression(isPartOfTestTarget);
/* 303 */     ifStatement.setThenStatement(gBDStatement);
/*     */     
/*     */ 
/* 306 */     bInstrumentation.statements().add(index, ifStatement);
/*     */   }
/*     */   
/*     */   private void branchDistanceInstrumentation(Block bInstrumentation, int index, Integer branch, String iteration, boolean forNegation) throws Exception
/*     */   {
/* 311 */     Expression expression = (Expression)((ASTNode)this.branch2ASTNode.get(branch)).getProperty("Expression");
/*     */     
/*     */ 
/* 314 */     PrefixExpression notExpression = this.ast.newPrefixExpression();
/* 315 */     notExpression.setOperator(PrefixExpression.Operator.NOT);
/* 316 */     Expression exp2 = (Expression)ASTNode.copySubtree(this.ast, expression);
/* 317 */     ParenthesizedExpression pExp = this.ast.newParenthesizedExpression();
/* 318 */     pExp.setExpression(exp2);
/* 319 */     notExpression.setOperand(pExp);
/*     */     
/*     */ 
/* 322 */     MethodInvocation gBD = this.ast.newMethodInvocation();
/* 323 */     gBD.setName(this.ast.newSimpleName("maintainBranchDistance"));
/*     */     Expression exp;
/*     */     Expression exp;
/* 326 */     if (!forNegation) {
/* 327 */       exp = (Expression)ASTNode.copySubtree(this.ast, String2Expression.getExpression(BranchDistance.expressionFormater(expression)));
/*     */     } else {
/* 329 */       exp = (Expression)ASTNode.copySubtree(this.ast, String2Expression.getExpression(BranchDistance.expressionFormater(notExpression)));
/*     */     }
/* 331 */     gBD.arguments().add(exp);
/*     */     
/* 333 */     ExpressionStatement gBDStatement = this.ast.newExpressionStatement(gBD);
/*     */     
/*     */ 
/* 336 */     MethodInvocation methodInvocation = this.ast.newMethodInvocation();
/* 337 */     methodInvocation.setName(this.ast.newSimpleName("isPartOfTestTarget"));
/* 338 */     methodInvocation.arguments().add(this.ast.newNumberLiteral(branch));
/* 339 */     if ((iteration == "0") || (iteration == "1")) {
/* 340 */       methodInvocation.arguments().add(this.ast.newNumberLiteral(iteration));
/*     */     } else {
/* 342 */       methodInvocation.arguments().add((Expression)ASTNode.copySubtree(this.ast, String2Expression.getExpression(iteration)));
/*     */     }
/*     */     
/* 345 */     InfixExpression ifExpression = this.ast.newInfixExpression();
/* 346 */     ifExpression.setOperator(InfixExpression.Operator.CONDITIONAL_AND);
/* 347 */     if (!forNegation) {
/* 348 */       ifExpression.setLeftOperand(notExpression);
/*     */     } else {
/* 350 */       ifExpression.setLeftOperand((Expression)ASTNode.copySubtree(this.ast, pExp));
/*     */     }
/* 352 */     ifExpression.setRightOperand(methodInvocation);
/* 353 */     IfStatement ifStatement = this.ast.newIfStatement();
/* 354 */     ifStatement.setExpression(ifExpression);
/*     */     
/* 356 */     ifStatement.setThenStatement(gBDStatement);
/*     */     
/*     */ 
/* 359 */     bInstrumentation.statements().add(index, ifStatement);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean visit(Block node)
/*     */   {
/* 366 */     if (node.getProperty("Source") == "Instrumentation") {
/* 367 */       return true;
/*     */     }
/* 369 */     addExecutionPathTracer(node, 0, "1", -1);
/*     */     
/*     */ 
/* 372 */     Integer tmpDominator = new Integer(-1);
/* 373 */     if (!this.cDominator.empty())
/* 374 */       tmpDominator = (Integer)this.cDominator.peek();
/* 375 */     this.branchDominator.put(new Integer(this.cBranch.intValue()), new Integer(tmpDominator.intValue()));
/* 376 */     this.cDominator.push(new Integer(this.cBranch.intValue()));
/* 377 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(Block node)
/*     */   {
/* 382 */     if (node.getProperty("Source") == "Instrumentation") {
/* 383 */       return;
/*     */     }
/* 385 */     this.cDominator.pop();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean visit(IfStatement node)
/*     */   {
/* 427 */     if (node.getExpression().toString().contains("isPartOfTestTarget")) {
/* 428 */       return false;
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
/* 453 */     if (!(node.getThenStatement() instanceof Block)) {
/* 454 */       Block b = this.ast.newBlock();
/* 455 */       Statement body = node.getThenStatement();
/* 456 */       node.setThenStatement(b);
/* 457 */       body.delete();
/* 458 */       b.statements().add(body);
/*     */     }
/* 460 */     if (!(node.getElseStatement() instanceof Block)) {
/* 461 */       Block b = this.ast.newBlock();
/* 462 */       Statement body = node.getElseStatement();
/* 463 */       node.setElseStatement(b);
/* 464 */       if (body != null) {
/* 465 */         body.delete();
/* 466 */         b.statements().add(body);
/*     */       }
/*     */     }
/*     */     
/* 470 */     node.setProperty("BranchNumber", Integer.valueOf(this.cBranch.intValue() + 1));
/* 471 */     node.setProperty("Expression", node.getExpression());
/* 472 */     this.branch2ASTNode.put(Integer.valueOf(this.cBranch.intValue() + 1), node);
/* 473 */     this.branch2Method.put(Integer.valueOf(this.cBranch.intValue() + 1), Integer.valueOf(this.methodNodeIndex));
/* 474 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(IfStatement node)
/*     */   {
/* 479 */     if (node.getExpression().toString().contains("isPartOfTestTarget")) {
/* 480 */       return;
/*     */     }
/*     */     try {
/* 483 */       branchDistanceSimpleInstrumentation((Block)node.getThenStatement(), 0, (Integer)node.getProperty("BranchNumber"), "-1", true);
/* 484 */       branchDistanceSimpleInstrumentation((Block)node.getElseStatement(), 0, (Integer)node.getProperty("BranchNumber"), "1", false);
/*     */     }
/*     */     catch (Exception e) {
/* 487 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean visit(SwitchCase node)
/*     */   {
/* 493 */     SwitchStatement parent = (SwitchStatement)node.getParent();
/* 494 */     int index = parent.statements().indexOf(node);
/* 495 */     Statement nextStatement = (Statement)parent.statements().get(index + 1);
/*     */     
/*     */ 
/* 498 */     if (!(nextStatement instanceof Block))
/*     */     {
/* 500 */       Block b = this.ast.newBlock();
/* 501 */       Statement body = nextStatement;
/* 502 */       parent.statements().add(index + 1, b);
/* 503 */       body.delete();
/* 504 */       b.statements().add(body);
/*     */     }
/*     */     
/* 507 */     String txtExpression = "";
/* 508 */     if (node.getExpression() != null) {
/* 509 */       txtExpression = parent.getExpression() + "==" + node.getExpression();
/*     */     } else {
/* 511 */       for (int i = 0; i < parent.statements().size(); i++) {
/* 512 */         if (((parent.statements().get(i) instanceof SwitchCase)) && (!parent.statements().get(i).equals(node))) {
/* 513 */           if (txtExpression != "") txtExpression = txtExpression + " && ";
/* 514 */           txtExpression = txtExpression + parent.getExpression() + "!=" + ((SwitchCase)parent.statements().get(i)).getExpression();
/*     */         }
/*     */       }
/*     */     }
/* 518 */     node.setProperty("BranchNumber", Integer.valueOf(this.cBranch.intValue() + 1));
/* 519 */     node.setProperty("Expression", (Expression)ASTNode.copySubtree(this.ast, String2Expression.getExpression(txtExpression)));
/* 520 */     this.branch2ASTNode.put(Integer.valueOf(this.cBranch.intValue() + 1), node);
/* 521 */     this.branch2Method.put(Integer.valueOf(this.cBranch.intValue() + 1), Integer.valueOf(this.methodNodeIndex));
/* 522 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(SwitchCase node)
/*     */   {
/*     */     try
/*     */     {
/* 529 */       ASTNode switchStatement = node.getParent();
/* 530 */       Block b = (Block)switchStatement.getParent();
/* 531 */       int index = b.statements().indexOf(switchStatement);
/* 532 */       int branch = ((Integer)node.getProperty("BranchNumber")).intValue();
/* 533 */       branchDistanceInstrumentation(b, index, Integer.valueOf(branch), "1", false);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 537 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean visit(ForStatement node)
/*     */   {
/* 543 */     if (node.getProperty("Encapsulated") == null) {
/* 544 */       return false;
/*     */     }
/*     */     
/* 547 */     Block b = this.ast.newBlock();
/* 548 */     if (!(node.getBody() instanceof Block))
/*     */     {
/* 550 */       Statement body = node.getBody();
/* 551 */       node.setBody(b);
/* 552 */       body.delete();
/* 553 */       b.statements().add(body);
/*     */     }
/*     */     else {
/* 556 */       b = (Block)node.getBody();
/*     */     }
/* 558 */     Block parent = (Block)node.getParent();
/* 559 */     int index = parent.statements().indexOf(node);
/*     */     
/*     */ 
/* 562 */     for (int i = 0; i < node.initializers().size(); i++) {
/* 563 */       Expression vde = (Expression)node.initializers().get(i);
/* 564 */       if ((vde instanceof VariableDeclarationExpression)) {
/* 565 */         for (int j = 0; j < ((VariableDeclarationExpression)vde).fragments().size(); j++) {
/* 566 */           VariableDeclarationFragment vdf = (VariableDeclarationFragment)ASTNode.copySubtree(this.ast, (ASTNode)((VariableDeclarationExpression)vde).fragments().get(j));
/* 567 */           VariableDeclarationStatement vds = this.ast.newVariableDeclarationStatement(vdf);
/* 568 */           parent.statements().add(index, vds);
/*     */         }
/*     */       }
/* 571 */       node.initializers().remove(i);
/*     */     }
/*     */     
/* 574 */     node.setProperty("BranchNumber", Integer.valueOf(this.cBranch.intValue() + 1));
/* 575 */     node.setProperty("Expression", node.getExpression());
/* 576 */     this.branch2ASTNode.put(Integer.valueOf(this.cBranch.intValue() + 1), node);
/* 577 */     this.branch2Method.put(Integer.valueOf(this.cBranch.intValue() + 1), Integer.valueOf(this.methodNodeIndex));
/* 578 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(ForStatement node)
/*     */   {
/* 583 */     if (node.getProperty("Encapsulated") == null) {
/* 584 */       encapsulatingNodeInBlock(node);
/* 585 */       node.setProperty("Encapsulated", "YES");
/* 586 */       return;
/*     */     }
/*     */     try {
/* 589 */       Block body = (Block)node.getBody();
/* 590 */       int branch = ((Integer)node.getProperty("BranchNumber")).intValue();
/* 591 */       instrumentLoop(body, branch);
/*     */     } catch (Exception e) {
/* 593 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean visit(WhileStatement node)
/*     */   {
/* 599 */     Block b = this.ast.newBlock();
/* 600 */     if (!(node.getBody() instanceof Block))
/*     */     {
/* 602 */       Statement body = node.getBody();
/* 603 */       node.setBody(b);
/* 604 */       body.delete();
/* 605 */       b.statements().add(body);
/*     */     }
/*     */     else {
/* 608 */       b = (Block)node.getBody();
/*     */     }
/* 610 */     node.setProperty("BranchNumber", Integer.valueOf(this.cBranch.intValue() + 1));
/* 611 */     node.setProperty("Expression", node.getExpression());
/* 612 */     this.branch2ASTNode.put(Integer.valueOf(this.cBranch.intValue() + 1), node);
/* 613 */     this.branch2Method.put(Integer.valueOf(this.cBranch.intValue() + 1), Integer.valueOf(this.methodNodeIndex));
/* 614 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(WhileStatement node)
/*     */   {
/*     */     try {
/* 620 */       int branch = ((Integer)node.getProperty("BranchNumber")).intValue();
/* 621 */       instrumentLoop((Block)node.getBody(), branch);
/*     */     } catch (Exception e) {
/* 623 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean visit(DoStatement node)
/*     */   {
/* 629 */     Block b = this.ast.newBlock();
/* 630 */     if (!(node.getBody() instanceof Block))
/*     */     {
/* 632 */       Statement body = node.getBody();
/* 633 */       node.setBody(b);
/* 634 */       body.delete();
/* 635 */       b.statements().add(body);
/*     */     }
/*     */     else {
/* 638 */       b = (Block)node.getBody();
/*     */     }
/* 640 */     node.setProperty("BranchNumber", Integer.valueOf(this.cBranch.intValue() + 1));
/* 641 */     node.setProperty("Expression", node.getExpression());
/* 642 */     this.branch2ASTNode.put(Integer.valueOf(this.cBranch.intValue() + 1), node);
/* 643 */     this.branch2Method.put(Integer.valueOf(this.cBranch.intValue() + 1), Integer.valueOf(this.methodNodeIndex));
/* 644 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(DoStatement node)
/*     */   {
/*     */     try {
/* 650 */       Block body = (Block)node.getBody();
/* 651 */       int branch = ((Integer)node.getProperty("BranchNumber")).intValue();
/* 652 */       instrumentLoop(body, branch);
/*     */     } catch (Exception e) {
/* 654 */       e.printStackTrace();
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
/*     */   private void encapsulatingNodeInBlock(ASTNode node)
/*     */   {
/* 673 */     Block parent = (Block)node.getParent();
/* 674 */     int index = parent.statements().indexOf(node);
/*     */     
/* 676 */     Block newBlock = this.ast.newBlock();
/* 677 */     parent.statements().add(newBlock);
/* 678 */     newBlock.setProperty("Source", "Instrumentation");
/*     */     
/* 680 */     node.delete();
/* 681 */     newBlock.statements().add(node);
/*     */   }
/*     */   
/*     */   private void instrumentLoop(Block body, int branch) throws Exception
/*     */   {
/* 686 */     Statement loopNode = (Statement)body.getParent();
/* 687 */     Block parent = (Block)loopNode.getParent();
/* 688 */     int index = parent.statements().indexOf(loopNode);
/*     */     
/*     */ 
/* 691 */     String varName = "i" + branch;
/* 692 */     insertIntegerVariableDeclaration(parent, index, varName, "1");
/*     */     
/*     */ 
/* 695 */     incrementIntegerVariable(body, varName);
/*     */     
/*     */ 
/* 698 */     branchDistanceSimpleInstrumentation(body, 0, Integer.valueOf(branch), "-1*i" + branch, true);
/* 699 */     branchDistanceSimpleInstrumentation(parent, index + 2, Integer.valueOf(branch), "i" + branch, false);
/*     */   }
/*     */   
/*     */   private void incrementIntegerVariable(Block block, String name) {
/* 703 */     PostfixExpression inc = this.ast.newPostfixExpression();
/* 704 */     inc.setOperator(PostfixExpression.Operator.INCREMENT);
/* 705 */     inc.setOperand(this.ast.newSimpleName(name));
/* 706 */     ExpressionStatement eStatement = this.ast.newExpressionStatement(inc);
/* 707 */     block.statements().add(eStatement);
/*     */   }
/*     */   
/*     */   private void insertIntegerVariableDeclaration(Block block, int index, String name, String initialize) {
/* 711 */     VariableDeclarationFragment vdf = this.ast.newVariableDeclarationFragment();
/* 712 */     vdf.setName(this.ast.newSimpleName(name));
/* 713 */     vdf.setInitializer(this.ast.newNumberLiteral(initialize));
/*     */     
/* 715 */     VariableDeclarationStatement vde = this.ast.newVariableDeclarationStatement(vdf);
/* 716 */     vde.setType(this.ast.newPrimitiveType(PrimitiveType.INT));
/*     */     
/* 718 */     block.statements().add(index, vde);
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\InstrumentorOld.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */