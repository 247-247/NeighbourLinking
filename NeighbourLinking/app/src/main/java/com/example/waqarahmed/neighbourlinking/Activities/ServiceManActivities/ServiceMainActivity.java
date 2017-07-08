package com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.waqarahmed.neighbourlinking.Activities.SignInActivity;
import com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment.AceptAndActiveRequest;
import com.example.waqarahmed.neighbourlinking.Fragments.ServiceManFragment.ServiceMan_newRequest;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.ServiceManServices.RetrievServicemanProfileInfo;
import com.example.waqarahmed.neighbourlinking.Shared.SharedPref;

public class ServiceMainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Neighbour 2 Neighbour");


        SharedPref.init(this);
        String isAccountSetup = SharedPref.read(SharedPref.ACCOUNT,"no");
        if(isAccountSetup.equals("no")){

            startActivity(new Intent(ServiceMainActivity.this,ServiceManProfileBuilding.class));
            finish();
        }
        else{

            RetrievServicemanProfileInfo retrievServicemanProfileInfo = new RetrievServicemanProfileInfo(this);
            retrievServicemanProfileInfo.execute();
        }


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

      //  mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

             Intent serviceProfile = new Intent(ServiceMainActivity.this,AboutServiceman.class);
             startActivity(serviceProfile);
            return true;
        }
        if (id == R.id.action_update) {

            Intent serviceProfile = new Intent(ServiceMainActivity.this,ServiceManProfileBuilding.class);
            startActivity(serviceProfile);
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPref.init(getApplicationContext());
            SharedPref.write(SharedPref.ID,0);
            SharedPref.write(SharedPref.IS_SIGN_IN,false);
            SharedPref.write(SharedPref.ACCOUNT,"no");
            SharedPref.writeObject(SharedPref.OBJECT,null);
            Intent login = new Intent(ServiceMainActivity.this , SignInActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_service_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            SharedPref.init(getApplicationContext());
            int s = SharedPref.read(SharedPref.ID,0);
//            SharedPref.write(SharedPref.ID, employeeId);//save int in shared preference.
//            SharedPref.write(SharedPref.IS_SIGN_IN, true);//save boolean in shared preference.
            if(position == 0){
            ServiceMan_newRequest serviceMan_newRequest = new ServiceMan_newRequest();
            return serviceMan_newRequest;
        }
           if(position == 1){
               AceptAndActiveRequest aceptAndActiveRequest = new AceptAndActiveRequest();

               return aceptAndActiveRequest;

          }

            return PlaceholderFragment.newInstance(1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "New";
                case 1:
                    return "History";
                case 2:
                    return "Panding";
            }
            return null;
        }
    }
}
