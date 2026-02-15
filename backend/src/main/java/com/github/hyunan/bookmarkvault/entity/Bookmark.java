package com.github.hyunan.bookmarkvault.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Bookmarks")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bm_id")
    private Integer bookmarkId;

    @Column(name = "u_id")
    private Integer userId;

    @Column(name = "bm_link", nullable = false)
    private String bookmarkLink;

    @Column(name = "bm_name", nullable = false)
    private String bookmarkName;

    public Bookmark() {}

    public Bookmark(Integer userId, String bookmarkLink, String bookmarkName) {
        this.userId = userId;
        this.bookmarkLink = bookmarkLink;
        this.bookmarkName = bookmarkName;
    }

    public Integer getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(Integer bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBookmarkLink() {
        return bookmarkLink;
    }

    public void setBookmarkLink(String bookmarkLink) {
        this.bookmarkLink = bookmarkLink;
    }

    public String getBookmarkName() {
        return bookmarkName;
    }

    public void setBookmarkName(String bookmarkName) {
        this.bookmarkName = bookmarkName;
    }
}
