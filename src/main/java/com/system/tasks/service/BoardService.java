package com.system.tasks.service;

import com.system.tasks.domain.*;
import com.system.tasks.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardColumnRepository columnRepository;
    private final CardRepository cardRepository;
    private final CardMovementRepository movementRepository;
    private final CardBlockRepository blockRepository;

    public BoardService(BoardRepository boardRepository, BoardColumnRepository columnRepository,
                        CardRepository cardRepository, CardMovementRepository movementRepository,
                        CardBlockRepository blockRepository) {
        this.boardRepository = boardRepository;
        this.columnRepository = columnRepository;
        this.cardRepository = cardRepository;
        this.movementRepository = movementRepository;
        this.blockRepository = blockRepository;
    }

    @Transactional
    public void createBoard(String name) {
        var board = new Board();
        board.setName(name);
        board = boardRepository.save(board);

        var initialCol = new BoardColumn();
        initialCol.setBoard(board);
        initialCol.setName("Pendente");
        initialCol.setType(ColumnType.INITIAL);
        initialCol.setOrderIndex(1);
        columnRepository.save(initialCol);

        var finalCol = new BoardColumn();
        finalCol.setBoard(board);
        finalCol.setName("Finalizado");
        finalCol.setType(ColumnType.FINAL);
        finalCol.setOrderIndex(2);
        columnRepository.save(finalCol);

        var cancelCol = new BoardColumn();
        cancelCol.setBoard(board);
        cancelCol.setName("Cancelamento");
        cancelCol.setType(ColumnType.CANCEL);
        cancelCol.setOrderIndex(3);
        columnRepository.save(cancelCol);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void createCard(Long boardId, String title, String description) {
        var board = boardRepository.findById(boardId).orElseThrow();
        var initialCol = board.getColumns().stream()
                .filter(col -> col.getType() == ColumnType.INITIAL)
                .findFirst()
                .orElseThrow();

        var card = new Card();
        card.setBoard(board);
        card.setColumn(initialCol);
        card.setTitle(title);
        card.setDescription(description);
        card.setIsBlocked(false);
        card = cardRepository.save(card);

        var movement = new CardMovement();
        movement.setCard(card);
        movement.setColumn(initialCol);
        movement.setEnteredAt(LocalDateTime.now());
        movementRepository.save(movement);
    }

    @Transactional
    public void moveCard(Long cardId) {
        var card = cardRepository.findById(cardId).orElseThrow();

        if (card.getIsBlocked()) {
            throw new IllegalStateException("O card está bloqueado e não pode ser movido.");
        }

        var currentCol = card.getColumn();

        if (currentCol.getType() == ColumnType.FINAL || currentCol.getType() == ColumnType.CANCEL) {
            throw new IllegalStateException("O card já está em uma coluna final ou de cancelamento.");
        }

        var board = card.getBoard();
        var nextCol = board.getColumns().stream()
                .filter(col -> col.getOrderIndex() == currentCol.getOrderIndex() + 1)
                .findFirst()
                .orElseThrow();

        var currentMovement = movementRepository.findTopByCardIdOrderByEnteredAtDesc(cardId);
        if (currentMovement != null) {
            currentMovement.setLeftAt(LocalDateTime.now());
            movementRepository.save(currentMovement);
        }

        card.setColumn(nextCol);
        cardRepository.save(card);

        var newMovement = new CardMovement();
        newMovement.setCard(card);
        newMovement.setColumn(nextCol);
        newMovement.setEnteredAt(LocalDateTime.now());
        movementRepository.save(newMovement);
    }

    @Transactional
    public void cancelCard(Long cardId) {
        var card = cardRepository.findById(cardId).orElseThrow();

        if (card.getIsBlocked()) {
            throw new IllegalStateException("Desbloqueie o card antes de cancelar.");
        }

        if (card.getColumn().getType() == ColumnType.FINAL) {
            throw new IllegalStateException("Cards finalizados não podem ser cancelados.");
        }

        var cancelCol = card.getBoard().getColumns().stream()
                .filter(col -> col.getType() == ColumnType.CANCEL)
                .findFirst()
                .orElseThrow();

        var currentMovement = movementRepository.findTopByCardIdOrderByEnteredAtDesc(cardId);
        if (currentMovement != null && currentMovement.getLeftAt() == null) {
            currentMovement.setLeftAt(LocalDateTime.now());
            movementRepository.save(currentMovement);
        }

        card.setColumn(cancelCol);
        cardRepository.save(card);

        var newMovement = new CardMovement();
        newMovement.setCard(card);
        newMovement.setColumn(cancelCol);
        newMovement.setEnteredAt(LocalDateTime.now());
        movementRepository.save(newMovement);
    }

    @Transactional
    public void blockCard(Long cardId, String reason) {
        var card = cardRepository.findById(cardId).orElseThrow();
        card.setIsBlocked(true);
        cardRepository.save(card);

        var block = new CardBlock();
        block.setCard(card);
        block.setBlockedReason(reason);
        block.setBlockedAt(LocalDateTime.now());
        blockRepository.save(block);
    }

    @Transactional
    public void unblockCard(Long cardId, String reason) {
        var card = cardRepository.findById(cardId).orElseThrow();
        card.setIsBlocked(false);
        cardRepository.save(card);

        var block = blockRepository.findTopByCardIdOrderByBlockedAtDesc(cardId);
        if (block != null) {
            block.setUnblockedReason(reason);
            block.setUnblockedAt(LocalDateTime.now());
            blockRepository.save(block);
        }
    }

    public void generateTimeReport(Long boardId) {
        var cards = cardRepository.findByBoardId(boardId);

        if (cards.isEmpty()) {
            System.out.println("Nenhum card encontrado neste board.");
            return;
        }

        for (var card : cards) {
            System.out.println("\nCard: " + card.getTitle());
            var movements = movementRepository.findByCardIdOrderByEnteredAtAsc(card.getId());

            for (var mov : movements) {
                var end = mov.getLeftAt() != null ? mov.getLeftAt() : LocalDateTime.now();
                var duration = Duration.between(mov.getEnteredAt(), end);

                System.out.printf("  Coluna: %s | Tempo: %d minutos%n",
                        mov.getColumn().getName(),
                        duration.toMinutes());
            }
        }
    }

    public void generateBlockReport(Long boardId) {
        var cards = cardRepository.findByBoardId(boardId);
        var hasBlocks = false;

        for (var card : cards) {
            var blocks = blockRepository.findByCardIdOrderByBlockedAtAsc(card.getId());

            if (!blocks.isEmpty()) {
                hasBlocks = true;
                System.out.println("\nCard: " + card.getTitle());

                for (var block : blocks) {
                    var end = block.getUnblockedAt() != null ? block.getUnblockedAt() : LocalDateTime.now();
                    var duration = Duration.between(block.getBlockedAt(), end);
                    var unblockReason = block.getUnblockedReason() != null ? block.getUnblockedReason() : "Ainda bloqueado";

                    System.out.printf("""
                              Motivo do bloqueio: %s
                              Motivo do desbloqueio: %s
                              Tempo bloqueado: %d minutos
                            """,
                            block.getBlockedReason(),
                            unblockReason,
                            duration.toMinutes());
                }
            }
        }

        if (!hasBlocks) {
            System.out.println("Nenhum registro de bloqueio encontrado para os cards deste board.");
        }
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public List<Card> findCardsByBoard(Long boardId) {
        return cardRepository.findByBoardId(boardId);
    }
}