package com.cvg.reports.service;

import com.cvg.reports.entity.Employee;
import com.cvg.reports.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private static final String ALLOWED_FORMATS[] = {"PDF", "HTML", "XML"};
    @Value("${jasper.path}")
    private String folder;
    @Autowired
    private EmployeeRepository employeeRepository;

        public File exportReport(String reportFormat) throws FileNotFoundException, JRException {
        File newReport = null;
        List<Employee> employees = this.employeeRepository.findAll();
        File file = ResourceUtils.getFile("classpath:employees.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        Map<String, Object> map = new HashMap<>();
        map.put("createdBy", "CRISTHIAN VILLEGAS");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
        String fullPathFile = this.folder + "\\employees" + reportFormat.toLowerCase();

        if ( Arrays.asList( ALLOWED_FORMATS ).contains(reportFormat) ) {
            switch (reportFormat){
                case "PDF":
                    JasperExportManager.exportReportToPdfFile(jasperPrint, fullPathFile);
                    break;
                case "HTML":
                    JasperExportManager.exportReportToHtmlFile(jasperPrint, fullPathFile);
                    break;
            }
            newReport = new File(fullPathFile);
            System.out.println("report generated in path: " + this.folder);
        } else {
            newReport = null;
            System.out.println("El formato seleccionado no es aceptado...");
        }
        return newReport;
    }
}
