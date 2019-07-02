package com.spring_boot.web_jpa_h2.hateoas;

import com.spring_boot.web_jpa_h2.controller.EmployeeController;
import com.spring_boot.web_jpa_h2.entity.Employee;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * This simple interface has one method: toResource(). It is based on converting a non-resource object (Employee) into a resource-based object (Resource<Employee>).
 * <p>
 * Spring HATEOAS’s abstract base class for all resources is ResourceSupport. But for simplicity, I recommend using Resource<T> as your mechanism to easily wrap all POJOs as resources.
 */
@Component
public class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {
    @Override
    public Resource<Employee> toResource(Employee employee) {
        return new Resource<Employee>(employee,
                linkTo(methodOn(EmployeeController.class).findById(employee.getId())).withRel("查询当前员工信息的链接"),
                linkTo(methodOn(EmployeeController.class).findAll()).withRel("查询所有员工信息的链接"));
    }
}
