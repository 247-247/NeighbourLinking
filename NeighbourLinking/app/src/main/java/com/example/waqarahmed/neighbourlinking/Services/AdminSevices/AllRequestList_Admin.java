package com.example.waqarahmed.neighbourlinking.Services.AdminSevices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceRequest;
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

public class AllRequestList_Admin extends AsyncTask<String, Void,  ArrayList<ServiceRequest>> {
    Context context;
    ProgressDialog progress ;
    ArrayList<ServiceRequest> manlist;
   // public AsyncResponseForRequest asyncResponse=null;

    public AllRequestList_Admin(Context ctx){

        context = ctx;

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
        manlist = new ArrayList<ServiceRequest>();

          }

    @Override
    protected  ArrayList<ServiceRequest> doInBackground(String... params) {
        String typ = params[0];
       // String id = params[1];
        if (AppStatus.getInstance(context).isOnline()) {
            String baseUrl = context.getResources().getString(R.string.baseUrl);
            String url_string = baseUrl+"/Neighbour/public/getAllRequests";
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
                  //  httpURLConnection.setRequestMethod("GET");
                    Log.i("TAG", "doInBackground: 4 ");
                    httpURLConnection.setDoInput(true);
                  //  httpURLConnection.setDoOutput(true);
                 //   OutputStream outputStream = httpURLConnection.getOutputStream();
                    Log.i("TAG", "doInBackground: 5 ");
                  //  BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                   // String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                   // bufferedWriter.write(post_data);
                    Log.i("TAG", "doInBackground: 7 ");
                  //  bufferedWriter.flush();
                  //  bufferedWriter.close();
                   // outputStream.close();

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
                        JSONArray jsonArray = jsonRootObject.getJSONArray("request");
                        for (int i = jsonArray.length() - 1; i >= 0; i--) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ServiceRequest r = new ServiceRequest();

                            r.setId(jsonObject.getInt("id"));
                            r.setType(jsonObject.getString("type"));
                            r.setOwner_house_address(jsonObject.getString("owner_house_address"));
                            r.setOwner_contact(jsonObject.getString("owner_contact"));
                            r.setOwner_name(jsonObject.getString("owner_name"));
                            r.setStatus(jsonObject.getString("status"));
                            r.setOwner_image_url(jsonObject.getString("owner_image_url"));
                            r.setPowerMan_name(jsonObject.getString("powerMan_name"));
                            r.setPowerMan_image_url(jsonObject.getString("powerMan_image_url"));
                            r.setPowerMan_id(jsonObject.getString("powerMan_id"));
                            r.setCreated_at(jsonObject.getString("created_at"));
                            r.setUpdated_at(jsonObject.getString("updated_at"));
                            r.setCause(jsonObject.getString("cause"));
                            r.setSender_id(jsonObject.getString("sender_id"));
                            r.setAcceptetStatus(jsonObject.getString("s")); // accept status


                            manlist.add(r);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //-----------
                        progress.dismiss();
                    return manlist;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            if (AppStatus.getInstance(context).isOnline())
                progress.dismiss();
        }

        return null;
    }

//    @Override
//    protected void onPostExecute( ArrayList<ServiceRequest> s) {
//        super.onPostExecute(s);
//
//
//        if(!s.isEmpty()) {
//            progress.dismiss();
//            asyncResponse.processFinish(s);
//
//
//        }
//
//        else
//        {
//            progress.dismiss();
//    }
    }



