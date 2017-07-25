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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.AboutBrand;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Comments;
import com.example.waqarahmed.neighbourlinking.Activities.Delete_Post;
import com.example.waqarahmed.neighbourlinking.Classes.Blog;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Shared.BrandSharedPref;
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


public class Home_Wall extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference mDatabaseReferenceBlog;
    DatabaseReference mDatabaseReferenceUser;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseLike;
    DatabaseReference mDatabaseComment;
    FirebaseAuth.AuthStateListener authStateListener;
    boolean mLikeProcess = false;
    String currentUserId;

    public Home_Wall() {
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
        View view =  inflater.inflate(R.layout.fragment_home__wall, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclr);
      //  recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReferenceBlog= FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
        mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Comment");
        if(mAuth.getCurrentUser() != null){
            currentUserId = mAuth.getCurrentUser().getUid().toString();

        }
        else {
            BrandSharedPref.init(getActivity().getApplicationContext());
            int id = BrandSharedPref.read(BrandSharedPref.ID,0);
            currentUserId  = String.valueOf(id);

        }

        mDatabaseReferenceBlog.keepSynced(true);
        mDatabaseReferenceUser.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabaseReferenceBlog
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, final Blog model, final int position) {
                final String post_key;
                post_key = getRef(position).getKey().toString();
                viewHolder.setName(model.getUsername());
                viewHolder.setSenderProfileImage(getActivity().getApplicationContext(),model.getSender_image());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setDate(model.getSend_date());
                viewHolder.setImage(getActivity().getApplicationContext(),model.getPost_image());
                viewHolder.setMlikebtn(post_key);
                viewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent BrandAboutInent = new Intent(getActivity().getApplicationContext(), AboutBrand.class);
                        BrandAboutInent.putExtra("id",model.getUid());
                        startActivity(BrandAboutInent);
                       // Toast.makeText(getActivity().getApplicationContext(),model.getUid(),Toast.LENGTH_SHORT).show();
                    }
                });


                viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  Toast.makeText(getActivity(),"Whole item Clicked",Toast.LENGTH_SHORT).show();
                         Intent Delete_Post_Intent = new Intent(getActivity(),Delete_Post.class);
                        Delete_Post_Intent.putExtra("post_key",post_key);
                         startActivity(Delete_Post_Intent);
                    }
                });
                viewHolder.post_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String post = getRef(position).toString();
                        Toast.makeText(getActivity(),post_key,Toast.LENGTH_SHORT).show();

                    }
                });
                viewHolder.mlikebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLikeProcess = true;
                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(mLikeProcess)
                                {
                                    if(dataSnapshot.child(post_key).hasChild(currentUserId)){
                                        mDatabaseLike.child(post_key).child(currentUserId).removeValue();
                                        mLikeProcess = false;
                                    }
                                    else {
                                        mDatabaseLike.child(post_key).child(currentUserId).setValue("AnyValue");
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
                        Intent comment_Intent = new Intent(getActivity().getApplicationContext(), Comments.class);
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
    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView post_title,likeCount,commentCount;
        ImageView post_image;
        ImageButton mlikebtn;
        ImageButton mComment;
        FirebaseAuth mAuth;
        ImageView   imageView1 ;
        DatabaseReference mDatabaseLike;
        DatabaseReference mDatabaseComment;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mComment = (ImageButton) mView.findViewById(R.id.comment_btn);
            post_title = (TextView) mView.findViewById(R.id.titleShow);
            post_image = (ImageView) mView.findViewById(R.id.imageShow);
            likeCount = (TextView) mView.findViewById(R.id.like_count);
            commentCount = (TextView) mView.findViewById(R.id.comment_count);
            mlikebtn = (ImageButton) itemView.findViewById(R.id.like_btn);
              imageView1 = (ImageView) mView.findViewById(R.id.senderImage_imageView);
            mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Comment");
            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
            mDatabaseLike.keepSynced(true);

        }

        public void setMlikebtn(final String post_key){
            final String currentUserId;

            if(mAuth.getCurrentUser() != null){
                currentUserId = mAuth.getCurrentUser().getUid().toString();

            }
            else {
                BrandSharedPref.init(mView.getContext());
                int id = BrandSharedPref.read(BrandSharedPref.ID,0);
                currentUserId  = String.valueOf(id);

            }

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if( currentUserId != null){
                        if(dataSnapshot.child(post_key).hasChild(currentUserId)){
                            likeCount.setText("likes"+" "+String.valueOf(dataSnapshot.child(post_key).getChildrenCount()));
                           // commentCount.setText(String.valueOf(dataSnapshot.child(post_key).getChildrenCount()));
                            mlikebtn.setImageResource(R.drawable.thumb_up_like_colored);
                            mDatabaseComment.child(post_key).addValueEventListener(new ValueEventListener() {
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
                            mlikebtn.setImageResource(R.drawable.thumb_up_blck);
                            mDatabaseComment.child(post_key).addValueEventListener(new ValueEventListener() {
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
            TextView post_date = (TextView) mView.findViewById(R.id.sender_date);
            post_date.setText(date);

        }
        public void setDesc(String des){
            TextView post_desc = (TextView) mView.findViewById(R.id.descShow);
            post_desc.setText(des);

        }
        public void setImage(final Context cxt, final String image){
            //final ImageView imageView = (ImageView) mView.findViewById(R.id.imageShow);

            Picasso.with(cxt).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(cxt).load(image).into(post_image);
                }
            });



        }
        public void setSenderProfileImage(final Context cxt, final String image){
            //final ImageView imageView1 = (ImageView) mView.findViewById(R.id.senderImage_imageView);

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
            TextView post_title = (TextView) mView.findViewById(R.id.senderName_textView);
            post_title.setText(name);
        }
    }



}
