package org.example.expert.domain.user;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class UserDummyInsertTest {

    private static final Logger log = LoggerFactory.getLogger(UserDummyInsertTest.class);
    @Autowired JdbcTemplate jdbcTemplate;
    @Autowired PasswordEncoder passwordEncoder;

    private static final String INSERT_SQL = "INSERT INTO users (email, password, nickname, user_role) VALUES (?, ?, ?, ?)";
    private static final int DATA_SIZE = 1000000;
    private static final int BATCH_SIZE = 2000;

    @Test
    @Commit
    void createUsers() {

        List<User> userList = new ArrayList<>();

        for (int i = 1; i < DATA_SIZE + 1; i++) {
            log.info("Create user {}", i);
            String email = "test" + i + "@test.com";
            String password = passwordEncoder.encode("test" + i);
            String nickname = "test" + i;

            userList.add(new User(email, password, nickname, UserRole.ROLE_USER));

            if (i % BATCH_SIZE == 0) {
                jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        User user = userList.get(i);
                        ps.setString(1, user.getEmail());
                        ps.setString(2, user.getPassword());
                        ps.setString(3, user.getNickname());
                        ps.setString(4, user.getUserRole().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return userList.size();
                    }
                });
                userList.clear();
            }
        }
    }

}