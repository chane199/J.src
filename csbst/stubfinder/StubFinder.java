/*    */ package csbst.stubfinder;
/*    */ 
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.reflections.Reflections;
/*    */ 
/*    */ public class StubFinder
/*    */ {
/*    */   public static Class getStub(Class abstractClass)
/*    */   {
/* 12 */     Class inheritClass = null;
/* 13 */     Set<Class> allAbstractInheritClasses = new HashSet();
/*    */     
/* 15 */     Reflections reflections = new Reflections(new Object[] { abstractClass.getPackage() });
/*    */     
/* 17 */     Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);
/*    */     
/* 19 */     for (Class cls : allAbstractInheritClasses) {
/* 20 */       if (cls.isAssignableFrom(inheritClass))
/* 21 */         if (Modifier.isAbstract(cls.getModifiers())) {
/* 22 */           allAbstractInheritClasses.add(cls);
/*    */         } else
/* 24 */           return cls;
/*    */     }
/* 26 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\JTExpert\JTExpert-1.2.jar!\csbst\stubfinder\StubFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */