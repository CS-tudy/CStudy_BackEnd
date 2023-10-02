package com.cstudy.moduleapi.application.workbook.impl;

import com.cstudy.moduleapi.application.workbook.WorkbookService;
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
import com.cstudy.modulecommon.error.question.NotFoundQuestionWithChoicesAndCategoryById;
import com.cstudy.modulecommon.error.workbook.NotFoundWorkbook;
import com.cstudy.modulecommon.error.workbook.NotFoundWorkbookQuestion;
import com.cstudy.modulecommon.repository.file.FileRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookQuestionRepository;
import com.cstudy.modulecommon.repository.workbook.WorkbookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkbookServiceImpl implements WorkbookService {

    private final WorkbookRepository workbookRepository;
    private final WorkbookQuestionRepository workbookQuestionRepository;
    private final QuestionRepository questionRepository;
    private final FileRepository fileRepository;
//    private final AwsS3Util awsS3Util;

    public WorkbookServiceImpl(
            WorkbookRepository workbookRepository,
            WorkbookQuestionRepository workbookQuestionRepository,
            QuestionRepository questionRepository,
            FileRepository fileRepository
//            ,AwsS3Util awsS3Util
    ) {
        this.workbookRepository = workbookRepository;
        this.workbookQuestionRepository = workbookQuestionRepository;
        this.questionRepository = questionRepository;
        this.fileRepository = fileRepository;
//        this.awsS3Util = awsS3Util;
    }

    /**
     * Get Workbook list.
     *
     * @param pageable    page information
     * @param title       search workbook containing title
     * @param description search workbook containing description
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkbookResponseDto> getWorkbookList(
            Pageable pageable,
            String title,
            String description,
            String titleDesc
    ) {
        return workbookRepository.findWorkbookList(pageable, title, description, titleDesc);
    }

    /**
     * 문제집 전체 List와 List에 따른 이미지 Path
     * @return WorkbookIdWithImagePath -> workbookId, imagePath (List)
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkbookIdWithImagePath> getWorkbookImagePathList() {

        List<Workbook> workbooks = workbookRepository.findByIdWithWorkbook();

        return workbooks.stream()
                .map(workbook -> {
                    List<String> imagePaths = workbook.getFiles().stream()
                            .map(File::getFileName)
                            .collect(Collectors.toList());

                    return WorkbookIdWithImagePath.builder()
                            .id(workbook.getId())
                            .imagePath(imagePaths)
                            .build();
                })
                .collect(Collectors.toList());
    }


    /**
     * Get Workbook.
     *
     * @param id workbook id
     */
    @Override
    @Transactional(readOnly = true)
    public WorkbookResponseDto getWorkbook(Long id) {
        Workbook workbook = workbookRepository.findById(id)
                .orElseThrow(() -> new NotFoundWorkbook(id));
        return WorkbookResponseDto.of(workbook);
    }

    /**
     * Get question list in workbook.
     *
     * @param pageable page information
     * @param id       workbook id
     */
    @Override
    @Transactional
    public Page<WorkbookQuestionResponseDto> getQuestions(Long id, Pageable pageable) {
        return workbookRepository.findWorkbookQuestionList(pageable, id);
    }

    /**
     * Create Workbook.
     *
     * @param workbookDto workbook information containing title and description.
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

    /**
     * Add question in workbook.
     *
     * @param requestDto contains question information.
     */
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
     * update workbook information.
     *
     * @param requestDto contains workbook information.
     */
    @Override
    @Transactional
    public void updateWorkbook(UpdateWorkbookRequestDto requestDto) {
        workbookRepository.findById(requestDto.getId())
                .orElseThrow(() -> new NotFoundWorkbook(requestDto.getId()))
                .changeWorkbook(requestDto);
    }

    /**
     * delete question in workbook.
     *
     * @param requestDto contains question and workbook id.
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
     *
     * @param file       사진 업로드
     * @param workbookId 문제집 아이디
     */
    @Override
    @Transactional
    public void uploadFile(MultipartFile file, Long workbookId) {

//        String uploadFileName = awsS3Util.uploadFile(file);

//        log.info("Uploading file : {} ", uploadFileName);

        Workbook workbook = workbookRepository.findById(workbookId)
                .orElseThrow(() -> new NotFoundWorkbook(workbookId));

        File fileName = File.builder()
//                .fileName(uploadFileName)
                .workbook(workbook)
                .build();

        fileRepository.save(fileName);
    }


}
