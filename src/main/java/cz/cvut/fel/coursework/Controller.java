package cz.cvut.fel.coursework;

import cz.cvut.fel.coursework.GUI.MainWindow;
import cz.cvut.fel.coursework.SERVER.IPIdentifier;
import cz.cvut.fel.coursework.SERVER.OSDetection;

public class Controller {

    public void getIP() {
        IPIdentifier ipIdentifier = new IPIdentifier();
        ipIdentifier.getIP();
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
