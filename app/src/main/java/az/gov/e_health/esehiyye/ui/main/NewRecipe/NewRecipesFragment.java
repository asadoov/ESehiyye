package az.gov.e_health.esehiyye.ui.main.NewRecipe;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import az.gov.e_health.esehiyye.Model.UserStruct;
import az.gov.e_health.esehiyye.R;

import static android.content.Context.MODE_PRIVATE;


public class NewRecipesFragment extends Fragment {
    ProgressDialog mWaitingDialog;
    NewRecipeAdapter adapter;
    NewRecipeStruct recipe = new NewRecipeStruct();

    DbSelect select = new DbSelect();
    DbInsert insert = new DbInsert();
    List<UserStruct> usrList;
    ListView recipeListView;
    List<String> selectedEventsId = new ArrayList<>();
    TextView notFoundLabel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_new_recipes, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Reseptlərim");
        recipeListView = view.findViewById(R.id.recipeList);
        notFoundLabel = view.findViewById(R.id.notFoundLabel);
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

                usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));

                recipe = select.GetNewRecipes(view);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {




                            if (recipe.GetRecipeListResult != null&&recipe.GetRecipeListResult.TotalCount > 0) {
                                adapter = new NewRecipeAdapter(getContext(), recipe);
                                recipeListView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }


                            mWaitingDialog.dismiss();
                        }
                    });
                }

            }
        });
        newsThread.start();
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long l) {
                Bundle args = new Bundle();
                args.putString("rID",Integer.toString((recipe.GetRecipeListResult.Results.get(position).Id)));
                args.putString("rDate",recipe.GetRecipeListResult.Results.get(position).RecipeDate);
                args.putString("rStatus",recipe.GetRecipeListResult.Results.get(position).StatusText);
                args.putString("rInst",recipe.GetRecipeListResult.Results.get(position).InstName);
                NewRecipeDetails recipeDetails = new NewRecipeDetails();
                recipeDetails.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, recipeDetails);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
                //dttNewEventsListView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }
        });

        return  view;

    }
}