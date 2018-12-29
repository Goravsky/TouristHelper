package com.Goravsky.touristhelper.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Goravsky.touristhelper.QrTypeDecoder;
import com.Goravsky.touristhelper.data.QrHistory;
import com.Goravsky.touristhelper.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final int CAMERA_PERMISSION_CODE = 1;

    private SurfaceView cameraView;
    private LinearLayout resultLayout;
    private TextView resultText;
    private Button specialButton;
    private Button getTextButton;
    private ImageButton historyButton;
    private CameraSource cameraSource;
    private Uri qrUri;
    private Intent specialIntent;
    private String qrType = "Text";
    private QrHistory qrHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Hide status bar and action bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        initComponents(); //инициализация компонентов

        //Запрос на использование камеры
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            createCameraSource(); //обращение к камере если разрешение было получено ранее
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        qrHistory.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resultLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        qrHistory.close();
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
                            QrTypeDecoder typeDecoder = new QrTypeDecoder(qrUri);
                            typeDecoder.decode();
                            qrType = typeDecoder.getType();

                            qrHistory.insert(qrType, new SimpleDateFormat("dd MMM").format(new Date()), qrUri.toString());

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

    private void initComponents(){
        cameraView = findViewById(R.id.cam_view);
        resultLayout = findViewById(R.id.result_layout);
        resultText = findViewById(R.id.qr_content);
        historyButton = findViewById(R.id.history_button);
        specialButton = findViewById(R.id.open_in_browser_button);
        getTextButton = findViewById(R.id.get_qr_result_button);
        qrHistory = new QrHistory(MainActivity.this, null, 1);

        historyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyIntent);
            }
        });
        getTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle getTextBundle = new Bundle();
                getTextBundle.putString("qrType", qrType);
                getTextBundle.putString("barcode", qrUri.toString());

                Intent getTextIntent = new Intent(MainActivity.this, DataActivity.class);
                getTextIntent.putExtra("content", getTextBundle);
                startActivity(getTextIntent);
            }
        });
        specialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(specialIntent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        createCameraSource();
        startCameraSourse();
    }
}