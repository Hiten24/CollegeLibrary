package com.example.onlinecollegelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.formula.ExternSheetReferenceToken;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IssuedBooksAdapter extends RecyclerView.Adapter<IssuedBooksAdapter.ViewHolder> {
    private Context context;
//    private List<IssuedBook> issuedBooks;
    private List<IssuedBook> issuedBooks;
    public IssuedBooksAdapter(Context context,List<IssuedBook> issuedBook){
        this.context = context;
        this.issuedBooks = issuedBook;
    }
    @NonNull
    @Override
    public IssuedBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.issued_book_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssuedBooksAdapter.ViewHolder holder, int position) {
        holder.sapId.setText(issuedBooks.get(position).getSapId());
        String isbn = issuedBooks.get(position).getIsbn();
        new DatabaseHelper(context).getBookDetails(isbn, new BookDetailsCallback() {
            @Override
            public void onBookReceived(Book book) {
                holder.bookName.setText(book.getTitle());
                Glide.with(context)
                        .load(book.getCover())
                        .placeholder(R.drawable.loadingimage)
                        .apply(new RequestOptions().transform(new RoundedCorners(16)))
                        .into(holder.bookCover);
            }
        });

//        DateAndTime date = issuedBooks.get(position).getDateAndTime();


        DateAndTime issueDate = issuedBooks.get(position).getIssueDate();
        DateAndTime expireDate = issuedBooks.get(position).getExpireDate();

        String strIssueDate = issueDate.getDate() + " " + issueDate.getStringMonth(issueDate.getMonth()) + ", " + issueDate.getYear();
        String strExpDate = expireDate.getDate() + " " + expireDate.getStringMonth(expireDate.getMonth()) + ", " + expireDate.getYear();

        holder.issuedDate.setText(strIssueDate);

        holder.expireDate.setText(strExpDate);

        Calendar expDate = Calendar.getInstance();
        expDate.set(expireDate.getYear(),expireDate.getMonth(),expireDate.getDate());
        Calendar currentDate = Calendar.getInstance();
        if(currentDate.compareTo(expDate) > 0){
            int remainingDays = Math.abs(Math.round((float) (expDate.getTimeInMillis() - currentDate.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
            holder.penaltyView.setText(remainingDays*20+" Rs.");
        }

        /*Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(),date.getMonth(),date.getDate());
        calendar.add(Calendar.DAY_OF_MONTH,7);
//        String strExpireDate = calendar.get(Calendar.DATE) + " " + new DateAndTime().getStringMonth(calendar.get(Calendar.MONTH)) + ", " + calendar.get(Calendar.YEAR);
        String strExpireDate = "25 april, 2021";
        holder.expireDate.setText(strExpireDate);*/

        /*Calendar calCurrentDate = Calendar.getInstance();
        if(calCurrentDate.compareTo(calendar) > 0){
            int remainingDays = Math.abs(Math.round((float) (calendar.getTimeInMillis() - calCurrentDate.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
            holder.penaltyView.setText(remainingDays*20+" Rs.");
        }*/

        holder.returnedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"You retured Book, Thank You!",Toast.LENGTH_SHORT).show();
                new DatabaseHelper(context).removeIssuedBook(issuedBooks.get(position).getSapId());
                FirebaseDatabase.getInstance().getReference("Books").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Book book = snapshot.child(isbn).getValue(Book.class);
//                        new DatabaseHelper(context).updateNoOfAvailableBooks(isbn,book,book.getNumber_of_available_books()+1);
                        FirebaseDatabase.getInstance().getReference("Books").child(isbn).child("number_of_available_books").setValue(book.getNumber_of_available_books()+1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                /*new DatabaseHelper(context).getBookDetails(issuedBooks.get(position).getIsbn(), new BookDetailsCallback() {
                    @Override
                    public void onBookReceived(Book book) {
//                        new DatabaseHelper(context).updateNoOfAvailableBooks(book.getIsbn(),book,book.getNumber_of_available_books()+1);
                        FirebaseDatabase.getInstance().getReference("Books").child("number_of_available_books").setValue(book.getNumber_of_available_books());
                    }
                });*/
            }
        });

        holder.bookName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBookDetail(isbn);
            }
        });

        holder.sapId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUser(issuedBooks.get(position).getSapId());
            }
        });

        holder.userDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUser(issuedBooks.get(position).getSapId());
            }
        });

        holder.bookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBookDetail(isbn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return issuedBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sapId,bookName,issuedDate,expireDate,returnedButton,penaltyView;
        ImageView userDp,bookCover;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.issued_book_book_name_value);
            sapId = itemView.findViewById(R.id.issued_book_user_sap_id_value);
            issuedDate = itemView.findViewById(R.id.issued_book_issue_date_value);
            userDp = itemView.findViewById(R.id.issued_book_user_dp);
            expireDate = itemView.findViewById(R.id.issued_book_exipry_date_value);
            bookCover = itemView.findViewById(R.id.issued_book_cover_img);
            returnedButton = itemView.findViewById(R.id.issued_book_confirm_btn);
            penaltyView = itemView.findViewById(R.id.issued_book_list_penalty_rs_value);
        }
    }

    private void gotoBookDetail(String isbn){
        Intent intent = new Intent(context,BookDetails.class);
        intent.putExtra("isbn",isbn);
        context.startActivity(intent);
    }

    private void gotoUser(String sapId){
        Intent intent = new Intent(context,UserProfile.class);
        intent.putExtra("sapId",sapId);
        context.startActivity(intent);
    }
}
