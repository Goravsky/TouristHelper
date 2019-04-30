package ru.textanalysis.touristhelper.translation;

import com.google.gson.annotations.SerializedName;

public class DetectLanguageModel {

    @SerializedName("code")
    private int mCode;

    @SerializedName("lang")
    private String mLang;

    public DetectLanguageModel(int code, String lang){
        mCode = code;
        mLang = lang;
    }

    public int getResponseCode() {
        return mCode;
    }

    public String getLang() {
        return mLang;
    }
}
