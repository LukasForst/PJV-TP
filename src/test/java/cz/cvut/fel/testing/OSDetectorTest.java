package cz.cvut.fel.testing;

import cz.cvut.fel.coursework.Controller;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class OSDetectorTest {

    private Controller c = new Controller();

    @Test
    public void OSDetection() {
        String os = System.getProperty("os.name");
        String osToControl;

        if(os.contains("Linux")){
            File f = new File("/usr/bin/notify-send");
            if(f.exists()) {
                osToControl = "LINUX";
            } else {
                osToControl = "LINUX_OTHER";
            }
        } else if (os.contains("Windows")) {
            if(os.contains("Windows 10")) {
                osToControl = "WINDOWS_10";
            }  else {
                osToControl = "WINDOWS_OTHER";
            }
        } else if(os.contains("Mac")){
            osToControl = "MAC";
        } else {
            osToControl = "UNSUPPORTED";
        }

        Assert.assertEquals(
                true,
                c.getOS().equals(osToControl)
        );
    }
}
