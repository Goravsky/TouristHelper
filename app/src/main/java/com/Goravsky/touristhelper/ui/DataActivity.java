package com.Goravsky.touristhelper.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.Goravsky.touristhelper.GetTextListener;
import com.Goravsky.touristhelper.Translator;
import com.Goravsky.touristhelper.WebPageParser;
import com.Goravsky.touristhelper.R;

public class DataActivity extends Activity implements GetTextListener {

    private TextView urlField;
    private TextView resultField;
    private Button translateButton;
    private Button simplifyButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        initComponents();
        decodeQrType();

        translateButton.setEnabled(true);
        simplifyButton.setEnabled(true);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Translator translator = new Translator(DataActivity.this);
                translator.execute(resultField.getText().toString());
            }
        });
    }

    private void initComponents(){
        translateButton = findViewById(R.id.translate_button);
        translateButton.setEnabled(false);
        simplifyButton = findViewById(R.id.simplify_button);
        simplifyButton.setEnabled(false);
        urlField = findViewById(R.id.main_url_view);
        resultField = findViewById(R.id.result_view);
        progressBar = findViewById(R.id.progress_bar);
        resultField.setMovementMethod(new ScrollingMovementMethod());
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void decodeQrType(){
        Bundle recievedBundle = getIntent().getBundleExtra("content");
        String receivedData = recievedBundle.get("barcode").toString().trim();
        String qrType = recievedBundle.get("qrType").toString().trim();
        if (qrType.equals("link")) {
            urlField.setText(receivedData);
            WebPageParser parser = new WebPageParser(DataActivity.this);      //создание фонового потока парсера веб-страницы
            parser.execute(receivedData);
        } else {
            urlField.setText(qrType.toUpperCase());
            resultField.setText(receivedData);
        }
    }

    @Override
    public void onTextProcessStarted() {
        resultField.setText("");
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTextComplete(String response) {
        progressBar.setVisibility(View.INVISIBLE);
        resultField.setText(response);
    }
}
