package az.gov.e_health.esehiyye.ui.main.Institutions;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.Institutions.InstStruct;
import az.gov.e_health.esehiyye.Model.Institutions.InstTypeStruct;
import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectType#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectType extends Fragment {

    Bundle bundle;
    DbSelect select = new DbSelect();
    List<InstTypeStruct> instTypes = new ArrayList<>();
    List<String> instTypesString = new ArrayList<>();
    List<InstStruct> instList = new ArrayList<>();
    ProgressDialog mWaitingDialog;
    long selectedRegion;
    long selectedInsType;
    LinearLayout instTypeLayout;
    TextView notFoundLabel;
    FragmentManager fragmentManager;
    Gson gson;
    List<InstTypeStruct> typeList = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectType.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectType newInstance(String param1, String param2) {
        SelectType fragment = new SelectType();
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
        View view = inflater.inflate(R.layout.fragment_select_type, container, false);
        bundle = this.getArguments();
        gson = new GsonBuilder().setLenient().create();
        selectedRegion = bundle.getLong("regionID");
        typeList = Arrays.asList(gson.fromJson(bundle.getString("TypeList"), InstTypeStruct[].class));

        // Inflate the layout for this fragment
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);

        fragmentManager = getFragmentManager();
        gson = new Gson();

        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Tibb müəssisələri barədə məlumat");
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
        mWaitingDialog = new ProgressDialog(getContext());
        mWaitingDialog.setMessage("Yüklənir..");

        //  final Spinner regionSpinner = view.findViewById(R.id.regionSpinner);
        // final Spinner typeSpinner = view.findViewById(R.id.typeSpinner);
        //   final Spinner instSpinner = view.findViewById(R.id.instSpinner);
        final ListView typeListView = view.findViewById(R.id.typeList);
        for (InstTypeStruct item : typeList
        ) {
            instTypesString.add(item.Name);
        }

        instTypeLayout = view.findViewById(R.id.typeListLayout);
        notFoundLabel = view.findViewById(R.id.notFoundLabel);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1, instTypesString);

        typeListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        typeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                mWaitingDialog.show();
                            }
                        });
                        selectedInsType = Long.parseLong(typeList.get(position).Id);
                        instList = select.GetInstList(selectedRegion, selectedInsType, view);

                        if (typeList.size() > 0) {


                            String json = gson.toJson(instList);
                            Bundle bundle = new Bundle();
                            bundle.putString("instList", json);

                            SelectInst fragment2 = new SelectInst();
                            fragment2.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {


                                mWaitingDialog.dismiss();
                            }
                        });
                    }
                });
                thread.start();
            }
        });

        final SearchView searchView = view.findViewById(R.id.typeSearch);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return false;
                    }


                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText) {

                        final List<String> temp = new ArrayList<>();

                        temp.clear();
                        for (String item : instTypesString) {
                            if (item.toLowerCase().contains(newText.toLowerCase())) {
                                temp.add(item);

                            }


                        }
                        if (temp != null) {
                            if (temp.size() > 0) {
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                typeListView.setVisibility(View.VISIBLE);
                                                notFoundLabel.setVisibility(View.GONE);
                                                //  Log.d("UI thread", "I am the UI thread");
                                            }
                                        });
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                        (getContext(), android.R.layout.simple_list_item_1, temp);

                                typeListView.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                typeListView.setAdapter(arrayAdapter);

                                                typeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {


                                                        Thread thread = new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {


                                                                getActivity().runOnUiThread(new Runnable() {
                                                                    public void run() {
                                                                        mWaitingDialog.show();
                                                                    }
                                                                });
                                                                selectedInsType = Long.parseLong(typeList.get(position).Id);
                                                                instList = select.GetInstList(selectedRegion, selectedInsType, view);

                                                                if (typeList.size() > 0) {

                                                                    String json = gson.toJson(instTypes);
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putString("TypeList", json);
                                                                    bundle.putLong("regionID", selectedRegion);
                                                                    SelectType fragment2 = new SelectType();
                                                                    fragment2.setArguments(bundle);
                                                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                                    fragmentTransaction.replace(R.id.container, fragment2);
                                                                    fragmentTransaction.addToBackStack(null);
                                                                    fragmentTransaction.commit();


                                                                }


                                                                getActivity().runOnUiThread(new Runnable() {
                                                                    public void run() {


                                                                        mWaitingDialog.dismiss();
                                                                    }
                                                                });
                                                            }
                                                        });
                                                        thread.start();

                                                    }
                                                });
                                            }
                                        });

                            } else {


                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                typeListView.setVisibility(View.GONE);
                                                notFoundLabel.setVisibility(View.VISIBLE);
                                                //  Log.d("UI thread", "I am the UI thread");
                                            }
                                        });
                            }
                        }


                        return true;
                    }
                });
        return view;
    }

}