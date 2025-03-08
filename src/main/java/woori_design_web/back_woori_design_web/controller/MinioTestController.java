package woori_design_web.back_woori_design_web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import woori_design_web.back_woori_design_web.service.impl.MinioStorageService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test/minio")
public class MinioTestController {

    private final MinioStorageService minioStorageService;

    /**
     * 파일 업로드 테스트 API
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bucketName") String bucketName) throws IOException {
        
        String objectName = file.getOriginalFilename();
        minioStorageService.uploadFile(bucketName, objectName, file);
        return ResponseEntity.ok("File uploaded successfully: " + objectName);
    }

    /**
     * 파일 다운로드 테스트 API
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("objectName") String objectName) throws IOException {
        
        byte[] data = minioStorageService.downloadFile(bucketName, objectName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"")
                .body(data);
    }

    /**
     * 파일 삭제 테스트 API
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("objectName") String objectName) throws IOException {
        
        minioStorageService.deleteFile(bucketName, objectName);
        return ResponseEntity.ok("File deleted successfully: " + objectName);
    }
}
