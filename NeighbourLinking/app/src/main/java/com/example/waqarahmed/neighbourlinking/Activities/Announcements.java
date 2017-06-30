package com.example.waqarahmed.neighbourlinking.Activities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Classes.Announcement;
import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Listener.RecyclerItemClickListener;
import com.example.waqarahmed.neighbourlinking.Services.BackgroundWorker;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsyncResponse;
import com.example.waqarahmed.neighbourlinking.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Announcements extends AppCompatActivity implements AsyncResponse{
RecyclerView announcement_recyclerView;
    ArrayList<Announcement> listAnnouncement;
  //  RetriveAllAnnouncementsBackgroundWorker retriveAllAnnouncementsBackgroundWorker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notifications");
       // retriveAllAnnouncementsBackgroundWorker = new RetriveAllAnnouncementsBackgroundWorker();
     listAnnouncement = new ArrayList<Announcement>();
        announcement_recyclerView = (RecyclerView) findViewById(R.id.recyclerView_announcement);
        announcement_recyclerView.setHasFixedSize(true);
        announcement_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        //this to set delegate/listener back to this class
//       retriveAllAnnouncementsBackgroundWorker.delegate = this;
//        retriveAllAnnouncementsBackgroundWorker.execute();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
    backgroundWorker.asyncResponse = this;
        backgroundWorker.execute(type);






    }

    @Override
    public void processFinish(String output) {
         String d;
        try {
            Log.i("TAG", "Post try ");
            JSONObject jsonRootObject = null;
            try {
                jsonRootObject = new JSONObject(output);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("TAG", "Pot After ");

            JSONArray jsonArray = jsonRootObject.getJSONArray("AllNotification");


//            JSONObject jsonObject = jsonArray.getJSONObject(1);
//            Toast.makeText(this,jsonObject.getString("email")+" "+jsonObject.getInt("id") ,Toast.LENGTH_LONG).show();

            for (int i=jsonArray.length()-1; i>=0; i--){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Announcement announcement = new Announcement();
                announcement.setId(jsonObject.getInt("id"));
                announcement.setEmail(jsonObject.getString("email"));
                announcement.setMsg_body(jsonObject.getString("msg_body"));
                announcement.setTitle(jsonObject.getString("title"));
                d =jsonObject.getString("created_at");
                announcement.setCreated_at(d.substring(0,d.indexOf(' ')));
                announcement.setSendind_to(jsonObject.getString("sendind_to"));
                announcement.setUpdated_at(jsonObject.getString("updated_at"));
                announcement.setStatus(jsonObject.getString("status"));
                listAnnouncement.add(announcement);
              //  Toast.makeText(this,listAnnouncement.get(i).getTitle() ,Toast.LENGTH_LONG).show();
            }
          //  Toast.makeText(this,listAnnouncement.get(4).getTitle() ,Toast.LENGTH_LONG).show();




        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
           RVAforAnnouncements rvAforAnnouncements = new RVAforAnnouncements(listAnnouncement,this);

            announcement_recyclerView.setAdapter(rvAforAnnouncements);
        announcement_recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Announcement announcement = listAnnouncement.get(position);

                final Snackbar snackbar =  Snackbar.make(findViewById(android.R.id.content), announcement.getTitle()+"\n"+announcement.getMsg_body(),Snackbar.LENGTH_LONG).setDuration(Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView tv= (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(10);
                snackbar.setDuration(1000000);
                snackbar.setAction("Back", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(snackbar.isShown()){
                           snackbar.dismiss();

                       }
                    }
                });
                snackbar.show();
//

            }
        }));

    }

    @Override
    protected void onStart() {
        super.onStart();
        connectionCheck();
        RVAforAnnouncements rvAforAnnouncements = new RVAforAnnouncements(listAnnouncement,this);
        announcement_recyclerView.setAdapter(rvAforAnnouncements);
    }

    public class RVAforAnnouncements extends RecyclerView.Adapter<RVAforAnnouncements.AnnouncementViewHolder>
    {

        List<Announcement> AllAnnouncementList;
        Context mContext;

        public RVAforAnnouncements(List<Announcement> allAnnouncementList, Context mContext) {
            AllAnnouncementList = allAnnouncementList;
            this.mContext = mContext;
        }

        @Override
        public RVAforAnnouncements.AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_recycler_item, parent, false);
            AnnouncementViewHolder    Avh = new AnnouncementViewHolder (v);
            return Avh ;
        }

        @Override
        public void onBindViewHolder(RVAforAnnouncements.AnnouncementViewHolder holder, int position) {

            Announcement announcement = AllAnnouncementList.get(position);
            holder.setName(announcement.getTitle());
            holder.setDate(announcement.getCreated_at());
            holder.setMsg(announcement.getMsg_body());

        }

        @Override
        public int getItemCount() {
            return AllAnnouncementList.size();
        }

        public  class AnnouncementViewHolder extends RecyclerView.ViewHolder{

            View mView;
            ImageView imageView ;
            TextView nameView,dateView,msgView,bdgView;

            String name;
            String imgURL;
            String date;
            String msg;
            String bdg;

            public AnnouncementViewHolder(View itemView) {
                super(itemView);

                mView = itemView;
                imageView = (ImageView)mView.findViewById(R.id.imageview_announcement_list_item);
                nameView = (TextView) itemView.findViewById(R.id.name_announcement_list_item);
                dateView = (TextView)itemView.findViewById(R.id.date_announcement_list_item);
                msgView = (TextView) itemView.findViewById(R.id.msg_announcement_list_item);
                bdgView = (TextView)itemView.findViewById(R.id.bedg_announcement_list_item);
            }


            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
                nameView.setText(name);
            }

            public String getImgURL() {
                return imgURL;
            }

            public void setImgURL( final String imgURL,final Context cxt) {
                Picasso.with(cxt).load(imgURL).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(cxt).load(imgURL).centerCrop().resize(75,75).into(imageView);
                    }
                });
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
                dateView.setText(date);
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
                msgView.setText(msg);
            }

            public String getBdg() {
                return bdg;
            }

            public void setBdg(String bdg) {
                this.bdg = bdg;
            }
        }  // ViewHolder End
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void connectionCheck(){
        if (AppStatus.getInstance(this).isOnline()) {


        } else {
            View view = null;

            Toast.makeText(Announcements.this,"You are Offline!!!",Toast.LENGTH_LONG).show();
        }

    }



}
