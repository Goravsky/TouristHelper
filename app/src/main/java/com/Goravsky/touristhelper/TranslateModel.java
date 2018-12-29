package com.Goravsky.touristhelper;

import com.google.gson.annotations.SerializedName;

public class TranslateModel {
    @SerializedName("code")
    private int mCode;

    @SerializedName("lang")
    private String mLang;

    @SerializedName("text")
    private String[] mText;

    public TranslateModel(int code,String lang, String[] text) {
        mCode = code;
        mLang = lang;
        mText = text;
    }

    public int getResponseCode() {
        return mCode;
    }

    public String getLang() {
        return mLang;
    }

    public String[] getText() {
        return mText;
    }
}

