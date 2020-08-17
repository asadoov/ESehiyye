package az.gov.e_health.esehiyye.ui.main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.DttStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;
import az.gov.e_health.esehiyye.R;

import static android.content.Context.MODE_PRIVATE;


public class DttYearsFragment extends Fragment {
    ProgressDialog mWaitingDialog;
    ArrayList<String> years =new ArrayList<String>();
    ArrayAdapter<String> adapter;
    List<DttStruct> dttList = new ArrayList<>();
    List<DttStruct> sortedDttList = new ArrayList<>();
    DbSelect select = new DbSelect();
    List<UserStruct> usrList;
     ListView yearsList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter=new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                years);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dtt_years, container, false);

        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Davamlı tibbi təhsil");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
      yearsList = view.findViewById(R.id.yearsList);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir. Gözləyin...", true);
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

                dttList = select.getDttTimeline(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null),usrList.get(0).VESIQE_FIN, view);
                if(getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            mWaitingDialog.dismiss();
                            years.clear();

                            if (dttList != null) {
                                for (DttStruct item : dttList
                                ) {
                                    if (!years.contains(item.YEAR)) {


                                        years.add(item.YEAR);
                                        adapter.notifyDataSetChanged();
                                    }
                                }


                                Collections.reverse(years);
                                yearsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                        String selectedYear = years.get(position).toString().trim();//first method
                                        sortedDttList.clear();

                                        for (DttStruct item : dttList
                                        ) {
                                            if (item.YEAR.equals(selectedYear)) {
                                                sortedDttList.add(item);

                                            }

                                        }
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("dttList", (Serializable) sortedDttList);

                                        DTTFragment fragment = new DTTFragment();
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.container, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }
                                });


                            } else {
                                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                alertDialog.setTitle("Xəta");
                                alertDialog.setMessage("Biraz sonra yenidən cəht edin");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yenilə",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                FragmentTransaction ft = getFragmentManager().beginTransaction();

                                                ft.detach(DttYearsFragment.this).attach(DttYearsFragment.this).commit();
                                            }
                                        });
alertDialog.show();
                            }

                        }
                    });
                }

            }
        });
        newsThread.start();

        view.findViewById(R.id.showDttMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DttMenu fragment = new DttMenu();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        yearsList.setAdapter(adapter);

      adapter.notifyDataSetChanged();
    }
}