/*     */ package csbst.testing.fitness;
/*     */ 
/*     */ import csbst.testing.JTE;
/*     */ import csbst.testing.Path;
/*     */ import ec.Fitness;
/*     */ import ec.util.Parameter;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*     */ 
/*     */ 
/*     */ public class ApproachLevel
/*     */   extends TestingFitness
/*     */ {
/*     */   private double DC;
/*     */   private double DL;
/*     */   private double level;
/*     */   private double branchDistance;
/*     */   private int branchID;
/*  19 */   private int influence = 1;
/*  20 */   private double maxSubBD = 0.0D;
/*  21 */   private double minSubBD = 0.0D;
/*  22 */   private Vector<ApproachLevel> branchFitness = new Vector();
/*     */   
/*     */   public static void maintainBranchDistance(int branch, int iteration, String expression) {
/*  25 */     if (underEvaluationFitness == null) {
/*  26 */       return;
/*     */     }
/*  28 */     if (JTE.getCurrentPathTarget().contains(branch)) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApproachLevel()
/*     */   {
/*  38 */     this.branchDistance = 0.0D;
/*     */   }
/*     */   
/*     */   public ApproachLevel(double bd) {
/*  42 */     this.branchDistance = bd;
/*     */   }
/*     */   
/*     */   public ApproachLevel(double bd, int bID)
/*     */   {
/*  47 */     this.branchDistance = bd;
/*  48 */     this.branchID = bID;
/*     */   }
/*     */   
/*     */   public ApproachLevel(Path currentPathTarget, IVariableBinding iVariableBinding) {
/*  52 */     this.branchFitness = new Vector();
/*  53 */     for (int i = 0; i < currentPathTarget.size(); i++) {
/*  54 */       ApproachLevel tmpFit = null;
/*  55 */       if (iVariableBinding == null) {
/*  56 */         tmpFit.influence = 1;
/*     */       } else {
/*  58 */         tmpFit.influence = JTE.getInfluence(iVariableBinding, tmpFit.getBranchID());
/*     */       }
/*  60 */       this.branchFitness.add(tmpFit);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getMinBD()
/*     */   {
/*  66 */     return this.minSubBD;
/*     */   }
/*     */   
/*     */   public double getMaxBD() {
/*  70 */     return this.maxSubBD;
/*     */   }
/*     */   
/*     */   public int getBranchID() {
/*  74 */     return this.branchID;
/*     */   }
/*     */   
/*     */   public boolean equals(ApproachLevel other)
/*     */   {
/*  79 */     boolean areEqual = this.DC == other.getDC();
/*  80 */     areEqual = (areEqual) && (this.branchDistance == other.getBD());
/*  81 */     areEqual = (areEqual) && (this.branchFitness.size() == other.getHeirarchy().size());
/*     */     
/*  83 */     int i = 0;
/*  84 */     while ((areEqual) && (i < this.branchFitness.size())) {
/*  85 */       areEqual = (areEqual) && (((ApproachLevel)this.branchFitness.get(i)).equals((ApproachLevel)other.getHeirarchy().get(i)));
/*     */     }
/*     */     
/*  88 */     return areEqual;
/*     */   }
/*     */   
/*     */   public Vector<ApproachLevel> getHeirarchy() {
/*  92 */     return this.branchFitness;
/*     */   }
/*     */   
/*  95 */   public double getDC() { return this.DC; }
/*     */   
/*     */   public void setDC(double dc)
/*     */   {
/*  99 */     this.DC = dc;
/*     */   }
/*     */   
/*     */   public double getBD() {
/* 103 */     return this.branchDistance;
/*     */   }
/*     */   
/*     */   public void setBD(double bd) {
/* 107 */     this.branchDistance = bd;
/*     */   }
/*     */   
/*     */   public void setLevel(int l) {
/* 111 */     this.level = l;
/*     */   }
/*     */   
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/* 117 */     ApproachLevel fit = (ApproachLevel)obj;
/* 118 */     if (this.level > fit.getDC())
/* 119 */       return -1;
/* 120 */     if (this.level < fit.getDC())
/* 121 */       return 1;
/* 122 */     if (this.branchDistance > fit.getBD())
/* 123 */       return -1;
/* 124 */     if (this.branchDistance < fit.getBD()) {
/* 125 */       return 1;
/*     */     }
/* 127 */     return 0;
/*     */   }
/*     */   
/*     */   public double getNormalizedBD() {
/* 131 */     return this.branchDistance / (this.branchDistance + 1.0D);
/*     */   }
/*     */   
/*     */   public double getNormalizedBDLog() {
/* 135 */     return 1.0D - 1.0D / (1.0D + Math.log(1.0D + this.branchDistance));
/*     */   }
/*     */   
/*     */   public double getNormalizedBDPow()
/*     */   {
/* 140 */     return 1.0D - Math.pow(1.001D, -1.0D * this.branchDistance);
/*     */   }
/*     */   
/*     */ 
/*     */   public void evaluatePathFitness()
/*     */   {
/* 146 */     for (ApproachLevel fit : this.branchFitness) {
/* 147 */       setBD(getBD() + fit.getBD());
/*     */     }
/*     */   }
/*     */   
/*     */   public Parameter defaultBase()
/*     */   {
/* 153 */     return null;
/*     */   }
/*     */   
/*     */   public double fitness()
/*     */   {
/* 158 */     return 0.0D + this.level + getNormalizedBDLog();
/*     */   }
/*     */   
/*     */   public boolean isIdealFitness()
/*     */   {
/* 163 */     return this.branchDistance == 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equivalentTo(Fitness _fitness)
/*     */   {
/* 169 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean betterThan(Fitness _fitness)
/*     */   {
/* 175 */     return compareTo(_fitness) == 1;
/*     */   }
/*     */   
/*     */   public void setBD(int bID, double branchDistance) {
/* 179 */     for (int i = 0; i < this.branchFitness.size(); i++) {
/* 180 */       if (((ApproachLevel)this.branchFitness.get(i)).getBranchID() == bID) {
/* 181 */         ((ApproachLevel)this.branchFitness.get(i)).setBD(branchDistance);
/* 182 */         ((ApproachLevel)this.branchFitness.get(i)).setLevel(branchDistance == 0.0D ? 0 : this.branchFitness.size() - i);
/* 183 */         if (this.maxSubBD < ((ApproachLevel)this.branchFitness.get(i)).getBD())
/* 184 */           this.maxSubBD = ((ApproachLevel)this.branchFitness.get(i)).getBD();
/* 185 */         if (this.minSubBD > ((ApproachLevel)this.branchFitness.get(i)).getBD())
/* 186 */           this.minSubBD = ((ApproachLevel)this.branchFitness.get(i)).getBD();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 192 */     String str = new String();
/* 193 */     str = "branchID: " + this.branchID + "= " + fitness() + " [";
/* 194 */     for (int i = 0; i < this.branchFitness.size(); i++) {
/* 195 */       str = str + ((ApproachLevel)this.branchFitness.get(i)).toString();
/* 196 */       if (i < this.branchFitness.size() - 1)
/* 197 */         str = str + ", ";
/*     */     }
/* 199 */     str = str + "]";
/* 200 */     return str;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\testing\fitness\ApproachLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */