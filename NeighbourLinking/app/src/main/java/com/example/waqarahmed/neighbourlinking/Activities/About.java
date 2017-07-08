package com.example.waqarahmed.neighbourlinking.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class About extends AppCompatActivity {
   ImageView mCurrentUserImage;
    TextView mHeadName, mHeadDate , genderView,EmailView;
    EditText mFirstName,mLastName,mEmail,mAddress,mCity,mGender , mCreateField;
    DatabaseReference mDatabaseReferenceCurrentUser;
    FirebaseAuth mAuth;
    ProgressDialog mProg;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    mCurrentUserImage = (ImageView) findViewById(R.id.currentUser_imageView_aboutActivity);
        mHeadName = (TextView) findViewById(R.id.currentUserName_textView_aboutActivity);
        mHeadDate= (TextView) findViewById(R.id.accountCreated_date_aboutActivity);
        mFirstName= (EditText) findViewById(R.id.firstName_editiew_aboutActivity);
        mLastName= (EditText) findViewById(R.id.lastName_editiew_aboutActivity);
        mEmail= (EditText) findViewById(R.id.emailName_editiew_aboutActivity);
        mAddress= (EditText) findViewById(R.id.addressName_editiew_aboutActivity);
        mCity= (EditText) findViewById(R.id.cityName_editiew_aboutActivity);
        mGender= (EditText) findViewById(R.id.gender_editiew_aboutActivity);
        genderView = (TextView) findViewById(R.id.gender_textview_aboutActivity);
        EmailView = (TextView) findViewById(R.id.email_view_aboutActivity);
        mAuth = FirebaseAuth.getInstance();
        mProg = new ProgressDialog(this);
        mCreateField = (EditText) findViewById(R.id.createDate_editiew_aboutActivity);


        if(mAuth.getCurrentUser() != null){

            userId = mAuth.getCurrentUser().getUid().toString();
            onIfUserItselfLogin(userId);
        }else {
            userId = getIntent().getStringExtra("id");
           onIntentReceived(userId);
      //      Toast.makeText(About.this,userId , Toast.LENGTH_SHORT).show();
        }






//        if(!userId.equals(null))
//        {
//            mProg.show();
//            mDatabaseReferenceCurrentUser = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
//            mDatabaseReferenceCurrentUser.keepSynced(true);
//            mDatabaseReferenceCurrentUser.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String f_name = (String) dataSnapshot.child("first_name").getValue();
//                    getSupportActionBar().setTitle(f_name);
//                    String  s_name= (String)dataSnapshot.child("last_name").getValue();
//                    String email =mAuth.getCurrentUser().getEmail();
//                    String Address = (String) dataSnapshot.child("address").getValue();
//                    String city = (String)dataSnapshot.child("city").getValue();
//                    String gender =(String) dataSnapshot.child("gender").getValue();
//                    String create_date =(String) dataSnapshot.child("create_date").getValue();
//                    final String image =(String) dataSnapshot.child("image").getValue();
//                    mFirstName.setText(f_name);
//                    mLastName.setText(s_name);
//                    mEmail.setText(email);
//                    mAddress.setText(Address);
//                    mCity.setText(city);
//                    mGender.setText(gender);
//                    mCreateField.setText(create_date);
//                    mCreateField.setText(create_date);
//                    mHeadName.setText(f_name);
//                    mHeadDate.setText(create_date);
//                    Picasso.with(About.this).load(image).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentUserImage , new Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onError() {
//                            Picasso.with(About.this).load(image).centerCrop().resize(75,75).into(mCurrentUserImage );
//                        }
//                    });
//
//                mProg.dismiss();
//
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//
//        }


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

    public void onIntentReceived(String id){
        if(!userId.equals(null))
        {
            mProg.show();
            mDatabaseReferenceCurrentUser = FirebaseDatabase.getInstance().getReference().child("User").child(id);
         //   mDatabaseReferenceCurrentUser.keepSynced(true);
            mDatabaseReferenceCurrentUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String f_name = (String) dataSnapshot.child("first_name").getValue();
                    getSupportActionBar().setTitle(f_name);
                    String  s_name= (String)dataSnapshot.child("last_name").getValue();
 //                    String email =mAuth.getCurrentUser().getEmail();
                    String Address = (String) dataSnapshot.child("address").getValue();
                    String city = (String)dataSnapshot.child("mobl").getValue();
                    String gender =(String) dataSnapshot.child("gender").getValue();
                    String create_date =(String) dataSnapshot.child("create_date").getValue();
                    final String image =(String) dataSnapshot.child("image").getValue();
                    mFirstName.setText(f_name);
                    mLastName.setText(s_name);
                   // mEmail.setText(email);
                    mEmail.setVisibility(View.INVISIBLE);
                    EmailView.setVisibility(View.INVISIBLE);
                    mAddress.setText(Address);
                    mCity.setText(city);
                    mGender.setText(gender);
                    mGender.setVisibility(View.INVISIBLE);
                    genderView.setVisibility(View.INVISIBLE);
                    mCreateField.setText(create_date);
                    mHeadName.setText(f_name);
                    mHeadDate.setText(create_date);
                    Picasso.with(About.this).load(image).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentUserImage , new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(About.this).load(image).centerCrop().resize(75,75).into(mCurrentUserImage );
                        }
                    });

                    mProg.dismiss();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }



    }
    public void onIfUserItselfLogin(String userId){
        if(!userId.equals(null))
        {
            mProg.show();
            mDatabaseReferenceCurrentUser = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
            mDatabaseReferenceCurrentUser.keepSynced(true);
            mDatabaseReferenceCurrentUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String f_name = (String) dataSnapshot.child("first_name").getValue();
                    getSupportActionBar().setTitle(f_name);
                    String  s_name= (String)dataSnapshot.child("last_name").getValue();
                    String email =mAuth.getCurrentUser().getEmail();
                    String Address = (String) dataSnapshot.child("address").getValue();
                    String city = (String)dataSnapshot.child("mobl").getValue();
                    String gender =(String) dataSnapshot.child("gender").getValue();
                    String create_date =(String) dataSnapshot.child("create_date").getValue();
                    final String image =(String) dataSnapshot.child("image").getValue();
                    mFirstName.setText(f_name);
                    mLastName.setText(s_name);
                    mEmail.setText(email);
                    mAddress.setText(Address);
                    mCity.setText(city);
                    mGender.setText(gender);
                    mCreateField.setText(create_date);
                    mCreateField.setText(create_date);
                    mHeadName.setText(f_name);
                    mHeadDate.setText(create_date);
                    Picasso.with(About.this).load(image).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentUserImage , new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(About.this).load(image).centerCrop().resize(75,75).into(mCurrentUserImage );
                        }
                    });

                mProg.dismiss();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }
}
