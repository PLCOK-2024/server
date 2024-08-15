package com.plcok.archive.entity;

import com.plcok.common.BaseEntity;
import com.plcok.common.entity.IReportable;
import com.plcok.user.entity.User;
import com.plcok.common.entity.enumerated.ResourceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "archives")
@AttributeOverrides({
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at", nullable = false)),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
@NoArgsConstructor
@AllArgsConstructor
public class Archive extends BaseEntity implements IReportable {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NotNull
    @Column(columnDefinition = "geometry")
    private Point location;

    @NotNull
    @Column(name = "position_x", nullable = false, precision = 10)
    private Double positionX;

    @NotNull
    @Column(name = "position_y", nullable = false, precision = 10)
    private Double positionY;

    @Size(max = 200)
    @Column(name = "address", length = 500)
    private String address;

    @Size(max = 200)
    @Column(name = "name", length = 200)
    private String name;

    @Size(max = 2000)
    @Column(name = "content", length = 2000)
    private String content;

    @NotNull
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    @OneToMany(mappedBy = "archive", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("sequence desc")
    private List<ArchiveAttach> archiveAttaches = new ArrayList<>();

    @OneToMany(mappedBy = "archive")
    private Set<ArchiveComment> archiveComments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "archive")
    private Set<ArchiveReaction> archiveReactions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "archive", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("sequence desc")
    private List<ArchiveTag> tags = new ArrayList<>();

    @Override
    public User getUser() {
        return author;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.ARCHIVE;
    }
}