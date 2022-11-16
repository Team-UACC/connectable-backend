CREATE TABLE IF NOT EXISTS `notice` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6) NOT NULL,
    `modified_date` datetime(6) NOT NULL,
    `notice_status` varchar(255) DEFAULT NULL,
    `title` varchar(255) NOT NULL,
    `contents` longtext NOT NULL,
    `artist_id` bigint NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;

ALTER TABLE notice ADD CONSTRAINT `fk_notice_artist` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`);

ALTER TABLE artist ADD COLUMN description varchar(255) DEFAULT NULL;
ALTER TABLE artist ADD COLUMN twitter_url varchar(255) DEFAULT NULL;
ALTER TABLE artist ADD COLUMN webpage_url varchar(255) DEFAULT NULL;
ALTER TABLE artist ADD COLUMN instagram_url varchar(255) DEFAULT NULL;