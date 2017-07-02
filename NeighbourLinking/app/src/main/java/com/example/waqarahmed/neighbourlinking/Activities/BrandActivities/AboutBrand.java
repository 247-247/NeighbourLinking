package com.example.waqarahmed.neighbourlinking.Activities.BrandActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.About;
import com.example.waqarahmed.neighbourlinking.Activities.MainActivity;
import com.example.waqarahmed.neighbourlinking.Classes.Brand;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.BrandServices.RetrievBranProfileInfoForClient;
import com.example.waqarahmed.neighbourlinking.Shared.BrandSharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class AboutBrand extends AppCompatActivity {
    ImageView mCurrentUserImage;
    TextView mHeadName, mHeadDate;
    EditText mFirstName,mContact,mEmail,mAddress,mCreatedAt,mStatus;
    Brand brandRecvdFromAsyc = null;


    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_brand);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCurrentUserImage = (ImageView) findViewById(R.id.currentUser_imageView_brandAboutActivity);
        mHeadName = (TextView) findViewById(R.id.currentUserName_textView_brandAboutActivity);
        mHeadDate= (TextView) findViewById(R.id.accountCreated_date_brandAboutActivity);
        mFirstName= (EditText) findViewById(R.id.firstName_editiew_brandAboutActivity);
        mContact= (EditText) findViewById(R.id.contact_editiew_brandAboutActivity);
        mEmail= (EditText) findViewById(R.id.emailName_editiew_brandAboutActivity);
        mAddress= (EditText) findViewById(R.id.addressName_editiew_brandAboutActivity);
        mCreatedAt= (EditText) findViewById(R.id.createDate_editiew_brandAboutActivity);
        mStatus= (EditText) findViewById(R.id.status_editiew_brandAboutActivity);


        BrandSharedPref.init(this);
        int i = BrandSharedPref.read(BrandSharedPref.ID,0);
        if(i != 0){

            userId = String.valueOf(i);
            onIfUserItselfLogin(userId);
        }else {
            userId = getIntent().getStringExtra("id");
          //  Toast.makeText(AboutBrand.this,userId,Toast.LENGTH_LONG).show();
            onIntentReceived(userId);
        }





    }

    private void onIntentReceived(String userId) {


        new RetrievBranProfileInfoForClient(this){
            @Override
            protected void onPostExecute(Brand brand) {
                super.onPostExecute(brand);
                if(brand.getStatus() == "Not Fount Result"){
                    Toast.makeText(AboutBrand.this,"Record not Found",Toast.LENGTH_SHORT).show();
                    Intent BrandIntent = new Intent(AboutBrand.this,MainActivity.class);
                    BrandIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    BrandIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                    startActivity(BrandIntent);
                }
                if(brand.getStatus() == "off line"){
                    Toast.makeText(AboutBrand.this,"network issue",Toast.LENGTH_SHORT).show();
                    Intent BrandIntent = new Intent(AboutBrand.this,MainActivity.class);
                    BrandIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    BrandIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                    startActivity(BrandIntent);
                }else{
                    brandRecvdFromAsyc = brand;
                    Picasso.with(AboutBrand.this).load(brandRecvdFromAsyc.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentUserImage , new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(AboutBrand.this).load(brandRecvdFromAsyc.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).into(mCurrentUserImage );
                        }
                    });


                    mHeadName.setText(brandRecvdFromAsyc.getName());
                    mHeadDate.setText(brandRecvdFromAsyc.getCreated_at());
                    mAddress.setText(brandRecvdFromAsyc.getAddress());
                    mContact.setText(brandRecvdFromAsyc.getContact());
                    mStatus.setText(brandRecvdFromAsyc.getStatus());
                    mCreatedAt.setText(brandRecvdFromAsyc.getCreated_at());
                    mFirstName.setText(brandRecvdFromAsyc.getName());
                    mEmail.setText(brandRecvdFromAsyc.getEmail());




                }

            }
        }.execute(userId);




    }

    private void onIfUserItselfLogin(String userId) {
        BrandSharedPref.init(this);
        final Brand brand = BrandSharedPref.readObject(BrandSharedPref.OBJECT,null);
        Picasso.with(AboutBrand.this).load(brand.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentUserImage , new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(AboutBrand.this).load(brand.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).into(mCurrentUserImage );
            }
        });


        mHeadName.setText(brand.getName());
        mHeadDate.setText(brand.getCreated_at());
        mAddress.setText(brand.getAddress());
        mContact.setText(brand.getContact());
        mStatus.setText(brand.getStatus());
        mCreatedAt.setText(brand.getCreated_at());
        mFirstName.setText(brand.getName());
        mEmail.setText(brand.getEmail());


    }
    @Override
    protected void onStart() {
        super.onStart();





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
