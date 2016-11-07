/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.analysis.LittralConstantAnalyser;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import csbst.testing.JTE;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
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
/*     */ public class FloatGenerator
/*     */   extends AbstractPrimitive<Float>
/*     */ {
/*     */   public FloatGenerator(AbsractGenerator parent, Class cls)
/*     */   {
/*  24 */     super(parent, cls);
/*  25 */     this.absolutuBound = Float.valueOf(Float.MAX_VALUE);
/*  26 */     this.absolutlBound = Float.valueOf(Float.MIN_VALUE);
/*  27 */     this.uBound = ((Float)this.absolutuBound);
/*  28 */     this.lBound = ((Float)this.absolutlBound);
/*  29 */     generateRandom();
/*     */   }
/*     */   
/*     */   public void setObject(Object obj)
/*     */   {
/*  34 */     Float v = (Float)obj;
/*  35 */     super.setObject(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  42 */     boolean returnValue = false;
/*  43 */     returnValue = gene instanceof FloatGenerator;
/*  44 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  45 */     return returnValue;
/*     */   }
/*     */   
/*     */   public void setlBound(Float l)
/*     */   {
/*  50 */     this.lBound = l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setuBound(Float u)
/*     */   {
/*  57 */     this.uBound = u;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void generateRandom()
/*     */   {
/*  64 */     Random random = new Random();
/*  65 */     generateRandom(random);
/*     */   }
/*     */   
/*     */   public void generateRandom(Random random) {
/*  69 */     if ((random.nextInt(100) < 5) && (!this.clazz.equals(Float.TYPE))) {
/*  70 */       setObject(null);
/*  71 */       return;
/*     */     }
/*     */     
/*  74 */     int probability = random.nextInt(100);
/*  75 */     if (((probability < 5) && (JTE.litteralConstantAnalyser.getFloatConstants().size() > 0)) || (
/*  76 */       (probability < 20) && (JTE.litteralConstantAnalyser.getFloatConstants().size() > 10))) {
/*  77 */       int index = random.nextInt(JTE.litteralConstantAnalyser.getFloatConstants().size());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  82 */       setObject((Float)JTE.litteralConstantAnalyser.getFloatConstants().get(index));
/*  83 */       return;
/*     */     }
/*     */     
/*  86 */     if (random.nextInt(100) < 90) {
/*  87 */       float range = 201.0F;
/*  88 */       setObject(Float.valueOf(-100.0F + random.nextFloat() * range));
/*     */     } else {
/*  90 */       setObject(Float.valueOf(((Float)this.lBound).floatValue() + random.nextFloat() * (((Float)this.uBound).floatValue() - ((Float)this.lBound).floatValue()))); }
/*  91 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  97 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 102 */     return super.equals(other);
/*     */   }
/*     */   
/*     */   public void mutate()
/*     */   {
/* 107 */     generateRandom();
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 112 */     FloatGenerator newGene = new FloatGenerator(this.parent, this.clazz);
/* 113 */     if (this.object != null)
/* 114 */       newGene.object = Float.valueOf(((Float)this.object).floatValue());
/* 115 */     newGene.fitness = this.fitness;
/* 116 */     newGene.clazz = this.clazz;
/* 117 */     newGene.variableBinding = this.variableBinding;
/* 118 */     newGene.random = this.random;
/* 119 */     newGene.seed = this.seed;
/*     */     
/* 121 */     return newGene;
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
/*     */   public String toString()
/*     */   {
/* 146 */     if (this.object == null)
/* 147 */       return "null";
/* 148 */     return this.object + "F";
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\FloatGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */