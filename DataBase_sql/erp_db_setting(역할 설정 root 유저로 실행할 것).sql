show databases;
use erp_db;
create user erp_user@localhost identified by 'erp_user';
grant all privileges on erp_db.* to erp_user@localhost;
alter user 'erp_user'@'localhost' identified with mysql_native_password by '123456';
SELECT user, host FROM mysql.user WHERE user = 'erp_user' AND host = 'localhost';


