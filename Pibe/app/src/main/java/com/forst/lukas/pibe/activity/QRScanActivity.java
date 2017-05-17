package com.forst.lukas.pibe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Activity which provide support for QR Scanning port and ip address. Result of the scanning
 * is handled in the {@link QRScanActivity#handleResult(Result)}<br>
 * QR code is in this format:<br>
 *     <i>IP *.*.*.*</i><br><i>PORT ****</i><br>
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
        Log.v(TAG, rawResult.getBarcodeFormat().toString());

        String ip = parseIP(resultText);
        int port = parsePort(resultText);

        Intent it = new Intent();
        it.putExtra("IP", ip);
        it.putExtra("port", port);

        if (ip.equals("") || ip.equals(" ") || port == -1) {
            setResult(101, it);
        } else {
            setResult(100, it);
        }
        finish();
    }

    /**
     * Parse IPv4 address from given raw data.<br>
     * Raw data has to contain <code>IP *.*.*.*</code> otherwise empty string is returned.
     */
    private String parseIP(String rawData) {
        String returnValue = "";
        if (rawData.contains("IP")) {
            for (String s : rawData.split("\n")) {
                if (s.contains("IP")) {
                    try {
                        returnValue = s.split(" ")[1];
                    } catch (NumberFormatException nfe) {
                        returnValue = "";
                    }
                }
            }
        }
        return returnValue;
    }

    /**
     * Parse port from given raw data.<br><i>PORT *****</i>, if there's no port number in the
     * raw data, <b>-1</b> is returned.
     */
    private int parsePort(String rawData) {
        int returnValue = -1;
        if (rawData.contains("PORT")) {
            for (String s : rawData.split("\n")) {
                if (s.contains("PORT")) {
                    try {
                        returnValue = Integer.parseInt(s.split(" ")[1]);
                    } catch (NumberFormatException nfe) {
                        returnValue = -1;
                    }
                }
            }
        }
        return returnValue;
    }

}