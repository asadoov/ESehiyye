package az.gov.e_health.esehiyye.ui.main.Dtt;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import az.gov.e_health.esehiyye.Model.Adapters.Dtt.DttNewEventsAdapter;
import az.gov.e_health.esehiyye.Model.DTT.DttNewEventStruct;
import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;
import az.gov.e_health.esehiyye.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DttNewEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DttNewEvents extends Fragment {
    ProgressDialog mWaitingDialog;
    ArrayList<String> years = new ArrayList<String>();
    DttNewEventsAdapter adapter;
    List<DttNewEventStruct> dttNewEventsList = new ArrayList<>();

    DbSelect select = new DbSelect();
    DbInsert insert = new DbInsert();
    List<UserStruct> usrList;
    ListView dttNewEventsListView;
    List<String> selectedEventsId = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DttNewEvents() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DttNewEvents.
     */
    // TODO: Rename and change types and number of parameters
    public static DttNewEvents newInstance(String param1, String param2) {
        DttNewEvents fragment = new DttNewEvents();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dtt_new_events, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("DTT üzrə konqres, konfrans, simpozium, seminar, treninq və s. tədris və elmi tədbirlər");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
        dttNewEventsListView = view.findViewById(R.id.dttNewEventList);

        final Thread newsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir. Gözləyin...", true);
                    }
                });
                SharedPreferences sharedPreferences
                        = getContext().getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);
                String jsonUserData = sharedPreferences.getString("userData", "");
                Gson gson = new GsonBuilder().setLenient().create();

                usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));

                dttNewEventsList = select.getDttNewEvents(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null), usrList.get(0).VESIQE_FIN, Calendar.getInstance().get(Calendar.YEAR), view);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                            years.clear();


                            if (dttNewEventsList.size() > 0) {
                                adapter = new DttNewEventsAdapter(getContext(), dttNewEventsList);
                                dttNewEventsListView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }


                            mWaitingDialog.dismiss();
                        }
                    });
                }

            }
        });
        newsThread.start();

        dttNewEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long l) {

                CheckBox newEventCheckBox = item.findViewById(R.id.newEventCheckBox);
                Button saveButton = (Button) view.findViewById(R.id.saveButton);
                if (dttNewEventsList.get(position).isChecked == true) {
                    dttNewEventsList.get(position).isChecked = false;

                    newEventCheckBox.setChecked(false);
                    selectedEventsId.remove(String.valueOf(dttNewEventsList.get(position).ID));

                } else {
                    dttNewEventsList.get(position).isChecked = true;
                    newEventCheckBox.setChecked(true);
                    selectedEventsId.add(String.valueOf(dttNewEventsList.get(position).ID));

                }
                if (selectedEventsId.size() > 0) {
                    saveButton.setVisibility(View.VISIBLE);
                    saveButton.setText(String.format("Əlavə et (%s)", selectedEventsId.size()));

                } else {
                    saveButton.setVisibility(View.GONE);
                }

                //dttNewEventsListView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                String eventsId = "";
                if (selectedEventsId.size() == 1) {
                    eventsId += String.format("%s;", selectedEventsId.get(0));
                } else {
                    for (String id : selectedEventsId
                    ) {
                        if (id.equals(selectedEventsId.get(selectedEventsId.size() - 1))) {
                            eventsId += String.format("%s;", id);
                        } else {
                            eventsId += String.format("%s,", id);
                        }


                    }

                }
                final String finalEventsId = eventsId;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mWaitingDialog = ProgressDialog.show(getContext(), "", "Serverlərimizə yerləşdirilir...", true);
                    }
                });
                final Thread newsThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SharedPreferences sharedPreferences
                                = getContext().getSharedPreferences("MySharedPref",
                                MODE_PRIVATE);
                        String jsonUserData = sharedPreferences.getString("userData", "");
                        Gson gson = new GsonBuilder().setLenient().create();

                        usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));

                        final List<FeedbackStatusStruct> statusList = insert.dttKonqresKonfransInsert(usrList.get(0).VESIQE_FIN, Calendar.getInstance().get(Calendar.YEAR), finalEventsId, view);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    mWaitingDialog.dismiss();
                                    years.clear();
                                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                    if (statusList.size() > 0 && statusList.get(0).RESULT.equals("1")) {

                                        //alertDialog.setTitle("Bildiriş");
                                        alertDialog.setMessage("Seçdiyiniz tədbirlər əlavə olundu");
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
                newsThread.start();
                //  Toast.makeText(getContext(), eventsId, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}