package cz.cvut.fel.coursework.SERVICES;

import org.json.JSONObject;
import cz.cvut.fel.coursework.Controller;

public class Notification {

    Controller c = new Controller();

    public void notificate(String message) {

        if (c.getOS().equals("MAC")) {
            macNotificator(message);
        }

        // TODO: Handle other OS
    }

    public void macNotificator(String message) {

        try {

            JSONObject obj = new JSONObject(message);
            String messagePackage = obj.getString("package");
            String messageContent = obj.getString("tickerText");

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
}
