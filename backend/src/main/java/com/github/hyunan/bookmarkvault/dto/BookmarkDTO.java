package com.github.hyunan.bookmarkvault.dto;

public class BookmarkDTO {
    private int bookmarkId;
    private String bookmarkName;
    private String bookmarkLink;

    public BookmarkDTO(int bookmarkId, String bookmarkName, String bookmarkLink) {
        this.bookmarkId = bookmarkId;
        this.bookmarkName = bookmarkName;
        this.bookmarkLink = bookmarkLink;
    }

    public String getBookmarkName() {
        return bookmarkName;
    }

    public void setBookmarkName(String bookmarkName) {
        this.bookmarkName = bookmarkName;
    }

    public String getBookmarkLink() {
        return bookmarkLink;
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public void setBookmarkLink(String bookmarkLink) {
        this.bookmarkLink = bookmarkLink;
    }
}
