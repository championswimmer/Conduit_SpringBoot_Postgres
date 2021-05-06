package com.scaler.conduit.repositories;

import com.scaler.conduit.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserEntityByUsername(
            @Param("username") String username
    );

    UserEntity findUserEntityByEmail(
            @Param("email") String email
    );
}
