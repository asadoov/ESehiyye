package az.gov.e_health.esehiyye.ui.main;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import az.gov.e_health.esehiyye.Model.UserStruct;

import az.gov.e_health.esehiyye.R;
import az.gov.e_health.esehiyye.ui.main.Dtt.DttYearsFragment;
import az.gov.e_health.esehiyye.ui.main.Institutions.SelectRegion;
import az.gov.e_health.esehiyye.ui.main.XBT.Xbt_main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment {

    public static ServicesFragment newInstance() {
        return new ServicesFragment();
    }

    List<UserStruct> usrList = new ArrayList<>();


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

        final SharedPreferences sharedPreferences
                = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);

        String jsonUserData = sharedPreferences.getString("userData", "");

        if (jsonUserData != "") {

            Gson gson = new GsonBuilder().setLenient().create();
            usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));
            switch (usrList.get(0).STATUS) {
                case "tibbkadr":
                    view.findViewById(R.id.onlyForDoctors).setVisibility(View.VISIBLE);
                    break;
                default:
                    view.findViewById(R.id.onlyForDoctors).setVisibility(View.GONE);
                    break;
            }
        } else {
            view.findViewById(R.id.onlyForDoctors).setVisibility(View.GONE);
        }

        view.findViewById(R.id.myDocs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonUserData = sharedPreferences.getString("userData", "");
                if (jsonUserData != "") {
                    FileCats mDocCats = new FileCats();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, mDocCats);
                    fragmentTransaction.addToBackStack(null);
                    //mDocCats .setArguments(args);
                    fragmentTransaction.commit();
                } else {
                    LoginFragment loginFragment = new LoginFragment();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.appContainer, loginFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        view.findViewById(R.id.myRecipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonUserData = sharedPreferences.getString("userData", "");
                if (jsonUserData != "") {
                    Recipes mDocCats = new Recipes();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, mDocCats);
                    fragmentTransaction.addToBackStack(null);
                    //mDocCats .setArguments(args);
                    fragmentTransaction.commit();
                } else {
                    LoginFragment loginFragment = new LoginFragment();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.appContainer, loginFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        view.findViewById(R.id.xbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonUserData = sharedPreferences.getString("userData", "");
                if (jsonUserData != "") {
                    Xbt_main mDocCats = new Xbt_main();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, mDocCats);
                    fragmentTransaction.addToBackStack(null);
                    //mDocCats .setArguments(args);
                    fragmentTransaction.commit();

                } else {
                    LoginFragment loginFragment = new LoginFragment();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.appContainer, loginFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });
        view.findViewById(R.id.immunity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImmunityFragment immunityFragment = new ImmunityFragment();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.appContainer, immunityFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        view.findViewById(R.id.institutions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectRegion fragment = new SelectRegion();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                //immunityFragment .setArguments(args);
                fragmentTransaction.commit();
            }
        });
        view.findViewById(R.id.drugs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonUserData = sharedPreferences.getString("userData", "");
                if (jsonUserData != "") {
                    DrugsFragment fragment = new DrugsFragment();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    //fragment .setArguments(args);
                    fragmentTransaction.commit();
                } else {
                    LoginFragment loginFragment = new LoginFragment();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.appContainer, loginFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
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


//        if (usrList.get(0).PHOTO_BASE64 != null && !usrList.get(0).PHOTO_BASE64.isEmpty())
//        {
//            byte[] decodedString = Base64.decode(usrList.get(0).PHOTO_BASE64, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            ImageView userImage =  view.findViewById(R.id.user_image);
//            userImage.setImageBitmap(decodedByte);
//
//        }
      view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Toast.makeText(getContext(), "aaaaaa", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    System.exit(0);
                }
                return false;
            }
        } );
        return view;
    }

}
