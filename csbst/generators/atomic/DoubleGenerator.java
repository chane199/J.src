/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.analysis.LittralConstantAnalyser;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class DoubleGenerator extends AbstractPrimitive<Double>
/*     */ {
/*     */   public DoubleGenerator(AbsractGenerator parent, Class cls)
/*     */   {
/*  11 */     super(parent, cls);
/*  12 */     this.absolutuBound = Double.valueOf(Double.MAX_VALUE);
/*  13 */     this.absolutlBound = Double.valueOf(Double.MIN_VALUE);
/*  14 */     this.uBound = ((Double)this.absolutuBound);
/*  15 */     this.lBound = ((Double)this.absolutlBound);
/*  16 */     generateRandom();
/*     */   }
/*     */   
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  21 */     boolean returnValue = false;
/*  22 */     returnValue = gene instanceof DoubleGenerator;
/*  23 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  24 */     return returnValue;
/*     */   }
/*     */   
/*     */   public void setObject(Object obj)
/*     */   {
/*  29 */     Double v = (Double)obj;
/*  30 */     super.setObject(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setlBound(Double l)
/*     */   {
/*  37 */     this.lBound = l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setuBound(Double u)
/*     */   {
/*  44 */     this.uBound = u;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void generateRandom()
/*     */   {
/*  51 */     Random random = new Random();
/*  52 */     generateRandom(random);
/*     */   }
/*     */   
/*     */   public void generateRandom(Random random) {
/*  56 */     if ((random.nextInt(100) < 5) && (!this.clazz.equals(Double.TYPE))) {
/*  57 */       setObject(null);
/*  58 */       return;
/*     */     }
/*     */     
/*  61 */     int probability = random.nextInt(100);
/*  62 */     if (((probability < 5) && (csbst.testing.JTE.litteralConstantAnalyser.getDoubleConstants().size() > 0)) || (
/*  63 */       (probability < 20) && (csbst.testing.JTE.litteralConstantAnalyser.getDoubleConstants().size() > 10))) {
/*  64 */       int index = random.nextInt(csbst.testing.JTE.litteralConstantAnalyser.getDoubleConstants().size());
/*     */       
/*     */ 
/*  67 */       setObject((Double)csbst.testing.JTE.litteralConstantAnalyser.getDoubleConstants().get(index));
/*  68 */       return;
/*     */     }
/*     */     
/*  71 */     if (random.nextInt(100) < 90) {
/*  72 */       double range = 201.0D;
/*  73 */       setObject(Double.valueOf(-100.0D + random.nextDouble() * range));
/*     */     } else {
/*  75 */       setObject(Double.valueOf(((Double)this.lBound).doubleValue() + random.nextDouble() * (((Double)this.uBound).doubleValue() - ((Double)this.lBound).doubleValue())));
/*     */     }
/*  77 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  83 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/*  88 */     return super.equals(other);
/*     */   }
/*     */   
/*     */   public void mutate()
/*     */   {
/*  93 */     generateRandom();
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/*  98 */     DoubleGenerator newGene = new DoubleGenerator(this.parent, this.clazz);
/*  99 */     if (this.object != null)
/* 100 */       newGene.object = Double.valueOf(((Double)this.object).doubleValue());
/* 101 */     newGene.fitness = this.fitness;
/* 102 */     newGene.clazz = this.clazz;
/* 103 */     newGene.variableBinding = this.variableBinding;
/* 104 */     newGene.random = this.random;
/* 105 */     newGene.seed = this.seed;
/*     */     
/* 107 */     return newGene;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 112 */     if (this.object == null)
/* 113 */       return "null";
/* 114 */     return ((Double)getObject()).toString() + "D";
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\DoubleGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */