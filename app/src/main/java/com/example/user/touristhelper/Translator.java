package com.example.user.touristhelper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Goravsky on 15/11/2018.
 */
public class Translator {

    private final String YANDEX_KEY = "trnsl.1.1.20181114T093141Z.b4840f8af776fa0c.f9b8e2b6d4b833f9ac4971aa9e0fb47779d885d9";
    private final String BASE_YANDEX_URL = "https://translate.yandex.net/";

    private String textForTranslate;
    private String resultString = "Failure to translate text";
    private YandexService yandexService;

    public Translator(String text) {

        textForTranslate = text;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_YANDEX_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        yandexService = retrofit.create(YandexService.class);
    }

    public void translate() {
        String recievedLanguage;
        String targetLanguage;

        //Запрос к Yandex API для определения языка
        Call<DetectLanguageModel> detectCall = yandexService.detectLanguage(YANDEX_KEY, textForTranslate);
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
            }else {
                resultString = textForTranslate;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResultString() {
        return resultString;
    }
}
