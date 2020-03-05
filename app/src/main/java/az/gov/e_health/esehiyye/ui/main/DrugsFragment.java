package az.gov.e_health.esehiyye.ui.main;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import az.gov.e_health.esehiyye.Model.Adapters.DrugsAdapter;
import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.DrugsStruct;

import az.gov.e_health.esehiyye.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrugsFragment extends Fragment {

    ProgressDialog mWaitingDialog;
    List<DrugsStruct> drugsList = new ArrayList<>();
    List<DrugsStruct> sortedDrugsList = new ArrayList<>();

    DbSelect select = new DbSelect();

    DrugsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sortedDrugsList.clear();
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_drugs, container, false);
        final   AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        TextView toolbarTitle = view.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Dərman vasitələri haqqında məlumat");
        ImageButton backButton = view.findViewById(R.id.backBtn);
        Button saveBtn = view.findViewById(R.id.saveChanges);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        SearchView searchView = view.findViewById(R.id.drugsSearch);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    // Override onQueryTextSubmit method
                    // which is call
                    // when submitquery is searched

                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {

                        return false;
                    }

                    // This method is overridden to filter
                    // the adapter according to a search query
                    // when the user is typing search
                    @Override
                    public boolean onQueryTextChange(String newText)
                    {
                        sortedDrugsList.clear();
                        for (DrugsStruct item:drugsList)
                             {
                                 if (item.AD.toLowerCase().contains(newText.toLowerCase())){
                                  sortedDrugsList.add(item);

                                 }


                    }
                        adapter = new DrugsAdapter(getContext(),sortedDrugsList);
                        ListView drugsListView = view.findViewById(R.id.drugsListView);
                        drugsListView.setAdapter(adapter);
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
                drugsList = select.getDrugs(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null), view);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mWaitingDialog.dismiss();
                        if (drugsList.size() > 0) {
                          adapter = new DrugsAdapter(getContext(), drugsList);
                            ListView drugsListView = view.findViewById(R.id.drugsListView);
                            drugsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                  if (sortedDrugsList.size()>0){
                                      alertDialog.setTitle(sortedDrugsList.get(position).AD);
                                      alertDialog.setMessage(String.format("BPA doza: %s\n\nTicarət forması: %s\n\nİstehsalçı (ölkə/firma): %s\n\nQeydiyyat bitmə tarixi: %s",sortedDrugsList.get(position).BPA_DOZA,sortedDrugsList.get(0).FORMA_TICARET,sortedDrugsList.get(0).FIRMA_OLKE,sortedDrugsList.get(0).QEYDIYYAT_BITME_TARIXI));

                                      alertDialog.show();

                                  }
                                  else
                                  {
                                      alertDialog.setTitle(drugsList.get(position).AD);
                                      alertDialog.setMessage(String.format("BPA doza: %s\n\nTicarət forması: %s\n\nİstehsalçı (ölkə/firma): %s\n\nQeydiyyat bitmə tarixi: %s",drugsList.get(position).BPA_DOZA,drugsList.get(0).FORMA_TICARET,drugsList.get(0).FIRMA_OLKE,drugsList.get(0).QEYDIYYAT_BITME_TARIXI));

                                      alertDialog.show();

                                  }

                                }
                            });
                            drugsListView.setAdapter(adapter);

                        } else {

                            alertDialog.setTitle("Bildiriş");
                            alertDialog.setMessage("Dərman vasitələri haqqında məlumatları yükləmək mümkün olmadı, zəhmət olmasa biraz sonra yenidən cəht edin");

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

