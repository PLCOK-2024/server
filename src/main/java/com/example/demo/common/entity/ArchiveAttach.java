package com.example.demo.common.entity;

import com.example.demo.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "archive_attaches", schema = "test")
public class ArchiveAttach extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @Size(max = 200)
    @NotNull
    @Column(name = "path", nullable = false, length = 200)
    private String path;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

}