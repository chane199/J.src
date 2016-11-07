/*     */ package csbst.generators.dynamic;
/*     */ 
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.testing.JTE;
/*     */ import csbst.utils.ClassLoaderUtil;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.Statement;
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
/*     */ public class GenericTypeGenerator
/*     */   extends AbsractGenerator
/*     */ {
/*     */   private Vector<AbsractGenerator> components;
/*     */   private ParameterizedType componentsTypes;
/*     */   
/*     */   public GenericTypeGenerator(AbsractGenerator parent, ParameterizedType types)
/*     */   {
/*  41 */     super(parent, null);
/*  42 */     this.componentsTypes = types;
/*     */     
/*     */ 
/*  45 */     if (this.componentsTypes.getActualTypeArguments().length == 0) {
/*  46 */       AbsractGenerator gene = createAdequateGene(this, Object.class);
/*  47 */       this.components.add(gene);
/*     */     }
/*     */     else {
/*  50 */       Class cls = null;
/*  51 */       Type[] arrayOfType; int j = (arrayOfType = this.componentsTypes.getActualTypeArguments()).length; for (int i = 0; i < j; i++) { Type t = arrayOfType[i];
/*  52 */         if ((t instanceof ParameterizedType)) {
/*  53 */           AbsractGenerator gene = new GenericTypeGenerator(this, (ParameterizedType)t);
/*  54 */           this.components.add(gene);
/*     */         }
/*     */         else {
/*  57 */           String nclass = t.toString();
/*  58 */           String[] nclassHierarchy = nclass.split(" ");
/*  59 */           AbsractGenerator gene; if ((nclassHierarchy == null) || (nclassHierarchy.length < 1)) {
/*  60 */             gene = createAdequateGene(this, Object.class);
/*     */           } else {
/*  62 */             if (nclassHierarchy.length == 1) {
/*  63 */               nclass = ClassLoaderUtil.toCanonicalName(nclass);
/*     */             } else
/*  65 */               nclass = nclassHierarchy[1];
/*     */             try {
/*  67 */               cls = JTE.magicClassLoader.loadClass(nclass);
/*     */             } catch (ClassNotFoundException e) {
/*  69 */               e.printStackTrace();
/*     */             }
/*     */           }
/*  72 */           AbsractGenerator gene = createAdequateGene(this, cls);
/*  73 */           this.components.add(gene);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void generateRandom() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void mutate() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/*  91 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 101 */     return 0;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\dynamic\GenericTypeGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */