package com.example.onlinecollegelibrary;

import java.util.Date;

public class IssuedBook {
    private String sapId,isbn;
    private DateAndTime issueDate,expireDate;

    public IssuedBook(String sapId,String isbn,DateAndTime issueDate,DateAndTime expireDate){
        this.sapId = sapId;
        this.isbn = isbn;
        this.issueDate = issueDate;
        this.expireDate = expireDate;
    }

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String book) {
        this.isbn = book;
    }

    public DateAndTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(DateAndTime issueDate) {
        this.issueDate = issueDate;
    }

    public DateAndTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(DateAndTime expireDate) {
        this.expireDate = expireDate;
    }
}
