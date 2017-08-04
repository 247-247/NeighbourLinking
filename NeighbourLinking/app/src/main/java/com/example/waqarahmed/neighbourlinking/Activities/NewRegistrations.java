package com.example.waqarahmed.neighbourlinking.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.waqarahmed.neighbourlinking.Activities.AdminActivities.NewTanantSignUp;
import com.example.waqarahmed.neighbourlinking.Activities.AdminActivities.UploadSkillActivity;
import com.example.waqarahmed.neighbourlinking.R;

public class NewRegistrations extends AppCompatActivity {

    Button tanantBtn, newServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_registrations);
        getSupportActionBar().setTitle("Admin View");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
        tanantBtn = (Button) findViewById(R.id.newTanant);
        newServiceBtn = (Button) findViewById(R.id.newBrand);
        tanantBtnListener();
        newServiceBtnListener();

    }

    private void tanantBtnListener() {
        tanantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTanant = new Intent(NewRegistrations.this , NewTanantSignUp.class);
                startActivity(newTanant);

            }
        });

    }
    private void newServiceBtnListener() {
        newServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTanant = new Intent(NewRegistrations.this , UploadSkillActivity.class);
                startActivity(newTanant);


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
