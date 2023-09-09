package com.cstudy.moduleapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private T body;

}