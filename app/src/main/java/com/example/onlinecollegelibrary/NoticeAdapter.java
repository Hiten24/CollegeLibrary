package com.example.onlinecollegelibrary;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {
    Context context;
    ArrayList<Notice> notices;
    public NoticeAdapter(Context context,ArrayList<Notice> notices){
        this.context = context;
        this.notices = notices;
    }

    @NonNull
    @Override
    public NoticeAdapter.NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_list_item,parent,false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.NoticeViewHolder holder, int position) {
        holder.title.setText(notices.get(position).getNoticeTitle());
        holder.date.setText(notices.get(position).getNoticeDate());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ShowNotice.class);
                intent.putExtra("isEditClicked",true);
                intent.putExtra("Notice Number",notices.get(position).getNoticeNumber());
                context.startActivity(intent);
            }
        });

        if(TextUtils.equals(new SessionManager(context).getUserType(),new DatabaseConstant().ADMIN)){
            holder.delete.setVisibility(View.VISIBLE);
        }else {
            holder.expand.setVisibility(View.VISIBLE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show();
                new DatabaseHelper(context).removeNotice(notices.get(position).getNoticeNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder{
        TextView title,date;
        ConstraintLayout view;
        ImageView expand,delete;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notice_list_title);
            date = itemView.findViewById(R.id.notice_list_date);
            view = itemView.findViewById(R.id.notice_constraint_view);
            expand = itemView.findViewById(R.id.notice_expand_button);
            delete = itemView.findViewById(R.id.notice_delete_button);
        }

    }
}
class Notice{
    String noticeNumber,noticeTitle,noticeDate;
    public Notice(String noticeNumber,String title, String date){
        this.noticeNumber = noticeNumber;
        this.noticeTitle = title;
        this.noticeDate = date;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticeNumber() {
        return noticeNumber;
    }

    public void setNoticeNumber(String noticeNumber) {
        this.noticeNumber = noticeNumber;
    }
}
