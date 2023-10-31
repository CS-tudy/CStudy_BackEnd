package com.cstudy.modulecommon.repository.member;


import com.cstudy.modulecommon.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Query(
        "select m from Member m " +
        "join fetch m.requests " +
        "where m.id = :id"
    )
    Member findMemberFetchRequest(@Param("id") Long id);

    @Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.questions")
    List<Member> findAllWithQuestions();

    @Query("SELECT m FROM Member m JOIN FETCH m.roles WHERE m.email = :email")
    Optional<Member> findByEmailWithRoles(@Param("email") String email);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select m from Member m where m.id = :memberId")
    Optional<Member> findByIdForUpdateOptimistic(@Param("memberId") Long memberId);

    Optional<Member> findByName(String value);

    @Query("select m from Member m join fetch m.file where m.id =:memberId")
    Optional<Member> findByMemberFetchFile(@Param("memberId") Long memberId);
}
