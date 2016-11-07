/*     */ package csbst.testing;
/*     */ 
/*     */ import csbst.analysis.String2Expression;
/*     */ import java.util.Map;
/*     */ import org.eclipse.jdt.core.dom.BooleanLiteral;
/*     */ import org.eclipse.jdt.core.dom.Expression;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BranchDistance
/*     */ {
/*  25 */   private static int K = 10;
/*     */   
/*     */   private static int expressionEvaluator(Expression expression, Map<String, Object> varsValues) throws Exception
/*     */   {
/*  29 */     if ((expression instanceof ParenthesizedExpression)) {
/*  30 */       return expressionEvaluator(((ParenthesizedExpression)expression).getExpression(), varsValues);
/*     */     }
/*     */     
/*  33 */     if (((expression instanceof NullLiteral)) || ((expression instanceof NumberLiteral)) || ((expression instanceof SimpleName)) || 
/*  34 */       ((expression instanceof QualifiedName)) || ((expression instanceof MethodInvocation))) {
/*  35 */       return ((Integer)varsValues.get(expression.toString())).intValue();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  40 */     if (!(expression instanceof InfixExpression)) {
/*  41 */       throw new Exception("Exepression cannot be expressed as a constraint : " + expression.toString());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  46 */     Expression leftEx = ((InfixExpression)expression).getLeftOperand();
/*  47 */     Expression rightEx = ((InfixExpression)expression).getRightOperand();
/*  48 */     InfixExpression.Operator op = ((InfixExpression)expression).getOperator();
/*     */     
/*  50 */     if ((expression instanceof InfixExpression)) {
/*  51 */       if (op == InfixExpression.Operator.DIVIDE) {
/*  52 */         return expressionEvaluator(leftEx, varsValues) / expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  54 */       if (op == InfixExpression.Operator.MINUS) {
/*  55 */         return expressionEvaluator(leftEx, varsValues) - expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  57 */       if (op == InfixExpression.Operator.PLUS) {
/*  58 */         return expressionEvaluator(leftEx, varsValues) + expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  60 */       if (op == InfixExpression.Operator.REMAINDER) {
/*  61 */         return expressionEvaluator(leftEx, varsValues) % expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  63 */       if (op == InfixExpression.Operator.TIMES) {
/*  64 */         return expressionEvaluator(leftEx, varsValues) * expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  66 */       if (op == InfixExpression.Operator.AND) {
/*  67 */         return expressionEvaluator(leftEx, varsValues) & expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  69 */       if (op == InfixExpression.Operator.OR) {
/*  70 */         return expressionEvaluator(leftEx, varsValues) | expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  72 */       if (op == InfixExpression.Operator.XOR) {
/*  73 */         return expressionEvaluator(leftEx, varsValues) ^ expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  75 */       if (op == InfixExpression.Operator.LEFT_SHIFT) {
/*  76 */         return expressionEvaluator(leftEx, varsValues) << expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  78 */       if (op == InfixExpression.Operator.RIGHT_SHIFT_SIGNED) {
/*  79 */         return expressionEvaluator(leftEx, varsValues) >> expressionEvaluator(rightEx, varsValues);
/*     */       }
/*  81 */       if (op == InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED) {
/*  82 */         return expressionEvaluator(leftEx, varsValues) >>> expressionEvaluator(rightEx, varsValues);
/*     */       }
/*     */       
/*  85 */       throw new Exception("Exepression Contains unsupported operator : " + expression.toString());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  90 */     throw new Exception("Unsported expression type : " + expression.toString());
/*     */   }
/*     */   
/*     */   public static int getBranchDistance(String expression)
/*     */     throws Exception
/*     */   {
/*  96 */     return getBranchDistance(String2Expression.getExpression(expression), false);
/*     */   }
/*     */   
/*     */   public static int getBranchDistance(Expression expression) throws Exception {
/* 100 */     return getBranchDistance(expression, false);
/*     */   }
/*     */   
/*     */   public static int getBranchDistance(Expression expression, boolean negation) throws Exception
/*     */   {
/* 105 */     if ((expression instanceof ParenthesizedExpression)) {
/* 106 */       return getBranchDistance(((ParenthesizedExpression)expression).getExpression(), negation);
/*     */     }
/*     */     
/*     */ 
/* 110 */     if (((expression instanceof PrefixExpression)) && 
/* 111 */       (((PrefixExpression)expression).getOperator() == PrefixExpression.Operator.NOT)) {
/* 112 */       return getBranchDistance(((PrefixExpression)expression).getOperand(), !negation);
/*     */     }
/*     */     
/* 115 */     if (!(expression instanceof InfixExpression)) {
/* 116 */       throw new Exception("Exepression cannot be expressed as a constraint : " + expression.toString());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 121 */     Expression leftEx = ((InfixExpression)expression).getLeftOperand();
/* 122 */     Expression rightEx = ((InfixExpression)expression).getRightOperand();
/* 123 */     InfixExpression.Operator op = ((InfixExpression)expression).getOperator();
/*     */     
/* 125 */     if (((op == InfixExpression.Operator.CONDITIONAL_AND) && (!negation)) || ((op == InfixExpression.Operator.CONDITIONAL_OR) && (negation))) {
/* 126 */       return getBranchDistance(leftEx, negation) + getBranchDistance(rightEx, negation);
/*     */     }
/* 128 */     if (((op == InfixExpression.Operator.CONDITIONAL_OR) && (!negation)) || ((op == InfixExpression.Operator.CONDITIONAL_AND) && (negation))) {
/* 129 */       return Math.min(getBranchDistance(leftEx, negation), getBranchDistance(rightEx, negation));
/*     */     }
/*     */     
/* 132 */     int leftVal = Integer.parseInt(leftEx.toString().equals("null") ? "0" : leftEx.toString());
/* 133 */     int rigthVal = Integer.parseInt(rightEx.toString().equals("null") ? "0" : rightEx.toString());
/* 134 */     int distance = 0;
/*     */     
/* 136 */     if (((op == InfixExpression.Operator.EQUALS) && (!negation)) || ((op == InfixExpression.Operator.NOT_EQUALS) && (negation))) {
/* 137 */       if (leftVal == rigthVal) {
/* 138 */         distance = 0;
/*     */       } else {
/* 140 */         distance = Math.abs(leftVal - rigthVal) + K;
/*     */       }
/*     */     }
/* 143 */     else if (((op == InfixExpression.Operator.NOT_EQUALS) && (!negation)) || ((op == InfixExpression.Operator.EQUALS) && (negation))) {
/* 144 */       if (leftVal != rigthVal) {
/* 145 */         distance = 0;
/*     */       } else {
/* 147 */         distance = K;
/*     */       }
/*     */     }
/* 150 */     else if (((op == InfixExpression.Operator.GREATER) && (!negation)) || ((op == InfixExpression.Operator.LESS_EQUALS) && (negation))) {
/* 151 */       if (leftVal > rigthVal) {
/* 152 */         distance = 0;
/*     */       } else {
/* 154 */         distance = rigthVal - leftVal + K;
/*     */       }
/*     */     }
/* 157 */     else if (((op == InfixExpression.Operator.LESS_EQUALS) && (!negation)) || ((op == InfixExpression.Operator.GREATER) && (negation))) {
/* 158 */       if (leftVal <= rigthVal) {
/* 159 */         distance = 0;
/*     */       } else {
/* 161 */         distance = leftVal - rigthVal + K;
/*     */       }
/*     */       
/*     */     }
/* 165 */     else if (((op == InfixExpression.Operator.GREATER_EQUALS) && (!negation)) || ((op == InfixExpression.Operator.LESS) && (negation))) {
/* 166 */       if (leftVal >= rigthVal) {
/* 167 */         distance = 0;
/*     */       } else {
/* 169 */         distance = rigthVal - leftVal + K;
/*     */       }
/*     */     }
/* 172 */     else if (((op == InfixExpression.Operator.LESS) && (!negation)) || ((op == InfixExpression.Operator.GREATER_EQUALS) && (negation))) {
/* 173 */       if (leftVal < rigthVal) {
/* 174 */         distance = 0;
/*     */       } else {
/* 176 */         distance = leftVal - rigthVal + K;
/*     */       }
/*     */     }
/*     */     else {
/* 180 */       throw new Exception(" Exepression contains unsupported operator : " + expression.toString());
/*     */     }
/* 182 */     return distance;
/*     */   }
/*     */   
/*     */   public static double normalize(int distance) {
/* 186 */     return 1.0D * distance / (distance + 1);
/*     */   }
/*     */   
/*     */   public static String expressionFormater(Expression expression) throws Exception
/*     */   {
/* 191 */     if ((expression instanceof ParenthesizedExpression)) {
/* 192 */       return "\"(\"+" + expressionFormater(((ParenthesizedExpression)expression).getExpression()) + "+\")\"";
/*     */     }
/*     */     
/* 195 */     if (((expression instanceof PrefixExpression)) && 
/* 196 */       (((PrefixExpression)expression).getOperator() == PrefixExpression.Operator.NOT)) {
/* 197 */       return "\"!\"+" + expressionFormater(((PrefixExpression)expression).getOperand());
/*     */     }
/*     */     
/* 200 */     if (((expression instanceof NullLiteral)) || ((expression instanceof BooleanLiteral)) || ((expression instanceof NumberLiteral)) || ((expression instanceof SimpleName)) || ((expression instanceof QualifiedName)) || ((expression instanceof MethodInvocation))) {
/* 201 */       return expression.toString();
/*     */     }
/*     */     
/* 204 */     if (!(expression instanceof InfixExpression)) {
/* 205 */       throw new Exception("Exepression cannot be expressed as a constraint : " + expression.toString());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 210 */     Expression leftEx = ((InfixExpression)expression).getLeftOperand();
/* 211 */     Expression rightEx = ((InfixExpression)expression).getRightOperand();
/* 212 */     InfixExpression.Operator op = ((InfixExpression)expression).getOperator();
/*     */     
/* 214 */     if ((op == InfixExpression.Operator.CONDITIONAL_AND) || (op == InfixExpression.Operator.CONDITIONAL_OR) || 
/* 215 */       (op == InfixExpression.Operator.EQUALS) || (op == InfixExpression.Operator.GREATER) || 
/* 216 */       (op == InfixExpression.Operator.GREATER_EQUALS) || (op == InfixExpression.Operator.LESS) || 
/* 217 */       (op == InfixExpression.Operator.LESS_EQUALS) || (op == InfixExpression.Operator.NOT_EQUALS)) {
/* 218 */       return expressionFormater(leftEx) + "+\"" + op + "\"+" + expressionFormater(rightEx);
/*     */     }
/* 220 */     return "(" + expression.toString() + ")";
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\BranchDistance.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */