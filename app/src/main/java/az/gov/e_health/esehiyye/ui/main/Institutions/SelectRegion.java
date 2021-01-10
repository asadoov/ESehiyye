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

import java.util.ArrayList;
import java.util.List;

import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.Institutions.InstStruct;
import az.gov.e_health.esehiyye.Model.Institutions.InstTypeStruct;
import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectRegion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectRegion extends Fragment {

    DbSelect select = new DbSelect();
    List<InstTypeStruct> instTypes = new ArrayList<>();
    List<InstStruct> instList = new ArrayList<>();
    ProgressDialog mWaitingDialog;
    long selectedInstID;
    long selectedRegion;
    String[] regionID_array, region_array;
    LinearLayout regionLayout;
    TextView notFoundLabel;
    FragmentManager fragmentManager;
    Gson gson;

    public SelectRegion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectRegion.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectRegion newInstance(String param1, String param2) {
        SelectRegion fragment = new SelectRegion();
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
        final View view = inflater.inflate(R.layout.fragment_select_region, container, false);
        //  final Spinner regionSpinner = view.findViewById(R.id.regionSpinner);
        // final Spinner typeSpinner = view.findViewById(R.id.typeSpinner);
        //   final Spinner instSpinner = view.findViewById(R.id.instSpinner);
        final ListView regionListView = view.findViewById(R.id.regionList);
        regionID_array = getContext().getResources().getStringArray(R.array.ID_REGIONS);
        region_array = getContext().getResources().getStringArray(R.array.spinnerItems);
        regionLayout = view.findViewById(R.id.regionListLayout);
        notFoundLabel = view.findViewById(R.id.notFoundLabel);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1, region_array);

        regionListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        regionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        selectedRegion = Long.parseLong(regionID_array[position]);
                        instTypes = select.GetInstTypeList(selectedRegion, view);

                        if (instTypes.size() > 0) {


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

        final SearchView searchView = view.findViewById(R.id.regionSearch);
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
                        for (String item : region_array) {
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
                                                regionListView.setVisibility(View.VISIBLE);
                                                notFoundLabel.setVisibility(View.GONE);
                                                //  Log.d("UI thread", "I am the UI thread");
                                            }
                                        });
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                        (getContext(), android.R.layout.simple_list_item_1, temp);

                                regionListView.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                regionListView.setAdapter(arrayAdapter);

                                                regionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {


                                                        Thread thread = new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                final List<String> list = new ArrayList<>();


                                                                getActivity().runOnUiThread(new Runnable() {
                                                                    public void run() {
                                                                        mWaitingDialog.show();
                                                                    }
                                                                });
                                                                selectedRegion = Long.parseLong(regionID_array[position]);
                                                                instTypes = select.GetInstTypeList(selectedRegion, view);

                                                                if (instTypes.size() > 0) {


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
                                                regionListView.setVisibility(View.GONE);
                                                notFoundLabel.setVisibility(View.VISIBLE);
                                                //  Log.d("UI thread", "I am the UI thread");
                                            }
                                        });
                            }
                        }


                        return true;
                    }
                });

//        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position, long id) {
//                // your code here
//                //  int tagValue = Integer.parseInt(selectedItemView.getTag().toString());
//
//
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final List<String> list = new ArrayList<>();
//                        if (position > 0) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                public void run() {
//                                    mWaitingDialog.show();
//                                }
//                            });
//                            selectedRegion = Long.parseLong(array_name[position]);
//                            instTypes = select.GetInstTypeList(selectedRegion, view);
//                            list.clear();
//                            list.add("Seçin..");
//                            if (instTypes.size() > 0) {
//
//
//                                for (InstTypeStruct item : instTypes
//                                ) {
//                                    list.add(item.Name);
//                                }
//
//
//                            }
//
//                        } else {
//                            selectedRegion = 0;
//                            list.clear();
//                            list.add("Seçin..");
//                        }
//                        getActivity().runOnUiThread(new Runnable() {
//                            public void run() {
//
//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
//                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                typeSpinner.setAdapter(adapter);
//                                mWaitingDialog.dismiss();
//                            }
//                        });
//                    }
//                });
//                thread.start();
//
//                //  Toast.makeText(getContext(), String.valueOf(array_name[position]), Toast.LENGTH_SHORT).show();
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position, long id) {
//                // your code here
//                //  int tagValue = Integer.parseInt(selectedItemView.getTag().toString());
//
//
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        final List<String> list = new ArrayList<>();
//                        if (position > 0 && selectedRegion > 0) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                public void run() {
//                                    mWaitingDialog.show();
//                                }
//                            });
//
//                            instList = select.GetInstList(selectedRegion, Long.parseLong(instTypes.get(position - 1).Id), view);
//                            list.clear();
//                            list.add("Seçin..");
//                            if (instList.size() > 0) {
//
//
//                                for (InstStruct item : instList
//                                ) {
//                                    list.add(item.Name);
//                                }
//
//
//                            }
//
//                        } else {
//                            list.clear();
//                            list.add("Seçin..");
//                        }
//                        getActivity().runOnUiThread(new Runnable() {
//                            public void run() {
//
//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
//                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                instSpinner.setAdapter(adapter);
//                                mWaitingDialog.dismiss();
//                            }
//                        });
//                    }
//                });
//                thread.start();
//
//                //  Toast.makeText(getContext(), String.valueOf(array_name[position]), Toast.LENGTH_SHORT).show();
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//        instSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position, long id) {
//                // your code here
//                //  int tagValue = Integer.parseInt(selectedItemView.getTag().toString());
//
//
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        final List<String> list = new ArrayList<>();
//                        if (position > 0) {
//
//                            selectedInstID = instList.get(position - 1).Id;
//
//
//                        } else {
//
//                        }
////                        getActivity().runOnUiThread(new Runnable() {
////                            public void run() {
////
////                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
////                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                                instSpinner.setAdapter(adapter);
////                                mWaitingDialog.dismiss();
////                            }
////                        });
//                    }
//                });
//                thread.start();
//
//                //  Toast.makeText(getContext(), String.valueOf(array_name[position]), Toast.LENGTH_SHORT).show();
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//        view.findViewById(R.id.showResults).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (selectedInstID > 0) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            mWaitingDialog.show();
//                        }
//                    });
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            InstStruct inst = select.LoadParameters(selectedInstID, view);
//                            Gson gson = new Gson();
//                            String jsonInst = gson.toJson(inst);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("obj", jsonInst);
//                            InstFragment fragment2 = new InstFragment();
//                            fragment2.setArguments(bundle);
//                            FragmentManager fragmentManager = getFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container, fragment2);
//                            fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                            getActivity().runOnUiThread(new Runnable() {
//                                public void run() {
//                                    mWaitingDialog.dismiss();
//                                }
//                            });
//
//                        }
//                    });
//                    thread.start();
//
//
//                }
//            }
//        });
        return view;
    }
}