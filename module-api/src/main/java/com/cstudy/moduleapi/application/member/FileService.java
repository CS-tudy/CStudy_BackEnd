package com.cstudy.moduleapi.application.member;

import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
   void uploadFiles(MultipartFile multipartFileList, LoginUserDto loginUserDto);

    byte[] getImageBytes(LoginUserDto loginUserDto);
}
