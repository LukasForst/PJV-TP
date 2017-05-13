package cz.cvut.fel.coursework;

import cz.cvut.fel.coursework.GUI.MainWindow;
import cz.cvut.fel.coursework.SERVICES.IPIdentifier;
import cz.cvut.fel.coursework.SERVICES.OSDetector;
import cz.cvut.fel.coursework.SERVICES.QRGenerator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {

    public void getIP() {
        IPIdentifier ipIdentifier = new IPIdentifier();
        String ip = ipIdentifier.getIP();
        Globals.setIP(ip);
    }

    public void getAppDirectory() {
        String userHome = System.getProperty("user.home");
        File theDir = new File(userHome + "/" + "NotificationDisplayer");

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            }
            catch (SecurityException e){
                e.printStackTrace();
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
        Globals.setAppDirectory(userHome + "/" + theDir.getName());
    }

    public void generatePathToImage() {
        Globals.setIMGPATH(Globals.getAppDirectory() + "/" + "qr.png");
        System.out.println("Path was set to: " + Globals.getIMGPATH());
    }

    public void saveQR() {
        QRGenerator qr = new QRGenerator();
        qr.saveQR();
    }

    public void initializeGUI() {
        MainWindow main = new MainWindow();
        main.createWindow();
    }

    public String getOS() {
        Enum os = OSDetector.getOS();
        return os.toString();
    }
}
