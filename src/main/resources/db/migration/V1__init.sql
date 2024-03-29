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
                          PRIMARY KEY (`id`)
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
                         PRIMARY KEY (`id`)
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
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `order_detail` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `created_date` datetime(6) NOT NULL,
                                `modified_date` datetime(6) NOT NULL,
                                `order_status` varchar(255) DEFAULT NULL,
                                `tx_hash` varchar(255) DEFAULT NULL,
                                `order_id` bigint NOT NULL,
                                `ticket_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
