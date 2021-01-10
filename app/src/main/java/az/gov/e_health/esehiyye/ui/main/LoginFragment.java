package az.gov.e_health.esehiyye.ui.main;

import androidx.appcompat.widget.Toolbar;
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

import az.gov.e_health.esehiyye.Model.Database.DbSelect;
import az.gov.e_health.esehiyye.Model.UserStruct;

import az.gov.e_health.esehiyye.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    DbSelect select = new DbSelect();
    ProgressDialog mWaitingDialog;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.login_fragment, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        final EditText email = view.findViewById(R.id.email);
        final EditText pass = view.findViewById(R.id.password);
        final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        if (sharedPreferences.getString("userData", "") != "") {

            AppFragment fragment2 = new AppFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        }


        view.findViewById(R.id.signInClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn(email.getText().toString(), pass.getText().toString(), view);
            }
        });
        view.findViewById(R.id.signUpclick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpFragment fragment2 = new SignUpFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        view.findViewById(R.id.forgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PasswordRecoveryFragment fragment2 = new PasswordRecoveryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().popBackStack();
            }
        });
        return view;
    }


    public void signIn(final String email, final String pass, final View view) {


// when you start loading the data


// when data have been loaded
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir. Gözləyin...", true);
            }
        });
        final EditText emailEdit = view.findViewById(R.id.email);
        final EditText passEdit = view.findViewById(R.id.password);
        Thread signInCall = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<UserStruct> userList = select.signIn(email, pass, view);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mWaitingDialog.dismiss();
                        if (userList.size() > 0 && userList.get(0).ID != null) {
                            AppFragment fragment2 = new AppFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }

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
