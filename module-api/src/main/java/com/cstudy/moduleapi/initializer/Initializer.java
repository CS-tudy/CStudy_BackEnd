package com.cstudy.moduleapi.initializer;

import com.cstudy.modulecommon.domain.choice.Choice;
import com.cstudy.modulecommon.domain.comment.Comment;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.domain.question.Category;
import com.cstudy.modulecommon.domain.question.Question;
import com.cstudy.modulecommon.domain.request.Request;
import com.cstudy.modulecommon.domain.role.Role;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.repository.comment.CommentRepository;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.notice.NoticeRepository;
import com.cstudy.modulecommon.repository.question.CategoryRepository;
import com.cstudy.modulecommon.repository.question.QuestionRepository;
import com.cstudy.modulecommon.repository.request.RequestRepository;
import com.cstudy.modulecommon.repository.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Configuration
public class Initializer {

    @Bean
    public CommandLineRunner initRoles(
            RoleRepository roleRepository,
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            CategoryRepository categoryRepository,
            QuestionRepository questionRepository,
            NoticeRepository noticeRepository,
            RequestRepository requestRepository,
            CommentRepository commentRepository

    ) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role custom = Role.builder()
                        .roleId(1L)
                        .name(RoleEnum.CUSTOM.getRoleName())
                        .build();

                Role admin = Role.builder()
                        .roleId(2L)
                        .name(RoleEnum.ADMIN.getRoleName())
                        .build();



                roleRepository.save(custom);
                roleRepository.save(admin);
            }

            if (memberRepository.count() == 0) {

                Optional<Role> adminRoleOptional = roleRepository.findByName(RoleEnum.ADMIN.getRoleName());
                Optional<Role> customRoleOptional = roleRepository.findByName(RoleEnum.CUSTOM.getRoleName());

                if (adminRoleOptional.isPresent()) {
                    Role adminRole = adminRoleOptional.get();

                    Member member = Member.builder()
                            .email("admin@admin.com")
                            .password(passwordEncoder.encode("admin1234!"))
                            .name("관리자")
                            .roles(Collections.singleton(adminRole))
                            .build();

                    memberRepository.save(member);
                }

                if (customRoleOptional.isPresent()) {
                    Role adminRole = adminRoleOptional.get();

                    Member member = Member.builder()
                            .email("test@test.com")
                            .password(passwordEncoder.encode("test1234!"))
                            .name("일반회원")
                            .roles(Collections.singleton(adminRole))
                            .build();

                    memberRepository.save(member);
                }
            }

            if (categoryRepository.count() == 0) {

                Category Java = Category.builder()
                        .categoryTitle("자바")
                        .build();

                Category Network = Category.builder()
                        .categoryTitle("네트워크")
                        .build();

                Category OS = Category.builder()
                        .categoryTitle("운영체제")
                        .build();

                Category DB = Category.builder()
                        .categoryTitle("데이터베이스")
                        .build();

                categoryRepository.save(Java);
                categoryRepository.save(Network);
                categoryRepository.save(OS);
                categoryRepository.save(DB);
            }

            Member member = memberRepository.findById(1L)
                    .orElseThrow();
            Member custom = memberRepository.findById(2L)
                    .orElseThrow();

            if (noticeRepository.count() == 0) {
                for (int i = 0; i < 20; i++) {
                    Notice notice=Notice.builder()
                            .title("제목"+i)
                            .content("내용"+i)
                            .member(member)
                            .build();
                    noticeRepository.save(notice);
                }
            }

            if (requestRepository.count() == 0) {
                for (int i = 0; i < 20; i++) {
                    Request request = Request.builder()
                            .title("제목"+i)
                            .description("설명"+i)
                            .member(custom)
                            .build();

                    requestRepository.save(request);
                }
            }

            Notice notice = noticeRepository.findById(1L)
                    .orElseThrow();
            if (commentRepository.count() == 0) {
                Comment comment = Comment.builder()
                        .content("1번댓글")
                        .notice(notice)
                        .member(custom)
                        .build();

                commentRepository.save(comment);
            }

            if (commentRepository.count() == 1) {
                Comment child1 = Comment.builder()
                        .content("2번댓글")
                        .parentCommentId(1L)
                        .notice(notice)
                        .member(member)
                        .build();

                Comment child2 = Comment.builder()
                        .content("3번댓글")
                        .parentCommentId(1L)
                        .notice(notice)
                        .member(member)
                        .build();

                Comment child3 = Comment.builder()
                        .content("4번댓글")
                        .parentCommentId(1L)
                        .notice(notice)
                        .member(member)
                        .build();

                Comment child4 = Comment.builder()
                        .content("5번댓글")
                        .parentCommentId(3L)
                        .notice(notice)
                        .member(custom)
                        .build();

                Comment child5 = Comment.builder()
                        .content("6번댓글")
                        .parentCommentId(3L)
                        .notice(notice)
                        .member(custom)
                        .build();
                commentRepository.save(child1);
                commentRepository.save(child2);
                commentRepository.save(child3);
                commentRepository.save(child4);
                commentRepository.save(child5);
            }
        };
    }
}
