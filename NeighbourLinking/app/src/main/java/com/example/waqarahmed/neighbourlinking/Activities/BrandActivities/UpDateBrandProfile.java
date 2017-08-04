package com.example.waqarahmed.neighbourlinking.Activities.BrandActivities;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.Classes.Brand;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.BrandServices.upLoadBranProfileInfo;
import com.example.waqarahmed.neighbourlinking.Shared.BrandSharedPref;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UpDateBrandProfile extends AppCompatActivity {
    ImageButton imageButton;
    EditText nameEdit,ContactEdit;
    Button donrBtn;
    Spinner spinner;
    String spnrValue;
    public static final int GALLARY_CODE = 1;
    Uri resultUri;
    String img;
    ProgressDialog progress ;

    StorageReference mUserProfileStorage;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_date_brand_profile);
        getSupportActionBar().setTitle("Account setup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brand_toolbr)));
        imageButton = (ImageButton) findViewById(R.id.image_brandPfofile);
        nameEdit = (EditText) findViewById(R.id.name_brandProfile);
        ContactEdit = (EditText) findViewById(R.id.contact_brandProfile);
        spinner = (Spinner) findViewById(R.id.shopNo_brandProfile);
        donrBtn = (Button) findViewById(R.id.doneBtn_brandProfile);
        BrandSharedPref.init(this);
         final Brand brand = BrandSharedPref.readObject(BrandSharedPref.OBJECT,null);
        Picasso.with(UpDateBrandProfile.this).load(brand.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(imageButton , new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(UpDateBrandProfile.this).load(brand.getImage_url()).placeholder(R.mipmap.ic_launcher).centerCrop().resize(75,75).into(imageButton );
            }
        });
        nameEdit.setText(brand.getName());
        ContactEdit.setText(brand.getContact());
        resultUri = Uri.parse(brand.getImage_url());


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        mUserProfileStorage = FirebaseStorage.getInstance().getReference().child("Profile_images");
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
            public void onClick(View view)
            {
                if(ContactEdit.length()==11) {
                    if (awesomeValidation.validate()) {

                        startSetupAccount();
                    } else {
                        Toast.makeText(UpDateBrandProfile.this,"Error occur",Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {
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

    private void startSetupAccount()
    {
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
                            @SuppressWarnings("VisibleForTests")  Uri downloadURL = taskSnapshot.getDownloadUrl();
                            progress.dismiss();
                            upLoadBranProfileInfo ul = new upLoadBranProfileInfo(UpDateBrandProfile.this);
                            ul.execute(name, downloadURL.toString(),contact,spnrValue);

                        }
                    });

                }

            }
            else
            {
                Toast.makeText(UpDateBrandProfile.this,"Network error Errors",Toast.LENGTH_SHORT).show();
            }



        }
        else{
            Toast.makeText(UpDateBrandProfile.this,"AccountSetup Field Errors",Toast.LENGTH_SHORT).show();
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
