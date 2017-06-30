package com.example.waqarahmed.neighbourlinking.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

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
    AlertDialog alertDialog ;

    public AsyncResponse asyncResponse=null;
    public  BackgroundWorker(Context ctx){

        context = ctx;

    }
    @Override
    protected String doInBackground(String... params) {
        String typ = params[0];
        String url_string = "http://41196a3f.ngrok.io/Neighbour/public/AllNotificationList";
        if(typ.equals("login")){   //http://localhost/ForJSONArray_push/index.php

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
              //  httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
               // OutputStream outputStream = httpURLConnection.getOutputStream();
              //  Log.i("TAG", "doInBackground: 5 ");
             //   BufferedWriter bufferedWriter  = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
//                String post_data = URLEncoder.encode("nam","UTF-8")+"="+URLEncoder.encode(nam,"UTF-8")+"&"+
//                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                Log.i("TAG", "doInBackground: 6 ");
              //  bufferedWriter.write(post_data);
                Log.i("TAG", "doInBackground: 7 ");
            //    bufferedWriter.flush();
            //    bufferedWriter.close();
              //  outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
               // BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream ));
                String result=" ";
                String line =" ";
                String TAG = "Tag";
                Log.i(TAG, "doInBackground: 8 ");
                while( (line = bufferedReader.readLine()) != null){

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




        Log.i("TAG", "doInBackground: 10 ");
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("OutPut");

    }

    @Override
    protected void onPostExecute(String aVoid) {


        super.onPostExecute(aVoid);
        if(aVoid != null) {
            //  alertDialog.setMessage(aVoid);
            //  alertDialog.show();
//
           asyncResponse.processFinish(aVoid);  // asyncResponse interface call here
//
//            try {
//                Log.i("TAG", "Post try ");
//                JSONObject jsonRootObject = null;
//                try {
//                    jsonRootObject = new JSONObject(aVoid);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Log.i("TAG", "Pot After ");
//
//                JSONArray jsonArray = jsonRootObject.getJSONArray("AllNotification");
//
//
//                JSONObject jsonObject = jsonArray.getJSONObject(1);
//                Toast.makeText(context,jsonObject.getString("email")+" "+jsonObject.getInt("id") ,Toast.LENGTH_LONG).show();
//
//            }
//            catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
        }
        else
        {
            alertDialog.setMessage("Pass Null here");
            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
