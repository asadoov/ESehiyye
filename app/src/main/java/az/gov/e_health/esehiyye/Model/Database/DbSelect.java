package az.gov.e_health.esehiyye.Model.Database;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import az.gov.e_health.esehiyye.Model.CypherStruct;
import az.gov.e_health.esehiyye.Model.DTT.DttDocImproveCourseStruct;
import az.gov.e_health.esehiyye.Model.DTT.DttNewEventStruct;
import az.gov.e_health.esehiyye.Model.DTT.DttStruct;
import az.gov.e_health.esehiyye.Model.DrugsStruct;
import az.gov.e_health.esehiyye.Model.FileStruct;
import az.gov.e_health.esehiyye.Model.Institutions.InstStruct;
import az.gov.e_health.esehiyye.Model.Institutions.InstTypeStruct;
import az.gov.e_health.esehiyye.Model.NewsStruct;
import az.gov.e_health.esehiyye.Model.RecipeStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;
import az.gov.e_health.esehiyye.Model.XbtStruct;
import az.gov.e_health.esehiyye.R;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class DbSelect {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();

    public List<CypherStruct> signInCypher(String email, String pass, final View view) {
        List<CypherStruct> cypherList = new ArrayList<CypherStruct>();
        final EditText emailEdit = view.findViewById(R.id.email);
        final EditText passEdit = view.findViewById(R.id.password);
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
        try {
            Response<List<CypherStruct>> response = call.execute();

            if (response.code() == 200) {
                cypherList = response.body();
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
                } else {
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    emailEdit.setError("*Zəhmət olmasa Emailınızın düzgünlüyünü yoxlayın");
                                    passEdit.setError("*Zəhmət olmasa şifrəninzin düzgünlüyünü yoxlayın");
                                    Log.d("UI thread", "I am the UI thread");
                                }
                            });
                }

            } else {
                ShowServerExceptionAlert(view);

            }


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);
            ex.printStackTrace();
        }
        // Toast.makeText(view.getContext(), cypherList.get(0).cypher1, Toast.LENGTH_SHORT).show();

        return cypherList;
    }

    public void refreshCypher(String cypher1, String cypher2, final View view) {
        List<CypherStruct> cypherList = new ArrayList<CypherStruct>();

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
        try {
            Response<List<CypherStruct>> response = call.execute();
            cypherList = response.body();

            //now we can do whatever we want with this list
            final SharedPreferences.Editor myEdit
                    = sharedPreferences.edit();

            if (cypherList.size() > 0) {


                myEdit.putString(
                        "cypher2",
                        cypherList.get(0).cypher);
                myEdit.commit();
                // Toast.makeText(view.getContext(), cypherList.get(0).cypher1, Toast.LENGTH_SHORT).show();
            } else {

                new Handler(Looper.getMainLooper()).post(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
                                    notFoundLabel.setVisibility(View.GONE);
                                } catch (Exception exception) {

                                }
                            }
                        });


                myEdit.putString(
                        "userData",
                        "");
                myEdit.putString(
                        "cypher1",
                        "");
                myEdit.putString(
                        "cypher2",
                        "");
                myEdit.commit();
                ShowSessionEnd(view);
