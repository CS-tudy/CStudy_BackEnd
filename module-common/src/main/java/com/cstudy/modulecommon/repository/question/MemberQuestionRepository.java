package com.cstudy.modulecommon.repository.question;

import com.cstudy.modulecommon.domain.question.MemberQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberQuestionRepository extends JpaRepository<MemberQuestion, Long> {



    @Query("SELECT CASE WHEN COUNT(mq) > 0 THEN true ELSE false END " +
            "FROM MemberQuestion mq " +
            "WHERE mq.member.id = :memberId " +
            "AND mq.question.id = :questionId " +
            "AND mq.success = :choiceNumber")
    boolean existsByMemberAndQuestionAndSuccess(@Param("memberId") Long memberId,
                                                @Param("questionId") Long questionId,
                                                @Param("choiceNumber") int choiceNumber);

    @Query("SELECT CASE WHEN COUNT(mq) > 0 THEN true ELSE false END " +
            "FROM MemberQuestion mq " +
            "WHERE mq.member.id = :memberId " +
            "AND mq.question.id = :questionId " +
            "AND mq.fail = :choiceNumber")
    boolean existsByMemberAndQuestionAndFail(@Param("memberId") Long memberId,
                                                @Param("questionId") Long questionId,
                                                @Param("choiceNumber") int choiceNumber);

    @Query("SELECT COUNT(MQ) FROM MemberQuestion MQ " +
            "WHERE MQ.member.id = :memberId " +
            "AND MQ.question.id = :questionId " +
            "AND MQ.success = 0")
    long countByMemberIdAndQuestionIdAndSuccessZero(@Param("memberId") Long memberId,
                                                    @Param("questionId") Long questionId);

    @Query("SELECT COUNT(MQ) FROM MemberQuestion MQ " +
            "WHERE MQ.member.id = :memberId " +
            "AND MQ.question.id = :questionId " +
            "AND MQ.fail = 0")
    long countByMemberIdAndQuestionIdAndFailZero(@Param("memberId") Long memberId,
                                                    @Param("questionId") Long questionId);

//    @Query("SELECT CASE WHEN EXISTS (" +
//            "SELECT 1 FROM MemberQuestion MQ " +
//            "WHERE MQ.member.id = :memberId " +
//            "AND MQ.question.id = :questionId " +
//            "AND MQ.success = 0) " +
//            "THEN true ELSE false END")
//    boolean existsByMemberIdAndQuestionIdAndSuccessZero(@Param("memberId") Long memberId,
//                                                        @Param("questionId") Long questionId);
//
//
//    @Query("SELECT CASE WHEN EXISTS (" +
//            "SELECT 1 FROM MemberQuestion MQ " +
//            "WHERE MQ.member.id = :memberId " +
//            "AND MQ.question.id = :questionId " +
//            "AND MQ.fail = 0) " +
//            "THEN true ELSE false END")
//    boolean existsByMemberIdAndQuestionIdAndFailZero(@Param("memberId") Long memberId,
//                                                     @Param("questionId") Long questionId);


    @Query("SELECT MQ FROM MemberQuestion MQ " +
            "WHERE MQ.member.id = :memberId " +
            "AND MQ.question.id = :questionId")
    Optional<MemberQuestion> findByQuestionAboutMemberIdAndQuestionId(@Param("memberId") Long memberId, @Param("questionId") Long questionId);

}
