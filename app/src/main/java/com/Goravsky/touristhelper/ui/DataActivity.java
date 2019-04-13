package com.Goravsky.touristhelper.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.Goravsky.touristhelper.GetTextListener;
import com.Goravsky.touristhelper.simplification.TextSimplifier;
import com.Goravsky.touristhelper.translation.Translator;
import com.Goravsky.touristhelper.WebPageParser;
import com.Goravsky.touristhelper.R;

public class DataActivity extends Activity implements GetTextListener {

    private TextView urlField;
    private TextView resultField;
    private Button translateButton;
    private Button simplifyButton;
    private ProgressBar progressBar;
    private Spinner languageList;
    private String receivedData;

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
                Translator translator = new Translator(DataActivity.this, languageList.getSelectedItem().toString().toLowerCase());
                translator.execute(receivedData);
            }
        });

        simplifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextSimplifier simplifier = new TextSimplifier(DataActivity.this, languageList.getSelectedItem().toString().toLowerCase());
                simplifier.execute(receivedData);
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
        resultField.setMovementMethod(ScrollingMovementMethod.getInstance());
        progressBar = findViewById(R.id.progress_bar);
        languageList = findViewById(R.id.spinner);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void decodeQrType(){
        Bundle recievedBundle = getIntent().getBundleExtra("content");
        String receivedUrl = recievedBundle.get("barcode").toString().trim();
        String qrType = recievedBundle.get("qrType").toString().trim();
        if (qrType.equals("link")) {
            urlField.setText(receivedUrl);
            WebPageParser parser = new WebPageParser(DataActivity.this);      //создание фонового потока парсера веб-страницы
            parser.execute(receivedUrl);
        } else {
            urlField.setText(qrType.toUpperCase());
            receivedData = receivedUrl;
            resultField.setText(receivedUrl);
        }
    }

    @Override
    public void onTextProcessStarted() {
        resultField.setText("");
        progressBar.setVisibility(View.VISIBLE);
    }

    /*
    @Override
    public void onSimplifyComplete(String response) {
        progressBar.setVisibility(View.INVISIBLE);
        if (response.length() > 8000){
            resultField.setText(response.substring(0, 8000));
        }else{
            resultField.setText(response);
        }
    }
    */

    @Override
    public void onTextComplete(String result) {
        progressBar.setVisibility(View.INVISIBLE);
        receivedData = result;
        resultField.setText(result);
    }

    @Override
    public void onSimplifyComplete(Spannable response) {
        progressBar.setVisibility(View.INVISIBLE);
        resultField.setText(response);
    }
}
