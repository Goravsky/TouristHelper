package ru.textanalysis.touristhelper;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

public class QrTypeDecoder {

    private Uri uri;
    private String type;
    private Intent intent;
    private String buttonName;

    public QrTypeDecoder(Uri inputUri){
        uri = inputUri;
        type = "text";

        intent = new Intent(Intent.ACTION_WEB_SEARCH);               //Интент по умолчанию, на случай если нет Схемы URI
        intent.putExtra(SearchManager.QUERY, uri.toString());
        buttonName = "Search in the web";
    }

    public void decode() {
        if (uri.getScheme() != null) {
            switch (uri.getScheme().toLowerCase()) {
                case "http":
                    type = "link";
                    //buttonName = Resources.getSystem().getString(R.string.link_button);
                    intent.setAction(Intent.ACTION_VIEW);
                    break;
                case "https":
                    type = "link";
                    //buttonName = Resources.getSystem().getString(R.string.link_button);
                    intent.setAction(Intent.ACTION_VIEW);
                    break;
                case "geo":
                    type = "geo";
                    //buttonName = Resources.getSystem().getString(R.string.geo_button);
                    intent.setAction(Intent.ACTION_VIEW);
                    break;
            }
            intent.setData(uri.normalizeScheme());
        } else if (uri.toString().matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
            type = "tel";
            //buttonName = Resources.getSystem().getString(R.string.tel_button);
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData( Uri.fromParts(type,uri.normalizeScheme().toString(),null));
        }
    }

    public Intent getIntent() {
        return intent;
    }

    public String getButtonName() {
        return buttonName;
    }

    public String getType() {
        return type;
    }

    public String getContent(){
        return uri.toString();
    }
}
