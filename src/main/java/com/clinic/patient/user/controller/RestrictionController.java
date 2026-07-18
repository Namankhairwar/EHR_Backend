package com.clinic.patient.user.controller;

import com.clinic.patient.user.dto.RestrictionRequestDto;
import com.clinic.patient.user.dto.RestrictionGrantResponseDto;
import com.clinic.patient.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restrictions")
@RequiredArgsConstructor
public class RestrictionController {

    private final UserService userService;

    @PostMapping("/request")
    public ResponseEntity<Void> requestRestriction(@RequestBody RestrictionRequestDto dto) {
        userService.requestRestrictionGrant(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/approve/{requestId}")
    public ResponseEntity<Void> approveRestriction(
            @PathVariable("requestId") Long requestId,
            @RequestBody(required = false) List<String> attributes) {
        userService.approveRestrictionGrant(requestId, attributes);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reject/{requestId}")
    public ResponseEntity<Void> rejectRestriction(@PathVariable("requestId") Long requestId) {
        userService.rejectRestrictionGrant(requestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RestrictionGrantResponseDto>> getRestrictionsForPatient(@PathVariable("patientId") String patientId) {
        return ResponseEntity.ok(userService.getRestrictionGrantsForPatient(patientId));
    }
}
