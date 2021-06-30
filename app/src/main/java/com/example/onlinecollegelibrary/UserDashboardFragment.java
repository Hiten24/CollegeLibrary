package com.example.onlinecollegelibrary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UserDashboardFragment extends Fragment {
    TextView userName;
    TextView requestedBookName,reqBookAuthorName,reqDate;
    ImageView reqBookCover;

    TextView issuedBookName,issBookAuthorName,issDate,expireDate;
    ImageView issBookCover;

    TextView penaltyRs,penaltyDay;
    TextView noOfIssuedBook,noOfBookDate;

    public UserDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_dashboard, container, false);
        SessionManager session = new SessionManager(getContext());
        userName = view.findViewById(R.id.user_db_user_name);
        userName.setText(session.getUserName());

        requestedBookName = view.findViewById(R.id.user_db_requested_book_name);
        reqBookAuthorName = view.findViewById(R.id.user_db_requested_book_author);
        reqDate = view.findViewById(R.id.user_db_requested_book_request_date_value);
        reqBookCover = view.findViewById(R.id.user_db_requested_book_cover);

        issuedBookName = view.findViewById(R.id.user_db_issued_book_name);
        issBookAuthorName = view.findViewById(R.id.user_db_issued_book_author);
        issDate = view.findViewById(R.id.user_db_issued_book_issued_date_value);
        issBookCover = view.findViewById(R.id.user_db_issued_book_issued_book_cover);
        expireDate = view.findViewById(R.id.user_db_issued_book_issued_date_value2);

        penaltyRs = view.findViewById(R.id.user_dashboard_fine_value);
        penaltyDay = view.findViewById(R.id.user_dashboard_days_value);

        noOfIssuedBook = view.findViewById(R.id.user_dashboard_no_of_issued_book);
        noOfBookDate = view.findViewById(R.id.user_dashboard_issue_date);

        new DatabaseHelper(getContext()).getRequestedBook(session.getSapId(), new RequestedBooksCallback() {
            @Override
            public void onRequestedBookReceived(String isbn, String date) {
                reqDate.setText(date);

                new DatabaseHelper(getContext()).getBookDetails(isbn, new BookDetailsCallback() {
                    @Override
                    public void onBookReceived(Book book) {
                        requestedBookName.setText(book.getTitle());
                        reqBookAuthorName.setText(book.getAuthor());
                        Glide.with(getContext())
                                .load(book.getCover())
                                .placeholder(R.drawable.loadingimage)
                                .apply(new RequestOptions().transform(new RoundedCorners(16)))
                                .into(reqBookCover);
                    }
                });
            }
        });

        FirebaseDatabase.getInstance().getReference(new DatabaseConstant().ISSUED_BOOK).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(session.getSapId()).exists()) {

                    noOfIssuedBook.setText("1");

                    DatabaseConstant dc = new DatabaseConstant();
                    String isbn = snapshot.child(session.getSapId()).child(dc.BOOK_ISBN).getValue(String.class);
                    DateAndTime issDate = snapshot.child(session.getSapId()).child(dc.BOOK_ISSUED_DATE).getValue(DateAndTime.class);
                    DateAndTime expDate = snapshot.child(session.getSapId()).child(dc.BOOK_EXPIRE_DATE).getValue(DateAndTime.class);
                    /*int date = snapshot.child(session.getSapId()).child(dc.BOOK_ISSUED_DATE).child("date").getValue(int.class);
                    int month = snapshot.child(session.getSapId()).child(dc.BOOK_ISSUED_DATE).child("month").getValue(int.class);
                    int year = snapshot.child(session.getSapId()).child(dc.BOOK_ISSUED_DATE).child("year").getValue(int.class);*/

                    String issueDate = issDate.getDate() + " " + new DateAndTime().getStringMonth(issDate.getMonth()) + ", " + issDate.getYear();
                    String strExpDate = expDate.getDate() + " " + new DateAndTime().getStringMonth(expDate.getMonth()) + ", " + expDate.getYear();

                    Calendar expDateCal = Calendar.getInstance();
                    expDateCal.set(expDate.getYear(),expDate.getMonth(),expDate.getDate());
                    Calendar calCurrentDate = Calendar.getInstance();

                    if(calCurrentDate.compareTo(expDateCal) > 0){
                        int remainingDays = Math.abs(Math.round((float) (expDateCal.getTimeInMillis() - calCurrentDate.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        penaltyDay.setText(remainingDays+" Days");
                        penaltyRs.setText(remainingDays*20+"");
                    }

                    String expireDateStr = expDate.getDate() + " " + new DateAndTime().getStringMonth(expDate.getMonth()) + ", " + expDate.getYear();

                    UserDashboardFragment.this.issDate.setText(issueDate);
                    noOfBookDate.setText(issueDate);
                    expireDate.setText(expireDateStr);

                    new DatabaseHelper(getContext()).getBookDetails(isbn, new BookDetailsCallback() {
                        @Override
                        public void onBookReceived(Book book) {
                            issuedBookName.setText(book.getTitle());
                            issBookAuthorName.setText(book.getAuthor());
                            Glide.with(getContext())
                                    .load(book.getCover())
                                    .placeholder(R.drawable.loadingimage)
                                    .apply(new RequestOptions().transform(new RoundedCorners(16)))
                                    .into(issBookCover);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*new DatabaseHelper(getContext()).getRequestedBook(session.getSapId(), new RequestedBooksCallback() {
            @Override
            public void onRequestedBookReceived(String isbn,String date) {
                reqDate.setText(date);
                *//*new DatabaseHelper(getContext()).getBookDetails(isbn, new BookDetailsCallback() {
                    @Override
                    public void onBookReceived(Book book) {
                        requestedBookName.setText(book.getTitle());
                        reqBookAuthorName.setText(book.getAuthor());
                        Glide.with(getContext())
                                .load(book.getCover())
                                .placeholder(R.drawable.loadingimage)
                                .apply(new RequestOptions().transform(new RoundedCorners(16)))
                                .into(reqBookCover);
                    }
                });*//*
            }
        });

        FirebaseDatabase.getInstance().getReference(new DatabaseContract().ISSUED_BOOK).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseContract db = new DatabaseContract();
                if (snapshot.child(session.getSapId()).exists()){
                DateAndTime date = snapshot.child(session.getSapId()).child(db.BOOK_ISSUED_DATE).getValue(DateAndTime.class);
                String dateString = date.getDate() + " " + date.getMonth() + ", " + date.getYear();
                String expDate = (date.getDate()+7) + " " + date.getMonth() + ", " + date.getYear();
                issDate.setText(dateString);
                expireDate.setText(expDate);


                DateAndTime dt = new DateAndTime();
                int currentDate = dt.getDate();
//                int currentMonth = dt.getNumMonth();
                int currentYear = dt.getYear();

//                if(currentDate > date.getDate())

                String isbn = snapshot.child(session.getSapId()).child(db.BOOK_ISBN).getValue(String.class);
                *//*new DatabaseHelper(getContext()).getBookDetails(isbn, new BookDetailsCallback() {
                    @Override
                    public void onBookReceived(Book book) {
                        issuedBookName.setText(book.getTitle());
                        issBookAuthorName.setText(book.getAuthor());
                        Glide.with(getContext())
                                .load(book.getCover())
                                .placeholder(R.drawable.loadingimage)
                                .apply(new RequestOptions().transform(new RoundedCorners(16)))
                                .into(issBookCover);
                    }
                });*//*

            } // if close

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        return view;
    }
}