package com.example.onlinecollegelibrary;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatWithAdminAdapter extends RecyclerView.Adapter<ChatWithAdminAdapter.ChatViewHolder> {
    Context context;
    int msgId;
    ArrayList<Message> messages;
    public ChatWithAdminAdapter(Context context,ArrayList<Message> messages){
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatWithAdminAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 0){
            view = LayoutInflater.from(context).inflate(R.layout.text_message_out,parent,false);
        }else if (viewType == 1){
            view = LayoutInflater.from(context).inflate(R.layout.text_message_in,parent,false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatWithAdminAdapter.ChatViewHolder holder, int position) {
        holder.messageView.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        String sapId = messages.get(position).getSapId();
        if(TextUtils.equals(sapId,new SessionManager(context).getSapId())){
            msgId = 0;
        }else {
            msgId = 1;
        }
        return msgId;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageView;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            if (msgId == 0)
                messageView = itemView.findViewById(R.id.text_message_out_view);
            else if(msgId == 1)
                messageView = itemView.findViewById(R.id.text_message_in_view);
        }
    }
}

class Message {
    String message,sapId,timestamp;

    public Message(){

    }

    public Message(String message,String sapId,String timestamp){
        this.message = message;
        this.sapId = sapId;
        this.timestamp = timestamp;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSapId() {
        return sapId;
    }

    public String getTimestamp() {
        return timestamp;
    }

}