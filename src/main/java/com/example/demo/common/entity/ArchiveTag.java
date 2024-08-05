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
@Table(name = "archive_tags", schema = "test")
public class ArchiveTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "archive_id", nullable = false)
    private Archive archive;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @NotNull
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

}