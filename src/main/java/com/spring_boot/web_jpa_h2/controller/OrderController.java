package com.spring_boot.web_jpa_h2.controller;

import com.spring_boot.web_jpa_h2.entity.Order;
import com.spring_boot.web_jpa_h2.exception.OrderNotFoundException;
import com.spring_boot.web_jpa_h2.hateoas.OrderResourceAssembler;
import com.spring_boot.web_jpa_h2.jpa.OrderRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "order")
public class OrderController {
    private OrderRepository orderRepository;
    private OrderResourceAssembler orderResourceAssembler;

    public OrderController(OrderRepository orderRepository, OrderResourceAssembler orderResourceAssembler) {
        this.orderRepository = orderRepository;
        this.orderResourceAssembler = orderResourceAssembler;
    }

    @GetMapping("/findAll")
    public Resources<Resource<Order>> findAll() {
        List<Resource<Order>> orders = orderRepository.findAll().stream()
                .map(orderResourceAssembler::toResource)
                .collect(Collectors.toList());
        return new Resources<Resource<Order>>(orders,
                linkTo(methodOn(OrderController.class).findAll()).withRel("查询所有订单"));
    }

    @GetMapping("/findById/{id}")
    public Resource<Order> findById(@PathVariable Long id) {
        return orderResourceAssembler.toResource(orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id)));
    }

    @PutMapping("/saveCancelled/{id}")
    public ResponseEntity<ResourceSupport> saveCancelled(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (order != null && order.getStatus() == Order.Status.IN_PROGRESS) {
            order.setStatus(Order.Status.CANCELLED);
            return ResponseEntity.ok(orderResourceAssembler.toResource(orderRepository.save(order)));
        } else {
            return ResponseEntity
                    .status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
        }
    }

    @PutMapping("/saveCompleted/{id}")
    public ResponseEntity<ResourceSupport> saveCompleted(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            order.setStatus(Order.Status.COMPLETED);
            return ResponseEntity.ok(orderResourceAssembler.toResource(orderRepository.save(order)));
        }
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
    }

    @PutMapping("/saveInProgress")
    public ResponseEntity<Resource<Order>> saveInProgress(@RequestBody Order order) {
        order.setStatus(Order.Status.IN_PROGRESS);
        Order newOrder = orderRepository.save(order);
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).findById(newOrder.getId())).toUri())
                .body(orderResourceAssembler.toResource(newOrder));
    }
}
