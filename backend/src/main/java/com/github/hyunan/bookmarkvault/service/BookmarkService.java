package com.github.hyunan.bookmarkvault.service;

import com.github.hyunan.bookmarkvault.dto.BookmarkDTO;
import com.github.hyunan.bookmarkvault.entity.Bookmark;
import com.github.hyunan.bookmarkvault.entity.User;
import com.github.hyunan.bookmarkvault.repository.BookmarkRepository;
import com.github.hyunan.bookmarkvault.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    public List<BookmarkDTO> getUserBookmarks(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(user.getId());
        return bookmarks.stream().map(bm -> new BookmarkDTO(bm.getBookmarkName(), bm.getBookmarkLink()))
                .toList();
    }

    @Transactional
    public void uploadBookmarks(MultipartFile file, String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            Document doc = Jsoup.parse(file.getInputStream(), "UTF-8", "");
            Elements links = doc.select("DT > A");
            bookmarkRepository.deleteAllByUserId(user.getId());
            List<Bookmark> newBookmarks = new ArrayList<>();
            for (var link : links) {
                String href = link.attr("HREF");
                String title = link.text();
                if (title.isEmpty())
                    title = "unnamed title";
                newBookmarks.add(new Bookmark(user.getId(), title, href));
            }
            bookmarkRepository.saveAll(newBookmarks);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String downloadBookmarks(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Bookmark> bookmarksList = bookmarkRepository.findAllByUserId(user.getId());
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE NETSCAPE-Bookmark-file-1>\n");
        html.append("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
        html.append("<TITLE>Bookmarks</TITLE>\n");
        html.append("<H1>Bookmarks</H1>\n");
        html.append("<DL><p>\n");
        for (var b : bookmarksList) {
            html.append("\t<DT><A HREF=").append("\"").append(b.getBookmarkLink()).append("\"")
                    .append(">").append(b.getBookmarkName()).append("</A>\n");
        }
        html.append("</DL><p>\n");
        return html.toString();
    }

    public void addBookmark(String username, BookmarkDTO bookmarkDTO) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        bookmarkRepository.save(new Bookmark(user.getId(), bookmarkDTO.getBookmarkLink(), bookmarkDTO.getBookmarkName()));
    }

    @Transactional
    public void removeBookmark(String username, int bookmarkId) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        bookmarkRepository.removeBookmarksByUserIdAndBookmarkId(user.getId(), bookmarkId);
    }

    @Transactional
    public void removeAllBookmarks(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        bookmarkRepository.removeAllByUserId(user.getId());
    }

    @Transactional
    public void updateBookmark(String username, int bookmarkId, BookmarkDTO updatedBookmark) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Bookmark bookmark = bookmarkRepository.findBookmarkByUserIdAndBookmarkId(user.getId(), bookmarkId);
        if (bookmark != null) {
            bookmark.setBookmarkName(updatedBookmark.getBookmarkName());
            bookmark.setBookmarkLink(updatedBookmark.getBookmarkLink());
        } else {
            throw new RuntimeException("Bookmark not found");
        }
    }

    public List<Bookmark> searchBookmarkByKeyword(String username, String query) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookmarkRepository.findAllByUserIdAndBookmarkNameContainingIgnoreCase(user.getId(), query);
    }
}
