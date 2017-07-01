package com.example.waqarahmed.neighbourlinking.Services.BrandServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.MainBrandActivity;
import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.Classes.Brand;
import com.example.waqarahmed.neighbourlinking.Shared.BrandSharedPref;

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

public class RetrievBranProfileInfo extends AsyncTask<String, Void, String> {

    Context cxt;
    ProgressDialog progress ;

    public RetrievBranProfileInfo(Context context) {
        cxt=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        if (AppStatus.getInstance(cxt).isOnline()){
//            if (progress == null) {
//                progress = AppUtils.createProgressDialog(cxt);
//                progress.show();
//            } else {
//                progress.show();
//            }

        }

    @Override
    protected String doInBackground(String... strings) {
        String Brand_name,Brand_image,Brand_contact,Brand_addres;



        if (AppStatus.getInstance(cxt).isOnline()) {
           // progress.show();

            String url_string = "http://b362f197.ngrok.io/Neighbour/public/getBrandBasesOnId";
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
                int id = BrandSharedPref.read(BrandSharedPref.ID,0);
                if(id != 0) {
                    String post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(id), "UTF-8");
                    bufferedWriter.write(post_data);
                    Log.i("TAG", "doInBackground: 7 ");
                }
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


                       JSONObject jsonObject = jsonRootObject.getJSONObject("BrandBaseOnId");

                 //   int employeeId = jsonObject.getInt("employeeId");

                    if(jsonObject.equals("no")){
                        return "no";
                    }
                    else{

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

                        BrandSharedPref.init(cxt);
                        BrandSharedPref.writeObject(BrandSharedPref.OBJECT,brand);
                        return "yes";
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
            return "offline";

        }
        return "no";
    }

        @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//            if(progress != null && progress.isShowing())
//                progress.dismiss();
            if(s.equals("yes")){
               // Toast.makeText(cxt,"True",Toast.LENGTH_SHORT).show();
//                Intent mainIntent = new Intent(cxt,MainBrandActivity.class);
//                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//
//                cxt.startActivity(mainIntent);



                // progress.dismiss();
            }
            else if(s.equals("no")){
               // Toast.makeText(cxt,"Brand Already exist",Toast.LENGTH_SHORT).show();


            }else{

            }

          //      Toast.makeText(cxt,"Network issue",Toast.LENGTH_SHORT).show();
//



        }
}
