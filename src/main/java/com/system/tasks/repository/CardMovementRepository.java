package com.system.tasks.repository;

import com.system.tasks.domain.CardMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CardMovementRepository extends JpaRepository<CardMovement, Long> {
    CardMovement findTopByCardIdOrderByEnteredAtDesc(Long cardId);
    List<CardMovement> findByCardIdOrderByEnteredAtAsc(Long cardId);
}