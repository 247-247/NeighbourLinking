package com.example.waqarahmed.neighbourlinking.Activities.TanantActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.waqarahmed.neighbourlinking.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateAccountTenetActivity extends AppCompatActivity {
    ImageButton imageButton;
    EditText mFirstName,mLastName,mMobile,mCountery;
    Button mSubmitButton;
    Spinner mGender;
    String[] mGenderArray ;
    ArrayAdapter<String> arrayAdapter;
    String mFirst_name , mLast_name,mobNO, mCountry , mGender_string;
    public static final int GALLARY_CODE = 1;
    DatabaseReference mDatabaseReferenceUser;
    FirebaseAuth mAuth;
    StorageReference mUserProfileStorage;
    ProgressDialog mProg;
    Uri resultUri;
    DatabaseReference mDatabaseReferenceCurrentUser;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account_tenet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mFirstName = (EditText) findViewById(R.id.first_name_edit_profileActivity);
        mLastName = (EditText) findViewById(R.id.last_name_edit_profileActivity);
        mMobile = (EditText) findViewById(R.id.adress_edit_profileActity);
        mSubmitButton = (Button) findViewById(R.id.submit_profile_btn);
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        mUserProfileStorage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        mAuth = FirebaseAuth.getInstance();
        PopulateUserData();
        mProg = new ProgressDialog(this);
        imageButton = (ImageButton) findViewById(R.id.profile_image);
       /* imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenue();
            }
        });*/
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSetupAccount();
            }
        });
    }

    private void showPopupMenue() {
        MenuBuilder menuBuilder =new MenuBuilder(this);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.picture_selection_menue, menuBuilder);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(this, menuBuilder, imageButton);
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
                imageButton.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void startSetupAccount()
    {
        mFirst_name = mFirstName.getText().toString().trim();
        mLast_name = mLastName.getText().toString().trim();
        mobNO=mMobile.getText().toString().trim();
        String current_userId = mAuth.getCurrentUser().getUid();
        final DatabaseReference mDatabaseChildUser = mDatabaseReferenceUser.child(current_userId);
        mProg.setMessage("SetUp Account");
        mProg.show();
        if( resultUri != null && !TextUtils.isEmpty(mFirst_name)  && !TextUtils.isEmpty(mLast_name) && !TextUtils.isEmpty(mobNO))
        {
            StorageReference mStorageChildImage = mUserProfileStorage.child(resultUri.getLastPathSegment());
            mStorageChildImage.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadURL = taskSnapshot.getDownloadUrl();
                    mDatabaseChildUser.child("image").setValue(downloadURL.toString());
                    mDatabaseChildUser.child("first_name").setValue(mFirst_name);
                    mDatabaseChildUser.child("last_name").setValue(mLast_name);
                    mDatabaseChildUser.child("mobl").setValue(mobNO);
                    mProg.dismiss();
                    int a=0;
                   // take to main Activity
                    Intent intent =new Intent(UpdateAccountTenetActivity.this , MainActivity.class);
                    startActivity(intent);


                }
            });
        }
        else if(!TextUtils.isEmpty(mFirst_name)  && !TextUtils.isEmpty(mLast_name) && !TextUtils.isEmpty(mobNO))
        {
            mProg.setMessage("SetUp Account");
            mProg.show();
            mDatabaseChildUser.child("first_name").setValue(mFirst_name);
            mDatabaseChildUser.child("last_name").setValue(mLast_name);
            mDatabaseChildUser.child("mobl").setValue(mobNO);
            mProg.dismiss();
            Intent intent =new Intent(UpdateAccountTenetActivity.this , MainActivity.class);
            startActivity(intent);

        }
        else
        {
            mProg.dismiss();
            Toast.makeText(UpdateAccountTenetActivity.this,"Field Errors",Toast.LENGTH_SHORT).show();
        }
    }
    public void PopulateUserData()
    {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabaseReferenceCurrentUser = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        mDatabaseReferenceCurrentUser.keepSynced(true);
        mDatabaseReferenceCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String isAdmin = (String) dataSnapshot.child("isAdmin").getValue();
                if (isAdmin.equals("Yes")) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
                } else {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tanat_toolbar)));
                }
                String f_name = (String) dataSnapshot.child("first_name").getValue();
                getSupportActionBar().setTitle(f_name);
                String s_name = (String) dataSnapshot.child("last_name").getValue();
                String email = mAuth.getCurrentUser().getEmail();
                String Address = (String) dataSnapshot.child("address").getValue(); // house no
                String mobile = (String) dataSnapshot.child("mobl").getValue();
                String gender = (String) dataSnapshot.child("gender").getValue();
                String create_date = (String) dataSnapshot.child("create_date").getValue();
                final String image = (String) dataSnapshot.child("image").getValue();
                mFirstName.setText(f_name);
                mLastName.setText(s_name);
                mMobile.setText(mobile);
               /* String stringUri = image;
                resultUri = Uri.parse(stringUri);*/
                Picasso.with(UpdateAccountTenetActivity.this).load(image).centerCrop().resize(75, 75).networkPolicy(NetworkPolicy.OFFLINE).into(imageButton, new Callback() {
                    @Override
                    public void onSuccess()
                    {
                       //todo
                    }
                    @Override
                    public void onError() {
                        Uri uri;

                        Picasso.with(UpdateAccountTenetActivity.this).load(image).centerCrop().resize(75, 75).into(imageButton);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    //on back button press this method will get invoke
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
