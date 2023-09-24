package com.cstudy.moduleapi.controller.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.moduleapi.dto.comment.NoticeCommentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.cstudy.moduleapi.config.ControllerTest.ADMIN_USER;

public class CommentMockApiCaller extends MockApiCaller {

    public CommentMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<String> postNewNoticeComment(NoticeCommentRequestDto request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<List<NoticeCommentResponse>> getCommentsForNotice(NoticeCommentRequestDto request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        List<NoticeCommentResponse> commentList = new ArrayList<>();
//        commentList.add()

        return new ApiResponse<>(response.getStatus(),commentList);
    }

}
