package com.team.creer_back.entity.member;

import com.team.creer_back.constant.Authority;
import com.team.creer_back.dto.member.MemberDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "phoneNum", nullable = false)
    private String phoneNum;
    @Column(name = "nickName", nullable = false)
    private String nickName;
    @Column(unique = true)
    private String email;
    @Lob
    private String image;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String name, String password, String email, String image, String address, String phoneNum, String nickName, Authority authority) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.image = image;
        this.address =address;
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.authority = authority;
    }
    public MemberDto toDto() {
        MemberDto memberDto = new MemberDto();
        memberDto.setImage(this.getImage());
        memberDto.setNickName(this.getNickName());
        memberDto.setAddress(this.getAddress());
        // 나머지 MemberDto의 필드를 채우는 작업을 수행합니다.
        return memberDto;
    }
    // getAuthorities() 를 호출 시, ROLE_USER 권한을 가진 GrantedAuthority의 리스트를 반환된다.
    // 이 경우, 모든 사용자에게 동일한 ROLE_USER 권한이 부여된다.
    // 사용자별로 다른 권한을 부여하기 위해서는 추가 작업이 필요하다.
    // ? extends GrantedAuthority : GrantedAuthority를 상속받은 어떤 클래스든 될 수 있는 객체의 컬렉션
    public Collection<? extends GrantedAuthority> getAuthority() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        // .asList() : 고정된 크기의 변경 가능한 리스트를 반환, 리스트의 요소를 덮어쓰기는 가능하나, 새로운 요소를 추가하거나, 기존 요소를 삭제할 수는 없다.
        // List.of() : 진정한 불변 리스트를 반환, 리스트의 크기 변경 및 요소 덮어쓰기 전부 불가능, 즉 모든 요소가 초기화 이후에 변경되지 않아야 하는 경우에 적합
        // List.of() 로 반환된 리스트를 변경하려고 하면 UnsupportedOperationException 가 발생한다.
    }
}