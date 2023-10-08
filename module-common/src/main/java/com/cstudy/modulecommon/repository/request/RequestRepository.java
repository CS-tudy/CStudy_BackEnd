package com.cstudy.modulecommon.repository.request;

import com.cstudy.modulecommon.domain.request.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(
        "select r from Request r " +
        "where r.member.id = :id"
    )
    Page<Request> findRequestByMemberId(@Param("id") Long id,
                                        Pageable pageable);

    @Query("SELECT R FROM Request R WHERE R.flag= FALSE")
    List<Request> findByNotAcceptRequestList();

    Optional<Request> findByIdAndMemberId(long id, Long memberId);
}
