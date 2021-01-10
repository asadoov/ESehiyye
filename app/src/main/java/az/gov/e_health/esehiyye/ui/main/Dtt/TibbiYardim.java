package az.gov.e_health.esehiyye.ui.main.Dtt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TibbiYardim#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TibbiYardim extends Fragment {
    ProgressDialog mWaitingDialog;

    public static TibbiYardim newInstance(String param1, String param2) {
        TibbiYardim fragment = new TibbiYardim();
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
        View view = inflater.inflate(R.layout.fragment_tibbi_yardim, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Yeni tədbir");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
        final Button patentDateButton = (Button) view.findViewById(R.id.patentDate);

        final EditText tesvirEdit = view.findViewById(R.id.tesvir);
        final DatePickerDialog.OnDateSetListener patentDate = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = String.valueOf(selectedMonth + 1);
                String day1 = String.valueOf(selectedDay);

                patentDateButton.setText(day1 + "/" + month1 + "/" + year1);

            }
        };

        patentDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

// Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        patentDate,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Patentin gözlənilən alınma tarixi");
                datePicker.show();
            }
        });


        // Listener


        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final String patentDate = patentDateButton.getText().toString();
                final String tesvirText = tesvirEdit.getText().toString();

                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                if (!patentDate.equals("Patentin gözlənilən alınma tarixi") && !tesvirText.isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            mWaitingDialog = ProgressDialog.show(getContext(), "", "Serverlərimizə yerləşdirilir...", true);
                        }
                    });
                    final DbInsert insert = new DbInsert();


                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final List<FeedbackStatusStruct> statusList = insert.tibbiyardimInsert(tesvirText, patentDate, Calendar.getInstance().get(Calendar.YEAR), view);
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        mWaitingDialog.dismiss();


                                        if (statusList.size() > 0 && statusList.get(0).RESULT.equals("1")) {

                                           // alertDialog.setTitle("Bildiriş");
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


                                        } else  {

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