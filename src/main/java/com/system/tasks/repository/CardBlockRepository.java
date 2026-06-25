package com.system.tasks.repository;

import com.system.tasks.domain.CardBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CardBlockRepository extends JpaRepository<CardBlock, Long> {
    CardBlock findTopByCardIdOrderByBlockedAtDesc(Long cardId);
    List<CardBlock> findByCardIdOrderByBlockedAtAsc(Long cardId);
}