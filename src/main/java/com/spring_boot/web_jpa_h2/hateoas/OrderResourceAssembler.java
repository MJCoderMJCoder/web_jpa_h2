package com.spring_boot.web_jpa_h2.hateoas;

import com.spring_boot.web_jpa_h2.controller.OrderController;
import com.spring_boot.web_jpa_h2.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class OrderResourceAssembler implements ResourceAssembler<Order, Resource<Order>> {
    @Override
    public Resource<Order> toResource(Order order) {
        // Unconditional links to single-item resource and aggregate root
        Resource<Order> orderResource = new Resource<Order>(order,
                linkTo(methodOn(OrderController.class).findById(order.getId())).withRel("查询当前订单"),
                linkTo(methodOn(OrderController.class).findAll()).withRel("查询所有订单"));
        // Conditional links based on state of the order

        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            orderResource.add(linkTo(methodOn(OrderController.class).saveCancelled(order.getId())).withRel("取消"));
            orderResource.add(linkTo(methodOn(OrderController.class).saveCompleted(order.getId())).withRel("确认"));
        } else {
            orderResource.add(linkTo(methodOn(OrderController.class).saveInProgress(order)).withRel("下单"));
        }
        return orderResource;
    }
}
