package com.Goravsky.touristhelper.simplification;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AnalysisService {
    @POST("summary/create")
    Call<SimplifyModel> getSummary (@Body RequestBody jsonText);
}
