package com.example.esehiyye.ui.main;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.esehiyye.Model.Database.DbSelect;
import com.example.esehiyye.Model.UserStruct;
import com.example.esehiyye.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment {

DbSelect select = new DbSelect();
    ProgressDialog mWaitingDialog;
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_fragment, container, false);

        final EditText email = view.findViewById(R.id.email);
        final EditText pass = view.findViewById(R.id.password);
        final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        if (sharedPreferences.getString("userData", null)!=null){

            ProfileFragment  fragment2 = new ProfileFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        }



        view.findViewById(R.id.signInClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

signIn(email.getText().toString(),pass.getText().toString(),view);
            }
        });
        return  view;
    }






    public void signIn(final String email,final String pass,final View view){


// when you start loading the data


// when data have been loaded
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir. Gözləyin...", true);
            }});
        final EditText emailEdit = view.findViewById(R.id.email);
        final EditText passEdit = view.findViewById(R.id.password);
            Thread signInCall = new Thread(new Runnable() {
                @Override
                public void run() {
                   final List<UserStruct> userList = select.signIn(email,pass,view);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (userList.size()>0) {
                                ProfileFragment fragment2 = new ProfileFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container, fragment2);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }
                            else {

                               emailEdit.setError("Zəhmət olmasa Email-zın düzgünlüyünü yoxlayın");
                                passEdit.setError("Zəhmət olmasa şifrəninzin düzgünlüyünü yoxlayın");
                            }
                            mWaitingDialog.dismiss();
                        }
                    });




                }
            });

            signInCall.start();
//try {
//
//    signInCall.join();
//}
//catch (Exception ex){
//    ex.printStackTrace();
//}



    }

}
