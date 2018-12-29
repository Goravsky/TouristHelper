package com.Goravsky.touristhelper;

import android.os.AsyncTask;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Goravsky on 15/11/2018.
 */
public class Translator extends AsyncTask<String, Void, String> {

    private final String YANDEX_KEY = "trnsl.1.1.20181114T093141Z.b4840f8af776fa0c.f9b8e2b6d4b833f9ac4971aa9e0fb47779d885d9";
    private final String BASE_YANDEX_URL = "https://translate.yandex.net/";

    private YandexService yandexService;
    private GetTextListener getTextListener;

    public Translator(GetTextListener listener) {
        this.getTextListener = listener;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_YANDEX_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        yandexService = retrofit.create(YandexService.class);
    }

    @Override
    protected String doInBackground(String... strings) {
        return translate(strings[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        getTextListener.onTextProcessStarted();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        getTextListener.onTextComplete(s);
    }

    public String translate(String text) {
        String textForTranslate = text;
        String resultString = "Failure to translate text";
        String recievedLanguage;
        String targetLanguage;

        //Запрос к Yandex API для определения языка
        Call<DetectLanguageModel> detectCall;

        if (textForTranslate.length() > 100) {
            detectCall = yandexService.detectLanguage(YANDEX_KEY, textForTranslate.substring(0, 100));  // для определения языка посылаются первые 100 символов
        } else {
            detectCall = yandexService.detectLanguage(YANDEX_KEY, textForTranslate);
        }
        try {
            recievedLanguage = detectCall.execute().body().getLang();
            if (recievedLanguage != "ru") {
                targetLanguage = recievedLanguage + "-ru";

                //Запрос к Yandex API для перевода
                Call<TranslateModel> translateCall = yandexService.translate(YANDEX_KEY, textForTranslate, targetLanguage);
                try {
                    resultString = translateCall.execute().body().getText()[0];
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                resultString = textForTranslate;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultString;
    }
}
