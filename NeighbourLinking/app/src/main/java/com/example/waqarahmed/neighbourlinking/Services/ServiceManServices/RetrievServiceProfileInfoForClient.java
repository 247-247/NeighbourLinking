package com.example.waqarahmed.neighbourlinking.Services.ServiceManServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.Classes.Brand;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Shared.BrandSharedPref;
import com.example.waqarahmed.neighbourlinking.Shared.SharedPref;

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
 * Created by Waqar ahmed on 6/1/2017.
 */

public class RetrievServiceProfileInfoForClient extends AsyncTask<String, Void, ServiceMan> {

    Context cxt;
    ProgressDialog progress ;

    public RetrievServiceProfileInfoForClient(Context context) {
        cxt=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (AppStatus.getInstance(cxt).isOnline()) {
            if (progress == null) {
                progress = AppUtils.createProgressDialog(cxt);
                progress.show();
            } else {
                progress.show();
            }

        }
    }
    @Override
    protected ServiceMan doInBackground(String... strings) {
        String id = strings[0];

        if (AppStatus.getInstance(cxt).isOnline()) {
            progress.show();
            String baseUrl = cxt.getResources().getString(R.string.baseUrl);
            String url_string = baseUrl+"/Neighbour/public/getEmployeeBasesOnId";
            try {
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
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                BrandSharedPref.init(cxt);


                    String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    bufferedWriter.write(post_data);
                    Log.i("TAG", "doInBackground: 7 ");
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
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

                try {
                    JSONObject jsonRootObject = null;
                    try {

                        jsonRootObject = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                       JSONObject jsonObject = jsonRootObject.getJSONObject("EmployeeBaseOnId");

                 //   int employeeId = jsonObject.getInt("employeeId");

                    if(jsonObject.equals("no")){
                        if(progress != null && progress.isShowing())
                            progress.dismiss();
                        ServiceMan serviceMan = new ServiceMan();
                        serviceMan.setStatus("Not Fount Result");
                        return serviceMan;
                    }
                    else{

                        ServiceMan serviceMan = new ServiceMan();
                        serviceMan.setId(jsonObject.getInt("id"));
                        serviceMan.setName(jsonObject.getString("name"));
                        serviceMan.setSkill(jsonObject.getString("skill"));
                        serviceMan.setImage_url(jsonObject.getString("image_url"));
                        serviceMan.setStatus(jsonObject.getString("status"));
                        serviceMan.setContact(jsonObject.getString("contact"));
                        serviceMan.setEmail(jsonObject.getString("email"));
                        serviceMan.setCreated_at(jsonObject.getString("created_at"));
                        serviceMan.setUpdated_at(jsonObject.getString("updated_at"));
                        serviceMan.setPassword(jsonObject.getString("password"));
                        serviceMan.setIsAccountSetUp(jsonObject.getString("isAccountSetUp"));

//                       SharedPref.init(cxt);
//                        SharedPref.writeObject(SharedPref.OBJECT,serviceMan);
                        if(progress != null && progress.isShowing())
                            progress.dismiss();
                        return serviceMan;
                    }





                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            if(progress != null && progress.isShowing())
                progress.dismiss();
            ServiceMan serviceMan = new ServiceMan();
            serviceMan.setStatus("off line");
            return serviceMan;

        }
        if(progress != null && progress.isShowing())
            progress.dismiss();
        ServiceMan serviceMan = new ServiceMan();
        serviceMan.setStatus("Not Fount Result");
        return serviceMan;

    }

//        @Override
//    protected void onPostExecute(Brand s) {
//        super.onPostExecute(s);
//
//
//
//        }
}
