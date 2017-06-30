package com.example.waqarahmed.neighbourlinking.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsynResonseForServiceManProfile;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.RetrievAllMenPowerList;
import com.example.waqarahmed.neighbourlinking.Services.RetrievAllServices;
import com.example.waqarahmed.neighbourlinking.Services.RetrievServiceManBasesOnId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ServiceManProfile extends AppCompatActivity implements AsynResonseForServiceManProfile {

    ImageView mCurrentServiceImage;
    TextView mHeadName, mHeadDate;
    EditText mFirstName, mStatusEdit , mCreateField,mCauseEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_man_profile);
        String id = getIntent().getStringExtra("id");
      //  Toast.makeText(ServiceManProfile.this,id,Toast.LENGTH_SHORT).show();
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mCurrentServiceImage = (ImageView) findViewById(R.id.currentUser_imageView_rDActivity);
        mHeadName = (TextView) findViewById(R.id.currentUserName_textView_rDActivity);
        mHeadDate= (TextView) findViewById(R.id.accountCreated_date_rDActivity);
        mFirstName= (EditText) findViewById(R.id.sName_editiew_rDActivity);
        mStatusEdit= (EditText) findViewById(R.id.status_editiew_rDActivity);
        mCauseEdit= (EditText) findViewById(R.id.cause_editiew_rDActivity);
        mCreateField = (EditText) findViewById(R.id.createDate_editiew_rAActivity);
        String type = "login";

        RetrievServiceManBasesOnId retriveServiceMan = new RetrievServiceManBasesOnId(this);
       retriveServiceMan.asyncResponse = this;
        retriveServiceMan.execute(type,id);
    }

    @Override
    public void processFinish(final ServiceMan Man) {


            Picasso.with(ServiceManProfile.this).load(Man.getImage_url()).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentServiceImage , new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ServiceManProfile.this).load(Man.getImage_url()).centerCrop().resize(75,75).into(mCurrentServiceImage );
                }
            });
            mHeadName.setText(Man.getName());
            mHeadDate.setText(Man.getSkill());
            mFirstName.setText(Man.getName());
            mStatusEdit.setText(Man.getStatus());
            mCreateField.setText(Man.getEmail());
            mCauseEdit.setText(Man.getContact());

    //    Toast.makeText(ServiceManProfile.this,Man.getName(),Toast.LENGTH_SHORT).show();
     //   Toast.makeText(ServiceManProfile.this,Man.getContact(),Toast.LENGTH_SHORT).show();




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
