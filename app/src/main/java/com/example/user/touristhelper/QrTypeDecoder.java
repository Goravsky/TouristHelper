package com.example.user.touristhelper;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;

public class QrTypeDecoder {

    private Uri uri;
    private Intent intent;
    private String buttonName;

    public QrTypeDecoder(Uri inputUri){
        uri = inputUri;

        intent = new Intent(Intent.ACTION_WEB_SEARCH);               //Интент по умолчанию, на случай если нет Схемы URI
        intent.putExtra(SearchManager.QUERY, uri.toString());
        buttonName = "Search in browser";
    }

    public void decode() {
        if (uri.getScheme() != null) {
            switch (uri.getScheme().toLowerCase()) {
                case "http":
                    buttonName = "Open in browser";
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(uri.normalizeScheme());
                    break;
                case "https":
                    buttonName = "Open in browser";
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(uri.normalizeScheme());
                    break;
                case "geo":
                    buttonName = "Look in maps";
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(uri.normalizeScheme());
                    break;
                case "tel":
                    buttonName = "Call to number";
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(uri.normalizeScheme());
                    break;
            }
        }
    }

    public Intent getIntent() {
        return intent;
    }

    public String getButtonName() {
        return buttonName;
    }
}
