/*     */ package csbst.analysis;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*     */ import org.eclipse.jdt.core.dom.CastExpression;
/*     */ import org.eclipse.jdt.core.dom.CharacterLiteral;
/*     */ import org.eclipse.jdt.core.dom.ITypeBinding;
/*     */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*     */ import org.eclipse.jdt.core.dom.NumberLiteral;
/*     */ import org.eclipse.jdt.core.dom.SimpleName;
/*     */ import org.eclipse.jdt.core.dom.StringLiteral;
/*     */ import org.eclipse.jdt.core.dom.ThrowStatement;
/*     */ import org.eclipse.jdt.core.dom.Type;
/*     */ 
/*     */ public class LittralConstantAnalyser extends ASTVisitor
/*     */ {
/*  19 */   private Map<IVariableBinding, java.util.Set<Integer>> dataMemberTransformers = new java.util.HashMap();
/*     */   
/*  21 */   private Vector<String> stringConstants = new Vector();
/*  22 */   private Vector<Character> characterConstants = new Vector();
/*  23 */   private Vector<Byte> byteConstants = new Vector();
/*  24 */   private Vector<Short> shortConstants = new Vector();
/*  25 */   private Vector<Integer> integerConstants = new Vector();
/*  26 */   private Vector<Long> longConstants = new Vector();
/*  27 */   private Vector<Float> floatConstants = new Vector();
/*  28 */   private Vector<Double> doubleConstants = new Vector();
/*     */   
/*  30 */   private Vector<ITypeBinding> castConstants = new Vector();
/*     */   
/*     */   public Vector<ITypeBinding> getCastConstants() {
/*  33 */     return this.castConstants;
/*     */   }
/*     */   
/*     */   public Vector<Byte> getByteConstants()
/*     */   {
/*  38 */     return this.byteConstants;
/*     */   }
/*     */   
/*     */   public Vector<Short> getShortConstants()
/*     */   {
/*  43 */     return this.shortConstants;
/*     */   }
/*     */   
/*     */   public Vector<Integer> getIntegerConstants() {
/*  47 */     return this.integerConstants;
/*     */   }
/*     */   
/*     */   public Vector<Long> getLongConstants() {
/*  51 */     return this.longConstants;
/*     */   }
/*     */   
/*     */   public Vector<Float> getFloatConstants() {
/*  55 */     return this.floatConstants;
/*     */   }
/*     */   
/*     */   public Vector<Double> getDoubleConstants() {
/*  59 */     return this.doubleConstants;
/*     */   }
/*     */   
/*     */   public Vector<String> getStringConstants() {
/*  63 */     return this.stringConstants;
/*     */   }
/*     */   
/*     */   public Vector<Character> getCharacterConstants() {
/*  67 */     return this.characterConstants;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(CastExpression node)
/*     */   {
/*  73 */     this.castConstants.add(node.getType().resolveBinding());
/*  74 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(StringLiteral node)
/*     */   {
/*  79 */     if ((!(node.getParent().getParent() instanceof ThrowStatement)) && 
/*  80 */       (!(node.getParent() instanceof ThrowStatement)))
/*  81 */       this.stringConstants.add(node.getLiteralValue());
/*  82 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(CharacterLiteral node)
/*     */   {
/*  88 */     this.characterConstants.add(Character.valueOf(node.charValue()));
/*  89 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(SimpleName node)
/*     */   {
/*  95 */     if ((node.resolveConstantExpressionValue() != null) && (node.resolveTypeBinding() != null) && (!node.resolveConstantExpressionValue().toString().equalsIgnoreCase("NaN"))) {
/*  96 */       String className = node.resolveTypeBinding().getBinaryName();
/*  97 */       insert(className, node.resolveConstantExpressionValue().toString());
/*     */     }
/*  99 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(NumberLiteral node)
/*     */   {
/* 105 */     String className = node.resolveTypeBinding().getBinaryName();
/* 106 */     if ((node.resolveConstantExpressionValue() != null) && (className != null)) {
/* 107 */       insert(className, node.resolveConstantExpressionValue().toString());
/*     */     }
/* 109 */     return true;
/*     */   }
/*     */   
/*     */   private void insert(String className, String value) {
/* 113 */     if ((className.equals("B")) || (className.equalsIgnoreCase("java.lang.Byte"))) {
/* 114 */       this.byteConstants.add(Byte.valueOf(Byte.parseByte(value)));
/* 115 */       this.byteConstants.add(Byte.valueOf((byte)(Byte.parseByte(value) + 1)));
/* 116 */       this.byteConstants.add(Byte.valueOf((byte)(Byte.parseByte(value) - 1)));
/*     */     }
/* 118 */     if ((className.equals("S")) || (className.equalsIgnoreCase("java.lang.Short"))) {
/* 119 */       this.shortConstants.add(Short.valueOf(Short.parseShort(value)));
/* 120 */       this.shortConstants.add(Short.valueOf((short)(Short.parseShort(value) + 1)));
/* 121 */       this.shortConstants.add(Short.valueOf((short)(Short.parseShort(value) - 1)));
/*     */     }
/* 123 */     if ((className.equals("I")) || (className.equalsIgnoreCase("java.lang.Integer"))) {
/* 124 */       this.integerConstants.add(Integer.valueOf(Integer.parseInt(value)));
/* 125 */       this.integerConstants.add(Integer.valueOf(Integer.parseInt(value) + 1));
/* 126 */       this.integerConstants.add(Integer.valueOf(Integer.parseInt(value) - 1));
/*     */     }
/* 128 */     if ((className.equals("J")) || (className.equalsIgnoreCase("java.lang.Long"))) {
/* 129 */       this.longConstants.add(Long.valueOf(Long.parseLong(value)));
/* 130 */       this.longConstants.add(Long.valueOf(Long.parseLong(value) + 1L));
/* 131 */       this.longConstants.add(Long.valueOf(Long.parseLong(value) - 1L));
/*     */     }
/* 133 */     if ((className.equals("F")) || (className.equalsIgnoreCase("java.lang.Float"))) {
/* 134 */       this.floatConstants.add(Float.valueOf(Float.parseFloat(value)));
/* 135 */       this.floatConstants.add(Float.valueOf(Float.parseFloat(value) + 1.0F));
/* 136 */       this.floatConstants.add(Float.valueOf(Float.parseFloat(value) - 1.0F));
/*     */     }
/* 138 */     if ((className.equals("D")) || (className.equalsIgnoreCase("java.lang.Double"))) {
/* 139 */       this.doubleConstants.add(Double.valueOf(Double.parseDouble(value)));
/* 140 */       this.doubleConstants.add(Double.valueOf(Double.parseDouble(value) + 1.0D));
/* 141 */       this.doubleConstants.add(Double.valueOf(Double.parseDouble(value) - 1.0D));
/*     */     }
/* 143 */     if (className.equalsIgnoreCase("java.lang.String")) {
/* 144 */       this.stringConstants.add(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\LittralConstantAnalyser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */