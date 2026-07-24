package com.clinic.patient.user.dto;

import com.clinic.patient.applicationCommonFeature.state.Role;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.user.entity.User;
import com.clinic.patient.user.state.BloodGroup;
import com.clinic.patient.user.state.Gender;
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
    private Doctor.DoctorProfile doctorProfile;
    private String maritalStatus;

}
