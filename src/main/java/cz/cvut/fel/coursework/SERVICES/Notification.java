package cz.cvut.fel.coursework.SERVICES;

import cz.cvut.fel.coursework.Controller;
import org.json.JSONObject;
import javax.swing.JOptionPane;
import java.io.IOException;

/**
 * Provides notification due to user's operation system
 * @author Anastasia Surikova
 */
public class Notification {

    Controller c = new Controller();

    /**
     * Detects operating system and calls other methods to display notification.
     * Gets source package and text from parameter.
     * @param message notification content
     */
    public void notificate(String message) {

        // Get source package and text
        JSONObject obj = new JSONObject(message);
        String messagePackage = obj.getString("package");
        String messageContent = obj.getString("tickerText");

        // Detect operating system and call relatable methods
        if (c.getOS().equals("MAC")) {
            macNotificator(messagePackage, messageContent);
        } else if (c.getOS().equals("LINUX")) {
            linuxNotificator(messagePackage, messageContent);
        } else if (c.getOS().equals("WINDOWS_10")) {
            windows10Notificator(messagePackage, messageContent);
        } else {
            // LINUX_OTHER, WINDOWS_OTHER, UNSUPPORTED
            JOptionPane.showMessageDialog(null, "Your operating system is not supported", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Displays notification on MAC OSx operating system
     * @param messagePackage source package
     * @param messageContent notification text
     */
    public void macNotificator(String messagePackage, String messageContent) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("display notification \"");
            sb.append(messageContent);
            sb.append("\" with title \"");
            sb.append(messagePackage);
            sb.append("\"");
            String command = sb.toString();

            Runtime runtime = Runtime.getRuntime();
            String[] args = { "osascript", "-e", command };
            Process process = runtime.exec(args);

        } catch (Exception e) {
            System.out.println("Oops, something went wrong :(");
            System.out.println(e.toString());
        }
    }

    /**
     * Displays notification on Linux operating system
     * @param messagePackage source package
     * @param messageContent notification text
     */
    public void linuxNotificator(String messagePackage, String messageContent) {
        String[] processName = {"notify-send", messagePackage, messageContent};
        try {
            Process myProcess = Runtime.getRuntime().exec(processName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays notification on Windows10 operating system
     * @param messagePackage source package
     * @param messageContent notification text
     */
    public void windows10Notificator(String messagePackage, String messageContent) {
        String para = "-h " + messagePackage + " -t " + messageContent;
        String[] processName = {"ToastNotify.exe", para};
        try {
            Process myProcess = Runtime.getRuntime().exec(processName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
