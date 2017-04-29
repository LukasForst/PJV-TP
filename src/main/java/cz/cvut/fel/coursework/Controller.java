package cz.cvut.fel.coursework;

import cz.cvut.fel.coursework.GUI.MainWindow;
import cz.cvut.fel.coursework.SERVER.IPIdentifier;

public class Controller {

    public void getIP() {
        IPIdentifier ipIdentifier = new IPIdentifier();
        ipIdentifier.getIP();
    }

    public void initializeGUI() {
        MainWindow main = new MainWindow();
        main.createWindow();
    }
}
