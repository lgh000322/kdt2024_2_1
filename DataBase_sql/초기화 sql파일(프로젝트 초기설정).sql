-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema erp_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `erp_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `erp_db` ;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS `board`;
DROP TABLE IF EXISTS `board_answer`;
DROP TABLE IF EXISTS `dept`;
DROP TABLE IF EXISTS `leave_log`;
DROP TABLE IF EXISTS `mail`;
DROP TABLE IF EXISTS `mail_store`;
DROP TABLE IF EXISTS `position`;
DROP TABLE IF EXISTS `received_mail`;
DROP TABLE IF EXISTS `salary_log`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `work_log`;

-- -----------------------------------------------------
-- Table `erp_db`.`position`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`position` (
  `position_num` BIGINT NOT NULL AUTO_INCREMENT,
  `position_name` VARCHAR(45) NOT NULL,
  `leave_day` INT NOT NULL,
  `basic_salary` INT NOT NULL,
  `leave_pay` INT NOT NULL,
  PRIMARY KEY (`position_num`),
  UNIQUE INDEX `position_num_UNIQUE` (`position_num` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erp_db`.`dept`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`dept` (
  `dept_num` BIGINT NOT NULL AUTO_INCREMENT,
  `dept_name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`dept_num`),
  UNIQUE INDEX `dept_num_UNIQUE` (`dept_num` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erp_db`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`user` (
  `user_num` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(30) NOT NULL,
  `password` VARCHAR(30) NOT NULL,
  `name` VARCHAR(15) NOT NULL,
  `tel` VARCHAR(20) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `role` ENUM('ADMIN', 'USER') NOT NULL,
  `remained_leave` INT NOT NULL DEFAULT 0,
  `position_num` BIGINT NOT NULL,
  `dept_num` BIGINT NOT NULL,
  PRIMARY KEY (`user_num`),
  UNIQUE INDEX `user_num_UNIQUE` (`user_num` ASC),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC),
  UNIQUE INDEX `tel_UNIQUE` (`tel` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  INDEX `fk_user_position_idx` (`position_num` ASC),
  INDEX `fk_user_dept1_idx` (`dept_num` ASC),
  CONSTRAINT `fk_user_position`
    FOREIGN KEY (`position_num`)
    REFERENCES `erp_db`.`position` (`position_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_dept1`
    FOREIGN KEY (`dept_num`)
    REFERENCES `erp_db`.`dept` (`dept_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erp_db`.`leave_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`leave_log` (
  `leave_num` BIGINT NOT NULL AUTO_INCREMENT,
  `request_date` DATE NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `acceptance_status` TINYINT NULL DEFAULT 0,
  `check_status` TINYINT DEFAULT 0,
  `user_num` BIGINT NOT NULL,
  PRIMARY KEY (`leave_num`),
  UNIQUE INDEX `leave_num_UNIQUE` (`leave_num` ASC),
  INDEX `fk_leave_log_user1_idx` (`user_num` ASC),
  CONSTRAINT `fk_leave_log_user1`
    FOREIGN KEY (`user_num`)
    REFERENCES `erp_db`.`user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erp_db`.`mail_store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`mail_store` (
  `mail_store_num` BIGINT NOT NULL AUTO_INCREMENT,
  `mail_type` ENUM('RECEIVED', 'SEND') NOT NULL,
  `user_num` BIGINT NOT NULL,
  PRIMARY KEY (`mail_store_num`),
  UNIQUE INDEX `mail_store_num_UNIQUE` (`mail_store_num` ASC),
  INDEX `fk_mail_store_user1_idx` (`user_num` ASC),
  CONSTRAINT `fk_mail_store_user1`
    FOREIGN KEY (`user_num`)
    REFERENCES `erp_db`.`user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erp_db`.`work_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`work_log` (
  `log_num` BIGINT NOT NULL AUTO_INCREMENT,
  `start_time` TIME NULL,
  `end_time` TIME NULL,
  `status` ENUM('ABSENCE', 'TARDINESS', 'ATTENDANCE', 'LEAVE') NOT NULL,
  `work_date` DATE NULL,
  `user_num` BIGINT NOT NULL,
  PRIMARY KEY (`log_num`),
  UNIQUE INDEX `log_num_UNIQUE` (`log_num` ASC),
  INDEX `fk_work_log_user1_idx` (`user_num` ASC),
  CONSTRAINT `fk_work_log_user1`
    FOREIGN KEY (`user_num`)
    REFERENCES `erp_db`.`user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erp_db`.`mail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS erp_db.mail (
  mai_num BIGINT NOT NULL AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  contents VARCHAR(5000) NULL,
  created_date DATE NOT NULL,
  mail_store_num BIGINT NOT NULL,
  is_deleted TINYINT(1) DEFAULT 0,  -- boolean 필드 추가, 기본값은 0 (false)
  PRIMARY KEY (mai_num),
  UNIQUE INDEX mai_num_UNIQUE (mai_num ASC),
  INDEX fk_mail_mail_store1_idx (mail_store_num ASC),
  CONSTRAINT fk_mail_mail_store1
    FOREIGN KEY (mail_store_num)
    REFERENCES erp_db.mail_store (mail_store_num)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
-- -----------------------------------------------------
-- Table `erp_db`.`received_mail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`received_mail` (
  `received_num` BIGINT NOT NULL AUTO_INCREMENT,
  `user_num` BIGINT NOT NULL,
  `mail_num` BIGINT NOT NULL,
  is_deleted TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`received_num`),
  UNIQUE INDEX `received_num_UNIQUE` (`received_num` ASC),
  INDEX `fk_received_mail_user1_idx` (`user_num` ASC),
  INDEX `fk_received_mail_mail1_idx` (`mail_num` ASC),
  CONSTRAINT `fk_received_mail_user1`
    FOREIGN KEY (`user_num`)
    REFERENCES `erp_db`.`user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_received_mail_mail1`
    FOREIGN KEY (`mail_num`)
    REFERENCES `erp_db`.`mail` (`mai_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erp_db`.`board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`board` (
  `board_num` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `contents` VARCHAR(5000) NULL,
  `user_num` BIGINT NOT NULL,
  `created_date` DATE NOT NULL,
  PRIMARY KEY (`board_num`),
  UNIQUE INDEX `board_num_UNIQUE` (`board_num` ASC),
  INDEX `fk_board_user1_idx` (`user_num` ASC),
  CONSTRAINT `fk_board_user1`
    FOREIGN KEY (`user_num`)
    REFERENCES `erp_db`.`user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erp_db`.`board_answer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`board_answer` (
  `answer_num` BIGINT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(1000) NULL,
  `board_num` BIGINT NOT NULL,
  `user_num` BIGINT NOT NULL,
  `created_date` DATE NOT NULL,
  PRIMARY KEY (`answer_num`),
  UNIQUE INDEX `answer_num_UNIQUE` (`answer_num` ASC),
  INDEX `fk_board_answer_board1_idx` (`board_num` ASC),
  INDEX `fk_board_answer_user1_idx` (`user_num` ASC),
  CONSTRAINT `fk_board_answer_board1`
    FOREIGN KEY (`board_num`)
    REFERENCES `erp_db`.`board` (`board_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_board_answer_user1`
    FOREIGN KEY (`user_num`)
    REFERENCES `erp_db`.`user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `erp_db`.`salary_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`salary_log` (
  `salary_num` BIGINT NOT NULL AUTO_INCREMENT,
  `received_date` DATE NOT NULL,
  `total_salary` INT NOT NULL,
  `user_num` BIGINT NOT NULL,
  PRIMARY KEY (`salary_num`),
  UNIQUE INDEX `salary_num_UNIQUE` (`salary_num` ASC),
  INDEX `fk_salary_log_user1_idx` (`user_num` ASC),
  CONSTRAINT `fk_salary_log_user1`
    FOREIGN KEY (`user_num`)
    REFERENCES `erp_db`.`user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Final Operations
-- -----------------------------------------------------
-- Restore original modes only if they were saved properly
SET SQL_MODE = IFNULL(@OLD_SQL_MODE, 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION');
SET FOREIGN_KEY_CHECKS = IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1);
SET UNIQUE_CHECKS = IFNULL(@OLD_UNIQUE_CHECKS, 1);

-- Insert initial data
INSERT INTO position (position_name, leave_day, basic_salary, leave_pay) VALUES 
('사원', 15, 3000000, 30000),
('대리', 16, 3500000, 35000),
('주임', 17, 4000000, 40000),
('팀장', 18, 4500000, 45000),
('부장', 19, 5000000, 50000),
('부사장', 20, 5500000, 60000),
('사장', 21, 6000000, 65000);

INSERT INTO dept (dept_name) VALUES 
('임시부서'),
('인사부'),
('재무부'),
('영업부'),
('IT'),
('법무부');

INSERT INTO `user` (user_id, password, name, tel, email, role, remained_leave, position_num, dept_num)
VALUES ('admin', 'admin', 'admin', '010-1111-1111', 'admin@knu.com', 'ADMIN', 21, 7, 2);

INSERT INTO `user` (user_id, password, name, tel, email, role, remained_leave, position_num, dept_num)
VALUES 
('normaluser1', 'normaluserpassword1', '홍길동', '010-2222-2222', 'normaluser1@naver.com', 'USER', 15, 1, 1),
('normaluser2', 'normaluserpassword2', '김철수', '010-3333-3333', 'normaluser2@naver.com', 'USER', 15, 1, 1),
('normaluser3', 'normaluserpassword3', '이영희', '010-4444-4444', 'normaluser3@naver.com', 'USER', 15, 1, 1),
('normaluser4', 'normaluserpassword4', '박민수', '010-5555-5555', 'normaluser4@naver.com', 'USER', 15, 1, 1),
('normaluser5', 'normaluserpassword5', '최지은', '010-6666-6666', 'normaluser5@naver.com', 'USER', 15, 1, 1),
('normaluser6', 'normaluserpassword6', '이재영', '010-7777-7777', 'normaluser6@naver.com', 'USER', 15, 1, 1),
('normaluser7', 'normaluserpassword7', '정하늘', '010-8888-8888', 'normaluser7@naver.com', 'USER', 15, 1, 1),
('normaluser8', 'normaluserpassword8', '김소연', '010-9999-9999', 'normaluser8@naver.com', 'USER', 15, 1, 1),
('normaluser9', 'normaluserpassword9', '이수민', '010-1010-1010', 'normaluser9@naver.com', 'USER', 15, 1, 1),
('normaluser10', 'normaluserpassword10', '박지민', '010-2020-2020', 'normaluser10@naver.com', 'USER', 15, 1, 1),
('normaluser11', 'normaluserpassword11', '최성훈', '010-3030-3030', 'normaluser11@naver.com', 'USER', 15, 1, 1),
('normaluser12', 'normaluserpassword12', '양하나', '010-4040-4040', 'normaluser12@naver.com', 'USER', 15, 1, 1),
('normaluser13', 'normaluserpassword13', '이재훈', '010-5050-5050', 'normaluser13@naver.com', 'USER', 15, 1, 1),
('normaluser14', 'normaluserpassword14', '정민지', '010-6060-6060', 'normaluser14@naver.com', 'USER', 15, 1, 1),
('normaluser15', 'normaluserpassword15', '이동준', '010-7070-7070', 'normaluser15@naver.com', 'USER', 15, 1, 1),
('normaluser16', 'normaluserpassword16', '김태현', '010-8080-8080', 'normaluser16@naver.com', 'USER', 15, 1, 1),
('normaluser17', 'normaluserpassword17', '정지우', '010-9090-9090', 'normaluser17@naver.com', 'USER', 15, 1, 1),
('normaluser18', 'normaluserpassword18', '최유리', '010-1111-2222', 'normaluser18@naver.com', 'USER', 15, 1, 1),
('normaluser19', 'normaluserpassword19', '이승현', '010-2222-3333', 'normaluser19@naver.com', 'USER', 15, 1, 1),
('normaluser20', 'normaluserpassword20', '박상혁', '010-3333-4444', 'normaluser20@naver.com', 'USER', 15, 1, 1);

INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 1);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 1);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 2);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 2);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 3);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 3);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 4);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 4);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 5);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 5);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 6);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 6);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 7);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 7);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 8);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 8);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 9);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 9);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 10);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 10);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 11);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 11);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 12);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 12);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 13);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 13);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 14);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 14);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 15);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 15);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 16);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 16);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 17);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 17);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 18);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 18);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 19);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 19);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 20);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 20);
INSERT INTO mail_store (mail_type, user_num) VALUES ('SEND', 21);
INSERT INTO mail_store (mail_type, user_num) VALUES ('RECEIVED', 21);

