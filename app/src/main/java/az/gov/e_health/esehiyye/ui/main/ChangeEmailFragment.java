package az.gov.e_health.esehiyye.ui.main;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.StatusStruct;

import az.gov.e_health.esehiyye.R;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeEmailFragment extends Fragment {

    ProgressDialog mWaitingDialog;
    DbInsert insert = new DbInsert();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view = inflater.inflate(R.layout.fragment_email_change, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Email redaktəsi");
        ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        Button saveBtn = view.findViewById(R.id.saveChanges);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(view);
            }
        });
        return view;
    }


    public void saveChanges(final View view) {
        TextInputLayout newEmailLayout = view.findViewById(R.id.newEmailLayout);
        TextInputLayout newEmailRepeatLayout = view.findViewById(R.id.repeatNewEmailLayout);
        EditText newEmail = view.findViewById(R.id.newEmail);
        EditText newEmailRepeat = view.findViewById(R.id.repeatNewEmail);
        final String newEmailText = newEmail.getText().toString();
       final String newEmailRepeatText = newEmailRepeat.getText().toString();


        if ((!newEmailText.isEmpty() && newEmailText != null) && (!newEmailRepeatText.isEmpty() && newEmailRepeatText != null)) {
            if (newEmailText.equals(newEmailRepeatText)) {
//                String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
//                (?=.*[0-9]) a digit must occur at least once
//                (?=.*[a-z]) a lower case letter must occur at least once
//                (?=.*[A-Z]) an upper case letter must occur at least once
//                (?=.*[@#$%^&+=]) a special character must occur at least once
//                (?=\\S+$) no whitespace allowed in the entire string
//                .{8,} at least 8 characters
                String pattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

                if (newEmailText.matches(pattern)) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir. Gözləyin...", true);
                        }
                    });
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    Thread signInCall = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences
                                    = getContext().getSharedPreferences("MySharedPref",
                                    MODE_PRIVATE);
                            final List<StatusStruct> status = insert.changeEmail(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null), newEmailText, view);


                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    if (status.size() > 0) {

                                        alertDialog.setTitle("Bildiriş");
                                        alertDialog.setMessage("Emailınz uğurla dəyişdrildi");
                                    } else {
                                        alertDialog.setTitle("Xəta");
                                        alertDialog.setMessage("Təəssüfki Emailınızı dəyişmək mümkün olmadı, biraz sonra yenidən cəht edin");


                                    }
                                    alertDialog.show();
                                }
                            });


                            mWaitingDialog.dismiss();

                        }
                    });

                    signInCall.start();
                } else {
                    newEmailLayout.setError("*Emailınızı düzgün daxil edin");
                    //  newEmailRepeatLayout.setError("*Yeni şifrə minimum 8 simvol, 1 böyük, 1 kiçik hərfdən və minimum 1 rəqəm dən ibarət olmalıdır");

                }

            } else {
                //  newEmailLayout.setError("Şifrə eyni olmalidir");
                newEmailRepeatLayout.setError("*Email eyni olmalidir");
            }
        } else {


            newEmailLayout.setError("*Zəhmət olmasa boşluğu doldurun");
            newEmailRepeatLayout.setError("*Zəhmət olmasa boşluğu doldurun");

        }

//        ChangePasswordFragment  changePasswordFragment = new ChangePasswordFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, changePasswordFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

    }

}
