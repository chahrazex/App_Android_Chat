package com.example.chatapppfe.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapppfe.Adapter.UsersAdapter;
import com.example.chatapppfe.Model.Chatlist;
import com.example.chatapppfe.Model.Users;
import com.example.chatapppfe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    public RecyclerView recyclerView ;
    private DatabaseReference reference ;
    private UsersAdapter usersAdapter ;
    private List<Users> mUers ;
    FirebaseUser firebaseUser ;
    private  List<Chatlist> userListe ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=v.findViewById(R.id.recyclerview_chat) ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser() ;

        userListe=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userListe.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chatlist chatlist=snapshot.getValue(Chatlist.class);
                    userListe.add(chatlist);
                }
                chatlist() ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return  v;
    }

    private void chatlist()
    {
        mUers=new ArrayList<>() ;
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Users users=snapshot.getValue(Users.class);
                    for (Chatlist chatlist:userListe)
                    {
                        if(users != null)
                        {
                            if(users.getId().equals(chatlist.getId()))
                            {
                                mUers.add(users);
                            }
                        }

                    }
                }
                usersAdapter=new UsersAdapter(getContext(),mUers,true,false);
                recyclerView.setAdapter(usersAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
