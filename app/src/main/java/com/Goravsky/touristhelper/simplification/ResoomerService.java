package com.Goravsky.touristhelper.simplification;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ResoomerService {
    @FormUrlEncoded
    @POST("summarizer/")
    Call<EnglishSimplifyModel> getSummary (@Field("API_KEY") String key, @Field("text") String text);
}
