CREATE TABLE `artist` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `artist_image` varchar(255) DEFAULT NULL,
                          `artist_name` varchar(255) DEFAULT NULL,
                          `bank_account` varchar(255) DEFAULT NULL,
                          `bank_company` varchar(255) DEFAULT NULL,
                          `email` varchar(255) DEFAULT NULL,
                          `password` varchar(255) DEFAULT NULL,
                          `phone_number` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `is_active` bit(1) NOT NULL,
                        `klaytn_address` varchar(255) DEFAULT NULL,
                        `nickname` varchar(255) DEFAULT NULL,
                        `phone_number` varchar(255) DEFAULT NULL,
                        `privacy_agreement` bit(1) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `created_date` datetime(6) NOT NULL,
                          `modified_date` datetime(6) NOT NULL,
                          `orderer_name` varchar(255) DEFAULT NULL,
                          `orderer_phone_number` varchar(255) DEFAULT NULL,
                          `user_id` bigint NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FKel9kyl84ego2otj2accfd8mr7` (`user_id`),
                          CONSTRAINT `FKel9kyl84ego2otj2accfd8mr7` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `event` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `contract_address` varchar(255) DEFAULT NULL,
                         `description` longtext NOT NULL,
                         `end_time` datetime(6) DEFAULT NULL,
                         `event_image` varchar(255) DEFAULT NULL,
                         `event_name` varchar(255) DEFAULT NULL,
                         `event_sales_option` varchar(255) DEFAULT NULL,
                         `instagram_url` varchar(255) DEFAULT NULL,
                         `location` varchar(255) DEFAULT NULL,
                         `sales_from` datetime(6) DEFAULT NULL,
                         `sales_to` datetime(6) DEFAULT NULL,
                         `start_time` datetime(6) DEFAULT NULL,
                         `twitter_url` varchar(255) DEFAULT NULL,
                         `webpage_url` varchar(255) DEFAULT NULL,
                         `artist_id` bigint NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FKdvvkbxm5gn0f66no9ja48hks2` (`artist_id`),
                         CONSTRAINT `FKdvvkbxm5gn0f66no9ja48hks2` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `ticket` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `price` int NOT NULL,
                          `ticket_metadata` text,
                          `ticket_sales_status` varchar(255) DEFAULT NULL,
                          `token_id` int NOT NULL,
                          `token_uri` varchar(255) DEFAULT NULL,
                          `event_id` bigint NOT NULL,
                          `user_id` bigint NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FKfytuhjopeamxbt1cpudy92x5n` (`event_id`),
                          KEY `FKdvt57mcco3ogsosi97odw563o` (`user_id`),
                          CONSTRAINT `FKdvt57mcco3ogsosi97odw563o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                          CONSTRAINT `FKfytuhjopeamxbt1cpudy92x5n` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `order_detail` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `created_date` datetime(6) NOT NULL,
                                `modified_date` datetime(6) NOT NULL,
                                `order_status` varchar(255) DEFAULT NULL,
                                `tx_hash` varchar(255) DEFAULT NULL,
                                `order_id` bigint NOT NULL,
                                `ticket_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FKrws2q0si6oyd6il8gqe2aennc` (`order_id`),
                                KEY `FKrre8eo0thwbk3a83648x1o0ep` (`ticket_id`),
                                CONSTRAINT `FKrre8eo0thwbk3a83648x1o0ep` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`),
                                CONSTRAINT `FKrws2q0si6oyd6il8gqe2aennc` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
