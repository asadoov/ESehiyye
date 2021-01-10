package az.gov.e_health.esehiyye.ui.main.Dtt;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.List;

import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Felsefe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Felsefe extends Fragment {

    ProgressDialog mWaitingDialog;

    public static Felsefe newInstance(String param1, String param2) {
        Felsefe fragment = new Felsefe();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_felsefe, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Fəlsəfə və ya tibb elmləri doktoru dərəcəsi almaq üçün dissertasiya işi");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
        final EditText nameEdit = view.findViewById(R.id.name);
        EditText yearEdit = view.findViewById(R.id.felsefeYear);

        yearEdit.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        // Listener


        String[] typeSpinnerList = new String[]{
                "Tibb üzrə fəlsəfə doktoru (48 kredit)", "Tibb elmləri doktoru (60 kredit)"
        };
        final Spinner typeSpinner = (Spinner) view.findViewById(R.id.creditSpinner);
        ArrayAdapter<String> distantSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, typeSpinnerList);
        distantSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(distantSpinnerAdapter);


        final Integer typeIndex = typeSpinner.getSelectedItemPosition();

        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final String nameEditText = nameEdit.getText().toString();

                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                if (!nameEditText.isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            mWaitingDialog = ProgressDialog.show(getContext(), "", "Serverlərimizə yerləşdirilir...", true);
                        }
                    });
                    final DbInsert insert = new DbInsert();
                    Integer typeID = 0;
                    switch (typeIndex) {
                        case 0:
                            typeID = 1701;
                            break;
                        case 1:
                            typeID = 1702;
                            break;
                    }


                    final Integer finalTypeID = typeID;
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final List<FeedbackStatusStruct> statusList = insert.dttFelsefeInsert(finalTypeID, nameEditText, Calendar.getInstance().get(Calendar.YEAR), view);
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        mWaitingDialog.dismiss();


                                        if (statusList.size() > 0 && statusList.get(0).RESULT.equals("1")) {

                                            alertDialog.setTitle("Bildiriş");
                                            alertDialog.setMessage("Tədbirlər təhsil planına əlavə olundu");
                                            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // TODO Auto-generated method stub
                                                            getFragmentManager().popBackStack();
                                                            backButton.setVisibility(View.GONE);

                                                        }
                                                    });


                                        } else {

                                            alertDialog.setTitle("Texniki xəta");
                                            alertDialog.setMessage("Biraz sonra yenidən cəht edin");
                                            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // TODO Auto-generated method stub
                                                            getFragmentManager().popBackStack();
                                                            backButton.setVisibility(View.GONE);

                                                        }
                                                    });
                                        }

                                        alertDialog.show();


                                    }
                                });
                            }
                        }
                    });
                    thread.start();

                } else {
                    alertDialog.setTitle("Bildiriş");
                    alertDialog.setMessage("Zəhmət olasa bütün boşluqları doldurun!");
                    alertDialog.show();
                }

            }
        });
        return view;
    }
}