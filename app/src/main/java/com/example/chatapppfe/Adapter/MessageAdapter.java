package com.example.chatapppfe.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.chatapppfe.Model.Chat;
import com.example.chatapppfe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> implements View.OnClickListener {
    public static final int MSG_TYPE_LEFT=0 ;
    public static final int MSG_TYPE_RIGHT=1 ;
    public Context mContext ;
    public List<Chat> mChat ;
    public String imageURL ;


    FirebaseUser firebaseUser ;

    public  MessageAdapter(Context mContext, List<Chat> mChat, String imageURL)
    {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        if (i== MSG_TYPE_LEFT)
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,viewGroup,false);
            return  new MessageAdapter.ViewHolder(view) ;
        }
        else
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,viewGroup,false);
            return  new MessageAdapter.ViewHolder(view) ;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Chat chat=mChat.get(i);
        if (imageURL.equals("default"))
        {
            viewHolder.profile_image.setImageResource(R.drawable.default_image_user);
        }
        else
        {
            Picasso.with(mContext).load(imageURL).into(viewHolder.profile_image);
        }

        viewHolder.show_message.setText(chat.getMessage());
        viewHolder.time_message.setText(chat.getTime());
        /*---------------------------------Delting Message -------------------------------*/
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(FirebaseDatabase.getInstance().getReference("Chats").child(chat.getId()).child(chat.getSender()).getKey().equals(firebaseUser.getUid()))
                {
                    final AlertDialog.Builder builder=new AlertDialog.Builder(mContext,R.style.AlertDialog);
                    builder.setTitle("Delete Message ?") ;
                    builder.setMessage("Deleting this Message will result in completely removing from the " +
                            "Database ") ;
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference("Chats").child(chat.getId()).removeValue();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog=builder.create() ;
                    alertDialog.show();

                }
                else
                {
                    final AlertDialog.Builder builder1=new AlertDialog.Builder(mContext,R.style.AlertDialog);
                    builder1.setTitle("Sorry ") ;
                    builder1.setMessage("You can delet only the message that you send !") ;
                    builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog=builder1.create() ;
                    alertDialog.show();
                }
                return true;
            }
        });
        if(i ==mChat.size()-1)
        {
            if (chat.isIsseen())
            {
                viewHolder.see_message.setText("Vu");
                viewHolder.delivred.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.delivred.setVisibility(View.VISIBLE);
                viewHolder.see_message.setVisibility(View.GONE);
            }
        }
        else
        {
            viewHolder.see_message.setVisibility(View.GONE);
            viewHolder.delivred.setVisibility(View.GONE);
        }
        /*-------------------------------------------------------------------------------------------------------*/

    }
    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return  MSG_TYPE_RIGHT;
        }
        else
        {
            return  MSG_TYPE_LEFT ;
        }
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView show_message,time_message,see_message,delivred;
        public ImageView profile_image ;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            time_message=itemView.findViewById(R.id.time_message);
            see_message=itemView.findViewById(R.id.seen_message) ;
            delivred=itemView.findViewById(R.id.delivered) ;
        }
    }

}



