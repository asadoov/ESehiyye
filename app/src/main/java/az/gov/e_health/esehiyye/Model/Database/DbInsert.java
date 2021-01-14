package az.gov.e_health.esehiyye.Model.Database;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.Model.FileUploadStatusStruct;
import az.gov.e_health.esehiyye.Model.StatusStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

                List<UserStruct> userList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));
                userList.get(0).EMAIL = newEmail;
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

                List<UserStruct> userList = Arrays.asList(gson.fromJson(jsonUserData, UserStruct[].class));
                userList.get(0).MOBILE = newPhone;
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

    public List<FeedbackStatusStruct> deleteCourse(String cypher1, String cypher2, int id, int type, final View view) {
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


            Call<List<FeedbackStatusStruct>> deleteCourse = api.deleteCourse(cypher1, cypher2, id, type);
            Response<List<FeedbackStatusStruct>> response = deleteCourse.execute();
            statusList = response.body();


            select.refreshCypher(cypher1, cypher2, view);


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> dttKonqresKonfransInsert(String fin, int year, String data, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insertKonqresKonfrans = api.dttKonqresKonfransInsert(cypher1, cypher2, fin, year, data);
            Response<List<FeedbackStatusStruct>> response = insertKonqresKonfrans.execute();
            statusList = response.body();


            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> dttDocImproveCourseInsert(String fin, int year, String data, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insertDocImprove = api.dttDocImproveInsert(cypher1, cypher2, fin, year, data);
            Response<List<FeedbackStatusStruct>> response = insertDocImprove.execute();
            statusList = response.body();


            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> dttXaricInsert(String tesvir, String teshkil, String bashlama_tarix, String bitme_tarix, int distant, int novu, int kredit, int year, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insertXaric = api.dttXaricInsert(cypher1, cypher2, tesvir, teshkil, bashlama_tarix, bitme_tarix, distant, novu, kredit, year);
            Response<List<FeedbackStatusStruct>> response = insertXaric.execute();
            statusList = response.body();


            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> dttFelsefeInsert(Integer typeID, String name, int year, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insertFelsefe = api.dttFelsefeInsert(cypher1, cypher2, typeID, name, year);
            Response<List<FeedbackStatusStruct>> response = insertFelsefe.execute();
            statusList = response.body();


            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> elmiJurnalInsert(Integer typeID, String jurnal, String name, int year, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insert = api.elmiJurnalInsert(cypher1, cypher2, typeID, name, jurnal, year);
            Response<List<FeedbackStatusStruct>> response = insert.execute();
            statusList = response.body();


            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> tibbiyardimInsert(String tesvir, String date, int year, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insert = api.tibbiyardIminsert(cypher1, cypher2, tesvir, date, year);
            Response<List<FeedbackStatusStruct>> response = insert.execute();
            statusList = response.body();


            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> ixtisasinsert(Integer senednovu, String neshrinadi, int seneddeishtirak, String tarix, int kredit, int year, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insertXaric = api.ixtisasinsert(cypher1, cypher2, senednovu, neshrinadi, seneddeishtirak, tarix, kredit, year);
            Response<List<FeedbackStatusStruct>> response = insertXaric.execute();
            statusList = response.body();


            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> tibbitehsilinsert(int saat, String fen, int year, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insertXaric = api.tibbitehsilinsert(cypher1, cypher2, saat, fen, year);
            Response<List<FeedbackStatusStruct>> response = insertXaric.execute();
            statusList = response.body();
            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> kurasiyainsert(int year, String baza, int novu, int say, int kredit, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insertXaric = api.kurasiyainsert(cypher1, cypher2, baza, novu, say, kredit, year);
            Response<List<FeedbackStatusStruct>> response = insertXaric.execute();
            statusList = response.body();
            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> trenerinsert(int saat, String fen, int year, final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insertXaric = api.trenerinsert(cypher1, cypher2, saat, fen, year);
            Response<List<FeedbackStatusStruct>> response = insertXaric.execute();
            statusList = response.body();
            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FeedbackStatusStruct> dttSend(final View view) {
        List<FeedbackStatusStruct> statusList = new ArrayList<>();

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


            Call<List<FeedbackStatusStruct>> insertXaric = api.dttsend(cypher1, cypher2);
            Response<List<FeedbackStatusStruct>> response = insertXaric.execute();
            statusList = response.body();
            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {

            return statusList;

        }


    }

    public List<FileUploadStatusStruct> FileUpload(int typeID, String filename, Bitmap bmp, final View view) {
        List<FileUploadStatusStruct> statusList = new ArrayList<>();

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

            //MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", data.getName(), RequestBody.create(MediaType.parse("image/*"), data));


            //   RequestBody fbody = RequestBody.create(MediaType.parse("image/*"),data);
            // String mypath = Uri.parse(photo.getData().getPath()).toString();

//            if(mypath.contains("document/raw:")){
//                mypath = mypath.replace("/document/raw:","");
//            }
//            File file = new File(mypath);
//            file.createNewFile();


            // File file = new File(ph)
//            MultipartBody.Part c1  = MultipartBody.Part.createFormData("cypher1", cypher1);
//            MultipartBody.Part c2 = MultipartBody.Part.createFormData("cypher2", cypher2);
//            MultipartBody.Part type = MultipartBody.Part.createFormData("type", String.valueOf(typeID));
//            MultipartBody.Part filename_p = MultipartBody.Part.createFormData("filename", filename);
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photo);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("cypher1", cypher1)
                    .addFormDataPart("cypher2", cypher2)
                    .addFormDataPart("type", String.valueOf(typeID))
                    .addFormDataPart("filename", filename)
                    .addFormDataPart("file", "file.jpeg", RequestBody.create(MediaType.parse("image/*"), bos.toByteArray()));
            RequestBody requestBody = builder.build();


            Call<List<FileUploadStatusStruct>> uploadFile = api.UploadFile(requestBody);
            Response<List<FileUploadStatusStruct>> response = uploadFile.execute();
            statusList = response.body();

            if (response.isSuccessful()) {
                select.refreshCypher(cypher1, cypher2, view);
            } else {
                select.ShowServerExceptionAlert(view);
            }


            return statusList;


        } catch (Exception ex) {
            select.ShowInternetExceptionAlert(view);
            return statusList;

        }


    }

    private File savebitmap(String filename) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(filename + ".jpg");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename + ".jpg");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            // make a new bitmap from your file
            Bitmap bitmap = BitmapFactory.decodeFile(file.getName());

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }
//    private File savebitmap(Bitmap bmp) {
//        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
//        OutputStream outStream = null;
//        // String temp = null;
//        File file = new File(extStorageDirectory, "temp.png");
//        if (file.exists()) {
//            file.delete();
//            file = new File(extStorageDirectory, "temp.png");
//
//        }
//
//        try {
//            outStream = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return file;
//    }

}
