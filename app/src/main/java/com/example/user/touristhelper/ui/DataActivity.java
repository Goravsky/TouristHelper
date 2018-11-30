package com.example.user.touristhelper.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.touristhelper.R;
import com.example.user.touristhelper.Translator;
import com.example.user.touristhelper.WebPageParser;

public class DataActivity extends Activity {

    private TextView urlField;
    private TextView resultField;
    private Button getTextButton;
    private Button translateButton;
    private Button simplifyButton;
    private ProgressBar progressBar;

    private String receivedData;
    private String textResult;
    private WebPageParser webPageParser;
    private Translator translatorTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        getTextButton = findViewById(R.id.gettext_button);
        getTextButton.setEnabled(false);
        translateButton = findViewById(R.id.translate_button);
        translateButton.setEnabled(false);
        simplifyButton = findViewById(R.id.simplify_button);
        simplifyButton.setEnabled(false);
        urlField = findViewById(R.id.main_url_view);
        resultField = findViewById(R.id.result_view);
        progressBar = findViewById(R.id.progress_bar);
        resultField.setMovementMethod(new ScrollingMovementMethod());
        progressBar.setVisibility(View.INVISIBLE);

        receivedData = getIntent().getStringExtra("barcode");

        if(android.util.Patterns.WEB_URL.matcher(receivedData).matches()){
            getTextButton.setEnabled(true);
            urlField.setText(receivedData);
        }else{
            translateButton.setEnabled(true);
            simplifyButton.setEnabled(true);
            urlField.setText("Text:");
            resultField.setText(receivedData);
            textResult = receivedData;
        }

        getTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateButton.setEnabled(true);
                simplifyButton.setEnabled(true);
                ParseTask myParser = new ParseTask();      //создание фонового потока
                myParser.execute();
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslationTask myTranslator = new TranslationTask();
                myTranslator.execute();
            }
        });
    }

    /*
    Фоновый поток, в котором будет производится парсинг веб-страницы
    */
    class ParseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            webPageParser = new WebPageParser();
            webPageParser.parseUrl(receivedData);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.INVISIBLE);
            textResult = webPageParser.getTextContent();
            resultField.setText(textResult);
        }
    }

    /*
     Фоновый поток в котором выполняется перевод текста
     */
    class TranslationTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            resultField.setText("");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            translatorTask = new Translator(textResult);
            translatorTask.translate();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressBar.setVisibility(View.INVISIBLE);
            resultField.setText(translatorTask.getResultString());
        }
    }
}
