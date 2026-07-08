//package com.clinic.patient.report.service;
//
//import com.clinic.patient.report.entity.Report;
//import com.clinic.patient.report.repositories.ReportRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.io.InputStream;
//import java.util.List;
//
//
///**
// * @ReportService has a features of
// *      add
// *      modify
// *      view
// *      download
// */
//@Service
//@Slf4j
//public class ReportService {
//    @Autowired
//   private ReportRepository reportRepository;
//    private ResponseEntity<?> view(long id){
//        Report reportById =  reportRepository.getReportById(id);
//        Resource inputStream = reportById.getReport();
//        Resource resource =inputStream;
//        String contentType = "form";
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION)
//                .body(resource);
//    }
//
//    private ResponseEntity<?> download() {
//
//    }
//
//    private ResponseEntity<?> add(){
//
//    }
//
//    private ResponseEntity<?> modify(){
//
//    }
//
//
//}
