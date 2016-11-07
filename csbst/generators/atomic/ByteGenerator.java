/*     */ package csbst.generators.atomic;
/*     */ 
/*     */ import csbst.analysis.LittralConstantAnalyser;
/*     */ import csbst.generators.AbsractGenerator;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class ByteGenerator extends AbstractPrimitive<Byte>
/*     */ {
/*     */   public ByteGenerator(AbsractGenerator parent, Class cls)
/*     */   {
/*  11 */     super(parent, cls);
/*     */     
/*  13 */     this.absolutuBound = Byte.valueOf((byte)Byte.MAX_VALUE);
/*  14 */     this.absolutlBound = Byte.valueOf((byte)Byte.MIN_VALUE);
/*  15 */     this.uBound = ((Byte)this.absolutuBound);
/*  16 */     this.lBound = ((Byte)this.absolutlBound);
/*  17 */     generateRandom();
/*     */   }
/*     */   
/*     */   public boolean isSameFamillyAs(AbsractGenerator gene)
/*     */   {
/*  22 */     boolean returnValue = false;
/*  23 */     returnValue = gene instanceof ByteGenerator;
/*  24 */     returnValue = (returnValue) && (this.clazz.equals(gene.getClazz()));
/*  25 */     return returnValue;
/*     */   }
/*     */   
/*     */   public void setObject(Object obj)
/*     */   {
/*  30 */     Byte v = (Byte)obj;
/*  31 */     super.setObject(v);
/*     */   }
/*     */   
/*     */   public void setlBound(Byte l)
/*     */   {
/*  36 */     this.lBound = l;
/*     */   }
/*     */   
/*     */   public void setuBound(Byte u)
/*     */   {
/*  41 */     this.uBound = u;
/*     */   }
/*     */   
/*     */   public void generateRandom()
/*     */   {
/*  46 */     Random random = new Random();
/*  47 */     generateRandom(random);
/*     */   }
/*     */   
/*     */   public void generateRandom(Random random) {
/*  51 */     if ((random.nextInt(100) < 5) && (!this.clazz.equals(Byte.TYPE))) {
/*  52 */       setObject(null);
/*  53 */       return;
/*     */     }
/*     */     
/*  56 */     int probability = random.nextInt(100);
/*  57 */     if (((probability < 5) && (csbst.testing.JTE.litteralConstantAnalyser.getByteConstants().size() > 0)) || (
/*  58 */       (probability < 20) && (csbst.testing.JTE.litteralConstantAnalyser.getByteConstants().size() > 10))) {
/*  59 */       int index = random.nextInt(csbst.testing.JTE.litteralConstantAnalyser.getByteConstants().size());
/*     */       
/*     */ 
/*  62 */       setObject(Byte.valueOf(((Byte)csbst.testing.JTE.litteralConstantAnalyser.getByteConstants().get(index)).byteValue()));
/*  63 */       return;
/*     */     }
/*     */     
/*  66 */     byte[] possibleValue = new byte[1];
/*  67 */     random.nextBytes(possibleValue);
/*  68 */     setObject(Byte.valueOf(possibleValue[0]));
/*  69 */     this.object = getObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  75 */     int hash = getClass().hashCode();
/*  76 */     if (getObject() == null)
/*  77 */       return hash;
/*  78 */     hash = (hash << 1 | hash >>> 31) ^ ((Byte)getObject()).byteValue();
/*  79 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/*  84 */     return super.equals(other);
/*     */   }
/*     */   
/*     */   public void mutate()
/*     */   {
/*  89 */     generateRandom();
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/*  94 */     ByteGenerator newGene = new ByteGenerator(this.parent, this.clazz);
/*  95 */     if (this.object != null)
/*  96 */       newGene.object = Byte.valueOf(((Byte)this.object).byteValue());
/*  97 */     newGene.fitness = this.fitness;
/*  98 */     newGene.clazz = this.clazz;
/*  99 */     newGene.variableBinding = this.variableBinding;
/* 100 */     newGene.random = this.random;
/* 101 */     newGene.seed = this.seed;
/*     */     
/* 103 */     return newGene;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 108 */     if (this.object == null)
/* 109 */       return "null";
/* 110 */     return ((Byte)getObject()).toString();
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\generators\atomic\ByteGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */