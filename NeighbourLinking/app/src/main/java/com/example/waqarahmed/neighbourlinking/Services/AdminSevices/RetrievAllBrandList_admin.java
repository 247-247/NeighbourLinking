package com.example.waqarahmed.neighbourlinking.Services.AdminSevices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.Brand;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
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

public class RetrievAllBrandList_admin extends AsyncTask<Void, Void,  ArrayList<Brand>> {
    Context context;
    ProgressDialog progress ;
    ArrayList<Brand> brandlist;


    public RetrievAllBrandList_admin(Context ctx){

        context = ctx;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        brandlist = new ArrayList<Brand>();
        if(AppStatus.getInstance(context).isOnline())
            progress.show();

          }

    @Override
    protected  ArrayList<Brand> doInBackground(Void... params) {
        String baseUrl = context.getResources().getString(R.string.baseUrl);
        String url_string = baseUrl+"/Neighbour/public/getAllBrands";

        if(AppStatus.getInstance(context).isOnline()) {
            progress.show();
            //http://localhost/ForJSONArray_push/index.php
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

                //------------

                String d;


                try {
                    JSONObject jsonRootObject = null;
                    try {

                        jsonRootObject = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray jsonArray = jsonRootObject.getJSONArray("Brands");
                    for (int i = jsonArray.length() - 1; i >= 0; i--) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Brand brand = new Brand();

                        brand.setId(jsonObject.getInt("id"));
                        brand.setName(jsonObject.getString("name"));
                        brand.setImage_url(jsonObject.getString("image_url"));
                        brand.setAddress(jsonObject.getString("address"));
                        brand.setFcm_id(jsonObject.getString("fcm_id"));
                        brand.setStatus(jsonObject.getString("status"));
                        brand.setContact(jsonObject.getString("contact"));
                        brand.setCurrentStatus(jsonObject.getString("currentStatus"));
                        brand.setEmail(jsonObject.getString("email"));
                        brand.setCreated_at(jsonObject.getString("created_at"));
                        brand.setUpdated_at(jsonObject.getString("updated_at"));
                        brand.setPassword(jsonObject.getString("password"));
                        brand.setIsAccountSetUp(jsonObject.getString("isAccountSetUp"));
                        brandlist.add(brand);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progress.dismiss();
                //-----------
                return brandlist;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{


        }
        if(AppStatus.getInstance(context).isOnline())
            progress.dismiss();

        return null;
    }



}
