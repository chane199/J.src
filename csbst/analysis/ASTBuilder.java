/*    */ package csbst.analysis;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.util.Hashtable;
/*    */ import org.eclipse.jdt.core.JavaCore;
/*    */ import org.eclipse.jdt.core.dom.ASTParser;
/*    */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ASTBuilder
/*    */ {
/*    */   String javaText;
/*    */   String source;
/*    */   String[] sourcePath;
/*    */   String[] classPath;
/*    */   CompilationUnit astRoot;
/*    */   
/*    */   public ASTBuilder(String sourcePath, String source)
/*    */     throws IOException
/*    */   {
/* 25 */     this.source = source;
/* 26 */     this.sourcePath = sourcePath.split(";");
/* 27 */     File file = new File(sourcePath + "//" + source);
/* 28 */     BufferedReader in = new BufferedReader(new FileReader(file));
/* 29 */     StringBuffer buffer = new StringBuffer();
/* 30 */     String line = null;
/* 31 */     while ((line = in.readLine()) != null) {
/* 32 */       buffer.append("\t" + line);
/* 33 */       buffer.append("\n");
/*    */     }
/*    */     
/*    */ 
/* 37 */     String text = buffer.toString();
/*    */     
/* 39 */     this.javaText = text;
/* 40 */     buildAST();
/*    */   }
/*    */   
/*    */   public ASTBuilder(String classPath, String sourcePath, String source) throws IOException
/*    */   {
/* 45 */     this.source = source;
/* 46 */     if (sourcePath != null)
/* 47 */       this.sourcePath = sourcePath.split(File.pathSeparator);
/* 48 */     if ((classPath != null) && (!classPath.equals("")))
/* 49 */       this.classPath = classPath.split(File.pathSeparator);
/* 50 */     File file = new File(source);
/* 51 */     BufferedReader in = new BufferedReader(new FileReader(file));
/* 52 */     StringBuffer buffer = new StringBuffer();
/* 53 */     String line = null;
/* 54 */     while ((line = in.readLine()) != null) {
/* 55 */       buffer.append("\t" + line);
/* 56 */       buffer.append("\n");
/*    */     }
/*    */     
/*    */ 
/* 60 */     String text = buffer.toString();
/*    */     
/* 62 */     this.javaText = text;
/* 63 */     buildAST();
/*    */   }
/*    */   
/*    */   CompilationUnit buildAST() {
/* 67 */     ASTParser parser = ASTParser.newParser(3);
/* 68 */     parser.setSource(this.javaText.toCharArray());
/* 69 */     parser.setEnvironment(this.classPath, this.sourcePath, null, true);
/* 70 */     parser.setResolveBindings(true);
/* 71 */     parser.setBindingsRecovery(true);
/* 72 */     parser.setStatementsRecovery(true);
/*    */     
/*    */ 
/*    */ 
/* 76 */     Hashtable<String, String> options = JavaCore.getDefaultOptions();
/* 77 */     options.put("org.eclipse.jdt.core.compiler.source", "1.6");
/* 78 */     parser.setCompilerOptions(options);
/* 79 */     parser.setUnitName(this.source);
/*    */     
/*    */ 
/*    */ 
/* 83 */     this.astRoot = ((CompilationUnit)parser.createAST(null));
/*    */     
/* 85 */     return this.astRoot;
/*    */   }
/*    */   
/*    */   public CompilationUnit getASTRoot()
/*    */   {
/* 90 */     return this.astRoot;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\analysis\ASTBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */