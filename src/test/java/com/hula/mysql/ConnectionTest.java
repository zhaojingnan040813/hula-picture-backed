package com.hula.mysql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
// 指定使用测试配置文件
//@TestPropertySource(locations = "classpath:application-test.properties")
@TestPropertySource(locations = "classpath:application.yml")
public class ConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 测试MySQL连接
    @Test
    public void testMySQLConnection() {
        String result = jdbcTemplate.queryForObject(
            "SELECT 'MySQL Connection Test'",
            String.class
        );
        assertThat(result).isEqualTo("MySQL Connection Test");
    }

    // 测试Redis连接
    @Test
    public void testRedisConnection() {
        String key = "redis_connection_test";
        String value = "Redis Connection Success";

        // 设置测试值
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        redisTemplate.delete(key); // 确保测试环境干净
        ops.set(key, value);

        // 获取并验证值
        String result = ops.get(key);
        assertThat(result).isEqualTo(value);
    }
}