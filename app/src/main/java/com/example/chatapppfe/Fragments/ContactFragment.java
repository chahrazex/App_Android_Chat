package com.example.chatapppfe.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chatapppfe.Adapter.UsersAdapter;
import com.example.chatapppfe.Model.Users;
import com.example.chatapppfe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.chatapppfe.R.layout.single_user_layout;

public class ContactFragment extends Fragment {


    public RecyclerView all_users ;
    private DatabaseReference reference ;
    private UsersAdapter usersAdapter ;
    private List<Users> mUers ;
    private EditText search ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_contact, container, false);
        all_users=v.findViewById(R.id.recyclerview);
        //search=v.findViewById(R.id.search_text);
        all_users.setHasFixedSize(true);
        all_users.setLayoutManager(new LinearLayoutManager(getContext()));
        mUers= new ArrayList<>();

        /*search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // searchUsers(s.toString()) ;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        readUers();
        return  v;

    }
    private void searchUsers(String s) {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser() ;
        Query query =FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Users users =snapshot.getValue(Users.class) ;
                    if(!users.getId().equals(firebaseUser.getUid()))
                    {
                        mUers.add(users);
                    }
                }
                usersAdapter=new UsersAdapter(getContext(),mUers,true,false) ;
                all_users.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void readUers() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser() ;
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user;
                    user = snapshot.getValue(Users.class);
                    if(user != null && firebaseUser != null)
                    {
                        if(!user.getId().equals(firebaseUser.getUid()))
                        {
                            mUers.add(user);
                        }

                    }

                }
                usersAdapter = new UsersAdapter(getContext(), mUers,true,false);
                all_users.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
