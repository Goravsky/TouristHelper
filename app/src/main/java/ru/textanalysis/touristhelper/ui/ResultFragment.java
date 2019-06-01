package ru.textanalysis.touristhelper.ui;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.textanalysis.touristhelper.QrTypeDecoder;
import ru.textanalysis.touristhelper.R;

/*TODO
  Дописать и заменить этим фрагментом контейнеры в HistoryActivity и MainActivity
 */
public class ResultFragment extends Fragment {

    private LinearLayout resultLayout;
    private TextView qrContentView;
    private Button specialButton;
    private Button getTextButton;
    private QrTypeDecoder typeDecoder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_result, container, false);

        resultLayout = view.findViewById(R.id.result_fragment_layout);
        qrContentView = view.findViewById(R.id.qr_content);
        specialButton = view.findViewById(R.id.special_button);
        getTextButton = view.findViewById(R.id.get_text_button);

        setListeners();

        return view;
    }

    public void setVisability(boolean inParam){
        if (inParam == true) {
            resultLayout.setVisibility(View.VISIBLE);
            resultLayout.bringToFront();
        }else {
            resultLayout.setVisibility(View.INVISIBLE);
        }
    }

    public boolean isActive(){
        if (resultLayout.getVisibility() == View.VISIBLE){
            return true;
        }else{
            return false;
        }
    }

    public void setDecoder(QrTypeDecoder decoder){
        typeDecoder = decoder;

        qrContentView.setText(typeDecoder.getContent());
        switch (typeDecoder.getType()){
            case "link":
                specialButton.setText(R.string.link_button);
                break;
            case "geo":
                specialButton.setText(R.string.geo_button);
                break;
            case "tel":
                specialButton.setText(R.string.tel_button);
                break;
        }
    }

    private void setListeners(){
        getTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle getTextBundle = new Bundle();
                getTextBundle.putString("qrType", typeDecoder.getType());
                getTextBundle.putString("barcode", typeDecoder.getContent());

                Intent getTextIntent = new Intent(getActivity(), DataActivity.class);
                getTextIntent.putExtra("content", getTextBundle);
                startActivity(getTextIntent);
            }
        });
        specialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(typeDecoder.getIntent());
            }
        });
    }
}
