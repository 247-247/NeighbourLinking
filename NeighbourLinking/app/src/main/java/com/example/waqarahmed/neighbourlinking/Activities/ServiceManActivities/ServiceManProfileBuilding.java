package com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.BrandProfileBuilding;
import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.MainBrandActivity;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManProfile;
import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.BrandServices.upLoadBranProfileInfo;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.upLoadServiceManProfileInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ServiceManProfileBuilding extends AppCompatActivity {
    ImageButton imageButton;
    EditText nameEdit,ContactEdit;
    Button donrBtn;
    Spinner spinner;
    String spnrValue;
    public static final int GALLARY_CODE = 1;
    Uri resultUri;
    private AwesomeValidation awesomeValidation;
    StorageReference mUserProfileStorage;
    ProgressDialog progress ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_man_profile_building);
        getSupportActionBar().setTitle("Account setup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imageButton = (ImageButton) findViewById(R.id.image_servicePfofile);
        nameEdit = (EditText) findViewById(R.id.name_serviceProfile);
        ContactEdit = (EditText) findViewById(R.id.contact_serviceProfile);
        spinner = (Spinner) findViewById(R.id.skill_list_serviceProfile);
        donrBtn = (Button) findViewById(R.id.doneBtn_serviceProfile);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mUserProfileStorage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        awesomeValidation.addValidation(this, R.id.name_serviceProfile, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
      //  awesomeValidation.addValidation(this, R.id.contact_brandProfile, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);

        spnrValue =spinner.getSelectedItem().toString();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenue();
            }
        });
        donrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContactEdit.length()==11) {
                    if (awesomeValidation.validate()) {
                        startSetupAccount();
                    } else {
                        //   Toast.makeText(BrandProfileBuilding.this,"Error occur",Toast.LENGTH_SHORT).show();

                    }
                }else{

                    ContactEdit.setError("Enter Write Contact");
                }
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
        spnrValue =spinner.getSelectedItem().toString();
        final String name = nameEdit.getText().toString();
        final String contact = ContactEdit.getText().toString();

        if(!TextUtils.isEmpty(name) && resultUri != null && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(spnrValue))
        {

            if (AppStatus.getInstance(this).isOnline())
            {
                if (progress == null)
                {
                    progress = AppUtils.createProgressDialog(this);
                    progress.show();
                    StorageReference mStorageChildImage = mUserProfileStorage.child(resultUri.getLastPathSegment());
                    mStorageChildImage.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            @SuppressWarnings("VisibleForTests") Uri downloadURL = taskSnapshot.getDownloadUrl();
                            progress.dismiss();
                            upLoadServiceManProfileInfo ul = new upLoadServiceManProfileInfo(ServiceManProfileBuilding.this);
                            ul.execute(name, downloadURL.toString(), contact, spnrValue);
                        }
                    });

                } else
                    {
                    // progress.show();
                    }
            }
            else
            {
                Toast.makeText(ServiceManProfileBuilding.this, "Network Errors", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(ServiceManProfileBuilding.this, "AccountSetup Field Errors", Toast.LENGTH_SHORT).show();
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
