/*     */ package csbst.analysis;
/*     */ 
/*     */ import choco.Choco;
/*     */ import choco.cp.model.CPModel;
/*     */ import choco.cp.solver.CPSolver;
/*     */ import choco.kernel.model.Model;
/*     */ import choco.kernel.model.constraints.Constraint;
/*     */ import choco.kernel.model.variables.integer.IntegerExpressionVariable;
/*     */ import choco.kernel.model.variables.integer.IntegerVariable;
/*     */ import choco.kernel.solver.Solver;
/*     */ import choco.kernel.solver.variables.integer.IntDomainVar;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.eclipse.jdt.core.dom.Expression;
/*     */ import org.eclipse.jdt.core.dom.ITypeBinding;
/*     */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*     */ import org.eclipse.jdt.core.dom.InfixExpression;
/*     */ import org.eclipse.jdt.core.dom.InfixExpression.Operator;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.NullLiteral;
/*     */ import org.eclipse.jdt.core.dom.NumberLiteral;
/*     */ import org.eclipse.jdt.core.dom.ParenthesizedExpression;
/*     */ import org.eclipse.jdt.core.dom.PrefixExpression;
/*     */ import org.eclipse.jdt.core.dom.PrefixExpression.Operator;
/*     */ import org.eclipse.jdt.core.dom.QualifiedName;
/*     */ import org.eclipse.jdt.core.dom.SimpleName;
/*     */ 
/*     */ public class DifficultyCoefficient
/*     */ {
/*     */   private static final int MaxInt = 1000;
/*     */   private static final int MinInt = -1000;
/*     */   private static final double B = 100.0D;
/*     */   
/*     */   public static double alphaPlus(Expression expression)
/*     */     throws Exception
/*     */   {
/*  38 */     short nbrVars = getNumberOfVariables(expression);
/*  39 */     return Math.pow(2.0D, 32.0D) / nbrVars;
/*     */   }
/*     */   
/*     */   public static double getDifficultyCoefficient(Expression expression) throws Exception {
/*  43 */     return 1.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double getDifficultyLevelPlus(Expression expression)
/*     */     throws Exception
/*     */   {
/*  54 */     return alphaPlus(expression) + betaPlus(expression);
/*     */   }
/*     */   
/*     */   public static IntegerExpressionVariable ExpressionToChocoExpression(Expression expression) throws Exception {
/*  58 */     return ExpressionToChocoExpression(expression, 1);
/*     */   }
/*     */   
/*     */   public static IntegerExpressionVariable ExpressionToChocoExpression(Expression expression, int signe) throws Exception
/*     */   {
/*  63 */     if ((expression instanceof ParenthesizedExpression)) {
/*  64 */       return ExpressionToChocoExpression(((ParenthesizedExpression)expression).getExpression());
/*     */     }
/*     */     
/*  67 */     if (((expression instanceof PrefixExpression)) && 
/*  68 */       (((PrefixExpression)expression).getOperator() == PrefixExpression.Operator.MINUS)) {
/*  69 */       return ExpressionToChocoExpression(((PrefixExpression)expression).getOperand(), -1);
/*     */     }
/*  71 */     if ((expression instanceof NumberLiteral))
/*     */     {
/*     */ 
/*  74 */       return Choco.constant(signe * Integer.parseInt(((NumberLiteral)expression).toString()));
/*     */     }
/*     */     
/*  77 */     if ((expression instanceof NullLiteral))
/*     */     {
/*     */ 
/*  80 */       return Choco.constant(0);
/*     */     }
/*     */     
/*  83 */     if ((expression instanceof SimpleName))
/*     */     {
/*  85 */       IntegerVariable op1 = Choco.makeIntVar(expression.toString(), 64536, 1000, new String[] { "cp:bound" });
/*  86 */       return op1;
/*     */     }
/*     */     
/*  89 */     if ((expression instanceof QualifiedName))
/*     */     {
/*     */ 
/*     */ 
/*  93 */       IVariableBinding variableBinding = null;
/*  94 */       variableBinding = (IVariableBinding)((QualifiedName)expression).resolveBinding();
/*  95 */       if (variableBinding == null) {
/*  96 */         throw new Exception("A constant cannot be binded to its value : " + expression.toString());
/*     */       }
/*     */       int value;
/*     */       int value;
/* 100 */       if (!variableBinding.getType().getName().equals("int")) {
/* 101 */         long val1 = ((Long)variableBinding.getConstantValue()).longValue();
/* 102 */         int value; if (val1 > 1000L) {
/* 103 */           value = 1000; } else { int value;
/* 104 */           if (val1 < -1000L) {
/* 105 */             value = 64536;
/*     */           } else
/* 107 */             value = Integer.parseInt(variableBinding.getConstantValue().toString());
/*     */         }
/*     */       } else {
/* 110 */         value = Integer.parseInt(variableBinding.getConstantValue().toString());
/*     */       }
/* 112 */       if (value > 1000) value = 1000;
/* 113 */       if (value < 64536) value = 64536;
/* 114 */       return Choco.constant(value);
/*     */     }
/*     */     
/*     */ 
/* 118 */     if ((expression instanceof MethodInvocation))
/*     */     {
/* 120 */       IntegerVariable op1 = Choco.makeIntVar("Relaxation", 64536, 1000, new String[] { "cp:bound" });
/* 121 */       return op1;
/*     */     }
/*     */     
/* 124 */     if ((expression instanceof InfixExpression))
/*     */     {
/*     */ 
/* 127 */       if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.DIVIDE) {
/* 128 */         return Choco.div(ExpressionToChocoExpression(((InfixExpression)expression).getLeftOperand()), ExpressionToChocoExpression(((InfixExpression)expression).getRightOperand()));
/*     */       }
/* 130 */       if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.MINUS) {
/* 131 */         return Choco.minus(ExpressionToChocoExpression(((InfixExpression)expression).getLeftOperand()), ExpressionToChocoExpression(((InfixExpression)expression).getRightOperand()));
/*     */       }
/* 133 */       if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.PLUS) {
/* 134 */         return Choco.plus(ExpressionToChocoExpression(((InfixExpression)expression).getLeftOperand()), ExpressionToChocoExpression(((InfixExpression)expression).getRightOperand()));
/*     */       }
/* 136 */       if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.REMAINDER) {
/* 137 */         return Choco.mod(ExpressionToChocoExpression(((InfixExpression)expression).getLeftOperand()), ExpressionToChocoExpression(((InfixExpression)expression).getRightOperand()));
/*     */       }
/* 139 */       if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.TIMES) {
/* 140 */         return Choco.mult(ExpressionToChocoExpression(((InfixExpression)expression).getLeftOperand()), ExpressionToChocoExpression(((InfixExpression)expression).getRightOperand()));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 145 */       IntegerVariable op1 = Choco.makeIntVar("Relaxation", 64536, 1000, new String[] { "cp:bound" });
/* 146 */       return op1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 152 */     throw new Exception("Unsported expression type : " + expression.toString());
/*     */   }
/*     */   
/*     */   public static int getSearchSpaceSize(Expression expression)
/*     */     throws Exception
/*     */   {
/* 158 */     return (int)Math.pow(2001.0D, getNumberOfVariables(expression));
/*     */   }
/*     */   
/*     */   public static short getNumberOfVariables(Expression expression) throws Exception {
/* 162 */     Set<String> variablesSet = new HashSet();
/* 163 */     return getNumberOfVariables(expression, (short)0, variablesSet);
/*     */   }
/*     */   
/*     */   public static short getNumberOfVariables(Expression expression, short currentVariablesNumber, Set<String> variablesSet)
/*     */     throws Exception
/*     */   {
/* 169 */     if ((expression instanceof ParenthesizedExpression)) {
/* 170 */       return getNumberOfVariables(((ParenthesizedExpression)expression).getExpression(), currentVariablesNumber, variablesSet);
/*     */     }
/*     */     
/* 173 */     if ((expression instanceof PrefixExpression)) {
/* 174 */       return getNumberOfVariables(((PrefixExpression)expression).getOperand(), currentVariablesNumber, variablesSet);
/*     */     }
/* 176 */     if ((expression instanceof InfixExpression)) {
/* 177 */       return (short)(currentVariablesNumber + 
/* 178 */         getNumberOfVariables(((InfixExpression)expression).getLeftOperand(), (short)0, variablesSet) + 
/* 179 */         getNumberOfVariables(((InfixExpression)expression).getRightOperand(), (short)0, variablesSet));
/*     */     }
/*     */     
/* 182 */     if (((expression instanceof NumberLiteral)) || ((expression instanceof QualifiedName)) || ((expression instanceof MethodInvocation))) {
/* 183 */       return currentVariablesNumber;
/*     */     }
/*     */     
/* 186 */     if ((expression instanceof SimpleName))
/*     */     {
/* 188 */       if (variablesSet.contains(((SimpleName)expression).toString())) {
/* 189 */         return currentVariablesNumber;
/*     */       }
/*     */       
/* 192 */       variablesSet.add(((SimpleName)expression).toString());
/* 193 */       return (short)(currentVariablesNumber + 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 199 */     throw new Exception("Unsported expression type : " + expression.toString());
/*     */   }
/*     */   
/*     */   public static double getNumberOfSolutions(Expression expression)
/*     */     throws Exception
/*     */   {
/* 205 */     return getNumberOfSolutions(expression, false);
/*     */   }
/*     */   
/*     */   public static double getNumberOfSolutions(Expression expression, boolean negation) throws Exception {
/* 209 */     double numberOfSolutions = 0.0D;
/*     */     
/* 211 */     if ((expression instanceof ParenthesizedExpression)) {
/* 212 */       return getNumberOfSolutions(((ParenthesizedExpression)expression).getExpression(), negation);
/*     */     }
/*     */     
/*     */ 
/* 216 */     if (((expression instanceof PrefixExpression)) && 
/* 217 */       (((PrefixExpression)expression).getOperator() == PrefixExpression.Operator.NOT)) {
/* 218 */       return getNumberOfSolutions(((PrefixExpression)expression).getOperand(), !negation);
/*     */     }
/*     */     
/* 221 */     if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.CONDITIONAL_AND) {
/* 222 */       return Math.min(getNumberOfSolutions(((InfixExpression)expression).getLeftOperand(), negation), getNumberOfSolutions(((InfixExpression)expression).getRightOperand(), negation));
/*     */     }
/* 224 */     if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.CONDITIONAL_OR) {
/* 225 */       return Math.max(getNumberOfSolutions(((InfixExpression)expression).getLeftOperand(), negation), getNumberOfSolutions(((InfixExpression)expression).getRightOperand(), negation));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 230 */     Model csp = new CPModel();
/*     */     
/* 232 */     IntegerVariable X = Choco.makeIntVar("Xexpression", 64536, 1000, new String[] { "cp:bound" });
/*     */     
/* 234 */     IntegerVariable Y = Choco.makeIntVar("Yexpression", 64536, 1000, new String[] { "cp:bound" });
/*     */     
/* 236 */     csp.addConstraint(Choco.eq(X, ExpressionToChocoExpression(((InfixExpression)expression).getLeftOperand())));
/* 237 */     csp.addConstraint(Choco.eq(Y, ExpressionToChocoExpression(((InfixExpression)expression).getRightOperand())));
/*     */     
/* 239 */     InfixExpression.Operator op = ((InfixExpression)expression).getOperator();
/*     */     
/* 241 */     if (op == InfixExpression.Operator.EQUALS) {
/* 242 */       csp.addConstraint(Choco.eq(X, Y));
/*     */ 
/*     */     }
/* 245 */     else if (op == InfixExpression.Operator.GREATER) {
/* 246 */       csp.addConstraint(Choco.gt(X, Y));
/*     */ 
/*     */     }
/* 249 */     else if (op == InfixExpression.Operator.GREATER_EQUALS) {
/* 250 */       csp.addConstraint(Choco.geq(X, Y));
/*     */ 
/*     */     }
/* 253 */     else if (op == InfixExpression.Operator.LESS) {
/* 254 */       csp.addConstraint(Choco.lt(X, Y));
/*     */ 
/*     */     }
/* 257 */     else if (op == InfixExpression.Operator.LESS_EQUALS) {
/* 258 */       csp.addConstraint(Choco.leq(X, Y));
/*     */ 
/*     */     }
/* 261 */     else if (op == InfixExpression.Operator.NOT_EQUALS) {
/* 262 */       csp.addConstraint(Choco.neq(X, Y));
/*     */     }
/*     */     else {
/* 265 */       throw new Exception("Exepression Contains unsupported comparaison operator : " + expression.toString());
/*     */     }
/*     */     
/* 268 */     Solver solver = new CPSolver();
/* 269 */     solver.setTimeLimit(200);
/* 270 */     solver.read(csp);
/* 271 */     solver.propagate();
/* 272 */     int xSup = solver.getVar(X).getSup();
/* 273 */     int xInf = solver.getVar(X).getInf();
/* 274 */     int ySup = solver.getVar(Y).getSup();
/* 275 */     int yInf = solver.getVar(Y).getInf();
/* 276 */     int dx = xSup - xInf + 1;
/* 277 */     int dy = ySup - yInf + 1;
/*     */     
/* 279 */     if (op == InfixExpression.Operator.EQUALS) {
/* 280 */       numberOfSolutions = dx;
/*     */ 
/*     */     }
/* 283 */     else if (op == InfixExpression.Operator.GREATER) {
/* 284 */       numberOfSolutions = xSup * (ySup + 1) - yInf * dx - 
/* 285 */         0.5D * (Math.pow(ySup, 2.0D) + ySup + 
/* 286 */         Math.pow(xInf, 2.0D) - xInf);
/*     */ 
/*     */     }
/* 289 */     else if (op == InfixExpression.Operator.GREATER_EQUALS) {
/* 290 */       int dxINdy = 0;
/* 291 */       if ((ySup >= xInf) && (xSup >= yInf)) {
/* 292 */         dxINdy = Math.min(ySup, xSup) - 
/* 293 */           Math.max(yInf, xInf) + 1;
/*     */       }
/* 295 */       numberOfSolutions = xSup * (ySup + 1) - yInf * dx - 
/* 296 */         0.5D * (Math.pow(ySup, 2.0D) + ySup + 
/* 297 */         Math.pow(xInf, 2.0D) - xInf) + dxINdy;
/*     */ 
/*     */     }
/* 300 */     else if (op == InfixExpression.Operator.LESS) {
/* 301 */       numberOfSolutions = ySup * (xSup + 1) - xInf * dy - 
/* 302 */         0.5D * (Math.pow(xSup, 2.0D) + xSup + 
/* 303 */         Math.pow(yInf, 2.0D) - yInf);
/*     */ 
/*     */     }
/* 306 */     else if (op == InfixExpression.Operator.LESS_EQUALS) {
/* 307 */       int dxINdy = 0;
/* 308 */       if ((xSup >= yInf) && (ySup >= xInf)) {
/* 309 */         dxINdy = Math.min(xSup, ySup) - 
/* 310 */           Math.max(xInf, yInf) + 1;
/*     */       }
/*     */       
/*     */ 
/* 314 */       numberOfSolutions = ySup * (xSup + 1) - xInf * dy - 0.5D * (Math.pow(xSup, 2.0D) + xSup + Math.pow(yInf, 2.0D) - yInf) + dxINdy;
/*     */ 
/*     */     }
/* 317 */     else if (op == InfixExpression.Operator.NOT_EQUALS) {
/* 318 */       int dxINdy = 0;
/* 319 */       if ((xSup >= yInf) && (ySup >= xInf)) {
/* 320 */         dxINdy = Math.min(xSup, ySup) - 
/* 321 */           Math.max(xInf, yInf);
/*     */       }
/* 323 */       numberOfSolutions = dx * dy - dxINdy;
/*     */     }
/*     */     
/* 326 */     return numberOfSolutions;
/*     */   }
/*     */   
/*     */   public static double beta(Expression expression) throws Exception
/*     */   {
/* 331 */     return beta(expression, false);
/*     */   }
/*     */   
/*     */   public static double beta(Expression expression, boolean negation) throws Exception {
/* 335 */     double density = 1.0D;
/*     */     
/* 337 */     if ((expression instanceof ParenthesizedExpression)) {
/* 338 */       return beta(((ParenthesizedExpression)expression).getExpression(), negation);
/*     */     }
/*     */     
/*     */ 
/* 342 */     if (((expression instanceof PrefixExpression)) && 
/* 343 */       (((PrefixExpression)expression).getOperator() == PrefixExpression.Operator.NOT)) {
/* 344 */       return beta(((PrefixExpression)expression).getOperand(), !negation);
/*     */     }
/*     */     
/* 347 */     if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.CONDITIONAL_AND) {
/* 348 */       return Math.max(beta(((InfixExpression)expression).getLeftOperand(), negation), beta(((InfixExpression)expression).getRightOperand(), negation));
/*     */     }
/* 350 */     if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.CONDITIONAL_OR) {
/* 351 */       return Math.min(beta(((InfixExpression)expression).getLeftOperand(), negation), beta(((InfixExpression)expression).getRightOperand(), negation));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 356 */     Model csp = new CPModel();
/*     */     
/* 358 */     IntegerVariable X = Choco.makeIntVar("Xexpression", 64536, 1000, new String[] { "cp:bound" });
/*     */     
/* 360 */     IntegerVariable Y = Choco.makeIntVar("Yexpression", 64536, 1000, new String[] { "cp:bound" });
/*     */     
/* 362 */     csp.addConstraint(Choco.eq(X, ExpressionToChocoExpression(((InfixExpression)expression).getLeftOperand())));
/* 363 */     csp.addConstraint(Choco.eq(Y, ExpressionToChocoExpression(((InfixExpression)expression).getRightOperand())));
/*     */     
/* 365 */     InfixExpression.Operator op = ((InfixExpression)expression).getOperator();
/*     */     
/* 367 */     if (op == InfixExpression.Operator.EQUALS) {
/* 368 */       csp.addConstraint(Choco.eq(X, Y));
/*     */ 
/*     */     }
/* 371 */     else if (op == InfixExpression.Operator.GREATER) {
/* 372 */       csp.addConstraint(Choco.gt(X, Y));
/*     */ 
/*     */     }
/* 375 */     else if (op == InfixExpression.Operator.GREATER_EQUALS) {
/* 376 */       csp.addConstraint(Choco.geq(X, Y));
/*     */ 
/*     */     }
/* 379 */     else if (op == InfixExpression.Operator.LESS) {
/* 380 */       csp.addConstraint(Choco.lt(X, Y));
/*     */ 
/*     */     }
/* 383 */     else if (op == InfixExpression.Operator.LESS_EQUALS) {
/* 384 */       csp.addConstraint(Choco.leq(X, Y));
/*     */ 
/*     */     }
/* 387 */     else if (op == InfixExpression.Operator.NOT_EQUALS) {
/* 388 */       csp.addConstraint(Choco.neq(X, Y));
/*     */     }
/*     */     else {
/* 391 */       throw new Exception("Exepression Contains unsupported comparaison operator : " + expression.toString());
/*     */     }
/*     */     
/* 394 */     Solver solver = new CPSolver();
/* 395 */     solver.setTimeLimit(200);
/* 396 */     solver.read(csp);
/* 397 */     solver.propagate();
/* 398 */     int xSup = solver.getVar(X).getSup();
/* 399 */     int xInf = solver.getVar(X).getInf();
/* 400 */     int ySup = solver.getVar(Y).getSup();
/* 401 */     int yInf = solver.getVar(Y).getInf();
/* 402 */     int dx = xSup - xInf + 1;
/* 403 */     int dy = ySup - yInf + 1;
/*     */     
/* 405 */     if (op == InfixExpression.Operator.EQUALS) {
/* 406 */       density = 1.0D / dx;
/*     */ 
/*     */     }
/* 409 */     else if (op == InfixExpression.Operator.GREATER) {
/* 410 */       density = (xSup * (ySup + 1) - yInf * dx - 
/* 411 */         0.5D * (Math.pow(ySup, 2.0D) + ySup + 
/* 412 */         Math.pow(xInf, 2.0D) - xInf)) / (dy * dx);
/*     */ 
/*     */     }
/* 415 */     else if (op == InfixExpression.Operator.GREATER_EQUALS) {
/* 416 */       int dxINdy = 0;
/* 417 */       if ((ySup >= xInf) && (xSup >= yInf)) {
/* 418 */         dxINdy = Math.min(ySup, xSup) - 
/* 419 */           Math.max(yInf, xInf) + 1;
/*     */       }
/* 421 */       density = (xSup * (ySup + 1) - yInf * dx - 
/* 422 */         0.5D * (Math.pow(ySup, 2.0D) + ySup + 
/* 423 */         Math.pow(xInf, 2.0D) - xInf) + dxINdy) / (dy * dx);
/*     */ 
/*     */     }
/* 426 */     else if (op == InfixExpression.Operator.LESS) {
/* 427 */       density = (ySup * (xSup + 1) - xInf * dy - 
/* 428 */         0.5D * (Math.pow(xSup, 2.0D) + xSup + 
/* 429 */         Math.pow(yInf, 2.0D) - yInf)) / (dx * dy);
/*     */ 
/*     */     }
/* 432 */     else if (op == InfixExpression.Operator.LESS_EQUALS) {
/* 433 */       int dxINdy = 0;
/* 434 */       if ((xSup >= yInf) && (ySup >= xInf)) {
/* 435 */         dxINdy = Math.min(xSup, ySup) - 
/* 436 */           Math.max(xInf, yInf) + 1;
/*     */       }
/*     */       
/*     */ 
/* 440 */       density = (ySup * (xSup + 1) - xInf * dy - 0.5D * (Math.pow(xSup, 2.0D) + xSup + Math.pow(yInf, 2.0D) - yInf) + dxINdy) / (dx * dy);
/*     */ 
/*     */     }
/* 443 */     else if (op == InfixExpression.Operator.NOT_EQUALS) {
/* 444 */       int dxINdy = 0;
/* 445 */       if ((xSup >= yInf) && (ySup >= xInf)) {
/* 446 */         dxINdy = Math.min(xSup, ySup) - 
/* 447 */           Math.max(xInf, yInf);
/*     */       }
/* 449 */       density = 1.0D * (dx * dy - dxINdy) / (dx * dy);
/*     */     }
/*     */     
/* 452 */     return 1.0D - density;
/*     */   }
/*     */   
/*     */   public static double alpha(Expression expression)
/*     */     throws Exception
/*     */   {
/* 458 */     if ((expression instanceof ParenthesizedExpression)) {
/* 459 */       return alpha(((ParenthesizedExpression)expression).getExpression());
/*     */     }
/*     */     
/* 462 */     Model csp = new CPModel();
/*     */     
/* 464 */     csp.addConstraint(ExpressionToConstraint(expression));
/*     */     
/*     */ 
/* 467 */     Solver solver = new CPSolver();
/* 468 */     solver.setTimeLimit(200);
/* 469 */     solver.read(csp);
/* 470 */     double dBefore = 1.0D;
/* 471 */     for (int v = 0; v < solver.getNbIntVars(); v++) {
/* 472 */       dBefore *= (solver.getIntVar(v).getSup() - solver.getIntVar(v).getInf() + 1);
/*     */     }
/*     */     
/* 475 */     solver.propagate();
/* 476 */     double dAfter = 1.0D;
/* 477 */     for (int v = 0; v < solver.getNbIntVars(); v++) {
/* 478 */       dAfter *= (solver.getIntVar(v).getSup() - solver.getIntVar(v).getInf() + 1);
/*     */     }
/*     */     
/*     */ 
/* 482 */     return 1.0D - dAfter / dBefore;
/*     */   }
/*     */   
/*     */   public static Constraint ExpressionToConstraint(Expression expression) throws Exception {
/* 486 */     return ExpressionToConstraint(expression, false);
/*     */   }
/*     */   
/*     */   public static Constraint ExpressionToConstraint(Expression expression, boolean negation) throws Exception
/*     */   {
/* 491 */     if ((expression instanceof ParenthesizedExpression)) {
/* 492 */       return ExpressionToConstraint(((ParenthesizedExpression)expression).getExpression(), negation);
/*     */     }
/*     */     
/*     */ 
/* 496 */     if (((expression instanceof PrefixExpression)) && 
/* 497 */       (((PrefixExpression)expression).getOperator() == PrefixExpression.Operator.NOT)) {
/* 498 */       return ExpressionToConstraint(((PrefixExpression)expression).getOperand(), !negation);
/*     */     }
/*     */     
/* 501 */     if (!(expression instanceof InfixExpression)) {
/* 502 */       throw new Exception("Exepression cannot be expressed as a constraint : " + expression.toString());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 507 */     Expression leftEx = ((InfixExpression)expression).getLeftOperand();
/* 508 */     Expression rightEx = ((InfixExpression)expression).getRightOperand();
/* 509 */     InfixExpression.Operator op = ((InfixExpression)expression).getOperator();
/*     */     
/* 511 */     if ((!(leftEx instanceof NumberLiteral)) && (!(rightEx instanceof NumberLiteral))) {
/* 512 */       if (op == InfixExpression.Operator.EQUALS) {
/* 513 */         if (!negation) {
/* 514 */           return Choco.eq(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 516 */         return Choco.neq(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 518 */       if (op == InfixExpression.Operator.GREATER) {
/* 519 */         if (!negation) {
/* 520 */           return Choco.gt(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 522 */         return Choco.leq(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 524 */       if (op == InfixExpression.Operator.GREATER_EQUALS) {
/* 525 */         if (!negation) {
/* 526 */           return Choco.geq(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 528 */         return Choco.lt(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */       }
/*     */       
/* 531 */       if (op == InfixExpression.Operator.LESS) {
/* 532 */         if (!negation) {
/* 533 */           return Choco.lt(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 535 */         return Choco.geq(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 537 */       if (op == InfixExpression.Operator.LESS_EQUALS) {
/* 538 */         if (!negation) {
/* 539 */           return Choco.leq(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 541 */         return Choco.gt(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 543 */       if (op == InfixExpression.Operator.NOT_EQUALS) {
/* 544 */         if (!negation) {
/* 545 */           return Choco.neq(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 547 */         return Choco.eq(ExpressionToChocoExpression(leftEx), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 549 */       if (op == InfixExpression.Operator.CONDITIONAL_AND) {
/* 550 */         if (!negation) {
/* 551 */           return Choco.and(new Constraint[] { ExpressionToConstraint(leftEx, negation), ExpressionToConstraint(rightEx, negation) });
/*     */         }
/* 553 */         return Choco.or(new Constraint[] { ExpressionToConstraint(leftEx, negation), ExpressionToConstraint(rightEx, negation) });
/*     */       }
/* 555 */       if (op == InfixExpression.Operator.CONDITIONAL_OR) {
/* 556 */         if (!negation) {
/* 557 */           return Choco.or(new Constraint[] { ExpressionToConstraint(leftEx, negation), ExpressionToConstraint(rightEx, negation) });
/*     */         }
/* 559 */         return Choco.and(new Constraint[] { ExpressionToConstraint(leftEx, negation), ExpressionToConstraint(rightEx, negation) });
/*     */       }
/* 561 */       throw new Exception("Exepression Contains unsupported comparaison operator : " + expression.toString());
/*     */     }
/*     */     
/* 564 */     if ((leftEx instanceof NumberLiteral)) {
/* 565 */       if (op == InfixExpression.Operator.EQUALS) {
/* 566 */         if (!negation) {
/* 567 */           return Choco.eq(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 569 */         return Choco.neq(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 571 */       if (op == InfixExpression.Operator.GREATER) {
/* 572 */         if (!negation) {
/* 573 */           return Choco.gt(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 575 */         return Choco.leq(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 577 */       if (op == InfixExpression.Operator.GREATER_EQUALS) {
/* 578 */         if (!negation) {
/* 579 */           return Choco.geq(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 581 */         return Choco.lt(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 583 */       if (op == InfixExpression.Operator.LESS) {
/* 584 */         if (!negation) {
/* 585 */           return Choco.lt(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 587 */         return Choco.geq(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 589 */       if (op == InfixExpression.Operator.LESS_EQUALS) {
/* 590 */         if (!negation) {
/* 591 */           return Choco.leq(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 593 */         return Choco.gt(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */       }
/* 595 */       if (op == InfixExpression.Operator.NOT_EQUALS) {
/* 596 */         if (!negation) {
/* 597 */           return Choco.neq(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */         }
/* 599 */         return Choco.eq(Integer.parseInt(leftEx.toString()), ExpressionToChocoExpression(rightEx));
/*     */       }
/*     */       
/* 602 */       throw new Exception("Exepression Contains unsupported comparaison operator : " + expression.toString());
/*     */     }
/*     */     
/* 605 */     if ((rightEx instanceof NumberLiteral)) {
/* 606 */       if (op == InfixExpression.Operator.EQUALS) {
/* 607 */         if (!negation) {
/* 608 */           return Choco.eq(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */         }
/* 610 */         return Choco.neq(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */       }
/* 612 */       if (op == InfixExpression.Operator.GREATER) {
/* 613 */         if (!negation) {
/* 614 */           return Choco.gt(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */         }
/* 616 */         return Choco.leq(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */       }
/* 618 */       if (op == InfixExpression.Operator.GREATER_EQUALS) {
/* 619 */         if (!negation) {
/* 620 */           return Choco.geq(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */         }
/* 622 */         return Choco.lt(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */       }
/* 624 */       if (op == InfixExpression.Operator.LESS) {
/* 625 */         if (!negation) {
/* 626 */           return Choco.lt(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */         }
/* 628 */         return Choco.geq(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */       }
/* 630 */       if (op == InfixExpression.Operator.LESS_EQUALS) {
/* 631 */         if (!negation) {
/* 632 */           return Choco.leq(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */         }
/* 634 */         return Choco.gt(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */       }
/* 636 */       if (op == InfixExpression.Operator.NOT_EQUALS) {
/* 637 */         if (!negation) {
/* 638 */           return Choco.neq(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */         }
/* 640 */         return Choco.eq(ExpressionToChocoExpression(leftEx), Integer.parseInt(rightEx.toString()));
/*     */       }
/*     */       
/* 643 */       throw new Exception("Exepression Contains unsupported comparaison operator : " + expression.toString());
/*     */     }
/*     */     
/* 646 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 651 */   public static double betaPlus(Expression expression) throws Exception { return betaPlus(expression, false); }
/*     */   
/*     */   public static double betaPlus(Expression expression, boolean negation) throws Exception {
/* 654 */     double density = 1.0D;
/*     */     
/* 656 */     if ((expression instanceof ParenthesizedExpression)) {
/* 657 */       return betaPlus(((ParenthesizedExpression)expression).getExpression(), negation);
/*     */     }
/*     */     
/*     */ 
/* 661 */     if (((expression instanceof PrefixExpression)) && 
/* 662 */       (((PrefixExpression)expression).getOperator() == PrefixExpression.Operator.NOT)) {
/* 663 */       return betaPlus(((PrefixExpression)expression).getOperand(), !negation);
/*     */     }
/*     */     
/* 666 */     if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.CONDITIONAL_AND) {
/* 667 */       return (short)(int)Math.max(betaPlus(((InfixExpression)expression).getLeftOperand(), negation), betaPlus(((InfixExpression)expression).getRightOperand(), negation));
/*     */     }
/* 669 */     if (((InfixExpression)expression).getOperator() == InfixExpression.Operator.CONDITIONAL_OR) {
/* 670 */       return (short)(int)Math.min(betaPlus(((InfixExpression)expression).getLeftOperand(), negation), betaPlus(((InfixExpression)expression).getRightOperand(), negation));
/*     */     }
/* 672 */     density = 1.0D * getNumberOfSolutions(expression) / getSearchSpaceSize(expression);
/* 673 */     double beta = Math.pow(2.0D, 16.0D) * (1.0D - density);
/*     */     
/* 675 */     return beta;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception
/*     */   {
/* 680 */     Expression expression = String2Expression.getExpression("X==Y");
/* 681 */     System.out.println("alpha=    " + alpha(expression));
/* 682 */     System.out.println("beta=    " + beta(expression));
/* 683 */     System.out.println("betaPlus=    " + betaPlus(expression));
/* 684 */     System.out.println("getSearchSpaceSize=    " + getNumberOfVariables(expression));
/* 685 */     System.out.println("getDifficultyCoefficientPlus=    " + getDifficultyLevelPlus(expression));
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\DifficultyCoefficient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */