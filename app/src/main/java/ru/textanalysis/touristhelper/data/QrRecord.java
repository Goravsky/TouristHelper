package ru.textanalysis.touristhelper.data;

import ru.textanalysis.touristhelper.R;

public class QrRecord {

    private String type;
    private String date;
    private String content;

   QrRecord(String t, String d, String c){
       type = t;
       date = d;
       content = c;
   }

    public int getTypeDrawable() {

        int resourseId = R.drawable.text;

        switch (type) {
            case "link":
                resourseId = R.drawable.link;
                break;
            case "geo":
                resourseId = R.drawable.geo;
                break;
            case "tel":
                resourseId = R.drawable.phone;
                break;
        }
        return resourseId;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
