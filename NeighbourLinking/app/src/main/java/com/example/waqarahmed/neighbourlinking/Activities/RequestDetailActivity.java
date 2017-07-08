package com.example.waqarahmed.neighbourlinking.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Classes.ServiceRequest;
import com.example.waqarahmed.neighbourlinking.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class RequestDetailActivity extends AppCompatActivity {
    ImageView mCurrentServiceImage;
    TextView mHeadName, mHeadDate;
    EditText mFirstName, mStatusEdit , mCreateField,mCauseEdit , isAccaptedEdit;
    ServiceRequest serviceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        getSupportActionBar().setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mCurrentServiceImage = (ImageView) findViewById(R.id.currentUser_imageView_rDActivity);
        mHeadName = (TextView) findViewById(R.id.currentUserName_textView_rDActivity);
        mHeadDate= (TextView) findViewById(R.id.accountCreated_date_rDActivity);
        mFirstName= (EditText) findViewById(R.id.sName_editiew_rDActivity);
        mStatusEdit= (EditText) findViewById(R.id.status_editiew_rDActivity);
        mCauseEdit= (EditText) findViewById(R.id.cause_editiew_rDActivity);
        isAccaptedEdit = (EditText) findViewById(R.id.isAccepted_editiew_rDActivity);
        mCreateField = (EditText) findViewById(R.id.createDate_editiew_rAActivity);
        serviceRequest = (ServiceRequest) getIntent().getSerializableExtra("rObject");
        setDisplayValue(serviceRequest);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(12000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
       // setDisplayValue();

      //  Toast.makeText(RequestDetailActivity.this,""+serviceRequest.getCause(),Toast.LENGTH_LONG).show();






    }

    @Override
    protected void onStart() {
        super.onStart();
        //setDisplayValue();
    }

    public void setDisplayValue(final ServiceRequest serviceRequest){


        Picasso.with(RequestDetailActivity.this).load(serviceRequest.getPowerMan_image_url()).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentServiceImage , new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(RequestDetailActivity.this).load(serviceRequest.getPowerMan_image_url()).centerCrop().resize(75,75).into(mCurrentServiceImage );
            }
        });
        mHeadName.setText(serviceRequest.getType());
        mHeadDate.setText(serviceRequest.getCreated_at());
        mFirstName.setText(serviceRequest.getPowerMan_name());
        mStatusEdit.setText(serviceRequest.getStatus());
        mCreateField.setText(serviceRequest.getCreated_at());
        mCauseEdit.setText(serviceRequest.getCause());
        isAccaptedEdit.setText(serviceRequest.getS());

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
