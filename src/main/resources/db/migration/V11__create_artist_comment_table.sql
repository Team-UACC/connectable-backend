CREATE TABLE IF NOT EXISTS `comment` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `created_date` datetime(6) NOT NULL,
    `modified_date` datetime(6) NOT NULL,
    `contents` longtext NOT NULL,
    `artist_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

alter table comment
    add constraint fk_comment_to_artist
        foreign key (artist_id)
            references artist(id);

alter table comment
    add constraint fk_comment_to_user
        foreign key (user_id)
            references user(id);