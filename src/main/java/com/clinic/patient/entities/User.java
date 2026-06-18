package com.clinic.patient.entities;

import com.clinic.patient.role.BloodGroup;
import com.clinic.patient.role.Gender;
import com.clinic.patient.role.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class User {
    @Embeddable
    @NoArgsConstructor
    @ToString
    @Getter
    @Setter

    public static class Address{

        @JsonCreator
        public Address(@JsonProperty String addressLine,
                       @JsonProperty  Long pin_code,
                       @JsonProperty String city,
                       @JsonProperty String state) {
            this.addressLine = addressLine;
            this.pin_code = pin_code;
            this.city = city;
            this.state = state;
        }

        @Column(name = "address_line")
        String addressLine;

        @Column(name= "state",
        nullable = false)
        String state;

        @Column(name= "city",
        nullable = false)
        String city;

        @Column(name = "postal_code",
                length = 6,
                nullable = false
        )
        Long pin_code;
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public  static class Emergency{

        @JsonCreator
        public Emergency(@JsonProperty  String contactName,
                         @JsonProperty  Long contactPhoneNo,
                         @JsonProperty  String relationship
                                                            ) {
            this.contactName = contactName;
            this.contactPhoneNo = contactPhoneNo;
            this.relationship = relationship;
        }

        @Column(name = "contactName")
        String contactName;

        @Column(name= "relationship")
        String relationship;

        @Column(name= "contactPhoneNo",
                nullable = false,length =10)
        Long contactPhoneNo;


    }


    @Transient
    private static final DateTimeFormatter obj=DateTimeFormatter.ofPattern("mm-DD-yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name="dob",columnDefinition = "Date")
    private LocalDate dob;


    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Column(nullable = false,
    unique = true,
    length = 10
    )
    private Long phoneNo;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    @Embedded
    private Address address;
    @Embedded
    private Emergency emergency;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Transient
    private static PasswordEncoder passwordEncoder;


    @PrePersist
    @PreUpdate
    private void encode(){
      if(!password.isEmpty())  password= passwordEncoder.encode(this.password);
      log.info("Password : {} and encoded pass {}",password,passwordEncoder);

    }

    public static void setPasswordEncoder(PasswordEncoder passwordEncode){
       User.passwordEncoder = passwordEncode;
    }

}
