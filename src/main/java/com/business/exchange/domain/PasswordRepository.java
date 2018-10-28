package com.business.exchange.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {

    Password findByUserId(int userId);
}
