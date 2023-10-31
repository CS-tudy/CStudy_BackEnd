package com.cstudy.moduleapi.application.member.impl;

import com.cstudy.moduleapi.application.member.FileService;
import com.cstudy.moduleapi.config.s3.AwsS3Util;
import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.file.FileRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.cstudy.moduleapi.config.s3.ImageUtils.decompressImage;
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;
    private final AwsS3Util awsS3Util;

    public FileServiceImpl(FileRepository fileRepository, MemberRepository memberRepository, AwsS3Util awsS3Util) {
        this.fileRepository = fileRepository;
        this.memberRepository = memberRepository;
        this.awsS3Util = awsS3Util;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * Upload single, multiple files to aws3 Enter file in key to enter as postman
     *
     * @param multipartFile 멀티파트 파일 List
     * @param loginUserDto      로그인 회원의 정보
     */
    @Override
    @Transactional
    public void uploadFiles(MultipartFile multipartFile, LoginUserDto loginUserDto)  {
        File file = File.builder()
                .fileName(awsS3Util.uploadCompressedImage(multipartFile))
                .member(memberRepository.findById(loginUserDto.getMemberId())
                        .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId())))
                .build();
        fileRepository.save(file);
    }

    /**
     * @param loginUserDto 로그인 회원의 정보
     * @return s3에 업로드 파일의 url을 통하여 전송
     */
    @Transactional
    @Override
    public byte[] getImageBytes(LoginUserDto loginUserDto) {
        byte[] bytes = awsS3Util.downloadFile(fileRepository.findLatestFileNameByMemberId(loginUserDto.getMemberId()));
        return decompressImage(bytes);
    }

}