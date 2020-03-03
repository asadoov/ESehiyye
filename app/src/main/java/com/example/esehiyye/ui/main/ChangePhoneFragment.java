package com.example.esehiyye.ui.main;


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

import com.example.esehiyye.Model.Database.DbInsert;
import com.example.esehiyye.Model.StatusStruct;
import com.example.esehiyye.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePhoneFragment extends Fragment {

    ProgressDialog mWaitingDialog;
    DbInsert insert = new DbInsert();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_change_phone, container, false);
        TextView toolbarTitle = view.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Mobil nömrənin redaktəsi");
        ImageButton backButton = view.findViewById(R.id.backBtn);
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
        TextInputLayout newPhoneLayout = view.findViewById(R.id.newPhoneLayout);
        TextInputLayout newPhoneRepeatLayout = view.findViewById(R.id.repeatNewPhoneLayout);
        EditText newPhone = view.findViewById(R.id.newPhone);
        EditText newPhoneRepeat = view.findViewById(R.id.repeatNewPhone);
        final String newPhoneText = newPhone.getText().toString();
        final String newPhoneRepeatText = newPhoneRepeat.getText().toString();


        if ((!newPhoneText.isEmpty() && newPhoneText != null) && (!newPhoneRepeatText.isEmpty() && newPhoneRepeatText != null)) {
            if (newPhoneText.equals(newPhoneRepeatText)) {
//                String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
//                (?=.*[0-9]) a digit must occur at least once
//                (?=.*[a-z]) a lower case letter must occur at least once
//                (?=.*[A-Z]) an upper case letter must occur at least once
//                (?=.*[@#$%^&+=]) a special character must occur at least once
//                (?=\\S+$) no whitespace allowed in the entire string
//                .{8,} at least 8 characters

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
                            final List<StatusStruct> status = insert.changePhone(sharedPreferences.getString("cypher1", null), sharedPreferences.getString("cypher2", null), newPhoneText, view);


                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    if (status.size() > 0) {

                                        alertDialog.setTitle("Bildiriş");
                                        alertDialog.setMessage("Nömrəniz uğurla dəyişdrildi");
                                    } else {
                                        alertDialog.setTitle("Xəta");
                                        alertDialog.setMessage("Təəsüfki nömrənizi dəyişmək mümkün olmadı, biraz sonra yenidən cəht edin");


                                    }
                                    alertDialog.show();
                                }
                            });


                            mWaitingDialog.dismiss();

                        }
                    });

                    signInCall.start();


            } else {
                //  newPhoneLayout.setError("Şifrə eyni olmalidir");
                newPhoneRepeatLayout.setError("Nömrələr eyni olmalidir");
            }
        } else {


            newPhoneLayout.setError("Zəhmət olmasa boşluğu doldurun");
            newPhoneRepeatLayout.setError("Zəhmət olmasa boşluğu doldurun");

        }

//        ChangePasswordFragment  changePasswordFragment = new ChangePasswordFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, changePasswordFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

    }

}
