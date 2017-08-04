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

import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.AboutBrand;
import com.example.waqarahmed.neighbourlinking.Activities.BrandActivities.UpDateBrandProfile;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Comments;
import com.example.waqarahmed.neighbourlinking.Activities.Delete_Post;
import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.Post;
import com.example.waqarahmed.neighbourlinking.Classes.Blog;
import com.example.waqarahmed.neighbourlinking.Classes.Brand;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Shared.BrandSharedPref;
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


public class UserProfile extends Fragment {
   ImageView mCurrentUser_imageView;
    TextView mCurrentUserName_textView , mAccountCreated_date_textView;
    Button mCurrentUserAbout_btn,mCurrentUserUpdatetd_btn,mCurrentUserPost_btn;


    RecyclerView recyclerView;
    DatabaseReference mDatabaseReferenceBlog;
    DatabaseReference mDatabaseReferenceUser;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseLike;
    Query mCurrentUserProfileQuery;
    DatabaseReference mCurrentUserProfileDatabaseRefence;

    boolean mLikeProcess = false;
    String currentUserId;
    FirebaseRecyclerAdapter<Blog, BBlogViewHolder> firebaseRecyclerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclrProfile);
        mCurrentUser_imageView = (ImageView) view.findViewById(R.id.currentUser_imageView);
        mCurrentUserName_textView= (TextView) view.findViewById(R.id.currentUserName_textView);
        mAccountCreated_date_textView = (TextView) view.findViewById(R.id.accountCreated_date);

        mCurrentUserAbout_btn= (Button) view.findViewById(R.id.currentUserAbout);
        mCurrentUserUpdatetd_btn = (Button) view.findViewById(R.id.currentUserUpdatetd);
        mCurrentUserPost_btn= (Button) view.findViewById(R.id.currentUserPost);
        BrandSharedPref.init(getActivity().getApplicationContext());
        currentUserId= String.valueOf(BrandSharedPref.read(BrandSharedPref.ID,0));

        mCurrentUserPost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Post_Intent = new Intent(getActivity().getApplicationContext(), Post.class);
                startActivity(Post_Intent);
            }
        });
        mCurrentUserAbout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent About_Intent = new Intent(getActivity().getApplicationContext(), AboutBrand.class);
                startActivity(About_Intent);
            }
        });
        mCurrentUserUpdatetd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update_Intent = new Intent(getActivity().getApplicationContext(), UpDateBrandProfile.class);
                startActivity(update_Intent);
            }
        });



        //  recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReferenceBlog= FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");

        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
        mCurrentUserProfileDatabaseRefence = FirebaseDatabase.getInstance().getReference().child("Blog");



        mDatabaseReferenceBlog.keepSynced(true);
        mDatabaseReferenceUser.keepSynced(true);
        mCurrentUserProfileDatabaseRefence.keepSynced(true);
        mDatabaseLike.keepSynced(true);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onStart();
        onCreate(null);

    }

    @Override
    public void onStart() {
        super.onStart();
        BrandSharedPref.init(getActivity().getApplicationContext());
        final Brand brand = BrandSharedPref.readObject(BrandSharedPref.OBJECT,null);
        if(brand != null) {
            Picasso.with(getActivity().getApplicationContext()).load(brand.getImage_url()).networkPolicy(NetworkPolicy.OFFLINE).into(mCurrentUser_imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(getActivity().getApplicationContext()).load(brand.getImage_url()).into(mCurrentUser_imageView);
                }
            });
            mCurrentUserName_textView.setText(brand.getName());
            mAccountCreated_date_textView.setText(brand.getCreated_at());

        }

        mCurrentUserProfileQuery = mCurrentUserProfileDatabaseRefence.orderByChild("uid").equalTo(currentUserId);
            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BBlogViewHolder>(
                    Blog.class,
                    R.layout.blog_row,
                    BBlogViewHolder.class,
                    mCurrentUserProfileQuery
            ) {
                @Override
                protected void populateViewHolder(BBlogViewHolder viewHolder, Blog model, final int position) {
                    final String post_key;
                    post_key = getRef(position).getKey().toString();
                    viewHolder.setName(model.getUsername());
                    viewHolder.setSenderProfileImage(getActivity().getApplicationContext(), model.getSender_image());
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setDate(model.getSend_date());
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getPost_image());
                    viewHolder.setMlikebtn(post_key);
                    viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //  Toast.makeText(getActivity(),"Whole item Clicked",Toast.LENGTH_SHORT).show();
                            Intent Delete_Post_Intent = new Intent(getActivity(), Delete_Post.class);
                            Delete_Post_Intent.putExtra("post_key", post_key);
                            startActivity(Delete_Post_Intent);
                        }
                    });
                    viewHolder.post_title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String post = getRef(position).toString();
                         //   Toast.makeText(getActivity(), post_key, Toast.LENGTH_SHORT).show();

                        }
                    });
                    viewHolder.mlikebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mLikeProcess = true;
                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (mLikeProcess) {
                                        if (dataSnapshot.child(post_key).hasChild(currentUserId)) {
                                            mDatabaseLike.child(post_key).child(currentUserId).removeValue();
                                            mLikeProcess = false;
                                        } else {
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
    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    public static class BBlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView post_title;
        ImageView post_image;
        ImageButton mlikebtn;
        FirebaseAuth mAuth;
        ImageButton mComment;
        DatabaseReference mDatabaseLike;
        Context context;
        public BBlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            context = itemView.getContext();
            mComment = (ImageButton) mView.findViewById(R.id.comment_btn);
            post_title = (TextView) mView.findViewById(R.id.titleShow);
            post_image = (ImageView) mView.findViewById(R.id.imageShow);
            mlikebtn = (ImageButton) itemView.findViewById(R.id.like_btn);
            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
            mDatabaseLike.keepSynced(true);

        }

        public void setMlikebtn(final String post_key){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BrandSharedPref.init(context);
                    String currentUserId = String.valueOf(BrandSharedPref.read(BrandSharedPref.ID,0));
                    if(!currentUserId.equals(0)){

                        if(dataSnapshot.child(post_key).hasChild(String.valueOf(BrandSharedPref.read(BrandSharedPref.ID,0)))){
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
            final ImageView imageView1 = (ImageView) mView.findViewById(R.id.senderImage_imageView);

            Picasso.with(cxt).load(image).centerCrop().resize(75,75).placeholder(R.mipmap.ic_launcher).networkPolicy(NetworkPolicy.OFFLINE).into(imageView1, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(cxt).load(image).centerCrop().placeholder(R.mipmap.ic_launcher).resize(75,75).into(imageView1);
                }
            });
        }
        public void setName(String name){
            TextView post_title = (TextView) mView.findViewById(R.id.senderName_textView);
            post_title.setText(name);
        }
    }


}
