package com.portfolio.blog.common.util.handler;

import com.portfolio.blog.post.domain.Attachments;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttachmentsHandler {
    @Value("${env.imageUploadSrc}")
    String path;
    Attachments attachments = Attachments.builder().build();

    public Attachments parseAttachments(MultipartFile multipartFile) throws Exception {
        if (!multipartFile.isEmpty()) {
            File file  = new File(path);

            // 디렉토리 없을시 생성
            if (!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

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
            file = new File(path + "\\" + newFileName);
            multipartFile.transferTo(file);

            // 파일 권한 설정
            file.setWritable(true);
            file.setReadable(true);

            // 첨부파일 객체 생성
            attachments = Attachments.builder()
                    .path(path)
                    .origin(multipartFile.getOriginalFilename())
                    .name(newFileName)
                    .size(multipartFile.getSize())
                    .build();
        }
        return attachments;
    }

    public void deleteFile(String fileName) throws Exception {
        if (!fileName.isEmpty()) {
            File file = new File(path + "\\" + fileName);

            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("file : was deleted");
                } else {
                    System.out.println("file : was not deleted");
                }
            }
        }
    }

}
