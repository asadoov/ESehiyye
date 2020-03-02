package com.example.esehiyye.Model.Database;

import android.content.SharedPreferences;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.esehiyye.Model.CypherStruct;
import com.example.esehiyye.Model.UserStruct;
import com.example.esehiyye.R;
import com.example.esehiyye.ui.main.MainFragment;
import com.example.esehiyye.ui.main.ProfileFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;



public class DbSelect {



    public List<CypherStruct> signInCypher(String email, String pass, final View view){ List<CypherStruct> cypherList=new ArrayList<CypherStruct>();

        final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        final ApiInterface api = retrofit.create(ApiInterface.class);

        Call<List<CypherStruct>> call = api.getSignInCypher(email, pass);

        //then finallly we are making the call using enqueue()
        //it takes callback interface as an argument
        //and callback is having two methods onRespnose() and onFailure
        //if the request is successfull we will get the correct response and onResponse will be executed
        //if there is some error we will get inside the onFailure() method
        try
        {
        Response<List<CypherStruct>> response = call.execute();
    cypherList  = response.body();

        //now we can do whatever we want with this list
        if (!cypherList.get(0).cypher1.isEmpty()) {


            final SharedPreferences.Editor myEdit
                    = sharedPreferences.edit();

            myEdit.putString(
                    "cypher1",
                    cypherList.get(0).cypher1);

            myEdit.putString(
                    "cypher2",
                    cypherList.get(0).cypher2);
            myEdit.putString(
                    "password",
                    pass);
            myEdit.commit();
           // Toast.makeText(view.getContext(), cypherList.get(0).cypher1, Toast.LENGTH_SHORT).show();
        }


        }
        catch (Exception ex){

            ex.printStackTrace();
        }
            // Toast.makeText(view.getContext(), cypherList.get(0).cypher1, Toast.LENGTH_SHORT).show();

return cypherList;
    }

    public void refreshCypher(String cypher1, String cypher2, final View view){ List<CypherStruct> cypherList=new ArrayList<CypherStruct>();

        final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                MODE_PRIVATE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        final ApiInterface api = retrofit.create(ApiInterface.class);

        Call<List<CypherStruct>> call = api.getRefreshCypher(cypher1, cypher2);

        //then finallly we are making the call using enqueue()
        //it takes callback interface as an argument
        //and callback is having two methods onRespnose() and onFailure
        //if the request is successfull we will get the correct response and onResponse will be executed
        //if there is some error we will get inside the onFailure() method
        try
        {
            Response<List<CypherStruct>> response = call.execute();
            cypherList  = response.body();

            //now we can do whatever we want with this list
            final SharedPreferences.Editor myEdit
                    = sharedPreferences.edit();

if(cypherList.get(0).cypher!="") {



    myEdit.putString(
            "cypher2",
            cypherList.get(0).cypher);
    myEdit.commit();
    // Toast.makeText(view.getContext(), cypherList.get(0).cypher1, Toast.LENGTH_SHORT).show();
}
else{
    String jsonUserData = sharedPreferences.getString("userData", "");
    Gson gson = new GsonBuilder().setLenient().create();

 List<UserStruct>  usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));
signInCypher(usrList.get(0).EMAIL,sharedPreferences.getString("password", ""),view);
    myEdit.commit();

}


        }
        catch (Exception ex){

            ex.printStackTrace();
        }
        // Toast.makeText(view.getContext(), cypherList.get(0).cypher1, Toast.LENGTH_SHORT).show();


    }


    public List<UserStruct> signIn(String email, String pass,final View view)

    {

     List<CypherStruct> cypherList = signInCypher(email,pass,view);
 List<UserStruct> userList = new ArrayList<UserStruct>();

try {


    final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
            MODE_PRIVATE);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
            .build();

    //creating the api interface
    final ApiInterface api = retrofit.create(ApiInterface.class);




            //now we can do whatever we want with this list
            if (!cypherList.get(0).cypher1.isEmpty()) {


                Call<List<UserStruct>> getUserData = api.getUserData(cypherList.get(0).cypher1, cypherList.get(0).cypher2);
                Response<List<UserStruct>> response = getUserData.execute();


                //In this point we got our hero list
                //thats damn easy right ;)
                userList = response.body();

                final SharedPreferences.Editor myEdit
                        = sharedPreferences.edit();
                //Serializing UserData
                Gson gson = new Gson();
                String jsonUserData = gson.toJson(userList);
                //Put to SharedPreference
                myEdit.putString(
                        "userData",
                        jsonUserData);
                myEdit.commit();
                //now we can do whatever we want with this list

                //Toast.makeText(getContext(), userList.get(0).NAME, Toast.LENGTH_SHORT).show();


            }







    return userList;
}
catch (Exception ex){

    return  userList;

}


    }

}
