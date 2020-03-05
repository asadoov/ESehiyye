package az.gov.e_health.esehiyye.Model.Database;

import az.gov.e_health.esehiyye.Model.CypherStruct;
import az.gov.e_health.esehiyye.Model.DrugsStruct;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.Model.NewsStruct;
import az.gov.e_health.esehiyye.Model.StatusStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {

    String BASE_URL = "https://eservice.e-health.gov.az";

    @GET("/iosmobileapplication/user/login")
    Call<List<CypherStruct>> getSignInCypher(@Query("email") String email, @Query("pass") String pass);
    @GET("/iosmobileapplication/user/cypher")
    Call<List<CypherStruct>> getRefreshCypher(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);
    @GET("/iosmobileapplication/user/userinfo")
    Call<List<UserStruct>> getUserData(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);
    @GET("/iosmobileapplication/profil/passchange")
    Call<List<StatusStruct>> passChange(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("newpass") String newPass);
    @GET("/iosmobileapplication/profil/emailchange")
    Call<List<StatusStruct>> mailChange(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2,@Query("newmail") String newmail);
    @GET("/iosmobileapplication/profil/mobilechange")
    Call<List<StatusStruct>> phoneChange(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2,@Query("newmobile") String newmobile);
    @GET("/iosmobileapplication/news/newslist")
    Call<List<NewsStruct>> getNews(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);
    @GET("/iosmobileapplication/qeydiyyatsizxidmetler/dermanvasiteleri")
    Call<List<DrugsStruct>> getDrugs(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);
    @GET("/iosmobileapplication/feedback/insert")
    Call<List<FeedbackStatusStruct>> sendFeedback(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("text") String text);




}

