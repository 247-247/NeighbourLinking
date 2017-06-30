package com.example.waqarahmed.neighbourlinking.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.example.waqarahmed.neighbourlinking.Classes.ServicesTypes;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsynResonseForMenPowerList;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsynResonseForServices;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Waqar ahmed on 6/1/2017.
 */

public class RetrievAllMenPowerList extends AsyncTask<String, Void,  ArrayList<ServiceMan>> {
    Context context;
    ProgressDialog progress ;
    ArrayList<ServiceMan> manlist;
    public AsynResonseForMenPowerList asyncResponse=null;

    public RetrievAllMenPowerList(Context ctx){

        context = ctx;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        manlist = new ArrayList<ServiceMan>();

          }

    @Override
    protected  ArrayList<ServiceMan> doInBackground(String... params) {
        String typ = params[0];
        String skillType = params[1];
        String url_string = "http://337cdc80.ngrok.io/Neighbour/public/getEmployeeBasesOnSkill";
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
                String post_data = URLEncoder.encode("skill","UTF-8")+"="+URLEncoder.encode(skillType,"UTF-8");
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
                    JSONArray jsonArray = jsonRootObject.getJSONArray("EmployeeBaseOnSkill");
                    for (int i=jsonArray.length()-1; i>=0; i--){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ServiceMan man = new ServiceMan();

                        man.setId(jsonObject.getInt("id"));
                        man.setEmail(jsonObject.getString("email"));
                        man.setImage_url(jsonObject.getString("image_url"));
                        man.setName(jsonObject.getString("name"));
                        man.setSkill(jsonObject.getString("skill"));
                        man.setContact(jsonObject.getString("contact"));




                        manlist.add(man);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }


                //-----------
                return manlist;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute( ArrayList<ServiceMan> s) {
        super.onPostExecute(s);


        if(!s.isEmpty()) {
            progress.dismiss();
            asyncResponse.processFinish(s);


        }

        else
        {
            progress.dismiss();
    }
    }


}
