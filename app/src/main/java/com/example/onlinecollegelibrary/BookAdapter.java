package com.example.onlinecollegelibrary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context context;
    private List<Book> books;
    public BookAdapter(List<Book> books,BookCallback callback){
        this.context = context;
        this.books = books;
        this.callback = callback;
    }
    String isbn;
    BookCallback callback;


    //where to get the single row as view holder
    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_book_manager,parent,false);
        return new ViewHolder(view);
    }

    // what will happen after we create the viewHolder object
    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        holder.title.setText(books.get(position).getTitle());
        holder.author.setText(books.get(position).getAuthor());
        holder.noOfBooks.setText(books.get(position).getNumber_of_available_books()+" / "+books.get(position).getNoOfBooks());
        Glide.with(holder.itemView.getContext())
                .load(books.get(position).getCover())
                .placeholder(R.drawable.loadingimage)
                .into(holder.cover);
        isbn = books.get(position).getIsbn();
        if (books.get(position).getNumber_of_available_books() > 0){
            holder.availableTag.setVisibility(View.VISIBLE);
            holder.unavailableTag.setVisibility(View.GONE);
        }else {
            holder.availableTag.setVisibility(View.GONE);
            holder.unavailableTag.setVisibility(View.VISIBLE);
        }
    }

    // How many items?
    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,author,noOfBooks,availableTag,unavailableTag;
        ImageView cover;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_name_admin_manger);
            author = itemView.findViewById(R.id.book_author_name_admin);
            noOfBooks = itemView.findViewById(R.id.numer_of_books_admin);
            cover = itemView.findViewById(R.id.book_cover_admin);
            availableTag = itemView.findViewById(R.id.book_available_tag);
            unavailableTag = itemView.findViewById(R.id.book_unavailable_tag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
//                    Log.d("checking",books.get(getAdapterPosition()).getTitle());
                    callback.onBookItemClick(books.get(getAdapterPosition()).getIsbn());
                }
            });
        }
    }
}
