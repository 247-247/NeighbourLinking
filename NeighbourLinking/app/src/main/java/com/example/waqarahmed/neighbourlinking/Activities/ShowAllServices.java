package com.example.waqarahmed.neighbourlinking.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.ManPowerList;
import com.example.waqarahmed.neighbourlinking.Classes.ServicesTypes;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsynResonseForServices;
import com.example.waqarahmed.neighbourlinking.Listener.RecyclerItemClickListener;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.RetrievAllServices;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowAllServices extends AppCompatActivity implements AsynResonseForServices{
    RecyclerView servicesType_recyclerView;
    ArrayList<ServicesTypes> Serviceslist;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_services);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Offered Services");
        servicesType_recyclerView= (RecyclerView) findViewById(R.id.recyclerView_serviceType);
        servicesType_recyclerView.setHasFixedSize(true);
        servicesType_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Serviceslist = new ArrayList<ServicesTypes>();
        servicesType_recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        ServicesTypes s = Serviceslist.get(position);
                       // Toast.makeText(ShowAllServices.this," "+s.getSkill(), Toast.LENGTH_LONG).show();

                        Intent RetrievServiceManListInrent = new Intent(ShowAllServices.this,ManPowerList.class);
                        RetrievServiceManListInrent.putExtra("Skill",s.getSkill());
                        startActivity(RetrievServiceManListInrent);

                    }
                })
        );


        String type = "login";
        RetrievAllServices retrievAllServices = new RetrievAllServices(this);
        retrievAllServices.asyncResponse = this;
        retrievAllServices.execute(type);
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



    @Override
    protected void onStart() {
        super.onStart();
        ServiceTypesRVA rvAforServiceTypes = new ServiceTypesRVA (Serviceslist,this);
        servicesType_recyclerView.setAdapter(rvAforServiceTypes);
    }

    @Override
    public void processFinish(ArrayList<ServicesTypes> Serviceslist) {
       this.Serviceslist = Serviceslist;
        this.onStart();

    }

    public static class ServiceTypesRVA extends RecyclerView.Adapter<ShowAllServices.ServiceViewHolder>{
        List<ServicesTypes> AllServices;
        Context mContext;

        public ServiceTypesRVA(List<ServicesTypes> allServices, Context mContext) {
            AllServices = allServices;
            this.mContext = mContext;
        }


        @Override
        public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_single_item, parent, false);
            ShowAllServices.ServiceViewHolder Svh = new ServiceViewHolder(v);
            return Svh ;
        }

        @Override
        public void onBindViewHolder(ServiceViewHolder holder, int position) {

            ServicesTypes service = AllServices.get(position);
            holder.setServicet_type_name(service.getSkill());
            holder.setService_type_image(service.getImage_url(),mContext);


        }

        @Override
        public int getItemCount() {
            return AllServices.size();
        }
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder{

        View Mview;
        TextView serviceNameView;
        ImageView serviceTypeImage;



        public ServiceViewHolder(View itemView ) {
            super(itemView);
            Mview = itemView;
            serviceTypeImage= (ImageView) itemView.findViewById(R.id.image_service_item);
            serviceNameView = (TextView) itemView.findViewById(R.id.name_service_item);

        }
        public void setServicet_type_name(String serviceName) {
            serviceNameView.setText(serviceName);

        }

        public void setService_type_image(final String service_type_image , final Context c) {
            Picasso.with(c).load(service_type_image).placeholder(R.mipmap.img_sample).centerCrop().resize(60,60).networkPolicy(NetworkPolicy.OFFLINE).into(serviceTypeImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(c).load(service_type_image).placeholder(R.mipmap.img_sample).centerCrop().resize(60,60).into(serviceTypeImage);
                }
            });

        }
    } // viwholder class
}
