package ru.textanalysis.touristhelper.ui;

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

import java.util.ArrayList;

import ru.textanalysis.touristhelper.GetTextListener;
import ru.textanalysis.touristhelper.simplification.TextSimplifier;
import ru.textanalysis.touristhelper.translation.Translator;
import ru.textanalysis.touristhelper.WebPageParser;
import ru.textanalysis.touristhelper.R;

public class DataActivity extends Activity implements GetTextListener {

    private TextView urlField;
    private TextView resultField;
    private Button translateButton;
    private Button simplifyButton;
    private ProgressBar progressBar;
    private Spinner languageList;
    private String receivedData;
    private String textLang;

    private String translateData;
    private Spannable simplifyData;
    private String eTranslateData;
    private Spannable eSimplifyData;

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
                if (translateData != null && languageList.getSelectedItem().toString().toLowerCase().equals("ru")) {
                    resultField.setText(translateData);
                } else if (eTranslateData != null && languageList.getSelectedItem().toString().toLowerCase().equals("en")) {
                    resultField.setText(eTranslateData);
                } else {
                    Translator translator = new Translator(DataActivity.this, languageList.getSelectedItem().toString().toLowerCase(),textLang);
                    translator.execute(receivedData);
                }
            }
        });

        simplifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simplifyData != null && languageList.getSelectedItem().toString().toLowerCase().equals("ru")) {
                    resultField.setText(simplifyData);
                } else if (eSimplifyData != null && languageList.getSelectedItem().toString().toLowerCase().equals("en")) {
                    resultField.setText(eSimplifyData);
                } else {
                    TextSimplifier simplifier = new TextSimplifier(DataActivity.this, languageList.getSelectedItem().toString().toLowerCase(),textLang);
                    simplifier.execute(receivedData);
                }
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

    @Override
    public void onTextComplete(ArrayList<String> result) {
        progressBar.setVisibility(View.INVISIBLE);
        receivedData = result.get(0);
        textLang = result.get(1).toLowerCase();
        resultField.setText(receivedData);
    }

    @Override
    public void onTranslateComplete(String result) {
        progressBar.setVisibility(View.INVISIBLE);
        resultField.setText(result);
        if(languageList.getSelectedItem().toString().toLowerCase().equals("en")){
            eTranslateData = result;
        }else{
            translateData = result;
        }
    }

    @Override
    public void onSimplifyComplete(Spannable result) {
        progressBar.setVisibility(View.INVISIBLE);
        resultField.setText(result);
        if(languageList.getSelectedItem().toString().toLowerCase().equals("en")){
            eSimplifyData = result;
        }else{
            simplifyData = result;
        }
    }
}
