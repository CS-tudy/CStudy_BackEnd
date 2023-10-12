package com.cstudy.moduleapi.controller.workbook;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.ControllerTest;
import com.cstudy.moduleapi.dto.workbook.WorkbookIdWithImagePath;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
import com.cstudy.modulecommon.dto.WorkbookSearchRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto.createWorkQuestion;
import static com.cstudy.modulecommon.dto.WorkbookResponseDto.createWorkbook;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class WorkbookControllerTest extends ControllerTest {

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    @AfterEach
    void cleanUp() {
        super.cleanup();
    }

    @DisplayName("/api/workbook")
    @Nested
    class 문제집_리스트_페이징 {

        private LocalDateTime time;
        private PageImpl<WorkbookResponseDto> workbookResponseDto;
        private PageImpl<WorkbookResponseDto> workbookResponseDto2;
        private PageImpl<WorkbookResponseDto> workbookResponseDto3;
        private PageImpl<WorkbookResponseDto> workbookResponseDto4;

        @BeforeEach
        void setUp() {
            time = LocalDateTime.of(2024, 5, 19, 20, 0);
            WorkbookResponseDto page1 = createWorkbook(1L, "제목1", "설명1", time);
            WorkbookResponseDto page2 = createWorkbook(2L, "제목2", "설명2", time.plusHours(1));
            WorkbookResponseDto page3 = createWorkbook(3L, "제목3", "설명3", time.plusHours(2));
            WorkbookResponseDto page4 = createWorkbook(4L, "제목4", "설명4", time.plusHours(3));

            List<WorkbookResponseDto> list = new ArrayList<>(Arrays.asList(page1, page2, page3, page4));
            workbookResponseDto = new PageImpl<>(list);

            List<WorkbookResponseDto> list2 = new ArrayList<>(List.of(page1));
            workbookResponseDto2 = new PageImpl<>(list2);

            List<WorkbookResponseDto> list3 = new ArrayList<>(List.of(page2));
            workbookResponseDto3 = new PageImpl<>(list3);

            List<WorkbookResponseDto> list4 = new ArrayList<>(List.of(page3));
            workbookResponseDto4 = new PageImpl<>(list4);


        }

        @Test
        public void 문제집_리스트_페이징_조회_성공_200() throws Exception {
            //given
            String url = "/api/workbook";
            given(workbookService.getWorkbookList(anyInt(), anyInt(), any(WorkbookSearchRequestDto.class)))
                    .willReturn(workbookResponseDto);
            //when
            ApiResponse<Page<WorkbookResponseDto>> response = workbookMockApiCaller.getWorkbookList(url);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);

            assertAll(
                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(4),

                    //WorkbookResponseDto page1 = createWorkbook(1L, "제목1", "설명1", time);
                    () -> assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("제목1"),
                    () -> assertThat(response.getBody().getContent().get(0).getDescription()).isEqualTo("설명1"),
                    () -> assertThat(response.getBody().getContent().get(0).getCreatedAt()).isEqualTo(time),

                    //WorkbookResponseDto page2 = createWorkbook(2L, "제목2", "설명2", time.plusHours(1));
                    () -> assertThat(response.getBody().getContent().get(1).getId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(1).getTitle()).isEqualTo("제목2"),
                    () -> assertThat(response.getBody().getContent().get(1).getDescription()).isEqualTo("설명2"),
                    () -> assertThat(response.getBody().getContent().get(1).getCreatedAt()).isEqualTo(time.plusHours(1)),

                    //WorkbookResponseDto page3 = createWorkbook(3L, "제목3", "설명3", time.plusHours(2));
                    () -> assertThat(response.getBody().getContent().get(2).getId()).isEqualTo(3L),
                    () -> assertThat(response.getBody().getContent().get(2).getTitle()).isEqualTo("제목3"),
                    () -> assertThat(response.getBody().getContent().get(2).getDescription()).isEqualTo("설명3"),
                    () -> assertThat(response.getBody().getContent().get(2).getCreatedAt()).isEqualTo(time.plusHours(2)),

                    //WorkbookResponseDto page4 = createWorkbook(4L, "제목4", "설명4", time.plusHours(3));
                    () -> assertThat(response.getBody().getContent().get(3).getId()).isEqualTo(4L),
                    () -> assertThat(response.getBody().getContent().get(3).getTitle()).isEqualTo("제목4"),
                    () -> assertThat(response.getBody().getContent().get(3).getDescription()).isEqualTo("설명4"),
                    () -> assertThat(response.getBody().getContent().get(3).getCreatedAt()).isEqualTo(time.plusHours(3))
            );
        }

        @Test
        public void 문제집_리스트_조회_제목_성공_200() throws Exception {
            //given
            String url = "/api/workbook?title=제목1";

            String title = "제목1";

            WorkbookSearchRequestDto workbookSearchRequestDto = WorkbookSearchRequestDto.builder()
                    .title(title)
                    .build();

            given(workbookService.getWorkbookList(anyInt(), anyInt(), eq(workbookSearchRequestDto)))
                    .willReturn(workbookResponseDto2);

            //when
            ApiResponse<Page<WorkbookResponseDto>> response = workbookMockApiCaller.getWorkbookList(url);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);

            assertAll(
                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(1),

                    //WorkbookResponseDto page1 = createWorkbook(1L, "제목1", "설명1", time);
                    () -> assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("제목1"),
                    () -> assertThat(response.getBody().getContent().get(0).getDescription()).isEqualTo("설명1"),
                    () -> assertThat(response.getBody().getContent().get(0).getCreatedAt()).isEqualTo(time)
            );
        }

        @Test
        public void 문제집_리스트_조회_설명_성공_200() throws Exception {
            //given
            String description = "설명2";
            String url = "/api/workbook?description=설명2";

            WorkbookSearchRequestDto workbookSearchRequestDto = WorkbookSearchRequestDto.builder()
                    .description(description)
                    .build();
            given(workbookService.getWorkbookList(anyInt(), anyInt(), eq(workbookSearchRequestDto)))
                    .willReturn(workbookResponseDto3);

            //when
            ApiResponse<Page<WorkbookResponseDto>> response = workbookMockApiCaller.getWorkbookList(url);

            //Then
            assertThat(response.getStatus()).isEqualTo(200);

            assertAll(
                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(1),

                    //WorkbookResponseDto page1 = createWorkbook(1L, "제목1", "설명1", time);
                    () -> assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(2L),
                    () -> assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("제목2"),
                    () -> assertThat(response.getBody().getContent().get(0).getDescription()).isEqualTo("설명2"),
                    () -> assertThat(response.getBody().getContent().get(0).getCreatedAt()).isEqualTo(time.plusHours(1))
            );
        }

        @Test
        public void 문제집_리스트_조회_제목_내용_성공_200() throws Exception {
            //given
            String titleDesc = "제목3내용3";
            String url = "/api/workbook?title_desc=제목3내용3";

            //when
            WorkbookSearchRequestDto requestDto =WorkbookSearchRequestDto.builder()
                            .titleDesc(titleDesc)
                                    .build();
            given(workbookService.getWorkbookList(anyInt(),anyInt(),eq(requestDto)))
                    .willReturn(workbookResponseDto4);

            ApiResponse<Page<WorkbookResponseDto>> response = workbookMockApiCaller.getWorkbookList(url);

            //Then
            assertThat(response.getStatus()).isEqualTo(200);

            assertAll(
                    () -> assertThat(response.getBody().getTotalPages()).isEqualTo(1),
                    () -> assertThat(response.getBody().getSize()).isEqualTo(1),

                    //WorkbookResponseDto page3 = createWorkbook(3L, "제목3", "설명3", time.plusHours(2));
                    () -> assertThat(response.getBody().getContent().get(0).getId()).isEqualTo(3L),
                    () -> assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("제목3"),
                    () -> assertThat(response.getBody().getContent().get(0).getDescription()).isEqualTo("설명3"),
                    () -> assertThat(response.getBody().getContent().get(0).getCreatedAt()).isEqualTo(time.plusHours(2))
            );
        }
    }

    @DisplayName("/api/workbook/{id}")
    @Nested
    class 문제집_정보_요청 {

        private LocalDateTime time;

        @BeforeEach
        void setUp() {
            time = LocalDateTime.of(2024, 5, 19, 20, 0);
            WorkbookResponseDto responseDto = createWorkbook(1L, "제목1", "설명1", time);

            given(workbookService.getWorkbook(anyLong())).willReturn(responseDto);
        }

        @Test
        public void 문제집_정보_요청_성공_200() throws Exception {
            //given
            long path = 1L;
            //when
            ApiResponse<WorkbookResponseDto> response = workbookMockApiCaller.getWorkbookById(path);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody().getId()).isEqualTo(1L);
            assertThat(response.getBody().getTitle()).isEqualTo("제목1");
            assertThat(response.getBody().getDescription()).isEqualTo("설명1");
            assertThat(response.getBody().getCreatedAt()).isEqualTo(time);
        }

        @Test
        public void 문제집_정보_요청_path_음수() throws Exception {
            //given
            String url = "/api/workbook/-1";
            //when
            ApiResponse<ErrorResponse> response = workbookMockApiCaller.sendGetRequest_WithNoAuthorization_ExpectErrorResponse(url, null);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody().getCode()).isEqualTo("8500");
            assertThat(response.getBody().getMessage()).isEqualTo("pathvariable은 양수로 처리를 해야됩니다.-1");
        }
    }

    @DisplayName("/api/workbook/upload/{id}")
    @Nested
    class 문제집_이미지_업로드 {

        private MockMultipartFile mockFile;

        @BeforeEach
        void setUp() {

        }

        @Test
        public void 이미지_업로드_성공_201() throws Exception {
            //given
            mockFile = new MockMultipartFile("file", "image.jpg", "text/plain", "이미지".getBytes());
            Long id = 1L;
            //when
            ApiResponse<String> response = workbookMockApiCaller.uploadFile(mockFile, id);
            //Then
            assertThat(response.getStatus()).isEqualTo(201);
        }

        @Test
        public void 이미지_업로드_martipart_value_가_다르면_400() throws Exception {
            //given
            mockFile = new MockMultipartFile("error", "image.jpg", "text/plain", "이미지".getBytes());
            Long id = 1L;
            //when
            ApiResponse<String> response = workbookMockApiCaller.uploadFileInvalid(mockFile, id);
            //Then
            assertThat(response.getStatus()).isEqualTo(400);
        }

        @Test
        public void 이미지_업로드_path_음수() throws Exception {
            //given
            mockFile = new MockMultipartFile("file", "image.jpg", "text/plain", "이미지".getBytes());
            Long id = -1L;

            //when
            ApiResponse<String> response = workbookMockApiCaller.uploadFileInvalid(mockFile, id);

            //Then
            assertThat(response.getStatus()).isEqualTo(400);
            assertThat(response.getBody()).isEqualTo("{\"code\":\"8500\"," +
                    "\"message\":\"pathvariable은 양수로 처리를 해야됩니다.-1\"," +
                    "\"validation\":{\"pathvariable\":\"id는 양수로 처리를 해야됩니다.\"}}");
        }
    }

    @Nested
    class 문제집_이미지_List {

        @BeforeEach
        void setUp() {

            WorkbookIdWithImagePath workbookIdWithImagePath = WorkbookIdWithImagePath.builder()
                    .id(1L)
                    .imagePath(List.of("img1", "img2"))
                    .build();

            WorkbookIdWithImagePath workbookIdWithImagePath2 = WorkbookIdWithImagePath.builder()
                    .id(2L)
                    .imagePath(List.of("img3", "img4"))
                    .build();

            List<WorkbookIdWithImagePath> workbookIdWithImagePaths = new ArrayList<>(Arrays.asList(workbookIdWithImagePath, workbookIdWithImagePath2));


            given(workbookService.getWorkbookImagePathList()).willReturn(workbookIdWithImagePaths);
        }

        @Test
        public void 문제집_이미지_path_성공_200() throws Exception {
            //given

            //when
            ApiResponse<List<WorkbookIdWithImagePath>> response = workbookMockApiCaller.getWorkbookImageList();
            //Then
            assertThat(response.getStatus()).isEqualTo(200);


            assertAll(
                    () -> assertEquals(response.getBody().get(0).getId(), 1L),
                    () -> assertThat(response.getBody().get(0).getImagePath().size()).isEqualTo(2),
                    () -> assertThat(response.getBody().get(0).getImagePath().get(0)).isEqualTo("img1"),
                    () -> assertThat(response.getBody().get(0).getImagePath().get(1)).isEqualTo("img2"),

                    () -> assertThat(response.getBody().get(1).getImagePath().get(0)).isEqualTo("img3"),
                    () -> assertThat(response.getBody().get(1).getImagePath().get(1)).isEqualTo("img4")
            );
        }

    }


    @DisplayName("/api/workbook/{id}/questions")
    @Nested
    class 문제집_문제_요청 {

        @BeforeEach
        void setUp() {
            WorkbookQuestionResponseDto page1 = createWorkQuestion(1L, 1L, "제목1");
            WorkbookQuestionResponseDto page2 = createWorkQuestion(2L, 2L, "제목2");
            WorkbookQuestionResponseDto page3 = createWorkQuestion(3L, 3L, "제목3");
            WorkbookQuestionResponseDto page4 = createWorkQuestion(4L, 4L, "제목4");

            List<WorkbookQuestionResponseDto> list = new ArrayList<>(Arrays.asList(page1, page2, page3, page4));
            PageImpl<WorkbookQuestionResponseDto> responseDtoPage = new PageImpl<>(list);

            given(workbookService.getQuestions(anyLong(), any(Pageable.class)))
                    .willReturn(responseDtoPage);
        }

        @Test
        public void 문제집_문제_요청_성공_200() throws Exception {
            //given
            long id = 1L;
            //when
            ApiResponse<Page<WorkbookQuestionResponseDto>> response = workbookMockApiCaller.getQuestions(id);
            //Then
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getBody().getSize()).isEqualTo(4);
            assertThat(response.getBody().getTotalPages()).isEqualTo(1);

            assertThat(response.getBody().getContent().get(0).getQuestionId()).isEqualTo(1L);
            assertThat(response.getBody().getContent().get(0).getWorkbookQuestionId()).isEqualTo(1L);
            assertThat(response.getBody().getContent().get(0).getTitle()).isEqualTo("제목1");

            assertThat(response.getBody().getContent().get(1).getQuestionId()).isEqualTo(2L);
            assertThat(response.getBody().getContent().get(1).getWorkbookQuestionId()).isEqualTo(2L);
            assertThat(response.getBody().getContent().get(1).getTitle()).isEqualTo("제목2");

            assertThat(response.getBody().getContent().get(2).getQuestionId()).isEqualTo(3L);
            assertThat(response.getBody().getContent().get(2).getWorkbookQuestionId()).isEqualTo(3L);
            assertThat(response.getBody().getContent().get(2).getTitle()).isEqualTo("제목3");

            assertThat(response.getBody().getContent().get(3).getQuestionId()).isEqualTo(4L);
            assertThat(response.getBody().getContent().get(3).getWorkbookQuestionId()).isEqualTo(4L);
            assertThat(response.getBody().getContent().get(3).getTitle()).isEqualTo("제목4");
        }

    }
}