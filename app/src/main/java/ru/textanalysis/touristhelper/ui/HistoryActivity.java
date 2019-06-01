package ru.textanalysis.touristhelper.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.textanalysis.touristhelper.QrTypeDecoder;
import ru.textanalysis.touristhelper.R;
import ru.textanalysis.touristhelper.data.QrHistory;
import ru.textanalysis.touristhelper.data.QrRecord;

public class HistoryActivity extends ListActivity {

    private QrHistory history;
    private TextView defaultText;
    private ResultFragment result;
    private HistoryArrayAdapter arrayAdapter;

    class HistoryArrayAdapter extends ArrayAdapter<QrRecord>{

        private  ArrayList<QrRecord> records;

        public HistoryArrayAdapter(Context context, ArrayList<QrRecord> rec){
            super(context,R.layout.list_element,rec);
            records = rec;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            QrRecord record = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element,null);
            }
            ((ImageView)convertView.findViewById(R.id.history_pic)).setImageResource(record.getTypeDrawable());
            ((TextView)convertView.findViewById(R.id.history_data)).setText(record.getDate());
            ((TextView)convertView.findViewById(R.id.history_content)).setText(record.getContent());

            return convertView;
        }

        public String getString(int position){
            return records.get(position).getContent();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        defaultText = findViewById(R.id.default_text);
        result = (ResultFragment) getFragmentManager().findFragmentById(R.id.history_result_fragment);

        history = new QrHistory(HistoryActivity.this, null, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        history.open();

        ArrayList<QrRecord> qrRecords = history.getRecords();
        if (qrRecords.size() > 0) {
            defaultText.setVisibility(View.INVISIBLE);
            arrayAdapter = new HistoryArrayAdapter(this,qrRecords);
            setListAdapter(arrayAdapter);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String pickedContent = arrayAdapter.getString(position);
        Uri clickedContent = Uri.parse(pickedContent);
        QrTypeDecoder typeDecoder = new QrTypeDecoder(clickedContent);
        typeDecoder.decode();

        result.setDecoder(typeDecoder);
        result.setVisability(true);
        getListView().setEnabled(false);
        getListView().setForeground(getDrawable(R.color.cast_expanded_controller_progress_text_color));
    }

    @Override
    protected void onStop() {
        super.onStop();
        history.close();
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (result.isActive()) {
            result.setVisability(false);
            getListView().setEnabled(true);
            getListView().setForeground(getDrawable(R.color.cast_expanded_controller_ad_container_white_stripe_color));
        } else {
            finish();
        }
    }
}

