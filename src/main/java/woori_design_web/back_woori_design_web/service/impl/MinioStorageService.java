package woori_design_web.back_woori_design_web.service.impl;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import woori_design_web.back_woori_design_web.service.ObjectStorageService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MinioStorageService implements ObjectStorageService {

    private final MinioClient minioClient;

    @Override
    public byte[] downloadFile(String bucketName, String objectName) throws IOException {
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return response.readAllBytes();
        } catch (Exception e) {
            throw new IOException("Failed to download file from MinIO", e);
        }
    }

    /**
     * MinIO에 파일을 업로드하는 메서드
     *
     * @param bucketName 버킷 이름
     * @param objectName 객체 이름
     * @param file       업로드할 파일
     * @throws IOException 파일 업로드 중 오류 발생시
     */
    public void uploadFile(String bucketName, String objectName, MultipartFile file) throws IOException {
        try {
            createBucketIfNotExists(bucketName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new IOException("Failed to upload file to MinIO", e);
        }
    }

    /**
     * MinIO에 저장된 파일을 삭제하는 메서드
     *
     * @param bucketName 버킷 이름
     * @param objectName 객체 이름
     * @throws IOException 파일 삭제 중 오류 발생시
     */
    public void deleteFile(String bucketName, String objectName) throws IOException {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new IOException("Failed to delete file from MinIO", e);
        }
    }

    /**
     * 버킷이 존재하지 않는 경우 새로 생성하는 메서드
     *
     * @param bucketName 버킷 이름
     * @throws IOException 버킷 생성 중 오류 발생시
     */
    private void createBucketIfNotExists(String bucketName) throws IOException {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new IOException("Failed to create bucket in MinIO", e);
        }
    }
}
