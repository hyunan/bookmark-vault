package com.github.hyunan.bookmarkvault.controller;

import com.github.hyunan.bookmarkvault.dto.BookmarkDTO;
import com.github.hyunan.bookmarkvault.service.AuthService;
import com.github.hyunan.bookmarkvault.service.BookmarkService;
import com.github.hyunan.bookmarkvault.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final JwtService jwtService;
    private final AuthService authService;
    private final BookmarkService bookmarkService;

    public BookmarkController(JwtService jwtService, AuthService authService, BookmarkService bookmarkService) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/viewBookmarks")
    public ResponseEntity<?> getBookmarks(@RequestHeader(value = "Authorization") String bearerToken) {
        if (!authService.isAuthorized(bearerToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
        String token = bearerToken.substring("Bearer ".length());
        try {
            List<BookmarkDTO> bookmarkList = bookmarkService.getUserBookmarks(jwtService.extractUsernameFromToken(token));
            return ResponseEntity.ok().body(Map.of("bookmarks", bookmarkList));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }

    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadBookmarks(@RequestParam("file")MultipartFile file,@RequestHeader(value = "Authorization") String bearerToken) {
        if (!authService.isAuthorized(bearerToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
        String token = bearerToken.substring("Bearer ".length());
        try {
            bookmarkService.uploadBookmarks(file, jwtService.extractUsernameFromToken(token));
            return ResponseEntity.ok().body(Map.of("success", "bookmarks were successfully uploaded"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "something went wrong while uploading"));
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadBookmarks(@RequestHeader(value = "Authorization") String bearerToken) {
        if (!authService.isAuthorized(bearerToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
        String token = bearerToken.substring("Bearer ".length());
        try {
            String html = bookmarkService.downloadBookmarks(jwtService.extractUsernameFromToken(token));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"bookmarks.html\"")
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllBookmarks(@RequestHeader(value = "Authorization") String bearerToken) {
        if (!authService.isAuthorized(bearerToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
        String token = bearerToken.substring("Bearer ".length());
        try {
            bookmarkService.removeAllBookmarks(jwtService.extractUsernameFromToken(token));
            return ResponseEntity.ok().body(Map.of("success", "user's bookmarks all deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{bookmarkId}")
    public ResponseEntity<?> deleteBookmark(@RequestHeader(value = "Authorization") String bearerToken, @PathVariable int bookmarkId) {
        if (!authService.isAuthorized(bearerToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
        String token = bearerToken.substring("Bearer ".length());
        try {
            bookmarkService.removeBookmark(jwtService.extractUsernameFromToken(token), bookmarkId);
            return ResponseEntity.ok().body(Map.of("success", "bookmark successfully deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBookmark(@RequestHeader(value = "Authorization") String bearerToken, @RequestBody BookmarkDTO bookmarkDTO) {
        if (!authService.isAuthorized(bearerToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
        String token = bearerToken.substring("Bearer ".length());
        try {
            bookmarkService.addBookmark(jwtService.extractUsernameFromToken(token), bookmarkDTO);
            return ResponseEntity.ok().body(Map.of("success", "added bookmark"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/edit/{bookmarkId}")
    public ResponseEntity<?> editBookmark(@RequestHeader(value = "Authorization") String bearerToken, @PathVariable int bookmarkId, @RequestBody BookmarkDTO bookmarkDTO) {
        if (!authService.isAuthorized(bearerToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
        String token = bearerToken.substring("Bearer ".length());
        try {
            bookmarkService.updateBookmark(jwtService.extractUsernameFromToken(token), bookmarkId, bookmarkDTO);
            return ResponseEntity.ok().body(Map.of("success", "edited bookmark"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<?> helloWorld(@RequestHeader(value = "Authorization") String bearerToken) {
        if (!authService.isAuthorized(bearerToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "user not authorized"));
        return ResponseEntity.ok().body(Map.of("success", "user is authorized\n Hello World!"));
    }
}
