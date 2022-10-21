CREATE TABLE IF NOT EXISTS `comment` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `created_date` datetime(6) NOT NULL,
    `modified_date` datetime(6) NOT NULL,
    `contents` longtext NOT NULL,
    `artist_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE comment
    ADD CONSTRAINT fk_comment_to_artist
        FOREIGN KEY (artist_id)
            REFERENCES artist(id);

ALTER TABLE comment
    ADD CONSTRAINT fk_comment_to_user
        FOREIGN KEY (user_id)
            REFERENCES user(id);