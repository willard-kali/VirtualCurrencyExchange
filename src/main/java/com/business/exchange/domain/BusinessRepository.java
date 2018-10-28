package com.business.exchange.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessRepository extends JpaRepository<Business, Long> {

//    @Query("select new com.business.exchange.domain.BusinessRecord(u_a.user_name,u_a.employeeid,u.user_name,u.employeeid,b.exchange_currency_number,b.exchange_date) from (select * from user) as u_a right join business b on u_a.user_id = b.src_user_id left join user u on u.user_id = b.dest_user_id")
//    List<BusinessRecord> findExchangeHistoryByJoin();

    List<Business> findAllBySrcUserIdEqualsOrDestUserIdEqualsOrderByExchangeDateDesc(int srcUserId, int destUserId);
}
