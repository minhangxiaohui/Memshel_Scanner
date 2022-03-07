package priv.ga0weI.scannerbaseonagent;

import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Attachthings {
    public static void main(String[] args) throws Exception {
//        1、find vm
        String pid = getpid().trim();
        VirtualMachine vm = VirtualMachine.attach(pid);
//        2、find agentfile
        String path = Attachthings.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String currentPath = Attachthings.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        currentPath = currentPath.substring(0, currentPath.lastIndexOf("/") + 1);
//        System.out.println("path:"+currentPath);
        String agentfile = currentPath.substring(1,currentPath.length())+"Scanner_agent-1.0-SNAPSHOT-jar-with-dependencies.jar".replace("/","\\");
//        3、load
        vm.loadAgent(agentfile);
//        vm.detach();
    }
    private static String getpid() throws Exception{
        Process ps = Runtime.getRuntime().exec("jps");
        InputStream is = ps.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bis = new BufferedReader(isr);
        String line;
        StringBuilder sb = new StringBuilder();
        String result = null;
        while((line=bis.readLine())!=null){
            sb.append(line+";");
        }
        String  [] xx= sb.toString().split(";");
        for (String x : xx){
            if (x.contains("Bootstrap")) //find tomcat
            {
                result=x.substring(0,x.length()-9);
            }
        }
        return result;
    }
}
