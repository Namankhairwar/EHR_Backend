package com.clinic.patient.user.controller;

import com.clinic.patient.applicationCommonFeature.exception.GlobalExceptionHandler;
import com.clinic.patient.applicationCommonFeature.mapping.MAP;
import com.clinic.patient.security.jwt.JwtService;
import com.clinic.patient.doctor.service.DoctorService;
import com.clinic.patient.user.dto.UserRequestDTO;
import com.clinic.patient.user.dto.UserResponseDTO;
import com.clinic.patient.user.service.UserService;
import com.clinic.patient.user.state.BloodGroup;
import org.aspectj.lang.NoAspectBoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
   private UserController(UserService userService,JwtService jwtService,DoctorService doctorService){
       this.userService =userService;
   }


    /**
     * @param id uses to fetch the user
     * @return user object
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
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
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
        try{ return ResponseEntity.ok( userService.updateUser(id,dto));}
        catch(GlobalExceptionHandler e){
            return GlobalExceptionHandler.incorrectUpdate(new NoAspectBoundException("user incorrect updating profile",new Exception()));
        }
    }

    @PatchMapping("/{id}/BloodGroup")
    public ResponseEntity<?> updateUserBloodGroup(@PathVariable Long id, @RequestBody UserRequestDTO bloodGroup) {

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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try{ userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return  GlobalExceptionHandler.incorrectUpdate(new ClassNotFoundException());
        }
    }
}
