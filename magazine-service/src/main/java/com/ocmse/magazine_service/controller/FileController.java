package com.ocmse.magazine_service.controller;

import com.ocmse.magazine_service.service.MagazineService;
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
@RequestMapping("/magazines-file")
public class FileController {

    private final Path pdfDir;
    private final Path coverDir;
    private final MagazineService magazineService;

    public FileController(@Value("${pdf.dir}") String pdfDir,
                          @Value("${cover.dir}") String coverDir,
                          MagazineService magazineService) {
        this.pdfDir = Paths.get(pdfDir);
        this.coverDir = Paths.get(coverDir);
        this.magazineService = magazineService;
    }

    /**
     * ✅ Serve the magazine PDF file (download only if purchased)
     */
    @GetMapping("/download/{magazineId}")
    public ResponseEntity<Resource> downloadMagazine(@PathVariable UUID magazineId,
                                                 @RequestHeader("X-User-Id") UUID userId) {
        // Step 1: Verify purchase
        if (!magazineService.checkIfUserHasPurchased(userId, magazineId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You haven't purchased this magazine.");
        }

        // Step 2: Build file path
        Path filePath = pdfDir.resolve(magazineId + ".pdf");

        // Step 3: Serve file
        return serveFile(filePath, MediaType.APPLICATION_PDF, true);
    }

    /**
     * ✅ Serve cover images (publicly accessible)
     */
    @GetMapping("/cover/{magazineId}")
    public ResponseEntity<Resource> getCoverImage(@PathVariable UUID magazineId) {
        // Assuming cover image is stored with .jpg extension
        Path imagePath = coverDir.resolve(magazineId + ".jpg");

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
