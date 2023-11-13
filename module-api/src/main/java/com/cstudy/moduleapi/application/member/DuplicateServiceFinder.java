package com.cstudy.moduleapi.application.member;

import com.cstudy.moduleapi.dto.member.DuplicateResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DuplicateServiceFinder {

    private final List<DuplicateService>duplicateServices;

    public DuplicateServiceFinder(List<DuplicateService> duplicateServices) {
        this.duplicateServices = duplicateServices;
    }

    public DuplicateResponseDto getVerifyResponseDto(String type, String value) {
        log.info("type : {}", type);
        log.info("value : {}", value);
        DuplicateService duplicateService = duplicateServices.stream()
                .filter(verifyService -> verifyService.getType().name().equals(type))
                .findAny()
                .orElseThrow(RuntimeException::new);

        return duplicateService.signupDivisionDuplicateCheck(type, value);
    }
}
