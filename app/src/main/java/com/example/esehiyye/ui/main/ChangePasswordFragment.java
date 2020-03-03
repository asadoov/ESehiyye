package com.example.esehiyye.ui.main;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esehiyye.Model.Database.DbInsert;
import com.example.esehiyye.Model.Database.DbSelect;
import com.example.esehiyye.Model.StatusStruct;
import com.example.esehiyye.Model.UserStruct;
import com.example.esehiyye.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    ProgressDialog mWaitingDialog;
   DbInsert insert = new DbInsert();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
   final View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        TextView toolbarTitle = view.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Şifrə yenilənməsi");
        ImageButton backButton = view.findViewById(R.id.backBtn);
        Button saveBtn = view.findViewById(R.id.saveChanges);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
saveChanges(view);
            }
        });
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
   return view;
    }

    public void saveChanges(final View view){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir. Gözləyin...", true);
            }});
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Thread signInCall = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences
                        = getContext().getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);
              final  List<StatusStruct> status = insert.changePassword(sharedPreferences.getString("cypher1", null),sharedPreferences.getString("cypher2", null),"Asadov001",view);


    getActivity().runOnUiThread(new Runnable() {
        public void run() {

            if (status.size()>0) {
                alertDialog.setTitle("Bildiriş");
                alertDialog.setMessage("Şifrəniz uğurla dəyişdrildi");
        }
else     {
                alertDialog.setTitle("Xəta");
                alertDialog.setMessage("Təəsüfki şifrənizi dəyişmək mümkün olmadı biraz sonra yenidən cəht edin");




        }
            alertDialog.show();
        }
    });


                mWaitingDialog.dismiss();

            }
        });

        signInCall.start();



//        ChangePasswordFragment  changePasswordFragment = new ChangePasswordFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, changePasswordFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

    }

}
