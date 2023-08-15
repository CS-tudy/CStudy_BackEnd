package com.cstudy.moduleapi.domain.notice.controller;

import com.cstudy.moduleapi.application.notice.NoticeService;
import com.cstudy.moduleapi.config.jwt.util.JwtTokenizer;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class NoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @MockBean
    private NoticeService noticeService;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtTokenizer.createAccessToken(1L, MemberTestEnum.VALID_EMAIL.getMessage(), List.of(RoleEnum.CUSTOM.getRoleName()));
    }

    @Test
    @DisplayName("공지사항 생성")
    void saveNoticeWithValid() throws Exception {
        //given
        NoticeSaveRequestDto noticeSaveRequestDto = NoticeSaveRequestDto.builder()
                .title("공지사항 제목")
                .content("공지사항 내용")
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/notice")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(noticeSaveRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("공지사항 생성 - Request Dto Title Empty")
    void saveNoticeWithInvalidCauseEmptyTitle() throws Exception {
        //given
        NoticeSaveRequestDto noticeSaveRequestDto = NoticeSaveRequestDto.builder()
                .content("공지사항 내용")
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/notice")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(noticeSaveRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("공지사항 생성 - Request Dto Content Empty")
    void saveNoticeWithInvalidCauseEmptyContent() throws Exception {
        //given
        NoticeSaveRequestDto noticeSaveRequestDto = NoticeSaveRequestDto.builder()
                .title("공지사항 제목")
                .build();
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/notice")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(noticeSaveRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("공지사항 수정")
    void updateNoticeWithValid() throws Exception {
        //given
        Long id = 1L;

        NoticeUpdateRequestDto noticeUpdateRequestDto = NoticeUpdateRequestDto.builder()
                .title("변경된 제목")
                .content("변경된 내용")
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/notice/{noticeId}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsBytes(noticeUpdateRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("공지사항 삭제")
    public void deleteNoticeWithValid() throws Exception{
        //given
        Long id = 1L;

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/notice/{noticeId}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
        //then
        //verify()
    }
}