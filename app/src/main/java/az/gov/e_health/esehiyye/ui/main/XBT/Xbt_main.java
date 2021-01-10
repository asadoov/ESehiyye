package az.gov.e_health.esehiyye.ui.main.XBT;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.XbtStruct;
import az.gov.e_health.esehiyye.R;
import az.gov.e_health.esehiyye.ui.main.PdfViewer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Xbt_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Xbt_main extends Fragment {

    ProgressDialog mWaitingDialog;
    List<XbtStruct> xbtFirstList;
    ListView xbtListView;
    TextView notFoundLabel;
    DbSelect select = new DbSelect();
    List<Long> idHistory = new ArrayList<>();

    public Xbt_main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Xbt_main.
     */
    // TODO: Rename and change types and number of parameters
    public static Xbt_main newInstance(String param1, String param2) {
        Xbt_main fragment = new Xbt_main();
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
        final View view = inflater.inflate(R.layout.fragment_xbt_main, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Xəstəliklərin beynalxalq təsnifatı");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idHistory.size() > 1) {
                    getXbtList(idHistory.get(idHistory.size() - 2), view);
                    idHistory.remove(idHistory.size() - 1);
                }
                else if (idHistory.size()==0){
                    getFragmentManager().popBackStack();
                    backButton.setVisibility(View.GONE);
                }
                else {
                    getXbtFirstList(view);
                    idHistory.remove(idHistory.size() - 1);

                }


            }
        });
        xbtListView = view.findViewById(R.id.xbtFirstList);
        notFoundLabel = view.findViewById(R.id.notFoundLabel);
        getXbtFirstList(view);


        final SearchView searchView = view.findViewById(R.id.xbtSearch);
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

                        final List<XbtStruct> temp = new ArrayList<>();

                        temp.clear();
                        for (XbtStruct item : xbtFirstList) {
                            if (item.XBT_BOLME_IZAH.toLowerCase().contains(newText.toLowerCase())) {
                                temp.add(item);

                            }


                        }
                        if (temp != null) {
                            if (temp.size() > 0) {
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                xbtListView.setVisibility(View.VISIBLE);
                                                notFoundLabel.setVisibility(View.GONE);
                                                //  Log.d("UI thread", "I am the UI thread");
                                            }
                                        });
                                List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                                for (XbtStruct item : temp) {
                                    Map<String, String> datum = new HashMap<String, String>(2);
                                    datum.put("title", item.XBT_BOLME_IZAH);
                                    if (item.TYPE != "") {
                                        datum.put("description", item.XBT_BOLME + " (" + item.TYPE + ")");
                                    } else {
                                        datum.put("description", "(" + item.XBT_BOLME + ")");
                                    }


                                    data.add(datum);
                                }
                                final SimpleAdapter adapter = new SimpleAdapter(getContext(), data,
                                        android.R.layout.simple_list_item_2,
                                        new String[]{"title", "description"},
                                        new int[]{android.R.id.text1,
                                                android.R.id.text2});
                                adapter.notifyDataSetChanged();
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                xbtListView.setAdapter(adapter);

                                                xbtListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                        if (temp.get(position).CNT > 0 || temp.get(position).TYPE != "") {
                                                            idHistory.add(temp.get(position).IDN);
                                                            getXbtList(temp.get(position).IDN, view);
                                                        }

                                                    }
                                                });
                                            }
                                        });

                            } else {

                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                xbtListView.setVisibility(View.GONE);
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

    private void getXbtList(final long id, final View view) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir...", true);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {

//                catID = bundle.getInt("catID");


                xbtFirstList = select.GetXbtList(id, view);


                if (xbtFirstList != null) {
                    if (xbtFirstList.size() > 0) {

                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {
//                                        filesLayout.setVisibility(View.VISIBLE);
//                                        notFoundLabel.setVisibility(View.GONE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });

                        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                        for (XbtStruct item : xbtFirstList) {
                            Map<String, String> datum = new HashMap<String, String>(2);
                            datum.put("title", item.XBT_BOLME_IZAH);
                            if (item.TYPE != "") {
                                datum.put("description", item.XBT_BOLME + " (" + item.TYPE + ")");
                            } else {
                                datum.put("description", "(" + item.XBT_BOLME + ")");
                            }
                            data.add(datum);
                        }
                        final SimpleAdapter adapter = new SimpleAdapter(getContext(), data,
                                android.R.layout.simple_list_item_2,
                                new String[]{"title", "description"},
                                new int[]{android.R.id.text1,
                                        android.R.id.text2});
                        adapter.notifyDataSetChanged();
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        xbtListView.setAdapter(adapter);
                                        xbtListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                if (xbtFirstList.get(position).CNT > 0) {
                                                    idHistory.add(xbtFirstList.get(position).IDN);
                                                    getXbtList(xbtFirstList.get(position).IDN, view);
                                                }

                                            }
                                        });

                                    }
                                });

                    }
                }
                mWaitingDialog.dismiss();
            }
        }).start();


    }

    private void getXbtFirstList(final View view) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir...", true);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {

//                catID = bundle.getInt("catID");


                xbtFirstList = select.GetXbtFirstList(view);


                if (xbtFirstList != null) {
                    if (xbtFirstList.size() > 0) {

                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {
//                                        filesLayout.setVisibility(View.VISIBLE);
//                                        notFoundLabel.setVisibility(View.GONE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });

                        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                        for (XbtStruct item : xbtFirstList) {
                            Map<String, String> datum = new HashMap<String, String>(2);
                            datum.put("title", item.XBT_BOLME_IZAH);
                            if (item.TYPE != "") {
                                datum.put("description", item.XBT_BOLME + " (" + item.TYPE + ")");
                            } else {
                                datum.put("description", "(" + item.XBT_BOLME + ")");
                            }
                            data.add(datum);
                        }
                        final SimpleAdapter adapter = new SimpleAdapter(getContext(), data,
                                android.R.layout.simple_list_item_2,
                                new String[]{"title", "description"},
                                new int[]{android.R.id.text1,
                                        android.R.id.text2});
                        adapter.notifyDataSetChanged();
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        xbtListView.setAdapter(adapter);
                                        xbtListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                idHistory.add(xbtFirstList.get(position).IDN);
                                                getXbtList(xbtFirstList.get(position).IDN, view);


                                            }
                                        });

                                    }
                                });

                    }
                }
                mWaitingDialog.dismiss();
            }
        }).start();


    }

//    public String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = getActivity().getAssets().open("xbt_firstlist");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }
}