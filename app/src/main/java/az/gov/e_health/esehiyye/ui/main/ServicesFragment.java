package az.gov.e_health.esehiyye.ui.main;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import az.gov.e_health.esehiyye.Model.UserStruct;

import az.gov.e_health.esehiyye.R;

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
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
           TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        backButton.setVisibility(View.GONE);
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
        view.findViewById(R.id.dtt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DttYearsFragment fragment = new DttYearsFragment();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                //fragment .setArguments(args);
                fragmentTransaction.commit();
            }
        });
        //Toast.makeText(getContext(), usrList.get(0).STATUS, Toast.LENGTH_SHORT).show();
        switch (usrList.get(0).STATUS) {
            case "tibbkadr":
                view.findViewById(R.id.onlyForDoctors).setVisibility(View.VISIBLE);
                break;
            default:
                view.findViewById(R.id.onlyForDoctors).setVisibility(View.GONE);
                break;
        }





//        if (usrList.get(0).PHOTO_BASE64 != null && !usrList.get(0).PHOTO_BASE64.isEmpty())
//        {
//            byte[] decodedString = Base64.decode(usrList.get(0).PHOTO_BASE64, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            ImageView userImage =  view.findViewById(R.id.user_image);
//            userImage.setImageBitmap(decodedByte);
//
//        }
        return  view;
    }

}
