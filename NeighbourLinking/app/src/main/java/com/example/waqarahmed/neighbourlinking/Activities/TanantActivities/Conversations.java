package com.example.waqarahmed.neighbourlinking.Activities.TanantActivities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Adapter.ChatMessageAdapter;
import com.example.waqarahmed.neighbourlinking.Classes.Message;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Shared.SharedPref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Conversations extends AppCompatActivity {

private RecyclerView mRecyclerView;
    private Button mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    String receiverId,userName,senderId , receverName;
    DatabaseReference mConversation , mUser;
    String currentUserId;
    FirebaseAuth mAuth;


    String chat_msg , chat_user_name;
    String room_1 , room_2;
    private ChatMessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mButtonSend = (Button) findViewById(R.id.btnn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        mImageView = (ImageView) findViewById(R.id.iv_image);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ChatMessageAdapter(this, new ArrayList<Message>());
        mRecyclerView.setAdapter(mAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        receiverId = getIntent().getExtras().get("receverId").toString();
        receverName = getIntent().getExtras().get("receverName").toString();
        getSupportActionBar().setTitle(receverName);


        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            currentUserId = mAuth.getCurrentUser().getUid().toString();
            mUser = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
            mUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userName = dataSnapshot.child("first_name").getValue().toString();
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

        }
        else{
            SharedPref.init(this);
           // currentUserId = String.valueOf(SharedPref.read(SharedPref.ID,0));
            ServiceMan serviceMan = SharedPref.readObject(SharedPref.OBJECT,null);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.serviceman_toolbr)));
            int id = serviceMan.getId();
            currentUserId = String.valueOf(id);
            userName = serviceMan.getName();

        }

//        room_1 = mAuth.getCurrentUser().getUid() + "_" + receiverId;
//        room_2 = receiverId + "_" + mAuth.getCurrentUser().getUid();
        room_1 = currentUserId + "_" + receiverId;
        room_2 = receiverId + "_" + currentUserId;
        mConversation = FirebaseDatabase.getInstance().getReference().child("Conversation");

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mConversation.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(room_1)) {
                            HashMap<String, Object> map1 = new HashMap<String, Object>();
                            String key = mConversation.child(room_1).push().getKey();
                            mConversation.updateChildren(map1);

                            DatabaseReference msg_root = mConversation.child(room_1).child(key);
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("recverId", receiverId);
                            map2.put("senderId", currentUserId);
                            map2.put("name", userName);
                            map2.put("msg", mEditTextMessage.getText().toString());
                            msg_root.updateChildren(map2);
                            mEditTextMessage.setText("");
                        } else if (dataSnapshot.hasChild(room_2)) {
                            HashMap<String, Object> map1 = new HashMap<String, Object>();
                            String key = mConversation.child(room_2).push().getKey();
                            mConversation.updateChildren(map1);

                            DatabaseReference msg_root = mConversation.child(room_2).child(key);
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("recverId", receiverId);
                            map2.put("senderId", mAuth.getCurrentUser().getUid());
                            map2.put("name", userName);
                            map2.put("msg", mEditTextMessage.getText().toString());
                            msg_root.updateChildren(map2);
                            mEditTextMessage.setText("");
                        } else {
                            HashMap<String, Object> map1 = new HashMap<String, Object>();
                            String key = mConversation.child(room_1).push().getKey();
                            mConversation.updateChildren(map1);
                            DatabaseReference msg_root = mConversation.child(room_1).child(key);
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("recverId", receiverId);
                            map2.put("senderId", currentUserId);
                            map2.put("name", userName);
                            map2.put("msg", mEditTextMessage.getText().toString());
                            msg_root.updateChildren(map2);
                            mEditTextMessage.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        mConversation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(room_1)){
                    mConversation.child(room_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s)
                        {

                            Message m = dataSnapshot.getValue(Message.class);
                            mAdapter.add(m);

                            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s)
                        {
                             //todo
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot)
                        {
                            //todo
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s)
                        {
                            //todo
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {
                            //todo
                        }
                    });
                }
                else if(dataSnapshot.hasChild(room_2)){
                    mConversation.child(room_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s)
                        {
                            Message m = dataSnapshot.getValue(Message.class);
                            mAdapter.add(m);
                           // sendNotification(m);
                            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s)
                        {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot)
                        {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s)
                        {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }
                    });
                }
                else{

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mConversation.keepSynced(true);
      //  mUser.keepSynced(true);
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
