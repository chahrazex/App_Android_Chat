package com.example.chatapppfe.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.chatapppfe.MessageActivity;
import com.example.chatapppfe.Model.Chat;
import com.example.chatapppfe.Model.Users;
import com.example.chatapppfe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private Context mContext ;
    private List<Users> mUsers ;
    private boolean isChat,chekbox;
    private String theLastMessage ;

    public  UsersAdapter(Context mContext,List<Users> mUsers ,boolean isChat,boolean chekbox)
    {
        this.mUsers =mUsers ;
        this.mContext=mContext ;
        this.isChat=isChat ;
        this.chekbox=chekbox ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.single_user_layout, viewGroup,false);
        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Users users=mUsers.get(position);

        viewHolder.name.setText(users.getUsername());
        if(chekbox)
        {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        if (users.getImageURL().equals("default"))
        {
            viewHolder.profile_image.setImageResource(R.drawable.default_image_user);
        }
        else
        {
            Glide.with(mContext).load(users.getImageURL()).into(viewHolder.profile_image);
        }



        if (isChat)
        {
            lastMessage(users.getId(),viewHolder.last_message );
        }
        else
        {
            viewHolder.last_message.setVisibility(View.GONE);
        }
        if(isChat)
        {
            if (users.getStatus().equals("online"))
            {
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            viewHolder.img_on.setVisibility(View.GONE);
            viewHolder.img_off.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",users.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name ,last_message;
        public ImageView profile_image,img_off,img_on;
        private CheckBox checkBox ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.display_Uername);
            profile_image=itemView.findViewById(R.id.image_user);
            img_on=itemView.findViewById(R.id.img_on);
            img_off=itemView.findViewById(R.id.img_off);
            last_message=itemView.findViewById(R.id.last_message);
            checkBox=itemView.findViewById(R.id.users_checkbox) ;



        }
    }
    //Chek for last message
    private void lastMessage (final String userid, final TextView last_message)
    {
        theLastMessage="default" ;
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats") ;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class);
                    if(chat != null && firebaseUser != null)
                    {
                        if(chat.getReceiver().equals(firebaseUser.getUid())&& chat.getSender().equals(userid)||
                                chat.getSender().equals(firebaseUser.getUid())&& chat.getReceiver().equals(userid))
                        {
                            theLastMessage=chat.getMessage() ;
                        }
                    }

                }
                switch (theLastMessage)
                {
                    case "default" :
                        last_message.setText("No Message");
                        break;
                    default:
                        last_message.setText(theLastMessage);
                        break;
                }
                theLastMessage="default" ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
