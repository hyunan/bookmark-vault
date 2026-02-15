package com.github.hyunan.bookmarkvault.repository;

import com.github.hyunan.bookmarkvault.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    List<Bookmark> findAllByUserId(int id);

    void deleteAllByUserId(int id);

    Bookmark findBookmarkByUserIdAndBookmarkId(Integer userId, Integer bookmarkId);

    void removeBookmarksByUserIdAndBookmarkId(Integer userId, Integer bookmarkId);

    void removeAllByUserId(Integer userId);
}
