package com.example.waqarahmed.neighbourlinking.Services.ServiceManServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.waqarahmed.neighbourlinking.Classes.ServiceRequest;
import com.example.waqarahmed.neighbourlinking.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Waqar ahmed on 6/1/2017.
 */

public class AccepRequestService extends AsyncTask<String, Void,  String> {
    Context context;


    public AccepRequestService(Context ctx){

        context = ctx;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

          }

    @Override
    protected  String doInBackground(String... params) {
        String typ = params[0];
        String id = params[1];
        String baseUrl = context.getResources().getString(R.string.baseUrl);
        String url_string = baseUrl+"/Neighbour/public/setAcceptRequest";
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
                httpURLConnection.setRequestMethod("POST");
                Log.i("TAG", "doInBackground: 4 ");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                 OutputStream outputStream = httpURLConnection.getOutputStream();
                  Log.i("TAG", "doInBackground: 5 ");
                   BufferedWriter bufferedWriter  = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                  bufferedWriter.write(post_data);
                Log.i("TAG", "doInBackground: 7 ");
                   bufferedWriter.flush();
                    bufferedWriter.close();
                  outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result=" ";
                String line =" ";
                String TAG = "Tag";
                Log.i(TAG, "doInBackground: 8 ");
                while( (line = bufferedReader.readLine()) != null){

                    result += line;
                    Log.i(TAG, "doInBackground: 9 ");

                }

                //------------


                return null;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute( String s) {
        super.onPostExecute(s);




        }


    }



