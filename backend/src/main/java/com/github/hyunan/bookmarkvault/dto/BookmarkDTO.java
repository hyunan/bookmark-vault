package com.github.hyunan.bookmarkvault.dto;

public class BookmarkDTO {
    private String bookmarkName;
    private String bookmarkLink;

    public BookmarkDTO(String bookmarkName, String bookmarkLink) {
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

    public void setBookmarkLink(String bookmarkLink) {
        this.bookmarkLink = bookmarkLink;
    }
}
