package com.cstudy.modulecommon.repository.member;

import com.cstudy.modulecommon.config.QueryDslConfig;
import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.request.Request;
import com.cstudy.modulecommon.error.member.NotFoundMemberEmail;
import com.cstudy.modulecommon.repository.request.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
public class MemberRepositoryTest {

    final String email = "admin@admin.com";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Nested
    class findByEmail {

        @BeforeEach
        void setUp() {
            memberRepository.save(Member.builder()
                    .name("김무건")
                    .email(email)
                    .password("1234")
                    .build());
        }

        @Test
        public void findByEmail() throws Exception {
            //given
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundMemberEmail(email));
            //when

            //Then
            assertThat(member.getEmail()).isEqualTo(email);
        }
    }

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(Member.builder()
                .name("김무건")
                .email(email)
                .password("1234")
                .build());


        requestRepository.save(Request.builder()
                .title("제목")
                .description("설명")
                .member(member)
                .build());
    }

    @Test
    public void findMemberFetchRequest() throws Exception{
        //given

        //when

        //Then
        //assertThat().isEqualTo();
    }


}
