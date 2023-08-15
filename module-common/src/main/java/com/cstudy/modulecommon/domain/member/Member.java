package com.cstudy.modulecommon.domain.member;

import com.cstudy.modulecommon.domain.BaseEntity;
import com.cstudy.modulecommon.domain.competition.MemberCompetition;
import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.request.Request;
import com.cstudy.modulecommon.domain.role.Role;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_email")
    private String email;

    private String password;

    @Column(name = "member_name")
    private String name;

    private double rankingPoint = 0L;

    @OneToOne(mappedBy = "member")
    private File file;

    @Version
    private Long version;

    @OneToMany(
            mappedBy = "member",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    Set<MemberQuestion> questions = new HashSet<>();

    @OneToMany(
            mappedBy = "member",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    List<MemberCompetition> memberCompetitions = new ArrayList<>();


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(
            mappedBy = "member",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    List<Request> requests = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "member_role",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    public void changePassword(String password) {
        this.password = password;
    }

    public void changeRole(Role role) {
        roles.add(role);
    }

    public void addRankingPoint(ChoiceAnswerRequestDto choiceAnswerRequestDto) {
        rankingPoint += 3L + (1 - (choiceAnswerRequestDto.getTime() / 1000.0));
    }

    public void minusRankingPoint(double choiceAnswerRequestDto) {
        rankingPoint -= 2L;
    }

    @Builder
    public Member(String email, String password, String name, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles = roles;
    }

    public void addRequest(Request request) {
        this.requests.add(request);
    }
}
