package com.Goravsky.touristhelper.translation;

import com.Goravsky.touristhelper.translation.DetectLanguageModel;
import com.Goravsky.touristhelper.translation.TranslateModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface YandexService {
    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<TranslateModel> translate(@Field("key") String key, @Field("text") String text, @Field("lang") String lang);

    @FormUrlEncoded
    @POST ("api/v1.5/tr.json/detect")
    Call<DetectLanguageModel> detectLanguage (@Field("key") String key, @Field("text") String text);
}
