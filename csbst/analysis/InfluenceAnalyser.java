/*     */ package csbst.analysis;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.ASTNode;
/*     */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*     */ import org.eclipse.jdt.core.dom.Assignment;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.DoStatement;
/*     */ import org.eclipse.jdt.core.dom.Expression;
/*     */ import org.eclipse.jdt.core.dom.FieldAccess;
/*     */ import org.eclipse.jdt.core.dom.FieldDeclaration;
/*     */ import org.eclipse.jdt.core.dom.ForStatement;
/*     */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*     */ import org.eclipse.jdt.core.dom.IfStatement;
/*     */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*     */ import org.eclipse.jdt.core.dom.SimpleName;
/*     */ import org.eclipse.jdt.core.dom.SwitchStatement;
/*     */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclaration;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
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
/*     */ public class InfluenceAnalyser
/*     */   extends ASTVisitor
/*     */ {
/*     */   private TypeDeclaration classNode;
/*  43 */   private Map<IVariableBinding, Set<IVariableBinding>> directIfluencePropagators = new HashMap();
/*  44 */   private Set<IVariableBinding> indirectIfluencer = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void influencePorterChecker(SimpleName sN, Expression exp)
/*     */   {
/*  53 */     final IVariableBinding variableName = (IVariableBinding)sN.resolveBinding();
/*     */     
/*  55 */     for (IVariableBinding V : this.indirectIfluencer) {
/*  56 */       ((Set)this.directIfluencePropagators.get(V)).add(variableName);
/*     */     }
/*  58 */     exp.accept(new ASTVisitor() {
/*     */       public boolean visit(SimpleName sN) {
/*  60 */         if (sN.resolveBinding() != null) {
/*  61 */           for (Map.Entry<IVariableBinding, Set<IVariableBinding>> entry : InfluenceAnalyser.this.directIfluencePropagators.entrySet())
/*  62 */             if (((Set)entry.getValue()).contains(sN.resolveBinding()))
/*  63 */               ((Set)entry.getValue()).add(variableName);
/*     */         }
/*  65 */         return true;
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void maintainBranchInfluenceVectors(final Block node, final Set<IVariableBinding> indirectInfluencerSet)
/*     */   {
/*  78 */     final Vector<IVariableBinding> parmsInf = new Vector();
/*  79 */     node.getProperty("inDirectIfluencePropagators");
/*  80 */     Expression exp = (Expression)node.getParent().getProperty("expression");
/*  81 */     if (exp == null) {
/*  82 */       return;
/*     */     }
/*  84 */     exp.accept(new ASTVisitor() {
/*     */       private void analyse(SimpleName sN) {
/*  86 */         if (sN.resolveBinding() != null) {
/*  87 */           Map<IVariableBinding, Set<IVariableBinding>> tmpDIP = (Map)node.getParent().getProperty("inDirectIfluencePropagators");
/*  88 */           for (Map.Entry<IVariableBinding, Set<IVariableBinding>> entry : tmpDIP.entrySet())
/*  89 */             if (((Set)entry.getValue()).contains(sN.resolveBinding())) {
/*  90 */               parmsInf.add((IVariableBinding)entry.getKey());
/*  91 */               ((Set)entry.getValue()).add((IVariableBinding)sN.resolveBinding());
/*  92 */               if (!InfluenceAnalyser.this.indirectIfluencer.contains(entry.getKey()))
/*  93 */                 indirectInfluencerSet.add((IVariableBinding)entry.getKey());
/*  94 */               InfluenceAnalyser.this.indirectIfluencer.add((IVariableBinding)entry.getKey());
/*     */             }
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean visit(SimpleName sN) {
/* 100 */         analyse(sN);
/* 101 */         return true;
/*     */       }
/*     */       
/* 104 */       public boolean visit(FieldAccess fA) { SimpleName sN = fA.getName();
/* 105 */         analyse(sN);
/* 106 */         return true;
/*     */       }
/*     */       
/* 109 */     });
/* 110 */     Set<IVariableBinding> influencers = new HashSet();
/* 111 */     node.setProperty("influencers", parmsInf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean visit(Assignment node)
/*     */   {
/* 118 */     if ((!(node.getLeftHandSide() instanceof SimpleName)) && (!(node.getLeftHandSide() instanceof FieldAccess)))
/* 119 */       return false;
/*     */     SimpleName sN;
/*     */     SimpleName sN;
/* 122 */     if ((node.getLeftHandSide() instanceof SimpleName)) {
/* 123 */       sN = (SimpleName)node.getLeftHandSide();
/*     */     } else {
/* 125 */       sN = ((FieldAccess)node.getLeftHandSide()).getName();
/*     */     }
/* 127 */     influencePorterChecker(sN, node.getRightHandSide());
/*     */     
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(TypeDeclaration node)
/*     */   {
/* 134 */     this.classNode = node;
/*     */     
/* 136 */     for (int i = this.classNode.getFields().length; i > 0; i--) {
/* 137 */       for (int j = this.classNode.getFields()[(i - 1)].fragments().size(); j > 0; j--) {
/* 138 */         Set<IVariableBinding> tmpSet = new HashSet();
/* 139 */         IVariableBinding field = ((VariableDeclaration)this.classNode.getFields()[(i - 1)].fragments().get(j - 1)).resolveBinding();
/* 140 */         tmpSet.add(field);
/* 141 */         this.directIfluencePropagators.put(field, tmpSet);
/*     */       }
/*     */     }
/*     */     
/* 145 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(MethodDeclaration node)
/*     */   {
/* 151 */     for (int i = node.parameters().size(); i > 0; i--) {
/* 152 */       Set<IVariableBinding> tmpSet = new HashSet();
/* 153 */       IVariableBinding parameter = ((VariableDeclaration)node.parameters().get(i - 1)).resolveBinding();
/* 154 */       tmpSet.add(parameter);
/* 155 */       this.directIfluencePropagators.put(parameter, tmpSet);
/*     */     }
/* 157 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void endVisit(MethodDeclaration node)
/*     */   {
/* 163 */     for (int i = node.parameters().size(); i > 0; i--) {
/* 164 */       IVariableBinding parameter = ((VariableDeclaration)node.parameters().get(i - 1)).resolveBinding();
/* 165 */       this.directIfluencePropagators.remove(parameter);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(Block node)
/*     */   {
/* 172 */     if (node.getProperty("numberBranch") != null)
/*     */     {
/*     */ 
/*     */ 
/* 176 */       Set<IVariableBinding> indirectInfluencerSet = new HashSet();
/* 177 */       node.setProperty("indirectInfluencerSet", indirectInfluencerSet);
/*     */       
/*     */ 
/* 180 */       if (node.getParent().getProperty("isMultiBranchesRoot") == "YES") {
/* 181 */         this.directIfluencePropagators.clear();
/* 182 */         for (Map.Entry<IVariableBinding, Set<IVariableBinding>> entry : ((Map)node.getParent().getProperty("inDirectIfluencePropagators")).entrySet()) {
/* 183 */           this.directIfluencePropagators.put((IVariableBinding)entry.getKey(), new HashSet((Collection)entry.getValue()));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 189 */       if (node.getParent().getProperty("isMultiBranchesRoot") == "YES") {
/* 190 */         maintainBranchInfluenceVectors(node, indirectInfluencerSet);
/*     */       }
/*     */     }
/* 193 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void endVisit(Block node)
/*     */   {
/* 199 */     if (node.getProperty("numberBranch") != null) {
/* 200 */       Set<IVariableBinding> indirectInfluencerSet = (Set)node.getProperty("indirectInfluencerSet");
/* 201 */       this.indirectIfluencer.removeAll(indirectInfluencerSet);
/*     */       
/*     */ 
/* 204 */       if (node.getParent().getProperty("isMultiBranchesRoot") == "YES") {
/* 205 */         for (Map.Entry<IVariableBinding, Set<IVariableBinding>> entry : this.directIfluencePropagators.entrySet()) {
/* 206 */           ((Set)((Map)node.getParent().getProperty("outDirectIfluencePropagators")).get(entry.getKey())).addAll((Collection)entry.getValue());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean visit(VariableDeclarationFragment node)
/*     */   {
/* 218 */     if (node.getInitializer() != null) {
/* 219 */       influencePorterChecker(node.getName(), node.getInitializer());
/*     */     }
/* 221 */     return true;
/*     */   }
/*     */   
/*     */   private void initializeMultiBranchesRoot(ASTNode node)
/*     */   {
/* 226 */     Map<IVariableBinding, Set<IVariableBinding>> inDirectIfluencePropagators = new HashMap();
/* 227 */     for (Map.Entry<IVariableBinding, Set<IVariableBinding>> entry : this.directIfluencePropagators.entrySet()) {
/* 228 */       inDirectIfluencePropagators.put((IVariableBinding)entry.getKey(), new HashSet((Collection)entry.getValue()));
/*     */     }
/* 230 */     node.setProperty("inDirectIfluencePropagators", inDirectIfluencePropagators);
/*     */     
/* 232 */     Map<IVariableBinding, Set<IVariableBinding>> outDirectIfluencePropagators = new HashMap();
/* 233 */     for (Object entry : this.directIfluencePropagators.entrySet()) {
/* 234 */       outDirectIfluencePropagators.put((IVariableBinding)((Map.Entry)entry).getKey(), new HashSet((Collection)((Map.Entry)entry).getValue()));
/*     */     }
/* 236 */     node.setProperty("outDirectIfluencePropagators", outDirectIfluencePropagators);
/*     */   }
/*     */   
/*     */   private void endOfMultiBranchesRoot(ASTNode node) {
/* 240 */     this.directIfluencePropagators.clear();
/* 241 */     this.directIfluencePropagators.putAll((Map)node.getProperty("outDirectIfluencePropagators"));
/*     */   }
/*     */   
/*     */   public boolean visit(IfStatement node) {
/* 245 */     if (node.getProperty("isMultiBranchesRoot") != "YES")
/* 246 */       return true;
/* 247 */     initializeMultiBranchesRoot(node);
/*     */     
/* 249 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void endVisit(IfStatement node)
/*     */   {
/* 255 */     if (node.getProperty("isMultiBranchesRoot") != "YES")
/* 256 */       return;
/* 257 */     endOfMultiBranchesRoot(node);
/*     */   }
/*     */   
/*     */   public boolean visit(SwitchStatement node)
/*     */   {
/* 262 */     initializeMultiBranchesRoot(node);
/* 263 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(SwitchStatement node)
/*     */   {
/* 268 */     endOfMultiBranchesRoot(node);
/*     */   }
/*     */   
/*     */   public boolean visit(ForStatement node)
/*     */   {
/* 273 */     initializeMultiBranchesRoot(node);
/* 274 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(ForStatement node)
/*     */   {
/* 279 */     endOfMultiBranchesRoot(node);
/*     */   }
/*     */   
/*     */   public boolean visit(WhileStatement node)
/*     */   {
/* 284 */     initializeMultiBranchesRoot(node);
/* 285 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(WhileStatement node)
/*     */   {
/* 290 */     endOfMultiBranchesRoot(node);
/*     */   }
/*     */   
/*     */   public boolean visit(DoStatement node)
/*     */   {
/* 295 */     initializeMultiBranchesRoot(node);
/* 296 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(DoStatement node)
/*     */   {
/* 301 */     endOfMultiBranchesRoot(node);
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\InfluenceAnalyser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */