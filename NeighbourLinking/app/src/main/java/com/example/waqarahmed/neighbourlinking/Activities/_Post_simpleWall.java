package com.example.waqarahmed.neighbourlinking.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.waqarahmed.neighbourlinking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class _Post_simpleWall extends AppCompatActivity {
    EditText mTitle , mDescription;
    Button mSubmit;
    DatabaseReference mDatabaseReferenceUser;
    DatabaseReference dbRefPost;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    DatabaseReference mCurrenUser;
    ProgressDialog mProgBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity___post_simple_wall);

        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mTitle = (EditText) findViewById(R.id.title_edit_post);
        mDescription = (EditText) findViewById(R.id.desc_edit_post);
        mSubmit = (Button) findViewById(R.id.done_post_post);
        mProgBar = new ProgressDialog(this);
        mProgBar.setMessage("Uploading...");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        dbRefPost= FirebaseDatabase.getInstance().getReference().child("Post");
        dbRefPost.keepSynced(true);
        mCurrenUser = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid());
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }
    private void startPosting() {

        final String title_val = mTitle.getText().toString().trim();
        final String desc_val = mDescription.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) ){
            mProgBar.show();


                    final DatabaseReference newPost = dbRefPost.push();


                    mCurrenUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(title_val);
                            newPost.child("desc").setValue(desc_val);
                          //  newPost.child("post_image").setValue(downloadUri.toString());
                            newPost.child("uid").setValue(firebaseUser.getUid());
                            newPost.child("sender_image").setValue(dataSnapshot.child("image").getValue());
                            newPost.child("send_date").setValue( new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                            newPost.child("username").setValue(dataSnapshot.child("first_name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent mainIntent = new Intent(_Post_simpleWall.this , MainActivity.class);
                                 //   mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mProgBar.dismiss();


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
