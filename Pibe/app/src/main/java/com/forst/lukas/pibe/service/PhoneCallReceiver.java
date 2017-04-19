package com.forst.lukas.pibe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.forst.lukas.pibe.data.PibeData;
import com.forst.lukas.pibe.tasks.ServerCommunication;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneCallReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager mtelephony = (TelephonyManager)
                context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mtelephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(TAG, "" + state + ", number: " + incomingNumber);
                        sendData(context, incomingNumber);
                        break;
                    default:
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void sendData(Context context, String incomingNumber) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("incomingNumber", incomingNumber);
            if (PibeData.isReadContactsPermission()) {
                jsonObject.put("contact_name", getContactName(context, incomingNumber));
            }

            new ServerCommunication().sendJSON(jsonObject);
        } catch (JSONException ignored) {
        }
    }

    private String getContactName(Context context, String incomingNumber) {
        String contactName = "";
        // TODO: 19/04/17 get contact name
        return contactName;
    }
}
