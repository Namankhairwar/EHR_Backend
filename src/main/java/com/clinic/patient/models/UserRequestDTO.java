package com.clinic.patient.models;

import com.clinic.patient.entities.User;
import com.clinic.patient.role.*;
import lombok.*;
@Getter
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserRequestDTO {

    private User.Address address;
    private String firstName;
    private String lastName;
    private String email;
    private String dob;
    private String password;
    private Role role;
    private Long phoneNo;
    private BloodGroup bloodGroup;
    private Gender gender;
    private User.Emergency emergencyContact;

}
