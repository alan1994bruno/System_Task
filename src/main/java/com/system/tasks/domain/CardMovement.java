package com.system.tasks.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDateTime;


@Entity
@Table(name = "card_movements")
@Data
public class CardMovement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private BoardColumn column;

    @Column(name = "entered_at")
    private LocalDateTime enteredAt;

    @Column(name = "left_at")
    private LocalDateTime leftAt;
}