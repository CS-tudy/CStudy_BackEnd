package com.cstudy.moduleapi.application.member;

import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.web.multipart.MultipartFile;


public interface FileService {
   void uploadFiles(MultipartFile file, LoginUserDto loginUserDto);

    byte[] getImageBytes(LoginUserDto loginUserDto);

    String getMemberImagePath(LoginUserDto loginUserDto);
}
