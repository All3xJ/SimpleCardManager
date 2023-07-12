CREATE DATABASE `cardmanagerdbgenova` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;






-- cardmanagerdbgenova.`user` definition

CREATE TABLE cardmanagerdbgenova.`user` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `enabled` bit(1) NOT NULL,
                                            `password` varchar(255) NOT NULL,
                                            `username` varchar(255) NOT NULL,
                                            PRIMARY KEY (`id`),
                                            UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- cardmanagerdbgenova.`role` definition

CREATE TABLE cardmanagerdbgenova. `role` (
                                             `id` tinyint NOT NULL AUTO_INCREMENT,
                                             `name` varchar(255) NOT NULL,
                                             PRIMARY KEY (`id`),
                                             UNIQUE KEY `UK_8sewwnpamngi6b1dwaa88askk` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- cardmanagerdbgenova.user_role definition

CREATE TABLE cardmanagerdbgenova. `user_role` (
                                                  `user_id` bigint NOT NULL,
                                                  `role_id` tinyint NOT NULL,
                                                  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
                                                  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
                                                  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                                                  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- cardmanagerdbgenova.card definition

CREATE TABLE  cardmanagerdbgenova. `card` (
                                              `id` bigint NOT NULL AUTO_INCREMENT,
                                              `credit` double NOT NULL,
                                              `enabled` bit(1) NOT NULL,
                                              `owner_id` bigint DEFAULT NULL,
                                              PRIMARY KEY (`id`),
                                              UNIQUE KEY `UK_evojhs42a0lpt48hdad3h9n2w` (`owner_id`),
                                              CONSTRAINT `FK8pspfj8x9rbqn67t0l8ir7im3` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- cardmanagerdbgenova.`transaction` definition

CREATE TABLE  cardmanagerdbgenova. `transaction` (
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- cardmanagerdbgenova.log definition

CREATE TABLE  cardmanagerdbgenova. `log` (
                                             `id` bigint NOT NULL AUTO_INCREMENT,
                                             `date_created` datetime(6) NOT NULL,
                                             `info` varchar(255) NOT NULL,
                                             `log_type` varchar(255) NOT NULL,
                                             `admin_id` bigint NOT NULL,
                                             `card_id` bigint DEFAULT NULL,
                                             `merchant_id` bigint DEFAULT NULL,
                                             PRIMARY KEY (`id`),
                                             KEY `FKkfdjo6r87c1xw857ovt1l6f4m` (`admin_id`),
                                             KEY `FKn3fq3egp1wrl6qjuqortmvp6i` (`card_id`),
                                             KEY `FKh4adaat47ed2o1ah0aocjkt7w` (`merchant_id`),
                                             CONSTRAINT `FKh4adaat47ed2o1ah0aocjkt7w` FOREIGN KEY (`merchant_id`) REFERENCES `user` (`id`),
                                             CONSTRAINT `FKkfdjo6r87c1xw857ovt1l6f4m` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`),
                                             CONSTRAINT `FKn3fq3egp1wrl6qjuqortmvp6i` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;





-- inserimento valori iniziali nelle tabelle

INSERT INTO cardmanagerdbgenova.`user` (enabled,password,username) VALUES
                                                                       (1,'$2a$10$dRyX8LO6/uIlCagdGZEusugS6Ysvq2V./pjDYL5hv.i4NHg2acXn6','admin'),
                                                                       (1,'$2a$10$jGPnbrUQjAjofbETLLFEfeCS3R9nC37bjmFhpPTvaSEBYyjIgNb3u','admin2'),
                                                                       (1,'$2a$10$pdbnRvCx1xk/9mU6wNJ0x.0ri33f6UBOsoZmDBURpYaTfbfImbX5i','merc'),
                                                                       (0,'$2a$10$93K53ZnokVTm0j3UBzrIweYv5foDotfUw4ivNIBnHrc7Finw3nBEq','merc2'),
                                                                       (1,'$2a$10$Pe.WAm9aBSKD2Y5pSrs3au/13wqCT4skjWIakgH4E0NHWOkVCNau.','ouTQCAXAG'),
                                                                       (1,'$2a$10$IVX..Bqjml.J2iMt1N7/Ce/jOyurCYm82VSbQFETc8f8cmAxBaMH2','jFWAwVl'),
                                                                       (1,'$2a$10$t2vxKOR07HAgUTU7x7Lgx.8dfCXiuLM.2NjKHA.MFk2kcc3e4r5zi','klXTdxSz');

INSERT INTO cardmanagerdbgenova.`role` (name) VALUES
                                                  ('ROLE_ADMIN'),
                                                  ('ROLE_MERCHANT'),
                                                  ('ROLE_CARDOWNER');

INSERT INTO cardmanagerdbgenova.user_role (user_id,role_id) VALUES
                                                                (1,1),
                                                                (2,1),
                                                                (3,2),
                                                                (4,2),
                                                                (5,3),
                                                                (6,3),
                                                                (7,3);

INSERT INTO cardmanagerdbgenova.card (credit,enabled,owner_id) VALUES
                                                                   (75.0,1,5),
                                                                   (200.0,0,6),
                                                                   (100.0,1,7);

INSERT INTO cardmanagerdbgenova.`transaction` (amount,date_created,card_id,merchant_id) VALUES
                                                                                            (-30.0,'2023-07-11 19:44:38.752000',1,3),
                                                                                            (5.0,'2023-07-11 19:44:43.493000',1,3),
                                                                                            (-50.0,'2023-07-11 19:44:50.904000',3,3),
                                                                                            (-5.0,'2023-07-11 19:44:57.550000',3,3),
                                                                                            (5.0,'2023-07-11 19:45:00.021000',3,3);

INSERT INTO cardmanagerdbgenova.log (date_created,info,log_type,admin_id,card_id,merchant_id) VALUES
                                                                                                  ('2023-07-11 19:40:02.292000','','registeredmerchant',1,NULL,3),
                                                                                                  ('2023-07-11 19:40:14.085000','','registeredmerchant',1,NULL,4),
                                                                                                  ('2023-07-11 19:40:18.451000','false','disableenablemerchant',1,NULL,3),
                                                                                                  ('2023-07-11 19:40:19.382000','true','disableenablemerchant',1,NULL,3),
                                                                                                  ('2023-07-11 19:41:41.855000','100.0','newcard',1,1,NULL),
                                                                                                  ('2023-07-11 19:41:54.389000','200.0','newcard',1,2,NULL),
                                                                                                  ('2023-07-11 19:42:07.905000','false','blockunblockcard',1,2,NULL),
                                                                                                  ('2023-07-11 19:42:08.363000','true','blockunblockcard',1,2,NULL),
                                                                                                  ('2023-07-11 19:42:08.872000','false','blockunblockcard',1,2,NULL),
                                                                                                  ('2023-07-11 19:42:09.256000','true','blockunblockcard',1,2,NULL);
INSERT INTO cardmanagerdbgenova.log (date_created,info,log_type,admin_id,card_id,merchant_id) VALUES
                                                                                                  ('2023-07-11 19:42:20.342000','false','disableenablemerchant',1,NULL,4),
                                                                                                  ('2023-07-11 19:42:54.166000','150.0','newcard',2,3,NULL),
                                                                                                  ('2023-07-11 19:43:22.442000','true','disableenablemerchant',2,NULL,4),
                                                                                                  ('2023-07-11 19:43:23.267000','false','disableenablemerchant',2,NULL,4),
                                                                                                  ('2023-07-11 19:43:31.247000','false','blockunblockcard',2,2,NULL);