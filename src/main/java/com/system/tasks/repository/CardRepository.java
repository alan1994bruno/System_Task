package com.system.tasks.repository;

import com.system.tasks.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByBoardId(Long boardId);
}