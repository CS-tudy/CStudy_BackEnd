package com.cstudy.moduleapi.application.notice.controller;

import com.cstudy.moduleapi.config.ControllerTestBase;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


class NoticeControllerTest extends ControllerTestBase {


    private String token;
    private static String ADMIN_VALID_TOKEN;

    @BeforeEach
    void setUp() {
        ADMIN_VALID_TOKEN = jwtTokenizer.createAccessToken(1L, MemberTestEnum.ADMIN_EMAIL.getMessage(), List.of(RoleEnum.ADMIN.getRoleName()));
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
                                .header("Authorization", "Bearer " + ADMIN_VALID_TOKEN)
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
                                .header("Authorization", "Bearer " + ADMIN_VALID_TOKEN)
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
                                .header("Authorization", "Bearer " + ADMIN_VALID_TOKEN)
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
                        MockMvcRequestBuilders.patch("/api/notice/{noticeId}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ADMIN_VALID_TOKEN)
                                .content(objectMapper.writeValueAsBytes(noticeUpdateRequestDto))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
        //then
        //verify()
    }

    @Test
    @DisplayName("공지사항 삭제")
    public void deleteNoticeWithValid() throws Exception {
        //given
        Long id = 1L;

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/notice/{noticeId}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + ADMIN_VALID_TOKEN)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
        //then
        //verify()
    }
}