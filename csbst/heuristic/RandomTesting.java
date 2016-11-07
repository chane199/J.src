/*     */ package csbst.heuristic;
/*     */ 
/*     */ import csbst.ga.ecj.TestCaseCandidate;
/*     */ import csbst.testing.JTE;
/*     */ import csbst.testing.fitness.TestingFitness;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomTesting
/*     */ {
/*     */   private int generations;
/*     */   private int currentGenNbr;
/*     */   private static Thread killerThread;
/*  21 */   static ConcurrentLinkedQueue<Thread> killerQueue = new ConcurrentLinkedQueue();
/*     */   
/*  23 */   static { killerThread = new Thread()
/*     */     {
/*     */       public void run() {
/*     */         for (;;) {
/*     */           Thread t;
/*  28 */           if (t.isAlive()) {
/*  29 */             t.interrupt();
/*     */             
/*  31 */             if (t.isAlive())
/*     */             {
/*  33 */               RandomTesting.killerQueue.add(t);
/*     */             }
/*     */           }
/*     */           Thread t;
/*  27 */           while ((t = (Thread)RandomTesting.killerQueue.poll()) == null) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  47 */     };
/*  48 */     killerThread.setPriority(1);
/*     */   }
/*     */   
/*     */   public RandomTesting(int generations) {
/*  52 */     this.generations = generations;
/*  53 */     this.currentGenNbr = 0;
/*     */   }
/*     */   
/*     */   public int getGeneartion() {
/*  57 */     return this.currentGenNbr;
/*     */   }
/*     */   
/*     */   private static int chmod(String filename, int mode) {
/*     */     try {
/*  62 */       Class<?> fspClass = Class.forName("java.util.prefs.FileSystemPreferences");
/*  63 */       Method chmodMethod = fspClass.getDeclaredMethod("chmod", new Class[] { String.class, Integer.TYPE });
/*  64 */       chmodMethod.setAccessible(true);
/*  65 */       return ((Integer)chmodMethod.invoke(null, new Object[] { filename, Integer.valueOf(mode) })).intValue();
/*     */     } catch (Throwable ex) {}
/*  67 */     return -1;
/*     */   }
/*     */   
/*     */   public int run(Set<Integer> toCheck)
/*     */   {
/*  72 */     if (this.generations <= 0)
/*  73 */       return 0;
/*  74 */     this.currentGenNbr = 0;
/*  75 */     final TestCaseCandidate currentChromosome = new TestCaseCandidate(JTE.currentClassUnderTest);
/*     */     
/*  77 */     this.currentGenNbr += 1;
/*     */     
/*  79 */     Thread thread = new Thread() {
/*     */       public void run() {
/*  81 */         RandomTesting.chmod("/Users/abdelilahsakti/Documents/saad", 777);
/*     */         
/*     */ 
/*     */ 
/*  85 */         currentChromosome.generateRandom();
/*  86 */         currentChromosome.getFitness().evaluate(currentChromosome);
/*     */       }
/*     */       
/*  89 */     };
/*  90 */     thread.setDaemon(false);
/*  91 */     thread.start();
/*     */     try {
/*  93 */       thread.join(7500L);
/*     */     } catch (InterruptedException e) {
/*  95 */       e.printStackTrace();
/*     */     }
/*  97 */     if (thread.isAlive()) {
/*  98 */       thread.interrupt();
/*  99 */       if (thread.isAlive()) {
/* 100 */         Thread.currentThread().setPriority(10);
/* 101 */         killerQueue.add(thread);
/* 102 */         currentChromosome.setInterrupted();
/* 103 */         if (!killerThread.isAlive())
/* 104 */           killerThread.start();
/*     */       }
/* 106 */       return -1;
/*     */     }
/*     */     
/* 109 */     return this.currentGenNbr;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\heuristic\RandomTesting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */