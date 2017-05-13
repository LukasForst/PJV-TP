package cz.cvut.fel.coursework;

import cz.cvut.fel.coursework.GUI.MainWindow;
import cz.cvut.fel.coursework.SERVICES.AppDirectory;
import cz.cvut.fel.coursework.SERVICES.IPIdentifier;
import cz.cvut.fel.coursework.SERVICES.OSDetector;
import cz.cvut.fel.coursework.SERVICES.QRGenerator;

public class Controller {

    public void setIP() {
        IPIdentifier ipIdentifier = new IPIdentifier();
        String ip = ipIdentifier.getIP();
        Globals.setIP(ip);
    }

    public void setAppDirectory() {
        AppDirectory ad = new AppDirectory();
        ad.createAppDirectory();
        Globals.setAppDirectory(ad.getUserHome() + "/" + ad.getDirName());
    }

    public void setPathToImage() {
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
