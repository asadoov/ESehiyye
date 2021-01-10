package az.gov.e_health.esehiyye.ui.main;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
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
public class FeedbackFragment extends Fragment {
    ProgressDialog progressDialog;
    DbInsert insert = new DbInsert();

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Əks əlaqə");
        final Button sendFeedback = view.findViewById(R.id.sendFeedback);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedbackClicked(view);
            }
        });
        return view;
    }

    public void sendFeedbackClicked(final View view) {
        SharedPreferences sharedPreferences
                = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);

        String jsonUserData = sharedPreferences.getString("userData", "");
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        if (jsonUserData != "") {
            Gson gson = new GsonBuilder().setLenient().create();

            final List<UserStruct> userList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));


            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            EditText feedback = view.findViewById(R.id.feedbackTxt);
            final String feedbackText = feedback.getText().toString();
            if (feedbackText.length() > 5) {
                Thread sendFeedbackThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {


                                progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Yüklənir. Gözləyin...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                            }
                        });
                        SharedPreferences sharedPreferences
                                = getContext().getSharedPreferences("MySharedPref",
                                MODE_PRIVATE);
                        final List<FeedbackStatusStruct> status = insert.sendFeedback(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null), feedbackText, view);


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                if (status.size() > 0) {

                                    alertDialog.setTitle("Bildiriş");
                                    alertDialog.setMessage(String.format("Müraciətiniz göndərildi, 24 saat ərzində emailınıza (%s) cavab göndəriləcək", userList.get(0).EMAIL));
                                } else {
                                    alertDialog.setTitle("Xəta");
                                    alertDialog.setMessage("Təəssüfki müraciətinizi göndərmək mümkün olmadı, biraz sonra yenidən cəht edin");


                                }
                                alertDialog.show();
                            }
                        });


                        progressDialog.dismiss();

                    }
                });

                sendFeedbackThread.start();

            } else {
                alertDialog.setTitle("Bildiriş");
                alertDialog.setMessage("Müraciətiniz daha ətraflı olmalıdır");
                alertDialog.show();
            }
        }
        else {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Daxil ol",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            LoginFragment loginFragment = new LoginFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.appContainer, loginFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Bağla",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setTitle("Bildiriş");
            alertDialog.setMessage("Müraciət etmək üçün sistemə giriş etməyiniz vacibdir");
            alertDialog.show();
        }
    }

}
