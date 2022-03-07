package priv.ga0weI.scannerbaseonagent;

import javassist.*;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//        System.out.println("ga0weI"+className);
        if (className.equals("org/apache/catalina/core/ApplicationFilterChain")) {
           try {
               ClassPool classPool = ClassPool.getDefault();

               if(className!=null) {
                   ClassClassPath path = new ClassClassPath(className.getClass());
                   classPool.insertClassPath(path);
               }
               if (classBeingRedefined != null)
               {
                   ClassClassPath path2 = new ClassClassPath(classBeingRedefined);
                   classPool.insertClassPath(path2);
               }
//               System.out.println("class name:"+className);
               CtClass ctClass;
               try {
                   ctClass = classPool.get("org.apache.catalina.core.ApplicationFilterChain");//对应类没有内存马则为null抛出异常
               }catch (NotFoundException e){
                   System.out.println("no memshell in ApplicationFilterChain");
                   return classfileBuffer;
               }

//               CtMethod ctMethod=ctClass.getDeclaredMethod("internalDoFilter");
               byte [] bytes = ctClass.toBytecode();
               ctClass.defrost();
               bytestoclass(bytes,".\\tmp\\getApplicationFilterChain.class"); //original class eval
               bytestoclass(classfileBuffer,".\\tmp\\getclassfileBuffer.class");//changed class
               System.out.println("class has been get at /tmp");
               System.out.println("memshell recover");
               return bytes;//recover

           }catch (Exception e){
//               e.printStackTrace();
           }

        }
        return  classfileBuffer;
    }

    /**
     * get class
     * @param bytes
     */
    private void bytestoclass(byte [] bytes,String filename) {
        try{
            File file = new File(".\\tmp");
            if (!file.exists())
                file.mkdir();
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(bytes);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