//                String jsonUserData = sharedPreferences.getString("userData", "");
//                Gson gson = new GsonBuilder().setLenient().create();
//
//                List<UserStruct> usrList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));
//                signInCypher(usrList.get(0).EMAIL, sharedPreferences.getString("password", ""), view);


            }


        } catch (Exception ex) {

            ex.printStackTrace();
        }
        // Toast.makeText(view.getContext(), cypherList.get(0).cypher1, Toast.LENGTH_SHORT).show();


    }


    public List<UserStruct> signIn(String email, String pass, final View view) {

        List<CypherStruct> cypherList = signInCypher(email, pass, view);
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

            final EditText emailEdit = view.findViewById(R.id.email);
            final EditText passEdit = view.findViewById(R.id.password);
            //now we can do whatever we want with this list
            if (cypherList.size() > 0 && !cypherList.get(0).cypher1.isEmpty() && !cypherList.get(0).cypher2.isEmpty()) {


                Call<List<UserStruct>> getUserData = api.getUserData(cypherList.get(0).cypher1, cypherList.get(0).cypher2);
                Response<List<UserStruct>> response = getUserData.execute();


                userList = response.body();

                switch (response.code()) {

                    case 200:
                        //In this point we got our hero list
                        //thats damn easy right ;)
                        if (userList.size() > 0 && userList.get(0).ID != null) {

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
                            if (userList.size() == 0) {
                                new Handler(Looper.getMainLooper()).post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                emailEdit.setError("*Zəhmət olmasa Emailınızın düzgünlüyünü yoxlayın");
                                                passEdit.setError("*Zəhmət olmasa şifrəninzin düzgünlüyünü yoxlayın");
                                                Log.d("UI thread", "I am the UI thread");
                                            }
                                        });


                            }
                        } else {
                            userList.clear();
                            ShowServerExceptionAlert(view);
                        }

                        break;
                    default:
                        ShowServerExceptionAlert(view);
                        break;


                }


            }


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);


        }

        return userList;
    }

    public void ShowSessionEnd(final View view) {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {

                        final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.setTitle("Sessiyanız başa çatıb");
                        alertDialog.setMessage("Digər qurğudan sizin akaunta giriş olunub");
                        alertDialog.setCancelable(false);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        FragmentManager fragManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                                        fragManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    }
                                });
                        alertDialog.show();
                        //  Log.d("UI thread", "I am the UI thread");
                    }
                });

    }

    public void ShowServerExceptionAlert(final View view) {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        final FragmentManager fragManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                        final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.setTitle("Server xətası");
                        alertDialog.setMessage("Biraz sonra yenidən cəht edin");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Əsas səhifə",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        fragManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


                                    }
                                });

                        alertDialog.show();
                        //  Log.d("UI thread", "I am the UI thread");
                    }
                });

    }

    public void ShowInternetExceptionAlert(final View view) {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.setTitle("Şəbəkə xətası");
                        alertDialog.setMessage("Şəbəkənizin açıq olduğundan əmin olun");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        alertDialog.show();
                        //  Log.d("UI thread", "I am the UI thread");
                    }
                });

    }

    public List<NewsStruct> getNews(String cypher1, String cypher2, final View view) {
        List<NewsStruct> newsList = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<NewsStruct>> getUserData = api.getNews(cypher1, cypher2);
            Response<List<NewsStruct>> response = getUserData.execute();
            newsList = response.body();


            refreshCypher(cypher1, cypher2, view);


            return newsList;


        } catch (Exception ex) {

            return newsList;

        }


    }

    public List<DrugsStruct> getDrugs(String cypher1, String cypher2, final View view) {

        List<DrugsStruct> list = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<DrugsStruct>> getUserData = api.getDrugs(cypher1, cypher2);
            Response<List<DrugsStruct>> response = getUserData.execute();
            list = response.body();


            refreshCypher(cypher1, cypher2, view);


            return list;


        } catch (Exception ex) {

            return list;

        }


    }

    public List<DttStruct> getDttTimeline(String cypher1, String cypher2, String fin, final View view) {
        List<DttStruct> list = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<DttStruct>> getUserData = api.dttTimeline(cypher1, cypher2, fin);
            Response<List<DttStruct>> response = getUserData.execute();
            list = response.body();

            if (response.code() == 200) {

                if (list.size() == 0) {
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
                                    notFoundLabel.setVisibility(View.VISIBLE);
                                }
                            });
                }
                refreshCypher(cypher1, cypher2, view);
            } else {

                ShowServerExceptionAlert(view);
            }


            return list;


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);
            return list;

        }


    }

    public List<DttNewEventStruct> getDttNewEvents(String cypher1, String cypher2, String fin, int year, final View view) {
        List<DttNewEventStruct> list = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<DttNewEventStruct>> getDttNewEvents = api.getDttNewEvents(cypher1, cypher2, fin, year);
            Response<List<DttNewEventStruct>> response = getDttNewEvents.execute();
            if (response.code() == 200) {
                list = response.body();


                refreshCypher(cypher1, cypher2, view);

            } else {

                Toast.makeText(view.getContext(), "Zəhmət olmasa biraz sonra yenidən cəht edin", Toast.LENGTH_SHORT).show();
            }


            return list;


        } catch (Exception ex) {

            return list;

        }


    }

    public List<DttDocImproveCourseStruct> getDttDocImproveCourseList(String fin, int year, final View view) {
        List<DttDocImproveCourseStruct> list = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            String cypher1 = sharedPreferences.getString("cypher1", null);
            String cypher2 = sharedPreferences.getString("cypher2", null);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<DttDocImproveCourseStruct>> getDttNewEvents = api.getDttDocImproveCourseList(cypher1, cypher2, fin, year);
            Response<List<DttDocImproveCourseStruct>> response = getDttNewEvents.execute();
            list = response.body();
            if (response.code() == 200) {

                if (list.size() == 0) {
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
                                    notFoundLabel.setVisibility(View.VISIBLE);
                                }
                            });
                }

                refreshCypher(cypher1, cypher2, view);

            } else {

                ShowServerExceptionAlert(view);
            }


            return list;


        } catch (Exception ex) {

            return list;

        }


    }

    public List<FileStruct> GetFileList(long catID, final View view) {
        List<FileStruct> list = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            String cypher1 = sharedPreferences.getString("cypher1", null);
            String cypher2 = sharedPreferences.getString("cypher2", null);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<FileStruct>> getUserData = api.GetFileList(cypher1, cypher2, catID);
            Response<List<FileStruct>> response = getUserData.execute();
            list = response.body();

            final LinearLayout fileList = view.findViewById(R.id.fileListLayout);
            final TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
            switch (response.code()) {

                case 200:
                    refreshCypher(cypher1, cypher2, view);
                    if (response.body() == null || response.body().size() == 0) {
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        fileList.setVisibility(View.GONE);

                                        notFoundLabel.setVisibility(View.VISIBLE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });
                    }

                    break;
                default:
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {


                                    fileList.setVisibility(View.GONE);
                                    notFoundLabel.setText("Serverlərimizdə xəta baş verdi, biraz sonra yenidən təkrar edin :(");
                                    notFoundLabel.setVisibility(View.VISIBLE);
                                    //  Log.d("UI thread", "I am the UI thread");
                                }
                            });
                    ShowServerExceptionAlert(view);
                    break;


            }
            return list;


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);
            return list;

        }


    }

    public List<RecipeStruct> GetRecipeList(final View view) {
        List<RecipeStruct> list = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            String cypher1 = sharedPreferences.getString("cypher1", null);
            String cypher2 = sharedPreferences.getString("cypher2", null);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<RecipeStruct>> getUserData = api.GetRecipeList(cypher1, cypher2);
            Response<List<RecipeStruct>> response = getUserData.execute();
            list = response.body();

            final LinearLayout recipeList = view.findViewById(R.id.recipeListLayout);
            final TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
            switch (response.code()) {

                case 200:
                    refreshCypher(cypher1, cypher2, view);
                    if (response.body() == null || response.body().size() == 0) {
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        recipeList.setVisibility(View.GONE);

                                        notFoundLabel.setVisibility(View.VISIBLE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });
                    }

                    break;
                default:
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {


                                    recipeList.setVisibility(View.GONE);
                                    notFoundLabel.setText("Serverlərimizdə xəta baş verdi, biraz sonra yenidən təkrar edin :(");
                                    notFoundLabel.setVisibility(View.VISIBLE);
                                    //  Log.d("UI thread", "I am the UI thread");
                                }
                            });
                    ShowServerExceptionAlert(view);
                    break;


            }
            return list;


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);
            return list;

        }


    }

    public List<InstTypeStruct> GetInstTypeList(long regionID, final View view) {
        List<InstTypeStruct> list = new ArrayList<>();

        try {


//            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
//                    MODE_PRIVATE);
//            String cypher1 = sharedPreferences.getString("cypher1", null);
//            String cypher2 = sharedPreferences.getString("cypher2", null);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<InstTypeStruct>> getUserData = api.GetInstTypeList(regionID, 0);
            Response<List<InstTypeStruct>> response = getUserData.execute();
            list = response.body();

            final LinearLayout recipeList = view.findViewById(R.id.recipeListLayout);
            final TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
            switch (response.code()) {

                case 200:

                    if (response.body() == null || response.body().size() == 0) {
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        recipeList.setVisibility(View.GONE);

                                        notFoundLabel.setVisibility(View.VISIBLE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });
                    }

                    break;
                default:
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {


                                    recipeList.setVisibility(View.GONE);
                                    notFoundLabel.setText("Serverlərimizdə xəta baş verdi, biraz sonra yenidən təkrar edin :(");
                                    notFoundLabel.setVisibility(View.VISIBLE);
                                    //  Log.d("UI thread", "I am the UI thread");
                                }
                            });
                    ShowServerExceptionAlert(view);
                    break;


            }
            return list;


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);
            return list;

        }


    }

    public List<InstStruct> GetInstList(long regionID, long typeID, final View view) {
        List<InstStruct> list = new ArrayList<>();

        try {


//            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
//                    MODE_PRIVATE);
//            String cypher1 = sharedPreferences.getString("cypher1", null);
//            String cypher2 = sharedPreferences.getString("cypher2", null);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<InstStruct>> getUserData = api.GetInstList(regionID, typeID);
            Response<List<InstStruct>> response = getUserData.execute();
            list = response.body();

            final LinearLayout recipeList = view.findViewById(R.id.recipeListLayout);
            final TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
            switch (response.code()) {

                case 200:

                    if (response.body() == null || response.body().size() == 0) {
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        recipeList.setVisibility(View.GONE);

                                        notFoundLabel.setVisibility(View.VISIBLE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });
                    }

                    break;
                default:
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {


                                    recipeList.setVisibility(View.GONE);
                                    notFoundLabel.setText("Serverlərimizdə xəta baş verdi, biraz sonra yenidən təkrar edin :(");
                                    notFoundLabel.setVisibility(View.VISIBLE);
                                    //  Log.d("UI thread", "I am the UI thread");
                                }
                            });
                    ShowServerExceptionAlert(view);
                    break;


            }
            return list;


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);
            return list;

        }


    }

    public InstStruct LoadParameters(long value, final View view) {
        InstStruct obj = new InstStruct();

        try {


//            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
//                    MODE_PRIVATE);
//            String cypher1 = sharedPreferences.getString("cypher1", null);
//            String cypher2 = sharedPreferences.getString("cypher2", null);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<InstStruct> getUserData = api.LoadParameters(value);
            Response<InstStruct> response = getUserData.execute();
            obj = response.body();

            final LinearLayout recipeList = view.findViewById(R.id.recipeListLayout);
            final TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
            switch (response.code()) {

                case 200:

                    if (response.body() == null) {
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        recipeList.setVisibility(View.GONE);

                                        notFoundLabel.setVisibility(View.VISIBLE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });
                    }

                    break;
                default:
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {


                                    recipeList.setVisibility(View.GONE);
                                    notFoundLabel.setText("Serverlərimizdə xəta baş verdi, biraz sonra yenidən təkrar edin :(");
                                    notFoundLabel.setVisibility(View.VISIBLE);
                                    //  Log.d("UI thread", "I am the UI thread");
                                }
                            });
                    ShowServerExceptionAlert(view);
                    break;


            }
            return obj;


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);
            return obj;

        }


    }

    public List<XbtStruct> GetXbtFirstList(final View view) {
        List<XbtStruct> obj = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            String cypher1 = sharedPreferences.getString("cypher1", null);
            String cypher2 = sharedPreferences.getString("cypher2", null);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<XbtStruct>> getUserData = api.GetXbtFirstList(cypher1, cypher2);
            Response<List<XbtStruct>> response = getUserData.execute();
            obj = response.body();

            final LinearLayout xbtListLayout = view.findViewById(R.id.xbtListLayout);
            final TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
            switch (response.code()) {

                case 200:
                    refreshCypher(cypher1, cypher2, view);
                    if (obj.size() == 0) {

                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        xbtListLayout.setVisibility(View.GONE);

                                        notFoundLabel.setVisibility(View.VISIBLE);

                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });
                    }

                    break;
                default:
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {


                                    xbtListLayout.setVisibility(View.GONE);
                                    notFoundLabel.setText("Serverlərimizdə xəta baş verdi, biraz sonra yenidən təkrar edin :(");
                                    notFoundLabel.setVisibility(View.VISIBLE);
                                    //  Log.d("UI thread", "I am the UI thread");
                                }
                            });
                    ShowServerExceptionAlert(view);
                    break;


            }
            return obj;


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);
            return obj;

        }


    }

    public List<XbtStruct> GetXbtList(long id, final View view) {
        List<XbtStruct> obj = new ArrayList<>();

        try {


            final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("MySharedPref",
                    MODE_PRIVATE);
            String cypher1 = sharedPreferences.getString("cypher1", null);
            String cypher2 = sharedPreferences.getString("cypher2", null);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                    .build();

            //creating the api interface
            final ApiInterface api = retrofit.create(ApiInterface.class);


            Call<List<XbtStruct>> getUserData = api.GetXbtList(cypher1, cypher2, id);
            Response<List<XbtStruct>> response = getUserData.execute();
            obj = response.body();

            final LinearLayout xbtListLayout = view.findViewById(R.id.xbtListLayout);
            final TextView notFoundLabel = view.findViewById(R.id.notFoundLabel);
            switch (response.code()) {

                case 200:
                    refreshCypher(cypher1, cypher2, view);
                    if (obj.size() == 0) {
                        new Handler(Looper.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        xbtListLayout.setVisibility(View.GONE);

                                        notFoundLabel.setVisibility(View.VISIBLE);
                                        //  Log.d("UI thread", "I am the UI thread");
                                    }
                                });
                    }

                    break;
                default:
                    new Handler(Looper.getMainLooper()).post(
                            new Runnable() {
                                @Override
                                public void run() {


                                    xbtListLayout.setVisibility(View.GONE);
                                    notFoundLabel.setText("Serverlərimizdə xəta baş verdi, biraz sonra yenidən təkrar edin :(");
                                    notFoundLabel.setVisibility(View.VISIBLE);
                                    //  Log.d("UI thread", "I am the UI thread");
                                }
                            });
                    ShowServerExceptionAlert(view);
                    break;


            }
            return obj;


        } catch (Exception ex) {
            ShowInternetExceptionAlert(view);
            return obj;

        }


    }
}
