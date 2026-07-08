package com.clinic.patient;

import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.doctor.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Slf4j
class PatientApplicationTests {

    @Autowired
    private DoctorService doc;
	@Test
    public void test(){

        Optional<Doctor> doctorById = doc.getDoctorById((long) 153);
        log.info(doctorById.get().toString());
    }

}
