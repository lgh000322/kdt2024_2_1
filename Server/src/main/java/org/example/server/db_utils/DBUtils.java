package org.example.server.db_utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.server.domain.leave_log.LeaveLog;

import javax.sql.DataSource;

import static org.example.server.consts.DBConst.*;

public class DBUtils {

    private static DataSource dataSource = null;

    public static DataSource createOrGetDataSource() {
        if (dataSource == null) {
            HikariConfig config = getHikariConfig();
            dataSource = new HikariDataSource(config);
        }

        return dataSource;
    }

    private static HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setDriverClassName(CLASS);

        config.setMaximumPoolSize(10); // 커넥션 풀 사이즈
        config.setMinimumIdle(2); // 커넥션 풀의 커넥션 유휴상태수
        config.setConnectionTimeout(30000); // 커넥션 타임 아웃
        return config;
    }
}
