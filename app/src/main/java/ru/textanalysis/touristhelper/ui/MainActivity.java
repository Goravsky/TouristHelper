package ru.textanalysis.touristhelper.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;


import ru.textanalysis.touristhelper.QrTypeDecoder;
import ru.textanalysis.touristhelper.R;
import ru.textanalysis.touristhelper.data.QrHistory;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class MainActivity extends AppCompatActivity implements BarcodeRetriever {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BarcodeCapture barcodeCapture;
    private ImageButton historyButton;
    private ImageButton flashButton;
    private Uri qrUri;
    private QrHistory qrHistory;
    private ResultFragment result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Hide status bar and action bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);

        historyButton = findViewById(R.id.history_button);
        flashButton = findViewById(R.id.flash_button);
        result = (ResultFragment) getFragmentManager().findFragmentById(R.id.scanner_result_fragment);
        qrHistory = new QrHistory(MainActivity.this, null, 1);
        historyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyIntent);
            }
        });
        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeCapture.setShowFlash(!barcodeCapture.isShowFlash());
                barcodeCapture.refresh();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        barcodeCapture.setShowDrawRect(true)
                .setTouchAsCallback(true)
                .setShouldShowText(true)
                .shouldAutoFocus(true);
        barcodeCapture.refresh();

        qrHistory.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        result.setVisability(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        qrHistory.close();
    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        Log.d(TAG, "Barcode read: " + barcode.displayValue);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                barcodeCapture.pause();

                qrUri = Uri.parse(barcode.displayValue);
                QrTypeDecoder typeDecoder = new QrTypeDecoder(qrUri);
                typeDecoder.decode();

                qrHistory.insert(typeDecoder.getType(), new SimpleDateFormat("dd MMM").format(new Date()), qrUri.toString());

                result.setDecoder(typeDecoder);
                result.setVisability(true);
            }
        });
    }

    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String reason) {

    }

    @Override
    public void onPermissionRequestDenied() {

    }

    @Override
    public void onBackPressed() {
        if(result.isActive()){
            result.setVisability(false);
            barcodeCapture.resume();
        }else{
            finish();
        }
    }
}