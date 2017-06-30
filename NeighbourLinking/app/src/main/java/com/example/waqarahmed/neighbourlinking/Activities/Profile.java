package com.example.waqarahmed.neighbourlinking.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile extends AppCompatActivity {
 ImageButton imageButton;
    EditText mFirstName,mLastName,mAddress,mCountery;
    Button mSubmitButton;
    Spinner mGender;
    String[] mGenderArray ;
    ArrayAdapter<String> arrayAdapter;
    String mFirst_name , mLast_name,mAdres, mCountry , mGender_string;
    public static final int GALLARY_CODE = 1;
    DatabaseReference mDatabaseReferenceUser;
    FirebaseAuth mAuth;
    StorageReference mUserProfileStorage;
    ProgressDialog mProg;
    Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mGenderArray = new String[]{"Male","Female"};
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mGenderArray);
        mGender = (Spinner) findViewById(R.id.gender_spiner_profileActivty);
        mGender.setAdapter(arrayAdapter);
        mFirstName = (EditText) findViewById(R.id.first_name_edit_profileActivity);
        mLastName = (EditText) findViewById(R.id.last_name_edit_profileActivity);
        mAddress = (EditText) findViewById(R.id.adress_edit_profileActity);
        mCountery = (EditText) findViewById(R.id.countery_edit_profileActivty);
        mSubmitButton = (Button) findViewById(R.id.submit_profile_btn);
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        mUserProfileStorage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        mAuth = FirebaseAuth.getInstance();
        mProg = new ProgressDialog(this);

        imageButton = (ImageButton) findViewById(R.id.profile_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showCustomImageSelectionDialog();
                showPopupMenue();
            }
        });

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

    private void startSetupAccount() {
       // final String profileName =  AccountSetupNAme.getText().toString().trim();
        mFirst_name = mFirstName.getText().toString().trim();
        mLast_name = mLastName.getText().toString().trim();
        mAdres=mAddress.getText().toString().trim();
        mCountry=mCountery.getText().toString().trim();
        mGender_string = mGender.getSelectedItem().toString();
        if(!TextUtils.isEmpty(mFirst_name) && resultUri != null && !TextUtils.isEmpty(mLast_name) && !TextUtils.isEmpty(mAdres)
                && !TextUtils.isEmpty(mCountry) && !TextUtils.isEmpty(mGender_string)) {
            mProg.setMessage("SetUp Account");
            mProg.show();
            StorageReference mStorageChildImage = mUserProfileStorage.child(resultUri.getLastPathSegment());
            mStorageChildImage.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")  Uri downloadURL = taskSnapshot.getDownloadUrl();
                    String current_userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference mDatabaseChildUser = mDatabaseReferenceUser.child(current_userId);
                    mDatabaseChildUser.child("first_name").setValue(mFirst_name);
                    mDatabaseChildUser.child("last_name").setValue( mLast_name);
                    mDatabaseChildUser.child("mobl").setValue(mAdres);
                    mDatabaseChildUser.child("city").setValue(mCountry);
                    mDatabaseChildUser.child("uid").setValue(mAuth.getCurrentUser().getUid());
                    mDatabaseChildUser.child("create_date").setValue( new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                    // Log.i("AccountSetup", downloadURL.toString());
                    mDatabaseChildUser.child("image").setValue(downloadURL.toString());
                    mDatabaseChildUser.child("gender").setValue(mCountry);
                    mProg.dismiss();
                    Intent mainIntent = new Intent(Profile.this , MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);


                }
            });
        }
        else{
            Toast.makeText(Profile.this,"AccountSetup Field Errors",Toast.LENGTH_SHORT).show();
        }


    }

}
