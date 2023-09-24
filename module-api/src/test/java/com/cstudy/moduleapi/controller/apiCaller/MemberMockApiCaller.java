package com.cstudy.moduleapi.controller.apiCaller;

import com.cstudy.moduleapi.ApiResponse;
import com.cstudy.moduleapi.config.MockApiCaller;
import com.cstudy.moduleapi.dto.member.*;
import com.cstudy.moduleapi.dto.refresh.RefreshTokenDto;
import com.cstudy.moduleapi.enums.MemberTestEnum;
import com.cstudy.moduleapi.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static com.cstudy.moduleapi.config.ControllerTest.ADMIN_USER;
import static com.cstudy.moduleapi.config.ControllerTest.CUSTOM_USER;

public class MemberMockApiCaller extends MockApiCaller {

    public MemberMockApiCaller(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<MemberLoginResponse> login() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/member/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(MemberLoginRequest.builder()
                        .email(MemberTestEnum.VALID_EMAIL.getMessage())
                        .password(MemberTestEnum.VALID_PASSWORD.getMessage())
                        .build()));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .accessToken(JsonPath.read(response.getContentAsString(), "$.accessToken"))
                .refreshToken(JsonPath.read(response.getContentAsString(), "$.refreshToken"))
                .name(JsonPath.read(response.getContentAsString(), "$.name"))
                .email(JsonPath.read(response.getContentAsString(), "$.email"))
                .build();

        return new ApiResponse<>(response.getStatus(), memberLoginResponse);
    }


    public ApiResponse<String> signup(MemberSignupRequest request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<String> logout(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<MemberLoginResponse> login(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .name(MemberTestEnum.CUSTOM_NAME.getMessage())
                .email(MemberTestEnum.VALID_EMAIL.getMessage())
                .build();

        return ApiResponse.success(response.getStatus(), memberLoginResponse);
    }

    public ApiResponse<ErrorResponse> deleteFail(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.code"))
                .message(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.message"))
                .validation(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.validation"))
                .build();

        return new ApiResponse<>(response.getStatus(), errorResponse);
    }

    public ApiResponse<ErrorResponse> emptyTokenlogout(String url, Object request) throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(JsonPath.read(mockMvc.perform(builder)
                        .andReturn()
                        .getResponse().getContentAsString(StandardCharsets.UTF_8), "$.code"))
                .message(JsonPath.read(mockMvc.perform(builder)
                        .andReturn()
                        .getResponse().getContentAsString(StandardCharsets.UTF_8), "$.message"))
                .build();

        return new ApiResponse<>(mockMvc.perform(builder)
                .andReturn()
                .getResponse().getStatus(), errorResponse);
    }



    public ApiResponse<String> checkEmailDuplication(String email) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/email")
                .param("email", email)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<String> checkNameDuplication(String name) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/name")
                .param("name", name)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }


    public ApiResponse<MemberLoginResponse> refreshTokenWithAccessToken(RefreshTokenDto request) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/member/refreshToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        MemberLoginResponse memberLoginResponse = MemberLoginResponse.builder()
                .accessToken(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.accessToken"))
                .refreshToken(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.refreshToken"))
                .name(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.name"))
                .email(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.email"))
                .build();

        return new ApiResponse<>(response.getStatus(), memberLoginResponse);
    }


    public ApiResponse<byte[]> downloadFile() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/member/download")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();


        return new ApiResponse<>(response.getStatus(),response.getContentAsByteArray());
    }

    public ApiResponse<String> uploadFiles(MockMultipartFile request) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/member/upload")
                .file(request)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + CUSTOM_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }

    public ApiResponse<MyPageResponseDto> getMyPage() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/member/member")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        // JSON 응답에서 필드 값을 읽어올 때 데이터 형식 확인 후 적절한 캐스팅 수행
        Object idObject = JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.id");
        Long id = (idObject instanceof Number) ? ((Number) idObject).longValue() : null;

        // MyPageResponseDto 객체 생성
        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                .id(id)
                .name(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.name"))
                .email(JsonPath.read(response.getContentAsString(StandardCharsets.UTF_8), "$.email"))
                .build();

        return new ApiResponse<>(response.getStatus(), myPageResponseDto);
    }


    public ApiResponse<String> changePasword(MemberPasswordChangeRequest request) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/api/member/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + ADMIN_USER);

        MockHttpServletResponse response = mockMvc.perform(builder)
                .andReturn()
                .getResponse();

        return new ApiResponse<>(response.getStatus(), response.getContentAsString(StandardCharsets.UTF_8));
    }
}
