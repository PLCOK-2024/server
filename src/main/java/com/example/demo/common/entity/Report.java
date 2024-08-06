package com.example.demo.common.entity;

import com.example.demo.common.BaseEntity;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reports", schema = "test")
@AttributeOverrides({
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at"))
})
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "resource_id")
    private Long resourceId;

    @NotNull
    @Lob
    @Column(name = "resource_type", nullable = false)
    private String resourceType;

}