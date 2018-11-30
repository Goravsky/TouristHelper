package com.example.user.touristhelper.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.touristhelper.QrTypeDecoder;
import com.example.user.touristhelper.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SurfaceView cameraView;
    private LinearLayout resultLayout;
    private TextView resultText;
    private Button specialButton;
    private Button getTextButton;
    private CameraSource cameraSource;
    private Uri qrUri;
    private Intent specialIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Hide status bar and action bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        cameraView = findViewById(R.id.cam_view);
        resultLayout = findViewById(R.id.result_layout);
        resultText = findViewById(R.id.qr_content);
        specialButton = findViewById(R.id.open_in_browser_button);
        getTextButton = findViewById(R.id.get_qr_result_button);

        getTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getTextIntent = new Intent(MainActivity.this, DataActivity.class);
                getTextIntent.putExtra("barcode", qrUri.toString());
                startActivity(getTextIntent);
            }
        });

        specialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(specialIntent);
            }
        });

        createCameraSource();
    }

    @Override
    protected void onResume() {
        super.onResume();

        resultLayout.setVisibility(View.GONE);
    }

    private void createCameraSource() {
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startCameraSourse();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cameraSource.stop();

                            qrUri = Uri.parse(barcodes.valueAt(0).displayValue);
                            System.out.println(qrUri);
                            QrTypeDecoder typeDecoder = new QrTypeDecoder(qrUri);
                            typeDecoder.decode();

                            specialButton.setText(typeDecoder.getButtonName());
                            specialIntent = typeDecoder.getIntent();
                            resultLayout.setVisibility(View.VISIBLE);
                            resultText.setText(qrUri.toString());
                        }
                    });
                }
            }
        });
    }

    private void startCameraSourse(){
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraSource.start(cameraView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(resultLayout.getVisibility() == View.VISIBLE){
            resultLayout.setVisibility(View.GONE);
            startCameraSourse();
        }else{
            finish();
        }
    }
}