package com.cstudy.modulecommon.repository.comment;


import com.cstudy.modulecommon.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
