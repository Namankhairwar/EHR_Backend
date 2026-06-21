package com.clinic.patient.doctor.entity;

import com.clinic.patient.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krishana dubey
 */
@Entity
@Table(name = "doctors")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    
    @OneToOne(targetEntity = User.class,fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "ehr_id")
    private User user;

    @Embeddable
    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorProfile{

        private String specialization;
        private String licenseNumber;

        @Convert(converter =MyConvertor.class)
        @Column(columnDefinition = "TEXT")
        private List<String> degrees;
    }

    @Embedded
    private DoctorProfile doctorProfile;

    private static class MyConvertor implements AttributeConverter<List<String>,String>{
        @Override
        public String convertToDatabaseColumn(List<String> o) {
           return o!=null?o.toString():null;
        }

        @Override
        public List<String> convertToEntityAttribute(String o) {
           String[] sep= o.split("[\\[\\],]");
           List<String> ret= new ArrayList<>();
            for (String s :sep) {
            if(!s.isEmpty()) ret.add(s);
            }
            return ret;
        }
    }

}
