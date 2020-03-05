package az.gov.e_health.esehiyye.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadNewsFragment extends Fragment {

    public ReadNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_news, container, false);
        TextView toolbarTitle = view.findViewById(R.id.toolbarTitle);

        ImageButton backButton = view.findViewById(R.id.backBtn);
        Button saveBtn = view.findViewById(R.id.saveChanges);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        TextView newsTitle = view.findViewById(R.id.newsTitle);
        TextView newsDescription = view.findViewById(R.id.newsDescription);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            toolbarTitle.setText(bundle.getString("newsTitle"));
            newsTitle.setText(bundle.getString("newsTitle"));
            newsDescription.setText(bundle.getString("newsDescription"));
            // handle your code here.
        }

        return view;
    }
}
