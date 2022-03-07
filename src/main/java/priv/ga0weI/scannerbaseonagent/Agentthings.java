package priv.ga0weI.scannerbaseonagent;

import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;

public class Agentthings {
    public static  void  agentmain(String Args, Instrumentation inst) throws Exception {
//
        MyTransformer myTransformer=new MyTransformer();
        inst.addTransformer(myTransformer,true);
        for (Class aclass:inst.getAllLoadedClasses()){
            if (aclass.getName().equals("org.apache.catalina.core.ApplicationFilterChain"))
            {

                System.out.println("agent main:"+aclass.getName());
                inst.retransformClasses(aclass);
//                inst.removeTransformer(myTransformer);
            }
        }

    }

}
