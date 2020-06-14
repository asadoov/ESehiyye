package az.gov.e_health.esehiyye.ui.main;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;

import az.gov.e_health.esehiyye.Model.UserStruct;

import az.gov.e_health.esehiyye.R;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment {


    List<UserStruct> usrList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);
           TextView toolbarTitle = view.findViewById(R.id.toolbarTitle);
     toolbarTitle.setText("Elektron xidmətlər");
        final FragmentManager fragmentManager = getFragmentManager();

        SharedPreferences sharedPreferences
                = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);

        String jsonUserData = sharedPreferences.getString("userData", "");
        Gson gson = new GsonBuilder().setLenient().create();

        usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));

        view.findViewById(R.id.immunity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImmunityFragment  immunityFragment = new ImmunityFragment();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, immunityFragment);
                fragmentTransaction.addToBackStack(null);
                //immunityFragment .setArguments(args);
                fragmentTransaction.commit();
            }
        });
        view.findViewById(R.id.drugs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrugsFragment  fragment = new DrugsFragment();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                //fragment .setArguments(args);
                fragmentTransaction.commit();
            }
        });
        if (usrList.get(0).STATUS!="tibbkadr"){

            view.findViewById(R.id.onlyForDoctors).setVisibility(View.GONE);

        }



        if (usrList.get(0).PHOTO_BASE64 != null && !usrList.get(0).PHOTO_BASE64.isEmpty())
        {
            byte[] decodedString = Base64.decode(usrList.get(0).PHOTO_BASE64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView userImage =  view.findViewById(R.id.user_image);
            userImage.setImageBitmap(decodedByte);

        }
        return  view;
    }

}
