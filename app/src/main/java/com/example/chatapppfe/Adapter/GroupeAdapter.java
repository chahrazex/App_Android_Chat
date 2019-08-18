package com.example.chatapppfe.Adapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatapppfe.GroupeChatActivity;
import com.example.chatapppfe.R;

import java.util.List;

public class GroupeAdapter extends RecyclerView.Adapter<GroupeAdapter.ViewHolder>{
    public Context mContext ;
    public List<Object> mGroupe ;

    public GroupeAdapter(Context mContext, List<Object> mGroupe) {
        this.mContext = mContext;
        this.mGroupe = mGroupe;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.groupe_item,viewGroup,false) ;
        return  new GroupeAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Object groupe=mGroupe.get(i);
        viewHolder.name.setText(groupe.toString());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, GroupeChatActivity.class) ;
                intent.putExtra("name",groupe.toString()) ;
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mGroupe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.groupe_name_item) ;
        }
    }
}


