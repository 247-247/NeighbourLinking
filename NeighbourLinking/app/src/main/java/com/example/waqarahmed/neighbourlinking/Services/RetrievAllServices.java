package com.example.waqarahmed.neighbourlinking.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.storage.OnObbStateChangeListener;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.waqarahmed.neighbourlinking.Classes.ServicesTypes;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsynResonseForServices;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsyncResponse;
import com.example.waqarahmed.neighbourlinking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Waqar ahmed on 6/1/2017.
 */

public class RetrievAllServices extends AsyncTask<String, Void,  ArrayList<ServicesTypes>> {
    Context context;
    ProgressDialog progress ;
    ArrayList<ServicesTypes> Serviceslist;
    public AsynResonseForServices asyncResponse=null;

    public  RetrievAllServices(Context ctx){

        context = ctx;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setTitle("Loading...");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        Serviceslist = new ArrayList<ServicesTypes>();

          }

    @Override
    protected  ArrayList<ServicesTypes> doInBackground(String... params) {
        String typ = params[0];
        String url_string ="http://41196a3f.ngrok.io/Neighbour/public/AllService";
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
                httpURLConnection.setDoInput(true);
                Log.i("TAG", "doInBackground: 6 ");
                Log.i("TAG", "doInBackground: 7 ");
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
                    JSONArray jsonArray = jsonRootObject.getJSONArray("AllServieses");
                    for (int i=jsonArray.length()-1; i>=0; i--){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ServicesTypes service = new ServicesTypes();
                        service.setId(jsonObject.getInt("id"));
                        service.setSkill(jsonObject.getString("Skill"));
                        service.setImage_url(jsonObject.getString("image_url"));

                        d =jsonObject.getString("created_at");
                        service.setCreated_at(d.substring(0,d.indexOf(' ')));
                        service.setUpdated_at(jsonObject.getString("updated_at"));

                        Serviceslist.add(service);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }


                //-----------
                return Serviceslist;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute( ArrayList<ServicesTypes> s) {
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
