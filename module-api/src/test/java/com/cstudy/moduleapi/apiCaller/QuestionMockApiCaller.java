package com.cstudy.moduleapi.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.question.CreateQuestionAndCategoryRequestDto;
import com.cstudy.moduleapi.dto.question.QuestionAnswerDto;
import com.cstudy.moduleapi.dto.question.QuestionResponseDto;
import com.cstudy.modulecommon.dto.ChoiceQuestionResponseDto;
import com.cstudy.modulecommon.dto.QuestionPageWithCategoryAndTitle;
import com.cstudy.modulecommon.dto.QuestionSearchCondition;
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
import static com.cstudy.moduleapi.config.ControllerTest.CUSTOM_USER;

public class QuestionMockApiCaller extends MockApiCaller {

    public QuestionMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }


    public ApiResponse<String> createQuestionChoice(CreateQuestionAndCategoryRequestDto requestDto) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<String> bulkCreateQuestionChoice(List<CreateQuestionAndCategoryRequestDto> createQuestionAndCategoryRequestDtos) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/questions/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createQuestionAndCategoryRequestDtos))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return ApiResponse.success(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }


    public ApiResponse<QuestionResponseDto> findQuestionPathId(Long questionId) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/questions/{questionId}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        String responseBody = response.getContentAsString(StandardCharsets.UTF_8);

        List<ChoiceQuestionResponseDto> choices = objectMapper.convertValue(
                JsonPath.parse(responseBody).read("$.choices[*]"), new TypeReference<>() {
                });


        QuestionResponseDto responseDto = QuestionResponseDto.builder()
                .title(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.title"))
                .categoryTitle(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.categoryTitle"))
                .description(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.description"))
                .explain(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.explain"))
                .choices(choices)
                .build();


        return ApiResponse.success(response.getStatus(), responseDto);
    }

    public ApiResponse<QuestionAnswerDto> choiceQuestion(Long questionId, Object responseDto) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/questions/{questionId}/answer", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responseDto))
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        QuestionAnswerDto questionAnswerDto = QuestionAnswerDto.builder()
                .answer(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.answer"))
                .build();


        return ApiResponse.success(response.getStatus(), questionAnswerDto);
    }

    public ApiResponse<Page<QuestionPageWithCategoryAndTitle>> questionPageWithCategory(String url ,QuestionSearchCondition request) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        String jsonResponse = response.getContentAsString(StandardCharsets.UTF_8);


        objectMapper.registerModule(new JavaTimeModule());

        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        List<QuestionPageWithCategoryAndTitle> content = objectMapper.readValue(jsonNode.get("content").toString(), new TypeReference<>() {
        });
        int totalPages = jsonNode.get("totalPages").asInt();
        long totalElements = jsonNode.get("totalElements").asLong();

        Page<QuestionPageWithCategoryAndTitle> page = new PageImpl<>(content, PageRequest.of(0, content.size()), totalElements);

        return ApiResponse.success(response.getStatus(), page);
    }



    public ApiResponse<Page<QuestionPageWithCategoryAndTitle>> findMyQuestion(String url ,QuestionSearchCondition request) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        String jsonResponse = response.getContentAsString(StandardCharsets.UTF_8);


        objectMapper.registerModule(new JavaTimeModule());

        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        List<QuestionPageWithCategoryAndTitle> content = objectMapper.readValue(jsonNode.get("content").toString(), new TypeReference<>() {});
        int totalPages = jsonNode.get("totalPages").asInt();
        long totalElements = jsonNode.get("totalElements").asLong();

        Page<QuestionPageWithCategoryAndTitle> page = new PageImpl<>(content, PageRequest.of(0, content.size()), totalElements);

        return ApiResponse.success(response.getStatus(), page);
    }
}
