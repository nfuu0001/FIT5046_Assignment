package com.example.fit5046_assignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import android.content.Context;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {
    private OkHttpClient client = null;
    private String results;
    private String credentialId;
    SharedPreferences sp;

    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    public NetworkConnection() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL =
            "http://192.168.108.1:8080/MovieMemoir/webresources/";

    public String getCredential(String username, String passwordHash) {
        final String methodPath = "moviememoir.credential/findByPasswordHashAndUsername/" + username + "/" + encryption(passwordHash);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            /*

            JSONObject jsonObj = new JSONObject(results);
            int credentialId = jsonObj.getInt("credentialId");
            SharedPreferences sp = getSharedPreferences("db",0);
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(results);

        return results;
    }

    public String getAllCinemaAddress() {
        final String methodPath = "moviememoir.cinema/findAllCinemaAddress";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        System.out.println(BASE_URL + methodPath);

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }



    // get first name
    public String getFirstName(int id){
        String firstName = "";
        final String methodPath = "moviememoir.person/findByCredentialId/" + id;
        Request.Builder builder = new Request.Builder();
        System.out.println(BASE_URL + methodPath);
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();


        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(results);
        return results;

    }


    // MD5
    public String encryption(String str) {
        MessageDigest md5 =null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return "error";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray =new byte[charArray.length];
        for (int i =0; i < charArray.length; i++){
            byteArray[i] = (byte) charArray[i];
        }
        byte[] digest = md5.digest(byteArray);
        StringBuilder sb =new StringBuilder();
        for (int i =0; i < digest.length; i++) {
            int var = digest[i] &0xff;
            if (var <16)
                sb.append("0");
            sb.append(Integer.toHexString(var));
        }
        return sb.toString();
    }

    // find all the usernames
    public String findAllUsernames() {
        final String methodPath = "moviememoir.credential/findAllUsername";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();


        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("all username --------------");
        //System.out.println(results);
        return results;
    }


    // Find max person id
    public int findMaxPersonId() {
        int maxPersonId = 0;
        final String methodPath = "moviememoir.person/findMaxPersonId";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(results);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(results);
            maxPersonId = jsonObj.getInt("personId");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maxPersonId;
    }

   // add new credential
   public String addCredential(Credential credential) {
       Gson gson = new Gson();
       String credentialJson = gson.toJson(credential);
       String strResponse = "";
       Log.i("json " , credentialJson);

       final String methodPath = "moviememoir.credential/";
       RequestBody body = RequestBody.create(credentialJson, JSON);
       Request request = new Request.Builder()
               .url(BASE_URL + methodPath)
               .post(body)
               .build();
       try {
           Response response= client.newCall(request).execute();
           strResponse= response.body().string();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return strResponse;
   }

    // add new person
    public String addPerson(Person person) {
        Gson gson = new Gson();
        String personJson = gson.toJson(person);
        String strResponse = "";
        Log.i("json " , personJson);

        final String methodPath = "moviememoir.person/";
        RequestBody body = RequestBody.create(personJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }



    // get top 5 movie
    public String getTopFive(int id){

        final String methodPath = "moviememoir.memoir/topFiveRatingScore/" + id;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public String movieSearchApi(String input){
        String API_KEY = "c95a86a330bcfa114a08107c010afc44";
        input = input.trim().replace(" ","%20");
        final String path = "https://api.themoviedb.org/3/search/movie?api_key="+ API_KEY + "&language=en-US&query=" + input + "&page=1&include_adult=false";
        Request.Builder builder = new Request.Builder();

        builder.url(path);
        Request request = builder.build();
        System.out.println(path);
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public Bitmap moviePoster(String posterUrl){
        Bitmap bitmap = null;
        final String path = "https://image.tmdb.org/t/p/w600_and_h900_bestv2" + posterUrl;
        try{
            URL url = new URL(path);
            InputStream inputStream = url.openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public String getMovieInfo(String input){
        String API_KEY = "c95a86a330bcfa114a08107c010afc44";
        final String path = "https://api.themoviedb.org/3/movie/" + input + "?api_key="+ API_KEY + "&language=en-US";

        Request.Builder builder = new Request.Builder();
        builder.url(path);
        Request request = builder.build();
        System.out.println(path);
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public String getPosterUrl(String movieId){
        String API_KEY = "c95a86a330bcfa114a08107c010afc44";
        final String path = "https://api.themoviedb.org/3/movie/" + movieId + "/images?api_key="+ API_KEY + "&language=en-US";
        Request.Builder builder = new Request.Builder();
        builder.url(path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }


    public String getMovieMoreInfo(String input){
        String API_KEY = "c95a86a330bcfa114a08107c010afc44";
        final String path = "https://api.themoviedb.org/3/movie/" + input + "/credits?api_key="+ API_KEY;
        System.out.println(path);
        Request.Builder builder = new Request.Builder();
        builder.url(path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public String moviesPerPostcode(String id, String start, String end){
        final String methodPath = "moviememoir.memoir/totalMoviesPerPostcode/" + id + "/" + start + "/" + end;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String moviesPerMonth(String id,String year){
        final String methodPath = "moviememoir.memoir/totalMoviesPerMonth/" + id + "/" + year;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }


    public String getAllCinemas(){
        final String methodPath = "moviememoir.cinema/findAllCinemas";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String getMaxCinemaId(){
        final String methodPath = "moviememoir.cinema/findMaxCinemaId";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String getMaxMemoirId(){
        final String methodPath = "moviememoir.memoir/findMaxMemoirId";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String addCinema(Cinema cinema) {
        Gson gson = new Gson();
        String cinemaJson = gson.toJson(cinema);
        String strResponse = "";
        Log.i("json " , cinemaJson);

        final String methodPath = "moviememoir.cinema/";
        RequestBody body = RequestBody.create(cinemaJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    public String addMemoir(Memoir memoir) {
        Gson gson = new Gson();
        String memoirJson = gson.toJson(memoir);
        String strResponse = "";
        Log.i("json " , memoirJson);

        final String methodPath = "moviememoir.memoir/";
        RequestBody body = RequestBody.create(memoirJson, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    public String findCinemaByName(String name){
        final String methodPath = "moviememoir.cinema/findByCinemaName/" + name;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String findPersonById(String personId){
        final String methodPath = "moviememoir.person/findByPersonId/" + personId;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }





}
