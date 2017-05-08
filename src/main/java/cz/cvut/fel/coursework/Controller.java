package cz.cvut.fel.coursework;

import cz.cvut.fel.coursework.GUI.MainWindow;
import cz.cvut.fel.coursework.SERVICES.IPIdentifier;
import cz.cvut.fel.coursework.SERVICES.OSDetector;
import cz.cvut.fel.coursework.SERVICES.QRGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {

    public void getIP() {
        IPIdentifier ipIdentifier = new IPIdentifier();
        String ip = ipIdentifier.getIP();
        Globals.setIP(ip);
    }

    public void generatePathToImage() {
        // TODO: maybe save the file somewhere else? - Definitely best idea in the code
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        // TODO: 5/5/17 CHANGE IT! Otherwise null point exception!
        //Globals.setIMGPATH(s + "/src/main/java/cz/cvut/fel/coursework/RESOURCES/qr.png");
        Globals.setIMGPATH("qr.png");
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
