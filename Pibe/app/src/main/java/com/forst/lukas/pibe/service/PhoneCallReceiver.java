package com.forst.lukas.pibe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.forst.lukas.pibe.data.PibeData;
import com.forst.lukas.pibe.tasks.ServerCommunication;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * @author Lukas Forst
 * */
public class PhoneCallReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(PibeData.hasReadPhoneStatePermission()) return;

        TelephonyManager mtelephony = (TelephonyManager)
                context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mtelephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d(TAG, "Incoming number: " + incomingNumber);
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
            jsonObject.put("incoming_call", incomingNumber);
            if (PibeData.isReadContactsPermission()) {
                new SendWithContactName(context, jsonObject).execute(incomingNumber);
            } else {
                new ServerCommunication().sendJSON(jsonObject);
            }
        } catch (JSONException ignored) {
        }
    }

    private class SendWithContactName extends AsyncTask<String, Void, String> {
        private Context context;
        private JSONObject jsonObject;

        private SendWithContactName(Context context, JSONObject jsonObject) {
            this.context = context;
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPostExecute(String name) {
            super.onPostExecute(name);
            try {
                jsonObject.put("contact_name", name);
                new ServerCommunication().sendJSON(jsonObject);
            } catch (JSONException ignored) {
            }
        }

        @Override
        protected String doInBackground(String... params) {
            //get contact name
            String incomingNumber = params[0];
            String contactName;
            // define the columns I want the query to return
            String[] projection = new String[]{
                    ContactsContract.PhoneLookup.DISPLAY_NAME,
                    ContactsContract.PhoneLookup._ID};

            // encode the phone number and build the filter URI
            Uri contactUri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));

            // query time
            Cursor cursor = context.getContentResolver()
                    .query(contactUri, projection, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.PhoneLookup.DISPLAY_NAME));
                    Log.v(TAG, "Contact found - " + contactName + " - " + incomingNumber);
                } else {
                    Log.v(TAG, "Contact Not Found @ " + incomingNumber);
                    contactName = "(unknown)";
                }
                cursor.close();
            } else {
                contactName = "(unknown)";
            }
            return contactName;
        }
    }
}
