package com.example.chatapppfe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapppfe.Adapter.MessageGroupeAdapter;
import com.example.chatapppfe.Model.ChatGroupe;
import com.example.chatapppfe.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class GroupeChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView GroupeName ;
    private Toolbar toolbar ;
    private ImageButton SendBtn ;
    private EditText message ;
    private Intent intent ;
    private DatabaseReference reference ;
    private FirebaseUser firebaseUser ;
    private List<ChatGroupe> mchatGroupe ;
    private MessageGroupeAdapter messageGroupeAdapter ;
    private final String imageUrl ="";
    String name ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe_chat);
        recyclerView=findViewById(R.id.recyclerview_Groupe_message) ;
        toolbar=findViewById(R.id.GroupeChat_activity_toolbar) ;
        GroupeName=findViewById(R.id.GroupeChat_activity_name) ;
        SendBtn=findViewById(R.id.GroupeChat_activity_btn_send) ;
        message=findViewById(R.id.GroupeChat_activity_text_send) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent=getIntent() ;
        name=intent.getStringExtra("name") ;
        GroupeName.setText(name);
        mchatGroupe=new ArrayList<>() ;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser() ;
        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_send() ;
            }
        });
        readMessage();

    }

    private void btn_send() {
        if (TextUtils.isEmpty(message.getText()))
        {
            Toasty.info(GroupeChatActivity.this,"You can't send empty message" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            sendMessage();
        }

    }

    private void sendMessage() {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser() ;
        reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()) ;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users=dataSnapshot.getValue(Users.class) ;
                reference= FirebaseDatabase.getInstance().getReference("Groupe").child(name);
                HashMap<String,String> hashMap =new HashMap<>() ;
                hashMap.put("id",firebaseUser.getUid());
                hashMap.put("name",users.getUsername()) ;
                hashMap.put("message",message.getText().toString());
                hashMap.put("imageurl",users.getImageURL()) ;
                reference.push().setValue(hashMap);
                message.setText("");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void readMessage()
    {
        reference=FirebaseDatabase.getInstance().getReference("Groupe").child(name);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchatGroupe.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ChatGroupe chatGroupe=snapshot.getValue(ChatGroupe.class);
                    mchatGroupe.add(chatGroupe) ;
                }
                messageGroupeAdapter=new MessageGroupeAdapter(GroupeChatActivity.this,mchatGroupe) ;
                recyclerView.setAdapter(messageGroupeAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
