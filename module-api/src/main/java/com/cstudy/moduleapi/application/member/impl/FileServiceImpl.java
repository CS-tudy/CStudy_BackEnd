package com.cstudy.moduleapi.application.member.impl;

import com.cstudy.moduleapi.application.member.FileService;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cstudy.modulecommon.error.member.NotFoundMemberId;
import com.cstudy.modulecommon.util.LoginUserDto;
import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.repository.file.FileRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final AmazonS3Client amazonS3Client;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    public FileServiceImpl(
            AmazonS3Client amazonS3Client,
            FileRepository fileRepository,
            MemberRepository memberRepository
    ) {
        this.amazonS3Client = amazonS3Client;
        this.fileRepository = fileRepository;
        this.memberRepository = memberRepository;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * Upload single, multiple files to aws3 Enter file in key to enter as postman
     * @param multipartFileList 멀티파트 파일 List
     * @param loginUserDto 로그인 회원의 정보
     * @return imagePathList
     * @throws Exception
     */
    @Override
    @Transactional
    public List<String> uploadFiles(MultipartFile[] multipartFileList, LoginUserDto loginUserDto) throws Exception {

        Member member = memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));

        List<String> imagePathList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFileList) {
            String imagePath = uploadFile(multipartFile, member);
            imagePathList.add(imagePath);
        }

        return imagePathList;
    }

    /**
     *
     * @param loginUserDto 로그인 회원의 정보
     * @return s3에 업로드 파일의 url을 통하여 전송
     */
    @Transactional
    @Override
    public byte[] getImageBytes(LoginUserDto loginUserDto) {

        Member member = memberRepository.findById(loginUserDto.getMemberId())
                .orElseThrow(() -> new NotFoundMemberId(loginUserDto.getMemberId()));

        String fileName = member.getFile().getFileName();

        String imageUrl = "https://" + S3Bucket + ".s3."+region+".amazonaws.com/" + fileName;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl, byte[].class);
        return response.getBody();
    }

    private String uploadFile(MultipartFile multipartFile, Member member) throws Exception {
        String originalName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        File file = File.builder()
                .fileName(originalName)
                .member(member)
                .build();

        fileRepository.save(file);

        long size = multipartFile.getSize(); // 파일 크기

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(size);

        // S3에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근 가능한 URL 가져오기
    }
}
