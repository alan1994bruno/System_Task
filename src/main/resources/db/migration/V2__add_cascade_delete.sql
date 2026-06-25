ALTER TABLE cards
DROP FOREIGN KEY fk_card_column;

ALTER TABLE cards
    ADD CONSTRAINT fk_card_column
        FOREIGN KEY (column_id) REFERENCES board_columns(id) ON DELETE CASCADE;


ALTER TABLE card_movements
DROP FOREIGN KEY fk_movement_column;

ALTER TABLE card_movements
    ADD CONSTRAINT fk_movement_column
        FOREIGN KEY (column_id) REFERENCES board_columns(id) ON DELETE CASCADE;