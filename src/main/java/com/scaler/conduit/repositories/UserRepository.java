package com.scaler.conduit.repositories;

import com.scaler.conduit.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserEntityByUsername(
            @Param("username") String username
    );

    UserEntity findUserEntityByEmail(
            @Param("email") String email
    );

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_followers (users_id, followers_id) VALUES(?2, ?1)", nativeQuery = true)
    void followUser(Long loggedInUserUserId, Long userToFollow);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_followers WHERE users_id = ?2 AND followers_id = ?1", nativeQuery = true)
    void unfollowUser(Long loggedInUserUserId, Long userToFollow);
}
