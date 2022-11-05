package com.example.reactive.repository;

import com.example.reactive.entity.UsersSetting;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UsersSettingRepository extends ReactiveCrudRepository<UsersSetting, String> {
}
