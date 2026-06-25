package com.system.tasks.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import lombok.Data;


@Entity
@Table(name = "board_columns")
@Data
public class BoardColumn {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String name;

    @Enumerated(EnumType.STRING)
    private ColumnType type;

    @Column(name = "order_index")
    private Integer orderIndex;
}