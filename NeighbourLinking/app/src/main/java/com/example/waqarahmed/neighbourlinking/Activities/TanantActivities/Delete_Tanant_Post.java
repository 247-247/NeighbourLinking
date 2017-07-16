package com.example.waqarahmed.neighbourlinking.Activities.TanantActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.AdminActivities.MainActivityAdmin;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Shared.AdminSharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Delete_Tanant_Post extends AppCompatActivity {
ImageView mSndrImageView,mPost_imageView;
    TextView mNameView,mDateView,mTitleView,mDiscriptionView;
    Button mDeletePost;
    FirebaseAuth mAuth;
    DatabaseReference mDatabasePost;
    String title,desc,user_id,user_name,post_image ,sender_image,post_date;
    String post_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_tanant_post);
        getSupportActionBar().setTitle("Delete Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        post_key = getIntent().getExtras().getString("post_key");
        mSndrImageView = (ImageView) findViewById(R.id.senderImage_imageView_dltActivity);
      //  mPost_imageView = (ImageView) findViewById(R.id.imageShow_imageView_dltActivity);
        mNameView = (TextView) findViewById(R.id.senderName_textView_dltActivity);
         mDateView = (TextView) findViewById(R.id.senderDate_textView_dltActivity);
        mTitleView =  (TextView) findViewById(R.id.titleShow_textView_dltActivity);
        mDiscriptionView =  (TextView) findViewById(R.id.descShow_textView_dltActivity);
        AdminSharedPref.init(this);

                mDeletePost = (Button) findViewById(R.id.dlt_done);
        mAuth = FirebaseAuth.getInstance();
        mDatabasePost = FirebaseDatabase.getInstance().getReference().child("Post").child(post_key);
       // mDatabasePost = FirebaseDatabase.getInstance().getReference().child("Post").child(post_key.toString());
        Log.i("Single View Activity", "onDataChange:Post key "+post_key);
               mDatabasePost.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                title = (String) dataSnapshot.child("title").getValue();
                desc = (String)dataSnapshot.child("desc").getValue();
              //  post_image =(String) dataSnapshot.child("post_image").getValue();
                user_id = (String) dataSnapshot.child("uid").getValue();
                user_name = (String)dataSnapshot.child("username").getValue();
                sender_image =(String) dataSnapshot.child("sender_image").getValue();
                post_date =(String) dataSnapshot.child("send_date").getValue();
                Log.i("Single View Activity", "onDataChange:title " +title);
                Log.i("Single View Activity", "onDataChange:Description "+desc);
                Log.i("Single View Activity", "onDataChange:Image "+post_image);
                Log.i("Single View Activity", "onDataChange:USer_Id "+user_id);
                Log.i("Single View Activity", "onDataChange:User_NAME "+user_name);
                if(mAuth.getCurrentUser().getUid().toString().equals(user_id))
                {
                    mDeletePost.setVisibility(View.VISIBLE);
                    Log.i("Visiblity", "onDataChange: Visible call"+user_id+" "+user_id+" "+"current_User"+" "+mAuth.getCurrentUser().getUid().toString());

                }else if((AdminSharedPref.read(AdminSharedPref.IS_ADMIN,"no").equals("yes"))){

                    mDeletePost.setVisibility(View.VISIBLE);
                }
                else
                {
                    mDeletePost.setVisibility(View.INVISIBLE);
                    Log.i("Visiblity", "onDataChange: IN_Visible call"+"Sender Id"+" "+user_id+" "+"current_User"+" "+mAuth.getCurrentUser().getUid().toString());
                }
                Picasso.with(Delete_Tanant_Post.this).load(sender_image).centerCrop().resize(75,75).into(mSndrImageView);
                mNameView.setText(user_name);
                mDateView.setText(post_date);
                mTitleView.setText(title);
              //  Picasso.with(Delete_Tanant_Post.this).load(post_image).into(mPost_imageView);
                mDiscriptionView.setText(desc);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        mDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Delete_Tanant_Post.this);
                builder1.setTitle("Delete Post");
                builder1.setMessage("Are you sure you want to delete this entry?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                mDatabasePost.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();


                                            if(AdminSharedPref.read(AdminSharedPref.IS_ADMIN,"no").equals("yes")){
                                                Intent mainIntent = new Intent(Delete_Tanant_Post.this, MainActivityAdmin.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                            }else{
                                                Intent mainIntent = new Intent(Delete_Tanant_Post.this, MainActivity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                            }
                                        }
                                        else{

                                        }
                                    }
                                });
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });


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


