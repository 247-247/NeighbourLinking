package com.example.waqarahmed.neighbourlinking.Activities.AdminActivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;

import com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities.AboutServiceman;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities.ServiceManProfileBuilding;
import com.example.waqarahmed.neighbourlinking.Activities.SignInActivity;
import com.example.waqarahmed.neighbourlinking.Fragments.AdminFragments.AcceptAndNeedToFullfill_adminFrgment;
import com.example.waqarahmed.neighbourlinking.Fragments.AdminFragments.AllnewRequest_adminFrgment;
import com.example.waqarahmed.neighbourlinking.Fragments.AdminFragments.PastHistoryRequest_adminFragment;
import com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment.AceptAndActiveRequest;
import com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment.ServiceMan_newRequest;
import com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment.ServiceMan_pastHistoryRequest;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.RetrievServicemanProfileInfo;
import com.example.waqarahmed.neighbourlinking.Shared.SharedPref;

public class MainServiceActivity_Admin extends AppCompatActivity {
  FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_service_man);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
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
                    AllnewRequest_adminFrgment sserviceMan_newRequest = new AllnewRequest_adminFrgment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, sserviceMan_newRequest)
                            .commit();
                }
                if(position == 1){
                    AcceptAndNeedToFullfill_adminFrgment aceptAndActiveRequest = new AcceptAndNeedToFullfill_adminFrgment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, aceptAndActiveRequest)
                            .commit();
                }
                if(position ==2) {
                    PastHistoryRequest_adminFragment serviceMan_pastHistoryRequest = new PastHistoryRequest_adminFragment();
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
 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_service_man, menu);
        return true;
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }
    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new Helper(context);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
