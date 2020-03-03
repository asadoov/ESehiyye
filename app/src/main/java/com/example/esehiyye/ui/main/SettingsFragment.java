package com.example.esehiyye.ui.main;


import android.content.SharedPreferences;
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

import com.example.esehiyye.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView toolbarTitle = view.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Parametrlər");
        ImageButton backButton = view.findViewById(R.id.backBtn);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Button logOut = view.findViewById(R.id.logOut);
        Button changeEmail = view.findViewById(R.id.changeMail);
        Button changePass = view.findViewById(R.id.changePassword);
        Button changePhone = view.findViewById(R.id.changeMobile);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordCLicked(view);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutClicked(view);
            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmailCLicked(view);
            }
        });
        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhoneCLicked(view);
            }
        });
        return view;
    }


    public void logOutClicked(View view) {
        final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        final SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString(
                "userData",
                null);
        myEdit.commit();
        MainFragment signInFragment = new MainFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, signInFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    public void changePasswordCLicked(View view) {


        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, changePasswordFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void changeEmailCLicked(View view) {


        ChangeEmailFragment changeEmailFragment = new ChangeEmailFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, changeEmailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void changePhoneCLicked(View view) {


        ChangePhoneFragment changePhoneFragment = new ChangePhoneFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, changePhoneFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
