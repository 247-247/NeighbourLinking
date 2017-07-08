package com.example.waqarahmed.neighbourlinking.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsyncResponse;
import com.example.waqarahmed.neighbourlinking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * Created by Waqar ahmed on 5/25/2017.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    ProgressDialog progress ;


    public AsyncResponse asyncResponse=null;
    public  BackgroundWorker(Context ctx){

        context = ctx;

    }

    @Override
    protected String doInBackground(String... params) {
        String typ = params[0];
        if (AppStatus.getInstance(context).isOnline()) {
            progress.show();
            String baseUrl = context.getResources().getString(R.string.baseUrl);
            String url_string = baseUrl+"/Neighbour/public/AllNotificationList";
            if (typ.equals("login")) {   //http://localhost/ForJSONArray_push/index.php

                try {
                    Log.i("TAG", "doInBackground: 1 ");

                    URL url = null;
                    try {
                        url = new URL(url_string);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Log.i("TAG", "doInBackground: 2 ");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    Log.i("TAG", "doInBackground: 3 ");
                    httpURLConnection.setRequestMethod("GET");
                    Log.i("TAG", "doInBackground: 4 ");
                    httpURLConnection.setDoInput(true);

                    Log.i("TAG", "doInBackground: 6 ");

                    Log.i("TAG", "doInBackground: 7 ");


                    InputStream inputStream = httpURLConnection.getInputStream();


                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                    String result = " ";
                    String line = " ";
                    String TAG = "Tag";
                    Log.i(TAG, "doInBackground: 8 ");
                    while ((line = bufferedReader.readLine()) != null) {

                        result += line;
                        Log.i(TAG, "doInBackground: 9 ");

                    }


                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }




        Log.i("TAG", "doInBackground: 10 ");
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (AppStatus.getInstance(context).isOnline()) {
            if (progress == null) {
                progress = AppUtils.createProgressDialog(context);
                progress.show();
            } else {
                progress.show();
            }

        }
    }

    @Override
    protected void onPostExecute(String aVoid) {


        super.onPostExecute(aVoid);
        if(progress != null && progress.isShowing())
            progress.dismiss();
        if(aVoid != null) {

           asyncResponse.processFinish(aVoid);  // asyncResponse interface call here

        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
