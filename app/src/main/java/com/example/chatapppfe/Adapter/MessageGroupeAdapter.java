package com.example.chatapppfe.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatapppfe.Model.ChatGroupe;
import com.example.chatapppfe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageGroupeAdapter extends RecyclerView.Adapter<MessageGroupeAdapter.ViewHolder>{
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context mContext  ;
    private List<ChatGroupe> chatGroupeList ;
    FirebaseUser firebaseUser ;

    public MessageGroupeAdapter(Context mContext, List<ChatGroupe> chatGroupeList) {
        this.mContext = mContext;
        this.chatGroupeList = chatGroupeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==MSG_TYPE_LEFT)
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageGroupeAdapter.ViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,viewGroup,false);
            return new MessageGroupeAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ChatGroupe chatGroupe=chatGroupeList.get(i);
        viewHolder.show_message.setText(chatGroupe.getMessage());
        viewHolder.name.setText(chatGroupe.getName());
        if (chatGroupe.getImageurl().equals("default"))
        {
            viewHolder.profile_image.setImageResource(R.drawable.default_image_user);
        }
        else
        {
            Picasso.with(mContext).load(chatGroupe.getImageurl()).into(viewHolder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return chatGroupeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser() ;
        if (chatGroupeList.get(position).getId().equals(firebaseUser.getUid()))
        {
            return MSG_TYPE_RIGHT ;
        }
        else
        {
            return MSG_TYPE_LEFT ;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message,name ;
        public ImageView profile_image ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameSender) ;
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image) ;
        }
    }

}

