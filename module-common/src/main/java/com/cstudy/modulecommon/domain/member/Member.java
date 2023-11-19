package com.cstudy.modulecommon.domain.member;

import com.cstudy.modulecommon.domain.BaseEntity;
import com.cstudy.modulecommon.domain.competition.MemberCompetition;
import com.cstudy.modulecommon.domain.file.File;
import com.cstudy.modulecommon.domain.notice.Notice;
import com.cstudy.modulecommon.domain.question.MemberQuestion;
import com.cstudy.modulecommon.domain.request.Request;
import com.cstudy.modulecommon.domain.role.Role;
import com.cstudy.modulecommon.dto.ChoiceAnswerRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER", uniqueConstraints = {
        @UniqueConstraint(name = "MEMBER_EMAIL", columnNames = {"email"}),
//        @UniqueConstraint(name = "memberIpAddress", columnNames = {"memberIpAddress"}),
//        @UniqueConstraint(name = "name", columnNames = {"name"}),
})
public class Member extends BaseEntity{

    /********************************* PK 필드 *********************************/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /********************************* PK가 아닌 필드 *********************************/

    @Column(nullable = false, length = 100)
    private String email;

    private String password;

    @Column(nullable = false)
    private String name;

    private double rankingPoint = 0L;

    private String memberIpAddress;

    private String countryIsoCode;


    /********************************* 동시성 버전 *********************************/

    @Version
    private Long version;


    /********************************* 연관관계 매핑 *********************************/

//    @JsonManagedReference
    @OneToMany(
            mappedBy = "member",
            fetch = FetchType.LAZY
    )
    private List<File> file = new ArrayList<>();

//    @JsonManagedReference
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

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Notice> notices = new ArrayList<>();

    @OneToMany(
            mappedBy = "member",
            fetch = FetchType.LAZY
    )
    List<Request> requests = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "member_role",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();


    /********************************* 비니지스 로직 *********************************/

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

    public Member update(String name) {
        this.name = name;
        return this;
    }



    /********************************* 연관관계 편의 메서드 *********************************/

    public void addRequest(Request request) {
        this.requests.add(request);
    }
}
