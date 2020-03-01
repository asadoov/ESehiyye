package com.example.esehiyye.Model.Database;

import com.example.esehiyye.Model.CypherStruct;
import com.example.esehiyye.Model.UserStruct;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface DbSelectInterface {

    String BASE_URL = "https://eservice.e-health.gov.az";

    @GET("/iosmobileapplication/user/login")
    Call<List<CypherStruct>> getCypher(@Query("email") String email,@Query("pass") String pass);

    @GET("/iosmobileapplication/user/userinfo")
    Call<List<UserStruct>> getUserData(@Query("cypher1") String email,@Query("cypher2") String pass);



}

