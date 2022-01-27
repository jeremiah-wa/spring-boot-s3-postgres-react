package com.example.springboots3postgresreact.datastore;

import com.example.springboots3postgresreact.profile.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class UserProfileDataStore implements UserProfileDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProfileDataStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertUserProfile(UserProfile userProfile) {
        return jdbcTemplate.update(
                "INSERT INTO schema.demobd (id, username, userprofileimagelink) VALUES (?, ?)",
                userProfile.getUserProfileId(), userProfile.getUsername(), userProfile.getUserProfileImageLink()
        );
    }

    @Override
    public List<UserProfile> getUserProfiles() {
        String sql = "SELECT * FROM userprofile";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String username = resultSet.getString("username");
            String userProfileImageLink = resultSet.getString("userprofileimagelink");
            return new UserProfile(id, username, userProfileImageLink);
        });
    }

    @Override
    public Optional<UserProfile> getUserProfileById(UUID userProfileId) {
        String sql = "SELECT * FROM userprofile WHERE id=?";

        UserProfile userProfile = jdbcTemplate.queryForObject(sql, new Object[]{userProfileId}, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String username = resultSet.getString("username");
            String userProfileImageLink = resultSet.getString("userprofileimagelink");
            return new UserProfile(id, username, userProfileImageLink);
        });

        return Optional.ofNullable(userProfile);
    }

    @Override
    public int updateUserProfileById(UUID userProfileId, UserProfile userProfile) {
        String sql = "UPDATE userprofile SET username=?, userprofileimagelink=? WHERE id=?";
        return jdbcTemplate.update(sql,
                userProfile.getUsername(),
                userProfile.getUserProfileImageLink().orElse(null),
                userProfileId);
    }

}
