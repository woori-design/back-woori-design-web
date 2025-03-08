package woori_design_web.back_woori_design_web.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ObjectStorageService {

    /**
     * 객체 스토리지에서 파일을 다운로드 받는 메서드
     *
     * @param bucketName 버킷 이름
     * @param objectName 객체 이름
     * @return 파일 데이터의 바이트 배열
     * @throws IOException 파일 다운로드 중 오류 발생시
     */
    byte[] downloadFile(String bucketName, String objectName) throws IOException;

}
