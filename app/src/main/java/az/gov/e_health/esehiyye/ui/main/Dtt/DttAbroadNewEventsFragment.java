package az.gov.e_health.esehiyye.ui.main.Dtt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.R;


public class DttAbroadNewEventsFragment extends Fragment {
    ProgressDialog mWaitingDialog;
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
        toolbarTitle.setText("Xaricdə keçirilən DTT üzrə konqres, konfrans, simpozium, seminar, treninq və s. tədris və elmi tədbirlər");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
        final Button startDateButton = (Button) view.findViewById(R.id.startDate);
        final Button endDateButton = (Button) view.findViewById(R.id.endDate);
        final EditText tesvirEdit = view.findViewById(R.id.tesvir);
        final EditText teshkilEdit = view.findViewById(R.id.teshkil);
        final DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = String.valueOf(selectedMonth + 1);
                String day1 = String.valueOf(selectedDay);

                startDateButton.setText(day1 + "/" + month1 + "/" + year1);

            }
        };
        final DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = String.valueOf(selectedMonth + 1);
                String day1 = String.valueOf(selectedDay);

                endDateButton.setText(day1 + "/" + month1 + "/" + year1);

            }
        };

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

// Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        endDatePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Bitmə tarixi");
                datePicker.show();
            }
        });
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

// Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        startDatePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Başlama tarixi");
                datePicker.show();
            }
        });


        // Listener


        String[] distantSpinnerList = new String[]{
                "Bəli", "Xeyr"
        };
        final Spinner distantSpinner = (Spinner) view.findViewById(R.id.distantSpinner);
        ArrayAdapter<String> distantSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, distantSpinnerList);
        distantSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distantSpinner.setAdapter(distantSpinnerAdapter);


        String[] typeSpinnerList = new String[]{
                "Məruzəçi", "Dinləyici"
        };
        Spinner typeSpinner = (Spinner) view.findViewById(R.id.typeSpinner);
        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, typeSpinnerList);
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);


        ArrayList<String> creditSpinnerList = new ArrayList<>();
        for (int i = 0; i < 41; i++) {

            creditSpinnerList.add(String.valueOf(i));
        }
        final Spinner creditSpinner = (Spinner) view.findViewById(R.id.creditSpinner);
        ArrayAdapter<String> creditSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, creditSpinnerList);
        creditSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creditSpinner.setAdapter(creditSpinnerAdapter);
        final Integer distantIndex = distantSpinner.getSelectedItemPosition();
        final Integer typeIndex = typeSpinner.getSelectedItemPosition();
        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final String startDateText = startDateButton.getText().toString();
                final String endDateText = startDateButton.getText().toString();
                final String tesvirText = tesvirEdit.getText().toString();
                final String teshkiText = teshkilEdit.getText().toString();
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                if (!startDateText.equals("Başlama tarixi") && !endDateText.equals("Bitmə tarixi") && !tesvirText.isEmpty() && !teshkiText.isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            mWaitingDialog = ProgressDialog.show(getContext(), "", "Tədbiriniz serverlərimizə yerləşdirilir...", true);
                        }
                    });
                    final DbInsert insert = new DbInsert();


                    Integer distantID = 0;
                    switch (typeIndex) {
                        case 0:
                            distantID = 1947;
                            break;
                        case 1:
                            distantID = 1948;
                            break;
                    }


                    Integer typeID = 0;
                    switch (typeIndex) {
                        case 0:
                            typeID = 1807;
                            break;
                        case 1:
                            typeID = 1950;
                            break;
                    }

                    final Integer finalDistantID = distantID;
                    final Integer finalTypeID = typeID;
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final List<FeedbackStatusStruct> statusList = insert.dttXaricInsert(tesvirText, teshkiText, startDateText, endDateText, finalDistantID, finalTypeID, creditSpinner.getSelectedItemPosition(), Calendar.getInstance().get(Calendar.YEAR), view);
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        mWaitingDialog.dismiss();


                                        if (statusList.size() > 0 && statusList.get(0).RESULT.equals("1")) {

                                            // alertDialog.setTitle("Bildiriş");
                                            alertDialog.setMessage("Tədbir əlavə olundu");


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