package com.example.waqarahmed.neighbourlinking.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.MainBrandActivity;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.About;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.CommentsOnPost;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities._Post_simpleWall;
import com.example.waqarahmed.neighbourlinking.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class MyPosts extends Fragment {

    ImageView mCurrentUser_imageView;
    TextView mCurrentUserName_textView , mAccountCreated_date_textView;
    Button mCurrentUserAbout_btn,mCurrentUserUpdatetd_btn,mCurrentUserPost_btn;
    RecyclerView recyclerView;
    DatabaseReference mDatabaseReferencePost;
    DatabaseReference mDatabaseReferenceUser;
    FirebaseAuth mAuth;
    DatabaseReference mDatabasePostLike;
    DatabaseReference mDatabasePostComment;
    boolean mLikeProcess = false;
    String currentUserId;
    DatabaseReference mCurrentUserProfileDatabaseRefence;
    DatabaseReference mCurrentUserInFo_fireBaseDbRef;
    Query  mCurrentUserProfileQuery;



    public MyPosts() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_posts, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclrProfile);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

     //   recyclerView = (RecyclerView) view.findViewById(R.id.recyclrProfile);
        mCurrentUser_imageView = (ImageView) view.findViewById(R.id.currentUser_imageView);
        mCurrentUserName_textView= (TextView) view.findViewById(R.id.currentUserName_textView);
        mAccountCreated_date_textView = (TextView) view.findViewById(R.id.accountCreated_date);

        mCurrentUserAbout_btn= (Button) view.findViewById(R.id.currentUserAbout);
        mCurrentUserUpdatetd_btn = (Button) view.findViewById(R.id.currentUserUpdatetd);
        mCurrentUserPost_btn= (Button) view.findViewById(R.id.currentUserPost);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReferencePost= FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabasePostLike = FirebaseDatabase.getInstance().getReference().child("Post_Like");
        mDatabasePostComment = FirebaseDatabase.getInstance().getReference().child("Post_Comment");
        mDatabaseReferencePost.keepSynced(true);
        mDatabaseReferenceUser.keepSynced(true);
        mDatabasePostLike.keepSynced(true);
        mCurrentUserPost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Post_Intent = new Intent(getActivity().getApplicationContext(), _Post_simpleWall.class);
                startActivity(Post_Intent);
            }
        });
        mCurrentUserAbout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent About_Intent = new Intent(getActivity().getApplicationContext(), About.class);
                startActivity(About_Intent);
            }
        });

        if(mAuth.getCurrentUser() != null) {

            currentUserId = mAuth.getCurrentUser().getUid();
            mCurrentUserInFo_fireBaseDbRef = mDatabaseReferenceUser.child(currentUserId);

            mCurrentUserInFo_fireBaseDbRef.keepSynced(true);
        }


        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            if(!currentUserId.equals(null)) {

                mCurrentUserInFo_fireBaseDbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("image")) {
                            String name = dataSnapshot.child("first_name").getValue().toString();
                              String create_date = dataSnapshot.child("create_date").getValue().toString();
                            final String imageUrl = dataSnapshot.child("image").getValue().toString();
                                   mCurrentUserName_textView.setText(name);
                                   mAccountCreated_date_textView.setText(create_date);
                            Picasso.with(getActivity().getApplicationContext()).load(imageUrl).centerCrop().resize(75, 75).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentUser_imageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(getActivity().getApplicationContext()).load(imageUrl).centerCrop().resize(75, 75).into(mCurrentUser_imageView);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mCurrentUserProfileQuery= mDatabaseReferencePost.orderByChild("uid").equalTo(currentUserId);
                FirebaseRecyclerAdapter<com.example.waqarahmed.neighbourlinking.Classes.Post, _Home_wall.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<com.example.waqarahmed.neighbourlinking.Classes.Post, _Home_wall.PostViewHolder>(
                        com.example.waqarahmed.neighbourlinking.Classes.Post.class,
                        R.layout.post_row,
                        _Home_wall.PostViewHolder.class,
                        mCurrentUserProfileQuery
                ) {
                    @Override
                    protected void populateViewHolder(_Home_wall.PostViewHolder viewHolder, com.example.waqarahmed.neighbourlinking.Classes.Post model, final int position) {
                        final String post_key;
                        post_key = getRef(position).getKey().toString();
                        viewHolder.setName(model.getUsername());
                        viewHolder.setSenderProfileImage(getActivity().getApplicationContext(), model.getSender_image());
                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setDate(model.getSend_date());

                        viewHolder.setMlikebtn(post_key);

                        viewHolder.post_title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String post = getRef(position).toString();
                                Toast.makeText(getActivity(), post_key, Toast.LENGTH_SHORT).show();

                            }
                        });
                        viewHolder.mlikebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mLikeProcess = true;
                                mDatabasePostLike.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (mLikeProcess) {
                                            if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                                                mDatabasePostLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                                mLikeProcess = false;
                                            } else {
                                                mDatabasePostLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("AnyValue");
                                                mLikeProcess = false;
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                        viewHolder.mComment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent comment_Intent = new Intent(getActivity().getApplicationContext(), CommentsOnPost.class);
                                comment_Intent.putExtra("postKey", post_key);
                                startActivity(comment_Intent);

                            }
                        });

                    }
                };

                recyclerView.setAdapter(firebaseRecyclerAdapter);

    }}
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView post_title;
        ImageView post_desc;
        ImageButton mlikebtn;
        ImageButton mComment;
        FirebaseAuth mAuth;
        DatabaseReference mDatabasePostLike;

        public PostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mComment = (ImageButton) mView.findViewById(R.id.comment_btn_post_row);
            post_title = (TextView) mView.findViewById(R.id.titleShow_post_row);

            mlikebtn = (ImageButton) itemView.findViewById(R.id.like_btn_post_row);
            mAuth = FirebaseAuth.getInstance();
            mDatabasePostLike = FirebaseDatabase.getInstance().getReference().child("Post_Like");
            mDatabasePostLike.keepSynced(true);
        }
        public void setMlikebtn(final String post_key){

            mDatabasePostLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(mAuth.getCurrentUser() != null){
                        if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                            mlikebtn.setImageResource(R.drawable.thumb_up_like_colored);

                        }else{

                            mlikebtn.setImageResource(R.drawable.thumb_up_blck);
                        }
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }
        public void setTitle(String title){

            post_title.setText(title);

        }
        public void setDate(String date){
            TextView post_date = (TextView) mView.findViewById(R.id.sender_date_post_row);
            post_date.setText(date);

        }
        public void setDesc(String des){
            TextView post_desc = (TextView) mView.findViewById(R.id.descShow_post_row);
            post_desc.setText(des);

        }
        public void setSenderProfileImage(final Context cxt, final String image){
            final ImageView imageView1 = (ImageView) mView.findViewById(R.id.senderImage_imageView_post_row);

            Picasso.with(cxt).load(image).centerCrop().resize(75,75).networkPolicy(NetworkPolicy.OFFLINE).into(imageView1, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(cxt).load(image).centerCrop().resize(75,75).into(imageView1);
                }
            });
        }
        public void setName(String name){
            TextView post_title = (TextView) mView.findViewById(R.id.senderName_textView_post_row);
            post_title.setText(name);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onCreate(null);
    }
}
