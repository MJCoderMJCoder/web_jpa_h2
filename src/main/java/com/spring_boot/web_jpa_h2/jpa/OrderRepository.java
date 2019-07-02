package com.spring_boot.web_jpa_h2.jpa;

import com.spring_boot.web_jpa_h2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
