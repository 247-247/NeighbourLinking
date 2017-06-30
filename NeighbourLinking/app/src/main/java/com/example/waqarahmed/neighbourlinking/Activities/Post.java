package com.example.waqarahmed.neighbourlinking.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Post extends AppCompatActivity {
    ImageButton mImageButton;
    EditText mTitle , mDescription;
    Button mSubmit;
    private Uri resultUri =null;
    StorageReference strRef;
    StorageReference mUserPostStorage;;
    DatabaseReference mDatabaseReferenceUser;
    DatabaseReference dbRefBlog;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    DatabaseReference mCurrenUser;
    ProgressDialog mProgBar;
    private static final int GALLARY_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mImageButton = (ImageButton) findViewById(R.id.img_btn);
        mTitle = (EditText) findViewById(R.id.title_edit);
        mDescription = (EditText) findViewById(R.id.desc_edit);
        mSubmit = (Button) findViewById(R.id.done_post);
        mProgBar = new ProgressDialog(this);
        mProgBar.setMessage("Uploading...");
        strRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
       firebaseUser = mAuth.getCurrentUser();
        dbRefBlog= FirebaseDatabase.getInstance().getReference().child("Blog");
        dbRefBlog.keepSynced(true);
        mCurrenUser = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid());
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent,GALLARY_CODE);
                showPopupMenue();
            }
        });
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
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) &&  resultUri!= null){
            mProgBar.show();

            StorageReference childRef = strRef.child("blogPhotos").child( resultUri.getLastPathSegment());
            childRef.putFile( resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")   final Uri downloadUri = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = dbRefBlog.push();


                    mCurrenUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(title_val);
                            newPost.child("desc").setValue(desc_val);
                            newPost.child("post_image").setValue(downloadUri.toString());
                            newPost.child("uid").setValue(firebaseUser.getUid());
                            newPost.child("sender_image").setValue(dataSnapshot.child("image").getValue());
                            newPost.child("send_date").setValue( new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                            newPost.child("username").setValue(dataSnapshot.child("first_name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent mainIntent = new Intent(Post.this , MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            });

        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLARY_CODE && resultCode == RESULT_OK){
            Uri imgeURL = data.getData();
            CropImage.activity(imgeURL)
                    .setGuidelines(CropImageView.Guidelines.ON).setGuidelinesColor(Color.rgb(0,230,0)).setBorderCornerColor(Color.BLUE).setBorderLineColor(Color.RED)
                    .setActivityMenuIconColor(Color.rgb(0,230,0)).setActivityMenuIconColor(Color.rgb(0,230,0))
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                mImageButton.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void showPopupMenue() {

        MenuBuilder menuBuilder =new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.picture_selection_menue, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, mImageButton);
        optionsMenu.setForceShowIcon(true);

// Set Item Click Listener
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.gallaryy:
                        Intent GallaryIntent = new Intent(Intent.ACTION_PICK);
                        GallaryIntent.setType("image/*");
                        startActivityForResult(GallaryIntent,GALLARY_CODE);
                        return true;
                    case R.id.camera: // Handle option2 Click
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,GALLARY_CODE);
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {}
        });

        optionsMenu.show();

    }  //showPopupMenue

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
