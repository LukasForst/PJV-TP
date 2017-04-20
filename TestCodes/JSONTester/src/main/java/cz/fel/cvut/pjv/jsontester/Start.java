package cz.fel.cvut.pjv.jsontester;

import org.json.JSONObject;

/**
 * Created by lukas on 4/20/17.
 */
public class Start {
    private static final int PORT = 3843;
    private static final String IP_ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
        JsonManager jm = new JsonManager();
        JSONObject notification = jm.generateNotification();
        JSONObject call = jm.generateCall();

        SendJSON sendJSON = new SendJSON(IP_ADDRESS, PORT);

        sendJSON.send(notification);
        //sendJSON.send(call);
        System.out.println("JSON sent.");
    }
}
