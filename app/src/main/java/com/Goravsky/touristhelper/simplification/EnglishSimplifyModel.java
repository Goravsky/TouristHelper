package com.Goravsky.touristhelper.simplification;

import com.google.gson.annotations.SerializedName;

public class EnglishSimplifyModel {
    public class Text{
        @SerializedName("size")
        private String size;

        @SerializedName("total_words")
        private String total_words;

        @SerializedName("content")
        private String content;

        public String getSize() {
            return size;
        }

        public String getTotal_words() {
            return total_words;
        }

        public String getContent() {
            return content;
        }
    }

    @SerializedName("ok")
    private String ok;

    @SerializedName("massage")
    private String message;

    @SerializedName("text")
    private Text text;

    @SerializedName("code")
    private String codeResponse;

    public String getOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }

    public Text getText() {
        return text;
    }

    public String getCodeResponse() {
        return codeResponse;
    }
}
