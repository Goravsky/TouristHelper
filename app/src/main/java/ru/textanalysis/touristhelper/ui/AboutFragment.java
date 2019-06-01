package ru.textanalysis.touristhelper.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ru.textanalysis.touristhelper.R;

public class AboutFragment extends Fragment {

    ListView apiView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        apiView = view.findViewById(R.id.api_list_view);
        apiView.setScrollContainer(false);
        String[] apiList = getResources().getStringArray(R.array.api_list);

        apiView.setAdapter(new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,apiList));
        apiView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                String url = getResources().getStringArray(R.array.url_list)[position];
                urlIntent.setData(Uri.parse(url));
                startActivity(urlIntent);
            }
        });


        return view;
    }
}
