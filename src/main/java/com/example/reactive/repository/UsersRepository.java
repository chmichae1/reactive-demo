package com.example.reactive.repository;

import com.example.reactive.entity.Users;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsersRepository extends ReactiveCrudRepository<Users, String> {
    @Query(value = "SELECT * FROM USERS WHERE uuid = :uuid")
    Mono<Users> findUserByUuid(String uuid);
    @Query(value = "DELETE FROM USERS WHERE uuid = :uuid")
    Mono<Users> deleteUserByUuid(String uuid);
}