-- User 1의 salary_log 삽입
INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-03-10', 6000000, 1);
INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-04-10', 6000000, 1);
INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-05-10', 6000000, 1);
INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-06-10', 6000000, 1);
INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-07-10', 6000000, 1);
INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-08-10', 6000000, 1);
INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-09-10', 6000000, 1);

DROP PROCEDURE IF EXISTS my_procedure;
DROP PROCEDURE IF EXISTS insert_work_log;

-- User 2부터 20까지의 salary_log 삽입
DELIMITER //

CREATE PROCEDURE my_procedure()
BEGIN
    DECLARE i INT DEFAULT 2;
    
    WHILE i <= 21 DO
        INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-03-10', 3000000, i);
        INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-04-10', 3000000, i);
        INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-05-10', 3000000, i);
        INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-06-10', 3000000, i);
        INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-07-10', 3000000, i);
        INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-08-10', 3000000, i);
        INSERT INTO salary_log (received_date, total_salary, user_num) VALUES ('2024-09-10', 3000000, i);
        
        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE insert_work_log()
BEGIN
    DECLARE i INT;
    DECLARE j INT;

    SET j = 0;
    WHILE j < 5 DO
        SET j = j + 1;
        SET i = 1;
        
        WHILE i <= 21 DO
            INSERT INTO work_log (start_time, end_time, status, work_date, user_num) 
            VALUES ('08:50', '17:52', 'ATTENDANCE', DATE_ADD('2024-09-16', INTERVAL j DAY), i);
            SET i = i + 1;
        END WHILE;
    END WHILE;
END //

DELIMITER ;

CALL my_procedure();
CALL insert_work_log();

