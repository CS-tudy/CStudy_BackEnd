package com.cstudy.moduleapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class CustomRequestBuilder extends MockApiCaller{

    private MockHttpServletRequestBuilder builder;

    public CustomRequestBuilder(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public CustomRequestBuilder post(String url) {
        builder = MockMvcRequestBuilders.post(url);
        return this;
    }

    public CustomRequestBuilder contentType(MediaType mediaType) {
        builder.contentType(mediaType.toString());
        return this;
    }

    public CustomRequestBuilder delete(String url) {
        builder = MockMvcRequestBuilders.delete(url);
        return this;
    }

    public CustomRequestBuilder get(String url) {
        builder = MockMvcRequestBuilders.get(url);
        return this;
    }
    public CustomRequestBuilder put(String url) {
        builder = MockMvcRequestBuilders.get(url);
        return this;
    }

    public CustomRequestBuilder content(Object content) throws Exception {
        String jsonContent = objectMapper.writeValueAsString(content);
        builder.content(jsonContent);
        return this;
    }

    public CustomRequestBuilder header(String name, String value) {
        builder.header(name, value);
        return this;
    }

    public MockHttpServletResponse perform() throws Exception {
        return mockMvc.perform(builder)
                .andReturn()
                .getResponse();
    }
}
