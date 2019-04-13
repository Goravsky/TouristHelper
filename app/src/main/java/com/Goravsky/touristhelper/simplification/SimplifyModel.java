package com.Goravsky.touristhelper.simplification;

import com.google.gson.annotations.SerializedName;

public class SimplifyModel {
    @SerializedName("summary")
    private String mSummary;

    @SerializedName("keyWords")
    private String[] mKeyWords;

    public SimplifyModel(String text, String[] keyWords){
        mSummary = text;
        mKeyWords = keyWords;
    }

    public String getSummary() {
        return mSummary;
    }

    public String[] getKeyWords() {
        return mKeyWords;
    }
}
