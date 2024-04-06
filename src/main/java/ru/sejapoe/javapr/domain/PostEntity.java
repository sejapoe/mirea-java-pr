package ru.sejapoe.javapr.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class PostEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posts_id_seq")
    @SequenceGenerator(name = "posts_id_seq", sequenceName = "posts_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "creation_date", nullable = false, columnDefinition = "timestamp", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant creationDate;


    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = true)
    private UserEntity author;
}
