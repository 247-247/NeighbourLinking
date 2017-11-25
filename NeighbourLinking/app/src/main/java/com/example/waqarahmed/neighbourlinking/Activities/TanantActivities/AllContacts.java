package com.example.waqarahmed.neighbourlinking.Activities.TanantActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.waqarahmed.neighbourlinking.Classes.Contact;
import com.example.waqarahmed.neighbourlinking.Listener.RecyclerItemClickListener;
import com.example.waqarahmed.neighbourlinking.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllContacts extends AppCompatActivity {
    RecyclerView contectRecyclerView;
    DatabaseReference mDatabaseReferenceUser;
    FirebaseAuth mAuth;
    private AdView mAdView;
    List<Contact> list = new ArrayList<Contact>();
    List<String> mStringList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);
        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        contectRecyclerView = (RecyclerView) findViewById(R.id.contactRecycler);
        contectRecyclerView.setHasFixedSize(true);
        contectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        DatabaseReference currentUser_Database = mDatabaseReferenceUser.child(mAuth.getCurrentUser().getUid().toString());
        currentUser_Database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String isAdmin = (String) dataSnapshot.child("isAdmin").getValue();
                if(isAdmin.equals("Yes")){
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
                }else {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tanat_toolbar)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        contectRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Contact c = list.get(position);

                        Intent convIntent = new Intent(AllContacts.this,Conversations.class);
                        convIntent.putExtra("receverId",c.getUid());
                        convIntent.putExtra("receverName",c.getFirst_name());

                        startActivity(convIntent);
                    }
                })
        );

        // ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    protected void onStart() {
        super.onStart();


        mDatabaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    DataSnapshot d = iterator.next();
                    if (d.hasChild("image")) {
                        Contact c = d.getValue(Contact.class);
                        if (!TextUtils.equals(c.getUid(), mAuth.getCurrentUser().getUid())) {

                            list.add(c);
                            Log.i("HHHHHHWWWWW", "onDataChange: " + c.getFirst_name());


                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        RVAdapter rvAdapter = new RVAdapter(list , getApplicationContext());
        contectRecyclerView.setAdapter(rvAdapter);
    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.contactViewHolder> {


        List<Contact> list;
        Context mContext;

        RVAdapter(List<Contact> userList, Context context) {
            list = userList;
            mContext = context;
        }

        @Override
        public contactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_item, parent, false);
            contactViewHolder cvh = new contactViewHolder(v);
            return cvh;
        }

        @Override
        public void onBindViewHolder(contactViewHolder holder, int position) {
            Contact contact = list.get(position);

            holder.setContact_name(contact.getFirst_name());
             holder.setContact_image(contact.getImage(),mContext);
            holder.setContact_date(contact.getAddress());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

            public  class contactViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView imageView ;
        TextView nameView,dateView;

        public contactViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            imageView = (ImageView)mView.findViewById(R.id.contentImage_imageView_contentView);
            nameView = (TextView) itemView.findViewById(R.id.contentName_textView_contentView);
            dateView = (TextView)itemView.findViewById(R.id.created_date_contentView);
        }
        public void setContact_name(String name) {

            nameView.setText(name);

        }
        public void setContact_image(final String image , final Context cxt) {
            Picasso.with(cxt).load(image).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(cxt).load(image).centerCrop().resize(75,75).into(imageView);
                }
            });
        }
        public void setContact_date(String date) {

            dateView.setText(date);

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
