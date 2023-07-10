INSERT INTO creditcardsmanager.`user` (username,password,enabled) VALUES
    ('a','$2a$10$5be/Yu2OVueApOz9wh/nxe7LOOtG6m887LJ5qhTCnvXszIp5muYSG',1);





INSERT INTO creditcardsmanager.`role` (name) VALUES
                                                 ('ROLE_ADMIN'),
                                                 ('ROLE_MERCHANT'),
                                                 ('ROLE_CARDOWNER');




INSERT INTO creditcardsmanager.user_role (user_id,role_id) VALUES
    (1,1);