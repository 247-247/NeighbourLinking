package com.example.waqarahmed.neighbourlinking.Services.AdminSevices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.AdminActivities.MainActivityAdmin;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.MainActivity;
import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Shared.AdminSharedPref;

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

public class uploadNotification extends AsyncTask<String, Void, String> {

    Context cxt;
    ProgressDialog progress ;
    String image,email;

    public uploadNotification(Context context) {
        cxt=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        AdminSharedPref.init(cxt);
              if (AppStatus.getInstance(cxt).isOnline()){
                  if (progress == null) {
                      image = AdminSharedPref.read(AdminSharedPref.IMAGE,null);
                      email = AdminSharedPref.read(AdminSharedPref.EMAIL,null);
                      progress = AppUtils.createProgressDialog(cxt);
                      progress.show();
                  } else {
                      progress.show();
                  }

              }



    }

    @Override
    protected String doInBackground(String... strings) {
        String title,type,disc;


        title = strings[0];
        disc = strings[1];
        if (AppStatus.getInstance(cxt).isOnline())
        {
            String baseUrl = cxt.getResources().getString(R.string.baseUrl);
            String url_string = baseUrl+"/Neighbour/public/InserNewNotificatationItem";
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
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8") + "&" +
                        URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8")+ "&" +
                        URLEncoder.encode("disc", "UTF-8") + "=" + URLEncoder.encode(disc, "UTF-8");

                //  bufferedWriter.write(post_data);
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
        return null;
    }

        @Override
    protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progress != null && progress.isShowing())
                progress.dismiss();
            Toast.makeText(cxt,"Notification sended",Toast.LENGTH_SHORT).show();


        }
}
