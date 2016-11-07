/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.analysis.LittralConstantAnalyser;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class ShortGenerator extends AbstractPrimitive<Short>
/*     */ {
/*     */   public ShortGenerator(AbsractGenerator parent, Class cls)
/*     */   {
/*  11 */     super(parent, cls);
/*     */     
/*  13 */     this.absolutuBound = Short.valueOf((short)Short.MAX_VALUE);
/*  14 */     this.absolutlBound = Short.valueOf((short)Short.MIN_VALUE);
/*  15 */     this.uBound = ((Short)this.absolutuBound);
/*  16 */     this.lBound = ((Short)this.absolutlBound);
/*  17 */     generateRandom();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  23 */     boolean returnValue = false;
/*  24 */     returnValue = gene instanceof ShortGenerator;
/*  25 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  26 */     return returnValue;
/*     */   }
/*     */   
/*     */   public void setObject(Object obj)
/*     */   {
/*  31 */     Short v = (Short)obj;
/*  32 */     super.setObject(v);
/*     */   }
/*     */   
/*     */   public void setlBound(Short l)
/*     */   {
/*  37 */     this.lBound = l;
/*     */   }
/*     */   
/*     */   public void setuBound(Short u)
/*     */   {
/*  42 */     this.uBound = u;
/*     */   }
/*     */   
/*     */   public void generateRandom()
/*     */   {
/*  47 */     Random random = new Random();
/*  48 */     generateRandom(random);
/*     */   }
/*     */   
/*     */   public void generateRandom(Random random) {
/*  52 */     if ((random.nextInt(100) < 5) && (!this.clazz.equals(Short.TYPE))) {
/*  53 */       setObject(null);
/*  54 */       return;
/*     */     }
/*     */     
/*  57 */     int probability = random.nextInt(100);
/*  58 */     if (((probability < 5) && (csbst.testing.JTE.litteralConstantAnalyser.getShortConstants().size() > 0)) || (
/*  59 */       (probability < 20) && (csbst.testing.JTE.litteralConstantAnalyser.getShortConstants().size() > 10))) {
/*  60 */       int index = random.nextInt(csbst.testing.JTE.litteralConstantAnalyser.getShortConstants().size());
/*     */       
/*     */ 
/*  63 */       setObject(Short.valueOf(((Short)csbst.testing.JTE.litteralConstantAnalyser.getShortConstants().get(index)).shortValue()));
/*  64 */       return;
/*     */     }
/*     */     
/*  67 */     if (random.nextInt(100) < 50) {
/*  68 */       short range = 201;
/*  69 */       setObject(Short.valueOf((short)(-100 + random.nextInt(range))));
/*     */     } else {
/*  71 */       int range = ((Short)this.uBound).shortValue() - ((Short)this.lBound).shortValue() + 1;
/*  72 */       int value = random.nextInt(range);
/*  73 */       while ((value > 32767) || (value < 32768)) {
/*  74 */         value = random.nextInt(range);
/*     */       }
/*  76 */       Short val = Short.valueOf((short)value);
/*  77 */       setObject(val);
/*     */     }
/*  79 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  85 */     int hash = getClass().hashCode();
/*  86 */     if (this.object == null) {
/*  87 */       return hash;
/*     */     }
/*  89 */     hash = (hash << 1 | hash >>> 31) ^ ((Short)getObject()).shortValue();
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
/* 105 */     ShortGenerator newGene = new ShortGenerator(this.parent, this.clazz);
/* 106 */     if (this.object != null)
/* 107 */       newGene.object = Short.valueOf(((Short)this.object).shortValue());
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
/* 121 */     return ((Short)getObject()).toString();
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\ShortGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */