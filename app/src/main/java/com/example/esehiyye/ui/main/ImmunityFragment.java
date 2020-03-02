package com.example.esehiyye.ui.main;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.esehiyye.R;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImmunityFragment extends Fragment {


    public ImmunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View view = inflater.inflate(R.layout.fragment_immunity, container, false);
        //ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),R.id.immunityList,immunityItems);

        TextView toolbarTitle = view.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("İmmunoprofilatika üzrə məlumat");
        ImageButton backButton = view.findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ListView immunityList = view.findViewById(R.id.immunityList);

        String[] values = new String[] { "Doğumdan sonra 12 saat ərzində",
                "4-7-ci gün ",
                "2 aylıqda",
        "3 aylıqda",
        "6 aylıqda",
        "12 aylıqda",
                "18 aylıqda",
                "6 yaşında"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        immunityList.setAdapter(adapter);
      final   AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        immunityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                case 0:

                    alertDialog.setTitle("Doğumdan sonra 12 saat ərzində");
                    alertDialog.setMessage("Hepatit B xəstəliyinə qarşı peyvənd");

                    alertDialog.show();
                    break;
                    case 1:
                        alertDialog.setTitle("4-7-ci gün");
                        alertDialog.setMessage("Vərəm əleyhinə peyvənd\n\nPoliomelitə qarşı peyvənd");

                        alertDialog.show();
                        break;
                    case 2:
                        alertDialog.setTitle("2 aylıqda");
                        alertDialog.setMessage("Difteriya, göyöskürək, tetanus, hepatit B və B tipli hemofil infeksiyaya qarşı peyvənd\n\nPoliomielitə qarşı peyvənd\n\nPnevmokok infeksiyalarına qarşı peyvənd");

                        alertDialog.show();
                        break;
                    case 3:
                        alertDialog.setTitle("3 aylıqda");
                        alertDialog.setMessage("Difteriya, göyöskürək, tetanus, hepatit B və B tipli hemofil infeksiyaya qarşı peyvənd\n\nPoliomielitə qarşı peyvənd");

                        alertDialog.show();
                        break;

                    case 4:
                        alertDialog.setTitle("6 aylıqda");
                        alertDialog.setMessage("Pnevmokok infeksiyalarına qarşı peyvənd");

                        alertDialog.show();
                        break;
                    case 5:
                        alertDialog.setTitle("12 aylıqda");
                        alertDialog.setMessage("Qızılca, parotit və məxmərəyə qarşı peyvənd\n\nVitamin A");

                        alertDialog.show();
                        break;
                    case 6:
                        alertDialog.setTitle("18 aylıqda");
                        alertDialog.setMessage("Difteriya, göyöskürək, tetanus qarşı peyvənd\n\nPoliomelitə qarşı peyvənd\n\nVitamin A");

                        alertDialog.show();
                        break;
                    case 7:
                        alertDialog.setTitle("6 yaşında");
                        alertDialog.setMessage("Qızılca, parotit və məxmərəyə qarşı peyvənd\n\nDifteriya və tetanus-a qarşı peyvənd \n\nVitamin A");

                        alertDialog.show();
                        break;
                }
            }
        });
//        setListAdapter(adapter);
return view;
    }

}
