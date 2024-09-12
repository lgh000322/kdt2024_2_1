-- -----------------------------------------------------
-- Schema `erp_db`
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `erp_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `erp_db`;

-- -----------------------------------------------------
-- Drop Tables if Exists (Ensure clean state)
-- -----------------------------------------------------
DROP TABLE IF EXISTS board_answer, board, dept, leave_log, mail, mail_store, position, received_mail, salary_log, `user`, work_log;

-- -----------------------------------------------------
-- Table `position`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `position` (
  `position_num` BIGINT NOT NULL AUTO_INCREMENT,
  `position_name` VARCHAR(45) NOT NULL,
  `leave_day` INT NOT NULL,
  `basic_salary` INT NOT NULL,
  `leave_pay` INT NOT NULL,
  PRIMARY KEY (`position_num`),
  UNIQUE INDEX `position_num_UNIQUE` (`position_num`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `dept`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dept` (
  `dept_num` BIGINT NOT NULL AUTO_INCREMENT,
  `dept_name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`dept_num`),
  UNIQUE INDEX `dept_num_UNIQUE` (`dept_num`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
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
  UNIQUE INDEX `user_num_UNIQUE` (`user_num`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id`),
  UNIQUE INDEX `tel_UNIQUE` (`tel`),
  UNIQUE INDEX `email_UNIQUE` (`email`),
  INDEX `fk_user_position_idx` (`position_num`),
  INDEX `fk_user_dept_idx` (`dept_num`),
  CONSTRAINT `fk_user_position`
    FOREIGN KEY (`position_num`)
    REFERENCES `position` (`position_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_dept`
    FOREIGN KEY (`dept_num`)
    REFERENCES `dept` (`dept_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `leave_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `leave_log` (
  `leave_num` BIGINT NOT NULL AUTO_INCREMENT,
  `request_date` DATE NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `acceptance_status` TINYINT NULL DEFAULT 0,
  `check_status` TINYINT DEFAULT 0,
  `user_num` BIGINT NOT NULL,
  PRIMARY KEY (`leave_num`),
  UNIQUE INDEX `leave_num_UNIQUE` (`leave_num`),
  INDEX `fk_leave_log_user_idx` (`user_num`),
  CONSTRAINT `fk_leave_log_user`
    FOREIGN KEY (`user_num`)
    REFERENCES `user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mail_store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mail_store` (
  `mail_store_num` BIGINT NOT NULL AUTO_INCREMENT,
  `mail_type` ENUM('RECEIVED', 'SEND') NOT NULL,
  `user_num` BIGINT NOT NULL,
  PRIMARY KEY (`mail_store_num`),
  UNIQUE INDEX `mail_store_num_UNIQUE` (`mail_store_num`),
  INDEX `fk_mail_store_user_idx` (`user_num`),
  CONSTRAINT `fk_mail_store_user`
    FOREIGN KEY (`user_num`)
    REFERENCES `user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `work_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `work_log` (
  `log_num` BIGINT NOT NULL AUTO_INCREMENT,
  `start_time` TIME NULL,
  `end_time` TIME NULL,
  `status` ENUM('ABSENCE', 'TARDINESS', 'ATTENDANCE', 'LEAVE') NOT NULL,
  `work_date` DATE NULL,
  `user_num` BIGINT NOT NULL,
  PRIMARY KEY (`log_num`),
  UNIQUE INDEX `log_num_UNIQUE` (`log_num`),
  INDEX `fk_work_log_user_idx` (`user_num`),
  CONSTRAINT `fk_work_log_user`
    FOREIGN KEY (`user_num`)
    REFERENCES `user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Initial Setup
-- -----------------------------------------------------
SET @OLD_SQL_MODE = @@SQL_MODE;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS;
SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS;

SET SQL_MODE = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION';
SET FOREIGN_KEY_CHECKS = 0;
SET UNIQUE_CHECKS = 0;

-- -----------------------------------------------------
-- Insert Data into `position`
-- -----------------------------------------------------
INSERT INTO position (position_name, leave_day, basic_salary, leave_pay) VALUES 
('사원', 15, 3000000, 30000),
('대리', 16, 3500000, 35000),
('주임', 17, 4000000, 40000),
('팀장', 18, 4500000, 45000),
('부장', 19, 5000000, 50000),
('부사장', 20, 5500000, 60000),
('사장', 21, 6000000, 65000);

-- -----------------------------------------------------
-- Insert Data into `dept`
-- -----------------------------------------------------
INSERT INTO dept (dept_name) VALUES 
('임시부서'), 
('인사부'), 
('재무부'), 
('영업부'), 
('IT'), 
('법무부');

-- -----------------------------------------------------
-- Insert Data into `user` admin
-- -----------------------------------------------------
INSERT INTO `user` (user_id, password, name, tel, email, role, remained_leave, position_num, dept_num)
VALUES ('admin', 'admin', 'admin', '010-1111-1111', 'admin@knu.com', 'ADMIN', 21, 1, 1);

-- -----------------------------------------------------
-- Final Operations
-- -----------------------------------------------------
SET SQL_MODE = IFNULL(@OLD_SQL_MODE, 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION');
SET FOREIGN_KEY_CHECKS = IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1);
SET UNIQUE_CHECKS = IFNULL(@OLD_UNIQUE_CHECKS, 1);

-- Example Queries for Verification
SELECT * FROM work_log;
SELECT * FROM leave_log;
SELECT * FROM position;
SELECT * FROM dept;
