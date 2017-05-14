package cz.cvut.fel.coursework;

import cz.cvut.fel.coursework.GUI.MainWindow;
import cz.cvut.fel.coursework.SERVICES.AppDirectory;
import cz.cvut.fel.coursework.SERVICES.IPIdentifier;
import cz.cvut.fel.coursework.SERVICES.OSDetector;
import cz.cvut.fel.coursework.SERVICES.QRGenerator;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides program's main functions.
 * Provides also logging to console and logfile 'log/controller.log'.
 * @author Anastasia Surikova
 */

public class Controller {

    private static final Logger LOG = Logger.getLogger(Controller.class.getName());

    public Controller() {
        LOG.setUseParentHandlers(false);
        try {
            LOG.addHandler(new FileHandler("log/controller.log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls IPIndentifier class and its method <b>getIP()</b>
     * Finds out hoster's IP address and sets it as global variable, so it can be used by other classes.
     * @see IPIdentifier
     */
    public void setIP() {
        IPIdentifier ipIdentifier = new IPIdentifier();
        String ip = ipIdentifier.getIP();
        Globals.setIP(ip);
        LOG.log(Level.INFO, "IP address was detected as: " + Globals.getIP());
        System.out.println("IP address was detected as: " + Globals.getIP());
    }

    /**
     * Calls AppDirectory class and its method <b>createAppDirectory()</b>.
     * Creates program's directory in user's home directory, where program output files (i.e. QR code image) will be kept.
     * Sets created path as global variable, so it can be used by other classes.
     * @see AppDirectory
     */
    public void setAppDirectory() {
        AppDirectory ad = new AppDirectory();
        ad.createAppDirectory();
        Globals.setAppDirectory(ad.getUserHome() + "/" + ad.getDirName());
        LOG.log(Level.INFO, "Program's directory was set to: " + Globals.getAPP_DIRECTORY());
        System.out.println("Program's directory was set to: " + Globals.getAPP_DIRECTORY());
    }

    /**
     * Gets path to program's directory and specifies the path to QR code image.
     */
    public void setPathToImage() {
        Globals.setImgPath(Globals.getAPP_DIRECTORY() + "/" + "qr.png");
        LOG.log(Level.INFO, "Path to image was set to: " + Globals.getIMG_PATH());
        System.out.println("Path to image was set to: " + Globals.getIMG_PATH());
    }

    /**
     * Calls QRGenerator class and its method <b>saveQR()</b>.
     * @see QRGenerator
     */
    public void saveQR() {
        QRGenerator qr = new QRGenerator();
        qr.saveQR();
        LOG.log(Level.INFO, "Image with QR code was saved." );
        System.out.println("Image with QR code was saved." );
    }

    /**
     * Calls OSDetector class and its method <b>getOS()</b>.
     * @return String Returns name of OS
     * @see OSDetector
     */
    public String getOS() {
        Enum os = OSDetector.getOS();
        LOG.log(Level.INFO, "Operating system was detected as: " + os.toString());
        System.out.println("Operating system was detected as: " + os.toString());
        return os.toString();
    }

    /**
     * Calls MainWindow class and its method <b>createWindow()</b> which initializes GUI.
     * @see MainWindow
     */
    public void initializeGUI() {
        MainWindow main = new MainWindow();
        main.createWindow();
        LOG.log(Level.INFO, "GUI was initialized.");
        System.out.println("GUI was initialized.");
    }
}
