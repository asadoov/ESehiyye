package com.example.esehiyye.ui.main;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.esehiyye.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordRecoveryFragment extends Fragment {


    public PasswordRecoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_recovery, container, false);
    }

}
