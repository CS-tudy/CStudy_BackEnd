package com.cstudy.moduleapi.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.comment.NoticeCommentResponse;
import com.cstudy.moduleapi.dto.request.RequestResponseDto;
import com.cstudy.moduleapi.dto.review.ReviewUserResponseDto;
import com.cstudy.moduleapi.dto.workbook.WorkbookIdWithImagePath;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.cstudy.modulecommon.dto.WorkbookQuestionResponseDto;
import com.cstudy.modulecommon.dto.WorkbookResponseDto;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cstudy.moduleapi.config.ControllerTest.ADMIN_USER;
import static com.cstudy.moduleapi.config.ControllerTest.CUSTOM_USER;

public class WorkbookMockApiCaller extends MockApiCaller {

    public WorkbookMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }


    public ApiResponse<Page<WorkbookResponseDto>> getWorkbookList(String url) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        String jsonResponse = response.getContentAsString(StandardCharsets.UTF_8);
        objectMapper.registerModule(new JavaTimeModule());

        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        List<WorkbookResponseDto> content = objectMapper.readValue(jsonNode.get("content").toString(), new TypeReference<>() {});
        int totalPages = jsonNode.get("totalPages").asInt();
        long totalElements = jsonNode.get("totalElements").asLong();

        Page<WorkbookResponseDto> page = new PageImpl<>(content, PageRequest.of(0, content.size()), totalElements);

        return ApiResponse.success(response.getStatus(), page);
    }

    public ApiResponse<WorkbookResponseDto> getWorkbookById(Long path) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/workbook/{path}", path)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        objectMapper.registerModule(new JavaTimeModule());
        WorkbookResponseDto workbookResponseDto = objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), WorkbookResponseDto.class);

        return ApiResponse.success(response.getStatus(), workbookResponseDto);
    }

    public ApiResponse<String> uploadFile(MockMultipartFile request, long id) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/workbook/upload/{id}", id)
                .file(request)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }


    public ApiResponse<String> uploadFileInvalid(MockMultipartFile request, long id) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/workbook/upload/{id}", id)
                .file(request)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();



        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }



    public ApiResponse<List<WorkbookIdWithImagePath>> getWorkbookImageList() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/workbook/images")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        String jsonResponse = response.getContentAsString(StandardCharsets.UTF_8);

        List<WorkbookIdWithImagePath> workbookList = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        return new ApiResponse<>(response.getStatus(), workbookList);


    }


    public ApiResponse<Page<WorkbookQuestionResponseDto>> getQuestions(Long path) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/workbook/{path}/questions",path)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        String jsonResponse = response.getContentAsString(StandardCharsets.UTF_8);
        objectMapper.registerModule(new JavaTimeModule());

        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        List<WorkbookQuestionResponseDto> content = objectMapper.readValue(jsonNode.get("content").toString(), new TypeReference<>() {});
        long totalElements = jsonNode.get("totalElements").asLong();

        Page<WorkbookQuestionResponseDto> page = new PageImpl<>(content, PageRequest.of(0, content.size()), totalElements);

        return ApiResponse.success(response.getStatus(), page);
    }

}
