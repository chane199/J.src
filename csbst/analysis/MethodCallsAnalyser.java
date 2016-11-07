/*    */ package csbst.analysis;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.eclipse.jdt.core.dom.ASTNode;
/*    */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*    */ import org.eclipse.jdt.core.dom.IMethodBinding;
/*    */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*    */ import org.eclipse.jdt.core.dom.Modifier;
/*    */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodCallsAnalyser
/*    */   extends ASTVisitor
/*    */ {
/*    */   private TypeDeclaration classNode;
/* 37 */   private Map<IMethodBinding, Set<Integer>> methodCallers = new HashMap();
/*    */   
/*    */   public Map<IMethodBinding, Set<Integer>> getMethodBranchCallersMap() {
/* 40 */     return this.methodCallers;
/*    */   }
/*    */   
/*    */   private int getBranch(MethodInvocation node)
/*    */   {
/* 45 */     ASTNode branch = node;
/* 46 */     while (branch.getProperty("numberBranch") == null)
/*    */     {
/* 48 */       branch = branch.getParent();
/* 49 */       if ((branch instanceof TypeDeclaration)) {
/* 50 */         return -1;
/*    */       }
/*    */     }
/*    */     
/* 54 */     return ((Integer)branch.getProperty("numberBranch")).intValue();
/*    */   }
/*    */   
/*    */   public Set<Integer> getBranchesCall(IMethodBinding method) {
/* 58 */     return (Set)this.methodCallers.get(method);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean visit(TypeDeclaration node)
/*    */   {
/* 64 */     if ((node.isLocalTypeDeclaration()) || (node.isMemberTypeDeclaration())) {
/* 65 */       return true;
/*    */     }
/* 67 */     this.classNode = node;
/* 68 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean visit(MethodInvocation node)
/*    */   {
/* 74 */     if (node.resolveMethodBinding() != null)
/*    */     {
/* 76 */       IMethodBinding method = node.resolveMethodBinding().getMethodDeclaration();
/* 77 */       if (method.getName().toString().equals("setNext")) {
/* 78 */         method = method;
/*    */       }
/* 80 */       if (!Modifier.isPrivate(method.getModifiers())) {
/* 81 */         return true;
/*    */       }
/* 83 */       int branchCaller = getBranch(node);
/* 84 */       if (branchCaller > -1)
/*    */       {
/* 86 */         if (!this.methodCallers.containsKey(method)) {
/* 87 */           this.methodCallers.put(method, new HashSet());
/*    */         }
/* 89 */         ((Set)this.methodCallers.get(method)).add(Integer.valueOf(branchCaller));
/*    */       }
/*    */     }
/*    */     
/* 93 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\MethodCallsAnalyser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */