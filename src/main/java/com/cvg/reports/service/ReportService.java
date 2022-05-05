package com.cvg.reports.service;

import com.cvg.reports.entity.Employee;
import com.cvg.reports.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private static final String FOLDER_CONTAINER = "YOUR_PATH";
    @Autowired
    private EmployeeRepository employeeRepository;

    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        List<Employee> employees = this.employeeRepository.findAll();
        File file = ResourceUtils.getFile("classpath:employees.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        Map<String, Object> map = new HashMap<>();
        map.put("createdBy", "CRISTHIAN VILLEGAS");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, FOLDER_CONTAINER + "\\employees.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, FOLDER_CONTAINER + "\\employees.pdf");
        }

        return "report generated in path: " + FOLDER_CONTAINER;
    }
}
