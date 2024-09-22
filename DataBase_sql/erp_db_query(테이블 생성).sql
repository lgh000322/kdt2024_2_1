
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';






-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema erp_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema erp_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `erp_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `erp_db` ;

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
  `role` VARCHAR(25) NOT NULL,
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
  `mail_type` VARCHAR(45) NOT NULL,
  `user_num` BIGINT NOT NULL,
  PRIMARY KEY (`mail_store_num`),
  UNIQUE INDEX `mail_num_UNIQUE` (`mail_store_num` ASC),
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
  `status` VARCHAR(45) NOT NULL,
  `work_date` DATE NULL,
  `user_num` BIGINT NOT NULL,
  `leave_num` BIGINT NOT NULL,
  PRIMARY KEY (`log_num`),
  UNIQUE INDEX `log_num_UNIQUE` (`log_num` ASC),
  INDEX `fk_work_log_user1_idx` (`user_num` ASC),
  INDEX `fk_work_log_leave_log1_idx` (`leave_num` ASC),
  CONSTRAINT `fk_work_log_user1`
    FOREIGN KEY (`user_num`)
    REFERENCES `erp_db`.`user` (`user_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_work_log_leave_log1`
    FOREIGN KEY (`leave_num`)
    REFERENCES `erp_db`.`leave_log` (`leave_num`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `erp_db`.`mail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `erp_db`.`mail` (
  `mai_num` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `contents` VARCHAR(5000) NULL,
  `created_date` DATE NOT NULL,
  `mail_store_num` BIGINT NOT NULL,
  PRIMARY KEY (`mai_num`),
  UNIQUE INDEX `mai_num_UNIQUE` (`mai_num` ASC),
  INDEX `fk_mail_mail_store1_idx` (`mail_store_num` ASC),
  CONSTRAINT `fk_mail_mail_store1`
    FOREIGN KEY (`mail_store_num`)
    REFERENCES `erp_db`.`mail_store` (`mail_store_num`)
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


ALTER TABLE leave_log
ADD check_status TINYINT DEFAULT 0;