package com.example.waqarahmed.neighbourlinking.Activities.TanantActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.PlaceRequestOnServer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RequestForm extends AppCompatActivity {
    ImageView serviceMan_imageView;
    EditText causeEdit,contactEdit;
    Button doneBtn;
    String cause,contact;
    String  user_id ,  Address , f_name , image;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReferenceCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Request");

        serviceMan_imageView = (ImageView) findViewById(R.id.serviceMan_imageview);
        causeEdit = (EditText) findViewById(R.id.cause_edit);
        contactEdit = (EditText) findViewById(R.id.contact_edit);
        doneBtn = (Button) findViewById(R.id.done_request);

        mAuth = FirebaseAuth.getInstance();
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(causeEdit.length()>0){
                    if(contactEdit.length()==11){
                        sendRequestOnServer();
                    }else{
                        contactEdit.setError("Enter Correct Contact!!!");
                    }

                }else {

                    causeEdit.setError("Cause Require!!!");
                }


            }
        });







    }
    public void sendRequestOnServer(){

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            mDatabaseReferenceCurrentUser = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid().toString());
            mDatabaseReferenceCurrentUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String cause = causeEdit.getText().toString();
                    String contact = contactEdit.getText().toString();
                    if(!TextUtils.isEmpty(cause ) &&!TextUtils.isEmpty(contact)) {
                        String type = getIntent().getStringExtra("type").toString();
                        String s_name = getIntent().getStringExtra("name").toString();
                        String s_image = getIntent().getStringExtra("image").toString();
                        int s_id = getIntent().getIntExtra("id", 0);

                        user_id = mAuth.getCurrentUser().getUid().toString();
                        Address = (String) dataSnapshot.child("address").getValue();
                        f_name = (String) dataSnapshot.child("first_name").getValue();
                        image = (String) dataSnapshot.child("image").getValue();
                        //Picasso.with(RequestForm.this).load(image).placeholder(R.mipmap.ic_launcher).centerCrop().into(serviceMan_imageView);
                        // Toast.makeText(RequestForm.this," "+user_id+Address+f_name+image,Toast.LENGTH_LONG).show();

                        PlaceRequestOnServer placeRequestOnServer = new PlaceRequestOnServer(RequestForm.this);
                        placeRequestOnServer.execute(type,cause, user_id, Address, contact, f_name, image, s_name, s_image, String.valueOf(s_id));

                       // causeEdit.setText(" ");
                       // contactEdit.setText(" ");

                    }else
                        Toast.makeText(RequestForm.this,"Field Error",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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
