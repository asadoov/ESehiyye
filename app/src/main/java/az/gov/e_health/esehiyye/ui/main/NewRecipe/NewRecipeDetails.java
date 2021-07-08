package az.gov.e_health.esehiyye.ui.main.NewRecipe;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import az.gov.e_health.esehiyye.Model.Adapters.NewRecipeAdapter;
import az.gov.e_health.esehiyye.Model.Database.DbInsert;
import az.gov.e_health.esehiyye.Model.Database.DbSelect;

import az.gov.e_health.esehiyye.Model.NewRecipeStruct;
import az.gov.e_health.esehiyye.Model.RecipeDrugsStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;
import az.gov.e_health.esehiyye.R;

import static android.content.Context.MODE_PRIVATE;


public class NewRecipeDetails extends Fragment {

    ProgressDialog mWaitingDialog;
    NewRecipeAdapter adapter;
    RecipeDrugsStruct recipe = new RecipeDrugsStruct();
Bundle bundle;
    DbSelect select = new DbSelect();
    DbInsert insert = new DbInsert();
    List<UserStruct> usrList;
    ListView recipeListView;
    List<String> selectedEventsId = new ArrayList<>();
    TextView notFoundLabel;
String rId="",rDate="",rStatus="",rInst="";
    TextView resId,resDate,resStatus,resInst;
    List<String> drugList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_new_recipe_details, container, false);
        bundle = this.getArguments();
        rId = bundle.getString("rID");
        rDate = bundle.getString("rDate");
        rStatus = bundle.getString("rStatus");
        rInst = bundle.getString("rInst");
        recipeListView = view.findViewById(R.id.recipeDrugList);
        //notFoundLabel = view.findViewById(R.id.notFoundLabel);

resId = view.findViewById(R.id.rId);
resId.setText("Resept №"+rId);
        resStatus = view.findViewById(R.id.rStatus);
        resStatus.setText(rStatus);
        resDate = view.findViewById(R.id.rDate);
        resDate.setText(rDate);
        resInst = view.findViewById(R.id.rInst);
        resInst.setText(rInst);
        final Thread newsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mWaitingDialog = ProgressDialog.show(getContext(), "", "Yüklənir. Gözləyin...", true);
                    }
                });
                SharedPreferences sharedPreferences
                        = getContext().getSharedPreferences("MySharedPref",
                        MODE_PRIVATE);
                String jsonUserData = sharedPreferences.getString("userData", "");
                Gson gson = new GsonBuilder().setLenient().create();



                recipe = select.GetDrugListbyRecipeId(Integer.parseInt(rId),view);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {


int i = 0;


                            if (recipe.GetDrugListbyRecipeIdResult.IsSuccessfullyExecuted == true &&recipe.GetDrugListbyRecipeIdResult.Results.size() > 0) {
                                for (NewRecipeStruct.Drug item : recipe.GetDrugListbyRecipeIdResult.Results
                                ) {
                                    i++;
                                    drugList.add(i+". "+item.drugName+" / "+item.drugRoute+" / "+item.drugQuantity+" "+item.drugDosage+" / "+item.drugScheduled+" / "+
                                            item.drugDurationCount +" "+item.drugDuration+" / "+item.drugNote );
                                }
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                        (getContext(), android.R.layout.simple_list_item_1, drugList);

                                recipeListView.setAdapter(arrayAdapter);
                                arrayAdapter.notifyDataSetChanged();
                            }


                            mWaitingDialog.dismiss();
                        }
                    });
                }

            }
        });
        newsThread.start();
        return view;
    }
}