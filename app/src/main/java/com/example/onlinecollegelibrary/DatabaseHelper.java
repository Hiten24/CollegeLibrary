package com.example.onlinecollegelibrary;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper /*implements DatabaseCallback*/ {
    Context context;

    public DatabaseHelper(Context context){
        this.context = context;
    }

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference("Books");
    DatabaseReference requestedRef = FirebaseDatabase.getInstance().getReference("Requested Books");
    DatabaseReference issuedRef = FirebaseDatabase.getInstance().getReference(new DatabaseConstant().ISSUED_BOOK);
    DatabaseReference noticeRef = FirebaseDatabase.getInstance().getReference(new DatabaseConstant().NOTICE_TABLE_TITLE);
    DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference(new DatabaseConstant().MESSAGE_TITLE);

    public void addUser(String name, String email, String sapId, String studentClass, String div, String address, String mobNo, String pass){
        User usr = new User(name,email,sapId,studentClass,div,address,mobNo,pass);
        userRef.child(sapId).setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context,"User added successfully!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getUserDetails(String sapId,DatabaseCallback callback){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onDataReceived(snapshot.child(sapId).getValue(User.class));
//                user = snapshot.child(sapId).getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addBooks(String isbn,String title, String page, String author, String publisher, String subject, String cover,int noOfBooks,int noOfAvailableBooks){
        Book book = new Book(title,page,author,publisher,subject,cover,noOfBooks,noOfAvailableBooks);
        bookRef.child(isbn).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context,"Book added successfully!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getBookDetails(String isbn,BookDetailsCallback callback){
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onBookReceived(snapshot.child(isbn).getValue(Book.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateNoOfAvailableBooks(String isbn,Book book,int value){
        addBooks(isbn,book.getTitle(),book.getPage(),book.getAuthor(),book.getPublisher(),book.getSubject(),book.getCover(),book.getNoOfBooks(),value);
//        bookRef.child(isbn).child("number_of_available_books").setValue(value);
    }

    public void addRequestedBooks(String isbn,String sapid,String date){
        requestedRef.child(sapid).child("Book ISBN").setValue(isbn);
        requestedRef.child(sapid).child("Date").setValue(date);
    }

    public void getRequestedBook(String sapid,RequestedBooksCallback callback){
        requestedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(sapid).exists()) {
                    String isbn = snapshot.child(sapid).child("Book ISBN").getValue(String.class);
                    String date = snapshot.child(sapid).child("Date").getValue(String.class);
                    callback.onRequestedBookReceived(isbn, date);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeRequestedBooks(String sapId){
        requestedRef.child(sapId).removeValue();
    }

    public void removeIssuedBook(String sapId){
        issuedRef.child(sapId).removeValue();
    }

    public void addIssuedBooks(String sapId, String isbn){
        DatabaseConstant db = new DatabaseConstant();
        issuedRef.child(sapId).child(db.BOOK_ISBN).setValue(isbn);

        DateAndTime dt = new DateAndTime();
        issuedRef.child(sapId).child(db.BOOK_ISSUED_DATE).setValue(dt);
        Calendar calendar = Calendar.getInstance();
        calendar.set(dt.getYear(),dt.getMonth(),dt.getDate());
        calendar.add(Calendar.DAY_OF_MONTH,7);
        issuedRef.child(sapId).child(db.BOOK_EXPIRE_DATE).setValue(new DateAndTime(calendar));

//        Calendar calendar = Calendar.getInstance();
//        issuedRef.child(sapId).child(db.ISSUED_BOOK)


        /*Calendar issueCal = Calendar.getInstance();
        DateAndTime issueDate = new DateAndTime(issueCal.get(Calendar.DATE),issueCal.get(Calendar.MONTH),issueCal.get(Calendar.YEAR));
        issuedRef.child(sapId).child(db.BOOK_ISSUED_DATE).setValue(issueCal.getTime());
        Calendar expCal = Calendar.getInstance();
        expCal.set(issueCal.get(Calendar.YEAR),issueCal.get(Calendar.MONTH),issueCal.get(Calendar.DATE));
        expCal.add(Calendar.DAY_OF_MONTH,7);
        DateAndTime expDate = new DateAndTime(expCal.get(Calendar.DATE),expCal.get(Calendar.MONTH),expCal.get(Calendar.YEAR));
        issuedRef.child(sapId).child(db.BOOK_EXPIRE_DATE).setValue(expCal.getTime());*/

        /*Calendar calendar = Calendar.getInstance();
        issuedRef.child(sapId).child(db.BOOK_ISSUED_DATE).child("date").setValue(calendar.get(Calendar.DATE));
        issuedRef.child(sapId).child(db.BOOK_ISSUED_DATE).child("month").setValue(calendar.get(Calendar.MONTH));
        issuedRef.child(sapId).child(db.BOOK_ISSUED_DATE).child("year").setValue(calendar.get(Calendar.YEAR));

        Calendar expCal = Calendar.getInstance();
        expCal.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
        issuedRef.child(sapId).child(db.BOOK_EXPIRE_DATE).child("date").setValue(expCal.get(Calendar.DATE));
        issuedRef.child(sapId).child(db.BOOK_EXPIRE_DATE).child("month").setValue(expCal.get(Calendar.MONTH));
        issuedRef.child(sapId).child(db.BOOK_EXPIRE_DATE).child("year").setValue(expCal.get(Calendar.YEAR));*/

        /*expireDate.setFullDate(calendar.get(Calendar.DATE),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));
        issuedRef.child(sapId).child(db.BOOK_EXPIRE_DATE).setValue(expireDate);*/
    }

    public void getNotice(String noticeId,NoticeCallback callback){
        noticeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseConstant dc = new DatabaseConstant();
                String title = snapshot.child(noticeId).child(dc.NOTICE_TITLE).getValue(String.class);
                String body = snapshot.child(noticeId).child(dc.NOTICE_BODY).getValue(String.class);
                String date = snapshot.child(noticeId).child(dc.NOTICE_DATE).getValue(String.class);
                callback.onNoticeReceived(title,body,date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeNotice(String noticeNumber){
        noticeRef.child(noticeNumber).removeValue();
    }

    public void addMessage(Message message,String SapId,String toSapId){
//        String SapId = new SessionManager(context).getSapId();
        DatabaseConstant dc = new DatabaseConstant();
        Calendar c = Calendar.getInstance();
        DateAndTime dateAndTime = new DateAndTime();
//        String time = new DateAndTime().getTime();
        /*messageRef.child(SapId).child(dc.MESSAGE).child(time).child(dc.MESSAGE).setValue(message);
        messageRef.child(SapId).child(dc.MESSAGE).child(time).child(dc.MESSAGE_FROM).setValue(new SessionManager(context).getSapId());*/
//        messageRef.child(SapId).child(dc.MESSAGE).push().setValue(message);
        messageRef.child(SapId).child(toSapId).push().setValue(message);
    }

    public void getMessage(){

    }
}
