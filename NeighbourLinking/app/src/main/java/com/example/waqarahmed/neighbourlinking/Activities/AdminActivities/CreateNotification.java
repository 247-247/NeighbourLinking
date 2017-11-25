package com.example.waqarahmed.neighbourlinking.Activities.AdminActivities;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.AdminSevices.uploadNotification;
import com.example.waqarahmed.neighbourlinking.Shared.AdminSharedPref;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class CreateNotification extends AppCompatActivity {
EditText titleEdit,discEdit;
    Button sndBtn;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private AwesomeValidation awesomValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
        titleEdit = (EditText) findViewById(R.id.title_edit_Nofification);
        discEdit = (EditText) findViewById(R.id.disc_edit_Nofification);
        awesomValidation = new AwesomeValidation(ValidationStyle.BASIC);
        getSupportActionBar().setTitle("New Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.colorMaster));
        sndBtn = (Button) findViewById( R.id.send_btn_notification);
        sndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // intenrial ads
        //end ads
    }

    private void sendNotification() {
        if(titleEdit.length() == 0){
            titleEdit.setError("Title Required");
        }else{

            if(discEdit.length() == 0){
                discEdit.setError("Discription Require");
            }
            else {
                String title, disc;
                title = titleEdit.getText().toString();
                disc = discEdit.getText().toString();
                uploadNotification upload = new uploadNotification(this);
                upload.execute(title,disc);
                titleEdit.setText("");
                discEdit.setText("");


            }

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
