package com.backend.connectable.artist.domain;

import com.backend.connectable.global.entity.BaseEntity;
import com.backend.connectable.user.domain.User;
import java.util.Objects;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Lob private String contents;

    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Comment(Long id, Artist artist, User user, String contents, boolean isDeleted) {
        this.id = id;
        this.artist = artist;
        this.user = user;
        this.contents = contents;
        this.isDeleted = isDeleted;
    }

    public boolean isCommentAuthor(User user) {
        return this.user.equals(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
