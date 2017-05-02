package cz.fel.cvut.pjv.jsontester;

import org.json.JSONObject;

/**
 * @author Lukas Forst
 */
public class Start {
    private static final int PORT = 3843;
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int NUMBER_OF_NOTIFICATION = 3;


    public static void main(String[] args) {
        JsonManager jm = new JsonManager();

        JSONObject notification = jm.generateNotification();
        JSONObject call = jm.generateCall();
        JSONObject activeNotifications = jm.generateActiveNotifications(NUMBER_OF_NOTIFICATION);

        SendJSON sendJSON = new SendJSON(IP_ADDRESS, PORT);

        sendJSON.send(notification);
        sendJSON.send(activeNotifications);
        //sendJSON.send(call);
        System.out.println("JSON sent.");
    }
}
