package cz.fel.cvut.pjv.jsontester;

import org.json.JSONException;
import org.json.JSONObject;
import sun.rmi.runtime.Log;

import java.util.Random;
import java.util.logging.Logger;

/**
 @author Lukas Forst
 */
public class JsonManager {

    private static final String TAG = "JsonManager";

    public JSONObject generateNotification(){
        JSONObject notification = new JSONObject();
        try {
            notification
                    .put("package", getPackage())
                    .put("tickerText", getTicker())
                    .put("id", getID())
                    .put("category", getCategory())
                    .put("onPostTime", getOnPostTime());
        } catch (Exception ignored){}
        return notification;
    }

    public JSONObject generateActiveNotifications(int numberOfNotifications){
        JSONObject active = new JSONObject();
        active.put("json_active", getAllActiveNotifications(numberOfNotifications));
        return active;
    }

    public JSONObject generateCall(){
        JSONObject call = new JSONObject();
        try {
            call.put("incoming_call", getIncomingNumber());
            if(new Random().nextInt() % 2 == 0){
                call.put("contact_name", getContactName());
            }
        } catch (JSONException ignored){}
        return call;
    }

    private String getContactName(){
        String names[] = {"Jacalyn Alvarado", "Karrie Bolyard", "Alysa Clutter",
                "Eliana Buse", "Oren Rodda", "Alysia Stancil", "Debbi Zetina", "Shanita Wolff"
        };
        return names[new Random().nextInt(names.length - 1)];
    }

    private String getIncomingNumber(){
        String numbers[] = {
                "(251) 546-9442", "(125) 546-4478", "(949)5694371", "(630)4468851", "2269062721"
        };
        return numbers[new Random().nextInt(numbers.length - 1)];
    }

    private String getTicker(){
        String tickers[] = {
            "Leprology isobront overgenerous ungraved",
                "subordinative. Boyo mangabey hyperscholastically precystic artistr" +
                        "y. Telemann paralytic overforced brininess employable",
                " amet, consectetur adipiscing elit. Mauris lorem ipsum, iaculis eu posuere a, dic" +
                        "tum eu purus. Nulla in ante ut purus viverra sodale s. Donec rutrum elit non " +
                        "fermentum pharetra. Donec ornare ornare",
                ":D", ":*", "Hello!", "How Are you today?", "facilisis purus. Vestibulum sed ipsum consequat"
        };
        return tickers[new Random().nextInt(tickers.length - 1)];
    }

    private String getPackage(){
        String appNames[] = {
                "Facebook", "Disa", "Messenger", "SMS", "Android system", "GMail", "Inbox"
        };
        return appNames[new Random().nextInt(appNames.length - 1)];
    }

    private String getID(){
        return String.valueOf(new Random().nextInt());
    }

    private String getCategory(){
        String categories[] = {
                "molasses", "sausages", "salmon", "watermelons", "rice vinegar", "sys"
        };
        return categories[new Random().nextInt(categories.length - 1)];
    }

    private String getOnPostTime(){
        long id = new Random().nextLong();
        id = id > 0 ? id : id * (-1);
        return String.valueOf(id);
    }

    private JSONObject getAllActiveNotifications(int numberOfNotifications) {

        JSONObject activeNotification = new JSONObject();
        //we can't handle it with i, because of possible errors while parsing
        int numberOfStoredNotifications = 0;

        for (int i =0; i < numberOfNotifications; i++) {
            try {
                JSONObject currentNotification = generateNotification();

                activeNotification.put("active_" + numberOfStoredNotifications++,
                        currentNotification);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return activeNotification;
    }
}
