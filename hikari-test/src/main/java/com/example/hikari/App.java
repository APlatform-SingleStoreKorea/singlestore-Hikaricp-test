package com.example.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    // 모든 테스트를 순차적으로 실행하는 main 메서드
    public static void main(String[] args) {
        // 기본 연결 테스트
        System.out.println("--- 기본 연결 테스트 시작 ---");
        testBasicConnection();

        // connectionTimeout 옵션 테스트
        System.out.println("\n--- connectionTimeout 옵션 테스트 시작 ---");
        testConnectionTimeout();

        // keepaliveTime 옵션 테스트
        System.out.println("\n--- keepaliveTime 옵션 테스트 시작 ---");
        testKeepaliveTime();
    }

    // 기본 연결 및 CRUD 테스트 메서드 
    private static void testBasicConnection() {
        HikariConfig config = new HikariConfig();
        // 사용자가 자신의 환경에 맞게 수정
        config.setJdbcUrl("jdbc:singlestore://{Host ip}:{Port}/{DB Name}");
        config.setUsername("{root}");
        config.setPassword("{PASSWD}");

        try (HikariDataSource ds = new HikariDataSource(config)) {
            System.out.println("HikariCP connection pool successfully initialized.");

            try (Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement()) {

                // 1. 기존 테이블이 존재하면 삭제 
                try {
                    stmt.execute("DROP TABLE users");
                    System.out.println("Table 'users' dropped if it existed.");
                } catch (SQLException e) {
                    System.out.println("Table 'users' did not exist, skipping drop.");
                }
                
                // 2. 테이블 생성
                stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(50))");
                System.out.println("Table 'users' created.");
                
                // 3. 데이터 삽입
                stmt.executeUpdate("INSERT INTO users (id, name) VALUES (1, 'Alice')");
                System.out.println("Record inserted: Alice (ID: 1)");
                
                // 4. 데이터 조회 (1차)
                System.out.println("--- First SELECT ---");
                try (ResultSet rs1 = stmt.executeQuery("SELECT * FROM users WHERE id = 1")) {
                    if (rs1.next()) {
                        System.out.println("Retrieved: ID: " + rs1.getInt("id") + ", Name: " + rs1.getString("name"));
                    }
                }
                
                // 5. 데이터 업데이트
                stmt.executeUpdate("UPDATE users SET name = 'Bob' WHERE id = 1");
                System.out.println("Record updated: Alice -> Bob (ID: 1)");

                // 6. 데이터 조회 (2차, 업데이트 확인)
                System.out.println("--- Second SELECT (after update) ---");
                try (ResultSet rs2 = stmt.executeQuery("SELECT * FROM users WHERE id = 1")) {
                    if (rs2.next()) {
                        System.out.println("Retrieved: ID: " + rs2.getInt("id") + ", Name: " + rs2.getString("name"));
                    }
                }

                // 7. 테이블 삭제
                stmt.execute("DROP TABLE users");
                System.out.println("Table 'users' dropped.");
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // connectionTimeout 옵션 테스트를 위한 메서드
    private static void testConnectionTimeout() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:singlestore://{Host ip}:{Port}/{DB Name}");
        config.setUsername("{root}");
        config.setPassword("{PASSWD}");
        
        config.setMaximumPoolSize(1);
        config.setConnectionTimeout(1000); 

        try (HikariDataSource ds = new HikariDataSource(config)) {
            Connection conn1 = ds.getConnection();
            System.out.println("첫 번째 커넥션 확보.");

            // 두 번째 커넥션을 요청하면 풀이 비어있으므로 타임아웃 발생
            Connection conn2 = ds.getConnection(); 
            
            // 예외가 발생하면 아래 코드는 실행되지 않음
            System.out.println("두 번째 커넥션 확보. (이 메시지는 보이면 안 됨)");
            conn1.close();
            conn2.close();

        } catch (SQLException e) {
            System.out.println("예상된 예외 발생: " + e.getMessage());
        }
    }

    // keepaliveTime 옵션 테스트를 위한 메서드
    private static void testKeepaliveTime() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:singlestore://{Host ip}:{Port}/{DB Name}");
        config.setUsername("{root}");
        config.setPassword("{PASSWD}");
        
        config.setKeepaliveTime(10000); // 10초
        config.setIdleTimeout(30000);  // 30초

        try (HikariDataSource ds = new HikariDataSource(config)) {
            Connection conn = ds.getConnection();
            System.out.println("커넥션 확보. keepaliveTime(10초)이 지나도록 대기...");

            // keepaliveTime보다 긴 15초 동안 대기
            Thread.sleep(15000);

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1")) {
                if (rs.next()) {
                    System.out.println("커넥션은 여전히 활성 상태이며 정상 작동합니다.");
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
