/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.analysis.LittralConstantAnalyser;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class LongGenerator extends AbstractPrimitive<Long>
/*     */ {
/*     */   public LongGenerator(AbsractGenerator parent, Class cls)
/*     */   {
/*  11 */     super(parent, cls);
/*  12 */     this.absolutuBound = Long.valueOf(Long.MAX_VALUE);
/*  13 */     this.absolutlBound = Long.valueOf(Long.MIN_VALUE);
/*  14 */     this.uBound = ((Long)this.absolutuBound);
/*  15 */     this.lBound = ((Long)this.absolutlBound);
/*  16 */     generateRandom();
/*     */   }
/*     */   
/*     */   public void setObject(Object obj)
/*     */   {
/*  21 */     Long v = (Long)obj;
/*  22 */     super.setObject(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setlBound(Long l)
/*     */   {
/*  29 */     this.lBound = l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setuBound(Long u)
/*     */   {
/*  36 */     this.uBound = u;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void generateRandom()
/*     */   {
/*  43 */     Random random = new Random();
/*  44 */     generateRandom(random);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  50 */     boolean returnValue = false;
/*  51 */     returnValue = gene instanceof LongGenerator;
/*  52 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  53 */     return returnValue;
/*     */   }
/*     */   
/*     */   public void generateRandom(Random random) {
/*  57 */     if ((random.nextInt(100) < 5) && (!this.clazz.equals(Long.TYPE))) {
/*  58 */       setObject(null);
/*  59 */       return;
/*     */     }
/*  61 */     int probability = random.nextInt(100);
/*  62 */     if (((probability < 5) && (csbst.testing.JTE.litteralConstantAnalyser.getLongConstants().size() > 0)) || (
/*  63 */       (probability < 20) && (csbst.testing.JTE.litteralConstantAnalyser.getLongConstants().size() > 10))) {
/*  64 */       int index = random.nextInt(csbst.testing.JTE.litteralConstantAnalyser.getLongConstants().size());
/*     */       
/*     */ 
/*  67 */       setObject((Long)csbst.testing.JTE.litteralConstantAnalyser.getLongConstants().get(index));
/*  68 */       return;
/*     */     }
/*     */     
/*  71 */     if (random.nextInt(100) < 90) {
/*  72 */       long range = 201L;
/*  73 */       setObject(Long.valueOf(-100 + random.nextInt((int)range)));
/*     */     }
/*     */     else
/*     */     {
/*  77 */       long randomValue = random.nextLong();
/*  78 */       if (random.nextInt() < 50)
/*  79 */         randomValue *= -1L;
/*  80 */       setObject(Long.valueOf(randomValue));
/*     */     }
/*  82 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  88 */     int hash = getClass().hashCode();
/*     */     
/*  90 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/*  95 */     return super.equals(other);
/*     */   }
/*     */   
/*     */   public void mutate()
/*     */   {
/* 100 */     generateRandom();
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 105 */     LongGenerator newGene = new LongGenerator(this.parent, this.clazz);
/* 106 */     if (this.object != null)
/* 107 */       newGene.object = Long.valueOf(((Long)this.object).longValue());
/* 108 */     newGene.fitness = this.fitness;
/* 109 */     newGene.clazz = this.clazz;
/* 110 */     newGene.variableBinding = this.variableBinding;
/* 111 */     newGene.random = this.random;
/* 112 */     newGene.seed = this.seed;
/*     */     
/* 114 */     return newGene;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 119 */     if (this.object == null)
/* 120 */       return "null";
/* 121 */     return ((Long)getObject()).toString() + "L";
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\LongGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */