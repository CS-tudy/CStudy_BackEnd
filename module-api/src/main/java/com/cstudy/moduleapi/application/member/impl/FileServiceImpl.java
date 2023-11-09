package com.cstudy.moduleapi.application.member.impl;

import com.cstudy.moduleapi.application.member.FileService;
import com.cstudy.moduleapi.config.s3.AwsS3Util;
import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.repository.file.FileRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.Optional;

import static com.cstudy.moduleapi.config.s3.ImageUtils.decompressImage;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;
    private final AwsS3Util awsS3Util;

    public FileServiceImpl(
            FileRepository fileRepository,
            MemberRepository memberRepository,
            AwsS3Util awsS3Util
    ) {
        this.fileRepository = fileRepository;
        this.memberRepository = memberRepository;
        this.awsS3Util = awsS3Util;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * 파일을 업로드 한다. 이때 loginUserDto를 기반으로 데이터를 삽입한다.
     */
    @Override
    @Transactional
    public void uploadFiles(MultipartFile file, LoginUserDto loginUserDto) {
        String uploadFile = awsS3Util.uploadFile(file);
        Member member = memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));
        fileRepository.save(File.builder()
                .fileName(uploadFile)
                .member(member)
                .build());

    }

    /**
     * 회원의 이미지를 BYTE[]로 불러온다.
     * 현재 이 부분을 고민하자.
     * //TODO : 이 부분을 CLOUD-FONT로 처리를 해야되는지 아니면 BYTE[]로 처리를 해야되는지 고민하자
     */
    @Transactional
    @Override
    public byte[] getImageBytes(LoginUserDto loginUserDto) {
        byte[] bytes = awsS3Util.downloadFile(fileRepository.findLatestFileNameByMemberId(loginUserDto.getMemberId()));
        return decompressImage(bytes);
    }

    @Override
    @Transactional(readOnly = true)
    public String getMemberImagePath(LoginUserDto loginUserDto) {
        Member member = memberRepository.findByMemberFetchFile(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));
        return member.getFile().stream().max(Comparator.comparing(File::getCreatedDate)).map(File::getFileName).orElse("testtest");
    }

}