package com.cstudy.moduleapi.application.workbook.impl;

import com.cstudy.moduleapi.application.workbook.WorkbookService;
import com.cstudy.moduleapi.config.s3.AwsS3Util;
import com.cstudy.moduleapi.dto.workbook.CreateWorkbookRequestDto;
import com.cstudy.moduleapi.dto.workbook.QuestionIdRequestDto;
import com.cstudy.moduleapi.dto.workbook.WorkbookIdWithImagePath;
import com.cstudy.moduleapi.dto.workbook.WorkbookQuestionRequestDto;
import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.domain.workbook.Workbook;
import com.cstudy.modulecommon.domain.workbook.WorkbookQuestion;
import com.cstudy.modulecommon.dto.UpdateWorkbookRequestDto;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import com.cstudy.modulecommon.dto.WorkbookSearchRequestDto;
import com.cstudy.modulecommon.error.question.NotFoundQuestionWithChoicesAndCategoryById;
import com.cstudy.modulecommon.error.workbook.NotFoundWorkbook;
import com.cstudy.modulecommon.error.workbook.NotFoundWorkbookQuestion;
import com.cstudy.modulecommon.repository.file.FileRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookQuestionRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkbookServiceImpl implements WorkbookService {

    private final static String WORKBOOK_BASIC_PICTURE = "WORKBOOK_BASIC_PICTURE";

    @Value("${img.cloudFront}")
    String BASE_CLOUD_FRONT;

    private final WorkbookRepository workbookRepository;
    private final WorkbookQuestionRepository workbookQuestionRepository;
    private final QuestionRepository questionRepository;
    private final FileRepository fileRepository;
    private final AwsS3Util awsS3Util;

    public WorkbookServiceImpl(
            WorkbookRepository workbookRepository,
            WorkbookQuestionRepository workbookQuestionRepository,
            QuestionRepository questionRepository,
            FileRepository fileRepository
            , AwsS3Util awsS3Util
    ) {
        this.workbookRepository = workbookRepository;
        this.workbookQuestionRepository = workbookQuestionRepository;
        this.questionRepository = questionRepository;
        this.fileRepository = fileRepository;
        this.awsS3Util = awsS3Util;
    }

    /**
     * 문제집 전체 조회 페이지의 페이징을 담당
     * PAGE, SIZE를 통해서 수를 조정하고 SEARCH를 통해서 페이징을 한다.
     *
     */
    @Override
    public Page<WorkbookResponseDto> getWorkbookList(int page, int size, WorkbookSearchRequestDto requestDto) {
        Pageable pageable = PageRequest.of(page, size);
        return workbookRepository.findWorkbookList(pageable, requestDto);
    }

    /**
     * 문제집의 이미지의 정보를 list를 통해서 보낸다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkbookIdWithImagePath> getWorkbookImagePathList() {
        return workbookRepository.findByIdWithWorkbook().stream()
                .map(workbook -> {
                    List<String> imagePaths = workbook.getFiles().stream()
                            .map(file -> BASE_CLOUD_FRONT + file.getFileName())
                            .collect(Collectors.toList());

                    if (imagePaths.isEmpty()) {
                        imagePaths.add(WORKBOOK_BASIC_PICTURE);
                    }

                    return WorkbookIdWithImagePath.builder()
                            .id(workbook.getId())
                            .imagePath(imagePaths)
                            .build();})
                .collect(Collectors.toList());
    }


    /**
     * 문제집의 페이징을 처리한다.
     */
    @Override
    @Transactional(readOnly = true)
    public WorkbookResponseDto getWorkbook(Long id) {
        return WorkbookResponseDto.of(workbookRepository.findById(id)
                .orElseThrow(() -> new NotFoundWorkbook(id)));
    }

    @Override
    @Transactional
    public Page<WorkbookQuestionResponseDto> getQuestions(Long id, Pageable pageable) {
        return workbookRepository.findWorkbookQuestionList(pageable, id);
    }

    /**
     * 새로운 문제집을 생성한다.
     * 이때 문제집은 관리자만 생성이 가능하다.
     */
    @Override
    @Transactional
    public Long createWorkbook(CreateWorkbookRequestDto workbookDto) {
        Workbook workbook = Workbook.builder()
                .title(workbookDto.getTitle())
                .description(workbookDto.getDescription())
                .build();
        workbookRepository.save(workbook);
        return workbook.getId();
    }

    @Override
    @Transactional
    public void addQuestion(WorkbookQuestionRequestDto requestDto) {
        Workbook workbook = workbookRepository.findById(requestDto.getWorkbookId())
                .orElseThrow(() -> new NotFoundWorkbook(requestDto.getWorkbookId()));

        for (QuestionIdRequestDto qId : requestDto.getQuestionIds()) {
            Question question = questionRepository.findById(qId.getId())
                    .orElseThrow(() -> new NotFoundQuestionWithChoicesAndCategoryById(qId.getId()));
            if (workbookQuestionRepository.existsByWorkbookAndQuestion(workbook, question)) {
                continue;
            }
            addWorkbookQuestion(workbook, question);
        }
    }

    @Transactional
    public void addWorkbookQuestion(Workbook workbook, Question question) {

        WorkbookQuestion workbookQuestion = WorkbookQuestion.builder()
                .workbook(workbook)
                .question(question)
                .build();

        workbookQuestionRepository.save(workbookQuestion);
        workbook.addQuestion(workbookQuestion);
    }

    /**
     * 문제집의 정보를 업데이트 한다.
     */
    @Override
    @Transactional
    public void updateWorkbook(UpdateWorkbookRequestDto requestDto) {
        workbookRepository.findById(requestDto.getId())
                .orElseThrow(() -> new NotFoundWorkbook(requestDto.getId()))
                .changeWorkbook(requestDto);
    }

    /**
     * 문제집의 문제를 삭제한다.
     */
    @Override
    @Transactional
    public void deleteQuestion(WorkbookQuestionRequestDto requestDto) {

        Workbook workbook = workbookRepository.findById(requestDto.getWorkbookId())
                .orElseThrow(() -> new NotFoundWorkbook(requestDto.getWorkbookId()));

        for (QuestionIdRequestDto qId : requestDto.getQuestionIds()) {
            Question question = questionRepository.findById(qId.getId())
                    .orElseThrow(() -> new NotFoundQuestionWithChoicesAndCategoryById(qId.getId()));
            WorkbookQuestion workbookQuestion = workbookQuestionRepository.findByWorkbookAndQuestion(workbook, question)
                    .orElseThrow(NotFoundWorkbookQuestion::new);
            workbook.deleteQuestion(workbookQuestion);
            workbookQuestionRepository.delete(workbookQuestion);
        }
    }

    /**
     * 단일 문제집 업로드
     */
    @Override
    @Transactional
    public void uploadFile(MultipartFile file, Long workbookId) {
        String uploadFileName = awsS3Util.uploadFile(file);
        String fileNameString = Paths.get(URI.create(uploadFileName).getPath()).getFileName().toString();

        log.info("Uploading file : {} ", uploadFileName);
        log.info("fileName 입니다. : {}", fileNameString);

        Workbook workbook = workbookRepository.findById(workbookId)
                .orElseThrow(() -> new NotFoundWorkbook(workbookId));

        File fileName = File.builder()
                .fileName(fileNameString)
                .workbook(workbook)
                .build();

        fileRepository.save(fileName);
    }


}
