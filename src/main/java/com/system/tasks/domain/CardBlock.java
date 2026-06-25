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
@Table(name = "card_blocks")
@Data
public class CardBlock {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "blocked_reason")
    private String blockedReason;

    @Column(name = "unblocked_at")
    private LocalDateTime unblockedAt;

    @Column(name = "unblocked_reason")
    private String unblockedReason;
}