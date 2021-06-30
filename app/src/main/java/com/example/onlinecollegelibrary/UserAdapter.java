package com.example.onlinecollegelibrary;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;
    ArrayList<User> users;
    Boolean isChat;
    public UserAdapter(Context context,ArrayList<User> users,Boolean isChat){
        this.context = context;
        this.users = users;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_list,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        /*holder.userName.setText(users.get(position).getName());
        holder.userSapId.setText(users.get(position).getSapId());*/
//        String sapId = users.get(position);
        holder.userSapId.setText(users.get(position).getSapId());
        holder.userName.setText(users.get(position).getName());
        /*new DatabaseHelper(context).getUserDetails(sapId, new DatabaseCallback() {
            @Override
            public void onDataReceived(User user) {
//                holder.userName.setText(user.getName());
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(isChat){
                    intent = new Intent(context,ChatActivity.class);
                    if(TextUtils.equals(new SessionManager(context).getUserType(),new DatabaseConstant().ADMIN)){
                        intent.putExtra("isFromAdmin",true);
                    }else {
                        intent.putExtra("isFromAdmin",false);
                    }
                    intent.putExtra("sapId",users.get(position).getSapId());
                }else {
                    intent = new Intent(context,UserProfile.class);
                    intent.putExtra("sapId",users.get(position).getSapId());
                }
                /*Intent intent = new Intent(context,UserProfile.class);
                intent.putExtra("sapId",sapId);*/
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        TextView userName,userSapId;
        View view;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_admin);
            userSapId = itemView.findViewById(R.id.user_sapid_admin);
            view = itemView;
        }
    }
}
