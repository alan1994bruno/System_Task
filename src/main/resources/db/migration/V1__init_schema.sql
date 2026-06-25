CREATE TABLE boards (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
);


CREATE TABLE board_columns (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               board_id BIGINT NOT NULL,
                               name VARCHAR(255) NOT NULL,
                               type ENUM('INITIAL', 'PENDING', 'FINAL', 'CANCEL') NOT NULL,
                               order_index INT NOT NULL,
                               CONSTRAINT fk_column_board FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE
);


CREATE TABLE cards (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       board_id BIGINT NOT NULL,
                       column_id BIGINT NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       is_blocked BOOLEAN DEFAULT FALSE,
                       CONSTRAINT fk_card_board FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
                       CONSTRAINT fk_card_column FOREIGN KEY (column_id) REFERENCES board_columns(id)
);


CREATE TABLE card_movements (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                card_id BIGINT NOT NULL,
                                column_id BIGINT NOT NULL,
                                entered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                left_at TIMESTAMP NULL,
                                CONSTRAINT fk_movement_card FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE,
                                CONSTRAINT fk_movement_column FOREIGN KEY (column_id) REFERENCES board_columns(id)
);


CREATE TABLE card_blocks (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             card_id BIGINT NOT NULL,
                             blocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             blocked_reason TEXT NOT NULL,
                             unblocked_at TIMESTAMP NULL,
                             unblocked_reason TEXT,
                             CONSTRAINT fk_block_card FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
);