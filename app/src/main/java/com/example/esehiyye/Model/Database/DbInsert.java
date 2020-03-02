package com.example.esehiyye.Model.Database;

import android.content.SharedPreferences;
import android.view.View;

import com.example.esehiyye.Model.CypherStruct;
import com.example.esehiyye.Model.StatusStruct;
import com.example.esehiyye.Model.UserStruct;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class DbInsert {
DbSelect select=new DbSelect();




    public List<StatusStruct> changePassword(String cypher1, String cypher2,String newPass,final View view)

    {
        List<StatusStruct> statusList = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);







                Call<List<StatusStruct>> getUserData = api.passChange(cypher1, cypher2,newPass);
                Response<List<StatusStruct>> response = getUserData.execute();
statusList=response.body();



                 select.refreshCypher(cypher1,cypher2,view);



            return  statusList;






        }
        catch (Exception ex){

            return  statusList;

        }


    }

}
