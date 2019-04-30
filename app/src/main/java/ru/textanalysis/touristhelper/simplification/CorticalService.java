package ru.textanalysis.touristhelper.simplification;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CorticalService {
    @POST("keywords?retina_name=en_associative")
    @Headers("api-key: 8f3ea4b0-443a-11e9-8f72-af685da1b20e")
    Call<EnglishSimplifyModel> getKeyWords (@Body RequestBody jsonText);
}
