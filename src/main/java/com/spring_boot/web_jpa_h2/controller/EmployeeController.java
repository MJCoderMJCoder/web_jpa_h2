package com.spring_boot.web_jpa_h2.controller;

import com.spring_boot.web_jpa_h2.entity.Employee;
import com.spring_boot.web_jpa_h2.exception.EmployeeNotFoundException;
import com.spring_boot.web_jpa_h2.hateoas.EmployeeResourceAssembler;
import com.spring_boot.web_jpa_h2.jpa.EmployeeRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * HTTP 请求方法
 * <p>
 * GET	请求指定的页面信息，并返回实体主体。
 * POST	向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST 请求可能会导致新的资源的建立和/或已有资源的修改。
 * PUT	从客户端向服务器传送的数据取代指定的文档的内容。
 * DELETE	请求服务器删除指定的页面。
 * PATCH	是对 PUT 方法的补充，用来对已知资源进行局部更新 。
 * HEAD	类似于 GET 请求，只不过返回的响应中没有具体的内容，用于获取报头
 * CONNECT	HTTP/1.1 协议中预留给能够将连接改为管道方式的代理服务器。
 * OPTIONS	允许客户端查看服务器的性能。
 * TRACE	回显服务器收到的请求，主要用于测试或诊断。
 */
@RestController
@RequestMapping(value = "employee")
public class EmployeeController {
    private EmployeeRepository employeeRepository;
    private EmployeeResourceAssembler employeeResourceAssembler;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeResourceAssembler employeeResourceAssembler) {
        this.employeeRepository = employeeRepository;
        this.employeeResourceAssembler = employeeResourceAssembler;
    }

    // ====================================== Aggregate root ======================================
    @GetMapping(value = "findAll")
    public Resources<Resource<Employee>> findAll() {
        List<Resource<Employee>> employees = employeeRepository.findAll().stream()
                .map(employeeResourceAssembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(employees,
                linkTo(methodOn(EmployeeController.class).findAll()).withSelfRel());
    }

    @PostMapping(value = "save")
    public Employee save(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // ====================================== Single item ======================================
    @GetMapping(value = "findById/{id}")
    public Resource<Employee> findById(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        return employeeResourceAssembler.toResource(employee);
    }

    @PutMapping(value = "save/{id}")
    public Employee save(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return employeeRepository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return employeeRepository.save(newEmployee);
                });
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }
}
