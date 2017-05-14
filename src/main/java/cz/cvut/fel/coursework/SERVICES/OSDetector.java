/**
 * @[ile        OSDetector
 * @date        21.3.17
 * @author      Lukas Forst
 * @package     com.lukas.OSDetector
 * @project     OSDetector
 */
package cz.cvut.fel.coursework.SERVICES;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
/**
 * <p>Provides way to distinguish current running Operating System.<br>
 * OSDetector contains only one static method <i>getOS()</i></p>
 * <p><b>enums:</b><br><i>LINUX, LINUX_OTHER, WINDOWS_10, WINDOWS_OTHER, MAC, UNSUPPORTED</i></p>
 * Method <i>getOS()</i>
 */
public enum OSDetector {
    LINUX, LINUX_OTHER, WINDOWS_10, WINDOWS_OTHER, MAC, UNSUPPORTED;
    /**
     * <p>Static method <i>getOS()</i> detects current operating system and returns it as an enum
     * from OSDetector class.</p>
     * <p>Method provides some workarounds to detect Windows 10 - older version of Java
     * cannot detect Windows 10, they show it as a Windows 8.1.</p>
     *
     * @return enum LINUX, LINUX_OTHER, WINDOWS_10, WINDOWS_OTHER, MAC, UNSUPPORTED
     */
    public static OSDetector getOS(){
        String os = System.getProperty("os.name");
        if(os.contains("Linux")){
            File f = new File("/usr/bin/notify-send"); //if it exist, we can use it to notify user
            if(f.exists()) {
                return LINUX;
            } else return LINUX_OTHER;
        } else if (os.contains("Windows")) {
            if(os.contains("Windows 10")) return WINDOWS_10;
            try{
                // workaround to be sure about Windows 10
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ver");
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
                if(new String(sb).contains("Version 10.")) {
                    return WINDOWS_10;
                } else {
                    return WINDOWS_OTHER;
                }
            } catch (Exception e){
                return WINDOWS_OTHER;
            }
        } else if(os.contains("Mac")){
            return MAC;
        } else {
            return UNSUPPORTED;
        }
    }
}