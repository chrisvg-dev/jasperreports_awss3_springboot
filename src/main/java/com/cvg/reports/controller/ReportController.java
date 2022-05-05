package com.cvg.reports.controller;

import com.cvg.reports.entity.Employee;
import com.cvg.reports.repository.EmployeeRepository;
import com.cvg.reports.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    public List<Employee> getEmployees() {
        return this.employeeRepository.findAll();
    }

    @GetMapping("/report/{type}")
    public String generateReport(@PathVariable String type) {
        try {
            return this.reportService.exportReport(type);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

}
