package com.cstudy.moduleapi.controller.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.comment.NoticeCommentRequestDto;
import com.cstudy.moduleapi.dto.comment.NoticeCommentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    public ApiResponse<List<NoticeCommentResponse>> getCommentsForNotice(Long noticeId) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/comment/{noticeId}", noticeId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        int responseStatus = response.getStatus();
        List<NoticeCommentResponse> commentList = new ArrayList<>();

        String responseBody = response.getContentAsString(StandardCharsets.UTF_8);
        List<Map<String, Object>> comments = JsonPath.read(responseBody, "$"); // JSONPath를 사용하여 댓글 데이터 추출

        for (Map<String, Object> commentMap : comments) {
            Integer idAsInteger = (Integer) commentMap.get("id");
            Integer memberIdAsInteger = (Integer) commentMap.get("memberId");
            Integer parentCommentIdAsInteger = (Integer) commentMap.get("parentCommentId");

            NoticeCommentResponse commentResponse = NoticeCommentResponse.builder()
                    .id(idAsInteger != null ? idAsInteger.longValue() : null)
                    .content((String) commentMap.get("content"))
                    .memberId(memberIdAsInteger != null ? memberIdAsInteger.longValue() : null)
                    .author((String) commentMap.get("author"))
                    .parentCommentId(parentCommentIdAsInteger != null ? parentCommentIdAsInteger.longValue() : null)
                    .build();
            commentList.add(commentResponse);
        }


        return new ApiResponse<>(responseStatus, commentList);
    }


}
