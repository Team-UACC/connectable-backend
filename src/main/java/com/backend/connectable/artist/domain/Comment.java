package com.backend.connectable.artist.domain;

import com.backend.connectable.global.entity.BaseEntity;
import com.backend.connectable.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Lob
    private String contents;
}
