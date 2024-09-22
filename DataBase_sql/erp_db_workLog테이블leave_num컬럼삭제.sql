-- 외래 키 제약 조건 삭제
ALTER TABLE work_log DROP FOREIGN KEY fk_work_log_leave_log1;
-- 외래 키 제약 조건 이름 확인
SHOW CREATE TABLE work_log;
-- leave_num 열 삭제
ALTER TABLE work_log DROP COLUMN leave_num;

select * from work_log;