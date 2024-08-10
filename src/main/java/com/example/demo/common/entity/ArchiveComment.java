package com.example.demo.common.entity;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.entity.enumerated.ResourceType;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "archive_comments", schema = "test")
@AttributeOverrides({
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at", nullable = false)),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveComment extends BaseEntity implements IReportable {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ArchiveComment parent;

    @Size(max = 2000)
    @NotNull
    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @OneToMany(mappedBy = "parent")
    private List<ArchiveComment> subComments = new ArrayList<>();

    @Override
    public User getUser() {
        return author;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.COMMENT;
    }
}