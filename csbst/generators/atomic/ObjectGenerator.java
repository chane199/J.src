/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.analysis.LittralConstantAnalyser;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.generators.dynamic.AbstractDynamicGenerator;
/*     */ import csbst.generators.dynamic.InstanceGenerator;
/*     */ import csbst.generators.dynamic.MethodGenerator;
/*     */ import csbst.testing.JTE;
/*     */ import csbst.utils.ClassLoaderUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.ITypeBinding;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.Type;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectGenerator
/*     */   extends AbsractGenerator
/*     */ {
/*     */   private AbsractGenerator actualGenarator;
/*     */   
/*     */   public ObjectGenerator(AbsractGenerator parent)
/*     */   {
/*  33 */     super(parent, Object.class);
/*  34 */     generateRandom();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  40 */     boolean returnValue = false;
/*  41 */     returnValue = gene instanceof ObjectGenerator;
/*  42 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  43 */     return returnValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void generateRandom()
/*     */   {
/*  52 */     if (((!(this.parent instanceof InstanceGenerator)) && 
/*  53 */       (!(this.parent instanceof MethodGenerator))) || (JTE.litteralConstantAnalyser.getCastConstants().size() < 1))
/*     */     {
/*  55 */       int index = this.random.nextInt(InstanceGenerator.defaultClassesSet.size());
/*  56 */       this.actualGenarator = createAdequateGene(this, (Class)InstanceGenerator.defaultClassesSet.get(index));
/*     */     }
/*     */     else
/*     */     {
/*  60 */       int index = this.random.nextInt(JTE.litteralConstantAnalyser.getCastConstants().size());
/*  61 */       String clsName = ClassLoaderUtil.toCanonicalName(((ITypeBinding)JTE.litteralConstantAnalyser.getCastConstants().get(index)).getBinaryName());
/*  62 */       Class cls = null;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/*  72 */         cls = JTE.magicClassLoader.loadClass(clsName);
/*  73 */         if ((cls == null) || (!AbstractDynamicGenerator.isAccessible(cls))) {
/*  74 */           JTE.litteralConstantAnalyser.getCastConstants().remove(index);
/*  75 */           generateRandom();
/*  76 */           return;
/*     */         }
/*     */       }
/*     */       catch (ClassNotFoundException e)
/*     */       {
/*  81 */         JTE.litteralConstantAnalyser.getCastConstants().remove(index);
/*  82 */         generateRandom();
/*  83 */         return;
/*     */       }
/*     */       
/*  86 */       this.actualGenarator = createAdequateGene(this, cls);
/*     */     }
/*     */     
/*  89 */     this.actualGenarator.generateRandom();
/*  90 */     this.clazz = Object.class;
/*     */   }
/*     */   
/*     */ 
/*     */   public void mutate()
/*     */   {
/*  96 */     generateRandom();
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 101 */     ObjectGenerator newObj = new ObjectGenerator(this.parent);
/* 102 */     newObj.clazz = this.clazz;
/* 103 */     newObj.variableBinding = this.variableBinding;
/* 104 */     newObj.fitness = this.fitness;
/* 105 */     newObj.object = this.object;
/* 106 */     newObj.seed = this.seed;
/* 107 */     newObj.random = this.random;
/*     */     
/* 109 */     newObj.actualGenarator = ((AbsractGenerator)this.actualGenarator.clone());
/* 110 */     return newObj;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getObject()
/*     */   {
/* 116 */     return this.actualGenarator.getObject();
/*     */   }
/*     */   
/*     */   public List<Statement> getStatements(AST ast, String varName, String pName)
/*     */   {
/* 121 */     List<Statement> returnList = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/* 125 */     String objectName = varName + pName + "O0";
/* 126 */     returnList.addAll(this.actualGenarator.getStatements(ast, objectName, ""));
/*     */     
/* 128 */     VariableDeclarationFragment varDec = ast.newVariableDeclarationFragment();
/* 129 */     varDec.setName(ast.newSimpleName(varName));
/*     */     
/* 131 */     varDec.setInitializer(ast.newSimpleName(objectName));
/* 132 */     VariableDeclarationStatement varDecStat = ast.newVariableDeclarationStatement(varDec);
/*     */     
/* 134 */     Type TypeName1 = getType2UseInJunitClass(this.clazz, ast);
/* 135 */     varDecStat.setType(TypeName1);
/* 136 */     returnList.add(varDecStat);
/*     */     
/* 138 */     return returnList;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 144 */     int hash = getClass().hashCode();
/*     */     
/* 146 */     hash = hash << 1 | hash >>> 31;
/*     */     
/* 148 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     return this.actualGenarator.toString();
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\ObjectGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */