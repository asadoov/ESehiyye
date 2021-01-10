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

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ixtisas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ixtisas extends Fragment {

    ProgressDialog mWaitingDialog;

    public static Ixtisas newInstance(String param1, String param2) {
        Ixtisas fragment = new Ixtisas();
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
        View view = inflater.inflate(R.layout.fragment_ixtisas, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("İxtisas üzrə sənədlərin hazırlanması");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
        final Button docDate = (Button) view.findViewById(R.id.date);

        final EditText docName = view.findViewById(R.id.name);

        final DatePickerDialog.OnDateSetListener docDateListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = String.valueOf(selectedMonth + 1);
                String day1 = String.valueOf(selectedDay);

                docDate.setText(day1 + "/" + month1 + "/" + year1);

            }
        };

        docDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

// Create the DatePickerDialog instance
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        docDateListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Tarix");
                datePicker.show();
            }
        });


        // Listener

        String[] typeSpinnerList = new String[]{
                "Milli rəhbərlik", "Dərslik", "Tədris vəsaiti", "Metodik rəhbərlik", "Metodik tövsitə", "Monoqrafiya", "Təhsil proqramı"
        };
        Spinner typeSpinner = (Spinner) view.findViewById(R.id.typeSpinner);
        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, typeSpinnerList);
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);

        String[] authorSpinnerList = new String[]{
                "Birinci müəllif", "Həmmüəllif", "Fəslin müəllifi", "Elmi redaktor", "Rəyçi", "Digər"
        };
        final Spinner authorSpinner = (Spinner) view.findViewById(R.id.authorSpinner);
        ArrayAdapter<String> distantSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, authorSpinnerList);
        distantSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        authorSpinner.setAdapter(distantSpinnerAdapter);


        final Integer authorIndex = authorSpinner.getSelectedItemPosition();
        final Integer typeIndex = typeSpinner.getSelectedItemPosition();
        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final String docDateText = docDate.getText().toString();

                final String docNameText = docName.getText().toString();

                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                if (!docDateText.equals("Tarix") && !docNameText.isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            mWaitingDialog = ProgressDialog.show(getContext(), "", "Tədbiriniz serverlərimizə yerləşdirilir...", true);
                        }
                    });
                    final DbInsert insert = new DbInsert();
                    Integer type = 0, kredit = 0;

                    switch (typeIndex) {
                        case 0:
                            type = 1718;
                            kredit = 60;
                            break;
                        case 1:
                            type = 1719;
                            kredit = 24;
                            break;
                        case 2:
                            type = 1720;
                            kredit = 6;
                            break;
                        case 3:
                            type = 1721;
                            kredit = 12;
                            break;
                        case 4:
                            type = 1722;
                            kredit = 3;
                            break;
                        case 5:
                            type = 1723;
                            kredit = 24;
                            break;
                        case 6:
                            type = 1724;
                            kredit = 0;
                            break;

                    }

                    Integer author = 0;
                    switch (authorIndex) {
                        case 0:
                            author = 1725;
                            break;
                        case 1:
                            author = 1726;
                            break;
                        case 2:
                            author = 1727;
                            break;
                        case 3:
                            author = 1728;
                            break;
                        case 4:
                            author = 1729;
                            break;
                        case 5:
                            author = 1809;
                            break;


                    }
                    final Integer finalType = type;
                    final Integer finalKredit = kredit;
                    final Integer finalAuthor = author;
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final List<FeedbackStatusStruct> statusList = insert.ixtisasinsert(finalType, docNameText, finalAuthor, docDateText, finalKredit, Calendar.getInstance().get(Calendar.YEAR), view);
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        mWaitingDialog.dismiss();


                                        if (statusList.size() > 0 && statusList.get(0).RESULT.equals("1")) {

                                            //alertDialog.setTitle("Bildiriş");
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


                                        } else if (statusList.size() > 0 && statusList.get(0).RESULT.equals("0")) {

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