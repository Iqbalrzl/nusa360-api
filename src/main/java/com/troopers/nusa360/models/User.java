package com.troopers.nusa360.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
//    private Profile profile;

}

