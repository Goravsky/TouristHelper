package com.Goravsky.touristhelper.translation;

import android.os.AsyncTask;

import com.Goravsky.touristhelper.GetTextListener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
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
    private String selectedLanguage;

    //Конструктор для DataActivity
    public Translator(GetTextListener listener, String sLanguage) {
        this.getTextListener = listener;

        selectedLanguage = sLanguage;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_YANDEX_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient().newBuilder()
                        .connectTimeout(5, TimeUnit.MINUTES)
                        .callTimeout(5, TimeUnit.MINUTES)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .writeTimeout(5, TimeUnit.MINUTES)
                        .build())
                .build();
        yandexService = retrofit.create(YandexService.class);
    }

    //Конструктор для TextSimplifier
    public Translator(String sLanguage){
        selectedLanguage = sLanguage;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_YANDEX_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        yandexService = retrofit.create(YandexService.class);
    }

    @Override
    protected String doInBackground(String... strings) {
        String language = getLanguage(strings[0]);
        String result;
        if(language.equals(selectedLanguage)){
            result = strings[0];
        }else{
            result = translate(strings[0],language);
        }
        return result;
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

    public String getLanguage(String text) {
        String language = null;
        if(text.length() > 300){
            String temp = text.substring(0,300);
            text = temp;
        }
        Call<DetectLanguageModel> detectCall = yandexService.detectLanguage(YANDEX_KEY, text);               //Запрос к Yandex API для определения языка

        try {
            language = detectCall.execute().body().getLang();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return language;
    }

    /*
    TODO разобраться с timeout
     */
    public String translate(String text, String recievedLanguage) {
        String resultString = "Failure to translate text";
        String requestLanguage = recievedLanguage + "-" + selectedLanguage;
        //ограничение размера текста 7000 символов
        if (text.length() > 8000){
            String temp = text.substring(0,8000);
            int index = Math.max(temp.lastIndexOf("."), temp.lastIndexOf("!"));
            index = Math.max(index, temp.lastIndexOf("?"));
            String temp2 = temp.substring(0,index + 1);
            text = temp2;
        }

        //Запрос к Yandex API для перевода
        Call<TranslateModel> translateCall = yandexService.translate(YANDEX_KEY, text, requestLanguage);
        try {
            Response<TranslateModel> translateModel = translateCall.execute();
            if (translateModel.isSuccessful()){
                resultString = translateModel.body().getText()[0];
            }else{
                resultString = translateModel.code() + ": " + translateModel.message() + "\n"
                + "Text size: " + text.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultString = e.getMessage();
        }

        return resultString;
    }
}
