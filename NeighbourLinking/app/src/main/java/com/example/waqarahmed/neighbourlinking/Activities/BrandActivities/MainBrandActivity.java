package com.example.waqarahmed.neighbourlinking.Activities.BrandActivities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import com.example.waqarahmed.neighbourlinking.Fragments.Home_Wall;
import com.example.waqarahmed.neighbourlinking.Fragments.UserProfile;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.BrandServices.RetrievBranProfileInfo;
import com.example.waqarahmed.neighbourlinking.Shared.BrandSharedPref;

public class MainBrandActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_brand);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.brand_toolbr)));
        setTitle("N 2 N");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        BrandSharedPref.init(this);
        String isAccountSetup = BrandSharedPref.read(BrandSharedPref.ACCOUNT,"no");
        if(isAccountSetup.equals("no")){

            startActivity(new Intent(MainBrandActivity.this,BrandProfileBuilding.class));
            finish();
        }
        else{

            RetrievBranProfileInfo retrievBranProfileInfo = new RetrievBranProfileInfo(this);
            retrievBranProfileInfo.execute();
        }



        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setBackground(new ColorDrawable(getResources().getColor(R.color.brand_toolbr)));


    }

    @Override
    protected void onStart() {
        super.onStart();

//        BrandSharedPref.init(this);
//        String isAccountStup = BrandSharedPref.read(BrandSharedPref.ACCOUNT,"no");
//        if(isAccountStup.equals("no")){
//
//        }else {
//            final Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(20000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            Brand brand = BrandSharedPref.readObject(BrandSharedPref.OBJECT,null);
//            if(!brand.equals(null)){
//                getSupportActionBar().setTitle(brand.getName());
//
//            }
//    }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_brand, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            Intent BreandProfileIntent = new Intent(MainBrandActivity.this,BrandProfileBuilding.class);
            startActivity(BreandProfileIntent);
            return true;
        }*/
        if (id == R.id.action_logout) {
            BrandSharedPref.init(getApplicationContext());
            BrandSharedPref.write(BrandSharedPref.ID,0);
            BrandSharedPref.write(BrandSharedPref.IS_SIGN_IN,false);
            BrandSharedPref.write(BrandSharedPref.ACCOUNT,"no");
            BrandSharedPref.writeObject(BrandSharedPref.OBJECT,null);
            Intent login = new Intent(MainBrandActivity.this , SignInActivity.class);  // then call log in page
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



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_brand, container, false);
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

//            BrandSharedPref.init(getApplicationContext());
//            int s = BrandSharedPref.read(BrandSharedPref.ID,0);
            switch (position) {
                case 0:
                    Home_Wall home_wall = new Home_Wall();
                    return home_wall;
                case 1:
//                    MyPosts myPosts = new MyPosts();
//                    return myPosts;
                    UserProfile userProfile = new UserProfile();
                    return userProfile;


                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
                case 1:
                    return "Profile";

            }
            return null;
        }
    }
    public void RefreshActivity(){ onRestart();
    }
}
