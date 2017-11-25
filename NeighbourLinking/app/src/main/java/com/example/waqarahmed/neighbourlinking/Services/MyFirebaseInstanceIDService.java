package com.example.waqarahmed.neighbourlinking.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by waqas on 3/3/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String token;
    FirebaseAuth mAuth;
    SimpleDateFormat dateFormat;
    String currentDateTime ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "fcmToken";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        token= FirebaseInstanceId.getInstance().getToken();
        mAuth = FirebaseAuth.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = dateFormat.format(new Date());
        storeTokenOnPrefrences(token);



    }


    @Override
    public void onCreate() {
        super.onCreate();
    }



    public void storeTokenOnPrefrences(String tok){

        SharedPreferences appPrefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log.d("Firebase", "New Token Created" + token);
        SharedPreferences.Editor notificationPrefsEditor = appPrefs.edit();
        notificationPrefsEditor.putString(Name,tok);
        notificationPrefsEditor.commit();
//        String v = appPrefs.getString(Name,"AnyVelue");
//        Log.i("RRRRRRR store retrieved", "storeTokenOnPrefrences: "+v);

    }


    /*
      try {
        Log.i("RRRRRRRRRRRGGRRRRRRRR", "registerToken: "+token);
        Log.i("RRRRRRRRRRRGGRRRRRRRR", "registerToken: "+currentDateTime);
        Log.i("TAG", "doInBackground: 1 ");
        String url_string ="http://20adaff5.ngrok.io/Neighbour/public/DeviceRegistration";
        URL url = new URL(url_string);
        Log.i("TAG", "doInBackground: 2 ");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        Log.i("TAG", "doInBackground: 3 ");
        httpURLConnection.setRequestMethod("POST");
        Log.i("TAG", "doInBackground: 4 ");
        httpURLConnection.setDoOutput(true);

        httpURLConnection.setDoInput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        Log.i("TAG", "doInBackground: 5 ");
        BufferedWriter bufferedWriter  = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
        String post_data = URLEncoder.encode("device_token","UTF-8")+"="+URLEncoder.encode(token,"UTF-8")+"&"+
                URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode("w@gmail.com","UTF-8")
                +"&"+
                URLEncoder.encode("created_at","UTF-8")+"="+URLEncoder.encode(currentDateTime,"UTF-8")
                +"&"+
                URLEncoder.encode("updated_at","UTF-8")+"="+URLEncoder.encode(currentDateTime,"UTF-8");
        Log.i("TAG", "doInBackground: 6 ");
        bufferedWriter.write(post_data);
        Log.i("TAG", "doInBackground: 7 ");
        bufferedWriter.flush();
        bufferedWriter.close();
        outputStream.close();
        InputStream inputStream = httpURLConnection.getInputStream();
//
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
//
//            String result=" ";
//            String line =" ";
//            String TAG = "Tag";
//            Log.i(TAG, "doInBackground: 8 ");
//            while( (line = bufferedReader.readLine()) != null){
//
//                result += line;
//                Log.i(TAG, "doInBackground: 9 ");
//
//            }
        // return result;
    } catch (MalformedURLException e) {
        e.printStackTrace();
        Log.i("TAGTTTTTTT", "doInBackground: 9 ");
    } catch (IOException e) {
        e.printStackTrace();
        Log.i("TAGTTTTTTT", "doInBackground: 9 ");
    }*/


}
