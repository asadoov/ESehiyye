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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import az.gov.e_health.esehiyye.Model.Adapters.NewsAdapter;
import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.NewsStruct;

import az.gov.e_health.esehiyye.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    ProgressDialog mWaitingDialog;
    List<NewsStruct> newsList = new ArrayList<>();
    List<NewsStruct> sortedNewsList = new ArrayList<>();
    NewsAdapter adapter;
    DbSelect select = new DbSelect();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        sortedNewsList.clear();
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Xəbərlər");
        final ListView newsListView = view.findViewById(R.id.newsListView);
        final SearchView searchView = view.findViewById(R.id.newsSearch);

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
                        sortedNewsList.clear();
                        for (NewsStruct item : newsList) {
                            if (item.NAME.toLowerCase().contains(newText.toLowerCase())) {
                                sortedNewsList.add(item);

                            }


                        }
                        adapter = new NewsAdapter(getContext(), sortedNewsList);
                        newsListView.setAdapter(adapter);
                        return true;
                    }
                });

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir. Gözləyin...", true);
            }
        });
        Thread newsThread = new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences
                        = getContext().getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);
                newsList = select.getNews(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null), view);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mWaitingDialog.dismiss();
                        if (newsList.size() > 0) {
                            adapter = new NewsAdapter(getContext(), newsList);
                            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Bundle bundle = new Bundle();

                                    if (sortedNewsList.size() > 0) {
                                        bundle.putString("newsTitle", sortedNewsList.get(position).NAME);
                                        bundle.putString("newsDescription", sortedNewsList.get(position).TEXT);
                                        ReadNewsFragment fragment = new ReadNewsFragment();
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.appContainer, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    } else {
                                        bundle.putString("newsTitle", newsList.get(position).NAME);
                                        bundle.putString("newsDescription", newsList.get(position).TEXT);
                                        ReadNewsFragment fragment = new ReadNewsFragment();
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.appContainer, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();


                                    }
                                    searchView.setQuery("", false);
                                }
                            });
                            newsListView.setAdapter(adapter);

                        } else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setTitle("Bildiriş");
                            alertDialog.setMessage("Xəbərləri yükləmək mümkün olmadı, zəhmət olmasa biraz sonra yenidən cəht edin");

                            alertDialog.show();
                        }

                    }
                });


            }
        });
        newsThread.start();
        return view;
    }

}
