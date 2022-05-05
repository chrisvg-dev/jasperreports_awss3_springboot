package com.cvg.reports.controller;

import com.cvg.reports.entity.Employee;
import com.cvg.reports.repository.EmployeeRepository;
import com.cvg.reports.service.AWSS3UploadService;
import com.cvg.reports.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/employees")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AWSS3UploadService s3Service;

    @GetMapping("/report/{type}")
    public String generateReport(@PathVariable String type) {
        if (type == null) {
            return "Debe seleccionar ";
        }
        String format = type.toUpperCase();
        try {
            File report = this.reportService.exportReport(format);

            boolean wasCreated = (report != null) ? true : false;
            boolean wasUploaded = false;
            if (wasCreated) {
                wasUploaded = this.s3Service.putObject("report.pdf", report);
            }
            return (wasCreated && wasUploaded)
                    ? "Reporte creado exitosamente..."
                    : "Hubo un problema al crear el reporte...";
        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }
    }
}
