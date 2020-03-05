package az.gov.e_health.esehiyye.Model.Database;

import android.content.SharedPreferences;
import android.view.View;

import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.Model.StatusStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;
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

public class DbInsert {
    DbSelect select = new DbSelect();


    public List<StatusStruct> changePassword(String cypher1, String cypher2, String newPass, final View view) {
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


            Call<List<StatusStruct>> getUserData = api.passChange(cypher1, cypher2, newPass);
            Response<List<StatusStruct>> response = getUserData.execute();
            statusList = response.body();

            if (statusList.size() > 0) {
                final SharedPreferences.Editor myEdit
                        = sharedPreferences.edit();
                myEdit.putString(
                        "password",
                        newPass);
                myEdit.commit();

            }

            select.refreshCypher(cypher1, cypher2, view);


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<StatusStruct> changeEmail(String cypher1, String cypher2, String newEmail, final View view) {
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


            Call<List<StatusStruct>> getUserData = api.mailChange(cypher1, cypher2, newEmail);
            Response<List<StatusStruct>> response = getUserData.execute();
            statusList = response.body();

            if (statusList.size() > 0) {
                String jsonUserData = sharedPreferences.getString("userData", "");
                Gson gson = new GsonBuilder().setLenient().create();

              List<UserStruct>  userList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));
              userList.get(0).EMAIL=newEmail;
             jsonUserData = gson.toJson(userList);
                //Put to SharedPreference

                final SharedPreferences.Editor myEdit
                        = sharedPreferences.edit();
                myEdit.putString(
                        "userData",
                        jsonUserData);
                myEdit.commit();

            }

            select.refreshCypher(cypher1, cypher2, view);


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }
    public List<StatusStruct> changePhone(String cypher1, String cypher2, String newPhone, final View view) {
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


            Call<List<StatusStruct>> getUserData = api.phoneChange(cypher1, cypher2, newPhone);
            Response<List<StatusStruct>> response = getUserData.execute();
            statusList = response.body();

            if (statusList.size() > 0) {
                String jsonUserData = sharedPreferences.getString("userData", "");
                Gson gson = new GsonBuilder().setLenient().create();

                List<UserStruct>  userList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));
                userList.get(0).MOBILE=newPhone;
                jsonUserData = gson.toJson(userList);
                //Put to SharedPreference

                final SharedPreferences.Editor myEdit
                        = sharedPreferences.edit();
                myEdit.putString(
                        "userData",
                        jsonUserData);
                myEdit.commit();

            }

            select.refreshCypher(cypher1, cypher2, view);


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }
    public List<FeedbackStatusStruct> sendFeedback(String cypher1, String cypher2, String text, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<FeedbackStatusStruct>> getUserData = api.sendFeedback(cypher1, cypher2, text);
            Response<List<FeedbackStatusStruct>> response = getUserData.execute();
            statusList = response.body();



            select.refreshCypher(cypher1, cypher2, view);


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }
}
