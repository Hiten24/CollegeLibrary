package com.example.onlinecollegelibrary;

public class RequestedBook {
    private String userSapId,bookName;
    public RequestedBook(){

    }

    public RequestedBook(String userName,String bookName){
        this.userSapId = userName;
        this.bookName = bookName;
    }

    public String getUserSapId() {
        return userSapId;
    }

    public String getBookName() {
        return bookName;
    }

}
