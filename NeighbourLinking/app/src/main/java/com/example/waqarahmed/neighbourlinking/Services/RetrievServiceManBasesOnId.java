package com.example.waqarahmed.neighbourlinking.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.ServiceManProfile;
import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsynResonseForMenPowerList;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsynResonseForServiceManProfile;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Waqar ahmed on 6/1/2017.
 */

public class RetrievServiceManBasesOnId extends AsyncTask<String, Void,  ServiceMan> {
    Context context;
    ProgressDialog progress ;
    ServiceMan man;
    public AsynResonseForServiceManProfile asyncResponse=null;

    public RetrievServiceManBasesOnId(Context ctx){

        context = ctx;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        if (AppStatus.getInstance(context).isOnline())
        progress.show();
        man = new ServiceMan();

          }

    @Override
    protected  ServiceMan doInBackground(String... params) {
        String typ = params[0];
        String id = params[1];
      //  Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
        if (AppStatus.getInstance(context).isOnline())
        {
        String url_string = "http://337cdc80.ngrok.io/Neighbour/public/getEmployeeBasesOnId";
        if(typ.equals("login")){   //http://localhost/ForJSONArray_push/index.php
            try {
                Log.i("TAG", "doInBackground:  "+id);
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
                String post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(String.valueOf(id),"UTF-8");
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

                String d;


                try {
                    JSONObject jsonRootObject = null;
                    try {

                        jsonRootObject = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = jsonRootObject.getJSONObject("EmployeeBaseOnId");
                    Log.i(TAG, "doInBackground: 111"+ jsonObject.getString("name"));

                        man.setId(jsonObject.getInt("id"));
                       man.setEmail(jsonObject.getString("email"));
                       man.setImage_url(jsonObject.getString("image_url"));
                       man.setName(jsonObject.getString("name"));
                       man.setSkill(jsonObject.getString("skill"));
                        man.setContact(jsonObject.getString("contact"));
                    man.setStatus(jsonObject.getString("status"));





                   // }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }


                //-----------
                return man;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }else{
        return null;

    }
        return null;
    }

    @Override
    protected void onPostExecute(ServiceMan s) {
        super.onPostExecute(s);

            progress.dismiss();
            asyncResponse.processFinish(s);








    }


}
