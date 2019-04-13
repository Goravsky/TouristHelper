package com.Goravsky.touristhelper;

import android.text.Spannable;

public interface GetTextListener {
    //void onSimplifyComplete(String result);
    void onSimplifyComplete(Spannable result);
    void onTextComplete(String result);
    void onTextProcessStarted();
}
