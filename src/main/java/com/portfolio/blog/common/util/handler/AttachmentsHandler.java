package com.portfolio.blog.common.util.handler;

import com.portfolio.blog.post.domain.Attachments;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttachmentsHandler {

    public List<Attachments> parseAttachments(List<MultipartFile> files) throws Exception {
        List<Attachments> attachmentsList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(files)) {
            String path = "D:\\tmp\\blog\\attachments";
            File file  = new File(path);

            // 디렉토리 없을시 생성
            if (!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                if (!wasSuccessful)
                    System.out.println("file : was not successful");

            }

            // 다중파일 처리
            for (MultipartFile multipartFile : files) {
                String originExtension = "";
                String contentType = multipartFile.getContentType();

                // 확장자명 없으면 브레이크
                if (ObjectUtils.isEmpty(contentType))
                    break;

                // jpeg, png 만 허용
                if (contentType.contains("image/jpeg"))
                    originExtension = ".jpg";
                else if (contentType.contains("image/png"))
                    originExtension = ".png";
                else
                    break;

                // 첨부파일 객체 생성
                String newFileName = String.valueOf(System.currentTimeMillis()) + originExtension;
                Attachments attachments = Attachments.builder()
                        .path(path)
                        .origin(multipartFile.getOriginalFilename())
                        .name(newFileName)
                        .size(multipartFile.getSize())
                        .build();

                // 리스트에 추가
                attachmentsList.add(attachments);

                // 파일 저장
                file = new File(path + newFileName);
                multipartFile.transferTo(file);

                // 파일 권한 설정
                file.setWritable(true);
                file.setReadable(true);
            }
        }
        return attachmentsList;
    }

}
