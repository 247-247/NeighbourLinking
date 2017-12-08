package com.example.waqarahmed.neighbourlinking.Activities.TanantActivities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.AdminActivities.MainActivityAdmin;
import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.MainBrandActivity;
import com.example.waqarahmed.neighbourlinking.Activities.NewRegistrations;
import com.example.waqarahmed.neighbourlinking.Activities.RequestRelatedDetail;
import com.example.waqarahmed.neighbourlinking.Activities.ServiceManActivities.MainServiceManActivity;
import com.example.waqarahmed.neighbourlinking.Activities.ShowAllServices;
import com.example.waqarahmed.neighbourlinking.Activities.SignInActivity;
import com.example.waqarahmed.neighbourlinking.Classes.AppStatus;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.Classes.BadgeUtils;
//import com.example.waqarahmed.neighbourlinking.Classes.Pager;
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

public class MainActivity extends AppCompatActivity
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tanat_toolbar)));
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.setBackground(new ColorDrawable(getResources().getColor(R.color.tanat_toolbar)));
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

        // mAuth.signOut();
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
                        Intent servceManIntent = new Intent(MainActivity.this, MainServiceManActivity.class);
                        servceManIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        servceManIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        servceManIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(servceManIntent);

                    }else{                              // then if service man not sign in
                        if(isBranSignIn){                 // chk for Brand sign in
                            Intent BrandMainIntent = new Intent(MainActivity.this, MainBrandActivity.class);
                            BrandMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            BrandMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            BrandMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(BrandMainIntent);
                        }else{  // if brand not sign in
                            Intent login = new Intent(MainActivity.this , SignInActivity.class);  // then call log in page
                            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(login);
                        }
                    }

                }else{
                    AdminSharedPref.init(MainActivity.this);
                    String isAdminLogin = AdminSharedPref.read(AdminSharedPref.IS_ADMIN,"no");
                    if(isAdminLogin.equals("yes")){
                        Intent AdminMainIntent = new Intent(MainActivity.this, MainActivityAdmin.class);
                        AdminMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        AdminMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        AdminMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(AdminMainIntent);
                    }
                    else{
                         // running the simple tanant view
                    }
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
                     // Header image
                  Picasso.with(getApplicationContext()).load(mImageURL).transform(new RoundedTransformation(50, 4))
                          .centerCrop().resize(65, 65).networkPolicy(NetworkPolicy.OFFLINE).into(mHeaderImgView, new Callback() {
                      @Override
                      public void onSuccess() {

                           //todo
                      }

                      @Override
                      public void onError() {
                          Picasso.with(getApplicationContext()).load(mImageURL).transform(new RoundedTransformation(50, 4)).centerCrop().resize(65, 65).into(mHeaderImgView);
                      }
                  });
                  mHeaderName.setText(mName);
                  mHeaderCity.setText(mCity);
              }else{

                     Intent intent = new Intent(MainActivity.this,Profile.class);  // build profile
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
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_logout) {
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
            Intent in = new Intent(MainActivity.this , _Post_simpleWall.class);
            startActivity(in);
        } else if (id == R.id.nav_wall) {

          //todo

        } else if (id == R.id.nav_announcements) {
            Intent in = new Intent(MainActivity.this , Announcements.class);
            startActivity(in);
        } else if (id == R.id.nav_message)
        {
            Intent in = new Intent(MainActivity.this , AllContacts.class);
            startActivity(in);
        }
        else if (id == R.id.nav_offer) {
            Intent in = new Intent(MainActivity.this , Offers.class);
            startActivity(in);
        }
        else if (id == R.id.nav_service) {
            Intent in = new Intent(MainActivity.this , ShowAllServices.class);
            startActivity(in);
        }

         else if (id == R.id.nav_send) {
            Intent in = new Intent(MainActivity.this , Requests_Tanants.class);
            startActivity(in);

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
                        Intent AccuntSetupIntent = new Intent(MainActivity.this, Profile.class);
                        AccuntSetupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(AccuntSetupIntent);
                    }
                }else{

                       //todo
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

            Toast.makeText(MainActivity.this,"You are Offline!!!",Toast.LENGTH_LONG).show();
        }

    }
}
