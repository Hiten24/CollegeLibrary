package com.example.onlinecollegelibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.formula.functions.T;
import org.w3c.dom.ls.LSException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RequestedBookAdapter extends RecyclerView.Adapter<RequestedBookAdapter.ViewHolder> {
    private Context context;
    private List<RequestedBook> requestedBooks;
//    private List<RequestedBookAdapter> requestedBooks;
    public RequestedBookAdapter(@NonNull Context context,List<RequestedBook> requestedBook) {
        this.context = context;
        this.requestedBooks = requestedBook;
    }

    //where to get the single row as view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requested_book_admin_list,parent,false);
        return new ViewHolder(view);
    }

    // what will happen after we create the viewHolder object
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String isbn = requestedBooks.get(position).getBookName();
        new DatabaseHelper(context).getBookDetails(isbn, new BookDetailsCallback() {
            @Override
            public void onBookReceived(Book book) {
                holder.bookName.setText(book.getTitle());
            }
        });

        holder.userName.setText(requestedBooks.get(position).getUserSapId());
        Toast.makeText(context,"hello",Toast.LENGTH_SHORT).show();

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,requestedBooks.get(position).getUserSapId(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,UserProfile.class);
                intent.putExtra("sapId",requestedBooks.get(position).getUserSapId());
                context.startActivity(intent);
            }
        });

        holder.bookName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,requestedBooks.get(position).getBookName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,BookDetails.class);
                intent.putExtra("isbn",requestedBooks.get(position).getBookName());
                context.startActivity(intent);
            }
        });

        holder.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(context);
                db.addIssuedBooks(requestedBooks.get(position).getUserSapId(),requestedBooks.get(position).getBookName());
                db.removeRequestedBooks(requestedBooks.get(position).getUserSapId());
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,requestedBooks.get(position).getUserSapId(),Toast.LENGTH_SHORT).show();
                new DatabaseHelper(context).removeRequestedBooks(requestedBooks.get(position).getUserSapId());
            }
        });
    }

    // How many items?
    @Override
    public int getItemCount() {
        return requestedBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName,bookName,confirmBtn;
        ImageView cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.request_book_user_name);
            bookName = itemView.findViewById(R.id.request_book_name);
            confirmBtn = itemView.findViewById(R.id.confirm_requested_book);

            cancel = itemView.findViewById(R.id.book_requested_cancel);
        }
    }
}
