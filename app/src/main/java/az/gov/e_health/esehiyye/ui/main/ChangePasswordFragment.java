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
public class ChangePasswordFragment extends Fragment {

    ProgressDialog mWaitingDialog;
    DbInsert insert = new DbInsert();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Şifrənin redaktəsi");
        ImageButton backButton = getActivity().findViewById(R.id.backBtn);
        Button saveBtn = view.findViewById(R.id.saveChanges);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(view);
            }
        });
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

    public void saveChanges(final View view) {
        TextInputLayout passLayout = view.findViewById(R.id.newPassLayout);
        TextInputLayout passRepeatLayout = view.findViewById(R.id.repeatNewPassLayout);
        EditText pass = view.findViewById(R.id.newPass);
        EditText passRepeat = view.findViewById(R.id.repeatNewPass);
        final String passText = pass.getText().toString();
        String passRepeatText = passRepeat.getText().toString();


        if ((!passText.isEmpty() && passText != null) && (!passRepeatText.isEmpty() && passRepeatText != null)) {
            if (passText.equals(passRepeatText)) {
//                String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
//                (?=.*[0-9]) a digit must occur at least once
//                (?=.*[a-z]) a lower case letter must occur at least once
//                (?=.*[A-Z]) an upper case letter must occur at least once
//                (?=.*[@#$%^&+=]) a special character must occur at least once
//                (?=\\S+$) no whitespace allowed in the entire string
//                .{8,} at least 8 characters
                String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}";

                if (passText.matches(pattern)) {
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
                            final List<StatusStruct> status = insert.changePassword(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null), passText, view);


                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    if (status.size() > 0) {

                                        alertDialog.setTitle("Bildiriş");
                                        alertDialog.setMessage("Şifrəniz uğurla dəyişdrildi");
                                    } else {
                                        alertDialog.setTitle("Xəta");
                                        alertDialog.setMessage("Təəsüfki şifrənizi dəyişmək mümkün olmadı biraz sonra yenidən cəht edin");


                                    }
                                    alertDialog.show();
                                }
                            });


                            mWaitingDialog.dismiss();

                        }
                    });

                    signInCall.start();
                } else {
                    passLayout.setError("*Yeni şifrə minimum 8 simvol, 1 böyük, 1 kiçik hərfdən və minimum 1 rəqəm dən ibarət olmalıdır");
                  //  passRepeatLayout.setError("*Yeni şifrə minimum 8 simvol, 1 böyük, 1 kiçik hərfdən və minimum 1 rəqəm dən ibarət olmalıdır");

                }

            } else {
              //  passLayout.setError("Şifrə eyni olmalidir");
                passRepeatLayout.setError("Şifrə eyni olmalidir");
            }
        } else {


            passLayout.setError("Zəhmət olmasa boşluğu doldurun");
            passRepeatLayout.setError("Zəhmət olmasa boşluğu doldurun");

        }

//        ChangePasswordFragment  changePasswordFragment = new ChangePasswordFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, changePasswordFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

    }

}
