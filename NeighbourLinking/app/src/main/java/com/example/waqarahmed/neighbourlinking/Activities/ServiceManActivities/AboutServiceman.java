package com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.MainActivity;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.RetrievServiceProfileInfoForClient;
import com.example.waqarahmed.neighbourlinking.Shared.SharedPref;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class AboutServiceman extends AppCompatActivity {
    ImageView mCurrentUserImage;
    TextView mHeadName, mHeadDate;
    EditText mFirstName,mContact,mEmail,mSkill,mCreatedAt,mStatus;
    ServiceMan serviceManRecvdFromAsyc = null;
    String userId;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    DatabaseReference mDatabaseReferenceUser , mDatabaseCurrentUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_serviceman);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCurrentUserImage = (ImageView) findViewById(R.id.currentUser_imageView_brandAboutActivity);
        mHeadName = (TextView) findViewById(R.id.currentUserName_textView_brandAboutActivity);
        mHeadDate= (TextView) findViewById(R.id.accountCreated_date_brandAboutActivity);
        mFirstName= (EditText) findViewById(R.id.firstName_editiew_brandAboutActivity);
        mContact= (EditText) findViewById(R.id.contact_editiew_brandAboutActivity);
        mEmail= (EditText) findViewById(R.id.emailName_editiew_brandAboutActivity);
        mSkill= (EditText) findViewById(R.id.skill_editiew_brandAboutActivity);
        mCreatedAt= (EditText) findViewById(R.id.createDate_editiew_brandAboutActivity);
        mStatus= (EditText) findViewById(R.id.status_editiew_brandAboutActivity);
        SharedPref.init(this);
        int i = SharedPref.read(SharedPref.ID,0);
        if(i != 0){

            userId = String.valueOf(i);
            onIfUserItselfLogin(userId);
        }else {
            mAuth = FirebaseAuth.getInstance();
            if(mAuth.getCurrentUser() != null){

                mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
                DatabaseReference currentUser_Database = mDatabaseReferenceUser.child(mAuth.getCurrentUser().getUid().toString());
                currentUser_Database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String isAdmin = (String) dataSnapshot.child("isAdmin").getValue();
                        if(isAdmin.equals("Yes")){
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
                        }else {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tanat_toolbar)));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            userId = getIntent().getStringExtra("id");
            //  Toast.makeText(AboutBrand.this,userId,Toast.LENGTH_LONG).show();
            onIntentReceived(userId);
        }

        // ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // intenrial ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                finish();
            }
        });
      //end ads

    }

    public  void showInternialAd()
    {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }
    private void onIntentReceived(String userId) {


        new RetrievServiceProfileInfoForClient(this){
            @Override
            protected void onPostExecute(ServiceMan serviceMan) {
                super.onPostExecute(serviceMan);
                if(serviceMan.getStatus() == "Not Fount Result"){
                    Toast.makeText(AboutServiceman.this,"Record not Found",Toast.LENGTH_SHORT).show();
                    Intent BrandIntent = new Intent(AboutServiceman.this,MainActivity.class);
                    BrandIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    BrandIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                    startActivity(BrandIntent);
                }
                if(serviceMan.getStatus() == "off line"){
                    Toast.makeText(AboutServiceman.this,"network issue",Toast.LENGTH_SHORT).show();
                    Intent BrandIntent = new Intent(AboutServiceman.this,MainActivity.class);
                    BrandIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    BrandIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

                    startActivity(BrandIntent);
                }else{
                    serviceManRecvdFromAsyc = serviceMan;
                    Picasso.with(AboutServiceman.this).load(serviceManRecvdFromAsyc.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentUserImage , new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(AboutServiceman.this).load(serviceManRecvdFromAsyc.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).into(mCurrentUserImage );
                        }
                    });


                    mHeadName.setText(serviceManRecvdFromAsyc.getName());
                    mHeadDate.setText(serviceManRecvdFromAsyc.getCreated_at());
                    mSkill.setText(serviceManRecvdFromAsyc.getSkill());
                    mContact.setText(serviceManRecvdFromAsyc.getContact());
                    mStatus.setText(serviceManRecvdFromAsyc.getStatus());
                    mCreatedAt.setText(serviceManRecvdFromAsyc.getCreated_at());
                    mFirstName.setText(serviceManRecvdFromAsyc.getName());
                    mEmail.setText(serviceManRecvdFromAsyc.getEmail());

                }

            }
        }.execute(userId);
    }
    private void onIfUserItselfLogin(String userId) {
        SharedPref.init(this);
        final ServiceMan serviceMan = SharedPref.readObject(SharedPref.OBJECT,null);
       // Toast.makeText(AboutServiceman.this,serviceMan.getSkill(),Toast.LENGTH_LONG).show();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.serviceman_toolbr)));
        Picasso.with(AboutServiceman.this).load(serviceMan.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentUserImage , new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(AboutServiceman.this).load(serviceMan.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).into(mCurrentUserImage );
            }
        });


        mHeadName.setText(serviceMan.getName());
        mHeadDate.setText(serviceMan.getCreated_at());
        mSkill.setText(serviceMan.getSkill());
        mContact.setText(serviceMan.getContact());
        mStatus.setText(serviceMan.getStatus());
        mCreatedAt.setText(serviceMan.getCreated_at());
        mFirstName.setText(serviceMan.getName());
        mEmail.setText(serviceMan.getEmail());


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
    public void onBackPressed()
    {
        super.onBackPressed();
        showInternialAd();
    }


}
