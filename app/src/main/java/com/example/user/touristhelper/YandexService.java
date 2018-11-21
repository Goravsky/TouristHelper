package com.example.user.touristhelper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YandexService {
    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<TranslateModel> translate(@Field("key") String key, @Field("text") String text, @Field("lang") String lang);

    @FormUrlEncoded
    @POST ("api/v1.5/tr.json/detect")
    Call<DetectLanguageModel> detectLanguage (@Field("key") String key, @Field("text") String text);
}
