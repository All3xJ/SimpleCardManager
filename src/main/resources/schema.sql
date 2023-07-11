CREATE DATABASE `testcards2` ;


CREATE TABLE testcards2.`user` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `enabled` bit(1) NOT NULL,
                                   `password` varchar(255) NOT NULL,
                                   `username` varchar(255) NOT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE testcards2.`role` (
                                   `id` tinyint NOT NULL AUTO_INCREMENT,
                                   `name` varchar(255) NOT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `UK_8sewwnpamngi6b1dwaa88askk` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE testcards2.`user_role` (
                                        `user_id` bigint NOT NULL,
                                        `role_id` tinyint NOT NULL,
                                        KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
                                        KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
                                        CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                                        CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE testcards2.`card` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `credit` double NOT NULL,
                                   `enabled` bit(1) NOT NULL,
                                   `owner_id` bigint DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `UK_evojhs42a0lpt48hdad3h9n2w` (`owner_id`),
                                   CONSTRAINT `FK8pspfj8x9rbqn67t0l8ir7im3` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE testcards2.`log` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `date_created` datetime(6) NOT NULL,
                                  `info` varchar(255) NOT NULL,
                                  `log_type` varchar(255) NOT NULL,
                                  `admin_id` bigint NOT NULL,
                                  `card_id` bigint DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `FKkfdjo6r87c1xw857ovt1l6f4m` (`admin_id`),
                                  KEY `FKn3fq3egp1wrl6qjuqortmvp6i` (`card_id`),
                                  CONSTRAINT `FKkfdjo6r87c1xw857ovt1l6f4m` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`),
                                  CONSTRAINT `FKn3fq3egp1wrl6qjuqortmvp6i` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE testcards2.`transaction` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `amount` double NOT NULL,
                                          `date_created` datetime(6) NOT NULL,
                                          `card_id` bigint NOT NULL,
                                          `merchant_id` bigint NOT NULL,
                                          PRIMARY KEY (`id`),
                                          KEY `FK484i2t8acnct6xy8ylevl40go` (`card_id`),
                                          KEY `FK4yhithmrpix4pmweoqq6r3shd` (`merchant_id`),
                                          CONSTRAINT `FK484i2t8acnct6xy8ylevl40go` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`),
                                          CONSTRAINT `FK4yhithmrpix4pmweoqq6r3shd` FOREIGN KEY (`merchant_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO testcards2.`user` (enabled,password,username) VALUES
                                                              (1,'$2a$10$dRyX8LO6/uIlCagdGZEusugS6Ysvq2V./pjDYL5hv.i4NHg2acXn6','admin'),
                                                              (1,'$2a$10$jGPnbrUQjAjofbETLLFEfeCS3R9nC37bjmFhpPTvaSEBYyjIgNb3u','admin2'),
                                                              (1,'$2a$10$aRDSrMx4Kp6udLEttKHuEuGVcJgsMOiVxhKVnM7H3HFmj46CV1dMW','rXBTxfRYv'),
                                                              (1,'$2a$10$mFpcc/y00JhptZqot5A0x.IjssHwYvaVlY0kurgcC5mae/jsy/2L2','uc1GKSm'),
                                                              (0,'$2a$10$6gOJMmX4NodqZJD1BSgRduUOAnuo5VR1SN.AzMpKJ38HRVO.URBX2','merc'),
                                                              (1,'$2a$10$JSv/4YMKbmyILq6K8PxeauQzewDk/dLNuQIIGaKxRONiuYvRZBM1O','merc2');


INSERT INTO testcards2.`role` (name) VALUES
                                         ('ROLE_ADMIN'),
                                         ('ROLE_MERCHANT'),
                                         ('ROLE_CARDOWNER');


INSERT INTO testcards2.user_role (user_id,role_id) VALUES
                                                       (1,1),
                                                       (2,1),
                                                       (3,3),
                                                       (4,3),
                                                       (5,2),
                                                       (6,2);


INSERT INTO testcards2.card (credit,enabled,owner_id) VALUES
                                                          (40.0,0,3),
                                                          (170.82,1,4);


INSERT INTO testcards2.`transaction` (amount,date_created,card_id,merchant_id) VALUES
                                                                                   (10.0,'2023-07-11 15:32:36.624000',1,1),
                                                                                   (-10.0,'2023-07-11 15:32:39.125000',1,1),
                                                                                   (-10.0,'2023-07-11 15:32:39.690000',1,1),
                                                                                   (-10.0,'2023-07-11 15:32:40.301000',1,1),
                                                                                   (-40.0,'2023-07-11 15:32:45.858000',1,1),
                                                                                   (-9.0,'2023-07-11 15:33:58.044000',2,1),
                                                                                   (-9.12,'2023-07-11 15:34:04.264000',2,1),
                                                                                   (-9.16,'2023-07-11 15:34:11.159000',2,1),
                                                                                   (-9.16,'2023-07-11 15:34:19.048000',2,1),
                                                                                   (19.16,'2023-07-11 15:34:24.918000',2,1);

INSERT INTO testcards2.`transaction` (amount,date_created,card_id,merchant_id) VALUES
                                                                                   (-1.0,'2023-07-11 15:46:16.471000',2,6),
                                                                                   (-10.9,'2023-07-11 15:46:25.112000',2,6);


INSERT INTO testcards2.log (date_created,info,log_type,admin_id,card_id) VALUES
                                                                             ('2023-07-11 15:32:12.829000','100.0','newcard',1,1),
                                                                             ('2023-07-11 15:32:22.253000','200.0','newcard',1,2),
                                                                             ('2023-07-11 15:34:35.461000','false','blockunblockcard',1,1),
                                                                             ('2023-07-11 15:34:37.222000','false','blockunblockcard',1,2),
                                                                             ('2023-07-11 15:34:39.514000','true','blockunblockcard',1,2),
                                                                             ('2023-07-11 15:34:48.330000','merc','registeredmerchant',1,NULL),
                                                                             ('2023-07-11 15:37:06.631000','merc','disableenablemerchant',1,NULL),
                                                                             ('2023-07-11 15:37:07.412000','merc','disableenablemerchant',1,NULL),
                                                                             ('2023-07-11 15:37:45.237000','merc2','registeredmerchant',1,NULL),
                                                                             ('2023-07-11 15:40:08.220000','merc','disableenablemerchant',2,NULL);


