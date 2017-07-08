package com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;

import com.example.waqarahmed.neighbourlinking.Activities.SignInActivity;
import com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment.AceptAndActiveRequest;
import com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment.ServiceMan_newRequest;
import com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment.ServiceMan_pastHistoryRequest;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.RetrievServicemanProfileInfo;
import com.example.waqarahmed.neighbourlinking.Shared.SharedPref;

public class MainServiceManActivity extends AppCompatActivity {
  FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_service_man);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SharedPref.init(this);
        String isAccountSetup = SharedPref.read(SharedPref.ACCOUNT,"no");
        if(isAccountSetup.equals("no")){

            startActivity(new Intent(MainServiceManActivity.this,ServiceManProfileBuilding.class));
            finish();
        }
        else{

            RetrievServicemanProfileInfo retrievServicemanProfileInfo = new RetrievServicemanProfileInfo(this);
            retrievServicemanProfileInfo.execute();
        }


        container = (FrameLayout) findViewById(R.id.container);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "New Request",
                        "Pending",
                        "History",
                }));
        ServiceMan_newRequest serviceMan_newRequest = new ServiceMan_newRequest();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, serviceMan_newRequest)
                .commit();

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                        .commit();

                if(position == 0){
                    ServiceMan_newRequest sserviceMan_newRequest = new ServiceMan_newRequest();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, sserviceMan_newRequest)
                            .commit();
                }
                if(position == 1){
                    AceptAndActiveRequest aceptAndActiveRequest = new AceptAndActiveRequest();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, aceptAndActiveRequest)
                            .commit();
                }
                if(position ==2) {
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                            .commit();
                    ServiceMan_pastHistoryRequest serviceMan_pastHistoryRequest = new ServiceMan_pastHistoryRequest();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, serviceMan_pastHistoryRequest)
                            .commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_service_man, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_about) {

            Intent serviceProfile = new Intent(MainServiceManActivity.this,AboutServiceman.class);
            startActivity(serviceProfile);
            return true;
        }
        if (id == R.id.action_update) {

            Intent serviceProfile = new Intent(MainServiceManActivity.this,ServiceManProfileBuilding.class);
            startActivity(serviceProfile);
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPref.init(getApplicationContext());
            SharedPref.write(SharedPref.ID,0);
            SharedPref.write(SharedPref.IS_SIGN_IN,false);
            SharedPref.write(SharedPref.ACCOUNT,"no");
            SharedPref.writeObject(SharedPref.OBJECT,null);
            Intent login = new Intent(MainServiceManActivity.this , SignInActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }



}
