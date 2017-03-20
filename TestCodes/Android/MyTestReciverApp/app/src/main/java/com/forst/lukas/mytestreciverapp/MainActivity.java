package com.forst.lukas.mytestreciverapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static int a = 0;
    private NotificationReceiver nReceiver;

    private Socket client;
    private InputStreamReader isr;
    private BufferedReader bf;
    //private AsyncClass ac;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.forst.lukas.mytestreciverapp.NOTIFICATION_EVENT");

        registerReceiver(nReceiver, filter);
    }

    private void setText(String read){
        TextView tx = (TextView) findViewById(R.id.log);
        tx.setText(read);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    public void buttonClicked(View view){

        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        android.support.v4.app.NotificationCompat.Builder ncomp = new android.support.v4.app.NotificationCompat.Builder(this);
        ncomp.setContentTitle("My Notification");
        ncomp.setContentText("Notification Listener Service Example");
        ncomp.setTicker("Notification Listener Service Example");
        ncomp.setSmallIcon(R.mipmap.ic_launcher);
        ncomp.setAutoCancel(true);
        nManager.notify((int)System.currentTimeMillis(),ncomp.build());


      /*  ac = new AsyncClass();
        ac.execute();
        Button b = (Button) findViewById(R.id.button);
        b.setEnabled(false);
        */
    }

    private MainActivity getMain(){
        return this;
    }

    public class NotificationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView removedText = (TextView) findViewById(R.id.removed_notification);
            TextView receivedText = (TextView) findViewById(R.id.received_notification);
            TextView log = (TextView) findViewById(R.id.log);

            try{
                JSONObject notification = new JSONObject(intent.getStringExtra("json"));
                Log.i("json", notification.toString());

                if(!notification.toString().contains("sys")){
                    new AsyncClass().execute(notification);
                }

            } catch (Exception e){
                Log.i("json error", e.getMessage());
            }

            log.setText( intent.getStringExtra("notification_posted") == null ? log.getText()
                    : intent.getStringExtra("notification_posted") + "\n" + log.getText() );
        }
    }


    class AsyncClass extends AsyncTask<JSONObject, Void,Void>
    {
        protected Void doInBackground(JSONObject... params)
        {
            //hardtask();
            sendJSON(params[0]);
            return null;
        }
        public void hardtask()
        {
            try
            {
                String serverName = "192.168.1.97";
                int port = 3843;
                JSONObject json = null;
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getMain());

                try{
                    client = new Socket(serverName, port);

                    InputStreamReader input = new InputStreamReader(client.getInputStream());
                    BufferedReader br = new BufferedReader(input);
                    OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
                    BufferedWriter bw = new BufferedWriter(out);

                    Log.i("communication", "successfully connected ");

                    bw.write("send_json\n");
                    bw.flush();

                    Log.i("communication", "json sent");
                    String read = br.readLine();

                    Log.i("communication", "json received");


                    if(read.equals("")){
                        mBuilder.setContentTitle("Err");
                        mBuilder.setContentText("err");
                        mBuilder.setTicker("err");
                        return;
                    }

                    Log.i("JSON", read);
                    json = new JSONObject(read);

                    mBuilder.setContentTitle(json.getString("title"));
                    mBuilder.setContentText(json.getString("text"));
                    mBuilder.setTicker(json.getString("ticker"));


                } catch (IOException e){
                    e.printStackTrace();
                    String exception = "IO";
                    mBuilder.setContentTitle(exception);
                    mBuilder.setContentText(exception);
                    mBuilder.setTicker(exception);

                } catch (JSONException e){
                    String exception = "JSON";
                    mBuilder.setContentTitle(exception);
                    mBuilder.setContentText(exception);
                    mBuilder.setTicker(exception);
                }

                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setAutoCancel(true);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(++a, mBuilder.build());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        protected boolean sendJSON(JSONObject jsonObject) {
            try{
                String serverName = "10.50.108.77";
                int port = 3843;

                client = new Socket(serverName, port);

                InputStreamReader input = new InputStreamReader(client.getInputStream());
                BufferedReader br = new BufferedReader(input);
                OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
                BufferedWriter bw = new BufferedWriter(out);

                Log.i("communication", "successfully connected ");

                bw.write(jsonObject.toString());
                bw.flush();

                client.close();
                return true;
            } catch (Exception e){
                Log.i("sendJsonProblem", e.getMessage());
                return false;
            }

        }
    }


}
