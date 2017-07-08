package com.example.waqarahmed.neighbourlinking.Activities.TanantActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

//import com.example.waqarahmed.neighbourlinking.Classes.Pager;
import com.example.waqarahmed.neighbourlinking.Classes.Pager_offers;
import com.example.waqarahmed.neighbourlinking.Fragments.Home_Wall;
import com.example.waqarahmed.neighbourlinking.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Offers extends AppCompatActivity {

 /*   static ViewPager viewPager_offer;
    TabLayout tabLayout_offer;*/
    DatabaseReference mDatabaseReferenceUser , mDatabaseCurrentUser;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Current Offer");

        // Never Remove it bcx may i need it in future

//        tabLayout_offer= (TabLayout) findViewById(R.id.tabLayout_offer);
//        viewPager_offer= (ViewPager) findViewById(R.id.pager_offer);
//        tabLayout_offer.addTab(tabLayout_offer.newTab().setText("Home"));
//        tabLayout_offer.addTab(tabLayout_offer.newTab().setText("Profile"));
//
//        tabLayout_offer.setTabGravity(TabLayout.GRAVITY_FILL);
//        Pager_offers adapter = new Pager_offers(getSupportFragmentManager(), tabLayout_offer.getTabCount());
//        viewPager_offer.setAdapter(adapter);
//        viewPager_offer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout_offer));
//        tabLayout_offer.setOnTabSelectedListener(new Offers.TabListners());
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Home_Wall home_wall = new Home_Wall();
        fragmentTransaction.replace(R.id.fragment_container,home_wall);
        fragmentTransaction.commit();





    }


//    public static class TabListners implements TabLayout.OnTabSelectedListener{
//
//        @Override
//        public void onTabSelected(TabLayout.Tab tab) {
//            viewPager_offer.setCurrentItem(tab.getPosition());
//        }
//
//        @Override
//        public void onTabUnselected(TabLayout.Tab tab) {
//
//        }
//
//        @Override
//        public void onTabReselected(TabLayout.Tab tab) {
//
//        }
//    }
//
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
