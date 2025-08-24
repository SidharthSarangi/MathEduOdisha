package com.ocmse.book_service.controller;

import com.ocmse.book_service.service.BookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/books-file")
public class FileController {

    private final Path pdfDir;
    private final Path coverDir;
    private final BookService bookService;

    public FileController(@Value("${pdf.dir}") String pdfDir,
                          @Value("${cover.dir}") String coverDir,
                          BookService bookService) {
        this.pdfDir = Paths.get(pdfDir);
        this.coverDir = Paths.get(coverDir);
        this.bookService = bookService;
    }

    /**
     * ✅ Serve the book PDF file (download only if purchased)
     */
    @GetMapping("/download/{bookId}")
    public ResponseEntity<Resource> downloadBook(@PathVariable UUID bookId,
                                                 @RequestHeader("X-User-Id") UUID userId) {
        // Step 1: Verify purchase
        if (!bookService.checkIfUserHasPurchased(userId, bookId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You haven't purchased this book.");
        }

        // Step 2: Build file path
        Path filePath = pdfDir.resolve(bookId + ".pdf");

        // Step 3: Serve file
        return serveFile(filePath, MediaType.APPLICATION_PDF, true);
    }

    /**
     * ✅ Serve cover images (publicly accessible)
     */
    @GetMapping("/cover/{bookId}")
    public ResponseEntity<Resource> getCoverImage(@PathVariable UUID bookId) {
        // Assuming cover image is stored with .jpg extension
        Path imagePath = coverDir.resolve(bookId + ".jpg");

        // Detect MIME type (e.g., image/jpeg or image/png)
        MediaType mediaType = MediaType.IMAGE_JPEG;
        try {
            String mimeType = Files.probeContentType(imagePath);
            if (mimeType != null) {
                mediaType = MediaType.parseMediaType(mimeType);
            }
        } catch (Exception ignored) {}

        return serveFile(imagePath, mediaType, false);
    }

    /**
     * ✅ Reusable method to serve files from disk
     */
    private ResponseEntity<Resource> serveFile(Path path, MediaType mediaType, boolean download) {
        try {
            if (!Files.exists(path)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found.");
            }

            Resource resource = new UrlResource(path.toUri());
            HttpHeaders headers = new HttpHeaders();

            if (download) {
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName());
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serve file.");
        }
    }
}
