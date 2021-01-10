package az.gov.e_health.esehiyye.ui.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import az.gov.e_health.esehiyye.Model.UserStruct;
import az.gov.e_health.esehiyye.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    List<UserStruct> usrList;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        Button logOut = view.findViewById(R.id.logOut);
        Button changeEmail = view.findViewById(R.id.changeMail);
        Button changePass = view.findViewById(R.id.changePassword);
        Button changePhone = view.findViewById(R.id.changeMobile);
        Button covid19 = view.findViewById(R.id.coronaNews);
        SharedPreferences sharedPreferences
                = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);

        String jsonUserData = sharedPreferences.getString("userData", "");
        if (jsonUserData != "") {
            Gson gson = new GsonBuilder().setLenient().create();

            usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));
            TextView userName = view.findViewById(R.id.user_name);
            TextView userDetails = view.findViewById(R.id.user_details);

            userName.setText(usrList.get(0).NAME);
            userDetails.setText(String.format("Boy: %s SM, Yaş: %s, Qan: %s", usrList.get(0).BOY.toString(), usrList.get(0).YASH.toString(), usrList.get(0).QAN));
            if (usrList.get(0).PHOTO_BASE64 != null && !usrList.get(0).PHOTO_BASE64.isEmpty()) {
                byte[] decodedString = Base64.decode(usrList.get(0).PHOTO_BASE64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ImageView userImage = view.findViewById(R.id.user_image);
                userImage.setImageBitmap(decodedByte);

            }
            CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setActivated(true);
            collapsingToolbar.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));
            collapsingToolbar.setTitle(usrList.get(0).NAME);

            covid19.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://www.koronavirusinfo.az/"));
                    startActivity(viewIntent);
                }
            });
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
        } else {
            logOut.setText("Daxil ol");
            changeEmail.setVisibility(View.GONE);
            changePass.setVisibility(View.GONE);
            changePhone.setVisibility(View.GONE);
            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginFragment loginFragment = new LoginFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.appContainer, loginFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

        }

        return view;
    }


    public void logOutClicked(View view) {
        final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        final SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString(
                "userData",
                "");
        myEdit.putString(
                "cypher1",
                "");
        myEdit.putString(
                "cypher2",
                "");
        myEdit.commit();
        LoginFragment signInFragment = new LoginFragment();
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
        fragmentTransaction.replace(R.id.appContainer, changePasswordFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void changeEmailCLicked(View view) {


        ChangeEmailFragment changeEmailFragment = new ChangeEmailFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.appContainer, changeEmailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void changePhoneCLicked(View view) {


        ChangePhoneFragment changePhoneFragment = new ChangePhoneFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.appContainer, changePhoneFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
