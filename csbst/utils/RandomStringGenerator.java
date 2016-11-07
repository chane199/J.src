/*     */ package csbst.utils;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class RandomStringGenerator
/*     */ {
/*   8 */   private int randomStringLength = 25;
/*   9 */   private boolean allowSpecialCharacters = true;
/*  10 */   private boolean allowDuplicates = false;
/*  11 */   private boolean isAlphanum = false;
/*  12 */   private boolean isNumeric = false;
/*  13 */   private boolean isAlpha = false;
/*  14 */   private boolean mixCase = false;
/*     */   private static final String specialCharacters = " \r\t!@$%*-_+:.><\"/\\#?&()_=^¨;'";
/*     */   private static final String alphabet = "abcdefghijklmnopqrstuvwxyzèàéù";
/*     */   private static final String capAlpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZÈÀÉÙ";
/*     */   private static final String num = "0123456789";
/*     */   private static String seeding;
/*     */   
/*     */   public RandomStringGenerator(int len, boolean sc, boolean d, boolean an, boolean n, boolean a, boolean mc, String seeding)
/*     */   {
/*  23 */     this.randomStringLength = len;
/*  24 */     this.allowSpecialCharacters = sc;
/*  25 */     this.allowDuplicates = d;
/*  26 */     this.isAlphanum = an;
/*  27 */     this.isNumeric = n;
/*  28 */     this.isAlpha = a;
/*  29 */     this.mixCase = mc;
/*  30 */     seeding = seeding;
/*     */   }
/*     */   
/*     */   public RandomStringGenerator(int len, boolean sc, boolean d, boolean an, boolean n, boolean a, boolean mc) {
/*  34 */     this(len, sc, d, an, n, a, mc, "");
/*     */   }
/*     */   
/*     */   public String getRandomString()
/*     */   {
/*  39 */     String returnVal = "";
/*  40 */     int specialCharactersCount = 0;
/*  41 */     int maxspecialCharacters = this.randomStringLength / 4;
/*  42 */     if (maxspecialCharacters < 1) {
/*  43 */       maxspecialCharacters = 1;
/*     */     }
/*     */     try {
/*  46 */       StringBuffer values = buildList();
/*  47 */       if ((values.length() == 0) || (this.randomStringLength == 0)) {
/*  48 */         Random rand = new Random();
/*  49 */         if (rand.nextBoolean()) {
/*  50 */           return null;
/*     */         }
/*  52 */         return "";
/*     */       }
/*  54 */       if ((values.length() < this.randomStringLength) && (!this.allowDuplicates))
/*  55 */         this.randomStringLength = values.length();
/*  56 */       for (int inx = 0; inx < this.randomStringLength; inx++) {
/*  57 */         int selChar = (int)(Math.random() * (values.length() - 1));
/*  58 */         if (this.allowSpecialCharacters)
/*     */         {
/*  60 */           if (" \r\t!@$%*-_+:.><\"/\\#?&()_=^¨;'".indexOf(values.charAt(selChar)) > -1)
/*     */           {
/*  62 */             specialCharactersCount++;
/*     */           }
/*     */         }
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
/*  75 */         returnVal = returnVal + values.charAt(selChar);
/*  76 */         if (!this.allowDuplicates) {
/*  77 */           values.deleteCharAt(selChar);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/*  81 */       returnVal = "Error While Processing Values";
/*  82 */       e.printStackTrace();
/*     */     }
/*  84 */     String newString = "";
/*     */     try {
/*  86 */       newString = new String(returnVal.getBytes(System.getProperty("file.encoding")), System.getProperty("file.encoding"));
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/*  89 */       e.printStackTrace();
/*     */     }
/*  91 */     return newString;
/*     */   }
/*     */   
/*     */   private StringBuffer buildList() {
/*  95 */     StringBuffer list = new StringBuffer(0);
/*  96 */     list.append(seeding);
/*  97 */     if ((this.isNumeric) || (this.isAlphanum)) {
/*  98 */       list.append("0123456789");
/*     */     }
/* 100 */     if ((this.isAlpha) || (this.isAlphanum)) {
/* 101 */       list.append("abcdefghijklmnopqrstuvwxyzèàéù");
/* 102 */       if (this.mixCase) {
/* 103 */         list.append("ABCDEFGHIJKLMNOPQRSTUVWXYZÈÀÉÙ");
/*     */       }
/*     */     }
/* 106 */     if (this.allowSpecialCharacters)
/*     */     {
/* 108 */       list.append(" \r\t!@$%*-_+:.><\"/\\#?&()_=^¨;'");
/*     */     }
/* 110 */     int currLen = list.length();
/* 111 */     String returnVal = "";
/* 112 */     for (int inx = 0; inx < currLen; inx++) {
/* 113 */       int selChar = (int)(Math.random() * (list.length() - 1));
/* 114 */       returnVal = returnVal + list.charAt(selChar);
/* 115 */       list.deleteCharAt(selChar);
/*     */     }
/* 117 */     list = new StringBuffer(returnVal);
/* 118 */     return list;
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\RandomStringGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */