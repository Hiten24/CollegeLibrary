package com.example.onlinecollegelibrary;

public class Book {
    String title,page,author,publisher,subject,cover,isbn;
    int noOfBooks,number_of_available_books;
    public Book(){

    }

    public Book(int number_of_available_books){
        this.number_of_available_books = number_of_available_books;
    }

    public Book(String title, String page, String author, String publisher, String subject, String cover,int noOfBooks,int noOfAvailableBooks) {
        this.title = title;
        this.page = page;
        this.author = author;
        this.publisher = publisher;
        this.subject = subject;
        this.cover = cover;
        this.noOfBooks = noOfBooks;
        this.number_of_available_books = noOfAvailableBooks;
    }

    public Book(String title,String author,String cover,String isbn,int noOfBooks,int noOfAvailableBooks){
        this.title = title;
        this.author = author;
        this.cover =cover;
        this.isbn = isbn;
        this.noOfBooks = noOfBooks;
        this.number_of_available_books = noOfAvailableBooks;
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

    public int getNoOfBooks() {
        return noOfBooks;
    }

    public void setNoOfBooks(int noOfBooks) {
        this.noOfBooks = noOfBooks;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getNumber_of_available_books() {
        return number_of_available_books;
    }

    public void setNumber_of_available_books(int number_of_available_books) {
        this.number_of_available_books = number_of_available_books;
    }
}
