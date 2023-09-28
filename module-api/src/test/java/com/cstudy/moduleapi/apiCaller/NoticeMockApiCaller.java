package com.cstudy.moduleapi.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.member.MemberLoginResponse;
import com.cstudy.moduleapi.dto.notice.NoticeSaveRequestDto;
import com.cstudy.modulecommon.dto.NoticeResponseDto;
import com.cstudy.modulecommon.dto.NoticeSearchRequestDto;
import com.cstudy.modulecommon.dto.NoticeUpdateRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.cstudy.moduleapi.config.ControllerTest.ADMIN_USER;

public class NoticeMockApiCaller extends MockApiCaller {

    public NoticeMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<Page<NoticeResponseDto>> findNoticeWithPage(String url, NoticeSearchRequestDto request) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        String jsonResponse = response.getContentAsString(StandardCharsets.UTF_8);


        objectMapper.registerModule(new JavaTimeModule());

        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        List<NoticeResponseDto> content = objectMapper.readValue(jsonNode.get("content").toString(), new TypeReference<>() {});
        int totalPages = jsonNode.get("totalPages").asInt();
        long totalElements = jsonNode.get("totalElements").asLong();

        Page<NoticeResponseDto> noticePage = new PageImpl<>(content, PageRequest.of(0, content.size()), totalElements);

        return ApiResponse.success(response.getStatus(), noticePage);
    }

    public ApiResponse<NoticeResponseDto> findByNoticeId(String url, Long noticeId) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url,noticeId)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        NoticeResponseDto noticeResponseDto = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), NoticeResponseDto.class);

        return ApiResponse.success(response.getStatus(), noticeResponseDto);
    }

    public ApiResponse<String> saveNotice(NoticeSaveRequestDto requestDto) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/notice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<String> updateNotice(NoticeUpdateRequestDto requestDto, Long noticeId) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/api/notice/{noticeId}",noticeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<String> deleteNotice(Long noticeId) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/api/notice/{noticeId}",noticeId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }
}
