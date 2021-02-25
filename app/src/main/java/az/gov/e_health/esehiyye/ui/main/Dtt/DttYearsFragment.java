package az.gov.e_health.esehiyye.ui.main.Dtt;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import az.gov.e_health.esehiyye.Model.DTT.DttStruct;
import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;
import az.gov.e_health.esehiyye.R;

import static android.content.Context.MODE_PRIVATE;


public class DttYearsFragment extends Fragment {
    ProgressDialog mWaitingDialog;
    ArrayList<String> years = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    List<DttStruct> dttList = new ArrayList<>();
    List<DttStruct> sortedDttList = new ArrayList<>();
    DbSelect select = new DbSelect();
    List<UserStruct> usrList;
    ListView yearsList;
    DbInsert insert = new DbInsert();
    List<Map<String, String>> data = new ArrayList<Map<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dtt_years, container, false);

        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Şəxsi kabinet");
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

                dttList = select.getDttTimeline(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null), usrList.get(0).VESIQE_FIN, view);
                if (getActivity() != null) {

                    data.clear();

                    years.clear();

                    if (dttList != null && dttList.size() > 0) {
                        for (DttStruct item : dttList
                        ) {

                            if (!years.contains(item.YEAR)) {


                                years.add(item.YEAR);

                            }
                        }
                        Collections.sort(years);
                        Integer yearIndex = 0;

                        for (String item : years) {

                            int totalCredit = 0;
                            int plannedCredit = 0;
                            for (DttStruct dtt : dttList) {
                                if (item.equals(dtt.YEAR) && !dtt.KREDIT.equals("")) {
                                    if (dtt.TESTIQLENIB.equals("1")) {
                                        totalCredit += Integer.parseInt(dtt.KREDIT);
                                    } else {

                                        plannedCredit += Integer.parseInt(dtt.KREDIT);
                                    }
                                }
                            }
                            Map<String, String> datum = new HashMap<String, String>(2);
                            datum.put("title", years.get(yearIndex));

                            datum.put("description", "Toplanmış kredit sayı: " + totalCredit + "\nPlanlaşdırılmış kredit sayı:" + plannedCredit + "");


                            data.add(datum);

                            //  years.set(yearIndex, years.get(yearIndex) + );
                            yearIndex++;
                        }


                        //Collections.reverse(years);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
//                                adapter = new ArrayAdapter<String>(getContext(),
//                                        android.R.layout.simple_list_item_1,
//                                        years);
//                                yearsList.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
                                final SimpleAdapter adapter = new SimpleAdapter(getContext(), data,
                                        android.R.layout.simple_list_item_2,
                                        new String[]{"title", "description"},
                                        new int[]{android.R.id.text1,
                                                android.R.id.text2}) {
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                       // text1.setTextColor(Color.RED);
                                        text1.setTypeface(null, Typeface.BOLD);
                                        return view;
                                    }

                                    ;

                                };
                                yearsList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        });

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


                    }


                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mWaitingDialog.dismiss();
                    }
                });
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
        view.findViewById(R.id.sendDtt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                //alertDialog.setTitle("");
                alertDialog.setMessage(Calendar.getInstance().get(Calendar.YEAR) + " -ci il üçün həkimin fərdi peşəkar inkişaf planı baş həkimə təstiqə gondərilsin?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Bəli",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                final Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Handler(Looper.getMainLooper()).post(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mWaitingDialog.show();
                                                    }
                                                });

                                        final List<FeedbackStatusStruct> statusList = insert.dttSend(view);

                                        if (getActivity() != null) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                public void run() {

                                                    mWaitingDialog.dismiss();
                                                    final AlertDialog resultDialog = new AlertDialog.Builder(view.getContext()).create();
                                                    if (statusList.size() > 0 && statusList.get(0).RESULT.equals("1")) {

                                                        //   alertDialog.setTitle("Bildiriş");
                                                        resultDialog.setMessage("Göndərildi!");
                                                        resultDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                                                                new DialogInterface.OnClickListener() {

                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        // TODO Auto-generated method stub
//                                                                        getFragmentManager().popBackStack();
//                                                                        backButton.setVisibility(View.GONE);

                                                                    }
                                                                });
                                                        resultDialog.show();

                                                    } else {

                                                        resultDialog.setTitle("Texniki xəta");
                                                        resultDialog.setMessage("Biraz sonra yenidən cəht edin");
                                                        resultDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                                                                new DialogInterface.OnClickListener() {

                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        // TODO Auto-generated method stub
                                                                        getFragmentManager().popBackStack();
                                                                        backButton.setVisibility(View.GONE);

                                                                    }
                                                                });
                                                        resultDialog.show();
                                                    }


                                                }
                                            });
                                        }
                                    }
                                });
                                thread.start();

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Xeyr",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                alertDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                yearsList.setAdapter(adapter);
//
//                adapter.notifyDataSetChanged();
//            }
//        });
    }
}