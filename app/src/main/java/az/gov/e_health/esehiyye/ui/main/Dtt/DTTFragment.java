package az.gov.e_health.esehiyye.ui.main.Dtt;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import az.gov.e_health.esehiyye.Model.Adapters.Dtt.DttAdapter;
import az.gov.e_health.esehiyye.Model.DTT.DttStruct;
import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.R;

import static android.content.Context.MODE_PRIVATE;


public class DTTFragment extends Fragment {

    DttAdapter adapter;
    ProgressDialog progressDialog;
    DbInsert insert = new DbInsert();
    ArrayList<DttStruct> dttList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_d_t_t, container, false);

        final ListView dttListView = view.findViewById(R.id.dttList);

        dttList = (ArrayList<DttStruct>) getArguments().getSerializable("dttList");
        adapter = new DttAdapter(getContext(), dttList);
        registerForContextMenu(dttListView);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Fərdi inkişaf planı");
        final ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                backButton.setVisibility(View.GONE);
            }
        });
//        dttListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//              if (dttList.size()>0){
//
//
//
//              }
//
//            }
//        });

        dttListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


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
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.dttList) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.dtt_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int pos = info.position;

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        switch (item.getItemId()) {
            case R.id.deleteCourse:
                Thread sendFeedbackThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {


                                progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Yüklənir. Gözləyin...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                            }
                        });
                        SharedPreferences sharedPreferences
                                = getContext().getSharedPreferences("MySharedPref",
                                MODE_PRIVATE);
                        final List<FeedbackStatusStruct> status = insert.deleteCourse(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null), dttList.get(pos).DOC_KURS_ID, dttList.get(pos).TYPE, info.targetView);


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                if (status != null) {
                                    if (status.size() > 0) {
                                        //Toast.makeText(getContext(),status.get(0).RESULT, Toast.LENGTH_SHORT).show();
                                        if (status.get(0).RESULT.equals("1")) {
                                           // alertDialog.setTitle("Bildiriş");
                                            alertDialog.setMessage(String.format("(%s) adlı kursunuz silindi", dttList.get(pos).KURS_ADI));
                                            dttList.remove(pos);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            alertDialog.setTitle("Texniki xəta");
                                            alertDialog.setMessage("Biraz sonra yenidən cəht edin");
                                        }
                                    }

                                }
                                alertDialog.show();
                            }
                        });


                        progressDialog.dismiss();

                    }
                });

                sendFeedbackThread.start();

                // add stuff here
                return true;
//            case R.id.edit:
//                // edit stuff here
//                return true;
//            case R.id.delete:
//                // remove stuff here
//                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}