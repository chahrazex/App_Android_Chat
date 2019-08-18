package com.example.chatapppfe.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapppfe.Adapter.GroupeAdapter;
import com.example.chatapppfe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GroupeFragment extends Fragment {
    private RecyclerView recyclerView ;
    private List<Object> listGroupe ;
    private GroupeAdapter groupeAdapte ;
    DatabaseReference reference;
    String name ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_groupe, container, false);
        recyclerView=view.findViewById(R.id.recyclerview_groupe);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayout =new GridLayoutManager(getContext(),2) ;
        recyclerView.setLayoutManager(gridLayout);
        listGroupe=new ArrayList<>();
        readGroupe();
        return  view ;
    }

    private void readGroupe() {
        reference= FirebaseDatabase.getInstance().getReference("Group");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listGroupe.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    name =snapshot.getKey();
                    listGroupe.add(name) ;

                }

                //Map<String,Object> groupe=(HashMap<String,Object>)dataSnapshot.getValue();

                groupeAdapte=new GroupeAdapter(getContext(),listGroupe);
                recyclerView.setAdapter(groupeAdapte);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

