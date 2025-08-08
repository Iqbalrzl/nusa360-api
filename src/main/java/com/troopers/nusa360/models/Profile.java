package com.troopers.nusa360.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "avatar_url")
    private String avatar_url;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private User user;
}
