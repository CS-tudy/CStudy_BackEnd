package com.cstudy.moduleapi.application.request.controller;

import com.cstudy.moduleapi.config.ControllerTestBase;
import com.cstudy.moduleapi.dto.request.CreateRequestRequestDto;
import com.cstudy.moduleapi.dto.request.FlagRequestDto;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.dto.UpdateRequestRequestDto;
import com.cstudy.modulecommon.error.enums.ErrorCode;
import com.cstudy.modulecommon.error.request.NotFoundRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class RequestControllerTest extends ControllerTestBase {


    private String token;
    private static String INVALID_TOKEN;
    private static String ADMIN_VALID_TOKEN;
    private static String CUSTOM_VALID_TOKEN;


    @BeforeEach
    void setUp() {
        token = jwtTokenizer.createAccessToken(1L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.ADMIN.getRoleName()));
        INVALID_TOKEN = jwtTokenizer.createAccessToken(2L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.CUSTOM.getRoleName()));
        ADMIN_VALID_TOKEN = jwtTokenizer.createAccessToken(1L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.ADMIN.getRoleName()));
        CUSTOM_VALID_TOKEN = jwtTokenizer.createAccessToken(2L, MemberTestEnum.ADMIN_EMAIL.getMessage(), List.of(RoleEnum.CUSTOM.getRoleName()));
    }

    @Test
    @DisplayName("특정 게시글 조회")
    void getRequestWithValid() throws Exception {
        //given
        Long id = 1L;

        LocalDateTime now = LocalDateTime.now();
        given(requestService.getRequest(anyLong())).willReturn(RequestResponseDto.builder()
                .id(1L)
                .title("제목")
                .memberId(1L)
                .description("설명")
                .createAt(now)
                .memberName("김무건")
                .flag(false)
                .build());

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/request/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.description").value("설명"))
                .andExpect(jsonPath("$.memberName").value("김무건"))
                .andExpect(jsonPath("$.flag").value(false))
                .andDo(MockMvcResultHandlers.print());
        //then
        verify(requestService).getRequest(anyLong());
    }

    @Test
    @DisplayName("게시판 문제 요청글 생성")
    void createRequestWithValid() throws Exception {
        //given
        CreateRequestRequestDto requestDto = CreateRequestRequestDto.builder()
                .title("요청 문제 1")
                .description("요청 문제는 네트워크 입니다.")
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + CUSTOM_VALID_TOKEN)
                                .content(objectMapper.writeValueAsBytes(requestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        //then
        //verify()
    }

    @Test
    @DisplayName("특정 게시글 글 조회 Invalid ID")
    void getRequestInvalidWithId() throws Exception {
        //given
        Long id = 2L;

        LocalDateTime now = LocalDateTime.now();
        given(requestService.getRequest(id)).willThrow(NotFoundRequest.class);

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/request/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.NotFoundRequest.getErrorCode()))
                .andDo(MockMvcResultHandlers.print());
        //then
        verify(requestService).getRequest(anyLong());
    }

    @Test
    @DisplayName("관리자가 요청 게시글 수락")
    void updateRequestFlagWithValid() throws Exception {
        //given
        FlagRequestDto flagRequestDto = new FlagRequestDto(1L, true);
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/request/approve")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ADMIN_VALID_TOKEN)
                                .content(objectMapper.writeValueAsBytes(flagRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
        //then
        //verify()
    }

    @Test
    @DisplayName("관리자가 아닌 게시글 요청 수락")
    void updateRequestFlagWithInValid() throws Exception {
        //given
        FlagRequestDto flagRequestDto = new FlagRequestDto(1L, true);
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/request/approve")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + CUSTOM_VALID_TOKEN)
                                .content(objectMapper.writeValueAsBytes(flagRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(MockMvcResultHandlers.print());
        //then
        //verify()
    }

    @Test
    @DisplayName("내가 요청한 문제 조회")
    void getMemberRequestListWithValid() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        List<RequestResponseDto> mockQuestionList = new ArrayList<>();
        mockQuestionList.add(new RequestResponseDto(1L, true, "tcp", "설명", 1L, "김무건", now));
        mockQuestionList.add(new RequestResponseDto(1L, true, "udp", "설명", 1L, "김무건", now));
        mockQuestionList.add(new RequestResponseDto(1L, false, "객체지향", "설명", 1L, "김무건", now));
        mockQuestionList.add(new RequestResponseDto(1L, false, "스레드", "설명", 1L, "김무건", now));

        Pageable pageable = PageRequest.of(0, 10);

        Page<RequestResponseDto> mockPageResult = new PageImpl<>(mockQuestionList, pageable, mockQuestionList.size());

        given(requestService.getRequestList(1L, pageable)).willReturn(mockPageResult);
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/request/member")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
        verify(requestService).getRequestList(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("요청 게시글 전체 리스트 조회")
    void getRequestListWithValid() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        List<RequestResponseDto> mockQuestionList = new ArrayList<>();
        mockQuestionList.add(new RequestResponseDto(1L, true, "tcp", "설명", 1L, "김무건", now));
        mockQuestionList.add(new RequestResponseDto(1L, true, "udp", "설명", 1L, "김무건", now));
        mockQuestionList.add(new RequestResponseDto(1L, false, "객체지향", "설명", 1L, "김무건", now));
        mockQuestionList.add(new RequestResponseDto(1L, false, "스레드", "설명", 1L, "김무건", now));

        Pageable pageable = PageRequest.of(0, 10);

        Page<RequestResponseDto> mockPageResult = new PageImpl<>(mockQuestionList, pageable, mockQuestionList.size());

        given(requestService.getRequestList(pageable)).willReturn(mockPageResult);
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/request/requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
        verify(requestService).getRequestList(any(Pageable.class));
    }

    @Test
    @DisplayName("요청 게시글 수정")
    void updateRequestWithValid() throws Exception {
        //given
        UpdateRequestRequestDto updateRequestRequestDto = UpdateRequestRequestDto.builder()
                .id(1L)
                .title("제목")
                .description("설명")
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ADMIN_VALID_TOKEN)
                                .content(objectMapper.writeValueAsBytes(updateRequestRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
        //then
        //verify()
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteRequestId() throws Exception {
        //given
        Long id = 1L;
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/request/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content("")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
        //then
        //verify()
    }
}