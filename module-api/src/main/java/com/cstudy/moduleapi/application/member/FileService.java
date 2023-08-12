package com.cstudy.moduleapi.application.member;

import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<String> uploadFiles(MultipartFile[] multipartFileList, LoginUserDto loginUserDto) throws Exception;

    byte[] getImageBytes(LoginUserDto loginUserDto);
}
