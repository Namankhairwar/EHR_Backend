package com.clinic.patient.user.controller;

import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.user.dto.UserRequestDTO;
import com.clinic.patient.user.dto.UserResponseDTO;
import com.clinic.patient.user.service.UserService;
import org.aspectj.lang.NoAspectBoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Krishana dubey
 * @apiNote
 *  user registration ,
 *  GET,PUT,POST,DELETE by ID
 *  user login
 */


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * @param id uses to fetch the user
     * @return user object
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
     try {
        return ResponseEntity.ok(userService.getUserById(id, UserResponseDTO.class));
     }catch(GlobalExceptionHandler e){
         return GlobalExceptionHandler.notFound(new ClassNotFoundException("Incorrect ehrId | Try again after some time"));
     }
    }

    /**
     * @param id uses to update the user
     * @return updated user object
     */
    @PreAuthorize("hasRole('ADMIN') or @accessGuard.isSelf(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserRequestDTO dto) {
        try{ return ResponseEntity.ok( userService.updateUser(id,dto));}
        catch(GlobalExceptionHandler e){
            return GlobalExceptionHandler.incorrectUpdate(new NoAspectBoundException("user incorrect updating profile",new Exception()));
        }
    }

    @PreAuthorize("hasRole('ADMIN') or @accessGuard.isSelf(#id)")
    @PatchMapping("/{id}/BloodGroup")
    public ResponseEntity<?> updateUserBloodGroup(@PathVariable String id, @RequestBody UserRequestDTO bloodGroup) {

      try{ return ResponseEntity.ok(userService.updateUser(id, MAP.map(bloodGroup,UserRequestDTO::new)));}
      catch(GlobalExceptionHandler e){
        return  GlobalExceptionHandler.incorrectUpdate(new ClassNotFoundException());
      }
    }



    /**
     *
     * @param id  uses to delete the user
     * @return void matters with the status code
     */
    @PreAuthorize("hasRole('ADMIN') or @accessGuard.isSelf(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try{ userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return  GlobalExceptionHandler.incorrectUpdate(new ClassNotFoundException());
        }
    }

    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    @GetMapping("/search-patients")
    public ResponseEntity<?> searchPatients(@RequestParam("query") String query) {
        return ResponseEntity.ok(userService.searchPatients(query));
    }
}
