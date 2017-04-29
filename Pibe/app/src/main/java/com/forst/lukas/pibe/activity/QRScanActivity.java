package com.forst.lukas.pibe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * @see <a href="https://github.com/dm77/barcodescanner">Barcode Scanner Library - Github</a>
 */
public class QRScanActivity extends Activity implements ZXingScannerView.ResultHandler {
    private final String TAG = this.getClass().getSimpleName();

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        String resultText = rawResult.getText();
        Log.v(TAG, resultText); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        String ip = parseIP(resultText);
        int port = parsePort(resultText);

        Intent it = new Intent();
        it.putExtra("IP", ip);
        it.putExtra("port", port);

        if (ip.equals("") || port == -1) {
            setResult(101, it);
        } else {
            setResult(100, it);
        }
        finish();
    }

    private String parseIP(String rawData) {
        String returnValue = "";
        if (rawData.contains("IP")) {
            for (String s : rawData.split("\n")) {
                if (s.contains("IP")) {
                    returnValue = s.split(" ")[1];
                }
            }
        }
        return returnValue;
    }

    private int parsePort(String rawData) {
        int returnValue = -1;
        if (rawData.contains("PORT")) {
            for (String s : rawData.split("\n")) {
                if (s.contains("PORT")) {
                    returnValue = Integer.parseInt(s.split(" ")[1]);
                }
            }
        }
        return returnValue;
    }

}