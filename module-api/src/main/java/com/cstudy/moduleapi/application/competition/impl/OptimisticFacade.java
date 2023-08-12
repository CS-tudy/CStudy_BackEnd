package com.cstudy.moduleapi.application.competition.impl;

import com.cstudy.moduleapi.application.competition.MemberCompetitionService;
import com.cstudy.modulecommon.util.LoginUserDto;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;

@Service
public class OptimisticFacade {
    private final MemberCompetitionService memberCompetitionService;

    public OptimisticFacade(MemberCompetitionService memberCompetitionService) {
        this.memberCompetitionService = memberCompetitionService;
    }

    public void joinCompetition(LoginUserDto loginUserDto, Long competitionId) throws InterruptedException {
        while (true) {
            try {
                memberCompetitionService.joinCompetition(loginUserDto, competitionId);
                break;
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException | CannotAcquireLockException e) {
                e.printStackTrace();
                Thread.sleep(50);
            }
        }
    }
}
