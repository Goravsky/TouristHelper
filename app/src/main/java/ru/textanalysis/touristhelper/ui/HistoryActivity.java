package ru.textanalysis.touristhelper.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import ru.textanalysis.touristhelper.QrTypeDecoder;
import ru.textanalysis.touristhelper.data.QrHistory;
import ru.textanalysis.touristhelper.data.QrRecord;
import ru.textanalysis.touristhelper.R;

import java.util.ArrayList;


public class HistoryActivity extends Activity {

    private TextView defaultText;
    private ImageView[] imageViews;
    private TextView[] dateViews;
    private TextView[] contentViews;
    private QrHistory history;
    private ResultFragment result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        history.open();
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        history.close();
    }

    private void initComponents(){
        history = new QrHistory(HistoryActivity.this, null, 1);

        defaultText = findViewById(R.id.default_text);
        result = (ResultFragment) getFragmentManager().findFragmentById(R.id.history_result_fragment);

        imageViews = new ImageView[11];
        dateViews = new TextView[11];
        contentViews = new TextView[11];

        imageViews[0] = findViewById(R.id.image_view0);
        imageViews[1] = findViewById(R.id.image_view1);
        imageViews[2] = findViewById(R.id.image_view2);
        imageViews[3] = findViewById(R.id.image_view3);
        imageViews[4] = findViewById(R.id.image_view4);
        imageViews[5] = findViewById(R.id.image_view5);
        imageViews[6] = findViewById(R.id.image_view6);
        imageViews[7] = findViewById(R.id.image_view7);
        imageViews[8] = findViewById(R.id.image_view8);
        imageViews[9] = findViewById(R.id.image_view9);
        imageViews[10] = findViewById(R.id.image_view10);

        dateViews[0] = findViewById(R.id.date_field0);
        dateViews[1] = findViewById(R.id.date_field1);
        dateViews[2] = findViewById(R.id.date_field2);
        dateViews[3] = findViewById(R.id.date_field3);
        dateViews[4] = findViewById(R.id.date_field4);
        dateViews[5] = findViewById(R.id.date_field5);
        dateViews[6] = findViewById(R.id.date_field6);
        dateViews[7] = findViewById(R.id.date_field7);
        dateViews[8] = findViewById(R.id.date_field8);
        dateViews[9] = findViewById(R.id.date_field9);
        dateViews[10] = findViewById(R.id.date_field10);

        contentViews[0] = findViewById(R.id.content_field0);
        contentViews[1] = findViewById(R.id.content_field1);
        contentViews[2] = findViewById(R.id.content_field2);
        contentViews[3] = findViewById(R.id.content_field3);
        contentViews[4] = findViewById(R.id.content_field4);
        contentViews[5] = findViewById(R.id.content_field5);
        contentViews[6] = findViewById(R.id.content_field6);
        contentViews[7] = findViewById(R.id.content_field7);
        contentViews[8] = findViewById(R.id.content_field8);
        contentViews[9] = findViewById(R.id.content_field9);
        contentViews[10] = findViewById(R.id.content_field10);

        for (int i = 0; i < 11; i++){
            contentViews[i].setOnClickListener(contentClickListener);
        }
    }

    private void initData() {
        ArrayList<QrRecord> records = history.getRecords();

        if (records.size() > 0) {
            defaultText.setVisibility(View.INVISIBLE);

            int counter;
            if (records.size() > 11) {
                counter = 11;
            } else {
                counter = records.size();
            }

            for (int i = 0; i < counter; i++) {
                imageViews[i].setVisibility(View.VISIBLE);
                dateViews[i].setVisibility(View.VISIBLE);
                contentViews[i].setVisibility(View.VISIBLE);

                QrRecord record = records.get(i);
                imageViews[i].setImageResource(record.getTypeDrawable());
                dateViews[i].setText(record.getDate());
                contentViews[i].setText(record.getContent());
            }

            System.out.println("Выведено:" + counter);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(result.isActive()){
            setResultRepresentation(false);
        }else{
            finish();
        }
    }

    /*
    Обработчик нажатия элемента спииска
     */
    private View.OnClickListener contentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView clickedView = findViewById(v.getId());
            String pickedContent = clickedView.getText().toString();
            Uri clickedContent = Uri.parse(pickedContent);
            QrTypeDecoder typeDecoder = new QrTypeDecoder(clickedContent);
            typeDecoder.decode();

            result.setDecoder(typeDecoder);

            setResultRepresentation(true);
        }
    };


    private void setResultRepresentation(boolean inParam){
        if(inParam == true){
            for(int i = 0; i < 11; i++){
                dateViews[i].setEnabled(false);
                contentViews[i].setEnabled(false);
            }
            result.setVisability(true);
        }else{
            for(int i = 0; i < 11; i++){
                dateViews[i].setEnabled(true);
                contentViews[i].setEnabled(true);
            }
            result.setVisability(false);
        }
    }
}
