package com.example.waqarahmed.neighbourlinking.Activities.AdminActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.RequestDetailActivity;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities.AboutServiceman;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.About;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Conversations;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.RequestForm;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment.ServiceMan_newRequest;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsynResonseForMenPowerList;
import com.example.waqarahmed.neighbourlinking.Interfaces.LongClickListener;
import com.example.waqarahmed.neighbourlinking.Listener.RecyclerItemClickListener;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.AdminSevices.RetrievAllserviceMenList;
import com.example.waqarahmed.neighbourlinking.Services.RetrievAllMenPowerList;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.AccepRequestService;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.setRejectRequestService;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AllServiceMenList extends AppCompatActivity implements AsynResonseForMenPowerList{
    String getIntent_skillName;
    RecyclerView menPowerList_recyclerView;
    TextView textView;
    ArrayList<ServiceMan> menlist;
    int p;  // position
    RVAdapterForMenList rvAdapterForMenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_power_list);
         
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Services Men");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
        menPowerList_recyclerView= (RecyclerView) findViewById(R.id.recyclerView_menPower);
        textView = (TextView) findViewById(R.id.messageView);
        menPowerList_recyclerView.setHasFixedSize(true);
        menPowerList_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        menlist = new ArrayList<ServiceMan>();
        registerForContextMenu(menPowerList_recyclerView);
        rvAdapterForMenList = new RVAdapterForMenList(menlist,this);
        RetrievAllserviceMenList retrievAllserviceMenList =new RetrievAllserviceMenList(this);
        retrievAllserviceMenList.asyncResponse= this;
        retrievAllserviceMenList.execute();



    }



    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    public void processFinish(ArrayList<ServiceMan> Manlist) {
        if(Manlist.size() != 0) {
            menPowerList_recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            this.menlist = Manlist;
             rvAdapterForMenList = new RVAdapterForMenList(Manlist, this);
            menPowerList_recyclerView.setAdapter(rvAdapterForMenList);



        }
        else{
            menPowerList_recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);


        }
    }


    public class RVAdapterForMenList extends RecyclerView.Adapter<RVAdapterForMenList.contactViewHolder> {


        List<ServiceMan> list;
        Context mContext;
        ServiceMan serviceMan2;  // use for Contextual menue

        RVAdapterForMenList(List<ServiceMan> userList, Context context) {
            list = userList;
            mContext = context;
        }

        @Override
        public RVAdapterForMenList.contactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_item, parent, false);
            contactViewHolder cvh = new contactViewHolder(v);
           return cvh;
        }

        @Override
        public void onBindViewHolder(RVAdapterForMenList.contactViewHolder holder, int position) {
            ServiceMan serviceMan   = list.get(position);

            holder.setContact_name(serviceMan.getName());
            holder.setContact_image(serviceMan.getImage_url(),mContext);
            holder.setContact_no(serviceMan.getContact());
            holder.setLongClickListener(new LongClickListener()
            {
                @Override
                public void onItemLongClick(int pos) {
                    p = pos;
                    serviceMan2 = list.get(pos);    // here get position of selected recycler item position
                }
            });
        }
        @Override
        public void onViewRecycled(contactViewHolder holder)
        {   // may not required
            super.onViewRecycled(holder);
            holder.itemView.setOnLongClickListener(null);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }
        @Override
        public long getItemId(int position) {
            // return position;
            return super.getItemId(position);


        }
        public void getItemSelected(MenuItem item) {
            //  Toast.makeText(mContext,"hello" +" "+item.getTitle(),Toast.LENGTH_LONG).show();
            if(item.getTitle().equals("About")){

                Intent intent = new Intent(AllServiceMenList.this, AboutServiceman.class);
                intent.putExtra("id",String.valueOf(serviceMan2.getId()));
                startActivity(intent);
            }else {

            }

        }

        public  class contactViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnCreateContextMenuListener{

            View mView;
            ImageView imageView ;
            TextView nameView,dateView;
            LongClickListener longClickListener;

            public contactViewHolder(View itemView) {
                super(itemView);

                mView = itemView;
                mView.setOnLongClickListener(this);
                mView.setOnCreateContextMenuListener(this);

                imageView = (ImageView)mView.findViewById(R.id.contentImage_imageView_contentView);
                nameView = (TextView) itemView.findViewById(R.id.contentName_textView_contentView);
                dateView = (TextView)itemView.findViewById(R.id.created_date_contentView);
            }
            public void setContact_name(String name) {

                nameView.setText(name);

            }
            public void setContact_image(final String image , final Context cxt) {
                Picasso.with(cxt).load(image).centerCrop().resize(75,75).placeholder(R.mipmap.ic_launcher).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(cxt).load(image).centerCrop().placeholder(R.mipmap.ic_launcher).resize(75,75).into(imageView);
                    }
                });
            }
            public void setContact_no(String no) {

                dateView.setText(no);

            }
            public  void setLongClickListener(LongClickListener lc){
                this.longClickListener = lc;
            }
            @Override
            public boolean onLongClick(View view) {
                this.longClickListener.onItemLongClick(getLayoutPosition());
                return false;
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


                contextMenu.setHeaderTitle("Seleect Acton");
               contextMenu.add("About");


            }





        }
}
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        rvAdapterForMenList.getItemSelected(item);
        return false;
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


}
