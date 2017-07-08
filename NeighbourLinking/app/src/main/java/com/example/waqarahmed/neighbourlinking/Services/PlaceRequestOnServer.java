package com.example.waqarahmed.neighbourlinking.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.MainActivity;
import com.example.waqarahmed.neighbourlinking.R;

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

public class PlaceRequestOnServer extends AsyncTask<String, Void,  Void> {

    Context cxt;
    ProgressDialog progress ;
    public PlaceRequestOnServer(Context context) {
        cxt=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(cxt);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

    }

    @Override
    protected Void doInBackground(String... strings) {
        String type, cause, sender_id, owner_house_address, owner_contact, owner_name, owner_image_url, powerMan_name, powerMan_image_url, poweMan_id;

        type = strings[0];
        cause = strings[1];
        sender_id = strings[2];
        owner_house_address = strings[3];
        owner_contact = strings[4];
        owner_name = strings[5];
        owner_image_url = strings[6];
        powerMan_name = strings[7];
        powerMan_image_url = strings[8];
        poweMan_id = strings[9];
        String baseUrl = cxt.getResources().getString(R.string.baseUrl);
        String url_string = baseUrl+"/Neighbour/public/newRequestInsert";
        try {
            Log.i("TAG", "doInBackground:  "+type+" "+cause+" "+sender_id+" "+owner_house_address);
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
            String post_data = URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8")+"&"+
                                URLEncoder.encode("cause","UTF-8")+"="+URLEncoder.encode(cause, "UTF-8")+"&"+
                                URLEncoder.encode("sender_id","UTF-8")+"="+URLEncoder.encode(sender_id,"UTF-8")+"&"+
                                URLEncoder.encode("owner_house_address","UTF-8")+"="+URLEncoder.encode(owner_house_address,"UTF-8")+"&"+
                                URLEncoder.encode("owner_contact","UTF-8")+"="+URLEncoder.encode(owner_contact,"UTF-8")+"&"+
                                URLEncoder.encode("owner_name","UTF-8")+"="+URLEncoder.encode(owner_name, "UTF-8")+"&"+
                                URLEncoder.encode("owner_image_url","UTF-8")+"="+URLEncoder.encode(owner_image_url,"UTF-8")+"&"+
                                URLEncoder.encode("powerMan_name","UTF-8")+"="+URLEncoder.encode(powerMan_name,"UTF-8")+"&"+
                                URLEncoder.encode("powerMan_image_url","UTF-8")+"="+URLEncoder.encode(powerMan_image_url,"UTF-8")+"&"+
                                URLEncoder.encode("powerMan_id", "UTF-8")+"="+URLEncoder.encode(poweMan_id,"UTF-8");
          //  bufferedWriter.write(post_data);
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



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

        @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
            progress.dismiss();
            Toast.makeText(cxt,"Request sended",Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(cxt,MainActivity.class);
                      cxt.startActivity(mainIntent);




        }
}
