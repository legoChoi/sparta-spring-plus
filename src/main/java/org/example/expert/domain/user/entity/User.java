package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.security.entity.CustomUserDetails;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users",
        indexes = { @Index(name = "idx_nickname", columnList = "nickname") }
)
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String email, String password, String nickname, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public User(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    @Builder
    private User(Long id, String email, String nickname, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public static User fromJwt(Long id, String email, String role) {
        return new User(id, email, UserRole.of(role));
    }

    public static User fromUserDetails(CustomUserDetails customUserDetails) {
        return new User(customUserDetails.getId(), customUserDetails.getEmail(), customUserDetails.getUsername(), customUserDetails.getUserRole());
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
