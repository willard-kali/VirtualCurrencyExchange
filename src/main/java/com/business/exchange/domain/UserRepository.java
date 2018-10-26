package com.business.exchange.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmployeeID(String employeeID);

    User findByUserId(int userId);

}
