package com.Goravsky.touristhelper.ui;

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

import com.Goravsky.touristhelper.QrTypeDecoder;
import com.Goravsky.touristhelper.data.QrHistory;
import com.Goravsky.touristhelper.data.QrRecord;
import com.Goravsky.touristhelper.R;

import java.util.ArrayList;


public class HistoryActivity extends Activity {

    private LinearLayout resultLayout;
    private TableLayout tableLayout;
    private TextView defaultText;
    private ImageView[] imageViews;
    private TextView[] dateViews;
    private TextView[] contentViews;
    private QrHistory history;
    private Button getTextButton;
    private Button specialButton;
    private TextView resultView;
    private Intent specialIntent;
    private String pickedContent;
    private String qrType;

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

        resultLayout = findViewById(R.id.history_result_layout);
        resultLayout.bringToFront();
        tableLayout = findViewById(R.id.history_table_layout);
        defaultText = findViewById(R.id.default_text);
        getTextButton = findViewById(R.id.history_get_result_button);
        specialButton = findViewById(R.id.history_open_in_browser_button);
        resultView = findViewById(R.id.history_result_view);

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

        for(int i = 0; i < 11; i++){
            imageViews[i].setVisibility(View.INVISIBLE);
            dateViews[i].setVisibility(View.INVISIBLE);
            contentViews[i].setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < 11; i++){
            contentViews[i].setOnClickListener(contentClickListener);
        }

        getTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle getTextBundle = new Bundle();
                getTextBundle.putString("qrType", qrType);
                getTextBundle.putString("barcode", pickedContent);

                Intent getTextIntent = new Intent(HistoryActivity.this, DataActivity.class);
                getTextIntent.putExtra("content", getTextBundle);
                startActivity(getTextIntent);
            }
        });

        specialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(specialIntent);
            }
        });
    }

    private void initData(){
        ArrayList<QrRecord> records = history.getRecords();

        int counter;
        if(records.size() > 11){
            counter = 11;
        }else{
            counter = records.size();
        }

        for(int i = 0; i < counter; i++){
            defaultText.setVisibility(View.INVISIBLE);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(resultLayout.getVisibility() == View.VISIBLE){
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
            pickedContent = clickedView.getText().toString();
            resultView.setText(pickedContent);
            Uri clickedContent = Uri.parse(clickedView.getText().toString());

            QrTypeDecoder typeDecoder = new QrTypeDecoder(clickedContent);
            typeDecoder.decode();
            qrType = typeDecoder.getType();
            specialButton.setText(typeDecoder.getButtonName());
            specialIntent = typeDecoder.getIntent();

            setResultRepresentation(true);
        }
    };

    private void setResultRepresentation(Boolean input){
        if(input == true){
            for(int i = 0; i < 11; i++){
                dateViews[i].setEnabled(false);
                contentViews[i].setEnabled(false);
            }

            resultLayout.setVisibility(View.VISIBLE);
        }else{
            for(int i = 0; i < 11; i++){
                dateViews[i].setEnabled(true);
                contentViews[i].setEnabled(true);
            }

            resultLayout.setVisibility(View.INVISIBLE);
        }
    }
}
