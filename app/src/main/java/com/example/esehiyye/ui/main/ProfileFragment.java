package com.example.esehiyye.ui.main;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.esehiyye.Model.UserStruct;
import com.example.esehiyye.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import android.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    List<UserStruct> usrList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        SharedPreferences sharedPreferences
                = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);

        String jsonUserData = sharedPreferences.getString("userData", "");
        Gson gson = new GsonBuilder().setLenient().create();

        usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));


        view.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsClick(usrList);
            }
        });
        view.findViewById(R.id.immunity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImmunityFragment  immunityFragment = new ImmunityFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, immunityFragment);
                fragmentTransaction.addToBackStack(null);
                //immunityFragment .setArguments(args);
                fragmentTransaction.commit();
            }
        });
        if (usrList.get(0).STATUS!="tibbkadr"){

view.findViewById(R.id.onlyForDoctors).setVisibility(View.GONE);

        }
       TextView userName = view.findViewById(R.id.user_name);
       TextView userDetails = view.findViewById(R.id.user_details);

       userName.setText(usrList.get(0).NAME);
       userDetails.setText(String.format("Boy: %s SM, Yaş: %s, Qan: %s",usrList.get(0).BOY.toString(),usrList.get(0).YASH.toString(),usrList.get(0).QAN));
        if (usrList.get(0).PHOTO_BASE64 != null && !usrList.get(0).PHOTO_BASE64.isEmpty())
        {
            byte[] decodedString = Base64.decode(usrList.get(0).PHOTO_BASE64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView userImage =  view.findViewById(R.id.user_image);
           userImage.setImageBitmap(decodedByte);

        }
       return view;
    }
    public void settingsClick(List<UserStruct> userList){
        Bundle args = new Bundle();



        //args.putParcelableArrayList("u", userList);



        SettingsFragment  fragment = new SettingsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragment .setArguments(args);
        fragmentTransaction.commit();

    }

}
