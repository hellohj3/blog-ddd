package com.portfolio.blog.post.application.service;

import com.portfolio.blog.common.util.handler.AttachmentsHandler;
import com.portfolio.blog.post.domain.Attachments;
import com.portfolio.blog.post.infra.AttachmentsRepository;
import com.portfolio.blog.post.ui.dto.AttachmentsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachmentsService {

    private final AttachmentsHandler attachmentsHandler;
    private final AttachmentsRepository attachmentsRepository;

    // 첨부파일 저장
    public AttachmentsDto saveAttachments(MultipartFile multipartFile) throws Exception {
        Attachments attachments = attachmentsHandler.parseAttachments(multipartFile);
        Attachments savedAttachments = attachmentsRepository.save(attachments);

        return savedAttachments.parseDto();
    }

    // 첨부파일 수정
    /*public AttachmentsResponseDto updateAttachments(AttachmentsRequestDto requestDto) throws Exception {
        Optional<Attachments> findByOptional = attachmentsRepository.findById(requestDto.getId());
        Attachments findByAttachments = findByOptional.orElseThrow(() -> new Exception("첨부파일 없음"));
        String result = findByAttachments.updateAttachments(requestDto);

        if (!"fail".equals(result))
            attachmentsHandler.deleteFile(result);

        return findByAttachments.parseResponseDto();
    }*/
}
