package com.example.waqarahmed.neighbourlinking.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.waqarahmed.neighbourlinking.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by waqas on 3/4/2017.
 */

public class DeviceRegistrationRelated extends AsyncTask<Void,Void,Void>
{
    Context context;
    String flag;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "fcmToken";

    String token;
    FirebaseAuth mAuth;
    SimpleDateFormat dateFormat;
    String currentDateTime ;

   public DeviceRegistrationRelated(Context cxt ){
       context = cxt;


    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mAuth = FirebaseAuth.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = dateFormat.format(new Date());
    }

    @Override
    protected Void doInBackground(Void... voids)
    {

        SharedPreferences appPrefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       // Log.d("Firebase", "New Token Created" + token);
        token = appPrefs.getString(Name,"AnyVelue");
        registerToken(token);
        Log.i("RRRRRRR Background", "storeTokenOnPrefrences: "+token);


//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder().add("Mes",text).build();
//        Request request = new Request.Builder().url("http://mywebsit.comli.com/pushMessage.php").post(body).build();

//        try {
//            client.newCall(request).execute();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }
    private void registerToken(String token) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("device_token",token).add("email",mAuth.getCurrentUser().getEmail()).add("created_at",currentDateTime).add("updated_at",currentDateTime).build();
        Log.i("RRRRRRRRRRRGGRRRRRRRR", "registerToken: "+token);
        Log.i("RRRRRRRRRRRGGRRRRRRRR", "registerToken: "+currentDateTime);
        String baseUrl = context.getResources().getString(R.string.baseUrl);
        Request request = new Request.Builder().url(baseUrl+"/Neighbour/public/DeviceRegistration").post(body).build();

        try {
            okhttp3.Response response= client.newCall(request).execute();
            Log.i("RRRRRRR Response", "registerToken: new CALLL "+request.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("LLLLLLLLLLLLLLLLLLLL", "registerToken: new CALLL ");
        }
    }
}
