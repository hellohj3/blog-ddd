package com.portfolio.blog.common.util.handler;

import com.portfolio.blog.post.domain.Attachments;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttachmentsHandler {
    @Value("${env.imageUploadBufferSrc}")
    String uploadBufferPath;
    @Value("${env.imageLoadSrc}")
    String loadPath;
    @Value("${env.imageUploadSrc}")
    String upLoadPath;
    Attachments attachments = Attachments.builder().build();

    public Attachments parseAttachments(MultipartFile multipartFile) throws Exception {
        if (!multipartFile.isEmpty()) {
            File fileUploadBufferPath = new File(uploadBufferPath);
            File fileUploadPath = new File(upLoadPath);

            // 디렉토리 없을시 생성
            if (!fileUploadBufferPath.exists()) {
                boolean wasSuccessful = fileUploadBufferPath.mkdirs();

                if (!wasSuccessful)
                    System.out.println("file : was not successful");
            }
            if (!fileUploadPath.exists()) {
                boolean wasSuccessful = fileUploadPath.mkdirs();

                if (!wasSuccessful)
                    System.out.println("file : was not successful");
            }

            // 단일파일 처리
            String originExtension = "";
            String contentType = multipartFile.getContentType();

            // 확장자명 없으면 브레이크
            if (ObjectUtils.isEmpty(contentType))
                throw new IllegalArgumentException("존재하지 않는 확장자");

            // jpeg, png 만 허용
            if (contentType.contains("image/jpeg"))
                originExtension = ".jpg";
            else if (contentType.contains("image/png"))
                originExtension = ".png";
            else
                throw new IllegalArgumentException("지원하지 않는 확장자");

            // 파일 저장
            String newFileName = String.valueOf(System.currentTimeMillis()) + originExtension;
            File file = new File(uploadBufferPath + "\\" + newFileName);
            multipartFile.transferTo(file);

            // 파일 권한 설정
            file.setWritable(true);
            file.setReadable(true);

            // 첨부파일 객체 생성
            attachments = Attachments.builder()
                    .path(loadPath)
                    .origin(multipartFile.getOriginalFilename())
                    .name(newFileName)
                    .size(multipartFile.getSize())
                    .build();
        }
        return attachments;
    }

    // 실제 사용중인 디렉토리에서 파일 일괄 삭제
    public void deleteRealFileBulk(List<String> fileNames) throws Exception {
        if (fileNames.size() > 0) {
            for (String fileName : fileNames) {
                deleteRealFileSingle(fileName);
            }
        }
    }

    // 실제 사용중인 디렉토리에서 파일 단건 삭제
    public void deleteRealFileSingle(String fileName) throws Exception {
        File file = new File(upLoadPath + "\\" + fileName);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("file : was deleted");
            } else {
                System.out.println("file : was not deleted");
            }
        }
    }

    // 버퍼 디렉토리에서 실제 사용하는 디렉토리로 파일 복사
    public void copyFile(List<String> fileNames) throws Exception {
        if (fileNames.size() > 0) {
            for (String fileName : fileNames) {
                File oldFile = new File(uploadBufferPath + "\\" + fileName);
                File newFile = new File(upLoadPath + "\\" + fileName);

                Files.copy(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

}
