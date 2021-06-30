package com.example.onlinecollegelibrary;

import javax.security.auth.callback.Callback;

interface DatabaseCallback {
    public void onDataReceived(User user);
}

interface BookDetailsCallback{
    public void onBookReceived(Book book);
}

interface RequestedBooksCallback{
    public void onRequestedBookReceived(String isbn,String date);
}

interface NoticeCallback{
    public void onNoticeReceived(String title,String body,String date);
}