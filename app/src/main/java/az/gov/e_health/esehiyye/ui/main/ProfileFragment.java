package az.gov.e_health.esehiyye.ui.main;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import az.gov.e_health.esehiyye.Model.UserStruct;

import az.gov.e_health.esehiyye.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

import java.util.List;

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
      Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
      toolbar.setVisibility(View.GONE);
      final FragmentManager fragmentManager = getFragmentManager();

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        ServicesFragment  servicesFragment = new ServicesFragment();
        Fragment selectedFragment = null;
        FragmentTransaction serviceTransaction = fragmentManager.beginTransaction();
        serviceTransaction.replace(R.id.appContainer, servicesFragment);
        serviceTransaction.addToBackStack(null);
        //immunityFragment .setArguments(args);
        serviceTransaction.commit();
        SharedPreferences sharedPreferences
                = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);

        String jsonUserData = sharedPreferences.getString("userData", "");
        Gson gson = new GsonBuilder().setLenient().create();

        usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));

        BottomNavigationView bottomNavigationView =  view.findViewById(R.id.bottom_navigation);
      bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        switch (item.getItemId()) {

                            case R.id.settings:
                                SettingsFragment  settingsFragment = new SettingsFragment();


                                fragmentTransaction.replace(R.id.appContainer, settingsFragment);
                                fragmentTransaction.addToBackStack(null);
                                //immunityFragment .setArguments(args);

                                break;

                            case R.id.news:
                                NewsFragment  newsFragment = new NewsFragment();
                                fragmentTransaction.replace(R.id.appContainer, newsFragment);
                                fragmentTransaction.addToBackStack(null);
                                //immunityFragment .setArguments(args);

                                break;

                            case R.id.profile:
                                ServicesFragment  servicesFragment = new ServicesFragment();
                                fragmentTransaction.replace(R.id.appContainer, servicesFragment);
                                fragmentTransaction.addToBackStack(null);
                                //immunityFragment .setArguments(args);

                                break;


                            case R.id.feedback:
                                FeedbackFragment  feedBackFragment = new FeedbackFragment();
                                fragmentTransaction.replace(R.id.appContainer, feedBackFragment);
                                fragmentTransaction.addToBackStack(null);
                                //immunityFragment .setArguments(args);

                                break;

                        }

                        fragmentTransaction.commit();

                        return true;
                    }
                });


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
