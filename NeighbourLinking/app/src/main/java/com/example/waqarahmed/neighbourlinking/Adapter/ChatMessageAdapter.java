package com.example.waqarahmed.neighbourlinking.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.waqarahmed.neighbourlinking.Classes.ChatMessage;
import com.example.waqarahmed.neighbourlinking.Classes.Message;
import com.example.waqarahmed.neighbourlinking.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.himanshusoni.chatmessageview.ChatMessageView;

/**
 * Created by himanshusoni on 06/09/15.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageHolder> {
    private final String TAG = "ChatMessageAdapter";
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1;
    FirebaseAuth mAuth;

    private List<Message> mMessages;
    private Context mContext;

    public ChatMessageAdapter(Context context, List<Message> data) {
        mContext = context;
        mMessages = data;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

   @Override
    public int getItemViewType(int position) {
        Message item = mMessages.get(position);

        if (item.getSenderId().equals(mAuth.getCurrentUser().getUid()))
            return MY_MESSAGE;
        else
            return OTHER_MESSAGE;

   }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MY_MESSAGE) {
            return new MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mine_message, parent, false));
        } else {
            return new MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_other_message, parent, false));
        }
    }

    public void add(Message message) {
        mMessages.add(message);
        notifyItemInserted(mMessages.size() - 1);
    }

    @Override
    public void onBindViewHolder(final MessageHolder holder, final int position) {
        Message chatMessage = mMessages.get(position);
      // if (chatMessage.isImage()) {
          //  holder.ivImage.setVisibility(View.VISIBLE);
            holder.tvMessage.setVisibility(View.VISIBLE);

           // holder.ivImage.setImageResource(R.drawable.img_sample);
        //} else {
         //   holder.ivImage.setVisibility(View.GONE);
        //    holder.tvMessage.setVisibility(View.VISIBLE);

            holder.tvMessage.setText(chatMessage.getMsg());


        String date = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
        holder.tvTime.setText(date);

        holder.chatMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
       // ImageView ivImage;
        ChatMessageView chatMessageView;

        MessageHolder(View itemView) {
            super(itemView);
            chatMessageView = (ChatMessageView) itemView.findViewById(R.id.chatMessageView);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
           // ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}