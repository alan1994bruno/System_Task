package com.system.tasks.repository;

import com.system.tasks.domain.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
}