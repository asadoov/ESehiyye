package az.gov.e_health.esehiyye.ui.main;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.FileStruct;
import az.gov.e_health.esehiyye.Model.RecipeStruct;
import az.gov.e_health.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recipes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recipes extends Fragment {

    ProgressDialog mWaitingDialog;
    TextView notFoundLabel;
    AlertDialog alertDialog;
    List<RecipeStruct> sortedFiles = new ArrayList<>();
    List<RecipeStruct> recipes = new ArrayList<>();
    ListView recipeListView;
    LinearLayout filesLayout;
    DbSelect select = new DbSelect();

    public Recipes() {
        // Required empty public constructor
    }


    public static Recipes newInstance(String param1, String param2) {
        Recipes fragment = new Recipes();
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
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        recipeListView = view.findViewById(R.id.recipes);
        filesLayout = view.findViewById(R.id.recipeListLayout);


        notFoundLabel = view.findViewById(R.id.notFoundLabel);
        notFoundLabel.setVisibility(View.GONE);


        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Reseptlərim");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
        getFileList(view);
        final SearchView searchView = view.findViewById(R.id.recipeSearch);
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

                        sortedFiles.clear();
                        for (RecipeStruct item : recipes) {
                            if (item.DERMAN.toLowerCase().contains(newText.toLowerCase())) {
                                sortedFiles.add(item);

                            }


                        }
                        if (sortedFiles != null) {
                            if (sortedFiles.size() > 0) {
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                filesLayout.setVisibility(View.VISIBLE);
                                                notFoundLabel.setVisibility(View.GONE);
                                                //  Log.d("UI thread", "I am the UI thread");
                                            }
                                        });
                                List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                                for (RecipeStruct item : sortedFiles) {
                                    Map<String, String> datum = new HashMap<String, String>(2);
                                    datum.put("title", item.DERMAN);
                                    datum.put("date", item.MUESSIE);
                                    data.add(datum);
                                }
                                final SimpleAdapter adapter = new SimpleAdapter(getContext(), data,
                                        android.R.layout.simple_list_item_2,
                                        new String[]{"title", "date"},
                                        new int[]{android.R.id.text1,
                                                android.R.id.text2});
                                adapter.notifyDataSetChanged();
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                recipeListView.setAdapter(adapter);


//                                                recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                                    @Override
//                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                                        Bundle bundle = new Bundle();
//                                                        bundle.putInt("fileID", Integer.parseInt(recipes.get(position).ID));
//                                                        PdfViewer fragment2 = new PdfViewer();
//                                                        fragment2.setArguments(bundle);
//                                                        FragmentManager fragmentManager = getFragmentManager();
//                                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                                        fragmentTransaction.replace(R.id.container, fragment2);
//                                                        fragmentTransaction.addToBackStack(null);
//                                                        fragmentTransaction.commit();
//
//                                                    }
//                                                });
                                            }
                                        });

                            } else {

                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                filesLayout.setVisibility(View.GONE);
                                                notFoundLabel.setVisibility(View.VISIBLE);
                                                //  Log.d("UI thread", "I am the UI thread");
                                            }
                                        });
                            }
                        }


                        return true;
                    }
                });


        // handle your code here.


        return view;
    }

    private void getFileList(final View view) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir...", true);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {


                recipes = select.GetRecipeList(view);


                if (recipes != null) {
                    if (recipes.size() > 0) {

                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        filesLayout.setVisibility(View.VISIBLE);
                                        notFoundLabel.setVisibility(View.GONE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });

                        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                        for (RecipeStruct item : recipes) {
                            Map<String, String> datum = new HashMap<String, String>(2);
                            datum.put("title", item.DERMAN);
                            datum.put("date", item.MUESSIE);
                            data.add(datum);
                        }
                        final SimpleAdapter adapter = new SimpleAdapter(getContext(), data,
                                android.R.layout.simple_list_item_2,
                                new String[]{"title", "date"},
                                new int[]{android.R.id.text1,
                                        android.R.id.text2});
                        adapter.notifyDataSetChanged();
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        recipeListView.setAdapter(adapter);


//                                        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                            @Override
//                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                                Bundle bundle = new Bundle();
//                                                bundle.putInt("fileID", Integer.parseInt(recipes.get(position).ID));
//                                                PdfViewer fragment2 = new PdfViewer();
//                                                fragment2.setArguments(bundle);
//                                                FragmentManager fragmentManager = getFragmentManager();
//                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                                fragmentTransaction.replace(R.id.container, fragment2);
//                                                fragmentTransaction.addToBackStack(null);
//                                                fragmentTransaction.commit();
//
//                                            }
//                                        });
                                    }
                                });

                    }
                }
                mWaitingDialog.dismiss();
            }
        }).start();


    }
}