package com.portfolio.blog.post.application.service;

import com.portfolio.blog.post.infra.AttachmentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttachmentsService {

    private final AttachmentsRepository;



}
