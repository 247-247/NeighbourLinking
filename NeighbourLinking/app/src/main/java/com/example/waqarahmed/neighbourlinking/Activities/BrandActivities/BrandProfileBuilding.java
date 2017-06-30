package com.example.waqarahmed.neighbourlinking.Activities.BrandActivities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.example.waqarahmed.neighbourlinking.Activities.MainActivity;
import com.example.waqarahmed.neighbourlinking.Activities.Profile;
import com.example.waqarahmed.neighbourlinking.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BrandProfileBuilding extends AppCompatActivity {
    ImageButton imageButton;
    EditText nameEdit,ContactEdit;
    Button donrBtn;
    Spinner spinner;
    String spnrValue;
    public static final int GALLARY_CODE = 1;
    Uri resultUri;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_profile_building);
        getSupportActionBar().setTitle("Account setup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imageButton = (ImageButton) findViewById(R.id.image_brandPfofile);
        nameEdit = (EditText) findViewById(R.id.name_brandProfile);
        ContactEdit = (EditText) findViewById(R.id.contact_brandProfile);
        spinner = (Spinner) findViewById(R.id.shopNo_brandProfile);
        donrBtn = (Button) findViewById(R.id.doneBtn_brandProfile);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.name_brandProfile, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
     //   awesomeValidation.addValidation(this, R.id.contact_brandProfile, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
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
                if(ContactEdit.length()>11) {
                if(awesomeValidation.validate()){
               startSetupAccount();
            }else{
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
        String name = nameEdit.getText().toString();
        String contact = ContactEdit.getText().toString();

        if(!TextUtils.isEmpty(name) && resultUri != null && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(spnrValue)) {


                    Intent mainIntent = new Intent(BrandProfileBuilding.this , MainBrandActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);




        }
        else{
            Toast.makeText(BrandProfileBuilding.this,"AccountSetup Faild Errors",Toast.LENGTH_SHORT).show();
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
