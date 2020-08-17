package az.gov.e_health.esehiyye.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DttAbroadNewEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DttAbroadNewEventsFragment extends Fragment {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dtt_abroad_new_events, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Yeni tədbirin əlavə edilməsi");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
        String[] distantSpinnerList = new String[] {
                "Bəli", "Xeyr"
        };
        Spinner distantSpinner = (Spinner) view.findViewById(R.id.distantSpinner);
        ArrayAdapter<String> distantSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, distantSpinnerList);
        distantSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distantSpinner.setAdapter(distantSpinnerAdapter);

        String[] typeSpinnerList = new String[] {
                "Məruzəçi", "Dinləyici"
        };
        Spinner typeSpinner = (Spinner) view.findViewById(R.id.typeSpinner);
        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, typeSpinnerList);
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);



        ArrayList<String> creditSpinnerList = new ArrayList<>();
for (int i=0;i<41;i++){

    creditSpinnerList.add(String.valueOf(i));
}
        Spinner creditSpinner = (Spinner) view.findViewById(R.id.creditSpinner);
        ArrayAdapter<String> creditSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, creditSpinnerList);
        creditSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creditSpinner.setAdapter(creditSpinnerAdapter);



        return  view;
    }
}