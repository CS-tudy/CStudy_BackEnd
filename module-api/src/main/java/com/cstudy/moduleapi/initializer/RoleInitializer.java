package com.cstudy.moduleapi.initializer;

import com.cstudy.modulecommon.domain.member.Member;
import com.cstudy.modulecommon.domain.question.Category;
import com.cstudy.modulecommon.domain.role.Role;
import com.cstudy.modulecommon.domain.role.RoleEnum;
import com.cstudy.modulecommon.repository.member.MemberRepository;
import com.cstudy.modulecommon.repository.question.CategoryRepository;
import com.cstudy.modulecommon.repository.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner initRoles(
            RoleRepository roleRepository,
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            CategoryRepository categoryRepository
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
        };
    }
}
