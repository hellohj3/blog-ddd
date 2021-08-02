package com.portfolio.blog.post.application.service;

import com.portfolio.blog.common.util.handler.AttachmentsHandler;
import com.portfolio.blog.post.domain.Attachments;
import com.portfolio.blog.post.infra.AttachmentsRepository;
import com.portfolio.blog.post.ui.dto.AttachmentsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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

    // 첨부파일 리스트 (in names)
    public List<AttachmentsDto> findAttachmentsByNames(List<String> names) {
        List<AttachmentsDto> attachmentsDtos = new ArrayList<>();
        List<Attachments> findByAttachmentsList = attachmentsRepository.findByNameIn(names);

        for (Attachments attachments : findByAttachmentsList)
            attachmentsDtos.add(attachments.parseDto());

        return attachmentsDtos;
    }
}
