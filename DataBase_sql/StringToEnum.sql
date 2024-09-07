alter table user modify column role ENUM('ADMIN','USER');
alter table mail_store modify column mail_type ENUM('RECEIVED','SEND');
alter table work_log modify column status ENUM('ABSENCE','TARDINESS','ATTENDANCE','LEAVE');