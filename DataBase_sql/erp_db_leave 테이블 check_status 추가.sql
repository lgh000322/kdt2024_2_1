use erp_db;
show tables;
ALTER TABLE leave_log
ADD check_status TINYINT DEFAULT 0;

select * from leave_log;