package com.example.waqarahmed.neighbourlinking.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Classes.Comment;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentsOnPost extends AppCompatActivity {
  //
  RecyclerView mRecyclerComment;
    EditText mCommentView;
    ImageButton mLeaveComment_btn;
    String post_key_string;
    DatabaseReference mDatabaseComment;
    DatabaseReference mDatabaseCommentts;
    DatabaseReference mDatabaseReferenceUser;
    FirebaseAuth mAuth;
    String comment_body;
    String snder_name , msgBody,sendingDate , sender_image_comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_on_post);
        getSupportActionBar().setTitle("Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        post_key_string = getIntent().getStringExtra("postKey").toString();
        mRecyclerComment = (RecyclerView) findViewById(R.id.commentRecycler);
        mRecyclerComment.setHasFixedSize(true);
        mRecyclerComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCommentView = (EditText) findViewById(R.id.message_edit);
        mLeaveComment_btn = (ImageButton) findViewById(R.id.sendComment_btn);
        mAuth = FirebaseAuth.getInstance();

        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");
        mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Post_Comment").child(post_key_string);
        if(mAuth.getCurrentUser() != null) {
            DatabaseReference currentUser_Database = mDatabaseReferenceUser.child(mAuth.getCurrentUser().getUid().toString());
            currentUser_Database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    sender_image_comment = (String) dataSnapshot.child("image").getValue();
                    snder_name = (String) dataSnapshot.child("first_name").getValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        mLeaveComment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                msgBody = mCommentView.getText().toString();
                if(!TextUtils.isEmpty(msgBody)){
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm a");
                    String date = df.format(Calendar.getInstance().getTime());
                    DatabaseReference db =  mDatabaseComment.push();

                    db.child("comment_sender_name").setValue(snder_name);
                    db.child("comment_sender_image").setValue(sender_image_comment);
                    db.child("comment_body").setValue(msgBody);
                    db.child("sending_date").setValue(date);
                    db.child("sender_uid").setValue(mAuth.getCurrentUser().getUid());
                    mCommentView.setText("");
                    Toast.makeText(CommentsOnPost.this,"Comment send",Toast.LENGTH_SHORT).show();


                }else{
                    mCommentView.setText("");

                    Toast.makeText(CommentsOnPost.this,post_key_string,Toast.LENGTH_SHORT).show();
                }

            }
        });

        mDatabaseReferenceUser.keepSynced(true);
        mDatabaseComment.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Comment, CommentsOnPost.CommentViewHolder> firebaseRecyclerAdapterr = new FirebaseRecyclerAdapter<Comment, CommentsOnPost.CommentViewHolder>
                (

                        Comment.class,
                        R.layout.comment_list_item,
                        CommentsOnPost.CommentViewHolder.class,
                        mDatabaseComment

                ) {

            @Override
            protected void populateViewHolder(CommentsOnPost.CommentViewHolder viewHolder, Comment model, int position) {

                viewHolder.setComment_sender_image(model.getComment_sender_image(),getBaseContext());
                viewHolder.setComment_sender_name(model.getComment_sender_name());
                viewHolder.setComment_body(model.getComment_body());
                viewHolder.setComment_sending_date(model.getSending_date());
                Log.i("TAG", "populateViewHolder: ");
                viewHolder.Mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CommentsOnPost.this,"Helllll",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        Log.i("TTTTTTTTTAG", "onStart: b ");
        mRecyclerComment.setAdapter(firebaseRecyclerAdapterr);
        Log.i("TTTTTTTTTAG", "onStart: f ");

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






    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        View Mview;
        TextView comment_sender_name;
        ImageView comment_sender_image;
        TextView comment_sending_date;
        TextView comment_body;

        public CommentViewHolder(View itemView ) {
            super(itemView);
            Mview = itemView;
            Log.i("HHHHHH:","CommentViewHolder: ");
            Log.i("HHHHHH:","CommentViewHolder: ");
            comment_sender_image = (ImageView) itemView.findViewById(R.id.senderImage_imageView_commentView);
            comment_sender_name = (TextView) itemView.findViewById(R.id.senderName_textView_commentView);
            comment_body = (TextView)itemView.findViewById(R.id.comment_body_commentView);
            comment_sending_date = (TextView) itemView.findViewById(R.id.sender_date_commentView);
        }
        public void setComment_sender_name(String sender_name_comment) {
            Log.i("HHHHHH", "setComment_sender_name: "+sender_name_comment);
            comment_sender_name.setText(sender_name_comment);

        }

        public void setComment_sender_image(final String sender_image_comment , final Context c) {
            Picasso.with(c).load(sender_image_comment).centerCrop().resize(60,60).networkPolicy(NetworkPolicy.OFFLINE).into(comment_sender_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(c).load(sender_image_comment).centerCrop().resize(60,60).into(comment_sender_image);
                }
            });

        }

        public void setComment_sending_date(String sending_date_comment) {
            comment_sending_date.setText(sending_date_comment);
        }

        public void setComment_body(String comnt_body_comment) {
            comment_body.setText(comnt_body_comment);
        }
    } // viwholder class
}
