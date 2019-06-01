package ru.textanalysis.touristhelper.ui;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xml.sax.XMLReader;

import ru.textanalysis.touristhelper.R;

public class InstructionFragment extends Fragment {

    private TextView helpView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instruction, container, false);
        helpView = view.findViewById(R.id.help_view);

        Html.ImageGetter htmlImageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                int resId = getResources().getIdentifier(source, "drawable", getContext().getPackageName());
                Drawable ret = getResources().getDrawable(resId);
                ret.setBounds(0, 0, ret.getIntrinsicWidth(), ret.getIntrinsicHeight());
                return ret;
            }
        };
        Html.TagHandler htmlTagHandler = new Html.TagHandler() {
            @Override
            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

            }
        };

        Spanned text =  Html.fromHtml(getString(getResources().getIdentifier("instruction", "string", getContext().getPackageName())), htmlImageGetter, htmlTagHandler);
        helpView.setText(text);

        return view;
    }
}
