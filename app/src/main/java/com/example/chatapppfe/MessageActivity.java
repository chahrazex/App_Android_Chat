package com.example.chatapppfe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.chatapppfe.Adapter.MessageAdapter;
import com.example.chatapppfe.Model.Chat;
import com.example.chatapppfe.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private CircleImageView mProfileImage ;
    private TextView mUsername ;
    private ImageButton btn_send ;
    private EditText text_send ;
    private Intent intent ;
    private DatabaseReference reference ;
    private MessageAdapter messageAdapter ;
    private List<Chat> mChat ;
    private RecyclerView recyclerView ;
    private FirebaseUser firebaseUser ;
    private String userid,imageurl;
    private Toolbar toolbar ;
    public ValueEventListener seenListner ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        InitializeFields();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mChat= new ArrayList<>();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intent=getIntent() ;
        userid=intent.getStringExtra("userid") ;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_send();
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class) ;
                mUsername.setText(users.getUsername());
                if(users.getImageURL().equals("default"))
                {
                    mProfileImage.setImageResource(R.drawable.default_image_user);
                }
                else
                {
                    Picasso.with(MessageActivity.this).load(users.getImageURL()).into(mProfileImage);
                }
                readMessage(firebaseUser.getUid(),userid, users.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(userid);

    }

    private void InitializeFields()
    {
        mProfileImage=findViewById(R.id.message_activity_profile_image);
        mUsername=findViewById(R.id.message_activity_username);
        btn_send=findViewById(R.id.btn_send);
        text_send=findViewById(R.id.text_send);
        recyclerView=findViewById(R.id.recyclerview) ;
        toolbar =findViewById(R.id.message_activity_toolbar);
    }
    private void seenMessage(final String userid)
    {
        reference=FirebaseDatabase.getInstance().getReference("Chats") ;
        seenListner=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class) ;
                    assert chat != null;
                    if (chat.getReceiver().equals(firebaseUser.getUid())&& chat.getSender().equals(userid))
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender ,String receiver ,String message )
    {
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference();
        Calendar calendar=Calendar.getInstance() ;
        SimpleDateFormat format=new SimpleDateFormat("HH:mm") ;
        String time=format.format(calendar.getTime()) ;
        HashMap<String ,Object> hashMap= new HashMap<>();
        String key = reference.child("Chats").push().getKey() ;
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("id",key);
        hashMap.put("time",time);
        hashMap.put("isseen",false) ;
        reference.child("Chats").child(key).setValue(hashMap) ;
        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("chatlist").child(firebaseUser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void readMessage(final String myid, final String userid, final String imageurl)
    {
        reference=FirebaseDatabase.getInstance().getReference("Chats") ;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mChat.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat ;
                    chat=snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid)&& chat.getSender().equals(userid)||
                            chat.getReceiver().equals(userid)&&chat.getSender().equals(myid))
                    {
                        mChat.add(chat);
                    }
                }
                messageAdapter=new MessageAdapter(MessageActivity.this,mChat,imageurl) ;
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public  void status(String status)
    {
        reference=FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()) ;
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status" ,status ) ;
        reference.updateChildren(hashMap);

    }
    public void btn_send()
    {
        String msg = text_send.getText().toString();
        text_send.setText("");
        if(!msg.equals(""))
        {
            sendMessage(firebaseUser.getUid(),userid,msg);
        }
        else
        {
            Toast.makeText(MessageActivity.this,"You can't send empty message",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListner);
        status("offline");
    }

}

