/*     */ package csbst.utils;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import org.eclipse.jdt.core.dom.AST;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.core.dom.PrimitiveType;
/*     */ import org.eclipse.jdt.core.dom.PrimitiveType.Code;
/*     */ import org.eclipse.jdt.core.dom.QualifiedName;
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
/*     */ public class ASTEditor
/*     */ {
/*     */   public static QualifiedName generateQualifiedName(String aName, AST _ast)
/*     */   {
/*  29 */     if (aName.lastIndexOf(".") < 0) return _ast.newQualifiedName(null, _ast.newSimpleName(aName));
/*  30 */     String[] names = aName.split("\\.");
/*     */     QualifiedName nQN;
/*  32 */     QualifiedName nQN; if (names.length == 1) {
/*  33 */       nQN = _ast.newQualifiedName(null, _ast.newSimpleName(names[0]));
/*     */     } else { QualifiedName nQN;
/*  35 */       if (names.length == 2) {
/*  36 */         nQN = _ast.newQualifiedName(_ast.newSimpleName(names[0]), _ast.newSimpleName(names[1]));
/*     */       } else
/*  38 */         nQN = _ast.newQualifiedName(generateQualifiedName(aName.substring(0, aName.lastIndexOf(".")), _ast), _ast.newSimpleName(names[(names.length - 1)]));
/*     */     }
/*  40 */     return nQN;
/*     */   }
/*     */   
/*     */   public static void unit2File(CompilationUnit unit, String fileName)
/*     */   {
/*  45 */     unit2File(unit.toString(), fileName);
/*     */   }
/*     */   
/*     */   public static void unit2File(String unit, String fileName)
/*     */   {
/*     */     try {
/*  51 */       File file = new File(fileName);
/*  52 */       file.getParentFile().mkdirs();
/*     */       
/*  54 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
/*  55 */       out.write(unit);
/*  56 */       out.close();
/*     */     } catch (IOException e) {
/*  58 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void copyFileUsingStream(File source, File dest)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: aconst_null
/*     */     //   3: astore_3
/*     */     //   4: new 118	java/io/FileInputStream
/*     */     //   7: dup
/*     */     //   8: aload_0
/*     */     //   9: invokespecial 120	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   12: astore_2
/*     */     //   13: aload_1
/*     */     //   14: iconst_0
/*     */     //   15: invokevirtual 123	java/io/File:setExecutable	(Z)Z
/*     */     //   18: pop
/*     */     //   19: aload_1
/*     */     //   20: iconst_1
/*     */     //   21: iconst_0
/*     */     //   22: invokevirtual 127	java/io/File:setWritable	(ZZ)Z
/*     */     //   25: pop
/*     */     //   26: new 89	java/io/FileOutputStream
/*     */     //   29: dup
/*     */     //   30: aload_1
/*     */     //   31: invokespecial 131	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
/*     */     //   34: astore_3
/*     */     //   35: sipush 1024
/*     */     //   38: newarray <illegal type>
/*     */     //   40: astore 4
/*     */     //   42: goto +12 -> 54
/*     */     //   45: aload_3
/*     */     //   46: aload 4
/*     */     //   48: iconst_0
/*     */     //   49: iload 5
/*     */     //   51: invokevirtual 132	java/io/OutputStream:write	([BII)V
/*     */     //   54: aload_2
/*     */     //   55: aload 4
/*     */     //   57: invokevirtual 137	java/io/InputStream:read	([B)I
/*     */     //   60: dup
/*     */     //   61: istore 5
/*     */     //   63: ifgt -18 -> 45
/*     */     //   66: goto +24 -> 90
/*     */     //   69: astore 6
/*     */     //   71: aload_2
/*     */     //   72: ifnull +7 -> 79
/*     */     //   75: aload_2
/*     */     //   76: invokevirtual 143	java/io/InputStream:close	()V
/*     */     //   79: aload_3
/*     */     //   80: ifnull +7 -> 87
/*     */     //   83: aload_3
/*     */     //   84: invokevirtual 144	java/io/OutputStream:close	()V
/*     */     //   87: aload 6
/*     */     //   89: athrow
/*     */     //   90: aload_2
/*     */     //   91: ifnull +7 -> 98
/*     */     //   94: aload_2
/*     */     //   95: invokevirtual 143	java/io/InputStream:close	()V
/*     */     //   98: aload_3
/*     */     //   99: ifnull +7 -> 106
/*     */     //   102: aload_3
/*     */     //   103: invokevirtual 144	java/io/OutputStream:close	()V
/*     */     //   106: return
/*     */     // Line number table:
/*     */     //   Java source line #63	-> byte code offset #0
/*     */     //   Java source line #64	-> byte code offset #2
/*     */     //   Java source line #66	-> byte code offset #4
/*     */     //   Java source line #67	-> byte code offset #13
/*     */     //   Java source line #68	-> byte code offset #19
/*     */     //   Java source line #69	-> byte code offset #26
/*     */     //   Java source line #70	-> byte code offset #35
/*     */     //   Java source line #72	-> byte code offset #42
/*     */     //   Java source line #73	-> byte code offset #45
/*     */     //   Java source line #72	-> byte code offset #54
/*     */     //   Java source line #75	-> byte code offset #69
/*     */     //   Java source line #76	-> byte code offset #71
/*     */     //   Java source line #77	-> byte code offset #75
/*     */     //   Java source line #78	-> byte code offset #79
/*     */     //   Java source line #79	-> byte code offset #83
/*     */     //   Java source line #80	-> byte code offset #87
/*     */     //   Java source line #76	-> byte code offset #90
/*     */     //   Java source line #77	-> byte code offset #94
/*     */     //   Java source line #78	-> byte code offset #98
/*     */     //   Java source line #79	-> byte code offset #102
/*     */     //   Java source line #81	-> byte code offset #106
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	107	0	source	File
/*     */     //   0	107	1	dest	File
/*     */     //   1	94	2	is	java.io.InputStream
/*     */     //   3	100	3	os	java.io.OutputStream
/*     */     //   40	16	4	buffer	byte[]
/*     */     //   45	5	5	length	int
/*     */     //   61	3	5	length	int
/*     */     //   69	19	6	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	69	69	finally
/*     */   }
/*     */   
/*     */   public static PrimitiveType.Code getPrimitiveCode(Class t)
/*     */   {
/*  84 */     if ((t.equals(Integer.TYPE)) || (t.getSimpleName().equals("int")))
/*  85 */       return PrimitiveType.INT;
/*  86 */     if ((t.equals(Byte.TYPE)) || (t.getSimpleName().equals("byte")))
/*  87 */       return PrimitiveType.BYTE;
/*  88 */     if ((t.equals(Short.TYPE)) || (t.getSimpleName().equals("short")))
/*  89 */       return PrimitiveType.SHORT;
/*  90 */     if ((t.equals(Long.TYPE)) || (t.getSimpleName().equals("long")))
/*  91 */       return PrimitiveType.LONG;
/*  92 */     if ((t.equals(Boolean.TYPE)) || (t.getSimpleName().equals("boolean")))
/*  93 */       return PrimitiveType.BOOLEAN;
/*  94 */     if ((t.equals(Character.TYPE)) || (t.getSimpleName().equals("char")))
/*  95 */       return PrimitiveType.CHAR;
/*  96 */     if ((t.equals(Double.TYPE)) || (t.getSimpleName().equals("double")))
/*  97 */       return PrimitiveType.DOUBLE;
/*  98 */     if ((t.equals(Float.TYPE)) || (t.getSimpleName().equals("float"))) {
/*  99 */       return PrimitiveType.FLOAT;
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getPrimitiveInitialiser(Class t)
/*     */   {
/* 111 */     if ((t.equals(Long.TYPE)) || (t.getSimpleName().equals("long"))) {
/* 112 */       return "0L";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 117 */     if ((t.equals(Double.TYPE)) || (t.getSimpleName().equals("double")))
/* 118 */       return "0d";
/* 119 */     if ((t.equals(Float.TYPE)) || (t.getSimpleName().equals("float"))) {
/* 120 */       return "0f";
/*     */     }
/* 122 */     return "0";
/*     */   }
/*     */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\utils\ASTEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */