package az.gov.e_health.esehiyye.Model.Database;

import java.util.List;

import az.gov.e_health.esehiyye.Model.CovidStruct;
import az.gov.e_health.esehiyye.Model.CypherStruct;
import az.gov.e_health.esehiyye.Model.DTT.DttDocImproveCourseStruct;
import az.gov.e_health.esehiyye.Model.DTT.DttNewEventStruct;
import az.gov.e_health.esehiyye.Model.DTT.DttStruct;
import az.gov.e_health.esehiyye.Model.DrugsStruct;
import az.gov.e_health.esehiyye.Model.FeedbackStatusStruct;
import az.gov.e_health.esehiyye.Model.FileStruct;
import az.gov.e_health.esehiyye.Model.FileUploadStatusStruct;

import az.gov.e_health.esehiyye.Model.Institutions.InstStruct;
import az.gov.e_health.esehiyye.Model.Institutions.InstTypeStruct;
import az.gov.e_health.esehiyye.Model.NewRecipeStruct;
import az.gov.e_health.esehiyye.Model.NewsStruct;
import az.gov.e_health.esehiyye.Model.RecipeDrugsStruct;
import az.gov.e_health.esehiyye.Model.RecipeStruct;
import az.gov.e_health.esehiyye.Model.StatusStruct;
import az.gov.e_health.esehiyye.Model.UserStruct;
import az.gov.e_health.esehiyye.Model.XbtStruct;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiInterface {

    String BASE_URL = "https://eservice.e-health.gov.az";

    String covidURL = "https://api.covid19api.com";

    @GET("/iosmobileapplication/user/login")
    Call<List<CypherStruct>> getSignInCypher(@Query("email") String email, @Query("pass") String pass);

    @GET("/iosmobileapplication/user/cypher")
    Call<List<CypherStruct>> getRefreshCypher(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);

    @GET("/iosmobileapplication/user/userinfo")
    Call<List<UserStruct>> getUserData(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);

    @GET("/iosmobileapplication/profil/passchange")
    Call<List<StatusStruct>> passChange(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("newpass") String newPass);

    @GET("/iosmobileapplication/profil/emailchange")
    Call<List<StatusStruct>> mailChange(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("newmail") String newmail);

    @GET("/iosmobileapplication/profil/mobilechange")
    Call<List<StatusStruct>> phoneChange(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("newmobile") String newmobile);

    @GET("/iosmobileapplication/news/newslist")
    Call<List<NewsStruct>> getNews(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);

    @GET("/iosmobileapplication/qeydiyyatsizxidmetler/dermanvasiteleri")
    Call<List<DrugsStruct>> getDrugs(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);

    @GET("/iosmobileapplication/feedback/insert")
    Call<List<FeedbackStatusStruct>> sendFeedback(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("text") String text);

    //------> DTT
    @GET("/iosmobileapplication/dtt/DTTTimeLineData")
    Call<List<DttStruct>> dttTimeline(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("fin") String fin);

    @GET("/iosmobileapplication/dtt/dttdelete")
    Call<List<FeedbackStatusStruct>> deleteCourse(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("id") int id, @Query("type") int type);

    @GET("/iosmobileapplication/dtt/konqreskonfranslist")
    Call<List<DttNewEventStruct>> getDttNewEvents(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("fin") String fin, @Query("year") int year);

    @GET("/iosmobileapplication/dtt/konqreskonfransinsert")
    Call<List<FeedbackStatusStruct>> dttKonqresKonfransInsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("fin") String fin, @Query("year") int year, @Query("data") String data);

    @GET("/iosmobileapplication/dtt/kursqrafik")
    Call<List<DttDocImproveCourseStruct>> getDttDocImproveCourseList(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("fin") String fin, @Query("year") int year);

    @GET("/iosmobileapplication/dtt/kursqrafikinsert")
    Call<List<FeedbackStatusStruct>> dttDocImproveInsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("fin") String fin, @Query("year") int year, @Query("data") String data);

    @GET("/iosmobileapplication/dtt/xaricinsert")
    Call<List<FeedbackStatusStruct>> dttXaricInsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("tesvir") String tesvir,
                                                    @Query("teshkil") String teshkil, @Query("bashlama_tarix") String bashlama_tarix, @Query("bitme_tarix") String bitme_tarix,
                                                    @Query("distant") int distant, @Query("novu") int typeID, @Query("kredit") int kredit,
                                                    @Query("year") int year);

    @GET("/iosmobileapplication/dtt/felsefeinsert")
    Call<List<FeedbackStatusStruct>> dttFelsefeInsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("novu") Integer typeID,
                                                      @Query("adi") String name,
                                                      @Query("year") int year);

    @GET("/iosmobileapplication/dtt/elmijurnalinsert")
    Call<List<FeedbackStatusStruct>> elmiJurnalInsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("novu") Integer typeID,
                                                      @Query("adi") String name, @Query("jurnal") String jurnal,
                                                      @Query("year") int year);

    @GET("/iosmobileapplication/dtt/tibbiyardiminsert")
    Call<List<FeedbackStatusStruct>> tibbiyardIminsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("tesvir") String tesvir,
                                                       @Query("tarix") String date,
                                                       @Query("year") int year);

    @GET("/iosmobileapplication/dtt/ixtisasinsert")
    Call<List<FeedbackStatusStruct>> ixtisasinsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("senednovu") Integer senednovu,
                                                   @Query("neshrinadi") String neshrinadi, @Query("seneddeishtirak") int seneddeishtirak,
                                                   @Query("tarix") String tarix, @Query("kredit") int kredit,
                                                   @Query("year") int year);

    @GET("/iosmobileapplication/dtt/tibbitehsilinsert")
    Call<List<FeedbackStatusStruct>> tibbitehsilinsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2,
                                                       @Query("saat") int saat, @Query("fen") String fen,
                                                       @Query("year") int year);

    @GET("/iosmobileapplication/dtt/kurasiyainsert")
    Call<List<FeedbackStatusStruct>> kurasiyainsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2,
                                                    @Query("baza") String baza, @Query("novu") int novu, @Query("say") int say, @Query("kredit") int kredit,
                                                    @Query("year") int year);

    @GET("/iosmobileapplication/dtt/trenerinsert")
    Call<List<FeedbackStatusStruct>> trenerinsert(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2,
                                                  @Query("saat") int saat, @Query("fen") String fen,
                                                  @Query("year") int year);

    @GET("/iosmobileapplication/dtt/dttsend")
    Call<List<FeedbackStatusStruct>> dttsend(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);

    //------->


    @GET("/iosmobileapplication/document/doclist")
    Call<List<FileStruct>> GetFileList(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("category") long catID);


    @POST("/iosmobileapplication/document/upload")
    Call<List<FileUploadStatusStruct>> UploadFile(@Body RequestBody file);

    @GET("/iosmobileapplication/resept/reseptlist")
    Call<List<RecipeStruct>> GetRecipeList(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);

    @GET("/EXidmetler/Institution/LoadTypeList")
    Call<List<InstTypeStruct>> GetInstTypeList(@Query("region") long region, @Query("typeid") long typeId);

    @GET("/EXidmetler/Institution/LoadInstList")
    Call<List<InstStruct>> GetInstList(@Query("region") long region, @Query("typeid") long typeId);

    @GET("/EXidmetler/Institution/LoadParameters")
    Call<InstStruct> LoadParameters(@Query("value") long value);

    @GET("/iosmobileapplication/xbt/xbtfirstlist")
    Call<List<XbtStruct>> GetXbtFirstList(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2);

    @GET("/iosmobileapplication/xbt/xbtlist")
    Call<List<XbtStruct>> GetXbtList(@Query("cypher1") String cypher1, @Query("cypher2") String cypher2, @Query("id") long id);
    @GET("/live/country/Azerbaijan")
    Call<List<CovidStruct>> GetCovidLive();
    @GET("/iosmobileapplication/user/asanlogin")
    Call<List<CypherStruct>> loginWithEgov(@Query("jwt") String token);
    @GET("/api/rest/Patient.svc/GetRecipeList")
    Call<NewRecipeStruct> getNewRecipes(@Query("accessToken") String accessToken, @Query("refreshToken") String refreshToken);
    @GET("/api/rest/Patient.svc/GetDrugListbyRecipeId")
    Call<RecipeDrugsStruct> GetDrugListbyRecipeId(@Query("accessToken") String accessToken, @Query("refreshToken") String refreshToken, @Query("recipeId") int recipeId);

}

