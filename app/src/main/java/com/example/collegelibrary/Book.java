package com.example.collegelibrary;

public class Book {
    String title,page,author,publisher,subject,cover,isbn;
    public Book(){

    }

    public Book(String title, String page, String author, String publisher, String subject, String cover, String isbn) {
        this.title = title;
        this.page = page;
        this.author = author;
        this.publisher = publisher;
        this.subject = subject;
        this.cover = cover;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getPage() {
        return page;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSubject() {
        return subject;
    }

    public String getCover() {
        return cover;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
