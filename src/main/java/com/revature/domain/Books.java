package com.revature.domain;

public class Books {
    private int bookId;
    private String name;

    public Books(int bookId, String name) {
        this.bookId = bookId;
        this.name = name;
    }
    public Books() {
    }
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "com.revature.domain.Books [bookId=" + bookId + ", name=" + name + "]";
    }
}
