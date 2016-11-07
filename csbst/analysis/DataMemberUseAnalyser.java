/*     */ package csbst.analysis;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*     */ import org.eclipse.jdt.core.dom.Assignment;
/*     */ import org.eclipse.jdt.core.dom.FieldAccess;
/*     */ import org.eclipse.jdt.core.dom.FieldDeclaration;
/*     */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.SimpleName;
/*     */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclaration;
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
/*     */ public class DataMemberUseAnalyser
/*     */   extends ASTVisitor
/*     */ {
/*  43 */   private Map<IVariableBinding, Set<Integer>> dataMemberTransformers = new HashMap();
/*     */   
/*     */   public Map<IVariableBinding, Set<Integer>> getDataMemberBranchTransformersMap() {
/*  46 */     return this.dataMemberTransformers;
/*     */   }
/*     */   
/*     */   private int getBranch(Assignment node)
/*     */   {
/*  51 */     ASTNode branch = node;
/*  52 */     while (branch.getProperty("numberBranch") == null) {
/*  53 */       branch = branch.getParent();
/*  54 */       if (branch == null) {
/*  55 */         return -1;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  60 */     return ((Integer)branch.getProperty("numberBranch")).intValue();
/*     */   }
/*     */   
/*     */   private int getBranch(MethodInvocation node)
/*     */   {
/*  65 */     ASTNode branch = node;
/*  66 */     while (branch.getProperty("numberBranch") == null)
/*     */     {
/*  68 */       branch = branch.getParent();
/*  69 */       if ((branch instanceof TypeDeclaration)) {
/*  70 */         return -1;
/*     */       }
/*     */     }
/*     */     
/*  74 */     return ((Integer)branch.getProperty("numberBranch")).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean visit(Assignment node)
/*     */   {
/*  82 */     if ((!(node.getLeftHandSide() instanceof SimpleName)) && (!(node.getLeftHandSide() instanceof FieldAccess)))
/*  83 */       return false;
/*     */     SimpleName sN;
/*     */   
/*  86 */     if ((node.getLeftHandSide() instanceof SimpleName)) {
/*  87 */       sN = (SimpleName)node.getLeftHandSide();
/*     */     } else {
/*  89 */       sN = ((FieldAccess)node.getLeftHandSide()).getName();
/*     */     }
/*  91 */     if (!(sN.resolveBinding() instanceof IVariableBinding)) {
/*  92 */       return false;
/*     */     }
/*  94 */     IVariableBinding iVB = (IVariableBinding)sN.resolveBinding();
/*  95 */     if ((iVB != null) && (iVB.isField())) {
/*  96 */       iVB = iVB.getVariableDeclaration();
/*  97 */       Set<Integer> tmpSet = (Set)this.dataMemberTransformers.get(iVB);
/*  98 */       if (tmpSet != null) {
/*  99 */         int branch = getBranch(node);
/* 100 */         if (branch != -1) {
/* 101 */           tmpSet.add(Integer.valueOf(branch));
/*     */         }
/*     */       }
/*     */     }
/* 105 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(MethodInvocation node)
/*     */   {
/* 111 */     if ((!(node.getExpression() instanceof SimpleName)) && (!(node.getExpression() instanceof FieldAccess)))
/* 112 */       return false;
/*    
/* 114 */     SimpleName sN; if ((node.getExpression() instanceof SimpleName)) {
/* 115 */       sN = (SimpleName)node.getExpression();
/*     */     } else {
/* 117 */       sN = ((FieldAccess)node.getExpression()).getName();
/*     */     }
/* 119 */     if (!(sN.resolveBinding() instanceof IVariableBinding)) {
/* 120 */       return false;
/*     */     }
/* 122 */     IVariableBinding iVB = (IVariableBinding)sN.resolveBinding();
/* 123 */     if ((iVB != null) && (iVB.isField())) {
/* 124 */       iVB = iVB.getVariableDeclaration();
/*     */       
/*     */ 
/* 127 */       Set<Integer> tmpSet = (Set)this.dataMemberTransformers.get(iVB.getVariableDeclaration());
/* 128 */       if (tmpSet != null) {
/* 129 */         int branch = getBranch(node);
/* 130 */         if (branch != -1) {
/* 131 */           tmpSet.add(Integer.valueOf(branch));
/*     */         }
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
/* 143 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean visit(TypeDeclaration node)
/*     */   {
/* 152 */     for (int i = node.getFields().length - 1; i >= 0; i--) {
/* 153 */       for (int j = node.getFields()[i].fragments().size() - 1; j >= 0; j--)
/*     */       {
/* 155 */         if (((VariableDeclaration)node.getFields()[i].fragments().get(j)).resolveBinding() != null) {
/* 156 */           Set<Integer> tmpSet = new HashSet();
/* 157 */           this.dataMemberTransformers.put(((VariableDeclaration)node.getFields()[i].fragments().get(j)).resolveBinding(), tmpSet);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 162 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\DataMemberUseAnalyser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */