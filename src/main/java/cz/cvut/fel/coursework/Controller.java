package cz.cvut.fel.coursework;

import cz.cvut.fel.coursework.GUI.MainWindow;
import cz.cvut.fel.coursework.SERVICES.IPIdentifier;
import cz.cvut.fel.coursework.SERVICES.OSDetection;
import cz.cvut.fel.coursework.SERVICES.QRCodeCreator;

public class Controller {

    public void getIP() {
        IPIdentifier ipIdentifier = new IPIdentifier();
        String ip = ipIdentifier.getIP();
        System.out.println(Globals.IP);
        Globals.setIP(ip);
        System.out.println(Globals.IP);
    }

    public void saveQR() {
        QRCodeCreator qr = new QRCodeCreator();
        qr.saveQR();
    }

    public void initializeGUI() {
        MainWindow main = new MainWindow();
        main.createWindow();
    }

    public String getOS() {
        Enum os = OSDetection.getOS();
        return os.toString();
    }
}
