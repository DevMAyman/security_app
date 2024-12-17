package com.security_app.repository;


import com.security_app.entity.CustomUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<CustomUser, Long> {
    Optional<CustomUser> findByEmail(String email);
}
