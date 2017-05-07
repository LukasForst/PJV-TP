package cz.cvut.fel.coursework.SERVICES;

import cz.cvut.fel.coursework.Controller;
import org.json.JSONObject;

import java.io.IOException;

public class Notification {

    Controller c = new Controller();

    public void notificate(String message) {
        JSONObject obj = new JSONObject(message);
        String messagePackage = obj.getString("package");
        String messageContent = obj.getString("tickerText");


        if (c.getOS().equals("MAC")) {
            macNotificator(messagePackage, messageContent);
        } else if (c.getOS().equals("LINUX")) {
            linuxNotificator(messagePackage, messageContent);
        } else if (c.getOS().equals("WINDOWS_10")) {
            windows10Notificator(messagePackage, messageContent);
        }

        // TODO: 5/5/17 Handle other OS like LINUX_OTHER, WINDOWS_OTHER, UNSUPPORTED
    }

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

    public void linuxNotificator(String messagePackage, String messageContent) {
        String[] processName = {"notify-send", messagePackage, messageContent};
        try {
            Process myProcess = Runtime.getRuntime().exec(processName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
