package com.spring_boot.web_jpa_h2.jpa;

import com.spring_boot.web_jpa_h2.entity.Employee;
import com.spring_boot.web_jpa_h2.entity.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot will run ALL CommandLineRunner beans once the application context is loaded.
 * <p>
 * This runner will request a copy of the EmployeeRepository you just created.
 * <p>
 * Using it, it will create two entities and store them.s
 */
@Configuration
public class LoadDatabase {
    @Bean
    CommandLineRunner initDataBase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        return args -> {
            employeeRepository.save(new Employee("Bilbo Baggins", "burglar"));
            employeeRepository.save(new Employee("Frodo Baggins", "thief"));
            orderRepository.save(new Order("还未真正生成订单，用户只是在看商品信息。", Order.Status.NOGENERATE));
            orderRepository.save(new Order("用户已下单，未支付，及正在处理中。", Order.Status.IN_PROGRESS));
            orderRepository.save(new Order("用户取消以下的订单", Order.Status.CANCELLED));
            orderRepository.save(new Order("用户已支付，完成订单交易。", Order.Status.COMPLETED));
        };
    }
}
