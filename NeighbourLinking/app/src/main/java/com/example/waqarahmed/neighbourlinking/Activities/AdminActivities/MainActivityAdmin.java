package com.example.waqarahmed.neighbourlinking.Activities.AdminActivities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.AllContacts;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Announcements;
import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.MainBrandActivity;
import com.example.waqarahmed.neighbourlinking.Activities.NewRegistrations;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Offers;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Profile;
import com.example.waqarahmed.neighbourlinking.Activities.RequestRelatedDetail;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities.MainServiceManActivity;
import com.example.waqarahmed.neighbourlinking.Activities.ShowAllServices;
import com.example.waqarahmed.neighbourlinking.Activities.SignInActivity;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities._Post_simpleWall;
import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.Classes.BadgeUtils;
import com.example.waqarahmed.neighbourlinking.Classes.Pager;
import com.example.waqarahmed.neighbourlinking.Classes.RoundedTransformation;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Shared.AdminSharedPref;
import com.example.waqarahmed.neighbourlinking.Shared.BrandSharedPref;
import com.example.waqarahmed.neighbourlinking.Shared.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

//import com.example.waqarahmed.neighbourlinking.Classes.Pager;

public class MainActivityAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseReference mDatabaseReferenceUser , mDatabaseCurrentUser;
    FirebaseAuth mAuth;
    ImageView mHeaderImgView;
    TextView mHeaderName;
    TextView mHeaderCity;

    FirebaseAuth.AuthStateListener authStateListener;
    TabLayout.OnTabSelectedListener tabSelectedListener;
    static ViewPager viewPager;
    TabLayout tabLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setBackground(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        AppUtils.savePreferenceLong("NOTICOUNT",0,this);
        BadgeUtils.clearBadge(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabListner());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        int m = navigationView.getMenu().findItem(R.id.nav_announcements).getItemId();
        View headerView = navigationView.getHeaderView(0);
        mHeaderImgView = (ImageView) headerView.findViewById(R.id.imageView);
        mHeaderName = (TextView) headerView.findViewById(R.id.header_name);
        mHeaderCity = (TextView) headerView.findViewById(R.id.textView);
      //  announcements_drwr= (TextView) navigationView.getMenu().findItem(R.id.nav_announcements);
       setMenuCounter(m,2);
        mAuth = FirebaseAuth.getInstance();
       //  mAuth.signOut();
        SharedPref.init(getApplicationContext());
        BrandSharedPref.init(getApplicationContext());
        final boolean isServiceManSignIn = SharedPref.read(SharedPref.IS_SIGN_IN,false);
        final boolean isBranSignIn = BrandSharedPref.read(BrandSharedPref.IS_SIGN_IN,false);
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){  //if tanant not sign in
                    if(isServiceManSignIn){                 // then chk service man sign in
                        Intent servceManIntent = new Intent(MainActivityAdmin.this, MainServiceManActivity.class);
                        startActivity(servceManIntent);

                    }else{                              // then if service man not sign in
                        if(isBranSignIn){                 // chk for Brand sign in
                            Intent BrandMainIntent = new Intent(MainActivityAdmin.this, MainBrandActivity.class);
                            startActivity(BrandMainIntent);
                        }else{                            // if brand not sign in
                            Intent login = new Intent(MainActivityAdmin.this , SignInActivity.class);  // then call log in page
                             login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                  startActivity(login);

                        }
                    }

                }else{

                }
            }
        };

      if(mAuth.getCurrentUser() != null)
      {
          mDatabaseCurrentUser = mDatabaseReferenceUser.child(mAuth.getCurrentUser().getUid());
          final String mCity;
          mDatabaseCurrentUser.addValueEventListener(new ValueEventListener()
          {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                 if(dataSnapshot.hasChild("image")){
                  final String mImageURL = dataSnapshot.child("image").getValue().toString();
                  String mName = dataSnapshot.child("first_name").getValue().toString();
                  String mCity = dataSnapshot.child("city").getValue().toString();
                  Picasso.with(getApplicationContext()).load(mImageURL).transform(new RoundedTransformation(50, 4))
                          .centerCrop().resize(70, 70).networkPolicy(NetworkPolicy.OFFLINE).into(mHeaderImgView, new Callback() {
                      @Override
                      public void onSuccess() {

                      }
                      @Override
                      public void onError() {
                          Picasso.with(getApplicationContext()).load(mImageURL).transform(new RoundedTransformation(50, 4)).centerCrop().resize(80, 80).into(mHeaderImgView);
                      }
                  });
                  mHeaderName.setText(mName);
                  mHeaderCity.setText("Admin");
              }
              else
                 {
                     Intent intent = new Intent(MainActivityAdmin.this,Profile.class);  // build profile
                     startActivity(intent);
                 }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
      }
      checkUserExists();

    }
    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_admin_activity, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sign_out)
        {
            AdminSharedPref.init(MainActivityAdmin.this);
            AdminSharedPref.write(AdminSharedPref.IS_ADMIN,"no");
            AdminSharedPref.write(AdminSharedPref.IMAGE,null);
            AdminSharedPref.write(AdminSharedPref.EMAIL,null);
            mAuth.signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_post) {
            // Handle the camera action
            Intent in = new Intent(MainActivityAdmin.this , _Post_simpleWall.class);
            startActivity(in);
        } else if (id == R.id.nav_wall) {


        } else if (id == R.id.nav_announcements) {
            Intent in = new Intent(MainActivityAdmin.this , Announcements.class);
            startActivity(in);

        } else if (id == R.id.nav_message)
        {
            Intent in = new Intent(MainActivityAdmin.this , AllContacts.class);
            startActivity(in);
        }
        else if (id == R.id.nav_offer) {
            Intent in = new Intent(MainActivityAdmin.this , Offers.class);
            startActivity(in);
        }
        else if (id == R.id.nav_service) {
            Intent in = new Intent(MainActivityAdmin.this , ShowAllServices_admin.class);
            startActivity(in);
        }

        else if (id == R.id.nav_notification_write) {
            Intent in = new Intent(MainActivityAdmin.this , CreateNotification.class);
            startActivity(in);


        } else if (id == R.id.nav_registaration) {
            Intent in = new Intent(MainActivityAdmin.this , NewRegistrations.class);
            startActivity(in);
        }
        else if (id == R.id.nav_servicemanlst) {
//            Intent in = new Intent(MainActivityAdmin.this , AllServiceMenList.class);
//            startActivity(in);
         Intent in = new Intent(MainActivityAdmin.this , AdminMainServiceManActivty.class);
           startActivity(in);


        }
        else if (id == R.id.nav_brandlst) {
            Intent in = new Intent(MainActivityAdmin.this , AdminMainBrandActivty.class);
            startActivity(in);


        }
        else if (id == R.id.nav_requestlst) {
            Intent in = new Intent(MainActivityAdmin.this , MainServiceActivity_Admin.class);
           startActivity(in);
        }
        else{

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        connectionCheck();
        mAuth.addAuthStateListener(authStateListener);
    }
    private void checkUserExists() {
        mDatabaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mAuth.getCurrentUser() != null) {
                    String currentUserId = mAuth.getCurrentUser().getUid().toString();             // mAuth.getCurrentUser().getUid();
                    if (!dataSnapshot.hasChild(currentUserId)) {
                        Intent AccuntSetupIntent = new Intent(MainActivityAdmin.this, Profile.class);
                        AccuntSetupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(AccuntSetupIntent);

                    }
                }else{


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static class TabListner implements TabLayout.OnTabSelectedListener{

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    public void connectionCheck(){
        if (AppStatus.getInstance(this).isOnline()) {


        } else {


            Toast.makeText(MainActivityAdmin.this,"You are Offline!!!",Toast.LENGTH_LONG).show();
        }

    }
}
