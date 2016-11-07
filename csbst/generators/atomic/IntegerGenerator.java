/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.analysis.LittralConstantAnalyser;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class IntegerGenerator extends AbstractPrimitive<Integer>
/*     */ {
/*     */   public IntegerGenerator(AbsractGenerator parent, Class cls)
/*     */   {
/*  11 */     super(parent, cls);
/*  12 */     this.absolutuBound = Integer.valueOf(Integer.MAX_VALUE);
/*  13 */     this.absolutlBound = Integer.valueOf(Integer.MIN_VALUE);
/*  14 */     this.uBound = ((Integer)this.absolutuBound);
/*  15 */     this.lBound = ((Integer)this.absolutlBound);
/*  16 */     generateRandom();
/*     */   }
/*     */   
/*     */   public void setObject(Object obj)
/*     */   {
/*  21 */     Integer v = (Integer)obj;
/*  22 */     super.setObject(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setlBound(Integer l)
/*     */   {
/*  29 */     this.lBound = l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setuBound(Integer u)
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
/*     */ 
/*     */   public void generateRandom(Random random)
/*     */   {
/*  51 */     if ((random.nextInt(100) < 5) && (!this.clazz.equals(Integer.TYPE))) {
/*  52 */       this.object = null;
/*  53 */       setObject(null);
/*  54 */       return;
/*     */     }
/*  56 */     int probability = random.nextInt(100);
/*  57 */     if (((probability < 5) && (csbst.testing.JTE.litteralConstantAnalyser.getIntegerConstants().size() > 0)) || (
/*  58 */       (probability < 20) && (csbst.testing.JTE.litteralConstantAnalyser.getIntegerConstants().size() > 10))) {
/*  59 */       int index = random.nextInt(csbst.testing.JTE.litteralConstantAnalyser.getIntegerConstants().size());
/*     */       
/*     */ 
/*  62 */       setObject((Integer)csbst.testing.JTE.litteralConstantAnalyser.getIntegerConstants().get(index));
/*  63 */       return;
/*     */     }
/*     */     
/*  66 */     if (random.nextInt(100) < 100) {
/*  67 */       int range = 2001;
/*  68 */       int val = random.nextInt(range) - 1000;
/*     */       
/*  70 */       setObject(Integer.valueOf(val));
/*     */     } else {
/*  72 */       long range = ((Integer)this.uBound).intValue() - ((Integer)this.lBound).intValue() + 1L;
/*  73 */       if (2147483647L >= range) {
/*  74 */         setObject(Integer.valueOf(((Integer)this.lBound).intValue() + random.nextInt((int)range)));
/*     */       }
/*     */       else {
/*  77 */         range = ((Integer)this.uBound).intValue() / 2L - ((Integer)this.lBound).intValue() / 2L;
/*  78 */         setObject(Integer.valueOf(((Integer)this.lBound).intValue() + random.nextInt((int)range)));
/*  79 */         if (random.nextBoolean())
/*  80 */           setObject(Integer.valueOf(2 * ((Integer)getObject()).intValue()));
/*     */       }
/*     */     }
/*  83 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  89 */     int hash = getClass().hashCode();
/*  90 */     if (this.object == null) {
/*  91 */       return 1;
/*     */     }
/*  93 */     hash = (hash << 1 | hash >>> 31) ^ ((Integer)getObject()).intValue();
/*  94 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/*  99 */     return super.equals(other);
/*     */   }
/*     */   
/*     */   public void mutate()
/*     */   {
/* 104 */     generateRandom();
/*     */   }
/*     */   
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/* 109 */     boolean returnValue = false;
/* 110 */     returnValue = gene instanceof IntegerGenerator;
/* 111 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/* 112 */     return returnValue;
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 117 */     IntegerGenerator newGene = new IntegerGenerator(this.parent, this.clazz);
/* 118 */     if (this.object != null)
/*     */     {
/* 120 */       newGene.object = Integer.valueOf(((Integer)this.object).intValue());
/*     */     }
/* 122 */     newGene.fitness = this.fitness;
/* 123 */     newGene.clazz = this.clazz;
/* 124 */     newGene.variableBinding = this.variableBinding;
/* 125 */     newGene.random = this.random;
/* 126 */     newGene.seed = this.seed;
/*     */     
/* 128 */     return newGene;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 133 */     if (this.object == null)
/* 134 */       return "null";
/* 135 */     return ((Integer)getObject()).toString();
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\IntegerGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */