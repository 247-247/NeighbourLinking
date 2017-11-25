package com.example.waqarahmed.neighbourlinking.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.CommentsOnPost;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Delete_Tanant_Post;
import com.example.waqarahmed.neighbourlinking.Classes.Post;
import com.example.waqarahmed.neighbourlinking.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class _Home_wall extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference mDatabaseReferencePost;
    DatabaseReference mDatabaseReferenceUser;
    FirebaseAuth mAuth;
    DatabaseReference mDatabasePostLike;
    DatabaseReference mDatabasePostComment;
    boolean mLikeProcess = false;
    public _Home_wall() {
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
        View view =inflater.inflate(R.layout.fragment___home_wall, container, false);
        Log.i("TTTTT","TTTTTTTTT1");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclr_post);
         //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReferencePost= FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabasePostLike = FirebaseDatabase.getInstance().getReference().child("Post_Like");
        mDatabasePostComment = FirebaseDatabase.getInstance().getReference().child("Post_Comment");
        mDatabaseReferencePost.keepSynced(true);
        mDatabaseReferenceUser.keepSynced(true);
        mDatabasePostLike.keepSynced(true);
        return  view;


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("TAGTAGGGGG","TTTTTTTTT");

        FirebaseRecyclerAdapter<Post,_Home_wall.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, _Home_wall.PostViewHolder>(
                Post.class,
                R.layout.post_row,
                _Home_wall.PostViewHolder.class,
                mDatabaseReferencePost
        ) {
            @Override
            protected void populateViewHolder(_Home_wall.PostViewHolder viewHolder, Post model, final int position) {
                final String post_key;
                post_key = getRef(position).getKey().toString();
                viewHolder.setName(model.getUsername());
                viewHolder.setSenderProfileImage(getActivity().getApplicationContext(),model.getSender_image());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setDate(model.getSend_date());

                viewHolder.setMlikebtn(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String post = getRef(position).toString();
                        Intent intent = new Intent(getActivity(), Delete_Tanant_Post.class);
                        intent.putExtra("post_key",post_key);
                       startActivity(intent);
                     //   Toast.makeText(getActivity(),post_key,Toast.LENGTH_SHORT).show();

                    }
                });
                viewHolder.mlikebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLikeProcess = true;
                        mDatabasePostLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(mLikeProcess)
                                {
                                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                                        mDatabasePostLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mLikeProcess = false;
                                    }
                                    else {
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


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



public static class PostViewHolder extends RecyclerView.ViewHolder{

    View mView;
    TextView post_title,likeCount,commentCount;
    ImageView post_desc;
    ImageButton mlikebtn;
    ImageButton mComment;
    FirebaseAuth mAuth;
    DatabaseReference mDatabasePostLike;
    DatabaseReference mDatabasePostCommment;

    public PostViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        mComment = (ImageButton) mView.findViewById(R.id.comment_btn_post_row);
        post_title = (TextView) mView.findViewById(R.id.titleShow_post_row);
         likeCount = (TextView) mView.findViewById(R.id.like_count);
        commentCount = (TextView) mView.findViewById(R.id.comment_count);
        mlikebtn = (ImageButton) itemView.findViewById(R.id.like_btn_post_row);
        mAuth = FirebaseAuth.getInstance();
        mDatabasePostLike = FirebaseDatabase.getInstance().getReference().child("Post_Like");
        mDatabasePostCommment = FirebaseDatabase.getInstance().getReference().child("Post_Comment");
        mDatabasePostLike.keepSynced(true);
    }
    public void setMlikebtn(final String post_key){

        mDatabasePostLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mAuth.getCurrentUser() != null){
                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                        likeCount.setText("likes"+" "+String.valueOf(dataSnapshot.child(post_key).getChildrenCount()));
                       // commentCount.setText(String.valueOf(dataSnapshot.child(post_key).getChildrenCount()));
                        mlikebtn.setImageResource(R.drawable.icon_blue_like_24);
                        mDatabasePostCommment.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                commentCount.setText("comments"+" "+String.valueOf(dataSnapshot.getChildrenCount()));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else{
                        likeCount.setText("Like"+" "+String.valueOf(dataSnapshot.child(post_key).getChildrenCount()));
                        mlikebtn.setImageResource(R.drawable.icon_black_like_24);
                        mDatabasePostCommment.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                commentCount.setText("comments"+" "+String.valueOf(dataSnapshot.getChildrenCount()));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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

}
