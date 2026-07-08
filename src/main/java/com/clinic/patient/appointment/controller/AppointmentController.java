package com.clinic.patient.appointment.controller;


import com.clinic.patient.appointment.dto.AppointmentRequestDTO;
import com.clinic.patient.appointment.dto.AppointmentResponseDTO;
import com.clinic.patient.appointment.service.AppointmentService;
import com.clinic.patient.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    // Create Appointment
    @PostMapping("")
    public ResponseEntity<?> createAppointment(
            @RequestBody AppointmentRequestDTO dto) {

        return appointmentService.createAppointment(dto);

    }

    // Get All Appointments
    @GetMapping("all/{user_id}")
    public ResponseEntity<?> getAllAppointments(@PathVariable(value = "user_id" )long user_id ,
                                                                           @RequestParam(value="size" , defaultValue = "5")int size,
                                                                           @RequestParam(value="page" , defaultValue = "0")int page,
                                                                           @RequestParam(value = "sortBy" , defaultValue = "appointmentTime") String sort) {


        if(userService.doesUserExist(user_id)) {
            return appointmentService.listAllAppointmentByPatient(user_id, size, page, sort);

        }
        return ResponseEntity.notFound().build();
    }

    // Update Appointment
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<?> updateAppointment(
            @RequestParam("id") long id,
            @RequestBody AppointmentRequestDTO dto) {

        return appointmentService.updateAppointment(id,dto);
    }

    // Cancel Appointment
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable Long id) {

        return
                appointmentService.cancelAppointment(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
       return appointmentService.deleteAppointment(id);
    }
}
