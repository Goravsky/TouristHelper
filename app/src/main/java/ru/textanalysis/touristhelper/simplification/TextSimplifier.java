package ru.textanalysis.touristhelper.simplification;

import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import ru.textanalysis.touristhelper.GetTextListener;
import ru.textanalysis.touristhelper.translation.Translator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TextSimplifier extends AsyncTask<String, Void, Spannable>  {
    private final String BASE_ANALYSIS_URL = "http://boberpul2.asuscomm.com:8088/";
    private final String BASE_TEXTSUMMARIZATION_URL = "https://resoomer.pro/";
    //private final String BASE_KEYWORDSEXTRACTION_URL = "http://api.cortical.io/rest/text/";
    private final String SUMMARIZATION_KEY = "AC6EA89A5A556335DE7724CE6598891D";

    private AnalysisService analysisService;
    private ResoomerService summarizationService;
    private CorticalService corticalService;
    private GetTextListener getTextListener;
    private OkHttpClient client;
    private String selectedLanguage;
    private String textLang;

    public TextSimplifier(GetTextListener listener, String sLanguage, String tLanguage) {
        this.getTextListener = listener;

        selectedLanguage = sLanguage;
        textLang = tLanguage;
        Retrofit retrofitResoomer = new Retrofit.Builder()
                .baseUrl(BASE_TEXTSUMMARIZATION_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Retrofit retrofitAnalysis = new Retrofit.Builder()
                .baseUrl(BASE_ANALYSIS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        analysisService = retrofitAnalysis.create(AnalysisService.class);
        summarizationService = retrofitResoomer.create(ResoomerService.class);

        client = new OkHttpClient().newBuilder().build();
    }

    private Spannable getEnglishSimplifying(String text){
        String summary = "Failure to simplify text";
        List<String> keyWords = new ArrayList<>();
        int percent = 100 - percentCount(text);

        //Call<EnglishSimplifyModel> call = summarizationService.getSummary(SUMMARIZATION_KEY, text, percent);
        Call<EnglishSimplifyModel> call = summarizationService.getSummary(SUMMARIZATION_KEY, text);
        try {
            retrofit2.Response<EnglishSimplifyModel> response = call.execute();
            EnglishSimplifyModel simplifyModel = response.body();
            summary = simplifyModel.getText().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text",text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.get("application/json"),jsonObject.toString());
        Request request = new Request.Builder()
                .url("http://api.cortical.io/rest/text/keywords?retina_name=en_associative")
                .header("api-key", "8f3ea4b0-443a-11e9-8f72-af685da1b20e")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String tempStr = response.body().string();
            keyWords = new Gson().fromJson(tempStr,new TypeToken<ArrayList<String>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Spannable simplifying;
        if (!summary.equals("")){
            simplifying = getMarkup(summary,keyWords);
        }else{
            simplifying = getMarkup(text, keyWords);
        }

        return simplifying;
    }

    private Spannable getRussianSimplifying(String text){
        String summary = "Failure to simplify text";
        ArrayList<String> keyWords = new ArrayList<>();

        int percent = percentCount(text);
        String text64 = android.util.Base64.encodeToString(text.getBytes(), android.util.Base64.URL_SAFE).replaceAll("\\n", "");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("text",text64);
            //jsonObject.put("percent", percent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=UTF-8"), jsonObject.toString());
        Call<SimplifyModel> call = analysisService.getSummary(requestBody);
        try {
            retrofit2.Response<SimplifyModel> response = call.execute();
            SimplifyModel simplifyModel = response.body();
            summary = new String(android.util.Base64.decode(simplifyModel.getSummary(),android.util.Base64.URL_SAFE));
            for (int i = 0; i < simplifyModel.getKeyWords().length; i++){
                keyWords.add(new String(android.util.Base64.decode(simplifyModel.getKeyWords()[i],android.util.Base64.URL_SAFE)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Spannable simplifying;
        if (!summary.equals("")){
            simplifying = getMarkup(summary,keyWords);
        }else{
            simplifying = getMarkup(text, keyWords);
        }

        return simplifying;
    }

    private Spannable getMarkup(String article, List<String> keyWords){
        Spannable simplifying = new SpannableString(Html.fromHtml(article));
        if(keyWords.size() > 0){
            for (int i = 0; i < keyWords.size(); i++) {
                String curKeyWord = keyWords.get(i);
                if (simplifying.toString().toLowerCase().indexOf(curKeyWord) > -1) {
                    simplifying.setSpan(new ForegroundColorSpan(Color.RED),
                            simplifying.toString().toLowerCase().indexOf(curKeyWord),
                            simplifying.toString().toLowerCase().indexOf(curKeyWord) + curKeyWord.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return simplifying;
    }

    private int percentCount(String text){
        int per;
        if (text.length() > 6000) {
            per = 20;
        } else if (text.length() < 1000) {
            per = 80;
        } else {
            per = 80 - (text.length() / 100);
        }
        return per;
    }

    @Override
    protected Spannable doInBackground(String...strings) {
        Translator translator = new Translator(selectedLanguage,textLang);
        String language = translator.getLanguage(strings[0]);
        String text;
        //проверка на совпадение языка текста
        if(language.equals(selectedLanguage)){
            text = strings[0];
        }else{
            text = translator.translate(strings[0], language);
        }

        Spannable simplifying;
        if (selectedLanguage.equals("ru")){
            simplifying = getRussianSimplifying(text);
        }else{
            simplifying = getEnglishSimplifying(text);
        }

        return simplifying;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        getTextListener.onTextProcessStarted();
    }

    @Override
    protected void onPostExecute(Spannable s) {
        super.onPostExecute(s);

        getTextListener.onSimplifyComplete(s);
    }
}
