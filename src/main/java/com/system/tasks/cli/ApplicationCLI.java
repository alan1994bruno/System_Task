package com.system.tasks.cli;

import com.system.tasks.domain.Board;
import com.system.tasks.service.BoardService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

    @Component
    public class ApplicationCLI implements CommandLineRunner {

        private final BoardService boardService;
        private final Scanner scanner;
        private Board selectedBoard;

        public ApplicationCLI(BoardService boardService) {
            this.boardService = boardService;
            this.scanner = new Scanner(System.in);
            this.selectedBoard = null;
        }

        @Override
        public void run(String... args) {
            var isRunning = true;

            while (isRunning) {
                try {
                    if (selectedBoard == null) {
                        displayMainMenu();
                        var choice = getUserChoice();
                        isRunning = processMainMenu(choice);
                    } else {
                        displayBoardMenu();
                        var choice = getUserChoice();
                        processBoardMenu(choice);
                    }
                } catch (Exception exception) {
                    System.out.println("\n[ERRO] " + exception.getMessage());
                }
            }
        }

        private void displayMainMenu() {
            System.out.print("""
            
            --- MENU PRINCIPAL ---
            1 - Criar novo board
            2 - Listar boards
            3 - Selecionar board
            4 - Excluir boards
            5 - Sair
            Escolha uma opção:\s""");
        }

        private boolean processMainMenu(int choice) {
            switch (choice) {
                case 1 -> executeCreateBoard();
                case 2 -> executeListBoards();
                case 3 -> executeSelectBoard();
                case 4 -> executeDeleteBoard();
                case 5 -> {
                    System.out.println("Saindo do sistema...");
                    return false;
                }
                default -> System.out.println("Opção inválida.");
            }
            return true;
        }

        private void displayBoardMenu() {
            System.out.print("""
            
            --- BOARD:\s""" + selectedBoard.getName() + """
            \s---
            1 - Criar um card
            2 - Listar cards
            3 - Mover card para próxima coluna
            4 - Cancelar um card
            5 - Bloquear card
            6 - Desbloquear card
            7 - Relatório de tempo das tarefas
            8 - Relatório de bloqueios
            9 - Fechar board
            Escolha uma opção:\s""");
        }

        private void processBoardMenu(int choice) {
            switch (choice) {
                case 1 -> executeCreateCard();
                case 2 -> executeListCards();
                case 3 -> executeMoveCard();
                case 4 -> executeCancelCard();
                case 5 -> executeBlockCard();
                case 6 -> executeUnblockCard();
                case 7 -> executeTimeReport();
                case 8 -> executeBlockReport();
                case 9 -> selectedBoard = null;
                default -> System.out.println("Opção inválida.");
            }
        }

        private int getUserChoice() {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException exception) {
                return -1;
            }
        }

        private void executeCreateBoard() {
            System.out.print("Digite o nome do novo board: ");
            var boardName = scanner.nextLine();
            boardService.createBoard(boardName);
            System.out.println("Board criado com sucesso!");
        }

        private void executeSelectBoard() {
            System.out.print("Digite o ID do board que deseja selecionar: ");
            var boardId = Long.parseLong(scanner.nextLine());
            selectedBoard = boardService.findById(boardId);
            if (selectedBoard == null) {
                System.out.println("Board não encontrado.");
            }
        }

        private void executeListBoards() {
            var boards = boardService.findAll();

            if (boards.isEmpty()) {
                System.out.println("Nenhum board encontrado no banco de dados.");
                return;
            }

            System.out.println("+------+------------------------------------------+-----------------+");
            System.out.println("| ID   | Nome do Board                            | Status          |");
            System.out.println("+------+------------------------------------------+-----------------+");

            for (var board : boards) {
                var cards = boardService.findCardsByBoard(board.getId());
                var status = "Inicial";

                if (!cards.isEmpty()) {
                    var isCompleted = cards.stream().allMatch(card ->
                            "FINAL".equals(card.getColumn().getType().name()) ||
                                    "CANCEL".equals(card.getColumn().getType().name())
                    );
                    status = isCompleted ? "Concluído" : "Em andamento";
                }

                System.out.printf("| %-4d | %-40s | %-15s |%n",
                        board.getId(),
                        truncateText(board.getName(), 40),
                        status);
            }

            System.out.println("+------+------------------------------------------+-----------------+");
        }

        private void executeDeleteBoard() {
            System.out.print("Digite o ID do board que deseja excluir: ");
            var boardId = Long.parseLong(scanner.nextLine());
            boardService.deleteBoard(boardId);
            System.out.println("Board excluído com sucesso!");
        }

        private void executeCreateCard() {
            System.out.print("Título do card: ");
            var title = scanner.nextLine();
            System.out.print("Descrição do card: ");
            var description = scanner.nextLine();
            boardService.createCard(selectedBoard.getId(), title, description);
            System.out.println("Card criado com sucesso!");
        }

        private void executeListCards() {
            var cards = boardService.findCardsByBoard(selectedBoard.getId());

            if (cards.isEmpty()) {
                System.out.println("Nenhum card encontrado neste board.");
                return;
            }

            System.out.println("+------+--------------------------------+-----------------+-------------+");
            System.out.println("| ID   | Título                         | Coluna          | Status      |");
            System.out.println("+------+--------------------------------+-----------------+-------------+");

            for (var card : cards) {
                var status = card.getIsBlocked() ? "Bloqueado" : "Livre";
                var columnName = card.getColumn().getName();

                System.out.printf("| %-4d | %-30s | %-15s | %-11s |%n",
                        card.getId(),
                        truncateText(card.getTitle(), 30),
                        truncateText(columnName, 15),
                        status);
            }

            System.out.println("+------+--------------------------------+-----------------+-------------+");
        }

        private void executeMoveCard() {
            System.out.print("ID do card para mover: ");
            var cardId = Long.parseLong(scanner.nextLine());
            boardService.moveCard(cardId);
            System.out.println("Card movido com sucesso!");
        }

        private void executeCancelCard() {
            System.out.print("ID do card para cancelar: ");
            var cardId = Long.parseLong(scanner.nextLine());
            boardService.cancelCard(cardId);
            System.out.println("Card cancelado com sucesso!");
        }

        private void executeBlockCard() {
            System.out.print("ID do card para bloquear: ");
            var cardId = Long.parseLong(scanner.nextLine());
            System.out.print("Motivo do bloqueio: ");
            var reason = scanner.nextLine();
            boardService.blockCard(cardId, reason);
            System.out.println("Card bloqueado com sucesso!");
        }

        private void executeUnblockCard() {
            System.out.print("ID do card para desbloquear: ");
            var cardId = Long.parseLong(scanner.nextLine());
            System.out.print("Motivo do desbloqueio: ");
            var reason = scanner.nextLine();
            boardService.unblockCard(cardId, reason);
            System.out.println("Card desbloqueado com sucesso!");
        }

        private void executeTimeReport() {
            System.out.println("Gerando relatório de tempo...");
            boardService.generateTimeReport(selectedBoard.getId());
        }

        private void executeBlockReport() {
            System.out.println("Gerando relatório de bloqueios...");
            boardService.generateBlockReport(selectedBoard.getId());
        }

        private String truncateText(String text, int maxLength) {
            if (text == null) {
                return "";
            }
            if (text.length() <= maxLength) {
                return text;
            }
            return text.substring(0, maxLength - 3) + "...";
        }
}
