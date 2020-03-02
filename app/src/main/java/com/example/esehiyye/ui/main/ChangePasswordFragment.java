package com.example.esehiyye.ui.main;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esehiyye.Model.Database.DbInsert;
import com.example.esehiyye.Model.Database.DbSelect;
import com.example.esehiyye.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {


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

    public void saveChanges(View view){

       

//        ChangePasswordFragment  changePasswordFragment = new ChangePasswordFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, changePasswordFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

    }

}
