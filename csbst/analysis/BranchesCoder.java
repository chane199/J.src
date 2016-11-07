/*     */ package csbst.analysis;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*     */ import org.eclipse.jdt.core.dom.AssertStatement;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.BooleanLiteral;
/*     */ import org.eclipse.jdt.core.dom.ClassInstanceCreation;
/*     */ import org.eclipse.jdt.core.dom.ConditionalExpression;
/*     */ import org.eclipse.jdt.core.dom.ConstructorInvocation;
/*     */ import org.eclipse.jdt.core.dom.DoStatement;
/*     */ import org.eclipse.jdt.core.dom.Expression;
/*     */ import org.eclipse.jdt.core.dom.ForStatement;
/*     */ import org.eclipse.jdt.core.dom.ITypeBinding;
/*     */ import org.eclipse.jdt.core.dom.IfStatement;
/*     */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*     */ import org.eclipse.jdt.core.dom.Modifier;
/*     */ import org.eclipse.jdt.core.dom.ReturnStatement;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
/*     */ import org.eclipse.jdt.core.dom.SwitchCase;
/*     */ import org.eclipse.jdt.core.dom.SwitchStatement;
/*     */ import org.eclipse.jdt.core.dom.TypeDeclaration;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BranchesCoder
/*     */   extends ASTVisitor
/*     */ {
/*     */   private int currentBranch;
/*  48 */   private int branchesCounter = 0;
/*     */   private TypeDeclaration classNode;
/*     */   private MethodDeclaration currentMethod;
/*  51 */   private Map<Integer, Block> branch2ASTNode = new HashMap();
/*     */   
/*     */   public Map<Integer, Block> getBranch2BlockMap() {
/*  54 */     return this.branch2ASTNode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeDeclaration getClassNode()
/*     */   {
/*  62 */     return this.classNode;
/*     */   }
/*     */   
/*     */   public int getLastBranch()
/*     */   {
/*  67 */     return this.branchesCounter;
/*     */   }
/*     */   
/*     */   private void defineNewBranch(Block node, Expression exp) {
/*  71 */     if (this.currentMethod == null) { return;
/*     */     }
/*  73 */     this.branchesCounter += 1;
/*  74 */     node.setProperty("numberBranch", Integer.valueOf(this.branchesCounter));
/*  75 */     node.setProperty("methodContainer", this.currentMethod);
/*  76 */     if (exp != null) {
/*  77 */       node.setProperty("expression", exp);
/*  78 */       if (!exp.toString().equalsIgnoreCase("true")) {
/*     */         try {
/*  80 */           node.setProperty("difficultyCoefficient", Double.valueOf(DifficultyCoefficient.getDifficultyCoefficient(exp)));
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/*  84 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*  88 */     this.currentBranch = this.branchesCounter;
/*  89 */     this.branch2ASTNode.put(Integer.valueOf(this.currentBranch), node);
/*     */   }
/*     */   
/*     */   private void encapsulatingNodeInBlock(ASTNode node)
/*     */   {
/*  94 */     Block parent = (Block)node.getParent();
/*  95 */     int index = parent.statements().indexOf(node);
/*     */     
/*  97 */     Block newBlock = node.getAST().newBlock();
/*  98 */     parent.statements().add(index, newBlock);
/*  99 */     newBlock.setProperty("Source", "Instrumentation");
/*     */     
/* 101 */     node.delete();
/* 102 */     newBlock.statements().add(node);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(TypeDeclaration node)
/*     */   {
/* 108 */     if ((node.isLocalTypeDeclaration()) || (node.isMemberTypeDeclaration())) {
/* 109 */       return true;
/*     */     }
/* 111 */     this.classNode = node;
/* 112 */     this.branchesCounter = 0;
/* 113 */     this.currentBranch = 0;
/* 114 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean visit(ClassInstanceCreation node)
/*     */   {
/* 121 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(MethodDeclaration node)
/*     */   {
/* 127 */     if ((Modifier.isAbstract(node.getModifiers())) || (node.getBody() == null)) {
/* 128 */       return false;
/*     */     }
/*     */     
/* 131 */     this.currentMethod = node;
/*     */     
/*     */ 
/*     */ 
/* 135 */     defineNewBranch(node.getBody(), String2Expression.getExpression("true"));
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(MethodDeclaration node)
/*     */   {
/* 141 */     this.currentMethod = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean visit(IfStatement node)
/*     */   {
/* 149 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 151 */     int parentBranch = this.currentBranch;
/* 152 */     node.setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/* 153 */     node.setProperty("isMultiBranchesRoot", "YES");
/* 154 */     node.setProperty("expression", node.getExpression());
/*     */     
/* 156 */     if (!(node.getThenStatement() instanceof Block)) {
/* 157 */       Block b = node.getAST().newBlock();
/* 158 */       Statement body = node.getThenStatement();
/* 159 */       node.setThenStatement(b);
/* 160 */       body.delete();
/* 161 */       b.statements().add(body);
/* 162 */       defineNewBranch(b, node.getExpression());
/* 163 */       b.setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/*     */     }
/*     */     else {
/* 166 */       defineNewBranch((Block)node.getThenStatement(), node.getExpression());
/* 167 */       ((Block)node.getThenStatement()).setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/*     */     }
/*     */     
/* 170 */     if (!(node.getElseStatement() instanceof Block)) {
/* 171 */       Block b = node.getAST().newBlock();
/* 172 */       Statement body = node.getElseStatement();
/* 173 */       node.setElseStatement(b);
/* 174 */       if (body != null) {
/* 175 */         body.delete();
/* 176 */         b.statements().add(body);
/*     */       }
/*     */       
/* 179 */       defineNewBranch(b, String2Expression.getExpression("!(" + node.getExpression().toString() + ")"));
/* 180 */       b.setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/*     */     }
/*     */     else
/*     */     {
/* 184 */       defineNewBranch((Block)node.getElseStatement(), String2Expression.getExpression("!(" + node.getExpression().toString() + ")"));
/* 185 */       ((Block)node.getElseStatement()).setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/*     */     }
/*     */     
/* 188 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(AssertStatement node)
/*     */   {
/* 193 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 195 */     IfStatement ifStatement = node.getAST().newIfStatement();
/* 196 */     Expression exp = (Expression)ASTNode.copySubtree(node.getAST(), node.getExpression());
/*     */     
/*     */ 
/* 199 */     ifStatement.setExpression(exp);
/*     */     
/* 201 */     Block bIf = node.getAST().newBlock();
/* 202 */     ifStatement.setThenStatement(bIf);
/* 203 */     int parentBranch = this.currentBranch;
/* 204 */     defineNewBranch(bIf, exp);
/* 205 */     bIf.setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/* 206 */     Block bElse = node.getAST().newBlock();
/* 207 */     ifStatement.setElseStatement(bElse);
/* 208 */     parentBranch = this.currentBranch;
/* 209 */     defineNewBranch(bElse, String2Expression.getExpression("!(" + exp + ")"));
/* 210 */     bElse.setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/*     */     
/* 212 */     if ((node.getParent() instanceof Block)) {
/* 213 */       ((Block)node.getParent()).statements().add(((Block)node.getParent()).statements().lastIndexOf(node), ifStatement);
/* 214 */     } else if ((node.getParent() instanceof SwitchStatement)) {
/* 215 */       ((SwitchStatement)node.getParent()).statements().add(((SwitchStatement)node.getParent()).statements().lastIndexOf(node), ifStatement);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 223 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(ConditionalExpression node)
/*     */   {
/* 228 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 230 */     IfStatement ifStatement = node.getAST().newIfStatement();
/* 231 */     Expression exp = (Expression)ASTNode.copySubtree(node.getAST(), node.getExpression());
/*     */     
/*     */ 
/* 234 */     ifStatement.setExpression(exp);
/*     */     
/* 236 */     Block bIf = node.getAST().newBlock();
/* 237 */     ifStatement.setThenStatement(bIf);
/* 238 */     int parentBranch = this.currentBranch;
/* 239 */     defineNewBranch(bIf, exp);
/* 240 */     bIf.setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/* 241 */     Block bElse = node.getAST().newBlock();
/* 242 */     ifStatement.setElseStatement(bElse);
/* 243 */     parentBranch = this.currentBranch;
/* 244 */     defineNewBranch(bElse, String2Expression.getExpression("!(" + exp + ")"));
/* 245 */     bElse.setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 252 */     ASTNode n = node.getParent();
/* 253 */     while ((n != null) && (!(n instanceof Statement))) {
/* 254 */       n = n.getParent();
/*     */     }
/* 256 */     if (n != null) {
/* 257 */       Statement eStat = (Statement)n;
/* 258 */       if ((eStat.getParent() instanceof Block)) {
/* 259 */         Block container = (Block)eStat.getParent();
/* 260 */         int index = container.statements().lastIndexOf(eStat);
/*     */         
/* 262 */         if ((container.statements().size() > 0) && (index <= container.statements().size()) && (
/* 263 */           ((container.statements().get(index) instanceof SuperConstructorInvocation)) || ((container.statements().get(index) instanceof ConstructorInvocation))))
/* 264 */           return true;
/* 265 */         container.statements().add(index, ifStatement);
/* 266 */       } else if ((eStat.getParent() instanceof SwitchStatement)) {
/* 267 */         SwitchStatement container = (SwitchStatement)eStat.getParent();
/* 268 */         int index = container.statements().lastIndexOf(eStat);
/*     */         
/* 270 */         if ((container.statements().size() > 0) && (index <= container.statements().size()) && (
/* 271 */           ((container.statements().get(index) instanceof SuperConstructorInvocation)) || ((container.statements().get(index) instanceof ConstructorInvocation))))
/* 272 */           return true;
/* 273 */         container.statements().add(index, ifStatement);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 279 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean visit(ReturnStatement node)
/*     */   {
/* 335 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 337 */     if ((node.getExpression() != null) && 
/* 338 */       (node.getExpression().resolveTypeBinding() != null) && 
/* 339 */       (node.getExpression().resolveTypeBinding().getBinaryName() != null))
/*     */     {
/* 341 */       if ((node.getExpression().resolveTypeBinding() != null) && 
/* 342 */         (node.getExpression().resolveTypeBinding().getBinaryName().equalsIgnoreCase("Z")) && 
/* 343 */         (!(node.getExpression() instanceof BooleanLiteral)))
/*     */       {
/* 345 */         IfStatement ifStatement = node.getAST().newIfStatement();
/* 346 */         Expression exp = (Expression)ASTNode.copySubtree(node.getAST(), node.getExpression());
/* 347 */         ifStatement.setExpression(exp);
/*     */         
/* 349 */         Block bIf = node.getAST().newBlock();
/* 350 */         ifStatement.setThenStatement(bIf);
/* 351 */         int parentBranch = this.currentBranch;
/* 352 */         defineNewBranch(bIf, exp);
/* 353 */         bIf.setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/* 354 */         Block bElse = node.getAST().newBlock();
/* 355 */         ifStatement.setElseStatement(bElse);
/* 356 */         parentBranch = this.currentBranch;
/* 357 */         defineNewBranch(bElse, String2Expression.getExpression("!(" + exp + ")"));
/* 358 */         bElse.setProperty("numberParentBranch", Integer.valueOf(parentBranch));
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 363 */         Statement eStat = node;
/*     */         
/* 365 */         if ((eStat.getParent() instanceof Block)) {
/* 366 */           Block container = (Block)eStat.getParent();
/* 367 */           int index = container.statements().lastIndexOf(eStat);
/* 368 */           if ((container.statements().size() > 0) && (index <= container.statements().size()) && (
/* 369 */             ((container.statements().get(index) instanceof SuperConstructorInvocation)) || ((container.statements().get(index) instanceof ConstructorInvocation))))
/* 370 */             return true;
/* 371 */           container.statements().add(index, ifStatement);
/* 372 */         } else if ((eStat.getParent() instanceof SwitchStatement)) {
/* 373 */           SwitchStatement container = (SwitchStatement)eStat.getParent();
/* 374 */           int index = container.statements().lastIndexOf(eStat);
/* 375 */           if ((container.statements().size() > 0) && (index <= container.statements().size()) && (
/* 376 */             ((container.statements().get(index) instanceof SuperConstructorInvocation)) || ((container.statements().get(index) instanceof ConstructorInvocation))))
/* 377 */             return true;
/* 378 */           container.statements().add(index, ifStatement);
/*     */         }
/*     */       }
/*     */     }
/* 382 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(SwitchStatement node)
/*     */   {
/* 388 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 390 */     node.setProperty("expression", node.getExpression());
/* 391 */     node.setProperty("isMultiBranchesRoot", "YES");
/* 392 */     node.setProperty("numberParentBranch", Integer.valueOf(this.currentBranch));
/* 393 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(SwitchCase node)
/*     */   {
/* 398 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 400 */     ASTNode ss = node.getParent();
/* 401 */     while (!(ss instanceof SwitchStatement)) {
/* 402 */       ss = ss.getParent();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 407 */     SwitchStatement parent = (SwitchStatement)ss;
/* 408 */     int index = parent.statements().indexOf(node);
/*     */     
/* 410 */     if (parent.statements().size() <= index + 1) {
/* 411 */       return false;
/*     */     }
/* 413 */     Statement nextStatement = (Statement)parent.statements().get(index + 1);
/*     */     
/*     */ 
/* 416 */     if ((nextStatement instanceof SwitchCase)) {
/* 417 */       return false;
/*     */     }
/* 419 */     Block b = null;
/* 420 */     if (!(nextStatement instanceof Block))
/*     */     {
/* 422 */       b = node.getAST().newBlock();
/*     */       
/* 424 */       parent.statements().add(index + 1, b);
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
/* 437 */       b.setProperty("numberParentBranch", parent.getProperty("numberParentBranch"));
/*     */     }
/*     */     else
/*     */     {
/* 441 */       b = (Block)nextStatement;
/* 442 */       b.setProperty("numberParentBranch", parent.getProperty("numberParentBranch"));
/*     */     }
/*     */     
/* 445 */     String txtExpression = "";
/* 446 */     if (node.getExpression() != null)
/*     */     {
/* 448 */       txtExpression = parent.getExpression() + "==" + node.getExpression();
/*     */     } else {
/* 450 */       for (int i = 0; i < parent.statements().size(); i++) {
/* 451 */         if (((parent.statements().get(i) instanceof SwitchCase)) && (!parent.statements().get(i).equals(node))) {
/* 452 */           if (txtExpression != "") txtExpression = txtExpression + " && ";
/* 453 */           txtExpression = txtExpression + parent.getExpression() + "!=" + ((SwitchCase)parent.statements().get(i)).getExpression();
/*     */         }
/*     */       }
/*     */     }
/* 457 */     defineNewBranch(b, String2Expression.getExpression(txtExpression));
/* 458 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(ForStatement node)
/*     */   {
/* 463 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 465 */     node.getProperty("isEncapsulated");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 470 */     node.setProperty("expression", node.getExpression());
/* 471 */     node.setProperty("isMultiBranchesRoot", "YES");
/* 472 */     Block b = node.getAST().newBlock();
/* 473 */     if (!(node.getBody() instanceof Block))
/*     */     {
/* 475 */       Statement body = node.getBody();
/* 476 */       node.setBody(b);
/* 477 */       body.delete();
/* 478 */       b.statements().add(body);
/* 479 */       b.setProperty("numberParentBranch", Integer.valueOf(this.currentBranch));
/*     */     }
/*     */     else {
/* 482 */       b = (Block)node.getBody();
/* 483 */       b.setProperty("numberParentBranch", Integer.valueOf(this.currentBranch));
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
/* 504 */     defineNewBranch(b, node.getExpression());
/* 505 */     return true;
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
/*     */   public boolean visit(WhileStatement node)
/*     */   {
/* 520 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 522 */     node.setProperty("expression", node.getExpression());
/* 523 */     node.setProperty("isMultiBranchesRoot", "YES");
/* 524 */     Block b = node.getAST().newBlock();
/* 525 */     if (!(node.getBody() instanceof Block))
/*     */     {
/* 527 */       Statement body = node.getBody();
/* 528 */       node.setBody(b);
/* 529 */       body.delete();
/* 530 */       b.statements().add(body);
/* 531 */       b.setProperty("numberParentBranch", Integer.valueOf(this.currentBranch));
/*     */     }
/*     */     else {
/* 534 */       b = (Block)node.getBody();
/* 535 */       b.setProperty("numberParentBranch", Integer.valueOf(this.currentBranch));
/*     */     }
/*     */     
/* 538 */     defineNewBranch(b, node.getExpression());
/* 539 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(DoStatement node)
/*     */   {
/* 544 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 546 */     node.setProperty("expression", node.getExpression());
/* 547 */     node.setProperty("isMultiBranchesRoot", "YES");
/* 548 */     Block b = node.getAST().newBlock();
/* 549 */     if (!(node.getBody() instanceof Block))
/*     */     {
/* 551 */       Statement body = node.getBody();
/* 552 */       node.setBody(b);
/* 553 */       body.delete();
/* 554 */       b.statements().add(body);
/* 555 */       b.setProperty("numberParentBranch", Integer.valueOf(this.currentBranch));
/*     */     }
/*     */     else {
/* 558 */       b = (Block)node.getBody();
/* 559 */       b.setProperty("numberParentBranch", Integer.valueOf(this.currentBranch));
/*     */     }
/*     */     
/* 562 */     defineNewBranch(b, node.getExpression());
/* 563 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(Block node)
/*     */   {
/* 568 */     if (this.currentMethod == null) { return true;
/*     */     }
/* 570 */     if (node.getProperty("numberBranch") != null)
/* 571 */       this.currentBranch = ((Integer)node.getProperty("numberBranch")).intValue();
/* 572 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(Block node)
/*     */   {
/* 577 */     if (this.currentMethod == null) { return;
/*     */     }
/* 579 */     if (node.getProperty("numberParentBranch") != null) {
/* 580 */       this.currentBranch = ((Integer)node.getProperty("numberParentBranch")).intValue();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\BranchesCoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */