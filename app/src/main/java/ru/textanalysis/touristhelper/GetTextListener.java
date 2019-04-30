package ru.textanalysis.touristhelper;

import android.text.Spannable;

import java.util.ArrayList;

public interface GetTextListener {
    void onSimplifyComplete(Spannable result);
    void onTranslateComplete(String result);
    void onTextComplete(ArrayList<String> result);
    void onTextProcessStarted();
}
